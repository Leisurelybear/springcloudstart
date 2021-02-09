/**
 * FileName: MessageProviderImpl
 * Author:   jason
 * Date:     2021/2/9 16:42
 * Description:
 */
package com.zhangxujie.springcloud.service.impl;

import com.zhangxujie.springcloud.service.IMessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

@EnableBinding(Source.class)//定义消息推送管道
public class MessageProviderImpl implements IMessageProvider {


    @Resource //消息发送通道
    private MessageChannel output;


    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        System.out.println("**** serial : " + serial);
        return serial;
    }
}
