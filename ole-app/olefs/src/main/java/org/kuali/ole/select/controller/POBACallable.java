package org.kuali.ole.select.controller;

import org.eclipse.jdt.internal.eval.GlobalVariable;
import org.kuali.ole.select.document.OLEPurchaseOrderBatchDocument;
import org.kuali.ole.select.document.service.impl.OLEPurchaseOrderBatchServiceImpl;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by pvsubrah on 6/15/15.
 */
public class POBACallable implements Callable<Object> {
    private final OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument;
    private final UserSession userSession;
    private final File file;

    public POBACallable(OLEPurchaseOrderBatchDocument olePurchaseOrderBatchDocument, UserSession session, File file) {
        this.olePurchaseOrderBatchDocument = olePurchaseOrderBatchDocument;
        this.userSession = session;
        this.file = file;
    }

    @Override
    public Object call() throws Exception {
        new OLEPurchaseOrderBatchServiceImpl().readFile(olePurchaseOrderBatchDocument, userSession, file);
        return null;
    }
}
