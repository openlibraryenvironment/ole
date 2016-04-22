package org.kuali.ole.docstore.discovery.solr.work.bib.marc;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.common.document.content.instance.Instance;
import org.kuali.ole.docstore.common.document.content.instance.InstanceCollection;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.einstance.WorkEInstanceOlemlDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.instance.oleml.WorkInstanceOlemlDocBuilder;
import org.kuali.ole.docstore.discovery.util.Languages;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.WorkBibDocumentIndexer;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.WorkBibMarcRecordProcessor;
import org.kuali.ole.docstore.utility.ISBNUtil;
import org.kuali.ole.utility.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class to Map, Convert and Build utils for Marc Fields to Discovery Fields and values conversion.
 *
 * @author Rajesh Chowdary K
 */
public class WorkBibMarcDocBuilder
        extends DocBuilder
        implements WorkBibMarcFields {

    private static final String PATTERN_CHAR = "*";
    private static final String SEPERATOR_DATA_FIELD = ", ";
    private static final String SEPERATOR_SUB_FIELD = " ";
    private static final String SEPERATOR_HYPHEN = " - ";
    private static final String SEPERATOR_DOUBLE_HYPHEN = " -- ";
    private static final String INSTANCE_IDENTIFIER = "instanceIdentifier";
    //    private static DocumentMetaData   marcDocumentMetaData         = null;

    public static Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();
    public static Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();
    private static final Logger LOG = LoggerFactory
            .getLogger(WorkBibMarcDocBuilder.class);

    static {
        //        marcDocumentMetaData = DocumentsMetaData.getInstance().getDocumentMetaData(DocCategory.WORK.getCode(), DocType.BIB.getCode(),
        //                DocFormat.MARC.getCode());

        List<DocumentCategory> docCategories = DocumentConfig.getInstance().getDocumentCategories();

        for (DocumentCategory cat : docCategories) {
            for (DocumentType type : cat.getDocumentTypes()) {
                for (DocumentFormat format : type.getDocumentFormats()) {
                    if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.BIB.isEqualTo(type.getId()) && DocFormat.MARC.isEqualTo(format.getId())) {
                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {
                            FIELDS_TO_TAGS_2_INCLUDE_MAP.put(field.getId(), field.getMapping().getInclude());
                            FIELDS_TO_TAGS_2_EXCLUDE_MAP.put(field.getId(), field.getMapping().getExclude());
                        }
                    }
                }
            }
        }

        // for (Field field : marcDocumentMetaData.getFields()) {
        // FIELDS_TO_TAGS_2_INCLUDE_MAP.put(field.getName(), field.get("include"));
        // FIELDS_TO_TAGS_2_EXCLUDE_MAP.put(field.getName(), field.get("exclude"));
        // }
        FIELDS_TO_TAGS_2_INCLUDE_MAP = Collections.unmodifiableMap(FIELDS_TO_TAGS_2_INCLUDE_MAP);
        FIELDS_TO_TAGS_2_EXCLUDE_MAP = Collections.unmodifiableMap(FIELDS_TO_TAGS_2_EXCLUDE_MAP);
    }

    /**
     * Method to build Field Value for a given field Name and given record.
     *
     * @param fieldName - field name should be one of the defined names in WorkBibMarcDocBuilder
     * @param record    - WorkBibMarcRecord
     * @return - returns the field value build over from the given record.
     */
    public Object buildFieldValue(String fieldName, WorkBibMarcRecord record) {
        List<ControlField> controlFieldList = record.getControlFields();
        List<DataField> dataFields = record.getDataFields();
        String includeTags = FIELDS_TO_TAGS_2_INCLUDE_MAP.get(fieldName);
        if ((includeTags != null) && (includeTags.length() > 0)) {
            String excludeTags = FIELDS_TO_TAGS_2_EXCLUDE_MAP.get(fieldName);
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
                        }
                    }
                }
            }
            return langs;
        } else if (fieldName.equals(FORMAT_DISPLAY) || fieldName.equals(FORMAT_SEARCH) || fieldName.equals(FORMAT_FACET)) {
            return getRecordFormat(record);
        } else {
            throw new RuntimeException("Unknown field named:" + fieldName);
        }
    }


    /**
     * Method to get Record Format.
     *
     * @param record
     * @return
     */
    public String getRecordFormat(WorkBibMarcRecord record) {
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

    /**
     * Method to give data field value of a given include tags and exclude tags
     *
     * @param includeTags
     * @param excludeTags
     * @param record
     * @param isHyphenSeperatorFirst - Pass 'false' by default (if it is not a subject field (Currently)).
     *                               - Pass 'true' if it has to get encoded first subfield values with " - ".
     * @param fieldName
     * @return
     */
    private Object getDataFieldValue(String includeTags, String excludeTags, WorkBibMarcRecord record,
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

    /**
     * Method to validate tag with given allowed tag format supplied.
     *
     * @param tag
     * @param tagFormat
     * @return
     */
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
            return false;
        }
    }

    /**
     * Method to build Solr Input Document from a given Work Bib Marc Record
     *
     * @param record
     * @return
     */
    public SolrInputDocument buildSolrInputDocument(WorkBibMarcRecord record) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(WorkBibMarcDocBuilder.LEADER, record.getLeader());

        // Title Field Calculations.
        List<ControlField> controlFieldList = record.getControlFields();

        for (ControlField cf : controlFieldList) {
            solrDoc.addField("controlfield_" + cf.getTag(), cf.getValue());
        }

        solrDoc.addField(DOC_TYPE, DocType.BIB.getDescription());
        solrDoc.addField(DOC_FORMAT, DocFormat.MARC.getDescription());

        for (String field : FIELDS_TO_TAGS_2_INCLUDE_MAP.keySet())
            addFieldToSolrDoc(record, field, buildFieldValue(field, record), solrDoc);
        addFieldToSolrDoc(record, ALL_TEXT, getAllText(record), solrDoc);
        addGeneralFieldsToSolrDoc(record, solrDoc);
        return solrDoc;
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
                        result = isbnValue + " " + ISBN_NOT_NORMALIZED;
                    }
                } else {
                    result = isbnValue;
                }
            }
        }
        return result;
    }

    private void addGeneralFieldsToSolrDoc(WorkBibMarcRecord record, SolrInputDocument solrDoc) {
        String isbnDataFields = FIELDS_TO_TAGS_2_INCLUDE_MAP.get(ISBN_SEARCH);
        for (DataField dataField : record.getDataFields()) {
            String tag = dataField.getTag();
            for (SubField subField : dataField.getSubFields()) {
                String subFieldKey = subField.getCode();
                String subFieldValue = subField.getValue();
                String key = tag + subFieldKey;
                subFieldValue = processGeneralFieldValue(tag, subFieldKey, subFieldValue, isbnDataFields);
                solrDoc.addField(key, subFieldValue);
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

    private void addFieldToSolrDoc(WorkBibMarcRecord record, String fieldName, Object value,
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
                    solrDoc.addField(fieldName, value.toString().substring(ind2Value));
                } else {
                    solrDoc.addField(fieldName, value);
                }
            } else if (fieldName.endsWith("_facet")) {
                if (value != null) {
                    solrDoc.addField(fieldName, getSortString(value.toString()));
                }
            } else {
                solrDoc.addField(fieldName, value);
            }
        }
    }

    private int getSecondIndicator(WorkBibMarcRecord record, String fieldName) {
        int ind2Value = 0;
        String fieldTags = FIELDS_TO_TAGS_2_INCLUDE_MAP.get(fieldName);
        String[] tagValueList = fieldTags.split(",");
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
            try {

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

        /*       if (tag.equals(tags[0])) {
            LOG.info("tags-->"+tags[0]);
            LOG.info("length-->"+tags[0].length());
            List<SubField> subFieldList = dataField.getSubFields();
            for (SubField subField : subFieldList) {
                sb.append(subField.getValue() + " ");
            }
        }*/
        return sb;
    }

    /**
     * Method to give all_text field to a given record.
     *
     * @param record
     * @return
     */
    public String getAllText(WorkBibMarcRecord record) {
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
            }
            allText.append(SEPERATOR_DATA_FIELD);
        }
        return allText.toString();
    }

    /**
     * Method to build Solr Input Documents from a given Work Bib Marc Records.
     *
     * @param marcRecords
     * @return
     */
    public List<SolrInputDocument> buildSolrInputDocuments(List<WorkBibMarcRecord> marcRecords) {
        List<SolrInputDocument> solrDocs = new ArrayList<SolrInputDocument>();
        if (marcRecords != null) {
            for (WorkBibMarcRecord record : marcRecords) {
                solrDocs.add(buildSolrInputDocument(record));

            }
        }
        return solrDocs;
    }

    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments,
                                       StopWatch buildSolrInputDocTimer, StopWatch xmlToPojoTimer) {

        IndexerService indexerService = null;
        if (requestDocument != null && requestDocument.getOperation() != null && "checkIn"
                .equalsIgnoreCase(requestDocument.getOperation())) {
            updateBibRecordInSolr(requestDocument, solrInputDocuments);
        } else {
            xmlToPojoTimer.resume();
            WorkBibMarcRecordProcessor recordProcessor = new WorkBibMarcRecordProcessor();
            WorkBibMarcRecord workBibMarcRecord = recordProcessor.fromXML(requestDocument.getContent().getContent())
                    .getRecords().get(0);
            xmlToPojoTimer.suspend();
            buildSolrInputDocTimer.resume();
            SolrInputDocument solrInputDocument = buildSolrInputDocument(workBibMarcRecord);

            if (requestDocument.getUuid() == null) {
                indexerService = getIndexerService(requestDocument);
                requestDocument.setUuid(indexerService.buildUuid());
            }
            solrInputDocument.setField(ID, requestDocument.getUuid());
            solrInputDocument.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(requestDocument.getUuid()));
            solrInputDocument.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(requestDocument.getUuid()));
            solrInputDocument.addField(UNIQUE_ID, requestDocument.getUuid());
            solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
            solrInputDocument.setField(BIB_ID, requestDocument.getUuid());
            if (requestDocument.getAdditionalAttributes() != null) {
                solrInputDocument.setField(STATUS_SEARCH, requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STATUS));
                if(StringUtils.isNotEmpty(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STATUS))){
                    solrInputDocument.setField(AdditionalAttributes.STATUS_UPDATED_ON, getDate(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STATUS_UPDATED_ON)));
                }
            }
            if (requestDocument.getAdditionalAttributes() != null && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG) != null
                    && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG).equalsIgnoreCase(Boolean.TRUE.toString())) {
                solrInputDocument.addField(STAFF_ONLY_FLAG, Boolean.TRUE.toString());
            } else {
                solrInputDocument.addField(STAFF_ONLY_FLAG, Boolean.FALSE.toString());
            }

            String createdBy = requestDocument.getAdditionalAttributes() == null ? null : requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.CREATED_BY);
            createdBy = createdBy == null ? requestDocument.getUser() : createdBy;
            solrInputDocument.setField(CREATED_BY, createdBy);
            solrInputDocument.setField(UPDATED_BY, createdBy);
            Date date = new Date();
            solrInputDocument.setField(DATE_ENTERED, date);
            solrInputDocument.setField(DATE_UPDATED, date);

            solrInputDocuments.add(solrInputDocument);
            for (RequestDocument linkRequestDocument : requestDocument.getLinkedRequestDocuments()) {
                String id = linkRequestDocument.getUuid();
                if (id == null) {
                    indexerService = getIndexerService(requestDocument);
                    id = indexerService.buildUuid();
                    linkRequestDocument.setUuid(id);
                    //id = linkRequestDocument.getId();
                }
                solrInputDocument.addField(INSTANCE_IDENTIFIER, id);
                if(DocType.INSTANCE.getCode().equalsIgnoreCase(linkRequestDocument.getType())) {
                    if (linkRequestDocument.getContent() != null
                            && linkRequestDocument.getContent().getContentObject() != null) {
                        InstanceCollection instanceCollection = (InstanceCollection) linkRequestDocument.getContent()
                                .getContentObject();
                        List<Instance> oleInstanceList = instanceCollection.getInstance();
                        for (Instance oleInstance : oleInstanceList) {
                            // To make sure that bib Id is added for instance but not duplicated.
                            if (!oleInstance.getResourceIdentifier().contains(requestDocument.getUuid())) {
                                // TODO: This logic should be also in docstore side - before creating instance node - so that instance xml has the resourceIdentifier field.
                                oleInstance.getResourceIdentifier().add(requestDocument.getUuid());
                            }
                            oleInstance.setInstanceIdentifier(id);
                        }
                    }
                    new WorkInstanceOlemlDocBuilder().buildSolrInputDocuments(linkRequestDocument, solrInputDocuments);
                }
                else if(DocType.EINSTANCE.getCode().equalsIgnoreCase(linkRequestDocument.getType())) {
                    new WorkEInstanceOlemlDocBuilder().buildSolrInputDocuments(linkRequestDocument, solrInputDocuments);
                }
            }
            buildSolrInputDocTimer.suspend();
        }
    }

    private void updateBibRecordInSolr(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
        IndexerService indexerService = getIndexerService(requestDocument);

        if (requestDocument.getId() != null && requestDocument.getId().length() > 0) {
            List<SolrDocument> solrDocumentList = indexerService.getSolrDocumentBySolrId(requestDocument.getId());
            SolrDocument solrDocument = solrDocumentList.get(0);
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            requestDocument.setUuid(requestDocument.getId());
            if (requestDocument.getContent().getContent() != null) {
                WorkBibMarcRecordProcessor recordProcessor = new WorkBibMarcRecordProcessor();
                WorkBibMarcRecord workBibMarcRecord = recordProcessor.fromXML(requestDocument.getContent().getContent())
                        .getRecords().get(0);
                solrInputDocument = new WorkBibMarcDocBuilder().buildSolrInputDocument(workBibMarcRecord);
                solrInputDocument.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(requestDocument.getUuid()));
                solrInputDocument.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(requestDocument.getUuid()));
                if (requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG) != null
                        && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG).equalsIgnoreCase(Boolean.TRUE.toString())) {
                    solrInputDocument.addField(STAFF_ONLY_FLAG, requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG));
                } else {
                    solrInputDocument.addField(STAFF_ONLY_FLAG, Boolean.FALSE.toString());
                }

                solrInputDocument.setField(STATUS_SEARCH, requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STATUS));
                if(StringUtils.isNotEmpty(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STATUS))){
                    solrInputDocument.setField(AdditionalAttributes.STATUS_UPDATED_ON, getDate(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STATUS_UPDATED_ON)));
                }
                setLinkedRequestDocumentValues(requestDocument, solrInputDocument, solrDocument, solrInputDocuments);
                setCommonFieldsForSolrDoc(solrInputDocument, requestDocument, solrDocument);
                solrInputDocuments.add(solrInputDocument);
            } else {
                buildSolrInputDocFromSolrDoc(solrDocument, solrInputDocument);
                setLinkedRequestDocumentValues(requestDocument, solrInputDocument, solrDocument, solrInputDocuments);
                setCommonFieldsForSolrDoc(solrInputDocument, requestDocument, solrDocument);
                solrInputDocuments.add(solrInputDocument);
            }
        }
    }

    private void setLinkedRequestDocumentValues(RequestDocument requestDocument, SolrInputDocument solrInputDocument,
                                                SolrDocument solrDocument, List<SolrInputDocument> solrInputDocuments) {
        if (requestDocument.getLinkedRequestDocuments() != null
                && requestDocument.getLinkedRequestDocuments().size() > 0) {
            buildLinkedRequestDocumentFields(requestDocument, solrInputDocument, solrDocument, solrInputDocuments);
        } else {
            if (solrDocument != null && solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null) {
                Object instanceIdentifier = solrDocument.getFieldValue(INSTANCE_IDENTIFIER);
                solrInputDocument.addField(INSTANCE_IDENTIFIER, instanceIdentifier);
            }
        }
    }

    private void buildLinkedRequestDocumentFields(RequestDocument requestDocument, SolrInputDocument solrInputDocument,
                                                  SolrDocument solrDocument,
                                                  List<SolrInputDocument> solrInputDocuments) {
        for (RequestDocument linkRequestDocument : requestDocument.getLinkedRequestDocuments()) {
            String id = linkRequestDocument.getUuid();
            if (id == null) {
                id = linkRequestDocument.getId();
            }
            List<SolrDocument> solrDocumentList = getIndexerService(requestDocument).getSolrDocumentBySolrId(
                    linkRequestDocument.getId());
            SolrDocument solrInstDocument = solrDocumentList.get(0);
            SolrInputDocument solrInstInputDocument = new SolrInputDocument();
            buildSolrInputDocFromSolrDoc(solrInstDocument, solrInstInputDocument);
            if (solrInstDocument.getFieldValue("bibIdentifier") != null) {
                if (!solrInstDocument.getFieldValue("bibIdentifier").toString().contains(requestDocument.getId())) {
                    solrInstInputDocument.addField("bibIdentifier", requestDocument.getId());
                }
            } else {
                solrInstInputDocument.addField("bibIdentifier", requestDocument.getId());
            }
            solrInputDocuments.add(solrInstInputDocument);
            if (linkRequestDocument.getType().equalsIgnoreCase(DocType.INSTANCE.getCode()) && solrDocument != null) {
                if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null) {
                    if (!solrDocument.getFieldValue(INSTANCE_IDENTIFIER).toString().contains(id)) {
                        solrInputDocument.addField(INSTANCE_IDENTIFIER, id);
                    }
                } else {
                    solrInputDocument.addField(INSTANCE_IDENTIFIER, id);
                }
            }
        }
    }

    private void setCommonFieldsForSolrDoc(SolrInputDocument solrInputDocument, RequestDocument requestDocument, SolrDocument solrDocument) {
        solrInputDocument.setField(ID, requestDocument.getUuid());
        solrInputDocument.addField(UNIQUE_ID, requestDocument.getUuid());
        solrInputDocument.setField(DOC_CATEGORY, DocCategory.WORK.getCode());
        String updatedBy = requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.UPDATED_BY);
        updatedBy = updatedBy == null ? requestDocument.getUser() : updatedBy;
        solrInputDocument.setField(UPDATED_BY, updatedBy);
        solrInputDocument.setField(DATE_UPDATED, new Date());
        solrInputDocument.setField(CREATED_BY, solrDocument.getFieldValue(CREATED_BY));
        solrInputDocument.setField(DATE_ENTERED, solrDocument.getFieldValue(DATE_ENTERED));
        solrInputDocument.setField(BIB_ID, requestDocument.getUuid());
    }

    public void buildSolrInputDocFromSolrDoc(SolrDocument solrDocument, SolrInputDocument solrInputDocument) {
        if (solrDocument != null) {
            Map<String, Collection<Object>> solrDocMap = solrDocument.getFieldValuesMap();
            if (solrDocMap != null && solrDocMap.size() > 0) {
                Set<String> resultField = solrDocMap.keySet();
                for (Iterator<String> iterator1 = resultField.iterator(); iterator1.hasNext(); ) {
                    String key = iterator1.next();
                    if (!key.equalsIgnoreCase(OleNGConstants._VERSION_)) {
                        Object value = solrDocMap.get(key);
                        solrInputDocument.addField(key, value);
                    }
                }
            }
        }
    }

    public SolrInputDocument buildSolrInputDocFromSolrDoc(SolrDocument solrDocument) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        if (solrDocument != null) {
            Map<String, Collection<Object>> solrDocMap = solrDocument.getFieldValuesMap();
            if (solrDocMap != null && solrDocMap.size() > 0) {
                Set<String> resultField = solrDocMap.keySet();
                for (Iterator<String> iterator1 = resultField.iterator(); iterator1.hasNext(); ) {
                    String key = iterator1.next();
                    if (!key.equalsIgnoreCase(OleNGConstants._VERSION_)) {
                        Object value = solrDocMap.get(key);
                        solrInputDocument.addField(key, value);
                    }
                }
            }
        }
        return solrInputDocument;
    }

    public void addInstIdToBib(String resId, String insId, List<SolrInputDocument> solrInputDocuments) {
        IndexerService indexerService = WorkBibDocumentIndexer.getInstance();
        List<SolrDocument> solrBibDoc = indexerService.getSolrDocumentBySolrId(resId);
        for (SolrDocument bibSolrDoc : indexerService.getSolrDocumentBySolrId(resId)) {
            if (bibSolrDoc.getFieldValue(INSTANCE_IDENTIFIER) != null) {
                if (bibSolrDoc.getFieldValue(INSTANCE_IDENTIFIER) instanceof List) {
                    List instIdList = (List) solrBibDoc.get(0).getFieldValue(INSTANCE_IDENTIFIER);
                    instIdList.add(insId);
                    bibSolrDoc.setField(INSTANCE_IDENTIFIER, instIdList);
                } else if (bibSolrDoc.getFieldValue(INSTANCE_IDENTIFIER) instanceof String) {
                    String instId = (String) solrBibDoc.get(0).getFieldValue(INSTANCE_IDENTIFIER);
                    List<String> instIdList = new ArrayList<String>();
                    instIdList.add(instId);
                    instIdList.add(insId);
                    bibSolrDoc.setField(INSTANCE_IDENTIFIER, instIdList);
                }
            } else {
                bibSolrDoc.setField(INSTANCE_IDENTIFIER, insId);
            }
            solrInputDocuments.add(buildSolrInputDocFromSolrDoc(bibSolrDoc));
        }
    }

    private Date getDate(String dateStr){
        DateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        try {
            if(StringUtils.isNotEmpty(dateStr)){
                return format.parse(dateStr);
            }else{
                return new Date();
            }

        } catch (ParseException e) {
            LOG.error("Error while parsing date:: "+dateStr+" for format:: "+Constants.DATE_FORMAT,e);
            return new Date();
        }
    }
}