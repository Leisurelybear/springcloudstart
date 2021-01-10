/**
 * FileName: PaymentMain9001
 * Author:   jason
 * Date:     2021/1/6 11:44
 * Description:
 */
package com.zhangxujie.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PaymentMain8002 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8002.class, args);
        //https://www.bilibili.com/video/BV1yE411x7Ky?p=8
    }
}
