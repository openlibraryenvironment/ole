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
package org.kuali.rice.core.web.format;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;

import java.text.ParseException;
import java.util.Date;

/**
 * This class is used to format timestamp objects.
 */

public class TimestampAMPMFormatter extends Formatter {
    private static final long serialVersionUID = 7612442662886603084L;

    private transient DateTimeService dateTimeService;

    /**
     * Unformats its argument and return a java.util.Date instance initialized with the resulting string.
     * 
     * @return a java.util.Date intialized with the provided string
     */
    public Object convertToObject(String target) {
        try {
        	return getDateTimeService().convertToSqlTimestamp(target);
        }
        catch (ParseException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_DATE_TIME, target, e);
        }
    }


    /**
     * Returns a string representation of its argument, formatted as a date with the "MM/dd/yyyy h:mm a" format.
     * 
     * @return a formatted String
     */
    public Object format(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String && StringUtils.isEmpty((String) value)) {
            return null;
        }
        return getDateTimeService().toDateTimeString((Date)value);
    }
    
    protected DateTimeService getDateTimeService() {
    	if (this.dateTimeService == null) {
    		this.dateTimeService = GlobalResourceLoader.getService(CoreConstants.Services.DATETIME_SERVICE);
    	}
    	return this.dateTimeService;
    }
}