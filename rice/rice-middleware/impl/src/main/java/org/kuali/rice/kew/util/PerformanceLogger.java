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
package org.kuali.rice.kew.util;

import java.util.Formatter;

/**
 * Records and logs performance information about an elapsed time period.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PerformanceLogger {

    private static final org.apache.log4j.Logger LOG =
        org.apache.log4j.Logger.getLogger(PerformanceLogger.class);
    private long startTime;
    private String documentId;
    
    public PerformanceLogger() {
        recordStartTime();
    }
    
    public PerformanceLogger(String documentId) {
        this();
        this.documentId = documentId;
    }
    
    private void recordStartTime() {
        this.startTime = System.currentTimeMillis();
    }
    
    public void log(String message) {
        log(message, false);
    }

    public void log(String message, boolean terminalPoint) {
    	if ( LOG.isInfoEnabled() ) {
	        long endTime = System.currentTimeMillis();
	        long totalTime = endTime - startTime;
            String logMessage = new Formatter().format("Time: %7dms, ", totalTime).toString();
            if (documentId != null) {
	            logMessage+="docId="+documentId+", ";
	        }
	        logMessage += message;
	        if (terminalPoint) {
	            logMessage += "\n";
	        }
	        LOG.info(logMessage);
    	}
    }

    public void debug(String message) {
        this.debug(message, false);
    }

    public void debug(String message, boolean terminalPoint) {
        if ( LOG.isDebugEnabled() ) {
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            String logMessage = new Formatter().format("Time: %7dms, ", totalTime).toString();
            if (documentId != null) {
                logMessage+="docId="+documentId+", ";
            }
            logMessage += message;
            if (terminalPoint) {
                logMessage += "\n";
            }
            LOG.debug(logMessage);
        }
    }
}
