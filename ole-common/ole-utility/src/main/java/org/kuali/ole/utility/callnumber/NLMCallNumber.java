package org.kuali.ole.utility.callnumber;

import org.solrmarc.callnum.LCCallNumber;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class NLMCallNumber extends LCCallNumber {
    private static NLMCallNumber ourInstance = null;

    public static NLMCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new NLMCallNumber();
        }
        return ourInstance;
    }
}
