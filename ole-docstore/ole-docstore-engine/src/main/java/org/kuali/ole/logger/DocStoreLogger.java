/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocStoreLogger {
    private Logger logger;

    public DocStoreLogger(String loggingClassName) {
        try {
            logger = LoggerFactory.getLogger(Class.forName(loggingClassName));

        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
    }

    public void log(String logMessage) {
        logger.info(logMessage);
    }

    public void log(String logMessage, Exception e) {
        logger.error(logMessage + e.getMessage());
    }

    public void logDebugMsg(String logMessage) {
        logger.debug(logMessage);
    }
}
