package org.kuali.ole.sys.context;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.DocumentActionParameters;
import org.kuali.rice.kew.api.action.InvalidActionTakenException;
import org.kuali.rice.kew.api.action.WorkflowDocumentActionsService;
import org.kuali.rice.kew.api.actionlist.ActionListService;
import org.kuali.rice.kew.api.document.InvalidDocumentContentException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 10/28/14.
 */
public class ActionListBatchProcessor extends AbstractStep {
    private WorkflowDocumentActionsService workflowDocumentActionsService;
    private ActionListService actionListService;
    private ParameterService parameterService;

    @Override
    public boolean execute(String jobName, Date jobRunDate) throws InterruptedException {
        ParameterKey principalIdParameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.SYS_NMSPC, OLEConstants.BATCH_CMPNT, "ACTN_LIST_BATCH_PRNPL_NM");
        Parameter principalId = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(principalIdParameterKey);
        String principal = principalId.getValue();


        ParameterKey requestCodeParameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.SYS_NMSPC, OLEConstants.BATCH_CMPNT,"ACTN_LIST_BATCH_RQST_CD");
        Parameter requestCode = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(requestCodeParameterKey);
        String request = requestCode.getValue();

        ParameterKey docTypeParameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.SYS_NMSPC, OLEConstants.BATCH_CMPNT,"ACTN_LIST_BATCH_DOC_TYP_CD");
        Parameter docTypeCode = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(docTypeParameterKey);

        List<ActionItem> actionItems = getActionListService().getActionItemsForPrincipal(principal);
        List<String> docIds = new ArrayList<String>();
        List<ActionItem> filteredActionItems = new ArrayList<ActionItem>();

        if(docTypeCode.getValue().equals("*")){
            filteredActionItems = actionItems;
        } else {
            for (Iterator<ActionItem> iterator = actionItems.iterator(); iterator.hasNext(); ) {
                ActionItem actionItem = iterator.next();
                if(docTypeCode.getValue().equals(actionItem.getDocName())){
                    filteredActionItems.add(actionItem);
                }
            }
        }

        for (Iterator<ActionItem> iterator = filteredActionItems.iterator(); iterator.hasNext(); ) {
            ActionItem actionItem = iterator.next();
            if(actionItem.getActionRequestCd().equals(requestCode.getValue())){
                docIds.add(actionItem.getDocumentId());
            }
        }

        switch (request){
            case "A":
                approve(docIds, principal);
                break;
            case "K":
                acknowledge(docIds, principal);
                break;
            case "F":
                fyi(docIds, principal);
                break;
            case "C":
                complete(docIds, principal);
                break;
        }
        return true;
    }

    private void complete(List<String> docIds, String principal) {
        for (Iterator<String> iterator = docIds.iterator(); iterator.hasNext(); ) {
            String docId = iterator.next();
            try {
                getWorkflowDocumentActionsService().complete(DocumentActionParameters.create(docId, principal));
            } catch (RiceIllegalArgumentException | InvalidDocumentContentException | InvalidActionTakenException e) {
                System.out.println(docId);
                e.printStackTrace();
            }
        }
    }

    private void fyi(List<String> docIds, String principal) {
        for (Iterator<String> iterator = docIds.iterator(); iterator.hasNext(); ) {
            String docId = iterator.next();
            try {
                getWorkflowDocumentActionsService().clearFyi(DocumentActionParameters.create(docId, principal));
            } catch (RiceIllegalArgumentException | InvalidDocumentContentException | InvalidActionTakenException e) {
                System.out.println(docId);
                e.printStackTrace();
            }
        }
    }

    private void acknowledge(List<String> docIds, String principal) {
        for (Iterator<String> iterator = docIds.iterator(); iterator.hasNext(); ) {
            String docId = iterator.next();
            try {
                getWorkflowDocumentActionsService().acknowledge(DocumentActionParameters.create(docId, principal));
            } catch (RiceIllegalArgumentException | InvalidDocumentContentException | InvalidActionTakenException e) {
                System.out.println(docId);
                e.printStackTrace();
            }
        }
    }

    private void approve(List<String> docIds, String principal) {
        for (Iterator<String> iterator = docIds.iterator(); iterator.hasNext(); ) {
            String docId = iterator.next();
            try {
                getWorkflowDocumentActionsService().approve(DocumentActionParameters.create(docId, principal));
            } catch (RiceIllegalArgumentException | InvalidDocumentContentException | InvalidActionTakenException e) {
                e.printStackTrace();
            }
        }
    }

    private WorkflowDocumentActionsService getWorkflowDocumentActionsService() {
        if (null == workflowDocumentActionsService) {
            workflowDocumentActionsService = KewApiServiceLocator.getWorkflowDocumentActionsService();
        }
        return workflowDocumentActionsService;
    }

    public org.kuali.rice.kew.api.actionlist.ActionListService getActionListService() {
        if (null == actionListService) {
            actionListService = KewApiServiceLocator.getActionListService();
        }
        return actionListService;
    }

    @Override
    public ParameterService getParameterService() {
        if(null == parameterService){
            parameterService = (ParameterService) SpringContext.getService("parameterService");
        }
        return parameterService;
    }
}
