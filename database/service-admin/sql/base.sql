CREATE TABLE `admin` (
         id int AUTO_INCREMENT PRIMARY KEY ,
         name varchar(255) UNIQUE,
         pwd varchar(255)
);
CREATE TABLE data(
         time DATETIME,
         type int,
         userID varchar(255),
         sa1 varchar(255),
         sa2 varchar(255),
         sa3 varchar(255),
         sa4 varchar(255)
);

