## Spring Cloud 学习Demo
> 该项目为学习尚硅谷周阳老师的SpringCloud微服务教程的学习项目，[参考视频](https://www.bilibili.com/video/BV1yE411x7Ky)

## 项目记录

* 使用 #bilibili# 标签，标注某部分代码出现在视频的第几P，从42P开始标记，之前都没有。
* 系统hosts映射如下
```
# Spring Cloud
127.0.0.1	eureka7001.com # Eureka服务器映射
127.0.0.1	eureka7002.com # Eureka集群2服务器映射
127.0.0.1	localhost
127.0.0.1	config3344.com # spring配置中心映射

# centos 7 - dev
192.168.56.101	centos-dev # 虚拟机映射，包括zookeeper、consul、RabbitMQ等服务器所在的虚拟机
```


### 1. 先创建provider-payment8001微服务
   1. pom依赖，加入springboot，mysql，mybatis，devtools（支持热部署，可选），lombok（简化开发）等依赖。
   2. 在application.yml中配置端口号，数据库相关配置，mybatis相关配置（比如mapper包位置，实体类位置）

### 2. 创建数据库payment{id，serial}

### 3. 创建consumer-order80微服务
   1. 利用RestTemplate调用8001端口微服务

### 4. 把实体类提出到api自己创建的包api-commons
   1. 先对api的maven进行clean
   2. 然后对其进行install
   3. 在其他用到实体类的地方的pom中引用该自定义包，见pom文件

### 5. 使用Eureka注册中心管理微服务地址
   1. 创建eureka-server7001微服务

   2. pom加入eureka-server相关依赖

      * ```xml
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
                </dependency>
        ```

   3. application.yml配置端口号和eureka

      * ``` yaml
        server:
          port: 7001
        eureka:
          instance:
            hostname: localhost
          client:
            #    不向注册中心注册自己
            register-with-eureka: false
            #    false表示自己是注册中心，维护实例，不需要检索服务
            fetch-registry: false
            service-url:
              defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
        ```

   4. Main类中，加@EnableEurekaServer

### 6. 其他微服务注册到Eureka
   1. 在需要的pom中加入Eureka的client包依赖（比如8001端口的微服务）
   2. 在8001端口等其他微服务的Main类中，加@EnableEurekaClient
   3. 先启动Eureka-server，再启动Eureka-Client（比如8001微服务），会发现在**http://localhost:7001/** 中有注册进去的微服务

### 7. Eureka集群部署
   1. 再创建一个eureka-server7002微服务，参口7001

   2. 改hosts，写7001，7002，分别映射localhost

      * ```
        # Spring Cloud
        127.0.0.1	eureka7001.com
        127.0.0.1	eureka7002.com
        ```

   3. 写YML，相互守望，相互注册

   4. 主启动

   5. 配置其他注册到里面的服务的YML，defaultZone设置为两个注册中心，用逗号隔开

### 8. 8001微服务集群搭建
   1. 创建与8001类似的微服务8002
   2. 添加pom
   3. 添加YML，修改端口号
   4. 测试：在controller层配置输出当前端口号，为后续负载均衡测试准备；在Eureka管理页面查看是否注册了多个
   5. 设置8001、8002服务名，在yml下添加如下内容

      + ```yaml
        eureka:
          instance:
            instance-id: payment8002 # 这里填写对应服务名
            prefer-ip-address: true # 配置显示ip
        ```

### 9. 使用RestTemplate：80端口调用8002端口微服务
   1. 在controller通过RestTemplate调用RestAPI时候，服务名代替URL硬编码
   2. 在80服务的容器配置中，为RestTemplate使用@LoadBalanced注解赋予RestTemplate负载均衡能力
   3. 启动8001，8002端口服务，再启动80端口服务
   4. 测试，http://localhost/order/consumer/payment/get/5，发现每次调用端口号不同

### 10.  配置SpringCloud的注册发现

1. 在8001上的controller中，注入discovery

   ```java
        @Resource//服务发现
        private DiscoveryClient discoveryClient;
   ```

2. 编写discovery方法
3. 8001主启动类，增加启动注册发现注解 **@EnableDiscoveryClient**

### 11. SpringCloud整合Zookeeper

1. 在CentOS 7 下载解压Zookeeper，复制conf/zoo_sample.cfg为zoo.cfg，关闭防火墙，通过./zkServer.sh start启动zk，通过./zkCli.sh来进入zk终端。

2. payment8004微服务

3. 写POM，YML，主启动类，Controller

   ```xml
   <!--  POM  -->
   <!--  添加zookeeper依赖-->
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
           </dependency>
   ```

   ```yaml
   # YML
   server:
     port: 8004
   spring:
     application:
       name: cloud-provider-payment
     cloud:
       zookeeper:
         connect-string: centos-dev:2181
   # 除此之外，需要配置DataSource，数据库账号密码，mybatis实体映射位置
   ```

4. 启动8004，注册到zookeeper，通过cli可以查看(ls/get命令)注册进去的节点和信息

5. 测试验证，通过写一个controller，查看是否成功返回


### 12. 写注册到Zookeeper的80端口consumer微服务

1. 创建cloud-consumerzk-order80微服务
2. pom(添加zookeeper官方依赖)、yml、主启动类
3. 写config配置RestTemplate，写controller添加测试方法（80端口通过resttemplate调用8004端口的服务）
4. 运行order微服务，查看虚拟机的zkCli中的服务是否注册成功

### 13. 安装Consul注册中心

1. 官网下载zip到linux，解压得到consul，可以放到/usr/bin目录方便使用
2. 执行：**consul agent -dev -node machine -client 0.0.0.0 -ui** 运行后，可以通过外部 **ip:8500** 访问web ui界面，注意：如果不加-client 0.0.0.0则不能被外部访问。

### 14. 创建8006端口provider-payment微服务，注册到Consul
1. 创建cloud-provider-payment在8006端口的微服务

2. 添加POM（consul），写yml（consul配置），如下

   * ```xml
             <!--        添加consul依赖-->
             <dependency>
                 <groupId>org.springframework.cloud</groupId>
                 <artifactId>spring-cloud-starter-consul-discovery</artifactId>
             </dependency>
     ```

   * ```yaml
     spring:
       application:
         name: cloud-provider-payment
       cloud:
         #添加consul配置
         consul:
           host: centos-dev
           port: 8500
           discovery:
             service-name: ${spring.application.name}
             heartbeat:
               enabled: true
     ```

3. 写主启动类，写controller，添加数据库相关依赖以及配置

4. 测试（通过consul Web UI查看是否注册成功、通过url访问能够返回结果）

### 15. 创建80端口consumer-order微服务，注册到Consul

1. 创建80微服务
2. 添加pom（consul），写yml（consul配置）
3. 写主启动类，写RestTemplate配置，写controller调用8006端口服务
4. 测试，是否注册成功，url是否可以通过80端口调用8006端口，返回正确信息

### 16. Ribbon负载均衡

1. 最初结合RestTemplate使用，config中加注解@LoadBalanced

2. 自定义负载均衡规则

   1. 在非@ComponentScan包（当前为com.zhangxujie.springcloud）下创建配置类MyRule（com.zhangxujie.ribbonrule.MyRule），配置算法。

   2. 在主启动类上加注解@RibbonClient(name=,configuration=)

      ```java
      @RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MyRibbonRule.class)
      ```

   3. 重启服务，url调用，发现80端口会随机调用8001/8002的服务

### 17. Ribbon补充：自己写负载均衡算法

1. 在**80端口微服务**上改造
2. 在配置类中，去除@LoadBalanced注解
3. 创建LoadBalancer接口
4. 创建MyLB类继承LoadBalancer，标记为Component
5. 在80端口微服务，添加一个GetMapping方法，测试是否成功。

### 18. OpenFeign：80端口使用接口调用微服务

* 模式：接口+注解，微服务调用接口，使用在Consumer端

1. 新建80端口微服务：cloud-consumer-feign-order80
2. POM（添加openfeign）：cloud-consumer-feign-order80/pom.xml
3. YML：cloud-consumer-feign-order80/src/main/resources/application.yml
4. 主启动类， 添加@EnableFeignClients注解
5. 业务类
   1. 写service层接口
      1. 在consumer微服务下，写service.PaymentFeignService接口
      2. 使用@FeignClient注解标识，value="远程微服务名称"，path="请求地址前缀，用于全部方法"
      3. 写微服务的方法，类似Controller层写法（这里实际封装了RestTemplate调用api的过程）
   2. 写controller层，见源代码
6. 测试，启动，访问consumer的url
7. OpenFeign：超时控制机制（默认调用超过1s则异常），在YML中开启配置
   1. 由内部Ribbon控制超时
   2. YML加入配置：cloud-consumer-feign-order80/src/main/resources/application.yml
8. OpenFeign：日志增强 80端口微服务
   1. 写配置类FeignConfig：cloud-consumer-feign-order80/src/main/java/com/zhangxujie/springcloud/config/FeignConfig.java
   2. YML配置：cloud-consumer-feign-order80/src/main/resources/application.yml
   3. 重启80端口微服务，调用，查看后台控制台有详细日志



### 19. 引入Hystrix，创建provider8001端口微服务

1. 创建provider 8001微服务
2. POM复制之前的，引入hystrix依赖
3. 写YML，创建Main
4. 写业务类
   1. 创建service层，写测试类（正常调用、超时调用）
   2. 创建controller层，写测试类（包括ok，timeout两个调用）
5. 开启700xEureka服务，开启这个8001微服务，测试url
6. 使用Jmeter压测工具测试，向timeout服务多线程请求，体验高并发下访问速度减慢
   1. 添加【测试计划 -> 线程(用户) -> 线程组】，设置·线程数·和·循环次数·
   2. 在【线程组】中，添加【取样器 -> HTTP请求】
   3. 设置IP、端口号、请求路径
   4. 启动压测，同时刷新OK的服务，发现请求响应速度变慢



### 20. 引入Hystrix，创建consumer80端口微服务

1. 创建consumer 80微服务（利用RPC框架OpenFeign）
2. POM复制OpenFeign章节的，另外引入Hystrix依赖
3. 写YML，复制之前章节的
4. 写主启动类，添加@EnableFeignClients激活Feign
5. 写业务类
   1. service，写OrderHystrixService接口，加注解@Component、@FeignClient(value、path)，写接口方法
   2. controller，@Resource注解标注service，使用RPC框架远程调用8001端口微服务的方法。
6. 启动，测试，调用是否成功，开启Jmeter压测，再通过RPC调用8001微服务，观察效果

### 21. Hystrix配置：服务降级、熔断、限流

#### 21.1. 服务降级

1. 8001端口微服务fallback：
   1. Service业务类加@HystrixCommand注解，标注处理降级的方法，处理降级。
   2. 主启动类激活，@EnableCircuitBreaker
   3. 运行测试（service写成timeout时间可传参，测试正常情况与降级情况）
2. 80端口服务fallback
   1. YML开启feign.hystrix.enabled=true
   2. 主启动加@EnableHystrix
   3. 写业务类：OrderController，其中添加@HystrixCommand，写降级处理方法
3. fallback和业务代码耦合问题
   1. 可以配置DefaultFallback
      1. 在类上使用@DefaultProperties(defaultFallback=“methodName”)注解标识默认fallback方法
      2. 写降级方法
      3. 需要降级方法只加@HystrixCommand注解，不需要写参数
   2. 解除耦合：对于80端口
      1. 创建PaymentHystrixFallbackService类(@Component)，实现Feign的PaymentHystrixService接口
      2. 对于每个方法，返回内容为服务降级后返回的内容
      3. YML中feign.hystrix.enabled=true
      4. PaymentHystrixService接口的@FeignClient指定降级类：fallback = PaymentHystrixFallbackService.class

#### 21.2. 服务熔断

1. 改造hystrix8001微服务
    1. 改造hystrix-8001的service方法。
        1. 写paymentCircuitBreaker方法
        2. 写fallback方法
        3. 配置注解，注解内容主要配置HystrixCommandProperties类中的属性值。
    2. 写controller方法,调用service的circuit方法
    3. 运行测试
        1. 正数ID，返回正确
        2. 负数ID（走fallback）
        3. 多次负数ID，再正确ID，会触发断路器生效。
        
### 21. Hystrix仪表盘图形监控

1. 创建hystrix-dashboard：9001微服务
2. POM添加hystrix-dashboard依赖，不需要注册到注册中心，不需要hystrix服务，不需要自定义api
3. 写YML，定义端口号9001
4. 写SpringBoot Main类，添加注解@EnableHystrixDashboard
5. 所有Provider微服务需要配置actuator依赖（都已经配过），才能监控
6. 配置hystrix-8001微服务
    1. 注意是否添加actuator依赖
    2. 主启动类添加配置
    3. 重启运行
7. 访问http://localhost:9001/hystrix，测试
    1. 监控url输入：http://localhost:8001/hystrix.stream
    2. 设置title标题，启动监控
    3. 多次访问8001正确请求微服务
    4. 多次访问8001错误请求，触发熔断，查看dashboard
    5. ![img-21-7-5.png](img/img-21-7-5.png)
    

### 22. Gateway网关配置

1. 创建微服务gateway9527
2. 写POM(gateway依赖，移除web和actuator依赖)
3. 写YML
    1. 端口，服务名
    2. 注册中心
    3. 配置网关 spring.cloud.gateway.routes
4. 运行7001，7002，8001，9527
    1. 访问8001微服务/payment/get/1
    2. 访问9527转发的微服务：http://xxx:9527/payment/get/1
5. 通过代码注解方式配置路由(config.GatewayConfig)，尝试访问配置好的地址
6. 动态路由，通过微服务名轮询不同端口的相同微服务
    1. spring.cloud.gateway.discovery.locator.enabled = true # 注册中心自动发现
    2. uri改为 lb://微服务名
    3. 启动 7001，7002，8001，8002，9527
    4. 访问http://localhost:9527/payment/2，每次端口号不同
7. predicates: 可以配置After（在多久之后可以访问该路由）,Cookie,Header等...
8. 微服务9527中创建filter.MyLogGatewayFilter来测试Filter的使用
    1. 使用exchange获取Request值，判断参数是否有uname
        1. 没有：返回不可访问
        2. 有：chain过滤链到下一层
    2. 测试，运行7001，7002，8001，8002，9527
    3. http://localhost:9527/guonei?uname=jason这样的url可以访问，但是去掉uname则不可访问
    4. 不可访问：![不可访问](img/img-22-8-3.png)

### 23. 配置中心 springconfig

1. GitHub或者gitee创建springcloud-config仓库
    1. 创建config-dev.yml, 写入信息; 创建config-prod.yml,写入信息.
2. 创建cloud-config-center3344模块
    1. 主启动类(@EnableConfigServer)、POM(添加新依赖)、配置application.yml(仓库信息等)
    2. 运行7001,7002,然后3344
    3. 访问 http://localhost:3344/master/config-dev.yml, 是否成功展示出配置文件
3. 创建cloud-config-client3355模块
    1. 主启动类(@EnableConfigServer)、POM(添加client新依赖)
    2. 配置YML
        1. 使用:bootstrap.yml(系统级参数,不改变)
        2. 不使用:application.yml(搭配springcloud-config动态改变), 加载顺序:bootstrap->application
    3. 写业务类controller
    4. 启动3355,测试
        1. 访问:http://localhost:3355/config/username
        2. 访问:http://localhost:3355/config/password
        3. 切换配置为prod,再次访问,则会发送变化
    5. 问题: 更改github的配置, 3344可以获取最新配置,但是3355配置依然缓存之前的没有改变
        1. POM中引入actuator依赖
        2. 3355的bootstrap添加配置(management.endpoints.web.exposure.include..),暴露监控端点.
        3. 业务类Controller添加@RefreshScope注解
        4. 测试, 先改变github,然后查看3355是否获取到值
        5. 需要运维改完配置,每次发一条个post来刷新3355: curl -X POST "http://localhost:3355/actuator/refresh"
        6. 再次测试 3355,可以获取最新配置信息
        
### 24. Rabbit MQ
1. Linux安装Rabbit MQ（这里是使用Docker安装的）
    1. 安装docker，参考：https://www.runoob.com/docker/centos-docker-install.html
    2. 配置docker的清华镜像源
    3. 安装rabbitmq：management
    4. 运行，在宿主机访问虚拟机的rabbitmq网页管理测试
2. 建项目，3366端口微服务，copy 3355微服务
    1. POM，与3355相同
    2. 写application.yml，与3355相同，注意改端口号
3. 3344的config-center添加消息总线RabbitMQ依赖
    1. pom添加依赖spring-cloud-starter-bus-amqp
    2. yml中添加RabbitMQ的配置
4. 3355、3366两个集群客户端添加RabbitMQ配置
    1. pom添加依赖
    2. yml中添加RabbitMQ的配置
5. 启动7001、7002、3344、3355、3366
    1. 访问测试3355、3366通
    2. 改变gitee的配置文件
    3. 使用命令刷新配置：curl -X POST "http://localhost:3344/actuator/bus-refresh"
    4. 测试访问3355、3366是否为最新配置
    5. ![24-5-5 刷新前](img/img-24-5-5.png)
    6. ![24-5-6 刷新后](img/img-24-5-6.png)
    7. 原理通过Rabbit MQ做bus来通知
    8. ![24-5-8 rabbitmq](img/img-24-5-8.png)
    9. 如果只通知3355端口，则通过服务名+端口号限制：curl -X POST "http://localhost:3344/actuator/bus-refresh/config-client:3355"
    
### 25. Stream + RabbitMQ创建提供者8801

1. 创建8801微服务,主启动类
2. 添加POM依赖：对stream-rabbitmq的依赖
3. YML配置: stream，以及配置eureka
4. 写service以及Impl，添加sendMessage功能，实现添加新注解 @EnableBinding
5. 写controller，调用service
6. 测试访问：http://localhost:8801/send，成功则显示流水号
7. 刷新多次访问send API，rabbit管理页面也会看到变化
    * ![25-7-1](img/img-25-7-1.png)
    * ![25-7-2](img/img-25-7-2.png)
    
### 26. Stream + RabbitMQ创建消费者8802

1. 创建8802微服务，主启动类
2. 添加POM依赖：对stream-rabbitmq的依赖
3. YML配置: 和8801相同，注意端口号，还有output改为input
4. controller，添加注解 @EnableBinding, 写消费RabbitMQ的业务代码
5. 8801和8802实际演示了RabbitMQ通过JavaAPI的消息生产与消费