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
import java.util.Iterator;
import java.util.List;

/**
 * A node in the trie.
 *
 */
public class PropertySerializerTrieNode implements PropertySerializabilityMetadata {
    private String pathString;
    private String propertyNameComponent;
    private PropertySerializability propertySerializability;

    private List<PropertySerializerTrieNode> childNodes;

    public PropertySerializerTrieNode(String pathString, String propertyNameComponent) {
        this.pathString = pathString;
        this.propertyNameComponent = propertyNameComponent;
        this.childNodes = null;
        this.propertySerializability = PropertySerializability.SERIALIZE_OBJECT;
    }

    public void addChildNode(PropertySerializerTrieNode child) {
        if (childNodes == null) {
            childNodes = new ArrayList<PropertySerializerTrieNode>();
        }
        childNodes.add(child);
    }

    /**
     * The name of this property, relative to the parent node (i.e. the child node name relative to its parents).
     *
     * @return
     */
    public String getPropertyNameComponent() {
        return propertyNameComponent;
    }

    /**
     * Retrieves the child node with the given name
     *
     * @param propertyNameComponent
     * @return
     */
    public PropertySerializerTrieNode getChildNode(String propertyNameComponent) {
        if (childNodes == null) {
            return null;
        }
        for (int i = 0; i < childNodes.size(); i++) {
            PropertySerializerTrieNode childNode = childNodes.get(i);
            if (childNode.getPropertyNameComponent().equals(propertyNameComponent)) {
                return childNode;
            }
        }
        return null;
    }

    /**
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityMetadata#getSerializableChildProperty(java.lang.String)
     */
    public PropertySerializabilityMetadata getSerializableChildProperty(String propertyNameComponent) {
        return getChildNode(propertyNameComponent);
    }

    /**
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityMetadata#getPathString()
     */
    public String getPathString() {
        return pathString;
    }

    /**
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityMetadata#getPropertySerializability()
     */
    public PropertySerializability getPropertySerializability() {
        return propertySerializability;
    }

    /**
     * Marks that all primitives of this object will be serialized.
     */
    public void setPropertySerializabilityToObjectAndAllPrimitives() {
        this.propertySerializability = PropertySerializability.SERIALIZE_OBJECT_AND_ALL_PRIMITIVES;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Path String: ").append(pathString).append(" Name component: ").append(propertyNameComponent);
        if (childNodes == null || childNodes.isEmpty()) {
            buf.append(" No child nodes.");
        }
        else {
            buf.append(" Child nodes: ");
            for (Iterator<PropertySerializerTrieNode> i = childNodes.iterator(); i.hasNext();) {
                buf.append(i.next().getPropertyNameComponent());
                if (i.hasNext()) {
                    buf.append(", ");
                }
            }
        }
        return super.toString();
    }
}
