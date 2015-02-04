package org.kuali.ole.ingest.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 12/25/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleDataField extends PersistableBusinessObjectBase {
    private Integer id;
    private Integer overlayOptionId;
    private String agendaName;
    private String dataFieldTag;
    private String dataFieldInd1;
    private String dataFieldInd2;
    private String subFieldCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOverlayOptionId() {
        return overlayOptionId;
    }

    public void setOverlayOptionId(Integer overlayOptionId) {
        this.overlayOptionId = overlayOptionId;
    }

    public String getAgendaName() {
        return agendaName;
    }

    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }

    public String getDataFieldTag() {
        return dataFieldTag;
    }

    public void setDataFieldTag(String dataFieldTag) {
        this.dataFieldTag = dataFieldTag;
    }

    public String getDataFieldInd1() {
        return dataFieldInd1;
    }

    public void setDataFieldInd1(String dataFieldInd1) {
        this.dataFieldInd1 = dataFieldInd1;
    }

    public String getDataFieldInd2() {
        return dataFieldInd2;
    }

    public void setDataFieldInd2(String dataFieldInd2) {
        this.dataFieldInd2 = dataFieldInd2;
    }

    public String getSubFieldCode() {
        return subFieldCode;
    }

    public void setSubFieldCode(String subFieldCode) {
        this.subFieldCode = subFieldCode;
    }
}
