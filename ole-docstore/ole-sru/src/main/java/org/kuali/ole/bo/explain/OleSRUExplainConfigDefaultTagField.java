package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainConfigDefaultTagField {

    private String type;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
        return "Explain ConfigDefaultTagField{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
