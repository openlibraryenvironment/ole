package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.deliver.printSlip.OlePrintSlip;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OLEDeliverService;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.exception.ParseException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The LoanController is the controller class for processing all the actions that corresponds to the Loan functionality in OLE.
 * The request mapping tag takes care of mapping the individual action to the corresponding functions.
 */
@Controller
@RequestMapping(value = "/loancontroller")
public class LoanController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(LoanController.class);

    private List<OleLoanDocument> printDueDateSlipList = new ArrayList<OleLoanDocument>();

    private List<OleLoanDocument> printHoldSlipList = new ArrayList<OleLoanDocument>();

    private LoanProcessor loanProcessor;

    private OleDeliverBatchServiceImpl oleDeliverBatchService;

    public static String fastAddBarcode = "";

    private List<String> loginUserList;
    private DocstoreClientLocator docstoreClientLocator;
    private CircDeskLocationResolver circDeskLocationResolver;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public OleDeliverBatchServiceImpl getOleDeliverBatchService() {
        if (oleDeliverBatchService == null) {
            oleDeliverBatchService = SpringContext.getBean(OleDeliverBatchServiceImpl.class);
        }
        return oleDeliverBatchService;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public void setOleDeliverBatchService(OleDeliverBatchServiceImpl oleDeliverBatchService) {
        this.oleDeliverBatchService = oleDeliverBatchService;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    /**
     * This method creates new OleLoan form
     *
     * @param request
     * @return OleLoanForm
     */
    @Override
    protected OleLoanForm createInitialForm(HttpServletRequest request) {
        return new OleLoanForm();
    }

    /**
     * This method converts UifFormBase to OleLoanForm
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the loan start method");
        fastAddBarcode = "";
        OleLoanForm oleLoanForm = null;
        if (form.getViewId().equalsIgnoreCase("PatronItemView")) {
            String formKey = request.getParameter("formKey");
            if (formKey == null) {
                if (loginUserList != null) {
                    loginUserList.clear();
                }
                oleLoanForm = (OleLoanForm) form;
                oleLoanForm.setReturnCheck(false);
                if (oleLoanForm.getOldPrincipalId() == null || "".equals(oleLoanForm.getOldPrincipalId()))
                    oleLoanForm.setOldPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
                Integer maxTimeForCheckInDate = 0;
                String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
                if (LOG.isInfoEnabled()) {
                    LOG.info("session timeout parameter:" + parameter);
                }
                String loanParameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_LOAN);
                String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
                String maxSessionTime = loanParameter;
                if (LOG.isInfoEnabled()) {
                    LOG.info("session timeout:" + maxSessionTime);
                }
                if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
                    oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));
                // Modified as per comments in Jira OLE-4901
                if (!getLoanProcessor().isValidCirculationDesk()) {
                    oleLoanForm.setLoanLoginUserInfo(GlobalVariables.getUserSession().getPrincipalName() + " " + OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
                    return super.start(oleLoanForm, result, request, response);
                }
                //To set circulation desk value initially
                oleLoanForm.setCirculationDesk(getLoanProcessor().getCircDeskId());
                oleLoanForm.setAudioForPastDate(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
                if (parameter != null) {
                    maxTimeForCheckInDate = Integer.parseInt(parameter);
                }
                oleLoanForm.setMaxTimeForCheckOutConstant(loanParameter);
                LOG.info("session timeout maxTimeForCheckInDate:" + maxTimeForCheckInDate);
                oleLoanForm.setMaxTimeForCheckInDate(maxTimeForCheckInDate);
                oleLoanForm.setCheckInDateMaxTime(maxTimeForCheckInDate);
                oleLoanForm.setDateAlertMessage(OLEConstants.CHECK_IN_DATE);
                oleLoanForm.setCurrentDate(new Date());
                oleLoanForm.setPatronFocus(true);
                if (oleLoanForm.getCheckInDate() == null) {
                    oleLoanForm.setCheckInDate(new Date());
                }
            } else {
                oleLoanForm = (OleLoanForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
                if (oleLoanForm.getOldPrincipalId() == null || "".equals(oleLoanForm.getOldPrincipalId()))
                    oleLoanForm.setOldPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
                oleLoanForm.setReturnCheck(false);
                oleLoanForm.setAjaxRequest(false);
                oleLoanForm.setAjaxReturnType("update-view");
                oleLoanForm = (OleLoanForm) form;
                oleLoanForm.setPageId(null);
                Integer maxTimeForCheckInDate = 0;
                String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
                LOG.info("session timeout parameter:" + parameter);
                String loanParameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_LOAN);
                String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);

                String maxSessionTime = loanParameter;
                if (LOG.isInfoEnabled()) {
                    LOG.info("session timeout:" + maxSessionTime);
                }
                if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
                    oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));

                oleLoanForm.setRemoveMissingPieceFlag(false);
                oleLoanForm.setRecordDamagedItemNote(false);
                oleLoanForm.setRecordMissingPieceNote(false);
                oleLoanForm.setRecordCheckoutMissingPieceNote(false);
                oleLoanForm.setDisplayRecordNotePopup(false);
                oleLoanForm.setCheckoutRecordFlag(false);
                oleLoanForm.setSkipMissingPieceRecordPopup(false);
                oleLoanForm.setSkipDamagedRecordPopup(false);
                oleLoanForm.setDisplayMissingPieceNotePopup(false);
                oleLoanForm.setCheckoutMissingPieceRecordFlag(false);
                oleLoanForm.setDisplayDamagedRecordNotePopup(false);
                oleLoanForm.setCheckoutDamagedRecordFlag(false);
                // Modified as per comments in Jira OLE-4901
                if (!getLoanProcessor().isValidCirculationDesk()) {
                    oleLoanForm.setLoanLoginUserInfo(GlobalVariables.getUserSession().getPrincipalName() + " " + OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
                    return super.start(oleLoanForm, result, request, response);
                }
                oleLoanForm.setAudioForPastDate(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
                if (parameter != null) {
                    maxTimeForCheckInDate = Integer.parseInt(parameter);
                }
                oleLoanForm.setMaxTimeForCheckOutConstant(loanParameter);
                LOG.info("session timeout maxTimeForCheckInDate:" + maxTimeForCheckInDate);
                oleLoanForm.setMaxTimeForCheckInDate(maxTimeForCheckInDate);
                oleLoanForm.setCheckInDateMaxTime(maxTimeForCheckInDate);
                oleLoanForm.setDateAlertMessage(OLEConstants.CHECK_IN_DATE);
                oleLoanForm.setCurrentDate(new Date());
                oleLoanForm.setPatronFocus(true);
                if (oleLoanForm.getCheckInDate() == null) {
                    oleLoanForm.setCheckInDate(new Date());
                }
            }


        } else {
            oleLoanForm = (OleLoanForm) form;
            //To set circulation desk value initially
            String formKey = request.getParameter("formKey");
            if (formKey == null) {
                oleLoanForm.setCirculationDesk(getLoanProcessor().getCircDesk());
            }
            if (oleLoanForm.getOldPrincipalId() == null || "".equals(oleLoanForm.getOldPrincipalId()))
                oleLoanForm.setOldPrincipalId(GlobalVariables.getUserSession().getPrincipalId());
            oleLoanForm.setPageId(null);
            oleLoanForm.setReturnCheck(true);
            Integer maxTimeForCheckInDate = 0;
            String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
            LOG.info("session timeout parameter:" + parameter);
            String loanParameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_LOAN);
            String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);

            String maxSessionTime = loanParameter;
            if (LOG.isInfoEnabled()) {
                LOG.info("session timeout:" + maxSessionTime);
            }
            if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
                oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));

            /*if (!loanProcessor.isValidCirculationDesk()) {
                oleLoanForm.setLoanLoginMessage(true);
                String loginInfo = loanProcessor.getErrorMessage();
                oleLoanForm.setLoanLoginUserInfo(loginInfo);
                return super.start(oleLoanForm, result, request, response);
                //throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPrincipalId(), "not Authorized", form.getViewId());
                //return new OLEKRADAuthorizationResolver().resolveException(request,response,null,new Exception("is not authorized"));
            }*/
            // Modified as per comments in Jira OLE-4901
            if (!getLoanProcessor().isValidCirculationDesk()) {
                oleLoanForm.setLoanLoginUserInfo(GlobalVariables.getUserSession().getPrincipalName() + " " + OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
                return super.start(oleLoanForm, result, request, response);
            }
            oleLoanForm.setAudioForPastDate(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
            if (parameter != null) {
                maxTimeForCheckInDate = Integer.parseInt(parameter);
            }
            oleLoanForm.setMaxTimeForCheckOutConstant(loanParameter);
            LOG.info("session timeout maxTimeForCheckInDate:" + maxTimeForCheckInDate);
            oleLoanForm.setMaxTimeForCheckInDate(maxTimeForCheckInDate);
            oleLoanForm.setCheckInDateMaxTime(maxTimeForCheckInDate);
            oleLoanForm.setDateAlertMessage(OLEConstants.CHECK_IN_DATE);
            oleLoanForm.setCurrentDate(new Date());
            oleLoanForm.setPatronFocus(true);
            if (oleLoanForm.getCheckInDate() == null) {
                oleLoanForm.setCheckInDate(new Date());
            }
            OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
            if (oleLoanDocument != null) {
                List<OleLoanDocument> documentList = oleLoanForm.getItemReturnList();
                if (documentList != null && documentList.size() > 0) {
                    OleCirculationDesk oleCirculationDesk = null;
                    OleLocation oleLocation = null;
                    if (oleLoanDocument.getCirculationLocationId() != null) {
                        //oleCirculationDesk = loanProcessor.getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
                        try{
                            oleLocation = getCircDeskLocationResolver().getLocationByLocationCode(oleLoanDocument.getItemLocation());
                        }
                        catch (Exception e){
                            LOG.error("Exception while fetching OleLocation based on item location" +e);
                        }
                        String routeTo = oleLoanForm.getRouteToLocation() != null ? oleLoanForm.getRouteToLocation() :
                                (oleLoanDocument.getRouteToLocation() != null ? oleLoanDocument.getRouteToLocation() :
                                        (oleLocation != null ? oleLocation.getLocationCode() : null));
                        documentList.get(0).setRouteToLocation(routeTo);
                    }
                }
            }
            oleLoanForm.setBackGroundCheckIn(false);
        }
        return super.start(oleLoanForm, result, request, response);
    }

    /**
     * To refresh patron record.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        if (oleLoanForm.getPatronFirstName() != null) {
            oleLoanForm.setReturnCheck(false);
            oleLoanForm.setPatronFirstName(null);
            super.refresh(oleLoanForm, result, request, response);
            return searchPatron(oleLoanForm, result, request, response);
        }
        if (oleLoanForm.getItemUuid() != null && oleLoanForm.getInstanceUuid() != null) {
            oleLoanForm.setReturnCheck(false);
            super.refresh(oleLoanForm, result, request, response);
            ModelAndView modelAndView = addItem(oleLoanForm, result, request, response);
            oleLoanForm.setItemUuid(null);
            oleLoanForm.setInstanceUuid(null);
            return modelAndView;
        }
        if (oleLoanForm.getReturnItemUuid() != null && oleLoanForm.getReturnInstanceUuid() != null) {
            oleLoanForm.setReturnCheck(true);
            super.refresh(oleLoanForm, result, request, response);
            ModelAndView modelAndView = validateItem(oleLoanForm, result, request, response);
            oleLoanForm.setReturnItemUuid(null);
            oleLoanForm.setReturnInstanceUuid(null);
            return modelAndView;
        }
        return super.refresh(oleLoanForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=showOnHoldRequest")
    public ModelAndView showOnHoldRequest(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response){
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        String formKey = request.getParameter("formKey");
        OleLoanForm loanForm = (OleLoanForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        if (loanForm!=null) {
            List<OleDeliverRequestBo> oleDeliverRequestBoList = loanForm.getOnHoldRequestForPatron();
            List<OleDeliverRequestBo> populateItemRecords=new ArrayList<>();
            for(OleDeliverRequestBo deliverOnHoldRequestBo : oleDeliverRequestBoList) {
                populateItemRecords.add(oleDeliverRequestDocumentHelperService.
                        processItem(deliverOnHoldRequestBo));
            }
            oleLoanForm.setOnHoldRequestForPatron(populateItemRecords);
        }
        return getUIFModelAndView(oleLoanForm, "OnHoldRequestPage");
    }

    /**
     * This method displays information about a patron in UI.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=searchPatron")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the search patron method");
        Long b1 = System.currentTimeMillis();
        fastAddBarcode = "";
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setLoanList(new ArrayList<OleLoanDocument>(0));
        oleLoanForm.setBlockPatron(false);
        oleLoanForm.setExistingLoanList(new ArrayList<OleLoanDocument>(0));
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.getErrorsAndPermission().clear();
        oleLoanForm.setPatronbill(false);
        oleLoanForm.setShowExistingLoan(false);
        try {
            String maxSessionTime = oleLoanForm.getMaxTimeForCheckOutConstant();
            if (LOG.isInfoEnabled()) {
                LOG.info("session timeout:" + maxSessionTime);
            }
            if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase("")){
                oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));
            }
            // Modified as per comments in Jira OLE-4901
            if (!getLoanProcessor().isValidCirculationDesk()) {
                oleLoanForm.setLoanLoginUserInfo(GlobalVariables.getUserSession().getPrincipalName() + " " + OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
                return super.start(oleLoanForm, result, request, response);
            }
            OleCirculationDesk oleCirculationDesk = getLoanProcessor().validateCalanderForCirculationDesk(oleLoanForm.getCirculationDesk());
            OleLoanDocument oleProxyLoanDocument = null;
            List<OlePatronDocument> oleRealPatron = oleLoanForm.getRealPatronList();
            List<OlePatronDocument> oleCurrentPatronDocumentList = oleLoanForm.getCurrentPatronList();
            OlePatronDocument oleCurrentPatronDocument = new OlePatronDocument();
            if (oleCurrentPatronDocumentList != null && oleCurrentPatronDocumentList.size() > 0) {
                oleCurrentPatronDocument = oleCurrentPatronDocumentList.get(0);
                String barcode = oleLoanForm.getPatronBarcode();
                if (oleLoanForm.isProxyDisplay() && barcode != null && !barcode.equalsIgnoreCase("") && oleCurrentPatronDocument.getBarcode() != null && !barcode.equalsIgnoreCase(oleCurrentPatronDocument.getBarcode())) {
                    oleLoanForm.getCurrentPatronList().clear();
                    oleLoanForm.setRealPatronBarcode(null);
                    oleLoanForm.setPatronBarcode(barcode);
                    oleLoanForm.setRealPatronList(null);
                    oleLoanForm.setProxyDisplay(false);
                    oleLoanForm.setBackUpDummyLoan(null);
                    oleRealPatron.clear();
                }
            }
            if (oleRealPatron != null && oleRealPatron.size() > 0 && !oleCurrentPatronDocument.isSelfCheckOut()) {
                for (int realPatron = 0; realPatron < oleRealPatron.size(); realPatron++) {
                    OlePatronDocument olePatronDocument = oleRealPatron.get(realPatron);
                    if (olePatronDocument.isRealPatronCheck()) {
                        oleLoanForm.setRealPatronBarcode(olePatronDocument.getBarcode());
                        oleLoanForm.setRealPatronFlag(false);
                        oleProxyLoanDocument = getLoanProcessor().getLoanDocument(olePatronDocument.getBarcode(), null, oleLoanForm.isSelfCheckOut(), false);
                        break;
                    }
                }
            }
            boolean isSelfCheckout = oleCurrentPatronDocument.isSelfCheckOut();
            if (oleCurrentPatronDocument.isSelfCheckOut()) {
                oleLoanForm.setSelfCheckOut(false);
                oleLoanForm.setRealPatronFlag(false);
            }
            Long b2 = System.currentTimeMillis();
            Long b3 = b2-b1;
            LOG.info("The time taken for pre-loaning patron"+b3);
            OleLoanDocument oleLoanDocument = getLoanProcessor().getLoanDocument(oleLoanForm.getPatronBarcode(), oleLoanForm.getRealPatronBarcode(), isSelfCheckout, false);
            Long b4 = System.currentTimeMillis();
            if (oleLoanDocument.isLostPatron()) {
                oleLoanForm.setBlockUser(true);
            }else {
                oleLoanForm.setBlockUser(false);
            }
            List<OleDeliverRequestBo> oleDeliverRequestBoList = oleLoanDocument.getHoldRequestForPatron(oleLoanDocument.getOlePatron(), oleCirculationDesk);
            if(oleDeliverRequestBoList!=null && oleDeliverRequestBoList.size() > 0) {
                oleLoanForm.setOnHoldRequestMessage(oleDeliverRequestBoList.get(0).getOnHoldRequestForPatronMessage());
                oleLoanForm.setOnHoldRequestForPatron(oleDeliverRequestBoList);
            }
            if (oleLoanDocument.getOlePatron() != null) {
                List<OlePatronNotes> olePatronNotes = oleLoanDocument.getOlePatron().getNotes();
                if(CollectionUtils.isNotEmpty(olePatronNotes)) {
                    oleLoanForm = getLoanProcessor().getPatronNote(olePatronNotes, oleLoanForm);
                    if(CollectionUtils.isNotEmpty(oleLoanForm.getOlePatronNotes())) {
                        oleLoanForm.setPatronNoteFlag(true);
                    }
                }
            }
            boolean activeProxyValue = true;
            int proxyPatronCount = 0;
            int proxyDisplayCount = 0;
            if (oleLoanDocument.getRealPatron() != null && oleLoanDocument.getRealPatron().size() > 0) {
                List<OlePatronDocument> realPatronActiveListOld = new ArrayList<OlePatronDocument>();
                for (OlePatronDocument oleRealPatronDocument : oleLoanDocument.getRealPatron()) {
                    if (!realPatronActiveListOld.contains(oleRealPatronDocument)) {
                        realPatronActiveListOld.add(oleRealPatronDocument);
                    }
                }
                List<OlePatronDocument> realPatronActiveList = new ArrayList<OlePatronDocument>();
                for (OlePatronDocument oleRealPatronDocument : realPatronActiveListOld) {
                    List<OleProxyPatronDocument> proxyPatronActiveList = new ArrayList<OleProxyPatronDocument>();
                    for (OleProxyPatronDocument oleProxyPatronDocument : oleRealPatronDocument.getOleProxyPatronDocuments()) {
                        if (oleProxyPatronDocument.getProxyPatronId().equalsIgnoreCase(oleLoanDocument.getPatronId())) {
                            proxyPatronCount++;
                            if (oleProxyPatronDocument.getProxyPatronExpirationDate() == null || !oleProxyPatronDocument.getProxyPatronExpirationDate().before(new Timestamp(System.currentTimeMillis()))) {
                                proxyPatronActiveList.add(oleProxyPatronDocument);
                                proxyDisplayCount++;
                            }
                        }
                    }
                    if (proxyPatronActiveList.size() > 0) {
                        oleLoanForm.setProxyDisplay(true);
                        oleRealPatronDocument.setOleProxyPatronDocuments(proxyPatronActiveList);
                        oleRealPatronDocument.setOleProxyPatronDocumentList(proxyPatronActiveList);
                        realPatronActiveList.add(oleRealPatronDocument);
                    } else {
                        activeProxyValue = false;
                    }
                }
                if (realPatronActiveList.size() > 0) {
                    oleLoanForm.setRealPatronList(realPatronActiveList);
                    List<OlePatronDocument> currentPatronList = new ArrayList<>();
                    currentPatronList.add(oleLoanDocument.getOlePatron());
                    oleLoanForm.setCurrentPatronList(currentPatronList);
                    oleLoanForm.setRealPatronFlag(true);
                }
            }
            if (!activeProxyValue && proxyPatronCount != 0 && proxyDisplayCount != proxyPatronCount) {
                oleLoanDocument = getLoanProcessor().getLoanDocument(oleLoanForm.getPatronBarcode(), oleLoanForm.getRealPatronBarcode(), false, true);
            }
            oleLoanForm.setBorrowerCode(oleLoanDocument.getBorrowerTypeCode());
            oleLoanForm.setPatronId(oleLoanDocument.getPatronId());
            String patronNameURL = getLoanProcessor().patronNameURL(oleLoanForm.getOldPrincipalId(), oleLoanForm.getPatronId());
            oleLoanForm.setPatronNameURL(patronNameURL);
            oleLoanForm.setProxyPatronId(oleLoanDocument.getProxyPatronId());
            //oleLoanForm.setRealPatronName(oleLoanDocument.getRealPatronName());
            oleLoanForm.setRealPatronType(oleLoanDocument.getRealPatronType());
            oleLoanForm.setAddressVerified(oleLoanDocument.isAddressVerified());
            oleLoanForm.setBlockLoan(oleLoanDocument.isBlockLoan());
            oleLoanForm.setBorrowerTypeId(oleLoanDocument.getBorrowerTypeId());
            oleLoanForm.setInformation("");
            if (oleLoanDocument.getRealPatronBarcode() != null) {
                for (OlePatronDocument olePatronDocument : oleLoanForm.getRealPatronList()) {
                    if (olePatronDocument.isRealPatronCheck()) {
                        oleLoanForm.setRealPatronId(olePatronDocument.getOlePatronId());
                    }
                }
                if (oleLoanForm.getRealPatronId() != null && (oleLoanForm.getRealPatronId() != null && !oleLoanForm.getRealPatronId().equalsIgnoreCase(""))) {
                    oleLoanDocument.setRealPatronName(oleLoanForm.getRealPatronName());
                }
            }
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setBlockItem(false);
            oleLoanForm.setBlockPatron(false);
            oleLoanForm.setNonCirculatingFlag(false);
            oleLoanForm.setItem(null);
            oleLoanForm.setOleItem(null);
            oleLoanForm.getErrorsAndPermission().putAll(oleLoanDocument.getErrorsAndPermission());
            if (StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
                oleLoanForm.setSuccess(false);
                oleLoanForm.setInformation("");
                oleLoanForm.setMessage(oleLoanDocument.getErrorMessage());
                getLoanProcessor().setErrorFlagForPatron(oleLoanDocument, oleLoanForm);
                oleLoanForm.setPatronName(null);
                String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
                oleLoanForm.setAudioEnable(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
            }
            if (oleProxyLoanDocument != null) {
                oleLoanForm.setRealPatronName(oleLoanDocument.getPatronName());
                oleLoanDocument = oleProxyLoanDocument;
                patronNameURL = getLoanProcessor().patronNameURL(oleLoanForm.getOldPrincipalId(), oleLoanForm.getProxyPatronId());
                oleLoanForm.setPatronNameURL(patronNameURL);
            } else {
                oleLoanForm.setRealPatronName(null);
            }
            oleLoanForm.setPatronName(oleLoanDocument.getPatronName());
            oleLoanForm.setBorrowerType(oleLoanDocument.getBorrowerTypeName());
            oleLoanForm.setPreferredAddress(oleLoanDocument.getPreferredAddress());
            oleLoanForm.setEmail(oleLoanDocument.getEmail());
            oleLoanForm.setPhoneNumber(oleLoanDocument.getPhoneNumber());
            oleLoanForm.setItemFocus(true);
            oleLoanForm.setPatronFocus(false);
            oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
            if (oleLoanForm.getProxyPatronId() != null) {
                String proxyPatronId = oleLoanForm.getProxyPatronId();
                oleLoanDocument.setProxyPatronId(oleLoanForm.getPatronId());
                oleLoanDocument.setPatronId(proxyPatronId);
                oleLoanForm.setProxyPatronId(oleLoanDocument.getProxyPatronId());
                oleLoanForm.setPatronId(oleLoanDocument.getPatronId());
            }
           /* oleLoanForm.setExistingLoanList(oleLoanForm.getDummyLoan().getOlePatron().getOleLoanDocuments());*/
            Long b5 = System.currentTimeMillis();
            Long total = b5 - b4;
            LOG.info("The time taken for post patron search:"+total);
        } catch (Exception e) {
            oleLoanForm.setInformation(e.getMessage());
            LOG.error("Exception while search patron time", e);
        }

        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * This method creates new loan for a patron and also renew the existing item.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addItem")
    public ModelAndView addItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the add item method");
        Long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setBlockItem(false);
        if(oleLoanForm.getItem().equals("")){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.EMPTY_ITEM_BARCODE);
            return getUIFModelAndView(oleLoanForm);
        }
        try {
            oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
            oleLoanForm.setInformation("");
            oleLoanForm.setSuccessInfo("");
            oleLoanForm.setReturnInformation("");
            // Modified as per comments in Jira OLE-4901
            if (!getLoanProcessor().isValidCirculationDesk()) {
                oleLoanForm.setLoanLoginUserInfo(GlobalVariables.getUserSession().getPrincipalName() + " " + OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
                return super.start(oleLoanForm, result, request, response);
            }
            boolean renewalFlag = false;
            List<OleLoanDocument> existItemList = new ArrayList<OleLoanDocument>();
            if (oleLoanForm.getExistingLoanList() != null && !oleLoanForm.getExistingLoanList().isEmpty())
                existItemList.addAll(oleLoanForm.getExistingLoanList());
            if (oleLoanForm.getLoanList() != null && !oleLoanForm.getLoanList().isEmpty())
                existItemList.addAll(oleLoanForm.getLoanList());
            String item = oleLoanForm.getItem();
              for (OleLoanDocument oleLoanDocument : existItemList) {
                if (oleLoanDocument.getItemId() != null && oleLoanDocument.getItemId().equals(item)) {
                    oleLoanForm.setRenewalFlag(true);
                    oleLoanForm.setBlockItem(true);
                    oleLoanForm.setBlockPatron(true);
                    oleLoanForm.setSuccess(false);
                    oleLoanForm.setMessage(OLEConstants.RENEWAL_ITM_POPUP);
                    renewalFlag = true;
                    break;
                }
              }
            if(!renewalFlag){
                OleLoanDocument loanDocument = getLoanProcessor().retrieveByPatronAndItem(oleLoanForm.getPatronId(),item);
                if (loanDocument!=null && loanDocument.getItemId() != null && loanDocument.getItemId().equals(item)) {
                    oleLoanForm.setRenewalLoan(loanDocument);
                    oleLoanForm.setRenewalFlag(true);
                    oleLoanForm.setBlockItem(true);
                    oleLoanForm.setBlockPatron(true);
                    oleLoanForm.setSuccess(false);
                    oleLoanForm.setMessage(OLEConstants.RENEWAL_ITM_POPUP);
                    renewalFlag = true;
                }
            }

            if (!renewalFlag) {
                try {
                    OleLoanDocument oleLoanDocument = new OleLoanDocument();

                    String ipAddress = request.getHeader("X-FORWARDED-FOR");
                    if (ipAddress == null) {
                        ipAddress = request.getRemoteAddr();
                    }
                    oleLoanDocument.setMachineId(ipAddress);
                    oleLoanDocument.setItemUuid(oleLoanForm.getItemUuid());
                    oleLoanForm.setAddressVerified(false);
                    oleLoanForm.getErrorsAndPermission().clear();
                    if (!getLoanProcessor().isClaimsReturnedItem(oleLoanForm.getItem(), oleLoanDocument)) {
                        oleLoanForm = getLoanProcessor().processLoan(oleLoanForm, oleLoanDocument);
                        oleLoanForm.setClaimsFlag(false);
                    } else {
                        oleLoanForm.setMessage("claims Returned Item");
                        oleLoanForm.setSuccess(false);
                        oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
                        oleLoanForm.setClaimsFlag(true);
                        oleLoanForm.setRecordNote(false);
                    }
                } catch (Exception e) {
                    LOG.error("Exception", e);
                    oleLoanForm.setInformation(e.getMessage());
                }
            }
            //oleLoanForm.setPatronbill(false);
            oleLoanForm.setPatronbill(getLoanProcessor().checkPatronBill(oleLoanForm.getPatronId()));
            Long end = System.currentTimeMillis();
            Long timeTaken = end - begin;
            LOG.info("-----------TimeTaken to complete one loan-----------"+timeTaken);
        } catch (Exception e) {
            LOG.error("Exception while adding an item", e);
        }
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * This method  creates loan for a patron who is not able to borrow.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=loan")
    public ModelAndView loanPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        Long begin = System.currentTimeMillis();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        LOG.debug("Inside the loan patron method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        StringBuffer buffer = new StringBuffer();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        //String newPrincipalId = oleLoanForm.getNewPrincipalId();
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        OleLoanDocument oleLoanDocumentChk = null;
        ModelAndView overrideModelView = null;
        if (StringUtils.isBlank(oleLoanForm.getItem())) {
            try {
                String principalId = GlobalVariables.getUserSession().getPrincipalId();
                Boolean overRideFlag = getLoanProcessor().checkOverRidePermission(principalId, oleLoanForm);
                if (!overRideFlag) {
                    oleLoanDocumentChk = getLoanProcessor().getLoanDocument(oleLoanForm.getPatronBarcode(), oleLoanForm.getRealPatronBarcode(), oleLoanForm.isSelfCheckOut(), false);
                    if (oleLoanDocumentChk.getErrorMessage() != null) {
                        if (!oleLoanForm.getLoanLoginName().equalsIgnoreCase("") || !oleLoanForm.getLoanLoginName().isEmpty()) {
                            buffer.append(oleLoanForm.getLoanLoginName() + "," + oleLoanForm.getCirculationDesk());
                            getLoanProcessor().getLoanUserList(loginUserList, buffer);
                        } else {
                            if (!oleLoanForm.getOldPrincipalId().equalsIgnoreCase("") || !oleLoanForm.getOldPrincipalId().isEmpty()) {
                                buffer.append(oleLoanForm.getOldPrincipalId() + "," + oleLoanForm.getCirculationDesk());
                                getLoanProcessor().getLoanUserList(loginUserList, buffer);
                            }
                        }
                        overrideModelView = this.overRide(form, result, request, response);
                    }
                }
            } catch (Exception e) {
                LOG.error("Check for Address Verified and Block Failed." + e.getMessage(), e);
            }
        } else {
            if (!oleLoanForm.getLoanLoginName().equalsIgnoreCase("") || !oleLoanForm.getLoanLoginName().isEmpty()) {
                buffer.append(oleLoanForm.getLoanLoginName() + "," + oleLoanForm.getCirculationDesk());
                getLoanProcessor().getLoanUserList(loginUserList, buffer);
            } else {
                if (!oleLoanForm.getOldPrincipalId().equalsIgnoreCase("") || !oleLoanForm.getOldPrincipalId().isEmpty()) {
                    buffer.append(oleLoanForm.getOldPrincipalId() + "," + oleLoanForm.getCirculationDesk());
                    getLoanProcessor().getLoanUserList(loginUserList, buffer);
                }
            }
            overrideModelView = this.overRide(form, result, request, response);
        }
        if (overrideModelView == null) {
            try {
                List<OleLoanDocument> existingItemList = new ArrayList<OleLoanDocument>();
                OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
                Timestamp checkinDate = new Timestamp(System.currentTimeMillis());
                if (oleLoanDocument != null) {
                    if (oleLoanDocument.getItemLoanStatus() != null && oleLoanDocument.getItemLoanStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_CHECKEDOUT)) {
                        oleLoanForm.setCheckInItem(oleLoanForm.getItem());
                        oleLoanForm.setBackGroundCheckIn(true);
                        //   validateItem(oleLoanForm,result,request,response);
                        oleLoanForm.setBackUpDummyLoan(oleLoanDocument);
                        oleLoanDocument = getLoanProcessor().getOleLoanDocumentUsingItemBarcode(oleLoanForm.getCheckInItem());
                        oleLoanDocument.setCheckInDate(checkinDate);
                        oleLoanForm.getErrorsAndPermission().clear();
                        oleLoanDocument = getLoanProcessor().returnLoan(oleLoanForm.getCheckInItem(), oleLoanDocument);
                        oleLoanForm.setDummyLoan(oleLoanDocument);
                        if (StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
                            oleLoanForm.setSuccess(true);
                            oleLoanForm.setMessage(null);
                            oleLoanForm.setReturnSuccess(false);
                            oleLoanForm.setReturnMessage(oleLoanDocument.getErrorMessage());
                            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                        }
                        if (oleLoanDocument.isCopyRequest()) {
                            oleLoanForm.setSuccess(true);
                            oleLoanForm.setMessage(null);
                            oleLoanForm.setCopyRequest(true);
                            oleLoanForm.setReturnSuccess(false);
                            oleLoanForm.setReturnMessage(OLEConstants.COPY_REQUEST_FULFILL);
                            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                        }
                        if (oleLoanDocument.isNumberOfPieces() || (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getNumberOfPieces() != null && !oleLoanDocument.getOleItem().getNumberOfPieces().equalsIgnoreCase(""))) {
                            oleLoanForm.setSuccess(true);
                            oleLoanForm.setMessage(null);
                            oleLoanForm.setNumberOfPieces(true);
                            oleLoanForm.setReturnSuccess(false);
                            oleLoanDocument.setContinueCheckIn(true);
                            oleLoanDocument.setBackgroundCheckInMissingPiece(true);
                            if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getNumberOfPieces() != null) {
                                if (oleLoanDocument.getOleItem().getMissingPieceFlagNote() != null) {
                                    oleLoanDocument.setDescription(oleLoanDocument.getOleItem().getMissingPieceFlagNote());
                                } else {
                                    oleLoanDocument.setDescription("");
                                }
                            }
                            if (oleLoanDocument.getItemNumberOfPieces() != null) {
                                oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED
                                        + OLEConstants.BREAK + "Total No of Pieces :      " + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.BREAK + "No of missing Pieces : " + (oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));
                            } else if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getNumberOfPieces() != null) {
                                oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getOleItem().getNumberOfPieces() + OLEConstants.PIECES_RETURNED
                                        + OLEConstants.BREAK + "Total No of Pieces :      " + oleLoanDocument.getOleItem().getNumberOfPieces() + OLEConstants.BREAK + "No of missing Pieces : " + (oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));
                            }
                            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                        }
                        OleLoanForm oleReturnLoanForm = (OleLoanForm) oleLoanForm;
                        if (oleReturnLoanForm.getDummyLoan() != null) {
                            oleLoanDocument.setItemLoanStatus(oleReturnLoanForm.getDummyLoan().getItemStatus());
                            oleLoanForm.getDummyLoan().setItemLoanStatus(oleReturnLoanForm.getDummyLoan().getItemStatus());
                        }
                    } else if (oleLoanForm.getBackUpDummyLoan() != null) {
                        oleLoanDocument = oleLoanForm.getBackUpDummyLoan();
                        oleLoanDocument.setBackgroundCheckInMissingPiece(oleLoanForm.getDummyLoan().isBackgroundCheckInMissingPiece());
                        if (oleLoanForm.getDummyLoan() != null) {
                            oleLoanDocument.setItemLoanStatus(oleLoanForm.getDummyLoan().getItemStatus());
                            oleLoanDocument.setItemDamagedStatus(oleLoanForm.getDummyLoan().isItemDamagedStatus());
                            oleLoanDocument.setItemDamagedNote(oleLoanForm.getDummyLoan().getItemDamagedNote());
                            oleLoanDocument.getOleItem().setMissingPieceFlagNote(oleLoanForm.getMissingPieceMessage());
                            if (oleLoanDocument.getOleItem() != null) {
                                oleLoanDocument.getOleItem().setMissingPiecesCount(oleLoanForm.getDummyLoan().getMissingPiecesCount());
                                oleLoanDocument.getOleItem().setMissingPieceFlag(oleLoanForm.getDummyLoan().isMissingPieceFlag());
                            }
                        }
                        oleLoanForm.setBackUpDummyLoan(null);
                    }
                    boolean indefinite = false;
                    if (oleLoanForm.getItem() != null && !oleLoanForm.getItem().isEmpty() && oleLoanForm.getDueDateMap() == null && oleLoanDocument.getExpirationDate() == null) {
                        indefinite = true;
                    }
                    if ((oleLoanForm.getItem() != null && !oleLoanForm.getItem().isEmpty()) || (oleLoanForm.getOleItem() != null && !oleLoanForm.getItem().isEmpty() && !oleLoanForm.getOleItem().getItemIdentifier().isEmpty()) || indefinite) {
                        if (oleLoanForm.getDueDateMap() != null) {
                            Timestamp timestamp;
                            Pattern pattern;
                            Matcher matcher;
                            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);
                            boolean timeFlag = false;
                            if (oleLoanForm.getPopDateTime() != null && !oleLoanForm.getPopDateTime().isEmpty()) {
                                String[] str = oleLoanForm.getPopDateTime().split(":");
                                pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
                                matcher = pattern.matcher(oleLoanForm.getPopDateTime());
                                timeFlag = matcher.matches();
                                if (timeFlag) {
                                    if (str != null && str.length <= 2) {
                                        oleLoanForm.setPopDateTime(oleLoanForm.getPopDateTime() + OLEConstants.CHECK_IN_TIME_MS);
                                    }
                                    timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(oleLoanForm.getPopDateTime()));
                                } else {
                                    oleLoanForm.setPopDateTimeInfo(OLEConstants.DUE_DATE_TIME_FORMAT_MESSAGE);
                                     /*return getUIFModelAndView(oleLoanForm,"PatronItemViewPage");*/
                                    return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                                }
                            } else if (fmt.format(oleLoanForm.getDueDateMap()).compareTo(fmt.format(new Date())) == 0) {
                                timestamp = new Timestamp(new Date().getTime());
                            } else {
                                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
                            }
                            oleLoanDocument.setLoanDueDate(timestamp);
                        }
                        if (oleLoanDocument.getItemLoanStatus() != null && !oleLoanDocument.getItemLoanStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_CHECKEDOUT) || oleLoanDocument.isBackgroundCheckInMissingPiece()) {
                            getLoanProcessor().saveLoan(oleLoanForm.getBackUpDummyLoan() != null ? oleLoanForm.getBackUpDummyLoan() : oleLoanDocument);
                            if (oleLoanForm.getBackUpDummyLoan() != null) {
                                oleLoanDocument = oleLoanForm.getBackUpDummyLoan();
                                oleLoanDocument.setBackgroundCheckInMissingPiece(oleLoanForm.getDummyLoan().isBackgroundCheckInMissingPiece());
                                if (oleLoanForm.getDummyLoan() != null) {
                                    oleLoanDocument.setItemLoanStatus(oleLoanForm.getDummyLoan().getItemStatus());
                                    oleLoanDocument.setItemDamagedStatus(oleLoanForm.getDummyLoan().isItemDamagedStatus());
                                    oleLoanDocument.setItemDamagedNote(oleLoanForm.getDummyLoan().getItemDamagedNote());
                                    oleLoanDocument.getOleItem().setMissingPieceFlagNote(oleLoanForm.getMissingPieceMessage());
                                    if (oleLoanDocument.getOleItem() != null) {
                                        oleLoanDocument.getOleItem().setMissingPiecesCount(oleLoanForm.getDummyLoan().getMissingPiecesCount());
                                        oleLoanDocument.getOleItem().setMissingPieceFlag(oleLoanForm.getDummyLoan().isMissingPieceFlag());
                                    }
                                }
                                oleLoanForm.setBackUpDummyLoan(null);
                            }
                            oleLoanForm.setSuccessMessage(oleLoanDocument.getSuccessMessage());
                            if (!oleLoanForm.isCheckOut())
                                existingItemList.add(oleLoanDocument);
                            oleLoanDocument.setBackgroundCheckInMissingPiece(false);
                        }

                        if (oleLoanForm.getLoanList() != null && !oleLoanForm.getLoanList().isEmpty()) {
                            existingItemList.addAll(oleLoanForm.getLoanList());
                        }
                        oleLoanForm.setLoanList(existingItemList);
                    }
                    if (StringUtils.isNotEmpty(oleLoanDocument.getItemUuid())) {
                        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleLoanDocument.getItemUuid());
                        String itemXmlContent = item.getContent();
                        // String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                        Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                        if(StringUtils.isBlank(oleLoanDocument.getLocation())){
                           getLoanProcessor().getLocation(oleItem, oleLoanDocument, item);
                        }
                        if (oleLoanDocument.getLocation() == null || oleLoanDocument.getLocation().isEmpty()) {
                            getLoanProcessor().getDefaultHoldingLocation(oleLoanDocument);
                        }
                        if (oleLoanDocument.getCheckInDate() == null) {
                            oleLoanDocument.setCheckInDate(checkinDate);
                        }
                        if (oleLoanDocument.getOleCirculationDesk() == null) {
                            oleLoanDocument.setCirculationLocationId(oleLoanForm.getCirculationDesk());
                        }
                        if (oleItem.isMissingPieceFlag()) {
                            oleLoanDocument.setMissingPieceNote(oleItem.getMissingPieceFlagNote());
                            oleLoanDocument.setMissingPieceFlag(oleItem.isMissingPieceFlag());
                        }
                        if (oleItem.isItemDamagedStatus()) {
                            oleLoanDocument.setItemDamagedNote(oleItem.getDamagedItemNote());
                        }
                        if (oleLoanForm.getPatronName() == null) {
                            oleLoanForm.setPatronName(oleLoanDocument.getPatronName());
                        }
                        if (oleItem.isClaimsReturnedFlag()) {
                            if (oleItem.getClaimsReturnedFlagCreateDate() != null)
                                oleLoanDocument.setClaimsReturnedDate(new Timestamp(df.parse(oleItem.getClaimsReturnedFlagCreateDate()).getTime()));
                            oleLoanDocument.setClaimsReturnedIndicator(true);
                            oleLoanDocument.setClaimsReturnNote(oleItem.getClaimsReturnedNote());
                        }
                        if (oleLoanForm.getPatronName() == null) {
                            oleLoanForm.setPatronName(oleLoanDocument.getPatronName());
                        }
                        if (oleLoanForm.isCheckOut()) {
                            if (!oleLoanDocument.getItemLoanStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_CHECKEDOUT))
                                getLoanProcessor().saveLoan(oleLoanForm.getBackUpDummyLoan() != null ? oleLoanForm.getBackUpDummyLoan() : oleLoanDocument);
                            oleLoanForm.setSuccessMessage(oleLoanDocument.getSuccessMessage());
                            List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
                            if (oleLoanDocument.getOleItem().getItemStatus() != null) {
                                oleLoanDocument.setItemStatusCode(oleLoanDocument.getOleItem().getItemStatus().getCodeValue());
                            } else {
                                oleLoanDocument.setItemStatusCode(oleLoanDocument.getItemLoanStatus());
                            }
                            OleItemAvailableStatus oleItemAvailableStatus = getLoanProcessor().validateAndGetItemStatus(oleLoanDocument.getItemStatusCode());
                            oleLoanDocument.setItemStatus(oleItemAvailableStatus != null ? oleItemAvailableStatus.getItemAvailableStatusName() : null);
                            oleLoanDocument.setDescription(oleLoanForm.getDescription());
                            oleLoanDocuments.add(oleLoanDocument);
                            if (oleLoanForm.getItemReturnList() != null) {
                                oleLoanDocuments.addAll(oleLoanForm.getItemReturnList());
                            }
                            oleLoanForm.setItemReturnList(oleLoanDocuments);
                            if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
                                if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                                    OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                                    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
                                    Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                                    if (oleNoticeBo != null) {
                                        oleNoticeBo.setCheckInDate(dateFormat.format(date));
                                        String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                                        OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                                        String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                                        if (replyToEmail!=null){
                                            oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                                        }else {
                                            String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                                fromAddress = OLEConstants.KUALI_MAIL;
                                            }
                                            oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                                        }
                                        if (LOG.isInfoEnabled()) {
                                            LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                                        }
                                    }
                                } else {
                                    if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                                        oleLoanForm.setBillAvailability(false);
                                    } else {
                                        oleLoanForm.setBillAvailability(true);
                                    }
                                }
                            }
                            if (oleLoanDocument.isCheckOut()) {
                                oleLoanForm.setDueDateSlip(true);
                                oleLoanForm.setBillAvailability(false);
                            }
                            oleLoanForm.setReturnSuccess(true);
                            oleLoanForm.setCheckOut(false);
                        }
                        oleLoanForm.setSuccess(true);
                        oleLoanForm.setMessage(null);
                        oleLoanForm.setItem("");
                        oleLoanForm.setInformation("");
                        oleLoanForm.setPopDateTimeInfo("");
                        oleLoanForm.setAddressVerified(false);
                        if (oleLoanDocument.isClaimsReturnedIndicator()) {
                            saveGeneralNoteForFlaggedItem(OLEConstants.CLAIMS_CHECKED_OUT_FLAG, true, oleLoanDocument, true, false, oleLoanForm.isBackGroundCheckIn(), oleLoanForm.getPatronBarcode());
                        }
                    }
                    if (oleLoanDocument.isMissingPieceFlag() || oleLoanDocument.isItemDamagedStatus()) {
                        flaggedNoteSave(form, result, request, response);
                    }
                }
            } catch (Exception e) {
                oleLoanForm.setInformation(e.getMessage());
                LOG.error("Exception", e);
            }
        }
        /*if (StringUtils.isNotEmpty(newPrincipalId)) {
            if (getLoanProcessor().isValidCirculationDesk()) {
                if (getLoanProcessor().getCircDeskId() != null) {
                    //oleLoanForm.setCirculationDesk(getLoanProcessor().getCircDeskId());
                }
            }
        }*/
        oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
        String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
        oleLoanForm.setAudioEnable(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
        oleLoanForm.setItemUuid(null);
        oleLoanForm.setInstanceUuid(null);
        if (oleLoanForm.getDummyLoan() != null && oleLoanForm.getDummyLoan().getOlePatron() != null && oleLoanForm.getDummyLoan().getErrorMessage() != null) {
            oleLoanForm.setMessage(null);
            oleLoanForm.setBlockPatron(false);
            oleLoanForm.setBlockItem(false);
            oleLoanForm.setSuccess(true);
        }
        Long end = System.currentTimeMillis();
        Long timeTaken = end - begin;
        LOG.info("-----------TimeTaken to complete override loan-----------"+timeTaken);
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method doesn't allow a patron to be loaned.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=noLoan")
    public ModelAndView doNotLoanPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the do not loan patron method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        if (oleLoanForm.isCheckOut()) {
            List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
            if (oleLoanDocument.getOleItem().getItemStatus() != null) {
                oleLoanDocument.setItemStatusCode(oleLoanDocument.getOleItem().getItemStatus().getCodeValue());
            } else {
                oleLoanDocument.setItemStatusCode(oleLoanDocument.getItemLoanStatus());
            }
            OleItemAvailableStatus oleItemAvailableStatus = getLoanProcessor().validateAndGetItemStatus(oleLoanDocument.getItemStatusCode());
            oleLoanDocument.setItemStatus(oleItemAvailableStatus != null ? oleItemAvailableStatus.getItemAvailableStatusName() : null);
            oleLoanDocuments.add(oleLoanDocument);
            if (oleLoanForm.getItemReturnList() != null) {
                oleLoanDocuments.addAll(oleLoanForm.getItemReturnList());
            }
            if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
                if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                    OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                    //SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
                    Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                    if (oleNoticeBo != null) {
                        oleNoticeBo.setCheckInDate(dateFormat.format(date));
                        String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                        OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                        String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                        if (replyToEmail != null) {
                            oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                        } else {
                            String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                fromAddress = OLEConstants.KUALI_MAIL;
                            }
                            oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                        }
                        if (LOG.isInfoEnabled()) {
                            LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                        }
                    }
                } else {
                    if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                        oleLoanForm.setBillAvailability(false);
                    } else {
                        oleLoanForm.setBillAvailability(true);
                    }
                }
            }
            oleLoanForm.setItemReturnList(oleLoanDocuments);
            oleLoanForm.setReturnSuccess(true);
            oleLoanForm.setCheckOut(false);
            String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
            oleLoanForm.setAudioEnable(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
        }
        if (oleLoanForm.getItem() == null || "".equals(oleLoanForm.getItem())) {
            clearPatronScreen(oleLoanForm, result, request, response);
        }
        oleLoanForm.setAddressVerified(false);
        oleLoanForm.setItem("");
        oleLoanForm.setInformation("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setDueDateSlip(false);
        oleLoanForm.setMessage(null);
        oleLoanForm.setSuccess(true);
        oleLoanForm.setItemUuid(null);
        oleLoanForm.setInstanceUuid(null);
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * Display the Fast-Add item dialog.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=openFastAdd")
    public ModelAndView openFastAdd(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the openFastAdd method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setFastAddItemIndicator(true);
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base") + "/ole-kr-krad/fastAddController?viewId=FastAddItemView&methodToCall=start";
        oleLoanForm.setFastAddUrl(url);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }


    /**
     * Delete the patron user note.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=deletePatronUserNote")
    public ModelAndView deletePatronUserNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the delete patron user note method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        try {
            List<OlePatronNotes> olePatronNotesList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(oleLoanForm.getOlePatronNotes())) {
                for(OlePatronNotes olePatronNotes : oleLoanForm.getOlePatronNotes()) {
                    if(olePatronNotes.isSelected()) {
                        olePatronNotesList.add(olePatronNotes);
                    }
                }
                if(CollectionUtils.isNotEmpty(olePatronNotesList)) {
                    oleLoanForm.setPatronNoteFlag(false);
                    KRADServiceLocator.getBusinessObjectService().delete(olePatronNotesList);
                }
            }
        } catch (Exception e) {
            oleLoanForm.setInformation(e.getMessage());
            LOG.error("Exception while deleting patron user note", e);
        }
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * Display the Alter due date dialog.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=editDueDate")
    public ModelAndView editDueDate(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the edit due date method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        StringBuffer buffer = new StringBuffer();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        List<OleLoanDocument> alterDueDateList = new ArrayList<OleLoanDocument>();
        alterDueDateList = getLoanProcessor().setListValues(oleLoanForm.getLoanList(), oleLoanForm.getExistingLoanList(), false, null, false);

        if (alterDueDateList.size() != 0) {
            oleLoanForm.setAlterDueDateList(alterDueDateList);
            oleLoanForm.setInformation("");
            if (!oleLoanForm.getLoanLoginName().equalsIgnoreCase("") || !oleLoanForm.getLoanLoginName().isEmpty()) {
                buffer.append(oleLoanForm.getLoanLoginName() + "," + oleLoanForm.getCirculationDesk());
                getLoanProcessor().getLoanUserList(loginUserList, buffer);
            } else {
                if (!oleLoanForm.getOldPrincipalId().equalsIgnoreCase("") || !oleLoanForm.getOldPrincipalId().isEmpty()) {
                    buffer.append(oleLoanForm.getOldPrincipalId() + "," + oleLoanForm.getCirculationDesk());
                    getLoanProcessor().getLoanUserList(loginUserList, buffer);
                }
            }
            ModelAndView overrideModelView = this.overRide(form, result, request, response);
           // if (getLoanProcessor().isValidCirculationDesk()) {
                // if(loanProcessor.getCircDeskId()!=null){
                //oleLoanForm.setCirculationDesk(loanProcessor.getCircDeskId());
                // }
           // }
            if (overrideModelView == null){
                oleLoanForm.setAlterDueDateFlag(true);
            }
        } else{
            oleLoanForm.setInformation(OLEConstants.ALTER_DUE_DATE_ERR_INFO);
        }

        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * Update the modified due date in loan.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=updateDueDate")
    public ModelAndView updateDueDate(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the update due date method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        boolean timeFlag = true;
        try {
            oleLoanForm.setAlterDueDateTimeInfo("");
                timeFlag = getLoanProcessor().updateLoan(oleLoanForm.getAlterDueDateList(), oleLoanForm.getPatronId(), false, false, oleLoanForm.getBorrowerCode());
            if (!timeFlag) {
                oleLoanForm.setAlterDueDateTimeInfo(OLEConstants.ALTER_DUE_DATE_TIME_FORMAT_MESSAGE);
                return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
            }
            // getLoanProcessor().updateItem(oleLoanForm.getAlterDueDateList(), false, false);
        } catch (Exception e) {
            oleLoanForm.setInformation(e.getMessage());
            LOG.error("Exception while updating due date", e);
        }
        oleLoanForm.setAlterDueDateFlag(false);
        List<OleLoanDocument> sortedExistingLoanList = oleLoanForm.getExistingLoanList();
        List<OleLoanDocument> LoanDocumentList = new ArrayList<>();
        List<OleLoanDocument> indefiniteLoanDocumentList = new ArrayList<>();
        for (OleLoanDocument loanDoc : sortedExistingLoanList) {
            if (loanDoc.getLoanDueDate() != null && !(loanDoc.getLoanDueDate().toString().isEmpty())) {
                LoanDocumentList.add(loanDoc);
            } else {
                indefiniteLoanDocumentList.add(loanDoc);
            }

        }
        Collections.sort(LoanDocumentList, new Comparator<OleLoanDocument>() {
            public int compare(OleLoanDocument o1, OleLoanDocument o2) {
                return o1.getLoanDueDate().compareTo(o2.getLoanDueDate());
            }
        });
        LoanDocumentList.addAll(indefiniteLoanDocumentList);
        oleLoanForm.setExistingLoanList(LoanDocumentList);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * Close the Alter due date dialog.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=closeAlterDueDate")
    public ModelAndView closeAlterDueDate(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the close Alter due date method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        /*String maxSessionTime = oleLoanForm.getMaxTimeForCheckOutConstant();
        if (LOG.isInfoEnabled()){
            LOG.info("session timeout" + maxSessionTime);
        }
        if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
            oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));*/
        List<OleLoanDocument> resetAlterDueDate = oleLoanForm.getAlterDueDateList();
        if (resetAlterDueDate != null) {
            for (int restDueDate = 0; restDueDate < resetAlterDueDate.size(); restDueDate++) {
                OleLoanDocument oleLoanDocument = (OleLoanDocument) resetAlterDueDate.get(restDueDate);
                if(oleLoanDocument!=null){
                    oleLoanDocument.setLoanDueDate(new Timestamp(oleLoanDocument.getPastDueDate().getTime()));
                    oleLoanDocument.setPastDueDate(null);
                }
            }
        }

        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * Display the claims return dialog.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=claimsReturn")
    public ModelAndView claimsReturn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the claims return method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        StringBuffer buffer = new StringBuffer();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setRemoveClaimsReturnFlag(false);
        boolean checkedItemsFlag = false;
        if (oleLoanForm.getLoanList().size() > 0) {
            for (int currentLoan = 0; currentLoan < oleLoanForm.getLoanList().size(); currentLoan++) {
                OleLoanDocument oleLoanDocument = (OleLoanDocument) oleLoanForm.getLoanList().get(currentLoan);
                if (oleLoanDocument.isCheckNo()) {
                    try {
                        String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                        Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                        oleLoanForm.setClaimsReturnNote(oleItem.getClaimsReturnedNote());
                        oleLoanForm.setItemClaimsReturnFlag(oleItem.isClaimsReturnedFlag());
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                    checkedItemsFlag = true;
                    break;
                }
            }
        }
        if (oleLoanForm.getExistingLoanList().size() > 0) {
            for (int currentLoan = 0; currentLoan < oleLoanForm.getExistingLoanList().size(); currentLoan++) {
                OleLoanDocument oleLoanDocument = (OleLoanDocument) oleLoanForm.getExistingLoanList().get(currentLoan);
                if (oleLoanDocument.isCheckNo()) {
                    try {
                        String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                        Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                        oleLoanForm.setItemClaimsReturnFlag(oleItem.isClaimsReturnedFlag());
                        oleLoanForm.setClaimsReturnNote(oleItem.getClaimsReturnedNote());
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                    checkedItemsFlag = true;
                    break;
                }
            }
        }
        if (checkedItemsFlag) {
            oleLoanForm.setInformation("");
            if (!oleLoanForm.getLoanLoginName().equalsIgnoreCase("") || !oleLoanForm.getLoanLoginName().isEmpty()) {
                buffer.append(oleLoanForm.getLoanLoginName() + "," + oleLoanForm.getCirculationDesk());
                getLoanProcessor().getLoanUserList(loginUserList, buffer);
            } else {
                if (!oleLoanForm.getOldPrincipalId().equalsIgnoreCase("") || !oleLoanForm.getOldPrincipalId().isEmpty()) {
                    buffer.append(oleLoanForm.getOldPrincipalId() + "," + oleLoanForm.getCirculationDesk());
                    getLoanProcessor().getLoanUserList(loginUserList, buffer);
                }
            }
            ModelAndView overrideModelView = this.overRide(form, result, request, response);
           // if (getLoanProcessor().isValidCirculationDesk()) {
                // if(loanProcessor.getCircDeskId()!=null){
                // oleLoanForm.setCirculationDesk(loanProcessor.getCircDeskId());
                //  }
           // }
            if (overrideModelView == null)
                oleLoanForm.setClaimsReturnFlag(true);
        } else
            oleLoanForm.setInformation(OLEConstants.CLAIMS_ITM_ERR_INFO);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * Save the claims return note in loan.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=applyClaimsReturn")
    public ModelAndView applyClaimsReturn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the apply claims return method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        List<OleLoanDocument> claimsList = new ArrayList<OleLoanDocument>();
        claimsList = getLoanProcessor().setListValues(oleLoanForm.getLoanList(), oleLoanForm.getExistingLoanList(), oleLoanForm.isClaimsReturnFlag(), oleLoanForm.getClaimsReturnNote(), oleLoanForm.isRemoveClaimsReturnFlag());
        try {
            getLoanProcessor().updateLoan(claimsList, oleLoanForm.getPatronId(), true, oleLoanForm.isRemoveClaimsReturnFlag(),oleLoanForm.getBorrowerCode());
            //   getLoanProcessor().updateItem(claimsList, true, oleLoanForm.isRemoveClaimsReturnFlag());
        } catch (Exception e) {
            LOG.error("Exception while setting claims return", e);
            oleLoanForm.setInformation(e.getMessage());
        }
        oleLoanForm.setClaimsReturnFlag(false);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }


    @RequestMapping(params = "methodToCall=showMissingPieceDialogBox")
    public ModelAndView showMissingPieceDialogBox(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        int count = 0;
        oleLoanForm.setDialogFlag(false);
        oleLoanForm.setDialogItemNoOfPieces(null);
        oleLoanForm.setDialogMissingPieceCount(null);
        oleLoanForm.setDialogText(null);
        oleLoanForm.setDialogItemNoOfPiecesReadOnly(false);
        oleLoanForm.setDialogMissingPieceCountReadOnly(false);
        oleLoanForm.setDialogErrorMessage(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        OleLoanDocument loanDocument = new OleLoanDocument();
        if (oleLoanForm.getLoanList().size() > 0) {
            for (OleLoanDocument oleLoanDocument : oleLoanForm.getLoanList()) {
                if (oleLoanDocument.isCheckNo()) {
                    loanDocument = oleLoanDocument;
                    count++;
                }
            }
        }
        if (oleLoanForm.getExistingLoanList().size() > 0) {
            for (OleLoanDocument oleLoanDocument : oleLoanForm.getExistingLoanList()) {
                if (oleLoanDocument.isCheckNo()) {
                    loanDocument = oleLoanDocument;
                    count++;
                }
            }
        }
        if (count == 1) {
            try {
                String itemXmlContent = getLoanProcessor().getItemXML(loanDocument.getItemUuid());
                Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                oleLoanForm.setDialogText(oleItem.getMissingPieceFlagNote());
                if (!oleItem.isMissingPieceFlag()) {
                    oleLoanForm.setDialogFlag(true);
                } else {
                    oleLoanForm.setRemoveMissingPieceButton(true);
                }
                oleLoanForm.setDialogMissingPieceCount(oleItem.getMissingPiecesCount());
                oleLoanForm.setDialogItemNoOfPieces(oleItem.getNumberOfPieces());
                if (oleItem.getNumberOfPieces() != null && !oleItem.getNumberOfPieces().equalsIgnoreCase("")) {
                    oleLoanForm.setDialogItemNoOfPiecesReadOnly(true);
                }
                if (oleItem.isMissingPieceFlag()) {
                    if (oleItem.getMissingPiecesCount() != null && !oleItem.getMissingPiecesCount().equalsIgnoreCase("")) {
                        oleLoanForm.setDialogMissingPieceCountReadOnly(true);
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
            oleLoanForm.setMissingPieceDialog(true);
            oleLoanForm.setMissingPieceLoanDocument(loanDocument);
        } else
            oleLoanForm.setInformation(OLEConstants.SELECT_SINGLE_ITEM);

        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * Save the claims return note in loan.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=updateMissingPieceItem")
    public ModelAndView updateMissingPieceItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the claims return method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
        StringBuffer buffer = new StringBuffer();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
       /* String maxSessionTime = oleLoanForm.getMaxTimeForCheckOutConstant();
        if (LOG.isInfoEnabled()){
            LOG.info("session timeout" + maxSessionTime);
        }
        if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
            oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));*/
        //SimpleDateFormat df = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_NOTICE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat (OLEConstants.DAT_FORMAT_EFFECTIVE);
        String parsedDate = simpleDateFormat.format((new Date()));
        OleLoanDocument loanDocument = oleLoanForm.getMissingPieceLoanDocument();
        if (oleLoanForm.getDialogItemNoOfPieces() == null || oleLoanForm.getDialogItemNoOfPieces() != null && oleLoanForm.getDialogItemNoOfPieces().equalsIgnoreCase("")) {
            oleLoanForm.setDialogErrorMessage("Provide information for no of pieces");
            oleLoanForm.setDialogItemNoOfPiecesReadOnly(false);
            oleLoanForm.setMissingPieceDialog(true);
            return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
        }
        if (oleLoanForm.getDialogMissingPieceCount() != null && oleLoanForm.getDialogItemNoOfPieces().equalsIgnoreCase("")) {
            int noOfPiece = Integer.parseInt(oleLoanForm.getDialogItemNoOfPieces());
            if (noOfPiece < 1) {
                oleLoanForm.setMissingPieceDialog(true);
                oleLoanForm.setDialogItemNoOfPiecesReadOnly(false);
                oleLoanForm.setDialogErrorMessage(OLEConstants.ERROR_MISSING_ITEM_NO_GREATER);
                return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
            }
        }
        oleLoanForm.setDialogItemNoOfPiecesReadOnly(true);
        if (oleLoanForm.getDialogMissingPieceCount() == null || (oleLoanForm.getDialogMissingPieceCount() != null && oleLoanForm.getDialogMissingPieceCount().equalsIgnoreCase(""))) {
            oleLoanForm.setMissingPieceDialog(true);
            oleLoanForm.setDialogMissingPieceCountReadOnly(false);
            oleLoanForm.setDialogErrorMessage(OLEConstants.ERROR_MISSING_ITEM_COUNT_REQUIRED);
            return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
        }
        if (oleLoanForm.getDialogMissingPieceCount() != null && !oleLoanForm.getDialogMissingPieceCount().equalsIgnoreCase("")) {
            int count = Integer.parseInt(oleLoanForm.getDialogMissingPieceCount());
            int noOfPiece = Integer.parseInt(oleLoanForm.getDialogItemNoOfPieces());
            if (count < 1) {
                oleLoanForm.setMissingPieceDialog(true);
                oleLoanForm.setDialogErrorMessage(OLEConstants.ERROR_MISSING_COUNT_NO_LESS);
                return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
            }
            if (count > noOfPiece) {
                oleLoanForm.setMissingPieceDialog(true);
                oleLoanForm.setDialogMissingPieceCountReadOnly(false);
                oleLoanForm.setDialogErrorMessage(OLEConstants.ERROR_MISSING_ITEM_GREATER_COUNT);
                return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
            }
        }

        if (oleLoanForm.getMissingPieceLoanDocument() != null && oleLoanForm.getMissingPieceLoanDocument().getItemUuid() != null) {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            try {
                boolean isMissingPieceFlag = false;
                String itemXmlContent = getLoanProcessor().getItemXML(oleLoanForm.getMissingPieceLoanDocument().getItemUuid());
                Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                boolean isUpdateNote=false;
                boolean isCreateNote=false;
                boolean isMissingFlagEnabledDB=(oleItem != null && oleItem.isMissingPieceFlag())?true:false;
                isCreateNote=!isMissingFlagEnabledDB && !oleLoanForm.isRemoveMissingPieceFlag();
                isUpdateNote=isMissingFlagEnabledDB && !oleLoanForm.isRemoveMissingPieceFlag();
                if (oleLoanForm.isRemoveMissingPieceFlag()) {
                    oleItem.setMissingPieceFlag(false);
                    isMissingPieceFlag = false;
                } else {
                    oleItem.setMissingPieceFlag(true);
                    isMissingPieceFlag = true;
                }
                if(isCreateNote){
                    missingPieceItemRecord.setMissingPieceCount(oleLoanForm.getDialogMissingPieceCount());
                    missingPieceItemRecord.setMissingPieceFlagNote(oleLoanForm.getDialogText());
                    missingPieceItemRecord.setMissingPieceDate(parsedDate);
                    missingPieceItemRecord.setItemId(oleLoanForm.getMissingPieceLoanDocument().getItemUuid());
                    missingPieceItemRecord.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                    missingPieceItemRecord.setPatronBarcode(oleLoanForm.getPatronBarcode());
                    missingPieceItemRecord.setPatronId(oleLoanForm.getPatronId());

                    if(CollectionUtils.isNotEmpty(oleItem.getMissingPieceItemRecordList())){

                        oleItem.getMissingPieceItemRecordList().add(missingPieceItemRecord);
                    } else {
                        List<MissingPieceItemRecord> missingPieceItemRecords=new ArrayList<MissingPieceItemRecord>();
                        missingPieceItemRecords.add(missingPieceItemRecord);
                        oleItem.setMissingPieceItemRecordList(missingPieceItemRecords);
                    }
                }else{
                    if(isUpdateNote){
                        updateMissingPieceNote(itemXmlContent,oleItem,oleLoanForm,loanDocument.getItemUuid());
                    }
                }
                    //getDocstoreClientLocator().getDocstoreClient().updateItem(item);

                oleItem.setMissingPiecesCount(oleLoanForm.getDialogMissingPieceCount());
                oleItem.setNumberOfPieces(oleLoanForm.getDialogItemNoOfPieces());
                oleItem.setMissingPieceFlagNote(oleLoanForm.getDialogText());
                itemXmlContent = new ItemOlemlRecordProcessor().toXML(oleItem);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("itemXmlContent" + itemXmlContent);
                }
                item.setContent(itemXmlContent);
                item.setCategory(OLEConstants.WORK_CATEGORY);
                item.setType(DocType.ITEM.getCode());
                item.setFormat(OLEConstants.OLEML_FORMAT);
                item.setId(oleLoanForm.getMissingPieceLoanDocument().getItemUuid());
                getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                if (isMissingPieceFlag) {
                    oleLoanForm.getMissingPieceLoanDocument().setMissingPieceNote(oleLoanForm.getDialogText());
                } else {
                    oleLoanForm.getMissingPieceLoanDocument().setMissingPieceNote("");
                }
                oleLoanForm.getMissingPieceLoanDocument().setCheckNo(false);
                oleLoanForm.setRemoveMissingPieceFlag(false);
            } catch (Exception e) {
                LOG.error("Exception occured when updating item" + e);
            }
        } else {
            oleLoanForm.setMissingPieceDialog(true);
            oleLoanForm.setErrorMessage("Not updated successfully");

        }
        oleLoanForm.setMissingPieceDialog(false);
        oleLoanForm.setRemoveMissingPieceButton(false);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=removeMissingPieceFromItem")
    public ModelAndView removeMissingPieceFromItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the claims return method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        StringBuffer buffer = new StringBuffer();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
      /*  String maxSessionTime = oleLoanForm.getMaxTimeForCheckOutConstant();
        if (LOG.isInfoEnabled()){
            LOG.info("session timeout" + maxSessionTime);
        }
        if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
            oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));*/
        oleLoanForm.setRemoveMissingPieceFlag(true);
        updateMissingPieceItem(form, result, request, response);

        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=removeDamagedFlagFromItem")
    public ModelAndView removeDamagedFlagFromItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the claims return method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        StringBuffer buffer = new StringBuffer();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setDialogFlag(false);
        updateDamagedItem(form, result, request, response);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=updateDamagedItem")
    public ModelAndView updateDamagedItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the claims return method");
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        if (oleLoanForm.getLoanList().size() > 0) {
            if (oleLoanForm.getLoanList().size() > 0) {
                for (OleLoanDocument oleLoanDocument : oleLoanForm.getLoanList()) {
                    if (oleLoanDocument.isCheckNo()) {
                        try {
                            String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                            Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                            if (oleLoanForm.isDialogFlag()) {
                                oleLoanDocument.setItemDamagedNote(oleLoanForm.getDialogText());
                                getLoanProcessor().updateItemDamagedHistory(oleItem,oleLoanDocument,oleLoanForm.getPatronId());
                            } else {
                                oleLoanDocument.setItemDamagedNote("");
                            }
                            oleItem.setItemDamagedStatus(oleLoanForm.isDialogFlag());
                            oleItem.setDamagedItemNote(oleLoanForm.getDialogText());
                            itemXmlContent = new ItemOlemlRecordProcessor().toXML(oleItem);
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("itemXmlContent" + itemXmlContent);
                            }
                            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                            item.setContent(itemXmlContent);
                            item.setCategory(OLEConstants.WORK_CATEGORY);
                            item.setType(DocType.ITEM.getCode());
                            item.setFormat(OLEConstants.OLEML_FORMAT);
                            item.setId(oleLoanDocument.getItemUuid());
                            getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                            if (oleLoanForm.isDialogFlag()) {
                                oleLoanDocument.setItemDamagedNote(oleLoanForm.getDialogText());
                            } else {
                                oleLoanDocument.setItemDamagedNote("");
                            }
                            oleLoanDocument.setCheckNo(false);
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        if (oleLoanForm.getExistingLoanList().size() > 0) {
            for (OleLoanDocument oleLoanDocument : oleLoanForm.getExistingLoanList()) {
                if (oleLoanDocument.isCheckNo()) {
                    try {
                        String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                        Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                        if (oleLoanForm.isDialogFlag()) {
                            oleLoanDocument.setItemDamagedNote(oleLoanForm.getDialogText());
                            getLoanProcessor().updateItemDamagedHistory(oleItem,oleLoanDocument,oleLoanForm.getPatronId());
                        } else {
                            oleLoanDocument.setItemDamagedNote("");
                        }
                        oleItem.setItemDamagedStatus(oleLoanForm.isDialogFlag());
                        oleItem.setDamagedItemNote(oleLoanForm.getDialogText());
                        itemXmlContent = new ItemOlemlRecordProcessor().toXML(oleItem);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("itemXmlContent" + itemXmlContent);
                        }
                        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                        item.setContent(itemXmlContent);
                        item.setCategory(OLEConstants.WORK_CATEGORY);
                        item.setType(DocType.ITEM.getCode());
                        item.setFormat(OLEConstants.OLEML_FORMAT);
                        item.setId(oleLoanDocument.getItemUuid());
                        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                        if (oleLoanForm.isDialogFlag()) {
                            oleLoanDocument.setItemDamagedNote(oleLoanForm.getDialogText());
                        } else {
                            oleLoanDocument.setItemDamagedNote("");
                        }
                        oleLoanDocument.setCheckNo(false);
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                }
            }

        }
        oleLoanForm.setRemoveItemDamagedButton(false);
        oleLoanForm.setMissingPieceDialog(false);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }


    @RequestMapping(params = "methodToCall=showDamagedDialogBox")
    public ModelAndView showDamagedDialogBox(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {

        LOG.debug("Inside the apply claims return method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        int count = 0;
        oleLoanForm.setDialogFlag(false);
        oleLoanForm.setDialogItemNoOfPieces(null);
        oleLoanForm.setDialogMissingPieceCount(null);
        oleLoanForm.setDialogText(null);
        oleLoanForm.setDialogItemNoOfPiecesReadOnly(false);
        oleLoanForm.setDialogMissingPieceCountReadOnly(false);
        oleLoanForm.setDialogErrorMessage(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setRemoveItemDamagedButton(false);
        OleLoanDocument loanDocument = null;
        int damagedItemCount=0;
        if (oleLoanForm.getLoanList().size() > 0) {
            for (OleLoanDocument oleLoanDocument : oleLoanForm.getLoanList()) {
                if (oleLoanDocument.isCheckNo()) {
                    loanDocument = oleLoanDocument;
                    count++;
                    if(StringUtils.isNotEmpty(loanDocument.getItemDamagedNote())){
                        damagedItemCount++;
                    }
                }
            }
        }
        if (oleLoanForm.getExistingLoanList().size() > 0) {
            for (OleLoanDocument oleLoanDocument : oleLoanForm.getExistingLoanList()) {
                if (oleLoanDocument.isCheckNo()) {
                    loanDocument = oleLoanDocument;
                    count++;
                    if(StringUtils.isNotEmpty(loanDocument.getItemDamagedNote())){
                        damagedItemCount++;
                    }
                }
            }
        }
        if (count > 0) {
            oleLoanForm.setDamagedItemDialog(true);
            oleLoanForm.setDialogFlag(true);
            if(damagedItemCount>0){
                oleLoanForm.setRemoveItemDamagedButton(true);
            }
            if (count == 1 && loanDocument != null && loanDocument.getItemUuid() != null) {
                try {
                    String itemXmlContent = getLoanProcessor().getItemXML(loanDocument.getItemUuid());
                    Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                    if (oleItem != null) {
                        oleLoanForm.setDialogText(oleItem.getDamagedItemNote());
                    }
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.error("while retrieving item damaged status info error" + e);
                    }
                }
            }
        } else {
            oleLoanForm.setDamagedItemDialog(false);
            oleLoanForm.setInformation(OLEConstants.CLAIMS_ITM_ERR_INFO);
        }
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=deactivateAllDialogs")
    public ModelAndView deactivateAllDialogs(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {

        LOG.debug("Inside the apply claims return method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setDialogFlag(false);
        oleLoanForm.setDialogItemNoOfPieces(null);
        oleLoanForm.setDialogMissingPieceCount(null);
        oleLoanForm.setDialogText(null);
        oleLoanForm.setDialogItemNoOfPiecesReadOnly(false);
        oleLoanForm.setDialogMissingPieceCountReadOnly(false);
        oleLoanForm.setDialogErrorMessage(null);
        oleLoanForm.setMissingPieceDialog(false);
        oleLoanForm.setDamagedItemDialog(false);
        oleLoanForm.setRemoveItemDamagedButton(false);
        for(OleLoanDocument loanDocument:oleLoanForm.getLoanList()){
            loanDocument.setCheckNo(false);
        }
        for (OleLoanDocument loanDocument : oleLoanForm.getExistingLoanList()) {
            loanDocument.setCheckNo(false);
        }
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");

    }

    /**
     * Change the circulation desk location and clear the screen.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=changeCirculationDeskLocation")
    public ModelAndView changeCirculationDeskLocation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the change circulation desk location method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setConfirmMessage(OLEConstants.CHANGE_LOC_MESS);
        oleLoanForm.setChangeLocationFlag(true);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=changeReturnCirculationDeskLocation")
    public ModelAndView changeReturnCirculationDeskLocation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                            HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the change circulation desk location method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setConfirmMessage(OLEConstants.CHANGE_LOC_MESS);
        oleLoanForm.setChangeLocationFlag(true);
        return getUIFModelAndView(oleLoanForm, "ReturnItemViewPage");
    }

    /**
     * Reset the old circulation location in loan.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=resetLocation")
    public ModelAndView resetLocation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the reset location method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setCirculationDesk(oleLoanForm.getPreviousCirculationDesk());
        oleLoanForm.setChangeLocationFlag(false);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=resetReturnLocation")
    public ModelAndView resetReturnLocation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the reset location method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setCirculationDesk(oleLoanForm.getPreviousCirculationDesk());
        oleLoanForm.setChangeLocationFlag(false);
        return getUIFModelAndView(oleLoanForm, "ReturnItemViewPage");
    }

    /**
     * This method clear UI for next borrower session..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveAndClear")
    public ModelAndView clearPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear patron method");
        fastAddBarcode = "";
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        String currentLocation = oleLoanForm.getCirculationDesk();
        oleLoanForm.setPreviousCirculationDesk(currentLocation);
        oleLoanForm.setNewPrincipalId("");
        oleLoanForm.setAddressVerified(false);
        oleLoanForm.setOverrideRenewItemFlag(false);
        oleLoanForm.setCurrentDate(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setBorrowerType(null);
        oleLoanForm.setPatronBarcode(null);
        oleLoanForm.setPatronName(null);
        oleLoanForm.setProxyPatronId(null);
        oleLoanForm.setRealPatronBarcode(null);
        oleLoanForm.setPatronId(null);
        oleLoanForm.setRealPatronList(null);
        oleLoanForm.setLoanList(null);
        oleLoanForm.setDueDateMap(null);
        oleLoanForm.setExistingLoanList(null);
        oleLoanForm.setDueDateMap(null);
        oleLoanForm.setMessage(null);
        oleLoanForm.setOnHoldRequestMessage(null);
        oleLoanForm.setSuccess(true);
        oleLoanForm.setChangeLocationFlag(false);
        oleLoanForm.setBlockLoan(false);
        oleLoanForm.setItemFocus(false);
        oleLoanForm.setSelfCheckOut(false);
        oleLoanForm.setCurrentPatronList(null);
        oleLoanForm.setPatronFocus(true);
        oleLoanForm.setBackGroundCheckIn(false);
        oleLoanForm.setRemoveMissingPieceFlag(false);
        oleLoanForm.setRecordDamagedItemNote(false);
        oleLoanForm.setRecordMissingPieceNote(false);
        oleLoanForm.setRecordCheckoutMissingPieceNote(false);
        oleLoanForm.setDisplayRecordNotePopup(false);
        oleLoanForm.setCheckoutRecordFlag(false);
        oleLoanForm.setSkipMissingPieceRecordPopup(false);
        oleLoanForm.setSkipDamagedRecordPopup(false);
        oleLoanForm.setDisplayMissingPieceNotePopup(false);
        oleLoanForm.setCheckoutMissingPieceRecordFlag(false);
        oleLoanForm.setDisplayDamagedRecordNotePopup(false);
        oleLoanForm.setCheckoutDamagedRecordFlag(false);
        oleLoanForm.setPatronbill(false);
        oleLoanForm.setPopDateTimeInfo("");
        oleLoanForm.setClaimsFlag(false);
        GlobalVariables.getUserSession().clearBackdoorUser();
        GlobalVariables.getUserSession().setBackdoorUser(oleLoanForm.getOldPrincipalId());
        oleLoanForm.setNewPrincipalId(null);

        if (!oleLoanForm.isClearUI()) {
            String principalId = GlobalVariables.getUserSession().getPrincipalId();
            OleCirculationDeskDetail oleCirculationDeskDetail = getLoanProcessor().getDefaultCirculationDesk(principalId);
            if (oleCirculationDeskDetail != null) {
                oleLoanForm.setCirculationDesk(oleCirculationDeskDetail.getCirculationDeskId());
                oleLoanForm.setPreviousCirculationDesk(oleLoanForm.getCirculationDesk());
            }
        }
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());

    }

    /**
     * This method clear UI for next borrower.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=clearPatron")
    public ModelAndView clearPatronScreen(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear patron Screen method");
        fastAddBarcode = "";
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        String currentLocation = oleLoanForm.getCirculationDesk();
        oleLoanForm.setPreviousCirculationDesk(currentLocation);
        if (oleLoanForm.getLoanList() != null && oleLoanForm.getLoanList().size() > 0 && !oleLoanForm.isChangeLocationFlag() && getLoanProcessor().getParameter(OLEConstants.PRINT_DUE_DATE_PER_TRANSACTION).equalsIgnoreCase("No")) {
            oleLoanForm.setDueDateSlip(true);
            printDueDateSlipList = oleLoanForm.getLoanList();
        }
        //oleLoanForm.setNewPrincipalId("");
        oleLoanForm.setAddressVerified(false);
        oleLoanForm.setOnHoldRequestMessage(null);
        oleLoanForm.setOverrideRenewItemFlag(false);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setBorrowerType(null);
        oleLoanForm.setPatronBarcode(null);
        oleLoanForm.setPatronName(null);
        oleLoanForm.setProxyPatronId(null);
        oleLoanForm.setRealPatronBarcode(null);
        oleLoanForm.setPatronId(null);
        oleLoanForm.setRealPatronList(null);
        oleLoanForm.setLoanList(null);
        oleLoanForm.setDueDateMap(null);
        oleLoanForm.setExistingLoanList(null);
        oleLoanForm.setDueDateMap(null);
        oleLoanForm.setMessage(null);
        oleLoanForm.setSuccess(true);
        oleLoanForm.setChangeLocationFlag(false);
        //oleLoanForm.setItemStatusLost(false);
        oleLoanForm.setBlockLoan(false);
        oleLoanForm.setItemFocus(false);
        oleLoanForm.setSelfCheckOut(false);
        oleLoanForm.setCurrentPatronList(null);
        oleLoanForm.setPatronFocus(true);
        oleLoanForm.setBackGroundCheckIn(false);
        oleLoanForm.setRemoveMissingPieceFlag(false);
        oleLoanForm.setRecordDamagedItemNote(false);
        oleLoanForm.setRecordMissingPieceNote(false);
        oleLoanForm.setRecordCheckoutMissingPieceNote(false);
        oleLoanForm.setDisplayRecordNotePopup(false);
        oleLoanForm.setCheckoutRecordFlag(false);
        oleLoanForm.setSkipMissingPieceRecordPopup(false);
        oleLoanForm.setSkipDamagedRecordPopup(false);
        oleLoanForm.setDisplayMissingPieceNotePopup(false);
        oleLoanForm.setCheckoutMissingPieceRecordFlag(false);
        oleLoanForm.setDisplayDamagedRecordNotePopup(false);
        oleLoanForm.setCheckoutDamagedRecordFlag(false);
        oleLoanForm.setPatronbill(false);
        oleLoanForm.setSuccessMessage(null);
        oleLoanForm.setPopDateTimeInfo("");
        oleLoanForm.setClaimsFlag(false);
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method override permission..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=overRide")
    public ModelAndView overRide(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the override method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setOverideMethodCall(oleLoanForm.getMethodToCall());
        oleLoanForm.setOverrideFlag(true);
        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        boolean newPrincipalFlag = false;
        if (StringUtils.isNotBlank(oleLoanForm.getNewPrincipalName())) {
            newPrincipalFlag = true;
        }
        if (newPrincipalFlag) {
            Person people = SpringContext.getBean(PersonService.class).getPersonByPrincipalName(oleLoanForm.getNewPrincipalName());
            if(people!=null){
                principalId=people.getPrincipalId();
                oleLoanForm.setNewPrincipalId(principalId);
            }
            if (people == null) {
                oleLoanForm.setOverrideLoginMessage(oleLoanForm.getNewPrincipalName() + " is invalid user Name.Please enter your user Name.");
                oleLoanForm.setOverrideErrorMessage(null);
                oleLoanForm.setNewPrincipalId(null);
                oleLoanForm.setNewPrincipalName(null);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
        } else {
        if(StringUtils.isNotBlank(principalId)){
            Person people = SpringContext.getBean(PersonService.class).getPerson(principalId);
            if(people == null) {
                oleLoanForm.setOverrideLoginMessage(people.getPrincipalName() + " is invalid user Name.Please enter your user Name.");
                oleLoanForm.setOverrideErrorMessage(null);
                oleLoanForm.setNewPrincipalId(null);
                oleLoanForm.setNewPrincipalName(null);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
        }
        }
        Boolean overRideFlag = getLoanProcessor().checkOverRidePermission(principalId, oleLoanForm);
        if (overRideFlag) {
            if ((!"".equals(oleLoanForm.getNewPrincipalId()) && oleLoanForm.getNewPrincipalId() != null)) {
                //oleLoanForm.setLoanLoginName(oleLoanForm.getNewPrincipalId());
               // GlobalVariables.getUserSession().setBackdoorUser(oleLoanForm.getNewPrincipalId());
               // String approverId = GlobalVariables.getUserSession().getPerson().getEntityId();
                oleLoanForm.getDummyLoan().setLoanApproverId(oleLoanForm.getNewPrincipalId());
               // GlobalVariables.getUserSession().clearBackdoorUser();
            }
            if (!newPrincipalFlag) {
               // oleLoanForm.setLoanLoginName(principalId);
               // GlobalVariables.getUserSession().setBackdoorUser(principalId);
               // String approverId = GlobalVariables.getUserSession().getPerson().getEntityId();
                oleLoanForm.getDummyLoan().setLoanApproverId(principalId);
               // GlobalVariables.getUserSession().clearBackdoorUser();
            }
            oleLoanForm.setNewPrincipalId("");
            oleLoanForm.setNewPrincipalName("");
            oleLoanForm.setOverrideFlag(false);
            oleLoanForm.setOverideMethodCall("");
            return null;
        }
         oleLoanForm.setNewPrincipalId(null);
        oleLoanForm.setNewPrincipalName(null);
        //GlobalVariables.getUserSession().clearBackdoorUser();
        if (!"".equals(oleLoanForm.getNewPrincipalId())) {
            oleLoanForm.setOverrideLoginMessage(principalId + " " + OLEConstants.OVERRIDE_LOGIN_ERR_INFO + OLEConstants.BREAK + oleLoanForm.getOverrideErrorMessage());
            oleLoanForm.setOverrideErrorMessage(null);
        }
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method loanLogin permission..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=loanLogin")
    public ModelAndView loanLogin(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the loanLogin method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        if (loginUserList == null) {
            loginUserList = new ArrayList<>();
        }
        oleLoanForm.setValidLogin(getLoanProcessor().isAuthorized(oleLoanForm.getLoanLoginName()));
        String patronNameURL = getLoanProcessor().patronNameURL(oleLoanForm.getLoanLoginName(), oleLoanForm.getPatronId());
        oleLoanForm.setPatronNameURL(patronNameURL);
        if (oleLoanForm.getLoanLoginName() != null && !oleLoanForm.getLoanLoginName().trim().isEmpty() && oleLoanForm.isValidLogin()) {
            oleLoanForm.setLoanLoginUserInfo("");
            oleLoanForm.setLoanLoginMessage(false);
            String loginUser = GlobalVariables.getUserSession().getPrincipalName();
            String circulationDesk = oleLoanForm.getCirculationDesk();
            GlobalVariables.getUserSession().setBackdoorUser(oleLoanForm.getLoanLoginName());
            start(form, result, request, response);
            if (getLoanProcessor().getCircDeskId() != null) {
                oleLoanForm.setCirculationDesk(getLoanProcessor().getCircDeskId());
            }
            StringBuffer users = new StringBuffer();
            users.append(loginUser + "," + circulationDesk);
            loginUserList.add(users.toString());
        }
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    @RequestMapping(params = "methodToCall=loanLogout")
    public ModelAndView loanLogout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the loanLogout method");
        int count;
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        if (oleLoanForm.getOldPrincipalId() != null && !oleLoanForm.getOldPrincipalId().trim().isEmpty()) {
            oleLoanForm.setLoanLoginUserInfo("");
            oleLoanForm.setLoanLoginMessage(false);
            if (loginUserList == null) {
                GlobalVariables.getUserSession().setBackdoorUser(null);
                return cancel(form, result, request, response);
            }
            if (loginUserList.size() > 0) {
                count = loginUserList.size() - 1;
                String usersList = loginUserList.get(count);
                loginUserList.remove(count);
                String user = GlobalVariables.getUserSession().getPrincipalName();
                String circulationId = oleLoanForm.getCirculationDesk();
                StringBuffer currentUser = new StringBuffer();
                currentUser.append(user + "," + circulationId);
                if (usersList.equalsIgnoreCase(currentUser.toString())) {
                    if (loginUserList.size() > 0) {
                        int size = loginUserList.size() - 1;
                        usersList = loginUserList.get(size);
                        loginUserList.remove(size);
                    } else {
                        GlobalVariables.getUserSession().setBackdoorUser(null);
                        return cancel(form, result, request, response);
                    }
                }
                if (usersList == "" || usersList.isEmpty()) {
                    GlobalVariables.getUserSession().setBackdoorUser(null);
                    return cancel(form, result, request, response);
                }
                String[] usersAndCirculationId = usersList.split(",");
                oleLoanForm.setLoginUser(usersAndCirculationId[0]);
                String patronNameURL = getLoanProcessor().patronNameURL(oleLoanForm.getLoginUser(), oleLoanForm.getPatronId());
                oleLoanForm.setPatronNameURL(patronNameURL);
                if (oleLoanForm.getLoginUser().equalsIgnoreCase(OLEConstants.ADMIN_USER)) {
                    GlobalVariables.getUserSession().setBackdoorUser(null);
                    return cancel(form, result, request, response);
                }
                GlobalVariables.getUserSession().setBackdoorUser(oleLoanForm.getLoginUser());
                start(form, result, request, response);
                oleLoanForm.setCirculationDesk(usersAndCirculationId[1]);
            } else {
                GlobalVariables.getUserSession().setBackdoorUser(null);
                return cancel(form, result, request, response);
            }
        }
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method no  override permission..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=noOverRide")
    public ModelAndView noOverRide(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the no override method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        /*String maxSessionTime = oleLoanForm.getMaxTimeForCheckOutConstant();
        if (LOG.isInfoEnabled()){
            LOG.info("session timeout" + maxSessionTime);
        }
        if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
            oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));*/
        oleLoanForm.setOverrideFlag(false);
        oleLoanForm.setOverideMethodCall("");
        oleLoanForm.setOverrideLoginMessage("");
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        GlobalVariables.getUserSession().setBackdoorUser(oleLoanForm.getLoanLoginName());
        oleLoanForm.setNewPrincipalId(oleLoanForm.getLoanLoginName());
        /*return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");*/
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method renewal the existing item
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=renewalItem")
    public ModelAndView renewalItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the renewal item method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        int renewCurrentCount = 0;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setMessage("");
        oleLoanForm.setPopDateTimeInfo("");
        oleLoanForm.setOverrideRenewItemFlag(false);
        oleLoanForm.setRenewalFlag(false);
        List<OleLoanDocument> existingItemList = new ArrayList<OleLoanDocument>();
        existingItemList.addAll(oleLoanForm.getExistingLoanList());
        if (oleLoanForm.getLoanList() != null && !oleLoanForm.getLoanList().isEmpty())
            existingItemList.addAll(oleLoanForm.getLoanList());
        OleLoanDocument oleLoanDocument = null;
        for (int i = 0; i < existingItemList.size(); i++) {
            OleLoanDocument loanDocument = existingItemList.get(i);
            if (loanDocument.getItemId()!=null && loanDocument.getItemId().equals(oleLoanForm.getItem())) {
                OleLoanDocument  oleLoanDocumentInDb = getLoanProcessor().getLoanDocumentsUsingItemIdAndPatronId(loanDocument.getItemUuid(),oleLoanForm.getPatronId());
                if(loanDocument.getLoanId()==null){
                 oleLoanDocument = oleLoanDocumentInDb;
                    oleLoanDocument.setClaimsReturnNote(loanDocument.getClaimsReturnNote());
                    oleLoanDocument.setClaimsReturnedIndicator(loanDocument.isClaimsReturnedIndicator());
                    oleLoanDocument.setClaimsReturnedDate(loanDocument.getClaimsReturnedDate());
                    oleLoanDocument.setBibUuid(loanDocument.getBibUuid());
                }else{
                    oleLoanDocument = loanDocument;
                    oleLoanDocument.setVersionNumber(oleLoanDocumentInDb.getVersionNumber());
                }
                oleLoanDocument.setOleItem(loanDocument.getOleItem());
                oleLoanDocument.setRenewalItemFlag(true);
                oleLoanDocument.setErrorMessage(null);
                renewCurrentCount = i;
                break;
            }
        }
        if(oleLoanDocument==null){
            OleLoanDocument loanDocument = oleLoanForm.getRenewalLoan();
            if (loanDocument.getItemId()!=null && loanDocument.getItemId().equals(oleLoanForm.getItem())) {
                if(loanDocument.getLoanId()==null){
                    oleLoanDocument = getLoanProcessor().getLoanDocumentsUsingItemIdAndPatronId(loanDocument.getItemUuid(),oleLoanForm.getPatronId());
                    oleLoanDocument.setClaimsReturnNote(loanDocument.getClaimsReturnNote());
                    oleLoanDocument.setClaimsReturnedIndicator(loanDocument.isClaimsReturnedIndicator());
                    oleLoanDocument.setClaimsReturnedDate(loanDocument.getClaimsReturnedDate());
                    oleLoanDocument.setBibUuid(loanDocument.getBibUuid());
                }else{
                    oleLoanDocument = loanDocument;
                }
                oleLoanDocument.setOleItem(loanDocument.getOleItem());
                oleLoanDocument.setRenewalItemFlag(true);
                oleLoanDocument.setErrorMessage(null);
            }
        }
        if(oleLoanDocument.getRenewalLoanDueDate() == null && oleLoanForm.getDueDateMap() != null){
                Timestamp timestamp;
                Pattern pattern;
                Matcher matcher;
                SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);
                boolean timeFlag = false;
                if (oleLoanForm.getPopDateTime() != null && !oleLoanForm.getPopDateTime().isEmpty()) {
                    String[] str = oleLoanForm.getPopDateTime().split(":");
                    pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
                    matcher = pattern.matcher(oleLoanForm.getPopDateTime());
                    timeFlag = matcher.matches();
                    if (timeFlag) {
                        if (str != null && str.length <= 2) {
                            oleLoanForm.setPopDateTime(oleLoanForm.getPopDateTime() + OLEConstants.CHECK_IN_TIME_MS);
                        }
                        timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(oleLoanForm.getPopDateTime()));
                    } else {
                        oleLoanForm.setPopDateTimeInfo(OLEConstants.DUE_DATE_TIME_FORMAT_MESSAGE);
                                     /*return getUIFModelAndView(oleLoanForm,"PatronItemViewPage");*/
                        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                    }
                } else if (fmt.format(oleLoanForm.getDueDateMap()).compareTo(fmt.format(new Date())) == 0) {
                    timestamp = new Timestamp(new Date().getTime());
                } else {
                    timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
                }
                oleLoanDocument.setManualRenewalDueDate(timestamp);
            if(oleLoanDocument.getLoanDueDate() != null && oleLoanDocument.getLoanDueDate().equals(oleLoanDocument.getManualRenewalDueDate())){
                oleLoanDocument.setRenewNotFlag(true);
            }else{
                oleLoanDocument.setRenewNotFlag(false);
            }
           // Timestamp timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
            oleLoanForm.setNonCirculatingFlag(false);
        }else{
            oleLoanDocument.setManualRenewalDueDate(null);
        }
        if(oleLoanDocument.getLoanDueDate() == null && oleLoanDocument.isRenewalItemFlag()){
            oleLoanForm.setInformation("");
            oleLoanForm.setReturnInformation("");
            oleLoanForm.setRenewalFlag(false);
            oleLoanForm.setSuccess(true);
            oleLoanForm.setMessage("");
            oleLoanForm.setOverrideRenewal(false);
            oleLoanForm.setRenewDueDateFlag(false);
            oleLoanForm.setOverrideRenewItemFlag(false);
            oleLoanForm.setNonCirculatingFlag(false);
            oleLoanForm.setRenewPermission(oleLoanDocument.isRenewPermission());
            oleLoanForm.setSuccessInfo(OLEConstants.RENEWAL_INDEFINITE_INFO);
            return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
        }
        if (!oleLoanForm.isOverrideRenewal()) {
         //   if (!getLoanProcessor().checkPendingRequestforItem(oleLoanDocument.getItemUuid())) {

                try {
                    oleLoanDocument.setPatronId(oleLoanForm.getPatronId());
                    oleLoanDocument.setBorrowerTypeId(oleLoanForm.getBorrowerTypeId());
                    oleLoanDocument.setBorrowerTypeName(oleLoanForm.getBorrowerType());
                    oleLoanDocument.setBorrowerTypeCode(oleLoanForm.getBorrowerCode());
                    oleLoanDocument.setCirculationLocationId(oleLoanForm.getCirculationDesk());
                    oleLoanDocument = getLoanProcessor().addLoan(oleLoanDocument.getPatronBarcode(), oleLoanDocument.getItemId(), oleLoanDocument,  null);
                    if(oleLoanDocument.isNonCirculatingItem()){
                     oleLoanForm.setNonCirculatingFlag(oleLoanDocument.isNonCirculatingItem());
                    }
                    if(oleLoanDocument.isIndefiniteCheckFlag()){
                        oleLoanForm.setRenewalFlag(false);
                        oleLoanForm.setOverrideRenewItemFlag(false);
                        oleLoanForm.setSuccess(true);
                        oleLoanForm.setMessage("");
                        oleLoanForm.setRenewPermission(oleLoanDocument.isRenewPermission());
                        oleLoanForm.setSuccessInfo(OLEConstants.RENEWAL_INDEFINITE_INFO);
                    }
                    if (StringUtils.isBlank(oleLoanDocument.getErrorMessage()) && !oleLoanDocument.isRenewNotFlag()) {
                        oleLoanForm.setRenewalFlag(false);
                        oleLoanForm.setOverrideRenewItemFlag(false);
                        oleLoanForm.setSuccess(true);
                        oleLoanForm.setMessage("");
                        // oleLoanForm.getExistingLoanList().remove(renewCurrentCount);
                        oleLoanForm.setRenewPermission(oleLoanDocument.isRenewPermission());
                        oleLoanForm.setSuccessInfo(OLEConstants.RENEWAL_ITM_SUCCESS_INFO);
                    } else if(oleLoanDocument.isRenewNotFlag()){
                        oleLoanForm.setRenewalFlag(false);
                        oleLoanForm.setOverrideRenewItemFlag(false);
                        oleLoanForm.setSuccess(true);
                        oleLoanForm.setMessage("");
                        oleLoanForm.setRenewPermission(oleLoanDocument.isRenewPermission());
                        oleLoanForm.setSuccessInfo(OLEConstants.RENEWAL_DUEDATE_SAME_INFO);
                    } else {
                        if (!oleLoanForm.isOverrideRenewal())
                            oleLoanForm.setOverrideRenewal(true);
                        oleLoanForm.setOverrideRenewItemFlag(true);
                        oleLoanForm.setRenewPermission(oleLoanDocument.isRenewPermission());
                        // errMsg = null;
                        if (StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
                            String errMsg = oleLoanDocument.getErrorMessage().substring(0, oleLoanDocument.getErrorMessage().lastIndexOf("(OR)"));
                            oleLoanForm.setMessage(errMsg);
                        }
                        if(getLoanProcessor().checkPendingRequestforItem(oleLoanDocument.getItemUuid())){
                            PermissionService service = KimApiServiceLocator.getPermissionService();
                            boolean hasPermission = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(),OLEConstants.DLVR_NMSPC,OLEConstants.PENDING_RQST_RENEWAL_ITM_INFO);
                            oleLoanForm.setOverrideRenewItemFlag(true);
                            oleLoanForm.setRenewalFlag(true);
                            if (hasPermission) {
                                oleLoanForm.setRenewalFlag(false);
                                oleLoanForm.setRenewPermission(true);
                            }
                        }

                    }
                    if(oleLoanDocument.getOleCirculationDesk()!=null && !oleLoanDocument.getOleCirculationDesk().isRenewLostItem() && oleLoanDocument.getItemLoanStatus().equals("LOST")){
                        oleLoanForm.setRenewalFlag(true);
                        oleLoanForm.setOverrideRenewItemFlag(true);
                    }
                    if(!oleLoanDocument.isIndefiniteCheckFlag()) {
                        if (oleLoanForm.getExistingLoanList() != null && oleLoanForm.getExistingLoanList().size() > 0) {
                            //  oleLoanForm.getExistingLoanList().remove(renewCurrentCount);
                            for (int i = 0; i < oleLoanForm.getExistingLoanList().size(); i++) {
                                if ((oleLoanForm.getExistingLoanList().get(i).getItemId()).equalsIgnoreCase(oleLoanDocument.getItemId())) {
                                    oleLoanForm.getExistingLoanList().remove(i);
                                    break;
                                }
                            }
                        }
                        if (oleLoanForm.getLoanList() != null && oleLoanForm.getLoanList().size() > 0) {
                            for (int i = 0; i < oleLoanForm.getLoanList().size(); i++) {
                                if ((oleLoanForm.getLoanList().get(i).getItemId()).equalsIgnoreCase(oleLoanDocument.getItemId())) {
                                    oleLoanForm.getLoanList().remove(i);
                                    break;
                                }
                            }
                        }
                        oleLoanForm.setOleLoanDocumentToLoanList(oleLoanDocument);
                    }

                } catch (Exception e) {
                    oleLoanForm.setInformation(e.getMessage());
                    LOG.error("Exception", e);
                }
        } else {
            try {
                ModelAndView overrideModelView = this.overRide(oleLoanForm, result, request, response);
                if (overrideModelView == null) {
                    getLoanProcessor().overrideSaveLoanForRenewal(oleLoanDocument);
                    if (oleLoanForm.getExistingLoanList() != null && oleLoanForm.getExistingLoanList().size() > 0) {
                        for (int i = 0; i < oleLoanForm.getExistingLoanList().size(); i++) {
                            if ((oleLoanForm.getExistingLoanList().get(i).getItemId()).equalsIgnoreCase(oleLoanDocument.getItemId())) {
                                oleLoanForm.getExistingLoanList().remove(i);
                                break;
                            }
                        }
                    }
                    if (oleLoanForm.getLoanList() != null && oleLoanForm.getLoanList().size() > 0) {
                        for (int i = 0; i < oleLoanForm.getLoanList().size(); i++) {
                            if ((oleLoanForm.getLoanList().get(i).getItemId()).equalsIgnoreCase(oleLoanDocument.getItemId())) {
                                oleLoanForm.getLoanList().remove(i);
                                break;
                            }
                        }
                    }
                    oleLoanForm.setOleLoanDocumentToLoanList(oleLoanDocument);
                    oleLoanForm.setMessage(null);
                    oleLoanForm.setSuccess(true);
                    oleLoanForm.setOverrideRenewal(false);
                    oleLoanForm.setRenewalFlag(false);
                    oleLoanForm.setOverrideRenewItemFlag(false);
                    if(!oleLoanDocument.isNonCirculatingItem()){
                       oleLoanForm.setNonCirculatingFlag(false);
                    }
                    if(oleLoanDocument.isRenewNotFlag()){
                        oleLoanForm.setSuccessInfo(OLEConstants.RENEWAL_DUEDATE_SAME_INFO);
                    }else {
                        oleLoanForm.setSuccessInfo(OLEConstants.RENEWAL_ITM_SUCCESS_INFO);
                    }
                }
                // } else
                //   oleLoanForm.setMessage(OLEConstants.RENEWAL_ITM_AFTER_FIXED_DUEDATE);

                if(oleLoanForm.getSuccessInfo().contains(OLEConstants.RENEWAL_ITM_SUCCESS_INFO)){
                    OleRenewalHistory oleRenewalHistory = new OleRenewalHistory();
                    oleRenewalHistory.setItemBarcode(oleLoanDocument.getItemId());
                    oleRenewalHistory.setItemId(oleLoanDocument.getItemUuid());
                    oleRenewalHistory.setLoanId(oleLoanDocument.getLoanId());
                    oleRenewalHistory.setOperatorId(oleLoanDocument.getLoanOperatorId());
                    oleRenewalHistory.setPatronBarcode(oleLoanForm.getPatronBarcode());
                    oleRenewalHistory.setRenewalDueDate(oleLoanDocument.getLoanDueDate());
                    oleRenewalHistory.setRenewedDate(new Timestamp(System.currentTimeMillis()));
                    KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistory);
                }

            } catch (Exception e) {
                LOG.error("Exception", e);
                oleLoanForm.setInformation(e.getMessage());
            }

        }

        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * This method not renewal the existing item
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=doNotRenewalItem")
    public ModelAndView doNotRenewalItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the do not renewal item method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setRenewalFlag(false);
        oleLoanForm.setSuccess(true);
        oleLoanForm.setMessage("");
        oleLoanForm.setOverrideRenewal(false);
        oleLoanForm.setRenewDueDateFlag(false);
        oleLoanForm.setOverrideRenewItemFlag(false);
        oleLoanForm.setNonCirculatingFlag(false);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    /**
     * This method renewal the list of existing items
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=renewalItems")
    public ModelAndView renewalItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the renewal items method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        oleLoanForm.setMessage("");
        oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
        List<OleLoanDocument> existingItemList = new ArrayList<OleLoanDocument>(0);
        List<OleLoanDocument> renewalItemList = new ArrayList<OleLoanDocument>(0);
        existingItemList.addAll(oleLoanForm.getExistingLoanList());
        existingItemList.addAll(oleLoanForm.getLoanList());
        boolean renewSelectFlag = true;
        for (int i = 0; i < existingItemList.size(); i++) {
            OleLoanDocument loanDocument = existingItemList.get(i);
            if (loanDocument.isCheckNo()) {
                renewSelectFlag = false;
                break;
            }
        }
        if (!renewSelectFlag) {
            String errMsg = "";
            for (int i = 0; i < existingItemList.size(); i++) {
                OleLoanDocument loanDocument = existingItemList.get(i);
                loanDocument.setErrorMessage(null);
                if (loanDocument.isCheckNo()) {
                    renewSelectFlag = false;
                    loanDocument.setCheckNo(false);
                    renewalItemList.add(loanDocument);
                    oleLoanForm.setRenewDueDateList(renewalItemList);
                }
            }
            List<OleLoanDocument> renewObjects = (List<OleLoanDocument>) getLoanProcessor().getLoanObjectsFromDAO(renewalItemList,oleLoanForm.getPatronId());
            int i=0;
            renewalItemList = new ArrayList<OleLoanDocument>(0);
            for(Object loanObject : renewObjects){
                OleLoanDocument loanDocument = (OleLoanDocument) loanObject;
                try {
                    loanDocument.setPatronId(oleLoanForm.getPatronId());
                    loanDocument.setBorrowerTypeId(oleLoanForm.getBorrowerTypeId());
                    loanDocument.setBorrowerTypeName(oleLoanForm.getBorrowerType());
                    loanDocument.setBorrowerTypeCode(oleLoanForm.getBorrowerCode());
                    loanDocument.setRenewalItemFlag(true);
                    loanDocument.setCirculationLocationId(oleLoanForm.getCirculationDesk());
                    loanDocument = getLoanProcessor().addLoan(loanDocument.getPatronBarcode(), loanDocument.getItemId(), loanDocument,  null);
                        /*if (getLoanProcessor().checkPendingRequestforItem(loanDocument.getItemUuid())) {
                            loanDocument.setErrorMessage(OLEConstants.PENDING_RQST_RENEWAL_ITM_INFO);
                            oleLoanForm.setRenewDueDateFlag(true);
                            renewalItemList.add(loanDocument);
                            oleLoanForm.setRenewDueDateList(renewalItemList);
                        } else */
                    oleLoanForm.setNonCirculatingFlag(loanDocument.isNonCirculatingItem());
                    if(loanDocument.isIndefiniteCheckFlag()){
                        errMsg = errMsg + (i + 1) + ". " + OLEConstants.RENEWAL_INDEFINITE_INFO + "  (" + loanDocument.getItemId() + ")<br/>";
                        oleLoanForm.setRenewDueDateFlag(false);
                     /*   oleLoanForm.setRenewalFlag(false);
                        oleLoanForm.setOverrideRenewal(false);
                        oleLoanForm.setOverrideRenewItemFlag(false);*/
                        oleLoanForm.setNonCirculatingFlag(false);
                        i++;
                    } else if(loanDocument.isRenewNotFlag()){
                        errMsg = errMsg + (i + 1) + ". " + OLEConstants.RENEWAL_DUEDATE_SAME_INFO + "  (" + loanDocument.getItemId() + ")<br/>";
                        oleLoanForm.setRenewDueDateFlag(false);
                     /*   oleLoanForm.setRenewDueDateFlag(false);
                        oleLoanForm.setRenewalFlag(false);
                        oleLoanForm.setOverrideRenewal(false);
                        oleLoanForm.setOverrideRenewItemFlag(false);*/
                        oleLoanForm.setNonCirculatingFlag(false);
                    } else if (StringUtils.isBlank(loanDocument.getErrorMessage())) {
                        errMsg = errMsg + (i + 1) + ". " + OLEConstants.RENEWAL_ITM_SUCCESS_INFO + "  (" + loanDocument.getItemId() + ")<br/>";
                        OleRenewalHistory oleRenewalHistory = new OleRenewalHistory();
                        oleRenewalHistory.setItemBarcode(loanDocument.getItemId());
                        oleRenewalHistory.setItemId(loanDocument.getItemUuid());
                        oleRenewalHistory.setLoanId(loanDocument.getLoanId());
                        oleRenewalHistory.setOperatorId(loanDocument.getLoanOperatorId());
                        oleRenewalHistory.setPatronBarcode(loanDocument.getPatronBarcode());
                        oleRenewalHistory.setRenewalDueDate(loanDocument.getLoanDueDate());
                        oleRenewalHistory.setRenewedDate(new Timestamp(System.currentTimeMillis()));
                        KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistory);
                        i++;
                        //oleLoanForm.getExistingLoanList().remove(i);
                        if (oleLoanForm.getExistingLoanList() != null && oleLoanForm.getExistingLoanList().size() > 0) {
                            //  oleLoanForm.getExistingLoanList().remove(renewCurrentCount);
                            for (int existingList = 0; existingList < oleLoanForm.getExistingLoanList().size(); existingList++) {
                                if ((oleLoanForm.getExistingLoanList().get(existingList).getItemId()).equalsIgnoreCase(loanDocument.getItemId())) {
                                    oleLoanForm.getExistingLoanList().remove(existingList);
                                    break;
                                }
                            }
                        }
                        if (oleLoanForm.getLoanList()!=null && oleLoanForm.getLoanList().size()>0){
                            for (int loanList = 0; loanList < oleLoanForm.getLoanList().size(); loanList++) {
                                if ((oleLoanForm.getLoanList().get(loanList).getItemId()).equalsIgnoreCase(loanDocument.getItemId())) {
                                    oleLoanForm.getLoanList().remove(loanList);
                                    break;
                                }
                            }
                        }
                        oleLoanForm.setOleLoanDocumentToLoanList(loanDocument);
                    } else {
                        if (getLoanProcessor().checkPendingRequestforItem(loanDocument.getItemUuid())){
                            PermissionService service = KimApiServiceLocator.getPermissionService();
                            boolean hasPermission = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(),OLEConstants.DLVR_NMSPC,OLEConstants.PENDING_RQST_RENEWAL_ITM_INFO);
                            if (!hasPermission){
                                loanDocument.setRenewPermissionForRequestedItem(true);
                            }
                        }
                        if(StringUtils.isNotBlank(loanDocument.getErrorMessage())) {
                            loanDocument.setErrorMessage(loanDocument.getErrorMessage().substring(0, loanDocument.getErrorMessage().lastIndexOf("(OR)")));
                        }
                        oleLoanForm.setRenewDueDateFlag(true);
                        if(loanDocument.getOleCirculationDesk()!=null && !loanDocument.getOleCirculationDesk().isRenewLostItem() && loanDocument.getItemLoanStatus().equals("LOST")){
                            oleLoanForm.setRenewalFlag(true);
                            oleLoanForm.setOverrideRenewItemFlag(true);
                        }
                        renewalItemList.add(loanDocument);
                        oleLoanForm.setRenewDueDateList(renewalItemList);
                    }
                } catch (Exception e) {
                    LOG.error("Exception", e);
                }

            }
            if (errMsg!="")
                oleLoanForm.setSuccessInfo(errMsg);
        }
        if (renewSelectFlag)
            oleLoanForm.setInformation(OLEConstants.RENEWAL_ITM_ERR_INFO);
        //}
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }


    /**
     * This method renewal the list of existing items  by using override
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=overrideRenewItems")
    public ModelAndView overrideRenewItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the renewal items method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
        oleLoanForm.setMessage("");
        oleLoanForm.setPopDateTimeInfo("");
        String info = oleLoanForm.getSuccessInfo()!=null ? oleLoanForm.getSuccessInfo() : "";
        List<OleLoanDocument> renewItemList = new ArrayList<OleLoanDocument>(0);
        renewItemList.addAll(oleLoanForm.getRenewDueDateList());
        try {
            boolean renewSelectFlag = true;
            for (int i = 0; i < renewItemList.size(); i++) {
                OleLoanDocument loanDocument = renewItemList.get(i);
                if (loanDocument.isRenewCheckNo()) {
                    renewSelectFlag = false;
                    break;
                }
            }
            if (!renewSelectFlag) {
                ModelAndView overrideModelView = this.overRide(form, result, request, response);
                if (overrideModelView == null) {

                    for (int i = 0; i < renewItemList.size(); i++) {
                        OleLoanDocument loanDocument = renewItemList.get(i);
                     //   if (!getLoanProcessor().checkPendingRequestforItem(loanDocument.getItemUuid())) {
                            loanDocument.setErrorMessage(null);
                            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
                            if (loanDocument.isRenewCheckNo()) {
                                loanDocument.setRenewCheckNo(false);
                                //if (currentDate.after(loanDocument.getLoanDueDate())) {
                                if(loanDocument.isNonCirculatingItem() && loanDocument.getRenewalDateMap() != null){
                                    Timestamp timestamp;
                                    Pattern pattern;
                                    Matcher matcher;
                                    SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);
                                    boolean timeFlag = false;
                                    oleLoanForm.setDueDateMap(loanDocument.getRenewalDateMap());
                                    if (loanDocument.getRenewalDateTime() != null && !loanDocument.getRenewalDateTime().isEmpty()) {
                                        String[] str = loanDocument.getRenewalDateTime().split(":");
                                        pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
                                        matcher = pattern.matcher(loanDocument.getRenewalDateTime());
                                        timeFlag = matcher.matches();
                                        if (timeFlag) {
                                            if (str != null && str.length <= 2) {
                                                loanDocument.setRenewalDateTime(loanDocument.getRenewalDateTime() + OLEConstants.CHECK_IN_TIME_MS);
                                            }
                                            timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(loanDocument.getRenewalDateMap()).concat(" ").concat(loanDocument.getRenewalDateTime()));
                                        } else {
                                            oleLoanForm.setPopDateTimeInfo(OLEConstants.DUE_DATE_TIME_FORMAT_MESSAGE);
                                     /*return getUIFModelAndView(oleLoanForm,"PatronItemViewPage");*/
                                            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                                        }
                                    } else if (fmt.format(oleLoanForm.getDueDateMap()).compareTo(fmt.format(new Date())) == 0) {
                                        timestamp = new Timestamp(new Date().getTime());
                                    } else {
                                        timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
                                    }
                                    loanDocument.setManualRenewalDueDate(timestamp);
                                    // Timestamp timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getDueDateMap()).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
                                    oleLoanForm.setNonCirculatingFlag(false);
                                }else{
                                    loanDocument.setManualRenewalDueDate(null);
                                }
                                getLoanProcessor().overrideSaveLoanForRenewal(loanDocument);
                                loanDocument.setErrorMessage(OLEConstants.RENEWAL_ITM_SUCCESS_INFO);
                                for (int j = 0; j < oleLoanForm.getExistingLoanList().size(); j++) {
                                    if (loanDocument.getLoanId().equals(oleLoanForm.getExistingLoanList().get(j).getLoanId())) {
                                        oleLoanForm.getExistingLoanList().remove(j);
                                        oleLoanForm.setOleLoanDocumentToLoanList(loanDocument);
                                    }
                                    if(loanDocument.getItemId().equals(oleLoanForm.getExistingLoanList().get(j).getItemId())){
                                        OleLoanDocument existingLoan = oleLoanForm.getExistingLoanList().get(j);
                                        existingLoan.setLoanDueDate(loanDocument.getLoanDueDate());
                                        existingLoan.setItemStatus(loanDocument.getItemStatus());
                                        existingLoan.setNumberOfRenewals(loanDocument.getNumberOfRenewals());
                                        existingLoan.setCourtesyNoticeFlag(false);
                                    }
                                }
                                if (oleLoanForm.getLoanList()!=null && oleLoanForm.getLoanList().size()>0){
                                    for (int loanList = 0; loanList < oleLoanForm.getLoanList().size(); loanList++) {
                                        if ((oleLoanForm.getLoanList().get(loanList).getItemId()).equalsIgnoreCase(loanDocument.getItemId())) {
                                            OleLoanDocument existingLoan = oleLoanForm.getLoanList().get(loanList);
                                            existingLoan.setLoanDueDate(loanDocument.getLoanDueDate());
                                            existingLoan.setCourtesyNoticeFlag(false);
                                            existingLoan.setItemStatus(loanDocument.getItemStatus());
                                            existingLoan.setNumberOfRenewals(loanDocument.getNumberOfRenewals());
                                            break;
                                        }
                                    }
                                }
                                oleLoanForm.setMessage("");
                                oleLoanForm.setSuccess(true);
                                oleLoanForm.setOverrideRenewal(false);
                                oleLoanForm.setNonCirculatingFlag(loanDocument.isNonCirculatingItem());
                                if(loanDocument.isRenewNotFlag()){
                                    info = info + (i + 1) + ". " + OLEConstants.RENEWAL_DUEDATE_SAME_INFO + "  (" + loanDocument.getItemId() + ")<br/>";
                                }else {
                                    info = info + (i + 1) + ". " + OLEConstants.RENEWAL_ITM_SUCCESS_INFO + "  (" + loanDocument.getItemId() + ")<br/>";
                                    OleRenewalHistory oleRenewalHistory = new OleRenewalHistory();
                                    oleRenewalHistory.setItemBarcode(loanDocument.getItemId());
                                    oleRenewalHistory.setItemId(loanDocument.getItemUuid());
                                    oleRenewalHistory.setLoanId(loanDocument.getLoanId());
                                    oleRenewalHistory.setOperatorId(loanDocument.getLoanOperatorId());
                                    oleRenewalHistory.setPatronBarcode(oleLoanForm.getPatronBarcode());
                                    oleRenewalHistory.setRenewalDueDate(loanDocument.getLoanDueDate());
                                    oleRenewalHistory.setRenewedDate(new Timestamp(System.currentTimeMillis()));
                                    KRADServiceLocator.getBusinessObjectService().save(oleRenewalHistory);
                                }
                                // } else
                                //loanDocument.setErrorMessage(OLEConstants.RENEWAL_ITM_AFTER_FIXED_DUEDATE);

                            }
                     //   }
                        oleLoanForm.setOverrideRenewItemFlag(false);

                    }

                }
            }
            if (info!=""){
                oleLoanForm.setSuccessInfo(info);
            }
        } catch (Exception e) {
            LOG.error("Exception ---> " + e, e);
        }
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }


    /**
     * This method initiate LoanProcessor.
     *
     * @return LoanProcessor
     */
    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    /**
     * This method creates new loan for a patron..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=validateItem")
    public ModelAndView validateItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside Validate Item ");
        }
        long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setRouteToLocation(null);
        oleLoanForm.setSuccessMessage(null);
        String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
        oleLoanForm.setAudioEnable(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
        oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setErrorMessage("");
        OleLoanDocument oleLoanDocument = null;
        if (oleLoanForm.getCheckInItem() != null) {
            oleLoanDocument = getLoanProcessor().getOleLoanDocumentUsingItemBarcode(oleLoanForm.getCheckInItem());
        } else {
            oleLoanDocument = getLoanProcessor().getOleLoanDocumentUsingItemUUID(oleLoanForm.getReturnItemUuid());
        }
        if (oleLoanDocument == null) {
            oleLoanDocument = new OleLoanDocument();
        }
        Timestamp timestamp;
        Pattern pattern;
        Matcher matcher;
        boolean timeFlag = false;
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);
        if (oleLoanForm.getCheckInTime() != null && !oleLoanForm.getCheckInTime().isEmpty()) {
            String[] str = oleLoanForm.getCheckInTime().split(":");
            pattern = Pattern.compile(OLEConstants.TIME_24_HR_PATTERN);
            matcher = pattern.matcher(oleLoanForm.getCheckInTime());
            timeFlag = matcher.matches();
            if (timeFlag) {
                if (str != null && str.length <= 2) {
                    oleLoanForm.setCheckInTime(oleLoanForm.getCheckInTime() + OLEConstants.CHECK_IN_TIME_MS);
                }
                timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getCheckInDate()).concat(" ").concat(oleLoanForm.getCheckInTime()));
            } else {
                //oleLoanForm.setReturnInformation(OLEConstants.CHECKIN_TIME_FORMAT_MESSAGE);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
        } else if (fmt.format(oleLoanForm.getCheckInDate()).compareTo(fmt.format(new Date())) == 0) {
            timestamp = new Timestamp(new Date().getTime());
        } else {
            timestamp = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).format(oleLoanForm.getCheckInDate()).concat(" ").concat(new SimpleDateFormat("HH:mm:ss").format(new Date())));
        }
        oleLoanDocument.setCheckInDate(timestamp);
        try {
            // Modified as per comments in Jira OLE-4901
            if (!getLoanProcessor().isValidCirculationDesk()) {
                oleLoanForm.setLoanLoginUserInfo(GlobalVariables.getUserSession().getPrincipalName() + " " + OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_VALIDATIONS);
                return super.start(oleLoanForm, result, request, response);
            }
            oleLoanDocument.setCirculationLocationId(oleLoanForm.getCirculationDesk());
            //oleLoanDocument.setDamagedCheckInOption(oleLoanForm.isDamagedCheckInOption());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Check-in Item Barcode Number --->" + oleLoanForm.getCheckInItem());
            }
            /*String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            oleLoanDocument.setCheckInMachineId(ipAddress);*/
            oleLoanDocument.setItemUuid(oleLoanForm.getReturnItemUuid());
            oleLoanForm.getErrorsAndPermission().clear();
            oleLoanDocument.setSkipDamagedCheckIn(oleLoanForm.isSkipDamagedCheckIn());
            oleLoanDocument = getLoanProcessor().returnLoan(oleLoanForm.getCheckInItem(), oleLoanDocument);
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            if(oleLoanDocument!=null && oleLoanDocument.getOleItem()!=null && oleLoanDocument.getOleItem().getCheckinNote()==null){
                getLoanProcessor().updateInTransitHistory(oleLoanDocument,oleLoanForm.getRouteToLocation());
            }
            oleLoanForm.setItemDamagedStatus(oleLoanDocument.isItemDamagedStatus());
            if (oleLoanDocument.isItemDamagedStatus() && (!oleLoanDocument.isSkipDamagedCheckIn())) {
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            } else {
                if (oleLoanForm.isRecordDamagedItemNote()) {
                    saveGeneralNoteForFlaggedItem(OLEConstants.DAMAGED_ITEM_CHECKED_IN_FLAG, true, oleLoanDocument, false, true, false, oleLoanForm.getPatronBarcode());
                    oleLoanForm.setRecordDamagedItemNote(false);
                }
                oleLoanForm.setSkipDamagedCheckIn(false);
                oleLoanForm.setItemDamagedStatus(false);
            }
            org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleLoanDocument.getItemUuid());
            String itemXmlContent = item.getContent();
            Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
            getLoanProcessor().getLocation(oleItem, oleLoanDocument, item);
            if (oleLoanDocument.getLocation() == null || oleLoanDocument.getLocation().isEmpty()) {
                getLoanProcessor().getDefaultHoldingLocation(oleLoanDocument);
            }
            if (oleItem.isClaimsReturnedFlag()) {
                oleLoanForm.setDummyLoan(oleLoanDocument);
                oleLoanForm.setClaimsReturned(true);
                oleLoanForm.setRecordNote(false);
                oleLoanForm.setReturnSuccess(false);
                oleLoanForm.setReturnMessage(OLEConstants.CLAIMS_RETURNED_MESSAGE);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
            oleLoanForm.setDummyLoan(oleLoanDocument);
            if (oleLoanDocument.isCopyRequest()) {
                oleLoanForm.setCopyRequest(true);
                oleLoanForm.setReturnSuccess(false);
                oleLoanForm.setReturnMessage(OLEConstants.COPY_REQUEST_FULFILL);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
            if (oleLoanDocument.isNumberOfPieces()) {
                oleLoanForm.setNumberOfPieces(true);
                oleLoanForm.setReturnSuccess(false);
                if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                    oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED
                            + OLEConstants.BREAK + "Total No of Pieces :      " + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.BREAK + "No of missing Pieces : " + (oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));
                } else {
                    oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED
                            + OLEConstants.BREAK + "Total No of Pieces :      " + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.BREAK + "No of missing Pieces : " + (oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));
                }
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
            String checkInNote = oleLoanDocument.getOleItem().getCheckinNote();
            if (checkInNote != null && !checkInNote.isEmpty() && StringUtils.isBlank(oleLoanForm.getReturnMessage())) {
                oleLoanForm.setCheckInNote(OLEConstants.CHECK_IN_NOTE_HEADER + checkInNote);
                oleLoanForm.setRouteToLocation(oleLoanDocument.getRouteToLocation());
                String principalId = GlobalVariables.getUserSession().getPrincipalId();
                oleLoanForm.setOkOrRemoveNote(getLoanProcessor().checkPermissionForRemoveNote(principalId));
            }
            if (!oleLoanDocument.isBackGroundCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
                oleLoanForm.setReturnSuccess(false);
                oleLoanForm.setReturnMessage(oleLoanDocument.getErrorMessage());
            } else {
                List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
                if (!oleLoanDocument.isCheckOut() || StringUtils.isBlank(oleLoanDocument.getErrorMessage()))
                    oleLoanDocuments.add(oleLoanDocument);
                if (oleLoanForm.getItemReturnList() != null) {
                    oleLoanDocuments.addAll(oleLoanForm.getItemReturnList());
                }
                oleLoanForm.setItemReturnList(oleLoanDocuments);
                //To refresh loan screen current and previous session loan list
            //    oleLoanForm.setExistingLoanList(getLoanProcessor().getPatronLoanedItemBySolr(oleLoanDocument.getPatronId()));
                OleLoanDocument tempOleLoanDocumentObj = null;
                List<OleLoanDocument> currentSessionList = oleLoanForm.getLoanList() != null ? oleLoanForm.getLoanList() : new ArrayList<OleLoanDocument>();
                for (OleLoanDocument currentOleLoanDocument : currentSessionList) {
                    if (currentOleLoanDocument.getItemId().equals(oleLoanForm.getCheckInItem())) {
                        tempOleLoanDocumentObj = currentOleLoanDocument;
                        break;
                    }
                }
                currentSessionList.remove(tempOleLoanDocumentObj);
                oleLoanForm.setLoanList(currentSessionList);
                //To refresh loan screen current and previous session loan list
                oleLoanForm.setCheckInItem("");
            }
        } catch (Exception e) {
            LOG.error("Exception in validate Item " + e);
            oleLoanForm.setReturnInformation(e.getMessage());
            oleLoanForm.setBillAvailability(false);
            try {
                org.kuali.ole.docstore.common.document.content.instance.Item oleItem = getLoanProcessor().checkItemStatusForItemBarcode(oleLoanForm.getCheckInItem());
                if(oleItem != null && oleItem.getItemStatus() != null && StringUtils.isNotBlank(oleLoanDocument.getPatronId()) && !oleItem.getItemStatus().getCodeValue().equalsIgnoreCase(OLEConstants.ITEM_STATUS_CHECKEDOUT)){
                    getLoanProcessor().rollbackItemStatus(oleItem,OLEConstants.ITEM_STATUS_CHECKEDOUT,oleLoanForm.getCheckInItem());
                    oleLoanForm.setReturnInformation(OLEConstants.RETURN_PROCESS_FAILURE);
                }

            }catch (Exception rollback){
                LOG.error("Exception occured during rollback item records " + rollback.getMessage());
            }
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if (!oleLoanForm.isCheckInNoteExists() && oleLoanForm.getCheckInNote() != null && StringUtils.isBlank(oleLoanForm.getReturnMessage())) {
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setCheckInNoteExists(true);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setReturnMessage(oleLoanForm.getCheckInNote());
            long end = System.currentTimeMillis();
            long total = end - begin;
            LOG.info("Time taken Inside Validate Item - checkin note"+total);
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if (oleLoanDocument.isCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage()) && StringUtils.isBlank(oleLoanForm.getReturnMessage())) {
            oleLoanForm.setDueDateEmpty(oleLoanDocument.isDueDateEmpty());
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setMessage(oleLoanDocument.getErrorMessage());
            oleLoanForm.setCheckOut(true);
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if ((StringUtils.isBlank(oleLoanForm.getReturnMessage())) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                // SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);

                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));

                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }
            }
        }
        if (oleLoanDocument.isCheckOut()) {
            oleLoanForm.setDueDateSlip(true);
            oleLoanForm.setBillAvailability(false);
        }
        if (request != null && !"".equalsIgnoreCase(request.toString())) {
            oleLoanForm.setDueDateSlip(false);
        }
        if (oleLoanDocument.getItemStatus() != null && oleLoanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_RECENTLY_RETURNED)) {
            oleLoanForm.setBillAvailability(false);
            oleLoanForm.setDueDateSlip(false);
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside Validate Item"+total);
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method  returns Item for a patron who is not able to return.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=returnItem")
    public ModelAndView returnItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside Return Item ");
        }
        long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
      /*  String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
        if(parameter != null){
            oleLoanForm.setMaxTimeForCheckInDate(Integer.parseInt(parameter) * 60);
        }*/


        String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
        oleLoanForm.setAudioEnable(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        Boolean overRideFlag = getLoanProcessor().checkOverRidePermission(principalId, oleLoanForm);
        if (oleLoanForm.getMissingPieceCount() != null) {
            oleLoanDocument.setMissingPiecesCount(oleLoanForm.getMissingPieceCount());
        }
        if (!overRideFlag) {
            ModelAndView modelAndView = this.overRide(form, result, request, response);
            if (modelAndView != null) {
                return modelAndView;
            }
        }
        try {
            oleLoanForm.getErrorsAndPermission().clear();
            oleLoanDocument = getLoanProcessor().returnLoan(oleLoanDocument);
            //   oleLoanForm.setSuccessMessage(oleLoanDocument.getSuccessMessage());
            if (oleLoanDocument.isNumberOfPieces()) {
                oleLoanForm.setNumberOfPieces(true);
                oleLoanForm.setReturnSuccess(false);
                //oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED);
                oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED
                        + OLEConstants.BREAK + "Total No of Pieces :      " + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.BREAK + "No of missing Pieces : " + (oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
            String checkInNote = oleLoanDocument.getOleItem().getCheckinNote();
            if (!oleLoanForm.isBackGroundCheckIn() && checkInNote != null && !checkInNote.isEmpty()) {
                oleLoanForm.setNumberOfPieces(false);
                oleLoanForm.setCheckInNote(OLEConstants.CHECK_IN_NOTE_HEADER + checkInNote);
                oleLoanForm.setRouteToLocation(oleLoanDocument.getRouteToLocation());
                oleLoanForm.setOkOrRemoveNote(getLoanProcessor().checkPermissionForRemoveNote(principalId));
            }
            List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
            if (!oleLoanDocument.isCheckOut() || StringUtils.isBlank(oleLoanDocument.getErrorMessage()))
                oleLoanDocuments.add(oleLoanDocument);
            if (oleLoanForm.getItemReturnList() != null) {
                oleLoanDocuments.addAll(oleLoanForm.getItemReturnList());
            }
            if (!oleLoanForm.isTempClaimsFlag()) {
                oleLoanForm.setItemReturnList(oleLoanDocuments);
            } else {
                oleLoanForm.setTempClaimsFlag(false);
            }

        } catch (Exception e) {
            LOG.error("Exception in return Item " + e, e);
        }
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setCheckInItem("");
        oleLoanForm.setReturnInformation("");
        if (!oleLoanForm.isCheckInNoteExists() && oleLoanForm.getCheckInNote() != null) {
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setCheckInNoteExists(true);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setReturnMessage(oleLoanForm.getCheckInNote());
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if (oleLoanDocument.isCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
            oleLoanForm.setDueDateEmpty(oleLoanDocument.isDueDateEmpty());
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setMessage(oleLoanDocument.getErrorMessage());
            oleLoanForm.setCheckOut(true);
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                // SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);

                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));

                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }
            }
        }
        if (oleLoanDocument.isCheckOut()) {
            oleLoanForm.setDueDateSlip(true);
            oleLoanForm.setBillAvailability(false);
        }
        if (oleLoanForm.isBackGroundCheckIn()) {
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            oleLoanDocument.setItemLoanStatus(oleLoanDocument.getItemStatusCode());
            loanPatron(oleLoanForm, result, request, response);
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside Return Item"+total);
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method  doesn't allow to return an item.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=noReturnItem")
    public ModelAndView doNotReturnItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside Do Not Return Item ");
        }
        OleLoanForm oleLoanForm = (OleLoanForm) form;
       /* String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
        if(parameter != null){
            oleLoanForm.setMaxTimeForCheckInDate(Integer.parseInt(parameter) * 60);
        }*/
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setCheckInItem("");
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setCheckInNote(null);
        oleLoanForm.setNumberOfPieces(false);
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        if (oleLoanForm.getDummyLoan().isCheckOut() && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                //SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));
                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }
            }
        }
        if (oleLoanForm.getDummyLoan() != null && oleLoanForm.getDummyLoan().isCheckOut()) {
            oleLoanForm.setDueDateSlip(true);
            oleLoanForm.setBillAvailability(false);
        }
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method  allows to continue Check-in.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=continueCheckIn")
    public ModelAndView continueCheckIn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside Continue Check-in ");
        }
        long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
       /* String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
        if(parameter != null){
            oleLoanForm.setMaxTimeForCheckInDate(Integer.parseInt(parameter) * 60);
        }*/

        String audioOption = getLoanProcessor().getParameter(OLEConstants.AUDIO_OPTION);
        oleLoanForm.setAudioEnable(audioOption != null && !audioOption.isEmpty() && audioOption.equalsIgnoreCase(OLEConstants.TRUE));
        String description = oleLoanForm.getDescription();
        String matchCheck = oleLoanForm.getMatchCheck();
        String copyCheck = oleLoanForm.getCopyCheck();
        oleLoanForm.getErrorsAndPermission().clear();
        OleLoanDocument oleLoanDocument = new OleLoanDocument();
        oleLoanDocument = oleLoanForm.getDummyLoan();
        oleLoanDocument.setDescription(oleLoanForm.getDescription());
        oleLoanDocument.setMissingPieceNote(oleLoanForm.getMissingPieceNote());
        if (oleLoanForm.getMissingPieceCount() != null) {
            oleLoanDocument.setMissingPiecesCount(oleLoanForm.getMissingPieceCount());
            oleLoanDocument.setPatronBarcode(oleLoanForm.getPatronBarcode());
        }
        try {
            if (copyCheck != null && copyCheck.equalsIgnoreCase(OLEConstants.TRUE)) {
                oleLoanForm.setCopyCheck("");
                getLoanProcessor().deleteRequestRecord(oleLoanDocument.getOleDeliverRequestBo());
                oleLoanDocument = getLoanProcessor().returnLoan(oleLoanDocument);
                //  oleLoanForm.setCopyRequest(false);
            } else if (copyCheck != null && copyCheck.equalsIgnoreCase(OLEConstants.FALSE)) {
                return endCheckInSession(form, result, request, response);
            }
            /*if (oleLoanForm.isCopyRequest() && oleLoanDocument.isNumberOfPieces()) {
                oleLoanForm.setCopyRequest(false);
                oleLoanForm.setNumberOfPieces(true);
                oleLoanForm.setReturnSuccess(false);
                oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }*/
            String itemUUid = oleLoanDocument.getItemUuid();
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUid);
            String itemXmlContent = item.getContent();
            Item oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
            getLoanProcessor().getLocation(oleItem, oleLoanDocument, item);
            if (oleLoanDocument.getLocation() == null || oleLoanDocument.getLocation().isEmpty()) {
                getLoanProcessor().getDefaultHoldingLocation(oleLoanDocument);
            }
            org.kuali.ole.docstore.common.document.Item itemXml = new ItemOleml();
            boolean claimsFlag = false;
            if (oleItem.isClaimsReturnedFlag()) {
                oleLoanForm.setTempClaimsFlag(true);
                claimsFlag = true;
                oleLoanForm.setClaimsReturned(false);
                oleLoanForm.setReturnSuccess(true);
                oleLoanForm.setReturnMessage(null);
                oleItem.setClaimsReturnedFlag(false);
                oleItem.setClaimsReturnedFlagCreateDate(null);
                oleItem.setClaimsReturnedNote(null);
                oleLoanDocument.getOleItem().setClaimsReturnedFlag(false);
                oleLoanDocument.getOleItem().setClaimsReturnedNote(null);
                oleLoanDocument.getOleItem().setClaimsReturnedFlagCreateDate(null);
                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                itemXml.setContent(itemOlemlRecordProcessor.toXML(oleItem));
                itemXml.setCategory(OLEConstants.WORK_CATEGORY);
                itemXml.setType(DocType.ITEM.getCode());
                itemXml.setFormat(OLEConstants.OLEML_FORMAT);
                itemXml.setId(oleLoanDocument.getItemUuid());
                getDocstoreClientLocator().getDocstoreClient().updateItem(itemXml);
                if (oleLoanForm.isRecordNote()) {
                    saveGeneralNoteForFlaggedItem(OLEConstants.CLAIMS_CHECKED_IN_FLAG, true, oleLoanDocument, false, true, false, oleLoanForm.getPatronBarcode());
                }
                oleLoanDocument = getLoanProcessor().returnLoan(oleLoanDocument);
            }
            if (oleLoanDocument.isCopyRequest()) {
                oleLoanForm.setCopyRequest(true);
                oleLoanForm.setReturnSuccess(false);
                oleLoanForm.setNumberOfPieces(true);
                oleLoanForm.setReturnMessage(OLEConstants.COPY_REQUEST_FULFILL);
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
            if ((oleLoanDocument.isNumberOfPieces() && (matchCheck == null)) || (oleItem != null && StringUtils.isNotEmpty(oleItem.getNumberOfPieces())) && (matchCheck == null)) {
                int noOfPiece = 0;
                if (oleItem.getNumberOfPieces() != null && !oleItem.getNumberOfPieces().equalsIgnoreCase("")) {
                    noOfPiece = Integer.parseInt(oleItem.getNumberOfPieces());
                }
                if (noOfPiece > 1) {
                    oleLoanForm.setNumberOfPieces(true);
                    oleLoanForm.setReturnSuccess(false);
                    oleLoanForm.setReturnMessage(OLEConstants.VERIFY_PIECES + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.PIECES_RETURNED
                            + OLEConstants.BREAK + "Total No of Pieces :      " + oleLoanDocument.getItemNumberOfPieces() + OLEConstants.BREAK + "No of missing Pieces : " + (oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));

                    return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                }
            }

            if (matchCheck != null && matchCheck.equalsIgnoreCase(OLEConstants.TRUE)) {
                oleLoanDocument.setContinueCheckIn(true);
                oleLoanDocument.getOleItem().setMissingPieceFlag(false);
                oleLoanDocument.getOleItem().setMissingPiecesCount("0");
                oleLoanDocument.getOleItem().setMissingPieceFlagNote(null);
                oleLoanDocument = getLoanProcessor().returnLoan(oleLoanDocument);
                oleLoanForm.setMatchCheck(null);
                oleLoanForm.setErrorMessage("");
                oleLoanForm.setMissingPieceCount("");
                oleLoanForm.setDescription("");
                oleLoanForm.setMissingPieceNote("");
                oleLoanForm.setSendMissingPieceMail(false);
            } else if (matchCheck != null && matchCheck.equalsIgnoreCase(OLEConstants.FALSE)) {
                Integer numberOfPieces = Integer.parseInt(oleItem != null && oleItem.getNumberOfPieces() != null && !oleItem.getNumberOfPieces().isEmpty() ? oleItem.getNumberOfPieces() : "0");
                if (numberOfPieces > 1) {
                    if (oleLoanForm.getMissingPieceCount() != null && !oleLoanForm.getMissingPieceCount().equalsIgnoreCase("")) {
                        if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getNumberOfPieces() != null && (!oleLoanDocument.getOleItem().getNumberOfPieces().equalsIgnoreCase(""))) {
                            int noOfPieces = Integer.parseInt(oleLoanDocument.getOleItem().getNumberOfPieces());
                            int missingPieceCount = Integer.parseInt(oleLoanDocument.getMissingPiecesCount());
                            int userMissingPieceCount = Integer.parseInt((oleLoanDocument.getOleItem().getMissingPiecesCount() != null ? oleLoanDocument.getOleItem().getMissingPiecesCount() : "0"));
                            if (missingPieceCount < 0) {
                                oleLoanForm.setCheckOut(true);
                                oleLoanForm.setMissingPieceValidationSuccess(true);
                                oleLoanForm.setErrorMessage("Missing piece should not less than 0");
                                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                            }
                            if (missingPieceCount + userMissingPieceCount > noOfPieces) {
                                oleLoanForm.setCheckOut(true);
                                oleLoanForm.setMissingPieceValidationSuccess(true);
                                oleLoanForm.setErrorMessage("Missing piece should not be greater than sum of no of pieces and missing pieces ");
                                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
                            }
                        }
                    }
                }
                oleLoanForm.setSendMissingPieceMail(true);
                oleLoanDocument.setDescription(description);
                oleLoanForm.setMissingPieceMessage(oleLoanDocument.getDescription());
                //loanProcessor.updateItemStatusWithMissingItem(oleLoanDocument);
                getLoanProcessor().updateMissingPiecesItemInfo(oleLoanDocument);
                if (oleLoanForm.isRecordMissingPieceNote() || oleLoanForm.isRecordCheckoutMissingPieceNote()) {
                    if (!oleLoanForm.isRecordCheckoutMissingPieceNote()) {
                        if (oleLoanForm.isRecordMissingPieceNote()) {
                            saveGeneralNoteForFlaggedItem(OLEConstants.MISSING_PIECE_ITEM_CHECKED_IN_FLAG, true, oleLoanDocument, false, true, false, oleLoanForm.getPatronBarcode());
                        }
                    } else {
                        saveGeneralNoteForFlaggedItem(OLEConstants.MISSING_PIECE_ITEM_CHECKED_IN_FLAG, true, oleLoanDocument, false, true, true, oleLoanForm.getPatronBarcode());
                    }
                    oleLoanForm.setRecordMissingPieceNote(false);
                    oleLoanForm.setRecordCheckoutMissingPieceNote(false);
                }
                /*loanProcessor.saveMissingPieceInfoForPatron(oleLoanDocument,true,oleLoanForm.getMissingPieceMessage());*/
                oleLoanDocument.setContinueCheckIn(true);
                oleLoanDocument = getLoanProcessor().returnLoan(oleLoanDocument);
                oleLoanForm.setMatchCheck(null);
                oleLoanForm.setErrorMessage("");
                oleLoanForm.setMissingPieceCount("");
                oleLoanForm.setDescription("");
                oleLoanForm.setMissingPieceNote("");
                if (itemUUid != null) {
                    itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
                    oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
                    oleLoanDocument.setOleItem(oleItem);
                    oleLoanDocument.setMissingPiecesCount(oleItem.getMissingPiecesCount());
                    oleLoanDocument.setMissingPieceFlag(oleItem.isMissingPieceFlag());
                    if (oleItem != null) {
                        OleItemAvailableStatus oleItemAvailableStatus = getLoanProcessor().validateAndGetItemStatus(oleLoanDocument.getItemStatusCode());
                        oleLoanDocument.setItemStatus(oleItemAvailableStatus != null ? oleItemAvailableStatus.getItemAvailableStatusName() : null);
                    }
                }
            } else if ((copyCheck == null || copyCheck.isEmpty()) && !oleLoanForm.isNumberOfPieces() && oleItem.isClaimsReturnedFlag()) {
                oleLoanDocument = getLoanProcessor().returnLoan(oleLoanDocument);
            }
            String checkInNote = oleLoanDocument.getOleItem().getCheckinNote();
            if (!oleLoanForm.isBackGroundCheckIn() && (oleLoanForm.isCopyRequest() || oleLoanForm.isNumberOfPieces() || claimsFlag) && checkInNote != null && !checkInNote.isEmpty()) {
                oleLoanForm.setNumberOfPieces(false);
                oleLoanForm.setCopyRequest(false);
                oleLoanForm.setCheckInNote(OLEConstants.CHECK_IN_NOTE_HEADER + checkInNote);
                oleLoanForm.setRouteToLocation(oleLoanDocument.getRouteToLocation());
                String principalId = GlobalVariables.getUserSession().getPrincipalId();
                oleLoanForm.setOkOrRemoveNote(getLoanProcessor().checkPermissionForRemoveNote(principalId));
            }
            List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
            if (!oleLoanDocument.isCheckOut() || StringUtils.isBlank(oleLoanDocument.getErrorMessage()))
                oleLoanDocuments.add(oleLoanDocument);
            if (oleLoanForm.getItemReturnList() != null) {
                oleLoanDocuments.addAll(oleLoanForm.getItemReturnList());
            }
            if (!oleLoanForm.isBackGroundCheckIn()) {
                oleLoanForm.setItemReturnList(oleLoanDocuments);

                //To refresh loan screen current and previous session loan list
               // oleLoanForm.setExistingLoanList(getLoanProcessor().getPatronLoanedItemBySolr(oleLoanDocument.getPatronId()));
                OleLoanDocument tempOleLoanDocumentObj = null;
                List<OleLoanDocument> currentSessionList = oleLoanForm.getLoanList() != null ? oleLoanForm.getLoanList() : new ArrayList<OleLoanDocument>();
                for (OleLoanDocument currentOleLoanDocument : currentSessionList) {
                    if (currentOleLoanDocument != null && currentOleLoanDocument.getItemId() != null && currentOleLoanDocument.getItemId().equals(oleLoanForm.getCheckInItem())) {
                        tempOleLoanDocumentObj = currentOleLoanDocument;
                        break;
                    }
                }
                currentSessionList.remove(tempOleLoanDocumentObj);
                oleLoanForm.setLoanList(currentSessionList);
                //To refresh loan screen current and previous session loan list
            }

        } catch (Exception e) {
            LOG.error("Exception in Continue Check-in " + e);
        }

        oleLoanForm.setNumberOfPieces(false);
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setCheckInItem("");
        oleLoanForm.setReturnInformation("");
        if (LOG.isDebugEnabled()) {
            LOG.debug("oleLoanForm.getFormKey()" + oleLoanForm.getFormKey());
        }
        if (oleLoanForm.isBackGroundCheckIn()) {
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            oleLoanDocument.setItemLoanStatus(oleLoanDocument.getItemStatusCode());
            loanPatron(oleLoanForm, result, request, response);
        }
        if (oleLoanForm.isTempClaimsFlag()) {
           /* oleLoanForm.setTempClaimsFlag(false);*/
            if (!oleLoanDocument.isBackGroundCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
                oleLoanForm.setReturnSuccess(false);
                oleLoanForm.setReturnMessage(oleLoanDocument.getErrorMessage());
                return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
            }
        }
        if (!oleLoanForm.isCheckInNoteExists() && oleLoanForm.getCheckInNote() != null) {
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setCheckInNoteExists(true);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setReturnMessage(oleLoanForm.getCheckInNote());
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if (oleLoanDocument.isCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
            oleLoanForm.setDueDateEmpty(oleLoanDocument.isDueDateEmpty());
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setMessage(oleLoanDocument.getErrorMessage());
            oleLoanForm.setCheckOut(true);
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        }
        if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                // SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null && (!oleLoanDocument.isBackgroundCheckInMissingPiece()) && oleLoanForm.isSendMissingPieceMail()) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));
                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }
            }
        }
        if (oleLoanDocument.isCheckOut()) {
            oleLoanForm.setDueDateSlip(true);
            oleLoanForm.setBillAvailability(false);
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside Continue checkin Item"+total);
        return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
    }

    /**
     * This method  ends Check-in session
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=endCheckInSession")
    public ModelAndView endCheckInSession(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside End Check-in session ");
        }
        OleLoanForm oleLoanForm = (OleLoanForm) form;

        String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
        if (parameter != null) {
            oleLoanForm.setMaxTimeForCheckInDate(Integer.parseInt(parameter));
        }
        if (oleLoanForm.getItemReturnList() != null) {
            List<OleLoanDocument> holdSlipList = new ArrayList<OleLoanDocument>();
            for (OleLoanDocument oleLoanDocument : oleLoanForm.getItemReturnList()) {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD)) {
                    holdSlipList.add(oleLoanDocument);
                }
            }
            OleCirculationDesk oleCirculationDesk = null;
            oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(oleLoanForm.getCirculationDesk());
            if (holdSlipList.size() > 0 && oleCirculationDesk != null && oleCirculationDesk.isPrintSlip() && oleCirculationDesk.isHoldQueue()) {
                printHoldSlipList = holdSlipList;
                oleLoanForm.setHoldSlip(true);
            }
        }
        oleLoanForm.setCopyRequest(false);
        oleLoanForm.setNumberOfPieces(false);
        oleLoanForm.setReturnInformation("");
        oleLoanForm.setCheckInItem("");
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setItemReturnList(null);
        oleLoanForm.setCheckInNote(null);
        oleLoanForm.setBillAvailability(false);
        oleLoanForm.setCheckInDate(new Timestamp(new Date().getTime()));
        oleLoanForm.setCheckInTime(null);
        if (!oleLoanForm.isClearUI()) {
            String principalId = GlobalVariables.getUserSession().getPrincipalId();
            OleCirculationDeskDetail oleCirculationDeskDetail = getLoanProcessor().getDefaultCirculationDesk(principalId);
            if (oleCirculationDeskDetail != null) {
                oleLoanForm.setCirculationDesk(oleCirculationDeskDetail.getCirculationDeskId());
                oleLoanForm.setPreviousCirculationDesk(oleLoanForm.getCirculationDesk());
            }
        }
        oleLoanForm.setSuccessMessage(null);
        //return getUIFModelAndView(oleLoanForm, oleLoanForm.getPageId());
        return getUIFModelAndView(oleLoanForm, "ReturnItemViewPage");
    }

    /**
     * This method will allow  library operator to print slips for user
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=printBill")
    public ModelAndView printBill(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        OlePrintSlip olePrintSlip = new OlePrintSlip();
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside Print Bill ");
        }
        String formKey = request.getParameter("formKey");
        OleLoanForm oleLoanForm = (OleLoanForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        if (oleLoanForm.getRouteToLocation() != null) {
            oleLoanDocument.setRouteToLocation(oleLoanForm.getRouteToLocation());
        }
        olePrintSlip.createPdfForPrintingSlip(oleLoanDocument, response);
        oleLoanForm.setBackGroundCheckIn(false);
        return null;
    }

    /**
     * This method will allow library operator to print due date slips for user.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=printLoanBill")
    public ModelAndView printLoanBill(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        OlePrintSlip olePrintSlip = new OlePrintSlip();
        if (LOG.isDebugEnabled()) {
            LOG.debug(" Inside Print Loan Bill ");
        }
        String formKey = request.getParameter("formKey");
        OleLoanForm oleLoanForm = (OleLoanForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        List<OleLoanDocument> oleLoanDocumentList = new ArrayList<OleLoanDocument>();
        if (getLoanProcessor().getParameter(OLEConstants.PRINT_DUE_DATE_PER_TRANSACTION).equalsIgnoreCase("No")) {
            oleLoanDocumentList = printDueDateSlipList;
        } else {
            if (oleLoanForm.getLoanList() != null && (oleLoanForm.getLoanList().size() > 0)) {
                oleLoanDocumentList.add(oleLoanForm.getLoanList().get(0));
            }
        }
        if (oleLoanDocument.isCheckOut()) {
            olePrintSlip.createPdfForBackGroundCheckOut(oleLoanDocument, response);
        } else if (oleLoanDocumentList != null && oleLoanDocumentList.size() > 0) {
            olePrintSlip.createDueDateSlipPdf(oleLoanDocumentList, response);
        }
        return null;
    }


    /**
     * This method will allow library operator to even if the check-in note exists
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=checkInNote")
    public ModelAndView checkInNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Check in Note Exists ");
        }
        long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
       /* String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
        if(parameter != null){
            oleLoanForm.setMaxTimeForCheckInDate(Integer.parseInt(parameter) * 60);
        }*/
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCirculationDeskByLocationCode(oleLoanForm.getRouteToLocation());
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        if ((oleLoanForm.getRouteToLocation() == null || oleLoanForm.getRouteToLocation().isEmpty()) && oleLoanDocument.getItemStatusCode().contains(OLEConstants.ITEM_STATUS_IN_TRANSIT)) {
            oleLoanForm.setInformation(OLEConstants.CIRC_DESK_REQUIRED);
            return getUIFModelAndView(form, "ReturnItemViewPage");
        } else if (oleCirculationDesk == null) {
            oleLoanForm.setInformation(OLEConstants.CIRC_DESK_INVALID);
            return getUIFModelAndView(form, "ReturnItemViewPage");
        } else {
            oleLoanDocument.setRouteToLocationName(oleCirculationDesk.getCirculationDeskPublicName());
        }
        oleLoanForm.setCheckInNoteExists(false);
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setCheckInNote(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        Item oleItem = oleLoanDocument.getOleItem();
        if (oleItem != null) {
            try {
                getLoanProcessor().removeCheckInNote(oleItem);
            } catch (Exception e) {
                LOG.error("Exception while removing check-in note", e);
            }
        }
        if (oleLoanDocument.isCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
            oleLoanForm.setDueDateEmpty(oleLoanDocument.isDueDateEmpty());
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setMessage(oleLoanDocument.getErrorMessage());
            oleLoanForm.setCheckOut(true);
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            return getUIFModelAndView(form, "ReturnItemViewPage");
        }
        if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));
                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }

            }
        }
        if (oleLoanDocument.isCheckOut() && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            oleLoanForm.setDueDateSlip(true);
            oleLoanForm.setBillAvailability(false);
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside Checkin note "+total);
        return getUIFModelAndView(form, "ReturnItemViewPage");
    }

    /**
     * This method will allow library operator to even if the check-in note exists
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=okCheckInNote")
    public ModelAndView okCheckInNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Check in Note Exists ");
        }
        long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
       /* String parameter = getLoanProcessor().getParameter(OLEConstants.MAX_TIME_CHECK_IN);
        if(parameter != null){
            oleLoanForm.setMaxTimeForCheckInDate(Integer.parseInt(parameter) * 60);
        }*/
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCirculationDeskByLocationCode(oleLoanForm.getRouteToLocation());
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        if ((oleLoanForm.getRouteToLocation() == null || oleLoanForm.getRouteToLocation().isEmpty()) && oleLoanDocument.getItemStatusCode().contains(OLEConstants.ITEM_STATUS_IN_TRANSIT)) {
            oleLoanForm.setInformation(OLEConstants.CIRC_DESK_REQUIRED);
            return getUIFModelAndView(form, "ReturnItemViewPage");
        } else if (oleCirculationDesk == null) {
            oleLoanForm.setInformation(OLEConstants.CIRC_DESK_INVALID);
            return getUIFModelAndView(form, "ReturnItemViewPage");
        } else {
            oleLoanDocument.setRouteToLocationName(oleCirculationDesk.getCirculationDeskPublicName());
            oleLoanDocument.setRouteToLocation(oleCirculationDesk.getCirculationDeskCode());
        }
        oleLoanForm.setCheckInNoteExists(false);
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setCheckInNote(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        /*if(oleLoanDocument.getItemStatus()!=null && (oleLoanDocument.getItemStatus().contains(OLEConstants.OleDeliverRequest.INTRANSIT)||oleLoanDocument.getItemStatus().contains(OLEConstants.OleDeliverRequest.HOLD))){
            oleLoanForm.setInTransit(true);
        } else {
            oleLoanForm.setInTransit(false);
        }*/
        if (oleLoanDocument.isCheckOut() && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
            oleLoanForm.setDueDateEmpty(oleLoanDocument.isDueDateEmpty());
            oleLoanForm.setDummyLoan(oleLoanDocument);
            oleLoanForm.setReturnSuccess(false);
            oleLoanForm.setMessage(oleLoanDocument.getErrorMessage());
            oleLoanForm.setCheckOut(true);
            oleLoanForm.setItem(oleLoanForm.getCheckInItem());
            oleLoanForm.setOleItem(oleLoanDocument.getOleItem());
            return getUIFModelAndView(form, "ReturnItemViewPage");
        }
        getLoanProcessor().updateInTransitHistory(oleLoanDocument,oleLoanForm.getRouteToLocation());
        if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag() && oleLoanForm.isSendMissingPieceMail()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                // SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);

                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));
                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }
            }
        }
        if (oleLoanDocument.isCheckOut() && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            oleLoanForm.setDueDateSlip(true);
            oleLoanForm.setBillAvailability(false);
        }
        if (oleLoanDocument.getItemStatus() != null && oleLoanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_RECENTLY_RETURNED)) {
            oleLoanForm.setBillAvailability(false);
            oleLoanForm.setDueDateSlip(false);
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside okcheckin note"+total);
        return getUIFModelAndView(form, "ReturnItemViewPage");
    }

    /**
     * This method clear UI for next borrower session..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=clearUI")
    public ModelAndView clearUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setClearUI(true);
        clearPatronScreen(oleLoanForm, result, request, response);
        endCheckInSession(oleLoanForm, result, request, response);
        oleLoanForm.setClearUI(false);
        return getUIFModelAndView(form, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=clearReturnUI")
    public ModelAndView clearReturnUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setClearUI(true);
        clearPatronScreen(oleLoanForm, result, request, response);
        endCheckInSession(oleLoanForm, result, request, response);
        oleLoanForm.setClearUI(false);
        return getUIFModelAndView(form, "ReturnItemViewPage");
    }

    /**
     * To refresh patron record.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=setItemBarcode")
    public ModelAndView setItemBarcode(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setItem(fastAddBarcode);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=gotoReturn")
    public ModelAndView gotoReturn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws IOException {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do?channelTitle=Loan&channelUrl=" + baseUrl + "/ole-kr-krad/loancontroller?viewId=ReturnItemView&methodToCall=start&formKey=" + oleLoanForm.getFormKey();
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        oleLoanForm.setSuccessMessage(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        return performRedirect(oleLoanForm, url, props);
    }


    @RequestMapping(params = "methodToCall=gotoLoan")
    public ModelAndView gotoLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setReturnCheck(false);
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String requestKey = request.getParameter("formKey");
        String url = baseUrl + "/portal.do?channelTitle=Loan&channelUrl=" + baseUrl + "/ole-kr-krad/loancontroller?viewId=PatronItemView&methodToCall=start&formKey=" + requestKey;
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(form.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, form.getReturnFormKey());
        }
        oleLoanForm.setSuccessMessage(null);
        oleLoanForm.setShowExistingLoan(false);
        return performRedirect(oleLoanForm, url, props);
    }

    /**
     * This method will allow library operator to even if it is damagedCheckIn
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=damagedCheckIn")
    public ModelAndView damagedCheckIn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Check in Note Exists ");
        }
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getCirculationDeskByLocationCode(oleLoanForm.getRouteToLocation());
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        if ((oleLoanForm.getRouteToLocation() == null || oleLoanForm.getRouteToLocation().isEmpty()) && oleLoanDocument.getItemStatusCode().contains(OLEConstants.ITEM_STATUS_IN_TRANSIT)) {
            oleLoanForm.setInformation(OLEConstants.CIRC_DESK_REQUIRED);
            return getUIFModelAndView(form, "ReturnItemViewPage");
        } else if (oleCirculationDesk == null) {
            oleLoanForm.setInformation(OLEConstants.CIRC_DESK_INVALID);
            return getUIFModelAndView(form, "ReturnItemViewPage");
        } else {
            oleLoanDocument.setRouteToLocationName(oleCirculationDesk.getCirculationDeskPublicName());
        }
        oleLoanForm.setDamagedCheckIn(false);
        oleLoanForm.setReturnSuccess(true);
        oleLoanForm.setReturnMessage(null);
        oleLoanForm.setInformation("");
        oleLoanForm.setSuccessInfo("");
        if (oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isPrintSlip()) {
            if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                OleNoticeBo oleNoticeBo = getLoanProcessor().getNotice(oleLoanDocument);
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
                SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.DATEFORMAT);
                Date date = new Date(oleLoanDocument.getCheckInDate().getTime());
                if (oleNoticeBo != null) {
                    oleNoticeBo.setCheckInDate(dateFormat.format(date));
                    String missingNoticeDetails = getOleDeliverBatchService().sendMissingNotice(oleNoticeBo);
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.RETURN_MISSING_PIECE_NOTICE_SUBJECT_LINE), new EmailBody(missingNoticeDetails), true);
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                }
            } else {
                if (oleLoanDocument.getItemStatusCode() != null && oleLoanDocument.getItemStatusCode().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleLoanDocument.getOleCirculationDesk() != null && oleLoanDocument.getOleCirculationDesk().isHoldQueue()) {
                    oleLoanForm.setBillAvailability(false);
                } else {
                    oleLoanForm.setBillAvailability(true);
                }
            }
        }
        return getUIFModelAndView(form, "ReturnItemViewPage");
    }

    @RequestMapping(params = "methodToCall=proceedLoan")
    public ModelAndView proceedLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        oleLoanDocument.setItemUuid(oleLoanForm.getItemUuid());
        try {
            getLoanProcessor().isClaimsReturnedItem(oleLoanForm.getItem(), oleLoanDocument);
            oleLoanForm.setAddressVerified(false);
            oleLoanForm.getErrorsAndPermission().clear();
            oleLoanForm.setClaimsFlag(false);
            getLoanProcessor().processLoan(oleLoanForm, oleLoanDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    private void saveGeneralNoteForFlaggedItem(String systemParameter, boolean multiValue, OleLoanDocument oleLoanDocument, boolean isCheckOut, boolean isCheckIn, boolean backgroundCheckin, String formBarcode) {
        String note = getLoanProcessor().getParameter(systemParameter);
        SimpleDateFormat df = new SimpleDateFormat(OLEConstants.TIMESTAMP);
        if (multiValue) {
            note = note.replace("[0]", oleLoanDocument.getItemId());
            if (isCheckOut) {
                note = note.replace("[0]", oleLoanDocument.getItemId());
                note = note.replace("[1]", df.format(oleLoanDocument.getCreateDate()).toString());
                Map map = new HashMap();
                map.put("circulationDeskId", oleLoanDocument.getCirculationLocationId());
                OleCirculationDesk oleCirculationDesk = getLoanProcessor().getBusinessObjectService().findByPrimaryKey(OleCirculationDesk.class, map);
                note = note.replace("[2]", oleCirculationDesk.getCirculationDeskCode());
                if (systemParameter.equalsIgnoreCase(OLEConstants.MISSING_PIECE_ITEM_CHECKED_OUT_FLAG)) {
                    int noOfMissingPiece = 0;
                    if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                        if (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().getMissingPiecesCount() != null && !oleLoanDocument.getOleItem().getMissingPiecesCount().equalsIgnoreCase("")) {
                            noOfMissingPiece = Integer.parseInt(oleLoanDocument.getOleItem().getMissingPiecesCount());
                        }
                    }
                    note = note.replace("[3]", noOfMissingPiece + "");
                }
                map.clear();
                map.put("olePatronId", oleLoanDocument.getPatronId());
                OlePatronDocument olePatronDocument = getLoanProcessor().getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, map);
                OlePatronNotes olePatronNotes = new OlePatronNotes();
                olePatronNotes.setPatronNoteText(note);
                map.clear();
                map.put("patronNoteTypeCode", "GENERAL");
                OlePatronNoteType olePatronNoteType = (OlePatronNoteType) getLoanProcessor().getBusinessObjectService().findByPrimaryKey(OlePatronNoteType.class, map);
                olePatronNotes.setPatronNoteTypeId(olePatronNoteType.getPatronNoteTypeId());
                olePatronNotes.setOlePatronId(olePatronDocument.getOlePatronId());
                olePatronDocument.getNotes().add(olePatronNotes);
                OlePatronDocument patronDocument = (OlePatronDocument) ObjectUtils.deepCopy(olePatronDocument);
                getLoanProcessor().getBusinessObjectService().save(patronDocument);
            }
            if (isCheckIn) {
                note = note.replace("[0]", oleLoanDocument.getItemId());
                note = note.replace("[1]", df.format(oleLoanDocument.getCheckInDate()).toString());
                note = note.replace("[2]", oleLoanDocument.getOleCirculationDesk().getCirculationDeskCode());
                if (oleLoanDocument != null && oleLoanDocument.getOlePatron() != null && oleLoanDocument.getOlePatron().getOlePatronId() != null) {
                    Map map = new HashMap();
                    OlePatronDocument olePatronDocument = new OlePatronDocument();
                   /* if (backgroundCheckin) {
                        map.put("barcode", formBarcode);
                        olePatronDocument = getLoanProcessor().getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, map);
                    } else {*/
                    map.put("olePatronId", oleLoanDocument.getPatronId());
                    olePatronDocument = getLoanProcessor().getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, map);
                    /*}*/
                    if (systemParameter.equalsIgnoreCase(OLEConstants.MISSING_PIECE_ITEM_CHECKED_IN_FLAG)) {
                        int noOfMissingPiece = 0;
                        if (oleLoanDocument.getOleItem().isMissingPieceFlag()) {
                            if (oleLoanDocument != null && oleLoanDocument.getMissingPiecesCount() != null && !oleLoanDocument.getMissingPiecesCount().equalsIgnoreCase("")) {
                                noOfMissingPiece = Integer.parseInt(oleLoanDocument.getMissingPiecesCount());
                            }
                        }
                        note = note.replace("[3]", noOfMissingPiece + "");
                    }
                    List<OlePatronNotes> olePatronNotesList = olePatronDocument.getNotes();
                    OlePatronNotes olePatronNotes = new OlePatronNotes();
                    olePatronNotes.setPatronNoteText(note);
                    map.clear();
                    map.put("patronNoteTypeCode", "GENERAL");
                    OlePatronNoteType olePatronNoteType = (OlePatronNoteType) getLoanProcessor().getBusinessObjectService().findByPrimaryKey(OlePatronNoteType.class, map);
                    olePatronNotes.setPatronNoteTypeId(olePatronNoteType.getPatronNoteTypeId());
                    olePatronNotes.setOlePatronId(olePatronDocument.getOlePatronId());
                    olePatronNotesList.add(olePatronNotes);
                    getLoanProcessor().getBusinessObjectService().save(olePatronDocument);
                }
            }

        }


    }

    @RequestMapping(params = "methodToCall=flaggedNoteSave")
    public ModelAndView flaggedNoteSave(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        oleLoanForm.setOleFormKey(oleLoanForm.getFormKey());
        Item oleItem = oleLoanDocument.getOleItem();
       /* if(oleItem==null){*/
        try {
            String itemUUid = oleLoanDocument.getItemUuid();
            String itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
            oleItem = getLoanProcessor().getItemPojo(itemXmlContent);
            itemXmlContent = getLoanProcessor().getItemXML(oleLoanDocument.getItemUuid());
            oleLoanDocument.setOleItem(oleItem);

        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("Error occurred while retrieving item in flaggedNoteSave" + e);
            }
        }

       /* }*/
        if (oleLoanDocument.isItemDamagedStatus()) {
            if (oleLoanForm.isSkipDamagedRecordPopup()) {
                if (oleLoanForm.isCheckoutDamagedRecordFlag()) {
                    oleLoanForm.setMessage(null);
                    saveGeneralNoteForFlaggedItem(OLEConstants.DAMAGED_ITEM_CHECKED_OUT_FLAG, true, oleLoanDocument, true, false, oleLoanForm.isBackGroundCheckIn(), oleLoanForm.getPatronBarcode());
                    oleLoanForm.setCheckoutDamagedRecordFlag(false);
                }
                oleLoanForm.setDisplayDamagedRecordNotePopup(false);
                oleLoanForm.setSuccessMessage(oleLoanDocument.getSuccessMessage());
            } else {
                oleLoanForm.setDisplayDamagedRecordNotePopup(true);
                oleLoanForm.setSkipDamagedRecordPopup(true);
                oleLoanForm.setMessage(null);
                oleLoanForm.setSuccessMessage(null);
                return getUIFModelAndView(form);
            }
        }
        if (oleLoanDocument.isMissingPieceFlag() || (oleLoanDocument.getOleItem() != null && oleLoanDocument.getOleItem().isMissingPieceFlag())) {
            if (oleLoanForm.isSkipMissingPieceRecordPopup()) {
                if (oleLoanForm.isCheckoutMissingPieceRecordFlag()) {
                    oleLoanForm.setMessage(null);
                    saveGeneralNoteForFlaggedItem(OLEConstants.MISSING_PIECE_ITEM_CHECKED_OUT_FLAG, true, oleLoanDocument, true, false, false, oleLoanForm.getPatronBarcode());
                    oleLoanForm.setCheckoutMissingPieceRecordFlag(false);
                }
                oleLoanForm.setDisplayMissingPieceNotePopup(false);
                oleLoanForm.setSuccessMessage(oleLoanDocument.getSuccessMessage());
            } else {
                oleLoanForm.setDisplayMissingPieceNotePopup(true);
                oleLoanForm.setSkipMissingPieceRecordPopup(true);
                oleLoanForm.setMessage(null);
                oleLoanForm.setSuccessMessage(null);
                return getUIFModelAndView(form);
            }
        }
        oleLoanForm.setSkipDamagedRecordPopup(false);
        oleLoanForm.setSkipMissingPieceRecordPopup(false);
        return getUIFModelAndView(oleLoanForm, "PatronItemViewPage");
    }

    @RequestMapping(params = "methodToCall=refreshReturnLoanList")
    public ModelAndView refreshReturnLoanList(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        long begin = System.currentTimeMillis();
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
        List<OleLoanDocument> documentList = oleLoanForm.getItemReturnList();
        if (documentList != null && documentList.size() > 0) {
            OleCirculationDesk oleCirculationDesk = null;
            OleLocation oleLocation = null;
            if (oleLoanDocument.getCirculationLocationId() != null) {
                // oleCirculationDesk = loanProcessor.getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
                try{
                    oleLocation = getCircDeskLocationResolver().getLocationByLocationCode(oleLoanDocument.getItemLocation());
                }
                catch (Exception e){
                    LOG.error("Exception while fetching OleLocation based on item location" +e);
                }
                String routeTo = oleLoanForm.getRouteToLocation() != null ? oleLoanForm.getRouteToLocation() :
                        (oleLoanDocument.getRouteToLocation() != null ? oleLoanDocument.getRouteToLocation() :
                                (oleLocation != null ? oleLocation.getLocationCode() : null));
                documentList.get(0).setRouteToLocation(routeTo);
            }
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside refreshReturnLoanList"+total);
        return getUIFModelAndView(form, "ReturnItemViewPage");
    }

    @RequestMapping(params = "methodToCall=printHoldSlips")
    public ModelAndView printHoldSlips(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        try {
            OlePrintSlip olePrintSlip = new OlePrintSlip();
            if (LOG.isDebugEnabled()) {
                LOG.debug(" Inside Print Hold Slips ");
            }
            String formKey = request.getParameter("formKey");
            OleLoanForm oleLoanForm = (OleLoanForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
            OleLoanDocument oleLoanDocument = oleLoanForm.getDummyLoan();
            OleCirculationDesk oleCirculationDesk = null;
            oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
            olePrintSlip.createHoldSlipPdf(printHoldSlipList, response, oleCirculationDesk);

        } catch (Exception e) {
            LOG.error("Exception while generating printHoldSlips " + e);
        }
        return null;
    }

    @RequestMapping(params = "methodToCall=refreshExport")
    public ModelAndView refreshExport(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm loanForm=(OleLoanForm)form;
        Date checkInDate=loanForm.getCheckInDate();
        start(form,result,request,response);
        loanForm.setCheckInDate(checkInDate);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=showExistingLoan")
    public ModelAndView showExistingLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        try {
            OleLoanForm loanForm = (OleLoanForm) form;
            loanForm.setShowExistingLoan(true);
            if(loanForm.getExistingLoanList().size()==0){
                OleLoanDocument oleLoanDocument = loanForm.getDummyLoan();
                if (oleLoanDocument.getRealPatronBarcode() != null) {
                    if (loanForm.getRealPatronId() != null && (loanForm.getRealPatronId() != null && !loanForm.getRealPatronId().equalsIgnoreCase(""))) {
                        loanForm.setExistingLoanList(getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr
                                (loanForm.getRealPatronId(), null));
                    }
                } else {
                    loanForm.setExistingLoanList(getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr
                            (oleLoanDocument.getPatronId(), null));
                }
                if(loanForm.getLoanList() != null && loanForm.getLoanList().size()>0){
                    for(OleLoanDocument oleLoanDocument1 : loanForm.getLoanList()){
                        if(loanForm.getExistingLoanList().contains(oleLoanDocument1)){
                            loanForm.getExistingLoanList().remove(oleLoanDocument1);
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(loanForm.getExistingLoanList())){
                    for(OleLoanDocument loanDocument : loanForm.getExistingLoanList()){
                        if(StringUtils.isNotEmpty(loanDocument.getRealPatronName())){
                            Map patronMap = new HashMap();
                            patronMap.put(OLEConstants.OlePatron.PATRON_ID, loanDocument.getRealPatronName());
                            OlePatronDocument olePatronDocument = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, patronMap);
                            if(olePatronDocument != null){
                                olePatronDocument = OLEDeliverService.populatePatronName(olePatronDocument);
                                loanDocument.setRealPatronName(olePatronDocument.getPatronName());
                                loanDocument.setProxyPatronBarcode(olePatronDocument.getBarcode());
                                loanDocument.setProxyPatronBarcodeUrl(OLEConstants.ASSIGN_INQUIRY_PATRON_ID + olePatronDocument.getOlePatronId() + OLEConstants.ASSIGN_PATRON_INQUIRY);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUIFModelAndView(form);
    }

    private OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public void setOleLoanDocumentsFromSolrBuilder(OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder) {
        this.oleLoanDocumentsFromSolrBuilder = oleLoanDocumentsFromSolrBuilder;
    }

    @RequestMapping(params = "methodToCall=hideExistingLoan")
    public ModelAndView hideExistingLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm loanForm=(OleLoanForm)form;
        loanForm.setShowExistingLoan(false);
        //loanForm.setExistingLoanList(new ArrayList<OleLoanDocument>());
        return getUIFModelAndView(form);
    }

    public void updateMissingPieceNote(String itemXmlContent,Item oleItem,OleLoanForm oleLoanForm,String itemUuid) throws Exception{

        Map<String,String> map = new HashMap<>();
        map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(itemUuid));
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord>) KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord.class, map, "missingPieceItemId", true);
        List<MissingPieceItemRecord> missingPieceItemRecords = new ArrayList<>();
        for(int index=0 ; index < missingPieceItemRecordList.size() ; index++){
            MissingPieceItemRecord missingPieceItemRecord1 = new MissingPieceItemRecord();
            if(index == missingPieceItemRecordList.size()-1){

                DateFormat dateFormat = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE);
                String missingPieceItemDate = dateFormat.format((new Date()));
                missingPieceItemRecord1.setMissingPieceDate(missingPieceItemDate);
                missingPieceItemRecord1.setMissingPieceCount(oleLoanForm.getDialogMissingPieceCount());
                missingPieceItemRecord1.setPatronBarcode(oleLoanForm.getPatronBarcode());
                missingPieceItemRecord1.setPatronId(oleLoanForm.getPatronId());
                missingPieceItemRecord1.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
                missingPieceItemRecord1.setItemId(oleLoanForm.getMissingPieceLoanDocument().getItemUuid());
                missingPieceItemRecord1.setMissingPieceFlagNote(oleLoanForm.getDialogText());

            } else {
                if (missingPieceItemRecordList.get(index).getMissingPieceDate() != null && !missingPieceItemRecordList.get(index).getMissingPieceDate().toString().isEmpty()) {
                    SimpleDateFormat dateToSimpleDateFormat = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE);
                    Date missingPieceItemDate = null;
                    if(null != (missingPieceItemRecordList.get(index).getMissingPieceDate())){
                        try {
                            missingPieceItemDate = new Date(missingPieceItemRecordList.get(index).getMissingPieceDate().getTime());
                        } catch (ParseException e) {
                            LOG.error("format string to Date " + e);
                        }
                    }
                    missingPieceItemRecord1.setMissingPieceDate(dateToSimpleDateFormat.format(missingPieceItemDate).toString());
                }
                missingPieceItemRecord1.setMissingPieceFlagNote(missingPieceItemRecordList.get(index).getMissingPieceFlagNote());
                missingPieceItemRecord1.setMissingPieceCount(missingPieceItemRecordList.get(index).getMissingPieceCount());
                missingPieceItemRecord1.setOperatorId(missingPieceItemRecordList.get(index).getOperatorId());
                missingPieceItemRecord1.setPatronBarcode(missingPieceItemRecordList.get(index).getPatronBarcode());
                missingPieceItemRecord1.setPatronId(missingPieceItemRecordList.get(index).getPatronId());
                missingPieceItemRecord1.setItemId(missingPieceItemRecordList.get(index).getItemId());
            }
            missingPieceItemRecords.add(missingPieceItemRecord1);
        }
        oleItem.setMissingPieceItemRecordList(missingPieceItemRecords);

    }

    @RequestMapping(params = "methodToCall=restoreSystemDateTime")
    public ModelAndView restoreSystemDateTime(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm loanForm=(OleLoanForm)form;
        DateFormat df = new SimpleDateFormat(OLEConstants.DEFAULT_DATE_FORMAT_24H);
        String currentDate = df.format(getDateTimeService().getCurrentDate());
        if(StringUtils.isNotBlank(currentDate)) {
            String[] currentDateTime = currentDate.split(" ");
            if(currentDateTime != null && currentDateTime.length > 0) {
                loanForm.setCheckInDate(new Date(currentDateTime[0]));
                if(currentDateTime.length > 1) {
                    loanForm.setCheckInTime(currentDateTime[1]);
                }
            }
        }
        return getUIFModelAndView(loanForm);
    }

    public DateTimeService getDateTimeService() {
        return (DateTimeService)SpringContext.getService("dateTimeService");
    }

}