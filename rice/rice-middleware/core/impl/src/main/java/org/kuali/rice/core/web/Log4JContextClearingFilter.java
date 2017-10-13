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
package org.kuali.rice.core.web;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Clears Log4J NDC and MDC at the end of each request
 */
public class Log4JContextClearingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            doAfterRequest(request, response);
        }
    }

    protected void doAfterRequest(HttpServletRequest request, HttpServletResponse response) {
        clearLog4JThreadLocals();
    }

    /**
     * Clear Log4J threadlocals
     */
    protected static void clearLog4JThreadLocals() {
        NDC.remove();
        MDC.clear();
        ThreadLocal tl = getMDCThreadLocal();
        if (tl != null) {
            tl.remove();
        }
    }

    /**
     * Get the Log4J MDC threadlocal via reflection
     * @return the MDC ThreadLocalMap object or null if unset or error
     */
    protected static ThreadLocal getMDCThreadLocal() {
        try {
            Field mdcField = MDC.class.getDeclaredField("mdc");
            if (mdcField != null) {
                mdcField.setAccessible(true);
                Object mdc = mdcField.get(null);
                Field tlmField = MDC.class.getDeclaredField("tlm");
                if (tlmField != null) {
                    tlmField.setAccessible(true);
                    return (ThreadLocal) tlmField.get(mdc);
                }
            }
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
        return null;
    }
}
