package com.qq.controller;


import com.qq.rest.RetWrapper;
import com.qq.security.Authorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class ResourceController {
    @GetMapping("/resource/{id}")
    @ResponseBody
    @Authorize
    public RetWrapper resource(Integer id) {
        return RetWrapper.ok("http://wx2.sinaimg.cn/large/006LfQcply1g3ule1szakj309q09qta0.jpg");

    }
}
