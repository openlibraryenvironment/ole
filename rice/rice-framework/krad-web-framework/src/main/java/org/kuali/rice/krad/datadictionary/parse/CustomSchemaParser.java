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
package org.kuali.rice.krad.datadictionary.parse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Map;

/**
 * Parser for parsing xml bean's created using the custom schema into normal spring bean format.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CustomSchemaParser extends AbstractSingleBeanDefinitionParser {
    private static final Log LOG = LogFactory.getLog(CustomSchemaParser.class);

    private static int beanNumber = 0;

    /**
     * Retrieves the class of the bean defined by the xml element.
     *
     * @param bean - The xml element for the bean being parsed.
     * @return The class associated with the provided tag
     */
    protected Class getBeanClass(Element bean) {
        Map<String, BeanTagInfo> beanType = null;

        // Attempt to load the list of tags
        try {
            beanType = CustomTagAnnotations.getBeanTags();
        } catch (Exception e) {
            LOG.error("Error retrieving bean tag information", e);
        }

        Class<?> beanTag = null;
        try {
            // Retrieve the connected class in the tag map using the xml tag's name.

            beanTag = beanType.get(bean.getLocalName()).getBeanClass();
        } catch (Exception e) {
            LOG.error("Error in retrieved bean tag information", e);
        }

        return beanTag;
    }

    /**
     * Parses the xml bean into a standard bean definition format and fills the information in the passed in definition
     * builder
     *
     * @param element - The xml bean being parsed.
     * @param parserContext - Provided information and functionality regarding current bean set.
     * @param bean - A definition builder used to build a new spring bean from the information it is filled with.
     */
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder bean) {
        // Retrieve custom schema information build from the annotations
        Map<String, Map<String, BeanTagAttributeInfo>> attributeProperties =
                CustomTagAnnotations.getAttributeProperties();
        Map<String, BeanTagAttributeInfo> entries = attributeProperties.get(element.getLocalName());

        // Log error if there are no attributes found for the bean tag
        if (entries == null) {
            LOG.error("Bean Tag not found " + element.getLocalName());
        }

        // Retrieve the information for the new bean tag and fill in the default parent if needed
        BeanTagInfo tagInfo = CustomTagAnnotations.getBeanTags().get(element.getLocalName());
        if (tagInfo.getParent().compareTo("none") != 0) {
            bean.setParentName(tagInfo.getParent());
        }

        // Create the map for the attributes found in the tag and process them in to the definition builder.
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            processSingleValue(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue(), entries, bean);
        }

        ArrayList<Element> children = (ArrayList<Element>) DomUtils.getChildElements(element);

        // Process the children found in the xml tag
        for (int i = 0; i < children.size(); i++) {
            String tag = children.get(i).getLocalName();
            BeanTagAttributeInfo info = entries.get(tag);

            String propertyName;
            BeanTagAttribute.AttributeType type;

            if(children.get(i).getTagName().equals("spring:property")){
                BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
                delegate.parsePropertyElement(children.get(i), bean.getBeanDefinition());
                continue;
            }

            // Sets the property name to be used when adding the property value
            if (info == null) {
                // If the tag is not in the schema map let spring handle the value by forwarding the tag as the
                // propertyName
                propertyName = tag;
                type = findBeanType(children.get(i));

            } else {
                // If the tag is found in the schema map use the connected name stored in the attribute information
                propertyName = info.getName();
                type = info.getType();
            }
            // Process the information stored in the child bean
            ArrayList<Element> grandChildren = (ArrayList<Element>) DomUtils.getChildElements(children.get(i));

            if (type == BeanTagAttribute.AttributeType.SINGLEBEAN) {
                bean.addPropertyValue(propertyName, parseBean(grandChildren.get(0), bean, parserContext));
            } else if (type == BeanTagAttribute.AttributeType.LISTBEAN) {
                bean.addPropertyValue(propertyName, parseList(grandChildren, children.get(i), bean, parserContext));
            } else if (type == BeanTagAttribute.AttributeType.LISTVALUE) {
                bean.addPropertyValue(propertyName, parseList(grandChildren, children.get(i), bean, parserContext));
            } else if (type == BeanTagAttribute.AttributeType.MAPVALUE) {
                bean.addPropertyValue(propertyName, parseMap(grandChildren, children.get(i), bean, parserContext));
            } else if (type == BeanTagAttribute.AttributeType.MAPBEAN) {
                bean.addPropertyValue(propertyName, parseMap(grandChildren, children.get(i), bean, parserContext));
            } else if (type == BeanTagAttribute.AttributeType.SETVALUE) {
                bean.addPropertyValue(propertyName, parseSet(grandChildren, children.get(i), bean, parserContext));
            } else if (type == BeanTagAttribute.AttributeType.SETBEAN) {
                bean.addPropertyValue(propertyName, parseSet(grandChildren, children.get(i), bean, parserContext));
            }
        }
        return;
    }

    /**
     * Adds the property value to the bean definition based on the name and value of the attribute.
     *
     * @param name - The name of the attribute.
     * @param value - The value of the attribute.
     * @param entries - The property entries for the over all tag.
     * @param bean - The bean definition being created.
     */
    private void processSingleValue(String name, String value, Map<String, BeanTagAttributeInfo> entries,
            BeanDefinitionBuilder bean) {

        if (name.toLowerCase().compareTo("parent") == 0) {
            // If attribute is defining the parent set it in the bean builder.
            bean.setParentName(value);
        } else if (name.toLowerCase().compareTo("abstract") == 0) {
            // If the attribute is defining the parent as  abstract set it in the bean builder.
            bean.setAbstract(Boolean.valueOf(value));
        } else if (name.toLowerCase().compareTo("id") == 0) {
            if (value.contains("Demo-CollectionGrouping-Section1")) {
                System.out.println();
            }

            //nothing - insures that its erased
        } else {
            // If the attribute is not a reserved case find the property name form the connected map and add the new
            // property value.

            if (name.contains("-ref")) {
                bean.addPropertyValue(name.substring(0, name.length() - 4), new RuntimeBeanReference(value));
            } else {
                BeanTagAttributeInfo info = entries.get(name);
                String propertyName;

                if (info == null) {
                    propertyName = name;
                } else {
                    propertyName = info.getName();
                }
                bean.addPropertyValue(propertyName, value);
            }
        }
    }

    /**
     * Finds the key of a map entry in the custom schema.
     *
     * @param grandchild - The map entry.
     * @return The object (bean or value) entry key
     */
    private Object findKey(Element grandchild) {
        String key = grandchild.getAttribute("key");
        if (!key.isEmpty()) {
            return key;
        } else {
            Element keyTag = DomUtils.getChildElementByTagName(grandchild, "spring:key");
            if (DomUtils.getChildElements(keyTag).size() == 0) {
                return keyTag.getTextContent();
            } else {
                return DomUtils.getChildElements(keyTag).get(0);
            }
        }
        //throw new Exception("Cannot find Map's key");
    }

    /**
     * Finds the value of a map entry in the custom schema.
     *
     * @param grandchild - The map entry.
     * @return The object (bean or value) entry value
     */
    private Object findValue(Element grandchild) {
        String value = grandchild.getAttribute("value");
        if (!value.isEmpty()) {
            return value;
        } else {
            Element valueTag = DomUtils.getChildElementByTagName(grandchild, "spring:value");
            if (DomUtils.getChildElements(valueTag).size() == 0) {
                return valueTag.getTextContent();
            } else {
                return DomUtils.getChildElements(valueTag).get(0);
            }
        }
        //throw new Exception("Cannot find Map's value");
    }

    /**
     * Finds the attribute type of the schema being used by the element.
     *
     * @param tag - The tag to check.
     * @return The schema attribute type.
     */
    private BeanTagAttribute.AttributeType findBeanType(Element tag) {
        int numberChildren = 0;

        // Checks if the user overrides the default attribute type of the schema.
        String overrideType = tag.getAttribute("overrideBeanType");
        if (!StringUtils.isEmpty(overrideType)) {
            if (overrideType.toLowerCase().compareTo("singlebean") == 0) {
                return BeanTagAttribute.AttributeType.SINGLEBEAN;
            }
            if (overrideType.toLowerCase().compareTo("singlevalue") == 0) {
                return BeanTagAttribute.AttributeType.SINGLEVALUE;
            }
            if (overrideType.toLowerCase().compareTo("listbean") == 0) {
                return BeanTagAttribute.AttributeType.LISTBEAN;
            }
            if (overrideType.toLowerCase().compareTo("listvalue") == 0) {
                return BeanTagAttribute.AttributeType.LISTVALUE;
            }
            if (overrideType.toLowerCase().compareTo("mapbean") == 0) {
                return BeanTagAttribute.AttributeType.MAPBEAN;
            }
            if (overrideType.toLowerCase().compareTo("mapvalue") == 0) {
                return BeanTagAttribute.AttributeType.MAPVALUE;
            }
            if (overrideType.toLowerCase().compareTo("setbean") == 0) {
                return BeanTagAttribute.AttributeType.SETBEAN;
            }
            if (overrideType.toLowerCase().compareTo("setvalue") == 0) {
                return BeanTagAttribute.AttributeType.SETVALUE;
            }
        }

        // Checks if the element is a list composed of standard types
        numberChildren = DomUtils.getChildElementsByTagName(tag, "spring:value").size();
        if (numberChildren > 0) {
            return BeanTagAttribute.AttributeType.LISTVALUE;
        }

        numberChildren = DomUtils.getChildElementsByTagName(tag, "spring:list").size();
        if (numberChildren > 0) {
            return BeanTagAttribute.AttributeType.LISTBEAN;
        }

        numberChildren = DomUtils.getChildElementsByTagName(tag, "spring:set").size();
        if (numberChildren > 0) {
            return BeanTagAttribute.AttributeType.SETBEAN;
        }

        // Checks if the element is a map
        numberChildren = DomUtils.getChildElementsByTagName(tag, "spring:entry").size();
        if (numberChildren > 0) {
            return BeanTagAttribute.AttributeType.MAPVALUE;
        }

        numberChildren = DomUtils.getChildElementsByTagName(tag, "spring:map").size();
        if (numberChildren > 0) {
            return BeanTagAttribute.AttributeType.MAPBEAN;
        }

        // Checks if the element is a list of beans
        numberChildren = DomUtils.getChildElements(tag).size();
        if (numberChildren > 1) {
            return BeanTagAttribute.AttributeType.LISTBEAN;
        }

        // Defaults to return the element as a single bean.
        return BeanTagAttribute.AttributeType.SINGLEBEAN;
    }

    /**
     * Parses a bean based on the namespace of the bean.
     *
     * @param tag - The Element to be parsed.
     * @param parent - The parent bean that the tag is nested in.
     * @param parserContext - Provided information and functionality regarding current bean set.
     * @return The parsed bean.
     */
    private Object parseBean(Element tag, BeanDefinitionBuilder parent, ParserContext parserContext) {
        if (tag.getNamespaceURI().compareTo("http://www.springframework.org/schema/beans") == 0) {
            return parseSpringBean(tag, parserContext);
        } else {
            return parseCustomBean(tag, parent, parserContext);
        }
    }

    /**
     * Parses a bean of the spring namespace.
     *
     * @param tag - The Element to be parsed.
     * @return The parsed bean.
     */
    private Object parseSpringBean(Element tag, ParserContext parserContext) {
        if (tag.getLocalName().compareTo("ref") == 0) {
            // Create the referenced bean by creating a new bean and setting its parent to the referenced bean
            // then replace grand child with it
            Element temp = tag.getOwnerDocument().createElement("bean");
            temp.setAttribute("parent", tag.getAttribute("bean"));
            tag = temp;
            return new RuntimeBeanReference(tag.getAttribute("parent"));
        }

        //peel off p: properties an make them actual property nodes - p-namespace does not work properly (unknown cause)
        Document document = tag.getOwnerDocument();
        NamedNodeMap attributes = tag.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++){
            Node attribute = attributes.item(i);
            String name = attribute.getNodeName();
            if(name.startsWith("p:")){
                Element property = document.createElement("property");
                property.setAttribute("name", StringUtils.removeStart(name, "p:"));
                property.setAttribute("value", attribute.getTextContent());

                if(tag.getFirstChild() != null){
                    tag.insertBefore(property, tag.getFirstChild());
                }
                else{
                    tag.appendChild(property);
                }
            }
        }

        // Create the bean definition for the grandchild and return it.
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        BeanDefinitionHolder bean = delegate.parseBeanDefinitionElement(tag);

        // Creates a custom name for the new bean.
        String name = bean.getBeanDefinition().getParentName() + "$Customchild" + beanNumber;
        if (tag.getAttribute("id") != null && !StringUtils.isEmpty(tag.getAttribute("id"))) {
            name = tag.getAttribute("id");
        } else {
            beanNumber++;
        }

        return new BeanDefinitionHolder(bean.getBeanDefinition(), name);
    }

    /**
     * Parses a bean of the custom namespace.
     *
     * @param tag - The Element to be parsed.
     * @param parent - The parent bean that the tag is nested in.
     * @param parserContext - Provided information and functionality regarding current bean set.
     * @return The parsed bean.
     */
    private Object parseCustomBean(Element tag, BeanDefinitionBuilder parent, ParserContext parserContext) {
        BeanDefinitionHolder bean;
        if (tag.getLocalName().compareTo("ref") == 0) {
            return new RuntimeBeanReference(tag.getAttribute("bean"));

        } else {
            BeanDefinition beanDefinition = parserContext.getDelegate().parseCustomElement(tag,
                    parent.getBeanDefinition());

            String name = beanDefinition.getParentName() + "$Customchild" + beanNumber;
            if (tag.getAttribute("id") != null && !StringUtils.isEmpty(tag.getAttribute("id"))) {
                name = tag.getAttribute("id");
            } else {
                beanNumber++;
            }
            bean = new BeanDefinitionHolder(beanDefinition, name);
        }

        return bean;
    }

    /**
     * Parses a list of elements into a list of beans/standard content.
     *
     * @param grandChildren - The list of beans/content in a bean property
     * @param child - The property tag for the parent.
     * @param parent - The parent bean that the tag is nested in.
     * @param parserContext - Provided information and functionality regarding current bean set.
     * @return A managedList of the nested content.
     */
    private ManagedList parseList(ArrayList<Element> grandChildren, Element child, BeanDefinitionBuilder parent,
            ParserContext parserContext) {
        ArrayList<Object> listItems = new ArrayList<Object>();

        for (int i = 0; i < grandChildren.size(); i++) {
            Element grandChild = grandChildren.get(i);

            if (grandChild.getTagName().compareTo("spring:value") == 0) {
                listItems.add(grandChild.getTextContent());
            } else {
                listItems.add(parseBean(grandChild, parent, parserContext));
            }
        }

        String merge = child.getAttribute("merge");

        ManagedList beans = new ManagedList(listItems.size());

        if (merge != null) {
            beans.setMergeEnabled(Boolean.valueOf(merge));
        }

        beans.addAll(listItems);
        return beans;
    }

    /**
     * Parses a list of elements into a set of beans/standard content.
     *
     * @param grandChildren - The set of beans/content in a bean property
     * @param child - The property tag for the parent.
     * @param parent - The parent bean that the tag is nested in.
     * @param parserContext - Provided information and functionality regarding current bean set.
     * @return A managedSet of the nested content.
     */
    private ManagedSet parseSet(ArrayList<Element> grandChildren, Element child, BeanDefinitionBuilder parent,
            ParserContext parserContext) {
        ManagedSet setItems = new ManagedSet();

        for (int i = 0; i < grandChildren.size(); i++) {
            Element grandChild = grandChildren.get(i);

            if (child.getTagName().compareTo("spring:value") == 0) {
                setItems.add(grandChild.getTextContent());
            } else {
                setItems.add(parseBean(grandChild, parent, parserContext));
            }
        }

        String merge = child.getAttribute("merge");
        if (merge != null) {
            setItems.setMergeEnabled(Boolean.valueOf(merge));
        }

        return setItems;
    }

    /**
     * Parses a list of elements into a map of beans/standard content.
     *
     * @param grandChildren - The list of beans/content in a bean property
     * @param child - The property tag for the parent.
     * @param parent - The parent bean that the tag is nested in.
     * @param parserContext - Provided information and functionality regarding current bean set.
     * @return A managedSet of the nested content.
     */
    private ManagedMap parseMap(ArrayList<Element> grandChildren, Element child, BeanDefinitionBuilder parent,
            ParserContext parserContext) {
        ManagedMap map = new ManagedMap();
        String merge = child.getAttribute("merge");

        for (int j = 0; j < grandChildren.size(); j++) {
            Object key = findKey(grandChildren.get(j));
            Object value = findValue(grandChildren.get(j));
            map.put(key, value);
        }

        if (merge != null) {
            map.setMergeEnabled(Boolean.valueOf(merge));
        }

        return map;
    }
}
