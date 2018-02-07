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

/**
 * begin Kuali Foundation modification
 * javadoc changed
 * This class is used to format Exception messages.
 * end Kuali Foundation modification
 */
public class FormatException extends RuntimeException {
    private static final long serialVersionUID = 6679793710457672426L;
    
    // begin Kuali Foundation modification
    // removed member variables: cause, formatter
    // added the following
    private String formatProperty;
    private Object formatValue;
    private final String errorKey;
    private final String[] errorArgs;
    // end Kuali Foundation modification

	// begin Kuali Foundation modification
	// removed public FormatException(Formatter formatter)
	// end Kuali Foundation modification
	
    public FormatException(String message) {
        // begin Kuali Foundation modification
        // orig code: this(message, null);
        this(message, RiceKeyConstants.ERROR_CUSTOM, message);
        // end Kuali Foundation modification
    }

    public FormatException(String message, Throwable cause) {
        // begin Kuali Foundation modification
        /* orig code: 
		super(message);
        this.cause = cause;
		*/
        this(message, RiceKeyConstants.ERROR_CUSTOM, message, cause);
        // end Kuali Foundation modification
    }

    // begin Kuali Foundation modification
    // removed getCause(), getFormatter, printStackTrace, setCause(Throwable), setFormatter
    // added thse methods
    public FormatException(String message, String errorKey, String errorArg) {
        super(message + ", " + errorKey + "[" + errorArg + "]");
        this.errorKey = errorKey;
        this.errorArgs = new String[] { errorArg };
    }

    public FormatException(String message, String errorKey, String errorArg, Throwable cause) {
        this(message, errorKey, errorArg);
        initCause(cause);
    }

    /**
     * @return Returns the formatProperty.
     */
    public String getFormatProperty() {
        return formatProperty;
    }

    /**
     * @param formatProperty The formatProperty to set.
     */
    public void setFormatProperty(String formatProperty) {
        this.formatProperty = formatProperty;
    }

    /**
     * @return Returns the formatValue.
     */
    public Object getFormatValue() {
        return formatValue;
    }

    /**
     * @param formatValue The formatValue to set.
     */
    public void setFormatValue(Object formatValue) {
        this.formatValue = formatValue;
    }

    /**
     * @return the error key for use in the global error map.
     */
    public String getErrorKey() {
        return errorKey;
    }

    /**
     * @return the array of arguments for the keyed error message.
     */
    public String[] getErrorArgs() {
        return errorArgs;
    }
    // end Kuali Foundation modification
}
