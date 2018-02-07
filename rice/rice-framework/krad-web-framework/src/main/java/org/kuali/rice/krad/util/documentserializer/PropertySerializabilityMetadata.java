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

/**
 * This class represents metadata about the serializibility of a property during the document serialization proces..
 */
public interface PropertySerializabilityMetadata {
    /**
     * See docs for the elements of this enum
     */
    public enum PropertySerializability {
        /**
         * Indicates that the property represented by this metadata object should be serialized (i.e. have an open
         * and close XML tag rendered) as well as all of the property's primitives.  It does not mean that all child
         * non-primitive properties should be serialized.  Child non-primitives are only serialized if a call to
         * {@link PropertySerializabilityMetadata#getSerializableSubProperty(String)} returns a non-null result when
         * the child property name is passed in.
         */
        SERIALIZE_OBJECT_AND_ALL_PRIMITIVES,

        /**
         * Indicates that the property represented by this metadata object should be serialized (i.e. have an open
         * and close XML tag rendered).  Child properties (primitive or otherwise) are only serialized if a call to
         * {@link PropertySerializabilityMetadata#getSerializableSubProperty(String)} returns a non-null result when
         * the child property name is passed in.
         */
        SERIALIZE_OBJECT
    }

    /**
     * Returns the serializability of this property.  See {@link PropertySerializability}.
     *
     * @return
     */
    public PropertySerializability getPropertySerializability();

    /**
     * Returns the full path string of the property corresponding to this metadata.
     *
     * @return
     */
    public String getPathString();

    /**
     * Returns metadata bout a child property, if it exists
     *
     * @param childPropertyName the name of a child property, relative to this property (i.e. no .'s in the name)
     * @return null if there is no child property with the specified name, otherwise, metadata about the child
     */
    public PropertySerializabilityMetadata getSerializableChildProperty(String childPropertyName);
}
