-- 创建人 史长浩
-- 说明: 任务新增 "关联任务" 字段
-- 上线时间 2020-09-10

ALTER TABLE `task`
ADD COLUMN `link_task`  bigint(20) NULL COMMENT '关联任务,个人任务关联多人任务';

ALTER TABLE `task_bug`
ADD COLUMN `problem_type`  tinyint(3) NOT NULL DEFAULT 0 COMMENT '问题类型(0:代码错误, 1:设计缺陷, 2:标准规范, 3:界面优化, 4:其他)' AFTER `frequency`;

ALTER TABLE `platform`
MODIFY COLUMN `name`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台名称' AFTER `id`;

ALTER TABLE `bug_manage`
MODIFY COLUMN `account_info`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号信息' AFTER `origin`;











