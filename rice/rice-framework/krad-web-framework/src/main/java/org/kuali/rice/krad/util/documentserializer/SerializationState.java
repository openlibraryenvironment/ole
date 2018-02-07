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

import java.util.ArrayList;
import java.util.List;


/**
 * This object keeps track of most of the open tags while a document is serialized.  Note that instances of this class
 * may not necessarily hold all open tags of a document while it is being serialized.  For example, tags enclosing list elements
 * and map entries are not contained within here.  See {@link DocumentSerializerServiceImpl} to determine when this object's state
 * is modified.
 *
 * This class's manipulators behave much like a stack, but it has random access characteristics like an array.
 */
public class SerializationState {
    protected class SerializationPropertyElement {
        private String elementName;
        private PropertyType propertyType;

        public SerializationPropertyElement(String elementName, PropertyType propertyType) {
            this.elementName = elementName;
            this.propertyType = propertyType;
        }

        public String getElementName() {
            return this.elementName;
        }

        public PropertyType getPropertyType() {
            return this.propertyType;
        }
    }

    private List<SerializationPropertyElement> pathElements;

    public SerializationState(){
        pathElements = new ArrayList<SerializationPropertyElement>();
    }

    /**
     * The number of property elements in this state object.
     *
     * @return
     */
    public int numPropertyElements() {
        return pathElements.size();
    }

    /**
     * Adds an additional state element into this object.
     *
     * @param elementName
     * @param propertyType the type of the property when it was serialized
     */
    public void addSerializedProperty(String elementName, PropertyType propertyType) {
        SerializationPropertyElement serializationPropertyElement = new SerializationPropertyElement(elementName, propertyType);
        pathElements.add(serializationPropertyElement);
    }

    /**
     * Removes the last added serialized property
     *
     */
    public void removeSerializedProperty() {
        pathElements.remove(pathElements.size() - 1);
    }

    /**
     * Retrieves the element name of the state element.  A parameter value of 0 represents the first element that was added
     * by calling {@link #addSerializedProperty(String, PropertyType)} that hasn't been removed, and a value of
     * {@link #numPropertyElements()} - 1 represents the element last added that hasn't been removed.
     *
     * @param propertyIndex most be between 0 and the value returned by {@link #numPropertyElements()} - 1
     * @return
     */
    public String getElementName(int propertyIndex) {
        return pathElements.get(propertyIndex).getElementName();
    }

    /**
     * Retrieves the property type of the state element.  A parameter value of 0 represents the first element that was added
     * by calling {@link #addSerializedProperty(String, PropertyType)} that hasn't been removed, and a value of
     * {@link #numPropertyElements()} - 1 represents the element last added that hasn't been removed.
     *
     * @param propertyIndex most be between 0 and the value returned by {@link #numPropertyElements()} - 1
     * @return
     */
    public PropertyType getPropertyType(int propertyIndex) {
        return pathElements.get(propertyIndex).getPropertyType();
    }
}
