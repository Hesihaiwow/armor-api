-- 创建人 史长浩
-- 说明: 发版计划新增字段
-- 上线时间 2020-03-09

ALTER TABLE `week_publish_plan`
ADD COLUMN `special_test_time`  varchar(100) NULL DEFAULT NULL COMMENT '专项测试时间' AFTER `can_online`,
ADD COLUMN `zujuan`  varchar(100) NULL DEFAULT NULL COMMENT '组卷' AFTER `special_test_time`,
ADD COLUMN `yuejuan`  varchar(100) NULL DEFAULT NULL COMMENT '阅卷' AFTER `zujuan`,
ADD COLUMN `saomiao`  varchar(100) NULL DEFAULT NULL COMMENT '扫描上传' AFTER `yuejuan`,
ADD COLUMN `xueyebaogao`  varchar(100) NULL DEFAULT NULL COMMENT '学业报告' AFTER `saomiao`,
ADD COLUMN `chanpin`  varchar(100) NULL DEFAULT NULL COMMENT '产品' AFTER `xueyebaogao`,
ADD COLUMN `real_test_time`  varchar(100) NULL DEFAULT NULL COMMENT '实际测试时间' AFTER `chanpin`,
ADD COLUMN `online_time`  varchar(100) NULL DEFAULT NULL COMMENT '预估上线时间' AFTER `real_test_time`,
ADD COLUMN `real_online_time`  varchar(100) NULL DEFAULT NULL COMMENT '实际上线时间' AFTER `online_time`;



