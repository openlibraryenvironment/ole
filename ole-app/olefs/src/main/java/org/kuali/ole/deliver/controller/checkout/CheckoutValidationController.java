package org.kuali.ole.deliver.controller.checkout;

import org.kuali.ole.deliver.controller.PermissionsValidatorUtil;
import org.kuali.ole.deliver.form.CircForm;
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
public class CheckoutValidationController extends CheckoutPatronController {

    @RequestMapping(params = "methodToCall=validateOveridePermission")
    public ModelAndView validateOveridePermission(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        circForm.getErrorMessage().clearErrorMessage();
        boolean hasValidOverridePermissions = new PermissionsValidatorUtil().hasValidOverridePermissions(circForm);
        if (!hasValidOverridePermissions) {
            circForm.setOverridingPrincipalName(null);
            return showDialog("overrideMessageDialog", circForm, request, response);
        }
        if (circForm.isProxyCheckDone() && circForm.isItemValidationDone()) {
            return proceedToSaveLoan(circForm, result, request, response);
        }
        return handleProxyPatronsIfExists(circForm, result, request, response);
    }
}
