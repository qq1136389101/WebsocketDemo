package com.chun.controller;

import java.io.IOException;

import com.chun.websocket.WebSocketServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * WebSocket服务器端推送消息示例Controller
 *
 * @author wallimn，http://wallimn.iteye.com
 *
 */
@RestController
public class WebSocketController {

    //页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav=new ModelAndView("index");
        mav.addObject("cid", cid);
        return mav;
    }
    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message) {
        try {
            WebSocketServer.sendInfo(message,cid);
            System.out.println("发送给id为"+cid+":"+message);
        } catch (IOException e) {
            e.printStackTrace();
            return cid+"#"+e.getMessage();
        }
        return cid;
    }

}