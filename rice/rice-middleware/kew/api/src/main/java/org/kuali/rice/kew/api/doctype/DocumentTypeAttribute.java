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
package org.kuali.rice.kew.api.doctype;

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
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentTypeAttribute.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentTypeAttribute.Constants.TYPE_NAME, propOrder = {
        DocumentTypeAttribute.Elements.ID,
        DocumentTypeAttribute.Elements.RULE_ATTRIBUTE,
        DocumentTypeAttribute.Elements.DOCUMENT_TYPE_ID,
        DocumentTypeAttribute.Elements.ORDER_INDEX,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentTypeAttribute
        extends AbstractDataTransferObject
        implements DocumentTypeAttributeContract
{
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.RULE_ATTRIBUTE, required = true)
    private final ExtensionDefinition ruleAttribute;
    @XmlElement(name = Elements.DOCUMENT_TYPE_ID, required = true)
    private final String documentTypeId;
    @XmlElement(name = Elements.ORDER_INDEX, required = false)
    private final int orderIndex;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     *
     */
    private DocumentTypeAttribute() {
        this.ruleAttribute = null;
        this.documentTypeId = null;
        this.orderIndex = 0;
        this.id = null;
    }

    private DocumentTypeAttribute(Builder builder) {
        this.ruleAttribute = builder.getRuleAttribute().build();
        this.documentTypeId = builder.getDocumentTypeId();
        this.orderIndex = builder.getOrderIndex();
        this.id = builder.getId();
    }

    @Override
    public ExtensionDefinition getRuleAttribute() {
        return this.ruleAttribute;
    }

    @Override
    public String getDocumentTypeId() {
        return this.documentTypeId;
    }

    @Override
    public int getOrderIndex() {
        return this.orderIndex;
    }

    @Override
    public String getId() {
        return this.id;
    }


    /**
     * A builder which can be used to construct {@link DocumentTypeAttribute} instances.  Enforces the constraints of the {@link DocumentTypeAttributeContract}.
     *
     */
    public final static class Builder
            implements Serializable, ModelBuilder, DocumentTypeAttributeContract
    {

        private ExtensionDefinition.Builder ruleAttribute;
        private String documentTypeId;
        private int orderIndex;
        private String id;

        private Builder(String documentTypeId, ExtensionDefinition.Builder ruleAttribute) {
            setDocumentTypeId(documentTypeId);
            setRuleAttribute(ruleAttribute);
        }

        public static Builder create(String documentTypeId, ExtensionDefinition.Builder ruleAttribute) {
            return new Builder(documentTypeId, ruleAttribute);
        }

        public static Builder create(DocumentTypeAttributeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getDocumentTypeId(), ExtensionDefinition.Builder.create(contract.getRuleAttribute()));
            builder.setOrderIndex(contract.getOrderIndex());
            builder.setId(contract.getId());
            return builder;
        }

        public DocumentTypeAttribute build() {
            return new DocumentTypeAttribute(this);
        }

        @Override
        public ExtensionDefinition.Builder getRuleAttribute() {
            return this.ruleAttribute;
        }

        @Override
        public String getDocumentTypeId() {
            return this.documentTypeId;
        }

        @Override
        public int getOrderIndex() {
            return this.orderIndex;
        }

        @Override
        public String getId() {
            return this.id;
        }

        public void setRuleAttribute(ExtensionDefinition.Builder ruleAttribute) {
            if (ruleAttribute == null) {
                throw new IllegalArgumentException("ruleAttribute is null");
            }
            this.ruleAttribute = ruleAttribute;
        }

        public void setDocumentTypeId(String documentTypeId) {
            if (StringUtils.isEmpty(documentTypeId)) {
                throw new IllegalArgumentException("documentTypeId is empty");
            }
            this.documentTypeId = documentTypeId;
        }

        public void setOrderIndex(int orderIndex) {
            this.orderIndex = orderIndex;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

    }


    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "documentTypeAttribute";
        final static String TYPE_NAME = "DocumentTypeAttributeType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        final static String RULE_ATTRIBUTE = "ruleAttribute";
        final static String DOCUMENT_TYPE_ID = "documentTypeId";
        final static String ORDER_INDEX = "orderIndex";
        final static String ID = "id";

    }

}