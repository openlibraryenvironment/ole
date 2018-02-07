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
package org.kuali.rice.ksb.messaging;

import org.kuali.rice.ksb.messaging.exceptionhandling.DefaultExceptionServiceImpl;
import org.kuali.rice.test.TestUtilities;
import org.kuali.rice.test.ThreadMonitor;


public class TestExceptionRoutingServiceImpl extends DefaultExceptionServiceImpl {

    	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
		.getLogger(TestExceptionRoutingServiceImpl.class);
    
	public void placeInExceptionRouting(Throwable throwable, PersistedMessageBO message, Object service) {
		ExceptionThreader exceptionThreader = new ExceptionThreader(throwable, message, service, this);
		ThreadMonitor.addThread(exceptionThreader);
		exceptionThreader.start();
	}
	
	private static class ExceptionThreader extends Thread {

		private Throwable throwable;
		private PersistedMessageBO message;
		private Object service;
		private TestExceptionRoutingServiceImpl testExceptionService;
		
		public ExceptionThreader(Throwable throwable, PersistedMessageBO message, Object service, TestExceptionRoutingServiceImpl testExceptionService) {
			this.throwable = throwable;
			this.message = message;
			this.service = service;
			this.testExceptionService = testExceptionService;
			TestUtilities.setExceptionThreader(this);
		}

		public void run() {
		    try {
			this.testExceptionService.callRealPlaceInExceptionRouting(this.throwable, this.message, this.service);
		    } catch (Throwable t) {
			LOG.fatal("Error executing callRealPlaceInExceptionRouting.", t);
		    }
		}
	}
	
	public void callRealPlaceInExceptionRouting(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
		super.placeInExceptionRouting(throwable, message, service);
	}
	
}
