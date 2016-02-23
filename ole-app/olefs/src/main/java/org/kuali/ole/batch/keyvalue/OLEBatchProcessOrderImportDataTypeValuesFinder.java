package org.kuali.ole.batch.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 10/17/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessOrderImportDataTypeValuesFinder extends KeyValuesBase {
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("", ""));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.CHART_CODE, "Chart Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE, "Item Chart Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ORG_CODE, "Org Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED,"Receiving Required"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER, "Contract Manager"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER, "Assign To User"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR,"Use Tax Indicator"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ORDER_TYPE, "Order Type"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.FUNDING_SOURCE, "Funding Source"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ,"Pay Req Positive Approval Req"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR,"Purchase Order Confirmation Indicator"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE, "Delivery Campus Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.BUILDING_CODE, "Building Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_CHOICE, "Vendor Choice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR,"Route To Requestor"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION, "Method Of PO Transmission"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.COST_SOURCE, "Cost Source"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.PERCENT, "Percent"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION, "Default Location"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.LIST_PRICE, "List Price"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_NUMBER, "Vendor Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME,"Vendor Alias Name"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR, "Acquisition Unit's Vendor account / Vendor Info Customer #"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.QUANTITY, "Quantity"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS, "No Of Parts"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER, "Vendor Reference Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE, OLEConstants.DONOR_CD));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.REQUESTOR_NAME, "Requestor Name"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ITEM_STATUS, "Item Status"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.DISCOUNT, "Discount"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE, "Discount Type"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, "Account Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.OBJECT_CODE, "Object Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE, "Vendor Profile Code"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.MISC_NOTE, "Miscellaneous/Other Note"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.RCPT_NOTE, "Receipt Note"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.RQST_NOTE, "Requestor Note"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.SELECTOR_NOTE, "Selector Note"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.SPL_PROCESS_NOTE, "Special Processing Instruction Note"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VNDR_INSTR_NOTE, "Vendor Instructions Note"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.CAPTION,"Caption"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.VOLUME_NUMBER,"Volume Number"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.FORMAT_TYP_NM,"Format"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER,OLEConstants.OLEBatchProcess.BUILDING_ROOM_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP,"Recurring Payment Type"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT,"Recurring Payment Begin Date"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT,"Recurring Payment End Date"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OLEEResourceRecord.FUND_CODE,"Fund Code"));
        Collections.sort(keyValues, new Comparator<KeyValue>() {
            public int compare(KeyValue keyValue1, KeyValue keyValue2) {
                return keyValue1.getValue().compareTo(keyValue2.getValue());

            }
        });
        return keyValues;

    }
}
