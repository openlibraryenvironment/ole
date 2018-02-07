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
package org.kuali.rice.ksb.messaging.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.kuali.rice.ksb.messaging.serviceexporters.ServiceExportManager;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

/**
 * A {@link HandlerMapping} which handles incoming HTTP requests from the bus.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KSBHttpInvokerHandler extends AbstractHandlerMapping {

    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        QName serviceName = getServiceNameFromRequest(request);
        ServiceExportManager serviceExportManager = KSBServiceLocator.getServiceExportManager();
        return serviceExportManager.getService(serviceName);
    }

    public QName getServiceNameFromRequest(HttpServletRequest request) {   	    	
    	return KSBServiceLocator.getServiceExportManager().getServiceName(request.getRequestURL().toString()); 
    }
    
}
