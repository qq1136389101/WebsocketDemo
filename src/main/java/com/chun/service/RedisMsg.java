package com.chun.service;


/**
 * RedisMsg 描述:
 * lsf
 *
 * @author lsf
 * @create 2019-06-10 17:54
 */
public interface RedisMsg {
    /**
     * 接受信息
     * @param message
     */
    public void receiveMessage(String message);

}
