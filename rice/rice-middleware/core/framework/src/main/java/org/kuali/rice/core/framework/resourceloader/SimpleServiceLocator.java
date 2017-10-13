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
package org.kuali.rice.core.framework.resourceloader;

import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.resourceloader.ServiceLocator;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link ServiceLocator} that allows users to put services into workflow's resource loading
 * wihout creating their own {@link ServiceLocator}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SimpleServiceLocator extends BaseLifecycle implements ServiceLocator {

	private Map<QName, Object> services = new HashMap<QName, Object>();

	public String getContents(String indent, boolean servicePerLine) {
		String contents = indent + "SpringLoader " + this + " services =";

		for (Map.Entry<QName, Object> serviceEntry : this.services.entrySet()) {
			if (servicePerLine) {
				contents += indent + "+++" + serviceEntry.getKey() + "=" + serviceEntry.getValue() + "\n";
			} else {
				contents += serviceEntry.getKey() + "=" + serviceEntry.getValue() + ", ";
			}
		}

		return contents;
	}

	public Object getService(QName qname) {
		return this.services.get(qname);
	}

	public void addService(QName name, Object service) {
	    this.services.put(name, service);
	}

	public void removeService(QName name) {
	    this.services.remove(name);
	}
}
