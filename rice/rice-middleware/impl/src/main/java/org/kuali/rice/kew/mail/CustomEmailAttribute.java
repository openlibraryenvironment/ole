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
 * Application areas can implement this interface to generate custom email content.
 * 
 * At the point that getCustomEmailSubject() and getCustomEmailBody() are called, the
 * RouteHeaderVO and ActionRequestVO have already been set so the implementing class can
 * simply call the approriate getters to retrieve the data beans.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface CustomEmailAttribute {

	/**
	 * Returns a String that will be appended to the standard email subject.
	 */
    public String getCustomEmailSubject() throws Exception;
    
    /**
     * Returns a String that will be appeneded to the standard email body.
     */
    public String getCustomEmailBody() throws Exception;
    
    /**
     * Gets the RouteHeaderVO bean which has document data 
     * @return
     */
    public Document getRouteHeaderVO();
    public void setRouteHeaderVO(Document routeHeaderVO);
    public ActionRequest getActionRequestVO();
    public void setActionRequestVO(ActionRequest actionRequestVO);
    
}
