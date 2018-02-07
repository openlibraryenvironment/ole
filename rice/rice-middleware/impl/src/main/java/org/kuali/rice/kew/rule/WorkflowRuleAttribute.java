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

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.xmlrouting.GenericXMLRuleAttribute;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kns.web.ui.Row;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * <p>Interface which abstracts a piece of information ("attribute") associated with
 * a Workflow document, which can be used to make routing decisions when combined
 * with Rules.</p>
 * <p>WorkflowAttribute lifecycle:</p>
 * <p>Definition via XML</p>
 * <ol>
 *   <li>Attribute definition is defined in XML (... any actions that are performed on-definition)</li>
 *   <li>Attribute definition is registered in a Rule Template definition in XML</li>
 *   <li>Rule Template is associated with a Document and a Rule in XML</li>
 * </ol>
 * <p>Definition via UI (this documentation needs work)</p>
 * <ol>
 *   <li>...?...</li>
 *   <li>{@link #validateRuleData(Map)} and {@link #validateRoutingData(Map)} and are called to validate the configuration/extension values
 *       of the rule definition, and the user-configured rule data (??). see {@link org.kuali.rice.kew.rule.web.WebRuleBaseValues},
 *       {@link org.kuali.rice.kew.rule.RuleRoutingAttribute}.</li>
 *   <li>Not sure where and why these are called, or how that is reconciled with XML-based ingestion....?  The 'required' field only seems
 *       to matter with regard to these two methods; it seems to only ever be used in implementations of these two methods</li> 
 * </ol>
 * <p>Runtime evaluation</p>
 * <ol>
 *   <li>Client application constructs {@link org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition} and attaches
 *       it to the client-side document</li>
 *   <li>Upon action taken on the document, the Attributes that are described by the <code>WorkflowAttributeDefinitionVO</code>s
 *       are looked up (... how ...) and constructed on the client side.</li>
 *   <li>If the attribute is a {@link WorkflowAttributeXmlValidator} (e.g. {@link org.kuali.rice.kew.rule.xmlrouting.StandardGenericXMLRuleAttribute}),
 *       then {@link WorkflowAttributeXmlValidator#validateClientRoutingData()} is called to validate any data the client app may have set
 *       on the client-instantiated attribute (the {@link org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition})</li>
 *   <li>Attribute content (content the attribute generates to place in the eDoc document content) is obtained from the attribute
 *       via {@link WorkflowRuleAttribute#getDocContent()} or {@link org.kuali.rice.kew.framework.document.attribute.SearchableAttribute#generateSearchContent(org.kuali.rice.kew.api.extension.ExtensionDefinition, String, org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition)},
 *       depending on whether the attribute is a WorkflowAttribute or {@link org.kuali.rice.kew.framework.document.attribute.SearchableAttribute}</li>
 *   <li>When a Rule is invoked (for instance, when a <code>requests</code> node is fired), all attributes associated with the rule
 *       (those associated with the Rule Template associated with the rule) which are WorkflowAttributes are enumerated and for each:
 *       <ol>
 *         <li>A special case is made for attributes defined in XML as
 *             of "RuleXmlAttribute" type ({@link KewApiConstants#RULE_XML_ATTRIBUTE_TYPE}): the attribute is cast to {@link GenericXMLRuleAttribute}
 *             and RuleAttribute business object is set on it ({@link GenericXMLRuleAttribute#setRuleAttribute(org.kuali.rice.kew.rule.RuleAttribute)}
 *             before proceeding with isMatch invocation. (what about a RuleAttributeAware interface so this can be done generically for all worklfow attribute
 *             implementations?)</li>
 *         <li>{@link WorkflowRuleAttribute#isMatch(DocumentContent, List)} is called with the Rule's extension values passed</li>
 *       </ol>
 *    </li>
 *    <li>If all attributes for the rule match, the rule is fired</li>
 * </ol>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowRuleAttribute extends Serializable {

    /**
     * Returns true if this Attribute finds a match in the given DocContent.  If true,
     * the associated document will be routed to the users specifed by the UNF
     * 
     * The isMatch method is responsible for determining whether content in a document matches content saved in workflow, thus determining whether to fire a rule or not.
     * The isMatch method takes a DocumentContent object and a list of rule extension objects and returns a Boolean. The DocumentContent object contains the data in XML
     * format that will be compared with the rules saved in workflow. Rule extension objects come from a potential rule that may match the document content on this eDoc.
     * The potential rule is selected based on the Document Type and Rule Templates associated with this eDoc. Each rule extension object contains a list of rule extension
     * value objects which have the data we will use in key value format to compare to the document content. The key will be determined by a unique string assigned by this
     * attribute. The Value is determined when a rule is created and data is entered for the particular key. If a match is found, this method returns true and the eDoc will
     * be routed based on this rule. If no match is found, the method returns false and the eDoc will not be routed based on this rule.
     */
    boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions);

    /**
     * Each Row contains Fields describing the UI-level presentation of a single RuleExtensionValue.
     * <p>
     * A single Row may contain more than one actual Field instance, but Fields beyond the first one
     * are there to provide context for the display and evaluation of the first one.
     * 
     * <p>The getRuleRows method returns a list of rows that contain Fields describing the UI-level presentation of a single RuleExtensionValue for the creation of a rule.
     * A single row may contain more than one actual Field object, but Fields beyond the first one are there to provide context for the display and evaluation of the first one.
     * For example, a secondary field may be a lookupable or a searchable valid values object. An individual field consists of a field label, a field help url, a field type,
     * whether a lookupable is associated with this field, a property name, a property value, valid values, the name of the lookupable, and the default lookupable name for this field.
     * The field label is a short title that will display on the jsp describing what data to enter for this field. The field help url is optional, but will pop open in a new window
     * the url entered. The field type is the type of field to display (e.g. hidden, text, drop down, radio, quickfinder (lookupable), lookup result only (this type is needed for
     * the lookupable, but does not actually render on the jsp), and finally drop down refresh (this type is a drop down box that when it's value changes another drop down box's
     * valid values will be changed as well)). The lookupable indicator determines whether this field will have a lookupable (valid value search) associated with it. The property
     * name is the name of the field when rendered on the jsp. This needs to be unique when compared to other field names on the jsp. The property value is the value entered into
     * the text box or other field type on the jsp. This value will be empty when creating the field and populated during form submission. A List of valid values is optional and
     * needed to populate drop down boxes or radio buttons. This list consists of KeyValue objects which the key will be the value passed in on submission and the label will
     * be what is display on the jsp. The lookupable is the name of the lookable service to be called for this field. The default lookupable name is the name of the field on the
     * lookupable itself. This may be the same of the property name of the field. This is needed when field conversions are needed. If the lookupable returns a key that doesn't
     * match a property name on the jsp it needs to be converted to one that does. So if the property name is different than the default lookupable name, the lookupable will
     * convert the return key to the property name of the field. Example: say by default a lookupable returns "color=red" on the url. The default lookupable name would be "color"
     * and if you needed a different name for this property on the jsp such as "wallcolor", the lookupable will now return "wallcolor=red" instead of the default "color=red". The
     * default lookupable name also is the key for rule extension values. When an extension value is saved the value comes from the UI and the key from this field. If this field
     * is left null, then the property name of the field will become the key stored in the database for the extension value. A row can also consist of a group label and number
     * of rows for this label to span for this attribute. If a group label is present, it will display on the rule creation jsp and group together the individual rows for this
     * attribute. The total rows number will rowspan the group label across the rows of this attribute.
     * NOTE: the group label and number of rows to span must be specified on each row object for the display to work correctly.
     * 
     * <p>Additionally, it is very important that the List of Row objects is reconstructed every time getRuleRows is called.  This is because the code which processes
     * these Rows will set the propertyValue directly on the Field objects contained within.  Essentially, this means the Rows and Fields should not be constructed once inside of
     * the attribute and cached statically, but instead be recreated each time this method is called.
     */
    List<Row> getRuleRows();

    /**
     * RoutingDataRows contain Rows describing the UI-level presentation of the ruleData fields
     * used to determine where a given document would be routed according to the associated rule.
     * 
     * <p>The getRoutingDataRows method returns a list of rows that contain Fields describing the UI-level presentation of a single RuleExtensionValue for the routing report feature.
     * These rows are used to determine where an eDoc would route if these values were entered. They are constructed the same way rule rows are described above and a lot of times
     * are identical.
     * 
     * <p>Additionally, it is very important that the List of Row objects is reconstructed every time getRoutingDataRows is called.  This is because the code which processes
     * these Rows will set the propertyValue directly on the Field objects contained within.  Essentially, this means the Rows and Fields should not be constructed once inside of
     * the attribute and cached statically, but instead be recreated each time this method is called.
     */
    List<Row> getRoutingDataRows();

    /**
     * Returns a String containing this Attribute's routingData values, formatted as a series of XML
     * tags.  The returned tags need not be contained within a single top-level tag.
     * <p>
     * For example, DollarRangeAttribute returns a single tag containing its totalDollarAmount, thus:
     * <pre>
     * &lt;totalDollarAmount&gt;345&lt;/totalDollarAmount&gt;
     * </pre>
     */
    String getDocContent();

    /**
     * Returns the List of RuleExtensionValue objects associated with this Rule, each of which contains
     * the name and value of one of the parameters used by this Attribute to control when the associated
     * Rule gets evaluated. RuleExtensionValues are assigned when the Rule is created.
     * <p>
     * For example, the DollarRangeAttribute has two associated RuleExtensionValues - its minimum and
     * maximum values - which control when a rule containing that attribute gets evaluated.
     * <p>
     * The UI instantiates a Rule from a RuleTemplate, uses the RuleRows of all of that Rule's Attributes
     * to build a form, uses user input to create a set of RuleExtensionValues which get persisted when
     * the rule is persisted.
     * 
     * To create a RuleExtensionValue object, instantiate a new object, set the key which will be located on the Rule's attribute,
     * set the value which will be entered on the form from the UI side, and finally add each RuleExtensionValue object to a list.
     * 
     * 
     * (Basically, given a configured/initialized attribute, marshalls out the RuleExtensionValues representing the current state of
     * the attribute - Aaron Hamid FIXME)
     */
    List<RuleExtensionValue> getRuleExtensionValues();
    
    /**
     * Validates routingData values in the incoming map.  Called by the UI during rule creation.
     * 
     * This method is responsible for validating and setting the data entered on the form from the UI of the routing report to the Rule's attribute.
     * The values will be in a Map with the key being the key of the RuleExtensionValue and the value being the value of the data entered from the
     * UI. This method is used for the routing report which may have different fields than the rule data.
     * 
     * @param paramMap Map containing the names and values of the routing data for this Attribute
     */
    List<RemotableAttributeError> validateRoutingData(Map<String, String> paramMap);
    
    /**
     * Validates ruleExtension values in the incoming map.  Called by the UI during rule creation.
     * 
     * This method is responsible for validating and setting the data entered on the form from the UI of the rule creation to the Rule's attribute.
     * The values will be in a Map with the key being the key of the RuleExtensionValue and the value being the value of the data entered from the UI.
     * This method is used for rule creation which may have different fields than the routing report data.
     * 
     * @param paramMap Map containing the names and values of the rule extensions for this Attribute
     */
    List<RemotableAttributeError> validateRuleData(Map<String, String> paramMap);
    
    /**
     * Sets the required flag for this Attribute to true.  If required is true, the extensionValues for
     * this Attribute must be filled in before the associated Rule can be persisted.
     * 
     * This method sets a flag on whether this attribute is required or not.
     * When a rule template is created, the rule's attribute can be required.
     * This setting is then passed to the attribute by this method and can be used by the validateRuleData or validateRoutingData method as fit.
     */
    void setRequired(boolean required);

    /**
     * Returns true if the extensionValues on this Attribute must be filled in before the associated
     * Rule can be persisted.
     */
    boolean isRequired();
    
}
