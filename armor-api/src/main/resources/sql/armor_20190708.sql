ALTER TABLE `task_temp` ADD COLUMN `task_level` int(1) DEFAULT NULL COMMENT '任务级别';

ALTER TABLE `task_user` ADD COLUMN `task_level` int(1) DEFAULT NULL COMMENT '任务级别';

ALTER TABLE `user` ADD COLUMN `level` int(5) DEFAULT NULL COMMENT '级别';