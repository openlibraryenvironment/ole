package org.kuali.ole.select.form;

import org.kuali.ole.select.document.OLEPurchaseOrderBatchDocument;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

/**
 * Created with IntelliJ IDEA.
 * User: gopalp
 * Date: 5/15/15
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderBatchForm extends TransactionalDocumentFormBase {

    private OLEPurchaseOrderBatchDocument olePurcharseOrderBatchDocument;

    public OLEPurchaseOrderBatchDocument getOlePurcharseOrderBatchDocument() {
        return olePurcharseOrderBatchDocument;
    }

    public void setOlePurcharseOrderBatchDocument(OLEPurchaseOrderBatchDocument olePurcharseOrderBatchDocument) {
        this.olePurcharseOrderBatchDocument = olePurcharseOrderBatchDocument;
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_POBA";
    }
}
