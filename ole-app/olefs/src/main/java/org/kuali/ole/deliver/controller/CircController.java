package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkout.CheckoutValidationController;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.util.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by pvsubrah on 6/3/15.
 */

@Controller
@RequestMapping(value = "/circcontroller")
public class CircController extends CheckoutValidationController {

    private static final Logger LOG = Logger.getLogger(CircController.class);
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;

    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        if (!circForm.isProceedWithCheckout()) {
            circForm.setPatronDocument(new OlePatronRecordUtil().getPatronRecordByBarcode(circForm.getPatronBarcode()));
            return searchPatron(form, result, request, response);
        } else {
            lookupItemAndSaveLoan(circForm, result, request, response);
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
            circForm.setShowExistingLoan(true);
            if (circForm.getPatronDocument().getOleLoanDocuments().size() > 0) {
                circForm.setExistingLoanList(getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr
                        (circForm.getPatronDocument().getOlePatronId(), null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getUIFModelAndView(form);
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
        return showDialog("circDeskChangeDialog", form, request, response);
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

    public OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();

        }
        return oleLoanDocumentsFromSolrBuilder;
    }
}
