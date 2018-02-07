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
package org.kuali.rice.kew.docsearch.xml;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.impex.xml.XmlConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Immutable object that encapsulates the XML searchable attribute content
 */
class XMLSearchableAttributeContent {
    private static final Logger LOG = Logger.getLogger(XMLSearchableAttributeContent.class);

    private ExtensionDefinition def;
    private Element attributeConfig;
    private Node searchingConfig;
    private String searchContent;
    private Map<String, FieldDef> fieldDefs;

    XMLSearchableAttributeContent(ExtensionDefinition ed) {
        this.def = ed;
    }

    XMLSearchableAttributeContent(String configXML) throws TransformerException {
        this.attributeConfig = XmlHelper.readXml(configXML).getDocumentElement();
    }

    XMLSearchableAttributeContent(Element configXML) {
        if (configXML == null) {
            throw new IllegalArgumentException("Configuration element must not be nil");
        }
        this.attributeConfig = configXML;
    }

    Node getSearchingConfig() throws XPathExpressionException, ParserConfigurationException {
        if (searchingConfig == null) {
            XPath xpath = XPathHelper.newXPath();
            // technically this should probably only be "searchingConfig", and not search the whole tree
            String searchingConfigExpr = "//searchingConfig";
            searchingConfig = (Node) xpath.evaluate(searchingConfigExpr, getAttributeConfig(), XPathConstants.NODE);
        }
        return searchingConfig;
    }

    String getSearchContent() throws XPathExpressionException, ParserConfigurationException {
        if (searchContent == null) {
            Node cfg = getSearchingConfig();
            XPath xpath = XPathHelper.newXPath();
            Node n = (Node) xpath.evaluate("xmlSearchContent", cfg, XPathConstants.NODE);
            if (n != null) {
                StringBuilder sb = new StringBuilder();
                NodeList list = n.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    sb.append(XmlJotter.jotNode(list.item(i)));
                }
                this.searchContent = sb.toString();
            }
        }
        return searchContent;
    }

    String generateSearchContent(Map<String, String> properties) throws XPathExpressionException, ParserConfigurationException {
        if (properties == null) {
            properties = new HashMap<String, String>();
        }
        // implementation quirk: if no fields were present, empty search content was returned
        List<FieldDef> fields = getFieldDefList();
        if (fields.size() == 0) {
            return "";
        }

        String searchContent = getSearchContent();

        // custom search content template is provided, evaluate it
        if (searchContent != null) {
            String generatedContent = searchContent;
            // if properties have been passed in, perform string replacement
            // NOTE: should default field <value>s also be used for substitution in addition to given properties?
            // Implementation note: if we want to be 100% backwards compatible we can't simply use a global StrSubstitutor
            // to replace all variables.  The implementation actually only replaces variables that are names of
            // *defined fields*; that means properties for fields which are not present on the attribute are NOT replaced.
            for (FieldDef field: fields) {
                if (StringUtils.isNotBlank(field.name)) {
                    String propValue = properties.get(field.name);
                    if (StringUtils.isNotBlank(propValue)) {
                        generatedContent = generatedContent.replaceAll("%" + field.name + "%", propValue);
                    }
                }
            }
            return generatedContent;
        } else { // use a default format
            // Standard doc content if no doc content is found in the searchingConfig xml.
            StringBuilder buf = new StringBuilder("<xmlRouting>");
            for (FieldDef field: fields) {
                if (StringUtils.isNotBlank(field.name)) {
                    String propValue = properties.get(field.name);
                    if (StringUtils.isNotBlank(propValue)) {
                        buf.append("<field name=\"");
                        buf.append(field.name);
                        buf.append("\"><value>");
                        buf.append(propValue);
                        buf.append("</value></field>");
                    }
                }
            }
            buf.append("</xmlRouting>");
            return buf.toString();
        }
    }

    /**
     * Returns a non-null but possibly empty list of FieldDefs
     * @return
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     */
    List<FieldDef> getFieldDefList() throws XPathExpressionException, ParserConfigurationException {
        return Collections.unmodifiableList(new ArrayList<FieldDef>(getFieldDefs().values()));
    }

    Map<String, FieldDef> getFieldDefs() throws XPathExpressionException, ParserConfigurationException {
        if (fieldDefs == null) {
            fieldDefs = new LinkedHashMap<String, FieldDef>();
            XPath xpath = XPathHelper.newXPath();
            Node searchingConfig = getSearchingConfig();
            if (searchingConfig != null) {
                NodeList list = (NodeList) xpath.evaluate("fieldDef", searchingConfig, XPathConstants.NODESET);
                for (int i = 0; i < list.getLength(); i++) {
                    FieldDef def = new FieldDef(list.item(i));
                    fieldDefs.put(def.name, def);
                }
            }
        }
        return fieldDefs;
    }

    protected Element getAttributeConfig() {
        if (attributeConfig == null) {
            try {
                String xmlConfigData = def.getConfiguration().get(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA);
                this.attributeConfig = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(xmlConfigData)))).getDocumentElement();
            } catch (Exception e) {
                String ruleAttrStr = (def == null ? null : def.getName());
                LOG.error("error parsing xml data from search attribute: " + ruleAttrStr, e);
                throw new RuntimeException("error parsing xml data from searchable attribute: " + ruleAttrStr, e);
            }
        }
        return attributeConfig;
    }

    /**
     * Encapsulates a field definition
     */
    static class FieldDef {
        final String name;
        final String title;
        final String defaultValue;
        final Display display;
        final Validation validation;
        final Visibility visibility;
        final SearchDefinition searchDefinition;
        final String fieldEvaluationExpr;
        final Boolean showResultColumn;
        final Lookup lookup;

        FieldDef(Node n) throws XPathExpressionException {
            XPath xpath = XPathHelper.newXPath();
            this.name = getStringAttr(n, "name");
            this.title= getStringAttr(n, "title");
            this.defaultValue = getNodeText(xpath, n, "value");
            this.fieldEvaluationExpr = getNodeText(xpath, n, "fieldEvaluation/xpathexpression");
            this.showResultColumn = getBoolean(xpath, n, "resultColumn/@show");
            // TODO: it might be better to invert responsibility here
            // so we can assign null values for missing entries (at least those we don't expect defaults for)
            this.display = new Display(xpath, n);
            this.validation = new Validation(xpath, n);
            this.visibility = new Visibility(xpath, n);
            this.searchDefinition = new SearchDefinition(xpath, n);
            this.lookup = new Lookup(xpath, n, name);
        }

        /**
         * Returns whether this field should be displayed in search results.  If 'resultColumn/@show' is explicitly defined
         * this value will be used, otherwise it will defer to the field criteria visibility, defaulting to 'true' if unset
         * @return
         */
        boolean isDisplayedInSearchResults() {
            return showResultColumn != null ? showResultColumn : (visibility.visible != null ? visibility.visible : true);
        }

        /**
         * Encapsulates display definition
         */
        static class Display {
            final String type;
            final String meta;
            final String formatter;
            final Collection<KeyValue> options;
            final Collection<String> selectedOptions;

            Display(XPath xpath, Node n) throws XPathExpressionException {
                type = getNodeText(xpath, n, "display/type");
                meta = getNodeText(xpath, n, "display/meta");
                formatter = getNodeText(xpath, n, "display/formatter");
                Collection<KeyValue> options = new ArrayList<KeyValue>();
                Collection<String> selectedOptions = new ArrayList<String>();
                
                NodeList nodes = (NodeList) xpath.evaluate("display[1]/values", n, XPathConstants.NODESET);
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    boolean selected = getBooleanAttr(node, "selected", false);
                    String title = getStringAttr(node, "title");
                    // TODO: test this - intent is that value without text content results in blank entry?
                    // this is to allow an empty drop down choice and can probably implemented in a better way
                    String value = node.getTextContent();
                    if (value == null) {
                        value = "";
                    }
                    options.add(new ConcreteKeyValue(value, title));
                    if (selected) {
                        selectedOptions.add(node.getTextContent());
                    }
                }

                this.options = Collections.unmodifiableCollection(options);
                this.selectedOptions = Collections.unmodifiableCollection(selectedOptions);
            }
        }

        /**
         * Encapsulates validation definition
         */
        static class Validation {
            final boolean required;
            final String regex;
            final String message;

            Validation(XPath xpath, Node n) throws XPathExpressionException {
                required = Boolean.parseBoolean(getNodeText(xpath, n, "validation/@required"));
                regex = getNodeText(xpath, n, "validation/regex");
                message = getNodeText(xpath, n, "validation/message");
            }
        }

        /**
         * Encapsulates visibility definition
         */
        static class Visibility {
            final Boolean visible;
            final String type;
            final String groupName;
            final String groupNamespace;

            Visibility(XPath xpath, Node n) throws XPathExpressionException {
                Boolean visible = null;
                String type = null;
                String groupName = null;
                String groupNamespace = null;
                Node node = (Node) xpath.evaluate("(visibility/field | visibility/column | visibility/fieldAndColumn)", n, XPathConstants.NODE); // NODE - just use first one
                if (node != null && node instanceof Element) {
                    Element visibilityEl = (Element) node;
                    type = visibilityEl.getNodeName();
                    Attr attr = visibilityEl.getAttributeNode("visible");
                    if (attr != null) {
                        visible = Boolean.valueOf(attr.getValue());
                    }
                    Node groupMember = (Node) xpath.evaluate("(" + XmlConstants.IS_MEMBER_OF_GROUP + "|" + XmlConstants.IS_MEMBER_OF_WORKGROUP + ")", visibilityEl, XPathConstants.NODE);
                    if (groupMember != null && groupMember instanceof Element) {
                        Element groupMemberEl = (Element) groupMember;
                        boolean group_def_found = false;
                        if (XmlConstants.IS_MEMBER_OF_GROUP.equals(groupMember.getNodeName())) {
                            group_def_found = true;
                            groupName = Utilities.substituteConfigParameters(groupMember.getTextContent().trim());
                            groupNamespace = Utilities.substituteConfigParameters(groupMemberEl.getAttribute(XmlConstants.NAMESPACE)).trim();
                        } else if (XmlConstants.IS_MEMBER_OF_WORKGROUP.equals(groupMember.getNodeName())) {
                            group_def_found = true;
                            LOG.warn("Rule Attribute XML is using deprecated element '" + XmlConstants.IS_MEMBER_OF_WORKGROUP +
                                     "', please use '" + XmlConstants.IS_MEMBER_OF_GROUP + "' instead.");
                            String workgroupName = Utilities.substituteConfigParameters(groupMember.getTextContent());
                            groupNamespace = Utilities.parseGroupNamespaceCode(workgroupName);
                            groupName = Utilities.parseGroupName(workgroupName);
                        }
                        if (group_def_found) {
                            if (StringUtils.isEmpty(groupName) || StringUtils.isEmpty(groupNamespace)) {
                                throw new RuntimeException("Both group name and group namespace must be present for group-based visibility.");
                            }

                            GroupService groupService = KimApiServiceLocator.getGroupService();
                            Group group = groupService.getGroupByNamespaceCodeAndName(groupNamespace, groupName);
                            UserSession session = GlobalVariables.getUserSession();
                            if (session != null) {
                             visible =  group == null ? false : groupService.isMemberOfGroup(session.getPerson().getPrincipalId(), group.getId());
                            }
                        }
                    }
                }
                this.visible = visible;
                this.type = type;
                this.groupName = groupName;
                this.groupNamespace = groupNamespace;
            }
        }

        /**
         * Encapsulates a SearchDefinition
         */
        static class SearchDefinition {
            final RangeOptions DEFAULTS = new RangeOptions(null, false, false);
            /**
             * The field search data type.  Guaranteed to be defined (defaulted if missing).
             */
            final String dataType;
            final boolean rangeSearch;
            final RangeOptions searchDef;
            final RangeOptions rangeDef;
            final RangeBound lowerBound;
            final RangeBound upperBound;

            SearchDefinition(XPath xpath, Node n) throws XPathExpressionException {
                String dataType = KewApiConstants.SearchableAttributeConstants.DEFAULT_SEARCHABLE_ATTRIBUTE_TYPE_NAME;
                // if element is missing outright, omit the defaults as well as it cannot be a ranged search
                // caller should check whether this is a ranged search
                RangeOptions searchDefDefaults = new RangeOptions();
                RangeOptions rangeDef = null;
                RangeBound lowerBound = null;
                RangeBound upperBound = null;
                boolean rangeSearch = false;
                Node searchDefNode = (Node) xpath.evaluate("searchDefinition", n, XPathConstants.NODE);
                if (searchDefNode != null) {
                    String s = getStringAttr(searchDefNode, "dataType");
                    // TODO: empty data type should really be invalid or default to something (String?)
                    if (StringUtils.isNotEmpty(s)) {
                        dataType = s;
                    }
                    // clearly there is a conflict if rangeSearch is false while range bounds are defined!
                    // this is not currently enforced
                    rangeSearch = getBooleanAttr(searchDefNode, "rangeSearch", false);

                    searchDefDefaults = new RangeOptions(xpath, searchDefNode, DEFAULTS);
                    Node rangeDefinition = (Node) xpath.evaluate("rangeDefinition", searchDefNode, XPathConstants.NODE);
                    // if range definition element is present, bounds derive settings from range definition
                    if (rangeDefinition != null) {
                        rangeDef = new RangeOptions(xpath, rangeDefinition, searchDefDefaults);
                        Node lower = (Node) xpath.evaluate("lower", rangeDefinition, XPathConstants.NODE);
                        lowerBound = lower == null ? new RangeBound(defaultInclusive(rangeDef, true)) : new RangeBound(xpath, lower, defaultInclusive(rangeDef, true));
                        Node upper = (Node) xpath.evaluate("upper", rangeDefinition, XPathConstants.NODE);
                        upperBound = upper == null ? new RangeBound(defaultInclusive(rangeDef, false)) : new RangeBound(xpath, upper, defaultInclusive(rangeDef, false));
                    } else if (rangeSearch) {
                        // otherwise if range search is specified but no rangedefinition element is present,
                        // bounds use options from search definition element
                        lowerBound = new RangeBound(defaultInclusive(searchDefDefaults, true));
                        upperBound = new RangeBound(defaultInclusive(searchDefDefaults, false));
                    }
                }
                this.dataType = dataType;
                this.rangeSearch = rangeSearch;
                this.searchDef = searchDefDefaults;
                this.rangeDef = rangeDef;
                this.lowerBound = lowerBound;
                this.upperBound = upperBound;
            }

            private static BaseRangeOptions defaultInclusive(BaseRangeOptions opts, boolean inclusive) {
                boolean inc = opts.inclusive == null ? inclusive : opts.inclusive;
                return new BaseRangeOptions(inc, opts.datePicker);
            }

            /**
             * Returns the most specific global/non-bounds options
             */
            public RangeOptions getRangeBoundOptions() {
                return rangeDef == null ? searchDef : rangeDef;
            }

            /**
             * Whether this appears to be a ranged search
             */
            public boolean isRangedSearch() {
                // this is a ranged search if
                // 1) searchDefinition declares this is a rangeSearch
                // OR
                // 2) rangeDefinition/bounds are present in searchDefinition
                return this.rangeSearch || (rangeDef != null);
            }

            /**
             * Base range options class used by search/range definition and bounds elements
             */
            static class BaseRangeOptions {
                protected final Boolean inclusive;
                protected final Boolean datePicker;

                BaseRangeOptions() {
                    this.inclusive = this.datePicker = null;
                }
                BaseRangeOptions(Boolean inclusive, Boolean datePicker) {
                    this.inclusive = inclusive;
                    this.datePicker = datePicker;
                }
                BaseRangeOptions(BaseRangeOptions defaults) {
                    this.inclusive = defaults.inclusive;
                    this.datePicker = defaults.datePicker;
                }
                BaseRangeOptions(XPath xpath, Node n, BaseRangeOptions defaults) {
                    this.inclusive = getBooleanAttr(n, "inclusive", defaults.inclusive);
                    this.datePicker = getBooleanAttr(n, "datePicker", defaults.datePicker);
                }
            }

            /**
             * Reads inclusive, caseSensitive, and datePicker options from attributes of
             * search definition and range definition elements.
             */
            static class RangeOptions extends BaseRangeOptions {
                protected final Boolean caseSensitive;
                RangeOptions() {
                    super();
                    this.caseSensitive = null;
                }
                RangeOptions(Boolean inclusive, Boolean caseSensitive, Boolean datePicker) {
                    super(inclusive, datePicker);
                    this.caseSensitive = caseSensitive;
                }
                RangeOptions(RangeOptions defaults) {
                    super(defaults);
                    this.caseSensitive = defaults.caseSensitive;
                }
                RangeOptions(XPath xpath, Node n, RangeOptions defaults) {
                    super(xpath, n, defaults);
                    this.caseSensitive = getBooleanAttr(n, "caseSensitive", defaults.caseSensitive);
                }
            }

            /**
             * Adds label to BaseRangeOptions
             */
            static class RangeBound extends BaseRangeOptions {
                final String label;
                RangeBound(BaseRangeOptions defaults) {
                    super(defaults);
                    this.label = null;
                }
                RangeBound(XPath xpath, Node n, BaseRangeOptions defaults) {
                    super(xpath, n, defaults);
                    this.label = getStringAttr(n, "label");
                }
            }
        }

        /**
         * Encapsulates a lookup definition
         * <lookup businessObjectClass="org.kuali.rice.kew.docsearch.xml.MyLookupable">
         *   <fieldConversions>
         *     <fieldConversion lookupFieldName="chart" localFieldName="MyBean.data"/>
         *   </fieldConversions>
         * </lookup>
         */
        static class Lookup {
            final String dataObjectClass;
            final Map<String, String> fieldConversions;

            Lookup(XPath xpath, Node n, String fieldName) throws XPathExpressionException {
                String dataObjectClass = null;
                Map<String, String> fieldConversions = new HashMap<String, String>();

                Node lookupNode = (Node) xpath.evaluate("lookup", n, XPathConstants.NODE);
                if (lookupNode != null) {
                    NamedNodeMap quickfinderAttributes = lookupNode.getAttributes();
                    Node dataObjectNode = quickfinderAttributes.getNamedItem("dataObjectClass");
                    if (dataObjectNode == null) {
                        // for legacy compatibility, though businessObjectClass is deprecated
                        dataObjectNode = quickfinderAttributes.getNamedItem("businessObjectClass");
                        if (dataObjectNode != null) {
                            LOG.warn("Field is using deprecated 'businessObjectClass' instead of 'dataObjectClass' for lookup definition, field name is: " + fieldName);
                        } else {
                            throw new ConfigurationException("Failed to locate 'dataObjectClass' for lookup definition.");
                        }
                    }
                    dataObjectClass = dataObjectNode.getNodeValue();
                    NodeList list = (NodeList) xpath.evaluate("fieldConversions/fieldConversion", lookupNode, XPathConstants.NODESET);
                    for (int i = 0; i < list.getLength(); i++) {
                        Node fieldConversionChildNode = list.item(i);
                        NamedNodeMap fieldConversionAttributes = fieldConversionChildNode.getAttributes();
                        // TODO: no validation on these attrs, could throw NPE
                        String lookupFieldName = fieldConversionAttributes.getNamedItem("lookupFieldName").getNodeValue();
                        String localFieldName = fieldConversionAttributes.getNamedItem("localFieldName").getNodeValue();
                        fieldConversions.put(lookupFieldName, localFieldName);
                    }
                }
                
                this.dataObjectClass = dataObjectClass;
                this.fieldConversions = Collections.unmodifiableMap(fieldConversions);
            }
        }
    }

    private static Boolean getBooleanAttr(Node n, String attributeName, Boolean dflt) {
        String nodeValue = getStringAttr(n, attributeName);
        return nodeValue == null ? dflt : Boolean.valueOf(nodeValue);
    }

    private static String getStringAttr(Node n, String attributeName) {
        Node attr = n.getAttributes().getNamedItem(attributeName);
        return attr == null ? null : attr.getNodeValue();
    }

    private static String getNodeText(XPath xpath, Node n, String expression) throws XPathExpressionException {
        Node node = (Node) xpath.evaluate(expression, n, XPathConstants.NODE);
        if (node == null) return null;
        return node.getTextContent();
    }

    private static Boolean getBoolean(XPath xpath, Node n, String expression) throws XPathExpressionException {
        String val = getNodeText(xpath, n, expression);
        return val == null ? null : Boolean.valueOf(val);
    }
}
