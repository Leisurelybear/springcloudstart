/**
 * FileName: OrderController
 * Author:   jason
 * Date:     2021/1/7 19:16
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.netflix.discovery.EurekaClient;
import com.zhangxujie.springcloud.lb.LoadBalancer;
import com.zhangxujie.springcloud.model.CommonResult;
import com.zhangxujie.springcloud.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/order")
@Slf4j
public class OrderController {

    //单机时候，使用单一URL
//    public static final String SERVICE_PAYMENT = "http://localhost:8001";

    //集群：利用Eureka的注册发现，和服务名来寻找
    public static final String SERVICE_PAYMENT = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancer loadBalancer;

    @Resource
    private DiscoveryClient discoveryClient;

    @GetMapping("/create")
    public CommonResult<Payment> createPayment(Payment payment) {
//        return restTemplate.postForObject(PAYMENT_URL + "/payment", payment, CommonResult.class);
        return restTemplate.postForObject(SERVICE_PAYMENT + "/payment", payment, CommonResult.class);

    }

    @GetMapping("/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
        return restTemplate.getForObject(SERVICE_PAYMENT + "/payment/" + id, CommonResult.class);
    }

    @GetMapping("/get/getentity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id){
        //getForEntity
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(SERVICE_PAYMENT + "/payment/" + id, CommonResult.class);
        log.info(entity.getStatusCode().toString());
        log.info(entity.getHeaders().toString());
        log.info(entity.getBody().toString());
        if (entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            return new CommonResult<>(500, "操作失败");
        }
    }

    @GetMapping("/lb/{id}")//通过这个controller调用，会使用自己定义的轮询方式调用Payment微服务 #bilibili#第42P
    public String getPaymentMyLB(@PathVariable("id") Long id){
        //这里如果要正常运行，需要注释掉config中的@LoadBalanced注解
        List<ServiceInstance> instances =  discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (instances == null || instances.size() <= 0){
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.instances(instances);
        URI uri = serviceInstance.getUri();
        System.out.println(uri);
        return restTemplate.getForObject(uri + "/payment/" + id, String.class);
    }

}
