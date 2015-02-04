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


import org.kuali.ole.utility.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 8/5/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetricsLogger {
    private long startTime;
    private long endTime;
    private Logger logger;

    public MetricsLogger(String loggingClassName) {
        try {
            logger = LoggerFactory.getLogger(Class.forName(loggingClassName));

        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
        }
    }


    public void startRecording() {
        startTime = System.currentTimeMillis();
    }

    public void endRecording() {
        endTime = System.currentTimeMillis();
    }

    public void printTimes(String message) {
        logger.info(message + DateTimeUtil.formatTime(endTime - startTime));
    }
}
