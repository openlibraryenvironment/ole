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
package org.kuali.rice.kim.bo.ui;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityBo;
import org.kuali.rice.kim.impl.role.RoleResponsibilityBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@IdClass(KimDocumentRoleResponsibilityActionId.class)
@Entity
@Table(name="KRIM_PND_ROLE_RSP_ACTN_MT")
public class KimDocumentRoleResponsibilityAction extends KimDocumentBoEditableBase {
	private static final long serialVersionUID = 696663543888096105L;
	@Id
	@GeneratedValue(generator="KRIM_ROLE_RSP_ACTN_ID_S")
	@GenericGenerator(name="KRIM_ROLE_RSP_ACTN_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRIM_ROLE_RSP_ACTN_ID_S"),
			@Parameter(name="value_column",value="id")
		})
	@Column(name="ROLE_RSP_ACTN_ID")
	protected String roleResponsibilityActionId;
	@Column(name="ROLE_RSP_ID")
	protected String roleResponsibilityId;
	@Column(name="ROLE_MBR_ID")
	protected String roleMemberId;
	@Column(name="ACTN_TYP_CD")
	protected String actionTypeCode;
	@Column(name="ACTN_PLCY_CD")
	protected String actionPolicyCode;
	@Column(name="PRIORITY_NBR")
	protected Integer priorityNumber;
	@Column(name="FRC_ACTN")
	protected boolean forceAction;
	// temporary default value in lieu of optimistic locking
	@Column(name="VER_NBR")
	protected Long versionNumber = (long) 0;
	@Transient
	protected ResponsibilityBo kimResponsibility;
	@Transient
	protected RoleResponsibilityBo roleResponsibility;


	
	/*{
		roleResponsibility = new RoleResponsibilityImpl();
		roleResponsibility.setKimResponsibility(new KimResponsibilityImpl());
		roleResponsibility.getKimResponsibility().setTemplate(new KimResponsibilityTemplateImpl());
	}*/
	
	/**
	 * @return the kimResponsibility
	 */
	public ResponsibilityBo getKimResponsibility() {
			if ( ObjectUtils.isNull( kimResponsibility ) && ObjectUtils.isNotNull( getRoleResponsibility() ) ) {
			//TODO: this needs to be changed to use the KimResponsibilityInfo object
			// but the changes are involved in the UiDocumentService based on the copyProperties method used
			// to move the data to/from the document/real objects
            Responsibility info = KimApiServiceLocator.getResponsibilityService().getResponsibility(getRoleResponsibility().getResponsibilityId());
            kimResponsibility = ResponsibilityBo.from(info);
			}
		return kimResponsibility;
	}
	/**
	 * @param kimResponsibility the kimResponsibility to set
	 */
	public void setKimResponsibility(ResponsibilityBo kimResponsibility) {
		this.kimResponsibility = kimResponsibility;
	}
	public String getRoleResponsibilityActionId() {
		return this.roleResponsibilityActionId;
	}
	public void setRoleResponsibilityActionId(String roleResponsibilityResolutionId) {
		this.roleResponsibilityActionId = roleResponsibilityResolutionId;
	}
	public String getRoleResponsibilityId() {
		return this.roleResponsibilityId;
	}
	public void setRoleResponsibilityId(String roleResponsibilityId) {
		this.roleResponsibilityId = roleResponsibilityId;
	}
	public String getActionTypeCode() {
		return this.actionTypeCode;
	}
	public void setActionTypeCode(String actionTypeCode) {
		this.actionTypeCode = actionTypeCode;
	}
	public Integer getPriorityNumber() {
		return this.priorityNumber;
	}
	public void setPriorityNumber(Integer priorityNumber) {
		this.priorityNumber = priorityNumber;
	}

	public String getActionPolicyCode() {
		return this.actionPolicyCode;
	}
	public void setActionPolicyCode(String actionPolicyCode) {
		this.actionPolicyCode = actionPolicyCode;
	}
	public String getRoleMemberId() {
		return this.roleMemberId;
	}
	public void setRoleMemberId(String roleMemberId) {
		this.roleMemberId = roleMemberId;
	}
	
	/**
	 * 
	 * This method fore readonlyalterdisplay
	 * 
	 * @return
	 */
	public String getActionPolicyDescription() {
		return (String)CodeTranslator.approvePolicyLabels.get(this.actionPolicyCode);
	}

	/**
	 * 
	 * This method fore readonlyalterdisplay
	 * 
	 * @return
	 */
	public String getActionTypeDescription() {
		return (String) CodeTranslator.arLabels.get(this.actionTypeCode);
	}
	/**
	 * @return the roleResponsibility
	 */
	public RoleResponsibilityBo getRoleResponsibility() {
		if ( ObjectUtils.isNull( roleResponsibility ) && roleResponsibilityId != null ) {
			//TODO: this needs to be changed to use the KimResponsibilityInfo object
			// but the changes are involved in the UiDocumentService based on the copyProperties method used
			// to move the data to/from the document/real objects
			roleResponsibility = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(RoleResponsibilityBo.class, getRoleResponsibilityId());
		}
		return roleResponsibility;
	}
	/**
	 * @param roleResponsibility the roleResponsibility to set
	 */
	public void setRoleResponsibility(RoleResponsibilityBo roleResponsibility) {
		this.roleResponsibility = roleResponsibility;
	}

	/**
	 * @return the forceAction
	 */
	public boolean isForceAction() {
		return this.forceAction;
	}
	/**
	 * @param forceAction the forceAction to set
	 */
	public void setForceAction(boolean forceAction) {
		this.forceAction = forceAction;
	}

}
