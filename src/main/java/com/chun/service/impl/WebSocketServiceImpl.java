package com.chun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.chun.Constant;
import com.chun.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;


/**
 * @author LSF
 */

@Service("webSocketService")
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    CTIHandler ctiHandler;
    @Autowired
    RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    /**
     * 发送给指定用户
     * @param userName      用户名
     * @param content       内容
     */
    @Override
    public void sendByUserName(String userName, String content) {
        try {
            // websocket的session在当前服务器，直接发送消息，不需要存入redis
            Boolean flag = ctiHandler.sendMessageToUser(userName, new TextMessage(content));
            if (!flag) {
                //发送失败广播出去，让其他节点发送
                //广播消息到各个订阅者
                JSONObject message = new JSONObject();
                message.put("receiverType", Constant.REDIS_TYPE_ONE);
                message.put("userName", userName);
                message.put("message", content);
                redisTemplate.convertAndSend(Constant.REDIS_CHANNAL, JSONObject.toJSONString(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("推送给客户端失败");
        }
    }

    /**
     * 发送给一组用户
     * @param groupId       组id
     * @param content       内容
     */
    @Override
    public void sendByGroupId(String groupId, String content) {
        //广播消息到各个订阅者
        JSONObject message = new JSONObject();
        message.put("receiverType", Constant.REDIS_TYPE_GROUP);
        message.put("groupId", groupId);
        message.put("message", content);
        redisTemplate.convertAndSend(Constant.REDIS_CHANNAL, JSONObject.toJSONString(message));
    }

}

