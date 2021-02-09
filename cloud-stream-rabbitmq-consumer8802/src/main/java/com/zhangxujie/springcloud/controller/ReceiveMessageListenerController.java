/**
 * FileName: ReceiveMessageListenerController
 * Author:   jason
 * Date:     2021/2/9 18:16
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> msg) {
        System.out.println("消费者1号 -----> 接受消息：" + msg.getPayload() + " --- Port: " + serverPort);
    }

}
