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

 Date: 01/11/2024 22:07:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for loanRecords
-- ----------------------------
DROP TABLE IF EXISTS `loanRecords`;
CREATE TABLE `loanRecords` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `borrower_name` varchar(255) NOT NULL COMMENT '借款人名称',
  `business_number` varchar(100) DEFAULT NULL COMMENT '业务编号',
  `loan_balance` decimal(15,2) DEFAULT NULL COMMENT '贷款余额',
  `loan_balance_change` decimal(15,2) DEFAULT NULL COMMENT '贷款余额发生额',
  `overdue_interest` decimal(15,2) DEFAULT NULL COMMENT '表外欠息金额',
  `overdue_interest_change` decimal(15,2) DEFAULT NULL COMMENT '表外欠息金额发生额',
  `primary_client_manager` varchar(255) DEFAULT NULL COMMENT '主办客户经理',
  `responsible_person` varchar(255) DEFAULT NULL COMMENT '管子责任人',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人',
  `operation_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `receive_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收回时间',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1832054287576887299 DEFAULT CHARSET=utf8mb4 ;

-- ----------------------------
-- Records of loanRecords
-- ----------------------------
BEGIN;
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1830983236994514945, 'admin', NULL, 37.00, 2.00, 85.00, NULL, NULL, 'ding', '1', '2024-09-03 22:56:36', '2024-09-13 22:56:00', NULL);
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1832048509000470529, 'admin', NULL, 30.00, 7.00, 80.00, NULL, NULL, 'ding', '1', '2024-09-06 21:29:37', '2024-09-06 21:29:00', NULL);
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1832048583608750081, 'admin', NULL, 23.00, 7.00, 75.00, NULL, NULL, 'ding', '1', '2024-09-06 21:29:55', '2024-09-06 21:29:00', NULL);
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1832048985532125186, 'admin', NULL, 22.00, 1.00, 74.00, NULL, NULL, 'ding', '1', '2024-09-06 21:31:31', '2024-09-06 21:31:00', NULL);
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1832053176161828866, 'admin', NULL, 16.00, 2.00, 74.00, NULL, NULL, 'ding', '1', '2024-09-06 21:48:10', '2024-09-06 21:48:00', NULL);
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1832053992620851202, 'admin', NULL, 14.00, 2.00, 74.00, NULL, NULL, 'ding', '1', '2024-09-06 21:51:24', '2024-09-27 21:50:00', NULL);
INSERT INTO `loanRecords` (`id`, `borrower_name`, `business_number`, `loan_balance`, `loan_balance_change`, `overdue_interest`, `overdue_interest_change`, `primary_client_manager`, `responsible_person`, `operator`, `operation_time`, `receive_time`, `remark`) VALUES (1832054287576887298, 'admin', NULL, 10.00, 2.00, 74.00, 0.00, NULL, 'ding', '1', '2024-09-06 21:52:35', '2024-09-06 21:52:00', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
