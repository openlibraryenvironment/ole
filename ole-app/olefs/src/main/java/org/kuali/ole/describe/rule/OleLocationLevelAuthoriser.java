package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.maintenance.MaintenanceViewAuthorizerBase;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * OleLocationLevelAuthoriser  class authorises permission for Location Level Maintenance Document
 */
public class OleLocationLevelAuthoriser extends MaintenanceViewAuthorizerBase {

    /**
     * This method  is used to check whether that particular field is editable or not.
     *
     * @param view
     * @param model
     * @param field
     * @param propertyName
     * @param user
     * @return boolean
     */
    @Override
    public boolean canEditField(View view, ViewModel model, Field field, String propertyName, Person user) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        // Checking whether the user has permission
        boolean canEdit = service.hasPermission(user.getPrincipalId(), KRADConstants.KRAD_NAMESPACE, OLEConstants.OleLocationLevel.EDIT_LOCATION_LEVEL_PERM);

        //for disabling the code and parent level id

        if (!field.getComponentSecurity().isEditAuthz() && field.getFieldLabel() != null && field.getFieldLabel().getLabelText() != null) {
            String fieldName = field.getFieldLabel().getLabelText();
            if (fieldName.equalsIgnoreCase(OLEConstants.OleLocationLevel.CODE) || fieldName.equalsIgnoreCase(OLEConstants.OleLocationLevel.PARENT_ID))
                return false;
        } else if (field.getComponentSecurity().isEditAuthz() && canEdit) {
            return true;
        } else {
            return false;
        }
        return true;
    }

}


