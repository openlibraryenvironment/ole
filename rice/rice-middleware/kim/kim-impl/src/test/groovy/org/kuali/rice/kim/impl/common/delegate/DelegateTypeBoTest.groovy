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
import org.kuali.rice.kim.api.common.delegate.DelegateType
import org.junit.Assert
import org.kuali.rice.kim.api.common.delegate.DelegateMember
import org.kuali.rice.core.api.delegation.DelegationType
import java.sql.Timestamp
import org.kuali.rice.core.api.membership.MemberType
import org.joda.time.DateTime

class DelegateTypeBoTest {
    
    @Test
    public void test_to() {
        DelegateTypeBo bo = new DelegateTypeBo(delegationId: '10001', roleId: '20002', active: true, kimTypeId: '30003',
                delegationTypeCode: 'S', members: getDelegateMemberBos());
        DelegateType immutable = DelegateTypeBo.to(bo)
        Assert.assertEquals(bo.delegationId, immutable.delegationId)
        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.active, immutable.active)
        Assert.assertEquals(bo.kimTypeId, immutable.kimTypeId)
        Assert.assertEquals(bo.delegationTypeCode, immutable.delegationType.code)
        assertMembersEquals(bo.members, immutable.members)
    }

    @Test
    public void test_from() {
        DelegateType.Builder builder = DelegateType.Builder.create('10001', DelegationType.SECONDARY,
                getDelegateMemberBuilders())
        builder.setRoleId('20002')
        builder.setActive(true)
        builder.setKimTypeId('30003')
        DelegateType immutable = builder.build()
        DelegateTypeBo bo = DelegateTypeBo.from(immutable)
        Assert.assertEquals(bo.delegationId, immutable.delegationId)
        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.active, immutable.active)
        Assert.assertEquals(bo.kimTypeId, immutable.kimTypeId)
        Assert.assertEquals(bo.delegationTypeCode, immutable.delegationType.code)
        assertMembersEquals(bo.members, immutable.members)
    }

    @Test
    public void testNotEqualsWithDelegateType() {
        DelegateType.Builder builder = DelegateType.Builder.create('10001', DelegationType.SECONDARY,
                getDelegateMemberBuilders())
        builder.setRoleId('20002')
        builder.setActive(true)
        builder.setKimTypeId('30003')
        DelegateType immutable = builder.build()
        DelegateTypeBo bo = DelegateTypeBo.from(immutable)
        Assert.assertFalse(bo.equals(immutable))
        Assert.assertFalse(immutable.equals(bo))
        Assert.assertEquals(immutable, DelegateTypeBo.to(bo))
    }

    private List<DelegateMember.Builder> getDelegateMemberBuilders() {
        DelegateMember.Builder memberBuilder = DelegateMember.Builder.create()
        memberBuilder.setDelegationMemberId('10101')
        memberBuilder.setDelegationId('20202')
        memberBuilder.setRoleMemberId('30303')
        memberBuilder.setMemberId('40404')
        memberBuilder.setType(MemberType.PRINCIPAL)
        memberBuilder.setVersionNumber(1)
        memberBuilder.setActiveFromDate(new DateTime(1325397600))
        memberBuilder.setActiveToDate(new DateTime(1357020000))
        memberBuilder.setAttributes(['1':'a', '2':'b', '3':'c'])
        List<DelegateMember.Builder> members = new ArrayList<DelegateMember.Builder>()
        members.add(memberBuilder)

        return members
    }
    
    private List<DelegateMemberBo> getDelegateMemberBos() {
        List<DelegateMemberBo> members = new ArrayList<DelegateMemberBo>()
        members.add(new DelegateMemberBo(delegationMemberId: '10101', delegationId: '20202',
                roleMemberId: '30303', memberId: '40404', typeCode: 'P', versionNumber: 1,
                activeFromDateValue: new Timestamp(1325397600), activeToDateValue: new Timestamp(1357020000),
                attributes: ['1':'a', '2':'b', '3':'c']))

        return members
    } 

    private void assertMembersEquals(List<DelegateMemberBo> delegateMemberBos, List<DelegateMember> delegateMembers) {
        Assert.assertEquals(delegateMemberBos.size(), delegateMembers.size())
        for (int i = 0; i < delegateMemberBos.size(); i++) {
            Assert.assertEquals(delegateMemberBos.get(i).delegationMemberId, delegateMembers.get(i).delegationMemberId)
            Assert.assertEquals(delegateMemberBos.get(i).delegationId, delegateMembers.get(i).delegationId)
            Assert.assertEquals(delegateMemberBos.get(i).roleMemberId, delegateMembers.get(i).roleMemberId)
            Assert.assertEquals(delegateMemberBos.get(i).memberId, delegateMembers.get(i).memberId)
            Assert.assertEquals(delegateMemberBos.get(i).type, delegateMembers.get(i).type)
            Assert.assertEquals(delegateMemberBos.get(i).activeFromDateValue.getTime(), delegateMembers.get(i).activeFromDate.getMillis())
            Assert.assertEquals(delegateMemberBos.get(i).activeToDateValue.getTime(), delegateMembers.get(i).activeToDate.getMillis())
            Assert.assertEquals(delegateMemberBos.get(i).attributes, delegateMembers.get(i).attributes)
        }
    }

}
