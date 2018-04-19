package org.kuali.ole.deliver.notice.valuesFinder;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 7/7/15.
 */
public class OleNoticeTypeKeyValuesFinder  extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue(OLEConstants.RECALL_NOTICE,"Recall Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ONHOLD_NOTICE,"OnHold Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ONHOLD_EXPIRATION_NOTICE,"OnHold Expiration Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.REQUEST_EXPIRATION_NOTICE,"Request Expiration Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ONHOLD_COURTESY_NOTICE,"OnHold Courtesy Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.COURTESY_NOTICE,"Courtesy Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OVERDUE_NOTICE,"Overdue Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.MISSING_PIECE_NOTICE,"Missing Piece Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_LOST,"Lost Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CHECKOUT_RECEIPT_NOTICE,OLEConstants.CHECKOUT_RECEIPT_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CANCELLATION_NOTICE,OLEConstants.CANCELLATION_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_RETURNED_NOTICE,OLEConstants.CLAIMS_RETURNED_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE,OLEConstants.CLAIMS_RETURNED_FOUND_NO_FEES_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE,OLEConstants.CLAIMS_RETURNED_FOUND_FINES_OWED_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NOTICE,OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE,OLEConstants.CLAIMS_RETURNED_NOT_FOUND_NO_FEES_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CLAIMS_RETURNED_NOT_FOUND_FINES_OWED_NOTICE_TITLE,"Claims Returned Not Found Fines Owed Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.LOST_ITEM_PROCESSING_FEE_NOTICE,OLEConstants.LOST_ITEM_PROCESSING_FEE_NOTICE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OVERDUE_FINE_NOTICE,OLEConstants.OVERDUE_FINE_NOTICE));
        return keyValues;
    }
}
