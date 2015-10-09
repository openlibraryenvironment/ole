package org.kuali.ole.batch.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMappingOptionsBo;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 3/21/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleBatchProcessProfileRule extends MaintenanceDocumentRuleBase {

    private boolean isCurrencyTypeAvailable = false;

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEBatchProcessProfileBo oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) document.getNewMaintainableObject().getDataObject();
        if(oleBatchProcessProfileBo.getBatchProcessProfileType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT) || oleBatchProcessProfileBo.getBatchProcessProfileType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.INVOICE_RECORD_IMPORT)){
            if(oleBatchProcessProfileBo.getBibImportProfileForOrderRecord() == null){
                GlobalVariables.getMessageMap().putError(OLEConstants.OLEBatchProcess.BIB_IMPORT_PROFILE, OLEKeyConstants.ERROR_REQUIRED, "Bib Import Profile");
            }
        }
        if(oleBatchProcessProfileBo.getBatchProcessProfileType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORDER_RECORD_IMPORT)) {
            isValid &= validateRequiredFieldsForOrderRecordImport(oleBatchProcessProfileBo);
        }
        if(oleBatchProcessProfileBo.getBatchProcessProfileType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.INVOICE_RECORD_IMPORT)) {
            isValid &= validateRequiredFieldsForInvoiceImport(oleBatchProcessProfileBo);
        }
        return isValid;
    }

    /**
     * This method  validates duplicate Agreement Method Id and return boolean value.
     *
     * @param
     * @return boolean
     */
    private boolean validateRequiredFieldsForOrderRecordImport(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        Set<String> availableFields = new HashSet<>();
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            for (int dataMapCount = 0;dataMapCount<oleBatchProcessProfileDataMappingOptionsBoList.size();dataMapCount++) {
                if(OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEEResourceRecord.FUND_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.COST_SOURCE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.BUILDING_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.ORG_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.CHART_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.FUNDING_SOURCE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
            }
        }
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
            for(int constantCount = 0;constantCount < oleBatchProcessProfileConstantsBoList.size();constantCount++ )  {
                if(OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEEResourceRecord.FUND_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.COST_SOURCE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.BUILDING_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.ORG_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.CHART_CODE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.FUNDING_SOURCE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
            }
        String requiredFields = null;
        requiredFields = checkForAllMandatoryFieldsForOrderImport(oleBatchProcessProfileBo,availableFields,requiredFields);
        if(requiredFields == null){
            return true;
        }
        else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OLEBatchProcess.REQUIRED_VALUES_FOR_ORDER_RECORD_IMPORT,requiredFields);
        }
        return false;
    }

    private String checkForAllMandatoryFieldsForOrderImport(OLEBatchProcessProfileBo oleBatchProcessProfileBo,Set<String> availableFields,String requiredFields){
        List<String> mandatoryFields = new ArrayList<>();
        if(oleBatchProcessProfileBo.getMarcOnly()){
            mandatoryFields.add(OLEConstants.OLEBatchProcess.LIST_PRICE);
            mandatoryFields.add(OLEConstants.OLEBatchProcess.VENDOR_NUMBER);
            mandatoryFields.add(OLEConstants.OLEBatchProcess.QUANTITY);
            mandatoryFields.add(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS);
            if (!availableFields.contains(OLEConstants.OLEEResourceRecord.FUND_CODE)) {
                mandatoryFields.add(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER);
                mandatoryFields.add(OLEConstants.OLEBatchProcess.OBJECT_CODE);
                mandatoryFields.add(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE);
            }
        }
        mandatoryFields.add(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.COST_SOURCE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.BUILDING_CODE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.ORG_CODE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.CHART_CODE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.FUNDING_SOURCE);
        mandatoryFields.removeAll(availableFields);
        if(mandatoryFields == null || mandatoryFields.size() == 0){
            return requiredFields;
        }
        else {
            requiredFields = addRequiredFieldsMessageForOrderImport(mandatoryFields);
            return requiredFields;
        }
    }

    private String addRequiredFieldsMessageForOrderImport(List<String> mandatoryFields){
        StringBuffer fieldsRequired = addFundCodeOrAccountingInfoInRequiredList(mandatoryFields);
        for(int fieldCount = 0; fieldCount < mandatoryFields.size();fieldCount++){
            if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.LIST_PRICE)){
                fieldsRequired.append(OLEConstants.REQUIRED_LIST_PRICE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_NUMBER)){
                fieldsRequired.append(OLEConstants.REQUIRED_VENDOR_NUMBER);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.QUANTITY)){
                fieldsRequired.append(OLEConstants.REQUIRED_QTY);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS)){
                fieldsRequired.append(OLEConstants.REQUIRED_NO_OF_PARTS);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.COST_SOURCE)){
                fieldsRequired.append(OLEConstants.REQUIRED_COST_SOURCE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION)){
                fieldsRequired.append(OLEConstants.REQUIRED_METHOD_OF_PO_TRANSMISSION);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.BUILDING_CODE)){
                fieldsRequired.append(OLEConstants.REQUIRED_BUILDING_CODE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER)){
                fieldsRequired.append(OLEConstants.REQUIRED_DELIVERY_BUILDING_ROOM_NUMBER);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE)){
                fieldsRequired.append(OLEConstants.REQUIRED_DELIVERY_CAMPUS_CODE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.ORG_CODE)){
                fieldsRequired.append(OLEConstants.REQUIRED_ORG_CODE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.CHART_CODE)){
                fieldsRequired.append(OLEConstants.REQUIRED_CHART_CODE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.FUNDING_SOURCE)){
                fieldsRequired.append(OLEConstants.REQUIRED_FUNDING_SOURCE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION)){
                fieldsRequired.append(OLEConstants.REQUIRED_DEFAULT_LOCATION);
            }
            if(!(fieldCount  == mandatoryFields.size()-1)){
                fieldsRequired.append(", ");
            }
        }
        fieldsRequired.append(mandatoryFields.size() == 1?" is":" are");
        return fieldsRequired.toString();
    }

    private StringBuffer addFundCodeOrAccountingInfoInRequiredList(List<String> mandatoryFields) {
        StringBuffer fieldsRequired = new StringBuffer();
        fieldsRequired.append("(");
        if (mandatoryFields.contains(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER)) {
            fieldsRequired.append(OLEConstants.REQUIRED_ACCOUNT_NUMBER);
            fieldsRequired.append(", ");
        }
        if (mandatoryFields.contains(OLEConstants.OLEBatchProcess.OBJECT_CODE)) {
            fieldsRequired.append(OLEConstants.REQUIRED_OBJECT_CODE);
            fieldsRequired.append(", ");
        }
        if (mandatoryFields.contains(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE)) {
            fieldsRequired.append(OLEConstants.REQUIRED_ITEM_CHART_CD);
            fieldsRequired.append(", ");
        }
        if (fieldsRequired.length() > 1) {
            fieldsRequired.deleteCharAt(fieldsRequired.length()-2);
            fieldsRequired.append(") / ");
            fieldsRequired.append(OLEConstants.OLEEResourceRecord.FUND_CODE);
            fieldsRequired.append(", ");
            return fieldsRequired;
        }
        return new StringBuffer();
    }


    private boolean validateRequiredFieldsForInvoiceImport(OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        Set<String> availableFields = new HashSet<>();
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            for (int dataMapCount = 0;dataMapCount<oleBatchProcessProfileDataMappingOptionsBoList.size();dataMapCount++) {
                if(OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.INVOICE_DATE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
                else if(OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())){
                    availableFields.add(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField());
                }
            }
        }
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
            for(int constantCount = 0;constantCount < oleBatchProcessProfileConstantsBoList.size();constantCount++ )  {
                if(OLEConstants.OLEBatchProcess.LIST_PRICE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())) {
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.INVOICE_DATE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    availableFields.add(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName());
                }
                else if(OLEConstants.OLEBatchProcess.CURRENCY_TYPE.equals(oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeName())){
                    if(!oleBatchProcessProfileConstantsBoList.get(constantCount).getAttributeValue().equalsIgnoreCase(OleSelectConstant.CURRENCY_TYPE_NAME)){
                        isCurrencyTypeAvailable = true;
                    }
                }
            }
        String requiredFields = null;
        requiredFields = checkForAllMandatoryFieldsForInvoiceImport(availableFields,requiredFields);
        if(requiredFields == null){
            return true;
        }
        else {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS,OLEConstants.OLEBatchProcess.REQUIRED_VALUES_FOR_ORDER_RECORD_IMPORT,requiredFields);
        }
        return false;
    }



    private String checkForAllMandatoryFieldsForInvoiceImport(Set<String> availableFields,String requiredFields){
        List<String> mandatoryFields = new ArrayList<>();
        mandatoryFields.add(OLEConstants.OLEBatchProcess.LIST_PRICE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.QUANTITY);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.INVOICE_DATE);
        mandatoryFields.add(OLEConstants.OLEBatchProcess.VENDOR_NUMBER);
        if(isCurrencyTypeAvailable){
            mandatoryFields.add(OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE);
            mandatoryFields.remove(OLEConstants.OLEBatchProcess.LIST_PRICE);
        }
        mandatoryFields.removeAll(availableFields);
        if(mandatoryFields == null || mandatoryFields.size() == 0){
            return requiredFields;
        }
        else {
            requiredFields = addRequiredFieldsMessageForInvoiceImport(mandatoryFields);
            return requiredFields;
        }
    }

    private String addRequiredFieldsMessageForInvoiceImport(List<String> mandatoryFields){
        StringBuffer fieldsRequired = new StringBuffer();
        for(int fieldCount = 0; fieldCount < mandatoryFields.size();fieldCount++){
            if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.LIST_PRICE)){
                fieldsRequired.append(OLEConstants.REQUIRED_INVOICED_PRICE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.FOREIGN_LIST_PRICE)){
                fieldsRequired.append(OLEConstants.REQUIRED_INVOICED_FOREIGN_PRICE);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.VENDOR_NUMBER)){
                fieldsRequired.append(OLEConstants.REQUIRED_VENDOR_NUMBER);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.QUANTITY)){
                fieldsRequired.append(OLEConstants.REQUIRED_QTY);
            }
            else if(mandatoryFields.get(fieldCount).equalsIgnoreCase(OLEConstants.OLEBatchProcess.INVOICE_DATE)){
                fieldsRequired.append(OLEConstants.REQUIRED_INVOICE_DATE);
            }
            if(!(fieldCount  == mandatoryFields.size()-1)){
                fieldsRequired.append(", ");
            }
        }
        fieldsRequired.append(mandatoryFields.size() == 1?" is":" are");
        return fieldsRequired.toString();
    }

}