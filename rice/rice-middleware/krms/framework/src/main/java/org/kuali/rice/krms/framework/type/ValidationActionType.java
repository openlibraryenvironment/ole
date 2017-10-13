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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.core.api.mo.common.Coded;

import java.util.HashSet;
import java.util.Set;

/**
 * enum used to specify the action type to be specified in the vended {@link ValidationAction}s. WARNING ERROR
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public enum ValidationActionType implements Coded {

    /**
     * use this flag with the static factory to get a {@link ValidationActionTypeService} that creates
     * warning actions.
     */
    WARNING("W"),

    /**
     * use this flag with the static factory to get a {@link ValidationActionTypeService} that creates
     * error actions.
     */
    ERROR("E");

    private final String code;

    /**
     * Create a ValidationActionType of the given typeCode
     * @param typeCode - typeCode created ValidationActionType should be of.
     */
    private ValidationActionType(String typeCode) {
        this.code = typeCode;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    /**
     * for each type, check the input with the lowercase version of the type name, and returns any match.
     * @param s the type to retrieve
     * @return the type, or null if a match is not found.
     */
    public static ValidationActionType fromString(String s) {
        for (ValidationActionType type : ValidationActionType.values()) {
            if (type.toString().equals(s.toLowerCase())) {
                return type;
            }
        }
        return null;
    }

    /**
     * for each type, check the input with the uppercase type code, and returns any match.
     * @param code the type to retrieve
     * @return the type, or null if a match is not found.
     */
    public static ValidationActionType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ValidationActionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Failed to locate the ValidationActionType with the given code: " + code);
    }

    /**
     * Set of valid type codes
     */
    public static final Set<String> VALID_TYPE_CODES = new HashSet<String>();
    static {
        for (ValidationActionType type : values()) {
            VALID_TYPE_CODES.add(type.getCode());
        }
    }

    /**
     * The code value for this object.  In general a code value cannot be null or a blank string.
     *
     * @return the code value for this object.
     */
    @Override
    public String getCode() {
        return code;
    }
}
