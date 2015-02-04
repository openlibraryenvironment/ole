package org.kuali.ole.docstore.util;

/**
 * Created with IntelliJ IDEA.
 * User: ND6967
 * Date: 2/5/13
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemExistsException extends Exception {
    public ItemExistsException() {
        super();
    }

    public ItemExistsException(String message) {
        super(message);
    }

    public ItemExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemExistsException(Throwable cause) {
        super(cause);
    }
}
