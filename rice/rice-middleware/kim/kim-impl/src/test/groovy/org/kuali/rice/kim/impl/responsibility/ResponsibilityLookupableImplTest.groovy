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
package org.kuali.rice.kim.impl.responsibility

import org.junit.Test
import org.kuali.rice.kns.lookup.LookupableHelperService
import static org.junit.Assert.assertEquals

/**
 * Tests the ResponsibilityLookupableImpl
 */
class ResponsibilityLookupableImplTest {
    private def lookupable = new ResponsibilityLookupableImpl()

    @Test
    void testGetCreateNewUrl() {
        lookupable.setLookupableHelperService([
            allowsNewOrCopyAction: { true },
            getReturnLocation: { "RETURN_LOCATION" }
        ] as LookupableHelperService)

        // test that the result is the same as the return value from the protected helper
        assertEquals(lookupable.getCreateNewUrl("maintenance.do?businessObjectClassName=org.kuali.rice.kim.impl.responsibility.ReviewResponsibilityBo&returnLocation=RETURN_LOCATION&methodToCall=start"), lookupable.getCreateNewUrl())
        //assertEquals("""<a title="Create a new record" href="maintenance.do?businessObjectClassName=org.kuali.rice.kim.impl.responsibility.ReviewResponsibilityBo&returnLocation=RETURN_LOCATION&methodToCall=start"><img src="images/tinybutton-createnew.gif" alt="create new" width="70" height="15"/></a>""", lookupable.getCreateNewUrl())
    }
}
