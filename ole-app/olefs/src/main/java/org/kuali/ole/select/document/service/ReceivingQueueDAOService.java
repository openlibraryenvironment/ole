package org.kuali.ole.select.document.service;

import org.kuali.ole.select.document.OlePurchaseOrderDocument;

import java.util.List;
import java.util.Map;

/**
 * Created by premkb on 9/7/15.
 */
public interface ReceivingQueueDAOService {

    public List<Map<String, Object>> getPODetails(Map<String, Object> criteria);

    public List<Map<String, Object>> getVendorPODetails(Map<String, Object> criteria);

    public List<OlePurchaseOrderDocument> getPODocumentList(Map<String, Object> criteria);

    public List<OlePurchaseOrderDocument> getPOVendorDocumentList(Map<String, Object> criteria);

}
