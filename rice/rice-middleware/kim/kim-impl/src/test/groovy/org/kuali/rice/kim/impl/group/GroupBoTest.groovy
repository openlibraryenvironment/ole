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
package org.kuali.rice.kim.impl.group

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kim.api.group.Group

class GroupBoTest {
  @Test
  public void testNotEqualsWithGroup() {
    Group immutable = Group.Builder.create("NMSP-CD", "name", "kimType1").build()
    GroupBo bo = GroupBo.from(immutable )
    Assert.assertFalse(bo.equals(immutable))
    Assert.assertFalse(immutable.equals(bo))
    Assert.assertEquals(immutable, GroupBo.to(bo))
  }
}
