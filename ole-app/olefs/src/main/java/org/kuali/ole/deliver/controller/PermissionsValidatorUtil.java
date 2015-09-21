package org.kuali.ole.deliver.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 6/14/15.
 */
public class PermissionsValidatorUtil {

    private PermissionService permissionService;
    private PersonService personService;


    public void handlePermissions(OleLoanForm oleLoanForm) {
        List<String> newErrorsAndPermission = oleLoanForm.getNewErrorsAndPermission();
        if (null != newErrorsAndPermission) {
            Boolean finalOverridePermission = true;
            for (Iterator<String> iterator = newErrorsAndPermission.iterator(); iterator.hasNext(); ) {
                String permissionName = iterator.next();
                String principalId = null;
                String newPrincipalName = oleLoanForm.getOverridingPrincipalName();
                if (StringUtils.isNotBlank(newPrincipalName)) {
                    Person supervisorForOverride = getPersonService().getPersonByPrincipalName(newPrincipalName);
                    if (null != supervisorForOverride) {
                        principalId = supervisorForOverride.getPrincipalId();
                    }
                }

                if (null == principalId) {
                    principalId = GlobalVariables.getUserSession().getPrincipalId();
                }

                boolean hasPermission = getPermissionService().hasPermission(principalId, OLEConstants.DLVR_NMSPC, permissionName);
                finalOverridePermission = finalOverridePermission && hasPermission;
                if (!finalOverridePermission) {
                    oleLoanForm.setOverrideFlag(true);
                    oleLoanForm.setOverrideLoginMessage("The current operator doesn't have the permissions to loan. Please ask a supervisor!");
                    break;
                }
                if (finalOverridePermission) {
                    oleLoanForm.setOverrideFlag(false);
                    oleLoanForm.setErrorMessage(null);
                    oleLoanForm.setNewErrorsAndPermission(null);
                    oleLoanForm.setOverridingPrincipalName("");
                    oleLoanForm.setNewPrincipalId(null);
                }
            }
        }
    }


    public boolean hasValidOverridePermissions(CircForm circForm) {
        Boolean finalOverridePermission = false;
        List<String> newErrorsAndPermission = circForm.getErrorMessage().getPermissions();
        if (null != newErrorsAndPermission) {
            for (Iterator<String> iterator = newErrorsAndPermission.iterator(); iterator.hasNext(); ) {
                String permissionName = iterator.next();
                String principalId = null;
                String loggedInPrincipalId = GlobalVariables.getUserSession().getPrincipalId();
                if (getPermissionService().hasPermission(loggedInPrincipalId, OLEConstants.DLVR_NMSPC, permissionName)) {
                    return true;
                } else {
                    if (StringUtils.isNotBlank(circForm.getOverridingPrincipalName())) {
                        String newPrincipalName = circForm.getOverridingPrincipalName();
                        Person supervisorForOverride = getPersonService().getPersonByPrincipalName(newPrincipalName);
                        if (null != supervisorForOverride) {
                            principalId = supervisorForOverride.getPrincipalId();
                        }
                        if (principalId != null) {
                            boolean hasPermission = getPermissionService().hasPermission(principalId, OLEConstants.DLVR_NMSPC, permissionName);
                            finalOverridePermission = hasPermission;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return finalOverridePermission;
    }

    private PersonService getPersonService() {
        if (null == personService) {
            personService = SpringContext.getBean(PersonService.class);
        }
        return personService;
    }

    private PermissionService getPermissionService() {
        if (null == permissionService) {
            permissionService = KimApiServiceLocator.getPermissionService();
        }
        return permissionService;
    }
}
