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
package edu.sampleu.travel.workflow;

public class XmlUtils {
	/**
	 * Returns a String containing the given content string enclosed within an
	 * open and close tag having the given tagName.
	 * 
	 * @param tagName
	 * @param content
	 * @return
	 */
	public static String encapsulate(String tagName, String content) {
		return openTag(tagName) + content + closeTag(tagName);
	}

	private static String openTag(String tagName) {
		return "<" + tagName + ">";
	}

	private static String closeTag(String tagName) {
		return "</" + tagName + ">";
	}

}
