package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.krad.OleInquiryController;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 10/27/14
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/olePatronInquiry")
public class OlePatronDocumentInquiryController extends OleInquiryController {
    private static final Logger LOG = Logger.getLogger(OlePatronDocumentInquiryController.class);

    private LoanProcessor loanProcessor;

    private LoanProcessor getLoanProcessor() {
        if (loanProcessor == null) {
            loanProcessor = new LoanProcessor();
        }
        return loanProcessor;
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=hidePatronLoanedItem")
    public ModelAndView hidePatronLoanedItem(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {

        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
       // olePatronDocument.setOleLoanDocuments(new ArrayList<OleLoanDocument>());
        olePatronDocument.setShowLoanedRecords(false);
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=showPatronLoanedItem")
    public ModelAndView showPatronLoanedItem(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
        try {
            olePatronDocument.setOleLoanDocuments(getLoanProcessor().getPatronLoanedItemBySolr(olePatronDocument.getOlePatronId()));
        } catch (Exception e) {
            LOG.error("While fetching loan records error occured" + e);
        }
        olePatronDocument.setShowLoanedRecords(true);
        return getUIFModelAndView(form);
    }

    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return super.start(form, result, request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=showPatronRequestedRecords")
    public ModelAndView showPatronRequestedRecords(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Patron View : showing Patron Requested Records");
        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
        try {
            OleDeliverRequestDocumentHelperServiceImpl requestService = new OleDeliverRequestDocumentHelperServiceImpl();
            List<OleDeliverRequestBo> oleDeliverRequestBoList = olePatronDocument.getOleDeliverRequestBos();
            if (oleDeliverRequestBoList.size() > 0) {
                for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
                    OleItemSearch oleItemSearch = requestService.getItemDetailsForPatron(oleDeliverRequestBoList.get(i).getItemUuid());
                    if (oleItemSearch != null && oleItemSearch.getItemBarCode() != null) {
                        oleDeliverRequestBoList.get(i).setTitle(oleItemSearch.getTitle());
                        oleDeliverRequestBoList.get(i).setCallNumber(oleItemSearch.getCallNumber());
                        oleDeliverRequestBoList.get(i).setAuthor(oleItemSearch.getAuthor());
                        oleDeliverRequestBoList.get(i).setItemType(oleItemSearch.getItemType());
                        oleDeliverRequestBoList.get(i).setItemStatus(oleItemSearch.getItemStatus());
                        oleDeliverRequestBoList.get(i).setShelvingLocation(oleItemSearch.getShelvingLocation());
                        oleDeliverRequestBoList.get(i).setCopyNumber(oleItemSearch.getCopyNumber());
                    }
                }
            }
            olePatronDocument.setOleDeliverRequestBos(oleDeliverRequestBoList);
        } catch (Exception e) {
            LOG.error("While fetching Patron Requested Records error occured" + e);
        }
        olePatronDocument.setShowRequestedItems(true);
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=hidePatronRequestedRecords")
    public ModelAndView hidePatronRequestedRecords(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Patron View : Hiding Patron Loaned Records");
        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
       // olePatronDocument.setOleDeliverRequestBos(new ArrayList<OleDeliverRequestBo>());
        olePatronDocument.setShowRequestedItems(false);
        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=showTemporaryCirculationHistoryRecords")
    public ModelAndView showTemporaryCirculationHistoryRecords(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                               HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Patron View : showing Patron TemporaryCirculationHistory Records");
        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
        try {
            olePatronDocument.setOleTemporaryCirculationHistoryRecords(getLoanProcessor().getPatronTemporaryCirculationHistoryRecords(olePatronDocument.getOlePatronId()));
        } catch (Exception e) {
            LOG.error("While fetching Patron TemporaryCirculationHistory Records error occured" + e);
        }

        olePatronDocument.setShowTemporaryCirculationHistoryRecords(true);
        return getUIFModelAndView(form);
    }
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=hideTemporaryCirculationHistoryRecords")
    public ModelAndView hideTemporaryCirculationHistoryRecords(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                                               HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Patron View : Hiding Patron Loaned Records");
        InquiryForm form = (InquiryForm) uifForm;
        OlePatronDocument olePatronDocument=(OlePatronDocument)form.getDataObject();
      //  olePatronDocument.setOleTemporaryCirculationHistoryRecords(new ArrayList<OleTemporaryCirculationHistory>());
        olePatronDocument.setShowTemporaryCirculationHistoryRecords(false);
        return getUIFModelAndView(form);
    }
}
