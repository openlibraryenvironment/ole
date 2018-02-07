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


/**
 * begin Kuali Foundation modification
 * This class is used to format phone number objects.
 * end Kuali Foundation modification
 */
public class PhoneNumberFormatter extends Formatter {
	// begin Kuali Foundation modification
    private static final long serialVersionUID = 241458864711484787L;
    // end Kuali Foundation modification

	// begin Kuali Foundation modification
	// removed: PHONE_NUMBER_ERROR_KEY, PARSE_MSG, FORMAT_MSG
    // todo: foreign phone numbers can be different lengths
    // end Kuali Foundation modification
    static final int NUM_DIGITS = 10;

    /**
     * begin Kuali Foundation modification
     * Removes formatting characters from the provided phone number and returns just the digits. Very lenient about formatting, but
     * requires a ten-digit number.
     * end Kuali Foundation modification
     */
    protected Object convertToObject(String target) {
        String digits = target.replaceAll("[^0-9]", "");
        if (digits.length() != NUM_DIGITS)
        	// begin Kuali Foundation modification
            throw new FormatException("parsing", RiceKeyConstants.ERROR_PHONE_NUMBER, target);
            // end Kuali Foundation modification

        return digits;
    }

    /**
     * Returns its argument formatted as a phone number in the style:
     * <p>
     * 
     * <pre>
     *   (999) 999-9999
     * </pre>
     */
    public Object format(Object value) {
        if (value == null)
            return null;
        if (!(value instanceof String))
        	// begin Kuali Foundation modification
            throw new FormatException("formatting", RiceKeyConstants.ERROR_PHONE_NUMBER, value.toString());
            // end Kuali Foundation modification

		// begin Kuali Foundation modification
        String digits = ((String) value).replaceAll("[^0-9]", "");

        if (digits.length() != NUM_DIGITS)
            throw new FormatException("formatting", RiceKeyConstants.ERROR_PHONE_NUMBER, value.toString());
        // end Kuali Foundation modification

        StringBuffer buf = new StringBuffer("(");
        buf.append(digits.substring(0, 3));
        buf.append(") ");
        buf.append(digits.substring(3, 6));
        buf.append("-");
        buf.append(digits.substring(6));

        return buf.toString();
    }

    /**
     * Validates a phone number string by passing it into the convertToObject method and determining if conversion succeeded.
     *
     * @param phoneNumberString The string to attempt to format.
     * @return True if no exceptions occurred when parsing and the conversion returned a non-null value; false otherwise.
     * @see org.kuali.rice.core.web.format.PhoneNumberFormatter#convertToObject(java.lang.String)
     */
    public boolean validate(String phoneNumberString) {
        Object phoneNumberObject = null;
        try {
            phoneNumberObject = convertToObject(phoneNumberString);
        } catch (Exception e) {
            phoneNumberObject = null;
        }
        return (phoneNumberObject != null);
    }
}
