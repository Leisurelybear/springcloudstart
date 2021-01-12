## Spring Cloud 学习Demo
> 该项目为学习尚硅谷周阳老师的SpringCloud微服务教程的学习项目，[参考视频](https://www.bilibili.com/video/BV1yE411x7Ky)

## 项目记录


1. 先创建provider-payment8001微服务
   1. pom依赖，加入springboot，mysql，mybatis，devtools（支持热部署，可选），lombok（简化开发）等依赖。
   2. 在application.yml中配置端口号，数据库相关配置，mybatis相关配置（比如mapper包位置，实体类位置）
   
2. 创建数据库payment{id，serial}

3. 创建consumer-order80微服务
   
   1. 利用RestTemplate调用8001端口微服务
   
4. 把实体类提出到api自己创建的包api-commons
   1. 先对api的maven进行clean
   2. 然后对其进行install
   3. 在其他用到实体类的地方的pom中引用该自定义包，见pom文件
   
5. 使用Eureka注册中心管理微服务地址
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

6. 其他微服务注册到Eureka
   1. 在需要的pom中加入Eureka的client包依赖（比如8001端口的微服务）
   2. 在8001端口等其他微服务的Main类中，加@EnableEurekaClient
   3. 先启动Eureka-server，再启动Eureka-Client（比如8001微服务），会发现在**http://localhost:7001/**中有注册进去的微服务
   
7. Eureka集群部署

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

8. 8001微服务集群搭建

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

9. 80端口

   1. 在controller通过RestTemplate调用RestAPI时候，服务名代替URL硬编码
   2. 在80服务的容器配置中，为RestTemplate使用@LoadBalanced注解赋予RestTemplate负载均衡能力
   3. 启动8001，8002端口服务，再启动80端口服务
   4. 测试，http://localhost/order/consumer/payment/get/5，发现每次调用端口号不同

10. 配置SpringCloud的注册发现

    1. 在8001上的controller中，注入discovery

       + ```java
             @Resource//服务发现
             private DiscoveryClient discoveryClient;
         ```

    2. 编写discovery方法

    3. 8001主启动类，增加启动注册发现注解 **@EnableDiscoveryClient**
    
11. SpringCloud整合Zookeeper

    1. 在CentOS 7 下载解压Zookeeper，复制conf/zoo_sample.cfg为zoo.cfg，关闭防火墙，通过./zkServer.sh start启动zk，通过./zkCli.sh来进入zk终端。
    
    2. payment8004微服务
    
    3. 写POM，YML，主启动类，Controller
    
       * ```xml
         <!--        添加zookeeper依赖-->
                 <dependency>
                     <groupId>org.springframework.cloud</groupId>
                     <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
                 </dependency>
         ```
    
       * ```yaml
         server:
           port: 8004
         spring:
           application:
             name: cloud-provider-payment
           cloud:
             zookeeper:
               connect-string: 192.168.56.101:2181
         # 除此之外，需要配置DataSource，数据库账号密码，mybatis实体映射位置
         ```
    
    4. 启动8004，注册到zookeeper，通过cli可以查看(ls/get命令)注册进去的节点和信息
    
    5. 测试验证，通过写一个controller，查看是否成功返回
    
12. 写注册zk的80端口consumer微服务


    1. 创建cloud-consumerzk-order80微服务
    2. pom(添加zookeeper官方依赖)、yml、主启动类
    3. 写config配置RestTemplate，写controller添加测试方法（80端口通过resttemplate调用8004端口的服务）
    4. 运行order微服务，查看虚拟机的zkCli中的服务是否注册成功

13. 装Consul注册中心


    1. 官网下载zip到linux，解压得到consul，可以放到/usr/bin目录方便使用
    2. 执行：**consul agent -dev -node machine -client 0.0.0.0 -ui** 运行后，可以通过外部 **ip:8500** 访问web ui界面，注意：如果不加-client 0.0.0.0则不能被外部访问。

14. 创建8006端口provider-payment微服务，注册到consul中
	1. 创建cloud-provider-payment在8006端口的微服务
	2. 添加POM（consul），写yml（consul配置）
	4. 写主启动类，写controller，添加数据库相关依赖以及配置
	6. 测试（通过consul Web UI查看是否注册成功、通过url访问能够返回结果）
15. 创建80端口consumer-order微服务，注册到consul中
    1. 创建80微服务
    2. 添加pom（consul），写yml（consul配置）
    3. 写主启动类，写RestTemplate配置，写controller调用8006端口服务
    4. 测试，是否注册成功，url是否可以通过80端口调用8006端口，返回正确信息

