package org.kuali.ole.utility;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.util.BibMarcUtil;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;

import java.util.*;

/**
 * Created by SheikS on 2/16/2016.
 */
public class BibUtil implements DocstoreConstants {

    private static final String SEPERATOR_SUB_FIELD = " ";

    private static final String SEPERATOR_HYPHEN = " - ";
    private static final String PATTERN_CHAR = "*";

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
}
