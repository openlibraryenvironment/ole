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
package org.kuali.rice.kew.messaging;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple utility class which can handle translated a comma-seperated String
 * into an array of String paramters and vice-versa.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ParameterTranslator {

	private static final String SLASH_REGEXP = "\\\\";
    private static final String SLASH_ESCAPE = "\\\\";
    private static final String COMMA_REGEXP = ",";
    private static final String COMMA_ESCAPE = "\\,";
	
	private String untranslatedString = "";
	
	public ParameterTranslator() {}
	
	public ParameterTranslator(String untranslatedString) {
		this.untranslatedString = untranslatedString;
	}
	
	public void addParameter(String value) {
		if (!org.apache.commons.lang.StringUtils.isEmpty(untranslatedString)) {
			untranslatedString += ",";
		}
		untranslatedString += escape(value);
	}
	
	private String escape(String value) {
		if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
			return "";
		}
    	// escape '\' and ',' with "\\" and "\,"
    	value = value.replaceAll(SLASH_REGEXP, SLASH_ESCAPE);
    	value = value.replaceAll(COMMA_REGEXP, COMMA_ESCAPE);
    	return value;
    }
	
	public String getUntranslatedString() {
		return untranslatedString;
	}
	
	public String[] getParameters() {
		List strings = new ArrayList();
		boolean isEscaped = false;
		StringBuffer buffer = null;
		for (int index = 0; index < untranslatedString.length(); index++) {
			char character = untranslatedString.charAt(index);
			if (isEscaped) {
				isEscaped = false;
				if (buffer == null) {
					buffer = new StringBuffer();
				}
				buffer.append(character);
			} else {
				if (character == '\\') {
					isEscaped = true;
				} else if (character == ',') {
					strings.add(buffer.toString());
					buffer = null;
				} else {
					if (buffer == null) {
						buffer = new StringBuffer();
					}
					buffer.append(character);
				}
			}
		}
		// put whatever is left in the buffer (after the last ',') into the list of strings
		if (buffer != null) {
			strings.add(buffer.toString());
		}
		return (String[])strings.toArray(new String[0]);
	}
	
}
