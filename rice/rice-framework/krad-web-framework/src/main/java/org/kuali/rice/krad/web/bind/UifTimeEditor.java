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
package org.kuali.rice.krad.web.bind;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.web.format.FormatException;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;

/**
 * PropertyEditor converts between time display strings and {@code java.sql.Time} objects
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifTimeEditor extends PropertyEditorSupport implements Serializable {

    private static final long serialVersionUID = 3087028215717940843L;

    private transient DateTimeService dateTimeService;

    /**
     * Converts the time object to the display string format.
     *
     * @return the time object in the display string format or null if it is empty
     */
    @Override
    public String getAsText() {
        if (getValue() == null) {
            return null;
        }

        if (getValue() instanceof String && StringUtils.isBlank((String) getValue())) {
            return null;
        }

        return getDateTimeService().toTimeString((Time) getValue());
    }

    /**
     * Converts the display string to a time object.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(convertToObject(text));
    }

    /**
     * Converts the display text to a time object.
     *
     * @param text the display text
     *
     * @return the time object
     */
    protected Object convertToObject(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        try {
            return getDateTimeService().convertToSqlTime(text);
        } catch (ParseException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_TIME, text, e);
        }
    }

    /**
     * Gets the date time service.
     *
     * @return the date time service
     */
    protected DateTimeService getDateTimeService() {
        if (dateTimeService == null) {
            dateTimeService = GlobalResourceLoader.getService(CoreConstants.Services.DATETIME_SERVICE);
        }
        return dateTimeService;
    }

}
