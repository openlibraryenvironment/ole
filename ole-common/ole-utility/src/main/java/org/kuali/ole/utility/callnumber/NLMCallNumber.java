package org.kuali.ole.utility.callnumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solrmarc.callnum.LCCallNumber;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class NLMCallNumber extends LCCallNumber {
    private static final Logger Log = LoggerFactory.getLogger(NLMCallNumber.class);
    private static NLMCallNumber ourInstance = null;

    public static NLMCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new NLMCallNumber();
        }
        return ourInstance;
    }

    @Override
    public void parse(String call) {
        try {
            super.parse(call);
        } catch (Exception e) {
            Log.error("NLM Call Number Exception" + e);
        }
    }
}
