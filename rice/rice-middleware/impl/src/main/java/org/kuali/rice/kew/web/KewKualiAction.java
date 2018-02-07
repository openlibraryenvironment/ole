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
package org.kuali.rice.kew.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.web.struts.action.KualiAction;

/**
 * A base action class for KEW screens.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class KewKualiAction extends KualiAction {
	
	public static final String DEFAULT_MAPPING = "basic";
	
	@Override 
	protected String getReturnLocation(HttpServletRequest request, ActionMapping mapping) 
    {
    	String mappingPath = mapping.getPath();
    	String basePath = getApplicationBaseUrl();
        return basePath + KewApiConstants.WEBAPP_DIRECTORY + mappingPath + ".do";
    }

	@Override
	protected ActionForward defaultDispatch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return start(mapping, form, request, response);
	}
	
	public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return mapping.findForward(getDefaultMapping());
	}
	
	protected String getDefaultMapping() {
		return DEFAULT_MAPPING;
	}
	
}
