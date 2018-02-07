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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.kew.api.rule.RuleExtension;
import org.kuali.rice.kew.routeheader.DocumentContent;


/**
 * Generic base class that implements common functionality to simplify implementing
 * a WorkflowAttribute.  This includes simplified template methods, as well as a generic
 * attribute content model.
 * 
 * <p>Control flow (for isMatch):</p>
 * 
 * <ol>
 *   <li>{@link #isMatch(DocumentContent, List)}
 *     <ol>
 *       <li>{@link #isMatch(List, List)}
 *         <ol>
 *           <li>{@link #isMatch(Map, List)}</li>
 *         </ol>
 *       </li>
 *     </ol>
 *   </li>
 * </ol>
 * 
 * The default matching algorithm will match:
 * <blockquote><i>if any single attribute's properties are a match for all rule extension values</i></blockquote>
 * This implementation does not (yet!) implement a generic internal map of properties, so it is up to subclasses
 * to expose specific named getters/setters to set data on an attribute of this ancestry.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class GenericWorkflowAttribute extends AbstractWorkflowAttribute {
    protected final Logger log = Logger.getLogger(getClass());
    protected final String attributeName;
    protected final GenericAttributeContent content;
    
    public GenericWorkflowAttribute() {
        this(null); // can't do getClass().getName() so we'll have to pass null...shame
    }

    public GenericWorkflowAttribute(String uniqueName) {
        if (uniqueName != null) {
            this.attributeName = uniqueName;
        } else {
            this.attributeName = getClass().getName();
        }
        content = new GenericAttributeContent(attributeName);
    }

    /**
     * Template method for subclasses to override to expose attribute state
     * @return map exposing attribute state
     */
    public abstract Map<String, String> getProperties();

    /**
     * Simply defers to GenericAttributeContent to generate suitable XML content in a standard fashion
     */
    public String getDocContent() {
        String dc = content.generateContent(getProperties());
        //log.info("Generating doc content: " + dc, new Exception("Dummy exception"));
        return dc;
    }

    public boolean isMatch(DocumentContent docContent, List<RuleExtension> ruleExtensions) {
        log.info("isMatch: " + docContent + " " + ruleExtensions);
        try {
            // could be multiple attributes on the incoming doc content!
            List<Map<String, String>> propertiesList = content.parseContent(docContent.getAttributeContent());
            
            return isMatch(propertiesList, ruleExtensions);
        } catch (XPathExpressionException xpee) {
            String message = "Error parsing attribute '" + attributeName + "' content: " + docContent.getDocContent();
            log.error(message, xpee);
            throw new RuntimeException(xpee);
        }
    }

    /**
     * Returns true if any single incoming attribute's properties are a match for all rule extension values
     * @param propertiesList the list of incoming attributes' properties
     * @param ruleExtensions the rule extensions
     * @return true if any single attribute's properties are a match for all rule extension values
     */
    protected boolean isMatch(List<Map<String, String>> propertiesList, List<RuleExtension> ruleExtensions) {
        for (Map<String, String> properties: propertiesList) {
            return isMatch(properties, ruleExtensions);
        }
        return false;
    }

    /**
     * Returns true if all key/value pairs defined by the specified rule extensions are present in the incoming attribute's
     * properties
     * @param properties incoming attribute's properties
     * @param ruleExtensions list of rule extensions
     * @return true if all key/value pairs defined by the specified rule extensions are present in the incoming attribute's
     */
    protected boolean isMatch(Map<String, String> properties, List<RuleExtension> ruleExtensions) {
        for (RuleExtension ruleExtension: ruleExtensions) {
            for (Map.Entry<String, String> ruleExtensionValue: ruleExtension.getExtensionValuesMap().entrySet()) {
                if (!ObjectUtils.equals(ruleExtensionValue.getValue(), properties.get(ruleExtensionValue.getKey()))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * These guys should probably be implemented to set the parameters on an internal member property map this attribute
     * should use to contain all properties set on it, like StandardGenericXmlAttribute.
     * @see #getProperties()
     * TODO: implement me!
     */
    public List validateRoutingData(Map paramMap) {
        return Collections.EMPTY_LIST;
    }
    public List validateRuleData(Map paramMap) {
        return Collections.EMPTY_LIST;
    }

    //public List validateClientRouting....

    /**
     * I think the job of this method is to marshal the current state of the attribute into a representative list of rule extension
     * values.  On that assumption, this method should simply create a list of RuleExtensionValues based on the the property map
     * this attribute uses to hold property values.
     * 
     * TODO: this is not fully implemented! e.g. generic property map like StandardGenericXmlAttribute
     */
    public List<RuleExtensionValue> getRuleExtensionValues() {
        log.info("getRuleExtensionValues");
        List<RuleExtensionValue> exts = new ArrayList<RuleExtensionValue>();
        Map<String, String> props = getProperties();
        if (props != null) {
            for (Map.Entry<String, String> entry: props.entrySet()) {
                if (entry.getValue() != null) {
                    RuleExtensionValue ruleVal = new RuleExtensionValue();
                    ruleVal.setKey(entry.getKey());
                    ruleVal.setValue(entry.getValue());
                    exts.add(ruleVal);
                }
            }
        }
        return exts;
    }
}
