package org.kuali.ole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/25/13
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class DevelopmentLoginFilter implements Filter {
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest)request) {
            @Override
            public String getRemoteUser() {
                return "admin";
            }}, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
