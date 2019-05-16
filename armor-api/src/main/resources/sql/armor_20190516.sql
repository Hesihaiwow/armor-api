ALTER TABLE feedback add COLUMN source int(1) DEFAULT NULL COMMENT '来源(0:其他,1:直播,2:小程序,3:IMS,4:学管端)';
ALTER TABLE feedback add COLUMN charge_man BIGINT(20) DEFAULT NULL COMMENT '负责人';
ALTER TABLE feedback DROP COLUMN from_coach;