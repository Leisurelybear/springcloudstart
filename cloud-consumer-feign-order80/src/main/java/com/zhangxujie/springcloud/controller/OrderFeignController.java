/**
 * FileName: OrderFeignController
 * Author:   jason
 * Date:     2021/1/15 22:31
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.model.CommonResult;
import com.zhangxujie.springcloud.model.Payment;
import com.zhangxujie.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderFeignController {

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/feign/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping(value = "/feign/timeout")//默认要求1s内回复结果
    public String paymentFeignTimeout() {
        return paymentFeignService.paymentFeignTimeout();
    }

}
