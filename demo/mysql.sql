CREATE DATABASE IF NOT EXISTS `sky_take_out`;

CREATE TABLE `address_book` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `user_id` bigint NOT NULL COMMENT 'User ID',
    `consignee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Consignee',
    `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT 'Gender',
    `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT 'Phone Number',
    `province_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Province Code',
    `province_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Province Name',
    `city_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'City Code',
    `city_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'City Name',
    `district_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'District Code',
    `district_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'District Name',
    `detail` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Detailed Address',
    `label` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Label',
    `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Default 0: No, 1: Yes',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Address Book';

CREATE TABLE `category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `type` int DEFAULT NULL COMMENT 'Type: 1 Dish Category, 2 Set Meal Category',
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'Category Name',
    `sort` int NOT NULL DEFAULT '0' COMMENT 'Order',
    `status` int DEFAULT NULL COMMENT 'Category Status 0: Disabled, 1: Enabled',
    `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    `create_user` bigint DEFAULT NULL COMMENT 'Created By',
    `update_user` bigint DEFAULT NULL COMMENT 'Updated By',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Dish and Set Meal Category';

CREATE TABLE `dish` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'Dish Name',
    `category_id` bigint NOT NULL COMMENT 'Dish Category ID',
    `price` decimal(10,2) DEFAULT NULL COMMENT 'Dish Price',
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Image',
    `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Description',
    `status` int DEFAULT '1' COMMENT '0: Not for Sale, 1: For Sale',
    `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    `create_user` bigint DEFAULT NULL COMMENT 'Created By',
    `update_user` bigint DEFAULT NULL COMMENT 'Updated By',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_dish_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Dish';

CREATE TABLE `dish_flavor` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `dish_id` bigint NOT NULL COMMENT 'Dish ID',
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'Flavor Name',
    `value` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Flavor Data List',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Dish Flavor Relation';

CREATE TABLE `employee` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'Name',
    `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'Username',
    `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT 'Password',
    `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phone Number',
    `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT 'Gender',
    `id_number` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT 'ID Number',
    `status` int NOT NULL DEFAULT '1' COMMENT 'Status 0: Disabled, 1: Enabled',
    `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    `create_user` bigint DEFAULT NULL COMMENT 'Created By',
    `update_user` bigint DEFAULT NULL COMMENT 'Updated By',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Employee Information';

CREATE TABLE `order_detail` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'Name',
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Image',
    `order_id` bigint NOT NULL COMMENT 'Order ID',
    `dish_id` bigint DEFAULT NULL COMMENT 'Dish ID',
    `setmeal_id` bigint DEFAULT NULL COMMENT 'Set Meal ID',
    `dish_flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Flavor',
    `number` int NOT NULL DEFAULT '1' COMMENT 'Quantity',
    `amount` decimal(10,2) NOT NULL COMMENT 'Amount',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Order Detail';

CREATE TABLE `orders` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Order Number',
    `status` int NOT NULL DEFAULT '1' COMMENT 'Order Status 1: Pending Payment, 2: Pending Order Acceptance, 3: Order Accepted, 4: In Delivery, 5: Completed, 6: Canceled, 7: Refunded',
    `user_id` bigint NOT NULL COMMENT 'User ID',
    `address_book_id` bigint NOT NULL COMMENT 'Address Book ID',
    `order_time` datetime NOT NULL COMMENT 'Order Time',
    `checkout_time` datetime DEFAULT NULL COMMENT 'Checkout Time',
    `pay_method` int NOT NULL DEFAULT '1' COMMENT 'Payment Method 1: WeChat, 2: Alipay',
    `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT 'Payment Status 0: Not Paid, 1: Paid, 2: Refunded',
    `amount` decimal(10,2) NOT NULL COMMENT 'Total Amount',
    `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'Remark',
    `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phone Number',
    `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Address',
    `user_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'User Name',
    `consignee` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'Consignee',
    `cancel_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Cancellation Reason',
    `rejection_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Rejection Reason',
    `cancel_time` datetime DEFAULT NULL COMMENT 'Cancellation Time',
    `estimated_delivery_time` datetime DEFAULT NULL COMMENT 'Estimated Delivery Time',
    `delivery_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Delivery Status 1: Immediate Delivery, 0: Scheduled Time',
    `delivery_time` datetime DEFAULT NULL COMMENT 'Delivery Time',
    `pack_amount` int DEFAULT NULL COMMENT 'Packaging Fee',
    `tableware_number` int DEFAULT NULL COMMENT 'Tableware Quantity',
    `tableware_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'Tableware Status 1: Provided per Dish, 0: Specific Quantity',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Order';

CREATE TABLE `setmeal` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `category_id` bigint NOT NULL COMMENT 'Dish Category ID',
    `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT 'Set Meal Name',
    `price` decimal(10,2) NOT NULL COMMENT 'Set Meal Price',
    `status` int DEFAULT '1' COMMENT 'Sale Status 0: Not For Sale, 1: For Sale',
    `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Description',
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Image',
    `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
    `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
    `create_user` bigint DEFAULT NULL COMMENT 'Created By',
    `update_user` bigint DEFAULT NULL COMMENT 'Updated By',
   PRIMARY KEY (`id`),
   UNIQUE KEY `idx_setmeal_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Set Meal';

CREATE TABLE `setmeal_dish` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `setmeal_id` bigint DEFAULT NULL COMMENT 'Set Meal ID',
    `dish_id` bigint DEFAULT NULL COMMENT 'Dish ID',
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'Dish Name (Redundant Field)',
    `price` decimal(10,2) DEFAULT NULL COMMENT 'Dish Price (Redundant Field)',
    `copies` int DEFAULT NULL COMMENT 'Number of Dishes',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Set Meal - Dish Relation';

CREATE TABLE `shopping_cart` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'Item Name',
    `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Image',
    `user_id` bigint NOT NULL COMMENT 'User ID',
    `dish_id` bigint DEFAULT NULL COMMENT 'Dish ID',
    `setmeal_id` bigint DEFAULT NULL COMMENT 'Set Meal ID',
    `dish_flavor` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Flavor',
    `number` int NOT NULL DEFAULT '1' COMMENT 'Quantity',
    `amount` decimal(10,2) NOT NULL COMMENT 'Amount',
    `create_time` datetime DEFAULT NULL COMMENT 'Creation Time',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='Shopping Cart';

CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `openid` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT 'WeChat User Unique Identifier',
    `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'Name',
    `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT 'Phone Number',
    `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT 'Gender',
    `id_number` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT 'ID Number',
    `avatar` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'Avatar',
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='User Information';