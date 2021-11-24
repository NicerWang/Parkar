# Service-Management


### ALL DataBase Manipulations:

```mysql
CREATE TABLE `parking_space` (
  `space_id` int NOT NULL AUTO_INCREMENT,
  `occupied` tinyint NOT NULL DEFAULT '0',
  `mode` int DEFAULT '0',
  `ban` tinyint DEFAULT '0',
  PRIMARY KEY (`space_id`)
) ;
```

```mysql
CREATE TABLE `parking_time` (
  `time_id` int NOT NULL AUTO_INCREMENT,
  `space_id` int DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`time_id`),
  KEY `parking_space_id_idx` (`space_id`),
  CONSTRAINT `parking_space_id` FOREIGN KEY (`space_id`) REFERENCES `parking_space` (`space_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;
```

```mysql
CREATE TABLE `parking_order` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
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
```





### API Reference:

run and visit:

/swagger-ui.html

特殊情况：
管理员更改车位信息时(put:administrator/parking/space/{spaceId}),除车位Id以外的参数以传统方式进行添加
如 ../administrator/parking/space/{spaceId}?ban=1
   ../administrator/parking/space/{spaceId}?occupied=1&mode=2
   
11.6新增api(Get): /machine/elevator/space/{licenseNumber} 传车牌号查最近的已预约车位及时间
    变更api(Post):/order/{mode}/{spaceId}/{startTime}/{endTime}  ==> /order/{mode}/{spaceId}/{startTime}/{endTime}/{licenseNumber}
                eg:/order/day/13/1636244716/1636266316/AE86

11.24修复api(Get): /machine/elevator/space/{licenseNumber}
    返回值变更:
        删除:`startTime`、`endTime`;
        新增:`currentOrder`、`laterOrder`、`hasExpired`;
        变更:`space`;
    描述:
        `currentOrder`:最早的 结束时间晚于当前时间的订单，可能已经超期30分钟;
        `hasExpired`:currentOrder超期30分钟时 hasExpired为true，否则为false;
        `laterOrder`:currentOrder超期30分钟并且有后续订单时值为 最早的后续订单， 其他情况为null;
        `space`:最早可用(已经生效且未超期30分钟、尚未生效的)预约订单的车位;
        原本`startTime`、`endTime`的信息现包含在`currentOrder`、`laterOrder`中。