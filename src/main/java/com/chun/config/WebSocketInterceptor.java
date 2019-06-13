/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.chun.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Redis配置
 *
 * @author Mark sunlightcs@gmail.com
 */

public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * 配置日志
     */
    private final static Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse seHttpResponse,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        String userName = request.getParameter("userName");
        String groupId = request.getParameter("groupId");
        attributes.put("userName", userName);
        attributes.put("groupId", groupId);
        logger.info("握手之前");
        //从request里面获取对象，存放attributes
        return super.beforeHandshake(serverHttpRequest, seHttpResponse, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        logger.info("握手之后");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
