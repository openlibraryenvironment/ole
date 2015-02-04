package org.kuali.ole.bo.explain;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/19/12
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUExplainSchema {

    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Explain Schema{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", identifier='" + identifier + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
