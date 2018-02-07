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
package org.kuali.rice.krad.datadictionary.validation;

import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntry;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntryBase;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows a dictionary object to expose information about its fields / attributes, including the values of
 * those fields, with some guidance from the DataDictionaryEntry object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DictionaryObjectAttributeValueReader extends BaseAttributeValueReader {

    protected Object object;
    protected DataDictionaryEntry entry;

    protected BeanWrapper beanWrapper;

    private String attributePath;

    public DictionaryObjectAttributeValueReader(Object object, String entryName, DataDictionaryEntry entry) {
        this.object = object;
        this.entry = entry;
        this.entryName = entryName;

        if (object != null) {
            beanWrapper = new BeanWrapperImpl(object);
        }
    }

    public DictionaryObjectAttributeValueReader(Object object, String entryName, DataDictionaryEntry entry,
            String attributePath) {
        this(object, entryName, entry);
        this.attributePath = attributePath;
    }

    @Override
    public Constrainable getDefinition(String attrName) {
        return entry != null ? entry.getAttributeDefinition(attrName) : null;
    }

    @Override
    public List<Constrainable> getDefinitions() {
        if (entry instanceof DataDictionaryEntryBase) {
            DataDictionaryEntryBase entryBase = (DataDictionaryEntryBase) entry;
            List<Constrainable> definitions = new ArrayList<Constrainable>();
            List<AttributeDefinition> attributeDefinitions = entryBase.getAttributes();
            definitions.addAll(attributeDefinitions);
            return definitions;
        }

        return null;
    }

    @Override
    public Constrainable getEntry() {
        if (entry instanceof Constrainable) {
            return (Constrainable) entry;
        }

        return null;
    }

    @Override
    public String getLabel(String attrName) {
        AttributeDefinition attributeDefinition = entry != null ? entry.getAttributeDefinition(attrName) : null;
        return attributeDefinition != null ? attributeDefinition.getLabel() : attrName;
    }

    @Override
    public Object getObject() {
        return this.object;
    }

    @Override
    public String getPath() {
        String path = ValidationUtils.buildPath(attributePath, attributeName);
        return path != null ? path : "";
    }

    @Override
    public Class<?> getType(String attrName) {
        PropertyDescriptor propertyDescriptor = beanWrapper.getPropertyDescriptor(attrName);

        return propertyDescriptor.getPropertyType();
    }

    @Override
    public boolean isReadable() {
        return beanWrapper.isReadableProperty(attributeName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X getValue() throws AttributeValidationException {
        Object value = getValue(attributeName);
        return (X) value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X getValue(String attrName) throws AttributeValidationException {
        X attributeValue = null;

        Exception e = null;
        try {
            attributeValue = (X) beanWrapper.getPropertyValue(attrName);
        } catch (IllegalArgumentException iae) {
            e = iae;
        } catch (InvalidPropertyException ipe) {
            //just return null
        }

        if (e != null) {
            throw new AttributeValidationException(
                    "Unable to lookup attribute value by name (" + attrName + ") using introspection", e);
        }

        //			JLR : KS has code to handle dynamic attributes -- not sure whether this is really needed anymore if we're actually relying on types
        //            // Extract dynamic attributes
        //            if(DYNAMIC_ATTRIBUTE.equals(propName)) {
        //                dataMap.putAll((Map<String, String>)value);
        //            } else {
        //				dataMap.put(propName, value);
        //            }

        return attributeValue;
    }

    /**
     * @return false if parent attribute exists and is not null, otherwise returns true.
     */
    public boolean isParentAttributeNull() {
        boolean isParentNull = true;

        if (isNestedAttribute()) {
            String[] pathTokens = attributeName.split("\\.");

            isParentNull = false;
            String parentPath = "";
            for (int i = 0; (i < pathTokens.length - 1) && !isParentNull; i++) {
                parentPath += pathTokens[i];
                isParentNull = beanWrapper.getPropertyValue(parentPath) == null;
                parentPath += ".";
            }
        }

        return isParentNull;
    }

    public boolean isNestedAttribute() {
        return (attributePath != null || attributeName.contains("."));
    }

    @Override
    public AttributeValueReader clone() {
        DictionaryObjectAttributeValueReader readerClone = new DictionaryObjectAttributeValueReader(this.object,
                this.entryName, this.entry, this.attributePath);
        readerClone.setAttributeName(this.attributeName);

        return readerClone;
    }

}