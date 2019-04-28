ALTER TABLE bug_manage add COLUMN demand_system_id int(10) DEFAULT NULL COMMENT '反馈系统id';

DROP TABLE IF EXISTS `mantis_bug_statistics`;
CREATE TABLE `mantis_bug_statistics` (
  `bs_id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) DEFAULT NULL COMMENT '任务id',
  `bug_id` int(10) NOT NULL COMMENT 'bugId',
  `reporter_id` int(10) NOT NULL COMMENT 'bug提交人',
  `handler_id` int(10) NOT NULL COMMENT 'bug处理人',
  `reporter_name` varchar(32) DEFAULT NULL COMMENT '提出人姓名',
  `handler_name` varchar(32) DEFAULT NULL COMMENT '解决人姓名',
  `severity` int(5) NOT NULL COMMENT '严重程度(10:需求或建议,30:不合理,40:一般错误,50:主要错误,60:严重错误)',
  `priority` int(5) NOT NULL COMMENT '优先级(10:无,20:低,30:中,40:高,50:加急,60:特急)',
  `status` int(5) NOT NULL COMMENT '状态(10:新建,20:打回,30:认可,40:已确认,50:已分派,80:已解决,90:已关闭)',
  `category_id` int(5) NOT NULL COMMENT '分类',
  `category_name` varchar(128) DEFAULT NULL COMMENT '分类名称',
  `import_time` datetime DEFAULT NULL COMMENT '导入时间',
  `date_submitted` int(10) DEFAULT NULL COMMENT '提出时间',
  `last_updated` int(10) DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`bs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='bug统计表';

DROP TABLE IF EXISTS `mantis_category`;
CREATE TABLE `mantis_category` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` int(10) NOT NULL COMMENT '分类id',
  `category_name` varchar(128) DEFAULT NULL COMMENT '分类名',
  `user_id` int(10) DEFAULT NULL COMMENT '负责人id',
  `project_id` int(10) DEFAULT NULL COMMENT '项目id',
  `sys_user_id` bigint(20) NOT NULL COMMENT '系统用户id',
  `user_name` varchar(32) NOT NULL COMMENT '用户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='mantis分类表';

DROP TABLE IF EXISTS `mantis_user`;
CREATE TABLE `mantis_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '账户名',
  `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='mantis用户表';





