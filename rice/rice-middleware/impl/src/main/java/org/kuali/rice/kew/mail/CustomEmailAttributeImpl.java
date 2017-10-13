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
package org.kuali.rice.kew.mail;

import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.document.Document;


/**
 * Default implementation of the {@link CustomEmailAttribute}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CustomEmailAttributeImpl implements CustomEmailAttribute {

    private Document routeHeaderVO;
    private ActionRequest actionRequestVO;

    public CustomEmailAttributeImpl() {}
    
    public String getCustomEmailSubject() throws Exception {
        return "";
    }
    
    public String getCustomEmailBody() throws Exception {
        return "";
    }

    public Document getRouteHeaderVO() {
        return routeHeaderVO;
    }

    public void setRouteHeaderVO(Document routeHeaderVO) {
        this.routeHeaderVO = routeHeaderVO;
    }

	public ActionRequest getActionRequestVO() {
		return actionRequestVO;
	}

	public void setActionRequestVO(ActionRequest actionRequestVO) {
		this.actionRequestVO = actionRequestVO;
	}

}
