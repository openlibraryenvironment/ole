package org.kuali.ole.docstore.validation;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 10/15/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkBibMarcRecordValidator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public List<DocStoreValidationError> validate(WorkBibMarcRecord workBibMarcRecord) {
        List<DocStoreValidationError> docStoreValidationErrors = new ArrayList<>();
        if (workBibMarcRecord != null) {
            validateControlFields(workBibMarcRecord, docStoreValidationErrors);
            validateLeader(workBibMarcRecord, docStoreValidationErrors);
            validateDataFields(workBibMarcRecord, docStoreValidationErrors);
        }
        return docStoreValidationErrors;
    }

    private void validateDataFields(WorkBibMarcRecord workBibMarcRecord, List<DocStoreValidationError> docStoreValidationErrors) {
        DocStoreValidationError docStoreValidationError;
        if (workBibMarcRecord != null && workBibMarcRecord.getDataFields() != null) {
            for (DataField dataField : workBibMarcRecord.getDataFields()) {
                String tag = dataField.getTag();
                if (tag.startsWith("00")) {
                    docStoreValidationError = new DocStoreValidationError();
                    docStoreValidationError.setErrorId("marc.editor.invalid.tag");
                    docStoreValidationError.addParams(tag);
                    docStoreValidationErrors.add(docStoreValidationError);
                }
                for (SubField subfield : dataField.getSubFields()) {
                    if (subfield.getCode().equals("")) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("error.bib.enter.missing.subfield");
                        docStoreValidationError.addParams(subfield.getCode());
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);

                    }

                   if (!subfield.getCode().matches("[a-z]") && !subfield.getCode().matches("[0-9]") ){
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("error.bib.enter.missing.subfield");
                        docStoreValidationError.addParams(subfield.getCode());
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);

                    }


                    if ( subfield.getCode().equals(" ") ) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("error.bib.enter.missing.subfield");
                        docStoreValidationError.addParams(subfield.getCode());
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);

                    }

                    if (subfield.getValue().equals("")) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("error.bib.enter.valid.subfield.value");
                        docStoreValidationError.addParams(subfield.getCode());
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);

                    }


                    if (subfield.getValue().contains("|") && subfield.getValue().equals("")) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("error.subfield.enter.valid.text.at");
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);

                    }

                }
            }
        }
        validateTittleTag(workBibMarcRecord, docStoreValidationErrors);
        validateTagLength(workBibMarcRecord, docStoreValidationErrors);
        validateInd(workBibMarcRecord, docStoreValidationErrors);
    }


    private void validateControlFields(WorkBibMarcRecord workBibMarcRecord, List<DocStoreValidationError> docStoreValidationErrors) {
        DocStoreValidationError docStoreValidationError;
        boolean is008Available = false;
        if (workBibMarcRecord != null && workBibMarcRecord.getControlFields() != null) {
            for (ControlField controlField : workBibMarcRecord.getControlFields()) {
                String tag = controlField.getTag();
                if (!(tag.startsWith("00"))) {
                    docStoreValidationError = new DocStoreValidationError();
                    docStoreValidationError.setErrorId("marc.editor.required.control.invalid");
                    docStoreValidationError.addParams(tag);
                    docStoreValidationErrors.add(docStoreValidationError);
                    docStoreValidationErrors.add(docStoreValidationError);
                }
                if (tag.equalsIgnoreCase("008")) {
                    is008Available = true;
                    if (controlField.getValue() != null && controlField.getValue().length() < 40) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("marc.editor.required.control.length.008");
                        docStoreValidationError.addParams(String.valueOf(controlField.getValue().length()));
                        docStoreValidationErrors.add(docStoreValidationError);
                    }
                }
                if (tag.equalsIgnoreCase("006")) {
                    if (controlField.getValue() != null && controlField.getValue().length() < 18) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("marc.editor.required.control.length.006");
                        docStoreValidationError.addParams(String.valueOf(controlField.getValue().length()));
                        docStoreValidationErrors.add(docStoreValidationError);
                    }
                }
            }
            if (!is008Available) {
                docStoreValidationError = new DocStoreValidationError();
                docStoreValidationError.setErrorId("marc.editor.required.control.008");
                docStoreValidationErrors.add(docStoreValidationError);
            }
        }
    }

    private void validateLeader(WorkBibMarcRecord workBibMarcRecord, List<DocStoreValidationError> docStoreValidationErrors) {
        DocStoreValidationError docStoreValidationError;
        if (workBibMarcRecord != null && workBibMarcRecord.getLeader() != null && !workBibMarcRecord.getLeader()
                .equals("")) {
          /*  if (workBibMarcRecord.getLeader().length() < 24) {
                docStoreValidationError = new DocStoreValidationError();
                docStoreValidationError.setErrorId("marc.editor.required.control.leader.length");
                docStoreValidationError.addParams(String.valueOf(workBibMarcRecord.getLeader().length()));
                docStoreValidationErrors.add(docStoreValidationError);
            }*/
            if (workBibMarcRecord.getLeader().length()>=10 && !workBibMarcRecord.getLeader().substring(9, 10).equals("a")) {
                docStoreValidationError = new DocStoreValidationError();
                docStoreValidationError.setErrorId("marc.editor.required.control.leader.unicode");
                docStoreValidationError.addParams(String.valueOf(workBibMarcRecord.getLeader().substring(9, 10)));
                docStoreValidationErrors.add(docStoreValidationError);
            }
        } else {
            if (workBibMarcRecord.getLeader().trim().equals("")) {
                docStoreValidationError = new DocStoreValidationError();
                docStoreValidationError.setErrorId("marc.editor.required.control.leader");
                docStoreValidationErrors.add(docStoreValidationError);
            }
        }
    }

    private void validateTittleTag(WorkBibMarcRecord workBibMarcRecord, List<DocStoreValidationError> docStoreValidationErrors) {
        DocStoreValidationError docStoreValidationError;
        if (workBibMarcRecord != null && workBibMarcRecord.getDataFields() != null) {
            Boolean is245present = false;
            Boolean isSubfield =false;
            for (DataField dataField : workBibMarcRecord.getDataFields()) {
                String tag = dataField.getTag();
                if (tag.equals("245")) {
                    is245present = true;
                    for (SubField subField : dataField.getSubFields()) {
                        if (subField.getCode() != null && subField.getCode().equals("a") && !subField.getValue()
                                .equals("")) {
                            isSubfield =true;
                        }
                        if (subField.getCode() != null && subField.getCode().equals("k") && !subField.getValue()
                                .equals("")) {
                            isSubfield =true;
                        }
                    }
                }
            }

            if (!isSubfield) {
                docStoreValidationError = new DocStoreValidationError();
                docStoreValidationError.setErrorId("marc.editor.invalid.title.field");
                docStoreValidationErrors.add(docStoreValidationError);
            }
            if (!is245present) {
                docStoreValidationError = new DocStoreValidationError();
                docStoreValidationError.setErrorId("marc.editor.invalid.title");
                docStoreValidationErrors.add(docStoreValidationError);
            }
        }
    }

    private void validateTagLength(WorkBibMarcRecord workBibMarcRecord, List<DocStoreValidationError> docStoreValidationErrors) {
        DocStoreValidationError docStoreValidationError;
        if (workBibMarcRecord != null && workBibMarcRecord.getDataFields() != null) {

            for (DataField dataField : workBibMarcRecord.getDataFields()) {
                String tag = dataField.getTag();
                if (tag.length() < 3) {
                    docStoreValidationError = new DocStoreValidationError();
                    docStoreValidationError.setErrorId("marc.editor.invalid.tag.length");
                    docStoreValidationErrors.add(docStoreValidationError);

                }
                try {
                    Integer.parseInt(tag);
                } catch (NumberFormatException ex) {
                    docStoreValidationError = new DocStoreValidationError();
                    docStoreValidationError.setErrorId("marc.editor.invalid.tag.length");
                    docStoreValidationErrors.add(docStoreValidationError);
                }
            }
        }

    }

    private void validateInd(WorkBibMarcRecord workBibMarcRecord, List<DocStoreValidationError>
            docStoreValidationErrors) {
        DocStoreValidationError docStoreValidationError;
        if (workBibMarcRecord != null && workBibMarcRecord.getDataFields() != null) {
            for (DataField dataField : workBibMarcRecord.getDataFields()) {
                if (StringUtils.isNotBlank(dataField.getInd1()) && StringUtils.isNotEmpty(dataField.getInd1()) &&
                        !dataField.getInd1().equals("#")) {
                    try {
                        Integer.parseInt(dataField.getInd1());
                    } catch (NumberFormatException ex) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("marc.editor.invalid.ind.length");
                        docStoreValidationError.addParams(dataField.getInd1());
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);
                    }
                }
                if (StringUtils.isNotBlank(dataField.getInd2()) && StringUtils.isNotEmpty(dataField.getInd2()) &&
                        !dataField.getInd2().equals("#")) {
                    try {
                        Integer.parseInt(dataField.getInd2());
                    } catch (NumberFormatException ex) {
                        docStoreValidationError = new DocStoreValidationError();
                        docStoreValidationError.setErrorId("marc.editor.invalid.ind.length");
                        docStoreValidationError.addParams(dataField.getInd2());
                        docStoreValidationError.addParams(dataField.getTag());
                        docStoreValidationErrors.add(docStoreValidationError);
                    }
                }
            }
        }

    }



    public Properties getPropertyValues(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Unable to load properties", e);
        }
        return properties;
    }


}


