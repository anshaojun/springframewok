package com.personal.springframework.controller;

import com.personal.springframework.model.Menu;
import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.Role;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.model.enums.BizCodeEnum;
import com.personal.springframework.model.enums.Permission;
import com.personal.springframework.service.RoleManageService;
import com.personal.springframework.util.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: springframework
 * @description: 角色管理
 * @author: 安少军
 * @create: 2022-02-07 10:14
 **/
@Controller
@RequestMapping("roleManage")
public class RoleManageController extends AbstractController {

    @Resource
    RoleManageService roleManageService;

    @ModelAttribute
    public Role initMenu(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isBlank(id)) {
            return new Role();
        }
        return roleManageService.getById(id);
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 跳转页面
     * @Date 9:37 2022/2/8
     * @Param []
     **/
    @RequestMapping("main")
    @RequiresPermissions({"sys:permission:list"})
    public String main() {
        return "roleList";
    }

    /**
     * @return com.personal.springframework.model.core.Page<com.personal.springframework.model.Role>
     * @Author 安少军
     * @Description 加载列表
     * @Date 9:37 2022/2/8
     * @Param [role]
     **/
    @RequestMapping("queryByPage")
    @ResponseBody
    @RequiresPermissions({"sys:permission:role:list"})
    public Page<Role> queryByPage(Role role) {
        return roleManageService.findPage(role);
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 表单页面
     * @Date 9:38 2022/2/8
     * @Param []
     **/
    @RequestMapping("form")
    @RequiresPermissions(value = {"sys:permission:role:add", "sys:permission:role:edit"}, logical = Logical.OR)
    public String form(HttpServletRequest request) {
        List permissions = new ArrayList<>();
        for (Permission p : Permission.values()) {
            permissions.add(p);
        }
        request.setAttribute("permissions", permissions);
        return "roleForm";
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 保存
     * @Date 9:38 2022/2/8
     * @Param [role]
     **/
    @RequestMapping("save")
    @RequiresPermissions(value = {"sys:permission:role:add", "sys:permission:role:edit"}, logical = Logical.OR)
    @ResponseBody
    public ResponseResult save(@Validated Role role) {
        roleManageService.saveNotExists(role, "role_name", "roleName", "角色名称");
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
    }

    /**
     * @return
     * @Author 安少军
     * @Description 删除
     * @Date 9:39 2022/2/8
     * @Param
     **/
    @RequestMapping("delete")
    @RequiresPermissions(value = {"sys:permission:role:del"})
    @ResponseBody
    public ResponseResult delete(Role role) {
        roleManageService.delete(role);
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
    }

    /**
     * @Author 安少军
     * @Description 关联菜单页面
     * @Date 16:09 2022/2/8
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping(value = "roleMenu", method = RequestMethod.GET)
    @RequiresPermissions({"sys:permission:addmenu"})
    public String roleMenuForm() {
        return "roleMenu";
    }

    /**
     * @Author 安少军
     * @Description 加载关联菜单
     * @Date 16:09 2022/2/8
     * @Param [role]
     * @return java.lang.String[]
     **/
    @RequestMapping("getConnected")
    @ResponseBody
    @RequiresPermissions({"sys:permission:addmenu"})
    public String[] getConnected(Role role) {
        List<Menu> menus = role.getMenuList();
        String[] selected = new String[menus.size()];
        for (int i = 0; i < menus.size(); i++) {
            selected[i] = menus.get(i).getId();
        }
        return selected;
    }

    /**
     * @Author 安少军
     * @Description 关联菜单
     * @Date 16:09 2022/2/8
     * @Param [roleId, menus]
     * @return com.personal.springframework.model.ResponseResult
     **/
    @RequestMapping(value = "roleMenu", method = RequestMethod.POST)
    @RequiresPermissions({"sys:permission:addmenu"})
    @ResponseBody
    public ResponseResult roleMenuSave(@RequestParam("roleId") String roleId, @RequestParam(value = "menuIds[]",required = false) String[] menus) {
        roleManageService.connectRoleMenu(roleId, menus);
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
    }
}
