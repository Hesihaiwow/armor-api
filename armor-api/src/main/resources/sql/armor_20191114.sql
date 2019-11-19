-- 创建人 史长浩
-- 说明: 任务积分表新增唯一索引
-- 上线时间 2019-11-15

ALTER TABLE `user_task_integral`
ADD COLUMN `bug_id`  bigint(20) NULL DEFAULT null COMMENT '线上任务bugId' AFTER `user_id`,
ADD UNIQUE INDEX `task_user_unique` (`task_id`, `user_id`) USING BTREE COMMENT '任务-用户联合唯一索引',
ADD UNIQUE INDEX `bug_user_unique` (`user_id`, `bug_id`) USING BTREE COMMENT 'bug-用户联合唯一索引';










