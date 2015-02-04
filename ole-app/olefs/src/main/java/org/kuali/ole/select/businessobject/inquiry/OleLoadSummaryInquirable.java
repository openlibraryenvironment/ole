/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject.inquiry;


import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.businessobject.OleLoadSumRecords;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.inquiry.KfsInquirableImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.services.IdentityManagementService;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.*;


/**
 * This class adds in some new sections for {@link Org} inquiries, specifically Org Hierarchy Org Review Hierarchy
 */
public class OleLoadSummaryInquirable extends KfsInquirableImpl {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLoadSummaryInquirable.class);

    /**
     * Overrides the helper method to build an inquiry url for a result field.
     * For the Vendor URL field, returns the url address as the inquiry URL, so that Vendor URL functions as a hyperlink.
     *
     * @param bo           the business object instance to build the urls for
     * @param propertyName the property which links to an inquirable
     * @return String url to inquiry
     */
    @Override
    public HtmlData getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {

        boolean hasPermission = false;
        String documentTypeName = OLEConstants.OleLoadSummary.LOAD_SUMMARY;
        String nameSpaceCode = OLEConstants.OleLoadSummary.LOAD_SUMMARY_NAMESPACE;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside getInquiryUrl documentTypeName   >>>>>>>>>>>>>>>>>" + documentTypeName);
            LOG.debug("Inside getInquiryUrl nameSpaceCode  >>>>>>>>>>>>>>>>>" + nameSpaceCode);
        }
        hasPermission = SpringContext.getBean(IdentityManagementService.class).hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), nameSpaceCode,
                OLEConstants.OleLoadSummary.CAN_SEARCH_LOAD_SUMMARY);

        if (!hasPermission) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Inside getInquiryUrl hasPermission   if>>>>>>>>>>>>>>>>>" + hasPermission);
            }
            throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPerson().getPrincipalName(), " to edit reuisition document ", "dfsf");
        } else {
            if (businessObject instanceof OleLoadSumRecords && attributeName.equalsIgnoreCase("documentNumber")) {
                List<String> primaryKeys = new ArrayList<String>();
                primaryKeys.add("documentNumber");
                String href = (getInquiryUrlForPrimaryKeys(OleLoadSumRecords.class, businessObject, primaryKeys, null)).getHref();
                AnchorHtmlData htmlData = new AnchorHtmlData();
                htmlData.setHref(href);
                return htmlData;
            }

            return super.getInquiryUrl(businessObject, attributeName, forceInquiry);
        }
    }

    @Override
    public AnchorHtmlData getInquiryUrlForPrimaryKeys(
            Class clazz, Object businessObject, List<String> primaryKeys, String displayText) {
        if (businessObject == null) {
            return new AnchorHtmlData(KRADConstants.EMPTY_STRING, KRADConstants.EMPTY_STRING);
        }

        OleLoadSumRecords oleLoadSumRecords = (OleLoadSumRecords) businessObject;
        Properties parameters = new Properties();
        Map<String, String> fieldList = new HashMap<String, String>();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, "docHandler");
        parameters.put(OLEConstants.DOCUMENT_TYPE_NAME, "OLE_ACQBTHUPLOAD");
        parameters.put(OLEConstants.PARAMETER_COMMAND, "displayDocSearchView");
        parameters.put("docId", oleLoadSumRecords.getDocumentNumber().toString());
        fieldList.put("docId", oleLoadSumRecords.getDocumentNumber().toString());
        if (StringUtils.isEmpty(displayText)) {
            return getHyperLink(clazz, fieldList, UrlFactory.parameterizeUrl(OLEConstants.BATCH_UPLOAD_ACTION_PATH, parameters));
        } else {
            return getHyperLink(clazz, fieldList, UrlFactory.parameterizeUrl(OLEConstants.BATCH_UPLOAD_ACTION_PATH, parameters), displayText);
        }
    }

}
