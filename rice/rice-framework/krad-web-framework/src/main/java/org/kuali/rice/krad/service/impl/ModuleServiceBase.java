/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Base implementation of the module service
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ModuleServiceBase extends RemoteModuleServiceBase implements ModuleService {

    protected static final Logger LOG = Logger.getLogger(ModuleServiceBase.class);

    protected BusinessObjectService businessObjectService;
    protected DataObjectMetaDataService dataObjectMetaDataService;

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObject(java.lang.Class, java.util.Map)
     */
    public <T extends ExternalizableBusinessObject> T getExternalizableBusinessObject(Class<T> businessObjectClass,
            Map<String, Object> fieldValues) {
        Class<? extends ExternalizableBusinessObject> implementationClass =
                getExternalizableBusinessObjectImplementation(businessObjectClass);
        ExternalizableBusinessObject businessObject =
                (ExternalizableBusinessObject) getBusinessObjectService().findByPrimaryKey(implementationClass,
                        fieldValues);
        return (T) businessObject;
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObject(java.lang.Class, java.util.Map)
     */
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(
            Class<T> externalizableBusinessObjectClass, Map<String, Object> fieldValues) {
        Class<? extends ExternalizableBusinessObject> implementationClass =
                getExternalizableBusinessObjectImplementation(externalizableBusinessObjectClass);
        return (List<T>) getBusinessObjectService().findMatching(implementationClass, fieldValues);
    }



    @Deprecated
    public String getExternalizableBusinessObjectInquiryUrl(Class inquiryBusinessObjectClass,
            Map<String, String[]> parameters) {
        if (!isExternalizable(inquiryBusinessObjectClass)) {
            return KRADConstants.EMPTY_STRING;
        }
        String businessObjectClassAttribute;

        Class implementationClass = getExternalizableBusinessObjectImplementation(inquiryBusinessObjectClass);
        if (implementationClass == null) {
            LOG.error("Can't find ExternalizableBusinessObject implementation class for " + inquiryBusinessObjectClass
                    .getName());
            throw new RuntimeException("Can't find ExternalizableBusinessObject implementation class for interface "
                    + inquiryBusinessObjectClass.getName());
        }
        businessObjectClassAttribute = implementationClass.getName();
        return UrlFactory.parameterizeUrl(getInquiryUrl(inquiryBusinessObjectClass), getUrlParameters(
                businessObjectClassAttribute, parameters));
    }

    @Deprecated
    @Override
    protected String getInquiryUrl(Class inquiryBusinessObjectClass) {
        
        String riceBaseUrl = "";
        String potentialUrlAddition = "";

        if (goToCentralRiceForInquiry()) {
            riceBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.KUALI_RICE_URL_KEY);
        } else {
            riceBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.APPLICATION_URL_KEY);
            potentialUrlAddition = "kr/";
        }
        
        String inquiryUrl = riceBaseUrl;
        if (!inquiryUrl.endsWith("/")) {
            inquiryUrl = inquiryUrl + "/";
        }
        return inquiryUrl + potentialUrlAddition + KRADConstants.INQUIRY_ACTION;
    }

    @Override
    public boolean isExternalizableBusinessObjectLookupable(Class boClass) {
        return getDataObjectMetaDataService().hasLocalLookup(boClass);
    }

    @Override
    public boolean isExternalizableBusinessObjectInquirable(Class boClass) {
        return getDataObjectMetaDataService().hasLocalInquiry(boClass);
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObjectLookupUrl(java.lang.Class,
     *      java.util.Map)
     */
    @Deprecated
    @Override
    public String getExternalizableBusinessObjectLookupUrl(Class inquiryBusinessObjectClass,
            Map<String, String> parameters) {
        Properties urlParameters = new Properties();

        String riceBaseUrl = "";
        String potentialUrlAddition = "";

        if (goToCentralRiceForInquiry()) {
            riceBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.KUALI_RICE_URL_KEY);
        } else {
            riceBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.APPLICATION_URL_KEY);
            potentialUrlAddition = "kr/";
        }
        
        String lookupUrl = riceBaseUrl;
        if (!lookupUrl.endsWith("/")) {
            lookupUrl = lookupUrl + "/";
        }
        
        if (parameters.containsKey(KRADConstants.MULTIPLE_VALUE)) {
            lookupUrl = lookupUrl + potentialUrlAddition + KRADConstants.MULTIPLE_VALUE_LOOKUP_ACTION;
        }
        else {
            lookupUrl = lookupUrl + potentialUrlAddition + KRADConstants.LOOKUP_ACTION;
        }
           
        for (String paramName : parameters.keySet()) {
            urlParameters.put(paramName, parameters.get(paramName));
        }

        /*Class clazz;
        if (inquiryBusinessObjectClass.getClass().isInterface()) {*/
        Class clazz = getExternalizableBusinessObjectImplementation(inquiryBusinessObjectClass);
        /*} else {
            clazz = inquiryBusinessObjectClass;
        }*/
        urlParameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, clazz == null ? "" : clazz.getName());

        return UrlFactory.parameterizeUrl(lookupUrl, urlParameters);
    }

    public boolean goToCentralRiceForInquiry() {
        return false;
    }

    /**
     * Returns the base URL to use for lookup requests to objects within the module
     *
     * @return String base lookup URL
     */
    @Override
    protected String getBaseLookupUrl() {
        if (goToCentralRiceForInquiry()) {
            return BaseLookupUrlsHolder.remoteKradBaseLookupUrl;
        } else {
            return BaseLookupUrlsHolder.localKradBaseLookupUrl;
        }
    }

    protected RunMode getRunMode(String module) {
        String propertyName = module + ".mode";
        String runMode = ConfigContext.getCurrentContextConfig().getProperty(propertyName);
        if (StringUtils.isBlank(runMode)) {
            throw new ConfigurationException("Failed to determine run mode for module '" + module + "'.  Please be sure to set configuration parameter '" + propertyName + "'");
        }
        return RunMode.valueOf(runMode.toUpperCase());
    }

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        if (dataObjectMetaDataService == null) {
            dataObjectMetaDataService = KRADServiceLocatorWeb.getDataObjectMetaDataService();
        }

        return dataObjectMetaDataService;
    }

    public void setDataObjectMetaDataService(DataObjectMetaDataService dataObjectMetaDataService) {
        this.dataObjectMetaDataService = dataObjectMetaDataService;
    }

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }

        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}

