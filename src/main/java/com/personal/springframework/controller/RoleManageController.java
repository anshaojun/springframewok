package com.personal.springframework.controller;

import com.personal.springframework.annotation.OperLog;
import com.personal.springframework.model.*;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.model.enums.AjaxResultEnum;
import com.personal.springframework.model.enums.OperModel;
import com.personal.springframework.model.enums.OperType;
import com.personal.springframework.service.RoleManageService;
import com.personal.springframework.util.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @RequiresPermissions({"sys:permission:list"})
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
    @RequiresPermissions(value = {"sys:permission:add", "sys:permission:edit"}, logical = Logical.OR)
    public String form(HttpServletRequest request) {
        /*List permissions = new ArrayList<>();
        for (Permission p : Permission.values()) {
            permissions.add(p);
        }
        request.setAttribute("permissions", permissions);*/
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
    @RequiresPermissions(value = {"sys:permission:add", "sys:permission:edit"}, logical = Logical.OR)
    @ResponseBody
    @OperLog(operType = OperType.SAVE, operModel = OperModel.ROLE, operDesc = "保存角色")
    public ResponseResult save(@Validated Role role) {
        roleManageService.saveNotExists(role, "role_name", "roleName", "角色名称");
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * @return
     * @Author 安少军
     * @Description 删除
     * @Date 9:39 2022/2/8
     * @Param
     **/
    @RequestMapping("delete")
    @RequiresPermissions(value = {"sys:permission:del"})
    @ResponseBody
    public ResponseResult delete(Role role) {
        roleManageService.delete(role);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 关联菜单页面
     * @Date 16:09 2022/2/8
     * @Param []
     **/
    @RequestMapping(value = "roleMenu", method = RequestMethod.GET)
    @RequiresPermissions({"sys:permission:addmenu"})
    public String roleMenuForm() {
        return "roleMenu";
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 关联单位页面
     * @Date 12:06 2022/3/4
     * @Param []
     **/
    @RequestMapping(value = "roleAgency", method = RequestMethod.GET)
    @RequiresPermissions({"sys:permission:addagency"})
    public String roleAgencyForm() {
        return "roleAgency";
    }

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 关联用户
     * @Date 16:17 2022/3/2
     * @Param []
     **/
    @RequestMapping(value = "roleUser", method = RequestMethod.GET)
    @RequiresPermissions({"sys:permission:addmenu"})
    public String roleUserForm() {
        return "roleUser";
    }


    /**
     * 获取关联用户
     *
     * @param role
     * @return java.lang.String[]
     * @author anshaojun
     * @date 2022/5/31 0031 15:19
     **/
    @RequestMapping("getConnectedUser")
    @ResponseBody
    @RequiresPermissions({"sys:permission:adduser"})
    public String[] getConnectedUser(Role role) {
        List<User> users = role.getUserList();
        String[] selected = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            selected[i] = users.get(i).getId();
        }
        return selected;
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 关联菜单
     * @Date 16:09 2022/2/8
     * @Param [roleId, menus]
     **/
    @RequestMapping(value = "roleMenu", method = RequestMethod.POST)
    @RequiresPermissions({"sys:permission:addmenu"})
    @ResponseBody
    public ResponseResult roleMenuSave(@RequestParam("roleId") String roleId, @RequestParam(value = "menuIds[]", required = false) String[] menus) {
        roleManageService.connectRoleMenu(roleId, menus);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 关联角色单位
     * @Date 12:07 2022/3/4
     * @Param [roleId, agencys]
     **/
    @RequestMapping(value = "roleAgency", method = RequestMethod.POST)
    @RequiresPermissions({"sys:permission:addagency"})
    @ResponseBody
    public ResponseResult roleAgencySave(@RequestParam("roleId") String roleId, @RequestParam(value = "agencyIds[]", required = false) String[] agencys) {
        roleManageService.connectRoleAgency(roleId, agencys);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }

    /**
     * 关联角色用户
     * @param roleId
     * @param users
     * @return com.personal.springframework.model.ResponseResult
     * @author anshaojun
     * @date 2022/5/31 0031 15:19
     **/
    @RequestMapping(value = "roleUser", method = RequestMethod.POST)
    @RequiresPermissions({"sys:permission:adduser"})
    @ResponseBody
    public ResponseResult roleUser(@RequestParam("roleId") String roleId, @RequestParam(value = "userIds[]", required = false) String[] users) {
        roleManageService.connectRoleUser(roleId, users);
        return ResponseResult.success(AjaxResultEnum.SUCCESS.getMsg());
    }
}
