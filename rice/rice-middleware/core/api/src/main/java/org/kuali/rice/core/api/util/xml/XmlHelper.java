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
package org.kuali.rice.core.api.util.xml;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Provides a set of utilities for XML-related operations on org.jdom & org.w3c
 * xml Objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class XmlHelper {
    private static final Log LOG = LogFactory.getLog(XmlHelper.class);

    private XmlHelper() {
        throw new UnsupportedOperationException("do not call");
    }

    /**
     * Creates jdom Document from a Reader.  Does not close the reader.
     *
     * @param xmlStream the reader representing the xmlstream
     * @return jdom document
     */
    public static org.jdom.Document buildJDocument(Reader xmlStream) {
        // use SAX Builder
        // don't verify for speed reasons
        final SAXBuilder builder = new SAXBuilder(false);
        try {
            return builder.build(xmlStream);
        } catch (IOException e) {
            throw new XmlException("Invalid xml string. ", e);
        } catch (JDOMException e) {
            throw new XmlException("Invalid xml string. ", e);
        }
    }

    /**
     * Creates jdom Document from a w3c Document.  Does not close the reader.
     *
     * @param document the w3c document
     * @return jdom document
     */
    public static org.jdom.Document buildJDocument(org.w3c.dom.Document document) {
        return new DOMBuilder().build(document);
    }

    /**
     * Find all Elements in document of a particular name
     *
     * @param root the starting Element to scan
     * @param elementName name of the Element to scan for
     * @return collection of the Elements found.
     *         returns an empty collection if none are found.
     */
    public static Collection<Element> findElements(Element root, String elementName) {
        Collection<Element> elementList = new ArrayList<Element>();

        if (root == null) {
            return elementList;
        }

        XmlHelper.findElements(root, elementName, elementList);

        return elementList;
    }

    public static void appendXml(Node parentNode, String xml) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        org.w3c.dom.Document xmlDocument = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        org.w3c.dom.Element xmlDocumentElement = xmlDocument.getDocumentElement();
        Node importedNode = parentNode.getOwnerDocument().importNode(xmlDocumentElement, true);
        parentNode.appendChild(importedNode);
    }

    public static org.w3c.dom.Document readXml(String xml) throws TransformerException {
        Source source = new StreamSource(new BufferedReader(new StringReader(xml)));
        DOMResult result = new DOMResult();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(source, result);
        return (org.w3c.dom.Document) result.getNode();
    }

    public static void propagateNamespace(Element element, Namespace namespace) {
        element.setNamespace(namespace);
        for (Object childElement : element.getChildren()) {
            propagateNamespace((Element) childElement, namespace);
        }
    }

    public static org.w3c.dom.Document trimXml(InputStream input) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document oldDocument = builder.parse(input);
        org.w3c.dom.Element naviElement = oldDocument.getDocumentElement();
        trimElement(naviElement);
        return oldDocument;
    }

    public static Document trimSAXXml(InputStream input) throws JDOMException, SAXException, IOException, ParserConfigurationException {
        SAXBuilder builder = new SAXBuilder(false);
        Document oldDocument = builder.build(input);
        Element naviElement = oldDocument.getRootElement();
        trimSAXElement(naviElement);
        return oldDocument;
    }

    /**
     * Convenience method that performs an xpath evaluation to determine whether the expression
     * evaluates to true (a node exists).
     * This is method exists only to disambiguate the cases of determining the *presence* of a node
     * and determining the *boolean value of the node as converted from a string*, as the syntaxes
     * are very similar and could be misleading.
     *
     * @param xpath      the XPath object
     * @param expression the XPath expression
     * @param object     the object on which to evaluate the expression as required by the XPath API, typically a Node
     * @return whether the result of the expression evaluation, which is whether or not a node was present
     * @throws XPathExpressionException if the expression fails
     */
    public static boolean pathExists(XPath xpath, String expression, Object object) throws XPathExpressionException {
        return ((Boolean) xpath.evaluate(expression, object, XPathConstants.BOOLEAN)).booleanValue();
    }

    public static org.w3c.dom.Element propertiesToXml(org.w3c.dom.Document doc, Object o, String elementName) throws Exception {
        Class<?> c = o.getClass();
        org.w3c.dom.Element wrapper = doc.createElement(elementName);
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if ("getClass".equals(name)) {
                continue;
            }
            // The post processor could be in another server and we would be unable to retrieve it.
            if ("getPostProcessor".equals(name)) {
                continue;
            }
            if (!name.startsWith("get") || method.getParameterTypes().length > 0) {
                continue;
            }
            name = name.substring("get".length());
            name = StringUtils.uncapitalize(name);
            try {
                Object result = method.invoke(o);
                final String value;
                if (result == null) {
                    LOG.debug("value of " + name + " method on object " + o.getClass() + " is null");
                    value = "";
                } else {
                    value = result.toString();
                }
                org.w3c.dom.Element fieldE = doc.createElement(name);
                fieldE.appendChild(doc.createTextNode(value));
                wrapper.appendChild(fieldE);
            } catch (Exception e) {
                throw new XmlException("Error accessing method '" + method.getName() + "' of instance of " + c, e);
            }
        }
        return wrapper;
    }

    /**
     * This function is tail-recursive and just adds the root to the list if it
     * matches and checks the children.
     *
     * @param root the root element to search under
     * @param elementName the element name to find
     * @param list a list of found element
     */
    private static void findElements(Element root, String elementName, Collection<Element> list) {
        if (root != null) {
            if (root.getName().equals(elementName)) {
                list.add(root);
            }

            for (Object item : root.getChildren()) {
                if (item != null) {
                    XmlHelper.findElements((Element) item, elementName, list);
                }
            }
        }
    }

    private static void trimElement(Node node) throws SAXException, IOException, ParserConfigurationException {

        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child != null) {
                    trimElement(child);
                }
            }
        } else {
            if (node.getNodeType() == Node.TEXT_NODE) {
                String text = node.getNodeValue();
                text = StringUtils.isEmpty(text) ? "" : text.trim();
                node.setNodeValue(text);
            }
        }
    }

    private static void trimSAXElement(Element element) throws SAXException, IOException, ParserConfigurationException {

        if (!element.getChildren().isEmpty()) {
            for (Object child : element.getChildren()) {
                if (child != null) {
                    trimSAXElement((Element) child);
                }
            }
        } else {
            String text = element.getTextTrim();
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            element.setText(text);
        }
    }
}
