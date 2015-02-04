package org.kuali.ole.ingest.pojo;

/**
 * OleNameTypes is a business object class for Ole Name Types
 */
public class OleNameTypes {

    private String title;
    private String first;
    private String surname;
    private String suffix;
    /**
     * Gets the first attribute.
     * @return  Returns the first.
     */
    public String getFirst() {
        return first;
    }
    /**
     * Sets the first attribute value.
     * @param first The first to set.
     */
    public void setFirst(String first) {
        this.first = first;
    }
    /**
     * Gets the surname attribute.
     * @return  Returns the surname.
     */
    public String getSurname() {
        return surname;
    }
    /**
     * Sets the surname attribute value.
     * @param surname The surname to set.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    /**
     * Gets the title attribute.
     * @return  Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets the title attribute value.
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gets the suffix attribute.
     * @return  Returns the suffix.
     */
    public String getSuffix() {
        return suffix;
    }
    /**
     * Sets the suffix attribute value.
     * @param suffix The suffix to set.
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
