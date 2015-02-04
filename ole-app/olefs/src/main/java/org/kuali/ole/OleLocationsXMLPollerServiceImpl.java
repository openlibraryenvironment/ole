package org.kuali.ole;

import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

/**
 * Created by pvsubrah on 12/5/13.
 */
public class OleLocationsXMLPollerServiceImpl extends OleXmlPollerServiceImpl {

    LocationsIngesterService locationsIngesterService;

    @Override
    protected XmlIngesterService getIngesterService() {
        return locationsIngesterService;
    }

    public void setLocationsIngesterService(LocationsIngesterService locationsIngesterService) {
        this.locationsIngesterService = locationsIngesterService;
    }
}
