package com.netty.websocket.main.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netty.websocket.common.dto.CommonReturn;
import com.netty.websocket.main.dao.FriendsRequestMapper;
import com.netty.websocket.main.entity.BirdUsers;
import com.netty.websocket.main.entity.FriendsRequest;
import com.netty.websocket.main.services.FriendsRequestServices;
import com.netty.websocket.main.services.UserServices;
import com.netty.websocket.tools.codeworker.QRCodeUtils;
import com.netty.websocket.tools.codeworker.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FriendsRequestServicesImpl extends ServiceImpl<FriendsRequestMapper, FriendsRequest> implements FriendsRequestServices {

    @Autowired
    UserServices userServices;


    @Override
    public CommonReturn addFriendsRequest(String userId, String username) {
        //首先先判断这个用户是否存在、userId、username是否为空、要发送请求的用户是否合法(不是自己本人或者不是自己的好友)
        CommonReturn result = userServices.byNameFindFirend(userId,username);

        if (result.data==null){

        }else{
            //查看表中用户是否已经给该用户发送好友请求了
            //获取到被添加的用户的用户信息
            BirdUsers user = (BirdUsers) result.getData();
            //查找表中是否有重复数据
            QueryWrapper<FriendsRequest> requestQueryWrapper = new QueryWrapper<>();
            requestQueryWrapper.eq("send_user_id",userId).eq("accept_user_id",user.getId());
            FriendsRequest friendsRequest = this.getOne(requestQueryWrapper);
            if (friendsRequest!=null){
                result.setAll(10001,null,"已经向该用户发起好友请求了，请勿重复发送!");
            }else{
                //往好友请求表中添加相应的数据
                FriendsRequest request = new FriendsRequest();
                request.setId(UuidUtil.generateStr(64));
                request.setSendUserId(userId);
                request.setAcceptUserId(user.getId());
                request.setRequestDateTime(new Date());
                this.save(request);
                result.setAll(20000,null,"发送好友请求成功");
            }
        }
        return result;
    }
}
