/**
 * FileName: MyRule
 * Author:   jason
 * Date:     2021/1/14 1:29
 * Description:
 */
package com.zhangxujie.ribbonrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRibbonRule {

    @Bean
    public IRule myRule(){
        return new RandomRule();
    }
}
