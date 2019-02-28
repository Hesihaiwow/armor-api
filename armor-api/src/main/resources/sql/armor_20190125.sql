ALTER TABLE bug_manage MODIFY project_id bigint(20) DEFAULT NULL
ALTER TABLE bug_manage MODIFY process_time datetime DEFAULT NULL COMMENT '处理时间'
ALTER TABLE bug_manage add COLUMN origin varchar(20) DEFAULT NULL COMMENT '反馈人'
ALTER TABLE bug_manage add COLUMN account_info varchar(100) DEFAULT NULL COMMENT '账号信息'
ALTER TABLE bug_manage add COLUMN demand_system varchar(100) DEFAULT NULL COMMENT '反馈系统'
ALTER TABLE bug_manage add COLUMN type int(1) DEFAULT NULL COMMENT '问题类型(0:bug,1:优化,2:协助)'
ALTER TABLE bug_manage add COLUMN is_solved int(1) DEFAULT NULL COMMENT '是否解决(0:未解决,1:已解决)'
ALTER TABLE bug_manage add COLUMN remark text DEFAULT NULL COMMENT '备注'