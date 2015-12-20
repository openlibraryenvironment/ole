package org.kuali.ole.oleng.service;

import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;

/**
 * Created by SheikS on 12/17/2015.
 */
public interface RequisitionService {
    public OleRequisitionDocument createPurchaseOrderDocument(OleOrderRecord oleOrderRecord) throws Exception;
}
