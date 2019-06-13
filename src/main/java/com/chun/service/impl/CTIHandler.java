package com.chun.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.chun.Constant;
import com.chun.service.RedisMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CTIHandler implements WebSocketHandler, RedisMsg {
    /**
     * 配置日志
     */
    private final static Logger logger = LoggerFactory.getLogger(CTIHandler.class);


    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    // 公共用户session池
    private static Map<String, WebSocketSession> socketMap = new HashMap<>();
    // 分组用户session池
    private static Map<String, List<WebSocketSession>> groupSocketMap = new HashMap<>();

    //新增socket
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("成功建立连接");
        //获取用户信息
        String userName = (String) session.getAttributes().get("userName");
        String groupId = (String) session.getAttributes().get("groupId");
        logger.info("获取当前" + socketMap.get(userName));

        // 添加到公共连接池
        if (socketMap.get(userName) == null) {
            socketMap.put(userName, session);
        }

        // 添加到分组连接池
        if(!StringUtils.isEmpty(groupId)){
            if(groupSocketMap.containsKey(groupId) && !groupSocketMap.get(groupId).contains(session)){
                groupSocketMap.get(groupId).add(session);
            }
            if(!groupSocketMap.containsKey(groupId)){
                List<WebSocketSession> socketList = new ArrayList<>();
                socketList.add(session);
                groupSocketMap.put(groupId, socketList);
            }
        }
        logger.info("链接成功");
    }

    //接收socket信息
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        logger.info("收到信息" + webSocketMessage.toString());
        webSocketSession.sendMessage(new TextMessage("服务器已接收你的消息"));
    }

    /**
     * 发送信息给指定用户
     *
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, TextMessage message) {
        WebSocketSession session = socketMap.get(clientId);
        if (session == null) {
            return false;
        }
        logger.info("进入发送消息");
        if (!session.isOpen()) {
            return false;
        }
        try {
            logger.info("正在发送消息");
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 群发消息
     * @param groupId       分组id
     * @param message       消息内容
     * @return
     */
    public boolean sendMessageToGroup(String groupId, TextMessage message){
        List<WebSocketSession> sessionList = groupSocketMap.get(groupId);
        if(sessionList != null){
            for (WebSocketSession session: sessionList) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.info("连接出错");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 删除 socketMap 里的 session
        String userName = (String) session.getAttributes().get("userName");
        if (socketMap.get(userName) != null) {
            socketMap.remove(userName);
        }

        // 删除 groupSocketMap 里的 session
        if(session.getAttributes().containsKey("groupId")){
            String groupId = (String) session.getAttributes().get("groupId");
            groupSocketMap.get(groupId).remove(session);
        }
        logger.info("连接已关闭：" + status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * todo 处理redis订阅的消息
     * 接受订阅信息
     */
    @Override
    public void receiveMessage(String message) {
        JSONObject sendMsg = JSONObject.parseObject(message.substring(message.indexOf("{")));
        String receiverType = sendMsg.getString("receiverType");
        TextMessage receiveMessage = new TextMessage(sendMsg.getString("message"));

        boolean flag = true;
        switch (receiverType){
            case Constant.REDIS_TYPE_ONE:
                String clientId = sendMsg.getString("userName");
                flag = sendMessageToUser(clientId, receiveMessage);
                break;
            case Constant.REDIS_TYPE_GROUP:
                String groupId = sendMsg.getString("groupId");
                flag = sendMessageToGroup(groupId, receiveMessage);
                break;
        }

        if (!flag) {
            logger.info("发送消息失败！");
        }
    }

}
