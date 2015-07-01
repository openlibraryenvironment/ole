package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hemalathas on 6/21/15.
 */
public class CheckoutItemController extends CircFastAddItemController {

    private static final Logger LOG = Logger.getLogger(CheckoutItemController.class);

    private CheckoutController checkoutController;

    @RequestMapping(params = "methodToCall=lookupItemAndSaveLoan")
    public ModelAndView lookupItemAndSaveLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        ErrorMessage errorMessage = getCheckoutController().lookupItemAndSaveLoan(circForm);
        circForm.setErrorMessage(errorMessage);
        if (null != errorMessage &&
                StringUtils.isNotBlank(errorMessage.getErrorMessage())) {
            if (null != errorMessage.getErrorCode() && errorMessage.getErrorCode().equals(DroolsConstants.ERROR_CODES.CUSTOM_LOAN_DUE_DATE_REQUIRED.getName())) {
                showDialog("customDueDateDialog", circForm, request, response);
            } else if (null != errorMessage.getErrorCode() && errorMessage.getErrorCode().equals(DroolsConstants.GENERAL_MESSAGE_FLAG)) {
                showDialog("generalMessageDialog", circForm, request, response);
            } else {
                showErrorMessageDialog(form, request, response);
            }
        } else {
            resetItemInfoForNextTrans(circForm);
        }


        return getUIFModelAndView(circForm);
    }

    private void resetItemInfoForNextTrans(CircForm circForm) {
        circForm.setItemBarcode("");
        circForm.setItemRecord(null);
        circForm.setItemValidationDone(false);
    }

    @RequestMapping(params = "methodToCall=proceedToSaveLoan")
    public ModelAndView proceedToSaveLoan(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        String customDate = request.getParameter("customDueDateMap");
        if(StringUtils.isNotBlank(customDate)){
            circForm.setCustomDueDateMap(new DateUtil().getDate(customDate));
        }
        UifFormBase uifFormBase = getCheckoutController().proceedToSaveLoan(circForm);
        resetItemInfoForNextTrans(circForm);
        return getUIFModelAndView(uifFormBase, "circViewPage");
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

    private CheckoutController getCheckoutController() {
        if (null == checkoutController) {
            checkoutController = new CheckoutController();
        }
        return checkoutController;
    }
}
