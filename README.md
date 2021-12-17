# Parkar

> 2019软件工程课程项目-停车场管理

项目共有6个模块，以下分别介绍。

## Adapter

> 基于`Spring Boot`，前端页面由`Thymeleaf`承载。

硬件适配器模块。

设计了一套通用的硬件接口(摄像头、传感器、电梯、操作面板)，并使用虚拟硬件对其进行了实现。

硬件可以与后台进行交互。

## Service-Admin

> 基于`Spring Boot`+`MyBatis`，使用`Restful API`，默认启用`swagger`

管理员服务模块。

* 面向管理员的功能：

  * 管理员登陆与密码修改

  * 定时`MapReuce`数据分析

* 面向用户的功能：

  * 数据收集

## Service-Management

> 基于`Spring Boot`+`MyBatis`，使用`Restful API`，默认启用`swagger`

车位管理系统。

* 面向用户的功能：

  * 价格查询

  * 查询(可用)车位

  * 提交订单、查询订单、延长订单、取消订单

  * 车位推荐

* 面向管理员的功能：
  * 查询所有订单、筛选订单、删除订单
  * 查询所有车位信息、增删车位、更新车位信息
  * 查询和设置价格

## Service-User

> 基于`Spring Boot`+`MyBatis Plus`，使用`Restful API`，默认启用`swagger`

用户管理系统。

* 面向用户的功能
  * 登陆注册
  * 更新信息
  * 增删车牌号
* 面向管理员的功能
  * 查询所有用户、删除用户

## Frontend-User

> 基于`vue3`+`vuex`+`vue-router`

用户前端。

* 通过响应式布局和`PWA`，支持了移动端。

  <img src="https://pictures-nicerwang-1256891306.cos.ap-beijing.myqcloud.com//imgIMG_0471%202.PNG" alt="IMG_0471 2" style="zoom:50%;" />

* 可视化车位选择。

* 页面渐变过渡、加载动画、权限管理。

## Frontend-Management

> 基于`vue3`+`vuex`+`vue-router`

管理员前端。

* 页面渐变过渡、加载动画、权限管理。

* 模糊查询

  ![截屏2021-12-17 上午10.52.44](https://pictures-nicerwang-1256891306.cos.ap-beijing.myqcloud.com//img%E6%88%AA%E5%B1%8F2021-12-17%20%E4%B8%8A%E5%8D%8810.52.44.png)

* 可视化车位选择。

  ![截屏2021-12-17 上午10.52.57](https://pictures-nicerwang-1256891306.cos.ap-beijing.myqcloud.com//img%E6%88%AA%E5%B1%8F2021-12-17%20%E4%B8%8A%E5%8D%8810.52.57.png)

* 基于`Echart`的数据展示

  ![截屏2021-12-17 上午10.52.30](https://pictures-nicerwang-1256891306.cos.ap-beijing.myqcloud.com//img%E6%88%AA%E5%B1%8F2021-12-17%20%E4%B8%8A%E5%8D%8810.52.30.png)

## 开发

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

  
