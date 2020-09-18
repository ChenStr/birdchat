package com.netty.websocket.netty.entity;

import com.netty.websocket.main.entity.BirdMsg;

import java.io.Serializable;

public class DataContent implements Serializable {

    //动作类型
    private Integer action;

    //用户的聊天内容
    private BirdMsg birdMsg;

    //扩展字段
    private String extand;

}
