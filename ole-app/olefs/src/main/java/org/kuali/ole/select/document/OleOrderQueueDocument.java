/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.cxf.common.util.StringUtils;
import org.joda.time.DateTime;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentHeaderService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.workflow.service.WorkflowDocumentService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * This class is the document class for Order Holding Queue.
 */
public class OleOrderQueueDocument extends TransactionalDocumentBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleOrderQueueDocument.class);

    private String documentNumber;

    private String selectedUserId;

    private String selectorUserId;

    private String principalName;

	/*private Timestamp createdDate;

    private Timestamp modifiedDate;*/

    private String statusCode;

    private Integer totalPrice;

    private String active;

    public List<OleRequisitionItem> requisitionItems = new ArrayList<OleRequisitionItem>();

    // Added for OLE-1976 Order Queue Search Enhancements
    private String title;

    private String author;

    private String publisher;

    private String isbn;

    private String requisitionDocNumber;

    private String internalRequestorId;

    private String externalRequestorId;

    private String requestorName;

    private String requisitionStatus;

    private String requisitionStatusCode;

    private String vendorName;

    private String formatTypeId;

    private OleFormatType oleFormatType;

    private Timestamp workflowStatusChangeDateFrom;

    private Timestamp workflowStatusChangeDateTo;

    private String requisitionSelectorUserId;

    private String requisitionSelectorPrincipalName;

    /**
     * Stores the value of selectorUserName.
     */
    private String selectorUserName;

    /**
     * Stores the value of accountNumber.
     */
    private String accountNumber;

    /**
     * Stores the value of objectCode.
     */
    private String objectCode;

    /**
     * Stores the value of chartOfAccountsCode.
     */
    private String chartOfAccountsCode;

    //private String selectorRoleName;

    // Added for OLE-1976 Order Queue Search Enhancements Ends

    @Override
    public String getDocumentNumber() {
        return documentNumber;
    }

    @Override
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSelectorUserId() {
        return selectorUserId;
    }

    public void setSelectorUserId(String selectorUserId) {
        this.selectorUserId = selectorUserId;
    }

    /*public Timestamp getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }*/
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Gets the principalName attribute.
     *
     * @return Returns the principalName.
     */
    public String getPrincipalName() {
        return principalName;
    }

    /**
     * Sets the principalName attribute value.
     *
     * @param principalName The principalName to set.
     */
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    /**
     * Gets the requisitions attribute.
     *
     * @return Returns the requisitions.
     */
    public List<OleRequisitionItem> getRequisitionItems() {
        return requisitionItems;
    }

    /**
     * Sets the requisitions attribute value.
     *
     * @param requisitions The requisitions to set.
     */
    public void setRequisitionItems(List<OleRequisitionItem> requisitionItems) {
        this.requisitionItems = requisitionItems;
    }

    public void addRequisitions(OleRequisitionItem data) {
        this.requisitionItems.add(data);
    }

    public boolean isRequisitionAdded() {
        if (this.requisitionItems != null) {
            return this.requisitionItems.size() > 0;
        } else {
            return false;
        }
    }

    // Added for OLE-1976 Order Queue Search Enhancements
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getRequisitionDocNumber() {
        return requisitionDocNumber;
    }

    public void setRequisitionDocNumber(String requisitionDocNumber) {
        this.requisitionDocNumber = requisitionDocNumber;
    }

    public String getInternalRequestorId() {
        return internalRequestorId;
    }

    public void setInternalRequestorId(String internalRequestorId) {
        this.internalRequestorId = internalRequestorId;
    }

    public String getExternalRequestorId() {
        return externalRequestorId;
    }

    public void setExternalRequestorId(String externalRequestorId) {
        this.externalRequestorId = externalRequestorId;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public String getRequisitionStatusCode() {
        return requisitionStatusCode;
    }

    public void setRequisitionStatusCode(String requisitionStatusCode) {
        this.requisitionStatusCode = requisitionStatusCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getFormatTypeId() {
        return formatTypeId;
    }

    public void setFormatTypeId(String formatTypeId) {
        this.formatTypeId = formatTypeId;
    }

    public OleFormatType getOleFormatType() {
        return oleFormatType;
    }

    public void setOleFormatType(OleFormatType oleFormatType) {
        this.oleFormatType = oleFormatType;
    }

    public Timestamp getWorkflowStatusChangeDateFrom() {
        return workflowStatusChangeDateFrom;
    }

    public void setWorkflowStatusChangeDateFrom(Timestamp workflowStatusChangeDateFrom) {
        this.workflowStatusChangeDateFrom = workflowStatusChangeDateFrom;
    }

    public Timestamp getWorkflowStatusChangeDateTo() {
        return workflowStatusChangeDateTo;
    }

    public void setWorkflowStatusChangeDateTo(Timestamp workflowStatusChangeDateTo) {
        this.workflowStatusChangeDateTo = workflowStatusChangeDateTo;
    }

    public String getRequisitionSelectorUserId() {
        return requisitionSelectorUserId;
    }

    public void setRequisitionSelectorUserId(String requisitionSelectorUserId) {
        this.requisitionSelectorUserId = requisitionSelectorUserId;
    }

    public String getRequisitionSelectorPrincipalName() {
        return requisitionSelectorPrincipalName;
    }

    public void setRequisitionSelectorPrincipalName(String requisitionSelectorPrincipalName) {
        this.requisitionSelectorPrincipalName = requisitionSelectorPrincipalName;
    }

    /**
     * Getter for property: selectorUserName.<br/>
     * Stores the value of selectorUserName.
     */
    public String getSelectorUserName() {
        return selectorUserName;
    }

    /**
     * Setter for property: selectorUserName.<br/>
     * Stores the value of selectorUserName.
     */
    public void setSelectorUserName(String selectorUserName) {
        this.selectorUserName = selectorUserName;
    }

    /**
     * Getter for property: accountNumber.<br/>
     * Stores the value of accountNumber.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Setter for property: accountNumber.<br/>
     * Stores the value of accountNumber.
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Getter for property: objectCode.<br/>
     * Stores the value of objectCode.
     */
    public String getObjectCode() {
        return objectCode;
    }

    /**
     * Setter for property: objectCode.<br/>
     * Stores the value of objectCode.
     */
    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    /**
     * Getter for property: chartOfAccountsCode.<br/>
     * Stores the value of chartOfAccountsCode.
     */
    public String getChartOfAccountsCode() {
        return chartOfAccountsCode;
    }

    /**
     * Setter for property: chartOfAccountsCode.<br/>
     * Stores the value of chartOfAccountsCode.
     */
    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }
    // Added for OLE-1976 Order Queue Search Enhancements Ends

    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */
    @SuppressWarnings("rawtypes")

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        return m;
    }

    /**
     * This method invokes approveDocument of DocumentService to approve selected requisitions
     * and sets errors appriopriately if approval fails.
     */
    public void massApprove() {
        LOG.debug("Inside massApprove of OleOrderQueueDocument");
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();
        ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
        boolean orderQueue = false;
        boolean orderQueueNotApproved = false;
        WorkflowDocumentService workflowDocumentService = KRADServiceLocatorWeb.getWorkflowDocumentService();
        Person person = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        WorkflowDocument workflowDocument;
        String docNum = null;
        StringBuilder orderQueueRequisitionApproved = new StringBuilder();
        StringBuilder orderQueueRequisitionNotApproved = new StringBuilder();
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>();
        for (OleRequisitionItem item : requisitionItems) {
            boolean itemAdded = item.isItemAdded();
            if (itemAdded) {
                try {
                    workflowDocument = workflowDocumentService.loadWorkflowDocument(item.getRequisition().getDocumentNumber(), person);
                    if (workflowDocument.getInitiatorPrincipalId().equals(GlobalVariables.getUserSession().getPerson().getPrincipalId())) {
                        if (DocumentStatus.ENROUTE.equals(workflowDocument.getStatus())) {
                            List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(item.getRequisition().getDocumentNumber());
                            for (ActionRequestValue actionRequest : actionReqValues) {
                                if (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(actionRequest.getActionRequested())) {
                                    Timestamp currentTime = SpringContext.getBean(DateTimeService.class).getCurrentTimestamp();
                                    List<ActionItem> actionItems = actionRequest.getActionItems();
                                    for (ActionItem actionItem : actionItems) {
                                        if (KewApiConstants.ACTION_REQUEST_APPROVE_REQ_LABEL.equals(actionItem.getActionRequestLabel())) {
                                            actionItem.setPrincipalId(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                                            actionItem.setDateAssigned(currentTime);
                                            actionListSrv.saveActionItem(actionItem);
                                        }
                                    }
                                    actionRequest.setPrincipalId(GlobalVariables.getUserSession().getPerson().getPrincipalId());
                                    actionRequest.setCreateDate(currentTime);
                                    actionReqSrv.saveActionRequest(actionRequest);


                                    boolean documentApproved = approveDocument(new Long(item.getRequisition().getPurapDocumentIdentifier()), item.getRequisition().getDocumentNumber(), actionRequest.getAnnotation());
                                    if (!documentApproved) {
                                        refreshItems.add(item);
                                    } else {
                                        if (!item.getRequisition().getDocumentNumber().equalsIgnoreCase(docNum)) {
                                            orderQueueRequisitionApproved.append(item.getRequisition().getDocumentNumber()).append(",");
                                            orderQueue = true;
                                            docNum = item.getRequisition().getDocumentNumber();
                                        }
                                    }
                                } else {
                                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_NOAPPROVALS_REQD, new String[]{item.getRequisition().getDocumentNumber()});
                                    refreshItems.add(item);
                                }
                            }
                        } else if (workflowDocument.isSaved()) {
                            boolean documentSubmitted = submitDocument(Long.valueOf(item.getRequisition().getPurapDocumentIdentifier()), item.getRequisition().getDocumentNumber(), OLEConstants.OrderQueue.SUBMIT_ANNOTATION);
                            if (!documentSubmitted) {
                                refreshItems.add(item);
                            } else {
                                orderQueueRequisitionApproved.append(item.getRequisition().getDocumentNumber()).append(",");
                                orderQueue = true;
                            }
                        } else {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_APPROVE, new String[]{workflowDocument.getStatus().toString(), item.getRequisition().getDocumentNumber()});
                            refreshItems.add(item);
                        }
                    } else {
                        orderQueueRequisitionNotApproved.append(item.getRequisition().getDocumentNumber()).append(",");
                        orderQueueNotApproved = true;
                    }
                } catch (WorkflowException ex) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, ex.getMessage());
                    refreshItems.add(item);
                }
            } else {
                refreshItems.add(item);
            }
        }
        int len = orderQueueRequisitionApproved.lastIndexOf(",");
        if (orderQueue) {
            orderQueueRequisitionApproved.replace(len, len + 1, " ");
            GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.MESSAGE_ORDERQUEUE_REQUISITIONS_APPROVED, new String[]{orderQueueRequisitionApproved.toString()});
        }

        int length = orderQueueRequisitionNotApproved.lastIndexOf(",");
        if (orderQueueNotApproved) {
            orderQueueRequisitionNotApproved.replace(length, length + 1, " ");
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_APPROVE_NOT_INITIATOR, new String[]{orderQueueRequisitionNotApproved.toString()});
        }
        requisitionItems = refreshItems;
        LOG.debug("Leaving massApprove of OleOrderQueueDocument");
    }

    /**
     * This method invokes approveDocument of DocumentService on the specified requisition document
     * and sets errors appriopriately if approval fails. Approves a document that is in enroute status.
     */
    private boolean approveDocument(Long requisitionDocumentId, String documentNumber, String annotation) {
        LOG.debug("Inside approveDocument of OleOrderQueueDocument");
        OleRequisitionDocument requisitionDocument = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(OleRequisitionDocument.class, requisitionDocumentId);
        requisitionDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(documentNumber));
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        try {
            requisitionDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().createWorkflowDocument(requisitionDocument.getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), principalPerson));
            SpringContext.getBean(DocumentService.class).approveDocument(requisitionDocument, principalPerson.getName() + OLEConstants.OrderQueue.APPROVE_ANNOTATION + annotation, null);
            return true;
        } catch (WorkflowException wfe) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_APPROVE_WFE, new String[]{documentNumber, wfe.getMessage()});
            return false;
        }
    }

    /**
     * This method invokes routeDocument of DocumentService on the specified requisition document
     * and sets errors appriopriately if approval fails. Submits a document that is in saved status.
     */
    private boolean submitDocument(Long requisitionDocumentId, String documentNumber, String annotation) {
        LOG.debug("Inside submitDocument of OleOrderQueueDocument");
        OleRequisitionDocument requisitionDocument = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(OleRequisitionDocument.class, requisitionDocumentId);
        requisitionDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(documentNumber));
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        try {
            requisitionDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(requisitionDocument.getDocumentNumber(), principalPerson));
            SpringContext.getBean(DocumentService.class).routeDocument(requisitionDocument, annotation, null);
            return true;
        } catch (WorkflowException wfe) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_SUBMIT_WFE, new String[]{documentNumber, wfe.getMessage()});
            return false;
        }
    }

    /**
     * This method validates for selector and assigns selected requisitions to the selector.
     */
    public void assign() {
        LOG.debug("Inside assign of OleOrderQueueDocument");
        if (selectedUserId == null || StringUtils.isEmpty(selectedUserId)) {
            Principal principalInfo = SpringContext.getBean(IdentityManagementService.class).getPrincipalByPrincipalName(GlobalVariables.getUserSession().getPerson().getPrincipalName());
            if (principalInfo != null) {
                selectedUserId = principalInfo.getPrincipalId();
            } else {
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_INVALID_PRINCIPAL_NAME,
                        new String[]{});
                return;
            }
            RoleService roleService = SpringContext.getBean(RoleService.class);
            // Check if the Selector actually has the role of SELECTOR
            String roleId = roleService.getRoleIdByNamespaceCodeAndName(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE_NAMESPACE, OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE);
            List<String> roleIds = new ArrayList<String>();
            roleIds.add(roleId);
            if (!roleService.principalHasRole(selectedUserId, roleIds, null)) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_INVALID_SELECTOR, new String[]{principalName});
                return;
            }
        }

        Long documentNumber;
        WorkflowDocumentService workflowDocumentService = KRADServiceLocatorWeb.getWorkflowDocumentService();
        Person person = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        WorkflowDocument workflowDocument;
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>();
        for (OleRequisitionItem item : requisitionItems) {
            boolean itemAdded = item.isItemAdded();
            if (itemAdded) {
                try {
                    workflowDocument = workflowDocumentService.loadWorkflowDocument(item.getRequisition().getDocumentNumber(), person);

                    // DocumentRouteHeaderValue routeHeader = SpringContext.getBean(RouteHeaderService.class).getRouteHeader(documentNumber);
                    Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(selectedUserId);
                    if (workflowDocument.isSaved() || workflowDocument.isEnroute()) {
                        assignActionRequests(workflowDocument.getDocumentId().toString());
                        GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.MESSAGE_ORDERQUEUE_REQUISITIONS_ASSIGNED, new String[]{item.getRequisition().getDocumentNumber(), principalName});
                    } else {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_ASSIGN, new String[]{workflowDocument.getStatus().toString(), item.getRequisition().getDocumentNumber()});
                        refreshItems.add(item);
                    }
                } catch (WorkflowException ex) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, ex.getMessage());
                    refreshItems.add(item);
                }

            } else {
                refreshItems.add(item);
            }
        }
        requisitionItems = refreshItems;
        LOG.debug("Leaving assign of OleOrderQueueDocument");
    }

    /**
     * This method assigns specified document to the selector.
     */
    private void assignActionRequests(String routeHeaderId) {
        LOG.debug("Inside assignActionRequests of OleOrderQueueDocument");
        Timestamp currentTime = SpringContext.getBean(DateTimeService.class).getCurrentTimestamp();
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();
        ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
        List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId);
        for (ActionRequestValue actionRequest : actionReqValues) {
            List<ActionItem> actionItems = actionRequest.getActionItems();
            for (ActionItem actionItem : actionItems) {
                actionItem.setPrincipalId(selectedUserId);
                actionItem.setDateAssigned(currentTime);
                actionListSrv.saveActionItem(actionItem);
            }
            actionRequest.setPrincipalId(selectedUserId);
            actionRequest.setCreateDate(currentTime);
            actionReqSrv.saveActionRequest(actionRequest);
        }
        LOG.debug("Leaving assignActionRequests of OleOrderQueueDocument");
    }

    /**
     * This method invokes cancelDocument of DocumentService to delete selected requisitions.
     * Sets error message appriopriately if this action fails.
     */
    public void delete() {
        LOG.debug("Inside delete of OleOrderQueueDocument");
        WorkflowDocumentService workflowDocumentService = SpringContext.getBean(WorkflowDocumentService.class);
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        WorkflowDocument workflowDocument;
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>();
        OleRequisitionDocument requisitionDocument;
        StringBuilder orderQueueRequisitionDeleted = new StringBuilder();
        boolean isErrorMsg = false;
        for (OleRequisitionItem item : requisitionItems) {
            boolean itemAdded = item.isItemAdded();
            if (itemAdded) {
                try {
                    workflowDocument = workflowDocumentService.loadWorkflowDocument(item.getRequisition().getDocumentNumber(), principalPerson);
                    if (workflowDocument.isSaved()) {
                        requisitionDocument = SpringContext.getBean(BusinessObjectService.class).findBySinglePrimaryKey(OleRequisitionDocument.class, Long.valueOf(item.getRequisition().getPurapDocumentIdentifier()));
                        requisitionDocument.setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(item.getRequisition().getDocumentNumber()));

                        try {
                            requisitionDocument.getDocumentHeader().setWorkflowDocument(workflowDocument);
                            SpringContext.getBean(DocumentService.class).cancelDocument(requisitionDocument, OLEConstants.OrderQueue.CANCEL_ANNOTATION);
                            orderQueueRequisitionDeleted.append(item.getRequisition().getDocumentNumber()).append(",");
                            isErrorMsg = true;
                        } catch (WorkflowException wfe) {
                            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_CANCELED_WFE, new String[]{requisitionDocument.getDocumentNumber(), wfe.getMessage()});
                            refreshItems.add(item);
                        }
                    } else {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_CANCELED, new String[]{workflowDocument.getStatus().toString(), item.getRequisition().getDocumentNumber()});
                        refreshItems.add(item);
                    }
                } catch (WorkflowException ex) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, ex.getMessage());
                    refreshItems.add(item);
                }
            } else {
                refreshItems.add(item);
            }
        }

        int len = orderQueueRequisitionDeleted.lastIndexOf(",");
        if (isErrorMsg) {
            orderQueueRequisitionDeleted.replace(len, len + 1, " ");
            GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.MESSAGE_ORDERQUEUE_REQUISITIONS_CANCELED, new String[]{orderQueueRequisitionDeleted.toString()});
        }
        requisitionItems = refreshItems;
        LOG.debug("Leaving delete of OleOrderQueueDocument");
    }

    /**
     * This method returns total price of the selected requisition items.
     *
     * @return Total Price of selected items
     */
    public KualiDecimal getTotalSelectedItemsPrice() {
        LOG.debug("Inside totalSelectedItems of OleOrderQueueDocument");
        KualiDecimal totalPrice = KualiDecimal.ZERO;
        ;
        StringBuilder orderQueueRequisitionHasNoPrice = new StringBuilder();
        boolean isInfoMsg = false;
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>();
        for (OleRequisitionItem item : requisitionItems) {
            if (item.isItemAdded()) {
                if (item.getSourceAccountingLines().size() > 0) {
                    if (item.getTotalAmount().equals(KualiDecimal.ZERO)) {
                        orderQueueRequisitionHasNoPrice.append(item.getRequisition().getDocumentNumber()).append(",");
                        isInfoMsg = true;
                    }
                    for (int i = 0; i < item.getSourceAccountingLines().size(); i++) {
                        KualiDecimal amount = item.getSourceAccountingLines().get(i).getAmount();
                        totalPrice = totalPrice.add(amount);
                    }
                } else {
                    if (item.getItemListPrice().equals(KualiDecimal.ZERO)) {
                        orderQueueRequisitionHasNoPrice.append(item.getRequisition().getDocumentNumber()).append(",");
                        isInfoMsg = true;
                    }
                    totalPrice = totalPrice.add(item.getItemListPrice());
                }
            }
            refreshItems.add(item);
        }
        requisitionItems = refreshItems;
        int len = orderQueueRequisitionHasNoPrice.lastIndexOf(",");
        if (isInfoMsg) {
            orderQueueRequisitionHasNoPrice.replace(len, len + 1, " ");
            GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.MESSAGE_ORDERQUEUE_REQUISITIONS_HAS_NO_PRICE, new String[]{orderQueueRequisitionHasNoPrice.toString()});
        }
        LOG.debug("Leaving totalSelectedItems of OleOrderQueueDocument");
        return totalPrice;
    }

    /**
     * This method populates a map with OleRequisitionItem fields for search criteria entered
     * from Order Holding Queue Page
     *
     * @return Map containing OleRequisitionItem fieldname and fieldvalue
     */
    public Map<String, String> populateRequisitionFields() throws Exception {
        LOG.debug("Inside populateRequisitionFields of OleOrderQueueDocument");
        Map<String, String> searchCriteriaMap = new HashMap<String, String>();
        DocumentEntry documentEntry = SpringContext.getBean(DataDictionaryService.class).getDataDictionary().getDocumentEntry(OleOrderQueueDocument.class.getName());

        Set attributeNames = documentEntry.getAttributeNames();
        for (Object attributeName : attributeNames) {
            String attr = (String) attributeName;
            if (!(OLEConstants.OrderQueue.selectorField.equals(attr) || OLEConstants.OrderQueue.workflowStatusChangeDateFrom.equals(attr) || OLEConstants.OrderQueue.workflowStatusChangeDateTo.equals(attr))) {
                String attrValue = (String) PropertyUtils.getProperty(this, attr);
                attr = OLEConstants.OrderQueue.REQUISITION_FIELDS.get(attr);
                if (ObjectUtils.isNotNull(attr) && ObjectUtils.isNotNull(attrValue)) {
                    searchCriteriaMap.put(attr, attrValue);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Search Criteria Size in OleOrderQueueDocument.populateRequisitionFields - " + searchCriteriaMap.size());
            }
        }
        LOG.debug("Leaving populateRequisitionFields of OleOrderQueueDocument");
        return searchCriteriaMap;
    }

    /**
     * This method filters the result list for other search criteria entered that are not a part of
     * Requisiton and Requisition Item related table.
     *
     * @param results
     * @return
     */
    public List<OleRequisitionItem> filterOtherSearchCriteria(List<OleRequisitionItem> results) {
        LOG.debug("Inside filterOtherSearchCriteria of OleOrderQueueDocument");
        boolean isSelectorLookup = false;
        if (this.selectorUserId != null) {
            isSelectorLookup = true;
        /*if(this.selectorUserId != null && !StringUtils.isEmpty(this.selectorUserId)){
            RoleService roleService = SpringContext.getBean(RoleService.class);
            // Check if the Selector actually has the role of SELECTOR
            String roleId = roleService.getRoleIdByNamespaceCodeAndName(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE_NAMESPACE, OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ROLE);
            List<String> roleIds = new ArrayList<String>();
            roleIds.add(roleId);
            if(roleService.principalHasRole(this.selectorUserId, roleIds, null)) {
                isSelectorLookup = true;
            }
            else{
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_INVALID_SELECTOR, new String[]{this.selectorUserId});
                return new ArrayList();
            }
        }*/
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Selector Lookup value in OleOrderQueueDocument.filterOtherSearchCriteria - " + isSelectorLookup);
        }

        // date lookup
        Map<String, List<String>> fixedParameters = preprocessDateFields();

        boolean isDateLookup = false;
        Map<String, Timestamp> workflowStatusChangeDate = new HashMap<String, Timestamp>();
        if (!((fixedParameters.get(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_FROM_DATE_LAST_MODIFIED) == null || fixedParameters.get(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_FROM_DATE_LAST_MODIFIED).get(0).isEmpty()) &&
                (fixedParameters.get(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_TO_DATE_LAST_MODIFIED) == null || fixedParameters.get(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_TO_DATE_LAST_MODIFIED).get(0).isEmpty()) &&
                (fixedParameters.get(OleSelectConstant.AcquisitionsSearch.INITIATOR) == null || fixedParameters.get(OleSelectConstant.AcquisitionsSearch.INITIATOR).get(0).isEmpty()))) {
            workflowStatusChangeDate = filterWorkflowStatusDate(results, fixedParameters);
            isDateLookup = true;
        }
        // date lookup
        if (LOG.isDebugEnabled()) {
            LOG.debug("Date Lookup value in OleOrderQueueDocument.filterOtherSearchCriteria - " + isDateLookup);
        }
        WorkflowDocumentService workflowDocumentService = SpringContext.getBean(WorkflowDocumentService.class);
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        WorkflowDocument workflowDocument;

        List<OleRequisitionItem> finalResult = new ArrayList<OleRequisitionItem>();
        boolean isValid = false;
        for (OleRequisitionItem item : results) {
            isValid = true;
            try {

                if (item.getRequisition().getDocumentHeader().getDocumentNumber() == null) {
                    item.getRequisition().setDocumentHeader(SpringContext.getBean(DocumentHeaderService.class).getDocumentHeaderById(item.getRequisition().getDocumentNumber()));
                }
                workflowDocument = workflowDocumentService.loadWorkflowDocument(item.getRequisition().getDocumentNumber(), principalPerson);
                //item.setApplicationDocumentStatus(workflowDocument.getApplicationDocumentStatus());
                if ((this.requisitionStatus != null)
                        && !workflowDocument.getApplicationDocumentStatus().equalsIgnoreCase(this.requisitionStatus)) {
                    isValid = false;
                }
                if (workflowDocument.isCanceled() ||
                        workflowDocument.isFinal()) {
                    isValid = false;
                }
                if (isValid && isSelectorLookup) {
                    if (selectorLookup(item, this.selectorUserId)) {
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                }

                if (isValid && this.requestorName != null && !StringUtils.isEmpty(this.requestorName)) {
                    if (this.internalRequestorId != null) {
                        if (this.internalRequestorId.equals(item.getInternalRequestorId())) {
                            isValid = true;
                        } else {
                            isValid = false;
                        }
                    } else if (this.externalRequestorId != null) {
                        if (this.externalRequestorId.equals(item.getRequestorId())) {
                            isValid = true;
                        } else {
                            isValid = false;
                        }
                    } else {
                        isValid = false;
                    }

                }

                if (isValid && isDateLookup) {
                    if (workflowStatusChangeDate.get(item.getRequisition().getDocumentNumber()) != null) {
                        item.setDateModified(convertTimestampToString(workflowStatusChangeDate.get(item.getRequisition().getDocumentNumber())));
                        isValid = true;
                    } else {
                        isValid = false;
                    }
                } else {
                    populateWorkflowStatusDate(item);
                }
                if (isValid) {
                    // jira OLE-2363 validation for order holding queue.
                    List<PurApAccountingLine> lines = item.getSourceAccountingLines();
                    if (lines.size() > 0) {
                        for (int i = 0; i < lines.size(); i++) {
                            OleRequisitionItem tempItem = new OleRequisitionItem();
                            tempItem = (OleRequisitionItem) ObjectUtils.deepCopy(item);
                            List<PurApAccountingLine> line = new ArrayList<PurApAccountingLine>();
                            line.add(lines.get(i));
                            tempItem.setSourceAccountingLines(null);
                            tempItem.setSourceAccountingLines(line);
                            finalResult.add(tempItem);
                        }
                    } else {
                        finalResult.add(item);
                    }

                }
            } catch (WorkflowException ex) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, ex.getMessage());
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Search Result size from OleOrderQueueDocument.filterOtherSearchCriteria after filtering - " + finalResult.size());
            LOG.debug("Leaving filterOtherSearchCriteria of OleOrderQueueDocument");
        }
        return finalResult;
    }

    protected Map<String, List<String>> preprocessDateFields() {
        LOG.debug("Inside preprocessDateFields of OleOrderQueueDocument");
        Map<String, List<String>> fieldsToUpdate = new HashMap<String, List<String>>();

        if (ObjectUtils.isNotNull(this.workflowStatusChangeDateFrom)) {
            fieldsToUpdate.put(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_FROM_DATE_LAST_MODIFIED, Collections.singletonList(convertTimestampToString(this.workflowStatusChangeDateFrom)));
        } else {
            fieldsToUpdate.put(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_FROM_DATE_LAST_MODIFIED, Collections.singletonList(""));
        }
        if (ObjectUtils.isNotNull(this.workflowStatusChangeDateTo)) {
            fieldsToUpdate.put(OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_TO_DATE_LAST_MODIFIED, Collections.singletonList(convertTimestampToString(this.workflowStatusChangeDateTo)));
        }
        if (isInitiatorOfREQS()) {
            fieldsToUpdate.put(OleSelectConstant.AcquisitionsSearch.INITIATOR, Collections.singletonList(GlobalVariables.getUserSession().getPrincipalName()));
        }
        LOG.debug("Leaving preprocessDateFields of OleOrderQueueDocument");
        return fieldsToUpdate;
    }

    /**
     * This method returns the true if the logined User has selector or super_selector role
     *
     * @return boolean
     */
    public boolean isInitiatorOfREQS() {
        boolean hasPermission = true;
        RoleService roleService = SpringContext.getBean(RoleService.class);
        List roleIds = new ArrayList<String>();
        String nameSpaceCode = OLEConstants.OleRequisition.REQUISITION_NAMESPACE;
        hasPermission = SpringContext.getBean(IdentityManagementService.class).hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.OleRequisitionItem.ORDER_HOLD_QUEUE_ASSIGN_OWN_REQ);
        if (hasPermission) {
            String roleId = roleService.getRoleIdByNamespaceCodeAndName(nameSpaceCode, OLEConstants.OrderQueue.OLE_SUPER_SELECTOR);
            roleIds.add(roleId);
            if (roleService.principalHasRole(GlobalVariables.getUserSession().getPerson().getPrincipalId(), roleIds, null)) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }

    /**
     * This method returns true if the selector is in the document's route log.
     *
     * @param item     Document containing this item will be validated for the selector.
     * @param selector Principal Id to be checked against the requisition document.
     * @return If the requisition item is valid to be returned with the search result.
     */
    public boolean selectorLookup(OleRequisitionItem item, String selector) {
        LOG.debug("Inside selectorLookup of OleRequisitionItemLookupableHelperServiceImpl");
        boolean isValid = false;
        WorkflowDocumentService workflowDocumentService = KRADServiceLocatorWeb.getWorkflowDocumentService();
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        WorkflowDocument workflowDocument;
        try {
            workflowDocument = workflowDocumentService.createWorkflowDocument(item.getRequisition().getDocumentHeader().getWorkflowDocument().getDocumentTypeName(), principalPerson);
            // Modified for OLE - 2541 starts
            Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipal(selector);
            List actionRequests = KEWServiceLocator.getActionRequestService().findAllActionRequestsByDocumentId(
                    item.getRequisition().getDocumentNumber());
            if (actionRequestListHasPrincipal(principal, actionRequests)) {
                isValid = true;
            }
            // Modified for OLE - 2541 ends
            // isValid = SpringContext.getBean(WorkflowDocumentService.class).isUserAuthenticatedByRouteLog(workflowDocument.getRouteHeaderId(), selector, true);
        } catch (WorkflowException wfe) {
            LOG.error("Workflow exception in selectorLookup :" + wfe.getMessage());
        }
        LOG.debug("Leaving selectorLookup of OleRequisitionItemLookupableHelperServiceImpl");
        return isValid;
    }

    // Modified for OLE - 2541
    private boolean actionRequestListHasPrincipal(Principal principal, List actionRequests) throws WorkflowException {
        for (Iterator iter = actionRequests.iterator(); iter.hasNext(); ) {
            ActionRequestValue actionRequest = (ActionRequestValue) iter.next();
            if (actionRequest.isRecipientRoutedRequest(new KimPrincipalRecipient(principal))) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method filters search results from OleRequisitionItem search based on workflow status change from/to date.
     *
     * @param searchResults   Search Result list from Requisition Item Lookup.
     * @param fixedParameters Map containing workflow status change From/To date search criteria.
     * @return Map containing document number and workflow status modified date for searchResults
     *         that passed the search criteria.
     */
    public Map<String, Timestamp> filterWorkflowStatusDate(List searchResults, Map<String, List<String>> fixedParameters) {
        DocumentSearchCriteria.Builder criteria = DocumentSearchCriteria.Builder.create();
        criteria.setDocumentAttributeValues(fixedParameters);
        DateTime dateTimeFrom = new DateTime(workflowStatusChangeDateFrom);
        DateTime dateTimeTo = new DateTime(workflowStatusChangeDateTo);
        criteria.setDateApplicationDocumentStatusChangedTo(dateTimeTo);
        if (workflowStatusChangeDateFrom == null && workflowStatusChangeDateTo != null) {
            criteria.setDateApplicationDocumentStatusChangedFrom(null);
        } else {
            criteria.setDateApplicationDocumentStatusChangedFrom(dateTimeFrom);
        }
        HashMap<String, Timestamp> map = new HashMap<String, Timestamp>();
        try {
            DocumentSearchResults components = KewApiServiceLocator.getWorkflowDocumentService().documentSearch(GlobalVariables.getUserSession().getPrincipalId(), criteria.build());
            List<DocumentSearchResult> docSearchResults = components.getSearchResults();
            for (DocumentSearchResult docSearchResult : docSearchResults) {
                map.put(docSearchResult.getDocument().getDocumentId(), new Timestamp(docSearchResult.getDocument()
                        .getApplicationDocumentStatusDate().getMillis()));
            }
        } catch (WorkflowServiceErrorException wsee) {
            for (WorkflowServiceError workflowServiceError : (List<WorkflowServiceError>) wsee.getServiceErrors()) {
                if (workflowServiceError.getMessageMap() != null && workflowServiceError.getMessageMap().hasErrors()) {
                    // merge the message maps
                    GlobalVariables.getMessageMap().merge(workflowServiceError.getMessageMap());
                } else {
                    //TODO: can we add something to this to get it to highlight the right field too?  Maybe in arg1
                    GlobalVariables.getMessageMap().putError(workflowServiceError.getMessage(), RiceKeyConstants.ERROR_CUSTOM, workflowServiceError.getMessage());
                }
            }
            ;
        } catch (Exception ex) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, ex.getMessage());
            LOG.error("Error filtering search results", ex);
        }
        return map;
    }

    /**
     * This method populates workflow status change date for a requisition item.
     *
     * @param item Requisition Item to populate workflow status date.
     * @return
     */
    public void populateWorkflowStatusDate(OleRequisitionItem item) {
        LOG.debug("Inside populateWorkflowStatusDate of OleRequisitionItemLookupableHelperServiceImpl");
        Long documentNumber = Long.valueOf(item.getRequisition().getDocumentNumber());
        WorkflowDocumentService workflowDocumentService = SpringContext.getBean(WorkflowDocumentService.class);
        Person principalPerson = SpringContext.getBean(PersonService.class).getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
        WorkflowDocument workflowDocument;
        try {
            workflowDocument = workflowDocumentService.loadWorkflowDocument(item.getRequisition().getDocumentNumber(), principalPerson);
            item.setDateModified(convertTimestampToString(new Timestamp(workflowDocument
                    .getApplicationDocumentStatusDate().getMillis())));
        } catch (WorkflowException ex) {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, ex.getMessage());
        }
        LOG.debug("Leaving populateWorkflowStatusDate of OleRequisitionItemLookupableHelperServiceImpl");
    }

    /**
     * This method converts specified timestamp to String
     *
     * @param dateTime
     * @return
     */
    public String convertTimestampToString(Timestamp timestamp) {
        LOG.debug("Inside convertTimestampToString of OleRequisitionItemLookupableHelperServiceImpl");
        String dateString = null;
        if (ObjectUtils.isNotNull(timestamp)) {
            try {
                DateTimeService dateTimeService = SpringContext.getBean(DateTimeService.class);
                java.sql.Date date = dateTimeService.convertToSqlDate(timestamp);
                dateString = dateTimeService.toDateString(date);
            } catch (ParseException exception) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, RiceKeyConstants.ERROR_CUSTOM, exception.getMessage());
            }
        }
        LOG.debug("Leaving convertTimestampToString of OleRequisitionItemLookupableHelperServiceImpl");
        return dateString;
    }
   /* /**
     * Gets the selectorRoleName attribute.
     * @return Returns the selectorRoleName.
     *//*
    public String getSelectorRoleName() {
        return selectorRoleName;
    }
    /**
     * Sets the selectorRoleName attribute value.
     * @param selectorRoleName The selectorRoleName to set.
     *//*
    public void setSelectorRoleName(String selectorRoleName) {
        this.selectorRoleName = selectorRoleName;
    }*/

}

