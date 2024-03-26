package com.personal.springframework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: springframework
 * @description: 主页
 * @author: 安少军
 * @create: 2021-12-30 13:07
 **/
@Controller
public class MainController {
    /**
     * 主页
     * @return
     */
    //空路径被404.疑似依赖版本问题
    @RequestMapping("/main")
    public String index(){
        return "index";
    }
}
