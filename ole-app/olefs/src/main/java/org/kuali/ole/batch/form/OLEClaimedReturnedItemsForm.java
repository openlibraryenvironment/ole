package org.kuali.ole.batch.form;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEClaimedReturnedItemResult;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchulakshmig on 3/16/16.
 */
public class OLEClaimedReturnedItemsForm extends UifFormBase {

    private String circulationDeskId;
    private boolean billForItem;
    private List<OLEClaimedReturnedItemResult> claimedReturnedItemResults = new ArrayList<>();
    private boolean claimsReturnPermission;

    public String getCirculationDeskId() {
        return circulationDeskId;
    }

    public void setCirculationDeskId(String circulationDeskId) {
        this.circulationDeskId = circulationDeskId;
    }

    public boolean isBillForItem() {
        return billForItem;
    }

    public void setBillForItem(boolean billForItem) {
        this.billForItem = billForItem;
    }

    public List<OLEClaimedReturnedItemResult> getClaimedReturnedItemResults() {
        return claimedReturnedItemResults;
    }

    public void setClaimedReturnedItemResults(List<OLEClaimedReturnedItemResult> claimedReturnedItemResults) {
        this.claimedReturnedItemResults = claimedReturnedItemResults;
    }

    public boolean hasClaimsReturnPermission() {
        Permission perm = KimApiServiceLocator.getPermissionService().findPermByNamespaceCodeAndName(OLEConstants.DLVR_NMSPC, OLEConstants.View_Claims_Return_Screen);
        if (perm != null) {
            claimsReturnPermission = KimApiServiceLocator.getPermissionService().hasPermission(GlobalVariables.getUserSession().getPerson().getPrincipalId(), OLEConstants.DLVR_NMSPC, OLEConstants.View_Claims_Return_Screen);
            return claimsReturnPermission;
        }
        else {
            return Boolean.TRUE;
        }
    }
}
