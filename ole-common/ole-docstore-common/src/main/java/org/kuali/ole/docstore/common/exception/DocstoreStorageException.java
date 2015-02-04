package org.kuali.ole.docstore.common.exception;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreStorageException extends DocstoreException {

    public DocstoreStorageException() {
        super();
    }

    public DocstoreStorageException(String message) {
        super(message);
    }

    public DocstoreStorageException(Exception e) {
        super(e);
        setErrorMessage(e.getMessage());
    }

    public DocstoreStorageException(String erroeCode, String errorMessage, TreeMap<String, String> errorParams) {
        super(erroeCode, errorMessage, errorParams);
    }

    public DocstoreStorageException(String erroeCode, String errorMessage) {
        super(erroeCode, errorMessage);
    }


}
