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
package org.kuali.rice.krad.demo.uif.components;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.kuali.rice.core.api.util.AbstractKeyValue;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.TabGroup;
import org.kuali.rice.krad.uif.control.MultiValueControl;
import org.kuali.rice.krad.uif.element.Header;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.FormView;
import org.kuali.rice.krad.uif.view.View;
import org.springframework.util.StringUtils;

import javax.swing.text.StyleContext;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * View for the ComponentLibrary demo examples of Uif Components
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentLibraryView extends FormView {
    private static final long serialVersionUID = 3981186175467661843L;

    private String rootJavadocAddress;
    private String rootDocBookAddress;
    private String docBookAnchor;
    private String componentName;
    private String javaFullClassPath;
    private String xmlFilePath;
    private String description;
    private String usage;
    private String largeExampleFieldId;

    public static enum ExampleSize {
        SMALL, LARGE, XLARGE, WINDOW;
    }

    private ExampleSize exampleSize;

    private Group detailsGroup;

    private ComponentExhibit exhibit;
    private List<Group> demoGroups;

    /**
     * Initializes the TabGroup that contains description and usage.  Processes ths source code marked with the
     * ex: comment tags and adds them to the ComponentExhibit for this view.
     *
     * @see Component#performInitialization(org.kuali.rice.krad.uif.view.View, Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        //set page name
        this.getPage().setHeaderText(this.getComponentName());

        TabGroup tabGroup = ComponentFactory.getTabGroup();
        List<Component> tabItems = new ArrayList<Component>();

        //Description processing
        Group descriptionGroup = ComponentFactory.getVerticalBoxGroup();

        //Description header
        Header descriptionHeader = (Header) ComponentFactory.getNewComponentInstance("Uif-SubSectionHeader");
        descriptionHeader.setHeaderLevel("H3");
        descriptionHeader.setHeaderText(messageService.getMessageText("KR-SAP", null, "componentLibrary.description"));
        descriptionHeader.setRender(false);
        descriptionGroup.setHeader(descriptionHeader);

        //Description message
        List<Component> descriptionItems = new ArrayList<Component>();
        Message descriptionMessage = ComponentFactory.getMessage();
        descriptionMessage.setMessageText(description);
        descriptionItems.add(descriptionMessage);
        descriptionGroup.setItems(descriptionItems);

        tabItems.add(descriptionGroup);

        //Usage processing
        Group usageGroup = ComponentFactory.getVerticalBoxGroup();

        //Usage header
        Header usageHeader = (Header) ComponentFactory.getNewComponentInstance("Uif-SubSectionHeader");
        usageHeader.setHeaderLevel("H3");
        usageHeader.setHeaderText(messageService.getMessageText("KR-SAP", null, "componentLibrary.usage"));
        usageHeader.setRender(false);
        usageGroup.setHeader(usageHeader);

        //Usage message
        List<Component> usageItems = new ArrayList<Component>();
        Message usageMessage = ComponentFactory.getMessage();
        usageMessage.setMessageText(usage);
        usageItems.add(usageMessage);
        usageGroup.setItems(usageItems);

        tabItems.add(usageGroup);

        //Documentation processing
        if (javaFullClassPath != null) {
            processDocumentationTab(tabItems);
        }

        //set tabGroup items
        tabGroup.setItems(tabItems);

        tabGroup.addStyleClass("demo-componentDetailsTabs");

        //Add tabGroup to detailsGroup
        List<Component> detailsItems = new ArrayList<Component>();
        detailsItems.addAll(detailsGroup.getItems());
        detailsItems.add(tabGroup);
        detailsGroup.setItems(detailsItems);
        view.assignComponentIds(detailsGroup);

        //exhibit setup
        List<String> sourceCode = new ArrayList<String>();

        //process source
        processXmlSource(sourceCode);

        //setup exhibit
        exhibit.setDemoSourceCode(sourceCode);
        exhibit.setDemoGroups(this.getDemoGroups());

        if (this.getExampleSize() != null &&
                (this.getExampleSize().equals(ExampleSize.LARGE) || this.getExampleSize().equals(ExampleSize.XLARGE))) {
            exhibit.getTabGroup().addStyleClass("demo-noTabs");
            Group headerRightGroup = view.getPage().getHeader().getRightGroup();
            for (Component item : headerRightGroup.getItems()) {
                if (item instanceof InputField && ((InputField) item).getControl() instanceof MultiValueControl && item
                        .getId().equals(this.getLargeExampleFieldId())) {
                    //List<ConcreteKeyValue> keyValues = new ArrayList<ConcreteKeyValue>();
                    List<KeyValue> values = new ArrayList<KeyValue>();
                    int i = 0;
                    for (Group demoGroup : demoGroups) {
                        values.add(new ConcreteKeyValue(demoGroup.getId(), demoGroup.getHeader().getHeaderText()));
                        i++;
                    }

                    //values.addAll(keyValues);
                    ((MultiValueControl) ((InputField) item).getControl()).setOptions(values);
                    item.setRender(true);
                }
            }
        }

        if(this.getExampleSize() != null && this.getExampleSize().equals(ExampleSize.XLARGE)){
            this.addStyleClass("demo-xLargeLibraryView");
        }

        //Add detailsGroup and exhibit to page
        List<Component> pageItems = new ArrayList<Component>();
        pageItems.addAll(this.getPage().getItems());
        pageItems.add(exhibit);
        pageItems.add(detailsGroup);
        this.getPage().setItems(pageItems);
    }

    /**
     * Builds out the documentation tab content by auto-generating the content for properties and documentation and
     * adds it to the tabItems list
     *
     * @param tabItems list of tab items for component details
     */
    private void processDocumentationTab(List<Component> tabItems) {
        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        try {
            Class<?> componentClass = Class.forName(javaFullClassPath);
            Method methodsArray[] = componentClass.getMethods();

            //get top level documentation for this class
            String classMessage = messageService.getMessageText("KR-SAP", null, javaFullClassPath);

            if (classMessage == null) {
                classMessage = "NO DOCUMENTATION AVAILABLE/FOUND... we are working on it!";
            }

            //scrub class message of @link and @code
            classMessage = classMessage.replaceAll("\\{[@#]link (.*?)\\}", "<i>$1</i>");
            classMessage = classMessage.replaceAll("\\{[@#]code (.*?)\\}", "<i>$1</i>");

            //Generate schema and bean Id reference table
            String schemaTable =
                    "<table class='demo-schemaIdDocTable'><tr><th>Schema Name</th>" + "<th>Uif Bean Id</th></tr>";
            if (componentClass.isAnnotationPresent(BeanTag.class)) {
                BeanTag beanTag = componentClass.getAnnotation(BeanTag.class);
                schemaTable = schemaTable +
                        "<tr><td>" + beanTag.name() + "</td><td>" + beanTag.parent() + "</td></tr>";
                schemaTable = schemaTable + "</table>";
            } else if (componentClass.isAnnotationPresent(BeanTags.class)) {
                BeanTags beanTags = componentClass.getAnnotation(BeanTags.class);
                BeanTag[] beanTagArray = beanTags.value();
                for (BeanTag beanTag : beanTagArray) {
                    schemaTable = schemaTable +
                            "<tr><td>" + beanTag.name() + "</td><td>" + beanTag.parent() + "</td></tr>";
                }
                schemaTable = schemaTable + "</table>";
            } else {
                schemaTable = "";
            }

            String javadocTitle = messageService.getMessageText("KR-SAP", null, "componentLibrary.javaDoc");
            String kradGuideTitle = messageService.getMessageText("KR-SAP", null, "componentLibrary.kradGuide");
            String devDocumentationTitle = messageService.getMessageText("KR-SAP", null,
                    "componentLibrary.devDocumentation");
            String beanDefsTitle = messageService.getMessageText("KR-SAP", null, "componentLibrary.beanDefs");

            //build documentation links from javadoc address and docbook address/anchor
            String docLinkDiv = "<div class='demo-docLinks'> "
                    + "<label>Additional Resources:</label><a class='demo-documentationLink'"
                    + " href='"
                    + getRootJavadocAddress()
                    + javaFullClassPath.replace('.', '/')
                    + ".html' target='_blank'>"
                    + javadocTitle
                    + "</a>"
                    + "<a class='demo-documentationLink'"
                    + " href='"
                    + getRootDocBookAddress()
                    + getDocBookAnchor()
                    + "' target='_blank'>"
                    + kradGuideTitle
                    + "</a>"
                    + "</div>";

            //initialize the documentation content
            String documentationMessageContent =
                    "<H3>" + this.getComponentName() + " " + devDocumentationTitle + "</H3>" +
                            docLinkDiv + classMessage + "<H3>" + beanDefsTitle + "</H3>" + schemaTable;

            List<String> propertyDescriptions = new ArrayList<String>();
            Map<String, List<String>> inheritedProperties = new HashMap<String, List<String>>();

            List<Method> methods = Arrays.asList(methodsArray);

            //alphabetize the methods by name
            Collections.sort(methods, new Comparator<Method>() {
                @Override
                public int compare(Method method1, Method method2) {
                    String name1 = getPropName(method1);
                    String name2 = getPropName(method2);
                    return name1.compareTo(
                            name2);  //To change body of implemented methods use File | Settings | File Templates.
                }
            });

            //Process all methods on this class
            for (Method method : methods) {
                BeanTagAttribute attribute = method.getAnnotation(BeanTagAttribute.class);
                if (attribute != null) {
                    //property variables
                    String name = getPropName(method);
                    String methodClass = method.getDeclaringClass().getName();
                    String returnType = method.getReturnType().getName();
                    returnType = returnType.replaceAll("<.*?>", "");
                    String returnTypeShort = returnType.substring(returnType.lastIndexOf(".") + 1);

                    //get property documentation message
                    String key = methodClass + "|" + name + "|" + returnTypeShort;
                    String propertyMessage = messageService.getMessageText("KR-SAP", null, key);

                    if (propertyMessage == null) {
                        propertyMessage = "NO DOCUMENTATION AVAILABLE... we are working on it!";
                    }

                    //scrub property message of @link and @code
                    propertyMessage = propertyMessage.replaceAll("\\{[@#]link (.*?)\\}", "<i>$1</i>");
                    propertyMessage = propertyMessage.replaceAll("\\{[@#]code (.*?)\\}", "<i>$1</i>");

                    //wrap in link if a kuali type
                    if (returnType.startsWith("org.kuali")) {
                        returnTypeShort = "<a href='"
                                + getRootJavadocAddress()
                                + returnType.replace('.', '/')
                                + ".html' target='_blank'>"
                                + returnTypeShort
                                + "</a>";
                    }

                    //html propertyMessage content
                    propertyMessage = "<div class='demo-propertyItem'>"
                            + "<h4 class='demo-propertyName'>"
                            + name
                            + "</h4>"
                            + "<div class='demo-propertyType'>"
                            + returnTypeShort
                            + "</div>"
                            + "<div class='demo-propertyDesc'>"
                            + propertyMessage
                            + "</div></div>";

                    if (!methodClass.equals(javaFullClassPath)) {
                        //if this method comes from a parent and not this class, put it in the inheritedPropertiesMap
                        List<String> classProperties = inheritedProperties.get(methodClass);
                        if (classProperties == null) {
                            classProperties = new ArrayList<String>();
                        }
                        classProperties.add(propertyMessage);
                        inheritedProperties.put(methodClass, classProperties);
                    } else {
                        propertyDescriptions.add(propertyMessage);
                    }
                }
            }

            documentationMessageContent =
                    documentationMessageContent + "<H3>Properties</H3><div class='demo-propertiesContent'>";
            for (String desc : propertyDescriptions) {
                documentationMessageContent = documentationMessageContent + desc;
            }
            documentationMessageContent = documentationMessageContent + "</div>";

            Group documentationGroup = ComponentFactory.getVerticalBoxGroup();

            //properties header
            Header documentationHeader = (Header) ComponentFactory.getNewComponentInstance("Uif-SubSectionHeader");
            documentationHeader.setHeaderLevel("H3");
            documentationHeader.setHeaderText(messageService.getMessageText("KR-SAP", null,
                    "componentLibrary.documentation"));
            documentationHeader.setRender(false);
            documentationGroup.setHeader(documentationHeader);

            List<Component> propertiesItems = new ArrayList<Component>();
            Message propertiesMessage = ComponentFactory.getMessage();
            propertiesMessage.setParseComponents(false);
            propertiesMessage.setMessageText(documentationMessageContent);
            propertiesItems.add(propertiesMessage);

            //create the inherited properties disclosures
            if (!inheritedProperties.isEmpty()) {

                //todo sort alphabetically here?
                for (String className : inheritedProperties.keySet()) {
                    String messageContent = "";
                    List<String> inheritedPropertyDescriptions = inheritedProperties.get(className);

                    for (String desc : inheritedPropertyDescriptions) {
                        messageContent = messageContent + desc;
                    }

                    Group iPropertiesGroup = ComponentFactory.getVerticalBoxGroup();

                    //inherited properties header
                    Header iPropHeader = (Header) ComponentFactory.getNewComponentInstance("Uif-SubSectionHeader");
                    iPropHeader.setHeaderLevel("H3");
                    iPropHeader.setHeaderText(messageService.getMessageText("KR-SAP", null,
                            "componentLibrary.inheritedFrom") + " " + className);
                    //iPropHeader.setRender(false);
                    iPropertiesGroup.setHeader(iPropHeader);
                    iPropertiesGroup.getDisclosure().setRender(true);
                    iPropertiesGroup.getDisclosure().setDefaultOpen(false);

                    List<Component> iPropertiesItems = new ArrayList<Component>();
                    Message iPropertiesMessage = ComponentFactory.getMessage();
                    iPropertiesMessage.setParseComponents(false);
                    iPropertiesMessage.setMessageText(messageContent);
                    iPropertiesItems.add(iPropertiesMessage);
                    iPropertiesGroup.setItems(iPropertiesItems);

                    propertiesItems.add(iPropertiesGroup);
                }
            }

            documentationGroup.setItems(propertiesItems);

            tabItems.add(documentationGroup);
        } catch (Exception e) {
            throw new RuntimeException("Error loading class: " + javaFullClassPath, e);
        }
    }

    /**
     * Gets the property name from the method by stripping get/is and making the first letter lowercase
     *
     * @param method the Method object
     * @return the property name for the Method passed in
     */
    private String getPropName(Method method) {
        String name = method.getName();

        if (name.startsWith("get")) {
            name = name.replaceFirst("get", "");
        } else {
            name = name.replaceFirst("is", "");
        }

        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);

        return name;
    }

    /**
     * Process xml source code to be consumed by the exhibit component
     *
     * @param sourceCode list of sourceCode to be filled in, in order the group exhibit examples appear
     */
    private void processXmlSource(List<String> sourceCode) {
        Map<String, String> idSourceMap = new HashMap<String, String>();
        if (xmlFilePath != null) {
            try {
                //Get the source file
                URL fileUrl = ComponentLibraryView.class.getClassLoader().getResource(xmlFilePath);
                File file = new File(fileUrl.toURI());
                Pattern examplePattern = Pattern.compile("ex:(.*?)(\\s|(-->))");

                boolean readingSource = false;
                String currentSource = "";
                String currentId = "";

                LineIterator lineIt = FileUtils.lineIterator(file);
                while (lineIt.hasNext()) {
                    String line = lineIt.next();
                    if (line.contains("ex:") && !readingSource) {
                        //found a ex: tag and are not already reading source
                        readingSource = true;

                        Matcher matcher = examplePattern.matcher(line);
                        if (matcher.find()) {
                            currentId = matcher.group(1);
                        }

                        currentSource = idSourceMap.get(currentId) != null ? idSourceMap.get(currentId) : "";

                        if (!currentSource.isEmpty()) {
                            currentSource = currentSource + "\n";
                        }
                    } else if (line.contains("ex:") && readingSource) {
                        //stop reading source on second ex tag
                        readingSource = false;
                        idSourceMap.put(currentId, currentSource);
                    } else if (readingSource) {
                        //when reading source just continue to add it
                        currentSource = currentSource + line + "\n";
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(
                        "file not found or error while reading: " + xmlFilePath + " for source reading", e);
            }
        }

        for (Group demoGroup : demoGroups) {
            //add source to the source list by order that demo groups appear
            String groupId = demoGroup.getId();
            String source = idSourceMap.get(groupId);
            if (source != null) {
                //translate the source to something that can be displayed
                sourceCode.add(translateSource(source));
            }
        }
    }

    /**
     * Translates the source by removing chracters that the dom will misinterpret as html and to ensure
     * source spacing is correct
     *
     * @param source the original source
     * @return that translated source used in the SyntaxHighlighter of the exhibit
     */
    private String translateSource(String source) {
        //convert characters to ascii equivalent
        source = source.replace("<", "&lt;");
        source = source.replace(">", "&gt;");
        source = source.replaceAll("[ \\t]", "&#32;");

        Pattern linePattern = Pattern.compile("((&#32;)*).*?(\\n)+");
        Matcher matcher = linePattern.matcher(source);
        int toRemove = -1;

        //find the line with the least amount of spaces
        while (matcher.find()) {
            String spaces = matcher.group(1);

            int count = StringUtils.countOccurrencesOf(spaces, "&#32;");
            if (toRemove == -1 || count < toRemove) {
                toRemove = count;
            }
        }

        matcher.reset();
        String newSource = "";

        //remove the min number of spaces from each line to get them to align left properly in the viewer
        while (matcher.find()) {
            String line = matcher.group();
            newSource = newSource + line.replaceFirst("(&#32;){" + toRemove + "}", "");
        }

        //remove very last newline
        newSource = newSource.replaceAll("\\n$", "");
        //replace remaining newlines with ascii equivalent
        newSource = newSource.replace("\n", "&#010;");

        return newSource;
    }

    /**
     * ComponentLibraryView constructor
     */
    public ComponentLibraryView() {
        demoGroups = new ArrayList<Group>();
    }

    /**
     * The name of the component (to be used by this page's header)
     *
     * @return componentName the name of the component being demoed
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the componentName
     *
     * @param componentName
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Set the java path to the class being used by this component
     * TODO not yet used
     *
     * @return the java path to the class
     */
    public String getJavaFullClassPath() {
        return javaFullClassPath;
    }

    /**
     * Get the java full class path
     *
     * @param javaFullClassPath
     */
    public void setJavaFullClassPath(String javaFullClassPath) {
        this.javaFullClassPath = javaFullClassPath;
    }

    /**
     * The xml file path that contains the source being used for this demo, must start with / (relative path)
     *
     * @return the xml file path
     */
    public String getXmlFilePath() {
        return xmlFilePath;
    }

    /**
     * Set the xml file path
     *
     * @param xmlFilePath
     */
    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * The description of the component being demoed by this view
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the usage description and examples of how to use this component
     *
     * @return the usage text
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Set the usage text
     *
     * @param usage
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * The details group that will contain the description, usage, and properties tabGroup
     *
     * @return the details group
     */
    public Group getDetailsGroup() {
        return detailsGroup;
    }

    /**
     * Set the details group
     *
     * @param detailsGroup
     */
    public void setDetailsGroup(Group detailsGroup) {
        this.detailsGroup = detailsGroup;
    }

    /**
     * Gets the exhibit that will display the example, source code, and tabs to switch between examples
     *
     * @return the ComponentExhibit for this component demo view
     */
    public ComponentExhibit getExhibit() {
        return exhibit;
    }

    /**
     * Set the ComponentExhibit for this demo
     *
     * @param exhibit
     */
    public void setExhibit(ComponentExhibit exhibit) {
        this.exhibit = exhibit;
    }

    /**
     * List of groups that will demostrate the functionality fo the component being demonstrated, these groups are
     * copied directly into componentExhibit - this is an ease of use property
     *
     * @return the demoGroups
     */
    public List<Group> getDemoGroups() {
        return demoGroups;
    }

    /**
     * Set the demoGroups used for demonstrating features of the component
     *
     * @param demoGroups
     */
    public void setDemoGroups(List<Group> demoGroups) {
        this.demoGroups = demoGroups;
    }

    /**
     * The root address to the javadoc for Rice
     *
     * @return the javadoc root address
     */
    public String getRootJavadocAddress() {
        return rootJavadocAddress;
    }

    /**
     * Set the root address to the javadoc for Rice
     *
     * @param rootJavadocAddress
     */
    public void setRootJavadocAddress(String rootJavadocAddress) {
        this.rootJavadocAddress = rootJavadocAddress;
    }

    /**
     * Get the root address to the docbook for KRAD
     *
     * @return KRAD's docbook address (url)
     */
    public String getRootDocBookAddress() {
        return rootDocBookAddress;
    }

    /**
     * Set the docbook root address
     *
     * @param rootDocBookAddress
     */
    public void setRootDocBookAddress(String rootDocBookAddress) {
        this.rootDocBookAddress = rootDocBookAddress;
    }

    /**
     * The anchor in the docbook this component is described at (do not include #)
     *
     * @return the anchor name
     */
    public String getDocBookAnchor() {
        if (docBookAnchor == null) {
            return "";
        } else {
            return "#" + docBookAnchor;
        }
    }

    /**
     * Set the docBookAnchor name for the component described by this view
     *
     * @param docBookAnchor
     */
    public void setDocBookAnchor(String docBookAnchor) {
        this.docBookAnchor = docBookAnchor;
    }

    public ExampleSize getExampleSize() {
        return exampleSize;
    }

    public void setExampleSize(ExampleSize exampleSize) {
        this.exampleSize = exampleSize;
    }

    public String getLargeExampleFieldId() {
        return largeExampleFieldId;
    }

    public void setLargeExampleFieldId(String largeExampleFieldId) {
        this.largeExampleFieldId = largeExampleFieldId;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        ComponentLibraryView libraryViewCopy = (ComponentLibraryView) component;

        libraryViewCopy.setRootJavadocAddress(this.rootJavadocAddress);
        libraryViewCopy.setRootDocBookAddress(this.rootDocBookAddress);
        libraryViewCopy.setDocBookAnchor(this.docBookAnchor);
        libraryViewCopy.setComponentName(this.componentName);
        libraryViewCopy.setJavaFullClassPath(this.javaFullClassPath);
        libraryViewCopy.setXmlFilePath(this.xmlFilePath);
        libraryViewCopy.setDescription(this.description);
        libraryViewCopy.setUsage(this.usage);
        libraryViewCopy.setLargeExampleFieldId(this.largeExampleFieldId);
        libraryViewCopy.setExampleSize(this.exampleSize);

        if (this.detailsGroup != null) {
            libraryViewCopy.setDetailsGroup((Group) this.detailsGroup.copy());
        }

        if (this.exhibit != null) {
            libraryViewCopy.setExhibit((ComponentExhibit) this.exhibit.copy());
        }

        if (this.demoGroups != null) {
            List<Group> demoGroupsCopy = new ArrayList<Group>();

            for (Group demoGroup : this.demoGroups) {
                demoGroupsCopy.add((Group) demoGroup.copy());
            }
            libraryViewCopy.setDemoGroups(demoGroupsCopy);
        }
    }
}
