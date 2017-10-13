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
package org.kuali.rice.kim.web.struts.action;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleQualifier;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase;
import org.kuali.rice.kim.web.struts.form.IdentityManagementPersonDocumentForm;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

/**
 * This is a description of what this class does - jonathan don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityManagementPersonInquiry extends IdentityManagementBaseInquiryAction {
    private static final Logger LOG = Logger.getLogger(IdentityManagementPersonInquiry.class);	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.web.struts.action.IdentityManagementBaseInquiryAction#loadKimObject(javax.servlet.http.HttpServletRequest, org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase)
	 */
	@Override
	protected void loadKimObject(HttpServletRequest request,
			IdentityManagementDocumentFormBase form) {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        String principalId = request.getParameter(KIMPropertyConstants.Person.PRINCIPAL_ID);
        String principalName = request.getParameter(KIMPropertyConstants.Person.PRINCIPAL_NAME);
        if ( StringUtils.isBlank(principalId) && StringUtils.isNotBlank(principalName) ) {
        	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName);
        	if ( principal != null ) {
        		principalId = principal.getPrincipalId();
        	}
        }
        if ( principalId != null ) {
        	Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
        	if (principal != null) {
	        	personDocumentForm.setPrincipalId(principalId);
	        	getUiDocumentService().loadEntityToPersonDoc(personDocumentForm.getPersonDocument(), personDocumentForm.getPrincipalId() );
	            personDocumentForm.setCanOverrideEntityPrivacyPreferences(getUiDocumentService().canOverrideEntityPrivacyPreferences(GlobalVariables.getUserSession().getPrincipalId(), personDocumentForm.getPrincipalId()));
	        	populateRoleInformation( personDocumentForm.getPersonDocument() );
        	} else {
        		LOG.error("No records found for Person Inquiry.");
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_INQUIRY);
        	}
        }
	}

	protected void populateRoleInformation( IdentityManagementPersonDocument personDoc ) {
		for (PersonDocumentRole role : personDoc.getRoles()) {
	        KimTypeService kimTypeService = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(
                    getKimTypeServiceName(KimTypeBo.to(role.getKimRoleType()))));
	        //it is possible that the the kimTypeService is coming from a remote application 
	        // and therefore it can't be guarenteed that it is up and working, so using a try/catch to catch this possibility.
	        try {
	            role.setDefinitions(kimTypeService.getAttributeDefinitions(role.getKimTypeId()));
	        } catch (Exception ex) {
                LOG.warn("Not able to retrieve KimTypeService from remote system for KIM Type Id: " + role.getKimTypeId(), ex);
            }
        	// when post again, it will need this during populate
            role.setNewRolePrncpl(new KimDocumentRoleMember());
            for (KimAttributeField key : role.getDefinitions()) {
            	KimDocumentRoleQualifier qualifier = new KimDocumentRoleQualifier();
            	//qualifier.setQualifierKey(key);
	        	setAttrDefnIdForQualifier(qualifier, key);
            	role.getNewRolePrncpl().getQualifiers().add(qualifier);
            }
	        role.setAttributeEntry( getUiDocumentService().getAttributeEntries( role.getDefinitions() ) );
		}
	}
	
    private void setAttrDefnIdForQualifier(KimDocumentRoleQualifier qualifier,KimAttributeField definition) {
   		qualifier.setKimAttrDefnId(definition.getId());
   		qualifier.refreshReferenceObject("kimAttribute");
    }
	private String getKimTypeServiceName (KimType kimType) {
    	String serviceName = kimType.getServiceName();
    	if (StringUtils.isBlank(serviceName)) {
    		serviceName = "kimTypeService";
    	}
    	return serviceName;

	}
	
}
