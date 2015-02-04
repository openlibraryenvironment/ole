package org.kuali.ole.docstore.common.exception;

import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreIndexException extends DocstoreException {

    public DocstoreIndexException() {
        super();
    }

    public DocstoreIndexException(String message) {
        super(message);
    }

    public DocstoreIndexException(Exception e) {
        super(e);
        setErrorMessage(e.getMessage());
    }

    public DocstoreIndexException(String erroeCode, String errorMessage, TreeMap<String, String> errorParams) {
        super(erroeCode, errorMessage, errorParams);
    }

    public DocstoreIndexException(String erroeCode, String errorMessage) {
        super(erroeCode, errorMessage);
    }
}
