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
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.ServletController;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * An adapter for the {@link ServletController} so that it conforms to Springs MVC {@link Controller} interface. 
 * 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CXFServletControllerAdapter implements Controller{

	private ServletController controller;
		
	/**
	 * This method invokes the cxf servlet controller
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
						
        try {
    		BusFactory.setThreadDefaultBus(KSBServiceLocator.getCXFBus());
            controller.invoke(request, response);
        } finally {
            BusFactory.setThreadDefaultBus(null);
        }

		return null;
	}
		
	public void setController(ServletController controller){
		this.controller = controller;
	}
}
