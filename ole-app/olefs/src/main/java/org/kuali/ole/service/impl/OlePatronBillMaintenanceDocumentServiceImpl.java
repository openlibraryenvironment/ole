package org.kuali.ole.service.impl;

import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronBillMaintenanceDocumentServiceImpl extends MaintenanceDocumentServiceImpl {

    private DocumentService documentService;
    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    /**
     * Gets the value of documentService which is of type DocumentService
     * @return documentService(DocumentService)
     */
    protected DocumentService getDocumentService() {
        return this.documentService;
    }
    /**
     * Sets the value for documentService which is of type DocumentService
     * @param documentService(DocumentService)
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }


    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters) {
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);

      document.getOldMaintainableObject().setDataObject(oldDataObject);
          PatronBillPayment patronBillPayment=(PatronBillPayment)document.getOldMaintainableObject().getDataObject();
        List<FeeType> feeTypes=patronBillPayment.getFeeType();
        for(FeeType feeType:feeTypes){
            if (feeType.getItemUuid() != null) {
                try{
                org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(feeType.getItemUuid());
                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
                if (feeType.getItemUuid().equals(item.getId())) {
                    feeType.setItemTitle(item.getHolding().getBib().getTitle());
                    feeType.setItemType(itemContent.getItemType().getCodeValue());
                }
            } catch(Exception e)
            {
                e.printStackTrace();
            }
            }
        }
        patronBillPayment.setFeeType(feeTypes);
        document.getOldMaintainableObject().setDataObject(patronBillPayment);
        Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);
        document.getNewMaintainableObject().setDataObject(newDataObject);
    }


}
