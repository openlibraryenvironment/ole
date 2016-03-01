package org.kuali.ole.vnd.document.validation.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.vnd.VendorKeyConstants;
import org.kuali.ole.vnd.VendorPropertyConstants;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 7/15/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleVendorRule extends VendorRule {

    @Override
    protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
        boolean valid = processValidation(document);
        VendorDetail vendorDetail =  (VendorDetail)document.getNewMaintainableObject().getDataObject();

        List<VendorContact> vendorContacts = vendorDetail.getVendorContacts();
        for (Iterator<VendorContact> iterator = vendorContacts.iterator(); iterator.hasNext(); ) {
            VendorContact vendorContact = iterator.next();
            vendorDetail.getVendorContactPhoneNumberMap().put(vendorContacts.indexOf(vendorContact),vendorContact.getVendorContactPhoneNumbers());
        }
        List<VendorTransmissionFormatDetail> vendorTransmissionFormatDetailList = vendorDetail.getVendorTransmissionFormat();
        List formatId = new ArrayList();
        if(!vendorDetail.isNonBillable()){
            if(vendorDetail.getPaymentMethodId() == null || vendorDetail.getPaymentMethodId().equals("")){
                putFieldError(VendorPropertyConstants.PAYMENT_METHOD_ID,VendorKeyConstants.PAYMENT_METHOD_ID_REQUIRED);
                valid &= false;
            }
            if(vendorDetail.getCurrencyTypeId() == null || vendorDetail.getCurrencyTypeId().equals("")){
                putFieldError(VendorPropertyConstants.CURRENCY_TYPE,VendorKeyConstants.CURRENCY_TYPE_REQUIRED);
                valid &= false;
            }
            if(vendorDetail.getVendorHeader() != null && (vendorDetail.getVendorHeader().getVendorTypeCode() == null || vendorDetail.getVendorHeader().getVendorTypeCode().equals(""))){
                putFieldError(VendorPropertyConstants.VENDOR_TYPE_CODE,VendorKeyConstants.VENDOR_TYPE_CODE_REQUIRED);
                valid &= false;
            }
            if(vendorDetail.getVendorHeader()!=null && (vendorDetail.getVendorHeader().getVendorOwnershipCode() == null || vendorDetail.getVendorHeader().getVendorOwnershipCode().equals(""))){
                putFieldError(VendorPropertyConstants.VENDOR_OWNERSHIP_CODE,VendorKeyConstants.OWNERSHIP_TYPE_CODE_REQUIRED);
                valid &= false;
            }
        }
        int activePreferredCount = 0;
        for (VendorTransmissionFormatDetail vendorTransmissionFormatDetail : vendorTransmissionFormatDetailList) {
            if (formatId.contains(vendorTransmissionFormatDetail.getVendorTransmissionFormatId())) {
                putFieldError(VendorPropertyConstants.VENDOR_TRANSMISSION_FORMAT, VendorKeyConstants.OLE_VENDOR_DUPLICATE_TRANS_FORMAT);
                valid &= false;
            }
            formatId.add(vendorTransmissionFormatDetail.getVendorTransmissionFormatId());
            if (vendorTransmissionFormatDetail.isVendorPreferredTransmissionFormat() && vendorTransmissionFormatDetail.isActive()) {
                activePreferredCount = activePreferredCount + 1;
            }
        }
        if (activePreferredCount > 1) {
            putFieldError(VendorPropertyConstants.VENDOR_TRANSMISSION_FORMAT, OLEKeyConstants.ERROR_DUPLICATE_PREFERRED_FORMAT, "preferred transmission format");
            valid &= false;
        }
        HashMap vendor = new HashMap();
        if (!vendorDetail.isActiveIndicator()) {
            vendor.put("vendorHeaderGeneratedIdentifier", vendorDetail.getVendorHeaderGeneratedIdentifier());
            vendor.put("vendorDetailAssignedIdentifier", vendorDetail.getVendorDetailAssignedIdentifier());
            List<OlePurchaseOrderDocument> purchaseOrderDocuments = (List<OlePurchaseOrderDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, vendor);
            if (purchaseOrderDocuments.size() > 0) {
                for(OlePurchaseOrderDocument olePurchaseOrderDocument : purchaseOrderDocuments) {
                    if(olePurchaseOrderDocument.getApplicationDocumentStatus().equals(VendorPropertyConstants.OPEN_PO)) {
                        putFieldError(VendorPropertyConstants.VENDOR_ACTIVE_INDICATOR, VendorKeyConstants.OLE_VENDOR_PURCHSEORDER_LINKED);
                    }
                }

            }
        }
        vendor = new HashMap();
        vendor.put("gokbId", vendorDetail.getGokbId());
        List<VendorDetail> vendorDetails = (List<VendorDetail>)KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, vendor);
        if (vendorDetail.getVendorHeaderGeneratedIdentifier() == null && vendorDetail.getVendorDetailAssignedIdentifier() == null) {
            if (vendorDetails.size() > 0) {
                putFieldError(VendorPropertyConstants.VENDOR_GOKB_ID, VendorKeyConstants.OLE_VENDOR_GOKB_EXISTS);
                valid &= false;
            }
        } else {
            if (vendorDetails.size() > 0) {
                for(VendorDetail detail:vendorDetails){
                    if(detail.isActiveIndicator() && ! detail.getVendorName().equalsIgnoreCase( vendorDetail.getVendorName())){
                        putFieldError(VendorPropertyConstants.VENDOR_GOKB_ID, VendorKeyConstants.OLE_VENDOR_GOKB_EXISTS);
                        valid &= false;
                    }
                }

            }
        }
        return valid & super.processCustomRouteDocumentBusinessRules(document);
    }

    private boolean processValidation(MaintenanceDocument document) {
        boolean valid = true;
        VendorDetail vendorDetail = (VendorDetail) document.getNewMaintainableObject().getDataObject();
        valid &= processExternalVendorCode(vendorDetail);

        return valid;
    }

    protected boolean processExternalVendorCode(VendorDetail vendorDetail) {
        boolean valid = true;
        List<VendorAlias> vendorAliases = vendorDetail.getVendorAliases();
        List<String> aliasList=new ArrayList<>();
        if(vendorAliases.size()>0) {
            for(VendorAlias vendorAlias : vendorAliases) {
                if(!aliasList.contains(vendorAlias.getVendorAliasType().getAliasType()))
                {
                    aliasList.add(vendorAlias.getVendorAliasType().getAliasType());
                } else{
                    putFieldError(VendorPropertyConstants.VENDOR_SEARCH_ALIASES, VendorKeyConstants.OLE_VENDOR_DUPLICATE_VARIANT_NAME);
                    valid &= false;
                }
            }
        }
        if (StringUtils.isBlank(vendorDetail.getVendorName())) {
            // At least one of the three vendor name fields must be filled in.

            if (StringUtils.isBlank(vendorDetail.getVendorFirstName()) && StringUtils.isBlank(vendorDetail.getVendorLastName())) {
//
//                GlobalVariables.getMessageMap().putErrorForSectionId("OleCirculationDesk-Locations", OLEConstants.OleCirculationDesk.OLE_VENDOR_DUPLICATE_ALIAS_NAME);
//                return getUIFModelAndView(form);
                putFieldError(VendorPropertyConstants.VENDOR_ALIAS_NAME, VendorKeyConstants.OLE_VENDOR_EMPTY_NAME);
                valid &= false;
            }
            // If either the vendor first name or the vendor last name have been entered, the other must be entered.

        }

        return valid;
    }
}
