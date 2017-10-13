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

import java.util.Collection;


/**
 * This class is used to prevent reformatting of String values.
 */
public class NoOpStringFormatter extends Formatter {
	
    private static final long serialVersionUID = 7724214487192363066L;

	/**
     * Does absolutely nothing to the given String. Yes, this is actually a valuable way to prevent the POJO stuff from converting a
     * simple String into a 1-element String array.
     */
    protected Object convertToObject(String target) {
        return target;
    }

    /**
     * Does absolutely nothing to the given String. Yes, this is actually a valuable way to prevent the POJO stuff from converting a
     * simple String into a 1-element String array.
     */
    public Object format(Object value) {
        if (value != null) {
            if (!(value instanceof String)) {
                throw new FormatException("the given " + value.getClass().getName() + " value is not a String");
            }
        }

        return value;
    }

    public Object convertFromPresentationFormat(Object value) {
        return super.convertFromPresentationFormat(value);
    }

    public Object formatArray(Object value) {
        return super.formatArray(value);
    }

    public Object formatCollection(Collection value) {
        return super.formatCollection(value);
    }

    public Object formatForPresentation(Object value) {
        return super.formatForPresentation(value);
    }

    public Object formatObject(Object value) {
        return super.formatObject(value);
    }
}
