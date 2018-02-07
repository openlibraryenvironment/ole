/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.uif.util;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Simple servlet filter used to monitor servlet request processing performance.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @version 2.4
 */
public class ProcessLoggingFilter implements Filter {

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {}

    /**
     * Wrap filter chain execution in a process trace.
     * 
     * @param request The servlet request.
     * @param response The servlet response.
     * @param filterChain The filter chain.
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     * @see ProcessLogger#follow(String, String, java.util.concurrent.Callable)
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
            throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestPath = httpRequest.getServletPath();
        if (httpRequest.getPathInfo() != null) {
            requestPath += httpRequest.getPathInfo();
        }

        try {
            ProcessLogger.follow("request", "Servlet Request " + requestPath, true, new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    filterChain.doFilter(request, response);
                    return null;
                }
            });
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof ServletException) {
                throw (ServletException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("Unexpected checked exception in servlet request", e);
            }
        }
    }

}
