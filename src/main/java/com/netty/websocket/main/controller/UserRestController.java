package com.netty.websocket.main.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netty.websocket.common.dto.CommonReturn;
import com.netty.websocket.common.other.ErrorCode;
import com.netty.websocket.main.dao.MyFriendsMapper;
import com.netty.websocket.main.dto.FriendsRequestDTO;
import com.netty.websocket.main.entity.BirdUsers;
import com.netty.websocket.main.entity.FriendsRequest;
import com.netty.websocket.main.services.FriendsRequestServices;
import com.netty.websocket.main.services.UserServices;
import com.netty.websocket.tools.password.PasswordUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

//@CrossOrigin(origins = "*")
@RestController
public class UserRestController {

    @Value("${update.root.path}")
    private String fileRootPath;

    @Autowired
    UserServices userServices;

    @Autowired
    FriendsRequestServices friendsRequestServices;

    /**
     * 添加用户或者用户登录的方法
     *
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    public CommonReturn registerOrlogin(@RequestBody BirdUsers user) {
        CommonReturn result = userServices.loginOrRegister(user);
        return result;
    }

    /**
     * 用户修改用户名
     * @param user
     * @return
     */
    @PostMapping("/user/edituser")
    public CommonReturn editUser(@RequestBody BirdUsers user){
        String[] strings = {"id","username","face_image","face_image_big","nickname"};
        CommonReturn result = userServices.setNickName(user,strings);
        return result;
    }

    /**
     * 用户头像上传
     */
    @PostMapping("/userface/upload")
    public CommonReturn faceupload(@RequestParam("file")MultipartFile file,String userId){
        CommonReturn result = userServices.setFaceImage(file,userId);
        return result;
    }

    /**
     * 用户通过输入用户名来添加好友
     * 前置条件:
     * 1、搜索的用户是否存在，则返回[无此用户]
     * 2、搜索的账号是你自己，则返回[不能添加自己]
     * 3、搜索的朋友已经是你的好友了，返回[该用户已经是你的好友了]
     * @param userId 发起搜索的用户id
     * @param username 用户输入的用户名
     * @return
     */
    @GetMapping("/user/bynamefirend")
    public CommonReturn byNameFindFirend(String userId,String username){
        CommonReturn result = userServices.byNameFindFirend(userId,username);
        return result;
    }

    /**
     * 向其他用户发送好友请求
     * @param user
     * @return
     */
    @PostMapping("user/addFirendRequest/")
    public CommonReturn addFirendRequest(@RequestBody BirdUsers user){
        CommonReturn result = friendsRequestServices.addFriendsRequest(user.getId(),user.getUsername());
        return result;
    }

    /**
     * 获取谁添加了这名用户
     * @param userId
     * @return
     */
    @GetMapping("user/bySendUserId")
    public CommonReturn bySendUserId(String userId){
        CommonReturn result = userServices.bySendUserId(userId);
        return result;
    }

    /**
     * 对其他用户传来的好友请求进行处理，最终返回你的所有的好友信息
     * 传入参数 userID(用户id)、friendId(朋友Id)、type 1:添加  0:忽略
     * 无论1还是0,请求表中都要删除有关的数据，成功添加时要操作两次，即用户与朋友存一次相反再存一次
     * @param
     * @return
     */
    @PutMapping("user/friendRequest")
    public CommonReturn friendRequest(@RequestBody FriendsRequestDTO friendsRequest){
        CommonReturn result = new CommonReturn();
        if(friendsRequest.getOperType()==1){
            result = userServices.addFriend(friendsRequest.getUserId(),friendsRequest.getFriendId());
        }else{
            result = userServices.ignore(friendsRequest.getUserId(),friendsRequest.getFriendId());
        }
        return result;
    }

    @GetMapping("user/getMyfriend")
    public CommonReturn getMyfriend(String userId){
        CommonReturn result = userServices.getMyfriend(userId);
        return result;
    }
}
