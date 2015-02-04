package org.kuali.ole.select.controller;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestBo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentAuthorizerBase;

import java.util.List;

/**
 * LicenseMaintenanceDocumentAuthorizerBase authorises permission for License Maintenance Document.
 */
public class LicenseMaintenanceDocumentAuthorizerBase extends MaintenanceDocumentAuthorizerBase {
    public DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);

    /**
     * Check user has credential to modify maintenanceDocument.
     *
     * @param document
     * @param user
     * @return boolean
     */
    @Override
    public boolean canEdit(Document document, Person user) {
        boolean canEdit = super.canEdit(document, user);
        String statusCode = document.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) document;
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) maintenanceDocument.getNewMaintainableObject().getDataObject();
        if (!canEdit) {
            canEdit &= oleLicenseRequestBo.getAssignee() != null &&
                    !(statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode()) || statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) ?
                    oleLicenseRequestBo.getAssignee().equalsIgnoreCase(user.getPrincipalId()) : true;
            String canEditStr = null;
            String routeHeaderId = document.getDocumentHeader().getDocumentNumber();
            if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
                List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId);
                for (ActionRequestValue actionRequest : actionReqValues) {
                    List<ActionItem> actionItems = actionRequest.getActionItems();
                    canEditStr = actionItems != null && actionItems.size() > 0 ? actionItems.get(0).getPrincipalId() : null;
                }
                return (oleLicenseRequestBo.getAssignee() != null && oleLicenseRequestBo.getAssignee().equalsIgnoreCase(user.getPrincipalId())) ||
                        (canEditStr != null && canEditStr.equalsIgnoreCase(user.getPrincipalId()));
            }
        }
        return canEdit;
    }

    /*public boolean canSave(Document document, Person user) {
        boolean canSave = super.canSave(document,user);
        String statusCode = document.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) document;
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo)maintenanceDocument.getNewMaintainableObject().getDataObject();
        String canSaveStr = null;
        String routeHeaderId = document.getDocumentHeader().getDocumentNumber();
        if(statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())){
            ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
            List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId);
            for(ActionRequestValue actionRequest:actionReqValues){
                List<ActionItem> actionItems = actionRequest.getActionItems();
                canSaveStr = actionItems!=null && actionItems.size()>0? actionItems.get(0).getPrincipalId():null;
            }
            return  (oleLicenseRequestBo.getAssignee()!=null && oleLicenseRequestBo.getAssignee().equalsIgnoreCase(user.getPrincipalId())) ||
                    (canSaveStr!=null && canSaveStr.equalsIgnoreCase(user.getPrincipalId()));
        }
        return canSave;
    }*/
    public boolean canRoute(Document document, Person user) {
        boolean canRoute = super.canRoute(document, user);
        String statusCode = document.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) document;
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) maintenanceDocument.getNewMaintainableObject().getDataObject();
        String canRouteStr = null;
        String routeHeaderId = document.getDocumentHeader().getDocumentNumber();
        if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
            ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
            List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId);
            for (ActionRequestValue actionRequest : actionReqValues) {
                List<ActionItem> actionItems = actionRequest.getActionItems();
                canRouteStr = actionItems != null && actionItems.size() > 0 ? actionItems.get(0).getPrincipalId() : null;
            }
            return (oleLicenseRequestBo.getAssignee() != null && oleLicenseRequestBo.getAssignee().equalsIgnoreCase(user.getPrincipalId())) ||
                    (canRouteStr != null && canRouteStr.equalsIgnoreCase(user.getPrincipalId()));
        }
        return canRoute;
    }
}
