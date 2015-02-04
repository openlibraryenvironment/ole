package org.kuali.ole.utility.callnumber;


/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class DDCallNumber extends AbstractCallNumber implements CallNumber {
    private static DDCallNumber ourInstance = null;

    public static DDCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new DDCallNumber();
        }
        return ourInstance;
    }

    public String getSortableKey(String callNumber) {
        String normalizedCallNumber = CallNumUtils.getDeweyShelfKey(callNumber);
        return normalizedCallNumber;
    }

    public boolean isValid(String callNumber) {
        boolean isValid = CallNumUtils.isValidDewey(callNumber);
        if (!isValid) {
            isValid = CallNumUtils.isValidDeweyWithCutter(callNumber);
        }
        return isValid;
    }
}
