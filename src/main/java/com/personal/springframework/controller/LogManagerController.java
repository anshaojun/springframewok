package com.personal.springframework.controller;

import com.personal.springframework.model.core.OperationLog;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.service.OperationLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @program: springframework
 * @description: 主页
 * @author: 安少军
 * @create: 2021-12-30 13:07
 **/
@Controller
@RequestMapping("logManager")
public class LogManagerController {

    @Resource
    OperationLogService operationLogService;

    /**
     * 主页
     * @return
     */
    @RequestMapping("main")
    public String index(){
        return "logList";
    }

    /**
     * @Author 安少军
     * @Description 分页查询
     * @Date 18:02 2022/3/10
     * @Param [operationLog]
     * @return com.personal.springframework.model.core.Page<com.personal.springframework.model.core.OperationLog>
     **/
    @RequestMapping("queryByPage")
    @ResponseBody
    @RequiresPermissions("log:oplog:list")
    public Page<OperationLog> queryByPage(OperationLog operationLog){
        return operationLogService.findPage(operationLog);
    }


}
