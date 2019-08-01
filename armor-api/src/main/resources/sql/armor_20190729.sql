-- 创建人 史长浩
-- 说明: 新建任务时,添加<功能点>  新建以下表
-- 上线时间 2019-08-01

DROP TABLE IF EXISTS `task_temp_module`;
CREATE TABLE `task_temp_module` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '项目名称',
  `description` varchar(500) DEFAULT NULL COMMENT '项目简介',
  `create_by` bigint(20) NOT NULL COMMENT '创建者id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除 (0:正常 1:已删除)',
  `image_url` varchar(100) DEFAULT NULL COMMENT '项目图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='临时任务涉及项目';

DROP TABLE IF EXISTS `task_function`;
CREATE TABLE `task_function` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `module_id` bigint(20) DEFAULT NULL COMMENT '涉及模块或子系统ID',
  `function` text NOT NULL COMMENT '功能点描述',
  `action` int(1) NOT NULL COMMENT '动作(0:新增,1:修改,2:删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务功能点表';

DROP TABLE IF EXISTS `task_temp_function`;
CREATE TABLE `task_temp_function` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tt_id` bigint(20) NOT NULL COMMENT '临时任务id',
  `function_id` bigint(20) NOT NULL COMMENT '关联任务功能点',
  `level` int(1) NOT NULL COMMENT '功能点复杂度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务临时功能点表';