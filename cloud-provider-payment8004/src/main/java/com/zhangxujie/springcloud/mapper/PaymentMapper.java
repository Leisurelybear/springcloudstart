package com.zhangxujie.springcloud.mapper;

import com.zhangxujie.springcloud.model.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentMapper {

    public int create(Payment payment);
    public Payment getPaymentById(@Param("id") Long id);

}