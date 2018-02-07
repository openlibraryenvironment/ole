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
package org.kuali.rice.kim.api.role;


import org.junit.Test
import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import org.junit.Assert

public class RoleResponsibilityActionTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

     static final String ID = "1";
     static final String ROLE_RESPONSIBILITY_ID = "2";
     static final String ROLE_MEMBER_ID = "3"
     static final String ACTION_TYPE_CODE = "H"
     static final String ACTION_POLICY_CODE = "A";
     static final boolean FORCE_ACTION = true;
     static final Integer PRIORITY_NUMBER = 9
     static final RoleResponsibility ROLE_RESPONSIBILITY;
     static final Long VERSION_NUMBER = 1L;

    private static final String XML = """
      <roleResponsibilityAction xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <roleResponsibilityId>${ROLE_RESPONSIBILITY_ID}</roleResponsibilityId>
        <roleMemberId>${ROLE_MEMBER_ID}</roleMemberId>
        <actionTypeCode>${ACTION_TYPE_CODE}</actionTypeCode>
        <actionPolicyCode>${ACTION_POLICY_CODE}</actionPolicyCode>
        <forceAction>${FORCE_ACTION}</forceAction>
        <priorityNumber>${PRIORITY_NUMBER}</priorityNumber>
        <roleResponsibility>
            <roleResponsibilityId>${ROLE_RESPONSIBILITY_ID}</roleResponsibilityId>
            <roleId>5</roleId>
            <responsibilityId>10</responsibilityId>
            <active>true</active>
            <versionNumber>1</versionNumber>
        </roleResponsibility>
        <versionNumber>1</versionNumber>
      </roleResponsibilityAction>
    """

    static {
        RoleResponsibility.Builder b = RoleResponsibility.Builder.create("5", "10");
        b.setRoleResponsibilityId(ROLE_RESPONSIBILITY_ID);
        b.versionNumber = VERSION_NUMBER
        ROLE_RESPONSIBILITY = b.build()
    }

    @Test
    public void testXmlUnmarshal() {
        JAXBContext jc = JAXBContext.newInstance(RoleResponsibilityAction.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        RoleResponsibilityAction rra = (RoleResponsibilityAction) unmarshaller.unmarshal(new StringReader(XML))

        Assert.assertEquals(ID,rra.id)
        Assert.assertEquals(ROLE_RESPONSIBILITY_ID,rra.roleResponsibilityId)
        Assert.assertEquals(ROLE_MEMBER_ID,rra.roleMemberId)
        Assert.assertEquals(ACTION_TYPE_CODE,rra.actionTypeCode)
        Assert.assertEquals(ACTION_POLICY_CODE,rra.actionPolicyCode)
        Assert.assertEquals(FORCE_ACTION,rra.forceAction)
        Assert.assertEquals(PRIORITY_NUMBER,rra.priorityNumber)
        Assert.assertEquals(ROLE_RESPONSIBILITY, rra.roleResponsibility)
        Assert.assertEquals(VERSION_NUMBER, rra.versionNumber)
    }

    @Test
    public void test_Builder() {
        RoleResponsibilityAction.Builder b = RoleResponsibilityAction.Builder.create()
        b.id = ID
        b.roleResponsibilityId = ROLE_RESPONSIBILITY_ID
        b.roleMemberId = ROLE_MEMBER_ID
        b.actionTypeCode = ACTION_TYPE_CODE
        b.actionPolicyCode = ACTION_POLICY_CODE
        b.forceAction = FORCE_ACTION
        b.priorityNumber = PRIORITY_NUMBER
        b.roleResponsibility = ROLE_RESPONSIBILITY
        b.versionNumber = VERSION_NUMBER
        b.build()
    }

    @Test
    public void test_Builder_only_required() {
        RoleResponsibilityAction.Builder b = RoleResponsibilityAction.Builder.create()
        b.id = ID
        b.versionNumber = VERSION_NUMBER
        b.build()
    }

    @Test
    public void test_Builder_blank_id() {
        RoleResponsibilityAction.Builder b = RoleResponsibilityAction.Builder.create()
        shouldFail(IllegalArgumentException) {b.id = "  "}
    }

}
