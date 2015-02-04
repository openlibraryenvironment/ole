package org.kuali.ole.docstore.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 10/15/13
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreValidationError {

    String errorId;
    List<String> errorParams;

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public List<String> getErrorParams() {
        return errorParams;
    }

    public void setErrorParams(List<String> errorParams) {
        this.errorParams = errorParams;
    }

    public void addParams(String errorParam){
        if(errorParams!=null){
            errorParams.add(errorParam);
        }else{
            errorParams = new ArrayList<>();
            errorParams.add(errorParam);
        }

    }

}
