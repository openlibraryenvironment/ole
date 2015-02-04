package org.kuali.ole.utility.callnumber;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CallNumberFactory {
    private static CallNumberFactory callNumberFactory = new CallNumberFactory();
    private Map<String, CallNumber> callNumberMap = new HashMap<String, CallNumber>();

    public static CallNumberFactory getInstance() {
        return callNumberFactory;
    }

    private CallNumberFactory() {
        initCallNumberMap();
    }

    private void initCallNumberMap() {
        String key = CallNumberType.LCC.getCode();
        callNumberMap.put(key, LCCallNumber.getInstance());

        key = CallNumberType.DDC.getCode();
        callNumberMap.put(key, DDCallNumber.getInstance());

        key = CallNumberType.NLM.getCode();
        callNumberMap.put(key, NLMCallNumber.getInstance());

        key = CallNumberType.SuDoc.getCode();
        callNumberMap.put(key, SuDocCallNumber.getInstance());

        key = CallNumberType.FOUR.getCode();
        callNumberMap.put(key, OtherCallNumber.getInstance());

        key = CallNumberType.FIVE.getCode();
        callNumberMap.put(key, OtherCallNumber.getInstance());

        key = CallNumberType.SIX.getCode();
        callNumberMap.put(key, OtherCallNumber.getInstance());

        key = CallNumberType.SEVEN.getCode();
        callNumberMap.put(key, OtherCallNumber.getInstance());

        key = CallNumberType.EIGHT.getCode();
        callNumberMap.put(key, OtherCallNumber.getInstance());

    }


    public CallNumber getCallNumber(String callNumberType) {
        if (callNumberMap.get(callNumberType) == null) {
            return OtherCallNumber.getInstance();
        }
        return callNumberMap.get(callNumberType);
    }
}
