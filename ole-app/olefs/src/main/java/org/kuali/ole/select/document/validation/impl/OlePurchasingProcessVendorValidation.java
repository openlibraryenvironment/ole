/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapRuleConstants;
import org.kuali.ole.module.purap.document.PurchasingDocument;
import org.kuali.ole.module.purap.document.PurchasingDocumentBase;
import org.kuali.ole.module.purap.document.validation.impl.PurchasingProcessVendorValidation;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.service.OleForiegnVendorPhoneNumberService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.ole.sys.service.impl.OleParameterConstants;
import org.kuali.ole.vnd.VendorKeyConstants;
import org.kuali.ole.vnd.VendorPropertyConstants;
import org.kuali.ole.vnd.businessobject.VendorAddress;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.businessobject.VendorHeader;
import org.kuali.ole.vnd.document.service.VendorService;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.datadictionary.validation.fieldlevel.PhoneNumberValidationPattern;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class OlePurchasingProcessVendorValidation extends PurchasingProcessVendorValidation {

    private VendorService vendorService;
    private ParameterService parameterService;
    private boolean currencyTypeIndicator = true;
    /**
     * @see org.kuali.ole.module.purap.document.validation.impl.PurchasingProcessVendorValidation#validate(org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent)
     */
    @Override
    public boolean validate(AttributedDocumentEvent event) {
        boolean valid = true;
        PurchasingDocument purDocument = (PurchasingDocument) event.getDocument();
        MessageMap errorMap = GlobalVariables.getMessageMap();
        errorMap.clearErrorPath();
        errorMap.addToErrorPath(PurapConstants.VENDOR_ERRORS);
        VendorDetail vendorDetail = vendorService.getVendorDetail(purDocument.getVendorHeaderGeneratedIdentifier(), purDocument.getVendorDetailAssignedIdentifier());
        if (ObjectUtils.isNull(vendorDetail)) {
            return valid;
        }
        VendorHeader vendorHeader = vendorDetail.getVendorHeader();

        // make sure that the vendor is not debarred
        /*if (vendorDetail.isVendorDebarred()) {
            valid &= false;
            errorMap.putError(VendorPropertyConstants.VENDOR_NAME, PurapKeyConstants.ERROR_DEBARRED_VENDOR);
        }*/

        if (vendorDetail.isVendorDebarred()) {
            if (parameterService.getParameterValueAsBoolean(OLEConstants.OptionalModuleNamespaces.PURCHASING_ACCOUNTS_PAYABLE, "Requisition", PurapParameterConstants.SHOW_DEBARRED_VENDOR_WARNING_IND)) {
                if (StringUtils.isEmpty(((PurchasingDocumentBase) purDocument).getJustification())) {
                    errorMap.putWarning(VendorPropertyConstants.VENDOR_NAME, PurapKeyConstants.WARNING_DEBARRED_VENDOR, vendorDetail.getVendorName());
                    valid &= false;
                }
            } else {
                errorMap.putError(VendorPropertyConstants.VENDOR_NAME, PurapKeyConstants.ERROR_DEBARRED_VENDOR);
                valid &= false;
            }
        }

        // make sure that the vendor is of allowed type
        List<String> allowedVendorTypes = new ArrayList<String>(parameterService.getParameterValuesAsString(OleParameterConstants.PURCHASING_DOCUMENT.class, PurapRuleConstants.PURAP_VENDOR_TYPE_ALLOWED_ON_REQ_AND_PO));
        if (allowedVendorTypes != null && !allowedVendorTypes.isEmpty()) {
            if (ObjectUtils.isNotNull(vendorHeader) && ObjectUtils.isNotNull(vendorHeader.getVendorTypeCode()) && !allowedVendorTypes.contains(vendorHeader.getVendorTypeCode())) {
                valid &= false;
                errorMap.putError(VendorPropertyConstants.VENDOR_NAME, PurapKeyConstants.ERROR_INVALID_VENDOR_TYPE);
            }
        }

        if (purDocument.getRequisitionSourceCode() != null && !purDocument.getRequisitionSourceCode().equals(PurapConstants.RequisitionSources.B2B)) {

            //If there is a vendor and the transmission method is FAX and the fax number is blank, display
            //error that the fax number is required.
            if (purDocument.getVendorHeaderGeneratedIdentifier() != null && purDocument.getPurchaseOrderTransmissionMethodCode().equals(PurapConstants.POTransmissionMethods.FAX) && StringUtils.isBlank(purDocument.getVendorFaxNumber())) {
                valid &= false;
                String attributeLabel = SpringContext.getBean(DataDictionaryService.class).
                        getDataDictionary().getBusinessObjectEntry(VendorAddress.class.getName()).
                        getAttributeDefinition(VendorPropertyConstants.VENDOR_FAX_NUMBER).getLabel();
                errorMap.putError(VendorPropertyConstants.VENDOR_FAX_NUMBER, OLEKeyConstants.ERROR_REQUIRED, attributeLabel);
            }
            //boolean foriegnVendor = vendorHeader.getVendorForeignIndicator();
            if (vendorDetail.getCurrencyType()!=null){
                if(vendorDetail.getCurrencyType().getCurrencyType().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                    currencyTypeIndicator=true;
                }
                else{
                    currencyTypeIndicator=false;
                }
            }
            if (StringUtils.isNotBlank(purDocument.getVendorFaxNumber())) {
                PhoneNumberValidationPattern phonePattern = new PhoneNumberValidationPattern();
                if (!currencyTypeIndicator) {
                    if (StringUtils.isNotEmpty(purDocument.getVendorFaxNumber()) && !SpringContext.getBean(OleForiegnVendorPhoneNumberService.class).isValidForiegnVendorPhoneNumber(purDocument.getVendorFaxNumber())) {
                        GlobalVariables.getMessageMap().putError(VendorPropertyConstants.VENDOR_FAX_NUMBER, VendorKeyConstants.ERROR_FORIEGN_VENDOR_FAX_NUMBER);
                        valid &= false;
                    }
                } else {
                    if (!phonePattern.matches(purDocument.getVendorFaxNumber())) {
                        valid &= false;
                        errorMap.putError(VendorPropertyConstants.VENDOR_FAX_NUMBER, PurapKeyConstants.ERROR_FAX_NUMBER_INVALID);
                    }
                }
            }
        }

        // make sure that the vendor is active
        if (!vendorDetail.isActiveIndicator()) {
            valid &= false;
            errorMap.putError(VendorPropertyConstants.VENDOR_NAME, PurapKeyConstants.ERROR_INACTIVE_VENDOR);
        }

        errorMap.clearErrorPath();
        return valid;

    }

    /**
     * Gets the vendorService attribute.
     *
     * @return Returns the vendorService.
     */
    @Override
    public VendorService getVendorService() {
        return vendorService;
    }

    /**
     * Sets the vendorService attribute value.
     *
     * @param vendorService The vendorService to set.
     */
    @Override
    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    /**
     * Gets the parameterService attribute.
     *
     * @return Returns the parameterService.
     */
    @Override
    public ParameterService getParameterService() {
        return parameterService;
    }

    /**
     * Sets the parameterService attribute value.
     *
     * @param parameterService The parameterService to set.
     */
    @Override
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }


}
