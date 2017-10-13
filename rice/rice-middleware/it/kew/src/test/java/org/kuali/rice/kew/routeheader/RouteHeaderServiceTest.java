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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.exception.LockingException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.test.BaselineTestCase;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;


@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class RouteHeaderServiceTest extends KEWTestCase {

    private Object lock = new Object();
    private RouteHeaderService routeHeaderService;

    protected void setUpAfterDataLoad() throws Exception {
        super.setUpAfterDataLoad();
        routeHeaderService = KEWServiceLocator.getRouteHeaderService();
    }
    
    /**
     * Tests the saving of a document with large XML content.  This verifies that large CLOBs (> 4000 bytes)
     * can be saved by OJB.  This can cause paticular issues with Oracle and OJB has to unwrap the native jdbc
     * Connections and Statements from the pooled connection.  We need to make sure this is working for our
     * pooling software of choice.
     */
    @Test
    public void testLargeDocumentContent() throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<content>");
        for (int index = 0; index < 10000; index++) {
            buffer.append("abcdefghijklmnopqrstuvwxyz");
        }
        buffer.append("</content>");
        DocumentRouteHeaderValue document = new DocumentRouteHeaderValue();
        document.setDocContent(buffer.toString());
        document.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_INITIATED_CD);
        document.setDocRouteLevel(0);
        document.setDateModified(new Timestamp(System.currentTimeMillis()));
        document.setCreateDate(new Timestamp(System.currentTimeMillis()));
        document.setInitiatorWorkflowId("1");
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName("TestDocumentType");
        assertNotNull(documentType);
        document.setDocumentTypeId(documentType.getDocumentTypeId());
        routeHeaderService.saveRouteHeader(document);
        assertNotNull("Document was saved, it should have an ID now.", document.getDocumentId());
        
        // now reload from database and verify it's the right size
        document = routeHeaderService.getRouteHeader(document.getDocumentId());
        String docContent = document.getDocContent();
        assertEquals("Doc content should be the same size as original string buffer.", buffer.length(), docContent.length());
        assertTrue("Should be greater than about 5000 bytes.", docContent.getBytes().length > 5000);
    }

    @Test public void testGetApplicationIdByDocumentId() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestDocumentType2");
    	String documentId = document.getDocumentId();
    	String applicationId = routeHeaderService.getApplicationIdByDocumentId(documentId);
    	assertEquals("applicationId should be KEWNEW", "KEWNEW", applicationId);

    	// now check TestDocumentType
    	document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("ewestfal"), "TestDocumentType");
    	documentId = document.getDocumentId();
    	applicationId = routeHeaderService.getApplicationIdByDocumentId(documentId);
    	assertEquals("applicationId should be KUALI", "KUALI", applicationId);
    }

    @Test public void testLockRouteHeader() throws Exception {
    	WorkflowDocument document = WorkflowDocumentFactory.createDocument(getPrincipalIdForName("rkirkend"), "TestDocumentType");
    	document.saveDocumentData();
    	final String documentId = document.getDocumentId();
        Locker locker = null;
        synchronized (lock) {
            locker = new Locker(documentId);
            locker.start();
            lock.wait();
        }
        // document should be locked by the other thread at this point
        try {
            routeHeaderService.lockRouteHeader(documentId, false);
            fail("The route header should be locked.");
        } catch (LockingException e) {
            // should have been thrown!
        }
        synchronized (lock) {
            lock.notify();
        }
        locker.join();
        // document should be unlocked now
        routeHeaderService.lockRouteHeader(documentId, false);
        assertTrue("Locker thread should have completed.", locker.isCompleted());

        // now configure a lock timeout for 2 seconds
        ConfigContext.getCurrentContextConfig().putProperty(Config.DOCUMENT_LOCK_TIMEOUT, "2");
        synchronized (lock) {
            locker = new Locker(documentId);
            locker.start();
            lock.wait();
        }
        // document should be locked by the other thread at this point
        long millisStart = System.currentTimeMillis();
        try {
        	routeHeaderService.lockRouteHeader(documentId, true);
        	fail("The route header should be locked.");
        }  catch (LockingException e) {
        	// should have been thrown!
        }

        long millisEnd = System.currentTimeMillis();
        long timeLocked = (millisEnd - millisStart);

        synchronized(lock) {
        	lock.notify();
        }
        locker.join();

        // assert that the time locked was close to 2000 milliseconds += 250 of a millisecond
//        assertTrue("Time locked should have been around 2000 milliseconds but was " + timeLocked, timeLocked > (2000-250) && timeLocked < (2000+250));
        
        // document should be unlocked again
        routeHeaderService.lockRouteHeader(document.getDocumentId(), false);
        assertTrue("Locker thread should have completed.", locker.isCompleted());
    }

    private class Locker extends Thread {
        private String documentId;
        private boolean isCompleted = false;
        public Locker(String documentId) {
            this.documentId = documentId;
        }
        public void run() {
            getTransactionTemplate().execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus status) {
                	synchronized (lock) {
                        routeHeaderService.lockRouteHeader(documentId, true);
                        try {
                            lock.notify();
                            lock.wait();
                        } catch (InterruptedException e) {
                            fail("Shouldn't have been interrupted");
                        }
                    }
                    return null;
                }
            });
            isCompleted = true;
        }
        public boolean isCompleted() {
            return isCompleted;
        }
    }

}
