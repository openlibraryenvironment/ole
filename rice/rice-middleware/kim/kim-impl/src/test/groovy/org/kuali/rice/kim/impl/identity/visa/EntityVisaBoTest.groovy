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
package org.kuali.rice.kim.impl.identity.visa

import org.kuali.rice.kim.api.identity.visa.EntityVisa
import org.junit.Test
import org.junit.Assert


class EntityVisaBoTest {
  @Test
  public void testNotEqualsWithEmail() {
      EntityVisa.Builder builder = EntityVisa.Builder.create();
      builder.setEntityId("101010")
      builder.setVisaEntry("1092193894839290")
      builder.setVisaId("8483904808239048")
      builder.setVisaTypeKey("VISA")
      EntityVisa immutable = builder.build()
      EntityVisaBo bo = EntityVisaBo.from(immutable )
      Assert.assertFalse(bo.equals(immutable))
      Assert.assertFalse(immutable.equals(bo))
      Assert.assertEquals(immutable, EntityVisaBo.to(bo))
  }
}
