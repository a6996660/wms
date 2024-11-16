DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`       varchar(255) NOT NULL COMMENT '日志名称',
    `type`       varchar(255) NOT NULL COMMENT '日志类型',
    `message`    text COMMENT '日志内容',
    `operator`   varchar(255)      DEFAULT NULL COMMENT '操作人',
    `createTime` timestamp    NULL DEFAULT current_timestamp() COMMENT '日志时间',
    `remark`     text COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1832054287576887299
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;