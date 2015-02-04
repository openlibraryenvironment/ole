package org.kuali.ole.deliver.controller;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.web.controller.InquiryController;
import org.kuali.rice.krad.web.form.InquiryForm;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 10/27/14
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/olePatronInquiry")
public class OlePatronDocumentInquiryController extends InquiryController {

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=hidePatronLoanedItem")
    public ModelAndView hidePatronLoanedItem(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {

        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
        olePatronDocument.setShowLoanedRecords(false);
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=showPatronLoanedItem")
    public ModelAndView showPatronLoanedItem(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
        olePatronDocument.setShowLoanedRecords(true);
        return getUIFModelAndView(form);
    }

    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return super.start(form, result, request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
