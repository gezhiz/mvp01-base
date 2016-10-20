package com.mvp01.controller;

import com.mvp01.common.exception.ErrcodeException;
import com.mvp01.config.WebConfig;
import com.mvp01.interceptor.LoginInterceptor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wenjie on 16/1/7.
 */
@Controller
public class PathController {
    @RequestMapping(value = "/goLogin", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().removeAttribute(LoginInterceptor.LOGININTERCEPTOR_LOGIN_DEST);
        }
        return "redirect:/user/user";
    }

    @RequestMapping(value = "/err", method = {RequestMethod.GET})
    public String err(HttpServletRequest request) {
        return "error";
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String toHome() {
        return "index";
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET})
    public String to(HttpServletRequest request, HttpServletResponse response) {
        if (WebConfig.DEBUG) {
            if (handleMock(request, response)) {
                return null;
            }
        }
        if (ExceptionController.isApiRequest(request)) {
            throw new ErrcodeException("请求地址不存在");
        }
        String uri = request.getServletPath();
        return uri;
    }

    @RequestMapping(value = "/**", method = {RequestMethod.POST})
    public String to1(HttpServletRequest request, HttpServletResponse response) {
        if (WebConfig.DEBUG) {
            if (handleMock(request, response)) {
                return null;
            }
        }
        if (ExceptionController.isApiRequest(request)) {
            throw new ErrcodeException("请求地址不存在");
        }
        return request.getServletPath();
    }

    private boolean handleMock(HttpServletRequest request, HttpServletResponse response) {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("classpath:/mock" + request.getServletPath());
        if (resource.exists()) {
            try {
                IOUtils.copy(resource.getInputStream(), response.getOutputStream());
                response.setContentType("application/json;charset=utf-8");
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
