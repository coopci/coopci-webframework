CREATE TABLE `user` (
   `id` bigint(11) NOT NULL AUTO_INCREMENT,
   `name` varchar(45) NOT NULL,
   `phone` varchar(45) NOT NULL DEFAULT '',
   `email` varchar(45) NOT NULL DEFAULT '',
   `itime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `utime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `password` varchar(128) NOT NULL DEFAULT '' COMMENT 'hash过的。',
   `nickname` varchar(45) NOT NULL DEFAULT '',
   `avatar` varchar(45) NOT NULL DEFAULT '',
   `enabled` tinyint(2) NOT NULL DEFAULT '1',
   PRIMARY KEY (`id`),
   UNIQUE KEY `user_name` (`name`)
 ) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='用户。';
 
 create or replace view v_user as
 select * from user;
 