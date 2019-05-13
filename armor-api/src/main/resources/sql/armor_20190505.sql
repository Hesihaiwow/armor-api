ALTER TABLE task_temp ADD COLUMN level int(1) DEFAULT 1 COMMENT '审核级别';

DROP TABLE IF EXISTS `task_review_log`;
CREATE TABLE `task_review_log` (
  `id` bigint(20) NOT NULL,
  `task_temp_id` bigint(20) NOT NULL COMMENT '临时任务id',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `check_user_id` bigint(20) NOT NULL COMMENT '审核用户id',
  `check_user_name` varchar(50) NOT NULL COMMENT '审核用户名字',
  `level` int(1) NOT NULL COMMENT '审核级别(1:一级,2:二级,3:三级)',
  `suggest` varchar(255) DEFAULT NULL COMMENT '建议',
  `review_time` datetime NOT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务审核日志';

DROP TABLE IF EXISTS `user_check_people`;
CREATE TABLE `user_check_people` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `check_user_id` bigint(20) NOT NULL COMMENT '审核用户id',
  `status` int(1) NOT NULL COMMENT '状态(0:可用 1:不可用)',
  `level` int(1) NOT NULL COMMENT '级别(1:一级,2:二级,3:三级)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户新建任务审核人表';

DROP TABLE IF EXISTS `task_modify_table`;
CREATE TABLE `task_modify_table` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `work_hours` decimal(10,1) NOT NULL COMMENT '工作量',
  `begin_time` datetime DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime DEFAULT NULL COMMENT '截止日期',
  `type` int(1) NOT NULL COMMENT '任务类型   1:个人任务  2:多人任务',
  `status` int(1) NOT NULL COMMENT '状态  1:进行中 2:已完成(待评价) 3: 已结束',
  `review_status` int(1) NOT NULL COMMENT '审核状态   1:待审核 2:审核通过',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `reason` varchar(255) NOT NULL COMMENT '修改原因',
  `description` varchar(255) NOT NULL COMMENT '任务描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='修改任务临时表';

DROP TABLE IF EXISTS `task_modify_user_week`;
CREATE TABLE `task_modify_user_week` (
  `id` bigint(20) NOT NULL COMMENT '主键',
	`tm_id` bigint(20) NOT NULL COMMENT '任务修改id',
  `week_number` int(3) NOT NULL COMMENT '本年度第几周',
  `hours` decimal(10,1) NOT NULL COMMENT '工作量',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `year` int(5) NOT NULL COMMENT '年度',
  `status` int(1) NOT NULL COMMENT '状态(0:待审核,1:审核通过)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='修改任务  用户-周工作量 临时表';