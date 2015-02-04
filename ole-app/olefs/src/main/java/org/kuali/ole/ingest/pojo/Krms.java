package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * Krms is a business object class for Krms
 */
public class Krms {
    private List<OleAgenda> oleAgendaList;
    /**
     * Gets the oleAgendaList attribute.
     * @return  Returns the oleAgendaList.
     */
    public List<OleAgenda> getOleAgendaList() {
        return oleAgendaList;
    }
    /**
     * Sets the oleAgendaList attribute value.
     * @param oleAgendaList  The oleAgendaList to set.
     */
    public void setOleAgendaList(List<OleAgenda> oleAgendaList) {
        this.oleAgendaList = oleAgendaList;
    }
}
