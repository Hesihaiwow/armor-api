-- 创建人 史长浩
-- 说明: 发布平台加 '分组' 字段
-- 上线时间 2020-09-17

ALTER TABLE `platform`
ADD COLUMN `group_mark`  tinyint(3) NOT NULL DEFAULT 0 COMMENT '分组(0:java, 1:php, 2:前端, 3:app&客户端)' AFTER `name`;


CREATE TABLE `week_publish_plan_platform_user` (
  `wpppu_id` bigint(20) NOT NULL COMMENT '主键',
  `wpp_id` bigint(20) NOT NULL COMMENT '周发版计划id',
  `platform_id` bigint(20) NOT NULL COMMENT '发布平台id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`wpppu_id`),
  UNIQUE KEY `plan_platform_user_index` (`wpp_id`,`platform_id`,`user_id`) USING BTREE COMMENT '计划_平台_用户唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发版计划多次发布的平台和用户表';




