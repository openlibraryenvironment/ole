package org.kuali.rice.kim.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;
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
public class OleEmailTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        EntityEmailTypeBo entityEmailTypeBo = (EntityEmailTypeBo) document.getNewMaintainableObject().getDataObject();
        String maintenanceAction = document.getNewMaintainableObject().getMaintenanceAction();
        if(maintenanceAction  != "Edit")
            isValid &= validateName(entityEmailTypeBo);
        else
            validateForEdit((EntityEmailTypeBo)document.getOldMaintainableObject().getDataObject(),(EntityEmailTypeBo)document.getNewMaintainableObject().getDataObject()) ;
        return isValid;
    }

    private boolean validateForEdit(EntityEmailTypeBo oldEmailType, EntityEmailTypeBo newEmailType)  {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("name", newEmailType.getName());
        List<EntityEmailTypeBo> nameInDatabase = (List<EntityEmailTypeBo>) getBoService().findMatching(EntityEmailTypeBo.class, criteria);
        if(nameInDatabase.size()>0)  {
            if(!oldEmailType.getName().equals(nameInDatabase.get(0))  && !newEmailType.getName().equals((oldEmailType.getName()))) {
                this.putFieldError(OLEConstants.EntityEmailTypeBo.EMAIL_TYPE_NAME, "error.duplicate.name");
                return false;
            }
        }
        return true;
    }

    private boolean validateName(EntityEmailTypeBo entityEmailTypeBo) {
        if (entityEmailTypeBo.getName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("name", entityEmailTypeBo.getName());
            List<EntityEmailTypeBo> nameInDatabase = (List<EntityEmailTypeBo>) getBoService().findMatching(EntityEmailTypeBo.class, criteria);
            if ((nameInDatabase.size() > 0)) {
                this.putFieldError(OLEConstants.EntityEmailTypeBo.EMAIL_TYPE_NAME, "error.duplicate.name");
                return false;
            }
        }
        return true;
    }
}
