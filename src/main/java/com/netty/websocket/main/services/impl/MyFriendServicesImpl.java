package com.netty.websocket.main.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netty.websocket.main.dao.MyFriendsMapper;
import com.netty.websocket.main.entity.MyFriends;
import com.netty.websocket.main.services.MyFriendServices;
import org.springframework.stereotype.Service;

@Service
public class MyFriendServicesImpl extends ServiceImpl<MyFriendsMapper, MyFriends> implements MyFriendServices {

}
