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
package org.kuali.rice.kns.web.struts.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class handles the logout. After logout it will do an external redirect to an url
 * specified by a Parameter (LOGOUT_REDIRECT_URL) or a config property (rice.portal.logout.redirectUrl).
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiLogoutAction extends Action {

    /**
     * Invalidates the users session and redirects to a configurable url after logout.
     * 
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String redirectString = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KRADConstants.KNS_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KRADConstants.LOGOFF_REDIRECT_URL_PARAMETER);

        if(redirectString == null) {
            redirectString = ConfigContext.getCurrentContextConfig().getProperty(KRADConstants.LOGOFF_REDIRECT_URL_PROPERTY);
        }
        
        request.getSession().invalidate();
        
        return new ActionForward(redirectString, true);
    }

}
