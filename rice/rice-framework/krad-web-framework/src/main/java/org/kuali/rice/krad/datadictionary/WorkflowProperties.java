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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This element is used to define a set of workflowPropertyGroups, which are used to
 * specify which document properties should be serialized during the document serialization
 * process.
 */
@BeanTag(name = "workflowProperties-bean")
public class WorkflowProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    protected List<WorkflowPropertyGroup> workflowPropertyGroups;

    public WorkflowProperties() {
        workflowPropertyGroups = new ArrayList<WorkflowPropertyGroup>();
    }

    /**
     * Returns a list of workflow property groups, which are used to determine which properties should be serialized
     * when generating
     * routing XML
     *
     * @return a list of {@link WorkflowPropertyGroup} objects, in the order in which they were added
     */
    @BeanTagAttribute(name = "workflowPropertyGroups", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<WorkflowPropertyGroup> getWorkflowPropertyGroups() {
        return this.workflowPropertyGroups;
    }

    /**
     * This element is used to define a set of workflowPropertyGroups, which are used to
     * specify which document properties should be serialized during the document serialization
     * process.
     */
    public void setWorkflowPropertyGroups(List<WorkflowPropertyGroup> workflowPropertyGroups) {
        this.workflowPropertyGroups = workflowPropertyGroups;
    }

}
