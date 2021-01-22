/**
 * FileName: PaymentService
 * Author:   jason
 * Date:     2021/1/16 22:20
 * Description:
 */
package com.zhangxujie.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String paymentInfo_OK(Integer id) {

        return "OK\t线程池：" + Thread.currentThread().getName() + "\tpaymentInfo_OK, id: " + id + "\t";
    }


    //fallbackMethod：兜底方法名
    @HystrixCommand(
            fallbackMethod = "paymentInfo_TimeoutFallbackMethod",
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000"//峰值 3s为上限，超过3s为超时，启用兜底方法
                    )
            }
    )
    public String paymentInfo_Timeout(Integer id) {
        //停留id秒后返回，id最高为3s，超过后为超时
        try {
            TimeUnit.SECONDS.sleep(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return "Timeout\t线程池：" + Thread.currentThread().getName() + "\tpaymentInfo_Timeout, id: " + id + "\t耗时" + id + "秒。";
    }

    public String paymentInfo_TimeoutFallbackMethod(Integer id) {
        return "Timeout\t线程池：" + Thread.currentThread().getName() + " - \t8001系统繁忙，请稍后由再试！ - \t - Handler处理 - \t id: " + id + "\t降级处理。";

    }


    //======服务熔断
    @HystrixCommand(
            fallbackMethod = "paymentCircuitBreaker_fallback",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率达到多少跳闸
            }
    )
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if (id < 0){
            throw new RuntimeException("***id 不能为负!");
        }
        String serialNumber = IdUtil.simpleUUID();
        return "调用成功！\t线程池：" + Thread.currentThread().getName() + " -- 流水号：" + serialNumber + " -- ID: " + id;
    }
    private String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能为负! - Wrong id: " + id;
    }


}
