package org.kuali.ole.select.controller;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.service.OleLicenseRequestService;
import org.kuali.ole.service.impl.OleLicenseRequestServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * OleLicenseRequestController is the controller class for License Request Maintenance Document.
 */
@Controller
@RequestMapping(value = "/oleLicenseRequest")
public class OleLicenseRequestController extends MaintenanceDocumentController {

    private OleLicenseRequestService oleLicenseRequestService;
    private final String RULE_EVALUATED = "Rule Evaluated";
    private final String ROUTED_EXTERNAL = "Routed External";
    protected static final String REDIRECT_PREFIX = "redirect:";

    private OleLicenseRequestService getOleLicenseRequestService() {
        if (oleLicenseRequestService == null) {
            oleLicenseRequestService = new OleLicenseRequestServiceImpl();
        }
        return oleLicenseRequestService;
    }

    /**
     * This method populates date of the eventlog object thereby adding to the existing list.
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addEventLogLine")
    public ModelAndView addEventLogLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(uifForm, addLinePath);
        OleEventLogBo oleEventLogBo = (OleEventLogBo) eventObject;
        oleEventLogBo.setEventType(OLEConstants.OleLicenseRequest.USER);
        oleEventLogBo.setCurrentTimeStamp();
        return addLine(uifForm, result, request, response);
    }

    /**
     * This method sets appropriate LicenseRequestStatusCode and gets the UUIDs for the agreement document from the docstore
     * (ingesting into the docstore) and proceeds with normal routing.
     *
     * @param form
     * @param result
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();
        OleLicenseRequestBo oldLicenseRequestBo = (OleLicenseRequestBo) document.getOldMaintainableObject().getDataObject();
        if (!validateFields(oleLicenseRequestBo)) {
            return getUIFModelAndView(form);
        }
        if (oldLicenseRequestBo != null && oleLicenseRequestBo.getLocationId() != null &&
                oleLicenseRequestBo.getLocationId() != OLEConstants.OleLicenseRequest.LICENSE_INITIAL_LOCATON &&
                !oleLicenseRequestBo.getLocationId().equals(oldLicenseRequestBo.getLocationId())) {
            updateEventLogForLocation(oleLicenseRequestBo, "location", null);
        }
        Map<String, String> licenses = getLicenseWorkFlows();
        if (licenses.containsKey(oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode())) {
            oleLicenseRequestBo.setLicenseRequestStatusCode(licenses.get(oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode()));
            document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                    getLicenseRequestName(licenses.get(oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode())));
            performWorkflowAction(form, UifConstants.WorkflowAction.SAVE, true);
            assignActionRequests(document.getDocumentHeader().getDocumentNumber(), oleLicenseRequestBo.getAssignee());
            return closeDocument(form, result, request, response);
        }
        oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.LICENSE_NEEDED);
        document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                getLicenseRequestName(OLEConstants.OleLicenseRequest.LICENSE_NEEDED));
        processAgreementDocument(oleLicenseRequestBo);
        String licenseRequestWorkflowTypeCode = oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode();
        if (licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.SIGNATORY_ONLY)) {
            oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_SIGNATURE);
            document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                    getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
        } else if (licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.REVIEW_ONLY)) {
            oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_REVIEW);
            document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                    getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
        } else if (licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.APPROVE_ONLY)) {
            oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_APPROVAL);
            document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                    getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
        } else if (licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.FULL_APPROVAL)
                || licenseRequestWorkflowTypeCode.equalsIgnoreCase(OLEConstants.OleLicenseRequest.ADDENDUM)) {
            oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_REVIEW);
            document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                    getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
        }

        boolean valid = false;
        EngineResults engineResult = executeEngineResults(oleLicenseRequestBo);
        List<ResultEvent> allResults = engineResult.getAllResults();
        for (Iterator<ResultEvent> resultEventIterator = allResults.iterator(); resultEventIterator.hasNext(); ) {
            ResultEvent resultEvent = resultEventIterator.next();
            if (resultEvent.getType().equals(RULE_EVALUATED)) {
                valid |= resultEvent.getResult();
            }
        }
        if (!valid) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.LICENSE_RULE_VAILDATIONS);
        }

        String currentUser = GlobalVariables.getUserSession().getPrincipalName();
        if (currentUser.equalsIgnoreCase(oleLicenseRequestBo.getAssignee()) && oleLicenseRequestBo.getEventLogs().size() < 1) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.LICENSE_EVENT_LOG_CHECK);
        }
        if (oleLicenseRequestBo.getLinkedAgreement() != null & oleLicenseRequestBo.getAgreementId() == null) {
            String agreementcontent = getOleLicenseRequestService().getAgreementContent(oleLicenseRequestBo.getLinkedAgreement());
            if (agreementcontent != null) {
                oleLicenseRequestBo.setAgreementId(getOleLicenseRequestService().ingestAgreementContent(agreementcontent));
                oleLicenseRequestBo.setLinkedAgreement("");
            } else {
                LOG.error("Error while retrieving the onixpl content");
            }
        }
        //modelAndView = super.route(form, result, request, response);

        return super.route(form, result, request, response);
    }

    /**
     * This method validates the required field of License Request Document
     *
     * @param oleLicenseRequestBo
     * @return isValid
     */
    private boolean validateFields(OleLicenseRequestBo oleLicenseRequestBo) {
        boolean isFieldsValid = true;
        LOG.debug("Inside ValidateFields method");
        if (oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode() == null ||
                oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode().isEmpty()) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                    OLEConstants.OleLicenseRequest.ERROR_REQUIRED, OLEConstants.OleLicenseRequest.INITIATE_LR_WORKFLOW);
            isFieldsValid &= false;
        }
        if (oleLicenseRequestBo.getAssignee() == null || oleLicenseRequestBo.getAssignee().isEmpty()) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                    OLEConstants.OleLicenseRequest.ERROR_REQUIRED, OLEConstants.OleLicenseRequest.DO_ASSIGNEE);
            isFieldsValid &= false;
        }
        if (oleLicenseRequestBo.getLicenseRequestTypeId() == null ||
                oleLicenseRequestBo.getLicenseRequestTypeId().isEmpty()) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.ERROR_REQUIRED,
                    OLEConstants.OleLicenseRequest.DO_LICENSE_REQUEST_TYPE);
            isFieldsValid &= false;
        }
        if (oleLicenseRequestBo.getAgreementMethodId() == null ||
                oleLicenseRequestBo.getAgreementMethodId().isEmpty()) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.ERROR_REQUIRED,
                    OLEConstants.OleLicenseRequest.DO_AGR_MTHD);
            isFieldsValid &= false;
        }
        return isFieldsValid;
    }

    /**
     * This method is to return the License Request Status
     */
    private String getLicenseRequestName(String licenseRequestCode) {
        String licenseRequestStatusName = "";
        Map criteria = new HashMap();
        criteria.put("code", licenseRequestCode);
        OleLicenseRequestStatus licenseRequestStatus = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleLicenseRequestStatus.class,
                criteria);
        licenseRequestStatusName = licenseRequestStatus.getName();
        return licenseRequestStatusName;
    }

    /**
     * This method assigns specified document to the selector.
     */
    private void assignActionRequests(String routeHeaderId, String selectedUserId) {
        LOG.debug("Inside assignActionRequests of LicenseRequestDocument");
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();
        ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
        List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId);
        for (ActionRequestValue actionRequest : actionReqValues) {
            List<ActionItem> actionItems = actionRequest.getActionItems();
            for (ActionItem actionItem : actionItems) {
                actionItem.setPrincipalId(selectedUserId);
                actionItem.setDateAssigned(currentTime);
                actionListSrv.saveActionItem(actionItem);
            }
            actionRequest.setPrincipalId(selectedUserId);
            actionRequest.setCreateDate(currentTime);
            //actionRequest.setAnnotation("KR-LIC Owner");
            actionReqSrv.saveActionRequest(actionRequest);
        }
        LOG.debug("Leaving assignActionRequests of LicenseRequestDocument");
    }


    /**
     * Performs the approve workflow action on the document
     *
     * @param form - document form base containing the document instance that will be approved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=approve")
    public ModelAndView approve(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm oldMaintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument oldDocument = (MaintenanceDocument) oldMaintenanceForm.getDocument();
        OleLicenseRequestBo newLicenseRequestBo = (OleLicenseRequestBo) oldDocument.getNewMaintainableObject().getDataObject();
        OleLicenseRequestBo oldLicenseRequestBo = (OleLicenseRequestBo) oldDocument.getOldMaintainableObject().getDataObject();
        if (!validateFields(newLicenseRequestBo)) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.ERROR_REQUIRED);
            return getUIFModelAndView(form);
        }
        if (oldLicenseRequestBo.getLocationId() != null && newLicenseRequestBo.getLocationId() != null &&
                !newLicenseRequestBo.getLocationId().equals(oldLicenseRequestBo.getLocationId())) {
            updateEventLogForLocation(newLicenseRequestBo, "location", null);
        }
        //Ingesting the Agreement Documents
        processAgreementDocument(newLicenseRequestBo);

        List<ActionRequest> oldActionRequests = oldDocument.getDocumentHeader().getWorkflowDocument().getDocumentDetail().getActionRequests();
        String oldRoleName = "";
        for (ActionRequest oldActionRequest : oldActionRequests) {
            if (oldActionRequest.getStatus().getCode().equalsIgnoreCase("A")) {
                oldRoleName = oldActionRequest.getQualifiedRoleNameLabel() != null ? oldActionRequest.getQualifiedRoleNameLabel() : "No Role";
            }
        }
        boolean validRule = false;
        EngineResults engineResult = executeEngineResults((OleLicenseRequestBo) oldDocument.getNewMaintainableObject().getDataObject());
        List<ResultEvent> allResults = engineResult.getAllResults();
        for (Iterator<ResultEvent> resultEventIterator = allResults.iterator(); resultEventIterator.hasNext(); ) {
            ResultEvent resultEvent = resultEventIterator.next();
            if (resultEvent.getType().equals(RULE_EVALUATED)) {
                validRule |= resultEvent.getResult();
            }
        }
        if (!validRule) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.LICENSE_RULE_VAILDATIONS);
        }
        String currentUser = GlobalVariables.getUserSession().getPrincipalName();
        boolean eventLogCheck = currentUser.equalsIgnoreCase(newLicenseRequestBo.getAssignee()) && newLicenseRequestBo.getEventLogs().size() < 1;
        if (eventLogCheck) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.LICENSE_EVENT_LOG_CHECK);
        }
        if (!validRule || eventLogCheck) {
            return getUIFModelAndView(form);
        }
        //form.setAnnotation(oldRoleName);
        performWorkflowAction(form, UifConstants.WorkflowAction.APPROVE, true);
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        List<ActionRequest> actionRequests = document.getDocumentHeader().getWorkflowDocument().getDocumentDetail().getActionRequests();
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();
        for (ActionRequest actionRequest : actionRequests) {
            String roleName = actionRequest != null && actionRequest.getQualifiedRoleNameLabel() != null ? actionRequest.getQualifiedRoleNameLabel() : "No Role";
            if (roleName.equalsIgnoreCase(oldRoleName)) {
                if (roleName.equalsIgnoreCase(OLEConstants.OleLicenseRequest.SIGNATORY_ROLE_NM) && actionRequest.getStatus().getCode().equalsIgnoreCase("D")) {
                    oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.SIGNATORY_COMPLETE);
                    document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                            getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    form.setAnnotation(OLEConstants.OleLicenseRequest.SIGNATORY_ROLE_NM);
                    getDocumentService().saveDocument(document);
                } else if (roleName.equalsIgnoreCase(OLEConstants.OleLicenseRequest.LICENSE_MNGR_ROLE_NM) && actionRequest.getStatus().getCode().equalsIgnoreCase("D")) {
                    if (newLicenseRequestBo.getLicenseRequestWorkflowTypeCode().equalsIgnoreCase(OLEConstants.OleLicenseRequest.SIGNATORY_ONLY)) {
                        oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_SIGNATURE);
                        document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                                getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    } else if (newLicenseRequestBo.getLicenseRequestWorkflowTypeCode().equalsIgnoreCase(OLEConstants.OleLicenseRequest.REVIEW_ONLY)) {
                        oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_REVIEW);
                        document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                                getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    } else if (newLicenseRequestBo.getLicenseRequestWorkflowTypeCode().equalsIgnoreCase(OLEConstants.OleLicenseRequest.APPROVE_ONLY)) {
                        oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.PENDING_APPROVAL);
                        document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                                getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    } else {
                        oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.UNIVERSITY_COMPLETE);
                        document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                                getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    }
                    form.setAnnotation(OLEConstants.OleLicenseRequest.LICENSE_MNGR_ROLE_NM);
                    getDocumentService().saveDocument(document);
                } else if (roleName.equalsIgnoreCase(OLEConstants.OleLicenseRequest.REVIEWER_ROLE_NM) && actionRequest.getStatus().getCode().equalsIgnoreCase("D")) {
                    oleLicenseRequestBo.setLicenseRequestStatusCode(
                            newLicenseRequestBo.getLicenseRequestWorkflowTypeCode().equalsIgnoreCase(OLEConstants.OleLicenseRequest.FULL_APPROVAL) ?
                                    OLEConstants.OleLicenseRequest.PENDING_UNIVERSITY : OLEConstants.OleLicenseRequest.REVIEW_COMPLETE);
                    document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                            getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    form.setAnnotation(OLEConstants.OleLicenseRequest.REVIEWER_ROLE_NM);
                    getDocumentService().saveDocument(document);
                } else if (roleName.equalsIgnoreCase(OLEConstants.OleLicenseRequest.APPROVER_ROLE_NM) && actionRequest.getStatus().getCode().equalsIgnoreCase("D")) {
                    oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.LICENSE_COMPLETE);
                    document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                            getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
                    form.setAnnotation(OLEConstants.OleLicenseRequest.APPROVER_ROLE_NM);
                    getDocumentService().saveDocument(document);
                    /*OleWebServiceProvider oleWebServiceProvider = (OleWebServiceProvider) GlobalResourceLoader.getService(OLEConstants.OLE_WEB_SERVICE_PROVIDER);

                    OleRequisitionWebService oleRequisitionWebService =
                            (OleRequisitionWebService) oleWebServiceProvider.
                                    getService("org.kuali.ole.service.OleRequisitionWebService", "oleRequisitionWebService", getURL());

                    oleRequisitionWebService.updateRequisitionStatus(oleLicenseRequestBo.getRequisitionDocNumber(),OLEConstants.OleLicenseRequest.LICENSE_COMPLETE_RETURN );*/
                }
            }
        }
        return returnToPrevious(form);
    }

    /**
     * Performs the disapprove workflow action on the document
     *
     * @param form - document form base containing the document instance that will be disapproved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=getDocument")
    public ModelAndView getDocument(@ModelAttribute("KualiForm")  DocumentFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws WorkflowException {

        loadDocument(form);
        return getUIFModelAndView(form);

    } @RequestMapping(params = "methodToCall=downloadDocument")
    public ModelAndView downloadDocument(@ModelAttribute("KualiForm")  DocumentFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws WorkflowException {
        loadDocument(form);
        Map<String,String> map=new HashMap<>();
        map.put("selectedLineIndex",request.getParameter("selectedLineIndex"));
        form.setActionParameters(map);
        return downloadAgreement(form,result,request,response);
    }
    @RequestMapping(params = "methodToCall=disapprove")
    public ModelAndView disapprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside disapprove method");
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();
        if (oleLicenseRequestBo.getEventLogs().size() < 1) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.LICENSE_DISAPPROVE_VALIDATIONS);
            return getUIFModelAndView(form);
        }
        oleLicenseRequestBo.setLicenseRequestStatusCode(OLEConstants.OleLicenseRequest.NEGOTIATION_FAILED);
        document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(
                getLicenseRequestName(oleLicenseRequestBo.getLicenseRequestStatusCode()));
        getDocumentService().saveDocument(document);
        performWorkflowAction(form, UifConstants.WorkflowAction.DISAPPROVE, true);

       /* OleWebServiceProvider oleWebServiceProvider = (OleWebServiceProvider) GlobalResourceLoader.getService(OLEConstants.OLE_WEB_SERVICE_PROVIDER);

        OleRequisitionWebService oleRequisitionWebService =
                (OleRequisitionWebService) oleWebServiceProvider.
                        getService("org.kuali.ole.service.OleRequisitionWebService", "oleRequisitionWebService", getURL());

        oleRequisitionWebService.updateRequisitionStatus(oleLicenseRequestBo.getRequisitionDocNumber(), OLEConstants.OleLicenseRequest.LICENSE_NEGOTIATION_FAILED);*/
        updateEventLogForLocation(oleLicenseRequestBo, "system", "License Request Disapproved");
        performWorkflowAction(form, UifConstants.WorkflowAction.SAVE, true);
        return returnToPrevious(form);
    }

    public String getURL() {
        return ConfigContext.getCurrentContextConfig().getProperty("oleRequisitionWebService.url");
    }

    /**
     * Execute the Engine and return the results
     *
     * @param oleLicenseRequestBo
     * @return EngineResults
     */
    private EngineResults executeEngineResults(OleLicenseRequestBo oleLicenseRequestBo) {
        Engine engine = KrmsApiServiceLocator.getEngine();
        EngineResults engineResult = null;
        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put("nm", OLEConstants.OleLicenseRequest.LICENSE_AGENDA_NM);
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        if (agendaBos != null && agendaBos.size() > 0) {
            AgendaBo agendaBo = agendaBos.get(0);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("AGENDA_NAME", agendaBo.getName());
            List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

            SelectionCriteria selectionCriteria =
                    SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()), getAgendaContext(OLEConstants.OleLicenseRequest.LICENSE_AGENDA_NM));

            ExecutionOptions executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

            Facts.Builder factBuilder = Facts.Builder.create();

            String licenseType = getLicenseType(oleLicenseRequestBo.getLicenseRequestTypeId());
            String workflowName = getWorkFlowName(oleLicenseRequestBo.getLicenseRequestWorkflowTypeCode());
            String agreementMethod = getAgreementMethod(oleLicenseRequestBo.getAgreementMethodId());
            HashMap<String, Object> termValues = new HashMap<String, Object>();

            termValues.put("licenseType", licenseType);
            termValues.put("agreementMethod", agreementMethod);
            termValues.put("workflowName", workflowName);
            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }
            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
        }
        return engineResult;
    }

    private String getAgreementMethod(String agreementMethodId) {
        OleAgreementMethod oleAgreementMethod = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleAgreementMethod.class, agreementMethodId);
        return oleAgreementMethod.getAgreementMethodName();
    }

    /**
     * This method returns selectionContext using contextName.
     *
     * @param contextName
     * @return Map
     */
    protected Map<String, String> getSelectionContext(String contextName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(OLEConstants.NAMESPACE_CODE_SELECTOR, "OLE");
        selector.put(OLEConstants.NAME_SELECTOR, contextName);
        return selector;
    }

    /**
     * This method returns agendaContext using agendaName.
     *
     * @param agendaName
     * @return Map
     */
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(OLEConstants.NAME_SELECTOR, agendaName);
        return selector;
    }

    /**
     * This method returns LicenseType using licenseRequestTypeId.
     *
     * @param licenseRequestTypeId
     * @return Map
     */
    private String getLicenseType(String licenseRequestTypeId) {
        OleLicenseRequestType oleLicenseRequestType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleLicenseRequestType.class, licenseRequestTypeId);
        return oleLicenseRequestType.getName();
    }

    /**
     * This method returns WorkFlowName using licenseRequestWorkflowTypeCode.
     *
     * @param licenseRequestWorkflowTypeCode
     * @return
     */
    private String getWorkFlowName(String licenseRequestWorkflowTypeCode) {
        OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OleLicenseRequestWorkflowType.class, licenseRequestWorkflowTypeCode);
        return oleLicenseRequestWorkflowType.getName();
    }

    /**
     * This method will add the agreement document to the existing list and also stores the attachment
     * to the specified path.
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=insertAgreementDocument")
    public ModelAndView insertAgreementDocument(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {

        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MultipartFile attachmentFile = form.getAttachmentFile();
        OleAgreementDocumentMetadata oleAgreementDocumentMetadata = null;
        if (attachmentFile!=null && attachmentFile.getOriginalFilename() != null && !attachmentFile.getOriginalFilename().isEmpty()) {
            try{
            String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
            CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(
                    selectedCollectionPath);
            String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
            Object eventObject = ObjectPropertyUtils.getPropertyValue(uifForm, addLinePath);
                oleAgreementDocumentMetadata = (OleAgreementDocumentMetadata) eventObject;
            oleAgreementDocumentMetadata.setCurrentTimeStamp();
            String userName = GlobalVariables.getUserSession().getPrincipalName();
            oleAgreementDocumentMetadata.setUploadedBy(userName);
            }catch(Exception e){
                LOG.info("Exception occured while processing the document " + e.getMessage());
                LOG.error(e,e);
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                        OLEConstants.UPLOAD_FILE_AGAIN, attachmentFile.getOriginalFilename());
                return getUIFModelAndView(form);
            }
            if (attachmentFile != null && !StringUtils.isBlank(attachmentFile.getOriginalFilename())) {
                if (attachmentFile.getSize() == 0) {
                    GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                            RiceKeyConstants.ERROR_UPLOADFILE_EMPTY, attachmentFile.getOriginalFilename());
                    return getUIFModelAndView(form);
                } else {
                    try {
                        oleAgreementDocumentMetadata.setAgreementFileName(attachmentFile.getOriginalFilename());
                        oleAgreementDocumentMetadata.setAgreementMimeType(attachmentFile.getContentType());
                        storeAgreementAttachment(attachmentFile);
                        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
                        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();
                        updateEventLogForLocation(oleLicenseRequestBo, "file", "Agreement Document uploaded - " + attachmentFile.getOriginalFilename());
                    } catch (Exception e) {
                        LOG.error("Exception while storing the Agreement Document"+e);
                    }
                }
            }
        } else {
            GlobalVariables.getMessageMap().putErrorForSectionId("AgreementDocumentSection", OLEConstants.OleLicenseRequest.ERROR_FILE_NOT_FOUND);
            return getUIFModelAndView(form);
        }

        return addLine(form, result, request, response);
    }

    /**
     * This method will deletes the agreement document from the existing list
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteAgreementDocument")
    public ModelAndView deleteAgreementDocument(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MultipartFile attachmentFile = form.getAttachmentFile();
        String selectedLineIndex = uifForm.getActionParamaterValue("selectedLineIndex");
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();
        OleAgreementDocumentMetadata oleAgreementDocumentMetadata = oleLicenseRequestBo.getAgreementDocumentMetadataList().get(Integer.parseInt(selectedLineIndex));
        boolean isSuccesfull = false;
        try {
            if (oleAgreementDocumentMetadata.getAgreementUUID() != null) {
                isSuccesfull = getOleLicenseRequestService().deleteAgreementDocument(oleAgreementDocumentMetadata);
            } else {
                isSuccesfull = deleteAgreementAttachment(attachmentFile);
            }
            if (isSuccesfull) {
                updateEventLogForLocation(oleLicenseRequestBo, "file",
                        "Agreement Document deleted - " + oleAgreementDocumentMetadata.getAgreementFileName());
                performWorkflowAction(form, UifConstants.WorkflowAction.SAVE, true);
            }
        } catch (Exception e) {
            LOG.error("Exception while deleting the Agreement Document"+e);
        }
        return deleteLine(uifForm, result, request, response);
    }

    /**
     * This method will download the agreement document from the docstore if it is already ingested
     * otherwise will download from the temprovary location.
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=downloadAgreement")
    public ModelAndView downloadAgreement(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        String selectedLineIndex = uifForm.getActionParamaterValue("selectedLineIndex");
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocumentBase document = (MaintenanceDocumentBase) maintenanceForm.getDocument();
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();

        OleAgreementDocumentMetadata oleAgreementDocumentMetadata = oleLicenseRequestBo.getAgreementDocumentMetadataList().get(Integer.parseInt(selectedLineIndex));
        File file;
        try {
            if (oleAgreementDocumentMetadata.getAgreementUUID() == null) {
                file = new File(getKualiConfigurationService().getPropertyValueAsString(
                        KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.AGREEMENT_TMP_LOCATION +
                        File.separator + oleAgreementDocumentMetadata.getAgreementFileName());
                LOG.info("Uploaded file location : " + file.getAbsolutePath());
            } else {
                file = getOleLicenseRequestService().downloadAgreementDocumentFromDocstore(oleAgreementDocumentMetadata);
                LOG.info("Uploaded file location : " + file.getAbsolutePath());

            }
            response.setContentType(oleAgreementDocumentMetadata.getAgreementMimeType());
            response.setContentLength((int) file.length());
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + oleAgreementDocumentMetadata.getAgreementFileName() + "\"");
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(fis, response.getOutputStream());

        } catch (Exception e) {
            LOG.error("Exception while retrieving the attachment"+e);
        } finally {
            updateEventLogForLocation(oleLicenseRequestBo, "agreement document", "Agreement Document Downloaded");
            performWorkflowAction(maintenanceForm, UifConstants.WorkflowAction.SAVE, true);
        }

        return null;
    }

    /**
     * This method will populate the url which will be used to view the related Requisition Document
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=viewRelatedDocument")
    public ModelAndView viewRelatedDocument(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) uifForm;
        String oleurl = ConfigContext.getCurrentContextConfig().getProperty("ole.url");
        String olePortal = oleurl.substring(0, oleurl.indexOf("portal.jsp"));
        String requisitionDocNumber = request.getParameter(OLEConstants.OleLicenseRequest.REQUISITION_DOC_NUM);
        String redirectUrl = olePortal + KewApiConstants.Namespaces.MODULE_NAME + "/" + KewApiConstants.DOC_HANDLER_REDIRECT_PAGE + "?" + KewApiConstants.COMMAND_PARAMETER + "=" + KewApiConstants.DOCSEARCH_COMMAND + "&" + KewApiConstants.DOCUMENT_ID_PARAMETER + "=" + requisitionDocNumber;
        //GlobalVariables.getUifFormManager().removeForm(uifForm);
        modelAndView = new ModelAndView(REDIRECT_PREFIX + redirectUrl);

        return modelAndView;
    }

    /**
     * This method store the uploaded agreement document to the specified location
     *
     * @return ModelAndView
     */
    private void storeAgreementAttachment(MultipartFile agreementFile) throws IOException {
        String location = "";
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.AGREEMENT_TMP_LOCATION;
        LOG.info("Agreement Attachment LOG :" + location);
        File dirLocation = new File(location);
        if (!dirLocation.exists()) {
            boolean success = dirLocation.mkdirs();
            if (!success) {
                LOG.error("Could not generate directory for File at: " + dirLocation.getAbsolutePath());
            }
        }
        location = location + File.separator + agreementFile.getOriginalFilename();
        InputStream fileContents = agreementFile.getInputStream();
        File fileOut = new File(location);
        FileOutputStream streamOut = null;
        BufferedOutputStream bufferedStreamOut = null;
        try {
            streamOut = new FileOutputStream(fileOut);
            bufferedStreamOut = new BufferedOutputStream(streamOut);
            int c;
            while ((c = fileContents.read()) != -1) {
                bufferedStreamOut.write(c);
            }
        } finally {
            bufferedStreamOut.close();
            streamOut.close();
        }

    }

    /**
     * This method true if the agreement document is new.
     *
     * @param agreementDocumentMetadata
     * @return boolean
     */
    private boolean isNewAgreementDocument(OleAgreementDocumentMetadata agreementDocumentMetadata) {
        return agreementDocumentMetadata.getAgreementUUID() == null;
    }

    /**
     * Saves the document instance contained on the form
     *
     * @param form - document form base containing the document instance that will be saved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleLicenseRequestBo newLicenseRequestBo = (OleLicenseRequestBo) document.getNewMaintainableObject().getDataObject();
        OleLicenseRequestBo oldLicenseRequestBo = (OleLicenseRequestBo) document.getOldMaintainableObject().getDataObject();
        if (oldLicenseRequestBo != null && newLicenseRequestBo.getLocationId() != null &&
                newLicenseRequestBo.getLocationId().equals(OLEConstants.OleLicenseRequest.LICENSE_INITIAL_LOCATON) &&
                !newLicenseRequestBo.getLocationId().equals(oldLicenseRequestBo.getLocationId())) {
            updateEventLogForLocation(newLicenseRequestBo, "location", null);
        }
        processAgreementDocument(newLicenseRequestBo);
        Map<String, String> licenses = getLicenseWorkFlows();
        if (licenses.containsKey(newLicenseRequestBo.getLicenseRequestWorkflowTypeCode())) {
            document.getDocumentHeader().getWorkflowDocument().setApplicationDocumentStatus(getLicenseRequestName(licenses.get(newLicenseRequestBo.getLicenseRequestWorkflowTypeCode())));
            if (newLicenseRequestBo.getLicenseRequestWorkflowTypeCode().equals(OLEConstants.OleLicenseRequest.LICENSE_RECEIVED)) {
                updateEventLogForLocation(newLicenseRequestBo, "license received", "status change");
            }
            newLicenseRequestBo.setLicenseRequestStatusCode(licenses.get(newLicenseRequestBo.getLicenseRequestWorkflowTypeCode()));
            performWorkflowAction(form, UifConstants.WorkflowAction.SAVE, true);
        }
        return super.save(form, result, request, response);
    }

   /* */

    /**
     * This method is used to view the agreement linked to the license request
     *
     * @return
     *//*
    @RequestMapping(params = "methodToCall=viewAgreement")
    public ModelAndView viewAgreement(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        OleLicenseRequestBo oleLicenseRequestBo = (OleLicenseRequestBo)document.getNewMaintainableObject().getDataObject();
        String url = PropertyUtil.getPropertyUtil().getProperty("docstore.url") ;
        if(oleLicenseRequestBo != null) {
            String redirectUrl = url+"?docAction=checkOut&uuid="+oleLicenseRequestBo.getAgreementId();
            //GlobalVariables.getUifFormManager().removeForm(uifForm);
            modelAndView = new ModelAndView(REDIRECT_PREFIX + redirectUrl);
        }
        else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleLicenseRequest.AGREEMENT_ERROR);
        }
        return modelAndView;
    }
*/
    private ConfigurationService getKualiConfigurationService() {
        return GlobalResourceLoader.getService("kualiConfigurationService");
    }

    /**
     * This method deletes the attached file
     *
     * @param agreementFile
     * @return boolean
     * @throws java.io.IOException
     */
    private boolean deleteAgreementAttachment(MultipartFile agreementFile) throws IOException {
        LOG.debug("Inside deleteAgreementAttachment method");
        String location = "";
        location = getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY) + OLEConstants.OleLicenseRequest.AGREEMENT_TMP_LOCATION + File.separator + agreementFile.getOriginalFilename();
        File attachmentFile = new File(location);
        return attachmentFile.delete();
    }

    /**
     * This method update the eventlog in the License Request according to the event type nad description
     *
     * @param oleLicenseRequestBo
     * @param eventType
     * @param description
     */
    private void updateEventLogForLocation(OleLicenseRequestBo oleLicenseRequestBo, String eventType, String description) {
        LOG.debug("Inside updateEventLogForLocation method");
        OleEventLogBo oleEventLogBo = new OleEventLogBo();
        oleEventLogBo.setCurrentTimeStamp();
        oleEventLogBo.setCreatedBy(GlobalVariables.getUserSession().getPrincipalName());
        if (eventType.equalsIgnoreCase("location")) {
            Map criteria = new HashMap();
            criteria.put("id", oleLicenseRequestBo.getLocationId());
            OleLicenseRequestLocation location = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleLicenseRequestLocation.class,
                    criteria);
            oleEventLogBo.setEventDescription(ROUTED_EXTERNAL);
            oleEventLogBo.setEventType("location : " + location.getDescription());
        } else {
            oleEventLogBo.setEventDescription(description);
            oleEventLogBo.setEventType(eventType);
        }

        oleLicenseRequestBo.getEventLogs().add(oleEventLogBo);
    }

    /**
     * This method ingest the new agreement document or update the existing agreement document metadata in the docstore
     *
     * @param oleLicenseRequestBo
     */
    private void processAgreementDocument(OleLicenseRequestBo oleLicenseRequestBo) {
        LOG.debug("Inside processAgreementDocument method");
        List<OleAgreementDocumentMetadata> agreementDocuments = oleLicenseRequestBo.getAgreementDocumentMetadataList();
        List<OleAgreementDocumentMetadata> newAgreementDocuments = new ArrayList<OleAgreementDocumentMetadata>();
        List<OleAgreementDocumentMetadata> checkInAgreementDocuments = new ArrayList<OleAgreementDocumentMetadata>();
        if (agreementDocuments.size() > 0) {
            for (OleAgreementDocumentMetadata agreementDocument : agreementDocuments) {
                if (isNewAgreementDocument(agreementDocument)) {
                    newAgreementDocuments.add(agreementDocument);
                } else {
                    checkInAgreementDocuments.add(agreementDocument);
                }
            }
        }
        List<OleAgreementDocumentMetadata> agreementDocumentMetadataList = new ArrayList<OleAgreementDocumentMetadata>();
        if (newAgreementDocuments.size() > 0) {
            agreementDocumentMetadataList.addAll(getOleLicenseRequestService().processIngestAgreementDocuments(newAgreementDocuments));
        }
        if (checkInAgreementDocuments.size() > 0) {
            agreementDocumentMetadataList.addAll(getOleLicenseRequestService().processCheckInAgreementDocuments(checkInAgreementDocuments));
        }
        oleLicenseRequestBo.setAgreementDocumentMetadataList(agreementDocumentMetadataList);
    }

    /**
     * This method returns a map which will have the Initail level Workfolw codes
     *
     * @return
     */
    private Map<String, String> getLicenseWorkFlows() {
        LOG.debug("Inside getLicenseWorkFlows method");
        Map<String, String> licenseWorkFlows = new HashMap<String, String>();
        licenseWorkFlows.put(OLEConstants.OleLicenseRequest.LICENSE_IN_NEGO, OLEConstants.OleLicenseRequest.LICENSE_IN_NEGO_VALUE);
        licenseWorkFlows.put(OLEConstants.OleLicenseRequest.LICENSE_IN_PROCESS, OLEConstants.OleLicenseRequest.LICENSE_IN_PROCESS_VALUE);
        licenseWorkFlows.put(OLEConstants.OleLicenseRequest.LICENSE_INITIAL_WORKFLOW, OLEConstants.OleLicenseRequest.LICENSE_NEEDED);
        licenseWorkFlows.put(OLEConstants.OleLicenseRequest.LICENSE_RECEIVED,
                OLEConstants.OleLicenseRequest.LICENSE_COMPLETE_RETURN);
        licenseWorkFlows.put(OLEConstants.OleLicenseRequest.LICENSE_REQUESTED, OLEConstants.OleLicenseRequest.LICENSE_REQUESTED_VALUE);
        return licenseWorkFlows;
    }

    @RequestMapping(params = "methodToCall=" + "closeDocument")
    public ModelAndView closeDocument(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {

        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        return performRedirect(form, url, props);
    }


}