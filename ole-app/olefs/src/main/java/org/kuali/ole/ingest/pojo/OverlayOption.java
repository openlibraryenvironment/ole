package org.kuali.ole.ingest.pojo;

//import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 12/22/12
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayOption extends PersistableBusinessObjectBase {

    private Integer id;
    private String agendaName;
    private String name;
    private List<DataField> dataFields;
    private List<OleDataField> oleDataFields;
    //private String agendaName;
/*    private String optionName;
    private String stringDataFields;*/

    /**
     * Gets the id attribute.
     * @return  Returns the id.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets the id attribute value.
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataField> getDataFields() {
        return dataFields;
    }

    public void setDataFields(List<DataField> dataFields) {
        this.dataFields = dataFields;
    }

/**
     * Gets the agendaName attribute.
     * @return  Returns the agendaName.
     */
/*    public String getAgendaName() {
        return agendaName;
    }*/
    /**
     * Sets the agendaName attribute value.
     * @param agendaName The agendaName to set.
     */
/*    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }*/
    /**
     * Gets the optionName attribute.
     * @return  Returns the optionName.
     */
/*    public String getOptionName() {
        return optionName;
    }*/
    /**
     * Sets the optionName attribute value.
     * @param optionName The optionName to set.
     */
/*    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }*/
    /**
     * Gets the stringDataFields attribute.
     * @return  Returns the stringDataFields.
     */
/*    public String getStringDataFields() {
        return stringDataFields;
    }*/

    public List<OleDataField> getOleDataFields() {
        return oleDataFields;
    }

    public void setOleDataFields(List<OleDataField> oleDataFields) {
        this.oleDataFields = oleDataFields;
    }

    /**
     * This method returns concatenated value of id,agendaName,optionName,stringDataFields
     * @return toStringMap
     */
    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("id", id);
        toStringMap.put("agendaName", agendaName);
        toStringMap.put("name", name);
        //toStringMap.put("stringDataFields", stringDataFields);*/
        return toStringMap;
    }
}
