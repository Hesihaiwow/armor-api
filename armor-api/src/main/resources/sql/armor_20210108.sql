CREATE TABLE if not exists `oranganization` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `org_name` varchar(20) NOT NULL COMMENT '机构名称',
    `description` varchar(200) DEFAULT NULL COMMENT '机构简介',
    `is_delete` int(1) NOT NULL COMMENT '状态 0:有效 1:删除',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构';

#1
ALTER TABLE bug_manage ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#5
ALTER TABLE bug_user ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#6
ALTER TABLE department ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#7
ALTER TABLE extra_work ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#8
ALTER TABLE extra_work_task ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#9
ALTER TABLE feedback ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#10
ALTER TABLE feedback_likes ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#15
ALTER TABLE feedback_plan ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#12
ALTER TABLE feedback_queue ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#14
ALTER TABLE feedback_read ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#13
ALTER TABLE feedback_rejected ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#11
ALTER TABLE feedback_reply ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#2
ALTER TABLE mantis_bug_statistics ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#4
ALTER TABLE mantis_category ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#3
ALTER TABLE mantis_user ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#17
ALTER TABLE notification ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#18
ALTER TABLE online_question_record ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#19
ALTER TABLE permission  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#20
ALTER TABLE platform ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#21
ALTER TABLE project ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#22
ALTER TABLE publish_info ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#25
ALTER TABLE resign_in ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#24
ALTER TABLE sign_in ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#25
ALTER TABLE tag ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#31
ALTER TABLE task ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#26
ALTER TABLE task_bug ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#27
ALTER TABLE task_evaluation ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#28
ALTER TABLE task_expand ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#29
ALTER TABLE task_function ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#30
ALTER TABLE task_log ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#31
ALTER TABLE task_modify_function  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#32
ALTER TABLE task_modify_table ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#33
ALTER TABLE task_modify_user_week  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#34
ALTER TABLE task_review ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#39
ALTER TABLE task_review_log ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#35
ALTER TABLE task_summary ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#36
ALTER TABLE task_tag  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#38
ALTER TABLE task_temp ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#37
ALTER TABLE task_temp_function  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#16
ALTER TABLE task_temp_module ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#40
ALTER TABLE task_temp_tag  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#41
ALTER TABLE task_test  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#42
ALTER TABLE task_user ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#43
ALTER TABLE test_example ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#44
ALTER TABLE test_function ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#48
ALTER TABLE `user` ADD ( org_id BIGINT (20) DEFAULT 1 COMMENT '机构ID', is_admin INT (1) DEFAULT 0 COMMENT '是否机构管理员:0:否，1:是' );
#49
ALTER TABLE user_check_people ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#45
ALTER TABLE user_group ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#46
ALTER TABLE user_integral ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#47
ALTER TABLE user_leave ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#23
ALTER TABLE user_rest_hours_log ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#24
ALTER TABLE user_sort  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#48
ALTER TABLE user_task_integral  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#49
ALTER TABLE user_week ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#38
ALTER TABLE user_week_temp ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#50
ALTER TABLE week_publish_plan ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#51
ALTER TABLE week_publish_plan_platform  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#52
ALTER TABLE week_publish_plan_platform_user  ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#53
ALTER TABLE week_publish_plan_task ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');
#54
ALTER TABLE work_group ADD (org_id BIGINT (20) default 1 COMMENT '机构ID');


## 插入管理员
INSERT INTO `user`(`id`, `name`, `account`, `password`, `phone`, `last_login`, `department_id`, `job_name`, `job_role`, `integral`, `status`, `create_time`, `is_delete`, `user_role`, `avatar_url`, `email`, `check_sort`, `level`, `job_number`, `rest_hours`, `org_id`, `is_admin`) VALUES (532140783038365696, 'admin', 'admin', '4fedbc5f700f9e9cfcd2d27f56db82e8', '16602146623', NULL, 110, '其他', 10, 0.0, 0, '2021-01-08 18:17:29', 0, '0', NULL, '11111111@qq.com', 123456, 9, '123456', 0.0, 110, 1);

## 插入机构
INSERT INTO `oranganization`(`id`, `org_name`, `description`,  `is_delete`, `create_time`,user_id) VALUES (1, '上海互教信息技术中心', '上海互教信息技术中心', 0, '2017-08-09 14:50:08','218260524016599040');