package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePaymentStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePaymentStatusKeyValues extends KeyValuesBase {

    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OlePaymentStatus> olePaymentStatus = KRADServiceLocator.getBusinessObjectService().findAll(OlePaymentStatus.class);
        for (OlePaymentStatus paymentStatus : olePaymentStatus) {
            if (paymentStatus.getPaymentStatusName().equals(OLEConstants.OlePatron.BILL_PAYMENT_STATUS_OUTSTANDING)) {
                keyValues.add(new ConcreteKeyValue(paymentStatus.getPaymentStatusId(), paymentStatus.getPaymentStatusName()));
                break;
            }
        }
        for (OlePaymentStatus paymentStatus : olePaymentStatus) {
            if (!paymentStatus.getPaymentStatusName().equals(OLEConstants.OlePatron.BILL_PAYMENT_STATUS_OUTSTANDING)) {
                keyValues.add(new ConcreteKeyValue(paymentStatus.getPaymentStatusId(), paymentStatus.getPaymentStatusName()));
            }
        }
        return keyValues;
    }
}
