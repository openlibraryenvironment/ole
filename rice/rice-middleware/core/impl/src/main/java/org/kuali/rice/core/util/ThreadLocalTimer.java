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
package org.kuali.rice.core.util;

/**
 * Thread Local Timer for Tread Safe Request Logging {@see RequestLoggingFilter}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ThreadLocalTimer {
    /**
     * startTimeThreadLocal ThreadLocal<Long>
     */
    public static ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<Long>();

    /**
     * Returns startTimeThreadLocal as long since epoch
     * @return long since epoch
     */
    public static long getStartTime() {
        return (Long)startTimeThreadLocal.get();
    }

    /**
     * Sets startTimeThreadLocal to the given long since epoch.
     * @param dateTime long since epoch
     */
    public static void setStartTime(long dateTime) {
        startTimeThreadLocal.set(Long.valueOf(dateTime));
    }

    /**
     * Cleanup, startTimeThreadLocal.remove()
     */
    public static void unset() {
        startTimeThreadLocal.remove();
    }
}
