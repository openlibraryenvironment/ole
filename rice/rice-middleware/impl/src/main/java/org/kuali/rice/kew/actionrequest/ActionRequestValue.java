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
package org.kuali.rice.kew.actionrequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.dto.DTOConverter.RouteNodeInstanceLoader;
import org.kuali.rice.kew.engine.CompatUtils;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.service.RuleServiceInternal;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.user.RoleRecipient;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * Bean mapped to DB. Represents ActionRequest to a workgroup, user or role.  Contains
 * references to children/parent if a member of a graph
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_ACTN_RQST_T")
//@Sequence(name="KREW_ACTN_RQST_S", property="actionRequestId")
@NamedQueries({
  @NamedQuery(name="ActionRequestValue.FindByDocumentId", query="select arv from ActionRequestValue arv where arv.documentId = :documentId"),
  @NamedQuery(name="ActionRequestValue.GetUserRequestCount", query="select count(arv) from ActionRequestValue arv where arv.documentId = :documentId and arv.recipientTypeCd = :recipientTypeCd and arv.principalId = :principalId and arv.currentIndicator = :currentIndicator"),
  @NamedQuery(name="ActionRequestValue.FindActivatedByGroup", query="select count(arv) from ActionRequestValue arv where arv.groupId = :groupId and arv.currentIndicator = :currentIndicator and arv.status = :status"),
  @NamedQuery(name="ActionRequestValue.FindAllByDocId", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator"),
  @NamedQuery(name="ActionRequestValue.FindAllPendingByDocId", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and (arv.status = :actionRequestStatus1 or arv.status = :actionRequestStatus2)"),
  @NamedQuery(name="ActionRequestValue.FindAllRootByDocId", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.parentActionRequest is null"),
  @NamedQuery(name="ActionRequestValue.FindByStatusAndDocId", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.status = :status"),
  @NamedQuery(name="ActionRequestValue.FindPendingByActionRequestedAndDocId", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.actionRequested = :actionRequested and (arv.status = :actionRequestStatus1 or arv.status = :actionRequestStatus2)"),
  @NamedQuery(name="ActionRequestValue.FindPendingByDocIdAtOrBelowRouteLevel", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.status <> :status and arv.routeLevel <= :routeLevel"),
  @NamedQuery(name="ActionRequestValue.FindPendingRootRequestsByDocIdAtOrBelowRouteLevel", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.parentActionRequest is null and arv.status <> :status and routeLevel <= :routeLevel"),
  @NamedQuery(name="ActionRequestValue.FindPendingRootRequestsByDocIdAtRouteLevel", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.parentActionRequest is null and arv.status <> :status and routeLevel = :routeLevel"),
  @NamedQuery(name="ActionRequestValue.FindPendingRootRequestsByDocIdAtRouteNode", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.parentActionRequest is null and arv.nodeInstance.routeNodeInstanceId = :routeNodeInstanceId and (arv.status = :actionRequestStatus1 or arv.status = :actionRequestStatus2)"),
  @NamedQuery(name="ActionRequestValue.FindPendingRootRequestsByDocumentType", query="select arv from ActionRequestValue arv where arv.documentId in (select drhv.documentId from DocumentRouteHeaderValue drhv where drhv.documentTypeId = :documentTypeId) and arv.currentIndicator = :currentIndicator and arv.parentActionRequest is null and (arv.status = :actionRequestStatus1 or arv.status = :actionRequestStatus2)"),
  @NamedQuery(name="ActionRequestValue.FindRootRequestsByDocIdAtRouteNode", query="select arv from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.parentActionRequest is null and arv.nodeInstance.routeNodeInstanceId = :routeNodeInstanceId"),
  @NamedQuery(name="ActionRequestValue.GetRequestGroupIds", query="select arv.groupId from ActionRequestValue arv where arv.documentId = :documentId and arv.currentIndicator = :currentIndicator and arv.recipientTypeCd = :recipientTypeCd"),
  @NamedQuery(name="ActionRequestValue.FindByStatusAndGroupId", query="select arv from ActionRequestValue arv where arv.groupId = :groupId and arv.currentIndicator = :currentIndicator and arv.status = :status")
})
public class ActionRequestValue implements Serializable {

	private static final long serialVersionUID = 8781414791855848385L;

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionRequestValue.class);

    private static final String ACTION_CODE_RANK = "FKACB";//B is a hack for allowing blanket approves to count for approve and complete requests in findPreviousAction in ActionTakenService this is a hack and accounts for the -3 on compareActionCode
    private static final String RECIPIENT_TYPE_RANK = "RWU";
    private static final List DELEGATION_TYPE_RANK = Arrays.asList(new Object[]{DelegationType.SECONDARY, DelegationType.PRIMARY, null});

    @Id
    @GeneratedValue(generator="KREW_ACTN_RQST_S")
	@GenericGenerator(name="KREW_ACTN_RQST_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_ACTN_RQST_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="ACTN_RQST_ID")
	private String actionRequestId;
    @Column(name="ACTN_RQST_CD")
	private String actionRequested;
    @Column(name="DOC_HDR_ID")
	private String documentId;
    @Column(name="STAT_CD")
	private String status;
    @Column(name="RSP_ID")
	private String responsibilityId;
    @Column(name="GRP_ID")
	private String groupId;
    @Column(name="RECIP_TYP_CD")
	private String recipientTypeCd;
    @Column(name="PRIO_NBR")
	private Integer priority;
    @Column(name="RTE_LVL_NBR")
	private Integer routeLevel;
    @Column(name="ACTN_TKN_ID", insertable=false, updatable=false)
	private String actionTakenId;
    @Column(name="DOC_VER_NBR")
    private Integer docVersion = 1;
	@Column(name="CRTE_DT")
	private java.sql.Timestamp createDate;
    @Column(name="RSP_DESC_TXT")
	private String responsibilityDesc;
    @Column(name="ACTN_RQST_ANNOTN_TXT")
	private String annotation;
    @Column(name="VER_NBR")
	private Integer jrfVerNbr;
    @Column(name="PRNCPL_ID")
	private String principalId;
    @Column(name="FRC_ACTN")
	private Boolean forceAction;
    @Column(name="PARNT_ID", insertable=false, updatable=false)
	private String parentActionRequestId;
    @Column(name="QUAL_ROLE_NM")
	private String qualifiedRoleName;
    @Column(name="ROLE_NM")
	private String roleName;
    @Column(name="QUAL_ROLE_NM_LBL_TXT")
	private String qualifiedRoleNameLabel;
    @Transient
    private String displayStatus;
    @Column(name="RULE_ID")
	private String ruleBaseValuesId;

    @Column(name="DLGN_TYP")
    private String delegationTypeCode;
    @Column(name="APPR_PLCY")
	private String approvePolicy;

    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="PARNT_ID")
	private ActionRequestValue parentActionRequest;
    @Fetch(value = FetchMode.SELECT)
    @OneToMany(mappedBy="parentActionRequest",cascade={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.EAGER)
    private List<ActionRequestValue> childrenRequests = new ArrayList<ActionRequestValue>();
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ACTN_TKN_ID")
	private ActionTakenValue actionTaken;
    //@OneToMany(fetch=FetchType.LAZY,mappedBy="actionRequestId")
    //private List<ActionItem> actionItems = new ArrayList<ActionItem>();
    @Column(name="CUR_IND")
    private Boolean currentIndicator = true;
    @Transient
    private String createDateString;

    /* New Workflow 2.1 Field */
    // The node instance at which this request was generated
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RTE_NODE_INSTN_ID")
	private RouteNodeInstance nodeInstance;

    @Column(name="RQST_LBL")
    private String requestLabel;
    
    @Transient
    private boolean resolveResponsibility = true;
    @Transient
    private DocumentRouteHeaderValue routeHeader;
    @Transient
    private List<ActionItem> simulatedActionItems;
    
    public ActionRequestValue() {
        createDate = new Timestamp(System.currentTimeMillis());
    }
    
    //@PrePersist
    public void beforeInsert(){
    	OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
    }
   
    public Group getGroup() {
        if (getGroupId() == null) {
            LOG.error("Attempting to get a group with a blank group id");
            return null;
        }
        return KimApiServiceLocator.getGroupService().getGroup(getGroupId());
    }

    public String getRouteLevelName() {
        // this is for backward compatibility of requests which have not been converted
        if (CompatUtils.isRouteLevelRequest(this)) {
            int routeLevelInt = getRouteLevel();
            if (routeLevelInt == KewApiConstants.EXCEPTION_ROUTE_LEVEL) {
                return "Exception";
            }

            List<RouteNode> routeLevelNodes = CompatUtils.getRouteLevelCompatibleNodeList(KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId).getDocumentType());
            if (!(routeLevelInt < routeLevelNodes.size())) {
                return "Not Found";
            }
            return ((RouteNode)routeLevelNodes.get(routeLevelInt)).getRouteNodeName();
        } else {
            return (nodeInstance == null ? "Exception" : nodeInstance.getName());
        }
    }

    public boolean isUserRequest() {
        return principalId != null;
    }

    public Principal getPrincipal() {
    	if (getPrincipalId() == null) {
    		return null;
    	}
    	return KEWServiceLocator.getIdentityHelperService().getPrincipal(getPrincipalId());
    }
    
    public Person getPerson() {
    	if (getPrincipalId() == null) {
    		return null;
    	}
    	return KimApiServiceLocator.getPersonService().getPerson(getPrincipalId());
    }

    public String getDisplayName() {
    	if (isUserRequest()) {
            Person person = getPerson();
            if ( person != null ) {
    	        return person.getName();
            }
    	} else if (isGroupRequest()) {
            Group group = getGroup();
            if ( group != null ) {
    		    return group.getName();
            } else {
                return getGroupId();
            }
    	} else if (isRoleRequest()) {
    		return getRoleName();
    	}
    	return "";
    }

    public Recipient getRecipient() {
        if (getPrincipalId() != null) {
            return new KimPrincipalRecipient(getPrincipal());
        } else if (getGroupId() != null){
            return new KimGroupRecipient(getGroup());
        } else {
        	return new RoleRecipient(this.getRoleName());
        }
    }

    public boolean isPending() {
        return ActionRequestStatus.INITIALIZED.getCode().equals(getStatus()) || ActionRequestStatus.ACTIVATED.getCode().equals(getStatus());
    }

    public String getStatusLabel() {
        return CodeTranslator.getActionRequestStatusLabel(getStatus());
    }

    public String getActionRequestedLabel() {
    	if (StringUtils.isNotBlank(getRequestLabel())) {
    		return getRequestLabel();
    	}
    	return CodeTranslator.getActionRequestLabel(getActionRequested());
    }

    /**
     * @return Returns the actionTaken.
     */
    public ActionTakenValue getActionTaken() {
        return actionTaken;
    }

    /**
     * @param actionTaken
     *            The actionTaken to set.
     */
    public void setActionTaken(ActionTakenValue actionTaken) {
        this.actionTaken = actionTaken;
    }

    /**
     * @return Returns the actionRequested.
     */
    public String getActionRequested() {
        return actionRequested;
    }

    /**
     * @param actionRequested
     *            The actionRequested to set.
     */
    public void setActionRequested(String actionRequested) {
        this.actionRequested = actionRequested;
    }

    /**
     * @return Returns the actionRequestId.
     */
    public String getActionRequestId() {
        return actionRequestId;
    }

    /**
     * @param actionRequestId
     *            The actionRequestId to set.
     */
    public void setActionRequestId(String actionRequestId) {
        this.actionRequestId = actionRequestId;
    }

    /**
     * @return Returns the actionTakenId.
     */
    public String getActionTakenId() {
        return actionTakenId;
    }

    /**
     * @param actionTakenId
     *            The actionTakenId to set.
     */
    public void setActionTakenId(String actionTakenId) {
        this.actionTakenId = actionTakenId;
    }

    /**
     * @return Returns the annotation.
     */
    public String getAnnotation() {
        return annotation;
    }

    /**
     * @param annotation
     *            The annotation to set.
     */
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    /**
     * @return Returns the createDate.
     */
    public java.sql.Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            The createDate to set.
     */
    public void setCreateDate(java.sql.Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * @return Returns the docVersion.
     */
    public Integer getDocVersion() {
        return docVersion;
    }

    /**
     * @param docVersion
     *            The docVersion to set.
     */
    public void setDocVersion(Integer docVersion) {
        this.docVersion = docVersion;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }
    
    /**
     * @return Returns the forceAction.
     */
    public Boolean getForceAction() {
        return forceAction;
    }

    /**
     * @param forceAction
     *            The forceAction to set.
     */
    public void setForceAction(Boolean forceAction) {
        this.forceAction = forceAction;
    }

    /**
     * @return Returns the jrfVerNbr.
     */
    public Integer getJrfVerNbr() {
        return jrfVerNbr;
    }

    /**
     * @param jrfVerNbr
     *            The jrfVerNbr to set.
     */
    public void setJrfVerNbr(Integer jrfVerNbr) {
        this.jrfVerNbr = jrfVerNbr;
    }

    /**
     * @return Returns the priority.
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * @param priority
     *            The priority to set.
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * @return Returns the recipientTypeCd.
     */
    public String getRecipientTypeCd() {
        return recipientTypeCd;
    }

    /**
     * @param recipientTypeCd
     *            The recipientTypeCd to set.
     */
    public void setRecipientTypeCd(String recipientTypeCd) {
        this.recipientTypeCd = recipientTypeCd;
    }

    /**
     * @return Returns the responsibilityDesc.
     */
    public String getResponsibilityDesc() {
        return responsibilityDesc;
    }

    /**
     * @param responsibilityDesc
     *            The responsibilityDesc to set.
     */
    public void setResponsibilityDesc(String responsibilityDesc) {
        this.responsibilityDesc = responsibilityDesc;
    }

    /**
     * @return Returns the responsibilityId.
     */
    public String getResponsibilityId() {
        return responsibilityId;
    }

    /**
     * @param responsibilityId
     *            The responsibilityId to set.
     */
    public void setResponsibilityId(String responsibilityId) {
        this.responsibilityId = responsibilityId;
    }

    /**
     * @return Returns the documentId.
     */
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getRouteLevel() {
        return routeLevel;
    }

    public void setRouteLevel(Integer routeLevel) {
        this.routeLevel = routeLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isInitialized() {
        return ActionRequestStatus.INITIALIZED.getCode().equals(getStatus());
    }

    public boolean isActive() {
        return ActionRequestStatus.ACTIVATED.getCode().equals(getStatus());
    }

    public boolean isApproveOrCompleteRequest() {
        return KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(getActionRequested()) || KewApiConstants.ACTION_REQUEST_COMPLETE_REQ.equals(getActionRequested());
    }

    public boolean isDone() {
        return ActionRequestStatus.DONE.getCode().equals(getStatus());
    }

    public boolean isReviewerUser() {
        return RecipientType.PRINCIPAL.getCode().equals(getRecipientTypeCd());
    }

    /**
     * Determines whether the specified principalId is in the recipient graph of this action request
     * @param principalId the principal id to check
     * @return whether the specified principalId is in the recipient graph of this action request
     */
    public boolean isRecipientRoutedRequest(String principalId) {
    	//before altering this method it is used in checkRouteLogAuthentication
    	//don't break that method
    	if (principalId == null || "".equals(principalId)) {
    		return false;
    	}

    	boolean isRecipientInGraph = false;
    	if (isReviewerUser()) {
    			isRecipientInGraph = getPrincipalId().equals(principalId);
    	} else if (isGroupRequest()) {
    		Group group = getGroup();
			if (group == null){
				LOG.error("Was unable to retrieve workgroup " + getGroupId());
			}
    		isRecipientInGraph = KimApiServiceLocator.getGroupService().isMemberOfGroup(principalId, group.getId());
    	}


        for (ActionRequestValue childRequest : getChildrenRequests())
        {
            isRecipientInGraph = isRecipientInGraph || childRequest.isRecipientRoutedRequest(principalId);
        }

    	return isRecipientInGraph;
    }

    public boolean isRecipientRoutedRequest(Recipient recipient) {
    	//before altering this method it is used in checkRouteLogAuthentication
    	//don't break that method
    	if (recipient == null) {
    		return false;
    	}

    	boolean isRecipientInGraph = false;
    	if (isReviewerUser()) {
    		if (recipient instanceof KimPrincipalRecipient) {
    			isRecipientInGraph = getPrincipalId().equals(((KimPrincipalRecipient) recipient).getPrincipalId());
    		} else if (recipient instanceof KimGroupRecipient){
    			isRecipientInGraph = KimApiServiceLocator.getGroupService().isMemberOfGroup(getPrincipalId(), ((KimGroupRecipient)recipient).getGroup().getId());
    		}

    	} else if (isGroupRequest()) {
    		Group group = getGroup();
			if (group == null){
				LOG.error("Was unable to retrieve workgroup " + getGroupId());
			}
    		if (recipient instanceof KimPrincipalRecipient) {
    			KimPrincipalRecipient principalRecipient = (KimPrincipalRecipient)recipient;
    			isRecipientInGraph = KimApiServiceLocator.getGroupService().isMemberOfGroup(principalRecipient.getPrincipalId(), group.getId());
    		} else if (recipient instanceof KimGroupRecipient) {
    			isRecipientInGraph = ((KimGroupRecipient) recipient).getGroup().getId().equals(group.getId());
    		}
    	}


        for (ActionRequestValue childRequest : getChildrenRequests())
        {
            isRecipientInGraph = isRecipientInGraph || childRequest.isRecipientRoutedRequest(recipient);
        }

    	return isRecipientInGraph;
    }

    public boolean isGroupRequest(){
    	return RecipientType.GROUP.getCode().equals(getRecipientTypeCd());
    }

    public boolean isRoleRequest() {
        return RecipientType.ROLE.getCode().equals(getRecipientTypeCd());
    }

    public boolean isAcknowledgeRequest() {
        return KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ.equals(getActionRequested());
    }

    public boolean isApproveRequest() {
        return KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(getActionRequested());
    }

    public boolean isCompleteRequst() {
        return KewApiConstants.ACTION_REQUEST_COMPLETE_REQ.equals(getActionRequested());
    }

    public boolean isFYIRequest() {
        return KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(getActionRequested());
    }

    /**
     * Allows comparison of action requests to see which is greater responsibility. -1 : indicates code 1 is lesser responsibility than code 2 0 : indicates the same responsibility 1 : indicates code1 is greater responsibility than code 2 The priority of action requests is as follows: fyi < acknowledge < (approve == complete)
     *
     * @param code1
     * @param code2
     * @param completeAndApproveTheSame
     * @return -1 if less than, 0 if equal, 1 if greater than
     */
    public static int compareActionCode(String code1, String code2, boolean completeAndApproveTheSame) {
    	int cutoff = Integer.MAX_VALUE;
    	if (completeAndApproveTheSame) {
    		// hacked so that APPROVE and COMPLETE are equal
    		cutoff = ACTION_CODE_RANK.length() - 3;
    	}
        Integer code1Index = Math.min(ACTION_CODE_RANK.indexOf(code1), cutoff);
        Integer code2Index = Math.min(ACTION_CODE_RANK.indexOf(code2), cutoff);
        return code1Index.compareTo(code2Index);
    }

    /**
     * Allows comparison of action requests to see which is greater responsibility. -1 : indicates type 1 is lesser responsibility than type 2 0 : indicates the same responsibility 1 : indicates type1 is greater responsibility than type 2
     *
     * @param type1
     * @param type2
     * @return -1 if less than, 0 if equal, 1 if greater than
     */
    public static int compareRecipientType(String type1, String type2) {
        Integer type1Index = RECIPIENT_TYPE_RANK.indexOf(type1);
        Integer type2Index = RECIPIENT_TYPE_RANK.indexOf(type2);
        return type1Index.compareTo(type2Index);
    }

    public static int compareDelegationType(DelegationType type1, DelegationType type2) {
    	Integer type1Index = DELEGATION_TYPE_RANK.indexOf(type1);
        Integer type2Index = DELEGATION_TYPE_RANK.indexOf(type2);
        return type1Index.compareTo(type2Index);
    }

    public List<ActionItem> getActionItems() {
    	if (this.simulatedActionItems == null || this.simulatedActionItems.isEmpty()) {
    		return (List<ActionItem>) KEWServiceLocator.getActionListService().findByActionRequestId(actionRequestId);
    	} else {
    		return this.simulatedActionItems;
    	}
    }

    public List<ActionItem> getSimulatedActionItems() {
    	if (this.simulatedActionItems == null) {
    		this.simulatedActionItems = new ArrayList<ActionItem>();
    	}
		return this.simulatedActionItems;
	}

	public void setSimulatedActionItems(List<ActionItem> simulatedActionItems) {
		this.simulatedActionItems = simulatedActionItems;
	}

	public Boolean getCurrentIndicator() {
        return currentIndicator;
    }

    public void setCurrentIndicator(Boolean currentIndicator) {
        this.currentIndicator = currentIndicator;
    }

    public String getParentActionRequestId() {
        return parentActionRequestId;
    }

    public void setParentActionRequestId(String parentActionRequestId) {
        this.parentActionRequestId = parentActionRequestId;
    }

    public ActionRequestValue getParentActionRequest() {
        return parentActionRequest;
    }

    public void setParentActionRequest(ActionRequestValue parentActionRequest) {
        this.parentActionRequest = parentActionRequest;
    }

    public List<ActionRequestValue> getChildrenRequests() {
        return childrenRequests;
    }

    public void setChildrenRequests(List<ActionRequestValue> childrenRequests) {
        this.childrenRequests = childrenRequests;
    }

    public String getQualifiedRoleName() {
        return qualifiedRoleName;
    }

    public void setQualifiedRoleName(String roleName) {
        this.qualifiedRoleName = roleName;
    }

    public DelegationType getDelegationType() {
        return DelegationType.fromCode(delegationTypeCode);
    }

    public void setDelegationType(DelegationType delegationPolicy) {
        this.delegationTypeCode = delegationPolicy == null ? null : delegationPolicy.getCode();
    }


    public String getDelegationTypeCode() {
        return delegationTypeCode;
    }

    public void setDelegationTypeCode(String delegationTypeCode) {
        this.delegationTypeCode = delegationTypeCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getApprovePolicy() {
        return approvePolicy;
    }

    public void setApprovePolicy(String requestType) {
        this.approvePolicy = requestType;
    }

    public boolean getHasApprovePolicy() {
        return getApprovePolicy() != null;
    }

    public boolean isDeactivated() {
        return ActionRequestStatus.DONE.getCode().equals(getStatus());
    }

    public boolean hasParent() {
        return getParentActionRequest() != null;
    }

    public boolean hasChild(ActionRequestValue actionRequest) {
        if (actionRequest == null)
            return false;
        String actionRequestId = actionRequest.getActionRequestId();
        for (Iterator<ActionRequestValue> iter = getChildrenRequests().iterator(); iter.hasNext();) {
            ActionRequestValue childRequest = iter.next();
            if (childRequest.equals(actionRequest) || (actionRequestId != null && actionRequestId.equals(childRequest.getActionRequestId()))) {
                return true;
            }
        }
        return false;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getQualifiedRoleNameLabel() {
        return qualifiedRoleNameLabel;
    }

    public void setQualifiedRoleNameLabel(String qualifiedRoleNameLabel) {
        this.qualifiedRoleNameLabel = qualifiedRoleNameLabel;
    }

    public String getCreateDateString() {
        if (createDateString == null || createDateString.trim().equals("")) {
            return RiceConstants.getDefaultDateFormat().format(getCreateDate());
        } else {
            return createDateString;
        }
    }

    public void setCreateDateString(String createDateString) {
        this.createDateString = createDateString;
    }

    public RouteNodeInstance getNodeInstance() {
		return nodeInstance;
	}

    public String getPotentialNodeName() {
        return (getNodeInstance() == null ? "" : getNodeInstance().getName());
    }

	public void setNodeInstance(RouteNodeInstance nodeInstance) {
		this.nodeInstance = nodeInstance;
	}

	public String getRecipientTypeLabel() {
		return RecipientType.fromCode(getRecipientTypeCd()).getLabel();
    }

    public RuleBaseValues getRuleBaseValues(){
        if(ruleBaseValuesId != null){
            return getRuleService().findRuleBaseValuesById(ruleBaseValuesId);
        }
        return null;
    }
    public String getRuleBaseValuesId() {
        return ruleBaseValuesId;
    }

    public void setRuleBaseValuesId(String ruleBaseValuesId) {
        this.ruleBaseValuesId = ruleBaseValuesId;
    }
    
	private RuleServiceInternal getRuleService() {
        return (RuleServiceInternal) KEWServiceLocator.getService(KEWServiceLocator.RULE_SERVICE);
    }

    public boolean isPrimaryDelegator() {
        boolean primaryDelegator = false;
        for (Iterator<ActionRequestValue> iter = childrenRequests.iterator(); iter.hasNext();) {
            ActionRequestValue childRequest = iter.next();
            primaryDelegator = DelegationType.PRIMARY.equals(childRequest.getDelegationType()) || primaryDelegator;
        }
        return primaryDelegator;
    }

    /**
     * Used to get primary delegate names on route log in the 'Requested Of' section so primary delegate requests
     * list the delegate and not the delegator as having the request 'IN ACTION LIST'.  This method doesn't recurse
     * and therefore assume an AR structure.
     *
     * @return primary delgate requests
     */
    public List<ActionRequestValue> getPrimaryDelegateRequests() {
        List<ActionRequestValue> primaryDelegateRequests = new ArrayList<ActionRequestValue>();
        for (ActionRequestValue childRequest : childrenRequests)
        {
            if (DelegationType.PRIMARY.equals(childRequest.getDelegationType()))
            {
                if (childRequest.isRoleRequest())
                {
                    for (ActionRequestValue actionRequestValue : childRequest.getChildrenRequests())
                    {
                        primaryDelegateRequests.add(actionRequestValue);
                    }
                } else
                {
                	primaryDelegateRequests.add(childRequest);
                }
            }
        }
        return primaryDelegateRequests;
    }

    public boolean isAdHocRequest() {                                          
    	return KewApiConstants.ADHOC_REQUEST_RESPONSIBILITY_ID.equals(getResponsibilityId());
    }

    public boolean isGeneratedRequest() {
    	return KewApiConstants.MACHINE_GENERATED_RESPONSIBILITY_ID.equals(getResponsibilityId());
    }

    public boolean isExceptionRequest() {
    	return KewApiConstants.EXCEPTION_REQUEST_RESPONSIBILITY_ID.equals(getResponsibilityId());
    }

    public boolean isRouteModuleRequest() {
    	// FIXME: KULRICE-5201 switched rsp_id to a varchar, so the comparison below is no longer valid
//    	return getResponsibilityId() > 0;
    	// TODO: KULRICE-5329 Verify that this code below makes sense 
    	return getResponsibilityId() != null && !KewApiConstants.SPECIAL_RESPONSIBILITY_ID_SET.contains(getResponsibilityId());
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("actionRequestId", actionRequestId)
            .append("actionRequested", actionRequested)
            .append("documentId", documentId)
            .append("status", status)
            .append("responsibilityId", responsibilityId)
            .append("groupId", groupId)
            .append("recipientTypeCd", recipientTypeCd)
            .append("priority", priority)
            .append("routeLevel", routeLevel)
            .append("actionTakenId", actionTakenId)
            .append("docVersion", docVersion)
            .append("createDate", createDate)
            .append("responsibilityDesc", responsibilityDesc)
            .append("annotation", annotation)
            .append("jrfVerNbr", jrfVerNbr)
            .append("principalId", principalId)
            .append("forceAction", forceAction)
            .append("parentActionRequestId", parentActionRequestId)
            .append("qualifiedRoleName", qualifiedRoleName)
            .append("roleName", roleName)
            .append("qualifiedRoleNameLabel", qualifiedRoleNameLabel)
            .append("displayStatus", displayStatus)
            .append("ruleBaseValuesId", ruleBaseValuesId)
            .append("delegationType", delegationTypeCode)
            .append("approvePolicy", approvePolicy)
            .append("actionTaken", actionTaken)
            .append("currentIndicator", currentIndicator)
            .append("createDateString", createDateString)
            .append("nodeInstance", nodeInstance).toString();
    }

	public String getRequestLabel() {
		return this.requestLabel;
	}

	public void setRequestLabel(String requestLabel) {
		this.requestLabel = requestLabel;
	}

    public String getGroupName() {
        return KimApiServiceLocator.getGroupService().getGroup(this.groupId).getName();
    }

	/**
	 * @return the resolveResponsibility
	 */
	public boolean getResolveResponsibility() {
		return this.resolveResponsibility;
	}

	/**
	 * @param resolveResponsibility the resolveResponsibility to set
	 */
	public void setResolveResponsibility(boolean resolveResponsibility) {
		this.resolveResponsibility = resolveResponsibility;
	}

	public DocumentRouteHeaderValue getRouteHeader() {
		if (this.routeHeader == null && this.documentId != null) {
			this.routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(this.documentId);
		}
		return this.routeHeader;
	}

	public void setRouteHeader(DocumentRouteHeaderValue routeHeader) {
		this.routeHeader = routeHeader;
	}
	
	public static ActionRequest to(ActionRequestValue actionRequestBo) {
		if (actionRequestBo == null) {
			return null;
		}
		return createActionRequestBuilder(actionRequestBo).build();
	}
	
	private static ActionRequest.Builder createActionRequestBuilder(ActionRequestValue actionRequestBo) {
		ActionRequest.Builder builder = ActionRequest.Builder.create(actionRequestBo.getActionRequestId(),
				ActionRequestType.fromCode(actionRequestBo.getActionRequested()),
				ActionRequestStatus.fromCode(actionRequestBo.getStatus()),
				actionRequestBo.getResponsibilityId(),
				actionRequestBo.getDocumentId(),
				RecipientType.fromCode(actionRequestBo.getRecipientTypeCd()));
		if (actionRequestBo.getActionTaken() != null) {
			builder.setActionTaken(ActionTaken.Builder.create(ActionTakenValue.to(actionRequestBo.getActionTaken())));
		}
		builder.setAnnotation(actionRequestBo.getAnnotation());
		builder.setCurrent(actionRequestBo.getCurrentIndicator().booleanValue());
		builder.setDateCreated(new DateTime(actionRequestBo.getCreateDate().getTime()));
		if (actionRequestBo.getDelegationType() != null) {
			builder.setDelegationType(actionRequestBo.getDelegationType());
		}
		builder.setForceAction(actionRequestBo.getForceAction().booleanValue());
		builder.setGroupId(actionRequestBo.getGroupId());
		builder.setNodeName(actionRequestBo.getPotentialNodeName());
		if (actionRequestBo.getParentActionRequestId() != null) {
			builder.setParentActionRequestId(actionRequestBo.getParentActionRequestId());
		}
		builder.setPrincipalId(actionRequestBo.getPrincipalId());
		if (actionRequestBo.getPriority() == null) {
			builder.setPriority(KewApiConstants.ACTION_REQUEST_DEFAULT_PRIORITY);
		} else {
            builder.setPriority(actionRequestBo.getPriority().intValue());
        }
        if (actionRequestBo.getRouteLevel() == null ) {
            builder.setRouteLevel(0);
        } else {
            builder.setRouteLevel(actionRequestBo.getRouteLevel().intValue());
        }
		builder.setQualifiedRoleName(actionRequestBo.getQualifiedRoleName());
		builder.setQualifiedRoleNameLabel(actionRequestBo.getQualifiedRoleNameLabel());
		builder.setRequestLabel(actionRequestBo.getRequestLabel());
		if (!StringUtils.isBlank(actionRequestBo.getApprovePolicy())) {
			builder.setRequestPolicy(ActionRequestPolicy.fromCode(actionRequestBo.getApprovePolicy()));
		}
		builder.setResponsibilityDescription(actionRequestBo.getResponsibilityDesc());
		builder.setRoleName(actionRequestBo.getRoleName());
		if (actionRequestBo.getNodeInstance() != null) {
			builder.setRouteNodeInstanceId(actionRequestBo.getNodeInstance().getRouteNodeInstanceId());
		}
		
		List<ActionRequest.Builder> childRequests = new ArrayList<ActionRequest.Builder>();
		if (actionRequestBo.getChildrenRequests() != null) {
			for (ActionRequestValue childActionRequestBo : actionRequestBo.getChildrenRequests()) {
				childRequests.add(createActionRequestBuilder(childActionRequestBo));
			}
		}
		builder.setChildRequests(childRequests);
		return builder;
	}
	
    /**
     * TODO - this javadoc copied from DTOConverter, needs to be updated!
     * 
     * Converts an ActionRequestVO to an ActionRequest. The ActionRequestDTO passed in must be the root action request in the
     * graph, otherwise an IllegalArgumentException is thrown. This is to avoid potentially sticky issues with circular
     * references in the conversion. NOTE: This method's primary purpose is to convert ActionRequestVOs returned from a
     * RouteModule. Incidentally, the DTO's returned from the route module will be lacking some information (like the node
     * instance) so no attempts are made to convert this data since further initialization is handled by a higher level
     * component (namely ActionRequestService.initializeActionRequestGraph).
     */
    public static ActionRequestValue from(ActionRequest actionRequest) {
        return ActionRequestValue.from(actionRequest, null);
    }
    
    /**
     * Converts an ActionRequestVO to an ActionRequest. The ActionRequestDTO passed in must be the root action request in the
     * graph, otherwise an IllegalArgumentException is thrown. This is to avoid potentially sticky issues with circular
     * references in the conversion. 
     * @param routeNodeInstanceLoader a service that will provide routeNodeInstanceS based on their IDs.
     */
    public static ActionRequestValue from(ActionRequest actionRequest, 
            RouteNodeInstanceLoader routeNodeInstanceLoader) {
        return convertActionRequest(actionRequest, null, routeNodeInstanceLoader);
    }

    private static ActionRequestValue convertActionRequest(ActionRequest actionRequest, ActionRequestValue parentActionRequestBo,
            RouteNodeInstanceLoader routeNodeInstanceLoader) {
        if (actionRequest == null) {
            return null;
        }
        ActionRequestValue actionRequestBo = new ActionRequestFactory().createBlankActionRequest();
        populateActionRequest(actionRequestBo, actionRequest, routeNodeInstanceLoader);
        if (parentActionRequestBo != null) {
            actionRequestBo.setParentActionRequest(parentActionRequestBo);
            actionRequestBo.setParentActionRequestId(parentActionRequestBo.getActionRequestId());
        }
        if (actionRequest.getChildRequests() != null) {
            for (ActionRequest childRequest : actionRequest.getChildRequests()) {
                actionRequestBo.getChildrenRequests().add(ActionRequestValue.convertActionRequest(childRequest, actionRequestBo, routeNodeInstanceLoader));
            }
        }
        return actionRequestBo;
    }

    /**
     * This method converts everything except for the parent and child requests
     */
    private static void populateActionRequest(ActionRequestValue actionRequestBo, ActionRequest actionRequest, 
            RouteNodeInstanceLoader routeNodeInstanceLoader) {

        actionRequestBo.setActionRequested(actionRequest.getActionRequested().getCode());
        if (!StringUtils.isBlank(actionRequest.getId())) {
            actionRequestBo.setActionRequestId(actionRequest.getId());
        }
        
        if (actionRequest.getActionTaken() != null) {
            // actionRequestBo.setActionTaken(ActionTakenValue.from(actionRequest.getActionTaken()));
            if (!StringUtils.isBlank(actionRequest.getActionTaken().getId())) {
                actionRequestBo.setActionTakenId(actionRequest.getActionTaken().getId());
            }
        }
        actionRequestBo.setAnnotation(actionRequest.getAnnotation());
        if (actionRequest.getRequestPolicy() != null) {
            actionRequestBo.setApprovePolicy(actionRequest.getRequestPolicy().getCode());
        }
        actionRequestBo.setCreateDate(new Timestamp(actionRequest.getDateCreated().getMillis()));
        actionRequestBo.setCurrentIndicator(actionRequest.isCurrent());
        if (actionRequest.getDelegationType() != null) {
            actionRequestBo.setDelegationType(actionRequest.getDelegationType());
        }
        //actionRequestBo.setDocVersion(actionRequest.?);
        actionRequestBo.setForceAction(actionRequest.isForceAction());
        actionRequestBo.setPriority(actionRequest.getPriority());
        actionRequestBo.setRouteLevel(actionRequest.getRouteLevel());
        actionRequestBo.setQualifiedRoleName(actionRequest.getQualifiedRoleName());
        actionRequestBo.setQualifiedRoleNameLabel(actionRequest.getQualifiedRoleNameLabel());
        actionRequestBo.setRecipientTypeCd(actionRequest.getRecipientType().getCode());
        actionRequestBo.setResponsibilityDesc(actionRequest.getResponsibilityDescription());
        if (!StringUtils.isBlank(actionRequest.getResponsibilityId())) {
            actionRequestBo.setResponsibilityId(actionRequest.getResponsibilityId());
        }
        actionRequestBo.setRoleName(actionRequest.getRoleName());
        String documentId = actionRequest.getDocumentId();
        if (documentId != null) {
            actionRequestBo.setDocumentId(documentId);
            actionRequestBo.setRouteHeader(KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId));
        }

        actionRequestBo.setStatus(actionRequest.getStatus().getCode());
        actionRequestBo.setPrincipalId(actionRequest.getPrincipalId());
        actionRequestBo.setGroupId(actionRequest.getGroupId());
        
        if (routeNodeInstanceLoader != null && !StringUtils.isBlank(actionRequest.getRouteNodeInstanceId())) {
            actionRequestBo.setNodeInstance(routeNodeInstanceLoader.load(actionRequest.getRouteNodeInstanceId()));
        }
    }
    
}
