/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50711
Source Host           : 127.0.0.1:3306
Source Database       : library

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2018-05-22 19:52:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_author
-- ----------------------------
DROP TABLE IF EXISTS `t_author`;
CREATE TABLE `t_author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_active` int(11) NOT NULL,
  `birth_place` varchar(255) DEFAULT NULL,
  `birth_year` int(11) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `created_time` datetime NOT NULL,
  `duties` varchar(255) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `ip` int(11) DEFAULT NULL,
  `job` varchar(255) DEFAULT NULL,
  `modified_time` datetime NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_author
-- ----------------------------

-- ----------------------------
-- Table structure for t_card
-- ----------------------------
DROP TABLE IF EXISTS `t_card`;
CREATE TABLE `t_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_active` int(11) NOT NULL,
  `created_time` datetime NOT NULL,
  `modified_time` datetime NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `sno` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_card
-- ----------------------------
INSERT INTO `t_card` VALUES ('1', '1', '2018-05-22 17:48:00', '2018-05-22 17:48:00', '蔡大哥大晒', '342401bf3afbe8b5f7c742f99b4759bf4a21a933', '3114008098');
INSERT INTO `t_card` VALUES ('2', '1', '2018-05-22 17:58:06', '2018-05-22 17:58:06', '德顺', '4f44aef6098645423c025ae3c3874e7fede7eba0', '3114009999');

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1024) NOT NULL DEFAULT '',
  `permit` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', '编辑用户', 'user:update');
INSERT INTO `t_permission` VALUES ('2', '删除用户', 'user:delete');
INSERT INTO `t_permission` VALUES ('3', '新增用户', 'user:add');
INSERT INTO `t_permission` VALUES ('4', '查看用户', 'user:show');
INSERT INTO `t_permission` VALUES ('5', '新增企业资讯', 'info:add');
INSERT INTO `t_permission` VALUES ('6', '删除企业资讯', 'info:delete');
INSERT INTO `t_permission` VALUES ('7', '更新企业资讯', 'info:update');
INSERT INTO `t_permission` VALUES ('8', '统计模块查看', 'statistics:show');
INSERT INTO `t_permission` VALUES ('9', '查看客户信息', 'customer:show');
INSERT INTO `t_permission` VALUES ('10', '客户信息修改', 'customer:update');
INSERT INTO `t_permission` VALUES ('11', '删除客户/冻结', 'customer:delete');
INSERT INTO `t_permission` VALUES ('12', '新增客户，帮客户注册', 'customer:add');
INSERT INTO `t_permission` VALUES ('13', '专著查看权限', 'treatise:show');
INSERT INTO `t_permission` VALUES ('14', '专著新增权限', 'treatise:add');
INSERT INTO `t_permission` VALUES ('15', '专著修改权', 'treatise:update');
INSERT INTO `t_permission` VALUES ('16', '专著删除权', 'treatise:delete');

-- ----------------------------
-- Table structure for t_record
-- ----------------------------
DROP TABLE IF EXISTS `t_record`;
CREATE TABLE `t_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(255) NOT NULL,
  `book_name` varchar(255) DEFAULT NULL,
  `created_time` datetime NOT NULL,
  `modified_time` datetime DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `sno` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_record
-- ----------------------------
INSERT INTO `t_record` VALUES ('1', '666666', '广东工业大学666', '2018-05-22 19:45:34', null, 'GGGGG', '3114008098');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1024) NOT NULL DEFAULT '',
  `name` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '管理员', 'admin');
INSERT INTO `t_role` VALUES ('2', '超级管理员', 'super');
INSERT INTO `t_role` VALUES ('4', '接待员/前台人员/收银员', 'receptionist');
INSERT INTO `t_role` VALUES ('5', '编写人员/资讯编辑/资料收录', 'editor');
INSERT INTO `t_role` VALUES ('6', '专著作者', 'author');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  KEY `FKjobmrl6dorhlfite4u34hciik` (`permission_id`),
  KEY `FK90j038mnbnthgkc17mqnoilu9` (`role_id`),
  CONSTRAINT `FK90j038mnbnthgkc17mqnoilu9` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FKjobmrl6dorhlfite4u34hciik` FOREIGN KEY (`permission_id`) REFERENCES `t_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES ('5', '5');
INSERT INTO `t_role_permission` VALUES ('5', '6');
INSERT INTO `t_role_permission` VALUES ('5', '7');
INSERT INTO `t_role_permission` VALUES ('5', '8');
INSERT INTO `t_role_permission` VALUES ('4', '9');
INSERT INTO `t_role_permission` VALUES ('2', '1');
INSERT INTO `t_role_permission` VALUES ('2', '2');
INSERT INTO `t_role_permission` VALUES ('2', '3');
INSERT INTO `t_role_permission` VALUES ('2', '4');
INSERT INTO `t_role_permission` VALUES ('2', '5');
INSERT INTO `t_role_permission` VALUES ('2', '6');
INSERT INTO `t_role_permission` VALUES ('2', '7');
INSERT INTO `t_role_permission` VALUES ('2', '8');
INSERT INTO `t_role_permission` VALUES ('2', '9');
INSERT INTO `t_role_permission` VALUES ('2', '10');
INSERT INTO `t_role_permission` VALUES ('2', '11');
INSERT INTO `t_role_permission` VALUES ('2', '12');
INSERT INTO `t_role_permission` VALUES ('1', '1');
INSERT INTO `t_role_permission` VALUES ('1', '2');
INSERT INTO `t_role_permission` VALUES ('1', '3');
INSERT INTO `t_role_permission` VALUES ('1', '4');
INSERT INTO `t_role_permission` VALUES ('1', '5');
INSERT INTO `t_role_permission` VALUES ('1', '6');
INSERT INTO `t_role_permission` VALUES ('1', '7');
INSERT INTO `t_role_permission` VALUES ('1', '8');
INSERT INTO `t_role_permission` VALUES ('1', '9');
INSERT INTO `t_role_permission` VALUES ('1', '10');
INSERT INTO `t_role_permission` VALUES ('1', '11');
INSERT INTO `t_role_permission` VALUES ('1', '12');
INSERT INTO `t_role_permission` VALUES ('1', '13');
INSERT INTO `t_role_permission` VALUES ('1', '14');
INSERT INTO `t_role_permission` VALUES ('1', '15');
INSERT INTO `t_role_permission` VALUES ('1', '16');

-- ----------------------------
-- Table structure for t_treatise
-- ----------------------------
DROP TABLE IF EXISTS `t_treatise`;
CREATE TABLE `t_treatise` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(40) NOT NULL,
  `book_name` varchar(40) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `no` varchar(40) DEFAULT NULL,
  `page_number` int(11) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `publish_date` date DEFAULT NULL,
  `publish_house` varchar(40) DEFAULT NULL,
  `publish_place` varchar(40) DEFAULT NULL,
  `words` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_treatise
-- ----------------------------
INSERT INTO `t_treatise` VALUES ('1', '66666', '广东工业大学666', '广东工业大学666广东工业大学666广东工业大学666', '666', '666', '666', '2018-05-22', '广东工业大学', '广东工业大学', '666');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_active` tinyint(6) NOT NULL,
  `created_time` datetime NOT NULL,
  `description` varchar(1024) DEFAULT '',
  `modified_time` datetime NOT NULL,
  `nickname` varchar(16) NOT NULL,
  `password` varchar(40) NOT NULL,
  `username` varchar(16) NOT NULL,
  `telephone` varchar(32) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `username_index` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '1', '2017-01-31 23:13:55', '就是我', '2017-02-05 15:15:55', 'Cherish', '342401bf3afbe8b5f7c742f99b4759bf4a21a933', 'cherish', null, null);
INSERT INTO `t_user` VALUES ('8', '1', '2017-04-16 13:32:44', '科技厅管理员', '2017-05-29 22:06:01', '小林', '46cc50571a16c00fb376518299fc19059fddb399', 'adminadmin', null, null);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKa9c8iiy6ut0gnx491fqx4pxam` (`role_id`),
  KEY `FKq5un6x7ecoef5w1n39cop66kl` (`user_id`),
  CONSTRAINT `FKa9c8iiy6ut0gnx491fqx4pxam` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FKq5un6x7ecoef5w1n39cop66kl` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1');
INSERT INTO `t_user_role` VALUES ('1', '2');
INSERT INTO `t_user_role` VALUES ('8', '1');
