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
package org.kuali.rice.core.api.resourceloader;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;

/**
 * Uses to locate services for the given service name.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ServiceLocator extends Lifecycle {

	/**
	 * Fetches the service with the given name.
	 */
	public <T extends Object> T getService(QName qname);

	public String getContents(String indent, boolean servicePerLine);

}
