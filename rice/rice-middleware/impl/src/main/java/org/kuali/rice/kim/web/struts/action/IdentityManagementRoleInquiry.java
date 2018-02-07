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
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase;
import org.kuali.rice.kim.web.struts.form.IdentityManagementRoleDocumentForm;
import org.kuali.rice.kns.web.struts.form.KualiTableRenderFormMetadata;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a description of what this class does - jonathan don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityManagementRoleInquiry extends IdentityManagementBaseInquiryAction {
	private static final Logger LOG = Logger.getLogger(IdentityManagementRoleInquiry.class);	
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.web.struts.action.IdentityManagementBaseInquiryAction#loadKimObject(javax.servlet.http.HttpServletRequest, org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase)
	 */
	@Override
	protected void loadKimObject(HttpServletRequest request,
			IdentityManagementDocumentFormBase form) {
        IdentityManagementRoleDocumentForm roleDocumentForm = (IdentityManagementRoleDocumentForm) form;
        String id = request.getParameter(KimConstants.PrimaryKeyConstants.ROLE_ID);
        String altId =  request.getParameter(KimConstants.PrimaryKeyConstants.SUB_ROLE_ID);

        String roleId = StringUtils.isNotEmpty(id) ? id : altId;
        Role role = KimApiServiceLocator.getRoleService().getRole(roleId);
        if (role != null) {
        	getUiDocumentService().loadRoleDoc(roleDocumentForm.getRoleDocument(), role);
        } else {
        	LOG.error("No records found for Role Inquiry.");
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_INQUIRY);
        }
	}

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IdentityManagementRoleDocumentForm roleDocumentForm = (IdentityManagementRoleDocumentForm) form;

        ActionForward forward = super.execute(mapping, form, request, response);

        String previouslySortedColumnName = (String)GlobalVariables.getUserSession().retrieveObject(KimConstants.KimUIConstants.KIM_ROLE_INQUIRY_SORT_PREV_COL_NM);
        Boolean sortDescending = ((Boolean)GlobalVariables.getUserSession().retrieveObject(KimConstants.KimUIConstants.KIM_ROLE_INQUIRY_SORT_DESC_VALUE));

        KualiTableRenderFormMetadata memberTableMetadata =  roleDocumentForm.getMemberTableMetadata();
        memberTableMetadata.setPreviouslySortedColumnName(previouslySortedColumnName);
        String columnToSort = memberTableMetadata.getColumnToSortName();
        if  (previouslySortedColumnName != null && StringUtils.isEmpty(columnToSort)) {
            memberTableMetadata.setColumnToSortName(previouslySortedColumnName);
        }
        if (sortDescending != null && !StringUtils.isEmpty(columnToSort)) {
            memberTableMetadata.setSortDescending(sortDescending.booleanValue());
        }
        if (roleDocumentForm.getMemberRows() != null) {
            memberTableMetadata.sort(roleDocumentForm.getMemberRows(), roleDocumentForm.getRecordsPerPage());
            memberTableMetadata.jumpToPage(memberTableMetadata.getSwitchToPageNumber(), roleDocumentForm.getMemberRows().size(), roleDocumentForm.getRecordsPerPage());
        }

        GlobalVariables.getUserSession().addObject(KimConstants.KimUIConstants.KIM_ROLE_INQUIRY_SORT_PREV_COL_NM, memberTableMetadata.getPreviouslySortedColumnName());
        GlobalVariables.getUserSession().addObject(KimConstants.KimUIConstants.KIM_ROLE_INQUIRY_SORT_DESC_VALUE, memberTableMetadata.isSortDescending());

        return forward;
    }

    public ActionForward sort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementRoleDocumentForm roleDocumentForm = (IdentityManagementRoleDocumentForm) form;
        String memberSearchValue = roleDocumentForm.getMemberSearchValue();
        if (memberSearchValue != null && !memberSearchValue.isEmpty()) {
            memberSearchValue = memberSearchValue.replaceAll("[%*]","");
            getUiDocumentService().loadRoleMembersBasedOnSearch(roleDocumentForm.getRoleDocument(), memberSearchValue);
        } else {
            clear(mapping, form, request, response);
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementRoleDocumentForm roleDocumentForm = (IdentityManagementRoleDocumentForm) form;
        roleDocumentForm.setMemberSearchValue("");
        getUiDocumentService().clearRestrictedRoleMembersSearchResults(roleDocumentForm.getRoleDocument());

        KualiTableRenderFormMetadata memberTableMetadata = roleDocumentForm.getMemberTableMetadata();
        if (roleDocumentForm.getMemberRows() != null) {
            memberTableMetadata.jumpToFirstPage(roleDocumentForm.getMemberRows().size(), roleDocumentForm.getRecordsPerPage());
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
}
