package com.netty.websocket.main.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netty.websocket.common.dto.CommonReturn;
import com.netty.websocket.common.service.BaseService;
import com.netty.websocket.main.dao.BirdUsersMapper;
import com.netty.websocket.main.entity.BirdUsers;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserServices extends IService<BirdUsers> {

//    /**
//     * 根据用户id查询用户
//     * @param id
//     * @return User
//     */
//    BirdUsers getUserById(String id);
//
//    /**
//     * 根据用户的用户名来查找用户
//     * @param username
//     * @return User
//     */
//    BirdUsers getUserByUsername(String username,String[] strings);

//    /**
//     * 创建用户
//     * @param user
//     * @return
//     */
//    BirdUsers createUser(BirdUsers user);

    /**
     * 登录或者注册
     * @param user
     * @return
     */
    CommonReturn loginOrRegister(BirdUsers user);

    /**
     * 修改用户的用户名
     * @param user
     * @param strings
     * @return
     */
    CommonReturn setNickName(BirdUsers user,String[] strings);

    /**
     * 文件上传
     * @param files 文件
     * @param userId 用户id
     * @param white 白名单
     * @param address 存放地址
     * @return
     */
    CommonReturn upLoad(MultipartFile[] files, String userId,String[] white,String address);

    /**
     * 批量文件删除
     * @param address
     * @return
     */
    CommonReturn deleteFile(List<String> address);

    /**
     * 更改用户头像
     * @param file
     * @param userId
     * @return
     */
    CommonReturn setFaceImage(MultipartFile file,String userId);


    /**
     * 根据用户名来添加用户
     * @param userId
     * @param username
     * @return
     */
    CommonReturn byNameFindFirend(String userId,String username);

    /**
     * 获取谁添加了这名用户
     * @param userId
     * @return
     */
    CommonReturn bySendUserId(String userId);

    /**
     * 添加朋友
     * @param userId
     * @param friendId
     * @return
     */
    CommonReturn addFriend(String userId,String friendId);

    /**
     * 忽略添加朋友
     * @param userId
     * @param friendId
     * @return
     */
    CommonReturn ignore(String userId,String friendId);

    /**
     * 通过userId来查找这名用户的朋友
     * @param userId
     * @return
     */
    CommonReturn getMyfriend(String userId);
}
