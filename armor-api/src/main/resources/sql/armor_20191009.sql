-- 创建人 史长浩
-- 说明: 考勤功能修改,新增工号和调休时长字段
-- 上线时间 2019-10-12

ALTER TABLE `user`
ADD COLUMN `job_number`  varchar(10) NULL DEFAULT NULL COMMENT '工号' AFTER `level`,
ADD COLUMN `rest_hours`  decimal(5,1) NULL DEFAULT 0 COMMENT '调休时长' AFTER `job_number`;

CREATE TABLE `user_rest_hours_log` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(50) NOT NULL COMMENT '用户名字',
  `leave_id` bigint(20) DEFAULT NULL COMMENT '请假id',
  `rest_hours` decimal(5,1) NOT NULL DEFAULT '0.0' COMMENT '调休时长(正数为增加,负数是减少)',
  `type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '类型(1:手动修改, 2:请假扣除,3: 加班累加)',
  `content` varchar(500) DEFAULT NULL COMMENT '日志内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `record_time` datetime NOT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='调休日志';

