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
package org.kuali.rice.ksb.messaging.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.RequestProcessor;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADUtils;

/**
 * A RequestProcessor implementation for Struts which handles determining whether or not access
 * should be allowed to the requested KSB page.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KSBStrutsRequestProcessor extends RequestProcessor {
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.apache.struts.action.RequestProcessor#processPreprocess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected boolean processPreprocess(HttpServletRequest request,
			HttpServletResponse response) {
		final UserSession session = KRADUtils.getUserSessionFromRequest(request);

        if (session == null) {
            throw new IllegalStateException("the user session has not been established");
        }

        GlobalVariables.setUserSession(session);
        GlobalVariables.clear();
		return super.processPreprocess(request, response);
	}
}
