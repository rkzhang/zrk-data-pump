CREATE TABLE `data_sync` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(80) COLLATE utf8mb4_bin NOT NULL COMMENT '表名',
  `key_name` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'key',
  `key_value` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'value',
  `opt_type` varchar(10) COLLATE utf8mb4_bin NOT NULL COMMENT '操作类型',
  `action` varchar(20) COLLATE utf8mb4_bin NOT NULL DEFAULT 'default' COMMENT '执行动作',
  `execution_count` int(11) unsigned DEFAULT '0' COMMENT '执行次数',
  `next_execution_time` datetime NOT NULL COMMENT '下次执行时间',
  `priority` int(11) DEFAULT '0' COMMENT '优先级',
  `key_expression` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'redis的key组成表达式',
  `err_info` text COLLATE utf8mb4_bin COMMENT '错误信息',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `stat` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_datasync_nextexecutiontime` (`next_execution_time`,`priority`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;