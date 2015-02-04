package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/20/12
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainIndexMapName {

    private String set;
    private String Value;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return "Explain IndexMapName{" +
                "set='" + set + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}
