-- 创建人 史长浩
-- 说明: 用户信息改动(改字段注释)  线上bug优化(新增字段)
-- 上线时间 2019-08-30

ALTER TABLE `user` MODIFY COLUMN `job_role` int(2) NOT NULL DEFAULT 0 COMMENT '角色(0:测试, 1:开发, 2:设计, 3:产品, 4:其他, 5:算法工程师)' AFTER `job_name`;

ALTER TABLE `bug_manage` ADD COLUMN `bug_no` int(6) DEFAULT NULL COMMENT 'bug编号';
ALTER TABLE `bug_manage` ADD COLUMN `is_delete`  int(1) DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)' AFTER `bug_no`;


