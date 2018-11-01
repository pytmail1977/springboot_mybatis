package org.iscas.tj2.pyt.springboot_mybatis.controller;

import javax.servlet.http.HttpServletRequest;

import org.iscas.tj2.pyt.springboot_mybatis.service.CoreService;
import org.iscas.tj2.pyt.springboot_mybatis.service.CoreServiceImplJHSBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CoreController {
    @Autowired //通过 @Autowired的使用来消除 set ，get方法
    //private CoreService coreService;
    private CoreServiceImplJHSBC jhsbcService;
    //？？？CoreService是一个interface，这里为怎么实例化
    
    //增加日志
    //log = LoggerFactory.getLogger(CoreController.class);

    //验证是否来自微信服务器的消息
    

    //调用核心业务类接收消息、处理消息跟推送消息
    @RequestMapping(value = "",method = RequestMethod.POST) //@RequestMapping(value="/departments”)则访问http://localhost/xxxx/departments的时候，会调用在其下面定义的方法
    public  String post(HttpServletRequest req){
        //String respMessage = coreService.processRequest(req);
    	String respMessage = jhsbcService.processRequest(req);
    	return respMessage;
    }
}
