DROP TABLE IF EXISTS `online_question_record`;
CREATE TABLE `online_question_record` (
  `oqr_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` text COMMENT '描述',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `work_hour` int(3) NOT NULL COMMENT '工作时长',
  `begin_time` datetime DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime DEFAULT NULL COMMENT '截止日期',
  `status` int(1) NOT NULL COMMENT '状态  1:进行中 2:已完成(待评价) 3: 已结束',
  `review_status` int(1) NOT NULL COMMENT '审核状态   1:待审核 2:审核不通过 3:审核通过 ',
  `is_delete` int(1) NOT NULL COMMENT '是否删除 (0:正常 1:已删除)',
  `create_by` bigint(20) NOT NULL COMMENT '创建者id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `complete_time` datetime DEFAULT NULL COMMENT '实际完成时间',
  PRIMARY KEY (`oqr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='线上问题记录表';

DROP TABLE IF EXISTS `record_url`;
CREATE TABLE `record_url` (
  `id` bigint(20) NOT NULL COMMENT '记录的图片地址id',
  `oqr_id` bigint(20) NOT NULL COMMENT '线上问题记录id',
  `url` text NOT NULL COMMENT '图片地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='线上问题记录关联图片地址表';