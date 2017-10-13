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
package org.kuali.rice.kew.actionrequest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentRefreshQueue;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * Tests the reference implementation of the {@link DocumentRefreshQueue}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentRefreshQueueTest extends KEWTestCase {

    protected void loadTestData() throws Exception {
        loadXmlFile("ActionRequestsConfig.xml");
    }

    @Test public void testDocumentRequeueSingleNode() throws Exception {
       WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), SeqSetup.DOCUMENT_TYPE_NAME);
       document.route("");
       document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
       assertTrue(document.isEnroute());
       List<ActionRequest> requests = document.getRootActionRequests();
       assertEquals("Should be 2 requests.", 2, requests.size());
       // save off request ids

       Set<String> requestIds = new HashSet<String>();
       for (ActionRequest request : requests) {
    	   requestIds.add(request.getId());
       }

       DocumentRouteHeaderValue documentH = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
       DocumentRefreshQueue documentRequeuer = KewApiServiceLocator.getDocumentRequeuerService(documentH.getDocumentType().getApplicationId(), documentH.getDocumentId(), 0);
       documentRequeuer.refreshDocument(document.getDocumentId());

       document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("bmcgough"), document.getDocumentId());
       assertTrue(document.isEnroute());
       requests = document.getRootActionRequests();
       assertEquals("Should be 2 requests.", 2, requests.size());
       for (ActionRequest requestVO : requests) {
           assertTrue("Request ids should be different.", !requestIds.contains(requestVO.getId()));
       }
       assertTrue(document.isApprovalRequested());
       document.approve("");

       // now there should just be a pending request to ryan, let's requeue again, because of force action = false we should still
       // have only one pending request to ryan
       documentRequeuer.refreshDocument(document.getDocumentId());
       document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("rkirkend"), document.getDocumentId());
       assertTrue(document.isEnroute());
       requests = document.getRootActionRequests();
       assertEquals("Should be 2 requests.", 2, requests.size());
       // there should only be one pending request to rkirkend
       boolean pendingToRkirkend = false;
        for (ActionRequest requestVO : requests)
        {
            if (requestVO.getPrincipalId().equals(getPrincipalIdForName("rkirkend")) && requestVO.isActivated())
            {
                assertFalse("rkirkend has too many requests!", pendingToRkirkend);
                pendingToRkirkend = true;
            } else
            {
                assertTrue("previous request to all others should be done.", requestVO.isDone());
            }
        }
       assertTrue(document.isApprovalRequested());
   }

   private class SeqSetup {
       public static final String DOCUMENT_TYPE_NAME = "DRSeqDocType";
       public static final String ADHOC_NODE = "AdHoc";
       public static final String WORKFLOW_DOCUMENT_NODE = "WorkflowDocument";
       public static final String WORKFLOW_DOCUMENT_2_NODE = "WorkflowDocument2";
   }

}
