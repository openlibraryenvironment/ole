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
package org.kuali.rice.kew.routemanager;

import java.util.List;
import java.util.Map;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;


public class ExplodingRuleAttribute implements WorkflowRuleAttribute {
    
    public static boolean dontExplode = false;

    public boolean isMatch(DocumentContent docContent, List ruleExtensions) {
        if (dontExplode) {
            return true;
        }
        throw new RuntimeException("I'm the exploder");
    }

    public List getRuleRows() {
        return null;
    }

    public List getRoutingDataRows() {
        return null;
    }

    public String getDocContent() {
        return null;
    }

    public List getRuleExtensionValues() {
        return null;
    }

    public List<RemotableAttributeError> validateRoutingData(Map paramMap) {
        return null;
    }

    public List<RemotableAttributeError> validateRuleData(Map paramMap) {
        return null;
    }

    public void setRequired(boolean required) {
    }

    public boolean isRequired() {
        return false;
    }
}
