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
import java.math.BigInteger;

/**
 * A document attribute which contains integer data.  Construct instances of {@code DocumentAttributeInteger} using
 * it's builder or the {@link DocumentAttributeFactory}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentAttributeInteger.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentAttributeInteger.Constants.TYPE_NAME, propOrder = {
    DocumentAttributeInteger.Elements.VALUE
})
public final class DocumentAttributeInteger extends DocumentAttribute {

    @XmlElement(name = Elements.VALUE, required = false)
    private final BigInteger value;

    @SuppressWarnings("unused")
    private DocumentAttributeInteger() {
        this.value = null;
    }

    private DocumentAttributeInteger(Builder builder) {
        super(builder.getName());
        this.value = builder.getValue();
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    @Override
    public DocumentAttributeDataType getDataType() {
        return DocumentAttributeDataType.INTEGER;
    }

    /**
     * A builder implementation which allows for construction of a {@code DocumentAttributeInteger}.
     */
    public static final class Builder extends AbstractBuilder<BigInteger> {

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
            return DocumentAttributeDataType.INTEGER;
        }

        @Override
        public DocumentAttributeInteger build() {
            return new DocumentAttributeInteger(this);
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentAttributeInteger";
        final static String TYPE_NAME = "DocumentAttributeIntegerType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String VALUE = "value";
    }

}
