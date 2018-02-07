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

class RoleResponsibilityBoTest {

    private static final ROLE_RESPONSIBILITY_ID = "42"
    private static final ROLE_ID = "1"
    private static final RESPONSIBILITY_ID = "13"
    private static final ACTIVE = true
    private static Long VERSION = 1L;

    @Test
    void test_to() {
        RoleResponsibilityBo bo = new RoleResponsibilityBo(
                roleId: ROLE_ID,
                roleResponsibilityId: ROLE_RESPONSIBILITY_ID,
                responsibilityId: RESPONSIBILITY_ID,
                active: ACTIVE,
                versionNumber: VERSION)
        RoleResponsibility immutable = RoleResponsibilityBo.to(bo)

        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.roleResponsibilityId, immutable.roleResponsibilityId)
        Assert.assertEquals(bo.responsibilityId, immutable.responsibilityId)
        Assert.assertEquals(bo.active, immutable.active)
        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)
    }

    @Test
    void test_from() {
        RoleResponsibility.Builder b = RoleResponsibility.Builder.create(ROLE_ID, RESPONSIBILITY_ID)
        b.roleResponsibilityId = ROLE_RESPONSIBILITY_ID
        b.versionNumber = VERSION
        RoleResponsibility immutable = b.build()

        RoleResponsibilityBo bo = RoleResponsibilityBo.from(immutable)

        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.roleResponsibilityId, immutable.roleResponsibilityId)
        Assert.assertEquals(bo.responsibilityId, immutable.responsibilityId)
        Assert.assertEquals(bo.active, immutable.active)
        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)
    }

    @Test
    void test_notEqualsToImmutable() {
        RoleResponsibilityBo bo = new RoleResponsibilityBo(
                roleId: ROLE_ID,
                roleResponsibilityId: ROLE_RESPONSIBILITY_ID,
                responsibilityId: RESPONSIBILITY_ID,
                active: ACTIVE,
                versionNumber: VERSION)
        RoleResponsibility immutable = RoleResponsibilityBo.to(bo)

        Assert.assertFalse(bo.equals(immutable))
        Assert.assertFalse(immutable.equals(bo))
    }
}
