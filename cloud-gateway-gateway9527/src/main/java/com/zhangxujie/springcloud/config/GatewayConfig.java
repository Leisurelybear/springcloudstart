/**
 * FileName: GatewayConfig
 * Author:   jason
 * Date:     2021/1/26 1:30
 * Description:
 */
package com.zhangxujie.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        routes.route(
                //配置这条，可以通过:9527/guonei 访问到后面的地址
                "path_route_p1", r -> r.path("/guonei").uri("http://news.baidu.com/guonei"))
                .route(
                        "path_route_p2", r -> r.path("/guoji").uri("http://news.baidu.com/guoji")
                ).build();


        return routes.build();
    }

}
