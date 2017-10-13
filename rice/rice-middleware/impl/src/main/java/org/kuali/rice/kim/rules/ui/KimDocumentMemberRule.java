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
package org.kuali.rice.kim.rules.ui;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.document.IdentityManagementRoleDocument;
import org.kuali.rice.kim.document.rule.AttributeValidationHelper;
import org.kuali.rice.kim.framework.role.RoleTypeService;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.services.KimImplServiceLocator;
import org.kuali.rice.kim.rule.event.ui.AddMemberEvent;
import org.kuali.rice.kim.rule.ui.AddMemberRule;
import org.kuali.rice.kns.rules.DocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.impl.KRADModuleService;
import org.kuali.rice.core.api.util.VersionHelper;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.api.bus.Endpoint;
import org.kuali.rice.ksb.api.bus.ServiceBus;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KimDocumentMemberRule extends DocumentRuleBase implements AddMemberRule {

	private static final String ERROR_PATH = "member.memberId";

	protected AttributeValidationHelper attributeValidationHelper = new AttributeValidationHelper();
	
	public boolean processAddMember(AddMemberEvent addMemberEvent){
		KimDocumentRoleMember newMember = addMemberEvent.getMember();
		IdentityManagementRoleDocument document = (IdentityManagementRoleDocument)addMemberEvent.getDocument();
	    boolean rulePassed = true;

        if (newMember == null || StringUtils.isBlank(newMember.getMemberId())){
            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_EMPTY_ENTRY, new String[] {"Member"});
            return false;
        }
    	if(!validAssignRole(newMember, document)) {
    		return false;
        }
		List<RemotableAttributeError> validationErrors = new ArrayList<RemotableAttributeError>();
        KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(document.getKimType());
        
        Long newMemberFromTime = newMember.getActiveFromDate() == null ? 0L : newMember.getActiveFromDate().getTime();
        Long newMemberToTime = newMember.getActiveToDate() == null ? Long.MAX_VALUE : newMember.getActiveToDate().getTime();
        
		List<RemotableAttributeError> errorsAttributesAgainstExisting  = new ArrayList<RemotableAttributeError>();
        Map<String, String> newMemberQualifiers = attributeValidationHelper.convertQualifiersToMap(newMember.getQualifiers());

	    Map<String, String> oldMemberQualifiers;
	    for (KimDocumentRoleMember member: document.getMembers()){
	    	Long memberFromTime = member.getActiveFromDate() == null ? 0L : member.getActiveFromDate().getTime();
            Long memberToTime = member.getActiveToDate() == null ? Long.MAX_VALUE : member.getActiveToDate().getTime();
	    	oldMemberQualifiers = attributeValidationHelper.convertQualifiersToMap(member.getQualifiers());

            if ((member.getMemberId().equals(newMember.getMemberId()) &&
                    member.getMemberTypeCode().equals(newMember.getMemberTypeCode()))
                    && ((newMemberFromTime >= memberFromTime && newMemberFromTime < memberToTime)
                    || (newMemberToTime >= memberFromTime && newMemberToTime <= memberToTime)))  {

                errorsAttributesAgainstExisting = kimTypeService.validateAttributesAgainstExisting(
	    			document.getKimType().getId(), newMemberQualifiers, oldMemberQualifiers);
			    validationErrors.addAll(
					attributeValidationHelper.convertErrorsForMappedFields(ERROR_PATH, errorsAttributesAgainstExisting));
	    	    if (!errorsAttributesAgainstExisting.isEmpty()) {
	                rulePassed = false;
	                GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_DUPLICATE_ENTRY, new String[] {"Member"});
	                break;
	    	    }
            }
	    }

        boolean shouldNotValidate = newMember.isRole();
	    if ( kimTypeService != null && ObjectUtils.isNotNull( document.getKimType() ) && StringUtils.isNotBlank(document.getKimType().getServiceName()) ) {
            VersionedService<RoleTypeService> versionedRoleTypeService = getVersionedRoleTypeService(document.getKimType());
            if (versionedRoleTypeService != null) {
                boolean versionOk = VersionHelper.compareVersion(versionedRoleTypeService.getVersion(), CoreConstants.Versions.VERSION_2_1_2)!=-1? true:false;
                if(versionOk) {
                    shouldNotValidate = versionedRoleTypeService.getService().shouldValidateQualifiersForMemberType( MemberType.fromCode(newMember.getMemberTypeCode()));
                } else {
                    shouldNotValidate = false;
                }
            }
        }
        if (kimTypeService !=null && !shouldNotValidate) {
    		List<RemotableAttributeError> localErrors = kimTypeService.validateAttributes( document.getKimType().getId(), attributeValidationHelper.convertQualifiersToMap( newMember.getQualifiers() ) );
	        validationErrors.addAll( attributeValidationHelper.convertErrors("member",
                    attributeValidationHelper.convertQualifiersToAttrIdxMap(newMember.getQualifiers()), localErrors) );
        }
    	if (!validationErrors.isEmpty()) {
    		attributeValidationHelper.moveValidationErrorsToErrorMap(validationErrors);
    		rulePassed = false;
    	}

		return rulePassed;
	} 

	protected boolean validAssignRole(KimDocumentRoleMember roleMember, IdentityManagementRoleDocument document){
        boolean rulePassed = true;
		if(StringUtils.isNotEmpty(document.getRoleNamespace())){
			Map<String,String> roleDetails = new HashMap<String,String>();
			roleDetails.put(KimConstants.AttributeConstants.NAMESPACE_CODE, document.getRoleNamespace());
			roleDetails.put(KimConstants.AttributeConstants.ROLE_NAME, document.getRoleName());
			if (!getDocumentDictionaryService().getDocumentAuthorizer(document).isAuthorizedByTemplate(
					document, 
					KimConstants.NAMESPACE_CODE, 
					KimConstants.PermissionTemplateNames.ASSIGN_ROLE,
					GlobalVariables.getUserSession().getPerson().getPrincipalId(), 
					roleDetails, null)){
	            GlobalVariables.getMessageMap().putError(ERROR_PATH, RiceKeyConstants.ERROR_ASSIGN_ROLE, 
	            		new String[] {document.getRoleNamespace(), document.getRoleName()});
	            rulePassed = false;
			}
		}
		return rulePassed;
	}

    private static class VersionedService<T> {

        String version;
        T service;

        VersionedService(String version, T service) {
            this.version = version;
            this.service = service;
        }

        T getService() {
            return this.service;
        }

        String getVersion() {
            return this.version;
        }

    }

    protected VersionedService<RoleTypeService> getVersionedRoleTypeService(KimType typeInfo) {
        String serviceName = typeInfo.getServiceName();
        if (serviceName != null) {
            String version = "2.0.0"; // default version since the base services have been available since then
            RoleTypeService roleTypeService = null;

            try {

                ServiceBus serviceBus = KsbApiServiceLocator.getServiceBus();
                Endpoint endpoint = serviceBus.getEndpoint(QName.valueOf(serviceName));
                if (endpoint != null) {
                    version = endpoint.getServiceConfiguration().getServiceVersion();
                }
                KimTypeService service = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(serviceName));
                if (service != null && service instanceof RoleTypeService) {
                    roleTypeService = (RoleTypeService) service;
                } else {
                    roleTypeService = (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
                }
            } catch (Exception ex) {
                roleTypeService = (RoleTypeService) KimImplServiceLocator.getService("kimNoMembersRoleTypeService");
            }

            return new VersionedService<RoleTypeService>(version, roleTypeService);
        }

        return null;
    }


}
