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
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase;
import org.kuali.rice.kim.web.struts.form.IdentityManagementGroupDocumentForm;
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
public class IdentityManagementGroupInquiry extends IdentityManagementBaseInquiryAction {
	private static final Logger LOG = Logger.getLogger(IdentityManagementGroupInquiry.class);

    /**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kim.web.struts.action.IdentityManagementBaseInquiryAction#loadKimObject(javax.servlet.http.HttpServletRequest, org.kuali.rice.kim.web.struts.form.IdentityManagementDocumentFormBase)
	 */
	@Override
	protected void loadKimObject(HttpServletRequest request, IdentityManagementDocumentFormBase form) {
        IdentityManagementGroupDocumentForm groupDocumentForm = (IdentityManagementGroupDocumentForm) form;
        String id = request.getParameter(KimConstants.PrimaryKeyConstants.GROUP_ID);
        String altId = request.getParameter(KimConstants.AttributeConstants.GROUP_ID);

        String groupId = StringUtils.isNotEmpty(id) ? id : altId;
        Group group = null;
        if (StringUtils.isNotEmpty(groupId)) {
        	group = KimApiServiceLocator.getGroupService().getGroup(groupId);
        } else {
        	String namespaceCode = request.getParameter(KimConstants.UniqueKeyConstants.NAMESPACE_CODE);
        	String groupName = request.getParameter(KimConstants.UniqueKeyConstants.GROUP_NAME);
        	
        	if (!StringUtils.isBlank(namespaceCode) && !StringUtils.isBlank(groupName)) {
        		group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespaceCode, groupName);
            }
        }
        if (group != null) {
        	getUiDocumentService().loadGroupDoc(groupDocumentForm.getGroupDocument(), group);
        } else {
        	LOG.error("No records found for Group Inquiry.");
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_INQUIRY);
        }
	}


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IdentityManagementGroupDocumentForm groupDocumentForm = (IdentityManagementGroupDocumentForm) form;

        ActionForward forward = super.execute(mapping, form, request, response);

        String previouslySortedColumnName = (String)GlobalVariables.getUserSession().retrieveObject(KimConstants.KimUIConstants.KIM_GROUP_INQUIRY_SORT_PREV_COL_NM);
        Boolean sortDescending = ((Boolean)GlobalVariables.getUserSession().retrieveObject(KimConstants.KimUIConstants.KIM_GROUP_INQUIRY_SORT_DESC_VALUE));

        KualiTableRenderFormMetadata memberTableMetadata =  groupDocumentForm.getMemberTableMetadata();
        memberTableMetadata.setPreviouslySortedColumnName(previouslySortedColumnName);
        if (sortDescending != null) {
            memberTableMetadata.setSortDescending(sortDescending.booleanValue());
        }
        if (groupDocumentForm.getMemberRows() != null) {
            memberTableMetadata.sort(groupDocumentForm.getMemberRows(), groupDocumentForm.getRecordsPerPage());
            memberTableMetadata.jumpToPage(memberTableMetadata.getSwitchToPageNumber(), groupDocumentForm.getMemberRows().size(), groupDocumentForm.getRecordsPerPage());
        }

        GlobalVariables.getUserSession().addObject(KimConstants.KimUIConstants.KIM_GROUP_INQUIRY_SORT_PREV_COL_NM, memberTableMetadata.getPreviouslySortedColumnName());
        GlobalVariables.getUserSession().addObject(KimConstants.KimUIConstants.KIM_GROUP_INQUIRY_SORT_DESC_VALUE, memberTableMetadata.isSortDescending());

        return forward;
    }


    public ActionForward sort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
}
