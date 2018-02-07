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
package org.kuali.rice.kew.routeheader;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.util.PerformanceLogger;

/**
 *
 */
public class WorkflowDocumentServiceTest extends KEWTestCase {
    protected void loadTestData() throws Exception {
        
    }

    @Ignore @Test
    public void testCreatePerformanceWithBranches() {
        testCreatePerformance("DocTypeWithBranches.xml", "Testing.LongRoutePath");
    }

    @Ignore @Test
    public void testCreatePerformanceWithoutBranches() {
        testCreatePerformance("DocTypeWithoutBranches.xml", "Testing.LongRoutePathWithoutBranches");
    }
    
    protected void testCreatePerformance(String xml, String doctype) {
        PerformanceLogger logger = new PerformanceLogger();
        logger.log("Loading " + xml);
        loadXmlFile(xml);
        logger.log("Done loading " + xml);

        System.err.println("Creating and routing " + doctype);
        WorkflowDocument document = WorkflowDocumentFactory.createDocument("admin", doctype);
        // There are no rules associated with this so this doc goes to final as soon as it is routed
        document.route("");
        // Setting the title so that routing data actually gets saved
        document.setTitle("Testing");
        // Sleep to give it a chance to route, may not be needed
        logger = new PerformanceLogger();
        logger.log("starting save routing data");
        document.saveDocumentData();
        logger.log("finished save routing data");
    }
}