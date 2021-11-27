# Parkar

2019软件工程项目-停车场管理

## 注意事项

* **请在提交代码时将spring.profiles.active改为production**
  * 涉及项目端口、数据库的信息，不要写在production当中
  * 如非必要，不要改动production配置文件

* 数据库内容请写在database/模块名/base.sql当中
  * 注意建表的顺序，如果有外键，则需要把被依赖的表先建立
  * 建议使用alter改动表，并在base.sql更新相应操作
  * conf文件夹内对应mysql的配置文件
  * 所有的3306端口均已打开，用户名密码和application.yml中一致
  
* JWTUtil已全部配置完成，可以直接使用：

```java
JWTUtil.Sign(userID)//传入用户ID，得到对应的Token
JWTUtil.check(token)//传入token，返回用户名，如果无效，返回空
JWTUtil.checkAdmin(token)//传入token，判断是否为admin，返回布尔值
```

* 数据库部分按照service-user下中的[README.md](./service-user/README.md)进行相关设计。