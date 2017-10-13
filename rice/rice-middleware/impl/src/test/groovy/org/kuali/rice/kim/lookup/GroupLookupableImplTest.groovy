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
package org.kuali.rice.kim.lookup

import javax.xml.namespace.QName
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.api.config.property.ConfigurationService
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader
import org.kuali.rice.core.api.resourceloader.ResourceLoader
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl
import org.kuali.rice.kns.lookup.LookupableHelperService
import static org.junit.Assert.assertEquals

/**
 * Tests the GroupLookupableImpl
 */
class GroupLookupableImplTest {
    private def lookupable = new GroupLookupableImpl()

    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");
        ConfigContext.init(config);
        GlobalResourceLoader.stop()
        GlobalResourceLoader.addResourceLoader([
            getName: { -> new QName("Foo", "Bar") },
            getService: { [ getPropertyValueAsString: { "KIM_BASE_PATH" } ] as ConfigurationService },
            stop: {}
        ] as ResourceLoader)
    }

    @Test
    void testGetCreateNewUrl() {
        lookupable.setLookupableHelperService([
            allowsNewOrCopyAction: { true },
            getReturnLocation: { "RETURN_LOCATION" }
        ] as LookupableHelperService)

        // test that the result is the same as the return value from the protected helper
        assertEquals(lookupable.getCreateNewUrl("KIM_BASE_PATH/identityManagementGroupDocument.do?returnLocation=RETURN_LOCATION&docTypeName=IdentityManagementGroupDocument&methodToCall=docHandler&command=initiate"), lookupable.getCreateNewUrl())
        //assertEquals("""<a title="Create a new record" href="KIM_BASE_PATH/identityManagementGroupDocument.do?returnLocation=RETURN_LOCATION&docTypeName=IdentityManagementGroupDocument&methodToCall=docHandler&command=initiate"><img src="images/tinybutton-createnew.gif" alt="create new" width="70" height="15"/></a>""", lookupable.getCreateNewUrl())
    }
}