/**
 * FileName: OrderFeignMain80
 * Author:   jason
 * Date:     2021/1/15 22:12
 * Description:
 */
package com.zhangxujie.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // #bilibili#P44
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class, args);
    }
}
