package org.iscas.tj2.pyt.springboot_mybatis.controller;

import javax.servlet.http.HttpServletRequest;

import org.iscas.tj2.pyt.springboot_mybatis.service.ICoreService;
import org.iscas.tj2.pyt.springboot_mybatis.testService.ITestService;
import org.iscas.tj2.pyt.springboot_mybatis.service.CoreServiceImplJHSBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CoreController {
	/*
	@RequestMapping("/")
	public String Hello() {
		return "hello this is response for request of root path";
	}
    */
	
	@Autowired //通过 @Autowired的使用来消除 set ，get方法
    @Qualifier("JHSBC")
    private ICoreService coreService;
    
	/*
    @Autowired
    private ITestService testService;
    */
	
    //private CoreServiceImplJHSBC jhsbcService;
    //？？？CoreService是一个interface，这里为怎么实例化——通过component scan
    //用spring框架，这里只能注入接口，否则maven install会报“o.m.s.mapper.ClassPathMapperScanner      : No MyBatis mapper was found in……”错误
    
    //增加日志
    //log = LoggerFactory.getLogger(CoreController.class);

    //验证是否来自微信服务器的消息
    

    //调用核心业务类接收消息、处理消息跟推送消息
    @RequestMapping(value = "core",method = RequestMethod.POST) //@RequestMapping(value="/departments”)则访问http://localhost/xxxx/departments的时候，会调用在其下面定义的方法
    public  String post(HttpServletRequest req){
    	System.out.println("Run to Here: coreService");
        String respMessage = coreService.processRequest(req);
        //String respMessage = jhsbcService.processRequest(req);
    	return respMessage;
    }
   
   /*
    @RequestMapping(value = "test",method = RequestMethod.POST) //@RequestMapping(value="/departments”)则访问http://localhost/xxxx/departments的时候，会调用在其下面定义的方法
    public  String postTest(HttpServletRequest req){
    	System.out.println("Run to Here: postTest");
        String respMessage = testService.processRequest(req);
        //String respMessage = jhsbcService.processRequest(req);
    	return respMessage;
    }
     
    */
}
