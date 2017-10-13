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
import org.kuali.rice.kns.web.ui.Row;

import java.util.Collections;
import java.util.List;
import java.util.Map;



/**
 * Abstract base class for {@link WorkflowRuleAttribute}s.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class AbstractWorkflowAttribute implements WorkflowRuleAttribute {
    protected boolean required;

    public List<Row> getRuleRows() {
        return Collections.EMPTY_LIST;
    }

    public List<Row> getRoutingDataRows() {
        return Collections.EMPTY_LIST;
    }

    public String getDocContent() {
        return "";
    }

    public List<RuleExtensionValue> getRuleExtensionValues() {
        return Collections.EMPTY_LIST;
    }

    public List<RemotableAttributeError> validateRoutingData(Map paramMap) {
        return Collections.EMPTY_LIST;
    }

    public List<RemotableAttributeError> validateRuleData(Map paramMap) {
        return Collections.EMPTY_LIST;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    /* TODO: document these two methods ... what are these for? */
    public String getIdFieldName() {
        return "";
    }
    public String getLockFieldName() {
        return "";
    }
}
