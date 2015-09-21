package org.kuali.ole.deliver.controller;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pvsubrah on 8/5/15.
 */

@Controller
@RequestMapping(value = "/autoCheckoutController")
public class AutoCheckoutController extends CircController {

    @RequestMapping(params = "methodToCall=backgroundCheckout")
    public ModelAndView backgroundCheckout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        String formKey = request.getParameter("checkinFormKey");
        CheckinForm checkinForm = (CheckinForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        if (null != checkinForm) {
            circForm.setSelectedCirculationDesk(checkinForm.getSelectedCirculationDesk());
            circForm.setAutoCheckout(true);
            DroolsExchange droolsExchange = checkinForm.getDroolsExchange();
            if (null != droolsExchange) {
                OleDeliverRequestBo requestBo = (OleDeliverRequestBo) droolsExchange.getFromContext("requestBo");
                if (null != requestBo) {
                    circForm.setPatronBarcode(requestBo.getBorrowerBarcode());
                    circForm.setItemBarcode(requestBo.getItemId());

                }
            }
        }
        ModelAndView modelAndView = searchPatron(circForm, result, request, response);
        return modelAndView;
    }

}
