package com.chun.config;


import com.chun.service.impl.CTIHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Redis配置
 *
 * @author Mark sunlightcs@gmail.com
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //handler是webSocket的核心，配置入口

        // 普通版的
        registry.addHandler(new CTIHandler(), "/websocket").setAllowedOrigins("*").addInterceptors(new WebSocketInterceptor());
        // sockjs版的
        registry.addHandler(new CTIHandler(), "/sockjs/websocket").setAllowedOrigins("*").addInterceptors(new WebSocketInterceptor()).withSockJS();
    }

}
