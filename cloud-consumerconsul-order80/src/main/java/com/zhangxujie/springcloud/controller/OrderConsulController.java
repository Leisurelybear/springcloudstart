/**
 * FileName: OrderConsulController
 * Author:   jason
 * Date:     2021/1/13 2:07
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping(value = "/order")
public class OrderConsulController {

    public static final String INVOKE_URL = "http://cloud-provider-payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/test")
    public CommonResult test(){
        String result = restTemplate.getForObject(INVOKE_URL + "/payment/test", String.class);
        return new CommonResult(200, result);
    }
}
