数据库建表语句

##### 用户表
CREATE TABLE USER(
id VARCHAR(50) PRIMARY KEY,
username VARCHAR(30),
password VARCHAR(100),
sex VARCHAR(20),
phone VARCHAR(20),
address VARCHAR(100),
balance INT,
is_disabled INT,
register_time DATETIME,
last_login_time DATETIME
);

##### 车牌表
CREATE TABLE vehicle(
vehicle_id VARCHAR(50) PRIMARY KEY,
user_id VARCHAR(50)
);

使用swagger进行接口测试，访问http://localhost:8080/swagger-ui.html
