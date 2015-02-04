package org.kuali.ole.pojo;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/9/12
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileAttribute {
    private Integer id;
    private String agendaName;
    private String attributeName;
    private String attributeValue;
    private String isSystemValue;

    public String getSystemValue() {
        return isSystemValue;
    }

    public void setSystemValue(String systemValue) {
        isSystemValue = systemValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
