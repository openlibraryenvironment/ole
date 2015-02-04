package org.kuali.ole.pojo.bib;

import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.LeaderTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/2/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibliographicRecord {
    private LeaderTag leader = new LeaderTag();
    private List<ControlField> controlfields = new ArrayList<ControlField>();
    private List<DataField> datafields = new ArrayList<DataField>();

    public String getLeader() {
        return leader.getValue();
    }

    public void setLeader(String leader) {
        this.leader.setValue(leader);
    }

    public List<ControlField> getControlfields() {
        return controlfields;
    }

    public List<DataField> getDatafields() {
        return datafields;
    }

    public void setDatafields(List<DataField> datafields) {
        this.datafields = datafields;
    }

    public void addDataField(DataField dataField) {
        if (null != datafields && !this.datafields.contains(dataField)) {
            this.datafields.add(dataField);
        }
    }

    public void setControlfields(List<ControlField> controlfields) {
        this.controlfields = controlfields;
    }

    public String getRecordId() {
        for (ControlField cf : controlfields) {
            if (cf.getTag().equals("001")) {
                return cf.getValue();
            }
        }
        return "";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        BibliographicRecord that = (BibliographicRecord) object;

        if (leader != null ? !leader.equals(that.leader) : that.leader != null) return false;
        if (controlfields != null ? !controlfields.equals(that.controlfields) : that.controlfields != null)
            return false;
        if (datafields != null ? !datafields.equals(that.datafields) : that.datafields != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = leader != null ? leader.hashCode() : 0;
        result = 31 * result + (controlfields != null ? controlfields.hashCode() : 0);
        result = 31 * result + (datafields != null ? datafields.hashCode() : 0);
        return result;
    }
}
