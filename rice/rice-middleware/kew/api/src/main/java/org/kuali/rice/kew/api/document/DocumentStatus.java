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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.mo.common.Coded;

import java.util.EnumSet;

/**
 * An enumeration representing valid workflow document statuses.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = "documentStatus")
@XmlType(name = "DocumentStatusType")
@XmlEnum
public enum DocumentStatus implements Coded {

	@XmlEnumValue("I") INITIATED("I", DocumentStatusCategory.PENDING),
	@XmlEnumValue("S") SAVED("S", DocumentStatusCategory.PENDING),
	@XmlEnumValue("R") ENROUTE("R", DocumentStatusCategory.PENDING),
    @XmlEnumValue("E") EXCEPTION("E", DocumentStatusCategory.PENDING),
	@XmlEnumValue("P") PROCESSED("P", DocumentStatusCategory.SUCCESSFUL),
	@XmlEnumValue("F") FINAL("F", DocumentStatusCategory.SUCCESSFUL),
	@XmlEnumValue("X") CANCELED("X", DocumentStatusCategory.UNSUCCESSFUL),
	@XmlEnumValue("D") DISAPPROVED("D", DocumentStatusCategory.UNSUCCESSFUL),
    /**
     * When invoked, RECALL & CANCEL action will perform the RECALL and set the route status of the document to the new, terminal status of RECALLED
     * @since 2.1
     */
    @XmlEnumValue("L") RECALLED("L", DocumentStatusCategory.UNSUCCESSFUL);

	private final String code;
    private final DocumentStatusCategory category;

	private DocumentStatus(String code, DocumentStatusCategory category) {
		this.code = code;
        this.category = category;
	}

	@Override
	public String getCode() {
		return code;
	}

    public DocumentStatusCategory getCategory() {
        return category;
    }

	public String getLabel() {
	    return name();
	}

	public static DocumentStatus fromCode(String code) {
		if (code == null) {
			return null;
		}
		for (DocumentStatus status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Failed to locate the DocumentStatus with the given code: " + code);
	}

    public static EnumSet<DocumentStatus> getStatusesForCategory(DocumentStatusCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("category was null");
        }
        EnumSet<DocumentStatus> categoryStatuses = EnumSet.noneOf(DocumentStatus.class);
        for (DocumentStatus status : values()) {
			if (status.category == category) {
				categoryStatuses.add(status);
			}
		}
        return categoryStatuses;
    }

}
