package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainDatabaseTitle {

    private String lang;
    private String primary;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return "Explain DatabaseTitle{" +
                "lang='" + lang + '\'' +
                ", primary='" + primary + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
