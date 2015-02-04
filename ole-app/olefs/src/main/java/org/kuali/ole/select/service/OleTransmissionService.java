package org.kuali.ole.select.service;

import org.kuali.ole.module.purap.transmission.service.TransmissionService;
import org.kuali.ole.vnd.businessobject.VendorTransmissionFormatDetail;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 11/7/11
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleTransmissionService extends TransmissionService {
    public static String ORDER_RECORDS = "Order_Records";
    public static String ORDERS_TO_BE_PROCESSED_BY_VENDOR_FOLDER = "Orders_To_Be_Processed_By_Vendor";

    public void doSFTPUpload(VendorTransmissionFormatDetail vendorTransmissionFormatDetail, String fileLocation, String fileName);

    public void doFTPUpload(VendorTransmissionFormatDetail vendorTransmissionFormatDetail, String fileLocation, String fileName);
}
