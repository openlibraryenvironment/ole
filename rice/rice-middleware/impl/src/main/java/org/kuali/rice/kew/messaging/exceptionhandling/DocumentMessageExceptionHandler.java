/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kew.messaging.exceptionhandling;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.ksb.messaging.PersistedMessageBO;
import org.kuali.rice.ksb.messaging.exceptionhandling.DefaultMessageExceptionHandler;
import org.kuali.rice.ksb.messaging.exceptionhandling.MessageExceptionHandler;
import org.kuali.rice.ksb.service.KSBServiceLocator;

/**
 * A {@link MessageExceptionHandler} which handles putting documents into
 * exception routing.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentMessageExceptionHandler extends DefaultMessageExceptionHandler {

	@Override
	protected void placeInException(Throwable throwable, PersistedMessageBO message) throws Exception {
		KEWServiceLocator.getExceptionRoutingService().placeInExceptionRouting(throwable, message, getDocumentId(message));
	}
	
	

	@Override
	public void handleExceptionLastDitchEffort(Throwable throwable, PersistedMessageBO message, Object service) throws Exception {
		KEWServiceLocator.getExceptionRoutingService().placeInExceptionRoutingLastDitchEffort(throwable, message, getDocumentId(message));
	}



	@Override
	protected void scheduleExecution(Throwable throwable, PersistedMessageBO message) throws Exception {
		String description = "DocumentId: " + getDocumentId(message);
		KSBServiceLocator.getExceptionRoutingService().scheduleExecution(throwable, message, description);
	}

	protected String getDocumentId(PersistedMessageBO message) {
		if (!StringUtils.isEmpty(message.getValue1())) {
			return message.getValue1();
		}
		throw new WorkflowRuntimeException("Unable to put this message in exception routing service name " + message.getServiceName());
	}
}
