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


/**
 * This class is used to convert Boolean objects into and from 'true' and 'false' Strings, since that's what JSTL expects to use in
 * conditionals.
 */
public class SimpleBooleanFormatter extends Formatter {
	
    private static final long serialVersionUID = 1733029018087594949L;

	/**
     * Converts the given true/false String into a Boolean.
     */
    protected Object convertToObject(String target) {
        return Boolean.valueOf(target);
    }

    /**
     * Converts the given Boolean into a boolean String.
     */
    public Object format(Object value) {
        Object formatted = value;

        if ((value != null) && (value instanceof Boolean)) {
            formatted = value.toString();
        }

        return formatted;
    }
}
