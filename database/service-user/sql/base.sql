CREATE TABLE user(
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