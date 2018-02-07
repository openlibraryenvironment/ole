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
package org.kuali.rice.krad.app.persistence.jpa;

import java.util.HashSet;
import java.util.Set;

/**
 * Business Object class exposer which adds non-KNS Rice entities to the KNS persistence units, so that these objects
 * can be used with KNS functionality - for instance, PersonImpl objects can be looked up. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RiceToNervousSystemBusinessObjectClassExposer implements PersistableBusinessObjectClassExposer {

	/**
	 * Exposes a list of non-KNS entity class names
	 * 
	 * @see PersistableBusinessObjectClassExposer#exposePersistableBusinessObjectClassNames()
	 */
	public Set<String> exposePersistableBusinessObjectClassNames() {
		Set<String> exposedClasses = new HashSet<String>();
		/*exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.AddressTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.AffiliationTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.CitizenshipStatusImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.EmailTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.EmploymentStatusImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.EmploymentTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.EntityNameTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.EntityTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.ExternalIdentifierTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.reference.impl.PhoneTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.types.impl.KimTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.types.impl.KimAttributeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.types.impl.KimTypeAttributeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimPrincipalImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityAffiliationImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityBioDemographicsImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityCitizenshipImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityEmploymentInformationImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityExternalIdentifierImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityPrivacyPreferencesImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityNameImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityEthnicityImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityResidencyImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityVisaImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityEntityTypeImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityAddressImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityEmailImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityPhoneImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.impl.GroupImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.group.impl.GroupMemberImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.group.impl.GroupAttributeDataImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.impl.role.RoleBo.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.RoleMemberImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.RoleMemberAttributeDataImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimDelegationImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimDelegationMemberImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimDelegationMemberAttributeDataImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimPermissionImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimPermissionTemplateImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.RolePermissionImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.PermissionAttributeDataImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimResponsibilityImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.KimResponsibilityTemplateImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.RoleResponsibilityImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.ResponsibilityAttributeDataImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.role.impl.RoleResponsibilityActionImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.identity.impl.KimEntityDefaultInfoCacheImpl.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentAffiliation.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentEmploymentInfo.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentPrivacy.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentName.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentAddress.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentEmail.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentPhone.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentRole.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.KimDocumentRoleMember.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.KimDocumentRoleQualifier.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibilityAction.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.PersonDocumentGroup.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.document.IdentityManagementRoleDocument.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.KimDocumentRolePermission.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.KimDocumentRoleResponsibility.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.RoleDocumentDelegation.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMemberQualifier.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.document.IdentityManagementGroupDocument.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.GroupDocumentMember.class.getName());
	    exposedClasses.add(org.kuali.rice.kim.bo.ui.GroupDocumentQualifier.class.getName()); 
	    exposedClasses.add(org.kuali.rice.kim.document.IdentityManagementKimDocument.class.getName()); 
	    exposedClasses.add(org.kuali.rice.kim.document.IdentityManagementPersonDocument.class.getName()); 
	    exposedClasses.add(org.kuali.rice.ken.bo.Notification.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationChannel.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationContentType.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationPriority.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationProducer.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationChannelReviewer.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationRecipient.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationRecipientList.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationSender.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.UserChannelSubscription.class.getName());
	    exposedClasses.add(org.kuali.rice.ken.bo.NotificationMessageDelivery.class.getName());
	    */
	    return exposedClasses;
	}

}
