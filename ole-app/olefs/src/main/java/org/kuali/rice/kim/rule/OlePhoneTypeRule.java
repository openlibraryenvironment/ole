package org.kuali.rice.kim.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo;
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
public class OlePhoneTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        EntityPhoneTypeBo entityPhoneTypeBo = (EntityPhoneTypeBo) document.getNewMaintainableObject().getDataObject();
        String maintenanceAction = document.getNewMaintainableObject().getMaintenanceAction();
        if(maintenanceAction  != "Edit")
            isValid &= validateName(entityPhoneTypeBo);
        else
            validateForEdit((EntityPhoneTypeBo)document.getOldMaintainableObject().getDataObject(),(EntityPhoneTypeBo)document.getNewMaintainableObject().getDataObject()) ;
        return isValid;
    }

    private boolean validateForEdit(EntityPhoneTypeBo oldPhoneType, EntityPhoneTypeBo newPhoneType)  {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("name", newPhoneType.getName());
        List<EntityPhoneTypeBo> nameInDatabase = (List<EntityPhoneTypeBo>) getBoService().findMatching(EntityPhoneTypeBo.class, criteria);
        if(nameInDatabase.size()>0)  {
            if(!oldPhoneType.getName().equals(nameInDatabase.get(0))  && !newPhoneType.getName().equals((oldPhoneType.getName()))) {
                this.putFieldError(OLEConstants.EntityPhoneTypeBo.PHONE_TYPE_NAME, "error.duplicate.name");
                return false;
            }
        }
        return true;
    }

    private boolean validateName(EntityPhoneTypeBo entityPhoneTypeBo) {
        if (entityPhoneTypeBo.getName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("name", entityPhoneTypeBo.getName());
            List<EntityPhoneTypeBo> nameInDatabase = (List<EntityPhoneTypeBo>) getBoService().findMatching(EntityPhoneTypeBo.class, criteria);
            if ((nameInDatabase.size() > 0)) {
                this.putFieldError(OLEConstants.EntityPhoneTypeBo.PHONE_TYPE_NAME, "error.duplicate.name");
                return false;
            }
        }
        return true;
    }
}
