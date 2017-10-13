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
package org.kuali.rice.kew.rule;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper class that can parse and generate generic attribute content
 * from Map<String,String> values.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GenericAttributeContent {
    private static final XPathExpression NAME_EXPR;
    private static final XPathExpression VALUE_EXPR;
    private static final XPathExpression FIELD_EXPR;
    static {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NAME_EXPR = xpath.compile("name");
            VALUE_EXPR = xpath.compile("value");
            FIELD_EXPR = xpath.compile("field");
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException(xpee);
        }
    }

    private final Logger log;

    private final String elementName;
    private final XPathExpression attr_expr;

    public GenericAttributeContent(Class clazz) {
        this(clazz.getName());
    }
    public GenericAttributeContent(String elementName) {
        this.elementName = elementName;
        log = Logger.getLogger(GenericAttributeContent.class + "[" + elementName + "]");
        try {
            attr_expr = XPathFactory.newInstance().newXPath().compile(elementName);
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException(xpee);
        }
    }

    public String generateContent(Map<String, String> properties) {
        if (properties.size() == 0) return "<" + elementName + "/>";

        StringBuilder sb = new StringBuilder();
        sb.append("<" + elementName + ">\r\n");
        for (Map.Entry<String, String> entry: properties.entrySet()) {
            String key = entry.getKey();
            sb.append("  <field>\r\n");
            if (key != null) {
                sb.append("    <name>" + key + "</name>\r\n");
            } else {
                log.warn("null key encountered");
            }
            String value = entry.getValue();
            if (value != null) {
                sb.append("    <value>" + entry.getValue() + "</value>\r\n");
            } else {
                log.warn("null value encountered for key: " + key);
            }
            sb.append("  </field>\r\n");
        }
        sb.append("</" + elementName + ">\r\n");

        return sb.toString();
    }

    public List<Map<String, String>> parseContent(Element attributeContent) throws XPathExpressionException {
        List<Map<String, String>> attrs = new ArrayList<Map<String, String>>();
        if (attributeContent == null) {
            return attrs;
        }
        log.info("Parsing content: "+ XmlJotter.jotNode(attributeContent));
        NodeList attrNodes = (NodeList) attr_expr.evaluate(attributeContent, XPathConstants.NODESET);
        if (attrNodes != null) {
            for (int i = 0; i < attrNodes.getLength(); i++) {
                Map<String, String> props = new HashMap<String, String>();
                attrs.add(props);
                Node node = attrNodes.item(i);
                log.info("Found matching attribute: " + XmlJotter.jotNode(node));
                NodeList fieldNodes = (NodeList) FIELD_EXPR.evaluate(node, XPathConstants.NODESET);
                for (int j = 0; j < fieldNodes.getLength(); j++) {
                    node = fieldNodes.item(j);
                    log.info("Found matching attribute content field: " + XmlJotter.jotNode(node));
                    Boolean b = (Boolean) NAME_EXPR.evaluate(node, XPathConstants.BOOLEAN);
                    if (!b.booleanValue()) {
                        log.error("Encountered field with no name, skipping!");
                        continue;
                    }
                    String name = NAME_EXPR.evaluate(node);
                    b = (Boolean) VALUE_EXPR.evaluate(node, XPathConstants.BOOLEAN);
                    String value = null;
                    if (b.booleanValue()) {
                        value = VALUE_EXPR.evaluate(node);
                    } else {
                        log.warn("No value defined for transmitted field named: " + name);
                    }
                    log.info("Matching attribute content field value: " + name + "=" + value);
                    props.put(name, value);
                }
            }
        }
        return attrs;
    }
}
