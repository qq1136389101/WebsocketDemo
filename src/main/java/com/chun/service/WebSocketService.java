package com.chun.service;

public interface WebSocketService {
    public void sendByUserName(String userName, String content);
    public void sendByGroupId(String groupId, String content);
}
