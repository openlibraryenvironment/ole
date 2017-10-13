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
package org.kuali.rice.ksb.messaging.exceptionhandling;

import org.kuali.rice.ksb.messaging.PersistedMessageBO;

/**
 * A MessageExceptionHandler handles exception which arrise during processing of the
 * message.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessageExceptionHandler {

	public void handleException(Throwable throwable, PersistedMessageBO message, Object service) throws Exception;
	
	public void handleExceptionLastDitchEffort(Throwable throwable, PersistedMessageBO message, Object service) throws Exception;
	
    /**
     * Determines whether the message would go into Exception if submitted.
     * @param message The PersistedMessageBO instance to be tested.
     * @return Returns true if the message would go into exception, otherwise returns false.
     */
    public boolean isInException(PersistedMessageBO message);

}
