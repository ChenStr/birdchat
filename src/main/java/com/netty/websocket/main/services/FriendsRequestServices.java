package com.netty.websocket.main.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.netty.websocket.common.dto.CommonReturn;
import com.netty.websocket.main.entity.FriendsRequest;

public interface FriendsRequestServices extends IService<FriendsRequest> {

    /**
     * 根据当前用户人id与另一个用户的名字给另一个用户发送添加好友请求
     * @param userId 当前用户人id
     * @param username 另一个用户的名字
     * @return
     */
    public CommonReturn addFriendsRequest(String userId,String username);

}
