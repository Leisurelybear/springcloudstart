/**
 * FileName: PaymentService
 * Author:   jason
 * Date:     2021/1/16 22:20
 * Description:
 */
package com.zhangxujie.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String paymentInfo_OK(Integer id) {

        return "OK\t线程池：" + Thread.currentThread().getName() + "\tpaymentInfo_OK, id: " + id + "\t";
    }


    //fallbackMethod：兜底方法名
    @HystrixCommand(
            fallbackMethod = "paymentInfo_TimeoutHandler",
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "3000"//峰值 3s为上限，超过3s为超时，启用兜底方法
                    )
            }
    )
    public String paymentInfo_Timeout(Integer id) {
        int t = 5;
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return "Timeout\t线程池：" + Thread.currentThread().getName() + "\tpaymentInfo_Timeout, id: " + id + "\t耗时" + t + "秒。";
    }

    public String paymentInfo_TimeoutHandler(Integer id) {
        return "Timeout\t线程池：" + Thread.currentThread().getName() + "\t系统繁忙，请稍后由再试！\tHandler, id: " + id + "\t降级处理。";

    }


}
