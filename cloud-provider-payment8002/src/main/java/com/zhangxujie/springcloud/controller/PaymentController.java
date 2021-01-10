/**
 * FileName: PaymentController
 * Author:   jason
 * Date:     2021/1/6 22:49
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.model.CommonResult;
import com.zhangxujie.springcloud.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.zhangxujie.springcloud.service.PaymentService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")//这里可以获得当前yml中的端口号
    private String serverPort;

    @GetMapping(value = "test")
    public CommonResult hello() {
        return new CommonResult(200, "Hello World", null);
    }


    @PostMapping
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info(String.format("****插入结果：【%s】, 来自端口【%s】服务", result, serverPort));
        if (result > 0) {
            return new CommonResult(200, "插入数据库成功", result);
        } else {
            return new CommonResult(500, "插入失败，服务器内部错误！", null);
        }
    }

    @GetMapping(value = "{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info(String.format("****查询结果：【%s】O(∩_∩)O 来自端口【%s】", payment, serverPort));
        if (payment != null) {
            return new CommonResult(200, "查询成功，服务端口：" + serverPort, payment);
        } else {
            return new CommonResult(500, "查询失败！", null);
        }
    }


}
