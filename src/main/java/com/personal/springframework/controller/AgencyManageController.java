package com.personal.springframework.controller;

import com.personal.springframework.model.Agency;
import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.enums.BizCodeEnum;
import com.personal.springframework.service.AgencyManageService;
import com.personal.springframework.util.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @program: springframewok
 * @description: 单位维护
 * @author: 安少军
 * @create: 2022-03-03 16:26
 **/
@Controller
@RequestMapping("agencyManage")
public class AgencyManageController extends AbstractController {

    @Resource
    AgencyManageService agencyManageService;

    @ModelAttribute
    public Agency initAgency(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isBlank(id)) {
            return new Agency();
        }
        return agencyManageService.getById(id);
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 主页
     * @Date 17:29 2022/3/3
     * @Param []
     **/
    @RequestMapping("main")
    public String main() {
        return "agencyList";
    }

    /**
     * @return java.lang.Object
     * @Author 安少军
     * @Description 加载单位树
     * @Date 17:29 2022/3/3
     * @Param []
     **/
    @RequestMapping("loadAgency")
    @ResponseBody
    @RequiresPermissions(value = {"sys:agency:list", "sys:permission:addagency"}, logical = Logical.OR)
    public Object loadAgency(@RequestParam(value = "roleId", required = false) String roleId, @RequestParam("checkbox") boolean checkbox) {
        return agencyManageService.loadAgencyTree(roleId, checkbox);
    }

    /**
     * @return com.personal.springframework.model.Agency
     * @Author 安少军
     * @Description 获取单位信息
     * @Date 17:29 2022/3/3
     * @Param [agency]
     **/
    @RequestMapping("getAgency")
    @ResponseBody
    @RequiresPermissions({"sys:agency:list"})
    public Agency getAgency(Agency agency) {
        return agency;
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 保存
     * @Date 17:30 2022/3/3
     * @Param [agency]
     **/
    @RequestMapping("save")
    @ResponseBody
    @RequiresPermissions(value = {"sys:agency:add", "sys:agency:edit"}, logical = Logical.OR)
    public ResponseResult save(@Validated Agency agency) {
        agencyManageService.save(agency);
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
    }


    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 删除
     * @Date 17:30 2022/3/3
     * @Param [agency]
     **/
    @RequestMapping("del")
    @ResponseBody
    @RequiresPermissions(value = {"sys:agency:del"})
    public ResponseResult del(Agency agency) {
        agencyManageService.delete(agency.getId());
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
    }
}
