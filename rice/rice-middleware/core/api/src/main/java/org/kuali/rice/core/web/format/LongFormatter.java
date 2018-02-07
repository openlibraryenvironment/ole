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
import org.kuali.rice.core.api.util.RiceKeyConstants;

/**
 * This class is used to format Long objects.
 */
public class LongFormatter extends Formatter {
    private static final long serialVersionUID = -926525576380295384L;

    /**
     * Returns an object representation of the provided string.
     */
    protected Object convertToObject(String string) {
        if (StringUtils.isEmpty(string))
            return null;

        try {
            return new Long(string);
        }
        catch (NumberFormatException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_LONG, string, e);
        }
    }

    /**
     * Returns the provided value as a formatted string
     */
    public Object format(Object value) {
        return (value == null ? null : value.toString());
    }
}
