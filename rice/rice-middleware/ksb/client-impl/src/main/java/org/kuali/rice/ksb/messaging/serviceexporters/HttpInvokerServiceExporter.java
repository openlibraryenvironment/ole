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
package org.kuali.rice.ksb.messaging.serviceexporters;

import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.messaging.KSBHttpInvokerServiceExporter;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class HttpInvokerServiceExporter implements ServiceExporter {

	public Object exportService(ServiceDefinition serviceDefinition) {
		KSBHttpInvokerServiceExporter service = new KSBHttpInvokerServiceExporter();
		service.setServiceDefinition(serviceDefinition);
		service.setService(serviceDefinition.getService());
		service.afterPropertiesSet();
		return service;
	}

}
