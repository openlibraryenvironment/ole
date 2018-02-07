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
package org.kuali.rice.core;

import org.apache.log4j.Logger;
import org.kuali.rice.core.util.ThreadLocalTimer;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    private static final Logger LOG = Logger.getLogger(RequestLoggingFilter.class);
    private List<String> extensionsToIgnore = Arrays.asList(".js"   + DEFAULT_AFTER_MESSAGE_SUFFIX,
                                                            ".css"  + DEFAULT_AFTER_MESSAGE_SUFFIX,
                                                            ".png"  + DEFAULT_AFTER_MESSAGE_SUFFIX,
                                                            ".gif"  + DEFAULT_AFTER_MESSAGE_SUFFIX,
                                                            ".jpg"  + DEFAULT_AFTER_MESSAGE_SUFFIX,
                                                            ".jpeg" + DEFAULT_AFTER_MESSAGE_SUFFIX);

    @Override
    protected void beforeRequest(HttpServletRequest httpServletRequest, String s) {
        if (LOG.isDebugEnabled() && loggableExtensions(s)) {
            ThreadLocalTimer.setStartTime(new Date().getTime());
        }
    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String s) {
        if (LOG.isDebugEnabled() && loggableExtensions(s)) {
            long startTime = ThreadLocalTimer.getStartTime();
            logElapsedTime(s, startTime);
            ThreadLocalTimer.unset();
        }
    }

    private void logElapsedTime(String s, long startTime) {
        long endTime = new Date().getTime();
        long elapsedTime = endTime - startTime;
        StringBuffer sb = new StringBuffer(s);
        sb.append(" ").append(elapsedTime).append(" ms.");
        LOG.debug(sb.toString());
    }

    private boolean loggableExtensions(String s) {
        if (s.contains(".") && s.endsWith(DEFAULT_AFTER_MESSAGE_SUFFIX)) {
            String match = s.substring(s.lastIndexOf("."), s.length());
            return !extensionsToIgnore.contains(match);
        }
        // Don't log requests to javamelody's monitoring
        return !s.endsWith("/monitoring]");
    }
}
