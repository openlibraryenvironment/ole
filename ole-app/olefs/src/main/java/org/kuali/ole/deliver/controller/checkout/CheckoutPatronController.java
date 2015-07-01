package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.deliver.controller.PatronLookupCircController;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hemalathas on 6/21/15.
 */
@Controller
@RequestMapping(value = "/patronCheckoutController")
public class CheckoutPatronController extends CheckoutItemController {

    private static final Logger LOG = Logger.getLogger(CheckoutPatronController.class);
    private PatronLookupCircController patronLookupCircController;

    @RequestMapping(params = "methodToCall=searchPatron")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;

        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        ErrorMessage errorMessage = getPatronLookupCircController().searchPatron(circForm);
        oleStopWatch.end();
        Log.info("Time taken to look up patron: " + oleStopWatch.getTotalTime() + " ms");

        if (null != errorMessage && StringUtils.isNotBlank(errorMessage.getErrorMessage())) {
            circForm.setErrorMessage(errorMessage);
            if (null != errorMessage.getErrorCode() && errorMessage.getErrorCode().equals(DroolsConstants.GENERAL_MESSAGE_FLAG)) {
                showDialog("generalMessageDialog", circForm, request, response);
            } else {
                showErrorMessageDialog(circForm, request, response);
            }
        } else {
            return handleProxyPatronsIfExists(circForm, result, request, response);
        }
        return getUIFModelAndView(circForm, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=handleProxyPatronsIfExists")
    public ModelAndView handleProxyPatronsIfExists(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        if (!circForm.isProxyCheckDone()) {
            PatronLookupCircController patronLookupCircController = getPatronLookupCircController();
            boolean proxyPatrons = patronLookupCircController.hasProxyPatrons(circForm);
            if (!proxyPatrons) {
                setProceedWithCheckoutFlag(circForm);
                circForm.setProxyCheckDone(true);
            } else {
                showDialog("proxyListCheckoutDialog", circForm, request, response);
            }
        } else {
            setProceedWithCheckoutFlag(circForm);
        }
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=processPatronSearchPostProxyHandling")
    public ModelAndView processPatronSearchPostProxyHandling(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        ErrorMessage errorMessage = getPatronLookupCircController().processPatronSearchPostProxyHandling(circForm);
        if(null != errorMessage && StringUtils.isNotEmpty(errorMessage.getErrorMessage())){
            showDialog("ptrnValidationErrorMessageDialog", circForm, request, response);
        } else {
            setProceedWithCheckoutFlag(circForm);
        }
        return getUIFModelAndView(circForm);
    }

    private void setProceedWithCheckoutFlag(CircForm circForm) {
        circForm.setProceedWithCheckout(true);
        circForm.getErrorMessage().setErrorCode(null);
        circForm.getErrorMessage().setErrorMessage(null);
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

    private PatronLookupCircController getPatronLookupCircController() {
        if (null == patronLookupCircController) {
            patronLookupCircController = new PatronLookupCircController();
        }
        return patronLookupCircController;
    }
}
