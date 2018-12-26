package cn.zmd.frame.common.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class RequestUtil {
    public RequestUtil() {
    }

    public static String getQueryString(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return request.getQueryString();
        } else {
            Map<String, String[]> params = request.getParameterMap();
            StringBuilder sb = new StringBuilder(1000);
            Iterator var3 = params.keySet().iterator();

            while (var3.hasNext()) {
                String key = (String) var3.next();
                String[] values = (String[]) params.get(key);

                for (int i = 0; i < values.length; ++i) {
                    sb.append(key).append("=").append(values[i]).append("&");
                }
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.lastIndexOf("&"));
            }

            return sb.toString();
        }
    }

    public static String getRequestParams(HttpServletRequest request) {
        String result = "{";
        Map maps = request.getParameterMap();
        Iterator keys = maps.keySet().iterator();

        boolean flag1;
        for (flag1 = false; keys.hasNext(); flag1 = true) {
            String k = (String) keys.next();
            String v = request.getParameter(k);
            result = result + k + ":[";
            result = result + v;
            result = result + "],";
        }

        if (flag1) {
            result = result.substring(0, result.length() - 1);
        }

        result = result + "}";
        return result;
    }

    public static String getAuthCodeDomain(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host != null) {
            host = host.substring(host.indexOf(46) + 1);
        }

        return host;
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static boolean isPjax(HttpServletRequest request) {
        return StringUtils.isNotBlank(request.getHeader("X-PJAX"));
    }

    public static String getFullUrl(HttpServletRequest request) {
        String scheme = request.getHeader("Scheme");
        String s = "http://";
        if ("https".equals(scheme)) {
            s = "https://";
        }

        String qs = request.getQueryString();
        String fullUrl = s + request.getServerName() + request.getRequestURI();
        if (!StringUtils.isEmpty(qs)) {
            fullUrl = fullUrl + "?" + qs;
        }

        return fullUrl;
    }

    public static String getRequestURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String qs = request.getQueryString();
        if (!StringUtils.isEmpty(qs)) {
            requestURI = requestURI + "?" + qs;
        }

        return requestURI;
    }

    public static boolean isPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod());
    }

    public static String getPostContent(HttpServletRequest request) {
        if (isPost(request)) {
            try {
                InputStreamReader reader = new InputStreamReader(request.getInputStream(), "utf-8");
                StringBuilder sb = new StringBuilder();
                char[] buffer = new char[512];
                boolean var4 = false;

                int len;
                while ((len = reader.read(buffer)) > 0) {
                    sb.append(buffer, 0, len);
                }

                return sb.toString();
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        return null;
    }
}