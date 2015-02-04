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
import org.kuali.ole.select.businessobject.OlePersonRequestor;
import org.kuali.ole.sys.businessobject.inquiry.KfsInquirableImpl;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.*;


/**
 * This class adds in some new sections for {@link Org} inquiries, specifically Org Hierarchy Org Review Hierarchy
 */
public class OlePersonRequestorInquirableImpl extends KfsInquirableImpl {

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
        if (businessObject instanceof OlePersonRequestor && attributeName.equalsIgnoreCase("id")) {
            List<String> primaryKeys = new ArrayList<String>();
            primaryKeys.add("id");
            String href = (getInquiryUrlForPrimaryKeys(OlePersonRequestor.class, businessObject, primaryKeys, null)).getHref();
            href = "kr/" + href;
            AnchorHtmlData htmlData = new AnchorHtmlData();
            htmlData.setHref(href);
            return htmlData;
        }

        return super.getInquiryUrl(businessObject, attributeName, forceInquiry);
    }

    @Override
    public AnchorHtmlData getInquiryUrlForPrimaryKeys(
            Class clazz, Object businessObject, List<String> primaryKeys, String displayText) {
        Map<String, String> fieldList = new HashMap<String, String>();
        if (businessObject == null) {
            return new AnchorHtmlData(KRADConstants.EMPTY_STRING, KRADConstants.EMPTY_STRING);
        }

        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.START_METHOD);
        OlePersonRequestor olePersonRequestor = (OlePersonRequestor) businessObject;
        if (olePersonRequestor.getInternalRequestorId() != null) {
            parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, "org.kuali.rice.kim.api.identity.Person");
            parameters.put("principalId", olePersonRequestor.getInternalRequestorId());
            fieldList.put("principalId", olePersonRequestor.getInternalRequestorId());
        } else if (olePersonRequestor.getExternalRequestorId() != null) {
            parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, "org.kuali.ole.select.businessobject.OleRequestor");
            parameters.put("requestorId", olePersonRequestor.getExternalRequestorId());
            fieldList.put("requestorId", olePersonRequestor.getExternalRequestorId());
        } else {
            parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, clazz.getClass());
            String titleAttributeValue;
            for (String primaryKey : primaryKeys) {
                titleAttributeValue = ObjectUtils.getPropertyValue(businessObject, primaryKey).toString();
                parameters.put(primaryKey, titleAttributeValue);
                fieldList.put(primaryKey, titleAttributeValue);
            }
        }
        if (StringUtils.isEmpty(displayText)) {
            return getHyperLink(clazz, fieldList, UrlFactory.parameterizeUrl(KRADConstants.INQUIRY_ACTION, parameters));
        } else {
            return getHyperLink(clazz, fieldList, UrlFactory.parameterizeUrl(KRADConstants.INQUIRY_ACTION, parameters), displayText);
        }
    }

}
