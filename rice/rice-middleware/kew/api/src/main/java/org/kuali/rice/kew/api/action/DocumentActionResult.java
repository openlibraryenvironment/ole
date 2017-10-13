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
package org.kuali.rice.kew.api.action;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.kew.api.document.Document;
import org.w3c.dom.Element;

@XmlRootElement(name = DocumentActionResult.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = DocumentActionResult.Constants.TYPE_NAME, propOrder = {
		DocumentActionResult.Elements.DOCUMENT,
		DocumentActionResult.Elements.VALID_ACTIONS,
		DocumentActionResult.Elements.REQUESTED_ACTIONS,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class DocumentActionResult extends AbstractDataTransferObject {
    
	private static final long serialVersionUID = -3916503634900791018L;

	@XmlElement(name = Elements.DOCUMENT, required = true)
    private final Document document;
	
	@XmlElement(name = Elements.VALID_ACTIONS, required = false)
	private final ValidActions validActions;
	
	@XmlElement(name = Elements.REQUESTED_ACTIONS, required = false)
	private final RequestedActions requestedActions;
	    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private DocumentActionResult() {
    	this.document = null;
    	this.validActions = null;
    	this.requestedActions = null;
    }
    
    private DocumentActionResult(Document document, ValidActions validActions, RequestedActions requestedActions) {
    	if (document == null) {
    		throw new IllegalArgumentException("document was null");
    	}
        this.document = document;
        this.validActions = validActions;
        this.requestedActions = requestedActions;
    }
    
    public static DocumentActionResult create(Document document, ValidActions validActions, RequestedActions requestedActions) {
    	return new DocumentActionResult(document, validActions, requestedActions);
    }
    
    public Document getDocument() {
        return document;
    }
    
    public ValidActions getValidActions() {
    	return validActions;
    }
    
    public RequestedActions getRequestedActions() {
    	return requestedActions;
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "documentActionResult";
        final static String TYPE_NAME = "DocumentActionResultType";
    }
    
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String DOCUMENT = "document";
        final static String VALID_ACTIONS = "validActions";
        final static String REQUESTED_ACTIONS = "requestedActions";
    }

}
