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
package org.kuali.rice.kim.impl.group

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.kuali.rice.kim.api.group.GroupMember
import org.kuali.rice.kim.api.group.GroupMemberContract
import org.kuali.rice.kim.impl.membership.AbstractMemberBo
import java.sql.Timestamp
import org.joda.time.DateTime

@Entity
@Table(name="KRIM_GRP_MBR_T")
public class GroupMemberBo extends AbstractMemberBo implements GroupMemberContract {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="KRIM_GRP_MBR_ID_S")
    @GenericGenerator(name="KRIM_GRP_MBR_ID_S",strategy="org.kuali.rice.core.framework.persistence.jpa.RiceNumericStringSequenceStyleGenerator",parameters=[
			@Parameter(name="sequence_name",value="KRIM_GRP_MBR_ID_S"),
			@Parameter(name="value_column",value="id")
		])
    @Column(name="GRP_MBR_ID")
	String id;

    @Column(name="GRP_ID")
	String groupId

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static GroupMember to(GroupMemberBo bo) {
        if (bo == null) {
            return null
        }

        return GroupMember.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static GroupMemberBo from(GroupMember im) {
        if (im == null) {
            return null
        }

        GroupMemberBo bo = new GroupMemberBo()
        bo.id = im.id
        bo.groupId = im.groupId
        bo.memberId = im.memberId
        bo.typeCode = im.type.code
        bo.activeFromDateValue = im.activeFromDate == null ? null : new Timestamp(im.activeFromDate.getMillis());
        bo.activeToDateValue = im.activeToDate == null ? null : new Timestamp(im.activeToDate.getMillis());
        bo.versionNumber = im.versionNumber
		bo.objectId = im.objectId;

        return bo
    }

}
