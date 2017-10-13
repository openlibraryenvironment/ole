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
package org.kuali.rice.kew.routemanager;

import org.kuali.rice.kew.messaging.exceptionhandling.ExceptionRoutingServiceImpl;
import org.kuali.rice.kew.test.TestUtilities;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.test.ThreadMonitor;



public class TestExceptionRoutingServiceImpl extends ExceptionRoutingServiceImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
	    .getLogger(TestExceptionRoutingServiceImpl.class);
    
	@Override
	public void placeInExceptionRouting(Throwable throwable, PersistedMessageBO persistedMessage, String documentId) {
        LOG.info("Invoking placeInExceptionRouting on TestExceptionRoutingServiceImpl");
		ExceptionThreader exceptionThreader = new ExceptionThreader(throwable, persistedMessage, documentId, this);
		ThreadMonitor.addThread(exceptionThreader);
		exceptionThreader.start();
        LOG.info("ExceptionThreader has been started");
	}
	
	private static class ExceptionThreader extends Thread {

		private Throwable throwable;
		private PersistedMessageBO message;
		String documentId;
		private TestExceptionRoutingServiceImpl testExceptionService;
		
		public ExceptionThreader(Throwable throwable, PersistedMessageBO message, String documentId, TestExceptionRoutingServiceImpl testExceptionService) {
			this.throwable = throwable;
			this.message = message;
			this.documentId = documentId;
			this.testExceptionService = testExceptionService;
			TestUtilities.setExceptionThreader(this);
		}

		public void run() {
		    try {
			    testExceptionService.callRealPlaceInExceptionRouting(throwable, message, documentId);
		    } catch (Exception e) {
			    LOG.error("Exception encountered when attempting to callRealPlaceInExceptionRouting", e);
		    }
		}
	}
	
	public void callRealPlaceInExceptionRouting(Throwable throwable, PersistedMessageBO message, String documentId) throws Exception {
        LOG.info("Invoking the real place in exception routing");
		super.placeInExceptionRouting(throwable, message, documentId);
        LOG.info("Document should now be in exception status");
	}	
}
