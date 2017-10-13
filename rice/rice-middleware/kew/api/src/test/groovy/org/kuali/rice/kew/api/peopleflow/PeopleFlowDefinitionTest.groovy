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

import org.junit.Test
import static org.junit.Assert.*
import org.kuali.rice.core.test.JAXBAssert
import org.kuali.rice.core.api.membership.MemberType

/**
 * Unit test for PeopleFlowDefinition
 */
class PeopleFlowDefinitionTest {

    private static final String NAMESPACE_CODE = "MyNamespace"
    private static final String NAME = "MyPeopleFlow"
    private static final String ID = "1"
    private static final String ATTRIBUTE_KEY = "key"
    private static final String ATTRIBUTE_VALUE = "value"
    private static final String DESCRIPTION = "desc"
    private static final String TYPE_ID = "2"
    private static final Long VERSION_NUMBER = 1
    private static final String MEMBER_ID = "admin"

    private static final String MINIMAL_XML = """
    <peopleFlowDefinition xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kew/v2_0">
      <namespaceCode>MyNamespace</namespaceCode>
      <name>MyPeopleFlow</name>
      <members/>
      <attributes/>
      <active>true</active>
    </peopleFlowDefinition>
    """

    private static final String MAXIMAL_XML = """
    <peopleFlowDefinition xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kew/v2_0">
      <id>1</id>
      <namespaceCode>MyNamespace</namespaceCode>
      <name>MyPeopleFlow</name>
      <typeId>2</typeId>
      <description>desc</description>
      <members>
        <member>
          <memberId>admin</memberId>
          <memberType>P</memberType>
          <priority>1</priority>
        </member>
      </members>
      <attributes>
        <ns2:entry key="key">value</ns2:entry>
      </attributes>
      <active>true</active>
      <versionNumber>1</versionNumber>
    </peopleFlowDefinition>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_namespace_and_name() {
        PeopleFlowDefinition.Builder.create(null, null)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_namespace() {
        PeopleFlowDefinition.Builder.create(null, "name")
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_empty_namespace() {
        PeopleFlowDefinition.Builder.create("", "name")
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_blank_namespace() {
        PeopleFlowDefinition.Builder.create("  ", "name")
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_null_name() {
        PeopleFlowDefinition.Builder.create("namespace", null)
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_empty_name() {
        PeopleFlowDefinition.Builder.create("namespace", "")
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_invalid_blank_name() {
        PeopleFlowDefinition.Builder.create("namespace", "  ")
    }

    @Test
    void test_Builder_minimal() {
        PeopleFlowDefinition.Builder builder = createMinimal()
        assertNotNull builder
        assert NAMESPACE_CODE == builder.getNamespaceCode()
        assert NAME == builder.getName()

        // should be initialized to empty map
        assertNotNull builder.getAttributes()
        assert builder.getAttributes().isEmpty()
        // should be initialized to empty list
        assertNotNull builder.getMembers()
        assert builder.getMembers().isEmpty()
        // should be initialized to true
        assert builder.isActive()

        PeopleFlowDefinition flow = builder.build()
        assertNotNull flow
        assert NAMESPACE_CODE == flow.getNamespaceCode()
        assert NAME == flow.getName()
        assert flow.getAttributes().isEmpty()
        assert flow.getMembers().isEmpty()
        assert flow.isActive()
        assertNull flow.getDescription()
        assertNull flow.getId()
        assertNull flow.getTypeId()
        assertNull flow.getVersionNumber()
    }

    @Test
    void test_Builder_maximal() {
        PeopleFlowDefinition.Builder builder = createMaximal()
        PeopleFlowDefinition flow = builder.build()
        assertNotNull flow
        assert NAMESPACE_CODE == flow.getNamespaceCode()
        assert NAME == flow.getName()
        assert ID == flow.getId()
        assert ATTRIBUTE_VALUE == flow.getAttributes().get(ATTRIBUTE_KEY)
        assert DESCRIPTION == flow.getDescription()
        assert TYPE_ID == flow.getTypeId()
        assert VERSION_NUMBER == flow.getVersionNumber()

        assert !flow.getMembers().isEmpty()
        assert 1 == flow.getMembers().size()
        PeopleFlowMember member = flow.getMembers()[0]
        assert MEMBER_ID == member.getMemberId()
        assert MemberType.PRINCIPAL == member.getMemberType()
    }

    @Test
    void test_Builder_contract() {
        PeopleFlowDefinition flow = createMaximal().build();
        PeopleFlowDefinition.Builder builder = PeopleFlowDefinition.Builder.create(flow)
        PeopleFlowDefinition flowCopy = builder.build()
        assert flow == flowCopy
    }

    @Test
	void test_Xml_Marshal_Unmarshal_minimal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMinimal().build(), MINIMAL_XML, PeopleFlowDefinition.class)
	}

    @Test
	void test_Xml_Marshal_Unmarshal_maximal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(createMaximal().build(), MAXIMAL_XML, PeopleFlowDefinition.class)
	}

    private PeopleFlowDefinition.Builder createMinimal() {
        return PeopleFlowDefinition.Builder.create(NAMESPACE_CODE, NAME)
    }

    private PeopleFlowDefinition.Builder createMaximal() {
        PeopleFlowDefinition.Builder builder = createMinimal()
        builder.setId(ID)
        builder.getAttributes().put(ATTRIBUTE_KEY, ATTRIBUTE_VALUE)
        builder.setDescription(DESCRIPTION)
        builder.setTypeId(TYPE_ID)
        builder.setVersionNumber(VERSION_NUMBER)

        PeopleFlowMember.Builder memberBuilder = PeopleFlowMember.Builder.create(MEMBER_ID, MemberType.PRINCIPAL)
        builder.setMembers([memberBuilder])

        return builder
    }

}