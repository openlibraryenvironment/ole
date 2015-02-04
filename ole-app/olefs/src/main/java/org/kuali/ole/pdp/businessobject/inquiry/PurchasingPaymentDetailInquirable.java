/*
 * Copyright 2009 The Kuali Foundation
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
package org.kuali.ole.pdp.businessobject.inquiry;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.kuali.ole.pdp.PdpPropertyConstants;
import org.kuali.ole.pdp.businessobject.PaymentDetail;
import org.kuali.ole.pdp.businessobject.PurchasingPaymentDetail;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

public class PurchasingPaymentDetailInquirable extends PaymentDetailInquirable {

    /**
     * @see org.kuali.ole.pdp.businessobject.inquiry.PaymentDetailInquirable#getInquiryUrl(org.kuali.rice.krad.bo.BusinessObject, java.lang.String, boolean)
     */
    @Override
    public HtmlData getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {
        PaymentDetail paymentDetail = (PaymentDetail) businessObject;
        if (PdpPropertyConstants.PaymentDetail.PAYMENT_DETAIL_NUMBER_OF_PAYMENTS_IN_PAYMENT_GROUP.equals(attributeName) && ObjectUtils.isNotNull(paymentDetail.getPaymentGroupId())) {

            Properties params = new Properties();
            params.put(OLEConstants.DISPATCH_REQUEST_PARAMETER, OLEConstants.SEARCH_METHOD);
            params.put(OLEConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, PurchasingPaymentDetail.class.getName());
            params.put(KRADConstants.DOC_FORM_KEY, "88888888");
            params.put(OLEConstants.HIDE_LOOKUP_RETURN_LINK, "true");
            params.put(OLEConstants.BACK_LOCATION, SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(KRADConstants.APPLICATION_URL_KEY) + "/" + OLEConstants.MAPPING_PORTAL + ".do");
            params.put(OLEConstants.LOOKUP_READ_ONLY_FIELDS, PdpPropertyConstants.PaymentDetail.PAYMENT_DETAIL_PAYMENT_GROUP_ID);
            params.put(PdpPropertyConstants.PaymentDetail.PAYMENT_DETAIL_PAYMENT_GROUP_ID, UrlFactory.encode(String.valueOf(paymentDetail.getPaymentGroupId())));
            String url = UrlFactory.parameterizeUrl(KRADConstants.LOOKUP_ACTION, params);

            Map<String, String> fieldList = new HashMap<String, String>();
            fieldList.put(PdpPropertyConstants.PaymentDetail.PAYMENT_DETAIL_PAYMENT_GROUP_ID, paymentDetail.getPaymentGroupId().toString());

            return getHyperLink(PaymentDetail.class, fieldList, url);
        }

        if (PdpPropertyConstants.PaymentDetail.PAYMENT_DETAIL_NUMBER_OF_PAYMENTS_IN_DISBURSEMENT.equals(attributeName) && ObjectUtils.isNotNull(paymentDetail.getPaymentGroup().getDisbursementNbr())) {

            Properties params = new Properties();
            params.put(OLEConstants.DISPATCH_REQUEST_PARAMETER, OLEConstants.SEARCH_METHOD);
            params.put(OLEConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, PurchasingPaymentDetail.class.getName());
            params.put(KRADConstants.DOC_FORM_KEY, "88888888");
            params.put(OLEConstants.HIDE_LOOKUP_RETURN_LINK, "true");
            params.put(OLEConstants.BACK_LOCATION, SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(KRADConstants.APPLICATION_URL_KEY) + "/" + OLEConstants.MAPPING_PORTAL + ".do");
            params.put(OLEConstants.LOOKUP_READ_ONLY_FIELDS, PdpPropertyConstants.PaymentDetail.PAYMENT_DISBURSEMENT_NUMBER);
            params.put(PdpPropertyConstants.PaymentDetail.PAYMENT_DISBURSEMENT_NUMBER, UrlFactory.encode(String.valueOf(paymentDetail.getPaymentGroup().getDisbursementNbr())));
            String url = UrlFactory.parameterizeUrl(KRADConstants.LOOKUP_ACTION, params);

            Map<String, String> fieldList = new HashMap<String, String>();
            fieldList.put(PdpPropertyConstants.PaymentDetail.PAYMENT_DISBURSEMENT_NUMBER, paymentDetail.getPaymentGroup().getDisbursementNbr().toString());

            return getHyperLink(PaymentDetail.class, fieldList, url);
        }

        return super.getInquiryUrl(businessObject, attributeName, forceInquiry);
    }

}
