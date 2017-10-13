/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.ken;

import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ken.api.KenApiConstants;
import org.kuali.rice.ken.api.service.SendNotificationService;
import org.kuali.rice.ken.test.KENTestCase;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

import javax.xml.namespace.QName;

import static org.junit.Assert.assertNotNull;


/**
 * Tests integration with KEW
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineMode(Mode.ROLLBACK_CLEAR_DB)
public class KEWIntegrationTest extends KENTestCase {

	@Test
    public void testKEWServicesAreAccessible() throws Exception {
        
        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        assertNotNull(identityService);
        LOG.info("Default KIM IdentityManagementService: " + identityService);

        SendNotificationService notification = (SendNotificationService) GlobalResourceLoader.getService(new QName(
                KenApiConstants.Namespaces.KEN_NAMESPACE_2_0, "sendNotificationService"));
        assertNotNull(notification);
        // XmlIngesterService is = SpringServiceLocator..getXmlIngesterService();
        // check that the quickstart user is present
        //assertNotNull(userService.getWorkflowUser(new WorkflowUserId("quickstart")));
    }
}
