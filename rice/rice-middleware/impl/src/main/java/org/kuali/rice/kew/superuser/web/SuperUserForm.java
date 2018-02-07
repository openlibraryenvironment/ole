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
package org.kuali.rice.kew.superuser.web;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.KewRoutingKualiForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A Struts ActionForm for the {@link SuperUserAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SuperUserForm extends KewRoutingKualiForm {

    private static final long serialVersionUID = 982228198266403397L;
    private String documentId;
    private String docHandlerUrl;
    private Integer routeLevel;
    private List<String> futureNodeNames = new ArrayList<String>();
    private String destNodeName;
    private String returnDestNodeName;
    private String action;
    private List<ActionRequestValue> actionRequests = new ArrayList<ActionRequestValue>();
    private String actionTakenActionRequestId;
    private String actionTakenNetworkId;
    private String actionTakenWorkGroupId;
    private String actionTakenRecipientCode;
    private boolean authorized;
    private boolean blanketApprove;
    private String methodToCall = "";
    private boolean runPostProcessorLogic = true;
    private String[] actionRequestRunPostProcessorCheck;

    private String lookupableImplServiceName;
    private String lookupType;

    private DocumentRouteHeaderValue routeHeader;

    // KULRICE-3035: Added the ability to store the doc search's "returnLocation" property so that the superuser form can create a proper "cancel" button.
    private String returnLocation;
    
    @Override
	public String getMethodToCall() {
        return methodToCall;
    }
    @Override
	public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public boolean isBlanketApprove() {
        return blanketApprove;
    }

    public void setBlanketApprove(boolean blanketApprove) {
        this.blanketApprove = blanketApprove;
    }

    public DocumentRouteHeaderValue getRouteHeader() {
        return routeHeader;
    }

    public void setRouteHeader(DocumentRouteHeaderValue routeHeader) {
        this.routeHeader = routeHeader;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocHandlerUrl() {
        return docHandlerUrl;
    }

    public void setDocHandlerUrl(String docHandlerUrl) {
        this.docHandlerUrl = docHandlerUrl;
    }

    public Integer getRouteLevel() {
        return routeLevel;
    }

    public void setRouteLevel(Integer routeLevel) {
        this.routeLevel = routeLevel;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<ActionRequestValue> getActionRequests() {
        return actionRequests;
    }

    public void setActionRequests(List<ActionRequestValue> actionRequests) {
        this.actionRequests = actionRequests;
    }

    public String getActionTakenActionRequestId() {
        return actionTakenActionRequestId;
    }

    public void setActionTakenActionRequestId(String actionTakenActionRequestId) {
        this.actionTakenActionRequestId = actionTakenActionRequestId;
    }

    public String getActionTakenNetworkId() {
        return actionTakenNetworkId;
    }

    public void setActionTakenNetworkId(String actionTakenNetworkId) {
        this.actionTakenNetworkId = actionTakenNetworkId;
    }

    public String getActionTakenWorkGroupId() {
        return actionTakenWorkGroupId;
    }

    public void setActionTakenWorkGroupId(String actionTakenWorkGroupId) {
        this.actionTakenWorkGroupId = actionTakenWorkGroupId;
    }

    /*public List getRouteLevels() {
        return routeLevels;
    }

    public void setRouteLevels(List routeLevels) {
        this.routeLevels = routeLevels;
    }*/

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    @Override
	public void reset(ActionMapping mapping, HttpServletRequest request){
        this.futureNodeNames = new ArrayList<String>();
    }

    public String getActionTakenRecipientCode() {
        return actionTakenRecipientCode;
    }

    public void setActionTakenRecipientCode(String actionTakenRecipientCode) {
        this.actionTakenRecipientCode = actionTakenRecipientCode;
    }

    public boolean isSUDocument() {
	if (routeHeader.isStateInitiated() || routeHeader.isStateSaved()) {
            return false;
        }
        return true;
    }

    public boolean isStateAllowsAction() {
        if ( routeHeader.isProcessed() || routeHeader.isDisaproved() ) {
            return false;
        }
        return true;
    }

    public boolean isSuperUserFinalApproveAllowed() {
        return getDocumentType().getAllowSuperUserFinalApprovalPolicy().getPolicyValue().booleanValue();
    }

    public boolean isSuperUserFinalApproveAllowedForActionRequest() {
        if (!isSuperUserFinalApproveAllowed() &&
            KEWServiceLocator.getRouteNodeService().findFutureNodeNames(getRouteHeader().getDocumentId()).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public DocumentType getDocumentType() {
        return getRouteHeader().getDocumentType();
    }

    public Set<KeyValue> getPreviousNodes() throws Exception {
    	List<String> nodeNames = KewApiServiceLocator.getWorkflowDocumentService().getPreviousRouteNodeNames(routeHeader.getDocumentId());
        Set<KeyValue> previousNodes = new HashSet<KeyValue>();
        for (String nodeName : nodeNames) {
			previousNodes.add(new ConcreteKeyValue(nodeName, nodeName));
		}
        return previousNodes;
    }

    public String getDestNodeName() {
        return destNodeName;
    }
    public void setDestNodeName(String previousNodeName) {
        this.destNodeName = previousNodeName;
    }
    public List<String> getFutureNodeNames() {
        return futureNodeNames;
    }
    public void setFutureNodeNames(List<String> futureNodeNames) {
        this.futureNodeNames = futureNodeNames;
    }
    public String getReturnDestNodeName() {
        return returnDestNodeName;
    }
    public void setReturnDestNodeName(String returnDestNodeName) {
        this.returnDestNodeName = returnDestNodeName;
    }
    public String getLookupableImplServiceName() {
        return lookupableImplServiceName;
    }
    public void setLookupableImplServiceName(String lookupableImplServiceName) {
        this.lookupableImplServiceName = lookupableImplServiceName;
    }
    public String getLookupType() {
        return lookupType;
    }
    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }
    public boolean isRunPostProcessorLogic() {
        return this.runPostProcessorLogic;
    }
    public void setRunPostProcessorLogic(boolean runPostProcessorLogic) {
        this.runPostProcessorLogic = runPostProcessorLogic;
    }
    public String[] getActionRequestRunPostProcessorCheck() {
        return this.actionRequestRunPostProcessorCheck;
    }
    public void setActionRequestRunPostProcessorCheck(String[] actionRequestRunPostProcessorCheck) {
        this.actionRequestRunPostProcessorCheck = actionRequestRunPostProcessorCheck;
    }
    public Boolean getActionRequestPostProcessorCheck(String actionRequestId) {
        return ArrayUtils.contains(getActionRequestRunPostProcessorCheck(), actionRequestId);
    }
    public List<String> getActionRequestPostProcessorCheck() {
        if (getActionRequestRunPostProcessorCheck() == null) {
            return null;
        }
        return Arrays.asList(getActionRequestRunPostProcessorCheck());
    }

    public String getReturnLocation() {
    	return returnLocation;
    }
    public void setReturnLocation(String returnLocation) {
    	this.returnLocation = returnLocation;
    }
    public boolean getActionRequestPostProcessorDisplayCheck(){
    	return getDocumentType().getSuPostprocessorOverridePolicy().getPolicyValue().booleanValue();
    }
}
