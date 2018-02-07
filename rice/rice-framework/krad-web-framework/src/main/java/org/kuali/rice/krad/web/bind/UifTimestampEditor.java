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

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * PropertyEditor converts between date display strings and
 * <code>java.sql.Timestamp</code> objects using the
 * <code>org.kuali.rice.core.api.datetime.DateTimeService</code>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifTimestampEditor extends UifDateEditor implements Serializable {
    private static final long serialVersionUID = -2776193044590819394L;

    /**
     * This overridden method uses the
     * <code>org.kuali.rice.core.api.datetime.DateTimeService</code> to convert
     * the time stamp object to the display string.
     *
     * @see UifDateEditor#getAsText()
     */
    @Override
    public String getAsText() {
        if (this.getValue() == null) {
            return null;
        }
        if ("".equals(this.getValue())) {
            return null;
        }

        return getDateTimeService().toDateTimeString((Timestamp) this.getValue());
    }

    /**
     * This overridden method converts the display string to a
     * <code>java.sql.Timestamp</code> object using the
     * <code>org.kuali.rice.core.api.datetime.DateTimeService</code>.
     *
     * @see UifDateEditor#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(new Timestamp(((Date) super.convertToObject(text)).getTime()));
    }

}
