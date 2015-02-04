package org.kuali.ole.docstore.common.exception;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private TreeMap<String, String> errorParams;

    public DocstoreException() {
        super();
    }

    public DocstoreException(String message) {
        super(message);
        setErrorMessage(message);
    }

    public DocstoreException(Exception e) {
        super(e);
        setErrorMessage(e.getMessage());
    }

    public DocstoreException(String errorCode, String errorMessage, TreeMap<String, String> errorParams) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorParams = errorParams;
    }

    public DocstoreException(String errorCode, String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TreeMap<String, String> getErrorParams() {
        return errorParams;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        stb.append(super.toString());
        stb.append("\n");
        stb.append("Error Code = ");
        stb.append(errorCode);
        stb.append("\n");
        stb.append("Error Message = ");
        stb.append(errorMessage);
        stb.append("\n");
        if (errorParams != null) {
            stb.append("ErrorParams = ");
            stb.append("\n");
            Set keySet = errorParams.keySet();
            Iterator<String> iterator = keySet.iterator();
            String key = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                stb.append("\t");
                stb.append(key);
                stb.append(" = ");
                stb.append(errorParams.get(key));
                stb.append("\n");
            }
        }
        return stb.toString();
    }

    public void addErrorParams(String key, String value) {
        if (errorParams == null) {
            errorParams = new TreeMap<>();
            errorParams.put(key, value);
        } else {
            errorParams.put(key, value);
        }

    }
}
