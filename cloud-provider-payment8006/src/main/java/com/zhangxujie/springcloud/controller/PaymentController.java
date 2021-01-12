/**
 * FileName: PaymentController
 * Author:   jason
 * Date:     2021/1/13 1:19
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping(value = "/test")
    public CommonResult test() {
        return new CommonResult(200, "Hello World! [Port: " + serverPort + "]");
    }

}
