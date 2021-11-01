# Parkar

2019软件工程项目-停车场管理

## 注意事项

* JWTUtil已全部配置完成，可以直接使用：

```java
JWTUtil.Sign(userID)//传入用户ID，得到对应的Token
JWTUtil.check(token)//传入token，返回用户名，如果无效，返回空
JWTUtil.checkAdmin(token)//传入token，判断是否为admin，返回布尔值
```

* 数据库部分按照service-user下中的[README.md](\service-user\README.md)进行相关设计。