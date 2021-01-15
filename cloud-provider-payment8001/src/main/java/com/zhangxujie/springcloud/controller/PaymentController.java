/**
 * FileName: PaymentController
 * Author:   jason
 * Date:     2021/1/6 22:49
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.model.CommonResult;
import com.zhangxujie.springcloud.model.Payment;
import com.zhangxujie.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")//这里可以获得当前yml中的端口号
    private String serverPort;

    @Resource//服务发现
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        services.forEach(elem -> log.info("service:" + elem));

        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        serviceInstanceList.forEach(e -> log.info(""
                + e.getInstanceId() + "\t"
                + e.getHost() + "\t"
                + e.getPort() + "\t"
                + e.getUri()));

        return discoveryClient;
    }


    @GetMapping(value = "test")
    public CommonResult hello() {
        return new CommonResult(200, "Hello World", null);
    }


    @PostMapping
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info(String.format("****插入结果：【%s】", result));
        if (result > 0) {
            return new CommonResult(200, "插入数据库成功", result);
        } else {
            return new CommonResult(500, "插入失败，服务器内部错误！", null);
        }
    }

    @GetMapping(value = "{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info(String.format("****查询结果：【%s】O(∩_∩)O ", payment));
        if (payment != null) {
            return new CommonResult(200, "查询成功，服务端口：" + serverPort, payment);
        } else {
            return new CommonResult(500, "查询失败！", null);
        }
    }

    @GetMapping(value = "/feign/timeout") // #bilibili#P45
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return serverPort;
        }
    }


}
