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
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.component.Component;
import org.kuali.rice.coreservice.api.component.ComponentService;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.TransactionalDocumentEntry;
import org.kuali.rice.krad.document.TransactionalDocument;
import org.kuali.rice.krad.service.DataDictionaryComponentPublisherService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference implementation of the {@code DataDictionaryComponentPublisherService}.
 *
 * This implementation derives components from the DataDictionary for all BusinessObjects and Documents.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionaryComponentPublisherServiceImpl implements DataDictionaryComponentPublisherService {

    private static final Logger LOG = Logger.getLogger(DataDictionaryComponentPublisherServiceImpl.class);

    private static final String DEFAULT_COMPONENT_SET_ID_PREFIX = "DD:";

    private DataDictionaryService dataDictionaryService;
    private KualiModuleService kualiModuleService;
    private ComponentService componentService;
    private String applicationId;

    @Override
    public void publishAllComponents() {
        List<Component> componentsToPublish = getComponentsToPublish();
        getComponentService().publishDerivedComponents(generateComponentSetId(), componentsToPublish);
    }

    protected String generateComponentSetId() {
        if (StringUtils.isBlank(getApplicationId())) {
            throw new ConfigurationException("A valid non-null, non-blank application id was not injected into " + getClass().getName());
        }
        return DEFAULT_COMPONENT_SET_ID_PREFIX + getApplicationId();
    }

    protected List<Component> getComponentsToPublish() {
        List<Component> components = new ArrayList<Component>();

        Map<String, Component> uniqueComponentMap = new HashMap<String, Component>();
        for (BusinessObjectEntry businessObjectEntry : getDataDictionaryService().getDataDictionary().getBusinessObjectEntries().values()) {
            try {
                Component component = deriveComponentFromBusinessObjectEntry(businessObjectEntry);
                uniqueComponentMap.put(component.getCode(), component);
            }
            catch (Exception e) {
                LOG.error("An exception was encountered when attempting to publish all components for business object class: " + businessObjectEntry.getBusinessObjectClass(), e);
            }
        }
        for (DocumentEntry documentEntry : getDataDictionaryService().getDataDictionary().getDocumentEntries().values()) {
            if (documentEntry instanceof TransactionalDocumentEntry) {
                try {
                    Component component = deriveComponentFromDocumentEntry(documentEntry);
                    uniqueComponentMap.put(component.getCode(), component);
                }
                catch (Exception e) {
                    LOG.error("An exception was encountered when attempting to publish all components for transactional document class: " + documentEntry.getDocumentClass(), e);
                }
            }
        }
        components.addAll(uniqueComponentMap.values());
        return components;
    }

	protected Component deriveComponentFromClass(Class<?> componentSourceClass) {
        String componentCode = getKualiModuleService().getComponentCode(componentSourceClass);
        String componentName = deriveComponentName(componentSourceClass);
        String namespace = getKualiModuleService().getNamespaceCode(componentSourceClass);
        if (StringUtils.isBlank(componentName)) {
            componentName = componentCode;
        }
        Component.Builder detailType = Component.Builder.create(namespace, componentCode, componentName);
        return detailType.build();
    }

    protected Component deriveComponentFromBusinessObjectEntry(BusinessObjectEntry businessObjectEntry) {
        Class<?> businessObjectClass = businessObjectEntry.getBaseBusinessObjectClass();
        if (businessObjectClass == null) {
            businessObjectClass = businessObjectEntry.getBusinessObjectClass();
        }
        return deriveComponentFromClass(businessObjectClass);
    }

    protected Component deriveComponentFromDocumentEntry(DocumentEntry documentEntry) {
        Class<?> documentClass = documentEntry.getBaseDocumentClass();
        if (documentClass == null) {
            documentClass = documentEntry.getDocumentClass();
        }
        return deriveComponentFromClass(documentClass);
    }

	protected String deriveComponentName(Class<?> componentSourceClass) {
        if (componentSourceClass == null) {
            throw new IllegalArgumentException("The deriveComponentName method requires non-null componentSourceClass");
        }
        
        /*
         * Some business objects have a Component annotation that sets the value
         * of the classes annotaion.  This if block will test to see if it is there, try to get the
         * component value from the Data Dictionary if the BusinessObjectEntry exists, if it doesn't
         * exist, it will fall back to the annotation's value.
         */
        if (componentSourceClass.isAnnotationPresent(ParameterConstants.COMPONENT.class)) {
            BusinessObjectEntry boe = getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(componentSourceClass.getName());
            if (boe != null) {
                return boe.getObjectLabel();
            }
            else {
                return ((ParameterConstants.COMPONENT) componentSourceClass.getAnnotation(ParameterConstants.COMPONENT.class)).component();
            }
        }

        /*
         * If block that determines if the class is either a BusinessObject or a TransactionalDocument
         * return calls try to either get the BusinessObjectEntry's ObjectLable, or grabbing the
         * data dictionary's BusinessTitleForClass if it is a BusinessObject, or the DocumentLabel if it is a
         * TransactionalDocument
         */
        if (TransactionalDocument.class.isAssignableFrom(componentSourceClass)) {
            return getDataDictionaryService().getDocumentLabelByClass(componentSourceClass);
        }
        else if (BusinessObject.class.isAssignableFrom(componentSourceClass) ) {
            BusinessObjectEntry boe = getDataDictionaryService().getDataDictionary().getBusinessObjectEntry(componentSourceClass.getName());
            if (boe != null) {
                return boe.getObjectLabel();
            }
            else {
                return KRADUtils.getBusinessTitleForClass(componentSourceClass);
            }
        }
        throw new IllegalArgumentException("The deriveComponentName method of requires TransactionalDocument or BusinessObject class. Was: " + componentSourceClass.getName() );
    }

    public DataDictionaryService getDataDictionaryService() {
        return dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }

    public KualiModuleService getKualiModuleService() {
        return kualiModuleService;
    }

    public void setKualiModuleService(KualiModuleService kualiModuleService) {
        this.kualiModuleService = kualiModuleService;
    }

    public ComponentService getComponentService() {
        if (componentService == null) {
            componentService = CoreServiceApiServiceLocator.getComponentService();
        }
        return componentService;
    }

    public void setComponentService(ComponentService componentService) {
        this.componentService = componentService;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
}
