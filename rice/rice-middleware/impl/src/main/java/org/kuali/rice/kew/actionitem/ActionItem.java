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
package org.kuali.rice.kew.actionitem;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.action.ActionItemContract;
import org.kuali.rice.kew.api.action.RecipientType;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.RowStyleable;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the model for action items. These are displayed as the action list as well.  Mapped to ActionItemService.
 * NOTE: This object contains denormalized fields that have been copied from related ActionRequestValue and DocumentRouteHeaderValue
 * objects for performance reasons.  These should be preserved and their related objects should not be added to the OJB
 * mapping as we do not want them loaded for each ActionItem object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="KREW_ACTN_ITM_T")
//@Sequence(name="KREW_ACTN_ITM_S",property="id")
@NamedQueries({
    @NamedQuery(name="ActionItem.QuickLinks.FindActionListStatsByPrincipalId", query="SELECT docName, COUNT(*) FROM ActionItem WHERE principalId = :principalId " +
        "AND (delegationType IS null OR delegationType != :delegationType) GROUP BY docName")
})
public class ActionItem implements ActionItemContract, Serializable {

    private static final long serialVersionUID = -1079562205125660151L;

    @Id
    @GeneratedValue(generator="KREW_ACTN_ITM_S")
	@GenericGenerator(name="KREW_ACTN_ITM_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_ACTN_ITM_S"),
			@Parameter(name="value_column",value="id")
	})
    @Column(name="ACTN_ITM_ID")
	private String id;
    @Column(name="PRNCPL_ID")
	private String principalId;
	@Column(name="ASND_DT")
	private Timestamp dateAssigned;
    @Column(name="RQST_CD")
	private String actionRequestCd;
    @Column(name="ACTN_RQST_ID", nullable=false)
	private String actionRequestId;
    @Column(name="DOC_HDR_ID")//, insertable=false, updatable=false)
	private String documentId;
    @Column(name="GRP_ID")
	private String groupId;
    @Column(name="DOC_HDR_TTL")
	private String docTitle;
    @Column(name="DOC_TYP_LBL")
	private String docLabel;
    @Column(name="DOC_HDLR_URL")
	private String docHandlerURL;
    @Column(name="DOC_TYP_NM")
	private String docName;
    @Column(name="RSP_ID")
    private String responsibilityId = "1";
    @Column(name="ROLE_NM")
	private String roleName;
    @Column(name="DLGN_PRNCPL_ID")
	private String delegatorPrincipalId;
    @Column(name="DLGN_GRP_ID")
	private String delegatorGroupId;
    @Column(name="DLGN_TYP")
	private String delegationType;
    @Column(name="RQST_LBL")
    private String requestLabel;

    // used by Document Operations screen
    @Transient
    private String dateAssignedStringValue;


    //@PrePersist
    public void beforeInsert(){
        OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
    }

    @Deprecated
    @Override
    public String getActionToTake() {
        // deprecated, always return null (see the contract javadoc for more details)
        return null;
    }

    @Deprecated
    @Override
    public String getDateAssignedString() {
        // deprecated, always return null (see the contract javadoc for more details)
        return null;
    }

    public String getDateAssignedStringValue() {
        if (StringUtils.isBlank(dateAssignedStringValue)) {
            return RiceConstants.getDefaultDateFormat().format(getDateAssigned());
        }
        return dateAssignedStringValue;
    }

    public void setDateAssignedStringValue(String dateAssignedStringValue) {
        this.dateAssignedStringValue = dateAssignedStringValue;
    }
    
    public String getId() {
        return id;
    }
    
    public String getPrincipalId() {
        return principalId;
    }
    
    public Timestamp getDateAssigned() {
        return dateAssigned;
    }

    public DateTime getDateTimeAssigned() {
        return new DateTime(dateAssigned);
    }
    
    public String getActionRequestCd() {
        return actionRequestCd;
    }
    
    public String getActionRequestId() {
        return actionRequestId;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public String getGroupId() {
        return groupId;
    }

    public String getDocTitle() {
        return docTitle;
    }
    
    public String getDocLabel() {
        return docLabel;
    }
    
    public String getDocHandlerURL() {
        return docHandlerURL;
    }
    
    public String getDocName() {
        return docName;
    }

    public String getResponsibilityId() {
        return responsibilityId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getDelegatorPrincipalId() {
        return delegatorPrincipalId;
    }

    public String getDelegatorGroupId() {
        return delegatorGroupId;
    }

    public DelegationType getDelegationType() {
        return DelegationType.fromCode(delegationType);
    }

    public String getRequestLabel() {
        return this.requestLabel;
    }

    private Group getGroup(String groupId) {
    	if (StringUtils.isBlank(groupId)) {
    		return null;
    	}
    	return KimApiServiceLocator.getGroupService().getGroup(groupId);
    }

    public Group getGroup(){
    	return getGroup(groupId);
    }

    public String getRecipientTypeCode() {
        String recipientTypeCode = RecipientType.PRINCIPAL.getCode();
        if (getRoleName() != null) {
            recipientTypeCode = RecipientType.ROLE.getCode();
        }
        if (getGroupId() != null) {
            recipientTypeCode = RecipientType.GROUP.getCode();
        }
        return recipientTypeCode;
    }
    
    public String getActionRequestLabel() {
    	if (StringUtils.isNotBlank(getRequestLabel())) {
    		return getRequestLabel();
    	}
    	return CodeTranslator.getActionRequestLabel(getActionRequestCd());
    }

    public boolean isWorkgroupItem() {
        return getGroupId() != null;
    }
    
    public Principal getPrincipal(){
        return KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
    }

    public void setResponsibilityId(String responsibilityId) {
        this.responsibilityId = responsibilityId;
    }
    
    public void setDocName(String docName) {
        this.docName = docName;
    }

    public void setActionRequestCd(String actionRequestCd) {
        this.actionRequestCd = actionRequestCd;
    }

    public void setDateAssigned(Timestamp dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActionRequestId(String actionRequestId) {
        this.actionRequestId = actionRequestId;
    }

    public void setDocHandlerURL(String docHandlerURL) {
        this.docHandlerURL = docHandlerURL;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setDocLabel(String docLabel) {
        this.docLabel = docLabel;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public void setDelegatorPrincipalId(String delegatorPrincipalId) {
        this.delegatorPrincipalId = delegatorPrincipalId;
    }
    
    public void setDelegatorGroupId(String delegatorGroupId) {
        this.delegatorGroupId = delegatorGroupId;
    }

    public void setDelegationType(DelegationType delegationType) {
        this.delegationType = delegationType == null ? null : delegationType.getCode();
    }
    
    public void setRequestLabel(String requestLabel) {
        this.requestLabel = requestLabel;
    }

    @Deprecated
    @Override
    public Integer getActionItemIndex() {
        // deprecated, always return null (see the contract javadoc for more details)
        return null;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("principalId", principalId)
                                        .append("dateAssigned", dateAssigned)
                                        .append("actionRequestCd", actionRequestCd)
                                        .append("actionRequestId", actionRequestId)
                                        .append("documentId", documentId)
                                        .append("groupId", groupId)
                                        .append("docTitle", docTitle)
                                        .append("docLabel", docLabel)
                                        .append("docHandlerURL", docHandlerURL)
                                        .append("docName", docName)
                                        .append("responsibilityId", responsibilityId)
                                        .append("roleName", roleName)
                                        .append("delegatorPrincipalId", delegatorPrincipalId)
                                        .append("delegatorGroupId", delegatorGroupId)
                                        .append("delegationType", delegationType)
                                        .toString();
    }

    public static org.kuali.rice.kew.api.action.ActionItem to(ActionItem bo) {
        if (bo == null) {
            return null;
        }
        return org.kuali.rice.kew.api.action.ActionItem.Builder.create(bo).build();
    }
    
    public static List<org.kuali.rice.kew.api.action.ActionItem> to(Collection<ActionItem> bos) {
        if (bos == null)
            return null;
        if (bos.isEmpty()) 
            return new ArrayList<org.kuali.rice.kew.api.action.ActionItem>();
        
        List<org.kuali.rice.kew.api.action.ActionItem> dtos = new ArrayList<org.kuali.rice.kew.api.action.ActionItem>(bos.size());
        for (ActionItem bo : bos) {
            dtos.add(ActionItem.to(bo));
        }
        return dtos;
    }
}
