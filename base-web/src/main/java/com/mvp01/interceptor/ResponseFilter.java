package com.mvp01.interceptor;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by wenjie on 16/1/12.
 */
public class ResponseFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        if (!httpServletRequest.getServletPath().startsWith("/static")) {
//            httpServletResponse.setHeader("Cache-Control", "no-cache, no-store"); // HTTP 1.1.
//            httpServletResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
////            httpServletResponse.setHeader("Expires", "0"); // Proxies.
//        }
        filterChain.doFilter(new MyRequestWrapper(httpServletRequest), httpServletResponse);
    }

    private static class MyRequestWrapper extends HttpServletRequestWrapper {

        public MyRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        public String[] getParameterValues(String var1) {
            String[] var2 = super.getParameterValues(var1);
            if(var2 == null) {
                return null;
            } else {
                int var3 = var2.length;
                String[] var4 = new String[var3];

                for(int var5 = 0; var5 < var3; ++var5) {
                    var4[var5] = this.cleanXSS(var2[var5]);
                }

                return var4;
            }
        }

        public String getParameter(String var1) {
            String var2 = super.getParameter(var1);
            return var2 == null?null:this.cleanXSS(var2);
        }

        public String getHeader(String var1) {
            String var2 = super.getHeader(var1);
            return var2 == null?null:this.cleanXSS(var2);
        }

        private String cleanXSS(String var1) {
            if(var1 == null) {
                return "";
            } else {
                String var2 = var1.replaceAll("\u0000", "");
                Pattern var3 = Pattern.compile("<script>(.*?)</script>", 2);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", 42);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("</script>", 2);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("<script(.*?)>", 42);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("eval\\((.*?)\\)", 42);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("expression\\((.*?)\\)", 42);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("javascript:", 2);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("vbscript:", 2);
                var2 = var3.matcher(var2).replaceAll("");
                var3 = Pattern.compile("onload(.*?)=", 42);
                var2 = var3.matcher(var2).replaceAll("");
//                var2 = var2.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
//                var2 = var2.replaceAll("\'", "&#39;");
//                var2 = var2.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
                return var2;
            }
        }
    }
}
