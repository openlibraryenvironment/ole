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
package org.kuali.rice.krad.web.bind;

import org.apache.commons.lang.ObjectUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.view.ViewIndex;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.KRADUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class is a top level BeanWrapper for a UIF View Model
 *
 * <p>
 * Registers custom property editors configured on the field associated with the property name for which
 * we are getting or setting a value. In addition determines if the field requires encryption and if so applies
 * the {@link UifEncryptionPropertyEditorWrapper}
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifViewBeanWrapper extends BeanWrapperImpl {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(UifViewBeanWrapper.class);

    // this is a handle to the target object so we don't have to cast so often
    private ViewModel model;

    // this stores all properties this wrapper has already checked
    // with the view so the service isn't called again
    private Set<String> processedProperties;

    public UifViewBeanWrapper(ViewModel model) {
        super(model);

        this.model = model;
        this.processedProperties = new HashSet<String>();
    }

    /**
     * Attempts to find a corresponding data field for the given property name in the current view or previous view,
     * then if the field has a property editor configured it is registered with the property editor registry to use
     * for this property
     *
     * @param propertyName - name of the property to find field and editor for
     */
    protected void registerEditorFromView(String propertyName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Attempting to find property editor for property '" + propertyName + "'");
        }

        // check if we already processed this property for this BeanWrapper instance
        if (processedProperties.contains(propertyName)) {
            return;
        }

        // when rendering the page, we will use the view that was just built, for post
        // we need to use the posted view (not the newly initialized view)
        ViewIndex viewIndex = null;
        if (model.getView() != null) {
            viewIndex = model.getView().getViewIndex();
        } else if (model.getPostedView() != null) {
            viewIndex = model.getPostedView().getViewIndex();
        }

        // if view index instance not established we cannot determine property editors
        if (viewIndex == null) {
            return;
        }

        PropertyEditor propertyEditor = null;
        boolean requiresEncryption = false;

        if (viewIndex.getFieldPropertyEditors().containsKey(propertyName)) {
            propertyEditor = viewIndex.getFieldPropertyEditors().get(propertyName);
        } else if (viewIndex.getSecureFieldPropertyEditors().containsKey(propertyName)) {
            propertyEditor = viewIndex.getSecureFieldPropertyEditors().get(propertyName);
            requiresEncryption = true;
        }

        if (propertyEditor != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Registering custom editor for property path '" + propertyName
                        + "' and property editor class '" + propertyEditor.getClass().getName() + "'");
            }

            if (requiresEncryption) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Enabling encryption for custom editor '" + propertyName +
                            "' and property editor class '" + propertyEditor.getClass().getName() + "'");
                }
                this.registerCustomEditor(null, propertyName, new UifEncryptionPropertyEditorWrapper(propertyEditor));
            } else {
                this.registerCustomEditor(null, propertyName, propertyEditor);
            }
        } else if (requiresEncryption) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No custom formatter for property path '" + propertyName
                        + "' but property does require encryption");
            }

            this.registerCustomEditor(null, propertyName, new UifEncryptionPropertyEditorWrapper(
                    findEditorForPropertyName(propertyName)));
        }

        processedProperties.add(propertyName);
    }

    protected PropertyEditor findEditorForPropertyName(String propertyName) {
        Class<?> clazz = getPropertyType(propertyName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Attempting retrieval of property editor using class '"
                    + clazz
                    + "' and property path '"
                    + propertyName
                    + "'");
        }

        PropertyEditor editor = findCustomEditor(clazz, propertyName);
        if (editor == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No custom property editor found using class '"
                        + clazz
                        + "' and property path '"
                        + propertyName
                        + "'. Attempting to find default property editor class.");
            }
            editor = getDefaultEditor(clazz);
        }

        return editor;
    }

    @Override
    public Class<?> getPropertyType(String propertyName) throws BeansException {
        try {
            PropertyDescriptor pd = getPropertyDescriptorInternal(propertyName);
            if (pd != null) {
                return pd.getPropertyType();
            }

            // Maybe an indexed/mapped property...
            Object value = super.getPropertyValue(propertyName);
            if (value != null) {
                return value.getClass();
            }

            // Check to see if there is a custom editor,
            // which might give an indication on the desired target type.
            Class<?> editorType = guessPropertyTypeFromEditors(propertyName);
            if (editorType != null) {
                return editorType;
            }
        } catch (InvalidPropertyException ex) {
            // Consider as not determinable.
        }

        return null;
    }

    @Override
    protected BeanWrapperImpl getBeanWrapperForPropertyPath(String propertyPath) {
        BeanWrapperImpl beanWrapper = super.getBeanWrapperForPropertyPath(propertyPath);

        PropertyTokenHolder tokens = getPropertyNameTokens(propertyPath);
        String canonicalName = tokens.canonicalName;

        int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(canonicalName);
        if (pos != -1) {
            canonicalName = canonicalName.substring(0, pos);
        }

        copyCustomEditorsTo(beanWrapper, canonicalName);

        return beanWrapper;
    }

    @Override
    public Object getPropertyValue(String propertyName) throws BeansException {
        registerEditorFromView(propertyName);

        Object value = null;
        try {
            value = super.getPropertyValue(propertyName);
        } catch (NullValueInNestedPathException e) {
           // swallow null values in path and return null as the value
        }

        return value;
    }

    @Override
    public void setPropertyValue(PropertyValue pv) throws BeansException {
        registerEditorFromView(pv.getName());
        
        if (pv != null && pv.getValue() instanceof String) {
            String propertyValue = (String) pv.getValue();

            if (propertyValue.endsWith(EncryptionService.ENCRYPTION_POST_PREFIX)) {
                propertyValue = org.apache.commons.lang.StringUtils.removeEnd(propertyValue, EncryptionService.ENCRYPTION_POST_PREFIX);
            }

            if (isSecure(pv.getName())) {
                try {
                    if (CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                        pv = new PropertyValue(pv, CoreApiServiceLocator.getEncryptionService().decrypt(propertyValue));
                    }
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        super.setPropertyValue(pv);
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) throws BeansException {
        registerEditorFromView(propertyName);
        
        if (value instanceof String) {
            String propertyValue = (String) value;

            if (propertyValue.endsWith(EncryptionService.ENCRYPTION_POST_PREFIX)) {
                propertyValue = org.apache.commons.lang.StringUtils.removeEnd(propertyValue, EncryptionService.ENCRYPTION_POST_PREFIX);
            }

            if (isSecure(propertyName)) {
                try {
                    if (CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                        value = CoreApiServiceLocator.getEncryptionService().decrypt(propertyValue);
                    }
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        super.setPropertyValue(propertyName, value);
    }

    @Override
    public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
        //TODO clear cache?
        model = (ViewModel) object;
        super.setWrappedInstance(object, nestedPath, rootObject);
    }

    @Override
    public void setWrappedInstance(Object object) {
        //TODO clear cache?
        model = (ViewModel) object;
        super.setWrappedInstance(object);
    }

    /**
     * Parse the given property name into the corresponding property name tokens.
     *
     * @param propertyName the property name to parse
     * @return representation of the parsed property tokens
     */
    private PropertyTokenHolder getPropertyNameTokens(String propertyName) {
        PropertyTokenHolder tokens = new PropertyTokenHolder();
        String actualName = null;
        List<String> keys = new ArrayList<String>(2);
        int searchIndex = 0;
        while (searchIndex != -1) {
            int keyStart = propertyName.indexOf(PROPERTY_KEY_PREFIX, searchIndex);
            searchIndex = -1;
            if (keyStart != -1) {
                int keyEnd = propertyName.indexOf(PROPERTY_KEY_SUFFIX, keyStart + PROPERTY_KEY_PREFIX.length());
                if (keyEnd != -1) {
                    if (actualName == null) {
                        actualName = propertyName.substring(0, keyStart);
                    }
                    String key = propertyName.substring(keyStart + PROPERTY_KEY_PREFIX.length(), keyEnd);
                    if ((key.startsWith("'") && key.endsWith("'")) || (key.startsWith("\"") && key.endsWith("\""))) {
                        key = key.substring(1, key.length() - 1);
                    }
                    keys.add(key);
                    searchIndex = keyEnd + PROPERTY_KEY_SUFFIX.length();
                }
            }
        }
        tokens.actualName = (actualName != null ? actualName : propertyName);
        tokens.canonicalName = tokens.actualName;
        if (!keys.isEmpty()) {
            tokens.canonicalName += PROPERTY_KEY_PREFIX +
                    StringUtils.collectionToDelimitedString(keys, PROPERTY_KEY_SUFFIX + PROPERTY_KEY_PREFIX) +
                    PROPERTY_KEY_SUFFIX;
            tokens.keys = StringUtils.toStringArray(keys);
        }
        return tokens;
    }

    private boolean isSecure(String propertyName) {
        return isSecure(getWrappedClass(), propertyName);
    }

    private boolean isSecure(Class<?> wrappedClass, String propertyPath) {
        if (KRADServiceLocatorWeb.getDataObjectAuthorizationService().attributeValueNeedsToBeEncryptedOnFormsAndLinks(wrappedClass, propertyPath)) {
            return true;
        }

        BeanWrapperImpl beanWrapper;
        try {
            beanWrapper = getBeanWrapperForPropertyPath(propertyPath);
        } catch (NotReadablePropertyException nrpe) {
            LOG.debug("Bean wrapper was not found for " + propertyPath + ", but since it cannot be accessed it will not be set as secure.", nrpe);
            return false;
        }

        if (org.apache.commons.lang.StringUtils.isNotBlank(beanWrapper.getNestedPath())) {
            PropertyTokenHolder tokens = getPropertyNameTokens(propertyPath);
            String nestedPropertyPath = org.apache.commons.lang.StringUtils.removeStart(tokens.canonicalName, beanWrapper.getNestedPath());

            return isSecure(beanWrapper.getWrappedClass(), nestedPropertyPath);
        }

        return false;
    }
    
    private static class PropertyTokenHolder {

        public String canonicalName;

        public String actualName;

        public String[] keys;
    }
}
