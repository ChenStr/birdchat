package com.netty.websocket.main.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用来接收用户处理好友请求接口参数的类
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FriendsRequestDTO {

    String userId;

    String friendId;

    //状态 0:拒绝 1:同意
    Integer operType;
}
