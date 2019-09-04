-- 创建人 史长浩
-- 说明: 任务评审和总结功能
-- 上线时间 2019-09-05

CREATE TABLE `task_review` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `comment` varchar(256) DEFAULT NULL COMMENT '评审内容',
  `suggest` varchar(256) DEFAULT NULL COMMENT '改进意见',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_by` bigint(20) NOT NULL COMMENT '更新人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务评审(待设计和设计中任务进入到开发阶段必须先评审)';

CREATE TABLE `task_summary` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `comment` varchar(256) DEFAULT NULL COMMENT '总结内容',
  `gain` varchar(256) DEFAULT NULL COMMENT '收获',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `update_by` bigint(20) NOT NULL COMMENT '更新人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务总结(已发布任务须总结)';


