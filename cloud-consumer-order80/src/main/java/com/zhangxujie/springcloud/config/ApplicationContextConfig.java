/**
 * FileName: ApplicationContextConfig
 * Author:   jason
 * Date:     2021/1/7 19:21
 * Description:
 */
package com.zhangxujie.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced//添加负载均衡支持
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    //相当于 applicationContext.xml  <bean id=".." class="...">

}
