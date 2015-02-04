package org.kuali.ole.service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 12/2/12
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OverlayMatchingService {

    public List getInstanceCollectionOnLocationMatch(String locationName) throws Exception;

    public List getInstanceCollectionOnItemBarcodenMatch(String itemBarcode) throws Exception;

    public List getInstanceCollectionOnVendorLineItemIdentifierMatch(String vendorLineItenIdentifier) throws Exception;

    public List getBibInfoUsingUUID(String id) throws Exception;
}
