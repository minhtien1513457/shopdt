use shopdt_api;

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` tinytext,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT 'use to call api',
  `email` varchar(255) NOT NULL COMMENT 'send username, access_key to this email',
  `password` varchar(255) NOT NULL,
  `role_id`  int(11) UNSIGNED NOT NULL,
  `status` tinyint(2) UNSIGNED NOT NULL  COMMENT '1: Active, 0: Inactive',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_user_2_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  UNIQUE KEY `account_unique` (`username`),
  UNIQUE KEY `email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `os` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` tinytext,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `type` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` tinytext,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` tinytext,
  `status` tinyint(4) DEFAULT NULL,
  `price`   bigint NOT NULL,
  `os_id`  int(11) UNSIGNED NOT NULL,
  `type_id`  int(11) UNSIGNED NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_product_2_os` FOREIGN KEY (`os_id`) REFERENCES `os` (`id`),
  CONSTRAINT `FK_product_2_type` FOREIGN KEY (`type_id`) REFERENCES `type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `gallery` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id`  int(11) UNSIGNED NOT NULL,
  `img_url` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_gallery_2_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `action` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` tinytext,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `action_product` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id` int(11) UNSIGNED NOT NULL,
  `action_id` int(11) UNSIGNED NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_action_product_2_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_action_product_2_action` FOREIGN KEY (`action_id`) REFERENCES `action` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `review` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`  int(11) UNSIGNED NOT NULL,
  `product_id`  int(11) UNSIGNED NOT NULL,
  `content` varchar(255) NOT NULL,
  `star`  int(11) UNSIGNED,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_review_2_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
  CONSTRAINT `FK_review_2_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `cart` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`  int(11) UNSIGNED NOT NULL,
  `amount`  int(11) UNSIGNED,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_cart_2_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `cart_product` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id` int(11) UNSIGNED NOT NULL,
  `cart_id` int(11) UNSIGNED NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_cart_product_2_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_cart_product_2_cart` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `historybuy` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id`  int(11) UNSIGNED NOT NULL,
  `total`  int(11) UNSIGNED,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `created_user` varchar(255) NOT NULL,
  `updated_user` varchar(255) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT NULL,
  `deleted_at` datetime NULL DEFAULT NULL,
  `deleted_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_historybuy_2_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `historybuy_product` (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_id` int(11) UNSIGNED NOT NULL,
  `historybuy_id` int(11) UNSIGNED NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_historybuy_product_2_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_historybuy_product_2_historybuy` FOREIGN KEY (`historybuy_id`) REFERENCES `historybuy` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;