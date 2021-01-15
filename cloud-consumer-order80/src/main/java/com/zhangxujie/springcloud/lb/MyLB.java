/**
 * FileName: MyLB
 * Author:   jason
 * Date:     2021/1/15 20:55
 * Description:
 */
package com.zhangxujie.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLB implements LoadBalancer{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){
        int current;
        int next;
        do {
            current = this.atomicInteger.get();//得到当前的数字
            //next 从0到2147483647循环
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        }while (!atomicInteger.compareAndSet(current, next));
        System.out.println("*****current: " + current);
        System.out.println("*****next: " + next);

        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {

        int index = getAndIncrement() % serviceInstances.size();


        return serviceInstances.get(index);
    }
}
