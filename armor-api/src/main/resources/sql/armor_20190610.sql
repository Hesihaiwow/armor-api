DROP TABLE IF EXISTS `platform`;
CREATE TABLE `platform` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '平台名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建者id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布平台';

DROP TABLE IF EXISTS `week_publish_plan`;
CREATE TABLE `week_publish_plan` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '关联任务',
  `condition` text COMMENT '发布情况',
  `can_online` int(1) DEFAULT '0' COMMENT '是否可以发布上线(0:不可以,1:可以)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='周发版计划';

DROP TABLE IF EXISTS `week_publish_plan_platform`;
CREATE TABLE `week_publish_plan_platform` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '关联任务',
  `platform_id` bigint(20) NOT NULL COMMENT '发布平台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务-发布平台关联表';



