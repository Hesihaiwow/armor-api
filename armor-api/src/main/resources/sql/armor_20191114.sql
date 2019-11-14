-- 创建人 史长浩
-- 说明: 任务积分表新增唯一索引
-- 上线时间 2019-11-15

ALTER TABLE `user_task_integral`
ADD UNIQUE INDEX `task_user_unique` (`task_id`, `user_id`) ;








