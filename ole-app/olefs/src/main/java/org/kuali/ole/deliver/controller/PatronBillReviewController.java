package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.bo.PatronBillReview;
import org.kuali.ole.deliver.form.PatronBillReviewForm;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/6/12
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/patronBillReview")
public class PatronBillReviewController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(org.kuali.ole.deliver.controller.PatronBillReviewController.class);


    private BusinessObjectService businessObjectService;

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * This method creates new PatronBillReview form
     *
     * @param request
     * @return PatronBillReviewForm
     */
    @Override
    protected PatronBillReviewForm createInitialForm(HttpServletRequest request) {
        return new PatronBillReviewForm();
    }

    /**
     * This method takes the initial request when click(Patron Bill) on Patron Screen.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        PatronBillReviewForm patronBillReviewForm = (PatronBillReviewForm) form;
        PatronBillReview patronBillReview = new PatronBillReview();
        List<PatronBillReviewForm> patronBillReviewFormList = patronBillReview.getPatronBill();
        if (patronBillReviewFormList.size() == 0) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_BILLS);
        }
        patronBillReviewForm.setPatronBillReviewFormList(patronBillReviewFormList);

        List<PatronBillReviewForm> patronBillReviewedFormList = patronBillReview.getPatronReviewedBill();
        patronBillReviewForm.setPatronBillReviewedFormList(patronBillReviewedFormList);
        return super.start(patronBillReviewForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=review")
    public ModelAndView billReviewed(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        PatronBillReviewForm patronBillReviewForm = (PatronBillReviewForm) form;
        List<PatronBillReviewForm> patronBillReviewFormList = patronBillReviewForm.getPatronBillReviewFormList();
        Iterator itr = patronBillReviewFormList.iterator();
        PatronBillReviewForm patronBillReviewForms;
        int count = 0;
        while (itr.hasNext()) {
            patronBillReviewForms = (PatronBillReviewForm) itr.next();
            boolean billReview = patronBillReviewForms.isSelect();
            if (billReview) {
                count += 1;
                String billNumber = patronBillReviewForms.getBillNumber();
                Map billNumberMap = new HashMap();
                billNumberMap.put("billNumber", billNumber);
                PatronBillPayment patronBillPayment = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(PatronBillPayment.class, billNumberMap);
                patronBillPayment.setReviewed(true);
                getBusinessObjectService().save(patronBillPayment);
            }
        }
        if (count == 0) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.SELECT);
        }

        return start(patronBillReviewForm, result, request, response);
    }
}




