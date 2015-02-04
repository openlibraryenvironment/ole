package org.kuali.ole.ingest.function;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.service.impl.OverlayMatchingServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/12/12
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationFunction implements Function {

    @Override
    public Object invoke(Object... arguments) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String existingDocstoreField = (String)(arguments[0]);
        String location = (String)(arguments[1]);
        if(location != null){
            try {
                List list = new OverlayMatchingServiceImpl().getInstanceCollectionOnLocationMatch(location);
                if(list.size() >= 1){
                    dataCarrierService.addData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE, list);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
