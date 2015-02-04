package org.kuali.ole.docstore.common.exception;

import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 2/19/14
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreDeserializeException extends DocstoreException {

    public DocstoreDeserializeException() {
        super();
    }

    public DocstoreDeserializeException(String message) {
        super(message);
    }

    public DocstoreDeserializeException(Exception e) {
        super(e);
        setErrorMessage(e.getMessage());
    }

    public DocstoreDeserializeException(String errorCode, String errorMessage, TreeMap<String, String> errorParams) {
        super(errorCode, errorMessage, errorParams);
    }

    public DocstoreDeserializeException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
