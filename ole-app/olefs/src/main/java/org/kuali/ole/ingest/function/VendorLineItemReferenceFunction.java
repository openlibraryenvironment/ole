package org.kuali.ole.ingest.function;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.service.impl.OverlayMatchingServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.framework.engine.Function;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/6/13
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class VendorLineItemReferenceFunction implements Function {


    @Override
    public Object invoke(Object... arguments) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String existingDocstoreField = (String)(arguments[0]);
        String vendorLineItemReferenceNumber = (String)(arguments[1]);
        if(vendorLineItemReferenceNumber != null){
            try {
                List list = new OverlayMatchingServiceImpl().getInstanceCollectionOnVendorLineItemIdentifierMatch(vendorLineItemReferenceNumber);
                if(list.size() >= 1){
                    for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                        Map map = (Map) iterator.next();
                        if (map.containsKey(OLEConstants.BIB_UNIQUE_ID)) {
                            map.remove(OLEConstants.BIB_UNIQUE_ID);
                        }
                        List<String> bibIdList = (List<String>) map.get(OLEConstants.BIB_IDENTIFIER);
                        map.put(OLEConstants.BIB_UNIQUE_ID,bibIdList.get(0));
                    }
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
