-- 创建人 史长浩
-- 说明: 组织结构新增"工作组"  用户表新增字段
-- 上线时间 2019-12-12

CREATE TABLE `work_group` (
  `id` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '团队名称',
  `description` varchar(200) DEFAULT NULL COMMENT '简介',
  `leader` bigint(20) NOT NULL COMMENT '负责人id',
  `is_delete` int(1) NOT NULL COMMENT '状态 0:有效 1:删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_by`  bigint(20) NOT NULL COMMENT '创建人',
  `update_by`  bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_index` (`name`) COMMENT '名称唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='团队';


ALTER TABLE `user`
ADD COLUMN `group_id`  bigint(20) NULL DEFAULT NULL COMMENT '团队id' AFTER `department_id`;




