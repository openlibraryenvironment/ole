package org.kuali.rice.kim.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/29/12
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleAddressTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        EntityAddressTypeBo entityAddressTypeBo = (EntityAddressTypeBo) document.getNewMaintainableObject().getDataObject();
        String maintenanceAction = document.getNewMaintainableObject().getMaintenanceAction();
        if(maintenanceAction  != "Edit")
            isValid &= validateName(entityAddressTypeBo);
        else
            validateForEdit((EntityAddressTypeBo)document.getOldMaintainableObject().getDataObject(),(EntityAddressTypeBo)document.getNewMaintainableObject().getDataObject()) ;
        return isValid;
    }

    private boolean validateForEdit(EntityAddressTypeBo oldAddressType, EntityAddressTypeBo newAddressType)  {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("name", newAddressType.getName());
        List<EntityAddressTypeBo> nameInDatabase = (List<EntityAddressTypeBo>) getBoService().findMatching(EntityAddressTypeBo.class, criteria);
        if(nameInDatabase.size()>0)  {
           if(!oldAddressType.getName().equals(nameInDatabase.get(0))  && !newAddressType.getName().equals((oldAddressType.getName()))) {
            this.putFieldError(OLEConstants.EntityAddressTypeBo.ADDRESS_TYPE_NAME, "error.duplicate.name");
            return false;
           }
        }
        return true;
    }

    private boolean validateName(EntityAddressTypeBo entityAddressTypeBo) {
        if (entityAddressTypeBo.getName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("name", entityAddressTypeBo.getName());
            List<EntityAddressTypeBo> nameInDatabase = (List<EntityAddressTypeBo>) getBoService().findMatching(EntityAddressTypeBo.class, criteria);
            if ((nameInDatabase.size() > 0)) {
                this.putFieldError(OLEConstants.EntityAddressTypeBo.ADDRESS_TYPE_NAME, "error.duplicate.name");
                return false;
            }
        }
        return true;
    }
}
