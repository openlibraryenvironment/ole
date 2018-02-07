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

import com.google.common.base.Function;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.search.Range;
import org.kuali.rice.core.api.search.SearchExpressionUtils;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAbstractControl;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableAttributeLookupSettings;
import org.kuali.rice.core.api.uif.RemotableDatepicker;
import org.kuali.rice.core.api.uif.RemotableHiddenInput;
import org.kuali.rice.core.api.uif.RemotableQuickFinder;
import org.kuali.rice.core.api.uif.RemotableRadioButtonGroup;
import org.kuali.rice.core.api.uif.RemotableSelect;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.framework.persistence.jdbc.sql.SQLUtils;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.docsearch.CaseAwareSearchableAttributeValue;
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implementation of a {@code SearchableAttribute} whose configuration is driven from XML.
 *
 * XML configuration must be supplied in the ExtensionDefinition configuration parameter {@link KewApiConstants#ATTRIBUTE_XML_CONFIG_DATA}.
 * Parsing of XML search configuration and generation of XML search content proceeds in an analogous fashion to {@link org.kuali.rice.kew.rule.xmlrouting.StandardGenericXMLRuleAttribute}.
 * Namely, if an <pre>searchingConfig/xmlSearchContent</pre> element is provided, its content is used as a template.  Otherwise a standard XML template is used.
 * This template is parameterized with variables of the notation <pre>%name%</pre> which are resolved by <pre>searchingConfig/fieldDef[@name]</pre> definitions.
 *
 * The XML content is not validated, but it must be well formed.
 *
 * Example 1:
 * <pre>
 *     <searchingConfig>
 *         <fieldDef name="def1" ...other attrs/>
 *             ... other config
 *         </fieldDef>
 *         <fieldDef name="def2" ...other attrs/>
 *             ... other config
 *         </fieldDef>
 *     </searchingConfig>
 * </pre>
 * Produces, when supplied with the workflow definition parameters: { def1: val1, def2: val2 }:
 * <pre>
 *     <xmlRouting>
 *         <field name="def1"><value>val1</value></field>
 *         <field name="def2"><value>val2</value></field>
 *     </xmlRouting>
 * </pre>
 *
 * Example 2:
 * <pre>
 *     <searchingConfig>
 *         <xmlSearchContent>
 *             <myGeneratedContent>
 *                 <version>whatever</version>
 *                 <anythingIWant>Once upon a %def1%...</anythingIWant>
 *                 <conclusion>Happily ever %def2%.</conclusion>
 *             </myGeneratedContent>
 *         </xmlSearchContent>
 *         <fieldDef name="def1" ...other attrs/>
 *             ... other config
 *         </fieldDef>
 *         <fieldDef name="def2" ...other attrs/>
 *             ... other config
 *         </fieldDef>
 *     </searchingConfig>
 * </pre>
 * Produces, when supplied with the workflow definition parameters: { def1: val1, def2: val2 }:
 * <pre>
 *     <myGeneratedContent>
 *         <version>whatever</version>
 *         <anythingIWant>Once upon a val1...</anythingIWant>
 *         <conclusion>Happily ever val2.</conclusion>
 *     </myGeneratedContent>
 * </pre>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StandardGenericXMLSearchableAttribute implements SearchableAttribute {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(StandardGenericXMLSearchableAttribute.class);
    private static final String FIELD_DEF_E = "fieldDef";
    /**
     * Compile-time option that controls whether we check and return errors for field bounds options that conflict with searchable attribute configuration.
     */
    private static final boolean PEDANTIC_BOUNDS_VALIDATION = true;


    @Override
    public String generateSearchContent(ExtensionDefinition extensionDefinition, String documentTypeName, WorkflowAttributeDefinition attributeDefinition) {
        Map<String, String> propertyDefinitionMap = attributeDefinition.getPropertyDefinitionsAsMap();
        try {
            XMLSearchableAttributeContent content = new XMLSearchableAttributeContent(getConfigXML(extensionDefinition));
            return content.generateSearchContent(propertyDefinitionMap);
        } catch (XPathExpressionException e) {
            LOG.error("error in getSearchContent ", e);
            throw new RuntimeException("Error trying to find xml content with xpath expression", e);
        } catch (Exception e) {
            LOG.error("error in getSearchContent attempting to find xml search content", e);
            throw new RuntimeException("Error trying to get xml search content.", e);
        }
    }

    @Override
    public List<DocumentAttribute> extractDocumentAttributes(ExtensionDefinition extensionDefinition, DocumentWithContent documentWithContent) {
        List<DocumentAttribute> searchStorageValues = new ArrayList<DocumentAttribute>();
        String fullDocumentContent = documentWithContent.getDocumentContent().getFullContent();
        if (StringUtils.isBlank(documentWithContent.getDocumentContent().getFullContent())) {
            LOG.warn("Empty Document Content found for document id: " + documentWithContent.getDocument().getDocumentId());
            return searchStorageValues;
        }
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(fullDocumentContent))));
        } catch (Exception e){
            LOG.error("error parsing docContent: "+documentWithContent.getDocumentContent(), e);
            throw new RuntimeException("Error trying to parse docContent: "+documentWithContent.getDocumentContent(), e);
        }
        XMLSearchableAttributeContent content = new XMLSearchableAttributeContent(getConfigXML(extensionDefinition));
        List<XMLSearchableAttributeContent.FieldDef> fields;
        try {
            fields = content.getFieldDefList();
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException("Error parsing searchable attribute content", xpee);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException("Error parsing searchable attribute content", pce);
        }
        XPath xpath = XPathHelper.newXPath(document);
        for (XMLSearchableAttributeContent.FieldDef field: fields) {
            if (StringUtils.isNotEmpty(field.fieldEvaluationExpr)) {
                List<String> values = new ArrayList<String>();
                try {
                    LOG.debug("Trying to retrieve node set with expression: '" + field.fieldEvaluationExpr + "'.");
                    NodeList searchValues = (NodeList) xpath.evaluate(field.fieldEvaluationExpr, document.getDocumentElement(), XPathConstants.NODESET);
                    // being that this is the standard xml attribute we will return the key with an empty value
                    // so we can find it from a doc search using this key
                    for (int j = 0; j < searchValues.getLength(); j++) {
                        Node searchValue = searchValues.item(j);
                        if (searchValue.getFirstChild() != null && (StringUtils.isNotEmpty(searchValue.getFirstChild().getNodeValue()))) {
                            values.add(searchValue.getFirstChild().getNodeValue());
                        }
                    }
                } catch (XPathExpressionException e) {
                    LOG.debug("Could not retrieve node set with expression: '" + field.fieldEvaluationExpr + "'. Trying string return type.");
                    //try for a string being returned from the expression.  This
                    //seems like a poor way to determine our expression return type but
                    //it's all I can come up with at the moment.
                    try {
                        String searchValue = (String) xpath.evaluate(field.fieldEvaluationExpr, document.getDocumentElement(), XPathConstants.STRING);
                        if (StringUtils.isNotBlank(searchValue)) {
                            values.add(searchValue);
                        }
                    } catch (XPathExpressionException xpee) {
                        LOG.error("Error retrieving string with expression: '" + field.fieldEvaluationExpr + "'", xpee);
                        throw new RuntimeException("Error retrieving string with expression: '" + field.fieldEvaluationExpr + "'", xpee);
                    }
                }

                // remove any nulls
                values.removeAll(Collections.singleton(null));
                // being that this is the standard xml attribute we will return the key with an empty value
                // so we can find it from a doc search using this key
                if (values.isEmpty()) {
                    values.add(null);
                }
                for (String value: values) {
                    DocumentAttribute searchableValue = this.setupSearchableAttributeValue(field.searchDefinition.dataType, field.name, value);
                    if (searchableValue != null) {
                        searchStorageValues.add(searchableValue);
                    }
                }
            }
        }
        return searchStorageValues;
    }

    private DocumentAttribute setupSearchableAttributeValue(String dataType, String key, String value) {
        SearchableAttributeValue attValue = DocumentSearchInternalUtils.getSearchableAttributeValueByDataTypeString(dataType);
        if (attValue == null) {
            String errorMsg = "Cannot find a SearchableAttributeValue associated with the data type '" + dataType + "'";
            LOG.error("setupSearchableAttributeValue() " + errorMsg);
            throw new RuntimeException(errorMsg);
        }
        value = (value != null) ? value.trim() : null;
        if ( (StringUtils.isNotBlank(value)) && (!attValue.isPassesDefaultValidation(value)) ) {
            String errorMsg = "SearchableAttributeValue with the data type '" + dataType + "', key '" + key + "', and value '" + value + "' does not pass default validation and cannot be saved to the database";
            LOG.error("setupSearchableAttributeValue() " + errorMsg);
            throw new RuntimeException(errorMsg);
        }
        attValue.setSearchableAttributeKey(key);
        attValue.setupAttributeValue(value);
        return attValue.toDocumentAttribute();
    }

    @Override
    public List<RemotableAttributeField> getSearchFields(ExtensionDefinition extensionDefinition, String documentTypeName) {
        List<RemotableAttributeField> searchFields = new ArrayList<RemotableAttributeField>();
        List<SearchableAttributeValue> searchableAttributeValues = DocumentSearchInternalUtils.getSearchableAttributeValueObjectTypes();

        XMLSearchableAttributeContent content = new XMLSearchableAttributeContent(getConfigXML(extensionDefinition));
        List<XMLSearchableAttributeContent.FieldDef> fields;
        try {
            fields = content.getFieldDefList();
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException("Error parsing searchable attribute configuration", xpee);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException("Error parsing searchable attribute configuration", pce);
        }
        for (XMLSearchableAttributeContent.FieldDef field: fields) {
            searchFields.add(convertFieldDef(field, searchableAttributeValues));
        }

        return searchFields;
    }

    /**
     * Converts a searchable attribute FieldDef to a RemotableAttributeField
     */
    private RemotableAttributeField convertFieldDef(XMLSearchableAttributeContent.FieldDef field, Collection<SearchableAttributeValue> searchableAttributeValues) {
        RemotableAttributeField.Builder fieldBuilder = RemotableAttributeField.Builder.create(field.name);

        fieldBuilder.setLongLabel(field.title);

        RemotableAttributeLookupSettings.Builder attributeLookupSettings = RemotableAttributeLookupSettings.Builder.create();
        fieldBuilder.setAttributeLookupSettings(attributeLookupSettings);

        // value
        if (field.defaultValue != null) {
            fieldBuilder.setDefaultValues(Collections.singletonList(field.defaultValue));
        }

        // Visibility
        applyVisibility(fieldBuilder, attributeLookupSettings, field);

        // Display
        RemotableAbstractControl.Builder controlBuilder = constructControl(field.display.type, field.display.options);
        fieldBuilder.setControl(controlBuilder);
        if ("date".equals(field.display.type)) {
            fieldBuilder.getWidgets().add(RemotableDatepicker.Builder.create());
            fieldBuilder.setDataType(DataType.DATE);
        }
        if (!field.display.selectedOptions.isEmpty()) {
            fieldBuilder.setDefaultValues(field.display.selectedOptions);
        }

        // resultcolumn
        attributeLookupSettings.setInResults(field.isDisplayedInSearchResults());

        // SearchDefinition
        // data type operations
        DataType dataType = DocumentSearchInternalUtils.convertValueToDataType(field.searchDefinition.dataType);
        fieldBuilder.setDataType(dataType);
        if (DataType.DATE == fieldBuilder.getDataType()) {
            fieldBuilder.getWidgets().add(RemotableDatepicker.Builder.create());
        }

        boolean isRangeSearchField = isRangeSearchField(searchableAttributeValues, fieldBuilder.getDataType(), field);
        if (isRangeSearchField) {
            attributeLookupSettings.setRanged(true);
            // we've established the search is ranged, so we can inspect the bounds
            attributeLookupSettings.setLowerBoundInclusive(field.searchDefinition.lowerBound.inclusive);
            attributeLookupSettings.setUpperBoundInclusive(field.searchDefinition.upperBound.inclusive);
            attributeLookupSettings.setLowerLabel(field.searchDefinition.lowerBound.label);
            attributeLookupSettings.setUpperLabel(field.searchDefinition.upperBound.label);
            attributeLookupSettings.setLowerDatePicker(field.searchDefinition.lowerBound.datePicker);
            attributeLookupSettings.setUpperDatePicker(field.searchDefinition.upperBound.datePicker);
        }

        Boolean caseSensitive = field.searchDefinition.getRangeBoundOptions().caseSensitive;
        if (caseSensitive != null) {
            attributeLookupSettings.setCaseSensitive(caseSensitive);
        }

        /**



         String formatterClass = (searchDefAttributes.getNamedItem("formatterClass") == null) ? null : searchDefAttributes.getNamedItem("formatterClass").getNodeValue();
         if (!StringUtils.isEmpty(formatterClass)) {
         try {
         myField.setFormatter((Formatter)Class.forName(formatterClass).newInstance());
         } catch (InstantiationException e) {
         LOG.error("Unable to get new instance of formatter class: " + formatterClass);
         throw new RuntimeException("Unable to get new instance of formatter class: " + formatterClass);
         }
         catch (IllegalAccessException e) {
         LOG.error("Unable to get new instance of formatter class: " + formatterClass);
         throw new RuntimeException("Unable to get new instance of formatter class: " + formatterClass);
         } catch (ClassNotFoundException e) {
         LOG.error("Unable to find formatter class: " + formatterClass);
         throw new RuntimeException("Unable to find formatter class: " + formatterClass);
         }
         }

         */

         String formatter = field.display.formatter == null ? null : field.display.formatter;
         fieldBuilder.setFormatterName(formatter);

        try {
        // Register this formatter so that you can use it later in FieldUtils when processing
            if(StringUtils.isNotEmpty(formatter)){
                Formatter.registerFormatter(Class.forName(formatter), Class.forName(formatter));
            }
        } catch (ClassNotFoundException e) {
         LOG.error("Unable to find formatter class: " + formatter);
         throw new RuntimeException("Unable to find formatter class: " + formatter);
         }


        // Lookup
        // XMLAttributeUtils.establishFieldLookup(fieldBuilder, childNode); // this code can probably die now that parsing has moved out to xmlsearchableattribcontent
        if (field.lookup.dataObjectClass != null) {
            RemotableQuickFinder.Builder quickFinderBuilder = RemotableQuickFinder.Builder.create(LookupUtils.getBaseLookupUrl(false), field.lookup.dataObjectClass);
            quickFinderBuilder.setFieldConversions(field.lookup.fieldConversions);
            fieldBuilder.getWidgets().add(quickFinderBuilder);
        }

        return fieldBuilder.build();
    }


    /**
     * Determines whether the searchable field definition is a ranged search
     * @param searchableAttributeValues the possible system {@link SearchableAttributeValue}s
     * @param dataType the UI data type
     * @return
     */
    private boolean isRangeSearchField(Collection<SearchableAttributeValue> searchableAttributeValues, DataType dataType, XMLSearchableAttributeContent.FieldDef field) {
        for (SearchableAttributeValue attValue : searchableAttributeValues)
        {
            DataType attributeValueDataType = DocumentSearchInternalUtils.convertValueToDataType(attValue.getAttributeDataType());
            if (attributeValueDataType == dataType) {
                return isRangeSearchField(attValue, field);
            }
        }
        String errorMsg = "Could not find searchable attribute value for data type '" + dataType + "'";
        LOG.error("isRangeSearchField(List, String, NamedNodeMap, Node) " + errorMsg);
        throw new WorkflowRuntimeException(errorMsg);
    }

    private boolean isRangeSearchField(SearchableAttributeValue searchableAttributeValue, XMLSearchableAttributeContent.FieldDef field) {
        // this is a ranged search if
        // 1) attribute value type allows ranged search
        boolean allowRangedSearch = searchableAttributeValue.allowsRangeSearches();
        // AND
        // 2) the searchDefinition specifies a ranged search
        return allowRangedSearch && field.searchDefinition.isRangedSearch();
    }

    /**
     * Applies visibility settings to the RemotableAttributeField
     */
    private void applyVisibility(RemotableAttributeField.Builder fieldBuilder, RemotableAttributeLookupSettings.Builder attributeLookupSettings, XMLSearchableAttributeContent.FieldDef field) {
        boolean visible = true;
        // if visibility is explicitly set, use it
        if (field.visibility.visible != null) {
            visible = field.visibility.visible;
        } else {
            if (field.visibility.groupName != null) {
                UserSession session = GlobalVariables.getUserSession();
                if (session == null) {
                    throw new WorkflowRuntimeException("UserSession is null!  Attempted to render the searchable attribute outside of an established session.");
                }
                GroupService groupService = KimApiServiceLocator.getGroupService();

                Group group = groupService.getGroupByNamespaceCodeAndName(field.visibility.groupNamespace, field.visibility.groupName);
                visible =  group == null ? false : groupService.isMemberOfGroup(session.getPerson().getPrincipalId(), group.getId());
            }
        }
        String type = field.visibility.type;
        if ("field".equals(type) || "fieldAndColumn".equals(type)) {
            // if it's not visible, coerce this field to a hidden type
            if (!visible) {
                fieldBuilder.setControl(RemotableHiddenInput.Builder.create());
            }
        }
        if ("column".equals(type) || "fieldAndColumn".equals(type)) {
            attributeLookupSettings.setInResults(visible);
        }
    }

    private RemotableAbstractControl.Builder constructControl(String type, Collection<KeyValue> options) {
        RemotableAbstractControl.Builder control = null;
        Map<String, String> optionMap = new LinkedHashMap<String, String>();
        for (KeyValue option : options) {
            optionMap.put(option.getKey(), option.getValue());
        }
        if ("text".equals(type) || "date".equals(type)) {
            control = RemotableTextInput.Builder.create();
        } else if ("select".equals(type)) {
            control = RemotableSelect.Builder.create(optionMap);
        } else if ("radio".equals(type)) {
            control = RemotableRadioButtonGroup.Builder.create(optionMap);
        } else if ("hidden".equals(type)) {
            control = RemotableHiddenInput.Builder.create();
        } else if ("multibox".equals(type)) {
            RemotableSelect.Builder builder = RemotableSelect.Builder.create(optionMap);
            builder.setMultiple(true);
            control = builder;
        } else {
            throw new IllegalArgumentException("Illegal field type found: " + type);
        }
        return control;
    }

    @Override
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(ExtensionDefinition extensionDefinition, DocumentSearchCriteria documentSearchCriteria) {
		List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        
        Map<String, List<String>> documentAttributeValues = documentSearchCriteria.getDocumentAttributeValues();
        if (documentAttributeValues == null || documentAttributeValues.isEmpty()) {
            // nothing to validate...
            return errors;
        }

        XMLSearchableAttributeContent content = new XMLSearchableAttributeContent(getConfigXML(extensionDefinition));
        List<XMLSearchableAttributeContent.FieldDef> fields;
        try {
            fields = content.getFieldDefList();
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException("Error parsing searchable attribute configuration", xpee);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException("Error parsing searchable attribute configuration", pce);
        }
        if (fields.isEmpty()) {
            LOG.warn("Could not find any field definitions (<" + FIELD_DEF_E + ">) or possibly a searching configuration (<searchingConfig>) for this XMLSearchAttribute");
            return errors;
        }

        for (XMLSearchableAttributeContent.FieldDef field: fields) {
            String fieldDefName = field.name;
            String fieldDefTitle = field.title == null ? "" : field.title;

            List<String> testObject = documentAttributeValues.get(fieldDefName);

            if (testObject == null || testObject.isEmpty()) {
                // no value to validate
                // not checking for 'required' here since this is *search* criteria, and required field can be omitted
                continue;
            }

            // What type of value is this searchable attribute field?
            // get the searchable attribute value by using the data type
            SearchableAttributeValue attributeValue = DocumentSearchInternalUtils.getSearchableAttributeValueByDataTypeString(field.searchDefinition.dataType);
            if (attributeValue == null) {
                String errorMsg = "Cannot find SearchableAttributeValue for field data type '" + field.searchDefinition.dataType + "'";
                LOG.error("validateUserSearchInputs() " + errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // 1) parse concrete values from possible range expressions
            // 2) validate any resulting concrete values whether they were original arguments or parsed from range expressions
            // 3) if the expression was a range expression, validate the logical validity of the range bounds

            List<String> terminalValues = new ArrayList<String>();
            List<Range> rangeValues = new ArrayList<Range>();

            // we are assuming here that the only expressions evaluated against searchable attributes are simple
            // non-compound expressions.  parsing compound expressions would require full grammar/parsing support
            // and would probably be pretty absurd assuming these queries are coming from UIs.
            // If they are not coming from the UI, do we need to support compound expressions?
            for (String value: testObject) {
                // is this a terminal value or does it look like a range?
                if (value == null) {
                    // assuming null values are not an error condition
                    continue;
                }
                // this is just a war of attrition, need real parsing
                String[] clauses = SearchExpressionUtils.splitOnClauses(value);
                for (String clause: clauses) {
                    // if it's not empty. see if it's a range
                    Range r = null;
                    if (StringUtils.isNotEmpty(value)) {
                        r = SearchExpressionUtils.parseRange(value);
                    }
                    if (r != null) {
                        // hey, it looks like a range
                        boolean errs = false;
                        if (!field.searchDefinition.isRangedSearch()) {
                            errs = true;
                            errors.add(RemotableAttributeError.Builder.create(field.name, "field does not support ranged searches but range search expression detected").build());
                        } else {
                            // only check bounds if range search is specified
                            // XXX: FIXME: disabling these pedantic checks as they are causing annoying test breakages
                            if (PEDANTIC_BOUNDS_VALIDATION) {
                                // this is not actually an error. just disregard case-sensitivity for data types that don't support it
                                /*if (!attributeValue.allowsCaseInsensitivity() && Boolean.FALSE.equals(field.searchDefinition.getRangeBoundOptions().caseSensitive)) {
                                    errs = true;
                                    errors.add(RemotableAttributeError.Builder.create(field.name, "attribute data type does not support case insensitivity but case-insensitivity specified in attribute definition").build());
                                }*/
                                if (r.getLowerBoundValue() != null && r.isLowerBoundInclusive() != field.searchDefinition.lowerBound.inclusive) {
                                    errs = true;
                                    errors.add(RemotableAttributeError.Builder.create(field.name, "range expression ('" + value + "') and attribute definition differ on lower bound inclusivity.  Range is: " + r.isLowerBoundInclusive() + " Attrib is: " + field.searchDefinition.lowerBound.inclusive).build());
                                }
                                if (r.getUpperBoundValue() != null && r.isUpperBoundInclusive() != field.searchDefinition.upperBound.inclusive) {
                                    errs = true;
                                    errors.add(RemotableAttributeError.Builder.create(field.name, "range expression ('" + value + "') and attribute definition differ on upper bound inclusivity.  Range is: " + r.isUpperBoundInclusive() + " Attrib is: " + field.searchDefinition.upperBound.inclusive).build());
                                }
                            }
                        }

                        if (!errs) {
                            rangeValues.add(r);
                        }
                    } else {
                        terminalValues.add(value);
                    }
                }
            }

            List<String> parsedValues = new ArrayList<String>();
            // validate all values
            for (String value: terminalValues) {
                errors.addAll(performValidation(attributeValue, field, value, fieldDefTitle, parsedValues));
            }
            for (Range range: rangeValues) {
                List<String> parsedLowerValues = new ArrayList<String>();
                List<String> parsedUpperValues = new ArrayList<String>();
                List<RemotableAttributeError> lowerErrors = performValidation(attributeValue, field,
                        range.getLowerBoundValue(), constructRangeFieldErrorPrefix(field.title,
                        field.searchDefinition.lowerBound), parsedLowerValues);
                errors.addAll(lowerErrors);
                List<RemotableAttributeError> upperErrors = performValidation(attributeValue, field, range.getUpperBoundValue(),
                        constructRangeFieldErrorPrefix(field.title, field.searchDefinition.upperBound), parsedUpperValues);
                errors.addAll(upperErrors);

                // if both values check out, perform logical range validation
                if (lowerErrors.isEmpty() && upperErrors.isEmpty()) {
                    // TODO: how to handle multiple values?? doesn't really make sense
                    String lowerBoundValue = parsedLowerValues.isEmpty() ? null : parsedLowerValues.get(0);
                    String upperBoundValue = parsedUpperValues.isEmpty() ? null : parsedUpperValues.get(0);

                    final Boolean rangeValid;
                    // for the sake of string searches, make sure the bounds are uppercased before comparison if the search
                    // is case sensitive.
                    if (KewApiConstants.SearchableAttributeConstants.DATA_TYPE_STRING.equals(field.searchDefinition.dataType)) {
                        boolean caseSensitive = field.searchDefinition.getRangeBoundOptions().caseSensitive == null ? true : field.searchDefinition.getRangeBoundOptions().caseSensitive;
                        rangeValid = ((CaseAwareSearchableAttributeValue) attributeValue).isRangeValid(lowerBoundValue, upperBoundValue, caseSensitive);
                    } else {
                        rangeValid = attributeValue.isRangeValid(lowerBoundValue, upperBoundValue);
                    }

                    if (rangeValid != null && !rangeValid) {
                        String errorMsg = "The " + fieldDefTitle + " range is incorrect.  The " +
                                (StringUtils.isNotBlank(field.searchDefinition.lowerBound.label) ? field.searchDefinition.lowerBound.label : KewApiConstants.SearchableAttributeConstants.DEFAULT_RANGE_SEARCH_LOWER_BOUND_LABEL)
                                + " value entered must come before the " +
                                (StringUtils.isNotBlank(field.searchDefinition.upperBound.label) ? field.searchDefinition.upperBound.label : KewApiConstants.SearchableAttributeConstants.DEFAULT_RANGE_SEARCH_UPPER_BOUND_LABEL)
                                + " value";
                        LOG.debug("validateUserSearchInputs() " + errorMsg + " :: field type '" + attributeValue.getAttributeDataType() + "'");
                        errors.add(RemotableAttributeError.Builder.create(fieldDefName, errorMsg).build());
                    }
                }
            }
       }
        return errors;
    }

    private String constructRangeFieldErrorPrefix(String fieldDefLabel, XMLSearchableAttributeContent.FieldDef.SearchDefinition.RangeBound rangeBound) {
        if ( StringUtils.isNotBlank(rangeBound.label) && StringUtils.isNotBlank(fieldDefLabel)) {
            return fieldDefLabel + " " + rangeBound.label + " Field";
        } else if (StringUtils.isNotBlank(fieldDefLabel)) {
            return fieldDefLabel + " Range Field";
        } else if (StringUtils.isNotBlank(rangeBound.label)) {
            return "Range Field " + rangeBound.label + " Field";
        }
        return null;
    }

    /**
     * Performs validation on a single DSC attribute value, running any defined custom validation regex after basic validation
     * @param attributeValue the searchable attribute value type
     * @param field the XMLSearchableAttributeContent field
     * @param enteredValue the value to validate
     * @param errorMessagePrefix a prefix for error messages
     * @param resultingValues optional list of accumulated parsed values
     * @return a (possibly empty) list of errors
     */
    private List<RemotableAttributeError> performValidation(SearchableAttributeValue attributeValue, final XMLSearchableAttributeContent.FieldDef field, String enteredValue, String errorMessagePrefix, List<String> resultingValues) {
        return DocumentSearchInternalUtils.validateSearchFieldValue(field.name, attributeValue, enteredValue, errorMessagePrefix, resultingValues, new Function<String, Collection<RemotableAttributeError>>() {
            @Override
            public Collection<RemotableAttributeError> apply(String value) {
                if (StringUtils.isNotEmpty(field.validation.regex)) {
                    Pattern pattern = Pattern.compile(field.validation.regex);
                    Matcher matcher = pattern.matcher(value);
                    if (!matcher.matches()) {
                        return Collections.singletonList(RemotableAttributeError.Builder.create(field.name, field.validation.message).build());
                    }
                }
                return Collections.emptyList();
            }
        });
    }

    // preserved only for subclasses
    protected Element getConfigXML(ExtensionDefinition extensionDefinition) {
        try {
            String xmlConfigData = extensionDefinition.getConfiguration().get(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA);
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new BufferedReader(new StringReader(xmlConfigData)))).getDocumentElement();
        } catch (Exception e) {
            String ruleAttrStr = (extensionDefinition == null ? null : extensionDefinition.getName());
            LOG.error("error parsing xml data from search attribute: " + ruleAttrStr, e);
            throw new RuntimeException("error parsing xml data from searchable attribute: " + ruleAttrStr, e);
        }
    }
}
