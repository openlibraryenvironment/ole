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
package org.kuali.rice.kim.impl.role

import java.sql.Timestamp
import java.text.SimpleDateFormat
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.kuali.rice.kim.api.role.RoleMember
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import org.kuali.rice.core.api.membership.MemberType

class RoleMemberBoTest {
    static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    static final String ACTIVE_FROM_STRING = "2011-01-01 12:00:00"
    static final String ACTIVE_TO_STRING = "2012-01-01 12:00:00"
    static final DateTime ACTIVE_FROM = FORMATTER.parseDateTime(ACTIVE_FROM_STRING)
    static final DateTime ACTIVE_TO = FORMATTER.parseDateTime(ACTIVE_TO_STRING)
    static final MemberType G = MemberType.GROUP

    @Test
    @Ignore("RoleMemberBo.getAttributes(), called by RoleMemberBo.to(), requires the GRL to use both RoleService and TypeInfoService - not setup for unit tests")
    void test_to() {
        RoleMemberBo bo = new RoleMemberBo(
                id: "1",
                roleId: "2",
                typeCode: "G",
                attributeDetails: [],
                roleRspActions: [],
                memberId: "22",
                activeFromDateValue: new Timestamp(ACTIVE_FROM.getMillis()),
                activeToDateValue: new Timestamp(ACTIVE_TO.getMillis())
        )

        RoleMember immutable = RoleMemberBo.to(bo)
        Assert.assertEquals(bo.id, immutable.id)
        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.typeCode, immutable.typeCode)
        Assert.assertEquals(bo.roleRspActions, immutable.roleRspActions)
        Assert.assertEquals(bo.memberId, immutable.memberId)
        Assert.assertEquals(bo.activeFromDate, immutable.activeFromDate)
        Assert.assertEquals(bo.activeToDate, immutable.activeToDate)
    }

    @Test
    void test_from() {
        RoleMember immutable = RoleMember.Builder.create("23", "1", "42", MemberType.GROUP, ACTIVE_FROM, ACTIVE_TO, [:], "", "").build()
        RoleMemberBo bo = RoleMemberBo.from(immutable)
        Assert.assertEquals(bo.id, immutable.id)
        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.type, immutable.type)
        Assert.assertEquals(bo.roleRspActions, immutable.roleRspActions)
        Assert.assertEquals(bo.memberId, immutable.memberId)
        Assert.assertEquals(bo.activeFromDate, immutable.activeFromDate)
        Assert.assertEquals(bo.activeToDate, immutable.activeToDate)
    }

    @Test
    void test_notEqualToImmutable() {
        RoleMember immutable = RoleMember.Builder.create("23", "1", "42", G, ACTIVE_FROM, ACTIVE_TO, [:], "", "").build()
        RoleMemberBo bo = RoleMemberBo.from(immutable)
        Assert.assertFalse(bo.equals(immutable))
        Assert.assertFalse(immutable.equals(bo))
    }
}
