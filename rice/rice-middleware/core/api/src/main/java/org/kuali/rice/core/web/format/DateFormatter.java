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
// begin Kuali Foundation modification
package org.kuali.rice.core.web.format;
// end Kuali Foundation modification

// begin Kuali Foundation modification
// import order changed, and java.util.Calendar, org.kuali.KeyConstants and org.kuali.rice.kradServiceLocatorInternal added

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;

/**
 * begin Kuali Foundation modification
 * This class is used to format Date objects.
 * end Kuali Foundation modification
 */
public class DateFormatter extends Formatter {
    private static final long serialVersionUID = 7612442662886603084L;

    private transient DateTimeService dateTimeService;

    /**
     * For a given user input date, this method returns the exact string the user entered after the last slash. This allows the
     * formatter to distinguish between ambiguous values such as "/06" "/6" and "/0006"
     *
     * @param date
     * @return
     */
    private String verbatimYear(String date) {
        String result = "";

        int pos = date.lastIndexOf("/");
        if (pos >= 0) {
            result = date.substring(pos);
        }

        return result;
    }
    // end Kuali Foundation modification


    /**
     * Unformats its argument and return a java.util.Date instance initialized with the resulting string.
     *
     * @return a java.util.Date intialized with the provided string
     */
    protected Object convertToObject(String target) {
        // begin Kuali Foundation modification
        try {
            Date result = getDateTimeService().convertToSqlDate(target);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(result);
            if (calendar.get(Calendar.YEAR) < 1000 && verbatimYear(target).length() < 4) {
                throw new FormatException("illegal year format", RiceKeyConstants.ERROR_DATE, target);
            }
            return result;
        } catch (ParseException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_DATE, target, e);
        }
        // end Kuali Foundation modification
    }

    /**
     * Returns a string representation of its argument, formatted as a date with the "MM/dd/yyyy" format.
     *
     * @return a formatted String
     */
    public Object format(Object value) {
        if (value == null) {
            return null;
        }
        // begin Kuali Foundation modification
        if ("".equals(value)) {
            return null;
        }
        return getDateTimeService().toDateString((java.util.Date) value);
        // end Kuali Foundation modification
    }


    /**
     * This method is invoked to validate a date string using the KNS Service
     * DateTimeService.
     *
     * @param dateString
     * @return
     */
    public boolean validate(String dateString) {
        boolean isValid = false;

        try {
            getDateTimeService().convertToSqlTimestamp(dateString);
            isValid = true;
        } catch (Exception e) {

        }

        return isValid;

    }
    
    protected DateTimeService getDateTimeService() {
    	if (this.dateTimeService == null) {
    		this.dateTimeService = GlobalResourceLoader.getService(CoreConstants.Services.DATETIME_SERVICE);
    	}
    	return this.dateTimeService;
    }

}