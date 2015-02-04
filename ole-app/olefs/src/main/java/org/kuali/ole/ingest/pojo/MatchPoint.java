package org.kuali.ole.ingest.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

/**
 * MatchPoint is a business object class for Match Point
 */
public class MatchPoint {
    private String field;
    private String subfield;
    /**
     * Gets the field attribute.
     * @return  Returns the field.
     */
    public String getField() {
        return field;
    }
    /**
     * Sets the id field attribute value.
     * @param field  The field to set.
     */
    public void setField(String field) {
        this.field = field;
    }
    /**
     * Gets the subfield attribute.
     * @return  Returns the subfield.
     */
    public String getSubfield() {
        return subfield;
    }
    /**
     * Sets the subfield attribute value.
     * @param subfield .The subfield to set.
     */
    public void setSubfield(String subfield) {
        this.subfield = subfield;
    }

}
