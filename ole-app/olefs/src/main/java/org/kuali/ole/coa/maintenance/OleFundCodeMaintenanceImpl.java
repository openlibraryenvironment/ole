package org.kuali.ole.coa.maintenance;

import org.kuali.ole.coa.businessobject.OleFundCode;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.util.Map;

/**
 * Created by angelind on 8/3/15.
 */
public class OleFundCodeMaintenanceImpl extends MaintainableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleFundCodeMaintenanceImpl.class);

    @Override
    public void processAfterNew(MaintenanceDocument document,
                                Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterNew()");
        super.processAfterNew(document,requestParameters);
        if(document != null && document.getNewMaintainableObject() != null) {
            OleFundCode oleFundCode = (OleFundCode) document.getNewMaintainableObject().getDataObject();
            if(oleFundCode != null) {
                oleFundCode.setActive(true);
            }
        }
    }
}
