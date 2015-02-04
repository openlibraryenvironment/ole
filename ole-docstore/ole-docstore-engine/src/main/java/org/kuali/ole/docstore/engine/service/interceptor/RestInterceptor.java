package org.kuali.ole.docstore.engine.service.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 3/12/14
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class RestInterceptor  extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/xml;charset=UTF-8");
        return true;
    }
}
