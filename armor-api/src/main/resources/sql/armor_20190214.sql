ALTER table online_question_record MODIFY work_hour decimal(10,1) NOT NULL COMMENT '工作时长'

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

