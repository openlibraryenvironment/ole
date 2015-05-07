package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OleCirculationDesk;

import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.krad.OleInquiryController;
import org.kuali.rice.krad.web.form.InquiryForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by hemalathas on 2/25/15.
 */
@Controller
@RequestMapping(value = "/oleCirculationDeskInquiry")
public class OLECirculationDeskInquiryController extends OleInquiryController {

    private static final Logger LOG = Logger.getLogger(OlePatronDocumentInquiryController.class);
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=generateOnHoldNotices")
    public ModelAndView generateOnHoldNotices(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {

        InquiryForm form = (InquiryForm) uifForm;
        OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        OleCirculationDesk oleCirculationDesk = (OleCirculationDesk)form.getDataObject();
        try{
           oleDeliverRequestDocumentHelperService.generateOnHoldNoticesBasedOnPickupLocation(oleCirculationDesk.getCirculationDeskId());
        }catch (Exception e) {
            LOG.error("While fetching loan records error occured" + e);
        }
        return getUIFModelAndView(form);
    }
}
