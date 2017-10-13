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

import org.joda.time.DateTime;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A document attribute which contains date/time data.  Construct instances of {@code DocumentAttributeDateTime} using
 * it's builder or the {@link DocumentAttributeFactory}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DocumentAttributeDateTime.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentAttributeDateTime.Constants.TYPE_NAME, propOrder = {
    DocumentAttributeDateTime.Elements.VALUE
})
public final class DocumentAttributeDateTime extends DocumentAttribute {

    @XmlElement(name = Elements.VALUE, required = false)
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime value;

    @SuppressWarnings("unused")
    private DocumentAttributeDateTime() {
        this.value = null;
    }

    private DocumentAttributeDateTime(Builder builder) {
        super(builder.getName());
        this.value = builder.getValue();
    }

    @Override
    public DateTime getValue() {
        return value;
    }

    @Override
    public DocumentAttributeDataType getDataType() {
        return DocumentAttributeDataType.DATE_TIME;
    }

    /**
     * A builder implementation which allows for construction of a {@code DocumentAttributeDateTime}.
     */
    public static final class Builder extends AbstractBuilder<DateTime> {

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
            return DocumentAttributeDataType.DATE_TIME;
        }

        @Override
        public DocumentAttributeDateTime build() {
            return new DocumentAttributeDateTime(this);
        }
        
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentAttributeDateTime";
        final static String TYPE_NAME = "DocumentAttributeDateTimeType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String VALUE = "value";
    }

}
