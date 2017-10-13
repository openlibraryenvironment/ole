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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.kns.web.struts.form.KualiTableRenderFormMetadata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a description of what this class does - jonathan don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class IdentityManagementBaseInquiryAction extends KualiAction {

	protected UiDocumentService uiDocumentService;

    protected UiDocumentService getUiDocumentService() {
		if ( uiDocumentService == null ) {
			uiDocumentService = KIMServiceLocatorInternal.getUiDocumentService();
		}
		return uiDocumentService;
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krad.web.struts.action.KualiAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IdentityManagementDocumentFormBase idmForm = (IdentityManagementDocumentFormBase) form;
		idmForm.setInquiry(true);

        loadKimObject( request, idmForm );

        KualiTableRenderFormMetadata memberTableMetadata = idmForm.getMemberTableMetadata();
        memberTableMetadata.jumpToPage(memberTableMetadata.getSwitchToPageNumber(), idmForm.getMemberRows().size(), idmForm.getRecordsPerPage());
    	
		return super.execute(mapping, form, request, response);
	}
	
    protected abstract void loadKimObject( HttpServletRequest request, IdentityManagementDocumentFormBase form );
	    
    public ActionForward inquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
 
    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiTableRenderAction#switchToPage(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward switchToPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementDocumentFormBase idmForm = (IdentityManagementDocumentFormBase) form;
        
        KualiTableRenderFormMetadata memberTableMetadata = idmForm.getMemberTableMetadata();
        memberTableMetadata.jumpToPage(memberTableMetadata.getSwitchToPageNumber(), idmForm.getMemberRows().size(), idmForm.getRecordsPerPage());
        memberTableMetadata.setColumnToSortIndex(memberTableMetadata.getPreviouslySortedColumnIndex());
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    

}
