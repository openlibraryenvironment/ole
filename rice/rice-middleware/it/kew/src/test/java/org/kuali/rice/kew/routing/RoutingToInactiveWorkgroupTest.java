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
package org.kuali.rice.kew.routing;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

@BaselineMode(Mode.CLEAR_DB)
public class RoutingToInactiveWorkgroupTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("RoutingConfig.xml");
    }
    
    @Test public void testRoutingToInactiveWorkgroup() throws Exception {
        WorkflowDocument doc = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "InactiveWorkgroupDocType");
        try {
            doc.route("");
            fail("document should have thrown routing exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestUtilities.getExceptionThreader().join();//wait for doc to go into exception routing
        doc = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), doc.getDocumentId());
        assertTrue("Document should be in exception routing because workgroup is inactive", doc.isException());

        try {
            doc.route("routing a document that is in exception routing");
            fail("Succeeded in routing document that is in exception routing");
        } catch (InvalidActionTakenException iate) {
            log.info("Expected exception occurred: " + iate);
        }
    }
}
