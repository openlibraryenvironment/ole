package org.kuali.ole.docstore.common.exception;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreValidationException extends DocstoreException {


    public DocstoreValidationException() {
        super();
    }

    public DocstoreValidationException(String message) {
        super(message);
    }

    public DocstoreValidationException(Exception e) {
        super(e);
        setErrorMessage(e.getMessage());
    }

    public DocstoreValidationException(String erroeCode, String errorMessage, TreeMap<String, String> errorParams) {
        super(erroeCode, errorMessage, errorParams);
    }

    public DocstoreValidationException(String erroeCode, String errorMessage) {
        super(erroeCode, errorMessage);
    }
}
