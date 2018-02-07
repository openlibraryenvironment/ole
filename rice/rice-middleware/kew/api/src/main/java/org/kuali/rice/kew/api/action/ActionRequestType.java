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

@XmlRootElement(name = "actionRequestType")
@XmlType(name = "ActionRequestTypeType")
@XmlEnum
public enum ActionRequestType implements Coded {

	@XmlEnumValue("C") COMPLETE("C"),
	@XmlEnumValue("A") APPROVE("A"),
	@XmlEnumValue("K") ACKNOWLEDGE("K"),
	@XmlEnumValue("F") FYI("F");
	
	private final String code;
	
	ActionRequestType(String code) {
		this.code = code;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	public String getLabel() {
		return name();
	}
	
	public static ActionRequestType fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (ActionRequestType request : values()) {
			if (request.code.equals(code)) {
				return request;
			}
		}
		throw new IllegalArgumentException("Failed to locate the ActionRequestType with the given code: " + code);
	}
	
}
