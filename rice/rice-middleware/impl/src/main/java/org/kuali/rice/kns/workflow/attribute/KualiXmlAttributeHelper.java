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
package org.kuali.rice.kns.workflow.attribute;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KualiXmlAttributeHelper {
    private static Log LOG = LogFactory.getLog(KualiXmlAttributeHelper.class);
    private static XPath xpath = XPathHelper.newXPath();
    private static final String testVal = "\'/[^\']*\'";// get the individual xpath tests.
    private static final String testVal2 = "/[^/]+/" + "*";// have to do this or the compiler gets confused by end comment.
    private static final String cleanVal = "[^/\']+";// get rid of / and ' in the resulting term.
    private static final String ruledataVal = "ruledata[^\']*\'([^\']*)";
    // TODO - enter JIRA
    // below removes wf:xstreamsafe( and )
    // below separates each wf:xstreamsafe() section into separate 'finds'
    private static final Pattern xPathPattern = Pattern.compile(testVal);
    private static final Pattern termPattern = Pattern.compile(testVal2);
    private static final Pattern cleanPattern = Pattern.compile(cleanVal);
    private static final Pattern targetPattern = Pattern.compile(ruledataVal);

    public static final String ATTRIBUTE_LABEL_BO_REFERENCE_PREFIX = "kuali_dd_label(";
    public static final String ATTRIBUTE_LABEL_BO_REFERENCE_SUFFIX = ")";
    public static final String ATTRIBUTE_SHORT_LABEL_BO_REFERENCE_PREFIX = "kuali_dd_short_label(";
    public static final String ATTRIBUTE_SHORT_LABEL_BO_REFERENCE_SUFFIX = ")";
    private static final String KUALI_VALUES_FINDER_REFERENCE_PREFIX = "kuali_values_finder_class(";
    private static final String KUALI_VALUES_FINDER_REFERENCE_SUFFIX = ")";
    public static final String notFound = "Label Not Found";

    private String lastXPath = "";

    /**
     * This method overrides the super class and modifies the XML that it operates on to put the name and the title in the place
     * where the super class expects to see them, even though they may no longer exist in the original XML.
     *
     * @see org.kuali.rice.kew.rule.xmlrouting.StandardGenericXMLRuleAttribute#getConfigXML()
     */

    public Element processConfigXML(Element root) {
        return this.processConfigXML(root, null);
    }

    /**
     * This method overrides the super class and modifies the XML that it operates on to put the name and the title in the place
     * where the super class expects to see them, overwriting the original title in the XML.
     *
     * @see org.kuali.rice.kew.rule.xmlrouting.StandardGenericXMLRuleAttribute#getConfigXML()
     */

    public Element processConfigXML(Element root, String[] xpathExpressionElements) {

        NodeList fields = root.getElementsByTagName("fieldDef");
        Element theTag = null;
        String docContent = "";


        /**
         * This section will check to see if document content has been defined in the configXML for the document type, by running an
         * XPath. If this is an empty list the xpath expression in the fieldDef is used to define the xml document content that is
         * added to the configXML. The xmldocument content is of this form, when in the document configXML. <xmlDocumentContent>
         * <org.kuali.rice.krad.bo.SourceAccountingLine> <amount> <value>%totaldollarAmount%</value> </amount>
         * </org.kuali.rice.krad.bo.SourceAccountingLine> </xmlDocumentContent> This class generates this on the fly, by creating an XML
         * element for each term in the XPath expression. When this doesn't apply XML can be coded in the configXML for the
         * ruleAttribute.
         *
         * @see org.kuali.rice.kew.plugin.attributes.WorkflowAttribute#getDocContent()
         */


        org.w3c.dom.Document xmlDoc = null;
        if (!xmlDocumentContentExists(root)) { // XML Document content is given because the xpath is non standard
            fields = root.getElementsByTagName("fieldDef");
            xmlDoc = root.getOwnerDocument();
        }
        for (int i = 0; i < fields.getLength(); i++) { // loop over each fieldDef
            String name = null;
            if (!xmlDocumentContentExists(root)) {
                theTag = (Element) fields.item(i);

                /*
                 * Even though there may be multiple xpath test, for example one for source lines and one for target lines, the
                 * xmlDocumentContent only needs one, since it is used for formatting. The first one is arbitrarily selected, since
                 * they are virtually equivalent in structure, most of the time.
                 */

                List<String> xPathTerms = getXPathTerms(theTag);
                if (xPathTerms.size() != 0) {
                    Node iterNode = xmlDoc.createElement("xmlDocumentContent");


                    xmlDoc.normalize();

                    iterNode.normalize();

                    /*
                     * Since this method is run once per attribute and there may be multiple fieldDefs, the first fieldDef is used
                     * to create the configXML.
                     */
                    for (int j = 0; j < xPathTerms.size(); j++) {// build the configXML based on the Xpath
                        // TODO - Fix the document content element generation
                        iterNode.appendChild(xmlDoc.createElement(xPathTerms.get(j)));
                        xmlDoc.normalize();

                        iterNode = iterNode.getFirstChild();
                        iterNode.normalize();

                    }
                    iterNode.setTextContent("%" + xPathTerms.get(xPathTerms.size() - 1) + "%");
                    root.appendChild(iterNode);
                }
            }
            theTag = (Element) fields.item(i);
            // check to see if a values finder is being used to set valid values for a field
            NodeList displayTagElements = theTag.getElementsByTagName("display");
            if (displayTagElements.getLength() == 1) {
                Element displayTag = (Element) displayTagElements.item(0);
                List valuesElementsToAdd = new ArrayList();
                for (int w = 0; w < displayTag.getChildNodes().getLength(); w++) {
                    Node displayTagChildNode = (Node) displayTag.getChildNodes().item(w);
                    if ((displayTagChildNode != null) && ("values".equals(displayTagChildNode.getNodeName()))) {
                        if (displayTagChildNode.getChildNodes().getLength() > 0) {
                            String valuesNodeText = displayTagChildNode.getFirstChild().getNodeValue();
                            String potentialClassName = getPotentialKualiClassName(valuesNodeText, KUALI_VALUES_FINDER_REFERENCE_PREFIX, KUALI_VALUES_FINDER_REFERENCE_SUFFIX);
                            if (StringUtils.isNotBlank(potentialClassName)) {
                                try {
                                    Class finderClass = Class.forName((String) potentialClassName);
                                    KeyValuesFinder finder = (KeyValuesFinder) finderClass.newInstance();
                                    NamedNodeMap valuesNodeAttributes = displayTagChildNode.getAttributes();
                                    Node potentialSelectedAttribute = (valuesNodeAttributes != null) ? valuesNodeAttributes.getNamedItem("selected") : null;
                                    for (Iterator iter = finder.getKeyValues().iterator(); iter.hasNext();) {
                                        KeyValue keyValue = (KeyValue) iter.next();
                                        Element newValuesElement = root.getOwnerDocument().createElement("values");
                                        newValuesElement.appendChild(root.getOwnerDocument().createTextNode(keyValue.getKey()));
                                        // newValuesElement.setNodeValue(KeyValue.getKey().toString());
                                        newValuesElement.setAttribute("title", keyValue.getValue());
                                        if (potentialSelectedAttribute != null) {
                                            newValuesElement.setAttribute("selected", potentialSelectedAttribute.getNodeValue());
                                        }
                                        valuesElementsToAdd.add(newValuesElement);
                                    }
                                } catch (ClassNotFoundException cnfe) {
                                    String errorMessage = "Caught an exception trying to find class '" + potentialClassName + "'";
                                    LOG.error(errorMessage, cnfe);
                                    throw new RuntimeException(errorMessage, cnfe);
                                } catch (InstantiationException ie) {
                                    String errorMessage = "Caught an exception trying to instantiate class '" + potentialClassName + "'";
                                    LOG.error(errorMessage, ie);
                                    throw new RuntimeException(errorMessage, ie);
                                } catch (IllegalAccessException iae) {
                                    String errorMessage = "Caught an access exception trying to instantiate class '" + potentialClassName + "'";
                                    LOG.error(errorMessage, iae);
                                    throw new RuntimeException(errorMessage, iae);
                                }
                            } else {
                                valuesElementsToAdd.add(displayTagChildNode.cloneNode(true));
                            }
                            displayTag.removeChild(displayTagChildNode);
                        }
                    }
                }
                for (Iterator iter = valuesElementsToAdd.iterator(); iter.hasNext();) {
                    Element valuesElementToAdd = (Element) iter.next();
                    displayTag.appendChild(valuesElementToAdd);
                }
            }
            if ((xpathExpressionElements != null) && (xpathExpressionElements.length > 0)) {
                NodeList fieldEvaluationElements = theTag.getElementsByTagName("fieldEvaluation");
                if (fieldEvaluationElements.getLength() == 1) {
                    Element fieldEvaluationTag = (Element) fieldEvaluationElements.item(0);
                    List tagsToAdd = new ArrayList();
                    for (int w = 0; w < fieldEvaluationTag.getChildNodes().getLength(); w++) {
                        Node fieldEvaluationChildNode = (Node) fieldEvaluationTag.getChildNodes().item(w);
                        Element newTagToAdd = null;
                        if ((fieldEvaluationChildNode != null) && ("xpathexpression".equals(fieldEvaluationChildNode.getNodeName()))) {
                            newTagToAdd = root.getOwnerDocument().createElement("xpathexpression");
                            newTagToAdd.appendChild(root.getOwnerDocument().createTextNode(generateNewXpathExpression(fieldEvaluationChildNode.getFirstChild().getNodeValue(), xpathExpressionElements)));
                            tagsToAdd.add(newTagToAdd);
                            fieldEvaluationTag.removeChild(fieldEvaluationChildNode);
                        }
                    }
                    for (Iterator iter = tagsToAdd.iterator(); iter.hasNext();) {
                        Element elementToAdd = (Element) iter.next();
                        fieldEvaluationTag.appendChild(elementToAdd);
                    }
                }
            }
            theTag.setAttribute("title", getBusinessObjectTitle(theTag));

        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(XmlJotter.jotNode(root));
            StringWriter xmlBuffer = new StringWriter();
            try {

                root.normalize();
                Source source = new DOMSource(root);
                Result result = new StreamResult(xmlBuffer);
                TransformerFactory.newInstance().newTransformer().transform(source, result);
            } catch (Exception e) {
                LOG.debug(" Exception when printing debug XML output " + e);
            }
            LOG.debug(xmlBuffer.getBuffer());
        }

        return root;
    }

    private String generateNewXpathExpression(String currentXpathExpression, String[] newXpathExpressionElements) {
        StringBuffer returnableString = new StringBuffer();
        for (int i = 0; i < newXpathExpressionElements.length; i++) {
            String newXpathElement = newXpathExpressionElements[i];
            returnableString.append(newXpathElement);

            /*
             * Append the given xpath expression onto the end of the stringbuffer only in the following cases - if there is only one
             * element in the string array - if there is more than one element in the string array and if the current element is not
             * the last element
             */
            if (((i + 1) != newXpathExpressionElements.length) || (newXpathExpressionElements.length == 1)) {
                returnableString.append(currentXpathExpression);
            }
        }
        return returnableString.toString();
    }

    /**
     * This method gets all of the text from the xpathexpression element.
     *
     * @param root
     * @return
     */
    private String getXPathText(Element root) {
        try {
            String textContent = null;
            Node node = (Node) xpath.evaluate(".//xpathexpression", root, XPathConstants.NODE);
            if (node != null) {
                textContent = node.getTextContent();
            }
            return textContent;
        } catch (XPathExpressionException e) {
            LOG.error("No XPath expression text found in element xpathexpression of configXML for document. " + e);
            return null;
            // throw e; Just writing labels or doing routing report.
        }
    }

    /**
     * This method uses an XPath expression to determine if the content of the xmlDocumentContent is empty
     *
     * @param root
     * @return
     */
    private boolean xmlDocumentContentExists(Element root) {
        try {
            if (((NodeList) xpath.evaluate("//xmlDocumentContent", root, XPathConstants.NODESET)).getLength() == 0) {
                return false;
            }
        } catch (XPathExpressionException e) {
            LOG.error("Error parsing xmlDocumentConfig.  " + e);
            return false;
        }
        return true;
    }

    public static String getPotentialKualiClassName(String testString, String prefixIndicator, String suffixIndicator) {
        if ((StringUtils.isNotBlank(testString)) && (testString.startsWith(prefixIndicator)) && (testString.endsWith(suffixIndicator))) {
            return testString.substring(prefixIndicator.length(), testString.lastIndexOf(suffixIndicator));
        }
        return null;
    }

    /**
     * Method to look up the title of each fieldDef tag in the RuleAttribute xml. This method checks the following items in the
     * following order:
     * <ol>
     * <li>Check for the business object name from {@link #getBusinessObjectName(Element)}. If it is not found or blank and the
     * 'title' attribute of the fieldDef tag is specified then return the value of the 'title' attribute.
     * <li>Check for the business object name from {@link #getBusinessObjectName(Element)}. If it is found try getting the data
     * dictionary label related to the business object name and the attribute name (found in the xpath expression)
     * <li>Check for the data dictionary title value using the attribute name (found in the xpath expression) and the KFS stand in
     * business object for attributes (see {@link KFSConstants#STAND_IN_BUSINESS_OBJECT_FOR_ATTRIBUTES}
     * <li>Check for the data dictionary title value using the xpath attribute name found in the xpath expression section. Use that
     * attribute name to get the label out of the KFS stand in business object for attributes (see
     * {@link KFSConstants#STAND_IN_BUSINESS_OBJECT_FOR_ATTRIBUTES}
     * <li>Check for the data dictionary title value using the xpath attribute name found in the xpath expression in the
     * wf:ruledata() section. Use that attribute name to get the label out of the KFS stand in business object for attributes (see
     * {@link KFSConstants#STAND_IN_BUSINESS_OBJECT_FOR_ATTRIBUTES}
     * </ol>
     *
     * @param root - the element of the fieldDef tag
     */
    private String getBusinessObjectTitle(Element root) {
        String businessObjectName = null;
        String businessObjectText = root.getAttribute("title");
        String potentialClassNameLongLabel = getPotentialKualiClassName(businessObjectText, ATTRIBUTE_LABEL_BO_REFERENCE_PREFIX, ATTRIBUTE_LABEL_BO_REFERENCE_SUFFIX);
        String potentialClassNameShortLabel = getPotentialKualiClassName(businessObjectText, ATTRIBUTE_SHORT_LABEL_BO_REFERENCE_PREFIX, ATTRIBUTE_SHORT_LABEL_BO_REFERENCE_SUFFIX);
        // we assume they want the long label... but allow for the short label
        boolean requestedShortLabel = false;

        if (StringUtils.isNotBlank(potentialClassNameLongLabel)) {
            businessObjectName = potentialClassNameLongLabel;
        } else if (StringUtils.isNotBlank(potentialClassNameShortLabel)) {
            businessObjectName = potentialClassNameShortLabel;
            requestedShortLabel = true;
        }
        if (StringUtils.isNotBlank(businessObjectName)) {
            DataDictionaryService DDService = KRADServiceLocatorWeb.getDataDictionaryService();

            String title = null;
            String targetVal = lastXPath; // Assume the attribute is the last term in the XPath expression

            if (LOG.isErrorEnabled()) {
                LOG.debug("Finding title in BO=" + businessObjectName + " ObjectName=" + targetVal);
            }

            if (StringUtils.isNotBlank(targetVal)) {
                // try to get the label based on the bo name and xpath attribute
                if (requestedShortLabel) {
                    title = DDService.getAttributeShortLabel(businessObjectName, targetVal);
                } else {
                    title = DDService.getAttributeLabel(businessObjectName, targetVal);
                }
                if (StringUtils.isNotBlank(title)) {
                    return title;
                }
            }
            // try to get the label based on the business object and xpath ruledata section
            targetVal = getRuleData(root);
            if (LOG.isErrorEnabled()) {
                LOG.debug("Finding title in BO=" + businessObjectName + " ObjectName=" + targetVal);
            }
            if (StringUtils.isNotBlank(targetVal)) {
                title = DDService.getAttributeLabel(businessObjectName, targetVal);
                if (StringUtils.isNotBlank(title)) {
                    return title;
                }
            }
            // If haven't found a label yet, its probably because there is no xpath. Use the name attribute to determine the BO
            // attribute to use.
            targetVal = root.getAttribute("name");
            if (LOG.isErrorEnabled()) {
                LOG.debug("Finding title in BO=" + businessObjectName + " ObjectName=" + targetVal);
            }
            title = DDService.getAttributeLabel(businessObjectName, targetVal);

            if (StringUtils.isNotBlank(title)) {
                return title;
            }
        }
        // return any potentially hard coded title info
        else if ((StringUtils.isNotBlank(businessObjectText)) && (StringUtils.isBlank(businessObjectName))) {
            return businessObjectText;
        }
        return notFound;

    }

    /**
     * This method gets the contents of the ruledata function in the xpath statement in the XML
     *
     * @param root
     * @return
     */
    private String getRuleData(Element root) {
        String xPathRuleTarget = getXPathText(root);

        // This pattern may need to change to get the last stanza of the xpath
        if (StringUtils.isNotBlank(xPathRuleTarget)) {
            Matcher ruleTarget = targetPattern.matcher(xPathRuleTarget);
            if (ruleTarget.find()) {
                xPathRuleTarget = ruleTarget.group(1);
            }
        }
        return xPathRuleTarget;
    }

    private List<String> getXPathTerms(Element myTag) {

        Matcher xPathTarget;
        String firstMatch;
        List<String> xPathTerms = new ArrayList();
        String allText = getXPathText(myTag);// grab the whole xpath expression
        if (StringUtils.isNotBlank(allText)) {
            xPathTarget = xPathPattern.matcher(allText);
            Matcher termTarget;
            Matcher cleanTarget;
            int theEnd = 0;// Have to define this or the / gets used up with the match and every other term is returned.

            xPathTarget.find(theEnd);
            theEnd = xPathTarget.end() - 1;
            firstMatch = xPathTarget.group();


            termTarget = termPattern.matcher(firstMatch);
            int theEnd2 = 0;
            while (termTarget.find(theEnd2)) { // get each term, clean them up, and add to the list.
                theEnd2 = termTarget.end() - 1;
                cleanTarget = cleanPattern.matcher(termTarget.group());
                cleanTarget.find();
                lastXPath = cleanTarget.group();
                xPathTerms.add(lastXPath);

            }
        }
        return xPathTerms;
    }

    private String getLastXPath(Element root) {
        List<String> tempList = getXPathTerms(root);
        return tempList.get(tempList.size());
    }
}
