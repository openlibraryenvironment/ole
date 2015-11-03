package org.kuali.ole.select.maintenance;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.sql.Date;
import java.util.Map;

/**
 * Created by maheswarang on 11/2/15.
 */
public class OleAccessActivationConfigurationMaintenanceImpl  extends MaintainableImpl {

    /**
     * This method will set the description when copy is selected
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterCopy(document, requestParameters);

        OLEAccessActivationConfiguration oleAccessActivationConfiguration = (OLEAccessActivationConfiguration) document.getNewMaintainableObject().getDataObject();
if(StringUtils.isNotEmpty(oleAccessActivationConfiguration.getMailId())){
    oleAccessActivationConfiguration.setNotificationSelector("mail");
}else if(oleAccessActivationConfiguration.getRecipientRoleId()!=null){
    oleAccessActivationConfiguration.setNotificationSelector("Role");
}else if(oleAccessActivationConfiguration.getRecipientUserId()!=null){
    oleAccessActivationConfiguration.setNotificationSelector("Person");
}

    }

    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);

        OLEAccessActivationConfiguration oleAccessActivationConfiguration = (OLEAccessActivationConfiguration) document.getNewMaintainableObject().getDataObject();
        if(StringUtils.isNotEmpty(oleAccessActivationConfiguration.getMailId())){
            oleAccessActivationConfiguration.setNotificationSelector("mail");
        }else if(oleAccessActivationConfiguration.getRecipientRoleId()!=null){
            oleAccessActivationConfiguration.setNotificationSelector("Role");
        }else if(oleAccessActivationConfiguration.getRecipientUserId()!=null){
            oleAccessActivationConfiguration.setNotificationSelector("Person");
        }
    }
}
