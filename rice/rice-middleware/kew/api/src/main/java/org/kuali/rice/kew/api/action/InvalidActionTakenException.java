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
package org.kuali.rice.kew.api.action;

import javax.xml.ws.WebFault;

import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;

@WebFault(name = "InvalidActionTakenFault", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
public class InvalidActionTakenException extends WorkflowRuntimeException {

	private static final long serialVersionUID = 4282067592790143952L;

	public InvalidActionTakenException() {
		super();
	}

	public InvalidActionTakenException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidActionTakenException(String message) {
		super(message);
	}

	public InvalidActionTakenException(Throwable cause) {
		super(cause);
	}

}
