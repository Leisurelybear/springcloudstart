/**
 * FileName: PaymentFallbackService
 * Author:   jason
 * Date:     2021/1/20 2:00
 * Description:
 */
package com.zhangxujie.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
public class PaymentHystrixFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "Fallback Service ----- OK";
    }

    @Override
    public String paymentInfo_Timeout(Integer id) {
        return "Fallback Service ----- Timeout";
    }
}
