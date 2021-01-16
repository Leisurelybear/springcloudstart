package com.zhangxujie.springcloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT"
        , path = "/payment/hystrix")
public interface OrderHystrixService {

    @GetMapping("/ok/{id}")
    String paymentInfo_OK(@PathVariable("id") Integer id);

    @GetMapping("/timeout/{id}")
    String paymentInfo_Timeout(@PathVariable("id") Integer id);

}
