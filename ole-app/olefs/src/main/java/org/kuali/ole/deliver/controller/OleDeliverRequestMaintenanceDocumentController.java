package org.kuali.ole.deliver.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.ASRConstants;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OLEDeliverNoticeHelperService;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.impl.OLEDeliverNoticeHelperServiceImpl;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/17/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/deliverRequestMaintenance")
public class OleDeliverRequestMaintenanceDocumentController extends MaintenanceDocumentController {
    private static final Logger LOG = Logger.getLogger(OleDeliverRequestMaintenanceDocumentController.class);
    private static final String NAMESPACE_CODE_SELECTOR = "namespaceCode";
    private static final String NAME_SELECTOR = "name";
    private final static String RULE_EVALUATED = "Rule Evaluated";
    private final static String ROUTED_EXTERNAL = "Routed External";
    private LoanProcessor loanProcessor;
    private DocstoreUtil docstoreUtil = getDocstoreUtil();
    private OleDeliverRequestDocumentHelperServiceImpl service =  getService();
    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService ;
    private OleCirculationPolicyService oleCirculationPolicyService;
    private CircDeskLocationResolver circDeskLocationResolver;

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }
    /**
     * This method initiate LoanProcessor.
     *
     * @return LoanProcessor
     */
    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = new LoanProcessor();
        }
        return loanProcessor;
    }

    public DocstoreUtil getDocstoreUtil(){
        if(docstoreUtil == null){
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getService(){
        if(service == null){
            service = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return service;

    }

    public OLEDeliverNoticeHelperService getOleDeliverNoticeHelperService() {
        if(oleDeliverNoticeHelperService ==null){
            oleDeliverNoticeHelperService = SpringContext.getBean(OLEDeliverNoticeHelperServiceImpl.class);
        }
        return oleDeliverNoticeHelperService;
    }

    /**
     * Gets the oleCirculationPolicyService attribute.
     *
     * @return Returns the oleCirculationPolicyService
     */
    public OleCirculationPolicyService getOleCirculationPolicyService() {
        if (null == oleCirculationPolicyService) {
            oleCirculationPolicyService = SpringContext.getBean(OleCirculationPolicyServiceImpl.class);
        }
        return oleCirculationPolicyService;
    }

    public void setOleDeliverNoticeHelperService(OLEDeliverNoticeHelperService oleDeliverNoticeHelperService) {
        this.oleDeliverNoticeHelperService = oleDeliverNoticeHelperService;
    }

    @RequestMapping(params = "methodToCall=" + "maintenanceCancel")
    public ModelAndView maintenanceCancel(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenanceForDelete(form, request, "Cancel");
        return getUIFModelAndView(form);
    }


    /**
     * This method invokes deleteAttachment method to delete attachment and set the error accordingly ..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "cancelDocument")
    public ModelAndView cancelDocument(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside Cancel document");
        MaintenanceDocument document = form.getDocument();
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) document.getOldMaintainableObject().getDataObject();
        service.cancelDocument(oleDeliverRequestBo);

        return back(form, result, request, response);
    }

    /**
     * This method populates confirmation to delete the document.
     *
     * @param form
     * @param request
     * @param maintenanceAction
     */
    protected void setupMaintenanceForDelete(MaintenanceDocumentForm form, HttpServletRequest request, String maintenanceAction) {
        MaintenanceDocument document = form.getDocument();
        if (document == null) {
            document = getMaintenanceDocumentService()
                    .setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                            maintenanceAction);

            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }

        form.setMaintenanceAction(maintenanceAction);
        getMaintenanceDocumentService().setupMaintenanceObject(document, maintenanceAction, request.getParameterMap());
        MaintenanceUtils.checkForLockingDocument(document, false);
    }

    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside route document");

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) form.getDocument();
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) maintenanceDocument.getDocumentDataObject();
        oleDeliverRequestBo.setMessage(null);
        oleDeliverRequestBo.setValidToProcess(true);
        oleDeliverRequestBo.setRequestLevel(OLEConstants.ITEM_LEVEL);

        if(!isItemBarcodeOrItemUUIDExist(oleDeliverRequestBo)){
            oleDeliverRequestBo.setValidToProcess(false);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ITEM_BARCODE_OR_UUID_REQUIRED);
            return super.route(form, result, request, response);
        }
        if (oleDeliverRequestBo.getOperatorModifiedId() == null) {
            if (!docstoreUtil.isItemAvailableInDocStore(oleDeliverRequestBo)) {
                oleDeliverRequestBo.setValidToProcess(false);
                GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.ITEM_ID, OLEConstants.ITEM_NOT_AVAILABLE);
                //return super.route(form,result,request,response);
            }

            oleDeliverRequestBo = service.processRequestType(oleDeliverRequestBo);
            if (oleDeliverRequestBo.getBorrowerId() == null) {
                oleDeliverRequestBo.setValidToProcess(false);
                GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.BORROWER_ID, OLEConstants.OleDeliverRequest.INVALID_PATRON_BARCODE);
                return getUIFModelAndView(form);
            }
            if (oleDeliverRequestBo.getRequestTypeId() == null || (oleDeliverRequestBo.getRequestTypeId() != null && oleDeliverRequestBo.getRequestTypeId().trim().isEmpty())) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleDeliverRequest.INVALID_REQUEST_TYPE);
                oleDeliverRequestBo.setValidToProcess(false);
                // return getUIFModelAndView(form);
            }
            if (oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_PROXY_PATRON) && oleDeliverRequestBo.getProxyBorrowerId() == null) {
                GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.PROXY_BORROWER_ID, OLEConstants.OleDeliverRequest.INVALID_PROXY_PATRON);
                oleDeliverRequestBo.setValidToProcess(false);
                //  return getUIFModelAndView(form);
            }
            if (oleDeliverRequestBo.getRequestTypeId() != null && !oleDeliverRequestBo.getRequestTypeId().equals("7") && !oleDeliverRequestBo.getRequestTypeId().equals("8")) {

                String message = service.patronRecordExpired(oleDeliverRequestBo);
                if (message != null) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.BORROWER_ID, message);
                    oleDeliverRequestBo.setValidToProcess(false);
                }

                if (!service.isValidProxyPatron(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.PROXY_BORROWER_ID, OLEConstants.OleDeliverRequest.INVALID_PROXY);
                    oleDeliverRequestBo.setValidToProcess(false);
                }
                if (!service.validateDeliveryPrivilege(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.NO_DELIVERY_PRIVILEGE);
                    oleDeliverRequestBo.setValidToProcess(false);
                }
                if (!service.validatePagingPrivilege(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.NO_PAGE_PRIVILEGE);
                    oleDeliverRequestBo.setValidToProcess(false);
                }

                if (service.isRequestAlreadyRaisedByPatron(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.ALREADY_RAISED);
                    oleDeliverRequestBo.setValidToProcess(false);
                }

                /* if( service.isItemAvailable(oleDeliverRequestBo)){
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.ITEM_ID,OLEConstants.OleDeliverRequest.ITEM_AVAILABLE);
                }*/
                ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
                String itemLocation = oleDeliverRequestBo.getItemLocation();
                if(itemLocation!=null ){
                    if (oleDeliverRequestBo.isASRItem() && oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS)) && !oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))){
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "Cannot create "+oleDeliverRequestBo.getRequestTypeCode()+" for this item");
                        oleDeliverRequestBo.setValidToProcess(false);
                    }else if(oleDeliverRequestBo.isASRItem() && !oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS)) && oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))){
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "Cannot create "+oleDeliverRequestBo.getRequestTypeCode()+" for this item");
                        oleDeliverRequestBo.setValidToProcess(false);
                    }
                }
                if (!service.canRaiseRequest(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.ITEM_ID, OLEConstants.OleDeliverRequest.NO_REQUEST,oleDeliverRequestBo.getRequestTypeCode(), oleDeliverRequestBo.getItemStatus());
                    oleDeliverRequestBo.setValidToProcess(false);
                }
                if (!service.isItemEligible(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.ITEM_ID, OLEConstants.OleDeliverRequest.ITEM_NOT_ELIGIBLE);
                    oleDeliverRequestBo.setValidToProcess(false);
                }

                if (service.isAlreadyLoaned(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.ITEM_ID, OLEConstants.OleDeliverRequest.ITEM_ALREADY_LOANED);
                    oleDeliverRequestBo.setValidToProcess(false);
                }

            }
            if (oleDeliverRequestBo.getRequestTypeId() != null && oleDeliverRequestBo.getRequestTypeId().equals("8")) {
                if (!oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.TRANSIT_ERROR);
                    oleDeliverRequestBo.setValidToProcess(false);
                }
                else if (!service.isItemAvailableForLoan(oleDeliverRequestBo)) {
                    if (service.isRequestRaised(oleDeliverRequestBo)) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.ITEM_IN_LOAN);
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.TRANSIT_REQUEST_RAISED);
                        oleDeliverRequestBo.setValidToProcess(false);
                    }
                    else
                    {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.ITEM_IN_LOAN);
                        oleDeliverRequestBo.setValidToProcess(false);
                    }

                }
                else  if (service.isRequestRaised(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.TRANSIT_REQUEST_RAISED);
                    oleDeliverRequestBo.setValidToProcess(false);
                }

                else if (service.processOperator(GlobalVariables.getUserSession().getPrincipalId())) {
                    LoanProcessor loanProcessor = getLoanProcessor();
                    try {

                        Item oleItem = oleDeliverRequestBo.getOleItem();
                        oleItem.setItemStatusEffectiveDate(new Date(System.currentTimeMillis()).toString());
                        if (oleDeliverRequestBo.getInTransitCheckInNote() != null && !oleDeliverRequestBo.getInTransitCheckInNote().isEmpty()) {
                            oleItem.setCheckinNote(oleDeliverRequestBo.getInTransitCheckInNote());
                        }
                        loanProcessor.updateItemStatus(oleItem, OLEConstants.OleDeliverRequest.INTRANSIT_STATUS);
                    } catch (Exception e) {
                        LOG.error("Exception", e);
                    }
                /*    if (!service.isItemAvailableForLoan(oleDeliverRequestBo)) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.ITEM_IN_LOAN);
                    }
                    if (service.isRequestRaised(oleDeliverRequestBo)) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.TRANSIT_REQUEST_RAISED);
                    }    */

                } else {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR, OLEConstants.OleDeliverRequest.INVALID_OPERATOR);
                    oleDeliverRequestBo.setValidToProcess(false);
                }
            }
            if (oleDeliverRequestBo.getRequestTypeId() != null && oleDeliverRequestBo.getRequestTypeId().equals("7")) {
                String message = service.patronRecordExpired(oleDeliverRequestBo);
                if (message != null) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.BORROWER_ID, message);
                    oleDeliverRequestBo.setValidToProcess(false);
                }
                if (service.isRequestAlreadyRaisedByPatron(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.BORROWER_ID, OLEConstants.OleDeliverRequest.ALREADY_RAISED);
                    oleDeliverRequestBo.setValidToProcess(false);
                }
                if (!service.canRaiseRequest(oleDeliverRequestBo)) {
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.ITEM_ID, OLEConstants.OleDeliverRequest.NO_REQUEST,oleDeliverRequestBo.getRequestTypeCode(), oleDeliverRequestBo.getItemStatus());
                    oleDeliverRequestBo.setValidToProcess(false);
                 }

            }

            oleDeliverRequestBo = service.processRequester(oleDeliverRequestBo);
             if(oleDeliverRequestBo.isValidToProcess()){
               service.processRequestExpirationDate(oleDeliverRequestBo);
               service.fireRules(oleDeliverRequestBo,false,false);
            //  oleDeliverRequestBo = service.reOrderQueuePosition(oleDeliverRequestBo);
        }
        if ((oleDeliverRequestBo.getMessage() != null && !oleDeliverRequestBo.getMessage().isEmpty())) {
            return getUIFModelAndView(form);
        }
       // oleDeliverRequestBo.setItemFullLocation(oleDeliverRequestBo.getOleItem().);
        oleDeliverRequestBo.setOleItem(null);

        return  super.route(form, result, request, response);
        }else{
            return  super.route(form, result, request, response);
        }
    }

    private boolean isItemBarcodeOrItemUUIDExist(OleDeliverRequestBo oleDeliverRequestBo) {
        if(StringUtils.isNotBlank(oleDeliverRequestBo.getItemId()) || StringUtils.isNotBlank(oleDeliverRequestBo.getItemUuid())){
            return true;
        }
        return false;
    }


    private EngineResults executeEngineResults(OleDeliverRequestBo oleDeliverRequestBo) {
        List<OleDeliverRequestBo> recallList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> holdList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> pageList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> asrList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
        Engine engine = KrmsApiServiceLocator.getEngine();
        EngineResults engineResult = null;
        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put(OLEConstants.NAME_NM, OLEConstants.REQUEST_AGENDA_NM);
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        if (agendaBos != null && agendaBos.size() > 0) {
            AgendaBo agendaBo = agendaBos.get(0);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.AGENDA_NAME, agendaBo.getName());
            List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

            SelectionCriteria selectionCriteria =
                    SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()),
                            getAgendaContext(OLEConstants.REQUEST_AGENDA_NM));

            ExecutionOptions executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

            Facts.Builder factBuilder = Facts.Builder.create();

            String borrowerType = "";
            if (oleDeliverRequestBo.getOlePatron() != null && oleDeliverRequestBo.getOlePatron().getOleBorrowerType() != null) {
                borrowerType = oleDeliverRequestBo.getOlePatron().getOleBorrowerType().getBorrowerTypeCode();
            }
            String itemType = oleDeliverRequestBo.getItemType();

            String requestTypeId = oleDeliverRequestBo.getRequestTypeId();

            String requestType = oleDeliverRequestBo.getRequestTypeCode();

            String location = oleDeliverRequestBo.getShelvingLocation();
            LoanProcessor loanProcessor = getLoanProcessor();



            List<FeeType> feeTypeList = getOleCirculationPolicyService().getPatronBillPayment(oleDeliverRequestBo.getBorrowerId());
            Integer overdueFineAmt = 0;
            Integer replacementFeeAmt = 0;
            Integer serviceFeeAmt = 0;
            for (FeeType feeType : feeTypeList) {
                Integer fineAmount = feeType.getFeeAmount().subtract(feeType.getPaidAmount()).intValue();
                overdueFineAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.OVERDUE_FINE) ? fineAmount : 0;
                replacementFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE) ? fineAmount : 0;
                serviceFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.SERVICE_FEE) ? fineAmount : 0;
            }


            OleLoanDocument oleLoanDocument = loanProcessor.getOleLoanDocumentUsingItemUUID(oleDeliverRequestBo.getItemUuid());
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            dataCarrierService.addData(OLEConstants.LOANED_DATE, oleLoanDocument != null ? oleLoanDocument.getCreateDate() : null);
            dataCarrierService.addData(OLEConstants.DUE_DATE,oleLoanDocument!=null?oleLoanDocument.getLoanDueDate():null);
            String patronId = oleDeliverRequestBo.getBorrowerId()!=null ?  oleDeliverRequestBo.getBorrowerId() : "";
            String itemId = oleDeliverRequestBo.getItemId()!=null ?  oleDeliverRequestBo.getItemId() : "";
            dataCarrierService.removeData(patronId+itemId);
            String borrowerId = oleDeliverRequestBo.getBorrowerId();
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
            if (requestTypeId != null && (requestTypeId.equals("1") || requestTypeId.equals("2"))) {
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "1");
                recallList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "2");
                recallList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if (requestTypeId != null && (requestTypeId.equals("3") || requestTypeId.equals("4"))) {
                //  holdList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "3");
                holdList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "4");
                holdList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if (requestTypeId != null && (requestTypeId.equals("5") || requestTypeId.equals("6"))) {
                // pageList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "5");
                pageList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "6");
                pageList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if(requestTypeId != null && (requestTypeId.equals("9"))){
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "9");
                asrList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));

            }
            Map<String, String> requestByBorrower = new HashMap<String, String>();
            requestByBorrower.put(OLEConstants.OleDeliverRequest.BORROWER_ID, borrowerId);
            requestsByBorrower = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestByBorrower);
            HashMap<String, Object> termValues = new HashMap<String, Object>();
            termValues.put(OLEConstants.BORROWER_TYPE, borrowerType);
            termValues.put(OLEConstants.ITEM_TYPE, itemType);
            termValues.put(OLEConstants.LOCATION, location);
            termValues.put(OLEConstants.ITEM_SHELVING, oleDeliverRequestBo.getShelvingLocation());
            termValues.put(OLEConstants.ITEM_COLLECTION, oleDeliverRequestBo.getItemCollection());
            termValues.put(OLEConstants.ITEM_LIBRARY, oleDeliverRequestBo.getItemLibrary());
            termValues.put(OLEConstants.ITEM_CAMPUS, oleDeliverRequestBo.getItemCampus());
            termValues.put(OLEConstants.ITEM_INSTITUTION, oleDeliverRequestBo.getItemInstitution());
            termValues.put(OLEConstants.MAX_NO_OF_RECALL_REQUEST, new Integer(recallList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_HOLD_REQUEST, new Integer(holdList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_PAGE_REQUEST, new Integer(pageList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_ASR_REQUEST, new Integer(asrList.size()) + 1);
            termValues.put(OLEConstants.OleDeliverRequest.CLAIMS_RETURNED_FLAG, oleDeliverRequestBo.isClaimsReturnedFlag());
            termValues.put(OLEConstants.FINE_AMOUNT, overdueFineAmt + replacementFeeAmt + serviceFeeAmt);
            // termValues.put("maxNumberOfRequestByBorrower",requestsByBorrower.size());
            termValues.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, requestTypeId);
            termValues.put(OLEConstants.REQUEST_TYPE, requestType);
            termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
            termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);

            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }
            if (LOG.isDebugEnabled()){
                LOG.debug("termValues.toString()" + termValues.toString());
            }
            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
            dataCarrierService.removeData(patronId+itemId);
            List<String> errorMessage = (List<String>) engineResult.getAttribute(OLEConstants.ERROR_ACTION);
            java.sql.Date d = (java.sql.Date) engineResult.getAttribute(OLEConstants.REQ_EXPIRATION_DATE);
            Timestamp recallDueDate = (Timestamp) engineResult.getAttribute(OLEConstants.RECALL_DUE_DATE);
            String notice = (String) engineResult.getAttribute(OLEConstants.NOTICE);
            oleDeliverRequestBo.setNoticeType(notice);
            if(oleDeliverRequestBo.getRequestExpiryDate()==null){
            Map<String,String> locationMap = new HashMap<String,String>();
            locationMap.put("locationCode",oleDeliverRequestBo.getShelvingLocation());
            List<OleLocation> oleLocationBos = (List<OleLocation>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class,locationMap);
            if(oleLocationBos!=null && oleLocationBos.size()>0){
                Map<String,String> circulationDeskLocationMap = new HashMap<String,String>();
                circulationDeskLocationMap.put("circulationDeskLocation",oleLocationBos.get(0).getLocationId());
                List<OleCirculationDeskLocation> oleCirculationDeskLocationList = (List<OleCirculationDeskLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskLocation.class,circulationDeskLocationMap);
                if(oleCirculationDeskLocationList!=null && oleCirculationDeskLocationList.size()>0){
                    for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocationList){
                        if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation() !=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())) {
                            String requestExpirationDays= oleCirculationDeskLocation.getOleCirculationDesk().getRequestExpirationDays();
                            if(requestExpirationDays!=null && !requestExpirationDays.equalsIgnoreCase("0")){
                                oleDeliverRequestBo.setRequestExpiryDate(service.addDate(new java.sql.Date(System.currentTimeMillis()), Integer.parseInt(requestExpirationDays)));
                            }
                        }
                    }
                }
            }
            }
            if(oleDeliverRequestBo.getRequestExpiryDate() == null){
                oleDeliverRequestBo.setRequestExpiryDate(d);
            }
            StringBuffer failures = new StringBuffer();
            if (errorMessage != null && errorMessage.size() > 0) {
                int i = 1;
                for (String errMsg : errorMessage) {
                    failures.append(i++ + ". " + errMsg + OLEConstants.BREAK);
                }
            }
            if (!failures.toString().isEmpty()) {
                oleDeliverRequestBo.setMessage(failures.toString());
            }
            else if(failures.toString().trim().isEmpty()){

            if(oleLoanDocument!=null && (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))){

                Timestamp itemDueDate = null;
                if(ObjectUtils.isNotNull(oleLoanDocument)){
                    itemDueDate = oleLoanDocument.getLoanDueDate();
                }
                Item oleItem = oleDeliverRequestBo.getOleItem();
                if(itemDueDate == null && recallDueDate!=null){
                    // oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(itemDueDate.getTime())));
                    oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                    oleLoanDocument.setLoanDueDate(recallDueDate);
                    oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                    oleItem.setDueDateTime(recallDueDate.toString());
                    getBusinessObjectService().save(oleLoanDocument);
                    OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                            getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                    oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                    OLEDeliverNoticeHelperService oleDeliverNoticeHelperService =getOleDeliverNoticeHelperService();
                    oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                    try{
                     /*   oleDeliverNoticeHelperService.generateDeliverNotices(oleLoanDocument.getPatronId(), oleLoanDocument.getItemUuid(),
                                oleLoanDocument.getOleCirculationDesk()!=null ? oleLoanDocument.getOleCirculationDesk().getCirculationDeskCode() : null,
                                oleLoanDocument.getBorrowerTypeCode(),itemType, oleDeliverRequestBo.getItemStatus(),
                                oleLoanDocument.isClaimsReturnedIndicator() ? OLEConstants.TRUE : OLEConstants.FALSE,
                                oleLoanDocument.getRepaymentFeePatronBillId() != null ? OLEConstants.TRUE : OLEConstants.FALSE,
                                oleDeliverRequestBo.getShelvingLocation(), oleDeliverRequestBo.getItemCollection(), oleDeliverRequestBo.getItemLibrary(),
                                oleDeliverRequestBo.getItemCampus(), oleDeliverRequestBo.getItemInstitution(), oleLoanDocument.getLoanDueDate(),oleLoanDocument.getLoanId(),oleDeliverRequestBo.getRequestTypeCode());
                    */
                        List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                        if(deliverNotices!=null){
                            for(OLEDeliverNotice deliverNotice : deliverNotices){
                                deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                            }
                            getBusinessObjectService().save(deliverNotices);
                        }
                    }catch(Exception e){
                        LOG.info("Exception occured while updating the date in notice table");
                        LOG.error(e,e);
                    }
                    oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate,"MM/dd/yyyy HH:mm:ss"));
                    try{
                        service.updateItem(oleItem);
                    }catch(Exception e){
                        if(LOG.isInfoEnabled()){
                            LOG.info("Exception occured while updating the item . " +e.getMessage() );
                        }
                        LOG.error(e,e);
                    }
                }
                if (recallDueDate != null && itemDueDate!=null ) {
                   // if(itemDueDate.compareTo(recallDueDate) > 0){
                        oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(itemDueDate.getTime())));
                        oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                        oleLoanDocument.setLoanDueDate(recallDueDate);
                        oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                        getBusinessObjectService().save(oleLoanDocument);
                        OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                                getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                        oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                        OLEDeliverNoticeHelperService oleDeliverNoticeHelperService =getOleDeliverNoticeHelperService();
                        oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                        try{
      /*                      oleDeliverNoticeHelperService.generateDeliverNotices(oleLoanDocument.getPatronId(), oleLoanDocument.getItemUuid(),
                                    oleLoanDocument.getOleCirculationDesk()!=null ? oleLoanDocument.getOleCirculationDesk().getCirculationDeskCode() : null,
                                    oleLoanDocument.getBorrowerTypeCode(),itemType, oleDeliverRequestBo.getItemStatus(),
                                    oleLoanDocument.isClaimsReturnedIndicator() ? OLEConstants.TRUE : OLEConstants.FALSE,
                                    oleLoanDocument.getRepaymentFeePatronBillId() != null ? OLEConstants.TRUE : OLEConstants.FALSE,
                                    oleDeliverRequestBo.getShelvingLocation(), oleDeliverRequestBo.getItemCollection(), oleDeliverRequestBo.getItemLibrary(),
                                    oleDeliverRequestBo.getItemCampus(), oleDeliverRequestBo.getItemInstitution(), oleLoanDocument.getLoanDueDate(),oleLoanDocument.getLoanId(),oleDeliverRequestBo.getRequestTypeCode());
                       */
                            List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                            if(deliverNotices!=null){
                                for(OLEDeliverNotice deliverNotice : deliverNotices){
                                    deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                    deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                                }
                                getBusinessObjectService().save(deliverNotices);
                            }
                        }catch(Exception e){
                            LOG.info("Exception occured while updating the date in notice table");
                            LOG.error(e,e);
                        }
                        oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate,"MM/dd/yyyy HH:mm:ss"));
                        try{
                            service.updateItem(oleItem);
                        }catch(Exception e){
                            if(LOG.isInfoEnabled()){
                                LOG.info("Exception occured while updating the item . " +e.getMessage() );
                            }
                            LOG.error(e,e);
                        }
                    /*}else{
                        oleDeliverRequestBo.setNewDueDate((new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime())));
                        oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime())));
                    }*/
                }
            }

            }
            dataCarrierService.addData(OLEConstants.ERROR_ACTION, null);

        }
        return engineResult;
    }

    /**
     * This method returns ElementValue using docContent and xpathExpression..
     *
     * @param docContent
     * @param xpathExpression
     * @return value
     */
    private String getElementValue(String docContent, String xpathExpression) {
        try {
            Document document = XmlHelper.trimXml(new ByteArrayInputStream(docContent.getBytes()));

            XPath xpath = XPathHelper.newXPath();
            String value = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);

            return value;

        } catch (Exception e) {
            LOG.error("Exception while getting element value", e);
            throw new RiceRuntimeException();
        }
    }

    /**
     * This method returns SelectionContext using contextName.
     *
     * @param contextName
     * @return Map
     */
    protected Map<String, String> getSelectionContext(String contextName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAMESPACE_CODE_SELECTOR, OLEConstants.OLE_NAMESPACE);
        selector.put(NAME_SELECTOR, contextName);
        return selector;
    }

    /**
     * This method returns AgendaContext using agendaName..
     *
     * @param agendaName
     * @return Map
     */
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAME_SELECTOR, agendaName);
        return selector;
    }


    @RequestMapping(params = "methodToCall=refreshPageView")
    public ModelAndView refreshPageView(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.refresh(form, result, request, response);
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) maintenanceForm.getDocument();
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) maintenanceDocument.getDocumentDataObject();
        oleDeliverRequestBo.setRequestTypePopulated(false);
        oleDeliverRequestBo.setRequestTypeId(null);
        if (oleDeliverRequestBo.getRequestTypeId() == null && oleDeliverRequestBo.getRequestTypeCode() != null && !oleDeliverRequestBo.getRequestTypeCode().trim().isEmpty()) {
            Map<String, String> requestTypeMap = new HashMap<String, String>();
            requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, oleDeliverRequestBo.getRequestTypeCode());
            List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
            if (oleDeliverRequestTypeList != null && oleDeliverRequestTypeList.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OleDeliverRequest.INVALID_REQUEST_TYPE);
            } else if (oleDeliverRequestTypeList != null && oleDeliverRequestTypeList.size() > 0) {
                oleDeliverRequestBo.setRequestTypeId(oleDeliverRequestTypeList.get(0).getRequestTypeId());
            }
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=searchItem")
    public ModelAndView searchItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) maintenanceForm.getDocument();
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) maintenanceDocument.getDocumentDataObject();
        if (!docstoreUtil.isItemAvailableInDocStore(oleDeliverRequestBo)) {
            oleDeliverRequestBo.setTitle(null);
            oleDeliverRequestBo.setAuthor(null);
            oleDeliverRequestBo.setCallNumber(null);
            oleDeliverRequestBo.setCopyNumber(null);
            oleDeliverRequestBo.setShelvingLocation(null);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.INV_ITEM_BAR);
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=searchPatron")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) maintenanceForm.getDocument();
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) maintenanceDocument.getDocumentDataObject();
        service.processPatron(oleDeliverRequestBo);
        if (oleDeliverRequestBo.getBorrowerName() == null || (oleDeliverRequestBo.getBorrowerName() != null && oleDeliverRequestBo.getBorrowerName().isEmpty()))
            GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.BORROWER_ID, OLEConstants.OleDeliverRequest.INVALID_PATRON);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=printPdf")
    public void printPdf(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        MaintenanceDocument maintenanceDocument = (MaintenanceDocument) maintenanceForm.getDocument();
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) maintenanceDocument.getDocumentDataObject();
        if (oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR) && oleDeliverRequestBo.getRequestTypeId().equals("8")) {
            int errorCount = GlobalVariables.getMessageMap().getErrorCount();
            if (errorCount == 0) {
                OleDeliverBatchServiceImpl oleDeliverBatchServiceimpl = new OleDeliverBatchServiceImpl();
                oleDeliverBatchServiceimpl.createPdfForIntransitRequest(oleDeliverRequestBo, response);
            }
        }
    }


}


