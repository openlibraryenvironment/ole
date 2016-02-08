package org.kuali.ole.module.purap.document.service.impl;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.document.service.PurchaseOrderService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Callable;

/**
 * Created by gopalp on 1/28/16.
 */
public class OLEAutoClosePOExecutor implements Callable {

    private PlatformTransactionManager transactionManager;
    private PurchaseOrderService purchaseOrderService;
    private String documentNumber;
    private UserSession userSession;

    public OLEAutoClosePOExecutor(String documentNumber, UserSession userSession) {
        this.documentNumber = documentNumber;
        this.userSession = userSession;
    }


    @Override
    public Object call() throws Exception {
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());

        try {
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    GlobalVariables.setUserSession(userSession);
                    String newStatus = PurapConstants.PurchaseOrderStatuses.APPDOC_PENDING_CLOSE;
                    String annotation = "This PO was automatically closed in batch.";
                    String documentType = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_CLOSE_DOCUMENT;
                    PurchaseOrderDocument purchaseOrderDocument = getPurchaseOrderService().createAndRoutePotentialChangeDocument(documentNumber, documentType, annotation, null, newStatus);
                    return purchaseOrderDocument;

                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.transactionManager = null;
        }
        return null;
    }


    public PurchaseOrderService getPurchaseOrderService() {
        if(null == purchaseOrderService) {
            purchaseOrderService = SpringContext.getBean(PurchaseOrderService.class);
        }
        return purchaseOrderService;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}
