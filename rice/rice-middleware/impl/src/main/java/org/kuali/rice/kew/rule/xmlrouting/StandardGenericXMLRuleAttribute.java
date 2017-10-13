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
package org.kuali.rice.kew.rule.xmlrouting;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.attribute.XMLAttributeUtils;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.WorkflowAttributeXmlValidator;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * A generic WorkflowAttribute implementation that can be defined completely by XML.
 * <ol>
 *   <li>This attribute implementation takes "properties" defined on the the {@link org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition}
 *       and maps them to the param map of {@link GenericXMLRuleAttribute}, which relate directly to a set of fields defined by the
 *       XML <code>&lt;routingConfig&gt;</code> configuration.</li>
 *   <li>Application of the properties defined on the WorkflowAttributeDefinition
 *       to the actual attribute is performed in  {@link org.kuali.rice.core.framework.resourceloader.ObjectDefinitionResolver#invokeProperties(Object, java.util.Collection)}</li>
 *   <li>These params are then used to perform one of either EITHER:
 *     <ul>
 *       <li>Replace parameters of the syntax <code>%<i>field name</i>%</code> in the doc content if doc content is
 *           defined in the <code>&lt;xmlDocumentContent&gt;</code></li>
 *       <li>Generate a generic doc content, containing the parameter key/value pairs, e.g.:
 *           <blockquote>
 *           <code><pre>
 *             &lt;xmlrouting&gt;
 *               &lt;field name="color"&gt;&lt;value&gt;red&lt;/value&gt;&lt;/field&gt;
 *               &lt;field name="shape"&gt;&lt;value&gt;circle&lt;/value&gt;&lt;/field&gt;
 *             &lt;/xmlrouting&gt;
 *           </pre></code>
 *           </blockquote>
 *       </li>
 *     </ul>
 *     Currently, only parameters that match fields configured in the routingConfig are honored (the others are ignored)
 *     (NOTE: to make this even more reusable we might want to consider generating content for all parameters, even those that
 *      do not have corresponding fields)
 *   </li>
 *   <li>The routingConfig element defines a set of <code>fieldDef</code>s, each of which may have an <code>xpathexpression</code> for field evaluation.
 *       This <code>xpathexpression</code> is used to determine whether the attribute's {@link #isMatch(DocumentContent, List)} will
 *       succeed.  Each fieldDef may also have a <code>validation</code> element which supplies a regular expression against which
 *       to validate the field value (given by the param map)</li>
 * </ol>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StandardGenericXMLRuleAttribute implements GenericXMLRuleAttribute, WorkflowAttributeXmlValidator {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(StandardGenericXMLRuleAttribute.class);

    private static final String FIELD_DEF_E = "fieldDef";

    private boolean evaluateForMissingExtensions = false;

    private NodeList getFields(XPath xpath, Element root, String[] types) throws XPathExpressionException {
        final String OR = " or ";
        StringBuffer findField = new StringBuffer("//routingConfig/" + FIELD_DEF_E);
        if (types != null && types.length > 0) {
            findField.append("[");
            for (String type : types) {
                findField.append("@workflowType='" + type + "'" + OR);
                // missing workflowType is equivalent ("defaults") to ALL
                if ("ALL".equals(type)) {
                    findField.append("not(@workflowType)" + OR);
                }
            }
            if (types.length > 0) {
                // remove trailing " or "
                findField.setLength(findField.length() - OR.length());
            }
            findField.append("]");
        }

        try {
            return (NodeList) xpath.evaluate(findField.toString(), root, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            LOG.error("Error evaluating expression: '" + findField + "'");
            throw e;
        }
    }

    private List<Row> getRows(Element root, String[] types) {
        List<Row> rows = new ArrayList<Row>();
        XPath xpath = XPathHelper.newXPath();
        NodeList fieldNodeList;
        try {
            fieldNodeList = getFields(xpath, root, types);
        } catch (XPathExpressionException e) {
            LOG.error("Error evaluating fields expression");
            return rows;
        }
        if (fieldNodeList != null) {
            for (int i = 0; i < fieldNodeList.getLength(); i++) {
                Node field = fieldNodeList.item(i);
                NamedNodeMap fieldAttributes = field.getAttributes();

                List<Field> fields = new ArrayList<Field>();
                Field myField = new Field(fieldAttributes.getNamedItem("title").getNodeValue(), "", "", false, fieldAttributes.getNamedItem("name").getNodeValue(), "", false, false, null, "");
                String quickfinderService = null;
                for (int j = 0; j < field.getChildNodes().getLength(); j++) {
                    Node childNode = field.getChildNodes().item(j);
                    if ("value".equals(childNode.getNodeName())) {
                        myField.setPropertyValue(childNode.getFirstChild().getNodeValue());
                    } else if ("display".equals(childNode.getNodeName())) {
                        List<KeyValue> options = new ArrayList<KeyValue>();
                        List<String> selectedOptions = new ArrayList<String>();
                        for (int k = 0; k < childNode.getChildNodes().getLength(); k++) {
                            Node displayChildNode = childNode.getChildNodes().item(k);
                            if ("type".equals(displayChildNode.getNodeName())) {
                                myField.setFieldType(convertTypeToFieldType(displayChildNode.getFirstChild().getNodeValue()));
                            } else if ("meta".equals(displayChildNode.getNodeName())) {
                                // i don't think the rule creation support things in this node.
                                // i don't think the flex Routing report supports things in this node.
                            } else if ("values".equals(displayChildNode.getNodeName())) {
                                NamedNodeMap valuesAttributes = displayChildNode.getAttributes();
                                String optionValue = "";
                                // if element is empty then child will be null
                                Node firstChild = displayChildNode.getFirstChild();
                                if (firstChild != null) {
                                	optionValue = firstChild.getNodeValue();
                                }
                                if (valuesAttributes.getNamedItem("selected") != null) {
                                    selectedOptions.add(optionValue);
                                }
                                String title = "";
                                Node titleAttribute = valuesAttributes.getNamedItem("title");
                                if (titleAttribute != null) {
                                	title = titleAttribute.getNodeValue();
                            	}
                            	options.add(new ConcreteKeyValue(optionValue, title));
                            }
                        }
                        if (!options.isEmpty()) {
                            myField.setFieldValidValues(options);
                            if (!selectedOptions.isEmpty()) {
                                //if (Field.MULTI_VALUE_FIELD_TYPES.contains(myField.getFieldType())) {
                                //    String[] newSelectedOptions = new String[selectedOptions.size()];
                                //    int k = 0;
                                //    for (Iterator iter = selectedOptions.iterator(); iter.hasNext();) {
                                //        String option = (String) iter.next();
                                //        newSelectedOptions[k] = option;
                                //        k++;
                                //    }
                                //    myField.setPropertyValues(newSelectedOptions);
                                //} else {
                                //
                                    myField.setPropertyValue((String)selectedOptions.get(0));
                                //}
                            }
                        }
                    } else if ("lookup".equals(childNode.getNodeName())) {
						XMLAttributeUtils.establishFieldLookup(myField, childNode);
					} 
                }
                fields.add(myField);
                rows.add(new Row(fields));
            }
        }
        return rows;
    }

    private static String convertTypeToFieldType(String type) {
        if ("text".equals(type)) {
            return Field.TEXT;
        } else if ("select".equals(type)) {
            return Field.DROPDOWN;
        } else if ("radio".equals(type)) {
            return Field.RADIO;
        } else if ("quickfinder".equals(type)) {
            return Field.QUICKFINDER;
        }
        return null;
    }

    // thin interface for generating the appropriate error type
    private static interface ErrorGenerator<T> {
        T generateInvalidFieldError(Node field, String fieldName, String message);
        T generateMissingFieldError(Node field, String fieldName, String message);
    }

    private ExtensionDefinition extensionDefinition;
    private Map paramMap = new HashMap();
    private List ruleRows = new ArrayList();
    private List routingDataRows = new ArrayList();
    private boolean required;

    public StandardGenericXMLRuleAttribute() {
    }

    public void setExtensionDefinition(ExtensionDefinition extensionDefinition) {
        this.extensionDefinition = extensionDefinition;
    }

//    public boolean isMatch(DocumentContent docContent, List ruleExtensions) {
//        XPath xpath = XPathHelper.newXPath(docContent.getDocument());
//        WorkflowFunctionResolver resolver = XPathHelper.extractFunctionResolver(xpath);
//        for (Iterator iter = ruleExtensions.iterator(); iter.hasNext();) {
//            RuleExtension extension = (RuleExtension) iter.next();
//            if (extension.getRuleTemplateAttribute().getRuleAttribute().getName().equals(ruleAttribute.getName())) {
//                List extensions = new ArrayList();
//                extensions.add(extension);
//                resolver.setRuleExtensions(extensions);
//                //xpath.setXPathFunctionResolver(resolver);
//                for (Iterator iterator = extension.getExtensionValues().iterator(); iterator.hasNext();) {
//                    RuleExtensionValue value = (RuleExtensionValue) iterator.next();
//                    String findXpathExpression = "//routingConfig/" + FIELD_DEF_E + "[@name='" + value.getKey() + "']/fieldEvaluation/xpathexpression";
//                    String xpathExpression = null;
//                    try {
//                        xpathExpression = (String) xpath.evaluate(findXpathExpression, getConfigXML(), XPathConstants.STRING);
//                        LOG.debug("routingConfig XPath expression: " + xpathExpression);
//                        if (!org.apache.commons.lang.StringUtils.isEmpty(xpathExpression)) {
//                            LOG.debug("DocContent: " + docContent.getDocContent());
//                            Boolean match = (Boolean) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.BOOLEAN);
//                            LOG.debug("routingConfig match? " + match);
//                            if (match != null && !match.booleanValue()) {
//                                return false;
//                            }
//                        }
//                    } catch (XPathExpressionException e) {
//                        LOG.error("error in isMatch ", e);
//                        throw new RuntimeException("Error trying to find xml content with xpath expressions: " + findXpathExpression + " or " + xpathExpression, e);
//                    }
//                }
//                resolver.setRuleExtensions(null);
//            }
//        }
//        String findXpathExpression = "//routingConfig/globalEvaluations/xpathexpression";
//        String xpathExpression = "";
//        try {
//            NodeList xpathExpressions = (NodeList) xpath.evaluate(findXpathExpression, getConfigXML(), XPathConstants.NODESET);
//            for (int i = 0; i < xpathExpressions.getLength(); i++) {
//                Node xpathNode = xpathExpressions.item(i);
//                xpathExpression = xpathNode.getFirstChild().getNodeValue();
//                LOG.debug("global XPath expression: " + xpathExpression);
//                if (!org.apache.commons.lang.StringUtils.isEmpty(xpathExpression)) {
//                    LOG.debug("DocContent: " + docContent.getDocContent());
//                    Boolean match = (Boolean) xpath.evaluate(xpathExpression, docContent.getDocument(), XPathConstants.BOOLEAN);
//                    LOG.debug("Global match? " + match);
//                    if (match != null && !match.booleanValue()) {
//                        return false;
//                    }
//                }
//            }
//        } catch (XPathExpressionException e) {
//            LOG.error("error in isMatch ", e);
//            throw new RuntimeException("Error trying to find xml content with xpath expressions: " + findXpathExpression, e);
//        }
//        return true;
//    }

    public boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions) {
        XPath xpath = null;
        String xPathCacheKey = null;
        RouteNodeInstance rni = docContent.getRouteContext().getNodeInstance();
        if (rni != null) {
            xPathCacheKey = "xPath" + rni.getRouteNodeInstanceId() + "-" + rni.getName();
            if(docContent.getRouteContext().getParameters().containsKey(xPathCacheKey)) {
                xpath = (XPath)docContent.getRouteContext().getParameters().get(xPathCacheKey);
            } else {
                xpath = XPathHelper.newXPath(docContent.getDocument());
                docContent.getRouteContext().getParameters().put(xPathCacheKey, xpath);
            }
        } else {
            xpath = XPathHelper.newXPath(docContent.getDocument());
            docContent.getRouteContext().getParameters().put(xPathCacheKey, xpath);
        }
        WorkflowFunctionResolver resolver = XPathHelper.extractFunctionResolver(xpath);
        resolver.setRuleExtensions(ruleExtensions);
        List<String> xPathExpressionsToEvaluate = extractExpressionsToEvaluate(xpath, docContent, ruleExtensions);
        for (String xPathExpressionToEvaluate : xPathExpressionsToEvaluate) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Evaluating xPath expression: " + xPathExpressionToEvaluate);
            }
            try {
                Boolean match = (Boolean) xpath.evaluate(xPathExpressionToEvaluate, docContent.getDocument(), XPathConstants.BOOLEAN);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Expression match result: " + match);
                }
                if (match != null && !match.booleanValue()) {
                    return false;
                }
            } catch (XPathExpressionException e) {
                LOG.error("Error in isMatch ", e);
                throw new RuntimeException("Error trying to evalute xml content with xpath expression: " + xPathExpressionToEvaluate, e);
            }
        }
        return true;
    }

    /**
     * Extracts the xPath expressions that should be evaluated in order to determine whether or not the rule matches.  THis should take
     * into account the value of evaluateForMissingExtensions.
     */
    protected List<String> extractExpressionsToEvaluate(XPath xpath, DocumentContent docContent, List<RuleExtension> ruleExtensions) {
        List<String> expressionsToEvaluate = new ArrayList<String>(ruleExtensions.size() + 1);
        Element configXml = getConfigXML();
        String findFieldExpressions = "//routingConfig/" + FIELD_DEF_E + "/fieldEvaluation/xpathexpression";
        try {
            NodeList xPathExpressions = (NodeList) xpath.evaluate(findFieldExpressions, configXml, XPathConstants.NODESET);
            for (int index = 0; index < xPathExpressions.getLength(); index++) {
                Element expressionElement = (Element) xPathExpressions.item(index);
                String expression = expressionElement.getTextContent();
                if (!isEvaluateForMissingExtensions()) {
                    Node parentNode = expressionElement.getParentNode().getParentNode();
                    Node fieldAttribute = parentNode.getAttributes().getNamedItem("name");
                    if (fieldAttribute == null || StringUtils.isEmpty(fieldAttribute.getNodeValue())) {
                        throw new WorkflowRuntimeException("Could not determine field name defined on fieldDef for xpath expression: " + expression);
                    }
                    String fieldName = fieldAttribute.getNodeValue();
                    boolean foundExtension = false;
                    outer:for (RuleExtension ruleExtension : ruleExtensions) {
                        if (ruleExtension.getRuleTemplateAttribute().getRuleAttribute().getName().equals(extensionDefinition.getName())) {
                            for (String ruleExtensionValueKey : ruleExtension.getExtensionValuesMap().keySet()) {
                                if (fieldName.equals(ruleExtensionValueKey)) {
                                    foundExtension = true;
                                    break outer;
                                }
                            }
                        }
                    }
                    if (!foundExtension) {
                        // if the rule does not have an extension value for the xpath expression on the corresponding field def, let's skip it
                        continue;
                    }
                }

                if (!StringUtils.isEmpty(expression)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding routingConfig XPath expression: " + expression);
                    }
                    expressionsToEvaluate.add(expression);
                }
            }
        } catch (XPathExpressionException e) {
            throw new WorkflowRuntimeException("Failed to evalute XPath expression for fieldDefs: " + findFieldExpressions);
        }
        String findGlobalExpressions = "//routingConfig/globalEvaluations/xpathexpression";
        try {
            NodeList xPathExpressions = (NodeList) xpath.evaluate(findGlobalExpressions, configXml, XPathConstants.NODESET);
            for (int index = 0; index < xPathExpressions.getLength(); index++) {
                Element expressionElement = (Element) xPathExpressions.item(index);
                //String expression = XmlJotter.jotNode(expressionElement);
                String expression = expressionElement.getTextContent();
                if (!StringUtils.isEmpty(expression)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding global XPath expression: " + expression);
                    }
                    expressionsToEvaluate.add(expression);
                }
            }
        } catch (XPathExpressionException e) {
            throw new WorkflowRuntimeException("Failed to evalute global XPath expression: " + findGlobalExpressions);
        }
        return expressionsToEvaluate;
    }

    public List getRuleRows() {
        if (ruleRows.isEmpty()) {
            ruleRows = getRows(getConfigXML(), new String[] { "ALL", "RULE" });
        }
        return ruleRows;
    }

    private String getValidationErrorMessage(XPath xpath, Element root, String fieldName) throws XPathExpressionException {
        String findErrorMessage = "//routingConfig/" + FIELD_DEF_E + "[@name='" + fieldName + "']/validation/message";
        return (String) xpath.evaluate(findErrorMessage, root, XPathConstants.STRING);
    }

    /**
     * Performs attribute validation producing a list of errors of the parameterized type T generated by the ErrorGenerator<T>
     * @throws XPathExpressionException
     */
    private <T> List<T> validate(Element root, String[] types, Map map, ErrorGenerator<T> errorGenerator) throws XPathExpressionException {
        List<T> errors = new ArrayList();
        XPath xpath = XPathHelper.newXPath();

        NodeList nodes = getFields(xpath, root, types);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node field = nodes.item(i);
            NamedNodeMap fieldAttributes = field.getAttributes();
            String fieldName = fieldAttributes.getNamedItem("name").getNodeValue();

            LOG.debug("evaluating field: " + fieldName);
            String findValidation = "//routingConfig/" + FIELD_DEF_E + "[@name='" + fieldName + "']/validation";

            Node validationNode = (Node) xpath.evaluate(findValidation, root, XPathConstants.NODE);
            boolean fieldIsRequired = false;
            if (validationNode != null) {
                NamedNodeMap validationAttributes = validationNode.getAttributes();
                Node reqAttribNode = validationAttributes.getNamedItem("required");
                fieldIsRequired = reqAttribNode != null && "true".equalsIgnoreCase(reqAttribNode.getNodeValue());
            }

            String findRegex = "//routingConfig/" + FIELD_DEF_E + "[@name='" + fieldName + "']/validation/regex";

            String regex = null;
            Node regexNode = (Node) xpath.evaluate(findRegex, root, XPathConstants.NODE);

            if (regexNode != null && regexNode.getFirstChild() != null) {
                regex = regexNode.getFirstChild().getNodeValue();
                if (regex == null) {
                    throw new RuntimeException("Null regex text node");
                }
            }/* else {
                if (fieldIsRequired) {
                    fieldIsOnlyRequired = true;
                    LOG.debug("Setting empty regex to .+ as field is required");
                    // NOTE: ok, so technically .+ is not the same as checking merely
                    // for existence, because a field can be extant but "empty"
                    // however this has no relevance to the user as an empty field
                    // is for all intents and purposes non-existent (not-filled-in)
                    // so let's just use this regex to simplify the logic and
                    // pass everything through a regex check
                    regex = ".+";
                } else {
                    LOG.debug("Setting empty regex to .* as field is NOT required");
                    regex = ".*";
                }
            }*/

            LOG.debug("regex for field '" + fieldName + "': '" + regex + "'");

            String fieldValue = null;
            if (map != null) {
                fieldValue = (String) map.get(fieldName);
            }

            LOG.debug("field value: " + fieldValue);

            // fix up non-existent value for regex purposes only
            if (fieldValue == null) {
                fieldValue = "";
            }

            if (regex == null){
                if (fieldIsRequired) {
                    if (fieldValue.length() == 0) {
                        errors.add(errorGenerator.generateMissingFieldError(field, fieldName, getValidationErrorMessage(xpath, root, fieldName)));
                    }
                }
            } else {
                if (!Pattern.compile(regex).matcher(fieldValue).matches()) {
                    LOG.debug("field value does not match validation regex");
                    errors.add(errorGenerator.generateInvalidFieldError(field, fieldName, getValidationErrorMessage(xpath, root, fieldName)));
                }
            }
        }
        return errors;
    }

    public List getRoutingDataRows() {
        if (routingDataRows.isEmpty()) {
            routingDataRows = getRows(getConfigXML(), new String[] { "ALL", "REPORT" });
        }
        return routingDataRows;
    }

    public String getDocContent() {
        XPath xpath = XPathHelper.newXPath();
        final String findDocContent = "//routingConfig/xmlDocumentContent";
        try {
            Node xmlDocumentContent = (Node) xpath.evaluate(findDocContent, getConfigXML(), XPathConstants.NODE);

            NodeList nodes = getFields(xpath, getConfigXML(), new String[] { "ALL", "REPORT", "RULE" });
//            if (nodes == null || nodes.getLength() == 0) {
//                return "";
//            }

            if (xmlDocumentContent != null && xmlDocumentContent.hasChildNodes()) {
                // Custom doc content in the routingConfig xml.
                String documentContent = "";
                NodeList customNodes = xmlDocumentContent.getChildNodes();
                for (int i = 0; i < customNodes.getLength(); i++) {
                    Node childNode = customNodes.item(i);
                    documentContent += XmlJotter.jotNode(childNode);
                }

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node field = nodes.item(i);
                    NamedNodeMap fieldAttributes = field.getAttributes();
                    String fieldName = fieldAttributes.getNamedItem("name").getNodeValue();
                    LOG.debug("Replacing field '" + fieldName + "'");
                    Map map = getParamMap();
                    String fieldValue = (String) map.get(fieldName);
                    if (map != null && !org.apache.commons.lang.StringUtils.isEmpty(fieldValue)) {
                        LOG.debug("Replacing %" + fieldName + "% with field value: '" + fieldValue + "'");
                        documentContent = documentContent.replaceAll("%" + fieldName + "%", fieldValue);
                    } else {
                        LOG.debug("Field map is null or fieldValue is empty");
                    }
                }
                return documentContent;
            } else {
                // Standard doc content if no doc content is found in the routingConfig xml.
                StringBuffer documentContent = new StringBuffer("<xmlRouting>");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node field = nodes.item(i);
                    NamedNodeMap fieldAttributes = field.getAttributes();
                    String fieldName = fieldAttributes.getNamedItem("name").getNodeValue();
                    Map map = getParamMap();
                    if (map != null && !org.apache.commons.lang.StringUtils.isEmpty((String) map.get(fieldName))) {
                        documentContent.append("<field name=\"");
                        documentContent.append(fieldName);
                        documentContent.append("\"><value>");
                        documentContent.append((String) map.get(fieldName));
                        documentContent.append("</value></field>");
                    }
                }
                documentContent.append("</xmlRouting>");
                return documentContent.toString();
            }
        } catch (XPathExpressionException e) {
            LOG.error("error in getDocContent ", e);
            throw new RuntimeException("Error trying to find xml content with xpath expression", e);
        } catch (Exception e) {
            LOG.error("error in getDocContent attempting to find xml doc content", e);
            throw new RuntimeException("Error trying to get xml doc content.", e);
        }
    }

    public List getRuleExtensionValues() {
        List extensionValues = new ArrayList();

        XPath xpath = XPathHelper.newXPath();
        try {
            NodeList nodes = getFields(xpath, getConfigXML(), new String[] { "ALL", "RULE" });
            for (int i = 0; i < nodes.getLength(); i++) {
                Node field = nodes.item(i);
                NamedNodeMap fieldAttributes = field.getAttributes();
                String fieldName = fieldAttributes.getNamedItem("name").getNodeValue();
                Map map = getParamMap();
                if (map != null && !org.apache.commons.lang.StringUtils.isEmpty((String) map.get(fieldName))) {
                    RuleExtensionValue value = new RuleExtensionValue();
                    value.setKey(fieldName);
                    value.setValue((String) map.get(fieldName));
                    extensionValues.add(value);
                }
            }
        } catch (XPathExpressionException e) {
            LOG.error("error in getRuleExtensionValues ", e);
            throw new RuntimeException("Error trying to find xml content with xpath expression", e);
        }
        return extensionValues;
    }

    public List<RemotableAttributeError> validateRoutingData(Map paramMap) {
        this.paramMap = paramMap;
        try {
            return validate(getConfigXML(), new String[] { "ALL", "REPORT" }, paramMap, new ErrorGenerator<RemotableAttributeError>() {
                public RemotableAttributeError generateInvalidFieldError(Node field, String fieldName, String message) {
                    return RemotableAttributeError.Builder.create("routetemplate.xmlattribute.error", message).build();
                }
                public RemotableAttributeError generateMissingFieldError(Node field, String fieldName, String message) {
                    return RemotableAttributeError.Builder.create("routetemplate.xmlattribute.required.error", field.getAttributes().getNamedItem("title").getNodeValue()).build();
                }
            });
        } catch (XPathExpressionException e) {
            LOG.error("error in validateRoutingData ", e);
            throw new RuntimeException("Error trying to find xml content with xpath expression", e);
        }
    }

    public List<RemotableAttributeError> validateRuleData(Map paramMap) {
        this.paramMap = paramMap;
        try {
            return validate(getConfigXML(), new String[] { "ALL", "RULE" }, paramMap, new ErrorGenerator<RemotableAttributeError>() {
                public RemotableAttributeError generateInvalidFieldError(Node field, String fieldName, String message) {
                    return RemotableAttributeError.Builder.create("routetemplate.xmlattribute.error", message).build();
                }
                public RemotableAttributeError generateMissingFieldError(Node field, String fieldName, String message) {
                    return RemotableAttributeError.Builder.create("routetemplate.xmlattribute.required.error", field.getAttributes().getNamedItem("title").getNodeValue()).build();
                }
            });
        } catch (XPathExpressionException e) {
            LOG.error("error in validateRoutingData ", e);
            throw new RuntimeException("Error trying to find xml content with xpath expression", e);
        }
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public Element getConfigXML() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(
                    new StringReader(extensionDefinition.getConfiguration().get(
                            KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA))))).getDocumentElement();
        } catch (Exception e) {
            String str = extensionDefinition == null ? "null" : extensionDefinition.getName();
            LOG.error("error parsing xml data from rule attribute: " + str, e);
            throw new RuntimeException("error parsing xml data from rule attribute: " + str, e);
        }
    }

    // TODO: possibly simplify even further by unifying AttributeError and WorkflowServiceError
    public List<RemotableAttributeError> validateClientRoutingData() {
        LOG.debug("validating client routing data");
        try {
            return validate(getConfigXML(), new String[] { "ALL", "RULE" }, getParamMap(), new ErrorGenerator<RemotableAttributeError>() {
                public RemotableAttributeError generateInvalidFieldError(Node field, String fieldName, String message) {
                    if (org.apache.commons.lang.StringUtils.isEmpty(message)) {
                        message = "invalid field value";
                    } else {
                        LOG.info("Message: '" + message + "'");
                    }
                    return RemotableAttributeError.Builder.create(fieldName, message).build();
                }
                public RemotableAttributeError generateMissingFieldError(Node field, String fieldName, String message) {
                    return RemotableAttributeError.Builder.create(fieldName, "Attribute is required; " + message).build();
                }
            });
        } catch (XPathExpressionException e) {
            LOG.error("error in validateClientRoutingData ", e);
            throw new RuntimeException("Error trying to find xml content with xpath expression", e);
        }
    }

    public Map getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * @return the evaluateForMissingExtensions
     */
    public boolean isEvaluateForMissingExtensions() {
        return this.evaluateForMissingExtensions;
    }

    /**
     * Sets whether or not to evaluate expressions if the extension corresponding to that expressions is not present on the rule.
     * The correspondence is made by comparing the name of the field declared on the fieldDef element and the name of the
     * rule extension key.  If this value is set to true then all xpath expressions defined on all fieldDefs will be evaluated
     * regardless of whether or not the rule has a corresponding extension value.
     *
     * <p>By default this is false to preserve backward compatible behavior.
     */
    public void setEvaluateForMissingExtensions(boolean evaluateForMissingExtensions) {
        this.evaluateForMissingExtensions = evaluateForMissingExtensions;
    }


}
