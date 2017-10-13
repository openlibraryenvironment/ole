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
package org.kuali.rice.kim.api.identity.principal;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

@XmlRootElement(name = EntityNamePrincipalName.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityNamePrincipalName.Constants.TYPE_NAME, propOrder = {
    EntityNamePrincipalName.Elements.DEFAULT_NAME,
    EntityNamePrincipalName.Elements.PRINCIPAL_NAME,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class EntityNamePrincipalName extends AbstractDataTransferObject {
    @XmlElement(name = Elements.PRINCIPAL_NAME, required = false)
    private final String principalName;
    @XmlElement(name = Elements.DEFAULT_NAME, required = false)
    private final EntityName defaultName;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;


    private EntityNamePrincipalName() {
        this.principalName = null;
        this.defaultName = null;
    }

    private EntityNamePrincipalName(Builder builder) {
        this.principalName = builder.getPrincipalName();
        this.defaultName = builder.getDefaultName() == null ? null : builder.getDefaultName().build();
    }

    public String getPrincipalName() {
        return principalName;
    }

    public EntityName getDefaultName() {
        return defaultName;
    }
    /**
     * A builder which can be used to construct {@link EntityDefault} instances.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder
    {
        private String principalName;
        private EntityName.Builder defaultName;

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String principalName, EntityName.Builder defaultName) {
            Builder builder = new Builder();
            builder.setPrincipalName(principalName);
            builder.setDefaultName(defaultName);
            return builder;
        }

        public static Builder create(EntityNamePrincipalName immutable) {
            if (immutable == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = new Builder();
            if (immutable.getDefaultName() != null) {
                builder.setDefaultName(EntityName.Builder.create(immutable.getDefaultName()));
            }
            return builder;
        }

        public String getPrincipalName() {
            return principalName;
        }

        public void setPrincipalName(String principalName) {
            this.principalName = principalName;
        }

        public EntityName.Builder getDefaultName() {
            return defaultName;
        }

        public void setDefaultName(EntityName.Builder defaultName) {
            this.defaultName = defaultName;
        }

        public EntityNamePrincipalName build() {
            return new EntityNamePrincipalName(this);
        }

    }

    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityNamePrincipalName";
        final static String TYPE_NAME = "EntityNamePrincipalNameType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {
        final static String DEFAULT_NAME = "defaultName";
        final static String PRINCIPAL_NAME = "principalName";
    }

    public static class Cache {
    	public final static String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + EntityNamePrincipalName.Constants.TYPE_NAME;
    }
}
