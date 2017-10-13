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
package org.kuali.rice.kim.api.type;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.w3c.dom.Element;

@XmlRootElement(name = KimAttributeField.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = KimAttributeField.Constants.TYPE_NAME, propOrder = {
    KimAttributeField.Elements.ATTRIBUTE_FIELD,
    KimAttributeField.Elements.ID,
    KimAttributeField.Elements.UNIQUE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class KimAttributeField
    extends AbstractDataTransferObject
    implements KimAttributeFieldContract
{

    @XmlElement(name = Elements.ATTRIBUTE_FIELD, required = true)
    private final RemotableAttributeField attributeField;
    @XmlElement(name = Elements.ID, required = true)
    private final String id;
    @XmlElement(name = Elements.UNIQUE, required = false)
    private final boolean unique;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private KimAttributeField() {
        this.attributeField = null;
        this.id = null;
        this.unique = false;
    }

    private KimAttributeField(Builder builder) {
        this.attributeField = builder.getAttributeField().build();
        this.id = builder.getId();
        this.unique = builder.isUnique();
    }

    @Override
    public RemotableAttributeField getAttributeField() {
        return this.attributeField;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isUnique() {
        return this.unique;
    }

    /**
     * Utility method to search a collection of attribute fields and returns
     * a field for a give attribute name.
     *
     * @param attributeName the name of the attribute to search for.  Cannot be blank or null.
     * @param fields cannot be null.
     *
     * @return the attribute field or null if not found.
     */
    public static KimAttributeField findAttribute(String attributeName, Collection<KimAttributeField> fields) {
        if (StringUtils.isBlank(attributeName)) {
            throw new IllegalArgumentException("attributeName is blank");
        }

        if (fields == null) {
            throw new IllegalArgumentException("errors is null");
        }

        for (KimAttributeField field : fields) {
            if (attributeName.equals(field.getAttributeField().getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * A builder which can be used to construct {@link KimAttributeField} instances.  Enforces the constraints of the {@link KimAttributeFieldContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, KimAttributeFieldContract
    {

        private RemotableAttributeField.Builder attributeField;
        private String id;
        private boolean unique;

        private Builder(RemotableAttributeField.Builder attributeField, String id) {
            setAttributeField(attributeField);
            setId(id);
        }

        public static Builder create(RemotableAttributeField.Builder attributeField, String id) {
            return new Builder(attributeField, id);
        }

        public static Builder create(KimAttributeFieldContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder b = create(RemotableAttributeField.Builder.create(contract.getAttributeField()), contract.getId());
            b.setUnique(contract.isUnique());
            return b;
        }

        public KimAttributeField build() {
            return new KimAttributeField(this);
        }

        @Override
        public RemotableAttributeField.Builder getAttributeField() {
            return this.attributeField;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public boolean isUnique() {
            return this.unique;
        }

        public void setAttributeField(RemotableAttributeField.Builder attributeField) {
            if (attributeField == null) {
                throw new IllegalArgumentException("attributeField is null");
            }

            this.attributeField = attributeField;
        }

        public void setId(String id) {
            if (StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "kimAttributeField";
        final static String TYPE_NAME = "KimAttributeFieldType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ATTRIBUTE_FIELD = "attributeField";
        final static String ID = "id";
        final static String UNIQUE = "unique";
    }

}