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
package org.kuali.rice.ken.util;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

/**
 * Wrapper for the Log4J performance log
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class PerformanceLog {
    private static final Logger LOG = Logger.getLogger("Performance");

    /**
     * This class 
 * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    public static final class PerformanceStopWatch {
        private StopWatch stopWatch = new StopWatch();
        private String message;
        
        /**
         * Constructs a PerformanceLog.java.
         * @param message
         */
        public PerformanceStopWatch(String message) {
            this.message = message;
            stopWatch.start();
        }

        /**
         * This method records the duration of how long a message delivery takes.
         */
        public void recordDuration() {
            logDuration(message, stopWatch.getTime());
        }
    }

    /**
     * This method returns an instance of the logger object.
     * @return Logger
     */
    public static Logger getInstance() {
        return LOG;
    }

    /**
     * This method returns a new stop watch instance.
     * @param message
     * @return PerformanceStopWatch
     */
    public static PerformanceStopWatch startTimer(String message) {
        return new PerformanceStopWatch(message);
    }
    
    /**
     * This method logs the duration information for a given message.
     * @param message
     * @param duration
     */
    public static void logDuration(String message, long duration) {
        LOG.info(message + ": " + DurationFormatUtils.formatDurationHMS(duration) + " (" + duration+ " ms)");
    }
}
