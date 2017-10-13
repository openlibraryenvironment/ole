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

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.Truth;

/**
 * begin Kuali Foundation modification
 * This class is used to format boolean values.
 * end Kuali Foundation modification
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BooleanFormatter extends Formatter {
    // begin Kuali Foundation modification
    // deleted line: public static final String BOOLEAN_ERROR_KEY = "error.boolean";
    private static final long serialVersionUID = -4109390572922205211L;

    // deleted line: static final String CONVERT_MSG = "Unable to create Boolean object from ";
    // end Kuali Foundation modification

	/* begin Kuali Foundation modification
	   deleted following method */
    // /**
	//  * Returns the error key for this Formatter.
	//  * 
	//  * @see Formatter#getErrorKey()
	//  */
	// public String getErrorKey() {
	// 	return BOOLEAN_ERROR_KEY;
	// }
	// end Kuali Foundation modification
	
    protected Object convertToObject(String target) {
        if (Formatter.isEmptyValue(target))
            return null;

        String stringValue = target.getClass().isArray() ? unwrapString(target) : (String) target;
        stringValue = stringValue.trim().toLowerCase();

        Boolean b = Truth.strToBooleanIgnoreCase(stringValue);
        if (b == null) {
            throw new FormatException("converting", RiceKeyConstants.ERROR_BOOLEAN, stringValue);
        }

        return b;
    }

    public Object format(Object target) {
        if (target == null)
            return null;
        // begin Kuali Foundation modification
        if (target instanceof String) {
            return target;
        }
        // end Kuali Foundation modification

        boolean isTrue = ((Boolean) target).booleanValue();

        return isTrue ? "Yes" : "No";
    }
}
