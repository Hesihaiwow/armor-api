DROP TABLE IF EXISTS `task_evaluation`;
CREATE TABLE `task_evaluation` (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `task_user_id` bigint(20) NOT NULL COMMENT '任务阶段用户id',
  `score` decimal(5,1) NOT NULL COMMENT '得分',
  `evaluation_option` int(1) NOT NULL COMMENT '评价项(1:沟通,2:态度,3:效率,4:质量,5:文档,6:美感)',
  `integral` int(5) DEFAULT NULL COMMENT '积分',
  `comment` text DEFAULT NULL COMMENT '评论描述',
  `evaluate_user_id` bigint(20) NOT NULL COMMENT '评分人id',
  `evaluate_time` datetime NOT NULL COMMENT '评分时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务评分';

