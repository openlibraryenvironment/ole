package org.kuali.ole.dsng.indexer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.engine.service.index.solr.Languages;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.utility.ISBNUtil;
import org.kuali.ole.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SheikS on 11/26/2015.
 */
public class BibIndexer extends OleDsNgIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(BibIndexer.class);

    private static final String PATTERN_CHAR = "*";
    private static final String SEPERATOR_SUB_FIELD = " ";
    private static final String SEPERATOR_HYPHEN = " - ";
    private static final String SEPERATOR_DOUBLE_HYPHEN = " -- ";
    private String publicationDateRegex = "[0-9]{4}";
    private static final String SEPERATOR_DATA_FIELD = ", ";
    private static final String DYNAMIC_FIELD_PREFIX = "mdf_";

    private BibMarcRecordProcessor bibMarcRecordProcessor;
    private DocumentSearchConfig documentSearchConfig;

    @Override
    public void indexDocument(Object object) {
        BibRecord bibRecord = (BibRecord) object;
        Map<String, SolrInputDocument> inputDocumentForBib = getInputDocumentForBib(bibRecord, null);
        List<SolrInputDocument> solrInputDocuments = getSolrInputDocumentListFromMap(inputDocumentForBib);
        commitDocumentToSolr(solrInputDocuments);
    }

    @Override
    public void updateDocument(Object object) {
        BibRecord bibRecord = (BibRecord) object;
        Map<String, SolrInputDocument> inputDocumentForBib = getInputDocumentForBib(bibRecord, null);
        List<SolrInputDocument> solrInputDocuments = getSolrInputDocumentListFromMap(inputDocumentForBib);
        commitDocumentToSolr(solrInputDocuments);
    }
    
    @Override
    public void deleteDocument(String bibId) {
        deleteBibDocumentFromSolr(bibId);
        indexDeletedBibInfoToSolr(bibId);
    }

    public Map<String,SolrInputDocument> getInputDocumentForBib(BibRecord bibRecord, Map parameterMap) {
        SolrInputDocument bibSolrInputDocument = buildSolrInputDocument(bibRecord, parameterMap);
        setCommonFields(bibRecord,bibSolrInputDocument);
        if (null != bibRecord.getStatusUpdatedDate()) {
            bibSolrInputDocument.setField(STATUS_UPDATED_ON, getDate(bibRecord.getStatusUpdatedDate().toString()));
        }

        List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();
        if(CollectionUtils.isNotEmpty(holdingsRecords)) {
            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                HoldingsRecord holdingsRecord = iterator.next();
                String holdingsIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, String.valueOf(holdingsRecord.getHoldingsId()));
                bibSolrInputDocument.addField(HOLDINGS_IDENTIFIER,holdingsIdentifierWithPrefix);
                // Todo : Need to do for Holdings
                parameterMap = new HoldingIndexer().getInputDocumentForHoldings(holdingsRecord,parameterMap);
            }
        }

        return parameterMap;
    }

    @Override
    public SolrInputDocument buildSolrInputDocument(Object object,Map<String,SolrInputDocument> parameterMap) {
        SolrInputDocument solrInputDocument = null;
        try {
            BibRecord bibRecord = (BibRecord) object;
            BibMarcRecords bibMarcRecords = getBibMarcRecordProcessor().fromXML(bibRecord.getContent());
            solrInputDocument = buildSolrInputDocumentWithBibMarcRecord(bibMarcRecords.getRecords().get(0));

            setCommonFields(bibRecord, solrInputDocument);

            assignUUIDs(solrInputDocument);

            addSolrInputDocumentToMap(parameterMap,solrInputDocument);
        } catch (Exception e) {
            LOG.info("Exception :", e);
            e.printStackTrace();
            throw new DocstoreIndexException(e.getMessage());
        }
        return solrInputDocument;
    }

    public SolrInputDocument buildSolrInputDocumentWithBibMarcRecord(BibMarcRecord record) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();

        solrInputDocument.addField(LEADER, record.getLeader());

        // Title Field Calculations.
        List<ControlField> controlFieldList = record.getControlFields();

        for (ControlField cf : controlFieldList) {
            solrInputDocument.addField("controlfield_" + cf.getTag(), cf.getValue());
        }

        solrInputDocument.addField(DOC_TYPE, DocType.BIB.getDescription());
        solrInputDocument.addField(DOC_FORMAT, DocFormat.MARC.getDescription());

        for (String field : documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP.keySet()) {
            Object object = buildFieldValue(field, record);
            if(object != null){
                addFieldToSolrDoc(record, field,object, solrInputDocument);
            }
        }


        addFieldToSolrDoc(record, ALL_TEXT, getAllText(record), solrInputDocument);

        addGeneralFieldsToSolrDoc(record, solrInputDocument);
        if (record.getLeader() == null || ((record.getLeader().length() >= 8) && (record.getLeader().charAt(7) != 's'))) {
            solrInputDocument.removeField(JOURNAL_TITLE_SEARCH);
            solrInputDocument.removeField(JOURNAL_TITLE_DISPLAY);
            solrInputDocument.removeField(JOURNAL_TITLE_SORT);
        }
        addFieldToSolrDoc(record, PUBLISHER_SORT, solrInputDocument.getFieldValue(PUBLISHER_SEARCH), solrInputDocument);

        return solrInputDocument;
    }

    protected void setCommonFields(BibRecord bibRecord, SolrInputDocument solrInputDocument) {
        String bibIdWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC, String.valueOf(bibRecord.getBibId()));
        solrInputDocument.setField(ID, bibIdWithPrefix);
        solrInputDocument.setField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(bibRecord.getBibId()));
        solrInputDocument.setField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(bibRecord.getBibId()));
        solrInputDocument.setField(UNIQUE_ID, bibIdWithPrefix);
        solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrInputDocument.setField(BIB_ID, bibIdWithPrefix);

        solrInputDocument.setField(STATUS_SEARCH, bibRecord.getStatus());
        solrInputDocument.setField(STATUS_DISPLAY, bibRecord.getStatus());

        if (null != bibRecord.getStatusUpdatedDate()) {
            solrInputDocument.setField(STATUS_UPDATED_ON, getDate(bibRecord.getStatusUpdatedDate().toString()));
        }

        solrInputDocument.setField(STAFF_ONLY_FLAG, bibRecord.getStaffOnlyFlag());

        String createdBy = bibRecord.getCreatedBy();
        solrInputDocument.setField(CREATED_BY, createdBy);
        solrInputDocument.setField(UPDATED_BY, createdBy);

        Date date = new Date();
        Date createdDate = null;

        if (null != bibRecord.getDateCreated()) {
            createdDate = getDate(bibRecord.getDateCreated().toString());
            solrInputDocument.setField(DATE_ENTERED, createdDate);
        } else {
            solrInputDocument.setField(DATE_ENTERED, date);
        }

        if (null != bibRecord.getDateEntered()) {
            solrInputDocument.setField(DATE_UPDATED, getDate(bibRecord.getDateEntered().toString()));
        } else {
            if (null != bibRecord.getDateCreated()) {
                // Updated date will have created date value when bib is not updated after it is created.
                solrInputDocument.setField(DATE_UPDATED, createdDate);
            } else {
                solrInputDocument.setField(DATE_UPDATED, date);
            }
        }
    }


    private Date getDate(String dateStr) {
        DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            if (StringUtils.isNotEmpty(dateStr)) {
                return format.parse(dateStr);
            } else {
                return new Date();
            }

        } catch (ParseException e) {
            LOG.info("Exception : " + dateStr + " for format:: " + Constants.DATE_FORMAT, e);
            return new Date();
        }
    }

    public Object buildFieldValue(String fieldName, BibMarcRecord record) {
        List<ControlField> controlFieldList = record.getControlFields();
        List<DataField> dataFields = record.getDataFields();
        String includeTags = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP.get(fieldName);
        if ((includeTags != null) && (includeTags.length() > 0)) {
            String excludeTags = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP.get(fieldName);
            if (excludeTags == null) {
                excludeTags = "";
            }
            if (fieldName.startsWith("Subject_")) {
                return getDataFieldValue(includeTags, excludeTags, record, true, fieldName);
            } else {
                if (fieldName.equals(ISBN_SEARCH))
                    return normalizeIsbn(getDataFieldValue(includeTags, excludeTags, record, false, fieldName));
                else
                    return getDataFieldValue(includeTags, excludeTags, record, false, fieldName);
            }
        } else if (fieldName.equals(PUBLICATIONDATE_DISPLAY) || fieldName.equals(PUBLICATIONDATE_SEARCH) || fieldName.equals(PUBLICATIONDATE_FACET)
                || fieldName.equals(PUBLICATIONDATE_SORT)) {
            String publicationDate = "";
            String publicationEndDate = "";
            Object publicationDateValue = null;
            for (ControlField controlField : controlFieldList) {
                if (controlField.getTag().equalsIgnoreCase("008")) {
                    String controlField008 = controlField.getValue();
                    if (controlField008 != null && controlField008.length() > 10) {
                        publicationDate = controlField008.substring(7, 11);
                        publicationDate = extractPublicationDateWithRegex(publicationDate);
                        if (controlField008.length() > 14) {
                            publicationEndDate = controlField008.substring(11, 15);
                            publicationEndDate = extractPublicationDateWithRegex(publicationEndDate);
                        }
                    }
                }
            }
            if (publicationDate == null || publicationDate.trim().length() == 0) {
                if (getDataFieldValue("260-c", "", record, true, fieldName) instanceof String) {
                    publicationDate = (String) getDataFieldValue("260-c", "", record, true, fieldName);
                } else if (getDataFieldValue("260-c", "", record, true, fieldName) instanceof List) {
                    publicationDate = ((List<String>) getDataFieldValue("260-c", "", record, true, fieldName)).get(0);
                }
                publicationDate = extractPublicationDateWithRegex(publicationDate);
            }
            if (fieldName.equals(PUBLICATIONDATE_FACET)) {
                if (publicationDate.equalsIgnoreCase("")) {
                    publicationDateValue = "Date could not be determined";
                } else {
                    publicationDateValue = buildPublicationDateFacetValue(publicationDate, publicationEndDate);
                }
                return publicationDateValue;
            }
            return publicationDate;
        } else if (fieldName.equals(LANGUAGE_DISPLAY) || fieldName.equals(LANGUAGE_SEARCH) || fieldName.equals(LANGUAGE_FACET)) {
            List<Object> langs = new ArrayList<Object>();
            for (ControlField controlField : controlFieldList) {
                if (controlField.getTag().equalsIgnoreCase("008")) {
                    String cf8 = controlField.getValue();
                    if (cf8 != null && cf8.length() > 37) {
                        String lang = Languages.getInstance(Languages.ISO_639_3).getLanguageDescription(
                                cf8.substring(35, 38));
                        langs.add(lang == null ? "Undefined" : lang);
                    }
                }
            }
            if (fieldName.equals(LANGUAGE_SEARCH) || fieldName.equals(LANGUAGE_FACET)) {
                for (DataField df : dataFields) {
                    if (df.getTag().equals("546")) {
                        try {
                            for (SubField subfield : df.getSubFields()) {
                                if (subfield.getCode().equalsIgnoreCase("a")) {
                                    langs.add(subfield.getValue());
                                }
                            }
                        } catch (RuntimeException re) {
                            LOG.info("Exception :", re);
                        }
                    }
                }
            }
            return langs;
        } else if (fieldName.equals(FORMAT_DISPLAY) || fieldName.equals(FORMAT_SEARCH) || fieldName.equals(FORMAT_FACET)) {
            return getRecordFormat(record);
        }  else if (fieldName.equals(RESOURCETYPE_DISPLAY) || fieldName.equals(RESOURCETYPE_SEARCH)) {
            return getRecordFormat_ResourceType(record);
        }  else if (fieldName.equals(CARRIER_DISPLAY) || fieldName.equals(CARRIER_SEARCH)) {
            return getRecordFormat_Carrier(record);
        } else if(fieldName.equals(DESCRIPTION_SEARCH)) {
            String excludeTags = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP.get(fieldName);
            if (excludeTags == null) {
                excludeTags = "";
            }
            if (includeTags == null) {
                includeTags = "";
            }
            return getDataFieldValue(includeTags, excludeTags, record, false, fieldName);
        } else {
            return null;
        }
    }

    private Object getDataFieldValue(String includeTags, String excludeTags, BibMarcRecord record,
                                     boolean isHyphenSeperatorFirst, String fieldName) {
        List<Object> fieldValues = new ArrayList<Object>();
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
                    List<SubField> subFields = dataField.getSubFields();
                    if (subFieldIdx != -1) { // Includes only one Sub Field of a main Data Field.
                        if (!excludeTags.contains(tag)) {
                            String subFieldCodes = tag.substring(subFieldIdx + 1, tag.length());
                            boolean isHyphenCodedOnce = false;
                            for (SubField subField : subFields) {
                                if (subFieldCodes.contains(subField.getCode())) {
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
                                    fieldValue.append(subField.getValue());
                                }
                            }
                        }
                    } else { // Includes whole Data Field i.e includes All Sub Fields in a datafield
                        boolean isHyphenCodedOnce = false;
                        boolean isFirstSubField = false;
                        for (SubField subField : subFields) {
                            if (!excludeTags.contains(dataField.getTag() + "-" + subField.getCode()) && !excludeTags
                                    .contains(tagNum + "-" + subField.getCode())) {
                                if (fieldValue.length() != 0) {
                                    if (!isHyphenSeperatorFirst || isHyphenCodedOnce || (
                                            dataField.getTag().endsWith("00") || dataField.getTag().endsWith("10")
                                                    || dataField.getTag().endsWith("11"))) {
                                        fieldValue.append(SEPERATOR_SUB_FIELD);
                                    } else if (fieldName != null && (fieldName.equalsIgnoreCase(SUBJECT_FACET)
                                            || fieldName.equalsIgnoreCase(SUBJECT_DISPLAY))) {
                                        if (dataField.getTag().equalsIgnoreCase("630")) {
                                            if (subField.getCode().equals("v") || subField.getCode().equals("x")
                                                    || subField.getCode().equals("y") || subField.getCode().equals("z")) {
                                                fieldValue.append(SEPERATOR_DOUBLE_HYPHEN);
                                            }
                                        } else if (dataField.getTag().equalsIgnoreCase("650") || dataField.getTag()
                                                .equalsIgnoreCase(
                                                        "651")) {
                                            if (isFirstSubField && fieldName.equalsIgnoreCase(SUBJECT_FACET)) {
                                                fieldValues.add(fieldValue.toString().trim());
                                            }
                                            fieldValue.append(SEPERATOR_DOUBLE_HYPHEN);
                                            isFirstSubField = true;
                                        } else {
                                            fieldValue.append(SEPERATOR_SUB_FIELD);
                                        }
                                    } else {
                                        if (fieldName.startsWith("Subject_")) {
                                            fieldValue.append(SEPERATOR_SUB_FIELD);
                                        } else {
                                            fieldValue.append(SEPERATOR_HYPHEN);
                                            isHyphenCodedOnce = true;
                                        }
                                    }
                                }
                                fieldValue.append(subField.getValue());
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
            return fieldValues;
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
            LOG.info("Exception :", e);
            return false;
        }
    }

    private Object normalizeIsbn(Object isbnValue) {
        Object result = null;
        ISBNUtil isbnUtil = new ISBNUtil();
        if (isbnValue != null) {
            if (isbnValue instanceof List) {
                result = new ArrayList<String>();
                for (Object obj : (List<Object>) isbnValue) {
                    if (((String) obj).length() > 0) {
                        try {
                            ((List<String>) result).add(isbnUtil.normalizeISBN(obj));
                        } catch (OleException e) {
                            // LOG.error("Exception :", e);
                            ((List<String>) result).add((String) obj + " " + ISBN_NOT_NORMALIZED);
                        }
                    } else {
                        ((List<String>) result).add((String) obj);
                    }
                }
            } else {
                if (((String) isbnValue).length() > 0) {
                    try {
                        result = isbnUtil.normalizeISBN(isbnValue);
                    } catch (OleException e) {
                        //  LOG.error("Exception :", e);
                        result = isbnValue + " " + ISBN_NOT_NORMALIZED;
                    }
                } else {
                    result = isbnValue;
                }
            }
        }
        return result;
    }

    public String extractPublicationDateWithRegex(String publicationDate) {
        Pattern pattern = Pattern.compile(publicationDateRegex);
        Matcher matcher = pattern.matcher(publicationDate);
        if (matcher.find()) {
            if (matcher.group(0).equalsIgnoreCase("0000")) {
                return "";
            }
            return matcher.group(0);
        } else {
            return "";
        }


    }

    public Object buildPublicationDateFacetValue(String publicationDate, String publicationEndDate) {
        int pubDat = 0;
        List<String> pubList = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        if (publicationDate != null && publicationDate.length() == 4 && Integer.parseInt(publicationDate) <= year) {
            int pubStartDate = Integer.parseInt(publicationDate);
            if (publicationEndDate != null && publicationEndDate.length() == 4 && pubStartDate < Integer
                    .parseInt(publicationEndDate)) {
                if (Integer.parseInt(publicationEndDate) > year) {
                    publicationEndDate = String.valueOf(year);
                }
                int pubEndDate = Integer.parseInt(publicationEndDate);
                while (pubStartDate < pubEndDate) {
                    pubStartDate = (pubStartDate / 10) * 10;
                    if (pubStartDate == 0) {
                        pubList.add("Date could not be determined");
                    } else {
                        pubList.add(String.valueOf(pubStartDate) + "s");
                    }
                    pubStartDate = pubStartDate + 10;
                }
                pubStartDate = Integer.parseInt(publicationDate);
                pubEndDate = Integer.parseInt(publicationEndDate);
                while (pubStartDate < pubEndDate) {
                    pubStartDate = (pubStartDate) / 100;
                    pubDat = (pubStartDate) + 1;
                    pubCentury(pubDat, pubList);
                    pubStartDate = pubStartDate * 100 + 100;
                }
            } else {
                pubDat = (pubStartDate / 10) * 10;
                int pubCen = ((pubStartDate) / 100) + 1;
                if (pubDat == 0) {
                    pubList.add("Date could not be determined");
                } else {
                    pubList.add(String.valueOf(pubDat) + "s");
                    pubCentury(pubCen, pubList);
                }
            }
        } else {
            pubList.add("Date could not be determined");
        }
        return pubList;
    }

    private void pubCentury(int pubCen, List<String> pubList) {
        String pubCentury = String.valueOf(pubCen);
        if (pubCentury.endsWith("1")) {
            if (pubCentury.equalsIgnoreCase("11")) {
                pubList.add(pubCentury + "th Century");
            } else {
                pubList.add(pubCentury + "st Century");
            }
        } else if (pubCentury.endsWith("2")) {
            if (pubCentury.equalsIgnoreCase("12")) {
                pubList.add(pubCentury + "th Century");
            } else {
                pubList.add(pubCentury + "nd Century");
            }
        } else if (pubCentury.endsWith("3")) {
            if (pubCentury.equalsIgnoreCase("13")) {
                pubList.add(pubCentury + "th Century");
            } else {
                pubList.add(pubCentury + "rd Century");
            }
        } else {
            pubList.add(pubCentury + "th Century");
        }

    }

    public String getRecordFormat_ResourceType(BibMarcRecord record) {
        String format = null;
        char leader6 = ' ';
        char leader7 = ' ';
        if (record.getLeader() != null) {
            String leader = record.getLeader().trim();
            if (StringUtils.isNotBlank(leader)) {
                if (leader.length() >= 7) {
                    leader6 = leader.charAt(6);
                }
                if (leader.length() >= 8) {
                    leader7 = leader.charAt(7);
                }

                if ((leader6 == 'a' || leader6 == 't') && leader7 == 'm') {
                    format = "Book";
                }
                if (leader6 == 'a' && leader7 == 's') {
                    format = "Serial";
                }
                if (leader6 == 'c' || leader6 == 'd') {
                    format = "Score";
                }
                if (leader6 == 'j' || leader6 == 'i') {
                    format = "Sound recording";
                }
                if (leader6 == 'e' || leader6 == 'f') {
                    format = "Map";
                }
                if (leader6 == 'g') {
                    format = "Motion picture";
                }
                if (leader6 == 'k') {
                    format = "Photo/Print";
                }
                if (leader6 == 'm') {
                    format = "Computer file";
                }
                if (leader6 == 'p') {
                    format = "Archival materials";
                }
                if (leader6 == 'r') {
                    format = "Artifacts";
                }
            }
        }
        return format;
    }

    public String getRecordFormat_Carrier(BibMarcRecord record) {
        String format = null;
        String cF7 = null;
        String cF8 = null;
        char cF70 = ' ';
        char cF71 = ' ';
        char cF823 = ' ';
        char cF829 = ' ';
        char leader06 = ' ';
        String leader ="";
        if (record.getLeader() != null) {
            leader = record.getLeader();
        }
        for (ControlField controlField : record.getControlFields()) {
            if (controlField.getTag().equals("007")) {
                cF7 = controlField.getValue();
            }else if(controlField.getTag().equals("008")){
                cF8 = controlField.getValue();
            }
        }

        if(StringUtils.isNotBlank(cF7) && cF7.length() >= 1){
            cF70 = cF7.charAt(0);
        }
        if(StringUtils.isNotBlank(cF7) && cF7.length() >= 2){
            cF71 = cF7.charAt(1);
        }
        if(StringUtils.isNotBlank(cF8) && cF8.length() >= 24){
            cF823 = cF8.charAt(23);
        }
        if(StringUtils.isNotBlank(cF8) && cF8.length() >= 30){
            cF829 = cF8.charAt(29);
        }
        if(StringUtils.isNotBlank(leader) && leader.length() >= 7){
            leader06 = leader.charAt(6);
        }

        if(cF70 == 'h'){
            format = "Microform";
            return format;
        }
        if(cF70 == 'c' && cF71 == 'r'){
            format = "Remote e-resource";
            return format;
        }
        if(cF70 == 'c' && cF71 != 'r'){
            format = "Direct access 3-resource";
            return format;
        }
        if((leader06 == 'a' || leader06 == 'c' || leader06 == 'd' || leader06 == 'p' || leader06 == 't') && (cF823 == 'd' || cF823 == 'f' || cF823 == 'r' || cF823 == ' ')){
            format = "Print";
        }
        if((leader06 == 'e' || leader06 == 'f' || leader06 == 'k') && (cF829 == 'd' || cF829 == 'r' || cF829 == ' ')){
            format = "Print";
        }
        return format;
    }

    public String getRecordFormat(BibMarcRecord record) {
        String format = null;
        String cF7 = null;
        String cF8 = null;
        String formatData = "";
        char cF8Ch21 = ' ';
        char cF8Ch22 = ' ';
        char cF8Ch28 = ' ';
        char cF7Ch0 = ' ';
        int cFIndex = record.getControlFields().indexOf(new ControlField("007"));
        if (cFIndex != -1) {
            cF7 = record.getControlFields().get(cFIndex).getValue();
        }
        cFIndex = record.getControlFields().indexOf(new ControlField("008"));
        if (cFIndex != -1) {
            cF8 = record.getControlFields().get(cFIndex).getValue();
        }
        Object tmp = null;
        tmp = getDataFieldValue("111-a", "", record, false, "");
        String dF111a = tmp != null ? tmp.toString() : null;
        tmp = getDataFieldValue("254-h", "", record, false, "");
        String dF254h = tmp != null ? tmp.toString() : null;
        tmp = getDataFieldValue("254-k", "", record, false, "");
        String dF254k = tmp != null ? tmp.toString() : null;
        tmp = getDataFieldValue("260-b", "", record, false, "");
        String dF260b = tmp != null ? tmp.toString() : null;
        tmp = getDataFieldValue("502-a", "", record, false, "");
        String dF502a = tmp != null ? tmp.toString() : null;
        tmp = getDataFieldValue("711-a", "", record, false, "");
        String dF711a = tmp != null ? tmp.toString() : null;

        if (cF8 != null && cF8.length() > 22) {
            cF8Ch21 = cF8.charAt(21);
            cF8Ch22 = cF8.charAt(22);
        }
        if (cF8 != null && cF8.length() > 28) {
            cF8Ch28 = cF8.charAt(28);
        }
        if (cF7 != null) {
            cF7Ch0 = cF7.charAt(0);
        }
        if (record.getLeader() != null && record.getLeader().length() > 8) {
            formatData = record.getLeader().substring(6, 8);
        }

        if (dF254h != null && dF254h.contains("micro")) {
            format = "Microformat";
        } else if (formatData.equals("tm") && dF502a != null) {
            format = "Thesis/Dissertation";
        } else if (dF111a != null || dF711a != null) {
            format = "Conference/Event";
        } else if (formatData.equals("aa") || formatData.equals("am") || formatData.equals("ac") || formatData
                .equals("tm")) {
            if (dF254k != null && dF254k.contains("kit")) {
                format = "Other";
            } else {
                format = "Book";
            }
        } else if (formatData.equals("im") || formatData.equals("jm") || formatData.equals("jc")
                || formatData.equals("jd") || formatData.equals("js")) {
            format = "Sound recording";
        } else if (formatData.equals("cm") || formatData.equals("dm") || formatData.equals("ca")
                || formatData.equals("cb") || formatData.equals("cd") || formatData.equals("cs")) {
            format = "Musical score";
        } else if (formatData.equals("fm") || ("".equals(formatData) && formatData.startsWith("e"))) {
            format = "Map/Atlas";
        } else if (formatData.equals("gm") || (cF7 != null && (cF7Ch0 == ('v')))) {
            format = "Video";
        } else if (formatData.equals("gm") || (cF7 != null && (cF7Ch0 == ('g')))) {
            format = "Projected graphic";
        } else if (formatData.equals("as") || formatData.equals("gs")) {
            format = "Journal/Periodical";
        } else if (formatData.equals("km")) {
            format = "Image";
        } else if (formatData.equals("mm")) {
            format = "Datafile";
        } else if (formatData.equals("as") && (cF8Ch21 == 'n' || cF8Ch22 == 'e')) {
            format = "Newspaper";
        } else if ("".equals(formatData) && formatData.startsWith("r")) {
            format = "3D object";
        } else if (formatData != "" && formatData.endsWith("i")) {
            format = "Database/Website";
        } else if (("".equals(formatData) && (!formatData.startsWith("c") || !formatData.startsWith("d")
                || !formatData.startsWith("i") || !formatData.startsWith("j"))) && (
                (cF8Ch28 == 'f' || cF8Ch28 == 'i' || cF8Ch28 == 'o') && (dF260b != null && !dF260b
                        .contains("press")))) {
            format = "Government document";
        } else {
            format = "Other";
        }
        return format;
    }

    private void addFieldToSolrDoc(BibMarcRecord record, String fieldName, Object value,
                                   SolrInputDocument solrDoc) {
        int ind2Value = 0;
        if (value instanceof List) {
            if (fieldName.toLowerCase().endsWith("_sort")) // Sort fields only the first value to be inserted.
            {
                ind2Value = getSecondIndicator(record, fieldName);
                LOG.debug("field name -->" + fieldName + "----->" + ind2Value);
                if (ind2Value > 0) {
                    solrDoc.addField(fieldName, ((List) value).get(0).toString().substring(ind2Value));
                } else {
                    solrDoc.addField(fieldName, ((List) value).get(0));
                }

            } else if (fieldName.endsWith("_facet")) {
                solrDoc.addField(fieldName, getSortString((List) value));
            } else {
                if (((List) value).size() > 0) {
                    for (Object obj : (List<Object>) value)
                    // All non Sort and Multi Valued Fields
                    {
                        solrDoc.addField(fieldName, obj);
                    }
                } else {
                    solrDoc.addField(fieldName, null);
                }
            }
        } else {
            if (fieldName.toLowerCase().endsWith("_sort")) // Sort fields only the first value to be inserted.
            {
                ind2Value = getSecondIndicator(record, fieldName);
                LOG.debug("field name -->" + fieldName + "----->" + ind2Value);
                if (value != null && ind2Value > 0) {
                    String fieldValue = value.toString();
                    try {
                        fieldValue = value.toString().substring(ind2Value);
                    } catch (Exception e) {
                        LOG.error("Exception while getting value:" + value.toString() + " for field:" + fieldName + ". Exception:" + e.toString());
                        // TODO: log the exception trace here.
                    }
                    solrDoc.addField(fieldName, fieldValue);
                } else {
                    solrDoc.addField(fieldName, value);
                }
            } else if (fieldName.endsWith("_facet")) {
                if (value != null) {
                    solrDoc.addField(fieldName, getNormalizedString(value.toString()));
                }
            } else {
                solrDoc.addField(fieldName, value);
            }
        }
    }

    private int getSecondIndicator(BibMarcRecord record, String fieldName) {
        int ind2Value = 0;
        String fieldTags = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP.get(fieldName);
        String[] tagValueList = null;
        if (fieldTags != null) {
            tagValueList = fieldTags.split(",");
            List<DataField> dataFieldList = record.getDataFields();
            String ind2 = null;
            boolean isVisit = true;
            for (DataField dataField : dataFieldList) {
                String tag = dataField.getTag();
                for (String tagValue : tagValueList) {
                    StringBuffer sb = null;
                    if (fieldName.equalsIgnoreCase(AUTHOR_SORT) || fieldName.equalsIgnoreCase(TITLE_SORT)) {
                        sb = getTagValues(dataField, tag, tagValue);
                        if (sb != null && sb.toString().length() > 0 && isVisit) {
                            ind2 = dataField.getInd2();
                            isVisit = false;
                        }

                    }
                }
            }
            try {
                if (ind2 != null)
                    ind2Value = Integer.parseInt(ind2);

            } catch (Exception e) {
                ind2Value = -1;
            }

        }
        return ind2Value;
    }

    private StringBuffer getTagValues(DataField dataField, String tag, String tagValue) {
        StringBuffer sb = new StringBuffer();
        String[] tags = tagValue.split("-");
        for (String tagName : tags) {
            if (tag.equalsIgnoreCase(tagName)) {
                List<SubField> subFieldList = dataField.getSubFields();
                for (SubField subField : subFieldList) {
                    sb.append(subField.getValue() + " ");
                }

            }
        }
        return sb;
    }

    public List<String> getSortString(List<String> list) {
        List<String> sortStringList = new ArrayList<String>();
        for (String str : list) {
            sortStringList.add(getNormalizedString(str));
        }
        return sortStringList;
    }

    public String getNormalizedString(String str) {
        String ret = "";
        StringBuffer sortString = new StringBuffer();
        ret = str.toLowerCase();
        ret = ret.replaceAll("[\\-\\/]", " ");
        ret = ret.replace("&lt;", "");
        ret = ret.replace("&gt;", "");
        ret = ret.replaceAll("[\\.\\,\\;\\:\\(\\)\\{\\}\\'\\!\\?\\\"\\<\\>\\[\\]]", "");
        ret = Normalizer.normalize(ret, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        ret = ret.replaceAll("\\s+", " ");
        sortString.append(ret);
        sortString.append(" /r/n!@#$");
        sortString.append(str);
        return sortString.toString();
    }

    public String getAllText(BibMarcRecord record) {
        StringBuilder allText = new StringBuilder();
        allText.append(record.getLeader());
        allText.append(SEPERATOR_DATA_FIELD);
        for (ControlField cf : record.getControlFields()) {
            allText.append(cf.getValue());
            allText.append(SEPERATOR_DATA_FIELD);
        }
        for (DataField df : record.getDataFields()) {
            for (SubField sf : df.getSubFields()) {
                allText.append(sf.getValue());
                allText.append(SEPERATOR_SUB_FIELD);
                if(sf.getValue().contains("-")){
                    allText.append(sf.getValue().replace("-",""));
                    allText.append(SEPERATOR_SUB_FIELD);
                }
            }
            allText.append(SEPERATOR_DATA_FIELD);
        }
        return allText.toString();
    }

    private void addGeneralFieldsToSolrDoc(BibMarcRecord record, SolrInputDocument solrDoc) {
        String isbnDataFields = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP.get(ISBN_SEARCH);
        for (DataField dataField : record.getDataFields()) {
            String tag = dataField.getTag();
            for (SubField subField : dataField.getSubFields()) {
                String subFieldKey = subField.getCode();
                String subFieldValue = subField.getValue();
                String key = tag + subFieldKey;
                subFieldValue = processGeneralFieldValue(tag, subFieldKey, subFieldValue, isbnDataFields);
                solrDoc.addField(DYNAMIC_FIELD_PREFIX + key, subFieldValue);
            }
        }
    }

    private String processGeneralFieldValue(String tag, String subFieldKey, String subFieldValue, String isbnKey) {
        String value = subFieldValue;
        if (isbnKey.contains(tag) && isbnKey.contains(subFieldKey)) {
            value = (String) normalizeIsbn(subFieldValue);
        }
        return value;
    }

    public BibMarcRecordProcessor getBibMarcRecordProcessor() {
        if(null == bibMarcRecordProcessor) {
            bibMarcRecordProcessor = new BibMarcRecordProcessor();
        }
        return bibMarcRecordProcessor;
    }

    public void setBibMarcRecordProcessor(BibMarcRecordProcessor bibMarcRecordProcessor) {
        this.bibMarcRecordProcessor = bibMarcRecordProcessor;
    }

    public DocumentSearchConfig getDocumentSearchConfig() {
        if(null == documentSearchConfig) {
            documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();
        }
        return documentSearchConfig;
    }

    public void setDocumentSearchConfig(DocumentSearchConfig documentSearchConfig) {
        this.documentSearchConfig = documentSearchConfig;
    }
}
