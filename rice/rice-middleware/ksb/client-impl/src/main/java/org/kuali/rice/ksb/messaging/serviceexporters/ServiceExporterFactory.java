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


import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.ServerRegistry;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.api.bus.support.JavaServiceDefinition;
import org.kuali.rice.ksb.api.bus.support.RestServiceDefinition;
import org.kuali.rice.ksb.api.bus.support.SoapServiceDefinition;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServiceExporterFactory {

	public static ServiceExporter getServiceExporter(ServiceDefinition serviceDefinition, Bus cxfBus) {
		
		if (serviceDefinition instanceof JavaServiceDefinition) {
			return new HttpInvokerServiceExporter();
		} else if (serviceDefinition instanceof SoapServiceDefinition) {
			return new SOAPServiceExporter((SoapServiceDefinition)serviceDefinition, cxfBus);
		} else if (serviceDefinition instanceof RestServiceDefinition) {
			return new RESTServiceExporter((RestServiceDefinition)serviceDefinition, cxfBus);
		}
		
		throw new IllegalArgumentException("ServiceDefinition type not supported " + serviceDefinition);
	}
	
	
	
}
