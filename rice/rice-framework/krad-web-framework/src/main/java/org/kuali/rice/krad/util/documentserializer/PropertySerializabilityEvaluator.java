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

import org.kuali.rice.krad.document.Document;

/**
 * Specifies an implementation used during document workflow XML serialization that
 * will be able to determine whether a specific property is serializable
 *
 */
public interface PropertySerializabilityEvaluator {

    /**
     * Initializes the evaluator so that calls to {@link #isPropertySerializable(DocumentSerializationState, Object, String, Object)} and
     * {@link #determinePropertyType(Object)} will function properly
     *
     * @param document the document instance
     */
    public void initializeEvaluatorForDocument(Document document);

    public void initializeEvaluatorForDataObject(Object businessObject);

    /**
     * Determines whether a child property of an object is serializable.
     *
     * @param state Information about the properties that have been serialized so far
     * @param containingObject The object containing the reference to childPropertyValue
     * @param childPropertyName The name property to determine whether to serialize, relative to containingObject (i.e. not a nested attribute)
     * @param childPropertyValue If serializable, this property would be serialized by the serializer service.
     * @return
     */
    public boolean isPropertySerializable(SerializationState state, Object containingObject, String childPropertyName, Object childPropertyValue);

    /**
     * Determines the type of a object
     *
     * @param object
     * @return
     */
    public PropertyType determinePropertyType(Object object);
}
