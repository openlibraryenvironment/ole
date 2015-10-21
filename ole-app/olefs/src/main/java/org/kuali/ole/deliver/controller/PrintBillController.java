package org.kuali.ole.deliver.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.controller.checkin.CheckinItemController;
import org.kuali.ole.deliver.drools.CheckedInItem;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.deliver.util.printSlip.OlePrintSlipUtil;
import org.kuali.ole.deliver.util.printSlip.OnHoldRecieptPrintSlipUtil;
import org.kuali.ole.deliver.util.printSlip.OnHoldRegularPrintSlipUtil;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 8/28/15.
 */
@Controller
@RequestMapping(value = "/printBillcontroller")
public class PrintBillController extends CheckinItemController {


    @RequestMapping(params = "methodToCall=printBill")
    public void printBill(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                          HttpServletRequest request, HttpServletResponse response) {
        String formKey = request.getParameter("checkinFormKey");
        CheckinForm checkinForm = (CheckinForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        if (null != checkinForm) {
            printSlip(checkinForm, response);
        }
    }

    public void printSlip(CheckinForm checkinForm, HttpServletResponse response) {
        DroolsExchange droolsExchange = checkinForm.getDroolsExchange();
        if(null != droolsExchange){
            OleItemRecordForCirc oleItemRecordForCirc = (OleItemRecordForCirc) droolsExchange.getFromContext("oleItemRecordForCirc");
            if(null != oleItemRecordForCirc){
                OlePrintSlipUtil oleRegularPrintSlipUtil = getCheckinUIController(checkinForm).getOlePrintSlipUtil(oleItemRecordForCirc);
                if (null != oleRegularPrintSlipUtil) {
                    oleRegularPrintSlipUtil.createPdfForPrintingSlip(oleItemRecordForCirc, response);
                }
            }
        }
    }

    @RequestMapping(params = "methodToCall=printSlipForEndSession")
    public void printSlipForEndSession (@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response){
        String formKey = request.getParameter("checkinFormKey");
        CheckinForm checkinForm = (CheckinForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
        if(null != checkinForm && null != checkinForm.getPrintFormat()){
            List<CheckedInItem> checkedInItemList = checkinForm.getCheckedInItemList();
            OlePrintSlipUtil olePrintSlipUtil = null;
            if (checkinForm.getPrintFormat().equals(OLEConstants.RECEIPT_PRINTER)) {
                olePrintSlipUtil = new OnHoldRecieptPrintSlipUtil();
            } else {
                olePrintSlipUtil = new OnHoldRegularPrintSlipUtil();
            }
            olePrintSlipUtil.createPdfForEndSessionPrintSlip(checkedInItemList,response);
            checkinForm.resetAll();
        }
    }

}
