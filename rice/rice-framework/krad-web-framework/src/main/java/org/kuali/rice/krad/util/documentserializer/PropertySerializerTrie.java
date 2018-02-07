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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.StringTokenizer;

/**
 * This is a implementation of a trie/prefix tree of that contains metadata about property serializability
 * during the document serialization process.
 *
 */
public class PropertySerializerTrie {
    private static final String PROPERTY_NAME_COMPONENT_SEPARATOR = ".";
    private PropertySerializerTrieNode rootNode;

    public PropertySerializerTrie() {
        rootNode = new PropertySerializerTrieNode(KRADConstants.EMPTY_STRING, KRADConstants.EMPTY_STRING);
    }

    /**
     * Registers a new serializable property so that all of its primitives are serialized.  All nesting properties
     * will be serialized only to render open/close tags to maintain consistency with the document structure, unless
     * they are registered as well.
     *
     * For example, if only property "document.a.b" is registered, then the XML will look like the following:
     *
     * &lt;document&gt;
     *     &lt;a&gt;
     *         &lt;b&gt;
     *             &lt;primitiveOfB&gt;valueOfPrimitive&lt;/primitiveOfB&gt;
     *         &lt;/b&gt;
     *     &lt;/a&gt;
     * &lt;/document&gt;
     *
     * That is, primitives of "document" and "document.a" will not be serialized unless those property strings are registered.
     *
     * @param propertyName
     * @param setPropertySerializabilityToObjectAndAllPrimitivesForAll
     */
    public void addSerializablePropertyName(String propertyName, boolean setPropertySerializabilityToObjectAndAllPrimitivesForAll) {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null attribute name specified");
        }
        if (StringUtils.isBlank(propertyName)) {
            rootNode.setPropertySerializabilityToObjectAndAllPrimitives();
        }
        else {
            StringTokenizer tok = new StringTokenizer(propertyName, PROPERTY_NAME_COMPONENT_SEPARATOR, false);
            StringBuilder buf = new StringBuilder();

            if(setPropertySerializabilityToObjectAndAllPrimitivesForAll)
            	rootNode.setPropertySerializabilityToObjectAndAllPrimitives();

            PropertySerializerTrieNode currentNode = rootNode;
            while (tok.hasMoreTokens()) {
                String attributeNameComponent = tok.nextToken();
                validateAttributeNameComponent(attributeNameComponent);

                buf.append(attributeNameComponent);

                // create a new node or retrieve existing node for this name component
                PropertySerializerTrieNode childNode = currentNode.getChildNode(attributeNameComponent);
                if (childNode == null) {
                    childNode = new PropertySerializerTrieNode(buf.toString(), attributeNameComponent);
                    currentNode.addChildNode(childNode);
                }

                if (tok.hasMoreTokens()) {
                    buf.append(PROPERTY_NAME_COMPONENT_SEPARATOR);
                }
                currentNode = childNode;
                if(setPropertySerializabilityToObjectAndAllPrimitivesForAll)
                	currentNode.setPropertySerializabilityToObjectAndAllPrimitives();
            }

            currentNode.setPropertySerializabilityToObjectAndAllPrimitives();
        }
    }

    /**
     * Retrieves the metadata about the given property name
     *
     * @param propertyName
     * @return
     */
    public PropertySerializabilityMetadata getPropertySerializabilityMetadata(String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null attribute name specified");
        }
        if (StringUtils.isBlank(propertyName)) {
            return rootNode;
        }
        else {
            StringTokenizer tok = new StringTokenizer(propertyName, PROPERTY_NAME_COMPONENT_SEPARATOR, false);

            PropertySerializerTrieNode currentNode = rootNode;
            while (tok.hasMoreTokens()) {
                String attributeNameComponent = tok.nextToken();
                validateAttributeNameComponent(attributeNameComponent);

                // retrieve the child node for this name component
                PropertySerializerTrieNode childNode = currentNode.getChildNode(attributeNameComponent);
                if (childNode == null) {
                    // we didn't find a child node, so we know that something wasn't added with the prefix we're processing
                    return null;
                }
                else {
                    // keep going until we hit the last token, at which case we'll get out of this loop
                    currentNode = childNode;
                }
            }
            return currentNode;
        }
    }

    /**
     * Returns the root node of the trie
     *
     * @return
     */
    public PropertySerializabilityMetadata getRootPropertySerializibilityMetadata() {
        return rootNode;
    }

    protected void validateAttributeNameComponent(String attributeNameComponent) {
        if (StringUtils.isBlank(attributeNameComponent)) {
            throw new IllegalArgumentException("Blank attribute name component specified");
        }
    }
}
