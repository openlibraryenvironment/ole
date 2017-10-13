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
package org.kuali.rice.kew.framework.document.search;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An enumeration which defines fields that are used on the document search screen and subject to customization by
 * specific document types which configure such customizations via the document search framework.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = "documentSearchResultField")
@XmlType(name = "DocumentSearchResultFieldType")
@XmlEnum
public enum StandardResultField {

    @XmlEnumValue("documentId") DOCUMENT_ID("documentId"),
    @XmlEnumValue("status") STATUS("status", "statusLabel"),
    @XmlEnumValue("documentType") DOCUMENT_TYPE("documentType", "documentTypeLabel"),
    @XmlEnumValue("title") TITLE("title"),
    @XmlEnumValue("initiator") INITIATOR("initiator", "initiatorDisplayName"),
    @XmlEnumValue("dateCreated") DATE_CREATED("dateCreated"),
    @XmlEnumValue("routeLog") ROUTE_LOG("routeLog");

    private final String standardFieldName;
    private final Set<String> additionalFieldNames;

    private StandardResultField(String standardFieldName, String... additionalFieldNames) {
        this.standardFieldName = standardFieldName;
        Set<String> additionalFieldNameSet = new HashSet<String>(Arrays.asList(additionalFieldNames));
        this.additionalFieldNames = Collections.unmodifiableSet(additionalFieldNameSet);
    }

    /**
     * Returns the standard field name of this standard result field as a string.
     *
     * @return the standard result field name
     */
    public String getStandardFieldName() {
        return standardFieldName;
    }

    /**
     * Returns additional field names that are valid and can be used to represent this standard result field as a string.
     *
     * @return a set of additional field names
     */
    public Set<String> getAdditionalFieldNames() {
        return additionalFieldNames;
    }

    /**
     * Returns true if the given field name represents a valid field name for this standard result field.  The given
     * field name is valid if it is the "standard" field name for this result field, or if it is contained withint the
     * set of additional field names.
     *
     * @param fieldName the field name to check
     *
     * @return true if the field name is valid for this result field, false otherwise
     */
    public boolean isFieldNameValid(String fieldName) {
        return fieldName.equals(standardFieldName) || additionalFieldNames.contains(fieldName);
    }

    public static StandardResultField fromFieldName(String fieldName) {
		if (fieldName == null) {
			return null;
		}
		for (StandardResultField resultField : values()) {
            if (resultField.isFieldNameValid(fieldName)) {
                return resultField;
            }
		}
		throw new IllegalArgumentException("Failed to locate the StandardResultField with the field name: " + fieldName);
	}

}
