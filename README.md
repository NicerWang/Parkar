### API



##### 参数描述:

* `{mode}` : 预约方式 可取值为`{day,month,year}` 对应短期、月租、年费，数据库中对应0,1,2
* `{spaceId}`: 车位id 目前存有数据`1-15`
* `{startTime}`: 查询、提交的时间段的起始时间 格式为UNIX时间戳，10位13位均可
* `{endTime}`: 查询、提交的时间段的结束时间 格式位UNIX时间戳， 10位13位均可
* `{userId}`: 用户id，暂未考虑过滤条件，目前任意纯数字均能通过



##### 路径：

* `ip:port/user/order/{mode}/time/{spaceId}`	  (Get)

```java
按车位id查询不可预约时段。
用于用户自主 先 选择车位时。
暂未知前端处理单个车位可预约时间段的显示方式，暂以不可预约时间段作为返回结果
返回值retMap:{
    "mode": {mode},
    "spaceId": {spaceId},
    "availableAnytime": false/true,//若没有不可预约时段  值为true
    "unavailableTimeList":[
        					{
                                //短期预约时 精确到分钟 月租、年费精确到日
                        		"startTime":"yyyy-MM-dd [hh:MM]?"，
                    			"endTime":"yyyy-MM-dd [hh:MM]?"
                            }
    					  ]
}
```

可能的[异常](#####异常)：[`IllegalArgumentException`](######`IllegalArgumentException`)





* `ip:port/user/order/{mode}/space/{startTime}/{endTime}`      (Get)

```java
按时间段查询可预约的车位。
startTime、endTime不可早于当前时间
用于用户自主 先 选择时间时。
返回值retMap:{
    "avaliableSpaceIdList":[Integer...],//在此时间段内可以预约的车位id
   	"startTime": {startTime},//格式为10位UNIX时间戳
    "endTime": {endTime}//格式位10位UNIX时间戳
}
```

可能的[异常](#####异常)：[`IllegalArgumentException`](######`IllegalArgumentException`)





* `ip:port/user/order/{mode}/{userId}/{spaceId}/{startTime}/{endTime}`    (Post)

```java
提交预约
返回值retMap:{
    //新插入的占用时段信息 反应数据库中真实情况，故startTime和endTime以日期 时间格式返回
    "newTime":{
        "timeId": Integer eg:14,
        "spaceId": Integer eg:12,
        "startTime": eg:"2021-11-02T10:30:00.000+08:00",
        "endTime": eg:"2021-11-02T15:02:00.000+08:00"
    }
    "newOrder":{
        "orderId": Integer eg:10,
        "userId": Integer eg:6598,
        "spaceId": Integer eg:12,
        "startTime": "2021-11-02T10:30:00.000+08:00",
        "endTime": "2021-11-02T15:02:00.000+08:00",
        "price": Bigdecimal eg:20.00,
        "mode": Integer eg:0,
        "paid": Byte eg:0
    }
}
```

可能的[异常](#####异常)：[`IllegalArgumentException`](######`IllegalArgumentException`)，[`TransactionException`](######`TransactionException`)









##### 异常

###### `IllegalArgumentException`

```java
HttpStatus:Bad Request;
状态码:400;
返回值retMap:{
    "illegalArguments":[...],//引发异常的非法参数
    "message":[String...],//对应非法参数的错误信息
    "requestPath":""//引发异常的请求路径模板 如/user/order/{mode}/time/{spaceId}
}
```

###### `TransactionException`

```java
HttpStatus:Internal Server Error
状态码:500;
返回值retMap:{
    "transactionName": eg:"insertOrderFromUser",//引发异常的业务
    "message": eg:[
        "the time period in the space has been occupied"
    ]//错误信息
}
```





##### 画好的饼

* `/user/order/{mode}/list`   (Get)  获取当前时刻可预约车位及其可预约时长
* `/administrator/parking/space/{spaceId}`   (Get)  查看指定车位信息
* `/administrator/parking/space/{spaceId}`   (Put)   释放指定车位
* `/administrator/parking/space/{spaceId}/ban`  (Put)   禁用指定车位
* `/administrator/parking/{spaceId}/{startTime}/{endTime}`  (Put) 指定车位指定已预约时段

* `/administrator/parking/time/{startTime}/{endTime}`  

  ​		(Get) 获取指定时段可预约(空闲)的车位   可以是历史时段