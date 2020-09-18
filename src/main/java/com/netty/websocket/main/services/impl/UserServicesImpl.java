package com.netty.websocket.main.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netty.websocket.common.dto.CommonReturn;
import com.netty.websocket.main.dao.BirdUsersMapper;
import com.netty.websocket.main.dao.MyFriendsMapper;
import com.netty.websocket.main.entity.BirdUsers;
import com.netty.websocket.main.entity.FriendsRequest;
import com.netty.websocket.main.entity.MyFriends;
import com.netty.websocket.main.services.FriendsRequestServices;
import com.netty.websocket.main.services.MyFriendServices;
import com.netty.websocket.main.services.UserServices;
import com.netty.websocket.tools.codeworker.QRCodeUtils;
import com.netty.websocket.tools.codeworker.UuidUtil;
import com.netty.websocket.tools.my.MyUtils;
import com.netty.websocket.tools.password.PasswordUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServicesImpl extends ServiceImpl<BirdUsersMapper,BirdUsers> implements UserServices {

    //注入mapper
    @Autowired
    UserServices userServices;

    @Autowired
    MyFriendServices myFriendServices;

    @Autowired
    FriendsRequestServices friendsRequestServices;

    @Value("${update.root.path}")
    private String fileRootPath;

    @Override
    public CommonReturn loginOrRegister(BirdUsers user) {
        CommonReturn result = new CommonReturn();
        //判断输入的账号与密码是否为空
        if (!MyUtils.StringIsNull(user.getUsername()) || !MyUtils.StringIsNull(user.getPassword())){
            result.setAll(10001,null,"参数有误");
            return result;
        }
        QueryWrapper<BirdUsers> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("username",user.getUsername());
        BirdUsers birdUser = this.getOne(usersQueryWrapper);
        //判断是新增还是登录
        if (birdUser == null) {
            //注册
            BirdUsers users = new BirdUsers();
            users.setUsername(user.getUsername());
            users.setNickname(user.getUsername());
            users.setPassword(PasswordUtils.encode(user.getPassword()));
            users.setCid(user.getCid());
            users.setId(UuidUtil.generateStr(64));
            //为每个用户生成一个唯一的二维码
            //指定二维码的保存路径
            String urlPath = new StringBuilder("qrcode/").append(users.getId()).append("Qrcode.png").toString();
            String qrcodePath = new StringBuffer(fileRootPath).append(urlPath).toString();
            //设置二维码
            QRCodeUtils.createQRCode(qrcodePath,"username:"+users.getUsername());
            //创建一个并保存二维码
            MultipartFile qrcodeFile = QRCodeUtils.fileToMultipart(qrcodePath);
            //将用户的二维码地址写入用户信息里
            users.setQrcode(urlPath);
            this.save(users);
            //将用户密码(敏感信息制空)
            users.setPassword(null);
            result.setAll(20000,users,"注册成功");
        } else {
            //登录，判断密码是否正确z
            if (!PasswordUtils.matches(user.getPassword(), birdUser.getPassword())) {
                result.setAll(10000,null,"账号或密码错误");
                return result;
            } else {
                birdUser.setPassword(null);
                result.setAll(20000,birdUser,"登录成功");
                return result;
            }
        }
        return result;
    }

    @Override
    public CommonReturn setNickName(BirdUsers user,String[] strings) {
        CommonReturn result = new CommonReturn();
        if (user!=null && MyUtils.StringIsNull(user.getId()) && this.getById(user.getId())!=null){
            BirdUsers data = new BirdUsers();
            data.setId(user.getId());
            data.setNickname(user.getNickname());
            QueryWrapper<BirdUsers> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",data.getId());
            queryWrapper.select(strings);
            this.update(data,queryWrapper);
            data = this.getById(user.getId());
            result.setAll(20000,data,"操作成功");
        }else{
            result.setAll(10001,null,"参数错误");
        }
        return result;
    }

    /**
     * 公共方法直接拖走
     * @param files 文件
     * @param originalFilename 你希望这个文件取什么名字
     * @param white 白名单
     * @param address 存放地址
     * @return
     */
    @Override
    public CommonReturn upLoad(MultipartFile[] files, String originalFilename, String[] white,String address) {
        CommonReturn result = new CommonReturn();
        //记录多张图片的上传地址
        List<String> addresss = new ArrayList<>();
        //判断有没有传入white(白名单)
        List<String> list = null;
        if (white==null || white.length==0){
            list = null;
        }else{
            list = Arrays.asList(white);
        }
        //判断是否有文件名
        if(!MyUtils.StringIsNull(originalFilename)){
            //设置文件名设置默认文件名
            originalFilename = new StringBuffer(UuidUtil.generateMixStr(16)).append(System.currentTimeMillis()).toString();
        }
        String lastName = "";
        //多文件上传
        for(MultipartFile file : files){
            //获取文件类型  "image/jpeg"  "image/png"
//            String type = file.getContentType();
            //拿取文件的后缀名
            lastName = MyUtils.getlastName(file.getOriginalFilename(),".");
            //lastName==null
            if (!MyUtils.StringIsNull(lastName)){
                result.setAll(10001,null,"文件名异常");
                return result;
            }
            if (list==null || list.size()==0 || list.contains(lastName)){
                //存储路径  fileRootPath 服务器前缀  address 具体文件夹名  originalFilename文件名   lastName文件后缀
                String filePath = new StringBuilder(fileRootPath).append(address).append(originalFilename).append(".").append(lastName).toString();
                //保存文件
                try {
                    file.transferTo(new File(filePath));
                }catch (IOException e){
                    e.printStackTrace();
                    result.setAll(40000,null,"上传失败");
                    return result;
                }
            }else{
                result.setAll(10001,null,"请按照指定的格式上传文件");
                this.deleteFile(addresss);
                return result;
            }
            addresss.add(new StringBuilder("").append(address).append(originalFilename).append(".").append(lastName).toString());
        }
        result.setAll(20000,addresss,"操作成功");
        return result;
    }

    /**
     * 公共方法直接拖走
     * @param address
     * @return
     */
    @Override
    public CommonReturn deleteFile(List<String> address) {
        CommonReturn result = new CommonReturn();
        String img_path = null;
        if (address==null || address.size()==0){
            return null;
        }
        for (int i = 0 ; i < address.size() ; i++){
            if (MyUtils.StringIsNull(address.get(i))){
                img_path = fileRootPath + address.get(i);
                //找到文件，初始化文件
                File file = new File(img_path);
                //文件是否存在
                if (file.exists()) {
                    //存在就删了
                    if (file.delete()) {
                        address.remove(i);
                    }
                }else{
                    address.remove(i);
                }
            }
        }
        result.setAll(20000,address,"还有以下文件没有删除");
        return result;
    }

    @Override
    @Transactional
    public CommonReturn setFaceImage(MultipartFile file, String userId) {
        CommonReturn result = new CommonReturn();
        //限制用户传入的文件格式
        String[] str = {"jpg","png"};
        //限制用户查询语句的输出格式
        String[] userStr = {"id","username","face_image","face_image_big","nickname","qrcode"};
        //查询用户数据库里有没有这样的用户
        QueryWrapper<BirdUsers> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("id",userId).select(userStr);
        BirdUsers user = this.getOne(usersQueryWrapper);
        MultipartFile[] files = {file};
        List<String> list = (List<String>) this.upLoad(files,new StringBuffer(userId).append(System.currentTimeMillis()).toString(),str,"images/").getData();
        //记录用户新头像地址
        String facePath = null;
        if (list!=null && list.size()>0){
            facePath = list.get(0);
        }else{
            result.setAll(10001,null,"文件上传失败");
        }

        if (MyUtils.StringIsNull(facePath) && user!=null){
            //获取用户之前的头像地址
            List<String> oldfacepath = new ArrayList<>();
            oldfacepath.add(user.getFaceImage());
            //给用户换头像
            user.setFaceImage(facePath);
            QueryWrapper<BirdUsers> usersQueryWrapper2 = new QueryWrapper<>();
            usersQueryWrapper2.eq("id",userId);
            this.update(user,usersQueryWrapper2);
            //删除用户之前的头像
            this.deleteFile(oldfacepath);
            result.setAll(20000,user,"操作成功");
        }else{
            result.setAll(10001,null,"找不到用户或者头像");
        }
        return result;
    }

    @Override
    public CommonReturn byNameFindFirend(String userId, String username) {
        CommonReturn result = new CommonReturn();
        if (!MyUtils.StringIsNull(userId) || !MyUtils.StringIsNull(username)){
            result.setAll(10001,null,"参数错误");
            return result;
        }
        String[] userStr = {"id","username","face_image","face_image_big","nickname","qrcode"};
        QueryWrapper<BirdUsers> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("username",username).select(userStr);
        BirdUsers user = this.getOne(usersQueryWrapper);
        //判断用户是否存在
        if (user==null){
            result.setAll(10001,null,"查无此用户，请检查用户名");
        }
        //判断查找到的用户是不是你自己
        else if(userId.equals(user.getId())){
            result.setAll(10001,null,"不能添加自己本身");
        }
        //判断此人是否已经是你的好友了
        else{
            //查找我的好友列表
            QueryWrapper<MyFriends> myFriendsQueryWrapper = new QueryWrapper<>();
            myFriendsQueryWrapper.eq("my_user_id",userId).eq("my_friend_user_id",user.getId());
            MyFriends myFriends = myFriendServices.getOne(myFriendsQueryWrapper);
            if(myFriends==null){
                //返回用户的数据
                result.setAll(20000,user,"操作成功");
            }else{
                result.setAll(10001,null,"该用户已经是您的好友了");
            }
        }
        return result;
    }


    @Override
    public CommonReturn bySendUserId(String userId) {
        CommonReturn result = new CommonReturn();
        //首先判断用户输入的userId是否合法
        if(MyUtils.StringIsNull(userId) && this.getById(userId)!=null){
            //查找该用户收到的所有添加好友请求
            QueryWrapper<FriendsRequest> requestQueryWrapper = new QueryWrapper<>();
            requestQueryWrapper.eq("accept_user_id",userId);
            List<FriendsRequest> friends = friendsRequestServices.list(requestQueryWrapper);
            //判断friends有没有数据
            if (friends!=null && friends.size()>0){
                //获取发起人的用户数据
                QueryWrapper<BirdUsers> queryWrapper = new QueryWrapper<>();
                for (int i = 0 ; i < friends.size() ; i++){
                    queryWrapper.eq("id",friends.get(i).getSendUserId());
                    if (i==friends.size()-1){
                        queryWrapper.select("id","username","nickname","face_image");
                    }else {
                        queryWrapper.or();
                    }
                }
                List<BirdUsers> users = this.list(queryWrapper);
                //最后判断一下是否准确
                if (users.size()==friends.size()){
                    result.setAll(20000,users,"操作成功");
                }else{
                    result.setAll(10001,null,"未知错误");
                }
            }else{
                result.setAll(20001,null,"暂时没有收到好友请求");
            }
        }else{
            result.setAll(10001,null,"参数不正确");
        }
        return result;
    }

    @Override
    @Transactional
    public CommonReturn addFriend(String userId, String friendId) {
        CommonReturn result = new CommonReturn();
        //判断参数是否合法
        if (MyUtils.StringIsNull(userId) && MyUtils.StringIsNull(friendId)){
            //删除请求表里的数据
            QueryWrapper<FriendsRequest> friendsRequestQueryWrapper = new QueryWrapper<>();
            friendsRequestQueryWrapper.eq("accept_user_id",userId).eq("send_user_id",friendId);
            Boolean flag = friendsRequestServices.remove(friendsRequestQueryWrapper);
            //查找一下朋友表里有没有这两条数据
            QueryWrapper<MyFriends> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("my_user_id",userId).eq("my_friend_user_id",friendId);
            MyFriends Friends1 = myFriendServices.getOne(queryWrapper1);
            QueryWrapper<MyFriends> queryWrapper2 = new QueryWrapper<>();
            queryWrapper1.eq("my_user_id",friendId).eq("my_friend_user_id",userId);
            MyFriends Friends2 = myFriendServices.getOne(queryWrapper1);
            //判断有没有删除成功
            if(flag==true && Friends1==null && Friends2==null){
                //添加数据
                MyFriends myFriends1 = new MyFriends();
                myFriends1.setId(UuidUtil.generateStr(64));
                myFriends1.setMyUserId(userId);
                myFriends1.setMyFriendUserId(friendId);
                MyFriends myFriends2 = new MyFriends();
                myFriends2.setId(UuidUtil.generateStr(64));
                myFriends2.setMyUserId(friendId);
                myFriends2.setMyFriendUserId(userId);
                myFriendServices.save(myFriends1);
                myFriendServices.save(myFriends2);
                //这名用户的所有好友
                //找出这名用户的所有好友id
                QueryWrapper<MyFriends> myFriendsQueryWrapper = new QueryWrapper<>();
                myFriendsQueryWrapper.eq("my_user_id",userId);
                List<MyFriends> myFriendsList = myFriendServices.list(myFriendsQueryWrapper);
                List<BirdUsers> myFriends = new ArrayList<>();
                if (myFriendsList!=null && myFriendsList.size()>0){
                    QueryWrapper<BirdUsers> queryWrapper = new QueryWrapper<>();
                    for (int i = 0 ; i < myFriendsList.size() ; i++){
                        queryWrapper.eq("id",myFriendsList.get(i).getMyFriendUserId());
                        if(i==myFriendsList.size()-1){
                            queryWrapper.select("id","username","face_image","face_image_big","nickname","qrcode");
                        }else{
                            queryWrapper.or();
                        }
                        myFriends = this.list(queryWrapper);
                    }
                }
                result.setAll(20000,myFriends,"操作成功");
            }else{
                result.setAll(10001,null,"删除失败");
            }
        }else{
            result.setAll(10001,null,"参数不正确");
        }
        return result;
    }

    @Override
    public CommonReturn ignore(String userId, String friendId) {
        CommonReturn result = new CommonReturn();
        if (MyUtils.StringIsNull(userId) && MyUtils.StringIsNull(friendId)){
            //删除请求表里的数据
            QueryWrapper<FriendsRequest> friendsRequestQueryWrapper = new QueryWrapper<>();
            friendsRequestQueryWrapper.eq("accept_user_id",userId).eq("send_user_id",friendId);
            Boolean flag = friendsRequestServices.remove(friendsRequestQueryWrapper);
            if (flag==true){

                //这名用户的所有好友
                //找出这名用户的所有好友id
                QueryWrapper<MyFriends> myFriendsQueryWrapper = new QueryWrapper<>();
                myFriendsQueryWrapper.eq("my_user_id",userId);
                List<MyFriends> myFriendsList = myFriendServices.list(myFriendsQueryWrapper);
                List<BirdUsers> myFriends = new ArrayList<>();
                if (myFriendsList!=null && myFriendsList.size()>0){
                    QueryWrapper<BirdUsers> queryWrapper = new QueryWrapper<>();
                    for (int i = 0 ; i < myFriendsList.size() ; i++){
                        queryWrapper.eq("id",myFriendsList.get(i).getMyFriendUserId());
                        if(i==myFriendsList.size()-1){
                            queryWrapper.select("id","username","face_image","face_image_big","nickname","qrcode");
                        }else{
                            queryWrapper.or();
                        }
                        myFriends = this.list(queryWrapper);
                    }
                }

                result.setAll(20000,myFriends,"操作成功");
            }else{
                result.setAll(10001,null,"删除失败");
            }
        }else{
            result.setAll(10001,null,"参数不正确");
        }
        return result;
    }

    @Override
    public CommonReturn getMyfriend(String userId) {
        CommonReturn result = new CommonReturn();
        //判断参数是否合法
        if (MyUtils.StringIsNull(userId)){
            //首先查询到该名用户的所有朋友id
            QueryWrapper<MyFriends> friendsQueryWrapper = new QueryWrapper<>();
            friendsQueryWrapper.eq("my_user_id",userId).select("my_friend_user_id");
            List<MyFriends> friends = myFriendServices.list(friendsQueryWrapper);
            List<BirdUsers> users = new ArrayList<>();
            if(friends!=null && friends.size()>0){
                List<String> friendids =  new ArrayList<>();
                friends.stream().forEach(i->friendids.add(i.getMyFriendUserId()));
                //格式化返回给前端的内容
                BirdUsers user = new BirdUsers();
                user.setId("111");
                user.setUsername("111");
                user.setFaceImage("111");
                user.setNickname("111");
                users = this.listByIds(friendids);
                users = (List<BirdUsers>) MyUtils.AllSet(users,user);
                result.setAll(20000,users,"操作成功");
            }else{
                result.setAll(20001,null,"您目前没有好友,赶快去添加吧");
            }
        }else{
            result.setAll(10001,null,"参数不正确");
        }
        return result;
    }
}
