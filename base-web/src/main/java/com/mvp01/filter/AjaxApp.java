package com.mvp01.filter;

import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by wenjie on 16/5/4.
 */
public class AjaxApp {

    public interface AjaxAppFilter {
        public String getReponse(ServletRequest request, ServletResponse response);
    }

    public static boolean handleAjaxAppRequest(ServletRequest request, ServletResponse response, AjaxAppFilter ajaxAppFilter) throws IOException {
        if (ajaxAppFilter != null && isAjaxRequest(request)) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(ajaxAppFilter.getReponse(request, response));
            writer.flush();
            return true;
        }
        return false;
    }

    public static boolean isAjaxRequest(ServletRequest request) {
        HttpServletRequest req = WebUtils.toHttp(request);
        String xmlHttpRequest = req.getHeader("X-Requested-With");
        if (xmlHttpRequest != null) {
            if (xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest")) {
                return true;
            }
        }
        return false;
    }
}
