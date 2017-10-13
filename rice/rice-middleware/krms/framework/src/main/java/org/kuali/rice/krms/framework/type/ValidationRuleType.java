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
 * enum used to specify the validationRule type to be specified in the vended {@link ValidationRule}s. INVALID VALID
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public enum ValidationRuleType implements Coded {
    /**
     * use this flag with the static factory to get a ValidationRuleTypeService} that creates
     * warning validationRules.
     */
    INVALID("I"),

    /**
     * use this flag with the static factory to get a ValidationRuleTypeService} that creates
     * error validationRules.
     */
    VALID("V");

    private final String code;

    /**
     * Create a ValdationRuleTye of the given code
     * @param code
     */
    private ValidationRuleType(String code) {
        this.code = code;
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
    public static ValidationRuleType fromString(String s) {
        for (ValidationRuleType type : ValidationRuleType.values()) {
            if (type.toString().equals(s.toLowerCase())) {
                return type;
            }
        }
        return null;
    }

    /**
     * for each type, check the input with the uppercase version of the type code, and returns any match.
     * @param code the type to retrieve
     * @return the type, or null if a match is not found.
     */
    public static ValidationRuleType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ValidationRuleType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Failed to locate the ValidationRuleType with the given code: " + code);
    }


    /**
     * Set of valid type codes
     */
    public static final Set<String> VALID_TYPE_CODES = new HashSet<String>();
    static {
        for (ValidationRuleType type : values()) {
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
