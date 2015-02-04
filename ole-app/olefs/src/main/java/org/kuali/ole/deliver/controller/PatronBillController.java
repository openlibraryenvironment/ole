package org.kuali.ole.deliver.controller;

import com.lowagie.text.Chunk;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.PatronBillForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 9/7/12
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping(value = "/patronbill")
public class PatronBillController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(PatronBillController.class);


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
     * This method creates new PatronBill form
     *
     * @param request
     * @return PatronBillForm
     */
    @Override
    protected PatronBillForm createInitialForm(HttpServletRequest request) {
        return new PatronBillForm();
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
        PatronBillForm patronBillForm = (PatronBillForm) form;
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        if (patronBillForm.getPatronId() == null && patronBillForm.getOlePatronId() == null) {
            patronBillForm.setOlePatronDocument(patronBillHelperService.getPatronDetails(patronBillForm.getPatronId()));
        }
        if (patronBillForm.getPatronId() != null) {
            patronBillForm.setOlePatronId(patronBillForm.getPatronId());
        }
        if (patronBillForm.getOlePatronId() != null) {
            patronBillForm.setOlePatronDocument(patronBillHelperService.getPatronDetails(patronBillForm.getOlePatronId()));
        }

        /*if(patronBillForm.getPatronBillPaymentList()!=null && patronBillForm.getPatronBillPaymentList().size()>0){
                patronBillForm.setPatronBillPaymentList(patronBillHelperService.sortById(patronBillForm.getPatronBillPaymentList()));

        }else{
            patronBillForm.setPatronBillPaymentList(patronBillHelperService.getBillPayment(patronBillForm.getPatronId()));

        }

        patronBillForm.setFeeTypes(patronBillHelperService.getFeeTypeList(patronBillForm.getPatronBillPaymentList()));
       *//* patronBillForm.setPatronBillPaymentList(patronBillHelperService.getBillPayment(patronBillForm.getPatronId()));*//*
        *//*patronBillForm.setPatronBillPaymentList(patronBillHelperService.sortById(patronBillForm.getPatronBillPaymentList()));
        KualiDecimal KualiDecimal = patronBillHelperService.populateGrandTotal(patronBillForm.getPatronBillPaymentList());
        patronBillForm.setGrandTotal(KualiDecimal);*/
        if (patronBillForm.getPatronId() != null) {
            patronBillForm.setOlePatronId(patronBillForm.getPatronId());
        }
        Map patronIdMap = new HashMap();
        patronIdMap.put("patronId", patronBillForm.getPatronId());
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<PatronBillPayment> patronBillPayments = (List<PatronBillPayment>) getBusinessObjectService().findMatching(PatronBillPayment.class, patronIdMap);
        patronBillForm.setPatronBillPaymentList(patronBillHelperService.sortById(patronBillPayments));
        /*for (PatronBillPayment patronBillPayment : patronBillPayments) {
            feeTypes.addAll(patronBillPayment.getFeeType());
        }*/
        List<FeeType> feeTypeList = patronBillHelperService.getFeeTypeList(patronBillPayments);

        //patronBillForm.setPatronBillPaymentList(patronBillPayments);
        patronBillForm.setFeeTypes(feeTypeList);

        KualiDecimal KualiDecimal = patronBillHelperService.populateGrandTotal(patronBillForm.getPatronBillPaymentList());
        patronBillForm.setGrandTotal(KualiDecimal);
        patronBillForm.setPaymentAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setPaymentMethod("");
        patronBillForm.setBillWisePayment(OLEConstants.OlePatron.DEFAULT);
        patronBillForm.setTransactionNumber("");
        patronBillForm.setTransactionNote("");
        /*form.setFormPostUrl(form.getFormPostUrl()+"?viewId=BillView&methodToCall=start&patronId="+patronBillForm.getOlePatronId());*/
        return super.start(patronBillForm, result, request, response);
    }

    /**
     * This method will accept payment from user and update the record in Patron bill payment table.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=accept")
    public ModelAndView acceptPayment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized acceptPayment Method");
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setMessage("");

        boolean valid = true;
        if (patronBillForm.getPaymentAmount() == null) {
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAY_AMT_EMPTY);
        } else {
            int signum = patronBillForm.getPaymentAmount().bigDecimalValue().signum();
            if (signum < 0) {
                valid = false;
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.NEGATIVE_NUM);
            }
        }
        if (patronBillForm.getPaymentMethod() == null || patronBillForm.getPaymentMethod().trim().isEmpty()) {
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAY_METHOD_REQUIRED);
        }
        if (!valid) {
            return getUIFModelAndView(patronBillForm);
        }
        KualiDecimal paymentAmount = patronBillForm.getPaymentAmount();
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
            List<PatronBillPayment> patronBillPayments = patronBillForm.getPatronBillPaymentList();
            if (!patronBillHelperService.isSelectedPaidPatronBillFullyPaid(patronBillPayments)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedPatronBills(patronBillPayments)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            paymentAmount = patronBillHelperService.billWiseTransaction(patronBillPayments, paymentAmount, patronBillForm.getPaymentMethod(), OLEConstants.FULL_PAID, OLEConstants.PAR_PAID, false, null, patronBillForm.getTransactionNumber(), patronBillForm.getTransactionNote(), patronBillForm.getPaidByUser(), patronBillForm.getCurrentSessionTransactions());
        } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
            List<FeeType> feeTypes = patronBillForm.getFeeTypes();
            if (!patronBillHelperService.isSelectedFeeTypeFullyPaid(feeTypes)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedFeeTypes(feeTypes)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            paymentAmount = patronBillHelperService.itemWiseTransaction(feeTypes, paymentAmount, patronBillForm.getPaymentMethod(), OLEConstants.FULL_PAID, OLEConstants.PAR_PAID, null, patronBillForm.getTransactionNumber(), patronBillForm.getTransactionNote(), patronBillForm.getPaidByUser(), patronBillForm.getCurrentSessionTransactions());
        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.SELECT);
            return getUIFModelAndView(patronBillForm);
        }
        if (paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0) {
            patronBillForm.setMessage(OLEConstants.BAL_AMT + paymentAmount);
        }

        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setPaymentAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setTransactionNumber("");
        patronBillForm.setTransactionNote("");
        return start(patronBillForm, result, request, response);
    }


    /**
     * This method will allow library operator to forgive payment for user
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=forgive")
    public ModelAndView forgivePayment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized forgivePayment Method");
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        patronBillForm.setMessage("");
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        boolean valid = true;
        if (patronBillForm.getPaymentAmount() == null) {
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAY_AMT_EMPTY);
        } else {
            int signum = patronBillForm.getPaymentAmount().bigDecimalValue().signum();
            if (signum < 0) {
                valid = false;
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.NEGATIVE_NUM);
            }
        }
       /* if(patronBillForm.getPaymentMethod()==null || patronBillForm.getPaymentMethod().trim().isEmpty()){
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAY_METHOD_REQUIRED );
        }*/
        if (!valid) {
            return getUIFModelAndView(patronBillForm);
        }
        KualiDecimal paymentAmount = patronBillForm.getPaymentAmount();
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
            List<PatronBillPayment> patronBillPayments = patronBillForm.getPatronBillPaymentList();
            if (!patronBillHelperService.isSelectedPaidPatronBillFullyPaid(patronBillPayments)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedPatronBills(patronBillPayments)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            paymentAmount = patronBillHelperService.billWiseTransaction(patronBillPayments, paymentAmount, OLEConstants.FORGIVE, OLEConstants.FORGIVEN, OLEConstants.PAR_PAID, false, patronBillForm.getForgiveNote(), patronBillForm.getTransactionNumber(), patronBillForm.getTransactionNote(), patronBillForm.getPaidByUser(), patronBillForm.getCurrentSessionTransactions());
        } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
            List<FeeType> feeTypes = patronBillForm.getFeeTypes();
            if (!patronBillHelperService.isSelectedFeeTypeFullyPaid(feeTypes)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedFeeTypes(feeTypes)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            paymentAmount = patronBillHelperService.itemWiseTransaction(feeTypes, paymentAmount, OLEConstants.FORGIVE, OLEConstants.FORGIVEN, OLEConstants.PAR_PAID, patronBillForm.getForgiveNote(), patronBillForm.getTransactionNumber(), patronBillForm.getTransactionNote(), patronBillForm.getPaidByUser(), patronBillForm.getCurrentSessionTransactions());

        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.SELECT);
            return start(patronBillForm, result, request, response);
        }
        if (paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0) {
            patronBillForm.setMessage(OLEConstants.BAL_AMT + paymentAmount);
        }
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setTransactionNumber("");
        patronBillForm.setTransactionNote("");
        return start(patronBillForm, result, request, response);
    }

    /**
     * This method will allow library operator to error payment for user
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=error")
    public ModelAndView errorPayment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized forgivePayment Method");
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setMessage("");
        boolean valid = true;
        if (patronBillForm.getPaymentAmount() == null) {
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAY_AMT_EMPTY);
        } else {
            int signum = patronBillForm.getPaymentAmount().bigDecimalValue().signum();
            if (signum < 0) {
                valid = false;
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.NEGATIVE_NUM);
            }
        }
      /*  if(patronBillForm.getPaymentMethod()==null || patronBillForm.getPaymentMethod().trim().isEmpty()){
            valid = false;
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.PAY_METHOD_REQUIRED );
        }*/
        if (!valid) {
            return getUIFModelAndView(patronBillForm);
        }
        KualiDecimal paymentAmount = patronBillForm.getPaymentAmount();
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
            List<PatronBillPayment> patronBillPayments = patronBillForm.getPatronBillPaymentList();
            if (!patronBillHelperService.isSelectedPaidPatronBillFullyPaid(patronBillPayments)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedPatronBills(patronBillPayments)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            paymentAmount = patronBillHelperService.billWiseTransaction(patronBillPayments, paymentAmount, OLEConstants.ERROR, OLEConstants.IN_ERROR, OLEConstants.PAR_PAID, false, patronBillForm.getErrorNote(), patronBillForm.getTransactionNumber(), patronBillForm.getTransactionNote(), patronBillForm.getPaidByUser(), patronBillForm.getCurrentSessionTransactions());
        } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
            List<FeeType> feeTypes = patronBillForm.getFeeTypes();
            if (!patronBillHelperService.isSelectedFeeTypeFullyPaid(feeTypes)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedFeeTypes(feeTypes)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            paymentAmount = patronBillHelperService.itemWiseTransaction(feeTypes, paymentAmount, OLEConstants.ERROR, OLEConstants.IN_ERROR, OLEConstants.PAR_PAID, patronBillForm.getErrorNote(), patronBillForm.getTransactionNumber(), patronBillForm.getTransactionNote(), patronBillForm.getPaidByUser(), patronBillForm.getCurrentSessionTransactions());

        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.SELECT);
            return start(patronBillForm, result, request, response);
        }
        if (paymentAmount.compareTo(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE) > 0) {
            patronBillForm.setMessage(OLEConstants.BAL_AMT + paymentAmount);
        }
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setTransactionNumber("");
        patronBillForm.setTransactionNote("");
        return start(patronBillForm, result, request, response);
    }

    /**
     * This method will allow library operator to forgive payment for user
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=print")
    public ModelAndView printBill(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Initialized printBill Method");
        PatronBillForm patronBillForm = (PatronBillForm) form;
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        patronBillForm.setMessage("");
        PrintBill printBill = new PrintBill();
        List<PatronBillPayment> patronBillList = new ArrayList<PatronBillPayment>();
        List<PatronBillPayment> patronBillPaymentList = new ArrayList<PatronBillPayment>();
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        OlePatronDocument olePatronDocument = patronBillForm.getOlePatronDocument();
        String firstName = olePatronDocument.getFirstName();
        String lastName = olePatronDocument.getLastName();
        List<String> transactionIds = new ArrayList<>();
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
            for (PatronBillPayment patronBillPayment : patronBillForm.getPatronBillPaymentList()) {
                if (patronBillPayment.isSelectBill()) {
                    patronBillPaymentList.add(patronBillPayment);
                    feeTypes = patronBillHelperService.getFeeTypeList(patronBillPaymentList);
                }
            }
            printBill.generatePdf(firstName, lastName, patronBillPaymentList, feeTypes, false, transactionIds, response);
        } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
            patronBillList = patronBillForm.getPatronBillPaymentList();
            for (FeeType feeType : patronBillForm.getFeeTypes()) {
                if (feeType.isActiveItem()) {
                    feeTypes.add(feeType);
                }
            }
            printBill.generatePdf(firstName, lastName, patronBillList, feeTypes, false, transactionIds, response);
        } else {
            feeTypes.addAll(patronBillForm.getFeeTypes());
            for (OleItemLevelBillPayment oleItemLevelBillPayment : patronBillForm.getCurrentSessionTransactions()) {
                transactionIds.add(oleItemLevelBillPayment.getPaymentId());
            }
            printBill.generatePdf(firstName, lastName, patronBillList, feeTypes, true, transactionIds, response);
        }
        patronBillForm.setTransactionNumber("");
        patronBillForm.setTransactionNote("");
        return getUIFModelAndView(patronBillForm, OLEConstants.OlePatron.BILL_VIEW_PAGE);
    }

    /**
     * This method will allow library operator to cancel payment for user
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=payment")
    public ModelAndView payment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        PatronBillForm patronBillForm = (PatronBillForm) form;
        patronBillForm.setMessage("");
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        form.setFormPostUrl(form.getFormPostUrl() + "?viewId=BillView&methodToCall=start&patronId=" + patronBillForm.getOlePatronId());
        return getUIFModelAndView(form, OLEConstants.OlePatron.BILL_VIEW_PAGE);
    }

    /**
     * Just returns as if return with no value was selected.
     */
    @Override
    @RequestMapping(params = "methodToCall=back")
    public ModelAndView back(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        return super.back(form, result, request, response);
    }

    /**
     * This method will allow library operator to enter note for cancel
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=cancellationNote")
    public ModelAndView cancellationNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        patronBillForm.setPatronAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setOlePatronDocument(patronBillHelperService.getPatronDetails(patronBillForm.getPatronId()));
        KualiDecimal paymentAmount = patronBillForm.getPaymentAmount();
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
            List<PatronBillPayment> patronBillPayments = patronBillForm.getPatronBillPaymentList();
            if (!patronBillHelperService.isSelectedPaidPatronBillFullyPaid(patronBillPayments)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedPatronBills(patronBillPayments)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            patronBillHelperService.billWiseCancellation(patronBillPayments, patronBillForm.getCancellationNote(), patronBillForm.getCurrentSessionTransactions());
        } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
            List<FeeType> feeTypes = patronBillForm.getFeeTypes();
            if (!patronBillHelperService.isSelectedFeeTypeFullyPaid(feeTypes)) {
                List<String> billIds = patronBillHelperService.getBillIds();
                Collections.sort(billIds);
                String buffer = "";
                for (String billId : billIds) {
                    buffer = buffer + billId + ",";
                }
                if (buffer.length() > 0) {
                    buffer = buffer.substring(0, buffer.length() - 1);
                }
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_SELECTED_ITEM_FULLY_PAID, new String[]{buffer.toString()});
                return getUIFModelAndView(form);
            }
            if (paymentAmount != null && paymentAmount.compareTo(patronBillHelperService.getSumOfSelectedFeeTypes(feeTypes)) > 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_OVER_PAYMENT);
                return getUIFModelAndView(form);
            }
            patronBillHelperService.itemWiseCancellation(feeTypes, patronBillForm.getCancellationNote(), patronBillForm.getCurrentSessionTransactions());
            patronBillForm.setPatronBillPaymentList(null);
        } else {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.SELECT);
        }
        /*try {
            refresh(form, result, request, response)  ;
        } catch (Exception e) {
             LOG.error(e,e);  //To change body of catch statement use File | Settings | File Templates.
        }*/
        patronBillForm.setPatronBillPaymentList(patronBillHelperService.getBillPayment(patronBillForm.getPatronId()));
        patronBillForm.setFeeTypes(patronBillHelperService.getFeeTypeList(patronBillForm.getPatronBillPaymentList()));
        KualiDecimal KualiDecimal = patronBillHelperService.populateGrandTotal(patronBillForm.getPatronBillPaymentList());
        patronBillForm.setGrandTotal(KualiDecimal);
        patronBillForm.setTransactionNumber("");
        patronBillForm.setTransactionNote("");
        return super.start(patronBillForm, result, request, response);
    }

    /**
     * This method will allow library operator to enter note for cancel
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=printBillPayment")
    public ModelAndView printBillPayment(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized printBillPayment Method");
        PatronBillForm patronBillForm = (PatronBillForm) form;
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        List<PatronBillPayment> patronBillPaymentList = patronBillHelperService.getBillPayment(patronBillForm.getOlePatronId());
        List<FeeType> feeTypeList = patronBillHelperService.getFeeTypeList(patronBillPaymentList);
        patronBillForm.setPrintBillReview(true);
        patronBillForm.setPrintFlag(true);
        boolean review = false;
        String reviewBillParameter = patronBillHelperService.getParameter(OLEConstants.OlePatron.PATRON_BILL_REVIEW_PRINT);
        boolean reviewParameter = reviewBillParameter.equalsIgnoreCase(OLEConstants.TRUE);
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.OlePatron.DEFAULT)) {
            if (patronBillForm.getCurrentSessionTransactions().size() == 0) {
                patronBillForm.setPrintFlag(false);
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.ERROR_TRANSACTION_SELECT);
                return getUIFModelAndView(patronBillForm, OLEConstants.OlePatron.BILL_VIEW_PAGE);
            }
        }
        if (patronBillForm.getPatronBillPaymentList().size() > 0) {
            for (PatronBillPayment patronBill : patronBillForm.getPatronBillPaymentList()) {
                if (patronBillPaymentList.size() > 0) {
                    for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
                        if (patronBillPayment.getBillNumber().equals(patronBill.getBillNumber())) {
                            if (patronBill.isSelectBill()) {
                                review = patronBillPayment.isReviewed();
                                if ((reviewParameter && !review)) {
                                    patronBillForm.setPrintBillReview(false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (patronBillForm.getFeeTypes().size() > 0) {
            for (FeeType feeTypeBo : patronBillForm.getFeeTypes()) {
                if (feeTypeList.size() > 0) {
                    for (FeeType feeType : feeTypeList) {
                        if (feeType.getBillNumber().equals(feeTypeBo.getBillNumber())) {
                            if (feeTypeBo.isActiveItem()) {
                                PatronBillPayment billPayment = feeType.getPatronBillPayment();
                                review = billPayment.isReviewed();
                                if ((reviewParameter && !review)) {
                                    patronBillForm.setPrintBillReview(false);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!patronBillForm.isPrintBillReview()) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.NOT_REVIEWED);
        }

        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<String> transactionIds = new ArrayList<>();
        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
            for (PatronBillPayment patronBillPayment : patronBillForm.getPatronBillPaymentList()) {
                if (patronBillPayment.isSelectBill()) {
                    feeTypes = patronBillHelperService.getFeeTypeList(patronBillPaymentList);
                    for (FeeType feeType : feeTypes) {
                        for (OleItemLevelBillPayment oleItemLevelBillPayment : feeType.getItemLevelBillPaymentList()) {
                            if(!transactionIds.contains(oleItemLevelBillPayment.getPaymentId())){
                                transactionIds.add(oleItemLevelBillPayment.getPaymentId());
                            }
                        }
                    }
                }
            }
        } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
            for (FeeType feeType : patronBillForm.getFeeTypes()) {
                if (feeType.isActiveItem()) {
                    for (OleItemLevelBillPayment oleItemLevelBillPayment : feeType.getItemLevelBillPaymentList()) {
                        if(!transactionIds.contains(oleItemLevelBillPayment.getPaymentId())){
                            transactionIds.add(oleItemLevelBillPayment.getPaymentId());
                        }
                    }
                }
            }
        } else {
            feeTypes.addAll(patronBillForm.getFeeTypes());
            for (OleItemLevelBillPayment oleItemLevelBillPayment : patronBillForm.getCurrentSessionTransactions()) {
                transactionIds.add(oleItemLevelBillPayment.getPaymentId());
            }
        }
        if(transactionIds.size()==0){
            patronBillForm.setPrintFlag(false);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.OUTSTANDING_BILL);
        }

        String[] formKey = (patronBillForm.getFormKey()).split(",");
        patronBillForm.setFormKey(formKey[0]);
        return getUIFModelAndView(patronBillForm, OLEConstants.OlePatron.BILL_VIEW_PAGE);
    }

    @RequestMapping(params = "methodToCall=mailToPatron")
    public ModelAndView mailToPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        PatronBillForm patronBillForm = (PatronBillForm) form;
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        List<PatronBillPayment> patronBillPaymentList = patronBillHelperService.getBillPayment(patronBillForm.getOlePatronId());
        List<FeeType> feeTypeList = patronBillHelperService.getFeeTypeList(patronBillPaymentList);
        if (patronBillForm.getPatronId() == null && patronBillForm.getOlePatronId() == null) {
            patronBillForm.setOlePatronDocument(patronBillHelperService.getPatronDetails(patronBillForm.getPatronId()));
        }
        List<FeeType> feeTypes = new ArrayList<FeeType>();
        List<String> transactionIds = new ArrayList<>();
        KualiDecimal feeAmount = new KualiDecimal(0);
        KualiDecimal paidAmount = new KualiDecimal(0);
        boolean isReadyToBuild = false;
        try {
            if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.OlePatron.DEFAULT)) {
                feeTypes = feeTypeList;
                for (OleItemLevelBillPayment oleItemLevelBillPayment : patronBillForm.getCurrentSessionTransactions()) {
                    transactionIds.add(oleItemLevelBillPayment.getPaymentId());
                }
            } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.ITEM_WISE)) {
                if (patronBillForm.getFeeTypes() != null) {
                    for (FeeType feeType : patronBillForm.getFeeTypes()) {
                        if (feeType.isActiveItem()) {
                            feeTypes.add(feeType);
                        }
                    }
                }
            } else if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.BILL_WISE)) {
                if (patronBillForm.getPatronBillPaymentList() != null) {
                    for (PatronBillPayment patronBillPayment : patronBillForm.getPatronBillPaymentList()) {
                        if (patronBillPayment != null) {
                            if (patronBillPayment.getFeeType() != null) {
                                feeTypes.addAll(patronBillPayment.getFeeType());
                            } else {

                            }
                        }
                    }

                }
            }
            if (transactionIds.size() == 0 && feeTypes.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.OUTSTANDING_BILL);
                return getUIFModelAndView(form);

            }
            if (feeTypes.size() > 0) {
                isReadyToBuild = true;
            }
            if (isReadyToBuild) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR>");
                buffer.append("<TABLE width=\"100%\">");
                buffer.append("<TR><TD><CENTER>" + "<b>" + OLEConstants.OlePatronBill.HEADER_PATRON_RECEIPT + "</b>" + "</CENTER></TD></TR>");
                buffer.append("<TR><TD><p>");
                buffer.append("<TR><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE>");
                buffer.append("<table id=\"PatronDetails\" style=\"text-align: center;\">");
                SimpleDateFormat df = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_PRINT);
                buffer.append("<tr><td style=\"text-align: left;\">Date       </td><td style=\"text-align: left;\">&nbsp;&nbsp;:&nbsp;&nbsp;" + (df.format(System.currentTimeMillis())) + "</td></tr>");
                buffer.append("<tr><td style=\"text-align: left;\">First Name </td><td style=\"text-align: left;\">&nbsp;&nbsp;:&nbsp;&nbsp;" + (patronBillForm.getOlePatronDocument().getFirstName() != null ? patronBillForm.getOlePatronDocument().getFirstName() : " ") + "</td></tr>");
                buffer.append("<tr><td style=\"text-align: left;\">Last Name  </td><td style=\"text-align: left;\">&nbsp;&nbsp;:&nbsp;&nbsp;" + (patronBillForm.getOlePatronDocument().getLastName() != null ? patronBillForm.getOlePatronDocument().getLastName() : " ") + "</td></tr>");
                StringBuffer bufferData = new StringBuffer();
                bufferData.append("</table><BR/><BR/><BR/><BR/>");
                bufferData.append("<table style=\"width: auto;text-align: center;\"  border=\"1\">");
                if (feeTypes != null) {
                    bufferData.append("<thead><tr>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_PATRON_RECEIPT_NUMBER + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_BILL_NUMBER + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_FEE_TYPE + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_TRANSACTION_DATE + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_OPERATOR_ID + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_ITEM_BARCODE + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_ITEM_TITLE + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_ITEM_AUTHOR + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_ITEM_CALL_NUMBER + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_TOTAL_AMOUNT + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_PAID_AMOUNT + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_TRANSACTION_NUMBER + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_TRANSACTION_NOTE + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_PAYMENT_MODE + "</b>" + "</td>");
                    bufferData.append("<td style=\"text-align: center;\">" + "<b>" + OLEConstants.OlePatronBill.LABEL_NOTE + "</b>" + "</td>");
                    bufferData.append("</tr></thead>");
                    bufferData.append("<tbody>");
                    for (FeeType feeType : feeTypes) {

                        List<OleItemLevelBillPayment> oleItemLevelBillPayments = new ArrayList<>();
                        if (feeType.getItemLevelBillPaymentList() != null) {
                            oleItemLevelBillPayments.addAll(feeType.getItemLevelBillPaymentList());
                        } else {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("lineItemId", feeType.getId());
                            List<OleItemLevelBillPayment> itemLevelBillPayments = (List<OleItemLevelBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(OleItemLevelBillPayment.class, map);
                            if (itemLevelBillPayments != null) {
                                oleItemLevelBillPayments.addAll(itemLevelBillPayments);
                            }
                        }
                        String feeTypeName = "";
                        if (feeType.getOleFeeType() != null && feeType.getOleFeeType().getFeeTypeName() != null) {
                            feeTypeName = feeType.getOleFeeType().getFeeTypeName();
                        }
                        for (OleItemLevelBillPayment oleItemLevelBillPayment : oleItemLevelBillPayments) {
                            boolean isAddContent = false;
                            if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.OlePatron.DEFAULT)) {
                                if (transactionIds.contains(oleItemLevelBillPayment.getPaymentId())) {
                                    isAddContent = true;
                                }
                            } else {
                                isAddContent = true;
                            }
                            if (isAddContent) {
                                bufferData.append("<tr>");
                                bufferData.append("<td style=\"text-align: center;\">" + (oleItemLevelBillPayment.getPaymentId() != null ? oleItemLevelBillPayment.getPaymentId() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getBillNumber() != null ? feeType.getBillNumber() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + feeTypeName + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (oleItemLevelBillPayment.getPaymentDate() != null ? df.format(oleItemLevelBillPayment.getPaymentDate()) : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (oleItemLevelBillPayment.getCreatedUser() != null ? oleItemLevelBillPayment.getCreatedUser() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getItemBarcode() != null ? feeType.getItemBarcode() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getItemTitle() != null ? feeType.getItemTitle() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getItemAuthor() != null ? feeType.getItemAuthor() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getItemCallNumber() != null ? feeType.getItemCallNumber() : " ") + "</td>");
                                /*bufferData.append("<td style=\"text-align: center;\">" + (feeType.getItemCopyNumber() != null ? feeType.getItemCopyNumber() : " ") + "</td>");*/
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getFeeAmount() != null ? "$" + feeType.getFeeAmount() : "$0") + "</td>");
                                bufferData.append("<td style=\"text-align: center; size:30px;\">" + (oleItemLevelBillPayment.getAmount() != null ? "$" + oleItemLevelBillPayment.getAmount() : "$0") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (oleItemLevelBillPayment.getTransactionNumber() != null ? oleItemLevelBillPayment.getTransactionNumber() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (oleItemLevelBillPayment.getTransactionNote() != null ? oleItemLevelBillPayment.getTransactionNote() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (oleItemLevelBillPayment.getPaymentMode() != null ? oleItemLevelBillPayment.getPaymentMode() : " ") + "</td>");
                                bufferData.append("<td style=\"text-align: center;\">" + (feeType.getGeneralNote() != null ? feeType.getGeneralNote() : " ") + "</td>");
                                bufferData.append("</tr>");
                                feeAmount = feeAmount.add(feeType.getFeeAmount());
                                paidAmount=paidAmount.add(oleItemLevelBillPayment.getAmount());
                            }

                        }

                    }
                    String totalAmountDue=feeAmount.subtract(paidAmount)!=null?feeAmount.subtract(paidAmount).toString():"0";
                    bufferData.append("</tbody>");
                    bufferData.append("</table>");
                    /*buffer.append("<tr><td style=\"text-align: left;\">"+OLEConstants.TOT_AMT+"  </td><td style=\"text-align: left;\">&nbsp;&nbsp;:&nbsp;&nbsp; &#x24;" + totalAmountDue + "</td></tr>");*/
                    buffer.append("<tr><td style=\"text-align: left;\">"+OLEConstants.TOT_AMT_PAID+"  </td><td style=\"text-align: left;\">&nbsp;&nbsp;:&nbsp;&nbsp; &#x24;" + (paidAmount!=null?paidAmount.toString():"0") + "</td></tr>");
                    buffer.append(bufferData.toString());
                }
                buffer.append("</p></TD></TR>");
                buffer.append("</BODY></HTML>");
                if (patronBillForm.getOlePatronDocument() != null) {
                    String email = "";
                    if (patronBillForm.getOlePatronDocument().getEmails() != null) {
                        for (EntityEmailBo emailBo : patronBillForm.getOlePatronDocument().getEmails()) {
                            if (emailBo != null && emailBo.isDefaultValue()) {
                                email = emailBo.getEmailAddress();
                            }
                        }
                    }
                    if ((email == null) || (email != null && email.equalsIgnoreCase(""))) {
                        Map<String, String> emailMap = new HashMap<String, String>();
                        if (patronBillForm.getOlePatronId() != null) {
                            emailMap.put("entityId", patronBillForm.getOlePatronId());
                        }
                        if (patronBillForm.getPatronId() != null) {
                            emailMap.put("entityId", patronBillForm.getPatronId());
                        }
                        List<EntityEmailBo> entityEmailBos = (List<EntityEmailBo>) getBusinessObjectService().findMatching(EntityEmailBo.class, emailMap);
                        if (entityEmailBos != null) {
                            for (EntityEmailBo emailBo : entityEmailBos) {
                                if (emailBo != null && emailBo.isDefaultValue()) {
                                    email = emailBo.getEmailAddress();
                                }
                            }
                        }
                    }
                    LoanProcessor loanProcessor = new LoanProcessor();
                    String fromAddress = loanProcessor.getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                    if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                        fromAddress = OLEConstants.KUALI_MAIL;
                    }
                    if (email != null && !email.equalsIgnoreCase("")) {
                        boolean sendMail = true;
                        if (patronBillForm.getBillWisePayment().equalsIgnoreCase(OLEConstants.OlePatron.DEFAULT)) {
                            if (patronBillForm.getCurrentSessionTransactions().size() == 0) {
                                sendMail = false;
                                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_TRANSACTION_SELECT);
                            }
                        }
                        if (sendMail) {
                            OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                            oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(email), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(buffer.toString()), true);
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Mail send successfully to " + email);
                            }
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.OlePatron.PATRON_SUCCESS_MAIL);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occured during sending mail" + e);
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.PATRON_SUCCESS_MAIL);
        }
        return getUIFModelAndView(patronBillForm);
    }



    @RequestMapping(params = "methodToCall=selectAllBill")
    public ModelAndView selectAllBill(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized acceptPayment Method");
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        KualiDecimal totalAmount=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(PatronBillPayment  patronBillPayment:patronBillForm.getPatronBillPaymentList()){
            /*if (patronBillPayment.getUnPaidBalance().bigDecimalValue().compareTo(OLEConstants.BIGDECIMAL_DEF_VALUE)!=0) {
                patronBillPayment.setSelectBill(true);
            }*/
            patronBillPayment.setSelectBill(true);
            totalAmount=new KualiDecimal(totalAmount.bigDecimalValue().add(patronBillPayment.getUnPaidBalance().bigDecimalValue()));
        }
        for(FeeType feeType:patronBillForm.getFeeTypes()){
            feeType.setActiveItem(false);
        }
        patronBillForm.setPaymentAmount(totalAmount);
        patronBillForm.setBillWisePayment(OLEConstants.BILL_WISE);
        return getUIFModelAndView(patronBillForm);
    }

    @RequestMapping(params = "methodToCall=selectAllItem")
    public ModelAndView selectAllItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized acceptPayment Method");
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        KualiDecimal totalAmount=OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE;
        for(PatronBillPayment  patronBillPayment:patronBillForm.getPatronBillPaymentList()){
            patronBillPayment.setSelectBill(false);
        }
        for(FeeType feeType:patronBillForm.getFeeTypes()){
            /*if (feeType.getBalFeeAmount().bigDecimalValue().compareTo(OLEConstants.BIGDECIMAL_DEF_VALUE) != 0) {
                feeType.setActiveItem(true);
            }*/
            feeType.setActiveItem(true);
            totalAmount=new KualiDecimal(totalAmount.bigDecimalValue().add(feeType.getBalFeeAmount().bigDecimalValue()));
        }
        patronBillForm.setPaymentAmount(totalAmount);
        patronBillForm.setBillWisePayment(OLEConstants.ITEM_WISE);
        return getUIFModelAndView(patronBillForm);
    }

    @RequestMapping(params = "methodToCall=deSelectAll")
    public ModelAndView deSelectAll(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized acceptPayment Method");
        PatronBillHelperService patronBillHelperService = new PatronBillHelperService();
        PatronBillForm patronBillForm = (PatronBillForm) form;
        for(FeeType feeType:patronBillForm.getFeeTypes()){
            feeType.setActiveItem(false);
        }
        for(PatronBillPayment  patronBillPayment:patronBillForm.getPatronBillPaymentList()){
             patronBillPayment.setSelectBill(false);
        }
        patronBillForm.setPaymentAmount(OLEConstants.KUALI_BIGDECIMAL_DEF_VALUE);
        patronBillForm.setBillWisePayment("default");
        return getUIFModelAndView(patronBillForm);
    }

}