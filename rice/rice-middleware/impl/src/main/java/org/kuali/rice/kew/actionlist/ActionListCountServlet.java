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
package org.kuali.rice.kew.actionlist;

import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



public class ActionListCountServlet extends HttpServlet {

	private static final long serialVersionUID = 260649920715567145L;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionListCountServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
	    PrintWriter out = response.getWriter();
	    int count = getCount(request);
	    out.println(Integer.toString(count));
	    out.close();
	}

	private int getCount(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			if (id == null || id.equals("")) {
				return 0;
			}
			String idType = request.getParameter("idType");
			if (idType == null || idType.equals("")) {
				idType = "a";
			}
			String principalId = null;
			if ("emplId".equalsIgnoreCase(idType) || "e".equalsIgnoreCase(idType)) {
				Person person = KimApiServiceLocator.getPersonService().getPersonByEmployeeId(id);
				if (person != null) {
					principalId = person.getPrincipalId();
				}
			} else if ("workflowId".equalsIgnoreCase(idType) || "w".equalsIgnoreCase(idType)) {
				principalId = id;
		    } else if ("authenticationId".equalsIgnoreCase(idType) || "a".equalsIgnoreCase(idType)) {
		    	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(id);
		    	if (principal != null) {
		    		principalId = principal.getPrincipalId();
		    	}
		    }
			if (principalId == null) {
				return 0;
			}
			return KEWServiceLocator.getActionListService().getCount(principalId);
		} catch (Throwable t) {
			LOG.error("Fatal error when querying for Action List Count", t);
			return 0;
		}
	}



}
