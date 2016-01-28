package org.kuali.ole.oleng.rest.filter;


import javax.servlet.*;
import java.io.IOException;

import org.apache.log4j.Logger;
/**
 * Created by SheikS on 1/28/2016.
 */
public class OleNGRestLoginFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(OleNGRestLoginFilter.class);

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("Initializing Rest Login Filter");
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("Chaining Rest Login Filter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
