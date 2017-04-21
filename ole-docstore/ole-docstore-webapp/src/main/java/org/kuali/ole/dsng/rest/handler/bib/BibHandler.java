package org.kuali.ole.dsng.rest.handler.bib;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.util.BibMarcUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by pvsubrah on 1/4/16.
 */
public abstract class BibHandler extends Handler {

    private List<BibHandler> bibMetaDetaHandler;

    private static final String SEPERATOR_SUB_FIELD = " ";

    private static final String SEPERATOR_HYPHEN = " - ";
    private static final String PATTERN_CHAR = "*";

    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange){

    }


    public Record isValidLeader(String marcContent) throws Exception {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(marcContent);
        String leader = null;
        for (Record record : records) {
            leader = record.getLeader().marshal();
            char unicode = leader.charAt(9);
            if (unicode != 'a') {
                return null;
            }
            return record;
        }
        return null;
    }


    public Record isValidLeaderCheck(String marcContent) throws Exception {
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(marcContent);
        String leader = null;
        for (Record record : records) {
            leader = record.getLeader().marshal();
            char unicode = leader.charAt(9);
            if (unicode != 'a') {
                throw new Exception(OleNGConstants.INVALID_LEADER + leader);
            }
            return record;
        }
        return null;
    }


    public String process001And003(Record record, String bibId) {
        replaceBibIdTo001Tag(record, bibId);
        replaceOrganizationCodeTo003Tag(record);
        return getMarcRecordUtil().convertMarcRecordToMarcContent(record);
    }

    public void replaceBibIdTo001Tag(Record record,String bibId) {
        if(getMarcRecordUtil().hasField(record, OleNGConstants.TAG_001)) {
            getMarcRecordUtil().updateControlFieldValue(record, OleNGConstants.TAG_001,bibId);
        } else {
            // If 001 tag is not available creating tag
            getMarcRecordUtil().addControlField(record, OleNGConstants.TAG_001,bibId);
        }
    }

    public void replaceOrganizationCodeTo003Tag(Record record) {
        String organizationCode = ConfigContext.getCurrentContextConfig().getProperty("organization.marc.code");
        if(getMarcRecordUtil().hasField(record, OleNGConstants.TAG_003)) {
            getMarcRecordUtil().updateControlFieldValue(record, OleNGConstants.TAG_003,organizationCode);
        } else {
            // If 003 tag is not available creating tag
            getMarcRecordUtil().addControlField(record, OleNGConstants.TAG_003,organizationCode);
        }
    }

    public BibRecord setDataMappingValues(BibRecord bibRecord, JSONObject requestJsonObject, Exchange exchange) {
        try {
            if (requestJsonObject.has(OleNGConstants.DATAMAPPING)) {
                JSONArray dataMappings = requestJsonObject.getJSONArray(OleNGConstants.DATAMAPPING);
                JSONObject dataMapping = dataMappings.getJSONObject(0);
                Map<String, Object> dataMappingsMap = new ObjectMapper().readValue(dataMapping.toString(), new TypeReference<Map<String, Object>>() {});
                for (Iterator iterator3 = dataMappingsMap.keySet().iterator(); iterator3.hasNext(); ) {
                    String key1 = (String) iterator3.next();
                    for (Iterator<BibHandler> iterator4 = getBibMetaDetaHandler().iterator(); iterator4.hasNext(); ) {
                        BibHandler bibHandler = iterator4.next();
                        if (bibHandler.isInterested(key1)) {
                            bibHandler.setBusinessObjectService(getBusinessObjectService());
                            bibHandler.processDataMappings(dataMapping, exchange);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bibRecord;
    }

    public List<BibHandler> getBibMetaDetaHandler() {
        if(null == bibMetaDetaHandler) {
            bibMetaDetaHandler = new ArrayList<BibHandler>();
            bibMetaDetaHandler.add(new BibStatusHandler());
            bibMetaDetaHandler.add(new StaffOnlyHandler());
        }
        return bibMetaDetaHandler;
    }

    public void saveBibInfoRecord(BibRecord bibRecord, boolean create) {
        String content = bibRecord.getContent();
        List<Record> records = getMarcRecordUtil().convertMarcXmlContentToMarcRecord(content);
        BibInfoRecord bibInfoRecord = null;
        if(!create) {
            bibInfoRecord = getBusinessObjectService().findBySinglePrimaryKey(BibInfoRecord.class,bibRecord.getBibId());
        }
        if(CollectionUtils.isNotEmpty(records)) {
            if(null == bibInfoRecord) {
                bibInfoRecord = new BibInfoRecord();
            }
            Record record = records.get(0);
            Map<String, String> dataFields = buildDataValuesForBibInfo(record);
            String bibId = DocumentUniqueIDPrefix.getDocumentId(bibRecord.getBibId());
            bibInfoRecord.setBibId(Integer.valueOf(bibId));
            bibInfoRecord.setBibIdStr(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, bibId));
            bibInfoRecord.setTitle(truncateData(dataFields.get(DocstoreConstants.TITLE_DISPLAY), 4000));
            bibInfoRecord.setAuthor(truncateData(dataFields.get(DocstoreConstants.AUTHOR_DISPLAY), 4000));
            bibInfoRecord.setPublisher(truncateData(dataFields.get(DocstoreConstants.PUBLISHER_DISPLAY), 4000));
            String isbn = truncateData(dataFields.get(DocstoreConstants.ISBN_DISPLAY), 100);
            String issn = truncateData(dataFields.get(DocstoreConstants.ISSN_DISPLAY), 100);

            if (org.apache.commons.lang.StringUtils.isNotEmpty(isbn)) {
                bibInfoRecord.setIsxn(isbn);
            } else {
                bibInfoRecord.setIsxn(issn);
            }

            getBusinessObjectService().save(bibInfoRecord);
        }
    }

    public Map<String, String> buildDataValuesForBibInfo(Record record) {
        Map<String, String> dataFields = new HashMap<String,String>();


        String titleInclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(TITLE_DISPLAY);
        String titleExclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(TITLE_DISPLAY);
        String title = this.getDataFieldValue(titleInclude, titleExclude, record, false, TITLE_DISPLAY);

        String authorInclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(AUTHOR_DISPLAY);
        String authorExclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(AUTHOR_DISPLAY);
        String author = this.getDataFieldValue(authorInclude, authorExclude, record, false, AUTHOR_DISPLAY);

        String publisherInclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(PUBLISHER_DISPLAY);
        String publisherExclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(PUBLISHER_DISPLAY);
        String publisher = this.getDataFieldValue(publisherInclude, publisherExclude, record, false, PUBLISHER_DISPLAY);

        String isbnInclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(ISBN_DISPLAY);
        String isbnExclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(ISBN_DISPLAY);
        String isbn = this.getDataFieldValue(isbnInclude, isbnExclude, record, false, ISBN_DISPLAY);


        String issnInclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(ISSN_DISPLAY);
        String issnExclude = BibMarcUtil.documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(ISSN_DISPLAY);
        String issn = this.getDataFieldValue(issnInclude, issnExclude, record, false, ISSN_DISPLAY);


        dataFields.put(TITLE_DISPLAY, title);
        dataFields.put(AUTHOR_DISPLAY, author);
        dataFields.put(PUBLISHER_DISPLAY, publisher);
        dataFields.put(ISBN_DISPLAY, isbn);
        dataFields.put(ISSN_DISPLAY, issn);

        return dataFields;
    }

    public String getDataFieldValue(String includeTags, String excludeTags, Record record,
                                    boolean isHyphenSeperatorFirst, String fieldName) {
        List<String> fieldValues = new ArrayList<String>();
        StringTokenizer includeTagsTokenizer = new StringTokenizer(includeTags, ",");

        while (includeTagsTokenizer.hasMoreElements()) {
            String tag = includeTagsTokenizer.nextToken();
            tag = tag.trim();
            int subFieldIdx = tag.indexOf('-');
            String tagNum = (subFieldIdx == -1) ? tag : tag.substring(0, subFieldIdx);

            for (int i = 0; i < record.getDataFields().size(); i++) {
                DataField dataField = record.getDataFields().get(i);
                if (isValidTag(dataField.getTag(), tagNum)) {
                    StringBuilder fieldValue = new StringBuilder();
                    List<Subfield> subFields = dataField.getSubfields();
                    if (subFieldIdx != -1) { // Includes only one Sub Field of a main Data Field.
                        if (excludeTags != null && !excludeTags.contains(tag)) {
                            String subFieldCodes = tag.substring(subFieldIdx + 1, tag.length());
                            boolean isHyphenCodedOnce = false;
                            for (Subfield subField : subFields) {
                                if (StringUtils.isNotBlank(subFieldCodes) && subFieldCodes.charAt(0) == (subField.getCode())) {
                                    if (fieldValue.length() != 0) {
                                        if (!isHyphenSeperatorFirst || isHyphenCodedOnce || (
                                                dataField.getTag().endsWith("00") || dataField.getTag().endsWith("10")
                                                        || dataField.getTag().endsWith("11"))) {
                                            fieldValue.append(SEPERATOR_SUB_FIELD);
                                        } else {
                                            fieldValue.append(SEPERATOR_HYPHEN);
                                            isHyphenCodedOnce = true;
                                        }
                                    }
                                    fieldValue.append(subField.getData());
                                }
                            }
                        }
                    } else { // Includes whole Data Field i.e includes All Sub Fields in a datafield
                        boolean isHyphenCodedOnce = false;
                        boolean isFirstSubfield = false;
                        for (Subfield subField : subFields) {
                            if (excludeTags != null &&  !excludeTags.contains(dataField.getTag() + "-" + subField.getCode()) && !excludeTags
                                    .contains(tagNum + "-" + subField.getCode())) {
                                if (fieldValue.length() != 0) {
                                    if (!isHyphenSeperatorFirst || isHyphenCodedOnce || (
                                            dataField.getTag().endsWith("00") || dataField.getTag().endsWith("10")
                                                    || dataField.getTag().endsWith("11"))) {
                                        fieldValue.append(SEPERATOR_SUB_FIELD);
                                    } else {

                                        fieldValue.append(SEPERATOR_HYPHEN);
                                        isHyphenCodedOnce = true;
                                    }
                                }
                                fieldValue.append(subField.getData());
                            }
                        }
                    }
                    if ((dataField.getTag().equalsIgnoreCase("650") || dataField.getTag().equalsIgnoreCase("651"))
                            && fieldValue != null && fieldValue.length() > 1 && fieldValue.toString().trim().length() > 1) {
                        String fieldVal = fieldValue.toString().trim();
                        String lastChar = String.valueOf(fieldVal.charAt(fieldVal.length() - 1));
                        if (!lastChar.equalsIgnoreCase(".")) {
                            fieldValue.append(".");
                        }
                    }
                    fieldValues.add(fieldValue.toString().trim());
                }
            }
        }
        if (fieldValues.size() == 1) {
            return fieldValues.get(0);
        } else if (fieldValues.size() > 0) {
            return fieldValues.get(0);
        } else {
            return null;
        }
    }

    private boolean isValidTag(String tag, String tagFormat) {
        try {
            if (!tagFormat.contains(PATTERN_CHAR)) {
                return tagFormat.equals(tag);
            } else {
                int idx = tagFormat.lastIndexOf(PATTERN_CHAR);
                return isValidTag(tag.substring(0, idx) + tag.substring(idx + PATTERN_CHAR.length(), tag.length()), tagFormat.substring(0, idx)
                        + tagFormat.substring(idx + PATTERN_CHAR.length(), tagFormat.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String truncateData(String data, int idLength) {
        //TODO: Handle the case of unicode chars where string.length() <> byte length
        String truncateData = data;
        if (data != null && data.length() > idLength) {
            truncateData = data.substring(0, (idLength-1));
        }
        return truncateData;
    }

    public void processIfDeleteAllExistOpsFound(BibRecord bibRecord, JSONObject requestJsonObject, Exchange exchange) {
        ArrayList<HoldingsRecord> holdingsListToDelete = new ArrayList<HoldingsRecord>();

        ArrayList<HoldingsRecord> listOfHoldingsToDelete = getListOfHoldingsToDelete(bibRecord, requestJsonObject);
        holdingsListToDelete.addAll(listOfHoldingsToDelete);

        ArrayList<HoldingsRecord> listOfEHoldingsToDelete = getListOfEHoldingsToDelete(bibRecord, requestJsonObject);
        holdingsListToDelete.addAll(listOfEHoldingsToDelete);

        if (CollectionUtils.isNotEmpty(holdingsListToDelete)) {
            List<HoldingsRecord> finalHoldingsRecordsToDelete = new ArrayList<HoldingsRecord>();
            for (Iterator<HoldingsRecord> iterator = holdingsListToDelete.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                String holdingsId = holdingsRecord.getHoldingsId();
                boolean holdingAttachedToPo = getBibValidationDao().isHoldingAttachedToPo(holdingsId);
                if(holdingAttachedToPo) {
                    Exception e = new Exception(OleNGConstants.ERR_HOLDINGS_HAS_REQ_OR_PO + DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + holdingsId);
                    addFailureReportToExchange(requestJsonObject, exchange,OleNGConstants.HOLDINGS,e,null);
                } else {
                    finalHoldingsRecordsToDelete.add(holdingsRecord);
                }
            }

            /*Delete from db*/
            getBusinessObjectService().delete(finalHoldingsRecordsToDelete);
            saveDeletedHolding(finalHoldingsRecordsToDelete);

            /*Delete from Solr*/
            List<String> idsToDeleteFromSolr = new ArrayList<String>();
            for (Iterator<HoldingsRecord> iterator = finalHoldingsRecordsToDelete.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                String holdingsId = holdingsRecord.getHoldingsId();
                idsToDeleteFromSolr.add(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML + "-" + holdingsId);
                if(PHoldings.PRINT.equalsIgnoreCase(holdingsRecord.getHoldingsType())) {
                    List<ItemRecord> itemRecords = holdingsRecord.getItemRecords();
                    if(CollectionUtils.isNotEmpty(itemRecords)) {
                        for (Iterator<ItemRecord> itemRecordIterator = itemRecords.iterator(); itemRecordIterator.hasNext(); ) {
                            ItemRecord itemRecord = itemRecordIterator.next();
                            idsToDeleteFromSolr.add(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + itemRecord.getItemId());
                        }
                    }
                }
            }

            String idsToDeleteFromSolrString = StringUtils.join(idsToDeleteFromSolr, " OR ");

            if(StringUtils.isNotBlank(idsToDeleteFromSolrString)) {
                String deleteQuery = "id:(" + idsToDeleteFromSolrString + ")";
                getSolrRequestReponseHandler().deleteFromSolr(deleteQuery);
            }
        }
    }

    private ArrayList<HoldingsRecord> getListOfHoldingsToDelete(BibRecord bibRecord, JSONObject requestJsonObject) {
        String addedOpsValue = getAddedOpsValue(requestJsonObject, OleNGConstants.HOLDINGS);
        return filterHoldingsOrEholdingsRecordsToDelete(bibRecord,addedOpsValue, PHoldings.PRINT);
    }


    private ArrayList<HoldingsRecord> getListOfEHoldingsToDelete(BibRecord bibRecord, JSONObject requestJsonObject) {
        String addedOpsValue = getAddedOpsValue(requestJsonObject, OleNGConstants.EHOLDINGS);
        return filterHoldingsOrEholdingsRecordsToDelete(bibRecord,addedOpsValue, EHoldings.ELECTRONIC);
    }

    private ArrayList<HoldingsRecord> filterHoldingsOrEholdingsRecordsToDelete(BibRecord bibRecord, String addedOpsValue, String type) {
        ArrayList<HoldingsRecord> holdingsListToDelete = new ArrayList<HoldingsRecord>();
        if(StringUtils.isNotBlank(addedOpsValue) && (addedOpsValue.equalsIgnoreCase(OleNGConstants.DELETE_ALL_EXISTING_AND_ADD) ||
                addedOpsValue.equalsIgnoreCase(OleNGConstants.DELETE_ALL))) {
            ArrayList<HoldingsRecord> finalHoldingsListForRetain = new ArrayList<HoldingsRecord>();
            List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();
            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                if(holdingsRecord.getHoldingsType().equalsIgnoreCase(type)) {
                    holdingsListToDelete.add(holdingsRecord);
                } else {
                    finalHoldingsListForRetain.add(holdingsRecord);
                }
            }
            bibRecord.setHoldingsRecords(finalHoldingsListForRetain);
        }
        return holdingsListToDelete;
    }


    private String getAddedOpsValue(JSONObject jsonObject, String docType) {
        JSONObject addedOps = getJSONObjectFromJSONObject(jsonObject, OleNGConstants.ADDED_OPS);
        return getStringValueFromJsonObject(addedOps,docType);
    }

    private void saveDeletedHolding(List<HoldingsRecord> holdingsRecords) {
        List<BibDeletionRecord> bibDeletionRecords= new ArrayList<BibDeletionRecord>();
        for(HoldingsRecord holdingsRecord : holdingsRecords){
            BibDeletionRecord bibDeletionRecord= new BibDeletionRecord();
            if(holdingsRecord.getBibId() != null){
                bibDeletionRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(holdingsRecord.getBibId()));
            }
            if(org.apache.commons.lang.StringUtils.isNotBlank(holdingsRecord.getHoldingsId())){
                bibDeletionRecord.setHoldingId(DocumentUniqueIDPrefix.getDocumentId(holdingsRecord.getHoldingsId()));
            }
            bibDeletionRecord.setBibIdIndicator("N");
            bibDeletionRecord.setHoldingIdIndicator("Y");
            bibDeletionRecord.setDateUpdated(new Timestamp(new Date().getTime()));
            bibDeletionRecords.add(bibDeletionRecord);
        }
        getBusinessObjectService().save(bibDeletionRecords);
    }

}
