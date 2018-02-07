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
import org.kuali.rice.kim.service.KIMServiceLocatorInternal
import org.kuali.rice.kim.service.UiDocumentService
import org.kuali.rice.krad.util.GlobalVariables
import org.kuali.rice.krad.UserSession
import org.kuali.rice.kim.api.identity.PersonService
import org.kuali.rice.kim.impl.identity.PersonImpl
import org.kuali.rice.kim.api.permission.PermissionService

/**
 * Tests the PersonLookupableImpl
 */
class PersonLookupableImplTest {
    private def lookupable = new PersonLookupableImpl()

    @Before
    void setupFakeEnv() {
        def config = new JAXBConfigImpl();
        config.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");
        config.putProperty(KIMServiceLocatorInternal.KIM_RUN_MODE_PROPERTY, "LOCAL");
        ConfigContext.init(config);
        GlobalResourceLoader.stop();
        GlobalResourceLoader.addResourceLoader([
            getName: { -> new QName("Foo", "Bar") },
            getService: { QName name ->
                def svc = [
                    //kimUiDocumentService: [ canModifyEntity: { a,b -> true } ] as UiDocumentService,
                    kimPermissionService: [ isAuthorized: {a, b, c, d -> true} ] as PermissionService,
                    personService: [ getPersonByPrincipalName: { new PersonImpl() } ] as PersonService,
                    kualiConfigurationService: [ getPropertyValueAsString: { "KIM_BASE_PATH" } ] as ConfigurationService
                ][name.getLocalPart()]
                svc
            },
            stop: {}
        ] as ResourceLoader)
    }

    @Test
    void testGetCreateNewUrl() {
        GlobalVariables.doInNewGlobalVariables() {
            GlobalVariables.setUserSession(new UserSession("foo"))
            lookupable.setLookupableHelperService([
                allowsNewOrCopyAction: { true },
                getReturnLocation: { "RETURN_LOCATION" }
            ] as LookupableHelperService)
            // test that the result is the same as the return value from the protected helper
            assertEquals(lookupable.getCreateNewUrl("KIM_BASE_PATH/identityManagementPersonDocument.do?returnLocation=RETURN_LOCATION&docTypeName=IdentityManagementPersonDocument&methodToCall=docHandler&command=initiate"), lookupable.getCreateNewUrl())
            //assertEquals("""<a title="Create a new record" href="KIM_BASE_PATH/identityManagementPersonDocument.do?returnLocation=RETURN_LOCATION&docTypeName=IdentityManagementPersonDocument&methodToCall=docHandler&command=initiate"><img src="images/tinybutton-createnew.gif" alt="create new" width="70" height="15"/></a>""", lookupable.getCreateNewUrl())
        }
    }
}
