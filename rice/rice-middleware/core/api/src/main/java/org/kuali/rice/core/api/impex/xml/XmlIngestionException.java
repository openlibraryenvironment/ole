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
package org.kuali.rice.core.api.impex.xml;

import org.kuali.rice.core.api.exception.RiceRuntimeException;

/**
 * A RuntimeException which indicates a problem during XML ingestion.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class XmlIngestionException extends RiceRuntimeException {

	private static final long serialVersionUID = 8355150049990519905L;

	public XmlIngestionException() {
		super();
	}

	public XmlIngestionException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmlIngestionException(String message) {
		super(message);
	}

	public XmlIngestionException(Throwable cause) {
		super(cause);
	}

}
