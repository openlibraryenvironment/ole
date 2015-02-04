package org.kuali.ole.docstore.common.util;

import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sambasivam on 17/10/14.
 */
public class BibMarcUtil {

    private static final String SEPERATOR_DATA_FIELD = ", ";
    private static final String SEPERATOR_SUB_FIELD = " ";
    private static final String PATTERN_CHAR = "*";
    private static final String SEPERATOR_HYPHEN = " - ";
    private static final String SEPERATOR_DOUBLE_HYPHEN = " -- ";

    public static DocumentSearchConfig documentSearchConfig = DocumentSearchConfig.getDocumentSearchConfig();

    public static String TITLE_DISPLAY = "Title_display";
    public static String AUTHOR_DISPLAY = "Author_display";
    public static String PUBLISHER_DISPLAY = "Publisher_display";
    public static String ISBN_DISPLAY = "ISBN_display";
    public static String ISSN_DISPLAY = "ISSN_display";


    static {
        List<String> fieldsNames = new ArrayList<>();
        fieldsNames.add(TITLE_DISPLAY);
        fieldsNames.add(AUTHOR_DISPLAY);
        fieldsNames.add(PUBLISHER_DISPLAY);
        fieldsNames.add(ISBN_DISPLAY);
        fieldsNames.add(ISSN_DISPLAY);

        documentSearchConfig.buildIncludeAndExcludeMapping(fieldsNames);
    }


    private static final Logger LOG = LoggerFactory.getLogger(BibMarcUtil.class);

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
    private String getDataFieldValue(String includeTags, String excludeTags, BibMarcRecord record,
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
                    List<SubField> subFields = dataField.getSubFields();
                    if (subFieldIdx != -1) { // Includes only one Sub Field of a main Data Field.
                        if (excludeTags != null && !excludeTags.contains(tag)) {
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
            return fieldValues.get(0);
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
            LOG.info("Exception :", e);
            return false;
        }
    }


    public Map<String, String> buildDataValuesForBibInfo(BibMarcRecord bibMarcRecord) {
        Map<String, String> dataFields = new HashMap<>();


        String titleInclude = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(TITLE_DISPLAY);
        String titleExclude = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(TITLE_DISPLAY);
        String title = this.getDataFieldValue(titleInclude, titleExclude, bibMarcRecord, false, TITLE_DISPLAY);

        String authorInclude = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(AUTHOR_DISPLAY);
        String authorExclude = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(AUTHOR_DISPLAY);
        String author = this.getDataFieldValue(authorInclude, authorExclude, bibMarcRecord, false, AUTHOR_DISPLAY);

        String publisherInclude = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(PUBLISHER_DISPLAY);
        String publisherExclude = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(PUBLISHER_DISPLAY);
        String publisher = this.getDataFieldValue(publisherInclude, publisherExclude, bibMarcRecord, false, PUBLISHER_DISPLAY);

        String isbnInclude = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(ISBN_DISPLAY);
        String isbnExclude = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(ISBN_DISPLAY);
        String isbn = this.getDataFieldValue(isbnInclude, isbnExclude, bibMarcRecord, false, ISBN_DISPLAY);


        String issnInclude = documentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP_SELECTED.get(ISSN_DISPLAY);
        String issnExclude = documentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP_SELECTED.get(ISSN_DISPLAY);
        String issn = this.getDataFieldValue(issnInclude, issnExclude, bibMarcRecord, false, ISSN_DISPLAY);


        dataFields.put(TITLE_DISPLAY, title);
        dataFields.put(AUTHOR_DISPLAY, author);
        dataFields.put(PUBLISHER_DISPLAY, publisher);
        dataFields.put(ISBN_DISPLAY, isbn);
        dataFields.put(ISSN_DISPLAY, issn);

        return dataFields;
    }


}
