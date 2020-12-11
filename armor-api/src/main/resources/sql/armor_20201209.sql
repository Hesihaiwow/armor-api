-- 创建人 史长浩
-- 说明: 个人任务多级审核
-- 上线时间 2020-12-17

INSERT INTO `mantis_category` (`id`, `category_id`, `category_name`, `user_id`, `project_id`, `sys_user_id`,`user_name`)
VALUES ('518255246414184448', '100', '全学科题库', '56', '57', '87527108896620544', '薛亮亮');

ALTER TABLE `task_temp`
    MODIFY COLUMN `task_id` bigint(20) NULL COMMENT '任务id',
    ADD COLUMN `link_task_id` bigint(20) NULL COMMENT '关联任务id(个人任务用)',
    ADD COLUMN `project_id`   bigint(20) NULL COMMENT '项目id(个人任务用)',
    ADD COLUMN `task_name`  varchar(256) NULL COMMENT '任务名称(个人任务用)';

ALTER TABLE `task_review_log`
MODIFY COLUMN `task_id`  bigint(20) NULL COMMENT '任务id';



CREATE TABLE `task_temp_tag`
(
    `id`     bigint(20) NOT NULL COMMENT '主键',
    `tt_id`  bigint(20) NOT NULL COMMENT '临时任务id',
    `tag_id` bigint(20) NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `tt_tag_index` (`tt_id`, `tag_id`) COMMENT '临时任务_标签唯一标签'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='临时任务_标签表(个人任务用)';



