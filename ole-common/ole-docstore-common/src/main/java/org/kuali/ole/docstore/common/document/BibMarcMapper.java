package org.kuali.ole.docstore.common.document;

import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 12/27/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */


public class BibMarcMapper {

    private static BibMarcMapper bibMarcMapper = null;
    private static Map<String, String> FIELDS_TO_TAGS_2_INCLUDE_MAP = new HashMap<String, String>();
    private String publicationDateRegex = "[0-9]{4}";

    private BibMarcMapper() {
        FIELDS_TO_TAGS_2_INCLUDE_MAP = Collections.unmodifiableMap(DocumentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP);
    }

    public static BibMarcMapper getInstance() {
        if (bibMarcMapper == null) {
            bibMarcMapper = new BibMarcMapper();
        }
        return bibMarcMapper;
    }

    public void extractFields(BibMarcRecord bibMarc, Bib bib) {
        for (String field : FIELDS_TO_TAGS_2_INCLUDE_MAP.keySet()) {
            buildFields(field, bibMarc, bib);
        }

    }

    private HashMap<String, ArrayList<String>> getTags(String tag) {

        String tags = FIELDS_TO_TAGS_2_INCLUDE_MAP.get(tag);
        String[] tagDetailArray = tags.split(",");

        HashMap<String, ArrayList<String>> dataFieldMap = new HashMap<>();
        for (int i = 0; i < tagDetailArray.length; i++) {
            ArrayList<String> subFieldList = new ArrayList<>();
            String dataField = null;
            String subFieldSplit[] = null;
            String[] tagSplit = tagDetailArray[i].split("-");
            if (tagSplit.length > 0) {
                dataField = tagSplit[0];
            }
            if (tagSplit.length > 1) {
                subFieldSplit = tagSplit[1].split(";");
                for (int subFieldCount = 0; subFieldCount < subFieldSplit.length; subFieldCount++) {
                    subFieldList.add(subFieldSplit[subFieldCount]);
                }
            }

            dataFieldMap.put(dataField, subFieldList);

        }

        return dataFieldMap;

    }


    public void buildFields(String field, BibMarcRecord bibMarc, Bib bib) {
        DataField dataField;

        if (field.equalsIgnoreCase("Title_display")) {
            HashMap<String, ArrayList<String>> titleDisplayMap = getTags("Title_display");
            StringBuilder title = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> titleDisplayMapEntry : titleDisplayMap.entrySet()) {
                String key = titleDisplayMapEntry.getKey();
                Object value = titleDisplayMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                title.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }
            String titleBib=title.toString();
            titleBib = titleBib.replaceAll("<","&lt;");
            titleBib = titleBib.replaceAll(">","&gt;");
            bib.setTitle(titleBib);
        } else if (field.equalsIgnoreCase("Author_display")) {
            HashMap<String, ArrayList<String>> authorDisplayMap = getTags("Author_display");
            StringBuilder author = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> authorDisplayMapEntry : authorDisplayMap.entrySet()) {
                String key = authorDisplayMapEntry.getKey();
                Object value = authorDisplayMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                author.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }

            bib.setAuthor(author.toString());
        } else if (field.equalsIgnoreCase("Publisher_display")) {
            // Publisher and publisher are separate we want them as single
            HashMap<String, ArrayList<String>> publicDisplayMap = getTags("PublicationPlace_display");
            StringBuilder publisher = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> publicDisplayMapEntry : publicDisplayMap.entrySet()) {
                String key = publicDisplayMapEntry.getKey();
                Object value = publicDisplayMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                if (publisher.length() > 0) {
                                    publisher.append(" ");
                                }
                                publisher.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }
            publicDisplayMap = getTags("Publisher_display");
            for (Map.Entry<String, ArrayList<String>> publicDisplayMapEntry : publicDisplayMap.entrySet()) {
                String key = publicDisplayMapEntry.getKey();
                Object value = publicDisplayMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                if (publisher.length() > 0) {
                                    publisher.append(" ");
                                }
                                publisher.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }
            bib.setPublisher(publisher.toString());
        } else if (field.equalsIgnoreCase("PublicationDate_display")) {

            HashMap<String, ArrayList<String>> publicationDateMap = getTags("PublicationDate_display");
            StringBuilder publisherDate = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> publicationDateMapEntry : publicationDateMap.entrySet()) {
                String key = publicationDateMapEntry.getKey();
                Object value = publicationDateMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                publisherDate.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }
            bib.setPublicationDate(publisherDate.toString());
        } else if (field.equalsIgnoreCase("ISBN_display")) {
            HashMap<String, ArrayList<String>> isbnMap = getTags("ISBN_display");
            StringBuilder isbn = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> isbnMapEntry : isbnMap.entrySet()) {
                String key = isbnMapEntry.getKey();
                Object value = isbnMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                isbn.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }
            bib.setIsbn(isbn.toString());
        } else if (field.equalsIgnoreCase("ISSN_display")) {
            HashMap<String, ArrayList<String>> issnMap = getTags("ISSN_display");
            StringBuilder issn = new StringBuilder();
            for (Map.Entry<String, ArrayList<String>> issnMapEntry : issnMap.entrySet()) {
                String key = issnMapEntry.getKey();
                Object value = issnMapEntry.getValue();
                ArrayList<String> subFieldList = (ArrayList<String>) value;
                dataField = bibMarc.getDataFieldForTag(key);
                if (dataField != null) {
                    for (SubField subField : dataField.getSubFields()) {
                        for (String subFieldStr : subFieldList) {
                            if (subField.getCode().equalsIgnoreCase(subFieldStr)) {
                                issn.append(subField.getValue());
                                break;
                            }
                        }
                    }

                }
            }
            bib.setIssn(issn.toString());
        }


    }

    private String extractPublicationDateWithRegex(String publicationDate) {
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

}
