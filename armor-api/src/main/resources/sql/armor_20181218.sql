/**
新建表notification
 */
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `nid` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content` text COMMENT '通知内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` int(1) NOT NULL COMMENT '读取状态(0:未读,1:已读)',
  `read_time` datetime DEFAULT NULL COMMENT '查看时间',
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知表';