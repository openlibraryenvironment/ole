package org.kuali.ole.deliver.controller;

import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.service.OLEDeliverNoticeService;
import org.kuali.ole.deliver.form.OLEDeliverNoticeSearchForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 10/16/15.
 */
@Controller
@RequestMapping(value = "/deliverNoticeSearchController")
public class OLEDeliverNoticeSearchController extends UifControllerBase {

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEDeliverNoticeSearchForm();
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm = (OLEDeliverNoticeSearchForm) form;
        OLEDeliverNoticeService oleNoticeService = new OLEDeliverNoticeService();
        List<File> expiredRequestList = new ArrayList<File>();
        List<File> courtesyNoticeList = new ArrayList<File>();
        List<File> overDueNoticeList = new ArrayList<File>();
        List<File> onHoldNoticeList = new ArrayList<File>();
        List<File> recallNoticeList = new ArrayList<File>();
        List<File> pickupNoticeList = new ArrayList<File>();
        List<File> onHoldCourtesyNoticeList = new ArrayList<File>();
        LoanProcessor loanProcessor = new LoanProcessor();
        String pdfLocationSystemParam = loanProcessor.getParameter(OLEParameterConstants.PDF_LOCATION);
        if (pdfLocationSystemParam == null || pdfLocationSystemParam.trim().isEmpty()) {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("staging.directory") + "/";
        } else {
            pdfLocationSystemParam = ConfigContext.getCurrentContextConfig().getProperty("homeDirectory") + "/" + pdfLocationSystemParam + "/";
        }
        File directory = new File(pdfLocationSystemParam);
        File[] fList = directory.listFiles();
        if (fList != null && fList.length > 0) {
            for (File file : fList) {
                if (file.isFile()) {
                    if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.EXP_REQ_TITLE).replaceAll(" ", "_"))) {
                        expiredRequestList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.COURTESY_TITLE).replaceAll(" ", "_"))) {
                        courtesyNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.OVERDUE_TITLE).replaceAll(" ", "_"))) {
                        overDueNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.EXPIRED_TITLE).replaceAll(" ", "_"))) {
                        onHoldCourtesyNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.ONHOLD_TITLE).replaceAll(" ", "_"))) {
                        onHoldNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.RECALL_TITLE).replaceAll(" ", "_"))) {
                        recallNoticeList.add(file);
                    } else if (file.getName().contains(loanProcessor.getParameter(OLEParameterConstants.PICKUP_TITLE).replaceAll(" ", "_"))) {
                        pickupNoticeList.add(file);
                    }
                }
            }
        }
        oleDeliverNoticeSearchForm.setOleRecallNoticeList(oleNoticeService.generateRecallNoticeList(recallNoticeList));
        oleDeliverNoticeSearchForm.setOleCourtesyNoticeList(oleNoticeService.generateCourtesyNoticeList(courtesyNoticeList));
        oleDeliverNoticeSearchForm.setOleExpiredRequestNoticeList(oleNoticeService.generateExpiredRequestNoticeList(expiredRequestList));
        oleDeliverNoticeSearchForm.setOleOnHoldCourtesyNoticeList(oleNoticeService.generateOnHoldCourtesyNoticeList(onHoldCourtesyNoticeList));
        oleDeliverNoticeSearchForm.setOleOnHoldNoticeList(oleNoticeService.generateOnHoldNoticeList(onHoldNoticeList));
        oleDeliverNoticeSearchForm.setOleOverDueNoticeList(oleNoticeService.generateOverDueNoticeList(overDueNoticeList));
        oleDeliverNoticeSearchForm.setOlePickupNoticeList(oleNoticeService.generatePickupNoticeList(pickupNoticeList));
        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverNoticeSearchForm oleDeliverNoticeSearchForm = (OLEDeliverNoticeSearchForm) form;

        oleDeliverNoticeSearchForm.setPatronBarcode(null);
        oleDeliverNoticeSearchForm.setItemBarcode(null);
        oleDeliverNoticeSearchForm.setDateSentFrom(null);
        oleDeliverNoticeSearchForm.setDateSentTo(null);
        oleDeliverNoticeSearchForm.setDeskLocation(null);
        oleDeliverNoticeSearchForm.setNoticeType(null);
        oleDeliverNoticeSearchForm.setOleCourtesyNoticeList(new ArrayList<OLECourtesyNotice>());
        oleDeliverNoticeSearchForm.setOleExpiredRequestNoticeList(new ArrayList<OLEExpiredRequestNotice>());
        oleDeliverNoticeSearchForm.setOleOnHoldNoticeList(new ArrayList<OLEOnHoldNotice>());
        oleDeliverNoticeSearchForm.setOleOnHoldCourtesyNoticeList(new ArrayList<OLEOnHoldCourtesyNotice>());
        oleDeliverNoticeSearchForm.setOleOverDueNoticeList(new ArrayList<OLEOverDueNotice>());
        oleDeliverNoticeSearchForm.setOleRecallNoticeList(new ArrayList<OLERecallNotice>());
        oleDeliverNoticeSearchForm.setOlePickupNoticeList(new ArrayList<OLEPickupNotice>());
        return getUIFModelAndView(oleDeliverNoticeSearchForm);
    }
}
