package org.kuali.ole.pojo.bib;

import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 12/28/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibliographicNamedFieldsBean implements Serializable {
    private String title;
    private String author;
    private String publisher;
    private String description;
    private BibliographicRecord bibliographicRecord;

    public String getTitle() {
        return getFieldNameFor("245", "a");
    }

    public void setTitle(String title) {
        setFieldValue(title, "245", "a");
    }

    public String getAuthor() {
        return getFieldNameFor("100", "a");
    }

    public void setAuthor(String author) {
        setFieldValue(author, "100", "a");
    }

    public String getPublisher() {
        return getFieldNameFor("260", "b");
    }

    public void setPublisher(String publisher) {
        setFieldValue(publisher, "260", "b");
    }

    public String getDescription() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getTitle() == "" ? "" : getTitle())
                .append(",")
                .append(getAuthor() == "" ? "" : getAuthor())
                .append(",")
                .append(getUniqueNumber() == "" ? "" : getUniqueNumber());
        String str = stringBuffer.toString();
        return
                str.charAt(str.length() - 1) == ',' ? str.substring(0, str.length() - 1) : str;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BibliographicRecord getBibliographicRecord() {
        if (null == bibliographicRecord) {
            bibliographicRecord = new BibliographicRecord();
        }
        return bibliographicRecord;
    }

    public void setBibliographicRecord(BibliographicRecord bibliographicRecord) {
        this.bibliographicRecord = bibliographicRecord;
    }

    public String getFieldNameFor(String dfTag, String sfCode) {
        List<DataField> datafields = getBibliographicRecord().getDatafields();
        if (null != datafields) {
            for (Iterator<DataField> iterator = datafields.iterator(); iterator.hasNext(); ) {
                DataField marcDataField = iterator.next();
                if (marcDataField.getTag().equals(dfTag)) {
                    List<SubField> subfields = marcDataField.getSubFields();
                    for (Iterator<SubField> marcSubFieldIterator = subfields.iterator(); marcSubFieldIterator.hasNext(); ) {
                        SubField marcSubField = marcSubFieldIterator.next();
                        if (marcSubField.getCode().equals(sfCode)) {
                            return marcSubField.getValue();
                        }
                    }
                }
            }
        }
        return "";
    }

    private void setFieldValue(String title, String dfTag, String sfCode) {
        DataField marcDataField = null;
        List<DataField> datafields = getBibliographicRecord().getDatafields();
        boolean titleDataFieldExists = false;

        if (null != datafields) {
            for (Iterator<DataField> iterator = datafields.iterator(); iterator.hasNext(); ) {
                DataField marcDataField1 = iterator.next();
                if (marcDataField1.getTag().equals(dfTag)) {
                    titleDataFieldExists = true;
                    break;
                }
            }
        }

        if (!titleDataFieldExists) {
            marcDataField = new DataField();
            marcDataField.setTag(dfTag);
            SubField marcSubField = new SubField();
            marcSubField.setCode(sfCode);
            marcSubField.setValue(title);
            marcDataField.addSubField(marcSubField);
            marcDataField.setInd1("");
            marcDataField.setInd2("");

            getBibliographicRecord().addDataField(marcDataField);
        }
    }

    public String getUniqueNumber() {
        return getFieldNameFor("", "");
    }
}
