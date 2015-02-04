package org.kuali.ole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/24/13
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLoginFilter implements Filter {
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
