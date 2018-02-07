/**
 * Copyright 2005-2013 The Kuali Foundation
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

import org.jdom.Document;
import org.jdom.Element;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.routeheader.DocumentContent;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MockWorkflowAttribute implements WorkflowRuleAttribute {
    
    private static final String MOCK_VALUE_ELEMENT = "mockValue";
    //private static final String VALUE_KEY = "value";
    
    private String value;
    
    public MockWorkflowAttribute() {}
    
    public MockWorkflowAttribute(String value) {
        setValue(value);
    }

    public String getDocContent() {
        if (value == null) return "";
        return "<"+MOCK_VALUE_ELEMENT+">"+value+"</"+MOCK_VALUE_ELEMENT+">";
    }
    
    public List parseDocContent(String docContent) {
        try {
            Document doc = XmlHelper.buildJDocument(new StringReader(docContent));
            Collection<Element> elements = XmlHelper.findElements(doc.getRootElement(), MOCK_VALUE_ELEMENT);
            Element mockValueElement = elements.iterator().next();
            List attributes = new ArrayList();
            if (mockValueElement != null) {
                attributes.add(new MockWorkflowAttribute(mockValueElement.getText()));
            }
            return attributes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public String getIdFieldName() {
        return null;
    }
    public String getLockFieldName() {
        return null;
    }
    public List getRoutingDataRows() {
        return null;
    }
    public List getRuleExtensionValues() {
        return null;
    }
    public List getRuleRows() {
        return null;
    }
    public boolean isMatch(DocumentContent docContent, List ruleExtensions) {
        return false;
    }
    public boolean isRequired() {
        return false;
    }
    
    public void setRequired(boolean required) {
    }
    public List<RemotableAttributeError> validateRoutingData(Map paramMap) {
        return null;
    }
    public List<RemotableAttributeError> validateRuleData(Map paramMap) {
        return null;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    
}
