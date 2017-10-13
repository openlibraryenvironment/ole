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
package org.kuali.rice.edl.impl;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.rice.edl.framework.workflow.EDocLitePostProcessor;
import org.kuali.rice.kew.test.KEWTestCase;


/**
 * This is a Test class to verify the edoc lite post processor. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EDocLitePostProcessorTest extends KEWTestCase {
    private static final Logger LOG = Logger.getLogger(EDocLitePostProcessorTest.class);

    private static final String CONTEXT_NAME = "/edl-test";

    @Test
    public void junk() {

    }
    /*
    @Test
    public void testPostEvent() throws Exception {
        String dummyData = "testing this stuff";
        Integer testServerPort = Integer.valueOf(getJettyServerPort() + 1);
        WorkflowDocument document = new WorkflowDocument(getPrincipalIdForName("ewestfal"), "TestDocumentType");
        String applicationContent = "<data><edlContent><edl><eventNotificationURL>" + ConfigContext.getCurrentContextConfig().getProperty(KewApiConstants.KEW_URL_HOST) + ":" + testServerPort + CONTEXT_NAME + "</eventNotificationURL><testThisData>" + dummyData + "</testThisData></edl></edlContent></data>";
        document.setApplicationContent(applicationContent);
        document.saveDocumentData();

        JettyServer server = null;
        try {
            server = new JettyServer(testServerPort.intValue(), CONTEXT_NAME, EDocLiteTestServlet.class);
            server.setTestMode(true);
            server.setFailOnContextFailure(true);
            server.start();

            EDocLitePostProcessor postProcessor = new EDocLitePostProcessor();
            postProcessor.doActionTaken(new ActionTakenEvent(document.getDocumentId(), document.getAppDocId(), new ActionTakenValue()));
            String eventType = EDocLitePostProcessor.EVENT_TYPE_ACTION_TAKEN;
            testPostProcessorMethod(document.getDocumentId(), dummyData, eventType);

            postProcessor = new EDocLitePostProcessor();
            postProcessor.doRouteLevelChange(new DocumentRouteLevelChange(document.getDocumentId(), document.getAppDocId(), 1, 2, null, null, null, null));
            eventType = EDocLitePostProcessor.EVENT_TYPE_ROUTE_LEVEL_CHANGE;
            testPostProcessorMethod(document.getDocumentId(), dummyData, eventType);

            postProcessor = new EDocLitePostProcessor();
            postProcessor.doRouteStatusChange(new DocumentRouteStatusChange(document.getDocumentId(), document.getAppDocId(), KewApiConstants.ROUTE_HEADER_INITIATED_CD, KewApiConstants.ROUTE_HEADER_ENROUTE_CD));
            eventType = EDocLitePostProcessor.EVENT_TYPE_ROUTE_STATUS_CHANGE;
            testPostProcessorMethod(document.getDocumentId(), dummyData, eventType);

            postProcessor = new EDocLitePostProcessor();
            postProcessor.doDeleteRouteHeader(new DeleteEvent(document.getDocumentId(), document.getAppDocId()));
            eventType = EDocLitePostProcessor.EVENT_TYPE_DELETE_ROUTE_HEADER;
            testPostProcessorMethod(document.getDocumentId(), dummyData, eventType);

        } finally {
            if (server != null) {
                try {
                    server.stop();
                } catch (Exception e) {
                    LOG.warn("Failed to shutdown one of the lifecycles!", e);
                }
            }
        }
    } */

    private void testPostProcessorMethod(String documentId, String dummyData, String eventType) {
        int maxWaitSeconds = EDocLitePostProcessor.SUBMIT_URL_MILLISECONDS_WAIT + 10000;
        assertTrue("Test Servlet data map should not be null after wait period", waitForData(maxWaitSeconds, 5000));
        testForStringExistence("Document Id", documentId);
        testForStringExistence("Dummy Data", dummyData);
        testForStringExistence("Event Type '" + eventType + "'", eventType);
    }

    private void testForStringExistence(String dataDescriptor, String dataToFind) {
        assertTrue(dataDescriptor = " should come back as part of the post data but didn't", EDocLiteTestServlet.postData.contains(dataToFind));
    }

    public static boolean waitForData(int maxWaitMilliseconds, int pauseMilliseconds) {
        boolean valueNotNull = false;
        boolean interrupted = false;
        long startTimeMs = System.currentTimeMillis();
        long endTimeMs = startTimeMs + maxWaitMilliseconds;

        try {
            Thread.sleep(pauseMilliseconds / 10); // the first time through, sleep a fraction of the specified time
        } catch (InterruptedException e) {
            interrupted = true;
        }
        valueNotNull = EDocLiteTestServlet.postData != null;
        LOG.debug("starting wait loop");
        while (!interrupted && !valueNotNull && (System.currentTimeMillis() < endTimeMs)) {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("sleeping for " + pauseMilliseconds + " ms");
                }
                Thread.sleep(pauseMilliseconds);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            LOG.debug("checking wait loop sentinel");
            valueNotNull = EDocLiteTestServlet.postData != null;
        }
        LOG.debug("finished wait loop (" + valueNotNull + ")");

        return valueNotNull;
    }
}
