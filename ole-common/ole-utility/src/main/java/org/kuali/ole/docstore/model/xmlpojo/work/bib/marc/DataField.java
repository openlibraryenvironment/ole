package org.kuali.ole.docstore.model.xmlpojo.work.bib.marc;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 */
public class DataField implements Serializable {
    private String tag;
    private String ind1;
    private String ind2;
    private List<SubField> subFields = new ArrayList<SubField>();

    public DataField() {
    }

    public DataField(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<SubField> getSubFields() {
        return subFields;
    }

    public void setSubFields(List<SubField> subFields) {
        this.subFields = subFields;
    }

    public void addSubField(SubField subField) {

        //code commented out based on jira OLE-4496

        /*if (!this.subFields.contains(subField)) {
            this.subFields.add(subField);
        }*/

        this.subFields.add(subField);
    }

    public String getInd1() {
        return ind1;
    }

    public void setInd1(String ind1) {
        this.ind1 = ind1;
    }

    public String getInd2() {
        return ind2;
    }

    public void setInd2(String ind2) {
        this.ind2 = ind2;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        DataField that = (DataField) object;

        if (ind1 != null ? !ind1.equals(that.ind1) : that.ind1 != null) return false;
        if (ind2 != null ? !ind2.equals(that.ind2) : that.ind2 != null) return false;
        if (subFields != null ? !subFields.equals(that.subFields) : that.subFields != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (ind1 != null ? ind1.hashCode() : 0);
        result = 31 * result + (ind2 != null ? ind2.hashCode() : 0);
        result = 31 * result + (subFields != null ? subFields.hashCode() : 0);
        return result;
    }

    public void addAllSubFields(List<SubField> subFields) {
        this.subFields.addAll(subFields);
    }
}
