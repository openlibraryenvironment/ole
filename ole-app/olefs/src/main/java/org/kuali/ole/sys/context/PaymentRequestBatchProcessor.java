package org.kuali.ole.sys.context;


import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.actionlist.ActionListService;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kew.api.document.DocumentRefreshQueue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Suresh.subbu on 6/4/2015.
 */
public class PaymentRequestBatchProcessor extends AbstractStep {
    private WorkflowDocumentActionsService workflowDocumentActionsService;
    private ActionListService actionListService;
    private ParameterService parameterService;

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {

        Map<String, String> payemntRequestMap = new HashMap<String, String>();
        org.kuali.rice.kew.api.doctype.DocumentType documentType = SpringContext.getBean(DocumentTypeService.class).getDocumentTypeByName("OLE_PREQ");
        payemntRequestMap.put("docRouteStatus", OLEConstants.DocumentStatusCodes.ENROUTE);
        payemntRequestMap.put("documentTypeId", documentType.getId());

        List<DocumentRouteHeaderValue> olePaymentRequestDocumentList = (List<DocumentRouteHeaderValue>) KRADServiceLocator.getBusinessObjectService().findMatching(DocumentRouteHeaderValue.class, payemntRequestMap);

        for (DocumentRouteHeaderValue olePaymentRequestDocument : olePaymentRequestDocumentList) {

            DocumentRefreshQueue docRequeue = KewApiServiceLocator.getDocumentRequeuerService(
                    olePaymentRequestDocument.getDocumentType().getApplicationId(), olePaymentRequestDocument.getDocumentId(), 0);
            docRequeue.refreshDocument(olePaymentRequestDocument.getDocumentId());

        }
        return true;
    }
}