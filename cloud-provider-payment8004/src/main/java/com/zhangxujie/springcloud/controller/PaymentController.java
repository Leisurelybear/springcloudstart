/**
 * FileName: PaymentController
 * Author:   jason
 * Date:     2021/1/12 0:20
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

    @Value("${server.port}")//这里可以获得当前yml中的端口号
    private String serverPort;

    @GetMapping(value = "test")
    public CommonResult hello() {
        return new CommonResult(200, "Hello World [port: " + serverPort + "]", null);
    }

}
