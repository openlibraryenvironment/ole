package org.kuali.ole.docstore.common.document.content.bib.marc;

import java.io.Serializable;

/**
 */
public class ControlField implements Serializable {
    private String tag;
    private String value;


    public ControlField() {
    }

    public ControlField(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ControlField that = (ControlField) object;

        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
