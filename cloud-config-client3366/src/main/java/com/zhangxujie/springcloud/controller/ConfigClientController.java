/**
 * FileName: ConfigClientController
 * Author:   jason
 * Date:     2021/2/8 21:44
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.username}")
    private String configUsername;

    @Value("${config.password}")
    private String configPassword;

    @GetMapping("/config/username")
    public String getConfigUsername(){
        return configUsername;
    }

    @GetMapping("/config/password")
    public String getConfigPassword() {
        return configPassword;
    }


    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return " * Server Port: " + serverPort + "\n * Config Info: " + configUsername + " - " + configPassword;
    }
}
