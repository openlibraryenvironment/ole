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

@XmlRootElement(name = "actionRequestPolicy")
@XmlType(name = "ActionRequestPolicyType")
@XmlEnum
public enum ActionRequestPolicy implements Coded {	
	
	@XmlEnumValue("F") FIRST("F", "FIRST"),
	@XmlEnumValue("A") ALL("A", "ALL");
	
	private final String code;
	private final String label;
	
	ActionRequestPolicy(String code, String label) {
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
		
	public static ActionRequestPolicy fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (ActionRequestPolicy value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		throw new IllegalArgumentException("Failed to locate the ActionRequestPolicy with the given code: " + code);
	}
	
}
