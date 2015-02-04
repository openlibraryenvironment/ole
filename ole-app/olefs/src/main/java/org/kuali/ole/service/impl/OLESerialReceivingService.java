package org.kuali.ole.service.impl;

import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.bo.OLESerialReceivingHistory;
import org.kuali.ole.select.bo.OLESerialReceivingType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/9/13
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OLESerialReceivingService {
    public void receiveRecord(OLESerialReceivingDocument oleSerialReceivingDocument, String receiptStatus);
    public List<OLESerialReceivingHistory> sortById(List<OLESerialReceivingHistory> oleSerialReceivingHistoryList);
    public void updateEnumValues(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void updateEnumCaptionValues(OLESerialReceivingDocument oleSerialReceivingDocument,OLESerialReceivingHistory oleSerialReceivingHistory);
    public void updatePOVendorDetail(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void validateVendorDetailsForSave(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void validateVendorDetailsForSelect(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void populateVendorAliasNameFromVendorName(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void populateVendorNameFromVendorId(String vendorId,OLESerialReceivingDocument oleSerialReceivingDocument);
    public void updateSerialIdInCopy(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void listOutHistoryBasedOnReceivingRecord(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void disapproveCreateNewWithExisting(OLESerialReceivingDocument newSerialReceivingDocument,OLESerialReceivingDocument oldSerialReceivingDocument);
    public void setEnumerationAndChronologyValues(OLESerialReceivingHistory oleSerialReceivingHistory);
    public String getParameter(String name);
    public void createOrUpdateReceivingRecordType(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void readReceivingRecordType(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void validateSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void populateEnumerationAndChronologyValues(OLESerialReceivingDocument oleSerialReceivingDocument, OLESerialReceivingType oleSerialReceivingType);
    public void createNewWithExisting(OLESerialReceivingDocument newSerialReceivingDocument,OLESerialReceivingDocument oldSerialReceivingDocument);
    public void validateClaim(OLESerialReceivingDocument oleSerialReceivingDocument);
    public void updateEnumerationAndChronologyValues(OLESerialReceivingDocument oleSerialReceivingDocument, OLESerialReceivingHistory oleSerialReceivingHistory);
    public void setEnumerationAndChronologyValues(OLESerialReceivingDocument oleSerialReceivingDocument, OLESerialReceivingHistory oleSerialReceivingHistory);
}
