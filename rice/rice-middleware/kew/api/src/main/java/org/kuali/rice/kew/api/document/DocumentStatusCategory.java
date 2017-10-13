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
package org.kuali.rice.kew.api.document;

import org.kuali.rice.core.api.mo.common.Coded;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A DocumentStatusCategory is a grouping of document statuses that is searchable
 * on the document search screen.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = "documentStatusCategory")
@XmlType(name = "DocumentStatusCategoryType")
@XmlEnum
public enum DocumentStatusCategory implements Coded {

    @XmlEnumValue("P") PENDING("P", "Pending"),
    @XmlEnumValue("S") SUCCESSFUL("S", "Successful"),
    @XmlEnumValue("U") UNSUCCESSFUL("U", "Unsuccessful");

	private final String code;
    private final String label;

	private DocumentStatusCategory(String code, String label) {
		this.code = code;
        this.label = label;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	public String getLabel() {
	    return label;
	}
	
	public static DocumentStatusCategory fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (DocumentStatusCategory status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Failed to locate the DocumentStatusCategory with the given code: " + code);
	}
	
}
