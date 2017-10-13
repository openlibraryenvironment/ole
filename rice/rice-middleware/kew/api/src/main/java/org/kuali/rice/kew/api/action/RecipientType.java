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
package org.kuali.rice.kew.api.action;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.mo.common.Coded;

@XmlRootElement(name = "recipientType")
@XmlType(name = "RecipientTypeType")
@XmlEnum
public enum RecipientType implements Coded {
	
	@XmlEnumValue("U") PRINCIPAL("U", "principal"),
	@XmlEnumValue("W") GROUP("W", "group"),
	@XmlEnumValue("R") ROLE("R", "role");
	
	private final String code;
	private final String label;
	
	RecipientType(String code, String label) {
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
		
	public static RecipientType fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (RecipientType request : values()) {
			if (request.code.equals(code)) {
				return request;
			}
		}
		throw new IllegalArgumentException("Failed to locate the RecipientType with the given code: " + code);
	}
	
}
