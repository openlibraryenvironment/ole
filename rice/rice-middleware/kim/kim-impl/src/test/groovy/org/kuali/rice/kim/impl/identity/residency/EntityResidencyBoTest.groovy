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
package org.kuali.rice.kim.impl.identity.residency

import org.junit.Test
import org.kuali.rice.kim.api.identity.residency.EntityResidency

import org.junit.Assert


class EntityResidencyBoTest {
  @Test
  public void testNotEqualsWithEmail() {
      EntityResidency.Builder builder = EntityResidency.Builder.create()
      builder.setId("1")
      builder.setEntityId("10101")
      builder.setInState("TX")
      builder.setDeterminationMethod("No idea what this should be")
      EntityResidency immutable = builder.build()
      EntityResidencyBo bo = EntityResidencyBo.from(immutable )
      Assert.assertFalse(bo.equals(immutable))
      Assert.assertFalse(immutable.equals(bo))
      Assert.assertEquals(immutable, EntityResidencyBo.to(bo))
  }
}
