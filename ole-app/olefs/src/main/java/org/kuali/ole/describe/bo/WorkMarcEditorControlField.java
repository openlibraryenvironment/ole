package org.kuali.ole.describe.bo;

import java.io.Serializable;

/**
 * MarcEditorControlField is business object class for Marc Editor
 */
public class WorkMarcEditorControlField implements Serializable {
    private String tag;
    private String value;

    /**
     * Gets the tag attribute.
     *
     * @return Returns the tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the tag attribute value.
     *
     * @param tag The tag to set.
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the value attribute.
     *
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value attribute value.
     *
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
