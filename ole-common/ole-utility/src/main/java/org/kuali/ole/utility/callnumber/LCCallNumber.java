package org.kuali.ole.utility.callnumber;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class LCCallNumber extends AbstractCallNumber implements CallNumber {

    private static LCCallNumber ourInstance = null;

    public static LCCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new LCCallNumber();
        }
        return ourInstance;
    }

    public String getSortableKey(String callNumber) {
        String normalizedCallNumber = CallNumUtils.getLCShelfkey(callNumber, null);
        return normalizedCallNumber;
    }

    public boolean isValid(String callNumber) {
        boolean isValid = CallNumUtils.isValidLC(callNumber);
        return isValid;
    }
}
