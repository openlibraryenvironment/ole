package org.kuali.ole.docstore.common.document;

import org.kuali.ole.docstore.common.document.config.DocumentSearchConfig;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.ControlField;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;

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
    private static Map<String, String> FIELDS_TO_TAGS_2_EXCLUDE_MAP = new HashMap<String, String>();
    private String publicationDateRegex = "[0-9]{4}";

    private BibMarcMapper() {
        DocumentSearchConfig.getDocumentSearchConfig();
        FIELDS_TO_TAGS_2_INCLUDE_MAP = Collections.unmodifiableMap(DocumentSearchConfig.FIELDS_TO_TAGS_2_INCLUDE_MAP);
        FIELDS_TO_TAGS_2_EXCLUDE_MAP = Collections.unmodifiableMap(DocumentSearchConfig.FIELDS_TO_TAGS_2_EXCLUDE_MAP);
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

    public void buildFields(String field, BibMarcRecord bibMarc, Bib bib) {
        DataField dataField;

        if (field.equalsIgnoreCase("Title_display")) {
            StringBuilder title = new StringBuilder();
            dataField = bibMarc.getDataFieldForTag("245");
            if(dataField!=null) {
            for (SubField subField : dataField.getSubFields()) {
                if (subField.getCode().equalsIgnoreCase("a")) {
                    title.append(subField.getValue());
                } else if (subField.getCode().equalsIgnoreCase("b")) {
                    title.append(subField.getValue());
                }
            }
            }
            bib.setTitle(title.toString());
        } else if (field.equalsIgnoreCase("Author_display")) {
            StringBuilder author = new StringBuilder();
            dataField = bibMarc.getDataFieldForTag("100");
            if(dataField!=null) {
                for (SubField subField : dataField.getSubFields()) {
                    if (subField.getCode().equalsIgnoreCase("a")) {
                        author.append(subField.getValue());
                    }
                }
            }
            dataField = bibMarc.getDataFieldForTag("110");
            if(dataField!=null) {
                for (SubField subField : dataField.getSubFields()) {
                    if (subField.getCode().equalsIgnoreCase("a")) {
                        author.append(subField.getValue());
                    }
                }
            }

            bib.setAuthor(author.toString());
        }
        else if (field.equalsIgnoreCase("Publisher_display")) {
            StringBuilder publisher = new StringBuilder();
            dataField = bibMarc.getDataFieldForTag("260");
            if(dataField!=null) {
                for (SubField subField : dataField.getSubFields()) {
                    if (subField.getCode().equalsIgnoreCase("a")) {
                        publisher.append(subField.getValue());
                    } else if (subField.getCode().equalsIgnoreCase("b")) {
                        publisher.append(subField.getValue());
                    }
                }
            }
            bib.setPublisher(publisher.toString());
        }
        else if(field.equalsIgnoreCase("PublicationDate_display")) {
            String publicationDate = "";
            for (ControlField controlField : bibMarc.getControlFields()) {
                if (controlField.getTag().equalsIgnoreCase("008")) {
                    String controlField008 = controlField.getValue();
                    if (controlField008 != null && controlField008.length() > 10) {
                        publicationDate = controlField008.substring(7, 11);
                        publicationDate = extractPublicationDateWithRegex(publicationDate);
                    }
                }
            }

            if (publicationDate == null || publicationDate.trim().length() == 0) {
                dataField = bibMarc.getDataFieldForTag("260");
                if(dataField!=null) {
                    for (SubField subField : dataField.getSubFields()) {

                       if(subField.getCode().equalsIgnoreCase("c")){
                           publicationDate=subField.getValue();
                        }
                    }
                }
                publicationDate = extractPublicationDateWithRegex(publicationDate);
            }

            bib.setPublicationDate(publicationDate);
        }else if (field.equalsIgnoreCase("ISBN_display")) {
            StringBuilder isbn = new StringBuilder();
            dataField = bibMarc.getDataFieldForTag("020");
            if(dataField!=null) {
                for (SubField subField : dataField.getSubFields()) {
                    if (subField.getCode().equalsIgnoreCase("a")) {
                        isbn.append(subField.getValue());
                    } else if (subField.getCode().equalsIgnoreCase("z")) {
                        isbn.append(subField.getValue());
                    }
                }
            }
            bib.setIsbn(isbn.toString());
        }
        else if (field.equalsIgnoreCase("ISSN_display")) {
            StringBuilder issn = new StringBuilder();
            dataField = bibMarc.getDataFieldForTag("022");
            if(dataField!=null) {
                for (SubField subField : dataField.getSubFields()) {
                    if (subField.getCode().equalsIgnoreCase("a")) {
                        issn.append(subField.getValue());
                    } else if (subField.getCode().equalsIgnoreCase("z")) {
                        issn.append(subField.getValue());
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
