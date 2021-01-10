/**
 * FileName: PaymentServiceImpl
 * Author:   jason
 * Date:     2021/1/6 22:46
 * Description:
 */
package com.zhangxujie.springcloud.service.impl;

import com.zhangxujie.springcloud.model.Payment;
import org.springframework.stereotype.Service;
import com.zhangxujie.springcloud.mapper.PaymentMapper;
import com.zhangxujie.springcloud.service.PaymentService;

import javax.annotation.Resource;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentMapper dao;

    public int create(Payment payment) {
        return dao.create(payment);
    }

    public Payment getPaymentById(Long id) {
        return dao.getPaymentById(id);
    }

}
