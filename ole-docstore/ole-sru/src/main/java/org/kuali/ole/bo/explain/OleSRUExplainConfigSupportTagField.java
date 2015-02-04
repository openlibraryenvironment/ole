package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainConfigSupportTagField {

    private String type;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Explain ConfigSupportTagField{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
