-- 创建人 史长浩
-- 说明: 线上bug调整
-- 上线时间 2020-07-09

ALTER TABLE `bug_manage`
ADD COLUMN `group_id`  bigint(20) NULL COMMENT '业务组' AFTER `is_delete`,
ADD COLUMN `affect_scope`  tinyint(3) NULL COMMENT '影响范围' AFTER `group_id`,
ADD COLUMN `year`  int(10) NULL COMMENT default 2019 '年份' AFTER `affect_scope`,
MODIFY COLUMN `create_time`  datetime NOT NULL COMMENT '创建时间' AFTER `description`,
MODIFY COLUMN `type`  int(1) NOT NULL DEFAULT 0 COMMENT '问题类型(0:bug,1:优化,2:协助)' AFTER `demand_system`,
MODIFY COLUMN `is_solved`  int(1) NOT NULL DEFAULT 0 COMMENT '是否解决(0:未解决,1:已解决,2:暂搁置)' AFTER `type`,
MODIFY COLUMN `discover_time`  datetime NOT NULL COMMENT '发现时间' AFTER `remark`,
MODIFY COLUMN `is_delete`  int(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)' AFTER `bug_no`;









