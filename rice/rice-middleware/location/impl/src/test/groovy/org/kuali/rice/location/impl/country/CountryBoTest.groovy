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
package org.kuali.rice.location.impl.country

import org.junit.Test
import org.kuali.rice.location.api.country.Country
import junit.framework.Assert


class CountryBoTest {
  @Test
  public void testNotEqualsWithCountry() {
    Country immutable = Country.Builder.create("US", null, "United States", false, true).build()
    CountryBo bo = CountryBo.from(immutable )
    Assert.assertFalse(bo.equals(immutable))
    Assert.assertFalse(immutable.equals(bo))
  }
}
