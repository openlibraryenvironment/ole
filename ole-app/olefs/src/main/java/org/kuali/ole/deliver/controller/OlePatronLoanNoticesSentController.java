package org.kuali.ole.deliver.controller;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.OLEDeliverNoticeHistory;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.form.OlePatronLoanNoticesSentForm;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OlePatronLoanNoticeService;
import org.kuali.ole.deliver.service.impl.OlePatronLoanNoticeServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
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
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 12/18/15.
 */
@Controller
@RequestMapping(value = "/loanSentNotices")
public class OlePatronLoanNoticesSentController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OlePatronLoanNoticesSentController.class);

    private BusinessObjectService businessObjectService;

    private OlePatronLoanNoticeService olePatronLoanNoticeService;

    public BusinessObjectService getBusinessObjectService(){
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

public OlePatronLoanNoticeService getOlePatronLoanNoticeService(){
    if(olePatronLoanNoticeService == null){
        olePatronLoanNoticeService = new OlePatronLoanNoticeServiceImpl();
    }
    return olePatronLoanNoticeService;
}

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OlePatronLoanNoticesSentForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OlePatronLoanNoticesSentForm oleLoanSentNoticesForm = (OlePatronLoanNoticesSentForm) form;
        return super.start(oleLoanSentNoticesForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=viewLoanSentNotices")
    public ModelAndView viewLoanSentNotices(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        OlePatronLoanNoticesSentForm oleLoanSentNoticesForm = (OlePatronLoanNoticesSentForm)form;
        oleLoanSentNoticesForm.setMessage(null);
        String loanId = request.getParameter("loanId");
        Map<String,String> criteriaMap = new HashMap<String,String>();
        criteriaMap.put("loanId",loanId);
        List<OLEDeliverNoticeHistory> oleDeliverNoticeHistories = (List<OLEDeliverNoticeHistory>)getBusinessObjectService().findMatching(OLEDeliverNoticeHistory.class,criteriaMap);
        oleLoanSentNoticesForm.setOleDeliverNoticeHistories(oleDeliverNoticeHistories);
        return getUIFModelAndView(oleLoanSentNoticesForm, "OleLoanNoticesSentViewPage");
    }

    @RequestMapping(params = "methodToCall=sendNotice")
    public ModelAndView sendNotice(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OlePatronLoanNoticesSentForm oleLoanSentNoticesForm = (OlePatronLoanNoticesSentForm)form;
        int index = Integer.parseInt(oleLoanSentNoticesForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEDeliverNoticeHistory oleDeliverNoticeHistory = oleLoanSentNoticesForm.getOleDeliverNoticeHistories().get(index);
        Map<String,String> patronMap = new HashMap<String,String>();
        patronMap.put("olePatronId",oleDeliverNoticeHistory.getPatronId());
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        if(olePatronDocumentList.size()>0){
            String mailAddress = olePatronDocumentList.get(0).getOlePatronEntityViewBo().getEmailAddress();
            getOlePatronLoanNoticeService().sendMail(mailAddress, new String(oleDeliverNoticeHistory.getNoticeContent()), new OleNoticeServiceImpl().getNoticeSubjectForNoticeType(oleDeliverNoticeHistory.getNoticeType()));
            oleLoanSentNoticesForm.setMessage("Mail Send Successfully for the patron  " + olePatronDocumentList.get(0).getPatronName()+" to the mail id "+mailAddress );
            OLEDeliverNoticeHistory oleDeliverNoticeHistory1 = getOlePatronLoanNoticeService().cloneOleDeliverNoticeHistory(oleDeliverNoticeHistory);
            getBusinessObjectService().save(oleDeliverNoticeHistory1);
            oleLoanSentNoticesForm.getOleDeliverNoticeHistories().add(oleDeliverNoticeHistory1);
        }
        return getUIFModelAndView(oleLoanSentNoticesForm);
    }
}