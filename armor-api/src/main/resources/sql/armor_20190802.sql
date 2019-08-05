-- 创建人 史长浩
-- 说明: 新建任务时,添加<功能点>  新建以下表
-- 上线时间 2019-08-05

CREATE TABLE `task_modify_function` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tm_id` bigint(20) NOT NULL COMMENT '临时任务id',
  `function_id` bigint(20) NOT NULL COMMENT '关联任务功能点',
  `level` int(1) NOT NULL COMMENT '功能点复杂度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='修改任务功能点表';