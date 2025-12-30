package com.atguigu.yygh.gateway.filter;

import com.google.common.net.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Henry Guan
 * @description 全局过滤器
 * @since 2023-04-13
 */
//@Component
public class AutoGlobalFilter implements GlobalFilter, Ordered {
    private AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //对于登录的接口，不拦截
        if (antPathMatcher.match("/admin/user/**",path)) {
            return chain.filter(exchange);
        } else {
            //对于非登录接口，验证是否登录
            List<String> strings = request.getHeaders().get("X-Token");
            if (strings == null) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                //路由跳转
                response.getHeaders().set(HttpHeaders.LOCATION,"http://localhost:9528");
                return response.setComplete();
            } else {
                return chain.filter(exchange);
            }
           /* if (strings.size() == 0) {
                //拦截
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("success", false);
                jsonObject.addProperty("code", 28004);
                jsonObject.addProperty("data", "鉴权失败");
                byte[] bytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bytes);
                //指定编码，否则在浏览器中会中文乱码
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                response.writeWith(Mono.just(buffer));
                return response.setComplete();
            } else {
                return chain.filter(exchange);
            }*/
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
