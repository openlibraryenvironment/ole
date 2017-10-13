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
package org.kuali.rice.kew.documentoperation.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.ListUtils;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * struts form bean for {@link DocumentOperationAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentOperationForm extends KualiForm {

	private static final long serialVersionUID = 2994179393392218743L;
	private DocumentRouteHeaderValue routeHeader;
    private String documentId;

    private List actionRequestOps = new ArrayList();
    private List actionTakenOps = new ArrayList();
    private List actionItemOps = new ArrayList();


    private String routeHeaderOp;

    private String dateModified;
    private String createDate;
    private String approvedDate;
    private String finalizedDate;
    private String routeStatusDate;
    private String lookupableImplServiceName;
    private String lookupType;
    private Map docStatuses = KewApiConstants.DOCUMENT_STATUSES;
    private Map actionRequestCds = KewApiConstants.ACTION_REQUEST_CD;
    private Map actionTakenCds = KewApiConstants.ACTION_TAKEN_CD;
    private List routeModules;
    private String routeModuleName;

    //variabes for RouteNodeInstances and branches
    private List routeNodeInstances=ListUtils.lazyList(new ArrayList(),
            new Factory() {
        		public Object create() {
        			return new RouteNodeInstance();
        		}
       		});

    private List routeNodeInstanceOps=new ArrayList();
    private List branches=ListUtils.lazyList(new ArrayList(),
            new Factory() {
				public Object create() {
					return new Branch();
				}
			});
    private List branchOps=new ArrayList();
    private List nodeStateDeleteOps=new ArrayList();
    private String nodeStatesDelete;
    private String branchStatesDelete;
    private String initialNodeInstances;

    private String annotation;

    private String blanketApproveUser;
    private String blanketApproveActionTakenId;
    private String blanketApproveNodes;
    private String actionInvocationUser;
    private String actionInvocationActionItemId;
    private String actionInvocationActionCode;

    private List<ActionRequestValue> actionRequests = new ArrayList<ActionRequestValue>();
    private List<ActionTakenValue> actionsTaken = new ArrayList<ActionTakenValue>();
    private List<ActionItem> actionItems = new ArrayList<ActionItem>();

    public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getInitialNodeInstances(){
    	return initialNodeInstances;
    }

    public void setInitialNodeInstances(String initialNodeInstances){
    	this.initialNodeInstances=initialNodeInstances;
    }

    public String getNodeStatesDelete(){
    	return nodeStatesDelete;
    }

    public void setNodeStatesDelete(String nodeStatesDelete){
    	this.nodeStatesDelete=nodeStatesDelete;
    }

    public String getBranchStatesDelete(){
    	return branchStatesDelete;
    }

    public void setBranchStatesDelete(String branchStatesDelete){
    	this.branchStatesDelete=branchStatesDelete;
    }

    public DocumentOperationForm(){
        routeHeader = new DocumentRouteHeaderValue();
    }

    public DocumentRouteHeaderValue getRouteHeader() {
        return routeHeader;
    }
    public void setRouteHeader(DocumentRouteHeaderValue routeHeader) {
        this.routeHeader = routeHeader;
    }

    public DocOperationIndexedParameter getActionRequestOp(int index) {
        while (actionRequestOps.size() <= index) {
            actionRequestOps.add(new DocOperationIndexedParameter(new Integer(index), KewApiConstants.NOOP));
        }
        return (DocOperationIndexedParameter) getActionRequestOps().get(index);
    }

    public DocOperationIndexedParameter getActionTakenOp(int index) {
        while (actionTakenOps.size() <= index) {
            actionTakenOps.add(new DocOperationIndexedParameter(new Integer(index), KewApiConstants.NOOP));
        }
        return (DocOperationIndexedParameter) getActionTakenOps().get(index);
    }

    public DocOperationIndexedParameter getRouteNodeInstanceOp(int index) {
        while (routeNodeInstanceOps.size() <= index) {
        	routeNodeInstanceOps.add(new DocOperationIndexedParameter(new Integer(index), KewApiConstants.NOOP));
        }
        return (DocOperationIndexedParameter) getRouteNodeInstanceOps().get(index);
    }

    public DocOperationIndexedParameter getBranchOp(int index) {
        while (branchOps.size() <= index) {
        	branchOps.add(new DocOperationIndexedParameter(new Integer(index), KewApiConstants.NOOP));
        }
        return (DocOperationIndexedParameter) getBranchOps().get(index);
    }

    public DocOperationIndexedParameter getActionItemOp(int index) {
        while (actionItemOps.size() <= index) {
            actionItemOps.add(new DocOperationIndexedParameter(new Integer(index), KewApiConstants.NOOP));
        }
        return (DocOperationIndexedParameter) getActionItemOps().get(index);
    }

    public DocOperationIndexedParameter getNodeStateDeleteOp(int index){
    	while(nodeStateDeleteOps.size()<=index){
    		nodeStateDeleteOps.add(new DocOperationIndexedParameter(new Integer(index),""));
    	}
    	return(DocOperationIndexedParameter) getNodeStateDeleteOps().get(index);
    }

    public List getActionItemOps() {
        return actionItemOps;
    }
    public void setActionItemOps(List actionItemOps) {
        this.actionItemOps = actionItemOps;
    }
    public List getActionRequestOps() {
        return actionRequestOps;
    }
    public void setActionRequestOps(List actionRequestOps) {
        this.actionRequestOps = actionRequestOps;
    }
    public List getActionTakenOps() {
        return actionTakenOps;
    }
    public List getRouteNodeInstanceOps() {
        return routeNodeInstanceOps;
    }

    public List getBranchOps(){
    	return branchOps;
    }

    public List getNodeStateDeleteOps(){
    	return nodeStateDeleteOps;
    }

    public void setActionTakenOps(List actionTakenOps) {
        this.actionTakenOps = actionTakenOps;
    }

    public void setRouteNodeInstanceOps(List routeNodeInstanceOps) {
        this.routeNodeInstanceOps = routeNodeInstanceOps;
    }

    public void setBranchOps(List branchOps){
    	this.branchOps=branchOps;
    }

    public void setNodeStateDeleteOps(List nodeStateDeleteOps){
    	this.nodeStateDeleteOps=nodeStateDeleteOps;
    }

    public String getRouteHeaderOp() {
        return routeHeaderOp;
    }
    public void setRouteHeaderOp(String routeHeaderOp) {
        this.routeHeaderOp = routeHeaderOp;
    }
    public String getApprovedDate() {
        return approvedDate;
    }
    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getFinalizedDate() {
        return finalizedDate;
    }
    public void setFinalizedDate(String finalizedDate) {
        this.finalizedDate = finalizedDate;
    }
    public String getRouteStatusDate() {
        return routeStatusDate;
    }
    public void setRouteStatusDate(String routeStatusDate) {
        this.routeStatusDate = routeStatusDate;
    }
    public String getDateModified() {
        return dateModified;
    }
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
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

    public Map getDocStatuses() {
        return docStatuses;
    }

    public Map getActionRequestCds() {
        return actionRequestCds;
    }
    public Map<String, String> getActionRequestRecipientTypes() {
    	Map<String, String> actionRequestRecipientTypes = new HashMap<String, String>();
    	for (RecipientType recipientType : RecipientType.values()) {
    		actionRequestRecipientTypes.put(recipientType.getCode(), recipientType.getLabel());
    	}
        return actionRequestRecipientTypes;
    }
    public Map<String, String> getActionRequestStatuses() {
        Map<String, String> actionRequestStatuses = new HashMap<String, String>();
    	for (ActionRequestStatus requestStatus : ActionRequestStatus.values()) {
    		actionRequestStatuses.put(requestStatus.getCode(), requestStatus.getLabel());
    	}
        return actionRequestStatuses;
    }

    public  List<ActionRequestValue> getActionRequests() {
        if (ObjectUtils.isNull(actionRequests) || actionRequests.isEmpty()) {
            List<ActionRequestValue> actionRequestsList = KEWServiceLocator.getActionRequestService().findByDocumentIdIgnoreCurrentInd(documentId);
            this.actionRequests = actionRequestsList;
        }
        return actionRequests;
    }

    public  List<ActionTakenValue> getActionsTaken() {
        if (ObjectUtils.isNull(actionsTaken) || actionsTaken.isEmpty()) {
            List<ActionTakenValue> actionsTakenList = KEWServiceLocator.getActionTakenService().findByDocumentIdIgnoreCurrentInd(documentId);
            this.actionsTaken = actionsTakenList;
        }
        return actionsTaken;
    }

    public  List<ActionItem> getActionItems() {
        if (ObjectUtils.isNull(actionItems) || actionItems.isEmpty()) {
            List<ActionItem> actionItemsList =  (List<ActionItem>)KEWServiceLocator.getActionListService().findByDocumentId(documentId);
            this.actionItems = actionItemsList;
        }
        return actionItems;
    }

    public Map getActionTakenCds() {
        return actionTakenCds;
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List getRouteModules() {
        return routeModules;
    }

    public void setRouteModules(List routeModules) {
        this.routeModules = routeModules;
    }

    public String getRouteModuleName() {
        return routeModuleName;
    }

    public void setRouteModuleName(String routeModuleName) {
        this.routeModuleName = routeModuleName;
    }

    /*
     * methods for route node instances
     */

    public List getRouteNodeInstances(){
    	return routeNodeInstances;
    }

    public void setRouteNodeInstances(List routeNodeInstances){
    	this.routeNodeInstances=routeNodeInstances;
    }

    public RouteNodeInstance getRouteNodeInstance(int index){
    	while (getRouteNodeInstances().size() <= index) {
                getRouteNodeInstances().add(new RouteNodeInstance());
            }
            return (RouteNodeInstance) getRouteNodeInstances().get(index);
    }

    public List getBranches(){
    	return branches;
    }

    public void setBranches(List branches){
    	this.branches=branches;
    }

    public Branch getBranche(int index){
    	while(getBranches().size()<=index){
    		getBranches().add(new Branch());
    	}
    	return (Branch) getBranches().get(index);
    }

    public void resetOps(){
    	routeNodeInstanceOps=new ArrayList();
    	branchOps=new ArrayList();
    	actionRequestOps = new ArrayList();
        actionTakenOps = new ArrayList();
        actionItemOps = new ArrayList();
    }

	public String getActionInvocationActionCode() {
		return actionInvocationActionCode;
	}

	public void setActionInvocationActionCode(String actionInvocationActionCode) {
		this.actionInvocationActionCode = actionInvocationActionCode;
	}

	public String getActionInvocationActionItemId() {
		return actionInvocationActionItemId;
	}

	public void setActionInvocationActionItemId(String actionInvocationActionItemId) {
		this.actionInvocationActionItemId = actionInvocationActionItemId;
	}

	public String getActionInvocationUser() {
		return actionInvocationUser;
	}

	public void setActionInvocationUser(String actionInvocationUser) {
		this.actionInvocationUser = actionInvocationUser;
	}

	public String getBlanketApproveActionTakenId() {
		return blanketApproveActionTakenId;
	}

	public void setBlanketApproveActionTakenId(String blanketApproveActionTakenId) {
		this.blanketApproveActionTakenId = blanketApproveActionTakenId;
	}

	public String getBlanketApproveNodes() {
		return blanketApproveNodes;
	}

	public void setBlanketApproveNodes(String blanketApproveNodes) {
		this.blanketApproveNodes = blanketApproveNodes;
	}

	public String getBlanketApproveUser() {
		return blanketApproveUser;
	}

	public void setBlanketApproveUser(String blanketApproveUser) {
		this.blanketApproveUser = blanketApproveUser;
	}


}
