package org.kuali.ole.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.service.DiscoveryHelperService;
import org.kuali.ole.service.OverlayMatchingService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 12/2/12
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayMatchingServiceImpl implements OverlayMatchingService {

    private DiscoveryHelperService discoveryHelperService ;


    public List getInstanceCollectionOnLocationMatch(String locationName) throws Exception{
        List resultList =  getDiscoveryHelperService().getResponseFromSOLR(OLEConstants.OverlayMatchingServiceImpl.LOCATION_LEVEL_SEARCH, locationName);
        return resultList;
    }

    public List getInstanceCollectionOnItemBarcodenMatch(String itemBarcode) throws Exception{
        List resutlList = getDiscoveryHelperService().getResponseFromSOLR(OLEConstants.OverlayMatchingServiceImpl.ITEM_BARCODE_DISPLAY, itemBarcode);
        return resutlList;
    }

    @Override
    public List getInstanceCollectionOnVendorLineItemIdentifierMatch(String vendorLineItenIdentifier) throws Exception {
        List resutlList = getDiscoveryHelperService().getResponseFromSOLR(OLEConstants.OverlayMatchingServiceImpl.VENDOR_LINEITEM_IDENTIFIER,vendorLineItenIdentifier);
        return resutlList;
    }

    @Override
    public List getBibInfoUsingUUID(String id) throws Exception {
        List resutlList = getDiscoveryHelperService().getBibInformationFromBibId(id);
        return resutlList;
    }

    public DiscoveryHelperService getDiscoveryHelperService() {
        if (null == discoveryHelperService) {
            discoveryHelperService = new DiscoveryHelperService();
        }
        return discoveryHelperService;
    }
}
