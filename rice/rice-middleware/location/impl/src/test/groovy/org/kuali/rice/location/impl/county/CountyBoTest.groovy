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
package org.kuali.rice.location.impl.county

import org.junit.Test
import org.kuali.rice.location.api.county.County
import junit.framework.Assert


class CountyBoTest {
  @Test
  public void testNotEqualsWithCampus() {
    County immutable = County.Builder.create("SHA", "SHA County", "US", "MI").build();
    CountyBo bo = CountyBo.from(immutable)
    Assert.assertFalse(bo.equals(immutable))
    Assert.assertFalse(immutable.equals(bo))
  }
}
