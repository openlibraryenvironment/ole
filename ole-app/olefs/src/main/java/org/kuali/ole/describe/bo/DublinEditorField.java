package org.kuali.ole.describe.bo;

import java.io.Serializable;

/**
 * DublinEditorField is business object class for Dublin Editor
 */
public class DublinEditorField implements Serializable {

    private String element;
    private String value;

    /**
     * Gets the element attribute.
     *
     * @return Returns the element.
     */
    public String getElement() {
        return element;
    }

    /**
     * Sets the element attribute value.
     *
     * @param element The element to set.
     */
    public void setElement(String element) {
        this.element = element;
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
