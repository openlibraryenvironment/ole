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
package org.kuali.rice.ken.api.notification;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = NotificationContentType.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = NotificationContentType.Constants.TYPE_NAME, propOrder = {
        NotificationContentType.Elements.NAME,
        NotificationContentType.Elements.VERSION,
        NotificationContentType.Elements.CURRENT,
        NotificationContentType.Elements.DESCRIPTION,
        NotificationContentType.Elements.NAMESPACE,
        NotificationContentType.Elements.XSD,
        NotificationContentType.Elements.XSL,
        NotificationContentType.Elements.ID,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class NotificationContentType
        extends AbstractDataTransferObject
        implements NotificationContentTypeContract
{

    @XmlElement(name = Elements.NAME, required = false)
    private final String name;
    @XmlElement(name = Elements.VERSION, required = false)
    private final Integer version;
    @XmlElement(name = Elements.CURRENT, required = false)
    private final boolean current;
    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;
    @XmlElement(name = Elements.NAMESPACE, required = false)
    private final String namespace;
    @XmlElement(name = Elements.XSD, required = false)
    private final String xsd;
    @XmlElement(name = Elements.XSL, required = false)
    private final String xsl;
    @XmlElement(name = Elements.ID, required = false)
    private final Long id;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private NotificationContentType() {
        this.name = null;
        this.version = null;
        this.current = false;
        this.description = null;
        this.namespace = null;
        this.xsd = null;
        this.xsl = null;
        this.id = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private NotificationContentType(Builder builder) {
        this.name = builder.getName();
        this.version = builder.getVersion();
        this.current = builder.isCurrent();
        this.description = builder.getDescription();
        this.namespace = builder.getNamespace();
        this.xsd = builder.getXsd();
        this.xsl = builder.getXsl();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getVersion() {
        return this.version;
    }

    @Override
    public boolean isCurrent() {
        return this.current;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getXsd() {
        return this.xsd;
    }

    @Override
    public String getXsl() {
        return this.xsl;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }


    /**
     * A builder which can be used to construct {@link NotificationContentType} instances.  Enforces the constraints of the {@link NotificationContentTypeContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, NotificationContentTypeContract
    {

        private String name;
        private Integer version;
        private boolean current;
        private String description;
        private String namespace;
        private String xsd;
        private String xsl;
        private Long id;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(NotificationContentTypeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            builder.setName(contract.getName());
            builder.setVersion(contract.getVersion());
            builder.setCurrent(contract.isCurrent());
            builder.setDescription(contract.getDescription());
            builder.setNamespace(contract.getNamespace());
            builder.setXsd(contract.getXsd());
            builder.setXsl(contract.getXsl());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public NotificationContentType build() {
            return new NotificationContentType(this);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Integer getVersion() {
            return this.version;
        }

        @Override
        public boolean isCurrent() {
            return this.current;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getNamespace() {
            return this.namespace;
        }

        @Override
        public String getXsd() {
            return this.xsd;
        }

        @Override
        public String getXsl() {
            return this.xsl;
        }

        @Override
        public Long getId() {
            return this.id;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public void setXsd(String xsd) {
            this.xsd = xsd;
        }

        public void setXsl(String xsl) {
            this.xsl = xsl;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "notificationContentType";
        final static String TYPE_NAME = "NotificationContentTypeType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String NAME = "name";
        final static String VERSION = "version";
        final static String CURRENT = "current";
        final static String DESCRIPTION = "description";
        final static String NAMESPACE = "namespace";
        final static String XSD = "xsd";
        final static String XSL = "xsl";
        final static String ID = "id";

    }

}