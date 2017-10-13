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
package org.kuali.rice.core.api.membership;

import org.kuali.rice.core.api.mo.common.Coded;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A MemberType is an enum that represents valid membership type for Role, Group, and Delegate members.
 * This enum is currently slightly overloaded, as only PRINCIPAL and GROUP are valid Group membership types.
 * @see org.kuali.rice.kim.impl.membership.AbstractMemberBo
 * @see org.kuali.rice.kim.impl.group.GroupMemberBo
 * @see org.kuali.rice.kim.impl.role.RoleMemberBo
 * @see org.kuali.rice.kim.impl.common.delegate.DelegateMemberBo
 */
@XmlRootElement(name = "memberType")
@XmlType(name = "MemberTypeType")
@XmlEnum
public enum MemberType implements Coded {

    /**
     * Member type corresponding to KIM roles
     */
    @XmlEnumValue("R") ROLE("R"),

    /**
     * Member type corresponding to KIM groups
     */
    @XmlEnumValue("G") GROUP("G"),

    /**
     * Member type corresponding to KIM principals
     */
    @XmlEnumValue("P") PRINCIPAL("P");

    public final String code;

    private MemberType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public static MemberType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (MemberType memberType : values()) {
            if (memberType.code.equals(code)) {
                return memberType;
            }
        }
        throw new IllegalArgumentException("Failed to locate the MemberType with the given code: " + code);
    }

}
