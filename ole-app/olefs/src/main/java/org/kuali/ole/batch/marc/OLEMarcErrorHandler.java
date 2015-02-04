package org.kuali.ole.batch.marc;

import org.marc4j.ErrorHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMarcErrorHandler extends ErrorHandler {
    private List<Error> errors;
    private Map<String,String> errorMap = new HashMap<String,String>();

    public void addError(String id, String field, String subfield, int severity, String message) {
        if (errors == null) {
            errors = new LinkedList();
        }

        errors.add(new OLEMarcError(this, id, field, subfield, severity, message));
    }

    public boolean hasErrors() {
        return (errors != null && errors.size() > 0);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public Map<String,String>  getErrorMap(){
        return errorMap;
    }

}