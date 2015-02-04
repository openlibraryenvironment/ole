/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.pdp.businessobject.lookup;

import java.util.Properties;

import org.kuali.ole.pdp.PdpPropertyConstants;
import org.kuali.ole.pdp.businessobject.PaymentProcess;
import org.kuali.ole.pdp.businessobject.ProcessSummary;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.HtmlData.AnchorHtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

public class PaymentProcessLookupableHelperService extends KualiLookupableHelperServiceImpl {
    
    /**
     * @see org.kuali.rice.kns.lookup.AbstractLookupableHelperServiceImpl#getInquiryUrl(org.kuali.rice.krad.bo.BusinessObject,
     *      java.lang.String)
     */
    @Override
    public HtmlData getInquiryUrl(BusinessObject bo, String propertyName) {
        AnchorHtmlData inquiryUrl = (AnchorHtmlData) super.getInquiryUrl(bo, propertyName);
        PaymentProcess paymentProcess = (PaymentProcess) bo;
        if (propertyName.equalsIgnoreCase(PdpPropertyConstants.PaymentProcess.PAYMENT_PROCESS_ID)) {
            Properties params = new Properties();
            params.put(OLEConstants.DISPATCH_REQUEST_PARAMETER, OLEConstants.SEARCH_METHOD);
            params.put(OLEConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, ProcessSummary.class.getName());
            params.put(KRADConstants.DOC_FORM_KEY, "88888888");
            params.put(OLEConstants.HIDE_LOOKUP_RETURN_LINK, "true");
            params.put(OLEConstants.RETURN_LOCATION_PARAMETER, OLEConstants.MAPPING_PORTAL + ".do");
            params.put(PdpPropertyConstants.ProcessSummary.PROCESS_SUMMARY_PROCESS_ID, UrlFactory.encode(String.valueOf(paymentProcess.getId())));
            String url = UrlFactory.parameterizeUrl(KRADConstants.LOOKUP_ACTION, params);
            inquiryUrl.setHref(url);
        }
        return inquiryUrl;
    }

}
