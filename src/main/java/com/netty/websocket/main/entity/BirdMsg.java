package com.netty.websocket.main.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@TableName("bird_msg")
@EqualsAndHashCode(callSuper=false)
public class BirdMsg {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private String msg;

    private Date createTime;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id == null ? null : id.trim();
//    }
//
//    public String getSendUserId() {
//        return sendUserId;
//    }
//
//    public void setSendUserId(String sendUserId) {
//        this.sendUserId = sendUserId == null ? null : sendUserId.trim();
//    }
//
//    public String getAcceptUserId() {
//        return acceptUserId;
//    }
//
//    public void setAcceptUserId(String acceptUserId) {
//        this.acceptUserId = acceptUserId == null ? null : acceptUserId.trim();
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg == null ? null : msg.trim();
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
}