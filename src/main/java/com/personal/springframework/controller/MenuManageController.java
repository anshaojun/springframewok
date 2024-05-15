package com.personal.springframework.controller;

import com.personal.springframework.model.Menu;
import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.enums.AjaxResultEnum;
import com.personal.springframework.service.MenuManageService;
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
 * @program: springframework
 * @description: 菜单管理
 * @author: 安少军
 * @create: 2022-01-10 14:49
 **/
@RequestMapping("menuManage")
@Controller
public class MenuManageController {

    @Resource
    MenuManageService menuManageService;

    @ModelAttribute
    public Menu initMenu(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isBlank(id)) {
            return new Menu();
        }
        return menuManageService.getById(id);
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 菜单管理
     * @Date 14:51 2022/1/10
     * @Param []
     **/
    @RequestMapping("main")
    public String main() {
        return "menuList";
    }

    /**
     * @return com.personal.springframework.model.core.Page<com.personal.springframework.model.Menu>
     * @Author 安少军
     * @Description 加载菜单列表
     * @Date 14:51 2022/1/10
     * @Param []
     **/
    @RequestMapping("loadMenu")
    @ResponseBody
    @RequiresPermissions(value = {"sys:menu:list", "sys:permission:addmenu"}, logical = Logical.OR)
    public Object loadMenu(@RequestParam(value = "roleId", required = false) String roleId, @RequestParam("checkbox") boolean checkbox) {
        return menuManageService.loadMenuTree(roleId, checkbox);
    }

    /**
     * @return com.personal.springframework.model.Menu
     * @Author 安少军
     * @Description 获取菜单信息
     * @Date 17:19 2022/1/26
     * @Param [menu]
     **/
    @RequestMapping("getMenu")
    @ResponseBody
    @RequiresPermissions({"sys:menu:list"})
    public Menu getMenu(Menu menu) {
        return menu;
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 保存菜单
     * @Date 17:19 2022/1/26
     * @Param [menu]
     **/
    @RequestMapping("save")
    @ResponseBody
    @RequiresPermissions(value = {"sys:menu:add", "sys:menu:edit"}, logical = Logical.OR)
    public ResponseResult save(@Validated Menu menu) {
        menuManageService.save(menu);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 删除菜单
     * @Date 17:20 2022/1/26
     * @Param [menu]
     **/
    @RequestMapping("del")
    @ResponseBody
    @RequiresPermissions(value = {"sys:menu:del"})
    public ResponseResult del(Menu menu) {
        menuManageService.delete(menu.getId());
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }


}
