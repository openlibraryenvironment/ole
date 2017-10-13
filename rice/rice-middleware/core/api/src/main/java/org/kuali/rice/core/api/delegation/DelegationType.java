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
package org.kuali.rice.core.api.delegation;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.mo.common.Coded;

@XmlRootElement(name = "delegationType")
@XmlType(name = "DelegationTypeType")
@XmlEnum
public enum DelegationType implements Coded {

    @XmlEnumValue("P")
    PRIMARY("P", "Primary"),

    @XmlEnumValue("S")
    SECONDARY("S", "Secondary");

    private final String code;
    private final String label;

    DelegationType(String code, String label) {
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

    public static DelegationType fromCode(String code) {
        if (code == null) {
            return null;
        }
        DelegationType value = parseCode(code);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException("Failed to locate the DelegationType with the given code: " + code);
    }

    public static DelegationType parseCode(String code) {
        for (DelegationType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
