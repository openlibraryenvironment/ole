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
package org.kuali.rice.kew.routeheader;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.CustomActionListAttribute;
import org.kuali.rice.kew.actionlist.DefaultCustomActionListAttribute;
import org.kuali.rice.kew.actionrequest.ActionRequestFactory;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContract;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentUpdate;
import org.kuali.rice.kew.api.exception.InvalidActionTakenException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.docsearch.DocumentSearchCriteriaEbo;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatus;
import org.kuali.rice.kew.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.CompatUtils;
import org.kuali.rice.kew.engine.node.Branch;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.mail.CustomEmailAttribute;
import org.kuali.rice.kew.mail.CustomEmailAttributeImpl;
import org.kuali.rice.kew.notes.CustomNoteAttribute;
import org.kuali.rice.kew.notes.CustomNoteAttributeImpl;
import org.kuali.rice.kew.notes.Note;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * A document within KEW.  A document effectively represents a process that moves through
 * the workflow engine.  It is created from a particular {@link DocumentType} and follows
 * the route path defined by that DocumentType.
 *
 * <p>During a document's lifecycle it progresses through a series of statuses, starting
 * with INITIATED and moving to one of the terminal states (such as FINAL, CANCELED, etc).
 * The list of status on a document are defined in the {@link KewApiConstants} class and
 * include the constants starting with "ROUTE_HEADER_" and ending with "_CD".
 *
 * <p>Associated with the document is the document content.  The document content is XML
 * which represents the content of that document.  This XML content is typically used
 * to make routing decisions for the document.
 *
 * <p>A document has associated with it a set of {@link ActionRequestValue} object and
 * {@link ActionTakenValue} objects.  Action Requests represent requests for user
 * action (such as Approve, Acknowledge, etc).  Action Takens represent action that
 * users have performed on the document, such as approvals or cancelling of the document.
 *
 * <p>The instantiated route path of a document is defined by it's graph of
 * {@link RouteNodeInstance} objects.  The path starts at the initial node of the document
 * and progresses from there following the next nodes of each node instance.  The current
 * active nodes on the document are defined by the "active" flag on the node instance
 * where are not marked as "complete".
 *
 * @see DocumentType
 * @see ActionRequestValue
 * @see ActionItem
 * @see ActionTakenValue
 * @see RouteNodeInstance
 * @see KewApiConstants
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_DOC_HDR_T")
//@Sequence(name="KREW_DOC_HDR_S", property="documentId")
@NamedQueries({
    @NamedQuery(name="DocumentRouteHeaderValue.FindByDocumentId", query="select d from DocumentRouteHeaderValue as d where d.documentId = :documentId"),
    @NamedQuery(name="DocumentRouteHeaderValue.QuickLinks.FindWatchedDocumentsByInitiatorWorkflowId", query="SELECT NEW org.kuali.rice.kew.quicklinks.WatchedDocument(documentId, docRouteStatus, docTitle) FROM DocumentRouteHeaderValue WHERE initiatorWorkflowId = :initiatorWorkflowId AND docRouteStatus IN ('"+ KewApiConstants.ROUTE_HEADER_ENROUTE_CD +"','"+ KewApiConstants.ROUTE_HEADER_EXCEPTION_CD +"') ORDER BY createDate DESC"),
    @NamedQuery(name="DocumentRouteHeaderValue.GetAppDocId", query="SELECT d.appDocId from DocumentRouteHeaderValue as d where d.documentId = :documentId"),
    @NamedQuery(name="DocumentRouteHeaderValue.GetAppDocStatus", query="SELECT d.appDocStatus from DocumentRouteHeaderValue as d where d.documentId = :documentId")
})
public class DocumentRouteHeaderValue extends PersistableBusinessObjectBase implements DocumentContract, DocumentSearchCriteriaEbo {
    private static final long serialVersionUID = -4700736340527913220L;
    private static final Logger LOG = Logger.getLogger(DocumentRouteHeaderValue.class);

    public static final String CURRENT_ROUTE_NODE_NAME_DELIMITER = ", ";

    @Column(name="DOC_TYP_ID")
    private String documentTypeId;
    @Column(name="DOC_HDR_STAT_CD")
    private java.lang.String docRouteStatus;
    @Column(name="RTE_LVL")
    private java.lang.Integer docRouteLevel;
    @Column(name="STAT_MDFN_DT")
    private java.sql.Timestamp dateModified;
    @Column(name="CRTE_DT")
    private java.sql.Timestamp createDate;
    @Column(name="APRV_DT")
    private java.sql.Timestamp approvedDate;
    @Column(name="FNL_DT")
    private java.sql.Timestamp finalizedDate;
    @Transient
    private DocumentRouteHeaderValueContent documentContent;
    @Column(name="TTL")
    private java.lang.String docTitle;
    @Column(name="APP_DOC_ID")
    private java.lang.String appDocId;
    @Column(name="DOC_VER_NBR")
    private java.lang.Integer docVersion = new Integer(KewApiConstants.DocumentContentVersions.NODAL);
    @Column(name="INITR_PRNCPL_ID")
    private java.lang.String initiatorWorkflowId;
    @Column(name="RTE_PRNCPL_ID")
    private java.lang.String routedByUserWorkflowId;
    @Column(name="RTE_STAT_MDFN_DT")
    private java.sql.Timestamp routeStatusDate;
    @Column(name="APP_DOC_STAT")
    private java.lang.String appDocStatus;
    @Column(name="APP_DOC_STAT_MDFN_DT")
    private java.sql.Timestamp appDocStatusDate;

    @Id
    @GeneratedValue(generator="KREW_DOC_HDR_S")
    @GenericGenerator(name="KREW_DOC_HDR_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
            @Parameter(name="sequence_name",value="KREW_DOC_HDR_S"),
            @Parameter(name="value_column",value="id")
    })
    @Column(name="DOC_HDR_ID")
    private java.lang.String documentId;

    //@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE, mappedBy="routeHeader")
    //@Fetch(value = FetchMode.SELECT)
    //private List<ActionRequestValue> actionRequests = new ArrayList<ActionRequestValue>();

    //@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE, mappedBy="routeHeader")
    //@OrderBy("actionDate ASC")
    //@Fetch(value = FetchMode.SELECT)
    //private List<ActionTakenValue> actionsTaken = new ArrayList<ActionTakenValue>();

    //@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE, mappedBy="routeHeader")
    //@Fetch(value = FetchMode.SELECT)
    //private List<ActionItem> actionItems = new ArrayList<ActionItem>();

    /**
     * The appDocStatusHistory keeps a list of Application Document Status transitions
     * for the document.  It tracks the previous status, the new status, and a timestamp of the 
     * transition for each status transition.
     */
    @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy="documentId")
    //@JoinColumn(referencedColumnName="DOC_HDR_ID")
    @OrderBy("statusTransitionId ASC")
    @Fetch(value = FetchMode.SELECT)
    private List<DocumentStatusTransition> appDocStatusHistory = new ArrayList<DocumentStatusTransition>();

    @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name="DOC_HDR_ID")
    @OrderBy("noteId ASC")
    private List<Note> notes = new ArrayList<Note>();

    @Transient
    private List<SearchableAttributeValue> searchableAttributeValues = new ArrayList<SearchableAttributeValue>();
    @Transient
    private Collection queueItems = new ArrayList();
    @Transient
    private boolean routingReport = false;
    @Transient
    private List<ActionRequestValue> simulatedActionRequests;

    private static final boolean FINAL_STATE = true;
    protected static final HashMap<String,String> legalActions;
    protected static final HashMap<String,String> stateTransitionMap;

    /* New Workflow 2.1 Field */
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
    @JoinTable(name = "KREW_INIT_RTE_NODE_INSTN_T", joinColumns = @JoinColumn(name = "DOC_HDR_ID"), inverseJoinColumns = @JoinColumn(name = "RTE_NODE_INSTN_ID")) 
    @Fetch(value = FetchMode.SELECT)
    private List<RouteNodeInstance> initialRouteNodeInstances = new ArrayList<RouteNodeInstance>();

    // an empty list of target document statuses or legal actions
    private static final String TERMINAL = "";

    static {
        stateTransitionMap = new HashMap<String,String>();
        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_INITIATED_CD, KewApiConstants.ROUTE_HEADER_SAVED_CD + KewApiConstants.ROUTE_HEADER_ENROUTE_CD + KewApiConstants.ROUTE_HEADER_CANCEL_CD);

        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_SAVED_CD, KewApiConstants.ROUTE_HEADER_SAVED_CD + KewApiConstants.ROUTE_HEADER_ENROUTE_CD + KewApiConstants.ROUTE_HEADER_CANCEL_CD + KewApiConstants.ROUTE_HEADER_PROCESSED_CD);

        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD +
                KewApiConstants.ROUTE_HEADER_CANCEL_CD + KewApiConstants.ROUTE_HEADER_PROCESSED_CD + KewApiConstants.ROUTE_HEADER_EXCEPTION_CD + KewApiConstants.ROUTE_HEADER_SAVED_CD
                + DocumentStatus.RECALLED.getCode());
        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD, TERMINAL);
        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_CANCEL_CD, TERMINAL);
        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_FINAL_CD, TERMINAL);
        stateTransitionMap.put(DocumentStatus.RECALLED.getCode(), TERMINAL);
        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_EXCEPTION_CD, KewApiConstants.ROUTE_HEADER_EXCEPTION_CD + KewApiConstants.ROUTE_HEADER_ENROUTE_CD + KewApiConstants.ROUTE_HEADER_CANCEL_CD + KewApiConstants.ROUTE_HEADER_PROCESSED_CD + KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD + KewApiConstants.ROUTE_HEADER_SAVED_CD);
        stateTransitionMap.put(KewApiConstants.ROUTE_HEADER_PROCESSED_CD, KewApiConstants.ROUTE_HEADER_FINAL_CD + KewApiConstants.ROUTE_HEADER_PROCESSED_CD);

        legalActions = new HashMap<String,String>();
        legalActions.put(KewApiConstants.ROUTE_HEADER_INITIATED_CD, KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_SAVED_CD + KewApiConstants.ACTION_TAKEN_COMPLETED_CD + KewApiConstants.ACTION_TAKEN_ROUTED_CD + KewApiConstants.ACTION_TAKEN_CANCELED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD + KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD + KewApiConstants.ACTION_TAKEN_MOVE_CD);
        legalActions.put(KewApiConstants.ROUTE_HEADER_SAVED_CD, KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_SAVED_CD + KewApiConstants.ACTION_TAKEN_COMPLETED_CD + KewApiConstants.ACTION_TAKEN_ROUTED_CD + KewApiConstants.ACTION_TAKEN_APPROVED_CD + KewApiConstants.ACTION_TAKEN_CANCELED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD + KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD + KewApiConstants.ACTION_TAKEN_MOVE_CD);
        /* ACTION_TAKEN_ROUTED_CD not included in enroute state
         * ACTION_TAKEN_SAVED_CD removed as of version 2.4
         */
        legalActions.put(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, /*KewApiConstants.ACTION_TAKEN_SAVED_CD + KewApiConstants.ACTION_TAKEN_ROUTED_CD + */KewApiConstants.ACTION_TAKEN_APPROVED_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ADHOC_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD + KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD + KewApiConstants.ACTION_TAKEN_CANCELED_CD + KewApiConstants.ACTION_TAKEN_COMPLETED_CD + KewApiConstants.ACTION_TAKEN_DENIED_CD + KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD + KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD + KewApiConstants.ACTION_TAKEN_SU_DISAPPROVED_CD + KewApiConstants.ACTION_TAKEN_SU_ROUTE_LEVEL_APPROVED_CD + KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD + KewApiConstants.ACTION_TAKEN_SU_RETURNED_TO_PREVIOUS_CD + KewApiConstants.ACTION_TAKEN_MOVE_CD + ActionType.RECALL.getCode());
        /* ACTION_TAKEN_ROUTED_CD not included in exception state
         * ACTION_TAKEN_SAVED_CD removed as of version 2.4.2
         */
        legalActions.put(KewApiConstants.ROUTE_HEADER_EXCEPTION_CD, /*KewApiConstants.ACTION_TAKEN_SAVED_CD + */KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD + KewApiConstants.ACTION_TAKEN_APPROVED_CD + KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD + KewApiConstants.ACTION_TAKEN_CANCELED_CD + KewApiConstants.ACTION_TAKEN_COMPLETED_CD + KewApiConstants.ACTION_TAKEN_DENIED_CD + KewApiConstants.ACTION_TAKEN_SU_APPROVED_CD + KewApiConstants.ACTION_TAKEN_SU_CANCELED_CD + KewApiConstants.ACTION_TAKEN_SU_DISAPPROVED_CD + KewApiConstants.ACTION_TAKEN_SU_ROUTE_LEVEL_APPROVED_CD + KewApiConstants.ACTION_TAKEN_RETURNED_TO_PREVIOUS_CD + KewApiConstants.ACTION_TAKEN_SU_RETURNED_TO_PREVIOUS_CD + KewApiConstants.ACTION_TAKEN_MOVE_CD);
        legalActions.put(KewApiConstants.ROUTE_HEADER_FINAL_CD, KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD);
        legalActions.put(KewApiConstants.ROUTE_HEADER_CANCEL_CD, KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD);
        legalActions.put(KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD, KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD);
        legalActions.put(KewApiConstants.ROUTE_HEADER_PROCESSED_CD, KewApiConstants.ACTION_TAKEN_FYI_CD + KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD + KewApiConstants.ACTION_TAKEN_ADHOC_REVOKED_CD);
        legalActions.put(DocumentStatus.RECALLED.getCode(), TERMINAL);
    }

    public DocumentRouteHeaderValue() {
    }

    public Principal getInitiatorPrincipal() {
        // if we are running a simulation, there will be no initiator
        if (getInitiatorWorkflowId() == null) {
            return null;
        }
        return KEWServiceLocator.getIdentityHelperService().getPrincipal(getInitiatorWorkflowId());
    }

    public Principal getRoutedByPrincipal()
    {
        if (getRoutedByUserWorkflowId() == null) {
            return null;
        }
        return KEWServiceLocator.getIdentityHelperService().getPrincipal(getRoutedByUserWorkflowId());
    }

    public String getInitiatorDisplayName() {
        return KEWServiceLocator.getIdentityHelperService().getPerson(getInitiatorWorkflowId()).getName();
    }

    public String getRoutedByDisplayName() {
        return KEWServiceLocator.getIdentityHelperService().getPerson(getRoutedByUserWorkflowId()).getName();
    }

    public String getCurrentRouteLevelName() {
        String name = "Not Found";
        // TODO the isRouteLevelDocument junk can be ripped out
        if(routingReport){
            name = "Routing Report";
        } else if (CompatUtils.isRouteLevelDocument(this)) {
            int routeLevelInt = getDocRouteLevel().intValue();
            LOG.info("Getting current route level name for a Route level document: " + routeLevelInt+CURRENT_ROUTE_NODE_NAME_DELIMITER+documentId);
            List routeLevelNodes = CompatUtils.getRouteLevelCompatibleNodeList(getDocumentType());
            LOG.info("Route level compatible node list has " + routeLevelNodes.size() + " nodes");
            if (routeLevelInt < routeLevelNodes.size()) {
                name = ((RouteNode)routeLevelNodes.get(routeLevelInt)).getRouteNodeName();
            }
        } else {
        	List<String> currentNodeNames = getCurrentNodeNames();
        	name = StringUtils.join(currentNodeNames, CURRENT_ROUTE_NODE_NAME_DELIMITER);
        }
        return name;
    }

    public List<String> getCurrentNodeNames() {
        return KEWServiceLocator.getRouteNodeService().getCurrentRouteNodeNames(getDocumentId());
    }

    public String getRouteStatusLabel() {
        return CodeTranslator.getRouteStatusLabel(getDocRouteStatus());
    }

    public String getDocRouteStatusLabel() {
        return CodeTranslator.getRouteStatusLabel(getDocRouteStatus());
    }
    /**
     * 
     * This method returns the Document Status Policy for the document type associated with this Route Header.
     * The Document Status Policy denotes whether the KEW Route Status, or the Application Document Status,
     * or both are to be displayed.
     * 
     * @return
     */
    public String getDocStatusPolicy() {
        return getDocumentType().getDocumentStatusPolicy().getPolicyStringValue();
    }

    public Collection getQueueItems() {
        return queueItems;
    }

    public void setQueueItems(Collection queueItems) {
        this.queueItems = queueItems;
    }

    public List<ActionItem> getActionItems() {
        return (List<ActionItem>) KEWServiceLocator.getActionListService().findByDocumentId(documentId);
    }

    public List<ActionTakenValue> getActionsTaken() {
       return (List<ActionTakenValue>) KEWServiceLocator.getActionTakenService().findByDocumentIdIgnoreCurrentInd(documentId);
    }

    public List<ActionRequestValue> getActionRequests() {
        if (this.simulatedActionRequests == null || this.simulatedActionRequests.isEmpty()) {
            return KEWServiceLocator.getActionRequestService().findByDocumentIdIgnoreCurrentInd(documentId);
        } else {
            return this.simulatedActionRequests;
        }
    }

    public List<ActionRequestValue> getSimulatedActionRequests() {
        if (this.simulatedActionRequests == null) {
            this.simulatedActionRequests = new ArrayList<ActionRequestValue>();
        }
        return this.simulatedActionRequests;
    }

    public void setSimulatedActionRequests(List<ActionRequestValue> simulatedActionRequests) {
        this.simulatedActionRequests = simulatedActionRequests;
    }

    public DocumentType getDocumentType() {
        return KEWServiceLocator.getDocumentTypeService().findById(getDocumentTypeId());
    }

    public java.lang.String getAppDocId() {
        return appDocId;
    }

    public void setAppDocId(java.lang.String appDocId) {
        this.appDocId = appDocId;
    }

    public java.sql.Timestamp getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(java.sql.Timestamp approvedDate) {
        this.approvedDate = approvedDate;
    }

    public java.sql.Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.sql.Timestamp createDate) {
        this.createDate = createDate;
    }

    public java.lang.String getDocContent() {
        return getDocumentContent().getDocumentContent();
    }

    public void setDocContent(java.lang.String docContent) {
        DocumentRouteHeaderValueContent content = getDocumentContent();
        content.setDocumentContent(docContent);
    }

    public java.lang.Integer getDocRouteLevel() {
        return docRouteLevel;
    }

    public void setDocRouteLevel(java.lang.Integer docRouteLevel) {
        this.docRouteLevel = docRouteLevel;
    }

    public java.lang.String getDocRouteStatus() {
        return docRouteStatus;
    }

    public void setDocRouteStatus(java.lang.String docRouteStatus) {
        this.docRouteStatus = docRouteStatus;
    }

    public java.lang.String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(java.lang.String docTitle) {
        this.docTitle = docTitle;
    }

    @Override
    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public java.lang.Integer getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(java.lang.Integer docVersion) {
        this.docVersion = docVersion;
    }

    public java.sql.Timestamp getFinalizedDate() {
        return finalizedDate;
    }

    public void setFinalizedDate(java.sql.Timestamp finalizedDate) {
        this.finalizedDate = finalizedDate;
    }

    public java.lang.String getInitiatorWorkflowId() {
        return initiatorWorkflowId;
    }

    public void setInitiatorWorkflowId(java.lang.String initiatorWorkflowId) {
        this.initiatorWorkflowId = initiatorWorkflowId;
    }

    public java.lang.String getRoutedByUserWorkflowId() {
        if ( (isEnroute()) && (StringUtils.isBlank(routedByUserWorkflowId)) ) {
            return initiatorWorkflowId;
        }
        return routedByUserWorkflowId;
    }

    public void setRoutedByUserWorkflowId(java.lang.String routedByUserWorkflowId) {
        this.routedByUserWorkflowId = routedByUserWorkflowId;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(java.lang.String documentId) {
        this.documentId = documentId;
    }

    public java.sql.Timestamp getRouteStatusDate() {
        return routeStatusDate;
    }

    public void setRouteStatusDate(java.sql.Timestamp routeStatusDate) {
        this.routeStatusDate = routeStatusDate;
    }

    public java.sql.Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(java.sql.Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * 
     * This method returns the Application Document Status.
     * This status is an alternative to the Route Status that may be used for a document.
     * It is configurable per document type.
     * 
     * @see ApplicationDocumentStatus
     * @see DocumentTypePolicy
     * 
     * @return
     */
    public java.lang.String getAppDocStatus() {
        if (appDocStatus == null || "".equals(appDocStatus)){
            return KewApiConstants.UNKNOWN_STATUS;
        }
        return appDocStatus;
    }

    public void setAppDocStatus(java.lang.String appDocStatus){
        this.appDocStatus = appDocStatus;
    }

    /**
     * 
     * This method returns a combination of the route status label and the app doc status.
     * 
     * @return
     */
    public String getCombinedStatus(){
        String routeStatus = getRouteStatusLabel();
        String appStatus = getAppDocStatus();
        if (routeStatus != null && routeStatus.length()>0){
            if (appStatus.length() > 0){
                routeStatus += ", "+appStatus;
            }
        } else {
            return appStatus;
        }
        return routeStatus;
    }

    /**
     * 
     * This method sets the appDocStatus.
     * It firsts validates the new value against the defined acceptable values, if defined.
     * It also updates the AppDocStatus date, and saves the status transition information
     * 
     * @param appDocStatus
     * @throws WorkflowRuntimeException
     */
    public void updateAppDocStatus(java.lang.String appDocStatus) throws WorkflowRuntimeException{
        //validate against allowable values if defined
        if (appDocStatus != null && appDocStatus.length() > 0 && !appDocStatus.equalsIgnoreCase(this.appDocStatus)){
            DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findById(this.getDocumentTypeId());
            if (documentType.getValidApplicationStatuses() != null  && documentType.getValidApplicationStatuses().size() > 0){
                Iterator<ApplicationDocumentStatus> iter = documentType.getValidApplicationStatuses().iterator();
                boolean statusValidated = false;
                while (iter.hasNext())
                {
                    ApplicationDocumentStatus myAppDocStat = iter.next();
                    if (appDocStatus.compareToIgnoreCase(myAppDocStat.getStatusName()) == 0)
                    {
                        statusValidated = true;
                        break;
                    }
                }
                if (!statusValidated){
                    WorkflowRuntimeException xpee = new WorkflowRuntimeException("AppDocStatus value " +  appDocStatus + " not allowable.");
                    LOG.error("Error validating nextAppDocStatus name: " +  appDocStatus + " against acceptable values.", xpee);
                    throw xpee;
                }
            }

            // set the status value
            String oldStatus = this.appDocStatus;
            this.appDocStatus = appDocStatus;

            // update the timestamp
            setAppDocStatusDate(new Timestamp(System.currentTimeMillis()));

            // save the status transition
            this.appDocStatusHistory.add(new DocumentStatusTransition(documentId, oldStatus, appDocStatus));
        }

    }


    public java.sql.Timestamp getAppDocStatusDate() {
        return appDocStatusDate;
    }

    public void setAppDocStatusDate(java.sql.Timestamp appDocStatusDate) {
        this.appDocStatusDate = appDocStatusDate;
    }

    public Object copy(boolean preserveKeys) {
        throw new UnsupportedOperationException("The copy method is deprecated and unimplemented!");
    }

    /**
     * @return True if the document is in the state of Initiated
     */
    public boolean isStateInitiated() {
        return KewApiConstants.ROUTE_HEADER_INITIATED_CD.equals(docRouteStatus);
    }

    /**
     * @return True if the document is in the state of Saved
     */
    public boolean isStateSaved() {
        return KewApiConstants.ROUTE_HEADER_SAVED_CD.equals(docRouteStatus);
    }

    /**
     * @return true if the document has ever been inte enroute state
     */
    public boolean isRouted() {
        return !(isStateInitiated() || isStateSaved());
    }

    public boolean isInException() {
        return KewApiConstants.ROUTE_HEADER_EXCEPTION_CD.equals(docRouteStatus);
    }

    public boolean isDisaproved() {
        return KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD.equals(docRouteStatus);
    }

    public boolean isCanceled() {
        return KewApiConstants.ROUTE_HEADER_CANCEL_CD.equals(docRouteStatus);
    }

    public boolean isFinal() {
        return KewApiConstants.ROUTE_HEADER_FINAL_CD.equals(docRouteStatus);
    }

    public boolean isEnroute() {
        return KewApiConstants.ROUTE_HEADER_ENROUTE_CD.equals(docRouteStatus);
    }

    /**
     * @return true if the document is in the processed state
     */
    public boolean isProcessed() {
        return KewApiConstants.ROUTE_HEADER_PROCESSED_CD.equals(docRouteStatus);
    }

    public boolean isRoutable() {
        return KewApiConstants.ROUTE_HEADER_ENROUTE_CD.equals(docRouteStatus) ||
                //KewApiConstants.ROUTE_HEADER_EXCEPTION_CD.equals(docRouteStatus) ||
                KewApiConstants.ROUTE_HEADER_SAVED_CD.equals(docRouteStatus) ||
                KewApiConstants.ROUTE_HEADER_PROCESSED_CD.equals(docRouteStatus);
    }

    /**
     * Return true if the given action code is valid for this document's current state.
     * This method only verifies statically defined action/state transitions, it does not
     * perform full action validation logic.
     * @see org.kuali.rice.kew.actions.ActionRegistry#getValidActions(org.kuali.rice.kim.api.identity.principal.PrincipalContract, DocumentRouteHeaderValue)
     * @param actionCd The action code to be tested.
     * @return True if the action code is valid for the document's status.
     */
    public boolean isValidActionToTake(String actionCd) {
        String actions = (String) legalActions.get(docRouteStatus);
        return actions.contains(actionCd);
    }

    public boolean isValidStatusChange(String newStatus) {
        return ((String) stateTransitionMap.get(getDocRouteStatus())).contains(newStatus);
    }

    public void setRouteStatus(String newStatus, boolean finalState) throws InvalidActionTakenException {
        if (newStatus != getDocRouteStatus()) {
            // only modify the status mod date if the status actually changed
            setRouteStatusDate(new Timestamp(System.currentTimeMillis()));
        }
        if (((String) stateTransitionMap.get(getDocRouteStatus())).contains(newStatus)) {
            LOG.debug("changing status");
            setDocRouteStatus(newStatus);
        } else {
            LOG.debug("unable to change status");
            throw new InvalidActionTakenException("Document status " + CodeTranslator.getRouteStatusLabel(getDocRouteStatus()) + " cannot transition to status " + CodeTranslator
                    .getRouteStatusLabel(newStatus));
        }
        setDateModified(new Timestamp(System.currentTimeMillis()));
        if (finalState) {
            LOG.debug("setting final timeStamp");
            setFinalizedDate(new Timestamp(System.currentTimeMillis()));
        }
    }

    /**
     * Mark the document as being processed.
     *
     * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentProcessed() throws InvalidActionTakenException {
        LOG.debug(this + " marked processed");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_PROCESSED_CD, !FINAL_STATE);
    }

    /**
     * Mark document cancled.
     *
     * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentCanceled() throws InvalidActionTakenException {
        LOG.debug(this + " marked canceled");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_CANCEL_CD, FINAL_STATE);
    }
    
    /**
     * Mark document recalled.
     *
     * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentRecalled() throws InvalidActionTakenException {
        LOG.debug(this + " marked recalled");
        setRouteStatus(DocumentStatus.RECALLED.getCode(), FINAL_STATE);
    }
    
    /**
     * Mark document disapproved
     *
     * @throws ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentDisapproved() throws InvalidActionTakenException {
        LOG.debug(this + " marked disapproved");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD, FINAL_STATE);
    }

    /**
     * Mark document saved
     *
     * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentSaved() throws InvalidActionTakenException {
        LOG.debug(this + " marked saved");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_SAVED_CD, !FINAL_STATE);
    }

    /**
     * Mark the document as being in the exception state.
     *
     * @throws org.kuali.rice.kew.api.exception.ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentInException() throws InvalidActionTakenException {
        LOG.debug(this + " marked in exception");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_EXCEPTION_CD, !FINAL_STATE);
    }

    /**
     * Mark the document as being actively routed.
     *
     * @throws ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentEnroute() throws InvalidActionTakenException {
        LOG.debug(this + " marked enroute");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, !FINAL_STATE);
    }

    /**
     * Mark document finalized.
     *
     * @throws ResourceUnavailableException
     * @throws InvalidActionTakenException
     */
    public void markDocumentFinalized() throws InvalidActionTakenException {
        LOG.debug(this + " marked finalized");
        setRouteStatus(KewApiConstants.ROUTE_HEADER_FINAL_CD, FINAL_STATE);
    }

    /**
     * This method takes data from a VO and sets it on this route header
     * @param routeHeaderVO
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     */
    public void setRouteHeaderData(Document routeHeaderVO) throws WorkflowException {
        if (!ObjectUtils.equals(getDocTitle(), routeHeaderVO.getTitle())) {
            KEWServiceLocator.getActionListService().updateActionItemsForTitleChange(getDocumentId(), routeHeaderVO.getTitle());
        }
        setDocTitle(routeHeaderVO.getTitle());
        setAppDocId(routeHeaderVO.getApplicationDocumentId());
        setDateModified(new Timestamp(System.currentTimeMillis()));
        updateAppDocStatus(routeHeaderVO.getApplicationDocumentStatus());

        /* set the variables from the routeHeaderVO */
        for (Map.Entry<String, String> kvp : routeHeaderVO.getVariables().entrySet()) {
            setVariable(kvp.getKey(), kvp.getValue());
        }
    }

    public void applyDocumentUpdate(DocumentUpdate documentUpdate) {
        if (documentUpdate != null) {
            String thisDocTitle = getDocTitle() == null ? "" : getDocTitle();
            String updateDocTitle = documentUpdate.getTitle() == null ? "" : documentUpdate.getTitle();
            if (!StringUtils.equals(thisDocTitle, updateDocTitle)) {
                KEWServiceLocator.getActionListService().updateActionItemsForTitleChange(getDocumentId(), documentUpdate.getTitle());
            }
            setDocTitle(updateDocTitle);
            setAppDocId(documentUpdate.getApplicationDocumentId());
            setDateModified(new Timestamp(System.currentTimeMillis()));
            updateAppDocStatus(documentUpdate.getApplicationDocumentStatus());

            Map<String, String> variables = documentUpdate.getVariables();
            for (String variableName : variables.keySet()) {
                setVariable(variableName, variables.get(variableName));
            }
        }
    }

    /**
     * Convenience method that returns the branch of the first (and presumably only?) initial node
     * @return the branch of the first (and presumably only?) initial node
     */
    public Branch getRootBranch() {
        if (!this.initialRouteNodeInstances.isEmpty()) {
            return ((RouteNodeInstance) getInitialRouteNodeInstance(0)).getBranch();
        } 
        return null;
    }

    /**
     * Looks up a variable (embodied in a "BranchState" key/value pair) in the
     * branch state table.
     */
    private BranchState findVariable(String name) {
        Branch rootBranch = getRootBranch();
        if (rootBranch != null) {
            List<BranchState> branchState = rootBranch.getBranchState();
            Iterator<BranchState> it = branchState.iterator();
            while (it.hasNext()) {
                BranchState state = it.next();
                if (ObjectUtils.equals(state.getKey(), BranchState.VARIABLE_PREFIX + name)) {
                    return state;
                }
            }
        }
        return null;
    }

    /**
     * Gets a variable
     * @param name variable name
     * @return variable value, or null if variable is not defined
     */
    public String getVariable(String name) {
        BranchState state = findVariable(name);
        if (state == null) {
            LOG.debug("Variable not found: '" + name + "'");
            return null;
        }
        return state.getValue();
    }

    public void removeVariableThatContains(String name) {
    List<BranchState> statesToRemove = new ArrayList<BranchState>();
    for (BranchState state : this.getRootBranchState()) {
        if (state.getKey().contains(name)) {
        statesToRemove.add(state);
        }
    }
    this.getRootBranchState().removeAll(statesToRemove);
    }

    /**
     * Sets a variable
     * @param name variable name
     * @param value variable value, or null if variable should be removed
     */
    public void setVariable(String name, String value) {
        BranchState state = findVariable(name);
        Branch rootBranch = getRootBranch();
        if (rootBranch != null) {
            List<BranchState> branchState = rootBranch.getBranchState();
            if (state == null) {
                if (value == null) {
                    LOG.debug("set non existent variable '" + name + "' to null value");
                    return;
                }
                LOG.debug("Adding branch state: '" + name + "'='" + value + "'");
                state = new BranchState();
                state.setBranch(rootBranch);
                state.setKey(BranchState.VARIABLE_PREFIX + name);
                state.setValue(value);
                rootBranch.addBranchState(state);
            } else {
                if (value == null) {
                    LOG.debug("Removing value: " + state.getKey() + "=" + state.getValue());
                    branchState.remove(state);
                } else {
                    LOG.debug("Setting value of variable '" + name + "' to '" + value + "'");
                    state.setValue(value);
                }
            }
        }
    }

    public List<BranchState> getRootBranchState() {
        if (this.getRootBranch() != null) {
            return this.getRootBranch().getBranchState();
        }
        return null;
    }

    public CustomActionListAttribute getCustomActionListAttribute() throws WorkflowException {
        CustomActionListAttribute customActionListAttribute = null;
        if (this.getDocumentType() != null) {
            customActionListAttribute = this.getDocumentType().getCustomActionListAttribute();
            if (customActionListAttribute != null) {
                return customActionListAttribute;
            }
        }
        customActionListAttribute = new DefaultCustomActionListAttribute();
        return customActionListAttribute;
    }

    public CustomEmailAttribute getCustomEmailAttribute() throws WorkflowException {
        CustomEmailAttribute customEmailAttribute = null;
        try {
            if (this.getDocumentType() != null) {
                customEmailAttribute = this.getDocumentType().getCustomEmailAttribute();
                if (customEmailAttribute != null) {
                    customEmailAttribute.setRouteHeaderVO(DocumentRouteHeaderValue.to(this));
                    return customEmailAttribute;
                }
            }
        } catch (Exception e) {
            LOG.debug("Error in retrieving custom email attribute", e);
        }
        customEmailAttribute = new CustomEmailAttributeImpl();
        customEmailAttribute.setRouteHeaderVO(DocumentRouteHeaderValue.to(this));
        return customEmailAttribute;
    }

    public CustomNoteAttribute getCustomNoteAttribute() throws WorkflowException
    {
        CustomNoteAttribute customNoteAttribute = null;
        try {
            if (this.getDocumentType() != null) {
                customNoteAttribute = this.getDocumentType().getCustomNoteAttribute();
                if (customNoteAttribute != null) {
                    customNoteAttribute.setRouteHeaderVO(DocumentRouteHeaderValue.to(this));
                    return customNoteAttribute;
                }
            }
        } catch (Exception e) {
            LOG.debug("Error in retrieving custom note attribute", e);
        }
        customNoteAttribute = new CustomNoteAttributeImpl();
        customNoteAttribute.setRouteHeaderVO(DocumentRouteHeaderValue.to(this));
        return customNoteAttribute;
    }

    public ActionRequestValue getDocActionRequest(int index) {
        List<ActionRequestValue> actionRequests = getActionRequests();
        while (actionRequests.size() <= index) {
            ActionRequestValue actionRequest = new ActionRequestFactory(this).createBlankActionRequest();
            actionRequest.setNodeInstance(new RouteNodeInstance());
            actionRequests.add(actionRequest);
        }
        return (ActionRequestValue) actionRequests.get(index);
    }

    public ActionTakenValue getDocActionTaken(int index) {
        List<ActionTakenValue> actionsTaken = getActionsTaken();
        while (actionsTaken.size() <= index) {
            actionsTaken.add(new ActionTakenValue());
        }
        return (ActionTakenValue) actionsTaken.get(index);
    }

    public ActionItem getDocActionItem(int index) {
        List<ActionItem> actionItems = getActionItems();
        while (actionItems.size() <= index) {
            actionItems.add(new ActionItem());
        }
        return (ActionItem) actionItems.get(index);
    }

    private RouteNodeInstance getInitialRouteNodeInstance(int index) {
        if (initialRouteNodeInstances.size() >= index) {
            return (RouteNodeInstance) initialRouteNodeInstances.get(index);
        }
        return null;
    }

//	/**
//	 * @param searchableAttributeValues The searchableAttributeValues to set.
//	 */
//	public void setSearchableAttributeValues(List<SearchableAttributeValue> searchableAttributeValues) {
//		this.searchableAttributeValues = searchableAttributeValues;
//	}
//
//	/**
//	 * @return Returns the searchableAttributeValues.
//	 */
//	public List<SearchableAttributeValue> getSearchableAttributeValues() {
//		return searchableAttributeValues;
//	}

    public boolean isRoutingReport() {
        return routingReport;
    }

    public void setRoutingReport(boolean routingReport) {
        this.routingReport = routingReport;
    }

    public List<RouteNodeInstance> getInitialRouteNodeInstances() {
        return initialRouteNodeInstances;
    }

    public void setInitialRouteNodeInstances(List<RouteNodeInstance> initialRouteNodeInstances) {
        this.initialRouteNodeInstances = initialRouteNodeInstances;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public DocumentRouteHeaderValueContent getDocumentContent() {
        if (documentContent == null) {
            documentContent = KEWServiceLocator.getRouteHeaderService().getContent(getDocumentId());
        }
        return documentContent;
    }

    public void setDocumentContent(DocumentRouteHeaderValueContent documentContent) {
        this.documentContent = documentContent;
    }

    public List<DocumentStatusTransition> getAppDocStatusHistory() {
        return this.appDocStatusHistory;
    }

    public void setAppDocStatusHistory(
            List<DocumentStatusTransition> appDocStatusHistory) {
        this.appDocStatusHistory = appDocStatusHistory;
    }

    @Override
    public DocumentStatus getStatus() {
        return DocumentStatus.fromCode(getDocRouteStatus());
    }

    @Override
    public DateTime getDateCreated() {
        if (getCreateDate() == null) {
            return null;
        }
        return new DateTime(getCreateDate().getTime());
    }

    @Override
    public DateTime getDateLastModified() {
        if (getDateModified() == null) {
            return null;
        }
        return new DateTime(getDateModified().getTime());
    }

    @Override
    public DateTime getDateApproved() {
        if (getApprovedDate() == null) {
            return null;
        }
        return new DateTime(getApprovedDate().getTime());
    }

    @Override
    public DateTime getDateFinalized() {
        if (getFinalizedDate() == null) {
            return null;
        }
        return new DateTime(getFinalizedDate().getTime());
    }

    @Override
    public String getTitle() {
        return docTitle;
    }

    @Override
    public String getApplicationDocumentId() {
        return appDocId;
    }

    @Override
    public String getInitiatorPrincipalId() {
        return initiatorWorkflowId;
    }

    @Override
    public String getRoutedByPrincipalId() {
        return routedByUserWorkflowId;
    }

    @Override
    public String getDocumentTypeName() {
        return getDocumentType().getName();
    }

    @Override
    public String getDocumentHandlerUrl() {
        return getDocumentType().getResolvedDocumentHandlerUrl();
    }

    @Override
    public String getApplicationDocumentStatus() {
        return appDocStatus;
    }

    @Override
    public DateTime getApplicationDocumentStatusDate() {
        if (appDocStatusDate == null) {
            return null;
        }
        return new DateTime(appDocStatusDate.getTime());
    }

    @Override
    public Map<String, String> getVariables() {
        Map<String, String> documentVariables = new HashMap<String, String>();
        /* populate the routeHeaderVO with the document variables */
        // FIXME: we assume there is only one for now
        Branch routeNodeInstanceBranch = getRootBranch();
        // Ok, we are using the "branch state" as the arbitrary convenient repository for flow/process/edoc variables
        // so we need to stuff them into the VO
        if (routeNodeInstanceBranch != null) {
            List<BranchState> listOfBranchStates = routeNodeInstanceBranch.getBranchState();
            for (BranchState bs : listOfBranchStates) {
                if (bs.getKey() != null && bs.getKey().startsWith(BranchState.VARIABLE_PREFIX)) {
                    LOG.debug("Setting branch state variable on vo: " + bs.getKey() + "=" + bs.getValue());
                    documentVariables.put(bs.getKey().substring(BranchState.VARIABLE_PREFIX.length()), bs.getValue());
                }
            }
        }
        return documentVariables;
    }

    public static Document to(DocumentRouteHeaderValue documentBo) {
        if (documentBo == null) {
            return null;
        }
        Document.Builder builder = Document.Builder.create(documentBo);
        return builder.build();
    }

    public static DocumentRouteHeaderValue from(Document document) {
        DocumentRouteHeaderValue documentBo = new DocumentRouteHeaderValue();
        documentBo.setAppDocId(document.getApplicationDocumentId());
        if (document.getDateApproved() != null) {
            documentBo.setApprovedDate(new Timestamp(document.getDateApproved().getMillis()));
        }
        if (document.getDateCreated() != null) {
            documentBo.setCreateDate(new Timestamp(document.getDateCreated().getMillis()));
        }
        if (StringUtils.isEmpty(documentBo.getDocContent())) {
            documentBo.setDocContent(KewApiConstants.DEFAULT_DOCUMENT_CONTENT);
        }
        documentBo.setDocRouteStatus(document.getStatus().getCode());
        documentBo.setDocTitle(document.getTitle());
        if (document.getDocumentTypeName() != null) {
            DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(document.getDocumentTypeName());
            if (documentType == null) {
                throw new RiceRuntimeException("Could not locate the given document type name: " + document.getDocumentTypeName());
            }
            documentBo.setDocumentTypeId(documentType.getDocumentTypeId());
        }
        if (document.getDateFinalized() != null) {
            documentBo.setFinalizedDate(new Timestamp(document.getDateFinalized().getMillis()));
        }
        documentBo.setInitiatorWorkflowId(document.getInitiatorPrincipalId());
        documentBo.setRoutedByUserWorkflowId(document.getRoutedByPrincipalId());
        documentBo.setDocumentId(document.getDocumentId());
        if (document.getDateLastModified() != null) {
            documentBo.setDateModified(new Timestamp(document.getDateLastModified().getMillis()));
        }
        documentBo.setAppDocStatus(document.getApplicationDocumentStatus());
        if (document.getApplicationDocumentStatusDate() != null) {
            documentBo.setAppDocStatusDate(new Timestamp(document.getApplicationDocumentStatusDate().getMillis()));
        }


        // Convert the variables
        Map<String, String> variables = document.getVariables();
        if( variables != null && !variables.isEmpty()){
            for(Map.Entry<String, String> kvp : variables.entrySet()){
                documentBo.setVariable(kvp.getKey(), kvp.getValue());
            }
        }

        return documentBo;
    }

}
