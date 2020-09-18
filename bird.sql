/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 127.0.0.1:3306
 Source Schema         : bird

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 18/09/2020 18:02:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bird_msg
-- ----------------------------
DROP TABLE IF EXISTS `bird_msg`;
CREATE TABLE `bird_msg`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `send_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发送给谁(用户id)',
  `accept_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发送人id',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发送消息',
  `create_time` datetime(0) DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bird_users
-- ----------------------------
DROP TABLE IF EXISTS `bird_users`;
CREATE TABLE `bird_users`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `face_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像(小)',
  `face_image_big` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像(大)',
  `nickname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `qrcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '二维码',
  `cid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'cid针对手机端用户使用的',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bird_users
-- ----------------------------
INSERT INTO `bird_users` VALUES ('a', '石头', '$2a$10$42mOFB/gr6rxNMlMxYTlo.hLhmE1K4em4h43edVklW6ct1GFkpkDO', 'images/a1600054007058.jpg', NULL, 'angelina', 'qrcode/fZ9Nn4KSVm43oSFPnotz8D6KkJH2HI2hG0HzxwFfhFanjxbFUPn2LZBaf9PWS8CjQrcode.png', '0e4030d6c8549e0cd8b3fa4f3b78467b');
INSERT INTO `bird_users` VALUES ('b', '老虎', '$2a$10$IrQ9qgQSCY3qJWaZSPXDxuCunOPDaK0/HFUi7RjkTkCCxTFepowe6', 'images/b1600054247315.jpg', NULL, '老虎', 'qrcode/tBEkDYZ6hYDM5YsGv1FPje8F0QW7JpeeswYUmieIAJePJtFslbjNAjRNPxtlO9YTQrcode.png', '0e4030d6c8549e0cd8b3fa4f3b78467b');
INSERT INTO `bird_users` VALUES ('c', '红', '$2a$10$Ly80VjuHNV5BtMWU41J85eiBJrJxmDdMqpAmFUBQTxVFoxkTNRzd.', 'images/c1600054314325.jpg', NULL, 'red', 'qrcode/gzjq7aNq4Dcs4MUhIG0t71Dr5w2oHNxIAAFj7DeUJTGAOGq0wmFoPkSRaLXqWRRzQrcode.png', '0e4030d6c8549e0cd8b3fa4f3b78467b');

-- ----------------------------
-- Table structure for friends_request
-- ----------------------------
DROP TABLE IF EXISTS `friends_request`;
CREATE TABLE `friends_request`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `send_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发起人id',
  `accept_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '发送给谁(用户id)',
  `request_date_time` datetime(0) DEFAULT NULL COMMENT '发起时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for my_friends
-- ----------------------------
DROP TABLE IF EXISTS `my_friends`;
CREATE TABLE `my_friends`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `my_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户id',
  `my_friend_user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户朋友id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of my_friends
-- ----------------------------
INSERT INTO `my_friends` VALUES ('B4UHvJpUKsnvWTBOvw35hgWQ6HRp7te0BxY6Ivcsmmy3XYkAEwVDPg27BMPRxnMa', 'b', 'c');
INSERT INTO `my_friends` VALUES ('kZCmNXdg1VMBY5NDQmafpiYT2A0MnxmEaQVNqXwKg7aXqsdWdYydd91p8HS4R5UU', 'c', 'b');
INSERT INTO `my_friends` VALUES ('R5FDVstpaAFSega2Hv6UcKE2JFaSw1ja1f0WI91MdIzJOMHc1GkFjXHtsT8CrFKI', 'a', 'b');
INSERT INTO `my_friends` VALUES ('wuwifoIqf5GDTCKuNTISPvehCoSuN3KcohrS9pAv3RyPJQ11hqNKLpNlT9xHfBp6', 'b', 'a');

SET FOREIGN_KEY_CHECKS = 1;
