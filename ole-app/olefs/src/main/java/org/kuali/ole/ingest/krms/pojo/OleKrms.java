package org.kuali.ole.ingest.krms.pojo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrms {
    private List<OleKrmsAgenda> agendas;

    public List<OleKrmsAgenda> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<OleKrmsAgenda> agendas) {
        this.agendas = agendas;
    }
}
