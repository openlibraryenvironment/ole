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
package org.kuali.rice.krms.api.repository.typerelation;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.util.jaxb.EnumStringAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Enumeration for RelationshipTypes.  USAGE_ALLOWED or UNKNOWN.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public enum RelationshipType implements Coded {

    /**
     * use this flag with the static factory to get a {@link RelationshipType} Unknown
     */
    UNKNOWN("U"),

    /**
     * use this flag with the static factory to get a {@link RelationshipType} Usage Allowed
     */
    USAGE_ALLOWED("A");

    private final String code;

    /**
     * Create a RelationshipType of the given code
     * @param code code the RelationshipType should be of.
     */
    private RelationshipType(String code) {
        this.code = code;
    }

    /**
     * Returns the operator code for this evaluation operator.
     *
     * @return the operatorCode
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * Set of valid type codes
     */
    public static final Set<String> VALID_TYPE_CODES = new HashSet<String>();
    static {
        for (RelationshipType relationshipType : values()) {
            VALID_TYPE_CODES.add(relationshipType.getCode());
        }
    }

    /**
     * Create a RelationshipType for the given code
     * @param code to type the RelationshipType
     * @return RelationshipType of the given code
     * @throws IllegalArgumentException if the given code does not exist
     */
    public static RelationshipType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (RelationshipType relationshipType : values()) {
            if (relationshipType.code.equals(code)) {
                return relationshipType;
            }
        }
        throw new IllegalArgumentException("Failed to locate the RelationshipType with the given code: " + code);
    }

    static final class Adapter extends EnumStringAdapter<RelationshipType> {
        @Override
        protected Class<RelationshipType> getEnumClass() {
            return RelationshipType.class;
        }

    }

}
