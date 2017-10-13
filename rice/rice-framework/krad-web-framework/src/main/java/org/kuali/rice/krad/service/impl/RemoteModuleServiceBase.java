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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.bo.ModuleConfiguration;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.PrimitiveAttributeDefinition;
import org.kuali.rice.krad.datadictionary.RelationshipDefinition;
import org.kuali.rice.krad.service.BusinessObjectNotLookupableException;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.ExternalizableBusinessObjectUtils;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class RemoteModuleServiceBase implements ModuleService {
    protected static final Logger LOG = Logger.getLogger(RemoteModuleServiceBase.class);

    protected ModuleConfiguration moduleConfiguration;
    protected KualiModuleService kualiModuleService;
    protected ApplicationContext applicationContext;
    protected ConfigurationService kualiConfigurationService;
    protected LookupService lookupService;

    /**
     * @see org.kuali.rice.krad.service.ModuleService#isResponsibleFor(java.lang.Class)
     */
    public boolean isResponsibleFor(Class businessObjectClass) {
        if (getModuleConfiguration() == null) {
            throw new IllegalStateException("Module configuration has not been initialized for the module service.");
        }

        if (getModuleConfiguration().getPackagePrefixes() == null || businessObjectClass == null) {
            return false;
        }
        for (String prefix : getModuleConfiguration().getPackagePrefixes()) {
            if (businessObjectClass.getPackage().getName().startsWith(prefix)) {
                return true;
            }
        }
        if (ExternalizableBusinessObject.class.isAssignableFrom(businessObjectClass)) {
            Class externalizableBusinessObjectInterface =
                    ExternalizableBusinessObjectUtils.determineExternalizableBusinessObjectSubInterface(
                            businessObjectClass);
            if (externalizableBusinessObjectInterface != null) {
                for (String prefix : getModuleConfiguration().getPackagePrefixes()) {
                    if (externalizableBusinessObjectInterface.getPackage().getName().startsWith(prefix)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Utility method to check for the presence of a non blank value in the map for the given key
     * Note: returns false if a null map is passed in.
     *
     * @param map the map to retrieve the value from
     * @param key the key to use
     * @return true if there is a non-blank value in the map for the given key.
     */
    protected static boolean isNonBlankValueForKey(Map<String, Object> map, String key) {
        if (map == null) return false;

        Object result = map.get(key);
        if (result instanceof String) {
            return !StringUtils.isBlank((String)result);
        }
        return result != null;
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#isResponsibleFor(java.lang.Class)
     */
    public boolean isResponsibleForJob(String jobName) {
        if (getModuleConfiguration() == null) {
            throw new IllegalStateException("Module configuration has not been initialized for the module service.");
        }

        if (getModuleConfiguration().getJobNames() == null || StringUtils.isEmpty(jobName)) {
            return false;
        }

        return getModuleConfiguration().getJobNames().contains(jobName);
    }


    public List listPrimaryKeyFieldNames(Class businessObjectInterfaceClass) {
        Class clazz = getExternalizableBusinessObjectImplementation(businessObjectInterfaceClass);
        return KRADServiceLocator.getPersistenceStructureService().listPrimaryKeyFieldNames(clazz);
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObjectDictionaryEntry(java.lang.Class)
     */
    public BusinessObjectEntry getExternalizableBusinessObjectDictionaryEntry(Class businessObjectInterfaceClass) {
        Class boClass = getExternalizableBusinessObjectImplementation(businessObjectInterfaceClass);

        return boClass == null ? null : KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary()
                .getBusinessObjectEntryForConcreteClass(boClass.getName());
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableDataObjectInquiryUrl(java.lang.Class,
     * java.util.Properties)
     */
    public String getExternalizableDataObjectInquiryUrl(Class<?> inquiryDataObjectClass, Properties parameters) {
        String baseUrl = getBaseInquiryUrl();

        // if external business object, replace data object in request with the actual impl object class
        if (ExternalizableBusinessObject.class.isAssignableFrom(inquiryDataObjectClass)) {
            Class implementationClass = getExternalizableBusinessObjectImplementation(inquiryDataObjectClass.asSubclass(
                    ExternalizableBusinessObject.class));
            if (implementationClass == null) {
                throw new RuntimeException("Can't find ExternalizableBusinessObject implementation class for "
                        + inquiryDataObjectClass.getName());
            }

            parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME, implementationClass.getName());
        }

        return UrlFactory.parameterizeUrl(baseUrl, parameters);
    }

    /**
     * Returns the base URL to use for inquiry requests to objects within the module
     *
     * @return String base inquiry URL
     */
    protected String getBaseInquiryUrl() {
        return getKualiConfigurationService().getPropertyValueAsString(KRADConstants.KRAD_INQUIRY_URL_KEY);
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableDataObjectLookupUrl(java.lang.Class,
     * java.util.Properties)
     */
    public String getExternalizableDataObjectLookupUrl(Class<?> lookupDataObjectClass, Properties parameters) {
        String baseUrl = getBaseLookupUrl();

        // if external business object, replace data object in request with the actual impl object class
        if (ExternalizableBusinessObject.class.isAssignableFrom(lookupDataObjectClass)) {
            Class implementationClass = getExternalizableBusinessObjectImplementation(lookupDataObjectClass.asSubclass(
                    ExternalizableBusinessObject.class));
            if (implementationClass == null) {
                throw new RuntimeException("Can't find ExternalizableBusinessObject implementation class for "
                        + lookupDataObjectClass.getName());
            }

            parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME, implementationClass.getName());
        }

        return UrlFactory.parameterizeUrl(baseUrl, parameters);
    }

    /**
     * Returns the base lookup URL for the Rice server
     *
     * @return String base lookup URL
     */
    protected String getRiceBaseLookupUrl() {
        return BaseLookupUrlsHolder.remoteKradBaseLookupUrl;
    }

    // Lazy initialization holder class idiom, see Effective Java item #71
    protected static final class BaseLookupUrlsHolder {

        public static final String localKradBaseLookupUrl;
        public static final String remoteKradBaseLookupUrl;

        static {
            remoteKradBaseLookupUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.KRAD_SERVER_LOOKUP_URL_KEY);
            localKradBaseLookupUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(KRADConstants.KRAD_LOOKUP_URL_KEY);
        }
    }

    /**
     * Returns the base URL to use for lookup requests to objects within the module
     *
     * @return String base lookup URL
     */
    protected String getBaseLookupUrl() {
        return getRiceBaseLookupUrl();
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

        String riceBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.KUALI_RICE_URL_KEY);
        String lookupUrl = riceBaseUrl;
        if (!lookupUrl.endsWith("/")) {
            lookupUrl = lookupUrl + "/";
        }
        if (parameters.containsKey(KRADConstants.MULTIPLE_VALUE)) {
            lookupUrl = lookupUrl + KRADConstants.MULTIPLE_VALUE_LOOKUP_ACTION;
        } else {
            lookupUrl = lookupUrl + KRADConstants.LOOKUP_ACTION;
        }
        for (String paramName : parameters.keySet()) {
            urlParameters.put(paramName, parameters.get(paramName));
        }

        Class clazz = getExternalizableBusinessObjectImplementation(inquiryBusinessObjectClass);
        urlParameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, clazz == null ? "" : clazz.getName());

        return UrlFactory.parameterizeUrl(lookupUrl, urlParameters);
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObjectsListForLookup(java.lang.Class,
     *      java.util.Map, boolean)
     */
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsListForLookup(
            Class<T> externalizableBusinessObjectClass, Map<String, Object> fieldValues, boolean unbounded) {
        Class<? extends ExternalizableBusinessObject> implementationClass =
                getExternalizableBusinessObjectImplementation(externalizableBusinessObjectClass);
        if (isExternalizableBusinessObjectLookupable(implementationClass)) {
            Map<String, String> searchCriteria = new HashMap<String, String>();
            for (Map.Entry<String, Object> fieldValue : fieldValues.entrySet()) {
                if (fieldValue.getValue() != null) {
                    searchCriteria.put(fieldValue.getKey(), fieldValue.getValue().toString());
                } else {
                    searchCriteria.put(fieldValue.getKey(), null);
                }
            }
            return (List<T>) getLookupService().findCollectionBySearchHelper(implementationClass, searchCriteria,
                    unbounded);
        } else {
            throw new BusinessObjectNotLookupableException(
                    "External business object is not a Lookupable:  " + implementationClass);
        }
    }

    /**
     * This method assumes that the property type for externalizable relationship in the business object is an interface
     * and gets the concrete implementation for it
     *
     * @see org.kuali.rice.krad.service.ModuleService#retrieveExternalizableBusinessObjectIfNecessary(org.kuali.rice.krad.bo.BusinessObject,
     *      org.kuali.rice.krad.bo.BusinessObject, java.lang.String)
     */
    public <T extends ExternalizableBusinessObject> T retrieveExternalizableBusinessObjectIfNecessary(
            BusinessObject businessObject, T currentInstanceExternalizableBO, String externalizableRelationshipName) {

        if (businessObject == null) {
            return null;
        }
        Class clazz;
        try {
            clazz = getExternalizableBusinessObjectImplementation(PropertyUtils.getPropertyType(businessObject,
                    externalizableRelationshipName));
        } catch (Exception iex) {
            LOG.warn("Exception:"
                    + iex
                    + " thrown while trying to get property type for property:"
                    + externalizableRelationshipName
                    + " from business object:"
                    + businessObject);
            return null;
        }

        //Get the business object entry for this business object from data dictionary
        //using the class name (without the package) as key
        BusinessObjectEntry entry =
                KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectEntries().get(
                        businessObject.getClass().getSimpleName());
        RelationshipDefinition relationshipDefinition = entry.getRelationshipDefinition(externalizableRelationshipName);
        List<PrimitiveAttributeDefinition> primitiveAttributeDefinitions =
                relationshipDefinition.getPrimitiveAttributes();

        Map<String, Object> fieldValuesInEBO = new HashMap<String, Object>();
        Object sourcePropertyValue;
        Object targetPropertyValue = null;
        boolean sourceTargetPropertyValuesSame = true;
        for (PrimitiveAttributeDefinition primitiveAttributeDefinition : primitiveAttributeDefinitions) {
            sourcePropertyValue = ObjectUtils.getPropertyValue(businessObject,
                    primitiveAttributeDefinition.getSourceName());
            if (currentInstanceExternalizableBO != null) {
                targetPropertyValue = ObjectUtils.getPropertyValue(currentInstanceExternalizableBO,
                        primitiveAttributeDefinition.getTargetName());
            }
            if (sourcePropertyValue == null) {
                return null;
            } else if (targetPropertyValue == null || (targetPropertyValue != null && !targetPropertyValue.equals(
                    sourcePropertyValue))) {
                sourceTargetPropertyValuesSame = false;
            }
            fieldValuesInEBO.put(primitiveAttributeDefinition.getTargetName(), sourcePropertyValue);
        }

        if (!sourceTargetPropertyValuesSame) {
            return (T) getExternalizableBusinessObject(clazz, fieldValuesInEBO);
        }
        return currentInstanceExternalizableBO;
    }

    /**
     * This method assumes that the externalizableClazz is an interface
     * and gets the concrete implementation for it
     *
     * @see org.kuali.rice.krad.service.ModuleService#retrieveExternalizableBusinessObjectIfNecessary(org.kuali.rice.krad.bo.BusinessObject,
     *      org.kuali.rice.krad.bo.BusinessObject, java.lang.String)
     */
    @Override
    public List<? extends ExternalizableBusinessObject> retrieveExternalizableBusinessObjectsList(
            BusinessObject businessObject, String externalizableRelationshipName, Class externalizableClazz) {

        if (businessObject == null) {
            return null;
        }
        //Get the business object entry for this business object from data dictionary
        //using the class name (without the package) as key
        String className = businessObject.getClass().getName();
        String key = className.substring(className.lastIndexOf(".") + 1);
        BusinessObjectEntry entry =
                KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getBusinessObjectEntries().get(
                        key);
        RelationshipDefinition relationshipDefinition = entry.getRelationshipDefinition(externalizableRelationshipName);
        List<PrimitiveAttributeDefinition> primitiveAttributeDefinitions =
                relationshipDefinition.getPrimitiveAttributes();
        Map<String, Object> fieldValuesInEBO = new HashMap<String, Object>();
        Object sourcePropertyValue;
        for (PrimitiveAttributeDefinition primitiveAttributeDefinition : primitiveAttributeDefinitions) {
            sourcePropertyValue = ObjectUtils.getPropertyValue(businessObject,
                    primitiveAttributeDefinition.getSourceName());
            if (sourcePropertyValue == null) {
                return null;
            }
            fieldValuesInEBO.put(primitiveAttributeDefinition.getTargetName(), sourcePropertyValue);
        }
        return getExternalizableBusinessObjectsList(getExternalizableBusinessObjectImplementation(externalizableClazz),
                fieldValuesInEBO);
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#getExternalizableBusinessObjectImplementation(java.lang.Class)
     */
    @Override
    public <E extends ExternalizableBusinessObject> Class<E> getExternalizableBusinessObjectImplementation(
            Class<E> externalizableBusinessObjectInterface) {
        if (getModuleConfiguration() == null) {
            throw new IllegalStateException("Module configuration has not been initialized for the module service.");
        }
        Map<Class, Class> ebos = getModuleConfiguration().getExternalizableBusinessObjectImplementations();
        if (ebos == null) {
            return null;
        }
        if (ebos.containsValue(externalizableBusinessObjectInterface)) {
            return externalizableBusinessObjectInterface;
        } else {
            Class<E> implementationClass = ebos.get(externalizableBusinessObjectInterface);

            int implClassModifiers = implementationClass.getModifiers();
            if (Modifier.isInterface(implClassModifiers) || Modifier.isAbstract(implClassModifiers)) {
                throw new RuntimeException("Implementation class must be non-abstract class: ebo interface: "
                        + externalizableBusinessObjectInterface.getName()
                        + " impl class: "
                        + implementationClass.getName()
                        + " module: "
                        + getModuleConfiguration().getNamespaceCode());
            }
            return implementationClass;
        }

    }

    @Deprecated
    protected Properties getUrlParameters(String businessObjectClassAttribute, Map<String, String[]> parameters) {
        Properties urlParameters = new Properties();
        for (String paramName : parameters.keySet()) {
            String[] parameterValues = parameters.get(paramName);
            if (parameterValues.length > 0) {
                urlParameters.put(paramName, parameterValues[0]);
            }
        }
        urlParameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, businessObjectClassAttribute);
        urlParameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.CONTINUE_WITH_INQUIRY_METHOD_TO_CALL);
        return urlParameters;
    }

    @Deprecated
    protected String getInquiryUrl(Class inquiryBusinessObjectClass) {
        String riceBaseUrl = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.KUALI_RICE_URL_KEY);
        String inquiryUrl = riceBaseUrl;
        if (!inquiryUrl.endsWith("/")) {
            inquiryUrl = inquiryUrl + "/";
        }
        return inquiryUrl + KRADConstants.INQUIRY_ACTION;
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        KualiModuleService kualiModuleService = null;
        try {
            kualiModuleService = KRADServiceLocatorWeb.getKualiModuleService();
            if (kualiModuleService == null) {
                kualiModuleService = ((KualiModuleService) applicationContext.getBean(
                        KRADServiceLocatorWeb.KUALI_MODULE_SERVICE));
            }
        } catch (NoSuchBeanDefinitionException ex) {
            kualiModuleService = ((KualiModuleService) applicationContext.getBean(
                    KRADServiceLocatorWeb.KUALI_MODULE_SERVICE));
        }
        kualiModuleService.getInstalledModuleServices().add(this);
    }

    /**
     * @return the moduleConfiguration
     */
    public ModuleConfiguration getModuleConfiguration() {
        return this.moduleConfiguration;
    }

    /**
     * @param moduleConfiguration the moduleConfiguration to set
     */
    public void setModuleConfiguration(ModuleConfiguration moduleConfiguration) {
        this.moduleConfiguration = moduleConfiguration;
    }

    /**
     * @see org.kuali.rice.krad.service.ModuleService#isExternalizable(java.lang.Class)
     */
    @Override
    public boolean isExternalizable(Class boClazz) {
        if (boClazz == null) {
            return false;
        }
        return ExternalizableBusinessObject.class.isAssignableFrom(boClazz);
    }

    public <T extends ExternalizableBusinessObject> T createNewObjectFromExternalizableClass(Class<T> boClass) {
        try {
            return (T) getExternalizableBusinessObjectImplementation(boClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create externalizable business object class", e);
        }
    }

    public DataObjectRelationship getBusinessObjectRelationship(Class boClass, String attributeName,
            String attributePrefix) {
        return null;
    }


    /**
     * @return the kualiModuleService
     */
    public KualiModuleService getKualiModuleService() {
        return this.kualiModuleService;
    }

    /**
     * @param kualiModuleService the kualiModuleService to set
     */
    public void setKualiModuleService(KualiModuleService kualiModuleService) {
        this.kualiModuleService = kualiModuleService;
    }

    protected ConfigurationService getKualiConfigurationService() {
        if (this.kualiConfigurationService == null) {
            this.kualiConfigurationService = CoreApiServiceLocator.getKualiConfigurationService();
        }

        return this.kualiConfigurationService;
    }

    public void setKualiConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.service.ModuleService#listAlternatePrimaryKeyFieldNames(java.lang.Class)
     */
    @Override
    public List<List<String>> listAlternatePrimaryKeyFieldNames(Class businessObjectInterfaceClass) {
        return null;
    }

    /**
     * This method determines whether or not this module is currently locked
     *
     * @see org.kuali.rice.krad.service.ModuleService#isLocked()
     */
    @Override
    public boolean isLocked() {
        ModuleConfiguration configuration = this.getModuleConfiguration();
        if (configuration != null) {
            String namespaceCode = configuration.getNamespaceCode();
            String componentCode = KRADConstants.DetailTypes.ALL_DETAIL_TYPE;
            String parameterName = KRADConstants.SystemGroupParameterNames.OLTP_LOCKOUT_ACTIVE_IND;
            ParameterService parameterService = CoreFrameworkServiceLocator.getParameterService();
            String shouldLockout = parameterService.getParameterValueAsString(namespaceCode, componentCode,
                    parameterName);
            if (StringUtils.isNotBlank(shouldLockout)) {
                return parameterService.getParameterValueAsBoolean(namespaceCode, componentCode, parameterName);
            }
        }
        return false;
    }

    /**
     * Gets the lookupService attribute.
     *
     * @return Returns the lookupService.
     */
    protected LookupService getLookupService() {
        return lookupService != null ? lookupService : KRADServiceLocatorWeb.getLookupService();
    }

    @Override
    public boolean goToCentralRiceForInquiry() {
        return false;
    }
}
