
--
-- Table structure for table `images`
--

DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `post_id` bigint(20) NOT NULL,
  `image_name` varchar(255)  DEFAULT NULL,
  `thumbnail_filename` varchar(255)  DEFAULT NULL,
  `filename` varchar(255)  DEFAULT NULL,
  `content_type` varchar(50)  DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `thumbnail_size` bigint(20) DEFAULT NULL,
  `datetime_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`image_id`)
) ;


--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission` varchar(80)  DEFAULT NULL,
  `role_name` varchar(45)  DEFAULT NULL,
  PRIMARY KEY (`role_id`)
);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

--
-- Table structure for table `user_social`
--

DROP TABLE IF EXISTS `user_social`;
CREATE TABLE `user_social` (
  `social_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50)  NOT NULL,
  `provider_id` varchar(25)  NOT NULL,
  `provider_userid` varchar(60)  NOT NULL DEFAULT '',
  `screen_name` varchar(50)  DEFAULT NULL,
  `display_name` varchar(255)  DEFAULT NULL,
  `profile_url` varchar(512)  DEFAULT NULL,
  `image_url` varchar(512)  DEFAULT NULL,
  `access_token` varchar(255)  NOT NULL,
  `secret` varchar(255)  DEFAULT NULL,
  `refresh_token` varchar(255)  DEFAULT NULL,
  `expire_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`social_id`)
);

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50)  NOT NULL,
  `email` varchar(150)  NOT NULL,
  `first_name` varchar(40)  NOT NULL,
  `last_name` varchar(40)  NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `account_expired` tinyint(1) NOT NULL DEFAULT '0',
  `account_locked` tinyint(1) NOT NULL DEFAULT '0',
  `credentials_expired` tinyint(1) NOT NULL DEFAULT '0',
  `has_avatar` tinyint(1) NOT NULL DEFAULT '0',
  `user_key` varchar(25) NOT NULL DEFAULT '0000000000000000',
  `provider_id` varchar(25) NOT NULL DEFAULT 'SITE',
  `PASSWORD` varchar(255) NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`)
);

INSERT INTO roles (role_id, permission, role_name) VALUES (1, 'nixmash:all', 'admin');
INSERT INTO roles (role_id, permission, role_name) VALUES (2, 'nixmash:view', 'user');

INSERT INTO users (user_id, username, email, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id, PASSWORD, version) VALUES (1, 'bob', 'bob@aol.com', 'Bob', 'Planter', 1, 0, 0, 0, 0, '0000000000000000', 'SITE', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 0);
INSERT INTO users (user_id, username, email, first_name, last_name, enabled, account_expired, account_locked, credentials_expired, has_avatar, user_key, provider_id, PASSWORD, version) VALUES (2, 'ken', 'ken@aol.com', 'Ken', 'Stark', 1, 0, 0, 0, 0, '0000000000000000', 'SITE', 'a4e63bcacf6c172ad84f9f4523c8f1acaf33676fa76d3258c67b7e7bbf16d777', 0);

INSERT INTO user_roles (id, user_id, role_id) VALUES (1, 1, 1);
INSERT INTO user_roles (id, user_id, role_id) VALUES (2, 1, 2);
INSERT INTO user_roles (id, user_id, role_id) VALUES (3, 2, 2);

