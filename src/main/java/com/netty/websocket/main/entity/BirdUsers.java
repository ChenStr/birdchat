package com.netty.websocket.main.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("bird_users")
@EqualsAndHashCode(callSuper=false)
public class BirdUsers {
    public String id;

    public String username;

    public String password;

    public String faceImage;

    public String faceImageBig;

    public String nickname;

    public String qrcode;

    public String cid;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id == null ? null : id.trim();
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username == null ? null : username.trim();
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password == null ? null : password.trim();
//    }
//
//    public String getFaceImage() {
//        return faceImage;
//    }
//
//    public void setFaceImage(String faceImage) {
//        this.faceImage = faceImage == null ? null : faceImage.trim();
//    }
//
//    public String getFaceImageBig() {
//        return faceImageBig;
//    }
//
//    public void setFaceImageBig(String faceImageBig) {
//        this.faceImageBig = faceImageBig == null ? null : faceImageBig.trim();
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname == null ? null : nickname.trim();
//    }
//
//    public String getQrcode() {
//        return qrcode;
//    }
//
//    public void setQrcode(String qrcode) {
//        this.qrcode = qrcode == null ? null : qrcode.trim();
//    }
//
//    public String getCid() {
//        return cid;
//    }
//
//    public void setCid(String cid) {
//        this.cid = cid == null ? null : cid.trim();
//    }
}