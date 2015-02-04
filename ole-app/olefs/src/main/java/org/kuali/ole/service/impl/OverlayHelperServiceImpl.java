package org.kuali.ole.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleItemRecordHandler;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Content;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;

import org.kuali.ole.ingest.pojo.OleDataField;
import org.kuali.ole.ingest.pojo.OverlayOption;
import org.kuali.ole.service.OverlayDataFieldService;
import org.kuali.ole.service.OverlayHelperService;
import org.kuali.ole.service.OverlayLookupTableService;
import org.kuali.ole.service.OverlayRetrivalService;
import org.kuali.ole.util.StringUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 11/29/12
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayHelperServiceImpl implements OverlayHelperService {

    private static final Logger LOG = Logger.getLogger(OverlayHelperServiceImpl.class);
    private BibliographicRecordHandler bibliographicRecordHandler;
    private OleItemRecordHandler oleItemRecordHandler;
    private InstanceOlemlRecordProcessor instanceOlemlRecordProcessor;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private OverlayRetrivalService overlayRetrivalService;
    private OverlayDataFieldService overlayDataFieldService;
    private OverlayLookupTableService overlayLookupTableService;
    private List<String> gPFList;
    private boolean isIgnoreGPF;


    private String getResponseContent(Response response){
        String responseString = null;
        List<ResponseDocument> responseDocumentList = response.getDocuments();
        for(ResponseDocument responseDocument : responseDocumentList){
            Content contentObj = responseDocument.getContent();
            responseString = contentObj.getContent();
        }
        return responseString;
    }

    private InstanceCollection updateInstance(InstanceCollection oldInstanceCollection,InstanceCollection newInstanceCollection)throws Exception{
        List<Instance> oldInstanceList = oldInstanceCollection.getInstance();
        List<Instance> newInstanceList = newInstanceCollection.getInstance();
        OleHoldings oldOleHoldings = null;
        List<Item> oldItemList = null;
        OleHoldings newOleHoldings = null;
        List<Item> newItemList = null;
        for(Instance oldInstance : oldInstanceList){
            oldOleHoldings = oldInstance.getOleHoldings();
            Items oldItems = oldInstance.getItems();
            oldItemList = oldItems.getItem();
        }
        for(Instance newInstance : newInstanceList){
            newOleHoldings = newInstance.getOleHoldings();
            Items newItems = newInstance.getItems();
            newItemList = newItems.getItem();
        }
        updateInstanceHolding(oldOleHoldings,newOleHoldings);
        updateMatchedItem(oldItemList,newItemList);
        return oldInstanceCollection;
    }

    private void updateInstanceHolding(OleHoldings oldOleHoldings,OleHoldings newOleHoldings)throws Exception{
/*        if(newOleHoldings.getReceiptStatus()!=null){
            oldOleHoldings.setReceiptStatus(overlayRetrivalService.getReceiptStatus());
        }*/
        if(newOleHoldings.getCallNumber()!=null){
            oldOleHoldings.setCallNumber(newOleHoldings.getCallNumber());
        }
        if(newOleHoldings.getExtension()!=null){
            oldOleHoldings.setExtension(newOleHoldings.getExtension());
        }
    }

    private void updateMatchedItem(List<Item> oldItemList,List<Item> newItemList)throws Exception{
        List<Item> matchedItemList = new ArrayList<Item>();
        List<Item> umMatchedItemList = new ArrayList<Item>();
        for(Item oldItem : oldItemList){
            for(Item newItem : newItemList){
                updateInstanceItem(oldItem,newItem);
            }
        }
    }

    private void updateInstanceItem(Item oldItem,Item newItem)throws Exception{
        if(newItem.getAccessInformation()!=null){
            oldItem.getAccessInformation().setUri(newItem.getAccessInformation().getUri());
        }
        if(newItem.getBarcodeARSL()!=null){
            oldItem.setBarcodeARSL(newItem.getBarcodeARSL());
        }
        if(newItem.getStatisticalSearchingCode()!=null){
            oldItem.setStatisticalSearchingCode(newItem.getStatisticalSearchingCode());
        }
        if(newItem.getItemType()!=null){
            oldItem.setItemType(newItem.getItemType());
        }
        if(newItem.getLocation()!=null){
            oldItem.getLocation().setStatus(newItem.getLocation().getStatus());
        }
        if(newItem.getCallNumber()!=null){
            oldItem.setCallNumber(newItem.getCallNumber());
        }
        if(newItem.getExtension()!=null){
            oldItem.setExtension(newItem.getExtension());
        }
    }


    @Override
    public String updateInstanceToDocstore(String instanceUUID, InstanceCollection oldInstanceCollection, InstanceCollection newInstanceCollection) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getUUID(Response response, String docType) throws Exception{
        List<ResponseDocument> documents = response.getDocuments();
        return getUUID(documents, docType);
    }

    private String getUUID(List<ResponseDocument> documents, String docType)throws Exception{
        String uuid = null;
        for (Iterator<ResponseDocument> iterator = documents.iterator(); iterator.hasNext(); ) {
            ResponseDocument responseDocument = iterator.next();
            if (responseDocument.getType().equals(docType)) {
                uuid = responseDocument.getUuid();
            } else {
                uuid = getUUID(responseDocument.getLinkedDocuments(), docType);
            }
        }
        return uuid;
    }


    public BibMarcRecord updateBibMarcRecordIncludingGPF(BibMarcRecord oldBibMarcRecord, BibMarcRecord newBibMarcRecord,List<String> gpfFieldList,List<OverlayOption> overlayOptionList)throws Exception{
        List<DataField> oldDatafields = oldBibMarcRecord.getDataFields();
        List<DataField> newDatafields = newBibMarcRecord.getDataFields();
        BibMarcRecord updatedBibMarcRecord = oldBibMarcRecord;
        OverlayOption deleteOverlayOption = getOverlayRetrivalService().getDeleteOverlayOptionWithWildCardSearch(newDatafields, overlayOptionList);
        List<DataField> updatedDatafieldList = null;
        updatedDatafieldList = addAllDataFieldsAndSubFields(oldDatafields, newDatafields);
        oldDatafields = updatedDatafieldList != null?updatedDatafieldList:oldDatafields;
        isIgnoreGPF = true;
        updatedDatafieldList = deleteOverlayOptionDataField(oldDatafields,deleteOverlayOption);
        updatedBibMarcRecord.setDataFields(updatedDatafieldList);
        return updatedBibMarcRecord;
    }


    public BibMarcRecord updateBibMarcRecordExcludingGPF(BibMarcRecord oldBibMarcRecord, BibMarcRecord newBibMarcRecord,List<String> gpfFieldList,List<OverlayOption> overlayOptionList)throws Exception{
        List<DataField> oldDatafields = oldBibMarcRecord.getDataFields();
        List<DataField> newDatafields = newBibMarcRecord.getDataFields();
        BibMarcRecord updatedBibMarcRecord = oldBibMarcRecord;
        OverlayOption deleteOverlayOption = getOverlayRetrivalService().getDeleteOverlayOptionWithWildCardSearch(newDatafields, overlayOptionList);
        LinkedHashMap<String,String> updationDataFieldMap = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> gpfMap = getGPFMap(oldDatafields, gpfFieldList);
        List<DataField> updatedDatafieldList = null;
        isIgnoreGPF = false;
        gPFList = gpfFieldList;
        oldDatafields = excludeGPF(oldDatafields,gpfMap,true,updationDataFieldMap);
        newDatafields = excludeGPF(newDatafields,gpfMap,false,updationDataFieldMap);
        updatedDatafieldList = addAllDataFieldsAndSubFields(oldDatafields, newDatafields);
        updatedDatafieldList = deleteOverlayOptionDataField(oldDatafields,deleteOverlayOption);
        updatedBibMarcRecord.setDataFields(updatedDatafieldList);
        //updateSubfieldValues(oldDatafields,newDatafields);
        return updatedBibMarcRecord;
    }

    private List<DataField> addAllDataFields(List<DataField> oldDatafields,List<DataField> newDatafields)throws Exception{
        LinkedHashMap<String,DataField> oldDataFieldValueMap = getOverlayDataFieldService().getDataFieldValueMap(oldDatafields);
        LinkedHashMap<String,DataField> newDataFieldValueMap = getOverlayDataFieldService().getDataFieldValueMap(newDatafields);
        String updationField = null;
        for(Map.Entry<String,DataField> newDataFieldEntry : newDataFieldValueMap.entrySet()){
            updationField = newDataFieldEntry.getKey();
            if(LOG.isDebugEnabled()){
                LOG.debug("updationField------------->"+updationField);
            }
            if(oldDataFieldValueMap.containsKey(updationField)==false){
                oldDatafields.add(newDataFieldValueMap.get(updationField));
            }
        }
        return oldDatafields;
    }

    private List<DataField> addAllDataFieldsAndSubFields(List<DataField> oldDatafields,List<DataField> newDatafields)throws Exception{
        LinkedHashMap<String,DataField> oldDataFieldMap = getOverlayDataFieldService().getDataFieldValueMap(oldDatafields);
        LinkedHashMap<String,DataField> newDataFieldMap = getOverlayDataFieldService().getDataFieldValueMap(newDatafields);
        LinkedHashMap<String,SubField> oleSubFieldMap = getOverlayDataFieldService().getSubFieldValueMap(oldDatafields);
        LinkedHashMap<String,SubField> newSubFieldMap = getOverlayDataFieldService().getSubFieldValueMap(newDatafields);
        LinkedHashMap<String,SubField> newUpdateSubFieldMap = new LinkedHashMap<String,SubField>();
        String fieldKey = null;
        String tempFieldKey = null;
        int fieldKeyLength;
        //for loop will add all the new datafields to the existing record from incoming record.
        for(Map.Entry<String,DataField> newDataFieldEntry : newDataFieldMap.entrySet()){
            fieldKey = newDataFieldEntry.getKey();
            if(!oldDataFieldMap.containsKey(fieldKey)){
                oldDatafields.add(newDataFieldEntry.getValue());
            }
        }
        //below for loops will add all the new subfields and update the existing subfield from incoming record.
        for(Map.Entry<String,SubField> newSubFieldEntry : newSubFieldMap.entrySet()){
            fieldKey = newSubFieldEntry.getKey();
            if(oleSubFieldMap.containsKey(fieldKey)){
                SubField oldSubField = oleSubFieldMap.get(fieldKey);
                SubField newSubField = newSubFieldEntry.getValue();
                oldSubField.setValue(newSubField.getValue());
            }else{
/*                fieldKeyLength = fieldKey.length();
                fieldKey = fieldKey.substring(0,fieldKeyLength-2);*/
                newUpdateSubFieldMap.put(fieldKey,newSubFieldEntry.getValue());
            }

        }
        for(Map.Entry<String,SubField> newUpdateSubField : newUpdateSubFieldMap.entrySet()){
            fieldKey = newUpdateSubField.getKey();
            fieldKeyLength = fieldKey.length();
            tempFieldKey = fieldKey.substring(0,fieldKeyLength-2);
            if(oldDataFieldMap.containsKey(tempFieldKey)){
                DataField dataField = oldDataFieldMap.get(tempFieldKey);
                List<SubField> subFieldList = dataField.getSubFields();
                subFieldList.add(newUpdateSubFieldMap.get(fieldKey));

            }
        }
        return oldDatafields;

    }

    private void updateSubfieldValues(List<DataField> oldDatafields,List<DataField> newDatafields)throws Exception{
        LinkedHashMap<String,SubField> oldSubFieldValueMap = getOverlayDataFieldService().getSubFieldValueMap(oldDatafields);
        LinkedHashMap<String,SubField> newSubFieldValueMap = getOverlayDataFieldService().getSubFieldValueMap(newDatafields);
        String updationField = null;
        for(Map.Entry<String,SubField> oleDataFieldEntry : oldSubFieldValueMap.entrySet()){
            updationField = oleDataFieldEntry.getKey();
            if(LOG.isDebugEnabled()){
                LOG.debug("updationField------------->"+updationField);
            }
            if(newSubFieldValueMap.containsKey(updationField)){
                SubField oldSubField = oleDataFieldEntry.getValue();
                SubField newSubField = newSubFieldValueMap.get(updationField);
                oldSubField.setValue(newSubField.getValue());
            }
        }
    }

    private List<DataField> excludeGPF(List<DataField> datafieldList,List<String> gpfFieldList,boolean isOldRecord)throws Exception{
        String tagFieldValue = null;
        String firstIndicatorValue = null;
        String secondIndicatorValue = null;
        String subFieldValue = null;
        String[] gpfFieldArr = null;
        List<DataField> updatedDatafieldList = new ArrayList<DataField>();
        List<SubField> updatedSubfieldList = null;
        SubField previousSubField = null;
        if(gpfFieldList.size()>0){
            for(DataField dataField : datafieldList){
                updatedSubfieldList = new ArrayList<SubField>();
                for(SubField subField : dataField.getSubFields()){
                    for(String gpfField :gpfFieldList){
                        gpfFieldArr = gpfField.split(OLEConstants.DELIMITER_DASH);
                        tagFieldValue = gpfFieldArr[0];
                        firstIndicatorValue = gpfFieldArr[1];
                        secondIndicatorValue = gpfFieldArr[2];
                        subFieldValue = gpfFieldArr[3];
                        //if(!(dataField.getTag().equals(tagFieldValue) && !subField.getCode().equals(subFieldValue))){
                        if(!((dataField.getTag()+subField.getCode()).equals(tagFieldValue+subFieldValue))){
                            if(previousSubField != subField){
                                updatedSubfieldList.add(subField);//Add the non Globally Protected fields for update
                            }
                        }else if(((dataField.getTag()+subField.getCode()).equals(tagFieldValue+subFieldValue)) && isOldRecord){
                            updatedSubfieldList.add(subField);//Add the Globally Protected fields to the list in the case of existing record to retain the existing value
                        }
                        previousSubField = subField;
                    }
                }
                dataField.setSubFields(updatedSubfieldList);
                updatedDatafieldList.add(dataField);
            }
        }else{
            updatedDatafieldList = datafieldList;
        }
        return updatedDatafieldList;
    }

/*    private List<DataField> excludeGPF(List<DataField> datafieldList,HashMap<String,String> gpfMap,boolean isOldRecord,HashMap<String,String> overlayOptionFieldMap)throws Exception{
        String tagFieldValue = null;
        String firstIndicator = null;
        String secondIndicator = null;
        String subFieldCode = null;
        String[] gpfFieldArr = null;
        String field = null;
        boolean isGPF = false;
        List<DataField> updatedDatafieldList = new ArrayList<DataField>();
        List<SubField> updatedSubfieldList = null;
        SubField previousSubField = null;
        if(gpfMap!=null && gpfMap.size()>0 && !gpfMap.isEmpty()){
            for(DataField dataField : datafieldList){
                updatedSubfieldList = new ArrayList<SubField>();
                for(SubField subField : dataField.getSubFields()){
                    tagFieldValue = dataField.getTag();
                    firstIndicator = dataField.getInd1()!=null?dataField.getInd1().trim():"";
                    secondIndicator = dataField.getInd2()!=null?dataField.getInd2().trim():"";
                    subFieldCode = subField.getCode().trim();
                    field = tagFieldValue+OLEConstants.DELIMITER_DASH+firstIndicator+OLEConstants.DELIMITER_DASH+secondIndicator+OLEConstants.DELIMITER_DASH+subFieldCode;
                    if(LOG.isInfoEnabled()){
                        LOG.info("field------------>"+field);
                        LOG.info("gpfMap field------------>"+gpfMap.get(field));
                    }
                    if(!gpfMap.containsKey(field)){
                        updatedSubfieldList.add(subField);
                    }else if(gpfMap.containsKey(field) && isOldRecord && !overlayOptionFieldMap.containsKey(field)){
                        //Add the Globally Protected fields to the list in case of existing record to retain the existing value
                        // and to exclude the overlay tag field which is also a globally protected field.
                        updatedSubfieldList.add(subField);
                    }
                }
                dataField.setSubFields(updatedSubfieldList);
                updatedDatafieldList.add(dataField);
            }
        }else{
            updatedDatafieldList = datafieldList;
        }
        return updatedDatafieldList;
    }*/

    private List<DataField> excludeGPF(List<DataField> datafieldList,LinkedHashMap<String,String> gpfMap,boolean isOldRecord,LinkedHashMap<String,String> overlayOptionFieldMap)throws Exception{
        String tagFieldValue = null;
        String firstIndicator = null;
        String secondIndicator = null;
        String subFieldCode = null;
        String[] gpfFieldArr = null;
        String field = null;
        boolean isGPF = false;
        List<DataField> updatedDatafieldList = new ArrayList<DataField>();
        List<SubField> updatedSubfieldList = null;
        SubField previousSubField = null;
        if(gpfMap!=null && gpfMap.size()>0 && !gpfMap.isEmpty()){
            for(DataField dataField : datafieldList){
                updatedSubfieldList = new ArrayList<SubField>();
                for(SubField subField : dataField.getSubFields()){
                    tagFieldValue = dataField.getTag();
                    firstIndicator = dataField.getInd1()!=null? StringUtil.trimHashNullValues(dataField.getInd1()):OLEConstants.DELIMITER_HASH;
                    secondIndicator = dataField.getInd2()!=null?StringUtil.trimHashNullValues(dataField.getInd2()):OLEConstants.DELIMITER_HASH;
                    subFieldCode = subField.getCode().trim();
                    field = tagFieldValue+firstIndicator+secondIndicator+OLEConstants.DELIMITER_DOLLAR+subFieldCode;
                    if(LOG.isDebugEnabled()){
                        LOG.debug("field------------>"+field);
                        LOG.debug("gpfMap field------------>"+gpfMap.get(field));
                    }
                    if(!gpfMap.containsKey(field)){
                        updatedSubfieldList.add(subField);
                    }else if(gpfMap.containsKey(field) && isOldRecord && !overlayOptionFieldMap.containsKey(field)){
                        //Add the Globally Protected fields to the list in case of existing record to retain the existing value
                        // and to exclude the overlay tag field which is also a globally protected field.
                        updatedSubfieldList.add(subField);
                    }
                }
                dataField.setSubFields(updatedSubfieldList);
                updatedDatafieldList.add(dataField);
            }
        }else{
            updatedDatafieldList = datafieldList;
        }
        return updatedDatafieldList;
    }

/*    private HashMap<String,String> getGPFMap(List<DataField> oldDatafields, List<String> gpfFieldList)throws Exception{
        HashMap<String,String> gpfMap = new HashMap<String,String>();
        gpfFieldList = checkWildCardSearchForGPF(oldDatafields, gpfFieldList);
        for(String gpfField :gpfFieldList){
            if(LOG.isInfoEnabled()){
                LOG.info("gpfField in getGPFMap------------>" + gpfField);
            }
            gpfMap.put(gpfField,gpfField);
        }
        return gpfMap;
    }*/

    private LinkedHashMap<String,String> getGPFMap(List<DataField> oldDatafields, List<String> gpfFieldList)throws Exception{
        LinkedHashMap<String,String> gpfMap = new LinkedHashMap<String,String>();
        gpfFieldList = checkWildCardSearchForGPF(oldDatafields, gpfFieldList);
        for(String gpfField :gpfFieldList){
            if(LOG.isDebugEnabled()){
                LOG.debug("gpfField in getGPFMap------------>" + gpfField);
            }
            gpfMap.put(gpfField,gpfField);
        }
        return gpfMap;
    }

    List<String> checkWildCardSearchForGPF(List<DataField> oldDatafields, List<String> gpfFieldList) {
        List<String> gpfUpdatedList = new ArrayList<String>();
        for(String gpfFields : gpfFieldList){
            if(gpfFields.contains("*")){
                char[] gpfFieldSplit = gpfFields.toCharArray();
                for(DataField oldDataField : oldDatafields) {
                    boolean oldDataFieldStartsWith = oldDataField.getTag().startsWith(String.valueOf(gpfFieldSplit[0]));
                    if(oldDataFieldStartsWith){
                        gpfUpdatedList.add(oldDataField.getTag());
                    }
                }
            }
            else {
                gpfUpdatedList.add(gpfFields);
            }
        }
        return gpfUpdatedList;
    }

    private List<DataField> addOverlayOptionDataField(List<DataField> datafieldList,OverlayOption overlayOption,HashMap<String,String> updationDataFieldMap)throws Exception{
        List<DataField> updatedDatafieldList = datafieldList;
        String tagFieldValue = null;
        String firstIndicatorValue = null;
        String secondIndicatorValue = null;
        String subFieldCode = null;
        Boolean isUpdationField = false;
        List<OleDataField> addOleDataFieldList = overlayOption.getOleDataFields();
        DataField addDataField = null;
        List<SubField> subFieldList = null;
        if(addOleDataFieldList!=null && addOleDataFieldList.size()>0){
            for(OleDataField oleDataField : addOleDataFieldList){
                tagFieldValue =  oleDataField.getDataFieldTag();
                firstIndicatorValue = oleDataField.getDataFieldInd1();
                secondIndicatorValue = oleDataField.getDataFieldInd2();
                subFieldCode = oleDataField.getSubFieldCode();
                if(!(addDataField!=null && addDataField.getTag().equals(tagFieldValue))){
                    addDataField = new DataField();
                    addDataField.setTag(tagFieldValue);
                    addDataField.setInd1(firstIndicatorValue);
                    addDataField.setInd2(secondIndicatorValue);
                    subFieldList = new ArrayList<SubField>();
                    SubField addSubField = new SubField();
                    addSubField.setCode(subFieldCode);
                    subFieldList.add(addSubField);
                    addDataField.setSubFields(subFieldList);
                    updationDataFieldMap.put(tagFieldValue+OLEConstants.DELIMITER_DASH+firstIndicatorValue+OLEConstants.DELIMITER_DASH+secondIndicatorValue+OLEConstants.DELIMITER_DASH+subFieldCode,
                            OLEConstants.OVERLAY_OPTION_ADD);
                }else{
                    SubField addSubField = new SubField();
                    addSubField.setCode(subFieldCode);
                    subFieldList.add(addSubField);
                    addDataField.setSubFields(subFieldList);
                    updationDataFieldMap.put(tagFieldValue+OLEConstants.DELIMITER_DASH+firstIndicatorValue+OLEConstants.DELIMITER_DASH+secondIndicatorValue+OLEConstants.DELIMITER_DASH+subFieldCode,
                            OLEConstants.OVERLAY_OPTION_ADD);
                }
                updatedDatafieldList.add(addDataField);
            }
        }
        return updatedDatafieldList;
    }



    private List<DataField> deleteOverlayOptionDataField(List<DataField> datafieldList,OverlayOption overlayOption)throws Exception{
        LinkedHashMap<String,SubField> oldSubFieldValueMap = getOverlayDataFieldService().getSubFieldValueMap(datafieldList);
        LinkedHashMap<String,SubField> updatedSubFieldValueMap = new LinkedHashMap<String, SubField>();
        List<DataField> updatedDatafieldList = new ArrayList<DataField>();
        List<SubField> updatedSubfieldList = null;
        SubField previousSubField = null;
        String tagFieldValue = null;
        String firstIndicator = null;
        String secondIndicator = null;
        String subFieldCode = null;
        if(overlayOption != null && overlayOption.getOleDataFields()!=null){
            List<OleDataField> deleteOleDataFieldList = overlayOption.getOleDataFields();
            DataField addDataField = null;
            List<String> keySBList = new ArrayList<String>();
            for(OleDataField oleDataField : deleteOleDataFieldList){
                tagFieldValue =  oleDataField.getDataFieldTag();
                firstIndicator = StringUtil.trimHashNullValues(oleDataField.getDataFieldInd1());
                secondIndicator = StringUtil.trimHashNullValues(oleDataField.getDataFieldInd2());
                subFieldCode = oleDataField.getSubFieldCode();
                StringBuffer keySB = new StringBuffer();
                keySB.append(tagFieldValue).append(firstIndicator)
                       .append(secondIndicator).append(OLEConstants.DELIMITER_DOLLAR).append(subFieldCode);
                if(isIgnoreGPF){
                    keySBList.add(keySB.toString());
                }
                else if(!gPFList.contains(keySB.toString())) {
                    keySBList.add(keySB.toString());
                }
            }
            for(Map.Entry<String,SubField> oldSubFieldValueMapEntry : oldSubFieldValueMap.entrySet()){
                if(!keySBList.contains(oldSubFieldValueMapEntry.getKey())){
                    updatedSubFieldValueMap.put(oldSubFieldValueMapEntry.getKey(),oldSubFieldValueMapEntry.getValue());
                }
            }

            for(Map.Entry<String,SubField> updatedSubFieldValueMapEntry : updatedSubFieldValueMap.entrySet()){
                String dataFieldTags = updatedSubFieldValueMapEntry.getKey();
                String tag=dataFieldTags.substring(0,3);
                String ind1 = dataFieldTags.substring(3,4);
                String ind2 = dataFieldTags.substring(4,5);
                boolean flag=false;
                for(DataField dataField1:updatedDatafieldList){
                    if (dataField1.getInd1().equals(ind1)&&dataField1.getInd2().equals(ind2)&&dataField1.getTag().equals(tag)){
                        SubField newsubField=new SubField();
                        newsubField.setCode(dataFieldTags.substring(6,7));
                        newsubField.setValue(updatedSubFieldValueMapEntry.getValue().getValue());
                        dataField1.getSubFields().add(newsubField);
                        flag=true;
                    }
                }
                if(flag==false){
                    DataField newDataField = new DataField();
                    newDataField.setTag(tag);
                    newDataField.setInd1(ind1);
                    newDataField.setInd2(ind2);

                    SubField newsubField=new SubField();
                    newsubField.setCode(dataFieldTags.substring(6,7));
                    newsubField.setValue(updatedSubFieldValueMapEntry.getValue().getValue());
                    List<SubField> subFieldList=new ArrayList<SubField>();
                    subFieldList.add(newsubField);

                    newDataField.setSubFields(subFieldList);
                    updatedDatafieldList.add(newDataField);
                }
            }
            return updatedDatafieldList;
        }else{
            return null;
        }
    }


    public OverlayRetrivalService getOverlayRetrivalService() {
        if(overlayRetrivalService == null){
            overlayRetrivalService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_RETRIVAL_SERVICE);
        }
        return overlayRetrivalService;
    }

    public void setOverlayRetrivalService(OverlayRetrivalService overlayRetrivalService) {
        this.overlayRetrivalService = overlayRetrivalService;
    }

    public OverlayDataFieldService getOverlayDataFieldService() {
        if(overlayDataFieldService == null){
            overlayDataFieldService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_DATAFIELD_SERVICE);
        }
        return overlayDataFieldService;
    }

    public void setOverlayDataFieldService(OverlayDataFieldService overlayDataFieldService) {
        this.overlayDataFieldService = overlayDataFieldService;
    }

    public OverlayLookupTableService getOverlayLookupTableService() {
        if(overlayLookupTableService == null){
            overlayLookupTableService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_LOOKUPTABLE_SERVICE);
        }
        return overlayLookupTableService;
    }

    public void setOverlayLookupTableService(OverlayLookupTableService overlayLookupTableService) {
        this.overlayLookupTableService = overlayLookupTableService;
    }

    private BibliographicRecordHandler getBibliographicRecordHandler(){
        if(bibliographicRecordHandler == null){
            bibliographicRecordHandler = new BibliographicRecordHandler();
        }
        return bibliographicRecordHandler;
    }

    private InstanceOlemlRecordProcessor getInstanceOlemlRecordProcessor() {
        if(instanceOlemlRecordProcessor == null){
            instanceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        }
        return instanceOlemlRecordProcessor;
    }

    private OleItemRecordHandler getOleItemRecordHandler() {
        if(oleItemRecordHandler == null){
            oleItemRecordHandler = new OleItemRecordHandler();
        }
        return oleItemRecordHandler;
    }

    public HoldingOlemlRecordProcessor getHoldingOlemlRecordProcessor() {
        if(holdingOlemlRecordProcessor == null){
            holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        }
        return holdingOlemlRecordProcessor;
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if(itemOlemlRecordProcessor == null){
            itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        }
        return itemOlemlRecordProcessor;
    }

}
