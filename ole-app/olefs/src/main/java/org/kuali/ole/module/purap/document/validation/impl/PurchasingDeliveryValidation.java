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
package org.kuali.ole.module.purap.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapPropertyConstants;
import org.kuali.ole.module.purap.document.PurchasingDocument;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.sys.service.PostalCodeValidationService;
import org.kuali.ole.vnd.businessobject.CampusParameter;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.Collections;

public class PurchasingDeliveryValidation extends PurchasingProcessRequestorPhoneAndEmailAddressValidation {

    protected DateTimeService dateTimeService;
    protected BusinessObjectService businessObjectService;
    protected PostalCodeValidationService postalCodeValidationService;

    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        PurchasingDocument purDocument = (PurchasingDocument) event.getDocument();

        GlobalVariables.getMessageMap().addToErrorPath(PurapConstants.DELIVERY_TAB_ERRORS);        
        /*//perform the validation against phone Number
        if (StringUtils.isNotBlank(purDocument.getRequestorPersonPhoneNumber())) {
            if (!validatePhoneNumber(purDocument.getRequestorPersonPhoneNumber())) {
                valid &= false;
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.REQUESTOR_PERSON_PHONE_NUMBER, PurapKeyConstants.ERROR_INVALID_PH0NE_NUMBER);
            }
        }*/

        //perform the validation against email address
        if (StringUtils.isNotBlank(purDocument.getRequestorPersonEmailAddress())) {
            if (!validateEmailAddress(purDocument.getRequestorPersonEmailAddress())) {
                valid &= false;
                GlobalVariables.getMessageMap().putError(PurapPropertyConstants.REQUESTOR_PERSON_EMAIL_ADDRESS, PurapKeyConstants.ERROR_INVALID_EMAIL_ADDRESS);
            }
        }

        postalCodeValidationService.validateAddress(purDocument.getDeliveryCountryCode(), purDocument.getDeliveryStateCode(), purDocument.getDeliveryPostalCode(), PurapPropertyConstants.DELIVERY_STATE_CODE, PurapPropertyConstants.DELIVERY_POSTAL_CODE);

        int match = businessObjectService.countMatching(CampusParameter.class, Collections.singletonMap("campusCode", purDocument.getDeliveryCampusCode()));
        if (match < 1) {
            valid = false;
            GlobalVariables.getMessageMap().putError(PurapPropertyConstants.DELIVERY_CAMPUS_CODE, PurapKeyConstants.ERROR_DELIVERY_CAMPUS_INVALID);
        }

        GlobalVariables.getMessageMap().clearErrorPath();
        return valid;
    }

    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setPostalCodeValidationService(PostalCodeValidationService postalCodeValidationService) {
        this.postalCodeValidationService = postalCodeValidationService;
    }
}
