package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronNotes;
import org.kuali.ole.deliver.controller.PatronLookupCircUIController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemalathas on 6/21/15.
 */
@Controller
@RequestMapping(value = "/patronCheckoutController")
public class CheckoutPatronController extends CheckoutItemController {

    private static final Logger LOG = Logger.getLogger(CheckoutPatronController.class);
    private PatronLookupCircUIController patronLookupCircUIController;
    private OlePatronRecordUtil olePatronRecordUtil;

    @RequestMapping(params = "methodToCall=searchPatron")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;

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
                showDialog("generalMessageAndResetUIDialog", circForm, request, response);
            } else {
                showErrorMessageDialog(circForm, request, response);
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
        if (!circForm.isProxyCheckDone()) {
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
        } else {
            setProceedWithCheckoutFlag(circForm);
        }
        if(circForm.isProxyCheckDone() && checkForPatronUserNotes(circForm.getDroolsExchange())) {
            showDialog("patronUserNotesDialog", circForm, request, response);
        } else if(circForm.isProxyCheckDone() && circForm.isAutoCheckout()){
            return lookupItemAndSaveLoan(circForm,result,request,response);
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

    @RequestMapping(params = "methodToCall=processPatronSearchPostProxyHandling")
    public ModelAndView processPatronSearchPostProxyHandling(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        DroolsExchange droolsExchange = new DroolsExchange();
        String isSelfCheckout = request.getParameter("isSelfCheckout");
        if(StringUtils.isNotBlank(isSelfCheckout)) {
            if(circForm.getPatronDocument() != null) {
                circForm.getPatronDocument().setCheckoutForSelf(Boolean.valueOf(isSelfCheckout));
            }
        }
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
        if (CollectionUtils.isNotEmpty(olePatronDocument.getOlePatronUserNotes())) {
            hasNotes = true;
        }
        return hasNotes;
    }

    @RequestMapping(params = "methodToCall=close")
    public ModelAndView patronDone(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        fastAddBarcode = "";
        CircForm circForm = (CircForm) form;
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

    public void setOlePatronRecordUtil(OlePatronRecordUtil olePatronRecordUtil) {
        this.olePatronRecordUtil = olePatronRecordUtil;
    }
}
