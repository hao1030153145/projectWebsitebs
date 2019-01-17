/*
 * @project: projectwebsitebs
 * @package: com.lianNLP.website.web.inteceptor
 * @title:   LoginInterceptor.java 
 *
 * Copyright (c) 2017 jeeframework Limited, Inc.
 * All rights reserved.
 */
package com.lianNLP.website.web.inteceptor;

import com.jeeframework.util.cookie.CookieHelper;
import com.jeeframework.util.encrypt.BASE64Util;
import com.jeeframework.util.encrypt.MD5Util;
import com.jeeframework.util.validate.Validate;
import com.lianNLP.website.biz.service.BossUserService;
import com.lianNLP.website.constant.Constants;
import com.lianNLP.website.integration.bo.BossUser;
import com.lianNLP.website.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 通用登录拦截器
 *
 * @author lance
 * @version 1.0 2017-02-14 9:32
 */
public abstract class LoginInterceptor implements HandlerInterceptor {

    @Resource
    protected BossUserService bossUserService;

    protected boolean validUserLoginStatus(HttpServletRequest request) throws ServletException, IOException {
        String currentUri = WebUtil.getUri(request);

        HttpSession session = request.getSession();

        BossUser userBO = null;

        try {
            if (validNeedVerifyLoginStatus(request)) {
                return true;
            }

            request.setAttribute("currentUri", currentUri);

            userBO = (BossUser) session.getAttribute(Constants.WITH_SESSION_USER);
            if (userBO != null) {
                return true;
            }


            String userInfoCookie = CookieHelper.getCookieValue(request, Constants.LOGIN_COOKIE_SIGN);
            if (validateLoginExpired(session, userInfoCookie)) {
                return true;
            }


        } finally {
            // 清除用户的角色信息
            request.setAttribute(Constants.REQUEST_USERBO, userBO);
        }
        return true;
    }

    protected boolean validateLoginExpired(HttpSession session, String userInfoCookie) throws IOException {
        if (!Validate.isEmpty(userInfoCookie)) {
            String userInfoCookieTmp = new String(BASE64Util.decode(userInfoCookie));
            String[] loginInfo = userInfoCookieTmp.split(":");

            String userId = loginInfo[0];
            String validTime = loginInfo[1];
            String cookieValueWithMd5 = loginInfo[2];


            BossUser userObj = bossUserService.getBossUserByUid(Long.valueOf(userId));

            if (userObj != null) {
                String compareMd5 = MD5Util.encrypt(userObj.getUid() + ":" + userObj.getPasswd() + ":" + validTime + ":" + Constants.LOGIN_KEY);

                if (cookieValueWithMd5.equals(compareMd5)) {
                    session.setAttribute(Constants.WITH_SESSION_USER, userObj);
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean validNeedVerifyLoginStatus(HttpServletRequest request) {
        String confEnv = System.getProperty("conf.env");

        if (!Validate.isEmpty(confEnv) && ("local".equalsIgnoreCase(confEnv) || "dev".equalsIgnoreCase(confEnv) || "ci".equalsIgnoreCase(confEnv))) {
            String testInLogin = request.getParameter("testInLogin");

            if (!Validate.isEmpty(testInLogin) && "no".equals(testInLogin)) {
                return true;
            }

        }
        return false;
    }
}
