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
package org.kuali.rice.krms.api.engine;

import org.kuali.rice.core.api.exception.RiceRuntimeException;

/**
 * A runtime exception which indicates that some resource required during engine
 * execution is unavailable. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EngineResourceUnavailableException extends RiceRuntimeException {

	private static final long serialVersionUID = 4936540099245413634L;

	/**
	 * @see RiceRuntimeException#RiceRuntimeException()
	 */
	public EngineResourceUnavailableException() {
		super();
	}

	/**
	 * @see RiceRuntimeException#RiceRuntimeException(String, Throwable)
     * @param message the exception message
     * @param cause the root Throwable cause.
     */
	public EngineResourceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RiceRuntimeException#RiceRuntimeException(String)
     * @param message the exception message
	 */
	public EngineResourceUnavailableException(String message) {
		super(message);
	}

	/**
	 * @see RiceRuntimeException#RiceRuntimeException(Throwable)
     * @param cause the root Throwable cause.
	 */
	public EngineResourceUnavailableException(Throwable cause) {
		super(cause);
	}
	
}
