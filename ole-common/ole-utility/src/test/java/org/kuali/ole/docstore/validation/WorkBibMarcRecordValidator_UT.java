package org.kuali.ole.docstore.validation;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.WorkBibMarcRecord;

public class WorkBibMarcRecordValidator_UT {
    private WorkBibMarcRecordValidator workBibMarcRecordValidator;

    @Before
    public void tearUp() {
        workBibMarcRecordValidator = new WorkBibMarcRecordValidator();
    }

    private DataField data(String tag, String ind1, String ind2, String code, String value) {
        DataField dataField = new DataField(tag);
        dataField.setInd1(ind1);
        dataField.setInd2(ind2);
        SubField subField = new SubField(code);
        subField.setValue(value);
        dataField.addSubField(subField);
        return dataField;
    }

    private WorkBibMarcRecord record() {
        WorkBibMarcRecord workBibMarcRecord = new WorkBibMarcRecord();
        workBibMarcRecord.setLeader("#####nam#a22######a#4500");
        ControlField controlField = new ControlField("008");
        controlField.setValue("######s########xxu###########000#0#eng#d");
        workBibMarcRecord.addControlFields(controlField);
        return workBibMarcRecord;
    }

    private WorkBibMarcRecord record(String tag, String ind1, String ind2, String code, String value) {
        WorkBibMarcRecord workBibMarcRecord = record();
        workBibMarcRecord.addMarcDataField(data(tag, ind1, ind2, code, value));
        return workBibMarcRecord;
    }

    private WorkBibMarcRecord record245() {
        return record("245", "1", "0", "a", "Title");
    }

    private WorkBibMarcRecord record245(String tag, String value) {
        WorkBibMarcRecord workBibMarcRecord = record245();
        ControlField controlField = new ControlField(tag);
        controlField.setValue(value);
        workBibMarcRecord.addControlFields(controlField);
        return workBibMarcRecord;
    }

    private WorkBibMarcRecord record245(String tag, String ind1, String ind2, String code, String value) {
        WorkBibMarcRecord workBibMarcRecord = record245();
        workBibMarcRecord.addMarcDataField(data(tag, ind1, ind2, code, value));
        return workBibMarcRecord;
    }

    /**
     * Return a String representation of the error.
     * @param err       -       error to collect the
     * @return
     */
    private String docStoreValidationErrorToString(DocStoreValidationError err) {
        StringBuilder s = new StringBuilder();

        s.append(err.getErrorId());

        if (err.getErrorParams() == null) {
            return s.toString();
        }

        for (String param : err.getErrorParams()) {
            s.append(" - ");
            s.append(param);
        }

        return s.toString();
    }

    /**
     * Assert that the record produces no error.
     * @param workBibMarcRecord -       record to validate
     */
    private void validate(WorkBibMarcRecord workBibMarcRecord) {
        List<DocStoreValidationError> errorList = workBibMarcRecordValidator.validate(workBibMarcRecord);

        if (errorList.size() == 0) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        for (DocStoreValidationError error : errorList) {
            if (msg.length() > 0) {
                msg.append(" # ");
            }
            msg.append(docStoreValidationErrorToString(error));
        }

        fail("Unexpected error(s): " + msg);
    }

    /**
     * Assert that the record produces an error with the expected text.
     * @param workBibMarcRecord -       record to validate
     * @param expected          -       expected error message
     */
    private void validate(WorkBibMarcRecord workBibMarcRecord, String expected) {
        List<DocStoreValidationError> errorList = workBibMarcRecordValidator.validate(workBibMarcRecord);
        List<String> actual = new ArrayList<>(errorList.size());

        for (DocStoreValidationError error : errorList) {
            actual.add(docStoreValidationErrorToString(error));
        }

        if (actual.contains(expected)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        for (String single : actual) {
            if (msg.length() > 0) {
                msg.append(" # ");
            }
            msg.append(single);
        }

        if (msg.length() == 0) {
            msg.append("none");
        }

        fail("Expected: " + expected + ", Actual: " + msg);
    }

    @Test
    public void validateNull() {
        validate(null);
    }

    @Test
    public void validateControlFieldsDataFieldsNull() {
        WorkBibMarcRecord record = new WorkBibMarcRecord();
        record.setControlFields(null);
        record.setDataFields(null);
        validate(record, "marc.editor.invalid.title");
    }

    @Test
    public void validate245() {
        validate(record245());
    }

    @Test
    public void validate009() {
        validate(record245("009", "1", "0", "a", "foo"), "marc.editor.invalid.tag - 009");
    }

    @Test
    public void validateSubcodea() {
        validate(record245("100", "1", "0", "a", "foo"));
    }

    @Test
    public void validateSubcodez() {
        validate(record245("100", "1", "0", "z", "foo"));
    }

    @Test
    public void validateSubcodeZero() {
        validate(record245("100", "1", "0", "0", "foo"));
    }

    @Test
    public void validateSubcodeNine() {
        validate(record245("100", "1", "0", "9", "foo"));
    }

    @Test
    public void validateSubcodeEmpty() {
        validate(record245("100", "1", "0", "", "foo"), "error.bib.enter.missing.subfield -  - 100");
    }

    @Test
    public void validateSubcodeSpace() {
        validate(record245("100", "1", "0", " ", "foo"), "error.bib.enter.missing.subfield -   - 100");
    }

    @Test
    public void validateSubcodeAmpersand() {
        validate(record245("100", "1", "0", "&", "foo"), "error.bib.enter.missing.subfield - & - 100");
    }

    @Test
    public void validateSubcodeCapitalA() {
        validate(record245("100", "1", "0", "A", "foo"), "error.bib.enter.missing.subfield - A - 100");
    }

    @Test
    public void validateControl010() {
        validate(record245("010", "value"), "marc.editor.required.control.invalid - 010");
    }

    @Test
    public void validateControl008Null() {      // null is 0 characters long
        validate(record245("008", null), "marc.editor.required.control.length.008 - 0");
    }

    @Test
    public void validateControl008Length39() {  // value is 39 characters long
        validate(record245("008", "######s########xxu###########000#0#eng#"), "marc.editor.required.control.length.008 - 39");
    }

    @Test
    public void validateControl006Null() {      // null is 0 characters long
        validate(record245("006", null), "marc.editor.required.control.length.006 - 0");
    }

    @Test
    public void validateControl006Length17() {  // value is 17 characters long
        validate(record245("006", "u###########000#0"), "marc.editor.required.control.length.006 - 17");
    }

    @Test
    public void validateControl006Length18() {  // value is 18 characters long
        validate(record245("006", "u###########000#0#"));
    }

    @Test
    public void validateControl008Missing() {
        WorkBibMarcRecord record = new WorkBibMarcRecord();
        record.setLeader("#####nam#a22######a#4500");
        validate(record, "marc.editor.required.control.008");
    }

    @Test
    public void validateLeaderNonUnicode() {
        WorkBibMarcRecord record = record245();
        record.setLeader("#####nam#X22######a#4500");
        validate(record, "marc.editor.required.control.leader.unicode - X");
    }

    @Test
    public void validateLeaderNull() {
        WorkBibMarcRecord record = record245();
        record.setLeader(null);
        validate(record, "marc.editor.required.control.leader");
    }

    @Test
    public void validateLeaderEmpty() {
        WorkBibMarcRecord record = record245();
        record.setLeader("");
        validate(record, "marc.editor.required.control.leader");
    }

    @Test
    public void validateLeaderShort() {
        WorkBibMarcRecord record = record245();
        record.setLeader("#####");
        validate(record);
    }

    @Test
    public void validate245Missing() {
        validate(record(), "marc.editor.invalid.title");
    }

    @Test
    public void validate245OnlySubfieldA() {
        validate(record("245", "1", "0", "a", "foo"));
    }

    @Test
    public void validate245OnlySubfieldB() {
        validate(record("245", "1", "0", "b", "foo"), "marc.editor.invalid.title.field");
    }

    @Test
    public void validate245OnlySubfieldK() {
        validate(record("245", "1", "0", "k", "foo"));
    }

    @Test
    public void validate245NullA() {
        validate(record("245", "1", "0", "a", null), "marc.editor.invalid.title.field");
    }

    @Test
    public void validate245EmptyA() {
        validate(record("245", "1", "0", "a", ""), "marc.editor.invalid.title.field");
    }

    @Test
    public void validate245WithoutSubfields() {
        WorkBibMarcRecord record = record245();
        record.getDataFieldForTag("245").setSubFields(null);
        validate(record, "marc.editor.invalid.title.field");
    }

    @Test
    public void validateTagNull() {
        validate(record245(null, "1", "0", "a", "foo"), "marc.editor.invalid.tag.length");
    }

    @Test
    public void validateTagEmpty() {
        validate(record245("", "1", "0", "a", "foo"), "marc.editor.invalid.tag.length");
    }

    @Test
    public void validateTag1() {
        validate(record245("1", "1", "0", "a", "foo"), "marc.editor.invalid.tag.length");
    }

    @Test
    public void validateTag99() {
        validate(record245("99", "1", "0", "a", "foo"), "marc.editor.invalid.tag.length");
    }

    @Test
    public void validateTagPlus99() {
        validate(record245("+99", "1", "0", "a", "foo"), "marc.editor.invalid.tag.length");
    }

    @Test
    public void validateIndNull() {
        validate(record("245", null, "0", "a", "Title"));
        validate(record("245", "1", null, "a", "Title"));
    }

    @Test
    public void validateIndEmpty() {
        validate(record("245", "", "0", "a", "Title"));
        validate(record("245", "1", "", "a", "Title"));
    }

    @Test
    public void validateIndSpace() {
        validate(record("245", " ", "0", "a", "Title"));
        validate(record("245", "1", " ", "a", "Title"));
    }

    @Test
    public void validateIndHash() {
        validate(record("245", "#", "0", "a", "Title"));
        validate(record("245", "1", "#", "a", "Title"));
    }

    @Test
    public void validateInd10() {
        validate(record("245", "10", "0", "a", "Title"), "marc.editor.invalid.ind.length - 10 - 245");
        validate(record("245", "1", "10", "a", "Title"), "marc.editor.invalid.ind.length - 10 - 245");
    }

    @Test
    public void validateIndLetter() {
        validate(record("245", "A", "0", "a", "Title"), "marc.editor.invalid.ind.length - A - 245");
        validate(record("245", "1", "A", "a", "Title"), "marc.editor.invalid.ind.length - A - 245");
    }

    @Test
    public void validateIndPlus1() {
        validate(record("245", "+1", "0", "a", "Title"), "marc.editor.invalid.ind.length - +1 - 245");
        validate(record("245", "1", "+1", "a", "Title"), "marc.editor.invalid.ind.length - +1 - 245");
    }
}
