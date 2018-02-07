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
package org.kuali.rice.kew.rule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.ksb.util.KSBConstants;

/**
 * Tests the generation of Action Requests from RoleAttributes
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoleAttributeTest extends KEWTestCase {

	@Test public void testWorkgroupRoleAttribute() throws Exception {
		loadXmlFile("WorkgroupRoleAttributeTestConfig.xml");
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "WorkgroupRoleAttributeDocument");
		document.route("");

		assertTrue("document should be enroute.", document.isEnroute());
	}

	/**
	 * Tests that if you return a non-null Id object with a null id value inside, that the role action request
	 * generation handles it properly.
	 */
	@Test public void testBadWorkgroupRoleAttribute() throws Exception {
		loadXmlFile("BadWorkgroupRoleAttributeTestConfig.xml");
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("user1"), "BadWorkgroupRoleAttributeDocument");


        ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.ALLOW_SYNC_EXCEPTION_ROUTING, "false");
		try {
			document.route("");
			fail("Should have thrown an error because we had some bad ids.");
		} catch (Exception e) {
        } finally {
            // be sure to change it back afterward!
            ConfigContext.getCurrentContextConfig().putProperty(KSBConstants.Config.ALLOW_SYNC_EXCEPTION_ROUTING, "true");
        }
	}
}
