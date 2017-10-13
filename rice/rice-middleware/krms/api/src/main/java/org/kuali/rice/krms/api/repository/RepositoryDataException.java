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
package org.kuali.rice.krms.api.repository;

import org.kuali.rice.core.api.exception.RiceRuntimeException;

/**
 * A runtime exception which indicates an unrecoverable data issue in the
 * rule repository.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RepositoryDataException extends RiceRuntimeException {

	private static final long serialVersionUID = 5856714188298264469L;

    /**
     * Default constructor
     */
	public RepositoryDataException() {
		super();
	}

    /**
     * @param message the exception message
     * @param cause the root Throwable cause.
     */
	public RepositoryDataException(String message, Throwable cause) {
		super(message, cause);
	}

    /**
     * @param message the exception message
     */
	public RepositoryDataException(String message) {
		super(message);
	}

    /**
     * @param cause the root Throwable cause.
     */
	public RepositoryDataException(Throwable cause) {
		super(cause);
	}
	
}
