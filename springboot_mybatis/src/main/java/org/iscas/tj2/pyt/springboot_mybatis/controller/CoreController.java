package org.iscas.tj2.pyt.springboot_mybatis.controller;

import javax.servlet.http.HttpServletRequest;

import org.iscas.tj2.pyt.springboot_mybatis.service.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreController {
    @Autowired
    private CoreService coreService;
    
    //增加日志
    //log = LoggerFactory.getLogger(CoreController.class);
    //验证是否来自微信服务器的消息
    // 调用核心业务类接收消息、处理消息跟推送消息
    @RequestMapping(value = "",method = RequestMethod.POST)
    
    public  String post(HttpServletRequest req){
        String respMessage = coreService.processRequest(req);
        return respMessage;
    }
}
