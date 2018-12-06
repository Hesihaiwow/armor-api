/**
feedback表更新字段
 */
  ALTER TABLE feedback ADD COLUMN target varchar(200) DEFAULT NULL COMMENT '达成的目标';
  ALTER TABLE feedback ADD COLUMN release_time datetime DEFAULT NULL COMMENT '期待上线时间';
  ALTER TABLE feedback ADD COLUMN question varchar(200) DEFAULT NULL COMMENT '需要解决什么问题';
  ALTER TABLE feedback ADD COLUMN type int(1) DEFAULT NULL COMMENT '0:个人建议,1:市场反馈,2:公司决策';
  ALTER TABLE feedback ADD COLUMN likes_num int(10) DEFAULT '0' COMMENT '点赞总数';
  ALTER TABLE feedback ADD COLUMN online_time datetime DEFAULT NULL COMMENT '上线时间';
  ALTER TABLE feedback ADD COLUMN coach_id int(10) DEFAULT NULL COMMENT '学管端需求创建者id';
  ALTER TABLE feedback MODIFY project_id bigint(20) DEFAULT NULL COMMENT '项目id';
  ALTER TABLE feedback MODIFY create_by bigint(20) DEFAULT NULL COMMENT '任务系统需求创建者id';
  ALTER TABLE feedback MODIFY content text DEFAULT NULL COMMENT '需求反馈内容';
/**
新建表feedback_accessory
 */
DROP TABLE IF EXISTS `feedback_accessory`;
CREATE TABLE `feedback_accessory` (
  `id` bigint(20) NOT NULL COMMENT '需求附件ID',
  `feedback_id` bigint(20) NOT NULL COMMENT '需求ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `url` text NOT NULL COMMENT '附件地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求附件表';

/**
新建表feedback_likes
 */
DROP TABLE IF EXISTS `feedback_likes`;
CREATE TABLE `feedback_likes` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '任务系统需求创建者id',
  `feedback_id` bigint(20) NOT NULL COMMENT '需求ID',
  `create_time` datetime NOT NULL COMMENT '点赞时间',
  `coach_id` int(10) DEFAULT NULL COMMENT '学管端需求点赞者id',
  `like_people` varchar(20) DEFAULT NULL COMMENT '点赞人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求点赞表';

/**
新建表feedback_queue
 */
 DROP TABLE IF EXISTS `feedback_queue`;
CREATE TABLE `feedback_queue` (
  `id` bigint(20) NOT NULL COMMENT 'ID，主键',
  `demand_id` bigint(20) NOT NULL COMMENT '需求ID',
  `agree_user_id` bigint(20) NOT NULL COMMENT '采纳人id',
  `agree_time` datetime NOT NULL COMMENT '采纳时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='需求排队表';

/*
新建表feedback_read
 */
DROP TABLE IF EXISTS `feedback_read`;
CREATE TABLE `feedback_read` (
  `id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL COMMENT '需求ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '任务系统需求回复者id',
  `read_time` datetime NOT NULL COMMENT '阅读时间',
  `coach_id` int(10) DEFAULT NULL COMMENT '学管端需求阅读者id',
  `read_people` varchar(20) DEFAULT NULL COMMENT '已读人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求已读表';

/**
新建表feedback_rejected
 */
DROP TABLE IF EXISTS `feedback_rejected`;
CREATE TABLE `feedback_rejected` (
  `id` bigint(20) NOT NULL COMMENT 'ID，主键',
  `demand_id` bigint(20) NOT NULL COMMENT '需求ID',
  `reject_user_id` bigint(20) NOT NULL COMMENT '驳回人id',
  `reject_time` datetime NOT NULL COMMENT '驳回时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='需求驳回表';

/**
新建表feedback_reply
 */
DROP TABLE IF EXISTS `feedback_reply`;
CREATE TABLE `feedback_reply` (
  `id` bigint(20) NOT NULL,
  `feedback_id` bigint(20) NOT NULL COMMENT '需求ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '任务系统回复人id',
  `content` varchar(500) NOT NULL COMMENT '回复内容',
  `reply_time` datetime NOT NULL COMMENT '回复时间',
  `coach_id` int(10) DEFAULT NULL COMMENT '学管端需求回复者id',
  `reply_people` varchar(20) DEFAULT NULL COMMENT '回复人姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需求回复表';