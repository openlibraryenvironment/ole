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
import org.kuali.rice.kim.api.role.RolePermission

class RolePermissionBoTest {

    private static final String ID = "50"
    private static final String ROLE_ID = "12013"
    private static final String PERMISSION_ID = "93121771551017"

    @Test
    public void testNotEqualsWithImmutable() {
        RolePermission immutable = RolePermission.Builder.create(ID, ROLE_ID, PERMISSION_ID).build()
        RolePermissionBo bo = RolePermissionBo.from(immutable)
        Assert.assertFalse(bo.equals(immutable))
        Assert.assertFalse(immutable.equals(bo))
        Assert.assertEquals(immutable, RolePermissionBo.to(bo))
    }

    @Test
    void test_to() {
        RolePermissionBo bo = new RolePermissionBo(
                id: ID,
                roleId: ROLE_ID,
                permissionId: PERMISSION_ID,
                active: true,
                versionNumber: 1L
        )
        RolePermission immutable = RolePermissionBo.to(bo)
        Assert.assertEquals(bo.id, immutable.id)
        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.active, immutable.active)
        Assert.assertEquals(bo.permissionId, immutable.permissionId)
        Assert.assertEquals(bo.versionNumber, immutable.versionNumber)
    }

    @Test
    void test_from() {
        RolePermission immutable = RolePermission.Builder.create(ID, ROLE_ID, PERMISSION_ID).build()
        RolePermissionBo bo = RolePermissionBo.from(immutable)
        Assert.assertEquals(bo.id, immutable.id)
        Assert.assertEquals(bo.roleId, immutable.roleId)
        Assert.assertEquals(bo.active, immutable.active)
        Assert.assertEquals(bo.permissionId, immutable.permissionId)
    }


}
