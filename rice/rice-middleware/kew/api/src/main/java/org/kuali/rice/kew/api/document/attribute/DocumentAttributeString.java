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
package org.kuali.rice.kew.api.document.attribute;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A document attribute which contains character data.  Construct instances of {@code DocumentAttributeString} using
 * it's builder or the {@link DocumentAttributeFactory}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentAttributeString.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentAttributeString.Constants.TYPE_NAME, propOrder = {
    DocumentAttributeString.Elements.VALUE
})
public final class DocumentAttributeString extends DocumentAttribute {

    @XmlElement(name = Elements.VALUE, required = false)
    private final String value;

    @SuppressWarnings("unused")
    private DocumentAttributeString() {
        this.value = null;
    }

    private DocumentAttributeString(Builder builder) {
        super(builder.getName());
        this.value = builder.getValue();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public DocumentAttributeDataType getDataType() {
        return DocumentAttributeDataType.STRING;
    }

    /**
     * A builder implementation which allows for construction of a {@code DocumentAttributeString}.
     */
    public static final class Builder extends AbstractBuilder<String> {

        private Builder(String name) {
            super(name);
        }

        /**
         * Create a builder for the document attribute using the given attribute name.
         *
         * @param name the name of the document attribute which should be built by this builder, should never be a
         * null or blank value
         * @return a builder instance initialized with the given attribute name
         */
        public static Builder create(String name) {
            return new Builder(name);
        }

        @Override
        public DocumentAttributeDataType getDataType() {
            return DocumentAttributeDataType.STRING;
        }

        @Override
        public DocumentAttributeString build() {
            return new DocumentAttributeString(this);
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentAttributeString";
        final static String TYPE_NAME = "DocumentAttributeStringType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String VALUE = "value";
    }

}
