package org.kuali.ole.service.impl;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.ingest.pojo.OleDataField;
import org.kuali.ole.ingest.pojo.OverlayOption;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.service.OverlayRetrivalService;
import org.kuali.ole.util.StringUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/10/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class OverlayRetrivalServiceImpl implements OverlayRetrivalService {

    private DataCarrierService dataCarrierService;

    @Override
    public List<OleGloballyProtectedField> getGloballyProtectedFields() throws Exception {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleGloballyProtectedField> oleGloballyProtectedFieldList = (List<OleGloballyProtectedField>) businessObjectService.findAll(OleGloballyProtectedField.class);
        return oleGloballyProtectedFieldList;
    }

    @Override
    public List<String> getGloballyProtectedFieldsList()throws Exception{
        List fieldList = new ArrayList<String>();
        StringBuffer fields = null;
        String tagField = null;
        String firstIndicator = null;
        String secondIndicator = null;
        String subField = null;
        List<OleGloballyProtectedField> protectedFieldList = getGloballyProtectedFields();
        for(OleGloballyProtectedField oleGloballyProtectedField : protectedFieldList){
            tagField = oleGloballyProtectedField.getTag();
            firstIndicator = oleGloballyProtectedField.getFirstIndicator()!=null? StringUtil.trimHashNullValues(oleGloballyProtectedField.getFirstIndicator()):OLEConstants.DELIMITER_HASH;
            secondIndicator = oleGloballyProtectedField.getSecondIndicator()!=null? StringUtil.trimHashNullValues(oleGloballyProtectedField.getSecondIndicator()):OLEConstants.DELIMITER_HASH;
            subField = oleGloballyProtectedField.getSubField()!=null?OLEConstants.DELIMITER_DOLLAR+oleGloballyProtectedField.getSubField():OLEConstants.DELIMITER_DOLLAR+"*";
            fields = new StringBuffer();
            fields = fields.append(tagField).append(firstIndicator).append(secondIndicator).append(subField);
            fieldList.add(fields.toString());
        }

        return fieldList;
    }

    @Override
    public List getGloballyProtectedFieldsModificationList() throws Exception {
        List fieldList = new ArrayList<String>();
        StringBuffer fields = null;
        String tagField = null;
        String firstIndicator = null;
        String secondIndicator = null;
        String subField = null;
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("modifyFlag","false");
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleGloballyProtectedField> protectedFieldList = (List<OleGloballyProtectedField>)businessObjectService.findMatching(OleGloballyProtectedField.class,criteria);
        for(OleGloballyProtectedField oleGloballyProtectedField : protectedFieldList){
            //if(!oleGloballyProtectedField.isModifyFlag()){
                tagField = oleGloballyProtectedField.getTag();
                firstIndicator = oleGloballyProtectedField.getFirstIndicator()!=null?"-"+ oleGloballyProtectedField.getFirstIndicator():"-";
                secondIndicator = oleGloballyProtectedField.getSecondIndicator()!=null?"-"+ oleGloballyProtectedField.getSecondIndicator():"-";
                subField = oleGloballyProtectedField.getSubField()!=null?"-"+ oleGloballyProtectedField.getSubField():"-";
                fields = new StringBuffer();
                fields = fields.append(tagField).append(firstIndicator).append(secondIndicator).append(subField);
                fieldList.add(fields.toString());
            //}
        }

        return fieldList;
    }

    @Override
    public OverlayOption getAddOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList) throws Exception {
        OverlayOption addOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_ADD)){
                overlayOption = checkWildCard(newDatafields, overlayOption);
                addOverlayOption = overlayOption;
            }
        }
        return addOverlayOption;
    }

    @Override
    public OverlayOption getAddOverlayOption(List<OverlayOption> overlayOptionList)throws Exception{
        OverlayOption addOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_ADD)){
                addOverlayOption = overlayOption;
            }
        }
        return addOverlayOption;
    }

    @Override
    public OverlayOption getDeleteOverlayOption(List<OverlayOption> overlayOptionList)throws Exception{
        OverlayOption deleteOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_DELETE)){
                deleteOverlayOption = overlayOption;
            }
        }
        return deleteOverlayOption;
    }

    @Override
    public OverlayOption getDeleteOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList) {
        OverlayOption deleteOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_DELETE)){
                overlayOption = checkWildCard(newDatafields, overlayOption);
                deleteOverlayOption = overlayOption;
            }
        }
        return deleteOverlayOption;
    }

    @Override
    public OverlayOption getUpdateOverlayOption(List<OverlayOption> overlayOptionList)throws Exception{
        OverlayOption updateOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_UPDATE)){
                updateOverlayOption = overlayOption;
            }
        }
        return updateOverlayOption;
    }

    @Override
    public OverlayOption getUpdateOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList) throws Exception {
        OverlayOption updateOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_UPDATE)){
                overlayOption = checkWildCard(newDatafields, overlayOption);
                updateOverlayOption = overlayOption;
            }
        }
        return updateOverlayOption;
    }

    /*@Override
    public OverlayOption getDontAddOverlayOption(List<OverlayOption> overlayOptionList)throws Exception{
        OverlayOption dontAddOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_DONTADD)){
                dontAddOverlayOption = overlayOption;
            }
        }
        return dontAddOverlayOption;
    }

    @Override
    public OverlayOption getDontAddOverlayOptionWithWildCardSearch(List<DataField> newDatafields, List<OverlayOption> overlayOptionList) {
        OverlayOption dontAddOverlayOption = null;
        for(OverlayOption overlayOption : overlayOptionList){
            if(overlayOption.getName().equals(OLEConstants.OVERLAY_OPTION_DONTADD)){
                overlayOption = checkWildCard(newDatafields, overlayOption);
                dontAddOverlayOption = overlayOption;
            }
        }
        return dontAddOverlayOption;
    }*/

    /*@Override
    public String getReceiptStatus() {
        List<ProfileAttributeBo> profileAttributesList = (List<ProfileAttributeBo>) getDataCarrierService().getData(OLEConstants.PROFILE_ATTRIBUTE_LIST);
        String receiptStatus = new String();
        String receiptStatusName = new String();
        for(ProfileAttributeBo profileAttributeBo : profileAttributesList){
            if(profileAttributeBo.getAttributeName().equals(OLEConstants.RECEIPT_STATUS)){
                receiptStatus = profileAttributeBo.getAttributeValue();
            }
        }
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> attributeMap = new HashMap<String, String>();
        attributeMap.put("receiptStatusCode",receiptStatus);
        List<OleReceiptStatus> oleReceiptStatusList = (List<OleReceiptStatus>) businessObjectService.findMatching(OleReceiptStatus.class,attributeMap);
        for(OleReceiptStatus oleReceiptStatus : oleReceiptStatusList){
            receiptStatusName = oleReceiptStatus.getReceiptStatusName();
        }
        return receiptStatusName;
    }*/


    public OverlayOption checkWildCard(List<DataField> newDatafieldList, OverlayOption overlayOption){
        List<OleDataField> overlayOptionOleDataFields = overlayOption.getOleDataFields();
        OverlayOption newOverlayOption = new OverlayOption();
        newOverlayOption.setId(overlayOption.getId());
        newOverlayOption.setAgendaName(overlayOption.getAgendaName());
        newOverlayOption.setName(overlayOption.getName());
        List<OleDataField> oleDataFields = new ArrayList<OleDataField>();
        for(OleDataField oleDataField : overlayOptionOleDataFields){
            String tag = oleDataField.getDataFieldTag();
            boolean isSubFieldCodeStar = "*".equalsIgnoreCase(oleDataField.getSubFieldCode());
            if(tag.contains("*")){
                char[] dataFieldSplit = oleDataField.getDataFieldTag().toCharArray();
                for(DataField dataFields : newDatafieldList){
                    boolean isStartsWithDataField = dataFields.getTag().startsWith(String.valueOf(dataFieldSplit[0]));
                    if(isStartsWithDataField){
                        for(SubField subField : dataFields.getSubFields()){
                            if(isSubFieldCodeStar){
                                OleDataField newOleDataFieldSubFieldCodeStar = new OleDataField();
                                newOleDataFieldSubFieldCodeStar.setId(oleDataField.getId());
                                newOleDataFieldSubFieldCodeStar.setOverlayOptionId(oleDataField.getOverlayOptionId());
                                newOleDataFieldSubFieldCodeStar.setAgendaName(oleDataField.getAgendaName());
                                newOleDataFieldSubFieldCodeStar.setDataFieldTag(dataFields.getTag());
                                newOleDataFieldSubFieldCodeStar.setDataFieldInd1(oleDataField.getDataFieldInd1());
                                newOleDataFieldSubFieldCodeStar.setDataFieldInd2(oleDataField.getDataFieldInd2());
                                newOleDataFieldSubFieldCodeStar.setSubFieldCode(subField.getCode());
                                oleDataFields.add(newOleDataFieldSubFieldCodeStar);
                            }
                            else if(oleDataField.getSubFieldCode().equalsIgnoreCase(subField.getCode())){
                                OleDataField newOleDataField = new OleDataField();
                                newOleDataField.setId(oleDataField.getId());
                                newOleDataField.setOverlayOptionId(oleDataField.getOverlayOptionId());
                                newOleDataField.setAgendaName(oleDataField.getAgendaName());
                                newOleDataField.setDataFieldTag(dataFields.getTag());
                                newOleDataField.setDataFieldInd1(oleDataField.getDataFieldInd1());
                                newOleDataField.setDataFieldInd2(oleDataField.getDataFieldInd2());
                                newOleDataField.setSubFieldCode(subField.getCode());
                                oleDataFields.add(newOleDataField);
                            }
                        }
                    }
                }
            }
            else if(!tag.contains("*") && oleDataField.getSubFieldCode().equalsIgnoreCase("*")){
                for(DataField dataFields : newDatafieldList){
                    if(tag.equalsIgnoreCase(dataFields.getTag())){
                        for(SubField subField : dataFields.getSubFields()){
                            OleDataField newOleDataFieldSubFieldCodeStar = new OleDataField();
                            newOleDataFieldSubFieldCodeStar.setId(oleDataField.getId());
                            newOleDataFieldSubFieldCodeStar.setOverlayOptionId(oleDataField.getOverlayOptionId());
                            newOleDataFieldSubFieldCodeStar.setAgendaName(oleDataField.getAgendaName());
                            newOleDataFieldSubFieldCodeStar.setDataFieldTag(dataFields.getTag());
                            newOleDataFieldSubFieldCodeStar.setDataFieldInd1(oleDataField.getDataFieldInd1());
                            newOleDataFieldSubFieldCodeStar.setDataFieldInd2(oleDataField.getDataFieldInd2());
                            newOleDataFieldSubFieldCodeStar.setSubFieldCode(subField.getCode());
                            oleDataFields.add(newOleDataFieldSubFieldCodeStar);
                        }
                    }
                }
            }
            else {
                oleDataFields.add(oleDataField);
            }
        }
        newOverlayOption.setOleDataFields(oleDataFields);
        return newOverlayOption;
    }
    @Override
    public OleCallNumber getCallNumberRecord(String inputValue)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put(OLEConstants.OVERLAY_INPUTVALUE,inputValue);
        List<OleCallNumber> oleCallNumberList = (List<OleCallNumber>) businessObjectService.findMatching(OleCallNumber.class,criteriaMap);
        if(oleCallNumberList!=null && oleCallNumberList.size()>0){
            return oleCallNumberList.iterator().next();
        }else{
            return null;
        }
    }

    public OleCallNumber getCallNumberRecord(HashMap<String,String> criteriaMap)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleCallNumber> oleCallNumberList = (List<OleCallNumber>) businessObjectService.findMatching(OleCallNumber.class,criteriaMap);
        if(oleCallNumberList!=null && oleCallNumberList.size()>0){
            return oleCallNumberList.iterator().next();
        }else{
            return null;
        }
    }
    @Override
    public OleCode getOleCodeRecord(String inputValue)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put(OLEConstants.OVERLAY_INPUTVALUE,inputValue);
        List<OleCode> oleCodeList = (List<OleCode>) businessObjectService.findMatching(OleCode.class,criteriaMap);
        if(oleCodeList!=null && oleCodeList.size()>0){
            return oleCodeList.iterator().next();
        }else{
            return null;
        }
    }
    @Override
    public OleCode getOleCodeRecord(HashMap<String,String> criteriaMap)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleCode> oleCodeList = (List<OleCode>) businessObjectService.findMatching(OleCode.class,criteriaMap);
        if(oleCodeList!=null && oleCodeList.size()>0){
            return oleCodeList.iterator().next();
        }else{
            return null;
        }
    }
    @Override
    public OleBudgetCode getOleBudgetCode(String inputValue)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put(OLEConstants.OVERLAY_INPUTVALUE,inputValue);
        List<OleBudgetCode> oleBudgetCodeList = (List<OleBudgetCode>) businessObjectService.findMatching(OleBudgetCode.class,criteriaMap);
        if(oleBudgetCodeList!=null && oleBudgetCodeList.size()>0){
            return oleBudgetCodeList.iterator().next();
        }else{
            return null;
        }
    }
    @Override
    public OleBudgetCode getOleBudgetCode(HashMap<String,String> criteriaMap)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleBudgetCode> oleBudgetCodeList = (List<OleBudgetCode>) businessObjectService.findMatching(OleBudgetCode.class,criteriaMap);
        if(oleBudgetCodeList!=null && oleBudgetCodeList.size()>0){
            return oleBudgetCodeList.iterator().next();
        }else{
            return null;
        }
    }

    @Override
    public OleVendorAccountInfo getAccountObjectForVendorRefNo(HashMap<String,String> criteriaMap)throws Exception{
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        List<OleVendorAccountInfo> oleVendorAccountInfoList = (List<OleVendorAccountInfo>) businessObjectService.findMatching(OleVendorAccountInfo.class,criteriaMap);
        if(oleVendorAccountInfoList!=null && oleVendorAccountInfoList.size()>0){
            return oleVendorAccountInfoList.iterator().next();
        }else{
            return null;
        }
/*        OleVendorAccountInfo vendor=new OleVendorAccountInfo();
        vendor=businessObjectService.findBySinglePrimaryKey(OleVendorAccountInfo.class,vendorReferenceNo);*/

    }

    /**
     *  Gets the dataCarrierService attribute.
     * @return  Returns dataCarrierService.
     */
    protected DataCarrierService getDataCarrierService() {
        if(dataCarrierService == null){
            return GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        }
        return dataCarrierService;
    }

}
