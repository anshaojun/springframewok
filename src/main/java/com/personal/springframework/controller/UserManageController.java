package com.personal.springframework.controller;

import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.User;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.model.enums.AjaxResultEnum;
import com.personal.springframework.service.UserManageService;
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
 * @description: 用户管理
 * @author: 安少军
 * @create: 2022-03-01 11:31
 **/
@Controller
@RequestMapping("userManage")
public class UserManageController extends AbstractController {

    @Resource
    UserManageService userManageService;

    @ModelAttribute
    public User initUser(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isBlank(id)) {
            return new User();
        }
        return userManageService.getById(id);
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 主页
     * @Date 11:52 2022/3/1
     * @Param []
     **/
    @RequestMapping("main")
    @RequiresPermissions("sys:user:list")
    public String main() {
        return "userList";
    }


    /**
     * @return com.personal.springframework.model.core.Page<com.personal.springframework.model.User>
     * @Author 安少军
     * @Description 分页
     * @Date 14:17 2022/3/1
     * @Param [user]
     **/
    @RequestMapping("queryByPage")
    @ResponseBody
    @RequiresPermissions("sys:user:list")
    public Page<User> queryByPage(User user) {
        return userManageService.findPage(user);
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 维护
     * @Date 14:19 2022/3/1
     * @Param []
     **/
    @RequestMapping("form")
    @RequiresPermissions(value = {"sys:user:add", "sys:user:edit"}, logical = Logical.OR)
    public String form() {
        return "userForm";
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 保存
     * @Date 15:12 2022/3/1
     * @Param [user]
     **/
    @RequestMapping("save")
    @RequiresPermissions(value = {"sys:user:add", "sys:user:edit"}, logical = Logical.OR)
    @ResponseBody
    public ResponseResult save(@Validated User user) {
        userManageService.save(user);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getCode(), AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * @Author 安少军
     * @Description 删除
     * @Date 17:53 2022/3/1
     * @Param [id]
     * @return com.personal.springframework.model.ResponseResult
     **/
    @RequiresPermissions(value = {"sys:user:del"})
    @ResponseBody
    @RequestMapping("delete")
    public ResponseResult delete(@RequestParam("id") String id){
        userManageService.delete(id);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getCode(), AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * @Author 安少军
     * @Description 批量删除
     * @Date 15:54 2022/3/2
     * @Param [ids]
     * @return com.personal.springframework.model.ResponseResult
     **/
    @RequiresPermissions(value = {"sys:user:del"})
    @ResponseBody
    @RequestMapping("batchDelete")
    public ResponseResult batchDelete(@RequestParam("ids[]") String[] ids){
        userManageService.batchDelete(ids);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getCode(), AjaxResultEnum.SUCCESS.getMsg());
    }

    @RequestMapping("loadUser")
    @ResponseBody
    @RequiresPermissions({"sys:permission:adduser"})
    public Object loaduser(){
        return userManageService.loadUser();
    }

}
