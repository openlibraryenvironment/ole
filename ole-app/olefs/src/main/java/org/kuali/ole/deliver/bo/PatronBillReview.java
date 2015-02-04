package org.kuali.ole.deliver.bo;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.form.PatronBillReviewForm;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/6/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatronBillReview {

    public List<PatronBillReviewForm> getPatronBill() {
        List<PatronBillReviewForm> patronBillPayments = new ArrayList<PatronBillReviewForm>();
        Map reviewMap = new HashMap();
        reviewMap.put("reviewed", 'N');
        List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, reviewMap);
        for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
            String itemBarcode = "";
            PatronBillReviewForm patronBillReviewForm = new PatronBillReviewForm();
            patronBillReviewForm.setBillNumber(patronBillPayment.getBillNumber());
            patronBillReviewForm.setBillDate(patronBillPayment.getBillDate());
            patronBillReviewForm.setPatronId(patronBillPayment.getPatronId());
            /*patronBillReviewForm.setFirstName(patronBillPayment.getFirstName());
            patronBillReviewForm.setLastName(patronBillPayment.getLastName());*/
            patronBillReviewForm.setTotalAmount(patronBillPayment.getTotalAmount().bigDecimalValue());
            Map itemMap = new HashMap();
            itemMap.put(OLEConstants.OlePatron.BILL_PAYMENT_ID, patronBillPayment.getBillNumber());
            List<FeeType> feeTypes = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, itemMap);
            if (feeTypes.size() > 0) {
                for (int i = 0; i < feeTypes.size(); i++) {
                    itemBarcode += feeTypes.get(i).getItemBarcode();
                    itemBarcode += OLEConstants.OlePatron.ITEMBARCODE_SEPARATOR;
                }
                itemBarcode = itemBarcode.substring(0, (itemBarcode.lastIndexOf(OLEConstants.OlePatron.ITEMBARCODE_SEPARATOR)));
            }
            patronBillReviewForm.setItemBarcode(itemBarcode);
            patronBillPayments.add(patronBillReviewForm);
        }
        return patronBillPayments;
    }

    public List<PatronBillReviewForm> getPatronReviewedBill() {
        List<PatronBillReviewForm> patronBillPayments = new ArrayList<PatronBillReviewForm>();
        Map reviewMap = new HashMap();
        reviewMap.put("reviewed", 'Y');
        List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, reviewMap);
        for (PatronBillPayment patronBillPayment : patronBillPaymentList) {
            String itemBarcode = "";
            PatronBillReviewForm patronBillReviewForm = new PatronBillReviewForm();
            patronBillReviewForm.setBillNumber(patronBillPayment.getBillNumber());
            patronBillReviewForm.setBillDate(patronBillPayment.getBillDate());
            patronBillReviewForm.setPatronId(patronBillPayment.getPatronId());
           /* patronBillReviewForm.setFirstName(patronBillPayment.getFirstName());
            patronBillReviewForm.setLastName(patronBillPayment.getLastName());*/
            patronBillReviewForm.setTotalAmount(patronBillPayment.getTotalAmount().bigDecimalValue());
            Map itemMap = new HashMap();
            itemMap.put(OLEConstants.OlePatron.BILL_PAYMENT_ID, patronBillPayment.getBillNumber());
            List<FeeType> feeTypes = (List<FeeType>) KRADServiceLocator.getBusinessObjectService().findMatching(FeeType.class, itemMap);
            if (feeTypes.size() > 0) {
                for (int i = 0; i < feeTypes.size(); i++) {
                    itemBarcode += feeTypes.get(i).getItemBarcode();
                    itemBarcode += OLEConstants.OlePatron.ITEMBARCODE_SEPARATOR;
                }
                itemBarcode = itemBarcode.substring(0, (itemBarcode.lastIndexOf(OLEConstants.OlePatron.ITEMBARCODE_SEPARATOR)));
            }
            patronBillReviewForm.setItemBarcode(itemBarcode);
            patronBillPayments.add(patronBillReviewForm);
        }
        return patronBillPayments;
    }
}
