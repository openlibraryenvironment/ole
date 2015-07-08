package org.kuali.ole.batch.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileMatchPoint;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 7/1/14
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchBibImportUtil {


    private static DocstoreClientLocator docstoreClientLocator;

    public static DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public static String getBibDataFieldValue(BibMarcRecord bibMarcRecord, String enteredDataField) {
        String[] dataFiledsArray = enteredDataField.split(" ");
        String entryDataField = null;
        String entryInds = null;
        String entrySubField = null;
        if (dataFiledsArray.length == 3) {
            entryDataField = dataFiledsArray[0];
            entryInds = dataFiledsArray[1];
            entrySubField = dataFiledsArray[2];
        } else if (dataFiledsArray.length == 2) {
            entryDataField = dataFiledsArray[0];
            entryInds = "##";
            entrySubField = dataFiledsArray[1];
        } else if (dataFiledsArray.length == 1) {
            entryDataField = dataFiledsArray[0];
            if (entryDataField.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CONTROL_FIELD_001)) {
                for (ControlField controlField : bibMarcRecord.getControlFields()) {
                    if (controlField.getTag().equalsIgnoreCase(OLEConstants.OLEBatchProcess.CONTROL_FIELD_001)) {
                        return controlField.getValue();
                    }
                }
                return null;
            }
        }
        List<String> dataFields = new ArrayList<>();
        String ind1 = "";
        String ind2 = "";
        String dataFieldInds = "";
        for (DataField dataField : bibMarcRecord.getDataFields()) {
            if (dataField.getTag().equalsIgnoreCase(entryDataField)) {
                ind1 = dataField.getInd1();
                ind2 = dataField.getInd2();
                if (entryInds.equals("##")) {
                    ind1 = "#";
                    ind2 = "#";
                } else if (entryInds.startsWith("#")) {
                    ind1 = "#";
                } else if (entryInds.endsWith("#")) {
                    ind2 = "#";
                }
                if (ind1.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind1) || ind1.equalsIgnoreCase("\\")) ind1 = "#";
                if (ind2.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind2) || ind2.equalsIgnoreCase("\\")) ind2 = "#";
                dataFieldInds = ind1 + ind2;
                if (dataFieldInds.equalsIgnoreCase(entryInds)) {
                    if (StringUtils.isNotEmpty(entrySubField)) {
                        StringBuffer subFieldValue = new StringBuffer("");
                        entrySubField = entrySubField.replace("$", " ");
                        String[] subFieldsArray = entrySubField.split(" ");
                        for (int i = 1; i < subFieldsArray.length; i++) {
                            String code = subFieldsArray[i];
                            for (SubField subField : dataField.getSubFields()) {
                                if (subField.getCode().equalsIgnoreCase(code)) {
                                    subFieldValue.append(subField.getValue());
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(subFieldValue.toString())) {
                            dataFields.add(subFieldValue.toString());
                        }

                    }
                }
            }
        }
        String dataFieldValue = null;

        if (dataFields != null && dataFields.size() > 0) {
            dataFieldValue = dataFields.get(0);
        }
        return dataFieldValue;
    }

    public static String getDataFieldValue(DataField dataField, String enteredDataField) {
        String[] dataFiledsArray = enteredDataField.split(" ");
        String entryDataField = null;
        String entryInds = null;
        String entrySubField = null;
        if (dataFiledsArray.length == 3) {
            entryDataField = dataFiledsArray[0];
            entryInds = dataFiledsArray[1];
            entrySubField = dataFiledsArray[2];
        } else if (dataFiledsArray.length == 2) {
            entryDataField = dataFiledsArray[0];
            entryInds = "##";
            entrySubField = dataFiledsArray[1];
        }
        StringBuffer subFieldValue = new StringBuffer("");
        String ind1 = "";
        String ind2 = "";
        String dataFieldInds = "";

        if (dataField.getTag().equalsIgnoreCase(entryDataField)) {
            ind1 = dataField.getInd1();
            ind2 = dataField.getInd2();
            if (entryInds.equals("##")) {
                ind1 = "#";
                ind2 = "#";
            } else if (entryInds.startsWith("#")) {
                ind1 = "#";
            } else if (entryInds.endsWith("#")) {
                ind2 = "#";
            }
            if (ind1.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind1) || ind1.equalsIgnoreCase("\\")) ind1 = "#";
            if (ind2.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind2) || ind2.equalsIgnoreCase("\\")) ind2 = "#";
            dataFieldInds = ind1 + ind2;
            if (dataFieldInds.equalsIgnoreCase(entryInds)) {
                if (StringUtils.isNotEmpty(entrySubField)) {
                    entrySubField = entrySubField.replace("$", " ");
                    String[] subFieldsArray = entrySubField.split(" ");
                    for (int i = 1; i < subFieldsArray.length; i++) {
                        String code = subFieldsArray[i];
                        for (SubField subField : dataField.getSubFields()) {
                            if (subField.getCode().equalsIgnoreCase(code)) {
                                subFieldValue.append(subField.getValue());
                            }
                        }
                    }
                }
            }
        }
        return subFieldValue.toString();
    }


    public static void buildLocationLevels(List<HoldingsTree> holdingsTreeList) {
        List<String> matchingLocationLevel = getLocationLevel();
        for (HoldingsTree holdingsTree : holdingsTreeList) {
            Holdings holdings = holdingsTree.getHoldings();
            holdings.setLevel1Location(matchingLocationLevel.get(0));
            holdings.setLevel2Location(matchingLocationLevel.get(1));
            holdings.setLevel3Location(matchingLocationLevel.get(2));
            holdings.setLevel4Location(matchingLocationLevel.get(3));
            holdings.setLevel5Location(matchingLocationLevel.get(4));
            for (Item item : holdingsTree.getItems()) {
                item.setLevel1Location(matchingLocationLevel.get(0));
                item.setLevel2Location(matchingLocationLevel.get(1));
                item.setLevel3Location(matchingLocationLevel.get(2));
                item.setLevel4Location(matchingLocationLevel.get(3));
                item.setLevel5Location(matchingLocationLevel.get(4));
            }

        }
    }


    public static void buildLocationLevels(Holdings holdings) {
        List<String> matchingLocationLevel = getLocationLevel();
        holdings.setLevel1Location(matchingLocationLevel.get(0));
        holdings.setLevel2Location(matchingLocationLevel.get(1));
        holdings.setLevel3Location(matchingLocationLevel.get(2));
        holdings.setLevel4Location(matchingLocationLevel.get(3));
        holdings.setLevel5Location(matchingLocationLevel.get(4));
    }

    public static void buildLocationLevels(Item item) {
        List<String> matchingLocationLevel = getLocationLevel();
        item.setLevel1Location(matchingLocationLevel.get(0));
        item.setLevel2Location(matchingLocationLevel.get(1));
        item.setLevel3Location(matchingLocationLevel.get(2));
        item.setLevel4Location(matchingLocationLevel.get(3));
        item.setLevel5Location(matchingLocationLevel.get(4));
    }


    private static List<String> getLocationLevel() {
        List<String> locationLevelName = new ArrayList<>();
        List<OleLocationLevel> locationLevel = (List<OleLocationLevel>) KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleLocationLevel.class, OLEConstants.LEVEL_ID, true);
        for (OleLocationLevel oleLocationLevel : locationLevel) {
            locationLevelName.add(oleLocationLevel.getLevelName());
        }
        return locationLevelName;
    }


    /**
     * Getting Sub field value
     *
     * @param bibField
     * @param dataField
     * @return
     */
    public static String getSubFieldValue(String bibField, DataField dataField) {
        String subFieldValue = "";
        DataField dataFieldObj = getDataField(bibField);
        for (SubField subField : dataField.getSubFields()) {
            for (SubField bibSubField : dataFieldObj.getSubFields()) {
                if (subField.getCode().equalsIgnoreCase(bibSubField.getCode())) {
                    subFieldValue = subFieldValue + subField.getValue();
                }
            }
        }

        return subFieldValue;
    }

    public static HoldingsTree buildHoldingsTree(String holdingsType) {
        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = null;
        if (holdingsType.equals(DocType.HOLDINGS.getCode())) {
            holdings = new PHoldings();
        }
        if (holdingsType.equals(DocType.EHOLDINGS.getCode())) {
            holdings = new org.kuali.ole.docstore.common.document.EHoldings();
        }

        holdingsTree.setHoldings(holdings);
        return holdingsTree;
    }


    /**
     * Performs to get match data fields list based on enter data enter field from bib record
     *
     * @param bibMarcRecord
     * @param enteredDataField
     * @return
     */
    public static List<String> getMatchedDataField(BibMarcRecord bibMarcRecord, String enteredDataField) {
        String[] dataFiledsArray = enteredDataField.split(" ");
        String entryDataField = null;
        String entryInds = null;
        String entrySubField = null;
        if (dataFiledsArray.length == 3) {
            entryDataField = dataFiledsArray[0];
            entryInds = dataFiledsArray[1];
            entrySubField = dataFiledsArray[2];
        } else if (dataFiledsArray.length == 2) {
            entryDataField = dataFiledsArray[0];
            entryInds = "##";
            entrySubField = dataFiledsArray[1];
        }
        List<String> dataFields = new ArrayList<>();
        String ind1 = "";
        String ind2 = "";
        String dataFieldInds = "";
        for (DataField dataField : bibMarcRecord.getDataFields()) {
            if (dataField.getTag().equalsIgnoreCase(entryDataField)) {
                ind1 = dataField.getInd1();
                ind2 = dataField.getInd2();
                if (entryInds.equals("##")) {
                    ind1 = "#";
                    ind2 = "#";
                } else if (entryInds.startsWith("#")) {
                    ind1 = "#";
                } else if (entryInds.endsWith("#")) {
                    ind2 = "#";
                }
                if (ind1.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind1) || ind1.equalsIgnoreCase("\\")) ind1 = "#";
                if (ind2.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind2) || ind2.equalsIgnoreCase("\\")) ind2 = "#";
                dataFieldInds = ind1 + ind2;
                if (dataFieldInds.equalsIgnoreCase(entryInds)) {
                    if (StringUtils.isNotEmpty(entrySubField)) {
                        StringBuffer subFieldValue = new StringBuffer("");
                        entrySubField = entrySubField.replace("$", " ");
                        String[] subFieldsArray = entrySubField.split(" ");
                        for (int i = 1; i < subFieldsArray.length; i++) {
                            String code = subFieldsArray[i];
                            for (SubField subField : dataField.getSubFields()) {
                                if (subField.getCode().equalsIgnoreCase(code)) {
                                    subFieldValue.append(subField.getValue());
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(subFieldValue.toString())) {
                            dataFields.add(subFieldValue.toString());
                        }

                    }
                }
            }
        }
        return dataFields;
    }

    public static Object getHoldings(HoldingsTree holdingsTree, String docType) {
        Holdings holdings = null;
        if ((docType.equals(DocType.HOLDINGS.getCode()) && holdingsTree.getHoldings() instanceof PHoldings)) {
            holdings = holdingsTree.getHoldings();
        } else if ((docType.equals(DocType.EHOLDINGS.getCode()) && holdingsTree.getHoldings() instanceof EHoldings)) {
            holdings = holdingsTree.getHoldings();
        }

        return holdings;
    }


    public static List<Item> getItem(List<HoldingsTree> holdingsTreeList) {
        List<Item> items = null;
        for (HoldingsTree holdingsTree : holdingsTreeList) {
            if (holdingsTree.getHoldings() instanceof PHoldings) {
                items = holdingsTree.getItems();

            }
        }
        return items;
    }


    public static List<OLEBatchProcessProfileMatchPoint> buildMatchPointListByDataType(List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileMatchPointList, String dataType) {
        List<OLEBatchProcessProfileMatchPoint> matchPointList = new ArrayList<>();
        for (OLEBatchProcessProfileMatchPoint profileMatchPoint : oleBatchProcessProfileMatchPointList) {
            if (profileMatchPoint.getMatchPointType().equalsIgnoreCase(dataType)) {
                matchPointList.add(profileMatchPoint);
            }
        }
        return matchPointList;
    }

    public static HoldingsTrees getHoldingsTrees(List<String> bibIds) {
        HoldingsTrees holdingsTrees = null;
        try {
            holdingsTrees = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsDocTrees(bibIds);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return holdingsTrees;
    }

    public static HoldingsTrees getHoldingsTrees(String bibId) {
        List<String> bibIds = new ArrayList<>();
        bibIds.add(bibId);
        return getHoldingsTrees(bibIds);

    }

    public static List<String> getItemIds(String holdingId) {
        HoldingsTree holdingsTree = null;
        try {
            holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(holdingId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> itemsIds = new ArrayList<>();

        for (Item item : holdingsTree.getItems()) {
            itemsIds.add(item.getId());
        }

        return itemsIds;
    }

    public static List<HoldingsTree> getHoldingsTrees(Holdings holdings) {
        List<HoldingsTree> holdingsTreeList = new ArrayList<>();
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(holdings);
        holdingsTreeList.add(holdingsTree);
        return holdingsTreeList;
    }

    /**
     * Performs the get match record value for search in doc store
     *
     * @param dataField
     * @return
     */
    public static String getDataFieldWithout$(String dataField) {
        String dataFieldWithout$ = null;
        if (OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equals(dataField)) {
            return OLEConstants.OLEBatchProcess.CONTROL_FIELD_NAME_001;
        } else {

            String[] matchRecordSplit = dataField.split(" ");
            String fullSubField = matchRecordSplit[matchRecordSplit.length - 1];
            dataFieldWithout$ = matchRecordSplit[0] + fullSubField.substring(fullSubField.length() - 1);
        }
        return OLEConstants.PREFIX_FOR_DATA_FIELD + dataFieldWithout$;
    }

    /**
     *
     * @param dataFieldString
     * @return
     */
    public static DataField getDataField(String dataFieldString) {
        DataField dataField = new DataField();
        if (dataFieldString != null) {
            String[] dataFieldArrray = dataFieldString.split(" ");

            dataField.setTag(dataFieldArrray[0]);
            List<SubField> subFields = new ArrayList<>();
            if (dataFieldArrray.length > 2) {
                dataField.setInd1(dataFieldArrray[1].substring(0, 1));
                dataField.setInd2(dataFieldArrray[1].substring(1));
                String[] subFiledArray = dataFieldArrray[2].split("\\$");
                for (String subFieldString : subFiledArray) {
                    if (StringUtils.isNotEmpty(subFieldString)) {
                    SubField subField = new SubField();
                    subField.setCode(subFieldString);
                    subFields.add(subField);
                    }
                }
            } else {
                if (dataFieldArrray.length == 2) {
                    String[] subFiledArray = dataFieldArrray[1].split("\\$");
                    for (String subFieldString : subFiledArray) {
                        if (StringUtils.isNotEmpty(subFieldString)) {
                            SubField subField = new SubField();
                            subField.setCode(subFieldString);
                            subFields.add(subField);
                        }
                    }
                }
            }
            dataField.setSubFields(subFields);

        }

        return dataField;

    }


    public static boolean isItemHoldingMapping(Map<String, String> itemHoldingsMapping) {

        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER)) {
            return true;
        }

        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_PREFIX)) {
            return true;
        }
        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_CALL_NUMBER_TYPE)) {
            return true;
        }

        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_COPY_NUMBER)) {
            return true;
        }

        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_1)) {
            return true;
        }
        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_2)) {
            return true;
        }
        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_3)) {
            return true;
        }
        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_4)) {
            return true;
        }
        if (itemHoldingsMapping.containsKey(Item.DESTINATION_FIELD_ITEM_HOLDINGS_LOCATION_LEVEL_5)) {
            return true;
        }
        return false;
    }

    /**
     * This method checks the existence of a '245' data field with a sub field 'a' in the bib marc record.
     * @param bibRecord
     * @return
     */
    public static boolean has245aDataField(BibMarcRecord bibRecord) {
        DataField dataField245 = bibRecord.getDataFieldForTag(OLEConstants.MARC_EDITOR_TITLE_245);
        if (dataField245 != null) {
            if (CollectionUtils.isNotEmpty(dataField245.getSubFields())) {
                for (SubField subField : dataField245.getSubFields()) {
                    if (!subField.getCode().isEmpty()) {
                        if (subField.getCode().equals(OLEConstants.A)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param dataFieldString
     * @param value
     * @return
     */
    public static DataField buildDataField(String dataFieldString, String value) {
        DataField dataField = new DataField();
        if (dataFieldString != null) {
            String[] dataFieldArrray = dataFieldString.split(" ");

            dataField.setTag(dataFieldArrray[0]);
            List<SubField> subFields = new ArrayList<>();
            if (dataFieldArrray.length > 2) {
                dataField.setInd1(dataFieldArrray[1].substring(0, 1));
                dataField.setInd2(dataFieldArrray[1].substring(1));
                String[] subFiledArray = dataFieldArrray[2].split("\\$");
                for (String subFieldString : subFiledArray) {
                    if (StringUtils.isNotEmpty(subFieldString)) {
                        SubField subField = new SubField();
                        subField.setCode(subFieldString);
                        subField.setValue(value);
                        subFields.add(subField);
                    }
                }
            } else {
                if (dataFieldArrray.length == 2) {
                    String[] subFiledArray = dataFieldArrray[1].split("\\$");
                    for (String subFieldString : subFiledArray) {
                        if (StringUtils.isNotEmpty(subFieldString)) {
                            SubField subField = new SubField();
                            dataField.setInd1(" ");
                            dataField.setInd2(" ");
                            subField.setCode(subFieldString);
                            subField.setValue(value);
                            subFields.add(subField);
                        }
                    }
                }
            }
            dataField.setSubFields(subFields);
        }
        return dataField;
    }

    public static List<DataField> getMatchedUrlDataFields(String tag, BibMarcRecord bibRecord){
        List<DataField> dataFieldList = new ArrayList<>();
        for(DataField dataField:bibRecord.getDataFields()){
            if(dataField.getTag().equalsIgnoreCase(tag)){
                dataFieldList.add(dataField);
            }
        }
        return dataFieldList;
    }
}
