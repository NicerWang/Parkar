
# Service-Admin

## All DataBase Manipulations:
```sql
create database parkar;

use parkar;

create table admin(
id int AUTO_INCREMENT PRIMARY KEY ,
name varchar(255) UNIQUE,
pwd varchar(255)
);

insert into admin value(null,"nicer","11111111");
```

##  API Reference:
run and visit:

/swagger-ui.html