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

 Date: 01/11/2024 22:25:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int NOT NULL,
  `menuCode` varchar(8) DEFAULT NULL COMMENT '菜单编码',
  `menuName` varchar(16) DEFAULT NULL COMMENT '菜单名字',
  `menuLevel` varchar(2) DEFAULT NULL COMMENT '菜单级别',
  `menuParentCode` varchar(8) DEFAULT NULL COMMENT '菜单的父',
  `menuClick` varchar(16) DEFAULT NULL COMMENT '点击触发的函数',
  `menuRight` varchar(8) DEFAULT NULL COMMENT '权限 0超级管理员，1表示管理员，2表示普通用户',
  `menuComponent` varchar(200) DEFAULT NULL,
  `menuIcon` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` (`id`, `menuCode`, `menuName`, `menuLevel`, `menuParentCode`, `menuClick`, `menuRight`, `menuComponent`, `menuIcon`) VALUES (1, '001', '管理员管理', '1', NULL, 'Admin', '0', 'admin/AdminManage.vue', 'el-icon-s-custom');
INSERT INTO `menu` (`id`, `menuCode`, `menuName`, `menuLevel`, `menuParentCode`, `menuClick`, `menuRight`, `menuComponent`, `menuIcon`) VALUES (2, '002', '用户管理', '1', NULL, 'User', '0', 'user/UserManage.vue', 'el-icon-s-custom');
INSERT INTO `menu` (`id`, `menuCode`, `menuName`, `menuLevel`, `menuParentCode`, `menuClick`, `menuRight`, `menuComponent`, `menuIcon`) VALUES (3, '003', '信息管理', '1', NULL, 'Storage', '0,1,2', 'storage/StorageManage.vue', 'el-icon-s-custom');
INSERT INTO `menu` (`id`, `menuCode`, `menuName`, `menuLevel`, `menuParentCode`, `menuClick`, `menuRight`, `menuComponent`, `menuIcon`) VALUES (4, '004', '日志查看', '1', NULL, 'Record', '0,1,2,3', 'record/RecordManage.vue', 'el-icon-s-custom');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
