package org.kuali.ole.batch.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.batch.impl.OLEBatchProcess;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.kuali.ole.OLEConstants.OLEBatchProcess.*;
import static org.kuali.ole.OLEConstants.OLEBatchProcess.ERR_ITEM;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceMappingHelper {
    private List<DataField> dataFieldList = new ArrayList<DataField>();
    private String location3, location4, callNumber, callNumberType;
    private StringBuilder errBuilder;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();

    private static final Logger LOG = Logger.getLogger(InstanceMappingHelper.class);


    public List<DataField> generateDataFieldForHolding(HoldingsTree holdingsTree, OLEBatchProcessProfileBo profile, StringBuilder errBuilder) throws Exception {
        dataFieldList.clear();
        this.errBuilder = errBuilder;

        Item item = null;
        if (holdingsTree != null) {
            Map<String, String> dataFieldsHoldingsMap = new HashMap<>();
            Map<String, String> dataFieldsItemsMap = new HashMap<>();
            Map<String, String> dataFieldsDonorMap = new HashMap<>();
            List<OLEBatchProcessProfileDataMappingOptionsBo> mappingOptionsBoList = profile.getOleBatchProcessProfileMappingOptionsList().get(0).getOleBatchProcessProfileDataMappingOptionsBoList();
            for (OLEBatchProcessProfileDataMappingOptionsBo mappingOptionsBo : mappingOptionsBoList) {
                if (mappingOptionsBo.getDataType().equalsIgnoreCase(BATCH_PROCESS_PROFILE_DATATYPE_HOLDINGS)) {
                    dataFieldsHoldingsMap.put(mappingOptionsBo.getDestinationField(), mappingOptionsBo.getSourceField());
                } else if (mappingOptionsBo.getDataType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_ITEM)) {
                    if (mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE)) {
                        dataFieldsDonorMap.put(mappingOptionsBo.getDestinationField(), mappingOptionsBo.getSourceField());
                    } else {
                        dataFieldsItemsMap.put(mappingOptionsBo.getDestinationField(), mappingOptionsBo.getSourceField());
                    }

                }
            }
            if (!CollectionUtils.isEmpty(dataFieldsHoldingsMap)) {
                generateSubFieldsForHolding(holdingsTree.getHoldings(), dataFieldsHoldingsMap);
            }
            if (!CollectionUtils.isEmpty(dataFieldsItemsMap) || !CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                for (org.kuali.ole.docstore.common.document.Item itemDoc : holdingsTree.getItems()) {

                    if (itemDoc == null) continue;


                    boolean isStaffOnly = false;
                    if (profile.getExportScope().equalsIgnoreCase(OLEBatchProcess.INCREMENTAL_EXPORT_EX_STAFF)) {
                        if (itemDoc.isStaffOnly()) {
                            isStaffOnly = true;
                        }
                    }

                    if (!isStaffOnly) {
                        if (itemDoc.getContent() != null && !itemDoc.getContent().isEmpty()) {
                            item = itemOlemlRecordProcessor.fromXML(itemDoc.getContent());
                        } else {
                            item = (Item) itemDoc.getContentObject();
                            item.setItemIdentifier(itemDoc.getId());
                        }
                        List<DataField> dataFieldsItemList = generateSubFieldsForItem(holdingsTree.getHoldings(), item, dataFieldsItemsMap, dataFieldsDonorMap, new ArrayList<DataField>());
                        if (!CollectionUtils.isEmpty(dataFieldsItemList))
                            dataFieldList.addAll(dataFieldsItemList);
                        dataFieldsItemList.clear();
                    }
                }
            }
            if (CollectionUtils.isEmpty(dataFieldsHoldingsMap) && CollectionUtils.isEmpty(dataFieldsItemsMap) && CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                return Collections.EMPTY_LIST;
            }
        }
        return dataFieldList;
    }

    protected void generateSubFieldsForHolding(Holdings holdingsDocument, Map<String, String> dataFieldsHoldingsMap) throws Exception {
        OleHoldings holdings = null;
        try {
            if (holdingsDocument.getContentObject() != null) {
                holdings= holdingsDocument.getContentObject();
            } else {
                holdings = holdingOlemlRecordProcessor.fromXML(holdingsDocument.getContent());
            }
            for (Map.Entry<String, String> entry : dataFieldsHoldingsMap.entrySet()) {
                DataField dataField;
                if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.LOCAL_IDENTIFIER)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    holdings.setHoldingsIdentifier(holdingsDocument.getId());
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingLocalIdentifier(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingLocalIdentifier(holdings, getCode(entry.getKey()), dataField);
                    }
                }else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumber(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumber(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberType(holdings, getCode(StringUtils.trim(entry.getKey())), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumberType(holdings, getCode(StringUtils.trim(entry.getKey())), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel1(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel1(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel2(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel2(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel3(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel3(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel4(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel4(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel5(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel5(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberPrefix(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumberPrefix(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COPY_NUMBER)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCopyNumber(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCopyNumber(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SOURCE_FIELD_DATE_CREATED)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateDateCreated(holdingsDocument, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateDateCreated(holdingsDocument, getCode(entry.getKey()), dataField);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping instance data ::" + holdings.getHoldingsIdentifier(), ex);
            buildError(ERR_INSTANCE, holdings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    protected DataField checkDataField(List<DataField> dataFieldList, String dataField) {
        for (DataField field : dataFieldList) {
            if (dataField.equals(field.getTag())) {
                return field;
            }
        }
        return null;
    }

    protected DataField getDataField(Map.Entry<String, String> entry) {
        DataField dataField = new DataField();
        dataField.setTag(StringUtils.trim(entry.getKey()).substring(0, 3));
        dataField.setInd1(" ");
        dataField.setInd2(" ");
        return dataField;
    }

    protected List<DataField> generateSubFieldsForItem(Holdings holdingsDocument, Item item, Map<String, String> dataFieldsItemsMap, Map<String, String> dataFieldsDonorMap, List<DataField> dataFieldItemList) throws Exception {
        OleHoldings holdings = null;
        //List<DataField> donorFieldList = new ArrayList<>();
        try {
            if (holdingsDocument.getContent() != null && !holdingsDocument.getContent().isEmpty()) {
                holdings = holdingOlemlRecordProcessor.fromXML(holdingsDocument.getContent());
            } else {
                holdings = holdingsDocument.getContentObject();
            }
            for (Map.Entry<String, String> entry : dataFieldsItemsMap.entrySet()) {
                DataField dataField;
                if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.LOCAL_IDENTIFIER)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateItemLocalIdentifier(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateItemLocalIdentifier(item, getCode(entry.getKey()), dataField);
                    }
                }else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (item.getCallNumber() == null) continue;
                    if (callNumber != null && StringUtils.isNotEmpty(item.getCallNumber().getNumber()) && item.getCallNumber().getNumber().equals(callNumber))
                        continue;
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumber(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCallNumber(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (item.getCallNumber() == null) continue;
                    if (callNumberType != null && item.getCallNumber().getShelvingScheme() != null && StringUtils.isNotEmpty(item.getCallNumber().getShelvingScheme().getCodeValue()) && item.getCallNumber().getShelvingScheme().getCodeValue().equals(callNumberType))
                        continue;
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberType(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCallNumberType(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COPY_NUMBER)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCopyNumber(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCopyNumber(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_ITEM_BARCODE)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateItemNumber(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateItemNumber(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ITEM_TYPE)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateItemType(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateItemType(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel1(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel1(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel2(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel2(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel3(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel3(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel4(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel4(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel5(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel5(item, getCode(entry.getKey()), dataField);
                    }
                } /*else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)) {
                    for (DonorInfo donorInfo : item.getDonorInfo()) {
                        DataField dataField1 = getDataField(entry);
                        generateDonorPublicDisplay(item, donorInfo, getCode(entry.getKey()), dataField1);
                        if (!dataField1.getSubFields().isEmpty()) donorFieldList.add(dataField1);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_NOTE)) {
                    for (DonorInfo donorInfo : item.getDonorInfo()) {
                        DataField dataField1 = getDataField(entry);
                        generateDonorNote(item, donorInfo, getCode(entry.getKey()), dataField1);
                        if (!dataField1.getSubFields().isEmpty()) donorFieldList.add(dataField1);
                    }
                }*/ else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberPrefix(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCallNumberPrefix(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumber(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCallNumber(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberType(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCallNumberType(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCopyNumber(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCopyNumber(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberPrefix(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateCallNumberPrefix(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel1(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel1(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel2(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel2(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel3(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel3(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel4(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel4(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel5(holdings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateLocationLevel5(holdings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_STATUS)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateItemStatus(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateItemStatus(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_ENUMERATION)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateEnumeration(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateEnumeration(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_CHRONOLOGY)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateChronology(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateChronology(item, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_VENDOR_LINE_ITEM_IDENTIFIER)) {
                    dataField = checkDataField(dataFieldItemList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateVendorLineItemIdentifier(item, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldItemList.add(dataField);
                    } else {
                        generateVendorLineItemIdentifier(item, getCode(entry.getKey()), dataField);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                //dataFieldItemList.addAll(donorFieldList);
                generateDonorFields(item, dataFieldsDonorMap);
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping item data ::" + item.getItemIdentifier(), ex);
            buildError(ERR_INSTANCE, item.getItemIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
        return dataFieldItemList;
    }

    private void generateDonorFields(Item item, Map<String, String> dataFieldsDonorMap) {
        List<DataField> donorFieldList = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(item.getDonorInfo())) {
                for (DonorInfo donorInfo : item.getDonorInfo()) {
                    for (Map.Entry<String, String> entry : dataFieldsDonorMap.entrySet()) {
                        DataField dataField;
                        if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)) {
                            dataField = checkDataField(donorFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorPublicDisplay(item, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubFields().isEmpty()) donorFieldList.add(dataField);
                            } else {
                                generateDonorPublicDisplay(item, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_NOTE)) {
                            dataField = checkDataField(donorFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorNote(item, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubFields().isEmpty()) donorFieldList.add(dataField);
                            } else {
                                generateDonorNote(item, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE)) {
                            dataField = checkDataField(donorFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorCode(item, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubFields().isEmpty()) donorFieldList.add(dataField);
                            } else {
                                generateDonorCode(item, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        }
                    }
                    dataFieldList.addAll(donorFieldList);
                    donorFieldList.clear();
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping item data ::" + item.getItemIdentifier(), ex);
            buildError(ERR_INSTANCE, item.getItemIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    /**
     * generates the subfields for the given instance and mapping options
     *
     * @param mappingOptionsBoList
     * @param instance
     * @param dataField
     */
    protected void generateSubFields(List<OLEBatchProcessProfileDataMappingOptionsBo> mappingOptionsBoList, Instance instance, DataField dataField) throws Exception {
        try {
            for (OLEBatchProcessProfileDataMappingOptionsBo mappingField : mappingOptionsBoList) {
                if (!mappingField.getDataType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_HOLDINGS)) continue;
                /*if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    generateCallNumber(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    generateCallNumberType(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    generateLocationLevel1(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    generateLocationLevel2(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    generateLocationLevel3(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    generateLocationLevel4(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    generateLocationLevel5(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    generateCallNumberPrefix(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COPY_NUMBER)) {
                    generateCopyNumber(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.SOURCE_FIELD_DATE_CREATED)) {
                    generateDateCreated(instance, getCode(mappingField.getDestinationField()), dataField);
                }*/
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping instance data ::" + instance.getInstanceIdentifier(), ex);
            buildError(ERR_INSTANCE, instance.getInstanceIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    protected void generateSubFields(List<OLEBatchProcessProfileDataMappingOptionsBo> mappingOptionsBoList, Instance instance, Item item, DataField dataField) throws Exception {
        try {
            for (OLEBatchProcessProfileDataMappingOptionsBo mappingField : mappingOptionsBoList) {
                if (!mappingField.getDataType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_ITEM)) continue;
                if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    if (item.getCallNumber() == null) continue;
                    if (callNumber != null && StringUtils.isNotEmpty(item.getCallNumber().getNumber()) && item.getCallNumber().getNumber().equals(callNumber))
                        continue;
                    generateCallNumber(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    if (item.getCallNumber() == null) continue;
                    if (callNumberType != null && item.getCallNumber().getShelvingScheme() != null && StringUtils.isNotEmpty(item.getCallNumber().getShelvingScheme().getCodeValue()) && item.getCallNumber().getShelvingScheme().getCodeValue().equals(callNumberType))
                        continue;
                    generateCallNumberType(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COPY_NUMBER)) {
                    generateCopyNumber(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_ITEM_BARCODE)) {
                    generateItemNumber(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.ITEM_TYPE)) {
                    generateItemType(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    generateLocationLevel1(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    generateLocationLevel2(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    generateLocationLevel3(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    generateLocationLevel4(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    generateLocationLevel5(item, getCode(mappingField.getDestinationField()), dataField);
                } /*else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)) {
                    getDonorPubDisp(item, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE)) {
                    getDonorNote(item, getCode(mappingField.getDestinationField()), dataField);
                }*/ else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    generateCallNumberPrefix(item, getCode(mappingField.getDestinationField()), dataField);
                } /*else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER)) {
                    generateCallNumber(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE)) {
                    generateCallNumberType(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER)) {
                    generateCopyNumber(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1)) {
                    generateLocationLevel1(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2)) {
                    generateLocationLevel2(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3)) {
                    generateLocationLevel3(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4)) {
                    generateLocationLevel4(instance, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5)) {
                    generateLocationLevel5(instance, getCode(mappingField.getDestinationField()), dataField);
                }*/
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping item data ::" + item.getItemIdentifier(), ex);
            buildError(ERR_INSTANCE, item.getItemIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    /**
     * generates subfields for the call number for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateCallNumber(Item item, String code, DataField dataField) {
        try {
            if (item != null && item.getCallNumber() != null && StringUtils.isNotEmpty(item.getCallNumber().getNumber())) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getCallNumber().getNumber());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateCallNumber()");
        }

    }

    /**
     * generates the subfields for the call number type for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateCallNumberType(Item item, String code, DataField dataField) {
        try {
            if (item != null && item.getCallNumber() != null && item.getCallNumber().getShelvingScheme() != null && StringUtils.isNotEmpty(item.getCallNumber().getShelvingScheme().getCodeValue())) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getCallNumber().getShelvingScheme().getCodeValue());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateCallNumberType()");
        }
    }

    /**
     * generates the subfields for the Item Local Identifier for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateItemLocalIdentifier(Item item, String code, DataField dataField) {
        try {
            if (item != null && item.getItemIdentifier() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(DocumentUniqueIDPrefix.getDocumentId(item.getItemIdentifier()));
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateItemLocalIdentifier()");
        }
    }

    /**
     * generates the subfields for the Holding Local Identifier for the given holding
     *
     * @param holdings
     * @param code
     * @param dataField
     */
    private void generateHoldingLocalIdentifier(OleHoldings holdings, String code, DataField dataField) {
        try {
            if (holdings != null && holdings.getHoldingsIdentifier() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(DocumentUniqueIDPrefix.getDocumentId(holdings.getHoldingsIdentifier()));
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateHoldingLocalIdentifier()");
        }
    }



    /**
         * generates the subfields for call number for the given instance
         *
         * @param holdings
         * @param code
         * @param dataField
         */
    private void generateCallNumber(OleHoldings holdings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (holdings != null && holdings.getCallNumber() != null && StringUtils.isNotEmpty(holdings.getCallNumber().getNumber())) {
                subField.setValue(holdings.getCallNumber().getNumber());
                callNumber = holdings.getCallNumber().getNumber();
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateCallNumber()");
        }

    }

    /**
     * generates the call number prefix for the given instance
     * @param holdings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateCallNumberPrefix(OleHoldings holdings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (holdings != null && holdings.getCallNumber() != null && StringUtils.isNotEmpty(holdings.getCallNumber().getPrefix())) {
                subField.setValue(holdings.getCallNumber().getPrefix());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateCallNumberPrefix()");
        }

    }

    /**
     * generates the copy number for the given instance
     * @param holdings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateCopyNumber(OleHoldings holdings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (holdings != null && StringUtils.isNotEmpty(holdings.getCopyNumber())) {
                subField.setValue(holdings.getCopyNumber());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateCopyNumber()");
        }

    }

    /**
     * generates the call number prefix for the given item
     * @param item
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateCallNumberPrefix(Item item, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (item != null && item.getCallNumber() != null && StringUtils.isNotEmpty(item.getCallNumber().getPrefix())) {
                subField.setValue(item.getCallNumber().getPrefix());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateCallNumberPrefix()");
        }

    }

    /**
     * generates the subfield for the call number type for the given instance
     *
     * @param holdings
     * @param code
     * @param dataField
     */
    private void generateCallNumberType(OleHoldings holdings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (holdings != null && holdings.getCallNumber() != null &&
                    holdings.getCallNumber().getShelvingScheme() != null && StringUtils.isNotEmpty(holdings.getCallNumber().getShelvingScheme().getCodeValue())) {
                subField.setValue(holdings.getCallNumber().getShelvingScheme().getCodeValue());
                callNumberType = holdings.getCallNumber().getShelvingScheme().getCodeValue();
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateCallNumberType()");
        }
    }

    /**
     * creates the subfields for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateCopyNumber(Item item, String code, DataField dataField) {
        try {
            if (item != null && StringUtils.isNotEmpty(item.getCopyNumber())) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getCopyNumber());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateCopyNumber()");
        }
    }

    private SubField generateMarcEnumeration(Item item, String code) {
        SubField subField = new SubField();
        subField.setCode(code);
        subField.setValue("");
        if (item != null && item.getEnumeration() != null) {
            subField.setValue(item.getEnumeration());
        }
        return subField;
    }

    /**
     * generates the subfields for all item numbers for all items for the given instance
     *
     * @param instance
     * @param code
     * @param dataField
     */
    private void generateItemNumber(Instance instance, String code, DataField dataField) {
        Items items = instance.getItems();
        for (Item item : items.getItem()) {
            generateItemNumber(item, code, dataField);
        }
    }

    /**
     * generates the subfield for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateItemNumber(Item item, String code, DataField dataField) {
        try {
            if (item != null && item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getAccessInformation().getBarcode());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateItemNumber()");
        }
    }

    /**
     * generates subfields for item type for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateItemType(Item item, String code, DataField dataField) {
        try {
            if (item != null && item.getItemType() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getItemType().getCodeValue());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateItemType()");
        }
    }

    /**
     * generates subfields for item status for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateItemStatus(Item item, String code, DataField dataField) {
        try {
            if (item != null && item.getItemStatus() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getItemStatus().getCodeValue());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateItemStatus()");
        }
    }

    /**
     * generates subfields for item type for all items in the given Instance
     *
     * @param instance
     * @param code
     * @param dataField
     */
    private void generateItemType(Instance instance, String code, DataField dataField) {
        Items items = instance.getItems();
        for (Item item : items.getItem()) {
            generateItemType(item, code, dataField);
        }
    }

    /**
     * generates the subfields for the location level -4 COLLECTION for the given instance
     *
     * @param holdings
     * @param code
     * @param dataField
     */
    private void generateLocationLevel4(OleHoldings holdings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (holdings != null && holdings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(holdings.getLocation().getLocationLevel(), "COLLECTION")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                location4 = locationLevelName;
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateLocationLevel4()");
        }

    }

    /**
     * generates the subfield for the location level 4 - COLLECTION for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateLocationLevel4(Item item, String code, DataField dataField) {
        String locationLevelName;
        try {
            if (item != null && item.getLocation() != null && (locationLevelName = getLocationLevelName(item.getLocation().getLocationLevel(), "COLLECTION")) != null) {
                //if (location4 != null && location4.equals(locationLevelName)) return;
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateLocationLevel4()");
        }

    }

    /**
     * gets the locationlevel name for the given level and locationlevel
     *
     * @param locationLevel
     * @param level
     * @return
     */
    private String getLocationLevelName(LocationLevel locationLevel, String level) {
        if (locationLevel == null || StringUtils.isEmpty(locationLevel.getLevel())) return null;
        if (locationLevel.getLevel().toUpperCase().startsWith(level)) return locationLevel.getName();
        return getLocationLevelName(locationLevel.getLocationLevel(), level);
    }

    /**
     * generates subfield for location level 3 - LIBRARY for the given instance
     *
     * @param holdings
     * @param code
     * @param dataField
     */
    private void generateLocationLevel3(OleHoldings holdings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (holdings != null && holdings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(holdings.getLocation().getLocationLevel(), "LIBRARY")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                location3 = locationLevelName;
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateLocationLevel3()");
        }
    }

    /**
     * generates subfield for location level 2 - CAMPUS for the given instance
     *
     * @param holdings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLocationLevel2(OleHoldings holdings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (holdings != null && holdings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(holdings.getLocation().getLocationLevel(), "CAMPUS")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateLocationLevel2()");
        }
    }

    /**
     * generates subfield for location level 1 - INSTITUTION for the given instance
     *
     * @param holdings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLocationLevel1(OleHoldings holdings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (holdings != null && holdings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(holdings.getLocation().getLocationLevel(), "INSTITUTION")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateLocationLevel1()");
        }
    }

    /**
     * generates subfield for location level 5 - SHELVING for the given instance
     *
     * @param holdings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLocationLevel5(OleHoldings holdings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (holdings != null && holdings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(holdings.getLocation().getLocationLevel(), "SHELVING")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateLocationLevel5()");
        }
    }

    /**
     * generates the subfields for the location level 3 - LIBRARY for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateLocationLevel3(Item item, String code, DataField dataField) {
        String locationLevelName;
        try {
            if (item != null && item.getLocation() != null && (locationLevelName = getLocationLevelName(item.getLocation().getLocationLevel(), "LIBRARY")) != null) {
                //if (location3 != null && location3.equals(locationLevelName)) return;
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateLocationLevel3()");
        }
    }

    /**
     * generates the subfields for the location level 2 - CAMPUS for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateLocationLevel2(Item item, String code, DataField dataField) {
        String locationLevelName;
        try {
            if (item != null && item.getLocation() != null && (locationLevelName = getLocationLevelName(item.getLocation().getLocationLevel(), "CAMPUS")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateLocationLevel2()");
        }
    }

    /**
     * generates the subfields for the location level 1 - INSTITUTION for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateLocationLevel1(Item item, String code, DataField dataField) {
        String locationLevelName;
        try {
            if (item != null && item.getLocation() != null && (locationLevelName = getLocationLevelName(item.getLocation().getLocationLevel(), "INSTITUTION")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateLocationLevel1()");
        }
    }

    /**
     * generates the subfields for the location level 5 - SHELVING for the given item
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateLocationLevel5(Item item, String code, DataField dataField) {
        String locationLevelName;
        try {
            if (item != null && item.getLocation() != null && (locationLevelName = getLocationLevelName(item.getLocation().getLocationLevel(), "SHELVING")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateLocationLevel5()");
        }
    }

    /**
     * generates the subfields for the Date Created for the given holdings
     *
     * @param holdings
     * @param code
     * @param dataField
     */
    private void generateDateCreated(Holdings holdings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (null != holdings && null != holdings.getCreatedOn()) {
                subField.setValue(holdings.getCreatedOn());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(holdings, ex, "generateDateCreated()");
        }
    }

    /**
     * generates the subfield for the item donor public display
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateDonorPublicDisplay(Item item, DonorInfo donorInfo, String code, DataField dataField) {
        try {
            if (null != donorInfo) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(donorInfo.getDonorPublicDisplay());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateDonorPublicDisplay()");
        }
    }

    /**
     * generates the subfield for the item donor note
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateDonorNote(Item item, DonorInfo donorInfo, String code, DataField dataField) {
        try {
            if (null != donorInfo) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(donorInfo.getDonorNote());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateDonorNote()");
        }
    }

    /**
     * generates the subfield for the item donor code
     *
     * @param item
     * @param code
     * @param dataField
     */
    private void generateDonorCode(Item item, DonorInfo donorInfo, String code, DataField dataField) {
        try {
            if (null != donorInfo) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(donorInfo.getDonorCode());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateDonorCode()");
        }
    }

    /**
     * generates subfield for Enumeration for the given item
     * @param item
     * @param code
     * @param dataField
     */
    private void generateEnumeration(Item item, String code, DataField dataField) {
        try {
            if (item != null && StringUtils.isNotEmpty(item.getEnumeration())) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getEnumeration());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateEnumeration()");
        }
    }

    /**
     * generates subfield for Chronology for the given item
     * @param item
     * @param code
     * @param dataField
     */
    private void generateChronology(Item item, String code, DataField dataField) {
        try {
            if (item != null && StringUtils.isNotEmpty(item.getChronology())) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getChronology());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateChronology()");
        }
    }

    /**
     * generates subfield for Vendor line item identifier for the given item
     * @param item
     * @param code
     * @param dataField
     */
    private void generateVendorLineItemIdentifier(Item item, String code, DataField dataField) {
        try {
            if (item != null && StringUtils.isNotEmpty(item.getVendorLineItemIdentifier())) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(item.getVendorLineItemIdentifier());
                addDataFieldForItem(dataField, subField);
            }
        } catch (Exception ex) {
            logError(item, ex, "generateVendorLineItemIdentifier()");
        }
    }

    /**
     * Logs error for exception happening for holdings or item mapping
     *
     * @param object
     * @param ex
     */
    private void logError(Object object, Exception ex, String method) {
        if (object != null) {
            if (object instanceof OleHoldings) {
                OleHoldings oleHoldings = (OleHoldings) object;
                LOG.error("Error while " + method + " for holding::" + oleHoldings.getHoldingsIdentifier(), ex);
                buildError(ERR_HOLDING, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), " ::At:: ", method, TIME_STAMP, new Date().toString());
                try {
                    throw ex;
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else if (object instanceof Holdings) {
                Holdings holdings = (Holdings) object;
                OleHoldings oleHoldings = null;
                if (holdings.getContent() != null && !holdings.getContent().isEmpty()) {
                    oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                } else {
                    oleHoldings = holdings.getContentObject();
                }
                LOG.error("Error while " + method + " for instance::" + oleHoldings.getHoldingsIdentifier(), ex);
                buildError(ERR_HOLDING, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), " ::At:: ", method, TIME_STAMP, new Date().toString());
                // buildError(ERR_INSTANCE,instance.getInstanceIdentifier(),ERR_CAUSE,ex.getMessage()," ::At:: ",method,TIME_STAMP,new Date().toString());
                try {
                    throw ex;
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else if (object instanceof Item) {
                Item item = (Item) object;
                LOG.error("Error while " + method + " for item data ::" + item.getItemIdentifier(), ex);
                buildError(ERR_ITEM, item.getItemIdentifier(), ERR_CAUSE, ex.getMessage(), " ::At:: ", method, TIME_STAMP, new Date().toString());
            }
        }
    }

    /*private void logError(Item item, Exception ex, String method) {
        LOG.error("Error while " + method + " for item data ::" + item.getItemIdentifier(), ex);
        buildError(ERR_ITEM, item.getItemIdentifier(), ERR_CAUSE, ex.getMessage(), " ::At:: ", method, TIME_STAMP, new Date().toString());
    }*/

    private void buildError(String... errorString) {
        for (String str : errorString) {
            errBuilder.append(str).append(COMMA);
        }
        errBuilder.append(lineSeparator);
    }


    private void addDataField(DataField dataField, SubField subField) {
        if (StringUtils.isEmpty(subField.getValue())) return;
        dataField.getSubFields().add(subField);
    }

    private void addDataFieldForItem(DataField dataField, SubField subField) {
        if (StringUtils.isEmpty(subField.getValue())) return;
        dataField.getSubFields().add(subField);
    }

    protected String getTag(String mappingField) {
        return StringUtils.trim(StringUtils.substringBefore(mappingField, "$"));
    }

    private String getCode(String mappingField) {
        return StringUtils.trim(StringUtils.substringAfter(mappingField, "$"));
    }

    public String getTagForExportFilter(String mappingField) {
        return StringUtils.trim(StringUtils.substringBefore(mappingField, " "));
    }

    public String getCodeForExportFilter(String mappingField) {
        int index = mappingField.indexOf('$');
        return String.valueOf(mappingField.charAt(index + 1));
    }
}
