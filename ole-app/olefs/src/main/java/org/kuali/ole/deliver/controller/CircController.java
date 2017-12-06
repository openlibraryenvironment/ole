package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.controller.checkin.CheckinItemController;
import org.kuali.ole.deliver.controller.checkout.CheckoutValidationController;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.controller.renew.RenewController;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.service.LostNoticesExecutor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OleLoanDocumentPlatformAwareDao;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.*;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pvsubrah on 6/3/15.
 */

@Controller
@RequestMapping(value = "/circcontroller")
public class CircController extends CheckoutValidationController {

    private static final Logger LOG = Logger.getLogger(CircController.class);
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;
    private OleLoanDocumentPlatformAwareDao oleLoanDocumentPlatformAwareDao;
    private BulkItemUpdateUtil bulkItemUpdateUtil;
    private RenewController renewController;
    private DateTimeService dateTimeService;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    List<OleLoanDocument> selectedLoanDocumentList = new ArrayList<>();

    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;

        if (!circForm.getPatronBarcode().equals(circForm.getPatronDocument().getBarcode())) {
            sendCheckoutRecipet(circForm);
            resetUI(circForm, result, request, response);
        }

        if (!circForm.isProceedWithCheckout()) {
            circForm.setPatronDocument(new OlePatronRecordUtil().getPatronRecordByBarcode(circForm.getPatronBarcode()));
            return searchPatron(form, result, request, response);
        } else {
            if(Boolean.valueOf(getParameter(OLEParameterConstants.LOAN_WHILE_FASTADD))){
                lookupItemAndSaveLoan(circForm, result, request, response);
            }
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=resetErrorMessages")
    public ModelAndView resetErrorMessages(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        //OleLoanForm oleLoanForm = resetForm(form);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=showExistingLoan")
    public ModelAndView showExistingLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        try {
            CircForm circForm = (CircForm) form;
            int defaultPageSize = getDefaultPageSize();
            circForm.setPageSize(String.valueOf(defaultPageSize));
            circForm.setPageNumber("1");
            circForm.setShowExistingLoan(true);
            if (circForm.getPatronDocument().getOleLoanDocuments().size() > 0) {
                circForm.setExistingLoanList(getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr
                        (circForm.getPatronDocument().getOlePatronId(), null));
            }
            circForm.setLightboxScript("jq('#existingLoanItemTable_length').val(" + defaultPageSize + ").attr('selected', 'selected');enableDataTableForExistingLoanedItem();");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUIFModelAndView(form);
    }

    public int getDefaultPageSize() {
        String defaultPageSizeFromParameter = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_PAGE_SIZE_LOANED_ITEMS);
        int defaultPageSize = 10;
        if(StringUtils.isNotBlank(defaultPageSizeFromParameter)){
            try{
                int searchLimit = Integer.parseInt(defaultPageSizeFromParameter);
                if(searchLimit > 0){
                    defaultPageSize = searchLimit;
                }else{
                    LOG.error("Invalid page size from the system parameter. So taking the default page size : " + defaultPageSize);
                }
            }catch(Exception exception){
                LOG.error("Invalid page size from the system parameter. So taking the default page size : " + defaultPageSize);
            }
        }else{
            LOG.error("Invalid page size from the system parameter. So taking the default page size : " + defaultPageSize);
        }
        return defaultPageSize;
    }

    @RequestMapping(params = "methodToCall=hideExistingLoan")
    public ModelAndView hideExistingLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setShowExistingLoan(false);
        circForm.setExistingLoanList(new ArrayList<OleLoanDocument>());
        return getUIFModelAndView(form);
    }

    // CirDesk Sections
    @RequestMapping(params = "methodToCall=changeCirculationDeskLocation")
    public ModelAndView changeCirculationDeskLocation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response) {
        showDialog("circDeskChangeDialog", form, request, response);
        String lightBoxScript = form.getLightboxScript();
        String circDeskLightBoxScript = lightBoxScript + "jq('#btnOkCircDesk').focus();";
        form.setLightboxScript(circDeskLightBoxScript);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=revertCircDeskLocationSelection")
    public ModelAndView revertCircDeskLocationSelection(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                        HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setSelectedCirculationDesk(circForm.getPreviouslySelectedCirculationDesk());
        return getUIFModelAndView(form, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=setPreviousCircDeskToCurrentlySelectedValue")
    public ModelAndView setPreviousCircDeskToCurrentlySelectedValue(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                                    HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setPreviouslySelectedCirculationDesk(circForm.getSelectedCirculationDesk());
        clearUI(form, result, request, response);
        return getUIFModelAndView(form, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=openClaimsReturnDialog")
    public ModelAndView openClaimsReturnDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            Boolean claimsReturnCancelRequestPopup = ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants
                    .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.WHILE_CLAIMS_RETURN_SHOW_CANCEL_REQUEST_POPUP);
            showDialog("claimsReturnDialog", circForm, request, response);
            ItemRecord itemRecord = getCheckoutUIController(circForm.getFormKey()).getItemRecordByBarcode(selectedLoanDocumentList.get(0).getItemId());
            circForm.setClaimsReturnNote((itemRecord != null) ? itemRecord.getClaimsReturnedNote() : "");
            circForm.setClaimsReturnFlag((itemRecord != null) ? (itemRecord.getClaimsReturnedFlag() != null) ?
                    itemRecord.getClaimsReturnedFlag().booleanValue() : false : false);
            if(claimsReturnCancelRequestPopup){
                circForm.setCancelRequest(request.getParameter("cancelRequest"));
            }else{
                circForm.setCancelRequest("true");
            }

        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to claim.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=checkForRequestExistsDialog")
    public ModelAndView checkForRequestExistsDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        boolean isRequestExists =false;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            Boolean claimsReturnCancelRequestPopup = ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants
                    .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.WHILE_CLAIMS_RETURN_SHOW_CANCEL_REQUEST_POPUP);
            if(claimsReturnCancelRequestPopup){
                for(OleLoanDocument loanDocument : selectedLoanDocumentList){
                    if(getOleDeliverRequestDocumentHelperService().getRequestByItem(loanDocument.getItemId()).size()>0){
                        isRequestExists = true;
                    }
                }
                if(isRequestExists){
                    showDialog("checkForRequestExistsDialog", circForm, request, response);
                }else{
                    openClaimsReturnDialog(form,result,request,response);
                }
            }else {
                openClaimsReturnDialog(form,result,request,response);
            }
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to claim.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=openDamagedItemDialog")
    public ModelAndView openDamagedItemDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            showDialog("damagedItemDialog", circForm, request, response);
            ItemRecord itemRecord = getCheckoutUIController(circForm.getFormKey()).getItemRecordByBarcode(selectedLoanDocumentList.get(0).getItemId());
            circForm.setDamagedItemNote((itemRecord != null) ? itemRecord.getDamagedItemNote() : "");
            circForm.setDamagedItemFlag((itemRecord != null) ? itemRecord.isItemDamagedStatus() : false);
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to damaged.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=openMissingPieceItemDialog")
    public ModelAndView openMissingPieceItemDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            showDialog("missingPieceDialog", circForm, request, response);
            ItemRecord itemRecord = getCheckoutUIController(circForm.getFormKey()).getItemRecordByBarcode(selectedLoanDocumentList.get(0).getItemId());
            circForm.setMissingPieceFlag((itemRecord != null) ? itemRecord.isMissingPieceFlag() : false);
            circForm.setMissingPieceNote(circForm.isMissingPieceFlag() ? (itemRecord != null) ? itemRecord.getMissingPieceFlagNote() : "" : "");
            circForm.setMissingPieceCount(circForm.isMissingPieceFlag() ? (itemRecord != null) ? itemRecord.getMissingPiecesCount() : "" : "");
            circForm.setNumberOfPiece((itemRecord != null) ? itemRecord.getNumberOfPieces() : "");
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to apply missing piece.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=applyClaimsReturn")
    public ModelAndView applyClaimsReturn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        String claimsDescription = request.getParameter("claimDescription");
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            oleLoanDocument.setClaimsReturnNote(claimsDescription);
            oleLoanDocument.setClaimsReturnedIndicator(true);
            oleLoanDocument.setClaimsReturnedDate(new Timestamp(new Date().getTime()));
            fireClaimsReturnedRules(oleLoanDocument);
        }
        createClaimsReturnForItem(circForm, selectedLoanDocumentList, circForm.getPatronDocument(),circForm.getCancelRequest());
        return getUIFModelAndView(form);
    }

    private void fireClaimsReturnedRules(OleLoanDocument oleLoanDocument) {
        CircUtilController circUtilController = new CircUtilController();
        ItemRecord itemRecord = circUtilController.getItemRecordByBarcode(oleLoanDocument.getItemId());
        if (itemRecord != null && !itemRecord.getClaimsReturnedFlag()) {
            oleLoanDocument.setLastClaimsReturnedSearchedDate(null);
            oleLoanDocument.setClaimsSearchCount(0);
            oleLoanDocument.setNoOfClaimsReturnedNoticesSent(0);
            OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, null);
            if (StringUtils.isBlank(oleLoanDocument.getItemFullLocation())) {
                oleLoanDocument.setItemFullLocation(oleItemRecordForCirc.getItemFullPathLocation());
            }
            if (oleItemRecordForCirc != null) {
                DroolsResponse droolsResponse = new DroolsResponse();
                List<Object> facts = new ArrayList<>();
                facts.add(oleItemRecordForCirc);
                facts.add(droolsResponse);
                circUtilController.fireRules(facts, null, "claims returned validation");

                List<OLEDeliverNotice> oleDeliverNoticeList = createNotices(droolsResponse, oleLoanDocument.getPatronId(), oleLoanDocument.getItemId());
                if (CollectionUtils.isNotEmpty(oleDeliverNoticeList)) {
                    oleLoanDocument.getDeliverNotices().addAll(oleDeliverNoticeList);
                }
            }
            sendClaimsReturnedNotice(oleLoanDocument);
        }
    }

    private List<OLEDeliverNotice> createNotices(DroolsResponse droolsResponse, String patronId, String itemId) {
        List<OLEDeliverNotice> oleDeliverNoticeList = new ArrayList<>();
        List<String> noticeTypes = new ArrayList<>();
        noticeTypes.add(OLEConstants.CLAIMS_RETURNED_NOTICE);
        noticeTypes.add(OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE);
        noticeTypes.add(OLEConstants.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE);
        noticeTypes.add(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NOTICE);
        noticeTypes.add(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE);
        noticeTypes.add(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_TITLE);

        for (String noticeType : noticeTypes) {
            String noticeContentConfigName = null;
            if (droolsResponse.getDroolsExchange().getFromContext(noticeType) != null) {
                noticeContentConfigName = (String) droolsResponse.getDroolsExchange().getFromContext(noticeType);
            }
            OLEDeliverNotice oleDeliverNotice = createNotice(noticeType, noticeContentConfigName, patronId, itemId);
            oleDeliverNoticeList.add(oleDeliverNotice);
        }
        return oleDeliverNoticeList;
    }

    private OLEDeliverNotice createNotice(String noticeType, String noticeContentConfigName, String patronId, String itemId) {
        OLEDeliverNotice oleDeliverNotice = new OLEDeliverNotice();
        oleDeliverNotice.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNotice.setPatronId(patronId);
        oleDeliverNotice.setItemBarcode(itemId);
        oleDeliverNotice.setNoticeType(noticeType);
        oleDeliverNotice.setNoticeContentConfigName(noticeContentConfigName);
        return oleDeliverNotice;
    }

    @RequestMapping(params = "methodToCall=removeClaimsReturn")
    public ModelAndView removeClaimsReturn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        deleteClaimsReturnForItem(circForm, selectedLoanDocumentList);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=openAlterDueDateDialog")
    public ModelAndView openAlterDueDateDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) throws JSONException {
        CircForm circForm = (CircForm) form;
        circForm.setErrorMessage(new ErrorMessage());
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            circForm.setLoanDocumentsForAlterDueDate(selectedLoanDocumentList);
            String jsonStringForAlterDueDate = getJsonStringForAlterDueDate(selectedLoanDocumentList);
            showDialogAndRunCustomScript("alterDueDateDialog", circForm,"populateDueDateForAlterDueDateDialog(" + jsonStringForAlterDueDate + ")");
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to alter due date.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }

    private String getJsonStringForAlterDueDate(List<OleLoanDocument> selectedLoanDocumentList) throws JSONException {
        JSONArray finalJsonArray = new JSONArray();
        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            JSONObject valueObject = new JSONObject();
            valueObject.put("itemBarcode",oleLoanDocument.getItemId());
            valueObject.put("date", getDateString(oleLoanDocument.getLoanDueDate()));
            valueObject.put("dateTime", getTimeString(oleLoanDocument.getLoanDueDate()));
            finalJsonArray.put(valueObject);
        }
        return finalJsonArray.toString();
    }

    private String getTimeString(Timestamp loanDueDate) {
        if (null != loanDueDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            return simpleDateFormat.format(loanDueDate);
        }
        return "";
    }

    private String getDateString(Timestamp loanDueDate) {
        if (null != loanDueDate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConfigContext.getCurrentContextConfig().getProperty("DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE"));
            return simpleDateFormat.format(loanDueDate);
        }
        return "";
    }

    @RequestMapping(params = "methodToCall=alterDueDate")
    public ModelAndView alterDueDate(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        String jsonString = request.getParameter("alterDueDateObjectValues");

        AlterDueDateAndRenewResponse alterDueDateAndRenewResponse = processJsonString(jsonString);

        String failureResponseMessage = prepareFailureResponseForInvalidDueDate(alterDueDateAndRenewResponse.getInvalidItemIdsMap(), "Invalid time format selected.");

        if(StringUtils.isNotBlank(failureResponseMessage)){
            circForm.getErrorMessage().setErrorMessage(failureResponseMessage);
        }

        try {
            AlterDueDateAndRenewResponse alterDueDateAndRenewResponseAfterSave = updateDueDate(alterDueDateAndRenewResponse.getValidItemIdsMap(), circForm);
            String failureResponseForRenew = prepareFailureResponseForInvalidDueDate(alterDueDateAndRenewResponseAfterSave.getInvalidItemIdsMap(), "");

            if(StringUtils.isNotBlank(failureResponseForRenew)){
                circForm.getErrorMessage().setErrorMessage(failureResponseForRenew);
            }

            //TODO: process invalid item ids; Set error message on circ message.
        } catch (Exception e) {
            //TODO: handle exception
            circForm.getErrorMessage().setErrorMessage("Alter due date was failed ");
            e.printStackTrace();
        }

        if(null != circForm.getErrorMessage() && StringUtils.isNotBlank(circForm.getErrorMessage().getErrorMessage())){
            showDialogAndRunCustomScript("generalInfoDialog", circForm, "jq('.loanedItemCBClass').removeAttr('checked');jq('.loaningItemCBClass').removeAttr('checked');");
        }else{
            circForm.setLightboxScript("jq('.loanedItemCBClass').removeAttr('checked');jq('.loaningItemCBClass').removeAttr('checked');");
        }
        return getUIFModelAndView(circForm);
    }

    private String prepareFailureResponseForInvalidDueDate(Map<String, String[]> invalidItemIdsMap, String customMsg) {
        CircUtilController circUtilController = new CircUtilController();
        StringBuilder stringBuilder = new StringBuilder();
        if(invalidItemIdsMap.size() >  0 ){
            for (Iterator<String> iterator = invalidItemIdsMap.keySet().iterator(); iterator.hasNext(); ) {
                String itemBarcode = iterator.next();
                circUtilController.addToFormattedContent(stringBuilder, "Alter due date was not successful for item (" + itemBarcode + " )." + customMsg);
            }
        }
        return stringBuilder.toString();
    }

    private AlterDueDateAndRenewResponse processJsonString(String jsonString) {
        HashMap<String, String[]> validItemIdsMap = new HashMap();
        HashMap<String, String[]> invalidItemIdsMap = new HashMap();
        CircUtilController circUtilController = new CircUtilController();

        JSONArray alterDueDateObjectValues = null;
        try {
            alterDueDateObjectValues = new JSONArray(jsonString);
            for (int index = 0; index < alterDueDateObjectValues.length(); index++) {
                JSONObject jsonObject = (JSONObject) alterDueDateObjectValues.get(index);
                String itemBarcode = getStringValueFromJsonObject(jsonObject, "itemBarcode");
                String[] alterDueDateTime = new String[2];
                alterDueDateTime[0] = getStringValueFromJsonObject(jsonObject, "date");
                alterDueDateTime[1] = getStringValueFromJsonObject(jsonObject, "dateTime");
                if (circUtilController.validateTime(alterDueDateTime[1])) {
                    validItemIdsMap.put(itemBarcode, alterDueDateTime);
                } else {
                    invalidItemIdsMap.put(itemBarcode, alterDueDateTime);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlterDueDateAndRenewResponse alterDueDateAndRenewResponse = new AlterDueDateAndRenewResponse();
        alterDueDateAndRenewResponse.setValidItemIdsMap(validItemIdsMap);
        alterDueDateAndRenewResponse.setInvalidItemIdsMap(invalidItemIdsMap);
        return alterDueDateAndRenewResponse;
    }


    public String getStringValueFromJsonObject(JSONObject jsonObject, String key) {
        String returnValue = null;
        try {
            returnValue = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private AlterDueDateAndRenewResponse updateDueDate(Map<String, String[]> itemBarcodeMap, CircForm circForm) throws Exception {
        HashMap<String, String[]> validItemIdsMap = new HashMap();
        HashMap<String, String[]> invalidItemIdsMap = new HashMap();

        List<OleLoanDocument> selectedLoanDocumentList = filterListForInvalidDueDateItems(itemBarcodeMap, getSelectedLoanDocumentList(circForm));
        if(selectedLoanDocumentList.size()>0){
            List<OleLoanDocument> selectedLoanDocumentListForCurrentSession = circForm.getLoanDocumentListForCurrentSession();
            for(OleLoanDocument loanDocument : selectedLoanDocumentList){
                if(selectedLoanDocumentListForCurrentSession.contains(loanDocument)) {
                    int i = 0;
                    for(OleLoanDocument loanDocument1 : selectedLoanDocumentListForCurrentSession){
                        if(loanDocument1.equals(loanDocument)){
                            circForm.getLoanDocumentListForCurrentSession().set(i,loanDocument);
                        }
                        i++;
                    }
                }else{
                    circForm.getLoanDocumentListForCurrentSession().add(loanDocument);
                }
            }
        }
        List<OleLoanDocument> loanDocumentsToBeUpdatedInDb = filterListForInvalidDueDateItems(itemBarcodeMap, getSelectedLoanDocumentList(circForm));
        CircUtilController circUtilController = new CircUtilController();

        Map<String, Map> docstoreResultMap = alterDueDateInDocstore(circForm, itemBarcodeMap, selectedLoanDocumentList);
        for (Iterator<String> iterator = docstoreResultMap.keySet().iterator(); iterator.hasNext(); ) {
            String itemUuid = iterator.next();
            Map result = docstoreResultMap.get(itemUuid);
            DocstoreDocument.ResultType resultType = (DocstoreDocument.ResultType) result.get("result");
            OleLoanDocument loanDocument = getCheckoutUIController(circForm.getFormKey()).getLoanDocumentFromListBasedOnItemUuid(itemUuid, selectedLoanDocumentList);
            if (resultType.equals(DocstoreDocument.ResultType.SUCCESS)) {
                if (null != loanDocument) {
                    String[] dueDateAndTime = itemBarcodeMap.get(loanDocument.getItemId());
                    String dateString = dueDateAndTime[0];
                    Timestamp newDueDate = null;
                    if (StringUtils.isNotBlank(dateString)) {
                        if(StringUtils.isBlank(dueDateAndTime[1])) {
                            dueDateAndTime[1] =ParameterValueResolver.getInstance().getParameter(OLEConstants
                                    .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_TIME_FOR_DUE_DATE);
                            if(StringUtils.isBlank(dueDateAndTime[1])){
                                dueDateAndTime[1] = new CircUtilController().getDefaultClosingTime(loanDocument,getDateFromString(dateString));
                            }
                        }
                        newDueDate = getCheckoutUIController(circForm.getFormKey()).processDateAndTimeForAlterDueDate(getDateFromString(dateString), dueDateAndTime[1]);
                    }
                    loanDocument.setLoanDueDate(newDueDate);
                    circUtilController.updateNoticesForLoanDocument(loanDocument);
                    validItemIdsMap.put(loanDocument.getItemId(), dueDateAndTime);
                }
            } else {
                loanDocumentsToBeUpdatedInDb.remove(loanDocument);
                invalidItemIdsMap.put(loanDocument.getItemId(), null);
                LOG.error("Alter due date was not successful for itemUUID : " + itemUuid);
            }
        }
        saveLoanDocumentsToDb(loanDocumentsToBeUpdatedInDb);
        //alterDueDateForLoanInDB(loanDocumentsToBeUpdatedInDb);
        //TODO: Handle rollback in doscstore if db update fails.
        AlterDueDateAndRenewResponse alterDueDateAndRenewResponse = new AlterDueDateAndRenewResponse();
        alterDueDateAndRenewResponse.setValidItemIdsMap(validItemIdsMap);
        alterDueDateAndRenewResponse.setInvalidItemIdsMap(invalidItemIdsMap);

        return alterDueDateAndRenewResponse;
    }

    private void saveLoanDocumentsToDb(List<OleLoanDocument> loanDocumentsToBeUpdatedInDb) {
        BusinessObjectService oleBusinessObjectService = (BusinessObjectService) SpringContext.getService("oleBusinessObjectService");
        oleBusinessObjectService.save(loanDocumentsToBeUpdatedInDb);
    }

    private Date getDateFromString(String dateString) throws ParseException {
        return getDateTimeService().convertToDate(dateString);
    }


    private List<OleLoanDocument> filterListForInvalidDueDateItems(Map<String, String[]> itemBarcodeMap, List<OleLoanDocument> selectedLoanDocumentList) {
        ArrayList<OleLoanDocument> filteredDocuments = new ArrayList<>();
        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            if (itemBarcodeMap.containsKey(oleLoanDocument.getItemId())) {
                filteredDocuments.add(oleLoanDocument);
            }
        }

        return filteredDocuments;
    }

    private Map<String, Map> alterDueDateInDocstore(CircForm circForm, Map<String, String[]> itemIdMap, List<OleLoanDocument> selectedLoanDocumentList) throws Exception {
        List<Item> itemList = new ArrayList<>();
        for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
            Map<String, Object> valueMap = new HashMap<>();
            OleLoanDocument oleLoanDocument = iterator.next();
            String[] dueDateAndTime = itemIdMap.get(oleLoanDocument.getItemId());
            String dateString = dueDateAndTime[0];
            Timestamp newDueDate = null;
            if (StringUtils.isNotBlank(dateString)) {
                if(StringUtils.isBlank(dueDateAndTime[1])) {
                    dueDateAndTime[1] = ParameterValueResolver.getInstance().getParameter(OLEConstants
                            .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_TIME_FOR_DUE_DATE);
                    if(StringUtils.isBlank(dueDateAndTime[1])){
                        dueDateAndTime[1] = new CircUtilController().getDefaultClosingTime(oleLoanDocument, getDateFromString(dateString));
                    }
                }
                newDueDate = getCheckoutUIController(circForm.getFormKey()).processDateAndTimeForAlterDueDate(getDateFromString(dateString), dueDateAndTime[1]);
            }
            valueMap.put("loanDueDate", newDueDate);
            valueMap.put("itemUUID", oleLoanDocument.getItemUuid());
            Item itemForUpdate = getCheckoutUIController(circForm.getFormKey()).getItemForUpdate(valueMap);
            if (null != itemForUpdate) {
                itemList.add(itemForUpdate);
            }

        }
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        Map<String, Map> docstoreResultMap = getBulkItemUpdateUtil().updateItemsToDocstore(itemList);
        oleStopWatch.end();
        System.out.println("Time taken to batch update item loan due dates in solr : " + oleStopWatch.getTotalTime());
        return docstoreResultMap;
    }

    private void alterDueDateForLoanInDB(List<OleLoanDocument> oleLoanDocuments) {
        List<BulkUpdateDataObject> bulkUpdateDataObjects = new ArrayList<>();
        for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            BulkUpdateDataObject bulkUpdateDataObject = new BulkUpdateDataObject();
            HashMap setClauseMap = new HashMap();
            setClauseMap.put("CURR_DUE_DT_TIME", oleLoanDocument.getLoanDueDate());
            bulkUpdateDataObject.setSetClauseMap(setClauseMap);
            HashMap whereClauseMap = new HashMap();
            whereClauseMap.put("ITM_ID", oleLoanDocument.getItemId());
            bulkUpdateDataObject.setWhereClauseMap(whereClauseMap);
            bulkUpdateDataObjects.add(bulkUpdateDataObject);
        }

        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        int[] updatedCount = getOleLoanDocumentPlatformAwareDao().updateLoanDocument(bulkUpdateDataObjects);
        oleStopWatch.end();
        System.out.println("Time taken to batch update loan documents in DB : " + oleStopWatch.getTotalTime());
        System.out.println(Arrays.toString(updatedCount));
    }

    public List<OleLoanDocument> getSelectedLoanDocumentList(CircForm circForm) {
        List<OleLoanDocument> selectedLoanList = new ArrayList<>();
        List<OleLoanDocument> loanList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(circForm.getExistingLoanList()))
            loanList.addAll(circForm.getExistingLoanList());
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentListForCurrentSession()))
            loanList.addAll(circForm.getLoanDocumentListForCurrentSession());
        if (CollectionUtils.isNotEmpty(loanList)) {
            for (Iterator<OleLoanDocument> iterator = loanList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                if (oleLoanDocument.isCheckNo() && !selectedLoanList.contains(oleLoanDocument)) {
                    selectedLoanList.add(oleLoanDocument);
                }
            }
        }
        return selectedLoanList;
    }

    @RequestMapping(params = "methodToCall=applyDamagedItem")
    public ModelAndView applyDamagedItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        String damageItemDescription = request.getParameter("damageItemDescription");
        List<OleLoanDocument> loanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                oleLoanDocument.setItemDamagedNote(damageItemDescription);
                oleLoanDocument.setDamagedItemIndicator(true);
                oleLoanDocument.setDamagedItemDate(new Timestamp(new Date().getTime()));
            }
            createDamagedItem(circForm, loanDocumentList, circForm.getPatronDocument());
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=removeDamagedItem")
    public ModelAndView removeDamagedItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        deleteDamagedItem(circForm, selectedLoanDocumentList);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=removeMissingPieceItem")
    public ModelAndView removeMissingPieceItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        deleteMissingPieceItem(circForm, selectedLoanDocumentList);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=applyMissingPieceItem")
    public ModelAndView applyMissingPieceItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        String missingPieceCnt = request.getParameter("missingPieceCnt");
        String noOfPieces = request.getParameter("noOfPieces");
        String missingPieceNote = request.getParameter("missingPieceNte");
        List<OleLoanDocument> loanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                oleLoanDocument.setMissingPiecesCount(missingPieceCnt);
                oleLoanDocument.setMissingPieceFlag(true);
                oleLoanDocument.setMissingPieceNote(missingPieceNote);
                oleLoanDocument.setNoOfMissingPiece(noOfPieces);
                oleLoanDocument.setMissingPieceItemDate(new Timestamp(new Date().getTime()));
            }
            createMissingPieceItem(circForm, loanDocumentList, circForm.getPatronDocument());
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=renew")
    public ModelAndView renew(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {

        CircForm circForm = (CircForm) form;

        if (null != circForm.getLoanDocumentsForRenew()) {
            circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
        }
        circForm.setErrorMessage(new ErrorMessage());
        selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            for(OleLoanDocument oleLoanDocument : selectedLoanDocumentList) {
                if(oleLoanDocument.getItemStatus().equals("LOST")) {
                    return showDialogAndRunCustomScript("renewLostItemsDialogMsg", form, "jq('#renewOptionsProceed').focus()");
                }
            }

        }
        return continueRenew(form,result,request,response);
    }

    @RequestMapping(params = "methodToCall=continueRenew")
    public ModelAndView continueRenew(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {

        CircForm circForm = (CircForm) form;

        if (null != circForm.getLoanDocumentsForRenew()) {
            circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
        }
        circForm.setErrorMessage(new ErrorMessage());
        //List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {

            DroolsResponse droolsResponse = getRenewController().renewItems(selectedLoanDocumentList, circForm.getPatronDocument());

            String messageContentForRenew = getRenewController().analyzeRenewedLoanDocuments(circForm, droolsResponse);

            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage(messageContentForRenew);
            circForm.setErrorMessage(errorMessage);

            if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentsForRenew())) {
                showDialogAndRunCustomScript("renewOverrideDialog", circForm, "jq('.renewItemCBClass').removeAttr('checked');jq('.renewItemCBClass').removeAttr('checked');");
            } else if (StringUtils.isNotBlank(messageContentForRenew)) {
                showDialogAndRunCustomScript("generalInfoWithRefreshDialog", circForm, "jq('.loanedItemCBClass').removeAttr('checked');jq('.loaningItemCBClass').removeAttr('checked');");
                circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
            }
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to renew.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }

        return getUIFModelAndView(form);
    }

    private void processJsonStringForRenew(CircForm circForm, String jsonString, List<OleLoanDocument> selectedLoanDocumentForRenewOverride) {
        JSONArray renewObjectValues = null;
        try {
            renewObjectValues = new JSONArray(jsonString);
            for (int index = 0; index < renewObjectValues.length(); index++) {
                JSONObject jsonObject = (JSONObject) renewObjectValues.get(index);
                String itemBarcode = getStringValueFromJsonObject(jsonObject, "itemBarcode");
                OleLoanDocument loanDocument = getCheckoutUIController(circForm.getFormKey()).getLoanDocumentFromListBasedOnItemBarcode(itemBarcode, selectedLoanDocumentForRenewOverride);
                if (null != loanDocument) {
                    loanDocument.setRenewCheckNo(true);
                    if (loanDocument.isNonCirculatingItem()) {
                        String dateString = getStringValueFromJsonObject(jsonObject, "date");
                        String dateTimeString = getStringValueFromJsonObject(jsonObject, "dateTime");
                        Timestamp timestamp = null;
                        try {
                            timestamp = getCheckoutUIController(circForm.getFormKey()).processDateAndTimeForAlterDueDate(new DateUtil().getDate(dateString), dateTimeString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loanDocument.setLoanDueDate(timestamp);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(params = "methodToCall=overrideRenewItems")
    public ModelAndView overrideRenewItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;

        String jsonString = request.getParameter("renewObjectValues");
        processJsonStringForRenew(circForm, jsonString, circForm.getLoanDocumentsForRenew());

        List<OleLoanDocument> circulatingLoanDocumentsForRenew = new ArrayList<>();
        List<OleLoanDocument> nonCirculatingLoanDocumentsForRenew = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentsForRenew())) {
            for (Iterator<OleLoanDocument> iterator = circForm.getLoanDocumentsForRenew().iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                if (oleLoanDocument.isRenewCheckNo()) {
                    if (oleLoanDocument.isNonCirculatingItem()) {
                        nonCirculatingLoanDocumentsForRenew.add(oleLoanDocument);
                    } else {
                        circulatingLoanDocumentsForRenew.add(oleLoanDocument);
                    }
                }
            }
            String responseForNonCirculatingItem = null;
            if (CollectionUtils.isNotEmpty(nonCirculatingLoanDocumentsForRenew)) {
                DroolsResponse droolsResponseForNonCirculatingItem = getRenewController().proceedToSaveLoanDocuments(nonCirculatingLoanDocumentsForRenew);
                responseForNonCirculatingItem = getRenewController().prepareResponseForRenewedLoanDocuments(droolsResponseForNonCirculatingItem);
            }

            String responseForCirculatingItem = null;
            if (CollectionUtils.isNotEmpty(circulatingLoanDocumentsForRenew)) {
                DroolsResponse droolsResponseForCirculatingItem = getRenewController().proceedToSaveLoanDocuments(circulatingLoanDocumentsForRenew);
                responseForCirculatingItem = getRenewController().prepareResponseForRenewedLoanDocuments(droolsResponseForCirculatingItem);
            }

            if (StringUtils.isNotBlank(responseForNonCirculatingItem)) {
                getRenewController().prepareResponeMessage(circForm, responseForNonCirculatingItem);
            }
            if (StringUtils.isNotBlank(responseForCirculatingItem)) {
                getRenewController().prepareResponeMessage(circForm, responseForCirculatingItem);
            }
        }

        if (CollectionUtils.isNotEmpty(nonCirculatingLoanDocumentsForRenew) && CollectionUtils.isNotEmpty(circulatingLoanDocumentsForRenew)) {
            circForm.getLoanDocumentsForRenew().clear();
        }

        if (null != circForm.getErrorMessage() && StringUtils.isNotBlank(circForm.getErrorMessage().getErrorMessage())) {
            showDialog("generalInfoDialog", circForm, request, response);
        }

        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=doNotRenewalItem")
    public ModelAndView doNotRenewalItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        if (null != circForm.getErrorMessage() && StringUtils.isNotBlank(circForm.getErrorMessage().getErrorMessage())) {
            showDialog("generalInfoDialog", circForm, request, response);
        } else {
            executeCustomScriptAfterClosingLightBox(circForm, "jq.fancybox.close(); jq('#checkoutItem_control').focus();");
        }
        circForm.getLoanDocumentsForRenew().clear();
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=newPage")
    public ModelAndView showNewPage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setPageNumber(request.getParameter("pageNumber"));
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=newPageCheckoutDialog")
    public ModelAndView showNewPageCheckoutDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setPageNumber(request.getParameter("pageNumber"));
        String overrideParameters = "{closeBtn:false,autoSize : false}";
        return showDialogWithOverrideParameters("proxyListCheckoutDialog", circForm,overrideParameters);
    }


    @RequestMapping(params = "methodToCall=newSizeCheckoutDialog")
    public ModelAndView showNewPageSizeCheckoutDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setPageSize(request.getParameter("pageSize"));
        String overrideParameters = "{closeBtn:false,autoSize : false}";
        return showDialogWithOverrideParameters("proxyListCheckoutDialog", circForm, overrideParameters);
    }


    @RequestMapping(params = "methodToCall=newSize")
    public ModelAndView showNewPageSize(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setPageSize(request.getParameter("pageSize"));
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=selectAll")
    public ModelAndView selectAll(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        for (OleLoanDocument oleLoanDocument : circForm.getExistingLoanList()) {
            oleLoanDocument.setCheckNo(true);
        }

        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=deselectAll")
    public ModelAndView deselectAll(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        for (OleLoanDocument oleLoanDocument : circForm.getExistingLoanList()) {
            oleLoanDocument.setCheckNo(false);
        }

        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=proceedForRenew")
    public ModelAndView proceedForRenew(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        if (null != circForm.getLoanDocumentsForRenew()) {
            circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
        }
        circForm.setErrorMessage(new ErrorMessage());

        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) circForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");
        if (oleItemRecordForCirc != null && oleItemRecordForCirc.getItemStatusRecord() != null && OLEConstants.ITEM_STATUS_LOST.equalsIgnoreCase(oleItemRecordForCirc.getItemStatusRecord().getCode())) {
            showDialog("renewLostItemDialogMsg", circForm, request, response);
        } else {
            renewItem(circForm, request, response);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=proceedForLostItemRenew")
    public ModelAndView proceedForLostItemRenew(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        renewItem(circForm, request, response);
        return getUIFModelAndView(form);
    }

    private void renewItem(CircForm circForm, HttpServletRequest request, HttpServletResponse response) {
        OleLoanDocument currentLoanDocument = getCheckoutUIController(circForm.getFormKey()).getCurrentLoanDocument(circForm.getItemBarcode());
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) circForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");
        if (oleItemRecordForCirc != null && oleItemRecordForCirc.getItemStatusRecord() != null) {
            currentLoanDocument.setItemStatus(oleItemRecordForCirc.getItemStatusRecord().getCode());
        }
        if (currentLoanDocument != null) {
            DroolsResponse droolsResponse = getRenewController().renewItems(Arrays.asList(currentLoanDocument), circForm.getPatronDocument());

            String messageContentForRenew = getRenewController().analyzeRenewedLoanDocuments(circForm, droolsResponse);

            ErrorMessage errorMessage = new ErrorMessage();

            if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentsForRenew())) {
                errorMessage.setErrorMessage(circForm.getLoanDocumentsForRenew().get(0).getErrorMessage());
                circForm.setErrorMessage(errorMessage);
                showDialog("checkoutRenewCustomDueDateConfirmationDialog", circForm, request, response);
            } else if (StringUtils.isNotBlank(messageContentForRenew)) {
                errorMessage.setErrorMessage(messageContentForRenew);
                circForm.setErrorMessage(errorMessage);
                showDialog("generalInfoWithRefreshDialog", circForm, request, response);
                circForm.setLoanDocumentsForRenew(new ArrayList<OleLoanDocument>());
            }
        }
    }

    @RequestMapping(params = "methodToCall=overrideCheckoutRenewItems")
    public ModelAndView overrideCheckoutRenewItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        circForm.getErrorMessage().setErrorMessage(null);
        String customDate = request.getParameter("renewCustomDueDateMap");
        String customDateTime = request.getParameter("renewCustomDueDateTime");
        if (StringUtils.isNotBlank(customDate)) {
            circForm.setCustomDueDateMapForRenew(new DateUtil().getDate(customDate));
        }
        circForm.setCustomDueDateTimeForRenew(customDateTime);
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentsForRenew())) {
            String responseForRenewItem = null;
            Timestamp timestamp = getCheckoutUIController(circForm.getFormKey
                    ()).processDateAndTimeForAlterDueDate(circForm.getCustomDueDateMapForRenew(), circForm.getCustomDueDateTimeForRenew());
            circForm.getLoanDocumentsForRenew().get(0).setLoanDueDate(timestamp);
            DroolsResponse droolsResponseForRenewItem = getRenewController().proceedToSaveLoanDocuments(circForm.getLoanDocumentsForRenew());
            responseForRenewItem = getRenewController().prepareResponseForRenewedLoanDocuments(droolsResponseForRenewItem);
            getRenewController().prepareResponeMessage(circForm, responseForRenewItem);
        }
        circForm.getLoanDocumentsForRenew().clear();
        if (null != circForm.getErrorMessage() && StringUtils.isNotBlank(circForm.getErrorMessage().getErrorMessage())) {
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=cancelOverrideCheckoutRenewItems")
    public ModelAndView cancelOverrideCheckoutRenewItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        circForm.getErrorMessage().setErrorMessage(null);
        circForm.getLoanDocumentsForRenew().clear();
        circForm.getCustomDueDateMapForRenew();
        circForm.getCustomDueDateTimeForRenew();
        circForm.getCustomDueDateTimeMessageForRenew();
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=openItemLostDialog")
    public ModelAndView openItemLostDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = selectedLoanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                if (oleLoanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST)) {
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setErrorMessage("The selected item is already billed as Lost.");
                    circForm.setErrorMessage(errorMessage);
                    showDialog("generalInfoDialog", circForm, request, response);
                    return getUIFModelAndView(form);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            showDialog("itemLostDialog", circForm, request, response);
            if (StringUtils.isNotBlank(request.getParameter("cancelRequest"))) {
                circForm.setCancelRequest(request.getParameter("cancelRequest"));
            } else {
                circForm.setCancelRequest("false");
            }
            ItemRecord itemRecord = getCheckoutUIController(circForm.getFormKey()).getItemRecordByBarcode(selectedLoanDocumentList.get(0).getItemId());
            circForm.setItemLostNote((itemRecord != null) ? itemRecord.getItemLostNote() : "");
        } else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item to Lost.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=applyItemLost")
    public ModelAndView applyItemLost(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        String itemLostDescription = request.getParameter("itemLostDescription");
        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        ExecutorService lostNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);
        List<OleLoanDocument> loanDocumentList = getSelectedLoanDocumentList(circForm);
        OLEDeliverNotice oleDeliverNotice;
         if (loanDocumentList.size() > 0) {
            List<OleLoanDocument> loanDocuments = new ArrayList<>();
            for (OleLoanDocument oleLoanDocument : loanDocumentList) {
                String itemFullLocation = getItemFullLocation(oleLoanDocument.getItemId());
                if(itemFullLocation != null) {
                    oleLoanDocument.setItemFullLocation(itemFullLocation);
                    oleLoanDocument.setItemLocation(itemFullLocation);
                }
                oleLoanDocument.setItemTypeDesc(new OleDeliverRequestDocumentHelperServiceImpl().getItemTypeDescByCode(oleLoanDocument.getItemType()));
                oleDeliverNotice = getLostNotice(oleLoanDocument.getDeliverNotices());
                if(oleDeliverNotice!=null)
                oleLoanDocument.setItemLostNote(itemLostDescription);
                oleLoanDocument.getDeliverNotices().clear();
                if(oleDeliverNotice!=null){
                    oleLoanDocument.getDeliverNotices().add(oleDeliverNotice);
                }
                oleLoanDocument.setIsManualBill(true);
                oleLoanDocument.setItemStatus(OLEConstants.ITEM_STATUS_LOST);
                if (circForm.getCancelRequest() != null && circForm.getCancelRequest().equalsIgnoreCase("true")) {
                    new OleDeliverRequestDocumentHelperServiceImpl().cancelPendingRequestForClaimsReturnedItem(oleLoanDocument.getItemUuid());
                }
                loanDocuments.add(oleLoanDocument);
            }
            Map lostMap = new HashMap();
            lostMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, OLEConstants.LOST_NOTICE);
            lostMap.put(OLEConstants.LOAN_DOCUMENTS, loanDocumentList);
            Runnable deliverLostNoticesExecutor = new LostNoticesExecutor(lostMap);
            lostNoticesExecutorService.execute(deliverLostNoticesExecutor);
           for(OleLoanDocument loanDocument : loanDocumentList) {
                        Map map = new HashMap();
                map.put("loanId",loanDocument.getLoanId());
                getBusinessObjectService().deleteMatching(OLEDeliverNotice.class,map);
            }
        }
        if(!lostNoticesExecutorService.isShutdown()) {
            lostNoticesExecutorService.shutdown();
        }
        return getUIFModelAndView(circForm);
    }


    @RequestMapping(params = "methodToCall=applyItemLostReplace")
    public ModelAndView applyItemLostReplace(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        String itemReplaceDescription = request.getParameter("itemReplaceDescription");
        List<OleLoanDocument> loanDocumentList = getSelectedLoanDocumentList(circForm);
        CheckinItemController checkinItemController = new CheckinItemController();
        if (loanDocumentList.size() > 0) {
            List<OleLoanDocument> loanDocuments = new ArrayList<>();
            for (OleLoanDocument oleLoanDocument : loanDocumentList) {
                OleLoanDocument loanDocument = getLoanDocument(oleLoanDocument.getItemId());
                oleLoanDocument.setItemStatus(OLEConstants.ITEM_STATUS_LOST_AND_PAID);
                if(circForm.getCancelRequest()!=null && circForm.getCancelRequest().equalsIgnoreCase("true")){
                    new OleDeliverRequestDocumentHelperServiceImpl().cancelPendingRequestForClaimsReturnedItem(loanDocument.getItemUuid());
                    loanDocument.setDeliverNotices(null);
                }
                loanDocument.setItemReplaceNote(itemReplaceDescription);
                loanDocuments.add(loanDocument);
                CheckinForm checkinForm = new CheckinForm();
                checkinForm.setItemBarcode(loanDocument.getItemId());
                checkinForm.setSelectedCirculationDesk(circForm.getSelectedCirculationDesk());
                checkinForm.setCustomDueDateMap(new Date());
                checkinItemController.getCheckinUIController(checkinForm).checkin(checkinForm);
                loanDocument.setItemStatus(OLEConstants.ITEM_STATUS_LOST_AND_PAID);
                checkinItemController.getCheckinUIController(checkinForm).
                        processCheckinAfterPreValidation((ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord"), checkinForm,loanDocument);
            }
            for(OleLoanDocument loanDocument : loanDocumentList) {
                        Map map = new HashMap();
                map.put("loanId",loanDocument.getLoanId());
                getBusinessObjectService().deleteMatching(OLEDeliverNotice.class,map);
            }
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=checkForRequestExistsLostItem")
    public ModelAndView checkForRequestExistsLostItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        boolean isRequestExists =false;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if(selectedLoanDocumentList.size()>0) {
            for (OleLoanDocument loanDocument : selectedLoanDocumentList) {
                if (getOleDeliverRequestDocumentHelperService().getRequestByItem(loanDocument.getItemId()).size() > 0) {
                    isRequestExists = true;
                }
            }
            if (isRequestExists) {
                showDialog("checkForRequestExistsLostItemDialog", circForm, request, response);
            } else {
                openItemLostDialog(circForm, result, request, response);
            }
        }else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please select any one of loaned item.");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(circForm);
    }


    @RequestMapping(params = "methodToCall=showItemLostReplace")
    public ModelAndView showItemLostReplace(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        if(StringUtils.isNotBlank(request.getParameter("cancelRequest"))) {
            circForm.setCancelRequest(request.getParameter("cancelRequest"));
        }else{
            circForm.setCancelRequest("false");
        }
        showDialog("itemReplaceDialog", form, request, response);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=checkForRequestExistsLostItemReplaceCopy")
    public ModelAndView checkForRequestExistsLostItemReplaceCopy(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        boolean isRequestExists = false;
        boolean isItemLost = true;
        boolean isBillValid = false;
        List<OleLoanDocument> selectedLoanDocumentList = getSelectedLoanDocumentList(circForm);
        if(CollectionUtils.isNotEmpty(selectedLoanDocumentList)) {
            for (OleLoanDocument loanDocument : selectedLoanDocumentList) {
                List<FeeType> feeTypes = loanDocument.getOlePatron().getPatronFeeTypes();
                if(!loanDocument.getItemStatus().equalsIgnoreCase(OLEConstants.ITEM_STATUS_LOST)){
                    isItemLost = false;
                    break;
                }  else {
                    for(FeeType feeType : feeTypes){
                        if(feeType.getPaymentStatusCode().equalsIgnoreCase(OLEConstants.PAY_OUTSTANDING) && feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE) && feeType.getItemBarcode().equalsIgnoreCase(loanDocument.getItemId())){
                            isBillValid = true;
                            break;
                        }
                    }
                    if (getOleDeliverRequestDocumentHelperService().getRequestByItem(loanDocument.getItemId()).size() > 0) {
                        isRequestExists = true;
                    }
                }
            }
            if (!isItemLost){
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setErrorMessage("Select only the Lost items to replace.");
                circForm.setErrorMessage(errorMessage);
                showDialog("generalInfoDialog", circForm, request, response);
            } else if (!isBillValid){
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setErrorMessage("selected item bill not outstanding.");
                circForm.setErrorMessage(errorMessage);
                showDialog("generalInfoDialog", circForm, request, response);
            }
            else if (isRequestExists) {
                showDialog("checkForRequestExistsLostItemReplaceCopyDialog", circForm, request, response);
            } else {
                showDialog("itemReplaceDialog", circForm, request, response);
            }
        }else {
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Select the Lost items to replace..");
            circForm.setErrorMessage(errorMessage);
            showDialog("generalInfoDialog", circForm, request, response);
        }
        return getUIFModelAndView(circForm);
    }

    public OLEDeliverNotice getLostNotice(List<OLEDeliverNotice> oleDeliverNotices){
        OLEDeliverNotice oleDeliverNotice = null;
        if(oleDeliverNotices!=null && oleDeliverNotices.size()>0){
            for(OLEDeliverNotice oleDeliverNotice1 : oleDeliverNotices){
                if(oleDeliverNotice1.getNoticeType().equals("Lost")){
                    oleDeliverNotice = oleDeliverNotice1;
                    break;
                }
            }
        }
        return oleDeliverNotice;
    }

    public String getItemFullLocation(String itemBarcode) {

        ItemInfoUtil itemInfoUtil = ItemInfoUtil.getInstance();
        ItemRecord itemRecord = itemInfoUtil.getItemRecordByBarcode(itemBarcode);
        String fullLocation = null;
        String location = null;
        location = itemRecord.getLocation();
        if (StringUtils.isBlank(location)) {
            Map<String, String> criteriaMap = new HashMap();
            criteriaMap.put("holdingsId", itemRecord.getHoldingsId());
            List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord
                            .class,
                    criteriaMap);
            HoldingsRecord holdingsRecord = holdingsRecords.get(0);
            location = holdingsRecord.getLocation();
        }
        OleItemRecordForCirc itemRecordForCirc = new OleItemRecordForCirc();
        ItemInfoUtil.getInstance().populateItemLocation(itemRecordForCirc, location);
        fullLocation = itemRecordForCirc.getItemFullPathLocation();
        return fullLocation;
    }



    public OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();

        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public OleLoanDocumentPlatformAwareDao getOleLoanDocumentPlatformAwareDao() {
        if (null == oleLoanDocumentPlatformAwareDao) {
            oleLoanDocumentPlatformAwareDao = (OleLoanDocumentPlatformAwareDao) SpringContext.getService("OleLoanDocumentPlatformAwareDao");
        }
        return oleLoanDocumentPlatformAwareDao;
    }

    public void setOleLoanDocumentPlatformAwareDao(OleLoanDocumentPlatformAwareDao oleLoanDocumentPlatformAwareDao) {
        this.oleLoanDocumentPlatformAwareDao = oleLoanDocumentPlatformAwareDao;
    }

    public BulkItemUpdateUtil getBulkItemUpdateUtil() {
        if (null == bulkItemUpdateUtil) {
            bulkItemUpdateUtil = new BulkItemUpdateUtil();
        }
        return bulkItemUpdateUtil;
    }

    public void setBulkItemUpdateUtil(BulkItemUpdateUtil bulkItemUpdateUtil) {
        this.bulkItemUpdateUtil = bulkItemUpdateUtil;
    }

    public RenewController getRenewController() {
        if (null == renewController) {
            renewController = new RenewController();
        }
        return renewController;
    }



    public DateTimeService getDateTimeService() {
        if(dateTimeService == null){
            dateTimeService = (DateTimeService) SpringContext.getService("dateTimeService");
        }
        return dateTimeService;
    }

    public void setDateTimeService(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    public void setRenewController(RenewController renewController) {
        this.renewController = renewController;
    }

    private String getParameter(String parameterName) {
        String parameter = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants
                .DLVR_CMPNT, parameterName);
        return parameter;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = (OleDeliverRequestDocumentHelperServiceImpl) SpringContext.getService("oleDeliverRequestDocumentHelperService");
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    public OleLoanDocument getLoanDocument(String itemBarcode) {
        HashMap<String, Object> criteriaMap = new HashMap<>();
        criteriaMap.put("itemId", itemBarcode);
        List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, criteriaMap);
        if (!CollectionUtils.isEmpty(oleLoanDocuments)) {
            return oleLoanDocuments.get(0);
        }
        return null;
    }
}
