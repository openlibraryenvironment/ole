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
package org.kuali.rice.kew.framework.document.search;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An immutable data transfer object used to hold a list of {@link RemotableAttributeField} objects and the name of the
 * {@link org.kuali.rice.kew.framework.document.attribute.SearchableAttribute} from which the fields are derived.  This
 * is essentially used as a grouping mechanism in order to identify which fields are sourced from which attributes.
 *
 * <p>Since this class serves primarily as a simple wrapper for use by {@link DocumentSearchCriteriaConfiguration},
 * it does not have a builder, only a single static create method that is used for constructing instances of it.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = AttributeFields.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AttributeFields.Constants.TYPE_NAME, propOrder = {
    AttributeFields.Elements.ATTRIBUTE_NAME,
    AttributeFields.Elements.REMOTABLE_ATTRIBUTE_FIELDS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class AttributeFields extends AbstractDataTransferObject {

	@XmlElement(name = Elements.ATTRIBUTE_NAME, required = true)
    private final String attributeName;

    @XmlElementWrapper(name = Elements.REMOTABLE_ATTRIBUTE_FIELDS, required = true)
    @XmlElement(name = Elements.REMOTABLE_ATTRIBUTE_FIELD, required = false)
    private final List<RemotableAttributeField> remotableAttributeFields;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private AttributeFields() {
        this.attributeName = null;
        this.remotableAttributeFields = null;
    }

    private AttributeFields(String attributeName, List<RemotableAttributeField> remotableAttributeFields) {
        if (StringUtils.isBlank(attributeName)) {
            throw new IllegalArgumentException("attributeName was blank or null");
        }
        if (remotableAttributeFields == null) {
            throw new IllegalArgumentException("attributeFields was blank or null");
        }
        this.attributeName = attributeName;
        this.remotableAttributeFields = Collections.unmodifiableList(new ArrayList<RemotableAttributeField>(remotableAttributeFields));
    }

    /**
     * Construct a new instance of {@code AttributeFields} with the given attribute name and list of remotable attribute
     * fields.
     *
     * @param attributeName the name of the attribute, must not be a null or blank value
     * @param attributeFields the remotable attribute fields to associate with the given attribute name
     *
     * @return a new AttributeFields instance containing the given values
     * @throws IllegalArgumentException if the given attributeName is blank or null
     */
    public static AttributeFields create(String attributeName, List<RemotableAttributeField> attributeFields) {
        if (attributeFields == null) {
            attributeFields = Collections.emptyList();
        }
        return new AttributeFields(attributeName, attributeFields);
    }

    /**
     * Returns the name of the searchable attribute associated with this attribute fields instance.  Should never return
     * a null or blank value.
     *
     * @return the searchable attribute name of this instance
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Returns a list of remotable attribute fields associated with the searchable attribute name of this instance.
     * This should never return a null reference, though the list returned can be empty.
     *
     * @return a list of remotable attribute fields associated with this instance
     */
    public List<RemotableAttributeField> getRemotableAttributeFields() {
        return remotableAttributeFields;
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "attributeFields";
        final static String TYPE_NAME = "AttributeFieldsType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ATTRIBUTE_NAME = "attributeName";
        final static String REMOTABLE_ATTRIBUTE_FIELDS = "remotableAttributeFields";
        final static String REMOTABLE_ATTRIBUTE_FIELD = "remotableAttributeField";
    }

}
