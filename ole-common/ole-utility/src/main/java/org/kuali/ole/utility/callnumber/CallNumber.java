package org.kuali.ole.utility.callnumber;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 20/2/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CallNumber {
    public String getSortableKey(String callNumber);

    public boolean isValid(String callNumber);
}
