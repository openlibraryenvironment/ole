package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndexSet {

    private String name;
    private String identifier;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "Explain IndexSet{" +
                "name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
