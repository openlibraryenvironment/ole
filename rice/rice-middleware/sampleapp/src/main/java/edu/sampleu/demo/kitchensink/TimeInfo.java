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
package edu.sampleu.demo.kitchensink;

/**
 * used to test spring expressions in the addline of a collection group - based on a KS class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TimeInfo {
    private String startTime;
    private String startTimeAmPm;
    private boolean allDay;

    /**
     * constructor
     * @param startTime - the start time
     * @param startTimeAmPm - whether the start time is PM/AM
     * @param allDay - whether the event is all day
     */
    public TimeInfo(String startTime, String startTimeAmPm, boolean allDay) {
        this.startTime = startTime;
        this.startTimeAmPm = startTimeAmPm;
        this.allDay = allDay;
    }

    /**
     * default constructor
     */
    public TimeInfo() {}

    /**

     * the start time
     *
     * @return start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * set the start time
     *
     * @param startTime - the start time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * get am or pm value
     *
     * @return  am or pm value
     */
    public String getStartTimeAmPm() {
        return startTimeAmPm;
    }

    /**
     *  get am or pm value
     *
     * @param startTimeAmPm - am or pm value
     */
    public void setStartTimeAmPm(String startTimeAmPm) {
        this.startTimeAmPm = startTimeAmPm;
    }

    /**
     * get all day value
     *
     * @return all day value
     */
    public boolean isAllDay() {
        return allDay;
    }

    /**
     * set all day value
     *
     * @param allDay -  all day value
     */
    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }
}
