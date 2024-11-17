/*
 Navicat Premium Data Transfer

 Source Server         : 树莓派
 Source Server Type    : MySQL
 Source Server Version : 101106 (10.11.6-MariaDB-0+deb12u1)
 Source Host           : 192.168.5.139:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 101106 (10.11.6-MariaDB-0+deb12u1)
 File Encoding         : 65001

 Date: 16/11/2024 11:41:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for taskSchedule
-- ----------------------------
DROP TABLE IF EXISTS `taskSchedule`;
CREATE TABLE `taskSchedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `taskId` varchar(255) NOT NULL COMMENT '任务ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `cron` varchar(100) DEFAULT NULL COMMENT '调度时间',
  `params` varchar(1024) DEFAULT NULL COMMENT '参数',
  `taskType` varchar(50) DEFAULT NULL COMMENT '任务类型',
  `createTime` timestamp NULL DEFAULT current_timestamp() COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `status` varchar(255) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1856369086651461634 DEFAULT CHARSET=utf8mb4 ;

-- ----------------------------
-- Records of taskSchedule
-- ----------------------------
BEGIN;
INSERT INTO `taskSchedule` (`id`, `taskId`, `name`, `cron`, `params`, `taskType`, `createTime`, `updateTime`, `status`) VALUES (5898393391, '1', '定时发消息', '5 27 * * * ?', '{   \"city\": [\"北京\", \"莱州\",\"威海\"],   \"api\": \"d55bd7f0e0524eef9f264b2fdc29b4fe\",   \"url\": \"http://192.168.5.139:3001/webhook/msg/v2?token=dingxhui\",   \"isRoom\": false,   \"name\": \"丁某某\" }', '1', '2024-11-14 00:25:34', '2024-11-15 00:26:27', '1');
INSERT INTO `taskSchedule` (`id`, `taskId`, `name`, `cron`, `params`, `taskType`, `createTime`, `updateTime`, `status`) VALUES (5898552074, '2', '天气消息', '0 10 7 * * ?', '{   \"city\": [\"北京\", \"莱州\",\"威海\"],   \"api\": \"d55bd7f0e0524eef9f264b2fdc29b4fe\",   \"url\": \"http://192.168.5.139:3001/webhook/msg/v2?token=dingxhui\",   \"isRoom\": true,   \"name\": \"交个朋友\" }', '2', '2024-11-14 00:52:01', '2024-11-15 00:26:51', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
