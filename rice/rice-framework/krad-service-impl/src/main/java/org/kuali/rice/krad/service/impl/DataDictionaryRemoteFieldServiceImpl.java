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
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAbstractControl;
import org.kuali.rice.core.api.uif.RemotableAbstractWidget;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableCheckbox;
import org.kuali.rice.core.api.uif.RemotableCheckboxGroup;
import org.kuali.rice.core.api.uif.RemotableHiddenInput;
import org.kuali.rice.core.api.uif.RemotableQuickFinder;
import org.kuali.rice.core.api.uif.RemotableRadioButtonGroup;
import org.kuali.rice.core.api.uif.RemotableSelect;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.uif.RemotableTextarea;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.service.DataDictionaryRemoteFieldService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.control.CheckboxControl;
import org.kuali.rice.krad.uif.control.CheckboxGroupControl;
import org.kuali.rice.krad.uif.control.Control;
import org.kuali.rice.krad.uif.control.GroupControl;
import org.kuali.rice.krad.uif.control.HiddenControl;
import org.kuali.rice.krad.uif.control.MultiValueControl;
import org.kuali.rice.krad.uif.control.RadioGroupControl;
import org.kuali.rice.krad.uif.control.SelectControl;
import org.kuali.rice.krad.uif.control.TextAreaControl;
import org.kuali.rice.krad.uif.control.TextControl;
import org.kuali.rice.krad.uif.control.UserControl;
import org.kuali.rice.krad.util.DataTypeUtil;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link DataDictionaryRemoteFieldService} service
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionaryRemoteFieldServiceImpl implements DataDictionaryRemoteFieldService {

    /**
     * @see org.kuali.rice.krad.service.DataDictionaryRemoteFieldService#buildRemotableFieldFromAttributeDefinition(java.lang.String,
     *      java.lang.String)
     */
    public RemotableAttributeField buildRemotableFieldFromAttributeDefinition(String componentClassName,
            String attributeName) {
        AttributeDefinition baseDefinition;
        Class<?> componentClass;
        // try to resolve the component name - if not possible - try to pull the definition from the app mediation service
        try {
            componentClass = (Class<? extends BusinessObject>) Class.forName(componentClassName);
            baseDefinition = getDataDictionaryService().getDataDictionary().getDictionaryObjectEntry(componentClassName)
                    .getAttributeDefinition(attributeName);
        } catch (ClassNotFoundException ex) {
            throw new RiceRuntimeException("Unable to find attribute definition for attribute : " + attributeName);
        }

        RemotableAttributeField.Builder definition = RemotableAttributeField.Builder.create(baseDefinition.getName());

        definition.setLongLabel(baseDefinition.getLabel());
        definition.setShortLabel(baseDefinition.getShortLabel());
        definition.setMaxLength(baseDefinition.getMaxLength());

        if (baseDefinition.isRequired() != null) {
            definition.setRequired(baseDefinition.isRequired());
        }

        definition.setForceUpperCase(baseDefinition.getForceUppercase());

        //set the datatype - needed for successful custom doc searches
        String dataType = DataTypeUtil.determineFieldDataType((Class<? extends BusinessObject>) componentClass,
                attributeName);
        definition.setDataType(DataType.valueOf(dataType.toUpperCase()));

        RemotableAbstractControl.Builder control = createControl(baseDefinition);
        if (control != null) {
            definition.setControl(control);
        }

        RemotableQuickFinder.Builder qf = createQuickFinder(componentClass, attributeName);
        if (qf != null) {
            definition.setWidgets(Collections.<RemotableAbstractWidget.Builder>singletonList(qf));
        }

        return definition.build();
    }

    /**
     * Creates a {@link RemotableAbstractControl} instance based on the control definition within the given
     * attribute definition
     *
     * @param attr - attribute definition instance to pull control from
     * @return RemotableAbstractControl instance or null if one could not be built
     */
    protected RemotableAbstractControl.Builder createControl(AttributeDefinition attr) {
        Control control = attr.getControlField();

        if (control != null) {
            if (control instanceof CheckboxControl) {
                return RemotableCheckbox.Builder.create();
            } else if (control instanceof CheckboxGroupControl) {
                return RemotableCheckboxGroup.Builder.create(getValues(attr));
            } else if (control instanceof HiddenControl) {
                return RemotableHiddenInput.Builder.create();
            } else if (control instanceof SelectControl) {
                RemotableSelect.Builder b = RemotableSelect.Builder.create(getValues(attr));
                b.setMultiple(((SelectControl) control).isMultiple());
                b.setSize(((SelectControl) control).getSize());
            } else if (control instanceof RadioGroupControl) {
                return RemotableRadioButtonGroup.Builder.create(getValues(attr));
            } else if (control instanceof TextControl) {
                final RemotableTextInput.Builder b = RemotableTextInput.Builder.create();
                b.setSize(((TextControl) control).getSize());
                return b;
            } else if (control instanceof UserControl) {
                final RemotableTextInput.Builder b = RemotableTextInput.Builder.create();
                b.setSize(((UserControl) control).getSize());
                return b;
            } else if (control instanceof GroupControl) {
                final RemotableTextInput.Builder b = RemotableTextInput.Builder.create();
                b.setSize(((GroupControl) control).getSize());
                return b;
            } else if (control instanceof TextAreaControl) {
                final RemotableTextarea.Builder b = RemotableTextarea.Builder.create();
                b.setCols(((TextAreaControl) control).getCols());
                b.setRows(((TextAreaControl) control).getRows());
                return b;
            }
        }
        
        return null;
    }

    /**
     * Will first try to retrieve options configured on the control.  If that doesn't return any values then will
     * try to use the optionfinder on the AttributeDefinition.
     *
     * @param attr - AttributeDefinition
     * @return Map of key value pairs
     */
    protected Map<String, String> getValues(AttributeDefinition attr) {
        Control control = attr.getControlField();

        if ((control instanceof MultiValueControl)
                && (((MultiValueControl) control).getOptions() != null)
                && !((MultiValueControl) control).getOptions().isEmpty()) {
            List<KeyValue> keyValues = ((MultiValueControl) control).getOptions();
                    Map<String, String> options = new HashMap<String, String> ();
                    for (KeyValue keyValue : keyValues) {
                        options.put(keyValue.getKey(), keyValue.getValue());
                    }
                    return options;
        } else if (attr.getOptionsFinder() != null) {
            return attr.getOptionsFinder().getKeyLabelMap();
        }

        return Collections.emptyMap();
    }

    /**
     * Builds a {@link RemotableQuickFinder} instance for the given attribute based on determined relationships
     *
     * <p>
     * Uses the {@link DataObjectMetaDataService} to find relationships the given attribute participates in within the
     * given class. If a relationship is not found, the title attribute is also checked to determine if a lookup should
     * be rendered back to the component class itself. If a relationship suitable for lookup is found, the associated
     * field conversions and lookup parameters are built
     * </p>
     *
     * @param componentClass - class that attribute belongs to and should be checked for relationships
     * @param attributeName - name of the attribute to determine quickfinder for
     * @return RemotableQuickFinder.Builder instance for the configured lookup, or null if one could not be found
     */
    protected RemotableQuickFinder.Builder createQuickFinder(Class<?> componentClass, String attributeName) {
        Object sampleComponent;
        try {
            sampleComponent = componentClass.newInstance();
        } catch (InstantiationException e) {
            throw new RiceRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RiceRuntimeException(e);
        }

        String lookupClassName = null;
        Map<String, String> fieldConversions = new HashMap<String, String>();
        Map<String, String> lookupParameters = new HashMap<String, String>();

        DataObjectRelationship relationship = getDataObjectMetaDataService().getDataObjectRelationship(sampleComponent,
                componentClass, attributeName, "", true, true, false);
        if (relationship != null) {
            lookupClassName = relationship.getRelatedClass().getName();

            for (Map.Entry<String, String> entry : relationship.getParentToChildReferences().entrySet()) {
                String fromField = entry.getValue();
                String toField = entry.getKey();
                fieldConversions.put(fromField, toField);
            }

            for (Map.Entry<String, String> entry : relationship.getParentToChildReferences().entrySet()) {
                String fromField = entry.getKey();
                String toField = entry.getValue();

                if (relationship.getUserVisibleIdentifierKey() == null || relationship.getUserVisibleIdentifierKey()
                        .equals(fromField)) {
                    lookupParameters.put(fromField, toField);
                }
            }
        } else {
            // check for title attribute and if match build lookup to component class using pk fields
            String titleAttribute = getDataObjectMetaDataService().getTitleAttribute(componentClass);
            if (StringUtils.equals(titleAttribute, attributeName)) {
                lookupClassName = componentClass.getName();

                List<String> pkAttributes = getDataObjectMetaDataService().listPrimaryKeyFieldNames(componentClass);
                for (String pkAttribute : pkAttributes) {
                    fieldConversions.put(pkAttribute, pkAttribute);
                    if (!StringUtils.equals(pkAttribute, attributeName)) {
                        lookupParameters.put(pkAttribute, pkAttribute);
                    }
                }
            }
        }
        
        if (StringUtils.isNotBlank(lookupClassName)) {
            String baseUrl = getKualiConfigurationService().getPropertyValueAsString(KRADConstants.KRAD_LOOKUP_URL_KEY);
            RemotableQuickFinder.Builder builder = RemotableQuickFinder.Builder.create(baseUrl, lookupClassName);
            builder.setLookupParameters(lookupParameters);
            builder.setFieldConversions(fieldConversions);

            return builder;
        }

        return null;
    }

    protected DataDictionaryService getDataDictionaryService() {
        return KRADServiceLocatorWeb.getDataDictionaryService();
    }

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        return KRADServiceLocatorWeb.getDataObjectMetaDataService();
    }

    protected ConfigurationService getKualiConfigurationService() {
        return CoreApiServiceLocator.getKualiConfigurationService();
    }
}
