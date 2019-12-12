-- 创建人 史长浩
-- 说明: 任务bug统计
-- 上线时间 2019-10-18

CREATE TABLE `task_bug` (
  `tb_id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `handler_id` bigint(20) NOT NULL COMMENT 'bug分派人',
  `severity` int(5) NOT NULL COMMENT '严重程度(1:需求或建议,2:一般错误,3:主要错误,4:严重错误)',
  `frequency` int(5) NOT NULL COMMENT '出现频率(1:固定重现, 2:测试随机, 3:无法重现)',
  `title` varchar(500) NOT NULL COMMENT '摘要',
  `description` text NOT NULL COMMENT '描述',
  `status` int(5) NOT NULL COMMENT '状态(1:已分派, 2:已解决, 3:已关闭,  4:打回)',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否 删除(0:否, 1:是)',
  PRIMARY KEY (`tb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务bug统计表';

CREATE TABLE `task_bug_log` (
  `tbl_id` bigint(20) NOT NULL COMMENT '主键',
  `tb_id` bigint(20) NOT NULL COMMENT '任务bugId',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`tbl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务bug日志表';

CREATE TABLE `task_bug_remark` (
  `tbr_id` bigint(20) NOT NULL COMMENT '主键',
  `tb_id` bigint(20) NOT NULL COMMENT '任务bugId',
  `remark` varchar(1000) DEFAULT NULL COMMENT '内容',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`tbr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务bug备注表';

