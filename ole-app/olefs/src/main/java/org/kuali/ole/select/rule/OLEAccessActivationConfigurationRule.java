package org.kuali.ole.select.rule;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
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
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEAccessActivationConfiguration accessConfiguration = (OLEAccessActivationConfiguration) document.getNewMaintainableObject().getDataObject();
        //OLERoleBo rolebo = (OLERoleBo ) document.getNewMaintainableObject().getDataObject()
        isValid &= validateUniqueAccessName(accessConfiguration);
        isValid &= validateAccessActivationWorkflows(accessConfiguration);
        isValid &= validateUniqueStatus(accessConfiguration);
        isValid &= validateRole(accessConfiguration);
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

    public boolean validateRole(OLEAccessActivationConfiguration accessConfiguration) {
        Map<String, String> criteria = new HashMap<String, String>();
        List<RoleBo> dataSourceNameInDatabaseroleName;
        RoleBo roleBo;
        boolean validRole = false;
        if (accessConfiguration.getAccessActivationWorkflowList() != null) {
            for (OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()) {
                if (workflow.getRoleId() != null && workflow.getRoleName() != null) {
                    criteria.put(OLEConstants.ACCESS_ROLE_ID, workflow.getRoleId());
                    criteria.put(OLEConstants.ACCESS_ROLE_NAME, workflow.getRoleName());
                    dataSourceNameInDatabaseroleName = (List<RoleBo>) getBoService()
                            .findMatching(RoleBo.class, criteria);
                    if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                        validRole = true;
                    } else {
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_ROLE_NAME_ID_FIELD, OLEConstants.ERROR_INVALID_ID_NAME);
                        validRole = false;
                    }
                } else if (workflow.getRoleId() == null && workflow.getRoleName() != null) {
                    criteria = new HashMap<String, String>();
                    criteria.put(OLEConstants.ACCESS_ROLE_NAME, workflow.getRoleName());
                    dataSourceNameInDatabaseroleName = (List<RoleBo>) getBoService()
                            .findMatching(RoleBo.class, criteria);
                    if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                        roleBo = dataSourceNameInDatabaseroleName.get(0);
                        workflow.setRoleId(roleBo.getId());
                        validRole = true;
                    } else {
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_ROLE_NAME_ID_FIELD, OLEConstants.ERROR_INVALID_NAME);
                        validRole = false;
                    }
                } else if (workflow.getRoleId() != null && workflow.getRoleName() == null) {
                    criteria = new HashMap<String, String>();
                    criteria.put(OLEConstants.ACCESS_ROLE_ID, workflow.getRoleId());
                    dataSourceNameInDatabaseroleName = (List<RoleBo>) getBoService()
                            .findMatching(RoleBo.class, criteria);
                    if (dataSourceNameInDatabaseroleName != null && dataSourceNameInDatabaseroleName.size() > 0) {
                        roleBo = dataSourceNameInDatabaseroleName.get(0);
                        workflow.setRoleName(roleBo.getName());
                        validRole = true;
                    } else {
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_ROLE_NAME_ID_FIELD, OLEConstants.ERROR_INVALID_ID);
                        validRole = false;
                    }
                }
            }
        }
        return validRole;
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