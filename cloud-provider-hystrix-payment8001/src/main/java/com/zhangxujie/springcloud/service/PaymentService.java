/**
 * FileName: PaymentService
 * Author:   jason
 * Date:     2021/1/16 22:20
 * Description:
 */
package com.zhangxujie.springcloud.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    public String paymentInfo_OK(Integer id){

        return "OK\t线程池：" + Thread.currentThread().getName() + "\tpaymentInfo_OK, id: " + id + "\t";
    }

    public String paymentInfo_Timeout(Integer id){
        int t = 5;
        try {
            TimeUnit.SECONDS.sleep(t);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return "Timeout\t线程池：" + Thread.currentThread().getName() + "\tpaymentInfo_Timeout, id: " + id + "\t耗时" + t + "秒。";
    }
}
