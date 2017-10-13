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

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kim.api.role.RoleResponsibility
import org.kuali.rice.kim.api.role.RoleResponsibilityAction
import org.apache.commons.lang.builder.EqualsBuilder

class RoleResponsibilityActionBoTest {

    static final String ID = "1";
    static final String ROLE_RESPONSIBILITY_ID = "2";
    static final String ROLE_MEMBER_ID = "3"
    static final String ACTION_TYPE_CODE = "H"
    static final String ACTION_POLICY_CODE = "A";
    static final boolean FORCE_ACTION = true;
    static final Integer PRIORITY_NUMBER = 9
    static final RoleResponsibility ROLE_RESPONSIBILITY;
    static final Long VERSION_NUMBER = 1L;

    static {
        RoleResponsibility.Builder b = RoleResponsibility.Builder.create("5", "10");
        b.setResponsibilityId(ROLE_RESPONSIBILITY_ID);
        b.versionNumber = VERSION_NUMBER
        ROLE_RESPONSIBILITY = b.build()
    }

    @Test
    void test_to() {
        RoleResponsibilityActionBo bo = new RoleResponsibilityActionBo(
                id: ID,
                roleResponsibilityId: ROLE_RESPONSIBILITY_ID,
                roleMemberId: ROLE_MEMBER_ID,
                actionTypeCode: ACTION_TYPE_CODE,
                actionPolicyCode: ACTION_POLICY_CODE,
                forceAction: FORCE_ACTION,
                priorityNumber: PRIORITY_NUMBER,
                roleResponsibility: RoleResponsibilityBo.from(ROLE_RESPONSIBILITY),
                versionNumber: VERSION_NUMBER
        )

        RoleResponsibilityAction immutable = RoleResponsibilityActionBo.to(bo)

        Assert.assertEquals(bo.id, immutable.id)
        Assert.assertEquals(bo.roleResponsibilityId, immutable.roleResponsibilityId)
        Assert.assertEquals(bo.roleMemberId, immutable.roleMemberId)
        Assert.assertEquals(bo.actionTypeCode, immutable.actionTypeCode)
        Assert.assertEquals(bo.actionPolicyCode, immutable.actionPolicyCode)
        Assert.assertEquals(bo.forceAction, immutable.forceAction)
        Assert.assertEquals(bo.priorityNumber, immutable.priorityNumber)
        Assert.assertEquals(RoleResponsibilityBo.to(bo.roleResponsibility), immutable.roleResponsibility)
        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)
    }

    @Test
    void test_from() {
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
        RoleResponsibilityAction immutable = b.build()

        RoleResponsibilityActionBo bo = RoleResponsibilityActionBo.from(immutable)

        Assert.assertEquals(bo.id, immutable.id)
        Assert.assertEquals(bo.roleResponsibilityId, immutable.roleResponsibilityId)
        Assert.assertEquals(bo.roleMemberId, immutable.roleMemberId)
        Assert.assertEquals(bo.actionTypeCode, immutable.actionTypeCode)
        Assert.assertEquals(bo.actionPolicyCode, immutable.actionPolicyCode)
        Assert.assertEquals(bo.forceAction, immutable.forceAction)
        Assert.assertEquals(bo.priorityNumber, immutable.priorityNumber)

        /* Use reflectionEquals on BO field since two instances of a BO are never equal according to .equals() */
        Assert.assertTrue(EqualsBuilder.reflectionEquals(bo.roleResponsibility, RoleResponsibilityBo.from(immutable.roleResponsibility)))

        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)

    }

    @Test
    void test_notEqualWithImmutable() {
        RoleResponsibilityActionBo bo = new RoleResponsibilityActionBo(
                id: ID,
                roleResponsibilityId: ROLE_RESPONSIBILITY_ID,
                roleMemberId: ROLE_MEMBER_ID,
                actionTypeCode: ACTION_TYPE_CODE,
                actionPolicyCode: ACTION_POLICY_CODE,
                forceAction: FORCE_ACTION,
                priorityNumber: PRIORITY_NUMBER,
                roleResponsibility: RoleResponsibilityBo.from(ROLE_RESPONSIBILITY),
                versionNumber: VERSION_NUMBER
        )

        RoleResponsibilityAction immutable = RoleResponsibilityActionBo.to(bo)

        Assert.assertFalse(immutable.equals(bo))
        Assert.assertFalse(bo.equals(immutable))
    }
}
