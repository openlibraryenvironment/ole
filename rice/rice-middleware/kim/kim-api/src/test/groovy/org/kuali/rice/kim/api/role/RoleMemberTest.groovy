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
package org.kuali.rice.kim.api.role

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import org.kuali.rice.core.api.membership.MemberType

class RoleMemberTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    static final String ROLE_MEMBER_ID = "1";
    static final String ROLE_ID = "23";
    static final Map<String, String> ATTRIBUTES = [:]
    static final List<RoleResponsibilityAction.Builder> ROLE_RESPONSIBILITY_ACTIONS = [create_rra_builder()];
    static final String MEMBER_NAME = "Spock";
    static final String MEMBER_NAMESPACE_CODE = "KUALI";
    static final String MEMBER_ID = "42";
    static final MemberType MEMBER_TYPE = MemberType.GROUP
    static final String MEMBER_TYPE_CODE = "G";

    static final String ACTIVE_FROM_STRING = "2011-01-01 12:00:00"
    static final DateTime ACTIVE_FROM = new DateTime(FORMATTER.parseDateTime(ACTIVE_FROM_STRING))
    static final String ACTIVE_TO_STRING = "2012-01-01 12:00:00"
    static final DateTime ACTIVE_TO = new DateTime(FORMATTER.parseDateTime(ACTIVE_TO_STRING))
    static final boolean ACTIVE = true;

    private static create_rra_builder() {
        RoleResponsibilityAction.Builder b = RoleResponsibilityAction.Builder.create()
        b.id = RoleResponsibilityActionTest.ID
        b.roleResponsibilityId = RoleResponsibilityActionTest.ROLE_RESPONSIBILITY_ID
        b.roleMemberId = RoleResponsibilityActionTest.ROLE_MEMBER_ID
        b.actionTypeCode = RoleResponsibilityActionTest.ACTION_TYPE_CODE
        b.actionPolicyCode = RoleResponsibilityActionTest.ACTION_POLICY_CODE
        b.forceAction = RoleResponsibilityActionTest.FORCE_ACTION
        b.priorityNumber = RoleResponsibilityActionTest.PRIORITY_NUMBER
        b.roleResponsibility = RoleResponsibilityActionTest.ROLE_RESPONSIBILITY
        b.versionNumber = RoleResponsibilityActionTest.VERSION_NUMBER
        return b;
    }

    private static final String XML = """
       <roleMember xmlns="http://rice.kuali.org/kim/v2_0">
         <id>${ROLE_MEMBER_ID}</id>
         <roleId>${ROLE_ID}</roleId>
         <attributes/>
         <roleResponsibilityActions>
           <roleResponsibilityAction>
            <id>${RoleResponsibilityActionTest.ID}</id>
            <roleResponsibilityId>${RoleResponsibilityActionTest.ROLE_RESPONSIBILITY_ID}</roleResponsibilityId>
            <roleMemberId>${RoleResponsibilityActionTest.ROLE_MEMBER_ID}</roleMemberId>
            <actionTypeCode>${RoleResponsibilityActionTest.ACTION_TYPE_CODE}</actionTypeCode>
            <actionPolicyCode>${RoleResponsibilityActionTest.ACTION_POLICY_CODE}</actionPolicyCode>
            <forceAction>${RoleResponsibilityActionTest.FORCE_ACTION}</forceAction>
            <priorityNumber>${RoleResponsibilityActionTest.PRIORITY_NUMBER}</priorityNumber>
            <roleResponsibility>
                <roleResponsibilityId>${RoleResponsibilityActionTest.ROLE_RESPONSIBILITY_ID}</roleResponsibilityId>
                <roleId>5</roleId>
                <responsibilityId>10</responsibilityId>
                <active>true</active>
                <versionNumber>1</versionNumber>
            </roleResponsibility>
            <versionNumber>1</versionNumber>
          </roleResponsibilityAction>
         </roleResponsibilityActions>
         <memberId>${MEMBER_ID}</memberId>
         <typeCode>${MEMBER_TYPE_CODE}</typeCode>
         <memberName></memberName>
         <memberNamespaceCode></memberNamespaceCode>
         <activeFromDate>${ACTIVE_FROM}</activeFromDate>
         <activeToDate>${ACTIVE_TO}</activeToDate>
         <active>${ACTIVE}</active>
       </roleMember>
    """

    @Test
    void testXmlUnmarshal() {
        List<RoleResponsibilityAction> roleResponsibilityActions = new ArrayList<RoleResponsibilityAction>()
        for (RoleResponsibilityAction.Builder rraBuilder: ROLE_RESPONSIBILITY_ACTIONS) {
            roleResponsibilityActions.add(rraBuilder.build());
        }
        System.out.println(XML);
        JAXBContext jc = JAXBContext.newInstance(RoleMember.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        RoleMember roleMember = (RoleMember) unmarshaller.unmarshal(new StringReader(XML))
        System.out.println(roleMember)
        Assert.assertEquals(ROLE_MEMBER_ID, roleMember.id)
        Assert.assertEquals(ROLE_ID, roleMember.roleId)
        Assert.assertEquals(ATTRIBUTES, roleMember.attributes)
        Assert.assertEquals(roleResponsibilityActions, roleMember.roleRspActions)
        Assert.assertEquals(MEMBER_ID, roleMember.memberId)
        Assert.assertEquals(MEMBER_TYPE_CODE, roleMember.getType().code)
        Assert.assertEquals(ACTIVE_FROM, roleMember.activeFromDate)
        Assert.assertEquals(ACTIVE_TO, roleMember.activeToDate)
    }

    @Test
    void testXmlMarshalingAndUnMarshalling() {
        JAXBContext jc = JAXBContext.newInstance(RoleMember.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()

        RoleMember.Builder roleMember = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "")
        roleMember.setRoleRspActions(ROLE_RESPONSIBILITY_ACTIONS)

        marshaller.marshal(roleMember.build(), sw)
        String xml = sw.toString()

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml))
        Object expected = unmarshaller.unmarshal(new StringReader(XML))
        Assert.assertEquals(expected, actual)
    }

    @Test
    void test_Builder_create() {
        RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "").build()
    }

    @Test
    void test_Builder_createWithContract() {
        RoleMemberContract rmc = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "").build()
        RoleMember rm = RoleMember.Builder.create(rmc).build();
        Assert.assertEquals(rm, rmc);
    }

    @Test
    void test_Builder_memberIdNull() {
        RoleMember.Builder b = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "")
        shouldFail(IllegalArgumentException) {
            b.memberId = null
        }
    }

    @Test
    void test_Builder_memberIdBlank() {
        RoleMember.Builder b = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "")
        shouldFail(IllegalArgumentException) {
            b.memberId = ""
        }
    }

    @Test
    void test_Builder_memberTypeCodeNull() {
        RoleMember.Builder b = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "")
        shouldFail(IllegalArgumentException) {
            b.type = null
        }
    }

    @Test
    void test_Builder_memberTypeCodeBlank() {
        RoleMember.Builder b = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "")
        shouldFail(IllegalArgumentException) {
            b.type = ""
        }
    }

    @Test
    void test_isActiveTimestamp_Between() {
        String betweenDate = "2011-06-01 12:00:00"
        DateTime betweenDateTime = FORMATTER.parseDateTime(betweenDate)

        RoleMember roleMember =
            RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "").build()

        Assert.assertTrue(roleMember.isActive(betweenDateTime))
    }

    @Test
    void test_isActiveTimestamp_Before() {
        String betweenDate = "2010-06-01 12:00:00"
        DateTime betweenDateTime = FORMATTER.parseDateTime(betweenDate)

        RoleMember roleMember =
            RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "").build()

        Assert.assertFalse(roleMember.isActive(betweenDateTime))
    }

    @Test
    void test_isActiveTimestamp_After() {
        String betweenDate = "2013-06-01 12:00:00"
        DateTime betweenDateTime = FORMATTER.parseDateTime(betweenDate)

        RoleMember roleMember =
            RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM, ACTIVE_TO, ATTRIBUTES, "", "").build()

        Assert.assertFalse(roleMember.isActive(betweenDateTime))
    }
}
