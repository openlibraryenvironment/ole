package org.kuali.ole.oleng.service;

import org.kuali.ole.Exchange;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;

import java.util.List;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface OleNGRequisitionService {
    public OleRequisitionDocument createNewRequisitionDocument() throws Exception;
    public OleRequisitionDocument populateReqDocWithOrderInformation(OleRequisitionDocument oleRequisitionDocument,
                                                                     List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception ;
    public OleRequisitionDocument saveRequsitionDocument(OleRequisitionDocument oleRequisitionDocument);
    public OleRequisitionDocument routeRequisitionDocument(OleRequisitionDocument oleRequisitionDocument) throws Exception;
    public OleRequisitionDocument createPurchaseOrderDocument(List<OleOrderRecord> oleOrderRecords, Exchange exchange) throws Exception;
    public void setOleNGMemorizeService(OleNGMemorizeService oleNGMemorizeService);
}
