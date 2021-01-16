/**
 * FileName: OrderController
 * Author:   jason
 * Date:     2021/1/17 0:00
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.service.OrderHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/order/hystrix")
public class OrderHystrixController {

    @Resource
    private OrderHystrixService service;

    @GetMapping("/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = service.paymentInfo_OK(id);
        log.info("*** " + result);
        return result;
    }

    @GetMapping("/timeout/{id}")
    public String paymentInfo_Timeout(@PathVariable("id") Integer id){
        String result = service.paymentInfo_Timeout(id);
        log.info("*** " + result);
        return result;
    }

}
