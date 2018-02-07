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
package org.kuali.rice.edl.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.kuali.rice.edl.impl.components.MatchingParam;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Contains a bunch of dom utility methods.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class EDLXmlUtils {

	private static final Logger LOG = Logger.getLogger(EDLXmlUtils.class);

	public static final String EDL_E = "edl";
	public static final String EDLCONTENT_E = "edlContent";
	public static final String DATA_E = "data";
	public static final String TYPE_E = "type";
	public static final String VALIDATION_E = "validation";
	public static final String VERSION_E = "version";
	public static final String DOCID_E = "docId";

	private EDLXmlUtils() {
		throw new UnsupportedOperationException("do not call");
	}

    /**
     * Returns a valid DocumentBuilder
     * @return a valid DocumentBuilder
     */
    public static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            // well folks, there is not much we can do if we get a ParserConfigurationException
            // so might as well isolate the evilness here, and just balk if this occurs
            String message = "Error obtaining document builder";
            LOG.error(message, pce);
            throw new RuntimeException(message, pce);
        }
    }


	public static Element createFieldDataElement(Element parentVersionElement, MatchingParam matchingParam) {
		Element fieldData = createChildElement(parentVersionElement, "field");
		fieldData.setAttribute("name", matchingParam.getParamName());
		if (matchingParam.getError().booleanValue()) {
			fieldData.setAttribute("invalid", "true");
			Element errorMessage = getOrCreateChildElement(fieldData, "errorMessage", true);
			placeTextInElement(errorMessage, matchingParam.getErrorMessage());
		}
		Element fieldValue = getOrCreateChildElement(fieldData, "value", true);
		placeTextInElement(fieldValue, matchingParam.getParamValue());
		return fieldData;
	}

	public static Element createChildElement(Element parentElement, String elementName) {
		Element child = parentElement.getOwnerDocument().createElement(elementName);
		parentElement.appendChild(child);
		return child;
	}

	public static Element getDocumentStateElement(Document dom) {
		return EDLXmlUtils.getOrCreateChildElement(dom.getDocumentElement(), "documentState", true);
	}


	public static void addGlobalErrorMessage(Document dom, String errorMessage) {
		Element documentState = getDocumentStateElement(dom);
		createTextElementOnParent(documentState, "error", errorMessage);
	}

	private static void placeTextInElement(Element element, String text) {
		if (element.getOwnerDocument() == null) {
			throw new WorkflowRuntimeException("The element must have an owner document in order to add text");
		}
		element.appendChild(element.getOwnerDocument().createTextNode(text));
	}

	public static Element createTextElementOnParent(Element parent, String childElementName, String text) {
		if (text == null) {
			throw new WorkflowRuntimeException("The text placed in an Element cannot be null");
		}
		Element childElement = parent.getOwnerDocument().createElement(childElementName);
		parent.appendChild(childElement);
		childElement.appendChild(parent.getOwnerDocument().createTextNode(text));
		return childElement;
	}

	public static Element getVersionFromData(Element dataElement, Integer versionCount) {
		if (dataElement == null) {
			throw new WorkflowRuntimeException("Attempting to put version element inside null data Element");
		}
		if (!dataElement.getTagName().equals(DATA_E)) {
			throw new WorkflowRuntimeException("Attempting to put version element inside a parent that is not a data element " + dataElement.getTagName());
		}
		Element version = createChildElement(dataElement, VERSION_E);
		version.setAttribute("current", "true");
		version.setAttribute("date", new Date().toString());
		version.setAttribute("version", versionCount.toString());
		return version;
	}

	public static Element getDataFromEDLDocument(Element edlContent, boolean create) {
        return getOrCreateChildElement(edlContent, DATA_E, create);
    }

    public static Element getEDLContent(Document displayDoc, boolean create) {
        return getOrCreateChildElement(displayDoc.getDocumentElement(), EDLCONTENT_E, create);
    }

    /**
     * Returns, and creates if absent, a child element
     * @param parent the parent element
     * @param name the name of the child element to create and/or return
     * @return reference to the child element
     */
    public static Element getOrCreateChildElement(Element parent, String name, boolean create) {
        if (parent == null) {
        	throw new WorkflowRuntimeException("Passed in null parent element attempting to create child element '" + name + "'");
        }
        Element child = getChildElement(parent, name);
        if (child == null && create) {
            LOG.debug("Creating child element '" + name + "' of parent: " + parent);
            child = parent.getOwnerDocument().createElement(name);
            parent.appendChild(child);
        }
        return child;
    }

    /**
     * Returns a node child with the specified tag name of the specified parent node,
     * or null if no such child node is found.
     * @param parent the parent node
     * @param name the name of the child node
     * @return child node if found, null otherwise
     */
    public static Element getChildElement(Node parent, String name) {
        NodeList childList = parent.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node node = childList.item(i);
            // we must test against NodeName, not just LocalName
            // LocalName seems to be null - I am guessing this is because
            // the DocumentBuilderFactory is not "namespace aware"
            // although I would have expected LocalName to default to
            // NodeName
            if (node.getNodeType() == Node.ELEMENT_NODE
                && (name.equals(node.getLocalName())
                   || name.equals(node.getNodeName()))) {
                return (Element) node;
            }
        }
        return null;
    }

    /**
     * Returns the text value of a child element with the given name, of the given parent element,
     * or null if the child does not exist or does not have a child text node
     * @param parent parent element
     * @param name name of child element
     * @return the text value of a child element with the given name, of the given parent element,
     * or null if the child does not exist or does not have a child text node
     */
    public static String getChildElementTextValue(Node parent, String name) {
        Element child = getChildElement(parent, name);
        if (child == null) {
            return null;
        }
        Node textNode = child.getFirstChild();
        if (textNode == null) {
            return null;
        }
        return textNode.getNodeValue();
    }



    /**
     * Adds the specified errors and messages to the &lt;documentState&gt; element of the
     * given EDL doc
     * @param doc the EDL doc
     * @param errors the list of error Strings
     * @param messages the list of message Strings
     */
    public static void addErrorsAndMessagesToDocument(Document doc, List errors, List messages, Map<String, String> fieldErrors) {
        Node documentState = EDLXmlUtils.getDocumentStateElement(doc);
        Iterator it = errors.iterator();
        while (it.hasNext()) {
            Element error = doc.createElement("error");
            error.appendChild(doc.createTextNode(it.next().toString()));
            documentState.appendChild(error);
        }
        it = messages.iterator();
        while (it.hasNext()) {
            Element error = doc.createElement("message");
            error.appendChild(doc.createTextNode(it.next().toString()));
            documentState.appendChild(error);
        }
        for (String errorKey : fieldErrors.keySet()) {
        	Element error = doc.createElement("fieldError");
        	error.setAttribute("key", errorKey);
        	error.appendChild(doc.createTextNode(fieldErrors.get(errorKey)));
        	documentState.appendChild(error);
        }
    }

}


