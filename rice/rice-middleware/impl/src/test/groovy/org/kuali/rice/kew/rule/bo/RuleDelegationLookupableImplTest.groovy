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
package org.kuali.rice.kew.rule.bo

import org.junit.Test
import org.kuali.rice.kns.lookup.LookupableHelperService
import static org.junit.Assert.assertEquals
import org.junit.Before
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.kuali.rice.core.api.config.property.ConfigContext

/**
 * Tests the RuleDelegationLookupableImpl
 */
class RuleDelegationLookupableImplTest {
    private def lookupable = new RuleDelegationLookupableImpl()

    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(org.kuali.rice.core.api.config.property.Config.KEW_URL, "KEW_URL");
        ConfigContext.init(config);
    }

    @Test
    void testGetCreateNewUrl() {
        lookupable.setLookupableHelperService([
            allowsMaintenanceNewOrCopyAction: { true },
            getReturnLocation: { "RETURN_LOCATION" }
        ] as LookupableHelperService)

        // test that the result is the same as the protected helper
        assertEquals(lookupable.getCreateNewUrl("KEW_URL/DelegateRule.do"), lookupable.getCreateNewUrl())
        //"""<a title="Create a new record" href="KEW_URL/DelegateRule.do"><img src="images/tinybutton-createnew.gif" alt="create new" width="70" height="15"/></a>""", lookupable.getCreateNewUrl())
    }
}
