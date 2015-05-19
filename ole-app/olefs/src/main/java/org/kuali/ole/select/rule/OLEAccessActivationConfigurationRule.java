package org.kuali.ole.select.rule;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hemalathas on 12/17/14.
 */

public class OLEAccessActivationConfigurationRule extends MaintenanceDocumentRuleBase {

    private PersonService personService;

    public PersonService getPersonService() {
    if(personService == null){
        personService = KimApiServiceLocator.getPersonService();
    }
        return personService;
    }


    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEAccessActivationConfiguration accessConfiguration = (OLEAccessActivationConfiguration) document.getNewMaintainableObject().getDataObject();
        //OLERoleBo rolebo = (OLERoleBo ) document.getNewMaintainableObject().getDataObject()
        isValid &= validateUniqueAccessName(accessConfiguration);
        isValid &= validateAccessActivationWorkflows(accessConfiguration);
        isValid &= validateUniqueStatus(accessConfiguration);
        return isValid;
    }

    public boolean validateUniqueAccessName(OLEAccessActivationConfiguration accessConfiguration) {
        if (StringUtils.isNotBlank(accessConfiguration.getWorkflowName())) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.ACCESS_NAME, accessConfiguration.getWorkflowName());
            List<OLEAccessActivationConfiguration> dataSourceNameInDatabaseAccessName = (List<OLEAccessActivationConfiguration>) getBoService().findMatching(OLEAccessActivationConfiguration.class, criteria);
            if (dataSourceNameInDatabaseAccessName.size() > 0) {
                for (OLEAccessActivationConfiguration oleConfigurationName : dataSourceNameInDatabaseAccessName) {
                    String accessActivationConfigurationId = oleConfigurationName.getAccessActivationConfigurationId();
                    if (null == accessConfiguration.getAccessActivationConfigurationId() || (!accessConfiguration.getAccessActivationConfigurationId().equalsIgnoreCase(accessActivationConfigurationId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_WORKFLOW_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validateUniqueOrderNo(OLEAccessActivationConfiguration accessConfiguration) {
        if (accessConfiguration.getAccessActivationWorkflowList() != null) {
            List<Integer> orderNos = new ArrayList<>();
            for (OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()) {
                if (!orderNos.contains(workflow.getOrderNo())){
                    orderNos.add(workflow.getOrderNo());
                }else {
                    GlobalVariables.getMessageMap().putError(OLEConstants.ORDER_NO_FIELD, OLEConstants.ERROR_DUPLICATE_ORDER_NO_FOUND);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateUniqueStatus(OLEAccessActivationConfiguration accessConfiguration) {
        if (accessConfiguration.getAccessActivationWorkflowList() != null) {
            List<String> statusList = new ArrayList<>();
            for (OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()) {
                if (!statusList.contains(workflow.getStatus())){
                    statusList.add(workflow.getStatus());
                }else {
                    GlobalVariables.getMessageMap().putError(OLEConstants.ORDER_NO_FIELD, OLEConstants.ERROR_DUPLICATE_STATUS_FOUND);
                    return false;
                }
            }
        }
        return true;
    }


    private boolean validateAccessActivationWorkflows(OLEAccessActivationConfiguration accessConfiguration) {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(accessConfiguration.getAccessActivationWorkflowList())) {
            GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_NAME_FIELD, OLEConstants.ERROR_ATLEAST_ONE_WORKFLOW);
            isValid = false;
        }else {
            isValid &= validateUniqueOrderNo(accessConfiguration);

        }
        return isValid;
    }
}