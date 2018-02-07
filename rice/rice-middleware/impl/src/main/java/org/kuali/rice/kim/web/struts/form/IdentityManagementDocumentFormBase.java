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
package org.kuali.rice.kim.web.struts.form;

import org.apache.log4j.Logger;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kns.util.PagingBannerUtils;
import org.kuali.rice.kns.web.struts.form.KualiTableRenderFormMetadata;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@SuppressWarnings("serial")
public abstract class IdentityManagementDocumentFormBase extends KualiTransactionalDocumentFormBase {
	protected static final Logger LOG = Logger.getLogger(IdentityManagementDocumentFormBase.class);
    protected static final String MAX_MEMBERS_PER_PAGE_PARM = "MAX_MEMBERS_PER_PAGE";
	protected transient KualiTableRenderFormMetadata memberTableMetadata;
    protected int recordsPerPage = -1;
    protected boolean inquiry = false;
    
	protected static final String CHANGE_NAMESPACE_METHOD_TO_CALL = "methodToCall.changeNamespace";
	protected static final String CHANGE_MEMBER_TYPE_CODE_METHOD_TO_CALL = "methodToCall.changeMemberTypeCode";
	protected static final String CHANGE_DEL_ROLE_MEMBER_METHOD_TO_CALL = "methodToCall.changeDelegationRoleMember";

    /**
     * @see org.kuali.rice.krad.web.struts.form.KualiDocumentFormBase#populate(javax.servlet.http.HttpServletRequest)
     */
    @SuppressWarnings("unchecked")
	@Override
    public void populate(HttpServletRequest request) {
        super.populate(request);

        if (memberTableMetadata == null)
        	memberTableMetadata = new KualiTableRenderFormMetadata();

        if (KRADConstants.TableRenderConstants.SWITCH_TO_PAGE_METHOD.equals(getMethodToCall())) {
            final String paramPrefix = KRADConstants.DISPATCH_REQUEST_PARAMETER + "." + KRADConstants.TableRenderConstants.SWITCH_TO_PAGE_METHOD + ".";
            memberTableMetadata.setSwitchToPageNumber(
                    PagingBannerUtils.getNumbericalValueAfterPrefix(paramPrefix, request.getParameterNames()));
            if (memberTableMetadata.getSwitchToPageNumber() == -1) {
                throw new RuntimeException("Couldn't find page number");
            }
        } else if (KRADConstants.TableRenderConstants.SORT_METHOD.equals(getMethodToCall())) {
            final String paramPrefix = KRADConstants.DISPATCH_REQUEST_PARAMETER + "." + KRADConstants.TableRenderConstants.SORT_METHOD + ".";
            memberTableMetadata.setColumnToSortIndex(PagingBannerUtils.getNumbericalValueAfterPrefix(paramPrefix, request.getParameterNames()));
            if (memberTableMetadata.getColumnToSortIndex() == -1) {
                memberTableMetadata.setColumnToSortName(PagingBannerUtils.getStringValueAfterPrefix(paramPrefix, request.getParameterNames()));
            }
        }
    }

	public KualiTableRenderFormMetadata getMemberTableMetadata() {
		return this.memberTableMetadata;
	}

	public void setMemberTableMetadata(
			KualiTableRenderFormMetadata memberTableMetadata) {
		this.memberTableMetadata = memberTableMetadata;
	}

	public int getRecordsPerPage() {
		if ( recordsPerPage == -1 ) {
			Parameter param = CoreFrameworkServiceLocator.getParameterService().getParameter(KimConstants.NAMESPACE_CODE, KRADConstants.DetailTypes.DOCUMENT_DETAIL_TYPE, MAX_MEMBERS_PER_PAGE_PARM);
			if ( param != null ) {
				try {
					recordsPerPage = Integer.parseInt( param.getValue() );
				} catch ( NumberFormatException ex ) {
					LOG.error( "Unable to parse parameter " + KimConstants.NAMESPACE_CODE+"/"+ KRADConstants.DetailTypes.DOCUMENT_DETAIL_TYPE+"/"+MAX_MEMBERS_PER_PAGE_PARM + "(+"+param.getValue()+") as an int - defaulting to 1." );
					recordsPerPage = 1;
				}
			} else {
				LOG.error( "Unable to find " + KimConstants.NAMESPACE_CODE+"/"+ KRADConstants.DetailTypes.DOCUMENT_DETAIL_TYPE+"/"+MAX_MEMBERS_PER_PAGE_PARM + " - defaulting to 1." );
				recordsPerPage = 1;
			}
		}
		return recordsPerPage;
	}

	// support for the inquiryControls tag since using same form
	public boolean isCanExport() {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List getMemberRows() {
		return new ArrayList();
	}

	/**
	 * @return the inquiry
	 */
	public boolean isInquiry() {
		return this.inquiry;
	}

	/**
	 * @param inquiry the inquiry to set
	 */
	public void setInquiry(boolean inquiry) {
		this.inquiry = inquiry;
	}
}
