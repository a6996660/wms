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

 Date: 01/11/2024 22:24:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blzc_data
-- ----------------------------
DROP TABLE IF EXISTS `blzc_data`;
CREATE TABLE `blzc_data` (
  `id` bigint NOT NULL,
  `bank` varchar(20) DEFAULT NULL,
  `user_name` varchar(100) NOT NULL,
  `user_type` varchar(30) DEFAULT NULL,
  `user_id` varchar(30) NOT NULL,
  `idc` varchar(50) DEFAULT NULL,
  `business_id` varchar(50) DEFAULT NULL,
  `first_date` varchar(20) DEFAULT NULL,
  `fafang_date` varchar(20) DEFAULT NULL,
  `daoqi_date` varchar(20) DEFAULT NULL,
  `blxc_date` varchar(20) DEFAULT NULL,
  `fafang_money` varchar(10) DEFAULT NULL,
  `daikuan_money` varchar(10) DEFAULT NULL,
  `bwqx_money` varchar(10) DEFAULT NULL,
  `dkgl_manager` varchar(15) DEFAULT NULL,
  `state` varchar(30) DEFAULT NULL,
  `case_id` varchar(30) DEFAULT NULL,
  `zhub_manager` varchar(15) DEFAULT NULL,
  `danb_way` varchar(10) DEFAULT NULL,
  `danb_name` varchar(100) DEFAULT NULL,
  `zbwsbldkje` varchar(20) DEFAULT NULL,
  `zbwsbldklx` varchar(20) DEFAULT NULL,
  `zbw_date` varchar(20) DEFAULT NULL,
  `daik_id` varchar(50) DEFAULT NULL,
  `faf_type` varchar(20) DEFAULT NULL,
  `hangy_type` varchar(100) DEFAULT NULL,
  `daik_use` varchar(100) DEFAULT NULL,
  `contact_num` varchar(30) DEFAULT NULL,
  `tel_num` varchar(30) DEFAULT NULL,
  `jiexhj_count` varchar(5) DEFAULT NULL,
  `first_jxhj_date` varchar(20) DEFAULT NULL,
  `qianx_count` varchar(5) DEFAULT NULL,
  `benjyq_count` varchar(5) DEFAULT NULL,
  `zuijyc_cuis_date` varchar(20) DEFAULT NULL,
  `zuijyc_jinzdc_date` varchar(20) DEFAULT NULL,
  `first_bulxc_date` varchar(20) DEFAULT NULL,
  `bank_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of blzc_data
-- ----------------------------
BEGIN;
INSERT INTO `blzc_data` (`id`, `bank`, `user_name`, `user_type`, `user_id`, `idc`, `business_id`, `first_date`, `fafang_date`, `daoqi_date`, `blxc_date`, `fafang_money`, `daikuan_money`, `bwqx_money`, `dkgl_manager`, `state`, `case_id`, `zhub_manager`, `danb_way`, `danb_name`, `zbwsbldkje`, `zbwsbldklx`, `zbw_date`, `daik_id`, `faf_type`, `hangy_type`, `daik_use`, `contact_num`, `tel_num`, `jiexhj_count`, `first_jxhj_date`, `qianx_count`, `benjyq_count`, `zuijyc_cuis_date`, `zuijyc_jinzdc_date`, `first_bulxc_date`, `bank_id`) VALUES (1, 'nihao', 'admin', '你好', '12', '234234', NULL, NULL, NULL, NULL, NULL, NULL, '10', '74', 'ding', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `blzc_data` (`id`, `bank`, `user_name`, `user_type`, `user_id`, `idc`, `business_id`, `first_date`, `fafang_date`, `daoqi_date`, `blxc_date`, `fafang_money`, `daikuan_money`, `bwqx_money`, `dkgl_manager`, `state`, `case_id`, `zhub_manager`, `danb_way`, `danb_name`, `zbwsbldkje`, `zbwsbldklx`, `zbw_date`, `daik_id`, `faf_type`, `hangy_type`, `daik_use`, `contact_num`, `tel_num`, `jiexhj_count`, `first_jxhj_date`, `qianx_count`, `benjyq_count`, `zuijyc_cuis_date`, `zuijyc_jinzdc_date`, `first_bulxc_date`, `bank_id`) VALUES (1831000554344595457, '', '533252', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', NULL, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `blzc_data` (`id`, `bank`, `user_name`, `user_type`, `user_id`, `idc`, `business_id`, `first_date`, `fafang_date`, `daoqi_date`, `blxc_date`, `fafang_money`, `daikuan_money`, `bwqx_money`, `dkgl_manager`, `state`, `case_id`, `zhub_manager`, `danb_way`, `danb_name`, `zbwsbldkje`, `zbwsbldklx`, `zbw_date`, `daik_id`, `faf_type`, `hangy_type`, `daik_use`, `contact_num`, `tel_num`, `jiexhj_count`, `first_jxhj_date`, `qianx_count`, `benjyq_count`, `zuijyc_cuis_date`, `zuijyc_jinzdc_date`, `first_bulxc_date`, `bank_id`) VALUES (1831001428362051585, '', 'r23423', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', NULL, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `blzc_data` (`id`, `bank`, `user_name`, `user_type`, `user_id`, `idc`, `business_id`, `first_date`, `fafang_date`, `daoqi_date`, `blxc_date`, `fafang_money`, `daikuan_money`, `bwqx_money`, `dkgl_manager`, `state`, `case_id`, `zhub_manager`, `danb_way`, `danb_name`, `zbwsbldkje`, `zbwsbldklx`, `zbw_date`, `daik_id`, `faf_type`, `hangy_type`, `daik_use`, `contact_num`, `tel_num`, `jiexhj_count`, `first_jxhj_date`, `qianx_count`, `benjyq_count`, `zuijyc_cuis_date`, `zuijyc_jinzdc_date`, `first_bulxc_date`, `bank_id`) VALUES (1831001600227852290, '32r23r', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', NULL, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `blzc_data` (`id`, `bank`, `user_name`, `user_type`, `user_id`, `idc`, `business_id`, `first_date`, `fafang_date`, `daoqi_date`, `blxc_date`, `fafang_money`, `daikuan_money`, `bwqx_money`, `dkgl_manager`, `state`, `case_id`, `zhub_manager`, `danb_way`, `danb_name`, `zbwsbldkje`, `zbwsbldklx`, `zbw_date`, `daik_id`, `faf_type`, `hangy_type`, `daik_use`, `contact_num`, `tel_num`, `jiexhj_count`, `first_jxhj_date`, `qianx_count`, `benjyq_count`, `zuijyc_cuis_date`, `zuijyc_jinzdc_date`, `first_bulxc_date`, `bank_id`) VALUES (1831002197802926082, 'vvvv', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', NULL, '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
