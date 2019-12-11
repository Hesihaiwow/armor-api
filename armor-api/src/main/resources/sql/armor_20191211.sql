-- 创建人 史长浩
-- 说明: 任务bug统计
-- 上线时间 2019-12-12

ALTER TABLE `task_bug`
ADD COLUMN `tb_no`  int(8) NOT NULL COMMENT 'bug编号' AFTER `tb_id`;

