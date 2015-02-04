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
package org.kuali.ole.utility;

import java.text.DecimalFormat;

public class DateTimeUtil {

    public static String formatTime(long endTime, long startTime) {
        long milliseconds = endTime - startTime;
        return formatTime(milliseconds);
    }

    public static String formatTime(long timeInMillisec) {
        long hours;
        long minutes;
        long seconds;
        long milliseconds = timeInMillisec;
        double secAndMilliSec = (double) timeInMillisec / 1000;
        double milliSec = secAndMilliSec - (long) secAndMilliSec;
        DecimalFormat df = new DecimalFormat("#.###");
        seconds = (milliseconds / 1000);
        // milliseconds = milliseconds - seconds * 1000;
        minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        hours = minutes / 60;
        minutes = minutes - hours * 60;
        secAndMilliSec = (double) seconds + Double.valueOf(df.format(milliSec));
        String timeTaken = hours + ":" + minutes + ":" + Double.valueOf(df.format(secAndMilliSec));
        return timeTaken;
    }

}
