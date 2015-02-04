package org.kuali.ole.utility.callnumber;

/**
 * Created with IntelliJ IDEA.
 * User: Pranitha J
 * Date: 7/3/13
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OtherCallNumber extends AbstractCallNumber implements CallNumber {

    private static OtherCallNumber ourInstance = null;

    public static OtherCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new OtherCallNumber();
        }
        return ourInstance;
    }

    public String getSortableKey(String callNumber) {
        String normalizedCallNumber = CallNumUtils.getSuDocShelfKey(callNumber);
        return normalizedCallNumber;
    }

    public boolean isValid(String callNumber) {
        return false;
    }
}
