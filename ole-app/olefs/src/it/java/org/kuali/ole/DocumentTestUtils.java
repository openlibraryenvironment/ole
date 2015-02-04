package org.kuali.ole;

import org.kuali.ole.fp.businessobject.InternalBillingItem;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentService;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/2/13
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentTestUtils {
    /**
     * @param quantity
     * @param stockDescription
     * @param stockNumber
     * @param unitAmount
     * @param unitOfMeasureCode
     * @return new InternalBillingItem initialized with the given values
     */
    public static InternalBillingItem createBillingItem(Integer quantity, String stockDescription, String stockNumber, Double unitAmount, String unitOfMeasureCode) {
        InternalBillingItem item = new InternalBillingItem();

        item.setItemQuantity(quantity);
        // item.setItemServiceDate( timestamp );
        item.setItemStockDescription(stockDescription);
        item.setItemStockNumber(stockNumber);
        item.setItemUnitAmount(new KualiDecimal(unitAmount.toString()));
        item.setUnitOfMeasureCode(unitOfMeasureCode);

        return item;
    }

    public static <D extends Document> D createDocument(DocumentService documentService, Class<D> docmentClass) throws WorkflowException {
        D document = (D) documentService.getNewDocument(docmentClass);
        document.getDocumentHeader().setExplanation("unit test created document");

        DocumentHeader documentHeader = document.getDocumentHeader();
        documentHeader.setDocumentDescription("unit test created document");

        return document;
    }
}
