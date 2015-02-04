package org.kuali.ole.docstore;

/**
 * Base class for all exceptions thrown by OLE DocStore.
 *
 * @author tirumalesh.b
 * @version %I%, %G%
 *          Date: 28/8/12 Time: 11:58 AM
 */
public class OleDocStoreException
        extends Exception {

    public OleDocStoreException() {
        super();
    }

    public OleDocStoreException(String message) {
        super(message);
    }

    public OleDocStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public OleDocStoreException(Throwable cause) {
        super(cause);
    }
}
