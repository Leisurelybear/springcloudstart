/**
 * FileName: PaymentMain9001
 * Author:   jason
 * Date:     2021/1/6 11:44
 * Description:
 */
package com.zhangxujie.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class PaymentMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8001.class, args);
        //https://www.bilibili.com/video/BV1yE411x7Ky?p=8
    }
}
