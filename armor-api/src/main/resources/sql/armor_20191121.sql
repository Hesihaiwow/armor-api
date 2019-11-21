-- 创建人 史长浩
-- 说明: 线上bug关联任务
-- 上线时间 2019-11-22

ALTER TABLE `bug_manage`
ADD COLUMN `task_id`  bigint(20) NULL DEFAULT NULL COMMENT '任务id' AFTER `project_id`;















