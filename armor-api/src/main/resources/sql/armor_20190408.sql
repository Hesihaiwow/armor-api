ALTER table task_expand ADD COLUMN create_time datetime DEFAULT NULL COMMENT '创建时间';

DROP TABLE IF EXISTS `task_temp`;
CREATE TABLE `task_temp` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `work_hours` decimal(10,1) NOT NULL COMMENT '工作量',
  `begin_time` datetime DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime DEFAULT NULL COMMENT '截止日期',
  `type` int(1) NOT NULL COMMENT '任务类型   1:个人任务  2:多人任务',
  `status` int(1) NOT NULL COMMENT '状态  1:进行中 2:已完成(待评价) 3: 已结束',
  `review_status` int(1) NOT NULL COMMENT '审核状态   1:待审核 2:审核通过 3:删除 ',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `description` varchar(255) NOT NULL COMMENT '任务描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='任务临时表';

DROP TABLE IF EXISTS `user_week_temp`;
CREATE TABLE `user_week_temp` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `week_number` int(3) NOT NULL COMMENT '本年度第几周',
  `hours` decimal(10,1) NOT NULL COMMENT '工作量',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `year` int(5) NOT NULL COMMENT '年度',
  `status` int(1) NOT NULL COMMENT '状态(0:待审核,1:审核通过,2:删除)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED COMMENT='用户-周工作量 临时表';






