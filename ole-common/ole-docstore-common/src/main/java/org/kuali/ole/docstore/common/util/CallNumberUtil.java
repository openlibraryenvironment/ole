package org.kuali.ole.docstore.common.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.utility.callnumber.*;

/**
 * Created by jayabharathreddy on 1/14/16.
 */
public class CallNumberUtil {

    public static String getShelfKey(String callNumber, String codeValue) {
        String Shelfkey = null;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = null;
            if (codeValue.equalsIgnoreCase(CallNumberType.LCC.getCode())) {
                callNumberObj = new LcCallNumber();
            } else if (codeValue.equalsIgnoreCase(CallNumberType.DDC.getCode())) {
                callNumberObj = new DDCallNumber();
            } else if (codeValue.equalsIgnoreCase(CallNumberType.NLM.getCode())) {
                callNumberObj = new NLMCallNumber();
            } else {
                callNumberObj = new LcCallNumber();
            }

            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                Shelfkey = callNumberObj.getShelfKey();
            }
        }
        return Shelfkey;
    }
}
