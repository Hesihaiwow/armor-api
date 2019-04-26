ALTER table online_question_record MODIFY work_hour decimal(10,1) NOT NULL COMMENT '工作时长';

DROP TABLE IF EXISTS `extra_work`;
CREATE TABLE `extra_work` (
  `id` bigint(20) NOT NULL COMMENT 'ID，主键',
  `user_id` bigint(20) NOT NULL COMMENT '申请人id',
  `reason` text NOT NULL COMMENT '原因',
  `begin_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `work_hours` decimal(10,1) NOT NULL COMMENT '加班时长',
  `is_delete` int(1) NOT NULL COMMENT '是否删除(0:未删除,1:已删除)',
  `check_time` datetime DEFAULT NULL COMMENT '申请通过时间',
  `review_status` int(1) DEFAULT NULL COMMENT '申请状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='加班申请表';

DROP TABLE IF EXISTS `extra_work_task`;
CREATE TABLE `extra_work_task` (
  `id` bigint(20) NOT NULL COMMENT 'ID，主键',
  `ew_id` bigint(20) NOT NULL COMMENT '加班申请id',
  `task_id` bigint(20) NOT NULL COMMENT '关联任务id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='加班关联任务表';

DROP TABLE IF EXISTS `user_sort`;
CREATE TABLE `user_sort` (
  `sort` int(5) NOT NULL COMMENT '序号',
  `user_name` varchar(10) NOT NULL COMMENT '用户名'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户-序号表';

DROP TABLE IF EXISTS `sign_in`;
CREATE TABLE `sign_in` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` int(1) NOT NULL COMMENT '状态(1:上班,2:下班,3:其他)',
  `check_time` datetime NOT NULL COMMENT '申请通过时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户考勤签到表';

DROP TABLE IF EXISTS `resign_in`;
CREATE TABLE `resign_in` (
  `id` bigint(20) NOT NULL COMMENT 'ID，主键',
  `user_id` bigint(20) NOT NULL COMMENT '申请人id',
  `reason` text NOT NULL COMMENT '原因',
  `recheck_time` datetime DEFAULT NULL COMMENT '申请通过时间',
  `review_status` int(1) DEFAULT NULL COMMENT '申请状态',
  `type` int(1) DEFAULT NULL COMMENT '类型(0:上班,1:下班)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='补打卡申请表';

