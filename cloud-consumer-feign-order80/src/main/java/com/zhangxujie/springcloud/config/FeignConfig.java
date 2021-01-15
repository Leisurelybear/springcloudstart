/**
 * FileName: FeignConfig
 * Author:   jason
 * Date:     2021/1/15 23:34
 * Description:
 */
package com.zhangxujie.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
