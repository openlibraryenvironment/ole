package org.kuali.ole.deliver.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 6/3/15.
 */
public class ErrorMessage {
    private String errorCode;
    private String errorMessage = null;
    private List<String> permissions = null;

    public void setErrorMessage(String errorMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != this.errorMessage && !StringUtils.isBlank(this.errorMessage)) {
            stringBuilder.append(this
                    .errorMessage).append
                    (OLEConstants.BREAK);
        }
        stringBuilder.append(errorMessage);
        this.errorMessage = stringBuilder.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void addOverridePermissions(String permissionName) {
        if(null == permissions){
            permissions = new ArrayList<>();
        }
        if(!permissions.contains(permissionName)){
            permissions.add(permissionName);
        }
    }

    public void clearPermissions(){
        this.permissions = null;
    }

    public void clearErrorMessage(){
        this.errorMessage = null;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
