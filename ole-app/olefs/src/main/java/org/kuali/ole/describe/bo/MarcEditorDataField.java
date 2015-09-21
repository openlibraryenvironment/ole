package org.kuali.ole.describe.bo;

import java.io.Serializable;

/**
 * MarcEditorDataField is business object class for Marc Editor
 */
public class MarcEditorDataField implements Serializable, Comparable<MarcEditorDataField>  {
    private String tag;
    private String ind1;
    private String ind2;
    private String value;
    private String value260a;
    private String value260b;
    private String value260c;
    private boolean selected;

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

    /**
     * Gets the ind1 attribute.
     *
     * @return Returns the ind1.
     */
    public String getInd1() {
        return ind1;
    }

    /**
     * Sets the ind1 attribute value.
     *
     * @param ind1 The ind1 to set.
     */
    public void setInd1(String ind1) {
        this.ind1 = ind1;
    }

    /**
     * Gets the ind2 attribute.
     *
     * @return Returns the ind2.
     */
    public String getInd2() {
        return ind2;
    }

    /**
     * Sets the ind2 attribute value.
     *
     * @param ind2 The ind2 to set.
     */
    public void setInd2(String ind2) {
        this.ind2 = ind2;
    }

    /**
     * Gets the selected attribute.
     *
     * @return Returns the selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected attribute value.
     *
     * @param selected The selected to set.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Gets the 260c attribute value.
     * @return
     */
    public String getValue260c() {
        return value260c;
    }

    /**
     * Sets the 260c attribute value.
     * @param value260c
     */
    public void setValue260c(String value260c) {
        this.value260c = value260c;
    }

    /**
     * Gets the 260b attribute value.
     * @return
     */
    public String getValue260b() {
        return value260b;
    }

    /**
     * Sets the 260b attribute value.
     * @param value260b
     */
    public void setValue260b(String value260b) {
        this.value260b = value260b;
    }

    /**
     * Gets the 260a attribute value.
     * @return
     */
    public String getValue260a() {
        return value260a;
    }

    /**
     * Sets the 260a attribute value.
     * @param value260a
     */
    public void setValue260a(String value260a) {
        this.value260a = value260a;
    }

    /**
     * Implementing equals,Indicates whether some other object is "equal to" this one.
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int compareTo(MarcEditorDataField o) {
        return this.getTag().compareTo(o.getTag());
    }
}
