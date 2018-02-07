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
package org.kuali.rice.kew.api.actionlist;

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
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinitionContract;
import org.w3c.dom.Element;

/**
 * Represents the display params of the inline action list view.  This dictates 
 * how the inline frame will look height-wise.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = DisplayParameters.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DisplayParameters.Constants.TYPE_NAME, propOrder = {
        DisplayParameters.Elements.FRAME_HEIGHT,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DisplayParameters implements Serializable, DisplayParametersContract {

	private static final long serialVersionUID = 8859107918934290768L;
	
	@XmlElement(name = Elements.FRAME_HEIGHT, required = true)
	private final Integer frameHeight;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
	/**
     * Private constructor used only by JAXB.
     * 
     */
	private DisplayParameters(){
		this.frameHeight = null;
	}
	
	/**
     * Constructs a DisplayParameters from the given builder.  This constructor is private and should only
     * ever be invoked from the builder.
     * 
     * @param builder the Builder from which to construct the DisplayParameters
     */
	private DisplayParameters(Builder builder){
		this.frameHeight = builder.getFrameHeight();
	}
	
	@Override
	public Integer getFrameHeight() {
		return this.frameHeight;
	}
	
    /**
     * A builder which can be used to construct {@link DisplayParameters} instances.  Enforces the constraints of the {@link DocumentContentContract}.
     */
    public final static class Builder implements Serializable, ModelBuilder, DisplayParametersContract {

        private static final long serialVersionUID = 2709781019428490297L;

        private Integer frameHeight;
        
        /**
         * Private constructor for creating a builder with all of it's required attributes.
         */
        private Builder(Integer frameHeight){
        	setFrameHeight(frameHeight);
        }
    
        public static Builder create(Integer frameHeight){
        	return new Builder(frameHeight);
        }
        
        /**
         * Creates a builder by populating it with data from the given {@linkDisplayParametersContract}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
        public static Builder create(DisplayParametersContract contract){
        	 if (contract == null) {
                 throw new IllegalArgumentException("contract was null");
             }
             Builder builder = create(contract.getFrameHeight());
             return builder;
        }
        
        /**
         * Builds an instance of a DisplayParameters based on the current state of the builder.
         * 
         * @return the fully-constructed CampusType
         */
        @Override
        public DisplayParameters build() {
            return new DisplayParameters(this);
        }
        
        @Override
        public Integer getFrameHeight() {
            return this.frameHeight;
        }
        
        public void setFrameHeight(Integer frameHeight){
            if (frameHeight == null) {
                throw new IllegalArgumentException("frameHeight is null");
            }
        	this.frameHeight = frameHeight;
        }
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "displayParameters";
        final static String TYPE_NAME = "DisplayParametersType";
    }
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {

        final static String FRAME_HEIGHT = "frameHeight";
    }
}
