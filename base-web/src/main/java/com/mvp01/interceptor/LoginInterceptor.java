package com.mvp01.interceptor;

import com.mvp01.utils.WebUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wenjie on 16/1/6.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{

    public final static String LOGININTERCEPTOR_LOGIN_DEST = "logininterceptor_login_dest";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 获得请求路径的uri

        String uri = WebUtil.getRequestUri(request);
//        BaseUser user = LoginUtils.getLoginUser(request);
//
//        if (StringUtils.isBlank(uri) || "/".equals(uri)) {
//            throw new PermissionException();
//        }

        if (uri.startsWith("/index")) {
            return true;
        }

//        if (uri.startsWith("/platform/login")) {
//            if (user != null) {
//                throw new LoginException();
//            }
//            return true;
//        }
//
//        if(uri.startsWith("/platform/do_login")) {
//            //已登录
//            if (user != null) {
//                throw new ErrcodeException("您已经登录过，请勿重复登录");
//            }
//            return true;
//        }
//
//        if(user == null) {
//            throw new LoginException();
//        }

        return true;
    }
}
