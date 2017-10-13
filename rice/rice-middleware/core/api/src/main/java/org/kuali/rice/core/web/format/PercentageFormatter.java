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

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiPercent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * begin Kuali Foundation modification
 * This class is used to format objects as a percent.
 * end Kuali Foundation modification
 */
public class PercentageFormatter extends Formatter {
	// begin Kuali Foundation modification
    private static final long serialVersionUID = 1323889942436009589L;
    // end Kuali Foundation modification

    /**
     * The default scale for percentage values
     */
    public final static int PERCENTAGE_SCALE = 2;

    /**
     * The default format for percentage values
     */
    public final static String PERCENTAGE_FORMAT = "#,##0.00";

	// begin Kuali Foundation modification
	// removed PARSE_MSG
	// end Kuali Foundation modification
	
    /**
     * Unformats its argument and returns a BigDecimal instance initialized with the resulting string value
     * 
     * @return a BigDecimal initialized with the provided string
     */
    protected Object convertToObject(String target) {
    	// begin Kuali Foundation modification
    	// using KualiPercent instead of BigDecimal, and exception msg changes
        try {
            DecimalFormat formatter = new DecimalFormat(PERCENTAGE_FORMAT);
            Number parsedNumber = formatter.parse(target.trim());
            return new KualiPercent(parsedNumber.doubleValue());
        }
        catch (NumberFormatException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_PERCENTAGE, target, e);
        }
        catch (ParseException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_PERCENTAGE, target, e);
        }
        // end Kuali Foundation modification
    }

    /**
     * Returns a string representation of its argument, formatted as a percentage value.
     * 
     * @return a formatted String
     */
    public Object format(Object value) {
        if (value == null)
            return "N/A";

        String stringValue = "";
        try {
        	if (value instanceof KualiDecimal) {
        		value = ((KualiDecimal)value).bigDecimalValue();
        	}
            BigDecimal bigDecValue = (BigDecimal) value;
            bigDecValue = bigDecValue.setScale(PERCENTAGE_SCALE, BigDecimal.ROUND_HALF_UP);
            stringValue = NumberFormat.getInstance().format(bigDecValue.doubleValue());
        }
        catch (IllegalArgumentException iae) {
        	// begin Kuali Foundation modification
            throw new FormatException("formatting", RiceKeyConstants.ERROR_PERCENTAGE, value.toString(), iae);
        	// end Kuali Foundation modification
        }

        return stringValue + " percent";
    }
}
