package com.personal.springframework.controller;

import com.personal.springframework.annotation.AccessLimit;
import com.personal.springframework.constant.Constant;
import com.personal.springframework.model.ResponseResult;
import com.personal.springframework.model.User;
import com.personal.springframework.model.enums.BizCodeEnum;
import com.personal.springframework.service.LoginService;
import com.personal.springframework.shiro.SessionDAO;
import com.personal.springframework.util.IPUtil;
import com.personal.springframework.util.UserUtil;
import com.personal.springframework.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * @program: springframework
 * @description: 登录
 * @author: 安少军
 * @create: 2021-12-29 17:36
 **/
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    CacheManager cacheManager;
    @Resource
    SessionDAO sessionDAO;
    @Resource
    LoginService loginService;

    /**
     * @return java.lang.String
     * @Author 安少军
     * @Description 跳转登录页
     * @Date 17:38 2021/12/29
     * @Param []
     **/
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String defaultLogin() {
        return "login";
    }


    @RequestMapping(value = "/validation", method = RequestMethod.GET)
    @AccessLimit(seconds = 60, maxCount = 60)
    public void validation(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //验证码放入缓存
        String sessionId = UserUtil.getSessionId();
        Element element = new Element(sessionId, code);
        cacheManager.getCache("validationCache").put(element);
        //验证码存入图片
        ServletOutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220, 60, os, code);
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 登录
     * @Date 14:57 2021/12/31
     * @Param [username, password, validation, rememberme, request, response]
     **/
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 600)
    public ResponseResult login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            /*@RequestParam("validation") String validation,*/
            @RequestParam(value = "rememberme", required = false) boolean rememberme,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) /*|| StringUtils.isBlank(validation)*/) {
            return ResponseResult.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg());
        }
        Subject subject = SecurityUtils.getSubject();
        //String sessionId = UserUtil.getSessionId();
        //验证验证码
        /*Element element = cacheManager.getCache("validationCache").get(sessionId);
        if (element == null) {
            return ResponseResult.error(BizCodeEnum.VALIDATION_EXCEPTION.getCode(), BizCodeEnum.VALIDATION_EXCEPTION.getMsg());
        }
        String sessionValid = (String) element.getValue();
        if (sessionValid == null) {
            return ResponseResult.error(BizCodeEnum.VALIDATION_EXCEPTION.getCode(), BizCodeEnum.VALIDATION_EXCEPTION.getMsg());
        }
        if (!sessionValid.equalsIgnoreCase(validation)) {
            return ResponseResult.error(BizCodeEnum.VALIDATION_ERROR_EXCEPTION.getCode(), BizCodeEnum.VALIDATION_ERROR_EXCEPTION.getMsg());
        }*/
        //单点登录
        //获取当前已登录的用户session列表
        if (!Constant.MULTIACCOUNT_LOGIN) {
            Collection<Session> sessions = sessionDAO.getActiveSessions();
            for (Session session : sessions) {
                Subject s = new Subject.Builder().session(session).buildSubject();
                //判断是否已通过认证
                if (s.isAuthenticated()) {
                    User u = (User) s.getPrincipal();
                    //sessionId不同 且 登录用户名相同——>进行登录限制
                    if (
                            u != null &&
                                    username.equals(u.getUserName()) &&
                                    password.equals(u.getPassWord()) &&
                                    !session.getId().equals(subject.getSession().getId())) {
                        sessionDAO.delete(session);
                    }
                }
            }
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberme);
        try {
            //指向Realm的用户认证和权限认证方法
            subject.login(token);
            //更新用户登陆时间
            User user = UserUtil.getLoginUser();
            user.setLastLoginIp(IPUtil.getRealIP(request));
            user.setLastLoginTime(new Date());
            loginService.updateLoginInfo(user);
        } catch (UnknownAccountException uae) {
            token.clear();
            return ResponseResult.error(BizCodeEnum.UNKNOWNACCOUNT_EXCEPTION.getCode(), BizCodeEnum.UNKNOWNACCOUNT_EXCEPTION.getMsg());
        } catch (IncorrectCredentialsException ice) {
            token.clear();
            return ResponseResult.error(BizCodeEnum.INCORRECTCREDENTIALS_EXCEPTION.getCode(), BizCodeEnum.INCORRECTCREDENTIALS_EXCEPTION.getMsg());
        } catch (LockedAccountException lae) {
            token.clear();
            return ResponseResult.error(BizCodeEnum.LOCKEDACCOUNT_EXCEPTION.getCode(), BizCodeEnum.LOCKEDACCOUNT_EXCEPTION.getMsg());
        } catch (ExcessiveAttemptsException eae) {
            token.clear();
            return ResponseResult.error(BizCodeEnum.EXCESSIVEATTEMPTS_EXCEPTION.getCode(), BizCodeEnum.EXCESSIVEATTEMPTS_EXCEPTION.getMsg());
        } catch (AuthenticationException ae) {
            token.clear();
            return ResponseResult.error(BizCodeEnum.AUTHENTICATION_EXCEPTION.getCode(), ae.getMessage());
        }
        if (subject.isAuthenticated()) {
            //重定向到之前页面
            /*SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if (savedRequest != null) {
                log.info("saved request:{}", savedRequest.getRequestURI());
                return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg()).put("savedUri", savedRequest.getRequestURI());
            }*/
            return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
        } else {
            token.clear();
            return ResponseResult.error(BizCodeEnum.LOGIN_EXCEPTION.getCode(), BizCodeEnum.LOGIN_EXCEPTION.getMsg());
        }
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 注销
     * @Date 10:33 2022/1/7
     * @Param []
     **/
    @RequestMapping("logout")
    @ResponseBody
    public ResponseResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg());
    }

    /**
     * @return com.personal.springframework.model.ResponseResult
     * @Author 安少军
     * @Description 获取用户信息
     * @Date 11:40 2022/1/7
     * @Param []
     **/
    @RequestMapping("sessionUser")
    @ResponseBody
    public ResponseResult sessionUser() {
        User user = UserUtil.getLoginUser();
        user.setPassWord(null);
        if (user != null) {
            return ResponseResult.success(BizCodeEnum.SUCCESS.getCode(), BizCodeEnum.SUCCESS.getMsg()).put("user", user);
        } else {
            return ResponseResult.success(BizCodeEnum.SESSION_EXCEPTION.getCode(), BizCodeEnum.SESSION_EXCEPTION.getMsg());
        }
    }
}
