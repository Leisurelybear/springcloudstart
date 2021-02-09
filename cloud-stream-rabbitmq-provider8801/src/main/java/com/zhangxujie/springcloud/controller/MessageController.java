/**
 * FileName: MessageController
 * Author:   jason
 * Date:     2021/2/9 16:58
 * Description:
 */
package com.zhangxujie.springcloud.controller;

import com.zhangxujie.springcloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {

    @Resource
    private IMessageProvider messageProvider;

    @GetMapping("/send")
    public String sendMessage(){
        return messageProvider.send();
    }

}
