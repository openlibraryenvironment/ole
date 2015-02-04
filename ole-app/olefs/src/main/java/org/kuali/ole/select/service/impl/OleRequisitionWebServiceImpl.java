package org.kuali.ole.select.service.impl;

import org.kuali.ole.module.purap.PurapConstants.LicenseRequestStatus;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.kuali.ole.select.service.OleRequisitionWebService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.action.ActionRequestType;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//import org.kuali.rice.kew.routeheader.service.WorkflowDocumentService;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/9/12
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRequisitionWebServiceImpl implements OleRequisitionWebService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleRequisitionWebServiceImpl.class);
    private DocumentService documentService;
    public List<OleRequisitionItem> requisitionItems = new ArrayList<OleRequisitionItem>();

    @Override
    public void updateRequisitionStatus(String documentNumber, String reqStatus) {
        try {
            //  OleRequisitionDocument requisitionDocument = (OleRequisitionDocument)
            //  SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(documentNumber);
            // String status = PurapConstants.RequisitionStatuses.DAPRVD_LICENSE;
            //requisitionDocument.setLicensingRequirementCode(reqStatus);
            // requisitionDocument.updateStatusAndSave(status);
        /*  //  @SuppressWarnings("restriction")
          //  String currentNodeName = getCurrentRouteNodeName(doc.getDocumentHeader().getWorkflowDocument());
         //   System.out.println("currentNodeName >>>>>>>>>" + currentNodeName);
           // String currentUser = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
           // System.out.println("11111111111 >>>>>>>>>" + GlobalVariables.getUserSession().getPrincipalName());
         //   System.out.println("@@@@@@@@@@@@@@@@@@ >>>>>>>>>" + GlobalVariables.getUserSession().getPrincipalName());
            //GlobalVariables.getUserSession().clearBackdoorUser();
          //  System.out.println("22222222222222 >>>>>>>>>" + GlobalVariables.getUserSession().getWorkflowDocument(doc.getDocumentHeader().getDocumentNumber()));
          //  System.out.println("get user name  >>>>>>>>>" +KIMServiceLocator.getPersonService().getPerson(doc.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId()).getPrincipalName());
           // GlobalVariables.getUserSession().setBackdoorUser((KIMServiceLocator.getPersonService().getPerson(doc.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId()).getPrincipalName()));

            //GlobalVariables.setUserSession(new UserSession(KIMServiceLocator.getPersonService().getPerson(doc.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId()).getPrincipalName()));
            System.out.println("333333333 >>>>>>>>>" + doc.getDocumentHeader().getWorkflowDocument().isApprovalRequested());
            System.out.println("@@@@@@@@@@@@@@ >>>>>>>>>" + doc.getDocumentHeader().getWorkflowDocument().isCompletionRequested());
            System.out.println("@@@@@@@@@@@@@@ >>>>>>>>>" + doc.getDocumentHeader().getWorkflowDocument().isBlanketApproveCapable());
       //     System.out.println("444444444444444 >>>>>>>>>" + GlobalVariables.getUserSession().getPrincipalName());

           // KNSServiceLocator.getDocumentService().approveDocument(doc, GlobalVariables.getUserSession().getPerson().getName() + "License Request Approved" , null);


           // OleRequisitionDocument requisitionDocument = (OleRequisitionDocument) KNSServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleRequisitionDocument.class,requisitionDocumentId);
            doc.setDocumentHeader(KNSServiceLocator.getDocumentHeaderService().getDocumentHeaderById(documentNumber));
            Person principalPerson = KIMServiceLocator.getPersonService().getPerson(doc.getDocumentHeader().getWorkflowDocument().getRouteHeader().getInitiatorPrincipalId());

            System.out.println("User is initiator >>>>>>>>>>>>>" + doc.getDocumentHeader().getWorkflowDocument().userIsInitiator(principalPerson));
            //         KIMServiceLocator.getPersonService().getPerson(GlobalVariables.getUserSession().getPerson().getPrincipalId());
            try{
                doc.getDocumentHeader().setWorkflowDocument(KNSServiceLocator.getWorkflowDocumentService().createWorkflowDocument(new Long(documentNumber), principalPerson));
                KNSServiceLocator.getDocumentService().approveDocument(doc, principalPerson.getName() + OLEConstants.OrderQueue.APPROVE_ANNOTATION + "KR-WKFLW Initiator" , null);
             //   return true;
            }catch(WorkflowException wfe) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_ORDERQUEUE_REQUISITIONS_APPROVE_WFE, new String[]{documentNumber,wfe.getMessage()});
               // return false;
            }


            //   SpringContext.getBean(DocumentService.class).approveDocument(doc, "Test",null);

            doc.getDocumentHeader().getWorkflowDocument().setClearFutureRequests();
            // GlobalVariables.getUserSession().clearBackdoorUser();
            //DocumentRouteStatusChangeDTO documentRouteStatusChangeDTO = new DocumentRouteStatusChangeDTO();
           // documentRouteStatusChangeDTO.setNewRouteStatus("CLOSED");
            //CLOSED
           // GlobalVariables.setUserSession(new UserSession(currentUser));


        */
            //    OleRequisitionDocument requisitionDocument = (OleRequisitionDocument)
            //          SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(documentNumber);

            //  requisitionDocument.setLicensingRequirementCode(reqStatus);
            // requisitionDocument.setStatusCode(PurapConstants.RequisitionStatuses.DAPRVD_LICENSE);
            //   requisitionDocument.updateStatusAndSave(PurapConstants.RequisitionStatuses.DAPRVD_LICENSE);
            // reqStatus="LRC";

            ActionListService actionListSrv = KEWServiceLocator.getActionListService();
            ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
            OleRequisitionDocument requisitionDocument = (OleRequisitionDocument)
                    SpringContext.getBean(DocumentService.class).getByDocumentHeaderId(documentNumber);
            Person person = KimApiServiceLocator.getPersonService().getPerson(requisitionDocument.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId());

            try {
                WorkflowDocument workflowDocument = KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(requisitionDocument.getDocumentNumber(), person);
                if (DocumentStatus.ENROUTE.equals(workflowDocument.getStatus())) {
                    List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(workflowDocument.getDocumentId());
                    for (ActionRequestValue actionRequest : actionReqValues) {
                        if (ActionRequestType.APPROVE.getCode().equals(actionRequest.getActionRequested())) {
                            Timestamp currentTime = SpringContext.getBean(DateTimeService.class).getCurrentTimestamp();
                            List<ActionItem> actionItems = actionRequest.getActionItems();
                            for (ActionItem actionItem : actionItems) {
                                if (ActionRequestType.APPROVE.getLabel().equals(actionItem.getActionRequestLabel())) {
                                    actionItem.setPrincipalId(person.getPrincipalId());
                                    actionItem.setDateAssigned(currentTime);
                                    actionListSrv.saveActionItem(actionItem);
                                }
                            }
                            actionRequest.setPrincipalId(person.getPrincipalId());
                            actionRequest.setCreateDate(currentTime);
                            actionReqSrv.saveActionRequest(actionRequest);
                            if (reqStatus.equals(LicenseRequestStatus.LICENSE_REQUEST_COMPLETE)) {
                                boolean documentApproved = approveDocument(requisitionDocument, requisitionDocument.getDocumentNumber(), actionRequest.getAnnotation(), person, reqStatus);
                            } else if (reqStatus.equals(LicenseRequestStatus.NEGOTIATION_FAILED)) {
                                boolean documentCanceled = cancelDocument(requisitionDocument, requisitionDocument.getDocumentNumber(), actionRequest.getAnnotation(), person, reqStatus);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception while updateRequisitionStatus"+e.getMessage());
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            LOG.error("Exception while updateRequisitionStatus"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean approveDocument(OleRequisitionDocument requisitionDocument, String documentNumber, String annotation, Person principalPerson, String reqStatus) {
        LOG.debug("Inside approveDocument of OleOrderQueueDocument");
        requisitionDocument.setDocumentHeader(KRADServiceLocatorWeb.getDocumentHeaderService().getDocumentHeaderById(documentNumber));
        /*requisitionDocument.setLicensingRequirementCode(PurapConstants.LicenseRequestStatus.LICENSE_REQUEST_COMPLETE);*/
        try {
            requisitionDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(documentNumber, principalPerson));
            KRADServiceLocatorWeb.getDocumentService().approveDocument(requisitionDocument, principalPerson.getName() + OLEConstants.LicenseRequest.APPROVE_ANNOTATION + annotation, null);
            return true;
        } catch (WorkflowException wfe) {
            GlobalVariables.getMessageMap().putError(OLEConstants.LicenseRequest.REQUISITIONS, OLEKeyConstants.ERROR_REQUISITION_APPROVE_WFE, new String[]{documentNumber, wfe.getMessage()});
            return false;
        }
    }

    private boolean cancelDocument(OleRequisitionDocument requisitionDocument, String documentNumber, String annotation, Person principalPerson, String reqStatus) {
        LOG.debug("Inside cancelDocument of OleOrderQueueDocument");
        requisitionDocument.setDocumentHeader(KRADServiceLocatorWeb.getDocumentHeaderService().getDocumentHeaderById(documentNumber));
        /*requisitionDocument.setLicensingRequirementCode(PurapConstants.LicenseRequestStatus.NEGOTIATION_FAILED);*/
        try {
            requisitionDocument.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().loadWorkflowDocument(documentNumber, principalPerson));
            KRADServiceLocatorWeb.getDocumentService().cancelDocument(requisitionDocument, principalPerson.getName() + OLEConstants.LicenseRequest.CANCEL_ANNOTATION + annotation);
            return true;
        } catch (WorkflowException wfe) {
            GlobalVariables.getMessageMap().putError(OLEConstants.LicenseRequest.REQUISITIONS, OLEKeyConstants.ERROR_REQUISITION_CANCELED_WFE, new String[]{documentNumber, wfe.getMessage()});
            return false;
        }
    }


    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    protected String getCurrentRouteNodeName(WorkflowDocument wd) throws WorkflowException {
        Set<String> nodeNames = wd.getCurrentNodeNames();
        if ((nodeNames == null) || (nodeNames.isEmpty())) {
            return null;
        } else {
            return nodeNames.iterator().next();
        }
    }

}
