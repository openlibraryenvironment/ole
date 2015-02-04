package org.kuali.ole;

import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

/**
 * Created by pvsubrah on 12/9/13.
 */
public class OlePatronsXMLPollerServiceImpl extends OleXmlPollerServiceImpl{
    private PatronsIngesterService patronsIngesterService;

    @Override
    protected XmlIngesterService getIngesterService() {
        return patronsIngesterService;
    }

    public void setPatronsIngesterService(PatronsIngesterService patronsIngesterService) {
        this.patronsIngesterService = patronsIngesterService;
    }

    public PatronsIngesterService getPatronsIngesterService() {
        return patronsIngesterService;
    }
}
