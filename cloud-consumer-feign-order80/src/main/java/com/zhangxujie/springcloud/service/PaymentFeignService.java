package com.zhangxujie.springcloud.service;


import com.zhangxujie.springcloud.model.CommonResult;
import com.zhangxujie.springcloud.model.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE", path = "/payment") //配置微服务名称
public interface PaymentFeignService {
    // #bilibili#P44
    @GetMapping("/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    // #bilibili#P45  feign超时机制
    @GetMapping(value = "/feign/timeout")
    String paymentFeignTimeout();
}
