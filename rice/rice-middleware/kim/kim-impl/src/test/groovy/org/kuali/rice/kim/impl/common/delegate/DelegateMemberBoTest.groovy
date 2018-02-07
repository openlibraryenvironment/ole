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
package org.kuali.rice.kim.impl.common.delegate

import org.junit.Test
import org.kuali.rice.kim.api.common.delegate.DelegateMember
import org.junit.Assert
import java.sql.Timestamp
import org.kuali.rice.core.api.membership.MemberType
import org.joda.time.DateTime

class DelegateMemberBoTest {

    @Test
    public void test_to() {
        DelegateMemberBo bo = new DelegateMemberBo(delegationMemberId: '10101', delegationId: '20202',
                roleMemberId: '30303', memberId: '40404', typeCode: 'P', versionNumber: 1,
                activeFromDateValue: new Timestamp(1325397600), activeToDateValue: new Timestamp(1357020000),
                attributes: ['1':'a', '2':'b', '3':'c'])
        DelegateMember immutable = DelegateMemberBo.to(bo)
        Assert.assertEquals(bo.delegationMemberId, immutable.delegationMemberId)
        Assert.assertEquals(bo.delegationId, immutable.delegationId)
        Assert.assertEquals(bo.roleMemberId, immutable.roleMemberId)
        Assert.assertEquals(bo.attributes, immutable.attributes)
        Assert.assertEquals(bo.memberId, immutable.memberId)
        Assert.assertEquals(bo.type, immutable.type)
        Assert.assertEquals(bo.activeFromDateValue.getTime(), immutable.activeFromDate.getMillis())
        Assert.assertEquals(bo.activeToDateValue.getTime(), immutable.activeToDate.getMillis())
        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)
    }

    @Test
    public void test_from() {
        DelegateMember.Builder builder = DelegateMember.Builder.create()
        builder.setDelegationMemberId('10101')
        builder.setDelegationId('20202')
        builder.setRoleMemberId('30303')
        builder.setMemberId('40404')
        builder.setType(MemberType.PRINCIPAL)
        builder.setVersionNumber(1)
        builder.setActiveFromDate(new DateTime(1325397600))
        builder.setActiveToDate(new DateTime(1357020000))
        builder.setAttributes(['1':'a', '2':'b', '3':'c'])
        DelegateMember immutable = builder.build()
        DelegateMemberBo bo = DelegateMemberBo.from(immutable)
        Assert.assertEquals(bo.delegationMemberId, immutable.delegationMemberId)
        Assert.assertEquals(bo.delegationId, immutable.delegationId)
        Assert.assertEquals(bo.roleMemberId, immutable.roleMemberId)
        Assert.assertEquals(bo.attributes, immutable.attributes)
        Assert.assertEquals(bo.memberId, immutable.memberId)
        Assert.assertEquals(bo.type, immutable.type)
        Assert.assertEquals(bo.activeFromDateValue.getTime(), immutable.activeFromDate.getMillis())
        Assert.assertEquals(bo.activeToDateValue.getTime(), immutable.activeToDate.getMillis())
        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)
    }

    @Test
    public void testNotEqualsWithDelegateMember() {
        DelegateMember.Builder builder = DelegateMember.Builder.create()
        builder.setDelegationMemberId('10101')
        builder.setDelegationId('20202')
        builder.setRoleMemberId('30303')
        builder.setMemberId('40404')
        builder.setType(MemberType.PRINCIPAL)
        builder.setVersionNumber(1)
        builder.setActiveFromDate(new DateTime(1325397600))
        builder.setActiveToDate(new DateTime(1357020000))
        builder.setAttributes(['1':'a', '2':'b', '3':'c'])
        DelegateMember immutable = builder.build()
        DelegateMemberBo bo = DelegateMemberBo.from(immutable)
        Assert.assertFalse(bo.equals(immutable))
        Assert.assertFalse((immutable.equals(bo)))
        Assert.assertEquals(immutable, DelegateMemberBo.to(bo))
    }
}