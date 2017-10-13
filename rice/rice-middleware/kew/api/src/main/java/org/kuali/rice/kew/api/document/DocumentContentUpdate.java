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
package org.kuali.rice.kew.api.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.w3c.dom.Element;

/**
 * Defines an update to document content on a particular workflow document.
 * Contains general application content as well as a list of attribute
 * definitions and searchable definitions.  When passed to the appropriate
 * workflow services to perform an update on document content, if any of the
 * internal content or definitions on this object have not been set then they
 * will not be updated.  This allows for this data structure to be used to only
 * update the portion of the document content that is desired to be updated.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = DocumentContentUpdate.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentContentUpdate.Constants.TYPE_NAME, propOrder = {
    DocumentContentUpdate.Elements.APPLICATION_CONTENT,
    DocumentContentUpdate.Elements.ATTRIBUTE_CONTENT,
    DocumentContentUpdate.Elements.SEARCHABLE_CONTENT,
    DocumentContentUpdate.Elements.ATTRIBUTE_DEFINITIONS,
    DocumentContentUpdate.Elements.SEARCHABLE_DEFINITIONS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentContentUpdate extends AbstractDataTransferObject {

	private static final long serialVersionUID = -7386661044232391889L;

	@XmlElement(name = Elements.APPLICATION_CONTENT, required = false)
	private final String applicationContent;
	
	@XmlElement(name = Elements.ATTRIBUTE_CONTENT, required = false)
	private final String attributeContent;

	@XmlElement(name = Elements.SEARCHABLE_CONTENT, required = false)
	private final String searchableContent;
	
	@XmlElementWrapper(name = Elements.ATTRIBUTE_DEFINITIONS, required = false)
	@XmlElement(name = Elements.ATTRIBUTE_DEFINITION, required = false)
    private List<WorkflowAttributeDefinition> attributeDefinitions;

	@XmlElementWrapper(name = Elements.SEARCHABLE_DEFINITIONS, required = false)
	@XmlElement(name = Elements.SEARCHABLE_DEFINITION, required = false)
	private List<WorkflowAttributeDefinition> searchableDefinitions;
    
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
    
	/**
     * Private constructor used only by JAXB.
     */
	private DocumentContentUpdate() {
	    this.applicationContent = null;
	    this.attributeContent = null;
	    this.searchableContent = null;
	    this.attributeDefinitions = null;
	    this.searchableDefinitions = null;
	}
	
    private DocumentContentUpdate(Builder builder) {
    	this.applicationContent = builder.getApplicationContent();
    	this.attributeContent = builder.getAttributeContent();
    	this.searchableContent = builder.getSearchableContent();
    	if (builder.getAttributeDefinitions() != null) {
    		this.attributeDefinitions = new ArrayList<WorkflowAttributeDefinition>(builder.getAttributeDefinitions());
    	} else {
    		this.attributeDefinitions = new ArrayList<WorkflowAttributeDefinition>();
    	}
    	if (builder.getSearchableDefinitions() != null) {
    		this.searchableDefinitions = new ArrayList<WorkflowAttributeDefinition>(builder.getSearchableDefinitions());
    	} else {
    		this.searchableDefinitions = new ArrayList<WorkflowAttributeDefinition>();
    	}
    }
        
	public String getApplicationContent() {
		return applicationContent;
	}
	
	public String getAttributeContent() {
		return attributeContent;
	}
	
	public String getSearchableContent() {
		return searchableContent;
	}

	public List<WorkflowAttributeDefinition> getAttributeDefinitions() {
		return Collections.unmodifiableList(attributeDefinitions);
	}

	public List<WorkflowAttributeDefinition> getSearchableDefinitions() {
		return Collections.unmodifiableList(searchableDefinitions);
	}
	
	/**
	 * A builder which can be used to construct {@link DocumentContentUpdate} instances.
	 */
	public final static class Builder implements Serializable, ModelBuilder {

		private static final long serialVersionUID = -1680695196516508680L;

		private String attributeContent;
		private String applicationContent;
		private String searchableContent;
		private List<WorkflowAttributeDefinition> attributeDefinitions;
		private List<WorkflowAttributeDefinition> searchableDefinitions;

		private Builder() {
			this.attributeContent = "";
			this.applicationContent = "";
			this.searchableContent = "";
			this.attributeDefinitions = new ArrayList<WorkflowAttributeDefinition>();
			this.searchableDefinitions = new ArrayList<WorkflowAttributeDefinition>();
		}

		public static Builder create() {
			return new Builder();
		}

		public static Builder create(DocumentContent documentContent) {
			if (documentContent == null) {
				throw new IllegalArgumentException("documentContent was null");
			}
			Builder builder = create();
			builder.setAttributeContent(documentContent.getAttributeContent());
			builder.setApplicationContent(documentContent.getApplicationContent());
			builder.setSearchableContent(documentContent.getSearchableContent());
			return builder;
		}
		
		public static Builder create(DocumentContentUpdate documentContentUpdate) {
			if (documentContentUpdate == null) {
				throw new IllegalArgumentException("documentContentUpdate was null");
			}
			Builder builder = create();
			builder.setAttributeContent(documentContentUpdate.getAttributeContent());
			builder.setApplicationContent(documentContentUpdate.getApplicationContent());
			builder.setSearchableContent(documentContentUpdate.getSearchableContent());
			builder.setAttributeDefinitions(documentContentUpdate.getAttributeDefinitions());
			builder.setSearchableDefinitions(documentContentUpdate.getSearchableDefinitions());
			return builder;
		}

		public DocumentContentUpdate build() {
			return new DocumentContentUpdate(this);
		}

		public String getAttributeContent() {
			return attributeContent;
		}

		public void setAttributeContent(String attributeContent) {
			this.attributeContent = attributeContent;
		}

		public String getApplicationContent() {
			return applicationContent;
		}

		public void setApplicationContent(String applicationContent) {
			this.applicationContent = applicationContent;
		}

		public String getSearchableContent() {
			return searchableContent;
		}

		public void setSearchableContent(String searchableContent) {
			this.searchableContent = searchableContent;
		}

		public List<WorkflowAttributeDefinition> getAttributeDefinitions() {
			return attributeDefinitions;
		}

		public void setAttributeDefinitions(List<WorkflowAttributeDefinition> attributeDefinitions) {
			this.attributeDefinitions = attributeDefinitions;
		}

		public List<WorkflowAttributeDefinition> getSearchableDefinitions() {
			return searchableDefinitions;
		}

		public void setSearchableDefinitions(List<WorkflowAttributeDefinition> searchableDefinitions) {
			this.searchableDefinitions = searchableDefinitions;
		}

	}
	
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentContentUpdate";
        final static String TYPE_NAME = "DocumentContentUpdateType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String APPLICATION_CONTENT = "applicationContent";
        final static String ATTRIBUTE_CONTENT = "attributeContent";
        final static String SEARCHABLE_CONTENT = "searchableContent";
        final static String ATTRIBUTE_DEFINITION = "attributeDefinition";
        final static String ATTRIBUTE_DEFINITIONS = "attributeDefinitions";
        final static String SEARCHABLE_DEFINITION = "searchableDefinition";
        final static String SEARCHABLE_DEFINITIONS = "searchableDefinitions";
    }

}
