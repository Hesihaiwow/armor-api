-- 创建人 史长浩
-- 说明: 任务总结优化
-- 上线时间 2019-10-30

ALTER TABLE `task_summary`
ADD COLUMN `quality`  decimal(2,1) NULL DEFAULT 0 COMMENT '任务完成质量' AFTER `is_delete`,
ADD COLUMN `is_delayed`  tinyint(1) NULL DEFAULT 0 COMMENT '是否延期(0:无, 1:有)' AFTER `quality`,
ADD COLUMN `has_communicate_problem`  tinyint(1) NULL DEFAULT 0 COMMENT '是否有沟通问题字段(0:无, 1:有)' AFTER `is_delayed` ;

ALTER TABLE `user_rest_hours_log`
MODIFY COLUMN `rest_hours`  decimal(5,0) NOT NULL DEFAULT 0.0 COMMENT '调休时长(正数为增加,负数是减少)' AFTER `ew_id`;






