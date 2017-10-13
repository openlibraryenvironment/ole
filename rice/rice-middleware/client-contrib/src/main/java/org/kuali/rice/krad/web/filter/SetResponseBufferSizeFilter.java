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
package org.kuali.rice.krad.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * This class can be used to increase the response buffer size so that if there is a late exception, Tomcat's ErrorReportValve can
 * display it on the page and return an error status in the HTTP reponse header. This filter must be configured early in the chain
 * in web.xml, before another filter writes anything to the response.
 * 
 * There's no configuration option for this already?
 * 
 * 
 */
public class SetResponseBufferSizeFilter implements Filter {

    private Log log;
    private int bufferSize;

    /**
     * Initializes this Filter with the required parameter, bufferSize.
     * 
     * @throws ServletException if the bufferSize parameter is missing or does not contain an integer.
     * @see Filter#init
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        log = LogFactory.getLog(SetResponseBufferSizeFilter.class);
        String bufferSizeParam = filterConfig.getInitParameter("bufferSize");
        if (log.isDebugEnabled()) {
            log.debug("bufferSizeParam=" + bufferSizeParam);
        }
        if (bufferSizeParam == null) {
            throw new ServletException("bufferSize parameter is required");
        }
        try {
            bufferSize = Integer.parseInt(bufferSizeParam);
        }
        catch (NumberFormatException e) {
            throw new ServletException("bufferSize parameter is not an integer", e);
        }
        log.info("Filter initialized. Response buffer size is " + bufferSize);
    }

    /**
     * Sets the bufferSize of the response.
     * 
     * @see Filter#doFilter
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("setting response buffer size to " + bufferSize);
        }
        servletResponse.setBufferSize(bufferSize);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Does nothing.
     * 
     * @see Filter#destroy
     */
    public void destroy() {
        // nothing to destroy
    }
}
