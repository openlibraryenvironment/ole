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
    /** Logger for {@link #getPropertyValues(InputStream)}. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Validate the record. Return an empty list if all is ok, otherwise return a list of error messages.
     * @param workBibMarcRecord  Record to validate
     * @return  error messages
     */
    public final List<DocStoreValidationError> validate(final WorkBibMarcRecord workBibMarcRecord) {
        List<DocStoreValidationError> docStoreValidationErrors = new ArrayList<>();
        if (workBibMarcRecord == null) {
            return docStoreValidationErrors;
        }

        validateControlFields(workBibMarcRecord, docStoreValidationErrors);
        validateLeader(workBibMarcRecord, docStoreValidationErrors);
        validateDataFields(workBibMarcRecord, docStoreValidationErrors);
        validateTitleTag(workBibMarcRecord, docStoreValidationErrors);
        return docStoreValidationErrors;
    }

    /**
     * Add a DocStoreValidationError with the errorId to errors.
     * @param errors   List to add the error to
     * @param errorId  The id to use for the error
     */
    private void add(final List<DocStoreValidationError> errors,
                     final String errorId) {

        DocStoreValidationError error = new DocStoreValidationError();
        error.setErrorId(errorId);
        errors.add(error);
    }

    /**
     * Create a DocStoreValidationError with the errorId and param
     * and add it to errors.
     * @param errors    List to add the error to
     * @param errorId   The id to use for the error
     * @param param     Parameter for the error
     */
    private void add(final List<DocStoreValidationError> errors,
                     final String errorId, final String param) {

        DocStoreValidationError error = new DocStoreValidationError();
        error.setErrorId(errorId);
        error.addParams(StringUtils.defaultString(param));
        errors.add(error);
    }

    /**
     * Create a DocStoreValidationError with the errorId, param1 and param2
     * and add it to errors.
     * @param errors    List to add the error to
     * @param errorId   The id to use for the error
     * @param param1    First parameter to add to the error
     * @param param2    Second parameter to add to the error
     */
    private void add(final List<DocStoreValidationError> errors,
                     final String errorId, final String param1, final String param2) {

        DocStoreValidationError error = new DocStoreValidationError();
        error.setErrorId(errorId);
        error.addParams(StringUtils.defaultString(param1));
        error.addParams(StringUtils.defaultString(param2));
        errors.add(error);
    }

    /**
     * Validate the data fields of the record.  On error add error messages to errors.
     * @param workBibMarcRecord Record to validate
     * @param errors            List to add the error messages to
     */
    private void validateDataFields(final WorkBibMarcRecord workBibMarcRecord,
                                    final List<DocStoreValidationError> errors) {

        for (DataField dataField : workBibMarcRecord.getDataFields()) {
            validateTagSubtags(dataField, errors);
            validateTagLength(dataField, errors);
            validateInd      (dataField, errors);
        }
    }

    /**
     * Validate the tag and the subfields of the dataField.  On error add error messages to errors.
     * @param dataField The DataField to validate
     * @param errors    List to add the error messages to
     */
    private void validateTagSubtags(final DataField dataField,
                                    final List<DocStoreValidationError> errors) {

        String tag = StringUtils.defaultString(dataField.getTag());
        if (tag.startsWith("00")) {
            add(errors, "marc.editor.invalid.tag", tag);
        }
        if (dataField.getSubFields() == null) {
            return;
        }
        for (SubField subfield : dataField.getSubFields()) {
            String code  = StringUtils.defaultString(subfield.getCode());
            String value = StringUtils.defaultString(subfield.getValue());

            if (!code.matches("[a-z0-9]")) {
                add(errors, "error.bib.enter.missing.subfield", code, tag);
            }

            if (value.equals("")) {
                add(errors, "error.bib.enter.valid.subfield.value", code, tag);
            }
        }
    }

    /**
     * Validate the control fields of the record.  On error add error messages to errors.
     * @param workBibMarcRecord The record whose control fields to validate
     * @param errors            List to add the error messages to
     */
    private void validateControlFields(final WorkBibMarcRecord workBibMarcRecord,
                                       final List<DocStoreValidationError> errors) {

        boolean is008Available = false;

        for (ControlField controlField : workBibMarcRecord.getControlFields()) {
            String tag   = StringUtils.defaultString(controlField.getTag());
            String value = StringUtils.defaultString(controlField.getValue());

            if (!(tag.startsWith("00"))) {
                add(errors, "marc.editor.required.control.invalid", tag);
            }
            if (tag.equals("008")) {
                is008Available = true;
                if (value.length() < 40) {
                    add(errors, "marc.editor.required.control.length.008",
                            String.valueOf(value.length()));
                }
            }
            if (tag.equals("006")) {
                if (value.length() < 18) {
                    add(errors, "marc.editor.required.control.length.006",
                            String.valueOf(value.length()));
                }
            }
        }
        if (!is008Available) {
            add(errors, "marc.editor.required.control.008");
        }
    }

    /**
     * Validate the leader field of the record.  On error add error messages to errors.
     * @param workBibMarcRecord The record whose leader field to validate
     * @param errors            List to add the error messages to
     */
    private void validateLeader(final WorkBibMarcRecord workBibMarcRecord,
                                final List<DocStoreValidationError> errors) {

        String leader = StringUtils.defaultString(workBibMarcRecord.getLeader());

        if (leader.trim().equals("")) {
            add(errors, "marc.editor.required.control.leader");
            return;
        }

        if(leader.length()!=24){
            add(errors, "marc.editor.required.control.leader.length", Integer.toString(leader.length()));
        }

        String charset = StringUtils.substring(leader, 9, 10);
        if (charset.equals("")) {       // charset not specified
            return;
        }
        if (!charset.equals("a")) {
            add(errors, "marc.editor.required.control.leader.unicode", charset);
        }
    }

    /**
     * Validate the record whether the title tag 245 is present and has a none-empty a subfield.
     * On error add error messages to errors.
     * @param workBibMarcRecord The record whose title tag to validate
     * @param errors            List to add the error message to
     */
    private void validateTitleTag(final WorkBibMarcRecord workBibMarcRecord,
                                  final List<DocStoreValidationError> errors) {

        Boolean is245present = false;
        Boolean isSubfield = false;
        for (DataField dataField : workBibMarcRecord.getDataFields()) {
            String tag = StringUtils.defaultString(dataField.getTag());

            if (!tag.equals("245")) {
                continue;
            }

            is245present = true;

            if (dataField.getSubFields() == null) {
                continue;
            }
            for (SubField subField : dataField.getSubFields()) {
                String code  = StringUtils.defaultString(subField.getCode());
                String value = StringUtils.defaultString(subField.getValue());
                // 245 must have a subfield a or a subfield k:
                // https://openlibraryenvironment.atlassian.net/browse/OLE-8312
                if (! code.equals("a") && ! code.equals("k")) {
                    continue;
                }
                if (value.equals("")) {
                    continue;
                }
                isSubfield = true;
            }
        }

        if (!is245present) {
            add(errors, "marc.editor.invalid.title");
            return;
        }

        if (!isSubfield) {
            add(errors, "marc.editor.invalid.title.field");
        }
    }

    /**
     * Validate the tag of the data field.  On error add error messages to errors.
     * @param dataField The DataField whose tag to validate
     * @param errors    List to add the error messages to
     */
    private void validateTagLength(final DataField dataField,
                                   final List<DocStoreValidationError> errors) {

        String tag = StringUtils.defaultString(dataField.getTag());
        if (!tag.matches("[0-9][0-9][0-9]")) {
            add(errors, "marc.editor.invalid.tag.length");
        }
    }

    /**
     * Validate the indicator.  On error add error messages to errors.
     * @param ind       The indicator value to validate
     * @param tag       The data field tag the indicator belongs to
     * @param errors    List to add the error messages to
     */
    private void validateInd(final String ind, final String tag,
                             final List<DocStoreValidationError> errors) {

        if (ind == null || ind.matches("[0-9# ]?")) {
            return;
        }

        add(errors, "marc.editor.invalid.ind.length", ind, tag);
    }

    /**
     * Validate the indicators.  On error add error messages to errors.
     * @param dataField The data field whose indicators to validate
     * @param errors    List to add the error messages to
     */
    private void validateInd(final DataField dataField,
                             final List<DocStoreValidationError> errors) {
        validateInd(dataField.getInd1().equals("\\")?"/":dataField.getInd1(), dataField.getTag(), errors);
        validateInd(dataField.getInd2().equals("\\")?"/":dataField.getInd2(), dataField.getTag(), errors);
    }

    /**
     * Load the properties from inputStream.
     * @param inputStream       The InputStream to load the properties from
     * @return                  the loaded Properties
     */
    public final Properties getPropertyValues(final InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Unable to load properties", e);
        }
        return properties;
    }
}
