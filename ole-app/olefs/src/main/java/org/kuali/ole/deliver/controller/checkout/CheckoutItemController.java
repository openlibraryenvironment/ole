package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.controller.checkin.CheckinItemController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.deliver.service.ClaimsReturnedNoticesExecutor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.common.document.content.instance.ItemClaimsReturnedRecord;
import org.kuali.ole.docstore.common.document.content.instance.ItemDamagedRecord;
import org.kuali.ole.docstore.common.document.content.instance.MissingPieceItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hemalathas on 6/21/15.
 */
public class CheckoutItemController extends CircFastAddItemController {

    private static final Logger LOG = Logger.getLogger(CheckoutItemController.class);


    @RequestMapping(params = "methodToCall=lookupItemAndSaveLoan")
    public ModelAndView lookupItemAndSaveLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        DroolsResponse droolsResponse = getCheckoutUIController(circForm.getFormKey()).lookupItemAndSaveLoan(circForm);
        if (null != droolsResponse &&
                StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ERROR_CODES.CUSTOM_LOAN_DUE_DATE_REQUIRED.getName())) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("customDueDateDialog", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.GENERAL_MESSAGE_FLAG)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("generalInfoDialog", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_AVAIL_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setRequestExistOrLoanedCheck(true);
                showDialog("overrideMessageDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_LOANED_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("checkoutRenewOptionDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.LOANED_BY_DIFFERENT_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.GENERAL_INFO)){
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("generalInfoDialog",circForm,request,response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ITEM_CLAIMS_RETURNED)){
                handleClaimsReturnedProcess(request, response, circForm, droolsResponse);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ITEM_DAMAGED)){
                handleDamagedItemProcess(request, response, circForm, droolsResponse);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ITEM_MISSING_PIECE)){
                handleMissingPieceProcess(request, response, circForm, droolsResponse);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.DUE_DATE_TRUNCATED)){
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("dueDateTruncated",circForm,request,response);
            }else {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setItemOverride(true);
                showErrorMessageDialog(circForm, request, response);
            }
        } else {
            resetItemInfoForNextTrans(circForm);
        }

        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String itemLightBoxScript = lightBoxScript + "jq('#checkoutItem_control').blur();";
            circForm.setLightboxScript(itemLightBoxScript);
        }

        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=processDamagedItem")
    public ModelAndView processDamagedItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        processItemInformationIfAvailable(request, circForm);
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) circForm.getDroolsExchange().getContext().get("oleItemRecordForCirc");
        DroolsResponse droolsResponse = getCheckoutUIController(circForm.getFormKey()).
                preValidationForDamagedItem(oleItemRecordForCirc.getItemRecord(), circForm);
        if (null != droolsResponse && StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ERROR_CODES.CUSTOM_LOAN_DUE_DATE_REQUIRED.getName())) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("customDueDateDialog", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_AVAIL_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setRequestExistOrLoanedCheck(true);
                showDialog("overrideMessageDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_LOANED_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("checkoutRenewOptionDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.LOANED_BY_DIFFERENT_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            } else if(null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
                handleMissingPieceProcess(request, response, circForm, droolsResponse);
            } else if (null!= droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
                handleDamagedItemProcess(request, response, circForm, droolsResponse);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.DUE_DATE_TRUNCATED)){
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("dueDateTruncated",circForm,request,response);
            } else {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setItemOverride(true);
                showErrorMessageDialog(circForm, request, response);
            }
        } else {
            resetItemInfoForNextTrans(circForm);
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String itemLightBoxScript = lightBoxScript + "jq('#checkoutItem_control').blur();";
            circForm.setLightboxScript(itemLightBoxScript);
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=processMissingPieceItem")
    public ModelAndView processMissingPieceItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        processItemInformationIfAvailable(request, circForm);
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) circForm.getDroolsExchange().getContext().get("oleItemRecordForCirc");
        DroolsResponse droolsResponse = getCheckoutUIController(circForm.getFormKey()).preValidationForMissingPiece(oleItemRecordForCirc.getItemRecord(), circForm);
        if (null != droolsResponse && StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ERROR_CODES.CUSTOM_LOAN_DUE_DATE_REQUIRED.getName())) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("customDueDateDialog", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_AVAIL_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setRequestExistOrLoanedCheck(true);
                showDialog("overrideMessageDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_LOANED_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("checkoutRenewOptionDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.LOANED_BY_DIFFERENT_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            } else if(null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
                handleMissingPieceProcess(request, response, circForm, droolsResponse);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.DUE_DATE_TRUNCATED)){
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("dueDateTruncated",circForm,request,response);
            } else {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setItemOverride(true);
                showErrorMessageDialog(circForm, request, response);
            }
        } else {
            resetItemInfoForNextTrans(circForm);
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String itemLightBoxScript = lightBoxScript + "jq('#checkoutItem_control').blur();";
            circForm.setLightboxScript(itemLightBoxScript);
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=processCheckoutAfterPreValidations")
    public ModelAndView processCheckoutAfterPreValidations(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        processItemInformationIfAvailable(request, circForm);
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) circForm.getDroolsExchange().getContext().get("oleItemRecordForCirc");
        DroolsResponse droolsResponse = getCheckoutUIController(circForm.getFormKey()).processCheckoutAfterPreValidations(circForm, oleItemRecordForCirc);
        if (null != droolsResponse && StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ERROR_CODES.CUSTOM_LOAN_DUE_DATE_REQUIRED.getName())) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("customDueDateDialog", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_AVAIL_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setRequestExistOrLoanedCheck(true);
                showDialog("overrideMessageDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.REQUEST_EXITS_FOR_LOANED_ITEM)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.CHECKED_OUT_BY_SAME_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("checkoutRenewOptionDialog", circForm, request, response);
            }else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.LOANED_BY_DIFFERENT_PATRON)) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("itemAlreadyLoaned", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.DUE_DATE_TRUNCATED)){
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showDialog("dueDateTruncated",circForm,request,response);
            } else {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                circForm.setItemOverride(true);
                showErrorMessageDialog(circForm, request, response);
            }
        } else {
            resetItemInfoForNextTrans(circForm);
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String itemLightBoxScript = lightBoxScript + "jq('#checkoutItem_control').blur();";
            circForm.setLightboxScript(itemLightBoxScript);
        }
        return getUIFModelAndView(circForm);
    }

    private void handleMissingPieceProcess(HttpServletRequest request, HttpServletResponse response, CircForm circForm, DroolsResponse droolsResponse) {
        circForm.setRecordNoteForMissingPiece(true);
        circForm.setErrorMessage(droolsResponse.getErrorMessage());
        showDialog("checkoutMissingPieceDialog",circForm,request,response);
    }

    private void handleDamagedItemProcess(HttpServletRequest request, HttpServletResponse response, CircForm circForm, DroolsResponse droolsResponse) {
        circForm.setRecordNoteForDamagedItem(true);
        circForm.setErrorMessage(droolsResponse.getErrorMessage());
        showDialog("checkoutDamagedItemDialog",circForm,request,response);
    }

    private void handleClaimsReturnedProcess(HttpServletRequest request, HttpServletResponse response, CircForm circForm, DroolsResponse droolsResponse) {
        circForm.setRecordNoteForClaimsReturn(true);
        circForm.setErrorMessage(droolsResponse.getErrorMessage());
        showDialog("checkoutClaimsReturnDialog",circForm,request,response);
    }

    private void processItemInformationIfAvailable(HttpServletRequest request, CircForm circForm) {

        claimsReturnedCheck(request, circForm);

        damagedItemCheck(request, circForm);

        missingPieceCheck(request, circForm);

    }

    private void missingPieceCheck(HttpServletRequest request, CircForm circForm) {
        String recordNoteForMissingPieceChecked = request.getParameter("recordNoteForMissingPieceCheck");
        if(StringUtils.isNotBlank(recordNoteForMissingPieceChecked)) {
            circForm.setRecordNoteForMissingPiece(Boolean.valueOf(recordNoteForMissingPieceChecked));
        }
        String missingPieceCheck = request.getParameter("missingPieceValueCheck");
        circForm.setMissingPieceMatchCheck(missingPieceCheck);
        String missingPieceCount = request.getParameter("newMissingPieceCount");
        circForm.setMissingPieces(missingPieceCount);
        String missingPieceNote = request.getParameter("newMissingPieceNote");
        circForm.setMismatchedMissingPieceNote(missingPieceNote);
    }

    private void damagedItemCheck(HttpServletRequest request, CircForm circForm) {
        String recordNoteForDamageChecked = request.getParameter("recordNoteForDamageChecked");
        if(StringUtils.isNotBlank(recordNoteForDamageChecked)) {
            circForm.setRecordNoteForDamagedItem(Boolean.valueOf(recordNoteForDamageChecked));
        }
    }

    private void claimsReturnedCheck(HttpServletRequest request, CircForm circForm) {
        String recordNoteForClaimChecked = request.getParameter("recordNoteForClaimChecked");
        if(StringUtils.isNotBlank(recordNoteForClaimChecked)) {
            circForm.setRecordNoteForClaimsReturn(Boolean.valueOf(recordNoteForClaimChecked));
        }
        String isItemFoundInLibrary = request.getParameter("isItemFoundInLibrary");
        if (StringUtils.isNotBlank(isItemFoundInLibrary)) {
            circForm.setItemFoundInLibrary(Boolean.valueOf(isItemFoundInLibrary));
        }
    }

    @RequestMapping(params = "methodToCall=proceedToValidateItemAndSaveLoan")
    public ModelAndView proceedToValidateItemAndSaveLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                         HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.setErrorMessage(new ErrorMessage());
        DroolsResponse droolsResponse = getCheckoutUIController(circForm.getFormKey()).proceedWithItemValidation(circForm);
        circForm.setDroolsExchange(droolsResponse.getDroolsExchange());
        circForm.setErrorMessage(droolsResponse.getErrorMessage());
        if (null != droolsResponse &&
                StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.ERROR_CODES.CUSTOM_LOAN_DUE_DATE_REQUIRED.getName())) {
                showDialog("customDueDateDialog", circForm, request, response);
            } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.GENERAL_MESSAGE_FLAG)) {
                showDialog("generalMessageAndResetUIDialog", circForm, request, response);
            } else {
                circForm.setItemOverride(true);
                showErrorMessageDialog(circForm, request, response);
            }
        } else {
            resetItemInfoForNextTrans(circForm);
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String itemLightBoxScript = lightBoxScript + "jq('#checkoutItem_control').blur();";
            circForm.setLightboxScript(itemLightBoxScript);
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=proceedToSaveLoan")
    public ModelAndView proceedToSaveLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        String customDate = request.getParameter("customDueDateMap");
        String customTime = request.getParameter("customDueTime");
        if (StringUtils.isNotBlank(customDate)) {
            circForm.setCustomDueDateMap(new DateUtil().getDate(customDate));
            if(StringUtils.isNotBlank(customTime)) {
                circForm.setCustomDueDateTime(customTime);
            } else {
                circForm.setCustomDueDateTime(ParameterValueResolver.getInstance().getParameter(OLEConstants
                        .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.DEFAULT_TIME_FOR_DUE_DATE));
            }
        }
        getCheckoutUIController(circForm.getFormKey()).proceedToSaveLoan(circForm);
        resetItemInfoForNextTrans(circForm);
        return getUIFModelAndView(circForm, "circViewPage");
    }


    @RequestMapping(params = "methodToCall=setItemBarcode")
    public ModelAndView setItemBarcode(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setItem(fastAddBarcode);
        return getUIFModelAndView(oleLoanForm, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=openFastAdd")
    public ModelAndView openFastAdd(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the openFastAdd method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setFastAddItemIndicator(true);
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base") + OLEConstants.FAST_ADD_LINK;
        oleLoanForm.setFastAddUrl(url);
        return getUIFModelAndView(oleLoanForm, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=proceedBackgroundCheckIn")
    public ModelAndView proceedBackgroundCheckIn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        CheckinItemController checkinItemController = new CheckinItemController();
        CheckinForm checkinForm = new CheckinForm();
        checkinForm.setItemBarcode(circForm.getItemBarcode());
        checkinForm.setSelectedCirculationDesk(circForm.getSelectedCirculationDesk());
        checkinForm.setCustomDueDateMap(new Date());
        DroolsResponse droolsResponse = checkinItemController.getCheckinUIController(checkinForm).checkin(checkinForm);
        if (droolsResponse != null && org.apache.commons.lang.StringUtils.isNotBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            if (droolsResponse.retriveErrorCode() != null) {
                droolsResponse.getErrorMessage().clearErrorMessage();
                if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_LOST)) {
                    droolsResponse.addErrorMessage("Item cannot be loaned, Item statis is ''lost'");
                } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
                    droolsResponse.addErrorMessage("Item is Claims Returned. So the checkin process has to be handled manually");
                } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
                    droolsResponse.addErrorMessage("Item has missing pieces. So the checkin process has to be handled manually");
                } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
                    droolsResponse.addErrorMessage("Item is Damaged. So the checkin process has to be handled manually");
                } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
                    droolsResponse.addErrorMessage("Requests exists for this item. So the checkin process has to be handled manually");
                } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.GENERAL_MESSAGE_FLAG)) {
                    droolsResponse.addErrorMessage("No checkin rule found!");
                }
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
            }
            showDialog("blockCheckinProcessDialog", circForm, request, response);
        } else {
            proceedToValidateItemAndSaveLoan(circForm, result, request, response);
        }
        return getUIFModelAndView(circForm);
    }

    public void createClaimsReturnForItem(CircForm circForm, List<OleLoanDocument> loanDocumentList, OlePatronDocument olePatronDocument,String cancelRequest) throws Exception {
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                List<ItemClaimsReturnedRecord> itemClaimsReturnedRecords = updateClaimsReturnedHistory(oleLoanDocument, olePatronDocument);
                Map parameterMap = new HashMap();
                parameterMap.put("claimsReturnNote", oleLoanDocument.getClaimsReturnNote());
                parameterMap.put("ClaimsReturnedDate", convertToString(oleLoanDocument.getClaimsReturnedDate()));
                parameterMap.put("itemClaimsReturnedRecords", itemClaimsReturnedRecords);
                parameterMap.put("itemClaimsReturnedFlag", oleLoanDocument.isClaimsReturnedIndicator());
                parameterMap.put("patronId", olePatronDocument.getOlePatronId());
                parameterMap.put("proxyPatronId", oleLoanDocument.getProxyPatronId());
                getCheckoutUIController(circForm.getFormKey()).updateItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid(), false);
                if(cancelRequest!=null && cancelRequest.equalsIgnoreCase("true")){
                    new OleDeliverRequestDocumentHelperServiceImpl().cancelPendingRequestForClaimsReturnedItem(oleLoanDocument.getItemUuid());
                }
            }
        }
    }

    public void sendClaimsReturnedNotice(OleLoanDocument oleLoanDocument) {
        Map claimMap = new HashMap();
        List<OleLoanDocument> oleLoanDocuments = Arrays.asList(oleLoanDocument);
        List<OleLoanDocument> oleLoanDocumentWithItemInfo = new ArrayList<>();
        try{
            OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = SpringContext.getBean(OleDeliverRequestDocumentHelperServiceImpl.class);
            oleLoanDocumentWithItemInfo = oleDeliverRequestDocumentHelperService.getLoanDocumentWithItemInfo(oleLoanDocuments,"false");
        }catch (Exception e){
            LOG.info("Exception occured while setting the item info " + e.getMessage());
            LOG.error(e,e);
        }

        claimMap.put(OLEConstants.LOAN_DOCUMENTS, oleLoanDocumentWithItemInfo);
        for (OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()) {
            if (oleDeliverNotice != null && oleDeliverNotice.getNoticeType().equalsIgnoreCase(OLEConstants.CLAIMS_RETURNED_NOTICE)) {
                claimMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, oleDeliverNotice.getNoticeContentConfigName());
                break;
            }
        }
        Runnable claimsReturnedNoticesExecutor = new ClaimsReturnedNoticesExecutor(claimMap);
        claimsReturnedNoticesExecutor.run();
    }

    public void deleteClaimsReturnForItem(CircForm circForm, List<OleLoanDocument> loanDocumentList) throws Exception {
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                Map parameterMap = new HashMap();
                parameterMap.put("deleteClaimsReturn", null);
                Boolean itemUpdate = getCheckoutUIController(circForm.getFormKey()).deleteItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid());
                if(itemUpdate!=null && itemUpdate.booleanValue()) {
                    oleLoanDocument.setClaimsReturnNote(null);
                    oleLoanDocument.setClaimsReturnedDate(null);
                }
                oleLoanDocument.setLastClaimsReturnedSearchedDate(null);
                oleLoanDocument.setClaimsSearchCount(0);
                oleLoanDocument.setNoOfClaimsReturnedNoticesSent(0);
            }
            getBusinessObjectService().save(loanDocumentList);
        }
    }

    private List<ItemClaimsReturnedRecord> updateClaimsReturnedHistory(OleLoanDocument existingLoanDocument, OlePatronDocument olePatronDocument) {
        SimpleDateFormat dateFormatForClaimsReturn = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(existingLoanDocument.getItemUuid()));
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord> claimsReturnedRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord>) getBusinessObjectService().findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord.class, map, "claimsReturnedId", true);
        List<ItemClaimsReturnedRecord> itemClaimsReturnedRecordList = new ArrayList<>();
        for (Iterator<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord> iterator = claimsReturnedRecordList.iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemClaimsReturnedRecord itemClaimsReturnedRecord = iterator.next();
            ItemClaimsReturnedRecord claimsReturnedRecord = new ItemClaimsReturnedRecord();
            if (itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate() != null && !itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate().toString().isEmpty()) {
                String formatedDateStringForClaimsReturn = convertToString(itemClaimsReturnedRecord.getClaimsReturnedFlagCreateDate());
                claimsReturnedRecord.setClaimsReturnedFlagCreateDate(formatedDateStringForClaimsReturn);
            }
            claimsReturnedRecord.setClaimsReturnedNote(itemClaimsReturnedRecord.getClaimsReturnedNote());
            claimsReturnedRecord.setClaimsReturnedPatronBarcode(itemClaimsReturnedRecord.getClaimsReturnedPatronBarcode());
            claimsReturnedRecord.setClaimsReturnedPatronId(itemClaimsReturnedRecord.getClaimsReturnedPatronId());
            claimsReturnedRecord.setClaimsReturnedOperatorId(itemClaimsReturnedRecord.getClaimsReturnedOperatorId());
            claimsReturnedRecord.setItemId(itemClaimsReturnedRecord.getItemId());
            itemClaimsReturnedRecordList.add(claimsReturnedRecord);
        }
        ItemClaimsReturnedRecord claimsReturnedRecord = new ItemClaimsReturnedRecord();
        claimsReturnedRecord.setClaimsReturnedNote(existingLoanDocument.getClaimsReturnNote());
        claimsReturnedRecord.setClaimsReturnedPatronBarcode(olePatronDocument.getBarcode());
        claimsReturnedRecord.setClaimsReturnedPatronId(olePatronDocument.getOlePatronId());
        claimsReturnedRecord.setClaimsReturnedOperatorId(GlobalVariables.getUserSession().getPrincipalId());
        claimsReturnedRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(existingLoanDocument.getItemUuid()));
        claimsReturnedRecord.setClaimsReturnedFlagCreateDate(dateFormatForClaimsReturn.format(existingLoanDocument.getClaimsReturnedDate()));
        itemClaimsReturnedRecordList.add(claimsReturnedRecord);
        return itemClaimsReturnedRecordList;
    }


    public void createMissingPieceItem(CircForm circForm, List<OleLoanDocument> loanDocumentList, OlePatronDocument olePatronDocument) throws Exception {
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                List<MissingPieceItemRecord> itemMissingPieceItemRecords = updateMissingPieceItemHistory(oleLoanDocument, olePatronDocument);
                Map parameterMap = new HashMap();
                parameterMap.put("missingPieceItemFlag", oleLoanDocument.isMissingPieceFlag());
                parameterMap.put("missingPieceItemNote", oleLoanDocument.getMissingPieceNote());
                parameterMap.put("noOfmissingPiece", oleLoanDocument.getNoOfMissingPiece());
                parameterMap.put("missingPieceItemCount", oleLoanDocument.getMissingPiecesCount());
                parameterMap.put("missingPieceItemDate", convertToString(oleLoanDocument.getMissingPieceItemDate()));
                parameterMap.put("itemMissingPieceItemRecords", itemMissingPieceItemRecords);
                parameterMap.put("patronId", olePatronDocument.getOlePatronId());
                parameterMap.put("proxyPatronId", oleLoanDocument.getProxyPatronId());
                getCheckoutUIController(circForm.getFormKey()).updateItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid(), false);
                new OleDeliverRequestDocumentHelperServiceImpl().cancelPendingRequestForMissingPieceItem(oleLoanDocument.getItemUuid());
            }
        }
    }


    private List<MissingPieceItemRecord> updateMissingPieceItemHistory(OleLoanDocument existingLoanDocument, OlePatronDocument olePatronDocument) {
        SimpleDateFormat dateFormatForMissingItem = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Map<String, String> map = new HashMap<>();
        map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(existingLoanDocument.getItemUuid()));
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> missingPieceItemRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord>) getBusinessObjectService().findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord.class, map, "missingPieceItemId", true);
        List<MissingPieceItemRecord> itemMissingPieceRecordList = new ArrayList<>();
        for (Iterator<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord> iterator = missingPieceItemRecordList.iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.MissingPieceItemRecord itemMissingPieceItemRecord = iterator.next();
            MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
            if (itemMissingPieceItemRecord.getMissingPieceDate() != null && !itemMissingPieceItemRecord.getMissingPieceDate().toString().isEmpty()) {
                Timestamp formatedDateStringForDamagedItem = itemMissingPieceItemRecord.getMissingPieceDate();
                missingPieceItemRecord.setMissingPieceDate(dateFormatForMissingItem.format(formatedDateStringForDamagedItem));
            }
            missingPieceItemRecord.setMissingPieceFlagNote(itemMissingPieceItemRecord.getMissingPieceFlagNote());
            missingPieceItemRecord.setMissingPieceCount(itemMissingPieceItemRecord.getMissingPieceCount());
            missingPieceItemRecord.setPatronBarcode(itemMissingPieceItemRecord.getPatronBarcode());
            missingPieceItemRecord.setPatronId(itemMissingPieceItemRecord.getPatronId());
            missingPieceItemRecord.setOperatorId(itemMissingPieceItemRecord.getOperatorId());
            missingPieceItemRecord.setItemId(itemMissingPieceItemRecord.getItemId());
            itemMissingPieceRecordList.add(missingPieceItemRecord);
        }
        MissingPieceItemRecord missingPieceItemRecord = new MissingPieceItemRecord();
        missingPieceItemRecord.setMissingPieceFlagNote(existingLoanDocument.getMissingPieceNote());
        missingPieceItemRecord.setMissingPieceCount(existingLoanDocument.getMissingPiecesCount());
        missingPieceItemRecord.setPatronBarcode(olePatronDocument.getBarcode());
        missingPieceItemRecord.setPatronId(olePatronDocument.getOlePatronId());
        missingPieceItemRecord.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
        missingPieceItemRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(existingLoanDocument.getItemUuid()));
        missingPieceItemRecord.setMissingPieceDate(dateFormatForMissingItem.format(existingLoanDocument.getMissingPieceItemDate()));
        itemMissingPieceRecordList.add(missingPieceItemRecord);
        return itemMissingPieceRecordList;
    }

    public void deleteMissingPieceItem(CircForm circForm, List<OleLoanDocument> loanDocumentList) throws Exception {
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                Map parameterMap = new HashMap();
                parameterMap.put("deleteMissingPieceItem", null);
                Boolean itemUpdate = getCheckoutUIController(circForm.getFormKey()).deleteItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid());
                if(itemUpdate!=null && itemUpdate.booleanValue()) {
                    oleLoanDocument.setMissingPieceNote(null);
                }
            }
        }
    }


    public void createDamagedItem(CircForm circForm, List<OleLoanDocument> loanDocumentList, OlePatronDocument olePatronDocument) throws Exception {
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                List<ItemDamagedRecord> itemDamagedItemRecords = updateDamagedItemHistory(oleLoanDocument, olePatronDocument);
                Map parameterMap = new HashMap();
                parameterMap.put("damagedItemNote", oleLoanDocument.getItemDamagedNote());
                parameterMap.put("damagedItemDate", convertToString(oleLoanDocument.getDamagedItemDate()));
                parameterMap.put("itemDamagedRecords", itemDamagedItemRecords);
                parameterMap.put("patronId", olePatronDocument.getOlePatronId());
                parameterMap.put("proxyPatronId", oleLoanDocument.getProxyPatronId());
                getCheckoutUIController(circForm.getFormKey()).updateItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid(), false);
                new OleDeliverRequestDocumentHelperServiceImpl().cancelPendingRequestForDamagedItem(oleLoanDocument.getItemUuid());
            }
        }
    }

    private List<ItemDamagedRecord> updateDamagedItemHistory(OleLoanDocument existingLoanDocument, OlePatronDocument olePatronDocument) {
        SimpleDateFormat dateFormatForDamagedItem = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        map.put("itemId", DocumentUniqueIDPrefix.getDocumentId(existingLoanDocument.getItemUuid()));
        List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord> damagedItemRecordList = (List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord>) getBusinessObjectService().findMatchingOrderBy(org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord.class, map, "itemDamagedId", true);
        List<ItemDamagedRecord> itemDamagedRecordList = new ArrayList<>();
        for (Iterator<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord> iterator = damagedItemRecordList.iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemDamagedRecord itemDamagedItemRecord = iterator.next();
            ItemDamagedRecord damagedItemRecord = new ItemDamagedRecord();
            if (itemDamagedItemRecord.getDamagedItemDate() != null && !itemDamagedItemRecord.getDamagedItemDate().toString().isEmpty()) {
                Timestamp formatedDateStringForDamagedItem = itemDamagedItemRecord.getDamagedItemDate();
                damagedItemRecord.setDamagedItemDate(dateFormatForDamagedItem.format(formatedDateStringForDamagedItem));
            }
            damagedItemRecord.setDamagedItemNote(itemDamagedItemRecord.getDamagedItemNote());
            damagedItemRecord.setPatronBarcode(itemDamagedItemRecord.getPatronBarcode());
            damagedItemRecord.setDamagedPatronId(itemDamagedItemRecord.getDamagedPatronId());
            damagedItemRecord.setOperatorId(itemDamagedItemRecord.getOperatorId());
            damagedItemRecord.setItemId(itemDamagedItemRecord.getItemId());
            itemDamagedRecordList.add(damagedItemRecord);
        }
        ItemDamagedRecord damagedItemRecord = new ItemDamagedRecord();
        damagedItemRecord.setDamagedItemNote(existingLoanDocument.getItemDamagedNote());
        damagedItemRecord.setPatronBarcode(olePatronDocument.getBarcode());
        damagedItemRecord.setDamagedPatronId(olePatronDocument.getOlePatronId());
        damagedItemRecord.setOperatorId(GlobalVariables.getUserSession().getPrincipalId());
        damagedItemRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(existingLoanDocument.getItemUuid()));
        damagedItemRecord.setDamagedItemDate(dateFormatForDamagedItem.format(existingLoanDocument.getDamagedItemDate()));
        itemDamagedRecordList.add(damagedItemRecord);
        return itemDamagedRecordList;
    }

    public void deleteDamagedItem(CircForm circForm, List<OleLoanDocument> loanDocumentList) throws Exception {
        if (CollectionUtils.isNotEmpty(loanDocumentList)) {
            for (Iterator<OleLoanDocument> iterator = loanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                Map parameterMap = new HashMap();
                parameterMap.put("deleteDamagedItem", null);
                Boolean itemUpdate = getCheckoutUIController(circForm.getFormKey()).deleteItemInfoInSolr(parameterMap, oleLoanDocument.getItemUuid());
                if(itemUpdate!=null && itemUpdate.booleanValue()) {
                    oleLoanDocument.setItemDamagedNote(null);
                }
            }
        }
    }


    public String convertToString(Timestamp date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String stringFormatOfDate = "";
        stringFormatOfDate = simpleDateFormat.format(date).toString();
        return stringFormatOfDate;
    }


}
