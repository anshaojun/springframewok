package com.personal.springframework.shiro;

import cn.hutool.core.collection.CollectionUtil;
import com.personal.springframework.model.Menu;
import com.personal.springframework.model.User;
import com.personal.springframework.model.enums.BizCodeEnum;
import com.personal.springframework.service.LoginService;
import com.personal.springframework.util.StringUtils;
import com.personal.springframework.util.UserUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @program: springframework
 * @description: Realm
 * @author: 安少军
 * @create: 2021-12-29 17:22
 **/
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    LoginService loginService;
    @Autowired
    SessionManager sessionManager;


    private void doAuthorization(SimpleAuthorizationInfo simpleAuthorizationInfo, List<Menu> menus) {
        for (Menu menu : menus) {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                // 添加基于Permission的权限信息
                for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                    simpleAuthorizationInfo.addStringPermission(permission);
                }
            }
            if(CollectionUtil.isNotEmpty(menu.getChild())){
                doAuthorization(simpleAuthorizationInfo,menu.getChild());
            }
        }
    }

    /**
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @Author 安少军
     * @Description 权限认证，即登录过后，遇到需要校验的shiro标签或后台shiro注解则会调用此方法
     * @Date 17:25 2021/12/29
     * @Param [principalCollection]
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = UserUtil.getLoginUser();
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = user.getMenuList();
            doAuthorization(info,list);
            return info;
        } else {
            return null;
        }
    }

    /**
     * @return org.apache.shiro.authc.AuthenticationInfo
     * @Author 安少军
     * @Description 身份认证。即登录通过账号和密码验证登陆人的身份信息
     * @Date 17:26 2021/12/29
     * @Param [authenticationToken]
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String passWord = new String((char[]) authenticationToken.getCredentials());
        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passWord);
        User auth = loginService.userCheck(user);
        if (auth == null) {
            throw new AuthenticationException(BizCodeEnum.UNKNOWNACCOUNT_EXCEPTION.getMsg());
        }
        auth = loginService.loginCheck(user);
        if (auth == null) {
            throw new AuthenticationException(BizCodeEnum.INCORRECTCREDENTIALS_EXCEPTION.getMsg());
        }
        return new SimpleAuthenticationInfo(auth, passWord, getName());
    }
}
