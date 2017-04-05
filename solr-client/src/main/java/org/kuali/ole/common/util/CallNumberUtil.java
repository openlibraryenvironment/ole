package org.kuali.ole.common.util;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.common.util.callnumber.CallNumberFactory;

/**
 * Created by pvsubrah on 1/4/16.
 */
public class CallNumberUtil {
    public String buildSortableCallNumber(String callNumber, String codeValue) {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                shelvingOrder = callNumberObj.getShelfKey();
            }
        }
        return shelvingOrder;
    }
}
