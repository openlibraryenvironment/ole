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

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.Truth;

/**
 * This class is a formatter for little-b boolean classes, that cannot accept a null.
 */
public class LittleBooleanFormatter extends Formatter {

    private static final long serialVersionUID = -1800859871401901842L;

    protected Object convertToObject(String target) {
        if (Formatter.isEmptyValue(target))
            return Boolean.FALSE;

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
            return "No";
        if (target instanceof String) {
            return target;
        }

        boolean isTrue = ((Boolean) target).booleanValue();

        return isTrue ? "Yes" : "No";
    }

    protected Object getNullObjectValue() {
        return Boolean.FALSE;
    }

}
