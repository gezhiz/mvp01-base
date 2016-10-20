package com.mvp01.utils;

import com.mvp01.common.exception.ErrcodeException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gezhizheng on 16/7/1
 */
public class WebUtil {

    public final static Double SEARCH_DISTANCE = Double.MAX_VALUE;
    public final static String TIME_REGEX = "^(0\\d{1}|1\\d{1}|2[0-3]):[0-5]\\d{1}$";

    public final static String PLATFORM_IM_USER = "sdeals_platform";
    public final static String PLATFORM_IM_PASS = "lingdaoyi01";

    public static List<String> CITY_LIST = new ArrayList<String>();
    static {
        CITY_LIST.add("北京");
    }

    public static String getRequestUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && contextPath.length() > 0) {
            uri = uri.substring(contextPath.length());
        }
        return uri;
    }

    public static void validateLocation(Double lat, Double lng) {
        if (lat != null) {
            if (lat < -90 || lat > 90) {
                throw new ErrcodeException("纬度lat参数的范围必须在-90~90之间");
            }
        }
        if (lng != null) {
            if (lng < -180 || lng > 180) {
                throw new ErrcodeException("经度lng参数的范围必须在-180~180之间");
            }
        }
    }
}
