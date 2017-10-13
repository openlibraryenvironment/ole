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
package org.kuali.rice.kim.impl.membership

import javax.persistence.Column
import org.kuali.rice.kim.impl.common.active.ActiveFromToBo
import org.kuali.rice.core.api.membership.MemberType

public abstract class AbstractMemberBo extends ActiveFromToBo {
    @Column(name="MBR_ID")
	String memberId;

	@Column(name="MBR_TYP_CD")
	private String typeCode;

    /**
     * Direct setter must be present for DD
     * @param typeCode the string member typeCode
     */
    void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    void setType(MemberType type) {
        typeCode = type.getCode()
    }

    MemberType getType() {
        return MemberType.fromCode(typeCode);
    }
}
