package org.kuali.ole.select.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;

import org.kuali.ole.select.bo.OLEAccessActivationConfiguration;
import org.kuali.ole.select.bo.OLEAccessActivationWorkFlow;
import org.kuali.ole.select.bo.OLERoleBo;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.view.LookupView;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.*;

/**
 * Created by hemalathas on 12/22/14.
 */
public class OLEAccessActivationConfigurationLookupableImpl extends LookupableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEAccessActivationConfigurationLookupableImpl.class);

   /* *//**
     * This method will populate the search criteria and return the search results
     *
     * @param form
     * @param searchCriteria
     * @param bounded
     * @return displayList(Collection)
     *//*
    @Override
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded) {
        LOG.debug("Inside performSearch()");
        Collection<?> displayList;
        displayList = super.performSearch(form,searchCriteria,bounded);
        try {
            // TODO delyea - is this the best way to set that the entire set has a returnable row?
            for (Object object : displayList) {
                if(object instanceof OLEAccessActivationConfiguration){
                    OLEAccessActivationConfiguration oleAccessActivationConfiguration = (OLEAccessActivationConfiguration) object;
                    List<OLEAccessActivationWorkFlow> accessActivationWorkFlowList = oleAccessActivationConfiguration.getAccessActivationWorkflowList();
                    for(OLEAccessActivationWorkFlow oleAccessActivationWorkFlow : accessActivationWorkFlowList){
                        Map<String,Object> oleBoMap = new HashMap<>();
                        oleBoMap.put("id",oleAccessActivationWorkFlow.getRoleId());
                        OLERoleBo oleRoleBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OLERoleBo.class,oleBoMap);
                        oleAccessActivationWorkFlow.setRoleName(oleRoleBo.getName());
                    }

                }
                if (isResultReturnable(object)) {
                    form.setAtLeastOneRowReturnable(true);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return displayList;
    }*/

    @Override
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall,
                                      List<String> pkNames) {
        LOG.debug("Inside getActionUrlHref()");
        LookupView lookupView = (LookupView) lookupForm.getView();

        Properties props = new Properties();
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);
        Map<String, String> primaryKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
        for (String primaryKey : primaryKeyValues.keySet()) {
            String primaryKeyValue = primaryKeyValues.get(primaryKey);

            props.put(primaryKey, primaryKeyValue);
        }

        if (StringUtils.isNotBlank(lookupForm.getReturnLocation())) {
            props.put(KRADConstants.RETURN_LOCATION_PARAMETER, lookupForm.getReturnLocation());
        }

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, OLEAccessActivationConfiguration.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        String maintenanceMapping = OLEConstants.ACCESS_ACTIVATION_CONFIGURATION_CONTROLLER;

        return UrlFactory.parameterizeUrl(maintenanceMapping, props);
    }
}
