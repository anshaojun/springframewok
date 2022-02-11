package com.personal.springframework.util;

import cn.hutool.core.collection.CollectionUtil;
import com.personal.springframework.constant.MenuOptions;
import com.personal.springframework.constant.RoleOptions;
import com.personal.springframework.model.Menu;
import com.personal.springframework.model.Role;
import com.personal.springframework.model.User;
import com.personal.springframework.repository.MenuMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: springframework
 * @description: 用户工具类
 * @author: 安少军
 * @create: 2021-12-30 11:05
 **/
public class UserUtil {
    private static MenuMapper menuMapper = SpringContextHolder.getBean(MenuMapper.class);

    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {

        }
        return null;
    }

    public static User getLoginUser() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            User user = (User) principal;
            user.setMenuList(getUserMenus());
            return user;
        } else {
            return new User();
        }
    }

    public static List<Menu> getUserMenus() {
        List<Menu> menuList = new ArrayList<>();
        Map<String, List<Menu>> groups = new HashMap<>();
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal != null) {
            User user = (User) principal;
            List<Role> roleList = user.getRoleList();
            //如果是超级管理员
            Map<String, Object> param = new HashMap<>();
            if (roleList.stream().filter(o -> o.getId().equals(RoleOptions.ADMIN.getId())).findAny().isPresent()) {
                menuList.addAll(menuMapper.getByParam(param));
            } else {
                roleList.forEach(r -> {
                    param.put("id", r.getId());
                    List<Menu> menu = menuMapper.getByParam(param);
                    menuList.addAll(menu);
                });
            }
            if (CollectionUtil.isNotEmpty(menuList)) {
                groups = menuList.stream().collect(Collectors.groupingBy(Menu::getMLevel));
                for (int i = 1; i <= groups.size(); i++) {
                    List<Menu> menus = groups.get(i + "");
                    if (CollectionUtil.isNotEmpty(menus)) {
                        List<Menu> lower = groups.get(i + 1 + "");
                        if (CollectionUtil.isNotEmpty(lower)) {
                            menus.forEach(m -> {
                                List<Menu> child = lower.stream().filter(l -> l.getParent().getId().equals(m.getId())).collect(Collectors.toList());
                                m.getChild().addAll(child);
                            });
                        }
                    }
                }
            }
        }
        return groups.get(MenuOptions.ONE_LEVEL.getId());
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (UnavailableSecurityManagerException e) {

        } catch (InvalidSessionException e) {

        }
        return null;
    }

    public static String getSessionId() {
        return (String) SecurityUtils.getSubject().getSession().getId();
    }
}
