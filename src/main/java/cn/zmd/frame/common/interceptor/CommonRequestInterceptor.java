package cn.zmd.frame.common.interceptor;

import cn.zmd.frame.common.utils.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * <p>类名：CommonRequestInterceptor </p>
 * <p>说明 ：description of the class</p>
 * <p>创建日期： 2018年09月19日 15时52分</p>
 * <p>作者 ：mandy_choi</p>
 * <p>当前版本 ： 1.0</p>
 * <p>更新描述 ：   this description for update of the class</p>
 * <p>最后更新者 : mandy_choi</p>
 */
@Component
public class CommonRequestInterceptor implements HandlerInterceptor {

    protected final static Logger logger = LoggerFactory.getLogger("controller");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("utf-8");
        String url = request.getRequestURI();

        if (!request.getMethod().equals("GET")) {
            try {
                String str = "";
                Enumeration<String> names = request.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String value = request.getParameter(name);
                    if (StringUtils.isBlank(str)) {
                        str += name + "=" + value;
                    } else {
                        str += "&" + name + "=" + value;
                    }
                }
                logger.info("url:{}, params:{}", request.getRequestURI());
                logger.info(str);
            } catch (Exception e) {
                logger.error("handler request log error", e);
            }
        } else {
            String params = RequestUtil.getRequestParams(request);
            if(StringUtils.isBlank(params)){
                params = RequestUtil.getPostContent(request);
            }
            logger.info("url:{} , params:{}", url, params);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setCharacterEncoding("utf-8");
    }
}
