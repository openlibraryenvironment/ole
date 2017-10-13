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

import java.io.Serializable;
import java.util.Map;

import org.kuali.rice.kew.api.util.CodeTranslator;

/**
 * A bean for the web-tier when represents the recipient of an Ad Hoc request.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AppSpecificRouteRecipient implements Serializable {

	private static final long serialVersionUID = 6587140192756322878L;

	private static Map actionRequestCds;
    protected String type;
    protected String actionRequested;
    protected String id;  //can be networkId or groupId (although, currently, it's being treated as principal name or group name)
    protected String namespaceCode; // Can be a group namespace code or a person name
    protected String actionRequestId; // The action request ID of the AdHoc route action that was sent for this app specific recipient, if any.

    public String getActionRequested() {
        return actionRequested;
    }
    public void setActionRequested(String actionRequested) {
        this.actionRequested = actionRequested;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public String getNamespaceCode() {
    	return namespaceCode;
    }
    public void setNamespaceCode(String namespaceCode) {
    	this.namespaceCode = namespaceCode;
    }

    public String getActionRequestId() {
    	return actionRequestId;
    }

    public void setActionRequestId(String actionRequestId) {
    	this.actionRequestId = actionRequestId;
    }

    public String getActionRequestedValue(){
        actionRequestCds.clear();
        actionRequestCds.putAll(CodeTranslator.arLabels);
        if(getActionRequested() != null && !getActionRequested().trim().equals("")){
            return (String) actionRequestCds.get(getActionRequested());
        }
        return null;
    }

}
