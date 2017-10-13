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

import java.io.Serializable;
import java.util.Date;

/**
 * DayEvent holds some information about an event
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class DayEvent extends TimeInfo implements Serializable {
    private String name;
    private Date date;

    /**
     * constructor
     * @param name
     * @param date
     */
    public DayEvent(String name, Date date, String startTime, String startTimeAmPm, boolean allDay) {
        super(startTime, startTimeAmPm, allDay);
        this.name = name;
        this.date = date;
    }

    /**
     * default constructor
     */
    public DayEvent () {};

    /**
     * get the event name
     *
     * @return the event name
     */
    public String getName() {
        return name;
    }

    /**
     * set the event name
     *
     * @param name - the event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get the event date
     * @return the event date
     */
    public Date getDate() {
        return date;
    }

    /**
     * set the event date
     * @param date - the event date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
