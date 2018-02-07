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
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ComponentBase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Creates and stores the information defined for the custom schema.  Loads the classes defined as having associated
 * custom schemas and creates the information for the schema by parsing there annotations.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CustomTagAnnotations {
    // Logger
    private static final Log LOG = LogFactory.getLog(CustomTagAnnotations.class);
    private static final String BEAN_SUFFIX = "-bean";

    private static Map<String, Map<String, BeanTagAttributeInfo>> attributeProperties;
    private static Map<String, BeanTagInfo> beanTags;
    private static List<Class<?>> customTagClasses;

    /**
     * Loads the list of class that have an associated custom schema.
     */
    private static void loadCustomTagClasses() {
        try {
            customTagClasses = findTagClasses("org.kuali.rice.krad");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Finds all the classes which have a BeanTag or BeanTags annotation
     *
     * @param basePackage the package to start in
     * @return classes which have BeanTag or BeanTags annotation
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findTagClasses(String basePackage) throws IOException, ClassNotFoundException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resolvedBasePackage = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(
                basePackage));
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolvedBasePackage + "/" + "**/*.class";
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (metadataReader != null && isBeanTag(metadataReader)) {
                    classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        }
        return classes;
    }

    /**
     * Returns true if the metadataReader representing the class has a BeanTag or BeanTags annotation
     *
     * @param metadataReader MetadataReader representing the class to analyze
     * @return true if BeanTag or BeanTags annotation is present
     */
    private static boolean isBeanTag(MetadataReader metadataReader) {
        try {
            try {
                Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
                if (c.getAnnotation(BeanTag.class) != null || c.getAnnotation(BeanTags.class) != null) {
                    return true;
                }
            } catch (Throwable e) {
                //skip
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Generate the custom schema for KRAD based on the custom tag annotations on KRAD classes
     *
     * @param doc the RescourceBundle containing the documentation
     * @return the
     */
    public static void generateSchemaFile(ResourceBundle doc) {
        Map<String, Map<String, BeanTagInfo>> nameTagMap = new HashMap<String, Map<String, BeanTagInfo>>();
        Map<String, BeanTagInfo> beanMap = CustomTagAnnotations.getBeanTags();
        BeanTagInfo infos[] = new BeanTagInfo[beanMap.values().size()];
        infos = beanMap.values().toArray(infos);
        String tags[] = new String[beanMap.entrySet().size()];
        try {
            tags = beanMap.keySet().toArray(tags);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //generate name-tag map
        for (int i = 0; i < infos.length; i++) {
            String name = infos[i].getBeanClass().getName();
            String tag = tags[i];
            Map<String, BeanTagInfo> existingTags = nameTagMap.get(name);

            if (existingTags == null) {
                existingTags = new HashMap<String, BeanTagInfo>();
            }

            if (infos[i].isDefaultTag() || existingTags.isEmpty()) {
                infos[i].setDefaultTag(true);
                existingTags.put("default", infos[i]);
            }

            if (infos[i].getParent() != null) {
                existingTags.put(infos[i].getParent(), infos[i]);
            }

            nameTagMap.put(name, existingTags);
        }

        try {
            //schema building begins
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            document = builder.newDocument();

            //init variables
            List<Element> types = new ArrayList<Element>();
            List<Element> elements = new ArrayList<Element>();
            Set<String> classKeys = nameTagMap.keySet();
            Map<String, Element> elementObjects = new HashMap<String, Element>();

            //fill in elementObjects will placeholder content to be filled out by the tag processing
            initializeElementObjects(document, classKeys, elementObjects);

            //get the attributes of componentBase (component sub-types create an extension to this)
            Map<String, BeanTagAttributeInfo> componentAttributes = getAttributes(ComponentBase.class);

            List<Element> componentAttributeElements = new ArrayList<Element>();

            //Create types for each element
            for (String className : classKeys) {
                Map<String, BeanTagInfo> tagMap = nameTagMap.get(className);
                Class clazz = Class.forName(className);
                BeanTagInfo typeInfo = tagMap.get("default");
                String currentType = typeInfo.getTag();
                boolean isComponentBase = currentType.equals("componentBase" + BEAN_SUFFIX);
                boolean isComponent = Component.class.isAssignableFrom(clazz);

                Element complexType = document.createElement("xsd:complexType");
                complexType.setAttribute("name", currentType + "-type");

                Element complexContent = null;
                Element extension = null;
                if (isComponent) {
                    complexContent = document.createElement("xsd:complexContent");
                    extension = document.createElement("xsd:extension");
                    extension.setAttribute("base", "componentAttributes-type");
                    complexContent.appendChild(extension);
                    complexType.appendChild(complexContent);
                }

                Element sequence = document.createElement("xsd:choice");
                sequence.setAttribute("minOccurs", "0");
                sequence.setAttribute("maxOccurs", "unbounded");

                List<Element> attributeProperties = new ArrayList<Element>();
                Map<String, BeanTagAttributeInfo> attributes = getAttributes(typeInfo.getBeanClass());

                if (attributes != null && !attributes.isEmpty()) {
                    for (BeanTagAttributeInfo aInfo : attributes.values()) {

                        boolean useAttribute = true;
                        boolean appendListAttributes = false;

                        //default to anyType
                        String attrType = "xsd:anyType";

                        //Process each type of content below by setting a type and special processing flags
                        if (aInfo.getType().equals(BeanTagAttribute.AttributeType.SINGLEVALUE)) {
                            attrType = "xsd:string";
                        } else if (aInfo.getType().equals(BeanTagAttribute.AttributeType.SINGLEBEAN)) {
                            String attributeClass = aInfo.getValueType().getName();
                            if (elementObjects.containsKey(attributeClass)) {
                                attrType = attributeClass;
                            }
                        } else if (aInfo.getType().equals(BeanTagAttribute.AttributeType.LISTVALUE) || aInfo.getType()
                                .equals(BeanTagAttribute.AttributeType.SETVALUE)) {
                            appendListAttributes = true;
                            attrType = "basicList-type";
                        } else if (aInfo.getType().equals(BeanTagAttribute.AttributeType.MAPVALUE) || aInfo.getType()
                                .equals(BeanTagAttribute.AttributeType.MAPBEAN) ||
                                aInfo.getType().equals(BeanTagAttribute.AttributeType.MAP2BEAN)) {
                            appendListAttributes = true;
                            attrType = "spring:mapType";
                        } else if (aInfo.getType().equals(BeanTagAttribute.AttributeType.LISTBEAN) || aInfo.getType()
                                .equals(BeanTagAttribute.AttributeType.SETBEAN)) {
                            useAttribute = false;
                            appendListAttributes = true;
                            attrType = "spring:listOrSetType";
                        }

                        //create the element and documentation
                        Element element = document.createElement("xsd:element");
                        element.setAttribute("name", aInfo.getName());
                        element.setAttribute("minOccurs", "0");
                        element.setAttribute("maxOccurs", "1");
                        element.appendChild(getDocAnnotation(document, doc, className, aInfo.getName(),
                                aInfo.getValueType().getName()));

                        //if this is a list it may require extra processing to add the merge attribute
                        if (appendListAttributes) {
                            Element extensionType = getListExtension(document, aInfo, attrType);
                            if (extensionType != null) {
                                element.appendChild(extensionType);
                            } else {
                                element.setAttribute("type", attrType);
                            }
                        } else {
                            element.setAttribute("type", attrType);
                        }

                        sequence.appendChild(element);

                        //skip to avoid duplication in children of component for same named attributes
                        if (isComponent && componentAttributes.containsValue(aInfo) && !isComponentBase) {
                            continue;
                        }

                        //only append attributes for properties that can be input as string values
                        if (useAttribute) {
                            Element attribute = document.createElement("xsd:attribute");
                            attribute.setAttribute("name", aInfo.getName());
                            attribute.appendChild(getDocAnnotation(document, doc, className, aInfo.getName(),
                                    aInfo.getValueType().getName()));
                            attributeProperties.add(attribute);
                        }
                    }
                }

                //spring:property element
                Element nestedPropertiesElement = document.createElement("xsd:element");
                nestedPropertiesElement.setAttribute("ref", "spring:property");
                nestedPropertiesElement.setAttribute("minOccurs", "0");
                nestedPropertiesElement.setAttribute("maxOccurs", "unbounded");
                sequence.appendChild(nestedPropertiesElement);

                //extension for attributes if this is a sub-class of component
                if (isComponent) {
                    extension.appendChild(sequence);
                } else {
                    complexType.appendChild(sequence);
                }

                //add parent to base types (ie, not component child classes)
                if (!isComponent || isComponentBase) {
                    Element parentAttribute = document.createElement("xsd:attribute");
                    parentAttribute.setAttribute("name", "parent");
                    parentAttribute.setAttribute("type", "xsd:string");
                    attributeProperties.add(parentAttribute);
                }

                //add anyAttribute to allow any arbitrary attribute (ie, dot notation nested property)
                Element anyAttribute = document.createElement("xsd:anyAttribute");
                anyAttribute.setAttribute("processContents", "skip");
                attributeProperties.add(anyAttribute);

                //do not append attributes here if componentBase (it will get its attributes through extension only)
                if (isComponentBase) {
                    componentAttributeElements = attributeProperties;
                } else {
                    for (Element attribute : attributeProperties) {
                        if (isComponent) {
                            extension.appendChild(attribute);
                        } else {
                            complexType.appendChild(attribute);
                        }
                    }
                }

                types.add(complexType);

                //add all tag types to relevant element objects
                appendElementObjects(document, elementObjects, clazz, tagMap);

                //create the tag type element for the currentType
                Element typeElement = document.createElement("xsd:element");
                typeElement.setAttribute("name", typeInfo.getTag());
                typeElement.setAttribute("type", currentType + "-type");
                typeElement.appendChild(getDocAnnotation(document, doc, className, null, null));
                elements.add(typeElement);

                //generate the remaining tag type elements for the rest of the tags of this class
                Set<String> tagKeys = tagMap.keySet();
                for (String key : tagKeys) {
                    String tag = tagMap.get(key).getTag();
                    if (!tag.equals(currentType)) {
                        Element element = document.createElement("xsd:element");
                        element.setAttribute("name", tag);
                        element.setAttribute("type", currentType + "-type");
                        element.appendChild(getDocAnnotation(document, doc, className, null, null));
                        elements.add(element);
                    }
                }
            }

            //fill in the schema with the collected pieces and write it out
            fillAndWriteSchema(builder, elements, types, elementObjects, componentAttributeElements);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Fills in the schema documents with the content passed in and writes them out.  Multiple schema files and
     * includes
     * are used due to a file size limitation of 45k lines in intelliJ for xsd files.
     *
     * @param builder documentBuilder to build documents with
     * @param elements the elements
     * @param types the element types
     * @param elementObjects the backing element objects (represent java classes)
     * @param componentAttributeElements a list of elements that are xsd:attribute, representing attributes of the
     * component type (used for extension)
     * @throws TransformerException
     * @throws IOException
     */
    private static void fillAndWriteSchema(DocumentBuilder builder, List<Element> elements, List<Element> types,
            Map<String, Element> elementObjects,
            List<Element> componentAttributeElements) throws TransformerException, IOException {

        //create top level schema
        Document topLevelDocument = builder.newDocument();
        Element schema = getSchemaInstance(topLevelDocument);

        //necessary include
        Element include = topLevelDocument.createElement("xsd:include");
        include.setAttribute("schemaLocation", "krad-elements.xsd");
        schema.appendChild(include);

        //append schema
        topLevelDocument.appendChild(schema);

        //start elements document
        Document elementsDocument = builder.newDocument();
        schema = getSchemaInstance(elementsDocument);
        List<Document> documentsToWrite = new ArrayList<Document>();

        //add all elements
        for (Element element : elements) {
            schema.appendChild(elementsDocument.importNode(element, true));
        }

        //append schema
        elementsDocument.appendChild(schema);

        //start baseTypesDocument
        Document baseTypesDocument = builder.newDocument();
        schema = getSchemaInstance(baseTypesDocument);

        //necessary include
        include = baseTypesDocument.createElement("xsd:include");
        include.setAttribute("schemaLocation", "krad-elements.xsd");
        schema.appendChild(include);

        //create basicList type
        Element basicListType = baseTypesDocument.createElement("xsd:complexType");
        basicListType.setAttribute("name", "basicList-type");

        Element basicListSequence = baseTypesDocument.createElement("xsd:sequence");
        Element basicListElement = baseTypesDocument.createElement("xsd:element");
        basicListElement.setAttribute("minOccurs", "0");
        basicListElement.setAttribute("maxOccurs", "unbounded");
        basicListElement.setAttribute("ref", "spring:value");

        Element basicListMergeAttribute = baseTypesDocument.createElement("xsd:attribute");
        basicListMergeAttribute.setAttribute("name", "merge");
        basicListMergeAttribute.setAttribute("type", "xsd:boolean");

        basicListSequence.appendChild(basicListElement);
        basicListType.appendChild(basicListSequence);
        basicListType.appendChild(basicListMergeAttribute);
        schema.appendChild(basicListType);

        //add component attributes type for extension
        Element componentAttributesType = baseTypesDocument.createElement("xsd:complexType");
        componentAttributesType.setAttribute("name", "componentAttributes-type");
        for (Element attribute : componentAttributeElements) {
            componentAttributesType.appendChild(baseTypesDocument.importNode(attribute, true));
        }
        schema.appendChild(componentAttributesType);

        //add all elementObjects complexTypes
        for (String objectName : elementObjects.keySet()) {
            schema.appendChild(baseTypesDocument.importNode(elementObjects.get(objectName), true));
        }

        //append schema
        baseTypesDocument.appendChild(schema);

        //start types schemas
        Document typesDocument = builder.newDocument();
        schema = getSchemaInstance(typesDocument);

        int startIndex = 0;
        int endIndex = 60;

        if (endIndex > types.size()) {
            endIndex = types.size();
        }

        boolean complete = false;

        while (!complete) {
            List<Element> typesSubList = types.subList(startIndex, endIndex);

            include = typesDocument.createElement("xsd:include");
            include.setAttribute("schemaLocation", "krad-baseTypes.xsd");
            schema.appendChild(include);

            //add all types
            for (Element type : typesSubList) {
                schema.appendChild(typesDocument.importNode(type, true));
            }

            //add to write list
            typesDocument.appendChild(schema);
            documentsToWrite.add(typesDocument);

            //setup next subList indices
            startIndex = endIndex;
            endIndex = endIndex + 60;

            if (endIndex > types.size()) {
                endIndex = types.size();
            }

            if (startIndex == types.size()) {
                complete = true;
            }

            //reset document and schema for next phase
            typesDocument = builder.newDocument();
            schema = getSchemaInstance(typesDocument);
        }

        Node elementsSchema = elementsDocument.getFirstChild();

        for (int i = 0; i < documentsToWrite.size(); i++) {
            //write includes in element document
            include = elementsDocument.createElement("xsd:include");
            include.setAttribute("schemaLocation", "krad-types" + (i + 1) + ".xsd");
            elementsSchema.insertBefore(include, elementsSchema.getFirstChild());
        }

        //write out all the documents
        writeDocument(topLevelDocument, "krad-schema.xsd");
        writeDocument(elementsDocument, "krad-elements.xsd");
        writeDocument(baseTypesDocument, "krad-baseTypes.xsd");

        int part = 1;
        for (Document document : documentsToWrite) {
            writeDocument(document, "krad-types" + part + ".xsd");
            part++;
        }
    }

    /**
     * Writes the document out with the provided documentName to the current directory
     *
     * @param document document to be written
     * @param documentName name of document to write to
     * @throws TransformerException
     * @throws IOException
     */
    private static void writeDocument(Document document, String documentName) throws TransformerException, IOException {
        File file = new File("./" + documentName);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new DOMSource(document), new StreamResult(new FileWriter(file)));
    }

    /**
     * Gets a new schema element instance with the krad namespace
     *
     * @param document the document
     * @return schema element with properties/imports filled in
     */
    private static Element getSchemaInstance(Document document) {
        //set up base schema tag
        Element schema = document.createElement("xsd:schema");
        schema.setAttribute("xmlns", "http://www.kuali.org/krad/schema");
        schema.setAttribute("targetNamespace", "http://www.kuali.org/krad/schema");
        schema.setAttribute("elementFormDefault", "qualified");
        schema.setAttribute("attributeFormDefault", "unqualified");

        schema.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        schema.setAttribute("xmlns:spring", "http://www.springframework.org/schema/beans");
        schema.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        schema.setAttribute("xsi:schemaLocation",
                "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd"
                        + " http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.0.xsd");

        //add spring import
        Element springImport = document.createElement("xsd:import");
        springImport.setAttribute("namespace", "http://www.springframework.org/schema/beans");
        schema.appendChild(springImport);

        return schema;
    }

    /**
     * Initializes an Element object for every org.kuali class in the classKeys set, to be filled in later by
     * appendElementObjects
     *
     * @param document the document
     * @param classKeys the classKeys which represent the class names
     * @param elementObjects the map to be initialized with the Elements created by this method
     * @throws ClassNotFoundException
     */
    private static void initializeElementObjects(Document document, Set<String> classKeys,
            Map<String, Element> elementObjects) throws ClassNotFoundException {
        //create/init default element object types
        for (String className : classKeys) {
            Class clazz = Class.forName(className);

            while (!clazz.equals(Object.class)) {
                //add tag to interface object elements
                for (Class currentInterface : clazz.getInterfaces()) {
                    //skip non-kuali interfaces
                    if (!currentInterface.getName().startsWith("org.kuali")) {
                        continue;
                    }

                    Element elementObject = document.createElement("xsd:complexType");
                    elementObject.setAttribute("name", currentInterface.getName());

                    Element choice = document.createElement("xsd:choice");
                    choice.setAttribute("minOccurs", "0");
                    choice.setAttribute("maxOccurs", "unbounded");
                    Element springBean = document.createElement("xsd:element");
                    springBean.setAttribute("ref", "spring:bean");
                    Element springRef = document.createElement("xsd:element");
                    springRef.setAttribute("ref", "spring:ref");

                    choice.appendChild(springRef);
                    choice.appendChild(springBean);
                    elementObject.appendChild(choice);
                    elementObjects.put(currentInterface.getName(), elementObject);

                }

                //add tag to class object elements
                Element elementObject = document.createElement("xsd:complexType");
                elementObject.setAttribute("name", clazz.getName());

                Element choice = document.createElement("xsd:choice");
                choice.setAttribute("minOccurs", "0");
                choice.setAttribute("maxOccurs", "unbounded");
                Element springBean = document.createElement("xsd:element");
                springBean.setAttribute("ref", "spring:bean");
                Element springRef = document.createElement("xsd:element");
                springRef.setAttribute("ref", "spring:ref");

                choice.appendChild(springRef);
                choice.appendChild(springBean);
                elementObject.appendChild(choice);
                elementObjects.put(clazz.getName(), elementObject);

                //process next superClass
                clazz = clazz.getSuperclass();
            }

        }
    }

    /**
     * Fill in the relevant element objects with the tags that are valid for the clazz type passed in
     *
     * @param document the Document
     * @param elementObjects the elementObject map containing the preinitialized elementObjects to fill in
     * @param clazz the class to be analyzed
     * @param tagMap the map of tags to be added to the relevant element object types
     */
    private static void appendElementObjects(Document document, Map<String, Element> elementObjects, Class clazz,
            Map<String, BeanTagInfo> tagMap) {
        Element elementObject = elementObjects.get(clazz.getName());
        Set<String> tagKeys = tagMap.keySet();

        while (!clazz.equals(Object.class)) {
            List<String> added = new ArrayList<String>();
            //add tag to interface object elements
            for (Class currentInterface : clazz.getInterfaces()) {
                if (currentInterface.getName().startsWith("org.kuali")) {
                    elementObject = elementObjects.get(currentInterface.getName());
                    for (String key : tagKeys) {
                        String tag = tagMap.get(key).getTag();
                        if (!added.contains(tag)) {
                            added.add(tag);
                            Element iRef = document.createElement("xsd:element");
                            iRef.setAttribute("ref", tag);
                            elementObject.getChildNodes().item(0).appendChild(iRef);
                        }
                    }
                }
            }

            //add tag to class object elements
            elementObject = elementObjects.get(clazz.getName());
            added = new ArrayList<String>();
            for (String key : tagKeys) {
                String tag = tagMap.get(key).getTag();
                if (!added.contains(tag)) {
                    added.add(tag);
                    Element ref = document.createElement("xsd:element");
                    ref.setAttribute("ref", tag);
                    elementObject.getChildNodes().item(0).appendChild(ref);

                }
            }

            //process next superClass
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Get the documentation annonation element for the class or property information passed in
     *
     * @param document the document
     * @param doc the ResourceBundle documentation resource
     * @param className name of the class to get documentation for.  If property and property type are not supplied,
     * returns the class documentation Element
     * @param property (optional) when supplied with propertyType the Element returned will be the property
     * documentation
     * @param propertyType (optional) must be supplied with property, the property's type
     * @return xsd:annotation Element representing the documentation for the class/property
     */
    private static Element getDocAnnotation(Document document, ResourceBundle doc, String className, String property,
            String propertyType) {
        try {
            Class clazz = Class.forName(className);

            Element annotation = document.createElement("xsd:annotation");
            Element documentation = document.createElement("xsd:documentation");
            documentation.setAttribute("source", clazz.getName());
            documentation.setAttribute("xml:lang", "en");

            String content = "documentation not available";

            if (property == null || propertyType == null) {
                if (doc.containsKey(clazz.getName())) {
                    content = "Backing Class: " + className + "\n\n" + doc.getString(clazz.getName());
                }
            } else {
                int begin = 0;
                int end = propertyType.length();

                if (propertyType.lastIndexOf('.') != -1) {
                    begin = propertyType.lastIndexOf('.') + 1;
                }

                if (propertyType.indexOf('<') != -1) {
                    end = propertyType.indexOf('<');
                }
                String key = clazz.getName() + "|" + property + "|" + propertyType.substring(begin, end);

                if (doc.containsKey(key)) {
                    content = doc.getString(key);
                } else {
                    //find the documentation content for this property on a parent class
                    boolean foundContent = false;
                    while (!clazz.equals(Object.class) && !foundContent) {
                        for (Class currentInterface : clazz.getInterfaces()) {
                            if (currentInterface.getName().startsWith("org.kuali")) {
                                key = currentInterface.getName() + "|" + property + "|" + propertyType.substring(begin,
                                        end);
                                foundContent = doc.containsKey(key) && StringUtils.isNotBlank(doc.getString(key));
                            }
                        }

                        if (foundContent) {
                            break;
                        }

                        key = clazz.getName() + "|" + property + "|" + propertyType.substring(begin, end);
                        foundContent = doc.containsKey(key) && StringUtils.isNotBlank(doc.getString(key));
                        clazz = clazz.getSuperclass();
                    }

                    if (foundContent) {
                        content = doc.getString(key);
                    }
                }
            }
            content = content.replaceAll("\\<li\\>", "\n-");
            content = content.replaceAll("\\<\\\\ol\\>", "\n");
            content = content.replaceAll("\\<\\\\ul\\>", "\n");
            content = content.replaceAll("\\<.*?\\>", "");
            content = content.replaceAll("\\{\\@code\\s(.*?)\\}", "$1");
            content = content.replaceAll("\\{\\@link\\s(.*?)\\}", "$1");
            content = content.replaceAll("\\{\\@see\\s(.*?)\\}", "$1");
            CDATASection cdata = document.createCDATASection(content);
            documentation.appendChild(cdata);

            //append doc
            annotation.appendChild(documentation);

            return annotation;
        } catch (Exception e) {
            throw new RuntimeException("class not found ", e);
        }
    }

    /**
     * Get the extension for a list type, this essentially adds the merge attribute onto list and map types
     *
     * @param document the document
     * @param aInfo attribute info for this element
     * @param attrType the xsd attribute type for this element
     * @return element with an extension that adds the merge attribute
     */
    private static Element getListExtension(Document document, BeanTagAttributeInfo aInfo, String attrType) {
        Element complexType = document.createElement("xsd:complexType");
        Element simpleContent = document.createElement("xsd:complexContent");
        Element extension = document.createElement("xsd:extension");

        if (aInfo.getGenericType() instanceof ParameterizedType
                && ((ParameterizedType) aInfo.getGenericType()).getActualTypeArguments().length == 1) {
            String genericParam = ((ParameterizedType) aInfo.getGenericType()).getActualTypeArguments()[0].toString();
            if (genericParam.contains("org.kuali") && !genericParam.contains("<")) {
                extension.setAttribute("base", genericParam.substring(genericParam.indexOf("org.kuali")));
            }
        }

        if (!extension.hasAttribute("base") && (attrType.equals("spring:mapType") || attrType.equals(
                "spring:listOrSetType"))) {
            extension.setAttribute("base", attrType);
        }

        Element mergeAttribute = document.createElement("xsd:attribute");
        mergeAttribute.setAttribute("name", "merge");
        mergeAttribute.setAttribute("type", "xsd:boolean");

        extension.appendChild(mergeAttribute);
        simpleContent.appendChild(extension);
        complexType.appendChild(simpleContent);

        if (extension.hasAttribute("base")) {
            return complexType;
        } else {
            return null;
        }
    }

    /**
     * Loads the list of class that have an associated custom schema.
     *
     * @param file - The file with the class list
     */
    private static void loadCustomTagClasses(String file) {
        ArrayList<String> classes = getClassList(file);

        customTagClasses = new ArrayList<Class<?>>();

        for (int i = 0; i < classes.size(); i++) {
            try {
                customTagClasses.add(Class.forName(classes.get(i).trim()));
            } catch (Exception e) {
                LOG.error("Class not Found : " + classes.get(i), e);
            }
        }
    }

    /**
     * Load the attribute information of the properties in the class repersented by the new tag.
     *
     * @param tagName - The name of the xml tag being created.
     * @param tagClass - The class being defined by the xml tag.
     */
    private static void loadAttributeProperties(String tagName, Class<?> tagClass) {
        Map<String, BeanTagAttributeInfo> entries = new HashMap<String, BeanTagAttributeInfo>();

        entries.putAll(getAttributes(tagClass));

        attributeProperties.put(tagName, entries);
    }

    /**
     * Creates a map of entries the properties marked by the annotation of the bean class.
     *
     * @param tagClass - The class being mapped for attributes.
     * @return Return a map of the properties found in the class with there associated information.
     */
    private static Map<String, BeanTagAttributeInfo> getAttributes(Class<?> tagClass) {
        Map<String, BeanTagAttributeInfo> entries = new HashMap<String, BeanTagAttributeInfo>();

        try {
            // Search the methods of the class using reflection for the attribute annotation
            Method methods[] = tagClass.getMethods();
            for (int i = 0; i < methods.length; i++) {
                BeanTagAttribute attribute = methods[i].getAnnotation(BeanTagAttribute.class);
                if (attribute != null) {
                    BeanTagAttributeInfo info = new BeanTagAttributeInfo();
                    info.setName(getFieldName(methods[i].getName()));
                    info.setType(attribute.type());
                    info.setValueType(methods[i].getReturnType());
                    info.setGenericType(methods[i].getGenericReturnType());
                    validateBeanAttributes(tagClass.getName(), attribute.name(), entries);
                    entries.put(attribute.name(), info);
                }
            }
        } catch (Throwable e) {
            //skip bad entry
        }

        return entries;
    }

    /**
     * Load the information for the xml bean tags defined in the custom schema through annotation of the represented
     * classes.
     */
    private static void loadBeanTags() {

        // Load the list of class to be searched for annotation definitions
        if (customTagClasses == null) {
            loadCustomTagClasses();
        }

        beanTags = new HashMap<String, BeanTagInfo>();

        attributeProperties = new HashMap<String, Map<String, BeanTagAttributeInfo>>();

        // For each class create the bean tag information and its associated attribute properties
        for (int i = 0; i < customTagClasses.size(); i++) {
            BeanTag[] annotations = new BeanTag[1];
            BeanTag tag = customTagClasses.get(i).getAnnotation(BeanTag.class);
            if (tag != null) {
                //single tag case
                annotations[0] = tag;
            } else {
                //multi-tag case
                BeanTags tags = customTagClasses.get(i).getAnnotation(BeanTags.class);
                if (tags != null) {
                    annotations = tags.value();
                } else {
                    //TODO throw exception instead?
                    continue;
                }
            }

            for (int j = 0; j < annotations.length; j++) {
                BeanTag annotation = annotations[j];
                BeanTagInfo info = new BeanTagInfo();
                info.setTag(annotation.name());

                if (j == 0) {
                    info.setDefaultTag(true);
                }

                info.setBeanClass(customTagClasses.get(i));
                info.setParent(annotation.parent());
                validateBeanTags(annotation.name());
                beanTags.put(annotation.name(), info);
                loadAttributeProperties(annotation.name(), customTagClasses.get(i));
            }
        }
    }

    /**
     * Retrieves the name of the property being defined by the tag by parsing the method name attached to the
     * annotation.  All annotations should be attached to the get method for the associated property.
     *
     * @param methodName - The name of the method attached to the annotation
     * @return Returns the property name associated witht he method.
     */
    private static String getFieldName(String methodName) {
        // Check if function is of the form isPropertyName()
        if (methodName.substring(0, 2).toLowerCase().compareTo("is") == 0) {
            String letter = methodName.substring(2, 3);
            return letter.toLowerCase() + methodName.substring(3, methodName.length());
        }
        // Since the annotation is attached to the get function the property name starts at the 4th letter and has been upper-cased as assumed by the Spring Beans.
        String letter = methodName.substring(3, 4);
        return letter.toLowerCase() + methodName.substring(4, methodName.length());
    }

    /**
     * Validates that the tag name is not already taken.
     *
     * @param tagName - The name of the tag for the new bean.
     * @return Returns true if the validation passes, false otherwise.
     */
    private static boolean validateBeanTags(String tagName) {
        boolean valid = true;
        String tags[] = new String[beanTags.keySet().size()];
        tags = beanTags.keySet().toArray(tags);

        for (int j = 0; j < tags.length; j++) {
            if (tagName.compareTo(tags[j]) == 0) {
                LOG.error("Duplicate tag name " + tagName);
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Validates that the tagName for the next property is not already taken.
     *
     * @param className - The name of the class being checked.
     * @param tagName - The name of the new attribute tag.
     * @param attributes - A map of the attribute tags already created
     * @return Returns true if the validation passes, false otherwise.
     */
    private static boolean validateBeanAttributes(String className, String tagName,
            Map<String, BeanTagAttributeInfo> attributes) {
        boolean valid = true;

        // Check for reserved tag names: ref, parent, abstract
        if ((tagName.compareTo("parent") == 0) || (tagName.compareTo("ref") == 0) || (tagName.compareTo("abstract")
                == 0)) {
            //LOG.error("Reserved tag name " + tagName + " in bean " + className);
            return false;
        }

        String tags[] = new String[attributes.keySet().size()];
        tags = attributes.keySet().toArray(tags);
        for (int j = 0; j < tags.length; j++) {
            if (tagName.compareTo(tags[j]) == 0) {
                LOG.error("Duplicate attribute tag name " + tagName + " in bean " + className);
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Retrieves the map of bean tags.  If the map has not been created yet the tags are loaded.
     * The Bean tag map is created using the xml tag name of the bean as the key with the value consisting of
     * information about the tag stored in a BeanTagInfo object.
     *
     * @return A map of xml tags and their associated information.
     */
    public static Map<String, BeanTagInfo> getBeanTags() {
        if (beanTags == null) {
            loadBeanTags();
        }
        if (beanTags.isEmpty()) {
            loadBeanTags();
        }
        return beanTags;
    }

    /**
     * Retrieves the map of bean tags.  If the map has not been created yet the tags are loaded.
     * The Bean tag map is created using the xml tag name of the bean as the key with the value consisting of
     * information about the tag stored in a BeanTagInfo object.
     *
     * @return A map of xml tags and their associated information.
     */
    public static Map<String, BeanTagInfo> getBeanTags(String file) {
        if (customTagClasses == null) {
            loadCustomTagClasses(file);
        }
        if (beanTags == null) {
            loadBeanTags();
        }
        if (beanTags.isEmpty()) {
            loadBeanTags();
        }
        return beanTags;
    }

    /**
     * Retrieves a map of attribute and property information for the bean tags.  if the map has not been created yet
     * the
     * bean tags are loaded.
     * The attribute map is a double layer map with the outer layer consisting of the xml tag as the key linked to a
     * inner map of all properties associated with it.  The inner map uses the attribute or xml sub tag as the key to
     * the information about the property stored in a BeanTagAttributeInfo object.
     *
     * @return A map of xml tags and their associated property information.
     */
    public static Map<String, Map<String, BeanTagAttributeInfo>> getAttributeProperties() {
        if (attributeProperties == null) {
            loadBeanTags();
        }
        if (attributeProperties.isEmpty()) {
            loadBeanTags();
        }
        return attributeProperties;
    }

    /**
     * Loads the list of classes involved in the custom schema from an xml document.  The list included in these xmls
     * can include lists from other documents so recursion is used to go through these other list and compile them all
     * together.
     *
     * @param path - The classpath resource to the list
     * @return A list of all classes to involved in the schema
     */
    private static ArrayList<String> getClassList(String path) {
        ArrayList<String> completeList = new ArrayList<String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;
            ApplicationContext app = new ClassPathXmlApplicationContext();
            InputStream stream = app.getResource(path).getInputStream();
            document = builder.parse(stream);

            // Read package names into a comma separated list
            NodeList classes = document.getElementsByTagName("class");
            String classList = "";
            for (int i = 0; i < classes.getLength(); i++) {
                classList = classList + classes.item(i).getTextContent() + ",";
            }

            // Split array into list by ,
            if (classList.length() > 0) {
                if (classList.charAt(classList.length() - 1) == ',') {
                    classList = classList.substring(0, classList.length() - 1);
                }
                String list[] = classList.split(",");
                for (int i = 0; i < list.length; i++) {
                    completeList.add(list[i]);
                }
            }

            // Add any schemas being built off of.
            NodeList includes = document.getElementsByTagName("include");
            for (int i = 0; i < includes.getLength(); i++) {
                completeList.addAll(getClassList(includes.item(i).getTextContent()));
            }

        } catch (Exception e) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document;
                File file = new File(path);
                document = builder.parse(file);

                // Read package names into a comma separated list
                NodeList classes = document.getElementsByTagName("class");
                String classList = "";
                for (int i = 0; i < classes.getLength(); i++) {
                    classList = classList + classes.item(i).getTextContent() + ",";
                }

                // Split array into list by ,
                if (classList.length() > 0) {
                    if (classList.charAt(classList.length() - 1) == ',') {
                        classList = classList.substring(0, classList.length() - 1);
                    }
                    String list[] = classList.split(",");
                    for (int i = 0; i < list.length; i++) {
                        completeList.add(list[i]);
                    }
                }

                // Add any schemas being built off of.
                NodeList includes = document.getElementsByTagName("include");
                for (int i = 0; i < includes.getLength(); i++) {
                    completeList.addAll(getClassList(includes.item(i).getTextContent()));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return completeList;
    }

}
