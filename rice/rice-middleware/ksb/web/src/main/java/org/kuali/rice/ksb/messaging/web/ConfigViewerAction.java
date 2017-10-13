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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.impl.config.property.ConfigLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/**
 * This is a description of what this class does - g don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ConfigViewerAction extends KSBAction{

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ksb.messaging.web.KSBAction#establishRequiredState(javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
	@Override
	public ActionMessages establishRequiredState(HttpServletRequest request,
			ActionForm actionForm) throws Exception {
		ConfigViewerForm  form = (ConfigViewerForm)actionForm;
		form.setProperties(this.getFilteredConfigList());
				
		return null;
	}
	
	protected List<KeyValue> getFilteredConfigList(){
		List<KeyValue> lRet = new ArrayList<KeyValue>();
		Properties p = ConfigContext.getCurrentContextConfig().getProperties();
		for(Object o: p.keySet()){
			String key = (String)o;			
			
			lRet.add(new ConcreteKeyValue(key,ConfigLogger.getDisplaySafeValue(key, p.getProperty(key))));			
		}
		return lRet;
	}

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ksb.messaging.web.KSBAction#start(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward start(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return mapping.findForward("basic");
	}

}
