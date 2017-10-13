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
package org.kuali.rice.krad.util.documentserializer;

import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityMetadata.PropertySerializability;

import java.util.Collection;
import java.util.Map;

/**
 * This abstract implementation provides a default implementation of {@link #determinePropertyType(Object)}, which should suffice for most
 * use cases.
 *
 */
public abstract class PropertySerializabilityEvaluatorBase implements PropertySerializabilityEvaluator {

    protected PropertySerializerTrie serializableProperties;

    @Override
	public void initializeEvaluatorForDocument(Document document){

	}

    @Override
	public void initializeEvaluatorForDataObject(Object businessObject){

	}

    /**
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator#determinePropertyType(java.lang.Object)
     */
    @Override
    public PropertyType determinePropertyType(Object propertyValue) {
        if (propertyValue == null) {
            return PropertyType.PRIMITIVE;
        }
        if (propertyValue instanceof BusinessObject) {
            return PropertyType.BUSINESS_OBJECT;
        }
        if (propertyValue instanceof Collection) {
            return PropertyType.COLLECTION;
        }
        if (propertyValue instanceof Map) {
            return PropertyType.MAP;
        }
        return PropertyType.PRIMITIVE;
    }

    /**
     * Returns whether a child property of a given containing object should be serialized, based on the metadata provided in the data dictionary.
     *
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator#isPropertySerializable(org.kuali.rice.krad.util.documentserializer.DocumentSerializationState, java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public boolean isPropertySerializable(SerializationState state, Object containingObject, String childPropertyName, Object childPropertyValue) {
        boolean allPropertiesMatched = true;

        PropertySerializabilityMetadata metadata = serializableProperties.getRootPropertySerializibilityMetadata();
        int i = 0;
        for (; i < state.numPropertyElements(); i++) {
            String nextPropertyName = state.getElementName(i);
            PropertySerializabilityMetadata nextMetadata = metadata.getSerializableChildProperty(nextPropertyName);

            if (nextMetadata == null) {
                allPropertiesMatched = false;
                break;
            }
            else {
                // we've found the child... continue searching deeper
                metadata = nextMetadata;
            }
        }

        if (allPropertiesMatched) {
            // complete match, so we determine if the child property is serializable
            return evaluateCompleteMatch(state, containingObject, metadata, childPropertyName, childPropertyValue);
        }
        else {
            // we have a partial match, so we have a different algorithm to determine serializibility
            // partial matches can occur for primitives that contains nested primitives.  For example, if we have a member field named
            // "amount" of type KualiDecimal, then the XML will have to look something like <amount><value>100.00</value></amount>.
            // It is likely that "value" isn't specified in the serializability path, so we need to make inferences about whether "value" is 
            // serializable
            return evaluatePartialMatch(state, i, containingObject, metadata, childPropertyName, childPropertyValue);
        }
    }

    /**
     * Evaluates whether a property is serializable when all properties in the serialization state have been matched up with the properties
     * defined in the data dictionary.
     *
     * @param state
     * @param containingObject
     * @param metadata
     * @param childPropertyName
     * @param childPropertyValue
     * @return whether the child property is serializable
     */
    protected boolean evaluateCompleteMatch(SerializationState state, Object containingObject, PropertySerializabilityMetadata metadata, String childPropertyName, Object childPropertyValue) {
        if (metadata.getPropertySerializability().equals(PropertySerializability.SERIALIZE_OBJECT_AND_ALL_PRIMITIVES)) {
            if (isPrimitiveObject(childPropertyValue)) {
                return true;
            }
        }
        return metadata.getSerializableChildProperty(childPropertyName) != null;
    }

    /**
     * Evaluates whether a property is serializable when only some of the properties in the serialization state have been matched up with the
     * serializable properties specified in the data dictionary.  This often occurs when we determine whether to serialize a primitive of a serialized primitive
     *
     * @param state
     * @param lastMatchedStateIndex the index of the state parameter that represents the last matched property
     * @param containingObject the object containing the child property
     * @param metadata metadata of the last matched property
     * @param childPropertyName the name of the child property that we are going to determine whether it is serializable
     * @param childPropertyValue the value of the child property that we are going to determine whether it is serializable
     * @return whether the child property is serializable
     */
    protected boolean evaluatePartialMatch(SerializationState state, int lastMatchedStateIndex, Object containingObject, PropertySerializabilityMetadata metadata, String childPropertyName, Object childPropertyValue) {

        if (metadata.getPropertySerializability().equals(PropertySerializability.SERIALIZE_OBJECT_AND_ALL_PRIMITIVES)) {
            return isPrimitiveObject(childPropertyValue);
        }
        return false;
    }

    /**
     * Whether the object represents a primitive
     *
     * @param object
     * @return
     */
    protected boolean isPrimitiveObject(Object object) {
        return PropertyType.PRIMITIVE.equals(determinePropertyType(object));
    }

}
