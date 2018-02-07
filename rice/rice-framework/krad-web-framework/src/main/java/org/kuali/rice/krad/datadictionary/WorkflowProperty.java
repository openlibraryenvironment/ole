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
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;

import java.io.Serializable;

/**
 * This class represents an serializable property when generating workflow routing XML.  The path contained within this
 * object
 * is relative to the basePath in the {@link WorkflowPropertyGroup} that contains this object.  The semantics of the
 * path are determined
 * by the {@link PropertySerializabilityEvaluator} that evaluates whether a property is serializable.
 */
@BeanTag(name = "workflowProperty-bean")
public class WorkflowProperty implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String path = null;

    /**
     * Default constructor, sets path to null
     */
    public WorkflowProperty() {}

    /**
     * Returns the path to the property that is serializable, relative to the {@link WorkflowPropertyGroup} that
     * contains this object
     *
     * @return
     */
    @BeanTagAttribute(name = "path")
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path to the property that is serializable, relative to the {@link WorkflowPropertyGroup} that contains
     * this object
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }
}
