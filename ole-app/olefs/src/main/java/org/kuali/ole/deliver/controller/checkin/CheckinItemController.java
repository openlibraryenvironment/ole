package org.kuali.ole.deliver.controller.checkin;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.OLEUifControllerBase;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.keyvalue.CirculationDeskChangeKeyValue;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by pvsubrah on 7/22/15.
 */
@Controller
@RequestMapping(value = "/checkincontroller")
public class CheckinItemController extends OLEUifControllerBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CheckinItemController.class);

    private CircDeskLocationResolver circDeskLocationResolver;
    private Map<String, CheckinUIController> checkinUIControllerMap = new HashMap<>();

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new CheckinForm();
    }

    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = null;


        if (form.getViewId().equalsIgnoreCase("checkinView")) {
            String formKey = request.getParameter("formKey");

            if (null == formKey) {
                checkinForm = (CheckinForm) form;
            } else {
                checkinForm = (CheckinForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
                /* OLE-8186 : Modified for fixing browser backward and forward button functionality.*/
                if(checkinForm.getView() == null) {
                    checkinForm = (CheckinForm) form;
                }
            }

            initCheckinForm(checkinForm);
            setPrincipalInfo(checkinForm);

            List<KeyValue> keyValues = new CirculationDeskChangeKeyValue().getKeyValues();
            if (keyValues.isEmpty()) {
                throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPrincipalId(), "not Authorized", form.getViewId());
            } else {
                checkinForm.setSelectedCirculationDesk(keyValues.get(0).getKey());
                checkinForm.setPreviouslySelectedCirculationDesk(checkinForm.getSelectedCirculationDesk());
            }
        }
        setPrintFormatAndHoldSlipQueue(checkinForm);
        /*String maxTimeoutCount = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.MAX_TIME_CHECK_IN);
        checkinForm.setMaxSessionTime(maxTimeoutCount);*/
        return super.start(checkinForm, result, request, response);
    }

    private void setPrintFormatAndHoldSlipQueue(CheckinForm checkinForm) {
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(checkinForm.getPreviouslySelectedCirculationDesk());
        if (oleCirculationDesk != null && oleCirculationDesk.isPrintSlip() && oleCirculationDesk.isHoldQueue()) {
            checkinForm.setPrintFormat(oleCirculationDesk.getHoldFormat());
            checkinForm.setPrintOnHoldSlipQueue(true);
        }
    }

    private void initCheckinForm(CheckinForm checkinForm) {
        checkinForm.setCustomDueDateMap(new Date());
        checkinForm.setCustomDueDateTime("");
    }

    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;
        checkinForm.setDroolsExchange(null);
        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).checkin(checkinForm);

        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_LOST_REPLACEMENT_BILL)) {
            handleLostItemWithBillProcess(request, response, checkinForm, droolsResponse);
        } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
            handleDamagedItemProcess(request, response, checkinForm, droolsResponse);
        } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
            handleClaimsReturnedProcess(request, response, checkinForm, droolsResponse);
        } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
            handleMissingPieceProcess(request, response, checkinForm, droolsResponse);
        } else if (droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
            handleCheckinRequestExistsProcess(request, response, checkinForm, droolsResponse);
        } else {
            checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
            showDialog("checkinGeneralInfoMessageDialog", checkinForm, request, response);
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("submitForm('refreshUI',null,null,true,function (){ jq('#checkIn-Item_control').focus(); validateCheckInDate(); });");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=processLostItemWithBill")
    public ModelAndView processLostItemWithBill(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;

        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).
                preValidationForLostItemWithReplacementBill((ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord"), checkinForm);

        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_LOST_REPLACEMENT_BILL)) {
            handleLostItemWithBillProcess(request, response, checkinForm, droolsResponse);
        }
        else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
            handleDamagedItemProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
            handleClaimsReturnedProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
            handleMissingPieceProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
            handleCheckinRequestExistsProcess(request, response, checkinForm, droolsResponse);
        }
        if (StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }


    @RequestMapping(params = "methodToCall=processDamaged")
    public ModelAndView processDamaged(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;

        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).
                preValidationForDamaged((ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord"), checkinForm);

        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_DAMAGED)) {
            handleDamagedItemProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
            handleClaimsReturnedProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
            handleMissingPieceProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
            handleCheckinRequestExistsProcess(request, response, checkinForm, droolsResponse);
        }
        if (StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=processClaimReturned")
    public ModelAndView processClaimReturned(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {

        CheckinForm checkinForm = (CheckinForm) form;
        processItemInformationIfAvailable(request, checkinForm);

        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).
                preValidationForClaimsReturned((ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord"), checkinForm);
        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_CLAIMS_RETURNED)) {
            handleClaimsReturnedProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
            handleMissingPieceProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
            handleCheckinRequestExistsProcess(request, response, checkinForm, droolsResponse);
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=processMissingPiece")
    public ModelAndView processMissingPiece(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {

        CheckinForm checkinForm = (CheckinForm) form;
        processItemInformationIfAvailable(request, checkinForm);

        ItemRecord itemRecord = (ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord");
        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).
                preValidationForMissingPiece(itemRecord, checkinForm);
        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.ITEM_MISSING_PIECE)) {
            handleMissingPieceProcess(request, response, checkinForm, droolsResponse);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
            handleCheckinRequestExistsProcess(request, response, checkinForm, droolsResponse);
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }


    @RequestMapping(params = "methodToCall=processCheckinRequestExists")
    public ModelAndView processCheckinRequestExists(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;
        processItemInformationIfAvailable(request, checkinForm);

        ItemRecord itemRecord = (ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord");
        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).
                preValidationForCheckinRequestExists(itemRecord, checkinForm);
        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        } else if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equalsIgnoreCase(DroolsConstants.CHECKIN_REQUEST_EXITS_FOR_THIS_ITEM)) {
            handleCheckinRequestExistsProcess(request, response, checkinForm, droolsResponse);
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }


    @RequestMapping(params = "methodToCall=processCheckinAfterPreValidation")
    public ModelAndView processCheckinAfterPreValidation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;
        ItemRecord itemRecord = (ItemRecord) checkinForm.getDroolsExchange().getContext().get("itemRecord");
        OleLoanDocument oleLoanDocument = (OleLoanDocument) checkinForm.getDroolsExchange().getContext().get("oleLoanDocument");
        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).
                processCheckinAfterPreValidation(itemRecord, checkinForm, oleLoanDocument);
        if (null != droolsResponse && StringUtils.isBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            postCheckinProcess(checkinForm, result, request, response);
        }else {
            checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
            showDialog("checkinGeneralInfoMessageDialog", checkinForm, request, response);
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=postCheckinProcess")
    public ModelAndView postCheckinProcess(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        Map<String, Object> droolsExchangeContext = checkinForm.getDroolsExchange().getContext();
        boolean checkinNote = false;
        if (null != droolsExchangeContext) {
            if (null != droolsExchangeContext.get("checkinNote")) {
                checkinNote = (boolean) droolsExchangeContext.get("checkinNote");
            }
        }

        if (checkinNote) {
            checkinForm.setPermissionToRemoveNote(checkPermissionForRemoveNote(GlobalVariables.getUserSession().getPrincipalId()));
            showDialog("checkInNoteDialog", checkinForm, request, response);
        } else {
            postCheckInNoteProcess(checkinForm, result, request, response);
        }
        checkinForm.reset();
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=postCheckInNoteProcess")
    public ModelAndView postCheckInNoteProcess(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        Map<String, Object> droolsExchangeContext = checkinForm.getDroolsExchange().getContext();
        if (null != droolsExchangeContext) {
            if (null != droolsExchangeContext.get(DroolsConstants.ROUTE_TO_LOCATION_SELECTOR)) {
                routeToLocationProcess(checkinForm, result, request, response);
            } else if (null != droolsExchangeContext.get(DroolsConstants.PRINT_SLIP_FLAG) && (!checkinForm.isPrintOnHoldSlipQueue() ||
                    checkinForm.getCheckedInItem().getItemStatus().contains(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD)) ) {
                printSlip(checkinForm, result, request, response);
            } else if (null != droolsExchangeContext.get(DroolsConstants.AUTO_CHECKOUT)) {
                autoCheckout(checkinForm, result, request, response);
            } else if(null != droolsExchangeContext.get(DroolsConstants.SHOW_LOCATION_POPUP)) {
                showLocationPopupMessage(checkinForm, result, request, response);
            } else {
                checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus();validateCheckInDate();");
            }
        }
        checkinForm.reset();
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=showLocationPopupMessage")
    public ModelAndView showLocationPopupMessage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        DroolsResponse droolsResponse = getCheckinUIController(checkinForm).locationPopupMessageCheck(checkinForm);
        if(null != droolsResponse) {
            showDialog("checkinLocationPopupMsg", checkinForm, request, response);
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=routeToLocationProcess")
    public ModelAndView routeToLocationProcess(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                               HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        Map<String, Object> droolsExchangeContext = checkinForm.getDroolsExchange().getContext();
        if (null != droolsExchangeContext) {
            if (null != droolsExchangeContext.get(DroolsConstants.ROUTE_TO_LOCATION_SELECTOR)) {
                showDialog("checkinRouteToDialog", checkinForm, request, response);
            }
        }
        if(StringUtils.isBlank(checkinForm.getLightboxScript())) {
            checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus(); validateCheckInDate();");
        }
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=postRouteToLocationProcess")
    public ModelAndView postRouteToLocationProcess(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        String routeToLocationValue = request.getParameter("checkinRouteToLocation");
        OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) checkinForm.getDroolsExchange().getFromContext("oleItemRecordForCirc");
        if (StringUtils.isNotBlank(routeToLocationValue)) {
            checkinForm.setRouteToLocation(routeToLocationValue);
            if (checkinForm.getCheckedInItemList().size() > 0) {
                checkinForm.getCheckedInItemList().get(0).setRouteToLocation(routeToLocationValue);
            }
            if(oleItemRecordForCirc != null) {
                oleItemRecordForCirc.setRouteToLocation(routeToLocationValue);
            }
        }
        getCheckinUIController(checkinForm).updateReturnHistory(oleItemRecordForCirc, checkinForm);
        if(!checkinForm.isPrintOnHoldSlipQueue()) {
            printSlip(checkinForm, result, request, response);
        }
        return getUIFModelAndView(checkinForm);
    }


    @RequestMapping(params = "methodToCall=autoCheckout")
    public ModelAndView autoCheckout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base") + "/ole-kr-krad/autoCheckoutController?viewId=circView&methodToCall=backgroundCheckout&checkinFormKey=" + checkinForm.getFormKey();
        showIFrameDialog(url, checkinForm, "jq('#checkIn-Item_control').focus();validateCheckInDate();");
        return getUIFModelAndView(checkinForm);
    }


    @RequestMapping(params = "methodToCall=printSlip")
    public ModelAndView printSlip(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        Map<String, Object> droolsExchangeContext = checkinForm.getDroolsExchange().getContext();

        if (null != droolsExchangeContext) {
            if (null != droolsExchangeContext.get(DroolsConstants.PRINT_SLIP_FLAG)) {
                String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base") + "/ole-kr-krad/printBillcontroller?viewId=circView&methodToCall=printBill&checkinFormKey=" + checkinForm.getFormKey();
                String customScript = "";
                if (null != droolsExchangeContext.get(DroolsConstants.AUTO_CHECKOUT)) {
                    customScript = "submitForm('autoCheckout', null,null,null,null);";
                } else {
                    customScript = "jq('#checkIn-Item_control').val('');jq('#checkIn-Item_control').focus();validateCheckInDate();";
                }
                return showIFrameDialog(url, checkinForm, customScript);
            }
        }
        checkinForm.reset();
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=reset")
    public ModelAndView reset(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;
        checkinForm.reset();
        return getUIFModelAndView(checkinForm);
    }

    @RequestMapping(params = "methodToCall=refreshUI")
    public ModelAndView refreshUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.refresh(form, result, request, response);
    }


    @RequestMapping(params = "methodToCall=deleteCheckinNote")
    public ModelAndView deleteCheckinNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {

        CheckinForm checkinForm = (CheckinForm) form;

        deleteCheckinNote(checkinForm);
        return getUIFModelAndView(form);

    }

    public void deleteCheckinNote(CheckinForm checkinForm) {
        String itemId = checkinForm.getCheckedInItem().getItemUuid();
        HashMap map = new HashMap();
        map.put("checkinNote", null);
        getCheckinUIController(checkinForm).deleteItemInfoInSolr(map, itemId);
    }


    @RequestMapping(params = "methodToCall=changeCirculationDeskLocation")
    public ModelAndView changeCirculationDeskLocation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response) {
        showDialog("circDeskChangeDialog", form, request, response);
        String lightBoxScript = form.getLightboxScript();
        String circDeskLightBoxScript = lightBoxScript + "jq('#btnOkCircDesk').focus();";
        form.setLightboxScript(circDeskLightBoxScript);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=setPreviousCircDeskToCurrentlySelectedValue")
    public ModelAndView setPreviousCircDeskToCurrentlySelectedValue(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                                    HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        checkinForm.setPreviouslySelectedCirculationDesk(checkinForm.getSelectedCirculationDesk());
        clearUI(form, result, request, response);
        setPrintFormatAndHoldSlipQueue(checkinForm);
        return getUIFModelAndView(form, "checkinViewPage");
    }

    @RequestMapping(params = "methodToCall=revertCircDeskLocationSelection")
    public ModelAndView revertCircDeskLocationSelection(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                        HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        checkinForm.setSelectedCirculationDesk(checkinForm.getPreviouslySelectedCirculationDesk());
        return getUIFModelAndView(form, "checkinViewPage");
    }

    @RequestMapping(params = "methodToCall=clearUI")
    public ModelAndView clearUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        CheckinForm checkinForm = (CheckinForm) form;
        checkinForm.resetAll();
        checkinForm.setLightboxScript("jq('#checkIn-Item_control').focus();validateCheckInDate();");
        return getUIFModelAndView(form, "checkinViewPage");
    }

    private void setPrincipalInfo(CheckinForm checkinForm) {
        if (StringUtils.isBlank(checkinForm.getLoggedInUser()))
            checkinForm.setLoggedInUser(GlobalVariables.getUserSession().getPrincipalId());
    }

    public CheckinBaseController getCheckinUIController(CheckinForm checkinForm) {
        if (!getCheckinUIControllerMap().containsKey(checkinForm.getFormKey())) {
            CheckinUIController checkinUIController = new CheckinUIController();
            getCheckinUIControllerMap().put(checkinForm.getFormKey(), checkinUIController);
            return checkinUIController;
        } else {
            return getCheckinUIControllerMap().get(checkinForm.getFormKey());
        }
    }

    public boolean checkPermissionForRemoveNote(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.DLVR_NMSPC, OLEConstants.CAN_REMOVE_NOTE);
    }

    public Map<String, CheckinUIController> getCheckinUIControllerMap() {
        return checkinUIControllerMap;
    }

    public void setCheckinUIControllerMap(Map<String, CheckinUIController> checkinUIControllerMap) {
        this.checkinUIControllerMap = checkinUIControllerMap;
    }

    @RequestMapping(params = "methodToCall=clearSession")
    public ModelAndView clearSession(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckinForm checkinForm = (CheckinForm) form;

        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        String circDesk = getCircDesk(principalId);
        if (org.apache.commons.lang.StringUtils.isNotBlank(circDesk)) {
            checkinForm.setSelectedCirculationDesk(circDesk);
        }

        if(checkinForm.isPrintOnHoldSlipQueue() && checkinForm.getCheckedInItemList() != null && checkinForm.getCheckedInItemList().size()>0 &&
            onHoldItemFound(checkinForm.getCheckedInItemList())){
            String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base") + "/ole-kr-krad/printBillcontroller?viewId=circView&methodToCall=printSlipForEndSession&checkinFormKey=" + checkinForm.getFormKey();
            showIFrameDialog(url, checkinForm, "submitForm('clearSession',null,null,null,null)");
        }else{
            checkinForm.resetAll();
        }
        checkinUIControllerMap.remove(checkinForm.getFormKey());
        return getUIFModelAndView(checkinForm);
    }

    private boolean onHoldItemFound(List<CheckedInItem> checkedInItemList) {
        for (Iterator<CheckedInItem> iterator = checkedInItemList.iterator(); iterator.hasNext(); ) {
            CheckedInItem checkedInItem = iterator.next();
            if (org.apache.commons.lang.StringUtils.isNotBlank(checkedInItem.getItemForCircRecord().getItemStatusToBeUpdatedTo()) && checkedInItem.getItemForCircRecord().getItemStatusToBeUpdatedTo().equals(OLEConstants.ITEM_STATUS_ON_HOLD)) {
                return true;
            }
        }
        return false;
    }

    private String getCircDesk(String principalId) {
        String circulationDeskId = getCircDeskLocationResolver().getCircDeskForOpertorId(principalId).getCirculationDeskId();
        return circulationDeskId;

    }

    private void handleMissingPieceProcess(HttpServletRequest request, HttpServletResponse response, CheckinForm checkinForm, DroolsResponse droolsResponse) {
        checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
        checkinForm.setRecordNoteForMissingPiece(true);
        showDialog("checkinMissingPieceDialog", checkinForm, request, response);
    }

    private void handleCheckinRequestExistsProcess(HttpServletRequest request, HttpServletResponse response, CheckinForm checkinForm, DroolsResponse droolsResponse) {
        checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
        showDialog("checkinRequestExistDialog", checkinForm, request, response);
    }

    private void handleClaimsReturnedProcess(HttpServletRequest request, HttpServletResponse response, CheckinForm checkinForm, DroolsResponse droolsResponse) {
        checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
        checkinForm.setRecordNoteForClaimsReturn(true);
        showDialog("checkinClaimsReturnDialog", checkinForm, request, response);
    }
    private void handleLostItemWithBillProcess(HttpServletRequest request, HttpServletResponse response, CheckinForm checkinForm, DroolsResponse droolsResponse) {
        checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
        checkinForm.setRecordNoteForLostItemWithBill(true);
        showDialog("checkinLostItemWithBillDialogMsg", checkinForm, request, response);
    }

    private void handleDamagedItemProcess(HttpServletRequest request, HttpServletResponse response, CheckinForm checkinForm, DroolsResponse droolsResponse) {
        checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
        checkinForm.setRecordNoteForDamagedItem(true);
        showDialog("checkinDamagedItemDialog", checkinForm, request, response);
    }

   /* private void handleLostItemProcess(HttpServletRequest request, HttpServletResponse response, CheckinForm checkinForm, DroolsResponse droolsResponse) {
        checkinForm.setErrorMessage(droolsResponse.getErrorMessage());
        showDialog("checkinLostItemDialogMsg", checkinForm, request, response);
    }*/

    private void processItemInformationIfAvailable(HttpServletRequest request, CheckinForm checkinForm) {
        claimsCheck(request, checkinForm);


        damagedCheck(request, checkinForm);


        missingPieceCheck(request, checkinForm);

    }

    private void missingPieceCheck(HttpServletRequest request, CheckinForm checkinForm) {
        String recordNoteForMissingPieceChecked = request.getParameter("recordNoteForMissingPieceCheck");
        if (StringUtils.isNotBlank(recordNoteForMissingPieceChecked)) {
            checkinForm.setRecordNoteForMissingPiece(Boolean.valueOf(recordNoteForMissingPieceChecked));

            String missingPieceCheck = request.getParameter("missingPieceValueCheck");
            checkinForm.setMissingPieceMatchCheck(missingPieceCheck);
            String missingPieceCount = request.getParameter("missingPieceCount");
            checkinForm.setMissingPieceCount(missingPieceCount);
            String missingPieceNote = request.getParameter("missingPieceNote");
            checkinForm.setMissingPieceNote(missingPieceNote);
        }
    }

    private void damagedCheck(HttpServletRequest request, CheckinForm checkinForm) {
        String recordNoteForDamageChecked = request.getParameter("recordNoteForDamageChecked");
        if (StringUtils.isNotBlank(recordNoteForDamageChecked)) {
            checkinForm.setRecordNoteForDamagedItem(Boolean.valueOf(recordNoteForDamageChecked));
        }
    }

    private void claimsCheck(HttpServletRequest request, CheckinForm checkinForm) {
        String recordNoteForClaimChecked = request.getParameter("recordNoteForClaimChecked");
        if (StringUtils.isNotBlank(recordNoteForClaimChecked)) {
            checkinForm.setRecordNoteForClaimsReturn(Boolean.valueOf(recordNoteForClaimChecked));
        }
        String isItemFoundInLibrary = request.getParameter("isItemFoundInLibrary");
        if (StringUtils.isNotBlank(isItemFoundInLibrary)) {
            checkinForm.setItemFoundInLibrary(Boolean.valueOf(isItemFoundInLibrary));
        }
    }


}
