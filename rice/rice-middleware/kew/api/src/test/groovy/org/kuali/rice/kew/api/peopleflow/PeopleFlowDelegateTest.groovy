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
 * Unit test for PeopleFlowDelegate
 */
class PeopleFlowDelegateTest {

    private final shouldFail = new GroovyTestCase().&shouldFail

    private static final String MINIMAL_XML = """
    <peopleFlowDelegate xmlns="http://rice.kuali.org/kew/v2_0">
        <memberId>admin</memberId>
        <memberType>P</memberType>
        <delegationType>S</delegationType>
    </peopleFlowDelegate>
    """

    private static final String MAXIMAL_XML = """
    <peopleFlowDelegate xmlns="http://rice.kuali.org/kew/v2_0">
        <memberId>admin</memberId>
        <memberType>P</memberType>
        <delegationType>P</delegationType>
        <responsibilityId>500</responsibilityId>
    </peopleFlowDelegate>
    """

    private static final String MAXIMAL_ROLE_XML = """
    <peopleFlowDelegate xmlns="http://rice.kuali.org/kew/v2_0">
        <memberId>1234</memberId>
        <memberType>R</memberType>
        <actionRequestPolicy>F</actionRequestPolicy>
        <delegationType>P</delegationType>
        <responsibilityId>501</responsibilityId>
    </peopleFlowDelegate>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_memberId() {
        PeopleFlowDelegate.Builder.create(null, MemberType.PRINCIPAL)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_empty_memberId() {
        PeopleFlowDelegate.Builder.create("", MemberType.PRINCIPAL)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_blank_memberId() {
        PeopleFlowDelegate.Builder.create("  ", MemberType.PRINCIPAL)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_memberType() {
        PeopleFlowDelegate.Builder.create("admin", null)
    }

    @Test
    void test_Builder_invalid_null_delegationType() {
        def builder = PeopleFlowDelegate.Builder.create("admin", MemberType.PRINCIPAL)
        shouldFail(IllegalArgumentException) {
            builder.setDelegationType(null)
        }
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_contract() {
        PeopleFlowDelegate.Builder.create(null)
    }

    @Test
    void test_Builder_minimal() {
        PeopleFlowDelegate.Builder builder = createMinimal()
        Assert.assertNotNull(builder)
        assert "admin" == builder.getMemberId()
        assert MemberType.PRINCIPAL == builder.getMemberType()
        // should be initialized to default value of secondary delegation
        assert DelegationType.SECONDARY == builder.getDelegationType()

        PeopleFlowDelegate delegate = builder.build()
        Assert.assertNotNull(delegate)
        assert "admin" == delegate.getMemberId()
        assert MemberType.PRINCIPAL == delegate.getMemberType()
        assert DelegationType.SECONDARY == delegate.getDelegationType()
    }

    @Test
    void test_Builder_maximal() {
        PeopleFlowDelegate.Builder builder = createMaximal()
        PeopleFlowDelegate delegate = builder.build()
        Assert.assertNotNull(delegate)
        assert "admin" == delegate.getMemberId()
        assert MemberType.PRINCIPAL == delegate.getMemberType()
        assert DelegationType.PRIMARY == delegate.getDelegationType()
        assert "500" == delegate.getResponsibilityId()
    }

    @Test
    void test_Builder_maximal_role() {
        PeopleFlowDelegate.Builder builder = createMaximal_role()
        PeopleFlowDelegate delegate = builder.build()
        Assert.assertNotNull(delegate)
        assert "1234" == delegate.getMemberId()
        assert MemberType.ROLE == delegate.getMemberType()
        assert ActionRequestPolicy.FIRST == delegate.getActionRequestPolicy()
        assert DelegationType.PRIMARY == delegate.getDelegationType()
        assert "501" == delegate.getResponsibilityId()
    }

    @Test
    void test_Builder_contract() {
        PeopleFlowDelegate delegate = createMaximal().build()
        assert delegate == PeopleFlowDelegate.Builder.create(delegate).build()
    }

    @Test
	void test_Xml_Marshal_Unmarshal_minimal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMinimal().build(), MINIMAL_XML, PeopleFlowDelegate.class)
	}

    @Test
	void test_Xml_Marshal_Unmarshal_maximal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMaximal().build(), MAXIMAL_XML, PeopleFlowDelegate.class)
	}

    private PeopleFlowDelegate.Builder createMinimal() {
        return PeopleFlowDelegate.Builder.create("admin", MemberType.PRINCIPAL)
    }

    private PeopleFlowDelegate.Builder createMaximal() {
        PeopleFlowDelegate.Builder builder = PeopleFlowDelegate.Builder.create("admin", MemberType.PRINCIPAL)
        builder.setDelegationType(DelegationType.PRIMARY)
        builder.setResponsibilityId("500")
        return builder
    }

    private PeopleFlowDelegate.Builder createMaximal_role() {
        PeopleFlowDelegate.Builder builder = PeopleFlowDelegate.Builder.create("1234", MemberType.ROLE)
        builder.setActionRequestPolicy(ActionRequestPolicy.FIRST)
        builder.setDelegationType(DelegationType.PRIMARY)
        builder.setResponsibilityId("501")
        return builder
    }

}