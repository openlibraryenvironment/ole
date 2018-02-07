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
package org.kuali.rice.kew.api.peopleflow

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert
import org.kuali.rice.core.api.delegation.DelegationType
import org.kuali.rice.kew.api.action.ActionRequestPolicy
import org.kuali.rice.core.api.membership.MemberType

/**
 * Unit test for PeopleFlowMember
 */
class PeopleFlowMemberTest {

    private static final String MINIMAL_XML = """
    <peopleFlowMember xmlns="http://rice.kuali.org/kew/v2_0">
        <memberId>admin</memberId>
        <memberType>P</memberType>
        <priority>1</priority>
        <delegates/>
    </peopleFlowMember>
    """

    private static final String MAXIMAL_XML = """
    <peopleFlowMember xmlns="http://rice.kuali.org/kew/v2_0">
        <memberId>admin</memberId>
        <memberType>P</memberType>
        <priority>10</priority>
        <delegates>
            <delegate>
                <memberId>ewestfal</memberId>
                <memberType>P</memberType>
                <delegationType>S</delegationType>
            </delegate>
            <delegate>
                <memberId>1</memberId>
                <memberType>G</memberType>
                <delegationType>P</delegationType>
            </delegate>
        </delegates>
    </peopleFlowMember>
    """

    private static final String MAXIMAL_WITH_ROLES_XML = """
    <peopleFlowMember xmlns="http://rice.kuali.org/kew/v2_0">
        <memberId>5</memberId>
        <memberType>R</memberType>
        <actionRequestPolicy>F</actionRequestPolicy>
        <responsibilityId>123</responsibilityId>
        <priority>5</priority>
        <delegates>
            <delegate>
                <memberId>ewestfal</memberId>
                <memberType>P</memberType>
                <delegationType>S</delegationType>
                <responsibilityId>1</responsibilityId>
            </delegate>
            <delegate>
                <memberId>1</memberId>
                <memberType>G</memberType>
                <delegationType>P</delegationType>
                <responsibilityId>2</responsibilityId>
            </delegate>
            <delegate>
                <memberId>2</memberId>
                <memberType>R</memberType>
                <actionRequestPolicy>A</actionRequestPolicy>
                <delegationType>P</delegationType>
                <responsibilityId>3</responsibilityId>
            </delegate>
        </delegates>
    </peopleFlowMember>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_memberId() {
        PeopleFlowMember.Builder.create(null, MemberType.PRINCIPAL)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_memberType() {
        PeopleFlowMember.Builder.create("admin", null)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_contract() {
        PeopleFlowMember.Builder.create(null)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_priority() {
        PeopleFlowMember.Builder builder = createMinimal()
        builder.setPriority(-1)
    }

    @Test
    void test_Builder_minimal() {
        PeopleFlowMember.Builder builder = createMinimal()
        Assert.assertNotNull(builder)
        assert "admin" == builder.getMemberId()
        assert MemberType.PRINCIPAL == builder.getMemberType()
        // should be initialized to default
        assert 1 == builder.getPriority()
        assert builder.getDelegates().isEmpty()

        PeopleFlowMember member = builder.build()
        Assert.assertNotNull(member)
        assert "admin" == member.getMemberId()
        assert MemberType.PRINCIPAL == member.getMemberType()
        assert 1 == member.getPriority()
        assert member.getDelegates().isEmpty()
    }

    @Test
    void test_Builder_maximal() {
        PeopleFlowMember.Builder builder = createMaximal()
        PeopleFlowMember member = builder.build()
        Assert.assertNotNull(member)
        assert "admin" == member.getMemberId()
        assert MemberType.PRINCIPAL == member.getMemberType()
        assert 10 == member.getPriority()
        assert 2 == member.getDelegates().size()

        def delegates = member.getDelegates().findAll { it.memberType == MemberType.PRINCIPAL }
        assert 1 == delegates.size()
        assert DelegationType.SECONDARY == delegates[0].delegationType
        assert "ewestfal" == delegates[0].memberId

        delegates = member.getDelegates().findAll { it.memberType == MemberType.GROUP }
        assert 1 == delegates.size()
        assert DelegationType.PRIMARY == delegates[0].delegationType
        assert "1" == delegates[0].memberId
    }

    @Test
    void test_Builder_maximal_withRoles() {
        PeopleFlowMember.Builder builder = createMaximal_withRoles()
        PeopleFlowMember member = builder.build()
        Assert.assertNotNull(member)
        assert "5" == member.getMemberId()
        assert MemberType.ROLE == member.getMemberType()
        assert 5 == member.getPriority()
        assert ActionRequestPolicy.FIRST == member.getActionRequestPolicy()
        assert "123" == member.getResponsibilityId()
        assert 3 == member.getDelegates().size()

        def delegates = member.getDelegates().findAll { it.memberType == MemberType.PRINCIPAL }
        assert 1 == delegates.size()
        assert DelegationType.SECONDARY == delegates[0].delegationType
        assert "ewestfal" == delegates[0].memberId
        assert "1" == delegates[0].responsibilityId
        Assert.assertNull(delegates[0].actionRequestPolicy)

        delegates = member.getDelegates().findAll { it.memberType == MemberType.GROUP }
        assert 1 == delegates.size()
        assert DelegationType.PRIMARY == delegates[0].delegationType
        assert "1" == delegates[0].memberId
        assert "2" == delegates[0].responsibilityId
        Assert.assertNull(delegates[0].actionRequestPolicy)

        delegates = member.getDelegates().findAll { it.memberType == MemberType.ROLE }
        assert 1 == delegates.size()
        assert DelegationType.PRIMARY == delegates[0].delegationType
        assert "2" == delegates[0].memberId
        assert "3" == delegates[0].responsibilityId
        assert ActionRequestPolicy.ALL == delegates[0].actionRequestPolicy

    }

    @Test
    void test_Builder_contract() {
        PeopleFlowMember member = createMaximal().build()
        assert member == PeopleFlowMember.Builder.create(member).build()
    }

    @Test
	void test_Xml_Marshal_Unmarshal_minimal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMinimal().build(), MINIMAL_XML, PeopleFlowMember.class)
	}

    @Test
	void test_Xml_Marshal_Unmarshal_maximal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMaximal().build(), MAXIMAL_XML, PeopleFlowMember.class)
	}

    @Test
	void test_Xml_Marshal_Unmarshal_maximal_withRoles() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMaximal_withRoles().build(), MAXIMAL_WITH_ROLES_XML, PeopleFlowMember.class)
	}

    private PeopleFlowMember.Builder createMinimal() {
        return PeopleFlowMember.Builder.create("admin", MemberType.PRINCIPAL)
    }

    private PeopleFlowMember.Builder createMaximal() {
        PeopleFlowMember.Builder builder = PeopleFlowMember.Builder.create("admin", MemberType.PRINCIPAL)
        builder.setPriority(10)
        PeopleFlowDelegate.Builder delegate1 = PeopleFlowDelegate.Builder.create("ewestfal", MemberType.PRINCIPAL)
        PeopleFlowDelegate.Builder delegate2 = PeopleFlowDelegate.Builder.create("1", MemberType.GROUP)
        delegate2.setDelegationType(DelegationType.PRIMARY)
        builder.getDelegates().add(delegate1)
        builder.getDelegates().add(delegate2)
        return builder
    }

    private PeopleFlowMember.Builder createMaximal_withRoles() {
        PeopleFlowMember.Builder builder = PeopleFlowMember.Builder.create("5", MemberType.ROLE)
        builder.setActionRequestPolicy(ActionRequestPolicy.FIRST)
        builder.setResponsibilityId("123")
        builder.setPriority(5)
        PeopleFlowDelegate.Builder delegate1 = PeopleFlowDelegate.Builder.create("ewestfal", MemberType.PRINCIPAL)
        delegate1.setResponsibilityId("1")
        PeopleFlowDelegate.Builder delegate2 = PeopleFlowDelegate.Builder.create("1", MemberType.GROUP)
        delegate2.setResponsibilityId("2")
        delegate2.setDelegationType(DelegationType.PRIMARY)
        PeopleFlowDelegate.Builder delegate3 = PeopleFlowDelegate.Builder.create("2", MemberType.ROLE)
        delegate3.setActionRequestPolicy(ActionRequestPolicy.ALL)
        delegate3.setDelegationType(DelegationType.PRIMARY)
        delegate3.setResponsibilityId("3")
        builder.getDelegates().add(delegate1)
        builder.getDelegates().add(delegate2)
        builder.getDelegates().add(delegate3)
        return builder
    }

}