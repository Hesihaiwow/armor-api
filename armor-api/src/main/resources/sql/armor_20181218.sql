/**
新建表notification
 */
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `nid` bigint(20) NOT NULL,
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content` text COMMENT '通知内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `status` int(1) NOT NULL COMMENT '读取状态(0:未读,1:已读)',
  `read_time` datetime DEFAULT NULL COMMENT '查看时间',
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知表';

UPDATE  `user` set `phone` = '19921640091' WHERE `id` = '87526662798835712';
UPDATE  `user` set `phone` = '18321736459' WHERE `id` = '87527787501453312';
UPDATE  `user` set `phone` = '18521319042' WHERE `id` = '87528289618362368';
UPDATE  `user` set `phone` = '15000147360' WHERE `id` = '87530396006219776';
UPDATE  `user` set `phone` = '17602156039' WHERE `id` = '87530485927903232';
UPDATE  `user` set `phone` = '15301843461' WHERE `id` = '87530731638620160';
UPDATE  `user` set `phone` = '18616842272' WHERE `id` = '87531156026687488';
UPDATE  `user` set `phone` = '17721023505' WHERE `id` = '87531357244227584';
UPDATE  `user` set `phone` = '17621957395' WHERE `id` = '87531755485003776';
UPDATE  `user` set `phone` = '13309438708' WHERE `id` = '87532428385583104';
UPDATE  `user` set `phone` = '18521527591' WHERE `id` = '87532762684194816';
UPDATE  `user` set `phone` = '18755386300' WHERE `id` = '87532830363484160';
UPDATE  `user` set `phone` = '18237626570' WHERE `id` = '87533168982228992';
UPDATE  `user` set `phone` = '18335754357' WHERE `id` = '87533682247598080';
UPDATE  `user` set `phone` = '15801717059' WHERE `id` = '87728919079288832';
UPDATE  `user` set `phone` = '13916203262' WHERE `id` = '87813626194296832';
UPDATE  `user` set `phone` = '15026856180' WHERE `id` = '123370447415803904';
UPDATE  `user` set `phone` = '18721668232' WHERE `id` = '139964039693336576';
UPDATE  `user` set `phone` = '18335754357' WHERE `id` = '143972418166194176';
UPDATE  `user` set `phone` = '18410128096' WHERE `id` = '156652903669432320';
UPDATE  `user` set `phone` = '18772503354' WHERE `id` = '156653026986164224';
UPDATE  `user` set `phone` = '18210969468' WHERE `id` = '160262509436076032';
UPDATE  `user` set `phone` = '15262252256' WHERE `id` = '168998950504759296';
UPDATE  `user` set `phone` = '16621279008' WHERE `id` = '191861900223447040';
UPDATE  `user` set `phone` = '17621959956' WHERE `id` = '196456439735123968';
UPDATE  `user` set `phone` = '13052358968' WHERE `id` = '196456575370526720';
UPDATE  `user` set `phone` = '18552172468' WHERE `id` = '204123205546278912';
UPDATE  `user` set `phone` = '15695167751' WHERE `id` = '212042124990873600';
UPDATE  `user` set `phone` = '15301843461' WHERE `id` = '221511280991666176';
UPDATE  `user` set `phone` = '17521561573' WHERE `id` = '224360943839084544';
UPDATE  `user` set `phone` = '18221056106' WHERE `id` = '236792415577440256';
UPDATE  `user` set `phone` = '17821758082' WHERE `id` = '239300658539266048';
UPDATE  `user` set `phone` = '17682482142' WHERE `id` = '244291925341896704';
UPDATE  `user` set `phone` = '18711138641' WHERE `id` = '249827968401014784';
UPDATE  `user` set `phone` = '13764771995' WHERE `id` = '254505599532793856';
UPDATE  `user` set `phone` = '18533711533' WHERE `id` = '254505986750939136';
UPDATE  `user` set `phone` = '18721821958' WHERE `id` = '254509133313605632';
UPDATE  `user` set `phone` = '18621098028' WHERE `id` = '259631358857445376';