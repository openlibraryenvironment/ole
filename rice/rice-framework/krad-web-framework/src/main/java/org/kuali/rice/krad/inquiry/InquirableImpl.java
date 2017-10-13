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
package org.kuali.rice.krad.inquiry;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.datadictionary.exception.UnknownBusinessClassAttributeException;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.DataObjectAuthorizationService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.util.ExternalizableBusinessObjectUtils;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the <code>Inquirable</code> interface that uses metadata
 * from the data dictionary and performs a query against the database to retrieve
 * the data object for inquiry
 *
 * <p>
 * More advanced lookup operations or alternate ways of retrieving metadata can
 * be implemented by extending this base implementation and configuring
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class InquirableImpl extends ViewHelperServiceImpl implements Inquirable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InquirableImpl.class);

    protected Class<?> dataObjectClass;

    /**
     * A list that can be used to define classes that are superclasses or
     * superinterfaces of kuali objects where those objects' inquiry URLs need
     * to use the name of the superclass or superinterface as the business
     * object class attribute
     */
    public static List<Class<?>> SUPER_CLASS_TRANSLATOR_LIST = new ArrayList<Class<?>>();

    /**
     * Finds primary and alternate key sets configured for the configured data object class and
     * then attempts to find a set with matching key/value pairs from the request, if a set is
     * found then calls the module service (for EBOs) or business object service to retrieve
     * the data object
     *
     * <p>
     * Note at this point on business objects are supported by the default implementation
     * </p>
     *
     * @see Inquirable#retrieveDataObject(java.util.Map<java.lang.String,java.lang.String>)
     */
    @Override
    public Object retrieveDataObject(Map<String, String> parameters) {
        if (dataObjectClass == null) {
            LOG.error("Data object class must be set in inquirable before retrieving the object");
            throw new RuntimeException("Data object class must be set in inquirable before retrieving the object");
        }

        // build list of key values from the map parameters
        List<String> pkPropertyNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(dataObjectClass);

        // some classes might have alternate keys defined for retrieving
        List<List<String>> alternateKeyNames = this.getAlternateKeysForClass(dataObjectClass);

        // add pk set as beginning so it will be checked first for match
        alternateKeyNames.add(0, pkPropertyNames);

        List<String> dataObjectKeySet = retrieveKeySetFromMap(alternateKeyNames, parameters);
        if ((dataObjectKeySet == null) || dataObjectKeySet.isEmpty()) {
            LOG.warn("Matching key set not found in request for class: " + getDataObjectClass());

            return null;
        }

        // found key set, now build map of key values pairs we can use to retrieve the object
        Map<String, Object> keyPropertyValues = new HashMap<String, Object>();
        for (String keyPropertyName : dataObjectKeySet) {
            String keyPropertyValue = parameters.get(keyPropertyName);

            // uppercase value if needed
            Boolean forceUppercase = Boolean.FALSE;
            try {
                forceUppercase = getDataDictionaryService().getAttributeForceUppercase(dataObjectClass,
                        keyPropertyName);
            } catch (UnknownBusinessClassAttributeException ex) {
                // swallowing exception because this check for ForceUppercase would
                // require a DD entry for the attribute, and we will just set force uppercase to false
                LOG.warn("Data object class "
                        + dataObjectClass
                        + " property "
                        + keyPropertyName
                        + " should probably have a DD definition.", ex);
            }

            if (forceUppercase.booleanValue() && (keyPropertyValue != null)) {
                keyPropertyValue = keyPropertyValue.toUpperCase();
            }

            // check security on key field
            if (getDataObjectAuthorizationService().attributeValueNeedsToBeEncryptedOnFormsAndLinks(dataObjectClass,
                    keyPropertyName)) {
                try {
                    if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                        keyPropertyValue = getEncryptionService().decrypt(keyPropertyValue);
                    }
                } catch (GeneralSecurityException e) {
                    LOG.error("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                    throw new RuntimeException("Data object class "
                            + dataObjectClass
                            + " property "
                            + keyPropertyName
                            + " should have been encrypted, but there was a problem decrypting it.", e);
                }
            }

            keyPropertyValues.put(keyPropertyName, keyPropertyValue);
        }

        // now retrieve the object based on the key set
        Object dataObject = null;

        ModuleService moduleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(
                getDataObjectClass());
        if (moduleService != null && moduleService.isExternalizable(getDataObjectClass())) {
            dataObject = moduleService.getExternalizableBusinessObject(getDataObjectClass().asSubclass(
                    ExternalizableBusinessObject.class), keyPropertyValues);
        } else if (BusinessObject.class.isAssignableFrom(getDataObjectClass())) {
            dataObject = getBusinessObjectService().findByPrimaryKey(getDataObjectClass().asSubclass(
                    BusinessObject.class), keyPropertyValues);
        }

        return dataObject;
    }

    /**
     * Iterates through the list of key sets looking for a set where the given map of parameters has
     * all the key names and values are non-blank, first matched set is returned
     *
     * @param potentialKeySets - List of key sets to check for match
     * @param parameters - map of parameter name/value pairs for matching key set
     * @return List<String> key set that was matched, or null if none were matched
     */
    protected List<String> retrieveKeySetFromMap(List<List<String>> potentialKeySets, Map<String, String> parameters) {
        List<String> foundKeySet = null;

        for (List<String> potentialKeySet : potentialKeySets) {
            boolean keySetMatch = true;
            for (String keyName : potentialKeySet) {
                if (!parameters.containsKey(keyName) || StringUtils.isBlank(parameters.get(keyName))) {
                    keySetMatch = false;
                }
            }

            if (keySetMatch) {
                foundKeySet = potentialKeySet;
                break;
            }
        }

        return foundKeySet;
    }

    /**
     * Invokes the module service to retrieve any alternate keys that have been
     * defined for the given class
     *
     * @param clazz - class to find alternate keys for
     * @return List<List<String>> list of alternate key sets, or empty list if none are found
     */
    protected List<List<String>> getAlternateKeysForClass(Class<?> clazz) {
        KualiModuleService kualiModuleService = getKualiModuleService();
        ModuleService moduleService = kualiModuleService.getResponsibleModuleService(clazz);

        List<List<String>> altKeys = null;
        if (moduleService != null) {
            altKeys = moduleService.listAlternatePrimaryKeyFieldNames(clazz);
        }

        return altKeys != null ? altKeys : new ArrayList<List<String>>();
    }

    /**
     * @see Inquirable#buildInquirableLink(java.lang.Object,
     *      java.lang.String, org.kuali.rice.krad.uif.widget.Inquiry)
     */
    @Override
    public void buildInquirableLink(Object dataObject, String propertyName, Inquiry inquiry) {
        Class<?> inquiryObjectClass = null;

        // inquiry into data object class if property is title attribute
        Class<?> objectClass = ObjectUtils.materializeClassForProxiedObject(dataObject);
        if (propertyName.equals(getDataObjectMetaDataService().getTitleAttribute(objectClass))) {
            inquiryObjectClass = objectClass;
        } else if (ObjectUtils.isNestedAttribute(propertyName)) {
            String nestedPropertyName = ObjectUtils.getNestedAttributePrefix(propertyName);
            Object nestedPropertyObject = ObjectUtils.getNestedValue(dataObject, nestedPropertyName);

            if (ObjectUtils.isNotNull(nestedPropertyObject)) {
                String nestedPropertyPrimitive = ObjectUtils.getNestedAttributePrimitive(propertyName);
                Class<?> nestedPropertyObjectClass = ObjectUtils.materializeClassForProxiedObject(nestedPropertyObject);

                if (nestedPropertyPrimitive.equals(getDataObjectMetaDataService().getTitleAttribute(
                        nestedPropertyObjectClass))) {
                    inquiryObjectClass = nestedPropertyObjectClass;
                }
            }
        }

        // if not title, then get primary relationship
        DataObjectRelationship relationship = null;
        if (inquiryObjectClass == null) {
            relationship = getDataObjectMetaDataService().getDataObjectRelationship(dataObject, objectClass,
                    propertyName, "", true, false, true);
            if (relationship != null) {
                inquiryObjectClass = relationship.getRelatedClass();
            }
        }

        // if haven't found inquiry class, then no inquiry can be rendered
        if (inquiryObjectClass == null) {
            inquiry.setRender(false);

            return;
        }

        if (DocumentHeader.class.isAssignableFrom(inquiryObjectClass)) {
            String documentNumber = (String) ObjectUtils.getPropertyValue(dataObject, propertyName);
            if (StringUtils.isNotBlank(documentNumber)) {
                inquiry.getInquiryLink().setHref(getConfigurationService().getPropertyValueAsString(
                        KRADConstants.WORKFLOW_URL_KEY)
                        + KRADConstants.DOCHANDLER_DO_URL
                        + documentNumber
                        + KRADConstants.DOCHANDLER_URL_CHUNK);
                inquiry.getInquiryLink().setLinkText(documentNumber);
                inquiry.setRender(true);
            }

            return;
        }

        synchronized (SUPER_CLASS_TRANSLATOR_LIST) {
            for (Class<?> clazz : SUPER_CLASS_TRANSLATOR_LIST) {
                if (clazz.isAssignableFrom(inquiryObjectClass)) {
                    inquiryObjectClass = clazz;
                    break;
                }
            }
        }

        if (!inquiryObjectClass.isInterface() && ExternalizableBusinessObject.class.isAssignableFrom(
                inquiryObjectClass)) {
            inquiryObjectClass = ExternalizableBusinessObjectUtils.determineExternalizableBusinessObjectSubInterface(
                    inquiryObjectClass);
        }

        // listPrimaryKeyFieldNames returns an unmodifiable list. So a copy is necessary.
        List<String> keys = new ArrayList<String>(getDataObjectMetaDataService().listPrimaryKeyFieldNames(
                inquiryObjectClass));

        if (keys == null) {
            keys = Collections.emptyList();
        }

        // build inquiry parameter mappings
        Map<String, String> inquiryParameters = new HashMap<String, String>();
        for (String keyName : keys) {
            String keyConversion = keyName;
            if (relationship != null) {
                keyConversion = relationship.getParentAttributeForChildAttribute(keyName);
            } else if (ObjectUtils.isNestedAttribute(propertyName)) {
                String nestedAttributePrefix = ObjectUtils.getNestedAttributePrefix(propertyName);
                keyConversion = nestedAttributePrefix + "." + keyName;
            }

            inquiryParameters.put(keyConversion, keyName);
        }

        inquiry.buildInquiryLink(dataObject, propertyName, inquiryObjectClass, inquiryParameters);
    }

    /**
     * @see Inquirable#setDataObjectClass(java.lang.Class)
     */
    @Override
    public void setDataObjectClass(Class<?> dataObjectClass) {
        this.dataObjectClass = dataObjectClass;
    }

    /**
     * Retrieves the data object class configured for this inquirable
     *
     * @return Class<?> of configured data object, or null if data object class not configured
     */
    protected Class<?> getDataObjectClass() {
        return this.dataObjectClass;
    }

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        return KRADServiceLocatorWeb.getDataObjectMetaDataService();
    }

    protected KualiModuleService getKualiModuleService() {
        return KRADServiceLocatorWeb.getKualiModuleService();
    }

    protected DataDictionaryService getDataDictionaryService() {
        return KRADServiceLocatorWeb.getDataDictionaryService();
    }

    protected DataObjectAuthorizationService getDataObjectAuthorizationService() {
        return KRADServiceLocatorWeb.getDataObjectAuthorizationService();
    }

    protected EncryptionService getEncryptionService() {
        return CoreApiServiceLocator.getEncryptionService();
    }

}
