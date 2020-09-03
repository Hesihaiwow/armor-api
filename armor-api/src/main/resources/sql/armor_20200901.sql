-- 创建人 史长浩
-- 说明: 重新建表  发版计划相关
-- 上线时间 2020-09-10

DROP TABLE IF EXISTS `week_publish_plan`;
CREATE TABLE `week_publish_plan` (
  `wpp_id` bigint(20) NOT NULL COMMENT '主键',
  `wpp_name` varchar(50) NOT NULL COMMENT '名称/标题',
  `publish_time` datetime NOT NULL COMMENT '发版时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `test_report` bigint(20) DEFAULT NULL COMMENT '测试报告',
  `is_delete` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否删除(0:未删除, 1:已删除)',
  PRIMARY KEY (`wpp_id`),
  UNIQUE KEY `name_index` (`wpp_name`) USING BTREE COMMENT '发版标题唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='周发版计划';



DROP TABLE IF EXISTS `week_publish_plan_platform`;
CREATE TABLE `week_publish_plan_platform` (
  `wppp_id` bigint(20) NOT NULL COMMENT '主键',
  `wpp_id` bigint(20) NOT NULL COMMENT '发版计划id',
  `platform_id` bigint(20) NOT NULL COMMENT '发布平台id',
  PRIMARY KEY (`wppp_id`),
  UNIQUE KEY `plan_platform_index` (`wpp_id`,`platform_id`) USING BTREE COMMENT '计划_平台唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='周发版计划关联发布平台';

DROP TABLE IF EXISTS `week_publish_plan_task`;
CREATE TABLE `week_publish_plan_task` (
  `wppt_id` bigint(20) NOT NULL COMMENT '主键',
  `wpp_id` bigint(20) NOT NULL COMMENT '发版计划id',
  `task_id` bigint(20) NOT NULL COMMENT '关联任务id',
  PRIMARY KEY (`wppt_id`),
  UNIQUE KEY `plan_task_index` (`wpp_id`,`task_id`) USING BTREE COMMENT '计划_任务唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='周发版计划关联任务';




