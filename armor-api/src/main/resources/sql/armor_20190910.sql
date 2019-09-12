-- 创建人 史长浩
-- 说明: 用户任务积分
-- 上线时间 2019-09-12

CREATE TABLE `user_task_integral` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) DEFAULT NULL COMMENT '任务id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `integral` decimal(10,2) NOT NULL COMMENT '积分',
  `score` decimal(10,2) DEFAULT NULL COMMENT '评价',
  `origin` int(1) NOT NULL COMMENT '积分来源(1:多人任务 2:个人任务 3:手动录入 4:bug积分)',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者id',
  `description` varchar(1000) DEFAULT NULL COMMENT '积分描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `review_status` int(1) DEFAULT '0' COMMENT '审核状态   1:待审核 2:审核不通过 3:审核通过 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户任务积分明细';