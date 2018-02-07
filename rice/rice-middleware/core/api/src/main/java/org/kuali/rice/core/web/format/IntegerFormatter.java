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
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.RiceKeyConstants;

/**
 * This class is used to format Integer objects.
 */
public class IntegerFormatter extends Formatter {
	// begin Kuali Foundation modification
    private static final long serialVersionUID = 226069363610021292L;

    // removed INTEGER_ERROR_KEY, PARSE_MSG, getErrorKey()
    // end Kuali Foundation modification

    /**
     * begin Kuali Foundation modification
     * Returns an object representation of the provided string.
     * end Kuali Foundation modification
     */
    protected Object convertToObject(String string) {
    	// begin Kuali Foundation modification
        if (StringUtils.isEmpty(string))
            return null;

        try {
            return new Integer(string);
        }
        catch (NumberFormatException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_INTEGER, string, e);
        }
        // end Kuali Foundation modification
    }

	// begin Kuali Foundation modification
    /**
     * Returns the provided value as a formatted string
     */
    public Object format(Object value) {
        return (value == null ? null : value.toString());
    }
    // end Kuali Foundation modification
}
