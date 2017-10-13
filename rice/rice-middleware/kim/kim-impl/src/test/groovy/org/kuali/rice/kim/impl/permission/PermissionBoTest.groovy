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
package org.kuali.rice.kim.impl.permission

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kim.api.permission.Permission
import org.kuali.rice.kim.api.type.KimType
import org.kuali.rice.kim.api.common.template.Template

class PermissionBoTest {
	
	private static final String ID = "50"
	private static final String NAMESPACE_CODE = "KUALI"
	private static final String NAME = "PermissionName"
	private static final Template.Builder TEMPLATE = PermissionTemplateBoTest.create()
	
	@Test
	public void testNotEqualsWithPermission() {
    Permission immutable = Permission.Builder.create(NAMESPACE_CODE, NAME).build()
    PermissionBo bo = PermissionBo.from(immutable)
    Assert.assertFalse(bo.equals(immutable))
    Assert.assertFalse(immutable.equals(bo))
    Assert.assertEquals(immutable, PermissionBo.to(bo))
  }
}
