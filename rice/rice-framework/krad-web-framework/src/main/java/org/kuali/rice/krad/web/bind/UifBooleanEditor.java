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

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

/**
 * PropertyEditor for booleans supports y/n which the spring version does not
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifBooleanEditor extends PropertyEditorSupport implements Serializable {
	private static final long serialVersionUID = -6333792216543862346L;

	private static final String TRUE_VALUES = "/true/yes/y/on/1/";
	private static final String FALSE_VALUES = "/false/no/n/off/0/";

	private static final String TRUE_VALUE = "true";
	private static final String FALSE_VALUE = "false";

    /**
     * Gets the property value as a string suitable for presentation
     * to a human to edit
     *
     * @return The property value as a string suitable for presentation
     *       to a human to edit.
     * <p>   Returns String "true" or "false".
     * <p>   Returns "null" is the value can't be expressed as a string.
     * <p>   If a non-null value is returned, then the PropertyEditor should
     *	     be prepared to parse that string back in setAsText().
     */
    @Override
	public String getAsText() {
		if(this.getValue() == null) {
			return "";
		}
		else if(((Boolean)this.getValue()).booleanValue()) {
			return TRUE_VALUE;
		}
		else {
			return FALSE_VALUE;
		}
	}

    /**
     * Sets the property value by parsing a given String
     *
     * <p>
     *     The text is compared against the configured acceptable string values for
     *     boolean true and false
     * </p>
     *
     * @param text  The string to be parsed.
     * @throws IllegalArgumentException if text does not contain either true or false
     */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String input = null;

		if(text != null) {
			StringBuilder builder = new StringBuilder();
			builder.append("/").append(text.toLowerCase()).append("/");
			input = builder.toString();

			if(TRUE_VALUES.contains(input)) {
				this.setValue(Boolean.TRUE);
			}
			else if(FALSE_VALUES.contains(input)) {
				this.setValue(Boolean.FALSE);
			}
			else {
				input = null;
			}
		}

		if(input == null) {
			throw new IllegalArgumentException("Invalid boolean input: " + text);
		}
	}

}
