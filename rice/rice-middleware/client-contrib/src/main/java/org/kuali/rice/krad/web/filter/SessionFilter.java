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

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(SessionFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            ((HttpServletRequest) request).getSession();
        }
        catch (IllegalStateException ise) {
            LOG.info("A user was denied a session");
            throw new IllegalStateException(new StringBuffer("Thank you for visiting Kuali Test Drive!\n\n").append("To ensure that test drivers of the Kuali System demo site have a safe and uneventful trip, we must limit the number of concurrent users and, unfortunately, that number has been reached.\n\n").append("Please check back later.\n\n").append("Questions can be submitted to the Kuali Test Drive listserv at mailto:kualitestdrive@oncourse.iu.edu").toString());
        }
        filterChain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("Initialized");
    }

    public void destroy() {
        LOG.info("Destroyed");
    }
}
