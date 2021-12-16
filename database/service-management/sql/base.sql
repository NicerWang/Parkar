CREATE TABLE `parking_space` (
                                 `space_id` int NOT NULL AUTO_INCREMENT,
                                 `occupied` tinyint NOT NULL DEFAULT '0',
                                 `mode` int DEFAULT '0',
                                 `ban` tinyint DEFAULT '0',
                                 `booked` tinyint DEFAULT '0',
                                 `floor` int DEFAULT '0',
                                 `x_coordinate` int DEFAULT '0',
                                 `y_coordinate` int DEFAULT '0',
                                 PRIMARY KEY (`space_id`)
) ;
CREATE TABLE `parking_time` (
    `time_id` int NOT NULL AUTO_INCREMENT,
    `space_id` int DEFAULT NULL,
    `start_time` timestamp NULL DEFAULT NULL,
    `end_time` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`time_id`),
    KEY `parking_space_id_idx` (`space_id`),
    CONSTRAINT `parking_space_id` FOREIGN KEY (`space_id`) REFERENCES `parking_space` (`space_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

CREATE TABLE `parking_order` (
     `order_id` int NOT NULL AUTO_INCREMENT,
     `user_id` varchar(255) DEFAULT NULL,
     `license_number` varchar(255) DEFAULT NULL,
     `space_id` int DEFAULT NULL,
     `start_time` timestamp NULL DEFAULT NULL,
     `end_time` timestamp NULL DEFAULT NULL,
     `price` decimal(10,2) DEFAULT NULL,
     `mode` int DEFAULT '0',
     `paid` tinyint DEFAULT '0',
     PRIMARY KEY (`order_id`),
     KEY `space_id_idx` (`space_id`),
     CONSTRAINT `space_id` FOREIGN KEY (`space_id`) REFERENCES `parking_space` (`space_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

create table parking_price
(
    `key`    varchar(255)      primary key,
    `value`   double               null
);
INSERT INTO parkar.parking_price (`key`, value) VALUES ('0', 1.8);
INSERT INTO parkar.parking_price (`key`, value) VALUES ('1', 188);
INSERT INTO parkar.parking_price (`key`, value) VALUES ('2', 888);