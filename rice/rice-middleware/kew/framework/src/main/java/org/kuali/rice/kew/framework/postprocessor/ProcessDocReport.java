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
package org.kuali.rice.kew.framework.postprocessor;

/**
 * Returned from a {@link org.kuali.rice.kew.framework.postprocessor.PostProcessor} to indicate success of failure of
 * a particular event.  If success is false then this will typically trigger
 * the document to go into exception routing.
 * 
 * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ProcessDocReport implements java.io.Serializable {

	static final long serialVersionUID = 376851530227478560L;

	private boolean success = false;
	private String message;
	private Exception processException = null;

	public ProcessDocReport(boolean success) {
		this(success, "");
	}

	public ProcessDocReport(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ProcessDocReport(boolean success, String message, Exception e) {
		this.success = success;
		this.message = message;
		this.processException = e;
	}

	public String getMessage() {
		return message;
	}

	public Exception getProcessException() {
		return processException;
	}

	public boolean isSuccess() {
		return success;
	}
}
