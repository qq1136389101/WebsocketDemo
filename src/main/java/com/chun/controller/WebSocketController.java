package com.chun.controller;

import com.chun.util.R;
import com.chun.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * WebSocket服务器端推送消息示例Controller
 *
 * @author wallimn，http://wallimn.iteye.com
 *
 */
@RestController
public class WebSocketController {

    @Autowired
    WebSocketService webSocketService;

    @RequestMapping("/sendMessageToUser")
    public R sendMessageToUser(String userName, String content){
        try {
            webSocketService.sendByUserName(userName, content);
        } catch (Exception e) {
            return R.error("发送消息失败");
        }
        return R.ok();
    }

    @RequestMapping("/sendMessageToGroup")
    public R sendMessageToGroup(String groupId, String content){
        try {
            webSocketService.sendByGroupId(groupId, content);
        } catch (Exception e) {
            return R.error("发送消息失败");
        }
        return R.ok();
    }

}