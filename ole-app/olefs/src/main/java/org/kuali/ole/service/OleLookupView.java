package org.kuali.ole.service;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bala.km
 * Date: 22/6/12
 * Time: 2:42 PM
 * To remove the action column from the look up search result ..
 */
public class OleLookupView extends LookupView {

    /**
     *  To remove the action column from the look up search result
     * @param view
     * @param model
     * @param parent
     */
    @Override
    public void performApplyModel(View view, Object model, Component parent) {
        Class className = this.getDataObjectClassName();
        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        super.performApplyModel(view, model, parent);
        if(className.getName().equalsIgnoreCase(OLEConstants.PATRON)) {
            boolean canCreate =  canCreate(principalId);
            if(!canCreate){
                if (!isSuppressActions() && isShowMaintenanceLinks()) {
                   // ((List<Field>) getResultsGroup().getItems()).remove(getResultsActionsFieldGroup());
                }
            }
        }

        if(className.getName().equalsIgnoreCase(OLEConstants.PATRON)) {
            for(int resultSize=0; resultSize < getResultFields().size(); resultSize++) {
                boolean hasDisplayBillRole =  canDisplayBill(principalId);
                boolean hasCreateBillRole = canCreateBill(principalId);
                if(!hasDisplayBillRole || !hasCreateBillRole) {
                    if(getResultFields().get(resultSize).getId().equalsIgnoreCase(OLEConstants.VIEWBILL) || getResultFields().get(resultSize).getId().equalsIgnoreCase(OLEConstants.CREATEBILL)){
                        ((List<Field>) getResultsGroup().getItems()).remove(getResultFields().get(resultSize));
                    }
                }
            }

        }
    }

    private boolean  canDisplayBill(String principalId){
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE,OLEConstants.CAN_DISPLAY_BILL);
    }

    private boolean  canCreateBill(String principalId){
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE,OLEConstants.CAN_CREATEORUPDATE_BILL);
    }

    private boolean  canCreate(String principalId){
        PermissionService service= KimApiServiceLocator.getPermissionService();
        return  service.hasPermission(principalId, OLEConstants.OlePatron.PATRON_NAMESPACE,OLEConstants.EDIT_PATRON_DOCUMENT);
    }
}
