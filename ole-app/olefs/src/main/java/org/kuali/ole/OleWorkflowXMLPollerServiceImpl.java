package org.kuali.ole;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;

/**
 * Created by pvsubrah on 12/5/13.
 */
public class OleWorkflowXMLPollerServiceImpl extends OleXmlPollerServiceImpl {
    @Override
    protected XmlIngesterService getIngesterService() {
        return CoreApiServiceLocator.getXmlIngesterService();
    }
}
