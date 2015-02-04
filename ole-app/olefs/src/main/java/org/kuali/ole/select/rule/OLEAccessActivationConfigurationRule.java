package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLERoleBo;
import org.kuali.ole.sys.spring.datadictionary.WorkflowAttributesBeanDefinitionParser;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

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

        OLEAccessActivationWorkFlow workflow = new OLEAccessActivationWorkFlow();
        OLEAccessActivationConfiguration accessConfiguration=(OLEAccessActivationConfiguration)document.getNewMaintainableObject().getDataObject();
        //OLERoleBo rolebo = (OLERoleBo ) document.getNewMaintainableObject().getDataObject()
        isValid &= validateUniqueAccessName(accessConfiguration);
        /*isValid &= validateUniqueOrderNo(accessConfiguration);
        isValid &= validateUniqueStatus(accessConfiguration);*/
        //isValid &= validateRole(accessConfiguration);
        return isValid;

    }

    public boolean validateUniqueAccessName (OLEAccessActivationConfiguration accessConfiguration){
        if(accessConfiguration.getWorkflowName()!=null){
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.ACCESS_NAME,accessConfiguration.getWorkflowName());
            List<OLEAccessActivationConfiguration> dataSourceNameInDatabaseAccessName = (List<OLEAccessActivationConfiguration>) getBoService().findMatching(OLEAccessActivationConfiguration.class,criteria);
            if(dataSourceNameInDatabaseAccessName.size()>0){
                for(OLEAccessActivationConfiguration oleconfigurationName : dataSourceNameInDatabaseAccessName){
                    String accessName=oleconfigurationName.getAccessActivationConfigurationId();
                    if(null==accessConfiguration.getAccessActivationConfigurationId() || (!accessConfiguration.getAccessActivationConfigurationId().equalsIgnoreCase(accessName))){
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_WORKFLOW_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }



    public boolean validateUniqueOrderNo (OLEAccessActivationConfiguration accessConfiguration ){
        if(accessConfiguration.getAccessActivationWorkflowList() != null){
            for(OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()){
                Map<String, Integer> criteria = new HashMap<String, Integer>();
                criteria.put(OLEConstants.ORDER_NO, workflow.getOrderNo());
                List<OLEAccessActivationWorkFlow> dataSourceNameInDatabaseorderno = (List<OLEAccessActivationWorkFlow>) getBoService()
                        .findMatching(OLEAccessActivationWorkFlow.class, criteria);
                if(dataSourceNameInDatabaseorderno.size()>0){
                    for(OLEAccessActivationWorkFlow accessActivation : dataSourceNameInDatabaseorderno){
                        String accessId=accessActivation.getAccessId();
                        if(null==workflow.getAccessId() || (!workflow.getAccessId().equalsIgnoreCase(accessId))){
                            GlobalVariables.getMessageMap().putError(OLEConstants.ORDER_NO_FIELD, OLEConstants.ERROR_DUPLICATE_ORDER_NO);
                            return false;
                        }
                    }

                }

            }
        }
        return true;
    }

    public boolean validateUniqueStatus (OLEAccessActivationConfiguration accessConfiguration ){
        if(accessConfiguration.getAccessActivationWorkflowList() != null ){
            for(OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()){
                Map<String, String> criteria = new HashMap<String, String>();
                criteria.put(OLEConstants.ACCESS_STATUS,workflow.getStatus());
                List<OLEAccessActivationWorkFlow> dataSourceNameInDatabaseStatus = (List<OLEAccessActivationWorkFlow>) getBoService()
                        .findMatching(OLEAccessActivationWorkFlow.class, criteria);
                if(dataSourceNameInDatabaseStatus.size()>0){
                    for(OLEAccessActivationWorkFlow accessActivation : dataSourceNameInDatabaseStatus){
                        String accessId=accessActivation.getAccessId();
                        if(null==workflow.getAccessId() || (!workflow.getAccessId().equalsIgnoreCase(accessId))){
                            GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_STATUS_FIELD, OLEConstants.ERROR_DUPLICATE_STATUS);
                            return false;
                        }
                    }

                }
            }

        }
        return true;
    }
    public boolean validateRole (OLEAccessActivationConfiguration accessConfiguration){
        Map<String, String> criteria = new HashMap<String, String>();
        List<RoleBo> dataSourceNameInDatabaseroleName ;
        RoleBo roleBo;
        boolean validRole = false;
        if(accessConfiguration.getAccessActivationWorkflowList() != null ){
            for(OLEAccessActivationWorkFlow workflow : accessConfiguration.getAccessActivationWorkflowList()){
                if(workflow.getRoleId()!=null && workflow.getRoleName()!=null){
                    criteria.put(OLEConstants.ACCESS_ROLE_ID,workflow.getRoleId());
                    criteria.put(OLEConstants.ACCESS_ROLE_NAME, workflow.getRoleName());
                    dataSourceNameInDatabaseroleName =  (List<RoleBo>) getBoService()
                            .findMatching(RoleBo.class, criteria);
                    if(dataSourceNameInDatabaseroleName!=null && dataSourceNameInDatabaseroleName.size()>0){
                        validRole= true;
                    }else{
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_ROLE_NAME_ID_FIELD,OLEConstants.ERROR_INVALID_ID_NAME );
                        validRole= false;
                    }
                }else if(workflow.getRoleId()==null && workflow.getRoleName()!=null){
                    criteria = new HashMap<String,String>();
                    criteria.put(OLEConstants.ACCESS_ROLE_NAME,workflow.getRoleName());
                    dataSourceNameInDatabaseroleName =  (List<RoleBo>) getBoService()
                            .findMatching(RoleBo.class, criteria);
                    if(dataSourceNameInDatabaseroleName!=null && dataSourceNameInDatabaseroleName.size()>0){
                        roleBo = dataSourceNameInDatabaseroleName.get(0);
                        workflow.setRoleId(roleBo.getId());
                        validRole= true;
                    }else{
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_ROLE_NAME_ID_FIELD, OLEConstants.ERROR_INVALID_NAME);
                        validRole= false;
                    }
                }else if(workflow.getRoleId()!=null && workflow.getRoleName()==null){
                    criteria = new HashMap<String,String>();
                    criteria.put(OLEConstants.ACCESS_ROLE_ID,workflow.getRoleId());
                    dataSourceNameInDatabaseroleName =  (List<RoleBo>) getBoService()
                            .findMatching(RoleBo.class, criteria);
                    if(dataSourceNameInDatabaseroleName!=null && dataSourceNameInDatabaseroleName.size()>0){
                        roleBo = dataSourceNameInDatabaseroleName.get(0);
                        workflow.setRoleName(roleBo.getName());
                        validRole= true;
                    }else{
                        GlobalVariables.getMessageMap().putError(OLEConstants.ACCESS_ROLE_NAME_ID_FIELD, OLEConstants.ERROR_INVALID_ID);
                        validRole= false;
                    }
                }


            }

        }
        return validRole;
    }

}