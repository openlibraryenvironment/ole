package org.kuali.ole.deliver.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 3/24/15.
 */
public class HandleErrorsAndOverridePermissions {

    private static HandleErrorsAndOverridePermissions handleErrorsAndOverridePermissions;

    private HandleErrorsAndOverridePermissions() {

    }

    public static HandleErrorsAndOverridePermissions getInstance() {
        if (null== handleErrorsAndOverridePermissions) {
            handleErrorsAndOverridePermissions = new HandleErrorsAndOverridePermissions();
        }
        return handleErrorsAndOverridePermissions;
    }


    public void handle(OleLoanDocument oleLoanDocument, String operatorId, org.kuali.ole.docstore.common.document.content.instance.Item item) {
        HashMap<String, String> errorsAndPermission;
        StringBuffer failures = new StringBuffer();

        errorsAndPermission = oleLoanDocument.getErrorsAndPermission();
        PermissionService service = KimApiServiceLocator.getPermissionService();

        int i = 1;
        if (errorsAndPermission != null) {
            Set<String> errorMessage = errorsAndPermission.keySet();
            if (errorMessage != null && errorMessage.size() > 0) {
                for (String errMsg : errorMessage) {
                    if (StringUtils.isNotEmpty(errMsg)) {
                        oleLoanDocument.getErrorsAndPermission().putAll(errorsAndPermission);
                        if (oleLoanDocument.isRenewalItemFlag()) {
                            String permission = errorsAndPermission.get(errMsg);
                            if (operatorId == null) {
                                if (GlobalVariables.getUserSession() != null) {
                                    operatorId = GlobalVariables.getUserSession().getPrincipalId();
                                }
                            }
                            boolean hasRenewPermission = service.hasPermission(operatorId, OLEConstants.DLVR_NMSPC, permission);
                            if (!hasRenewPermission) {
                                oleLoanDocument.setRenewPermission(false);
                            } else {
                                oleLoanDocument.setRenewPermission(true);
                            }
                            failures.append(errMsg + OLEConstants.OR);
                        } else {
                            failures.append(i++ + ". " + errMsg + OLEConstants.BREAK);
                        }
                    }
                }
            }
        }

        if (oleLoanDocument.getErrorMessage() != null && StringUtils.isNotBlank(oleLoanDocument.getErrorMessage())) {
            StringTokenizer stringTokenizer = new StringTokenizer(oleLoanDocument.getErrorMessage(), "\n");

            while (stringTokenizer.hasMoreTokens()) {
                String errMsg = stringTokenizer.nextToken();
                if (errMsg.equalsIgnoreCase("Inform the current borrower that this item has some missing pieces before proceeding with checkout.")) {
                    if (item != null && item.getMissingPiecesCount() != null) {
                        errMsg = errMsg.replace("some", item.getMissingPiecesCount());
                    }
                }
                if (oleLoanDocument.isRenewalItemFlag()) {
                    failures.append(errMsg + OLEConstants.OR);
                } else {
                    if (errMsg.equalsIgnoreCase("Item status is Lost")) {
                        //oleLoanDocument.setStatusLost(true);
                        failures.append(i++ + "." + errMsg + "-" + OLEConstants.ITEMSTATUSLOST + OLEConstants.BREAK);
                    } else {
                        failures.append(i++ + ". " + errMsg + OLEConstants.BREAK);
                    }
                }
            }
        }
    }
}
