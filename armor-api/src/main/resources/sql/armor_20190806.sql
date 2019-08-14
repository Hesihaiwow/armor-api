-- 创建人 史长浩
-- 说明: 测试用例  建表
-- 上线时间 2019-08-15

CREATE TABLE `test_function` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `create_name` varchar(32) DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人id',
  `update_name` varchar(32) DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试用例功能点表';

CREATE TABLE `test_example` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `function_id` bigint(20) NOT NULL COMMENT '任务功能点ID',
  `check_point` text DEFAULT NULL COMMENT '检查项',
  `expect_result` text DEFAULT NULL COMMENT '预期结果',
  `remark` text DEFAULT NULL COMMENT '备注',
  `type` int(1) DEFAULT NULL COMMENT '正反用例   0:正常用例  1:异常用例',
  `status` int(1) DEFAULT NULL COMMENT '状态  0:通过,1:失败 2:阻塞',
  `exam_status` int(1) DEFAULT NULL COMMENT '评审状态   0:评审通过, 1:评审失败',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `create_by` bigint(20) NOT NULL COMMENT '创建人id',
  `create_name` varchar(32) DEFAULT NULL COMMENT '创建人姓名',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新人id',
  `update_name` varchar(32) DEFAULT NULL COMMENT '更新人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试用例表';