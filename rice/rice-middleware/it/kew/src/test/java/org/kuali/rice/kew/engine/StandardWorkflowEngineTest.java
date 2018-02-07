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
package org.kuali.rice.kew.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.messaging.exceptionhandling.DocumentMessageExceptionHandler;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.support.JavaServiceDefinition;
import org.kuali.rice.ksb.messaging.service.KSBJavaService;
import org.kuali.rice.test.BaselineTestCase;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class StandardWorkflowEngineTest extends KEWTestCase {

	protected void loadTestData() throws Exception {
		loadXmlFile("EngineConfig.xml");
	}

	/**
	 * Tests that the proper state is set up on the root branch in the document
	 * to indicate that both PROCESSED and FINAL callbacks have been made into
	 * the post processor.
	 */
	@Test public void testSystemBranchState() throws Exception {
		// route the document to final
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SimpleDocType");
		document.route("");
		assertTrue("Document should be final.", document.isFinal());

		// now look at the branch state
		DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
		Branch rootBranch = routeHeader.getRootBranch();
		assertNotNull(rootBranch);
		BranchState processedBranchState = rootBranch.getBranchState(KewApiConstants.POST_PROCESSOR_PROCESSED_KEY);
		BranchState finalBranchState = rootBranch.getBranchState(KewApiConstants.POST_PROCESSOR_FINAL_KEY);
		assertNotNull(processedBranchState);
		assertNotNull(finalBranchState);
		assertEquals("true", processedBranchState.getValue());
		assertEquals("true", finalBranchState.getValue());
		assertEquals(1, TestPostProcessor.processedCount);
		assertEquals(1, TestPostProcessor.finalCount);
	}

	/**
	 * Tests that a FINAL document can go into exception routing and recover
	 * properly while only calling the PROCESSED and FINAL callbacks once.
	 */
	@Test public void testFinalDocumentExceptionRoutingRecovery() throws Exception {

		// route the document to final
		WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "SimpleDocType");
		document.route("");
		assertTrue("Document should be final.", document.isFinal());
		assertEquals(1, TestPostProcessor.processedCount);
		assertEquals(1, TestPostProcessor.finalCount);

		// now queue up an exploder which should push the document into
		// exception routing
		JavaServiceDefinition serviceDef = new JavaServiceDefinition();
		serviceDef.setPriority(new Integer(1));
		serviceDef.setQueue(true);
		serviceDef.setRetryAttempts(0);
		serviceDef.setServiceInterface(KSBJavaService.class.getName());
		serviceDef.setServiceName(new QName("KEW", "exploader"));
		serviceDef.setService(new ImTheExploderProcessor());

		serviceDef.setMessageExceptionHandler(DocumentMessageExceptionHandler.class.getName());
		serviceDef.validate();
		KsbApiServiceLocator.getServiceBus().publishService(serviceDef, true);

        DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(document.getDocumentId());
        String applicationId = routeHeader.getDocumentType().getApplicationId();

        KSBJavaService exploderAsService = (KSBJavaService) KsbApiServiceLocator.getMessageHelper().getServiceAsynchronously(new QName(
                "KEW", "exploader"), null, null, routeHeader.getDocumentId(), null);
        exploderAsService.invoke("");
		TestUtilities.waitForExceptionRouting();

		// the document should be in exception routing now
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
		assertTrue("Document should be in exception routing.", document.isException());
		assertEquals(1, TestPostProcessor.processedCount);
		assertEquals(1, TestPostProcessor.finalCount);

		assertTrue("ewestfal should have a complete request.", document.isCompletionRequested());
		document.complete("");

		// the document should be final once again
		document = WorkflowDocumentFactory.loadDocument(getPrincipalIdForName("ewestfal"), document.getDocumentId());
		assertTrue("Document should be final.", document.isFinal());
		assertEquals(1, TestPostProcessor.processedCount);
		assertEquals(1, TestPostProcessor.finalCount);
	}

	public void tearDown() throws Exception {
	    try {
		TestPostProcessor.resetProcessedCount();
		TestPostProcessor.resetFinalCount();
	    } finally {
		super.tearDown();
	    }
	}

	public static class TestPostProcessor extends DefaultPostProcessor {

		public static int finalCount = 0;

		public static int processedCount = 0;

		public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
			if (KewApiConstants.ROUTE_HEADER_PROCESSED_CD.equals(statusChangeEvent.getNewRouteStatus())) {
				processedCount++;
			} else if (KewApiConstants.ROUTE_HEADER_FINAL_CD.equals(statusChangeEvent.getNewRouteStatus())) {
				finalCount++;
			}
			return new ProcessDocReport(true);
		}

		public static void resetProcessedCount() {
			processedCount = 0;
		}

		public static void resetFinalCount() {
			finalCount = 0;
		}
	}

	public static class ImTheExploderProcessor implements KSBJavaService {

		public void invoke(Serializable payLoad) {
			throw new WorkflowRuntimeException("I'm the Exploder!!!");
		}

	}

}
