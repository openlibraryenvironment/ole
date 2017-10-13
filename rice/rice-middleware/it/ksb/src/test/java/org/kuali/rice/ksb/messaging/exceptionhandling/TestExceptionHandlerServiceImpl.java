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
package org.kuali.rice.ksb.messaging.exceptionhandling;

import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.test.TestUtilities;


public class TestExceptionHandlerServiceImpl extends DefaultExceptionServiceImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
	    .getLogger(TestExceptionHandlerServiceImpl.class);
        
    public void placeInExceptionRouting(Throwable throwable, PersistedMessageBO message, Object service) {
		LOG.info("Executing placeInExceptionRouting - creating and starting the ExceptionThreader");
		ExceptionThreader exceptionThreader = new ExceptionThreader(throwable, message, service, this);
		exceptionThreader.start();
	}
	
	private static class ExceptionThreader extends Thread {

		private Throwable throwable;
		private PersistedMessageBO message;
		private Object service;
		private TestExceptionHandlerServiceImpl testExceptionService;
		
		public ExceptionThreader(Throwable throwable, PersistedMessageBO message, Object service, TestExceptionHandlerServiceImpl testExceptionService) {
			this.throwable = throwable;
			this.message = message;
			this.service = service;
			this.testExceptionService = testExceptionService;
			TestUtilities.setExceptionThreader(this);
		}

		public void run() {
			LOG.info("Running the ExceptionThreader - sleeping for 3 seconds - " + this.toString());
		    try {
			Thread.sleep(3000);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		    try {
		    	LOG.info("Executing 'real' placeInExceptionRouting from ExceptionThreader.");
		    	this.testExceptionService.callRealPlaceInExceptionRouting(this.throwable, this.message, this.service);
		    } catch (Throwable t) {
		    	LOG.fatal("Error executing callRealPlaceInExceptionRouting.", t);
		    }
		}
	}
	
	public void callRealPlaceInExceptionRouting(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
		LOG.info("Executing callRealPlaceInExceptionRouting from TestExceptionHandlerServiceImpl.");
		super.placeInExceptionRouting(throwable, message, service);
		LOG.info("Message was successfully placed in exception routing.");
	}
	
}
