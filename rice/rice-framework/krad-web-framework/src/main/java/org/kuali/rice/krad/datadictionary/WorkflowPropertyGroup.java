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
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.KRADConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows for grouping of related {@link WorkflowProperty} objects
 *
 * <p>
 * This element is used to define a set of workflowProperty tags, which are used to
 * specify which document properties should be serialized during the document serialization
 * process.  This element allows for all the nested workflowProperty tags to be relative
 * to some base path.  This base path itself is relative to the object being serialized
 * during the document serialization process (which is not necessarily the document itself,
 * but a wrapper around the document).If blank/missing, the base path will be assumed to be
 * the property path to the document.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "workflowPropertyGroup-bean")
public class WorkflowPropertyGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String basePath = KRADConstants.EMPTY_STRING;
    protected List<WorkflowProperty> workflowProperties = new ArrayList<WorkflowProperty>();

    /**
     * The list of {@link WorkflowProperty} objects
     *
     * @return List<WorkflowProperty>
     */
    @BeanTagAttribute(name = "workflowProperties", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<WorkflowProperty> getWorkflowProperties() {
        return workflowProperties;
    }

    /**
     * The base path of the group which all {@link WorkflowProperty} objects are relative to
     *
     * <p>
     * The base path itself should be relative from the object being serialized, which may not necessarily be the
     * document, see {@link Document#wrapDocumentWithMetadataForXmlSerialization()}
     * and {@link Document#getBasePathToDocumentDuringSerialization()}. If blank/missing, the base path will be
     * assumed to be the property path to the document.
     * </p>
     *
     * @return String
     */
    @BeanTagAttribute(name = "basePath")
    public String getBasePath() {
        return this.basePath;
    }

    /**
     * Setter for the base path
     *
     * @param basePath - the base path
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Setter for workflow properties list
     *
     * @param workflowProperties - the list of workflow properties
     */
    public void setWorkflowProperties(List<WorkflowProperty> workflowProperties) {
        this.workflowProperties = workflowProperties;
    }

}
