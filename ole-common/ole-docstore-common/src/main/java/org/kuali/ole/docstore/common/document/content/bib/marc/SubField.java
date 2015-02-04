package org.kuali.ole.docstore.common.document.content.bib.marc;

import java.io.Serializable;

/**
 */
public class SubField implements Serializable {

    private String code;
    private String value;

    public SubField() {
    }

    public SubField(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SubField that = (SubField) object;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
