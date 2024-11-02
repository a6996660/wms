/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 01/11/2024 22:25:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `no` varchar(20) DEFAULT NULL COMMENT '账号',
  `name` varchar(100) NOT NULL COMMENT '名字',
  `password` varchar(20) NOT NULL COMMENT '密码',
  `age` int DEFAULT NULL,
  `sex` int DEFAULT NULL COMMENT '性别',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `role_id` int DEFAULT NULL COMMENT '角色0超级管理员，1管理员，2普通账量',
  `isValid` varchar(4) DEFAULT 'Y' COMMENT '是否有效，Y有效，其他无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (1, 'admin', '超级管理员', '1234', 0, 0, '0', 1, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (2, 'xiaoming2', '小明2', '123', NULL, NULL, NULL, 0, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (3, 'xiaoming', '小明', '123', NULL, NULL, NULL, 0, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (4, 'xiaoming', '小明', '123', NULL, NULL, NULL, 0, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (5, 'xiaoming', '小明', '123', NULL, NULL, NULL, 0, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (6, 'ding', 'dingxhui', '1234', 1, 1, '18563137538', 1, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (7, '1', '1', '1', NULL, NULL, NULL, 0, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (8, '222', '2', '222', 22, 0, '18563137538', 2, 'Y');
INSERT INTO `user` (`id`, `no`, `name`, `password`, `age`, `sex`, `phone`, `role_id`, `isValid`) VALUES (9, '4', '4', '4', NULL, NULL, NULL, 3, 'Y');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
