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
package org.kuali.rice.kim.document;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegation;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.springframework.util.AutoPopulatingList;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

/**
 * This is a description of what this class does - bhargavp don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
@AttributeOverrides({
	@AttributeOverride(name="documentNumber",column=@Column(name="FDOC_NBR"))
})
@AssociationOverrides({
	@AssociationOverride(name="documentHeader",joinColumns=@JoinColumn(name="FDOC_NBR",referencedColumnName="DOC_HDR_ID",insertable=false,updatable=false))
})
public class IdentityManagementKimDocument extends TransactionalDocumentBase {

	protected static final Logger LOG = Logger.getLogger(IdentityManagementKimDocument.class);
	
	@OneToMany(targetEntity=RoleDocumentDelegation.class, fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name="FDOC_NBR",insertable=false,updatable=false)
	protected List<RoleDocumentDelegation> delegations = new AutoPopulatingList(RoleDocumentDelegation.class);
	@Transient
	protected List<RoleDocumentDelegationMember> delegationMembers = new AutoPopulatingList(RoleDocumentDelegationMember.class);
	@Transient
	protected transient SequenceAccessorService sequenceAccessorService;
	
	protected void addDelegationMemberToDelegation(RoleDocumentDelegationMember delegationMember){
		RoleDocumentDelegation delegation;
		if(DelegationType.PRIMARY.getCode().equals(delegationMember.getDelegationTypeCode())){
			delegation = getPrimaryDelegation();
		} else{
			delegation = getSecondaryDelegation();
		}
		delegationMember.setDelegationId(delegation.getDelegationId());
		delegation.getMembers().add(delegationMember);
		delegation.setRoleId(delegationMember.getRoleBo().getId());
		delegation.setKimTypeId(delegationMember.getRoleBo().getKimTypeId());
	}

	protected RoleDocumentDelegation getPrimaryDelegation(){
		RoleDocumentDelegation primaryDelegation = null;
		for(RoleDocumentDelegation delegation: getDelegations()){
			if(delegation.isDelegationPrimary()) {
				primaryDelegation = delegation;
            }
		}
		if(primaryDelegation==null){
			primaryDelegation = new RoleDocumentDelegation();
			primaryDelegation.setDelegationId(getDelegationId());
			primaryDelegation.setDelegationTypeCode(DelegationType.PRIMARY.getCode());
			getDelegations().add(primaryDelegation);
		}
		return primaryDelegation;
	}

	protected String getDelegationId(){
		return getSequenceAccessorService().getNextAvailableSequenceNumber(KimConstants.SequenceNames.KRIM_DLGN_ID_S, this.getClass()).toString();
	}
	
	protected RoleDocumentDelegation getSecondaryDelegation(){
		RoleDocumentDelegation secondaryDelegation = null;
		for(RoleDocumentDelegation delegation: getDelegations()){
			if(delegation.isDelegationSecondary()) {
				secondaryDelegation = delegation;
            }
		}
		if(secondaryDelegation==null){
			secondaryDelegation = new RoleDocumentDelegation();
			secondaryDelegation.setDelegationId(getDelegationId());
			secondaryDelegation.setDelegationTypeCode(DelegationType.SECONDARY.getCode());
			getDelegations().add(secondaryDelegation);
		}
		return secondaryDelegation;
	}

	/**
	 * @return the delegations
	 */
	public List<RoleDocumentDelegation> getDelegations() {
		return this.delegations;
	}

	/**
	 * @param delegations the delegations to set
	 */
	public void setDelegations(List<RoleDocumentDelegation> delegations) {
		this.delegations = delegations;
	}

	/**
	 * @return the delegationMembers
	 */
	public List<RoleDocumentDelegationMember> getDelegationMembers() {
		return this.delegationMembers;
	}

	/**
	 * @param delegationMembers the delegationMembers to set
	 */
	public void setDelegationMembers(
			List<RoleDocumentDelegationMember> delegationMembers) {
		this.delegationMembers = delegationMembers;
	}
	
	protected SequenceAccessorService getSequenceAccessorService(){
		if(this.sequenceAccessorService==null){
	    	this.sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
		}
		return this.sequenceAccessorService;
	}

    public String getKimAttributeDefnId(KimAttributeField definition){
   		return definition.getId();
    }

}
