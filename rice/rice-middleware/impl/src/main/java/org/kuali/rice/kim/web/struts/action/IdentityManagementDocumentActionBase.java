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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.framework.role.RoleTypeService;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.type.KimTypeLookupableHelperServiceImpl;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.kns.web.struts.form.KualiTableRenderFormMetadata;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
abstract public class IdentityManagementDocumentActionBase extends KualiTransactionalDocumentActionBase {

    private static final Logger LOG = Logger.getLogger( IdentityManagementDocumentActionBase.class );

	protected static final String CHANGE_MEMBER_TYPE_CODE_METHOD_TO_CALL = "changeMemberTypeCode";
	protected static final String CHANGE_NAMESPACE_METHOD_TO_CALL = "changeNamespace";

	protected IdentityService identityService;
	protected ResponsibilityService responsibilityService;
	protected UiDocumentService uiDocumentService;
		
	@Override
	public ActionForward performLookup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward =  super.performLookup(mapping, form, request, response);
		String path = forward.getPath();
		//Making the hack look cleaner!
		forward.setPath(KimCommonUtilsInternal.getPathWithKimContext(path, getActionName()));
		return forward;
	}

	protected abstract String getActionName();
	
    protected IdentityService getIdentityService() {
    	if ( identityService == null ) {
    		identityService = KimApiServiceLocator.getIdentityService();
    	}
		return identityService;
	}

    protected ResponsibilityService getResponsibilityService() {
    	if ( responsibilityService == null ) {
    		responsibilityService = KimApiServiceLocator.getResponsibilityService();
    	}
		return responsibilityService;
	}

    protected UiDocumentService getUiDocumentService() {
		if ( uiDocumentService == null ) {
			uiDocumentService = KIMServiceLocatorInternal.getUiDocumentService();
		}
		return uiDocumentService;
	}

	@Override
    protected String getReturnLocation(HttpServletRequest request, ActionMapping mapping){
    	String returnLocation = super.getReturnLocation(request, mapping);
    	return KimCommonUtilsInternal.getPathWithKimContext(returnLocation, getActionName());
    }

	@Override
    protected ActionForward returnToSender(HttpServletRequest request, ActionMapping mapping, KualiDocumentFormBase form) {
        ActionForward dest;
        if (form.isReturnToActionList()) {
            String workflowBase = getKualiConfigurationService().getPropertyValueAsString(
                    KRADConstants.WORKFLOW_URL_KEY);
            String actionListUrl = workflowBase + "/ActionList.do";

            dest = new ActionForward(actionListUrl, true);
        } else if (StringUtils.isNotBlank(form.getBackLocation())){
        	dest = new ActionForward(form.getBackLocation(), true);
        } else {
        	dest = mapping.findForward(KRADConstants.MAPPING_PORTAL);
            ActionForward newDest = new ActionForward();
            //why is this being done?
            KimCommonUtilsInternal.copyProperties(newDest, dest);
            newDest.setPath(getApplicationBaseUrl());
            return newDest;
        }

        setupDocumentExit();
        return dest;
    }    

	protected ActionForward getBasePathForward(HttpServletRequest request, ActionForward forward){
		ActionForward newDest = new ActionForward();
        KimCommonUtilsInternal.copyProperties(newDest, forward);
        newDest.setPath(getApplicationBaseUrl());
        return newDest;
    }

    public ActionForward switchToPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementDocumentFormBase idmForm = (IdentityManagementDocumentFormBase) form;
        
        KualiTableRenderFormMetadata memberTableMetadata = idmForm.getMemberTableMetadata();
        memberTableMetadata.jumpToPage(memberTableMetadata.getSwitchToPageNumber(), idmForm.getMemberRows().size(), idmForm.getRecordsPerPage());
        memberTableMetadata.setColumnToSortIndex(memberTableMetadata.getPreviouslySortedColumnIndex());
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    protected void applyPagingAndSortingFromPreviousPageView(IdentityManagementDocumentFormBase idmForm) {
        KualiTableRenderFormMetadata memberTableMetadata = idmForm.getMemberTableMetadata();

        memberTableMetadata.jumpToPage(memberTableMetadata.getViewedPageNumber(), idmForm.getMemberRows().size(), idmForm.getRecordsPerPage());
    }

    protected boolean validateRole( String roleId, Role role, String propertyName, String message){
    	if ( role == null ) {
        	GlobalVariables.getMessageMap().putError(propertyName, RiceKeyConstants.ERROR_INVALID_ROLE, roleId );
    		return false;
    	}
    	KimType typeInfo = KimApiServiceLocator.getKimTypeInfoService().getKimType(role.getKimTypeId());
    	
    	if(KimTypeLookupableHelperServiceImpl.hasDerivedRoleTypeService(typeInfo)){
        	GlobalVariables.getMessageMap().putError(propertyName, RiceKeyConstants.ERROR_CANT_ADD_DERIVED_ROLE, message);
        	return false;
        }
    	return true;
    }
 
    public ActionForward changeNamespace(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return refresh(mapping, form, request, response);
    }

    protected KimTypeService getKimTypeService( KimType typeInfo ) {
		String serviceName = typeInfo.getServiceName();
		if ( StringUtils.isNotBlank(serviceName) ) {
			try {
				KimTypeService service = (KimTypeService) GlobalResourceLoader.getService(QName.valueOf(serviceName));
				if ( service != null && service instanceof RoleTypeService) {
					return service;
				} else {
					return (RoleTypeService) KIMServiceLocatorInternal.getService("kimNoMembersRoleTypeService");
				}
			} catch ( Exception ex ) {
				LOG.error( "Unable to find role type service with name: " + serviceName, ex );
				return (RoleTypeService) KIMServiceLocatorInternal.getService("kimNoMembersRoleTypeService");
			}
		}
		return null;
    }

}
