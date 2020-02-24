package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronNotes;
import org.kuali.ole.deliver.bo.OleProxyPatronDocument;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.PatronLookupCircUIController;
import org.kuali.ole.deliver.controller.PermissionsValidatorUtil;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.util.StringUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.sys.context.SpringContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by hemalathas on 6/21/15.
 */
@Controller
@RequestMapping(value = "/patronCheckoutController")
public class CheckoutPatronController extends CheckoutItemController {

    private static final Logger LOG = Logger.getLogger(CheckoutPatronController.class);
    private PatronLookupCircUIController patronLookupCircUIController;
    private OlePatronRecordUtil olePatronRecordUtil;
    private OlePatronHelperService olePatronHelperService;
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    private ErrorMessage errorMessage;

    @RequestMapping(params = "methodToCall=searchPatron")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        sendCheckoutRecipet(circForm);
        if (circForm.isAutoCheckout()) {
            resetUIForAutoCheckout(circForm, result, request, response);
        }else{
            resetUI(circForm,result,request,response);
        }

        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("circForm", circForm);
        DroolsResponse droolsResponse = getPatronLookupCircUIController().searchPatron(droolsExchange);
        oleStopWatch.end();
        Log.info("Time taken to look up patron: " + oleStopWatch.getTotalTime() + " ms");

        if (null != droolsResponse && StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
            circForm.setErrorMessage(droolsResponse.getErrorMessage());
            if (null != droolsResponse.retriveErrorCode() && droolsResponse.retriveErrorCode().equals(DroolsConstants.GENERAL_MESSAGE_FLAG)) {
                String createNewPatronLink = (String) droolsExchange.getContext().get("createNewPatronLink");
                if(StringUtils.isNotBlank(createNewPatronLink)) {
                    circForm.setCreateNewPatronLink(createNewPatronLink);
                }
                showDialog("patronInvalidOrLostDialog", circForm, request, response);
            }
        } else {
            return handleProxyPatronsIfExists(circForm, result, request, response);
        }

        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String patronLightBoxScript = lightBoxScript + "jq('#barcodeFieldSection_control').blur();";
            circForm.setLightboxScript(patronLightBoxScript);
        }
        return getUIFModelAndView(circForm, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=handleProxyPatronsIfExists")
    public ModelAndView handleProxyPatronsIfExists(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        circForm.setPageSize("10");
        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("circForm", circForm);
        boolean proxyPatrons = getPatronLookupCircUIController().hasProxyPatrons(droolsExchange);
        if (!proxyPatrons) {
            setProceedWithCheckoutFlag(circForm);
            circForm.setProxyCheckDone(true);
        } else {
            String overrideParameters = "{closeBtn:false,autoSize : false}";
            showDialogWithOverrideParameters("proxyListCheckoutDialog", circForm,overrideParameters);
        }

        if(circForm.isProxyCheckDone()) {
            DroolsResponse droolsResponse = getPatronLookupCircUIController().processPatronValidation(droolsExchange);
            if (null != droolsResponse && StringUtils.isNotBlank(droolsResponse.retrieveErrorMessage())) {
                circForm.setErrorMessage(droolsResponse.getErrorMessage());
                showErrorMessageDialog(circForm, request, response);
            } else {
                postPatronValidation(circForm, result, request, response);
            }
        }
        if(!StringUtils.isNotBlank(circForm.getErrorMessage().getErrorMessage())){
            if(circForm.isProxyCheckDone() && (searchHold(circForm))) {
                showHoldErrorMessageDialog(circForm, request, response);
            }
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String patronLightBoxScript = lightBoxScript + "jq('#barcodeFieldSection_control').blur();";
            circForm.setLightboxScript(patronLightBoxScript);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=postPatronValidation")
    public ModelAndView postPatronValidation(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        setProceedWithCheckoutFlag(circForm);
        if(checkForPatronUserNotes(circForm.getDroolsExchange())) {
            showDialog("patronUserNotesDialog", circForm, request, response);
        } else if(circForm.isAutoCheckout()) {
            return lookupItemAndSaveLoan(circForm, result, request, response);
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=processPatronSearchPostProxyHandling")
    public ModelAndView processPatronSearchPostProxyHandling(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        DroolsExchange droolsExchange = new DroolsExchange();
        String selectedBarcode = request.getParameter("selectedBarcode");
        setSelectedPatron(circForm, selectedBarcode);
        droolsExchange.addToContext("circForm", circForm);
        DroolsResponse droolsResponse = getPatronLookupCircUIController().processPatronSearchPostProxyHandling(droolsExchange);
        if(null != droolsResponse && StringUtils.isNotEmpty(droolsResponse.retrieveErrorMessage())){
            showDialog("ptrnValidationErrorMessageDialog", circForm, request, response);
        } else {
            setProceedWithCheckoutFlag(circForm);
            if(circForm.isProxyCheckDone() && checkForPatronUserNotes(circForm.getDroolsExchange())) {
                showDialog("patronUserNotesDialog", circForm, request, response);
            } else if(circForm.isProxyCheckDone() && circForm.isAutoCheckout()){
                return lookupItemAndSaveLoan(circForm,result,request,response);
            }
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String patronLightBoxScript = lightBoxScript + "jq('#barcodeFieldSection_control').blur();";
            circForm.setLightboxScript(patronLightBoxScript);
        }
        return getUIFModelAndView(circForm);
    }

    private void setSelectedPatron(CircForm circForm, String selectedBarcode) {
        if(StringUtils.isNotBlank(selectedBarcode)) {
            OlePatronDocument patronDocument = circForm.getPatronDocument();
            if(patronDocument.getBarcode().equalsIgnoreCase(selectedBarcode)) {
                patronDocument.setCheckoutForSelf(true);
            } else {
                List<OleProxyPatronDocument> oleProxyPatronDocuments = patronDocument.getOleProxyPatronDocumentList();
                if(CollectionUtils.isNotEmpty(oleProxyPatronDocuments)) {
                    for (Iterator<OleProxyPatronDocument> iterator = oleProxyPatronDocuments.iterator(); iterator.hasNext(); ) {
                        OleProxyPatronDocument oleProxyPatronDocument = iterator.next();
                        OlePatronDocument olePatronDocument = oleProxyPatronDocument.getOlePatronDocument();
                        String proxyPatronBarcode = olePatronDocument.getBarcode();
                        if(selectedBarcode.equalsIgnoreCase(proxyPatronBarcode)) {
                            oleProxyPatronDocument.getOlePatronDocument().setCheckoutForSelf(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(params = "methodToCall=deletePatronUserNotes")
    public ModelAndView deletePatronUserNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        DroolsExchange droolsExchange = new DroolsExchange();
        droolsExchange.addToContext("circForm", circForm);
        List<OlePatronNotes> olePatronNotesList = new ArrayList<>();
        OlePatronDocument olePatronDocument = getPatronLookupCircUIController().getPatronDocument(droolsExchange);
        for(OlePatronNotes olePatronNotes : olePatronDocument.getOlePatronUserNotes()) {
            if(olePatronNotes.isSelected()) {
                olePatronNotesList.add(olePatronNotes);
            }
        }
        if(CollectionUtils.isNotEmpty(olePatronNotesList)) {
            KRADServiceLocator.getBusinessObjectService().delete(olePatronNotesList);
        }
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=postPatronUserNotes")
    public ModelAndView postPatronUserNotes(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        if(circForm.isAutoCheckout()) {
            return lookupItemAndSaveLoan(circForm, result, request, response);
        }
        circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        return getUIFModelAndView(circForm);
    }

    private void setProceedWithCheckoutFlag(CircForm circForm) {
        OlePatronDocument patronDocumentForItemValidation = getCheckoutUIController(circForm.getFormKey()).getPatronDocumentForItemValidation(circForm);
        if (GlobalVariables.getUserSession() != null && null != patronDocumentForItemValidation) {
            patronDocumentForItemValidation.setPatronRecordURL(getOlePatronRecordUtil().patronNameURL(GlobalVariables.getUserSession().getPrincipalId(), patronDocumentForItemValidation.getOlePatronId()));
        }
        circForm.getDroolsExchange().addToContext("circForm", circForm);
        getPatronLookupCircUIController().setPatronDocument(circForm.getDroolsExchange(), patronDocumentForItemValidation);
        circForm.setProceedWithCheckout(true);
        circForm.getErrorMessage().setErrorCode(null);
        circForm.getErrorMessage().setErrorMessage(null);
    }

    public boolean checkForPatronUserNotes(DroolsExchange droolsExchange) {
        boolean hasNotes = false;
        OlePatronDocument olePatronDocument = getPatronLookupCircUIController().getPatronDocument(droolsExchange);
        if (null != olePatronDocument && CollectionUtils.isNotEmpty(olePatronDocument.getOlePatronUserNotes())) {
            hasNotes = true;
        }
        return hasNotes;
    }

    @RequestMapping(params = "methodToCall=close")
    public ModelAndView patronDone(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        fastAddBarcode = "";
        CircForm circForm = (CircForm) form;
        sendCheckoutRecipet(circForm);
        clearUI(circForm, result, request, response);
        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        String circDesk = getCircDesk(principalId);
        if (circDesk != null) {
            circForm.setSelectedCirculationDesk(circDesk);
            circForm.setPreviouslySelectedCirculationDesk(circForm.getSelectedCirculationDesk());
        }
        return getUIFModelAndView(circForm, circForm.getPageId());
    }

    @RequestMapping(params = "methodToCall=clearPatron")
    public ModelAndView clearPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        fastAddBarcode = "";
        CircForm circForm = (CircForm) form;
        sendCheckoutRecipet(circForm);
        clearUI(circForm, result, request, response);
        return getUIFModelAndView(circForm, circForm.getPageId());
    }

    private PatronLookupCircUIController getPatronLookupCircUIController() {
        if (null == patronLookupCircUIController) {
            patronLookupCircUIController = new PatronLookupCircUIController();
        }
        return patronLookupCircUIController;
    }

    public OlePatronRecordUtil getOlePatronRecordUtil() {
        if(null == olePatronRecordUtil){
            olePatronRecordUtil = new OlePatronRecordUtil();
        }
        return olePatronRecordUtil;
    }

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }

    public boolean searchHold(CircForm circForm
    ) throws Exception {
        OleCirculationDesk oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(circForm.getSelectedCirculationDesk());
        OlePatronDocument olePatronDocument = circForm.getPatronDocument();
        List<OleDeliverRequestBo> oleDeliverRequestBoList = olePatronDocument.getOleDeliverRequestBos();
        errorMessage = new ErrorMessage();
        int holdSameLocCount = 0;
        int holdOtherLocCount = 0;
        if(oleDeliverRequestBoList != null && oleDeliverRequestBoList.size()>0) {
            for(OleDeliverRequestBo deliverRequestBo : oleDeliverRequestBoList) {
                if (deliverRequestBo.getRequestTypeCode() != null && deliverRequestBo.getRequestTypeCode().contains("Hold")) {
                    Map itemIdMap = new HashMap();
                    itemIdMap.put(OLEConstants.ITEM_ID, deliverRequestBo.getItemUuid().substring(4));
                    ItemRecord itemRecord = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(ItemRecord.class, itemIdMap);
                    if(itemRecord.getItemStatusRecord().getCode().equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD)) {
                        if(deliverRequestBo.getBorrowerQueuePosition().intValue() == 1) {
                            if (StringUtils.isNotBlank(oleCirculationDesk.getShowItemOnHold()) && oleCirculationDesk.getShowItemOnHold().equals(OLEConstants.CURR_CIR_DESK) && oleCirculationDesk.getOlePickupCirculationDeskLocations() != null){
                                Collection<Object> oleCirculationDeskLocations =  getOleLoanDocumentDaoOjb().getPickUpLocationForCirculationDesk(oleCirculationDesk);
                                if(isPickupCirculationLocationMatched(oleCirculationDeskLocations,deliverRequestBo)) {
                                    deliverRequestBo.setOnHoldRequestForPatronMessage(OLEConstants.PTRN_RQST_MSG_CURR_CIR_DESK);
                                    holdSameLocCount = holdSameLocCount + 1;
                                }
                            }else if (StringUtils.isNotBlank(oleCirculationDesk.getShowItemOnHold()) && oleCirculationDesk.getShowItemOnHold().equals(OLEConstants.ALL_CIR_DESK)) {
                            deliverRequestBo.setOnHoldRequestForPatronMessage(OLEConstants.PTRN_RQST_MSG_ALL_CIR_DESK);
                            holdOtherLocCount = holdOtherLocCount + 1;
                            }
                        }
                    }
                }
            }
            if(holdSameLocCount > 0) {
                errorMessage.setErrorMessage(OLEConstants.PTRN_RQST_MSG_CURR_CIR_DESK);
                circForm.setErrorMessage(errorMessage);
            }
            if(holdOtherLocCount > 0) {
                errorMessage.setErrorMessage(OLEConstants.PTRN_RQST_MSG_ALL_CIR_DESK);
                circForm.setErrorMessage(errorMessage);
            }
        }
        if(holdSameLocCount > 0  || holdOtherLocCount > 0) {
            return true;
        }
        return false;
    }

    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if(oleLoanDocumentDaoOjb == null){
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }

    public void setOleLoanDocumentDaoOjb(OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb) {
        this.oleLoanDocumentDaoOjb = oleLoanDocumentDaoOjb;
    }

    public boolean isPickupCirculationLocationMatched(Collection<Object> pickUpLocation,OleDeliverRequestBo deliverRequestBo) {

        for (Object oleCirculationDeskLoc : pickUpLocation){
            if(deliverRequestBo.getPickUpLocationCode() != null) {
                OleCirculationDeskLocation oleCirculationDeskLocation = (OleCirculationDeskLocation)oleCirculationDeskLoc;
                if (StringUtils.isNotBlank(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()) &&
                        oleCirculationDeskLocation.getOleCirculationDesk() !=null &&
                        oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode()!= null &&
                        oleCirculationDeskLocation.getOleCirculationDesk().getCirculationDeskCode().contains(deliverRequestBo.getPickUpLocationCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping(params = "methodToCall=handleOnholdRequestIfExists")
    public ModelAndView handleOnholdRequestIfExists(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        circForm.setPageSize("10");
        if((searchHold(circForm))) {
            showHoldErrorMessageDialog(circForm, request, response);
        }else{
            boolean hasValidOverridePermissions = new PermissionsValidatorUtil().hasValidOverridePermissions(circForm);
            if(CollectionUtils.isEmpty(circForm.getErrorMessage().getPermissions())){
                return postPatronValidation(circForm, result, request, response);
            } else {
                if ((hasValidOverridePermissions)) {
                    if (circForm.isProxyCheckDone() && circForm.isItemValidationDone() && !circForm.isItemOverride() && !circForm.isRequestExistOrLoanedCheck()) {
                        circForm.getErrorMessage().clearErrorMessage();
                        return proceedToSaveLoan(circForm, result, request, response);
                    } else if (circForm.isItemOverride()) {
                        circForm.setItemOverride(false);
                        circForm.getErrorMessage().clearErrorMessage();
                        return proceedToSaveLoan(circForm, result, request, response);
                    }else if(circForm.isRequestExistOrLoanedCheck()){
                        circForm.setRequestExistOrLoanedCheck(false);
                        return proceedToValidateItemAndSaveLoan(circForm, result, request, response);
                    }
                    return postPatronValidation(circForm, result, request, response);
                } else {
                    circForm.setOverridingPrincipalName(null);
                    return showDialog("overrideMessageDialog", circForm, request, response);
                }
            }
        }
        if(StringUtils.isBlank(circForm.getLightboxScript())){
            circForm.setLightboxScript("jq('#checkoutItem_control').focus();");
        } else {
            String lightBoxScript = circForm.getLightboxScript();
            String patronLightBoxScript = lightBoxScript + "jq('#barcodeFieldSection_control').blur();";
            circForm.setLightboxScript(patronLightBoxScript);
        }
        return getUIFModelAndView(form);
    }

    public void sendCheckoutRecipet(CircForm circForm) {
        List<OleLoanDocument> oleLoanDocumentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentListForCurrentSession())) {
            oleLoanDocumentList.addAll(circForm.getLoanDocumentListForCurrentSession());
            circForm.getLoanDocumentListForCurrentSession().clear();
        }
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentsForRenew())) {
            for(OleLoanDocument oleLoanDocument :circForm.getLoanDocumentsForRenew()) {
                if(!oleLoanDocumentList.contains(oleLoanDocument)) {
                    oleLoanDocumentList.add(oleLoanDocument);
                }
            }
            circForm.getLoanDocumentsForRenew().clear();
        }
        if(CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
            try {
                getOlePatronHelperService().sendMailToPatron(oleLoanDocumentList);
            } catch (Exception e) {
                LOG.error("Error while sending sendout Receipt"+e.getLocalizedMessage());
            }
        }

    }

}
