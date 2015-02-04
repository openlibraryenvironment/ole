package org.kuali.ole.bo.cql;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/10/12
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class CQLRelationTag {

    private String value;
    private List<CQLModifiers> modifiers;

    public List<CQLModifiers> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<CQLModifiers> modifiers) {
        this.modifiers = modifiers;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
