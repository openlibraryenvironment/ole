package org.kuali.ole.utility.callnumber;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuDocCallNumber extends AbstractCallNumber implements CallNumber {
    private static SuDocCallNumber ourInstance = null;

    public static SuDocCallNumber getInstance() {
        if (null == ourInstance) {
            ourInstance = new SuDocCallNumber();
        }
        return ourInstance;
    }


    public String getSortableKey(String callNumber) {
        String normalizedCallNumber = CallNumUtils.getSuDocShelfKey(callNumber);
        return normalizedCallNumber;
    }

    public boolean isValid(String callNumber) {
        //TODO:Need to compute
        return true;
    }

}
