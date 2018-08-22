package org.iscas.tj2.pyt.springboot_mybatis.controller;

import org.iscas.tj2.pyt.springboot_mybatis.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    //增加日志
    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    //验证是否来自微信服务器的消息
    //@RequestMapping(value = "/spring")
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String checkSignature(@RequestParam(name = "signature" ,required = false) String signature  ,
                                 @RequestParam(name = "nonce",required = false) String  nonce ,
                                 @RequestParam(name = "timestamp",required = false) String  timestamp ,
                                 @RequestParam(name = "echostr",required = false) String  echostr){
    	System.out.print("将要检查token");
    	// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败        
    	if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            log.info("接入成功");
            return echostr;
        }
        log.error("接入失败");
        return "";
        
    }
}
