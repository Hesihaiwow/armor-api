-- 创建人 史长浩
-- 说明: 任务查询优化
-- 上线时间 2019-11-27

ALTER TABLE `task`
DROP INDEX `id`,
DROP INDEX `index`;

ALTER TABLE `task`
ADD INDEX `stage_index` (`stage_id`) USING BTREE ;

ALTER TABLE `task_tag`
ADD INDEX `tag_index` (`tag_id`) USING BTREE ;

ALTER TABLE `task_user`
ADD INDEX `task_index` (`task_id`) USING BTREE ;

















