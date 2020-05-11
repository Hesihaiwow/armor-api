-- 创建人 史长浩
-- 说明: 任务新增测试用例文档字段
-- 上线时间 2020-05-12

ALTER TABLE `task`
ADD COLUMN `test_doc` text default NULL COMMENT '测试用例文档' AFTER `doc`;





