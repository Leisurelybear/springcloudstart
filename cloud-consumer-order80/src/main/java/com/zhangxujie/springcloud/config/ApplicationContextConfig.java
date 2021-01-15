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
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced//添加负载均衡支持 ，注释掉以使用自定义负载均衡 #bilibili#第42P
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().forEach(c -> {
            if (c instanceof StringHttpMessageConverter){
                ((StringHttpMessageConverter) c).setDefaultCharset(Charset.forName("UTF-8"));
            }
        });
        return restTemplate;
    }
    //相当于 applicationContext.xml  <bean id=".." class="...">

}
