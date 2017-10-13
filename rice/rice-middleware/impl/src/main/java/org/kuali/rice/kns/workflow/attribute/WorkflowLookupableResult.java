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
package org.kuali.rice.kns.workflow.attribute;

import org.kuali.rice.krad.bo.BusinessObject;

/**
 * This interface is used to get the cglib Enhancer to simulate the returnUrl bean property required by workflow on the
 * BusinessObject proxies returned by the getSearchResults(Map fieldValues, Map fieldConversions) method of WorkflowLookupableImpl.
 * It also extends Map and simulates a bean property of itself on the proxy, because we want a generic getter that returns objects
 * and will allow us to format booleans.
 * 
 * 
 * @see org.kuali.rice.kew.attribute.WorkflowLookupableImpl
 * @see WorkflowLookupableInvocationHandler
 * @deprecated This will go away once workflow supports simple url integration for custom attribute lookups.
 */
public interface WorkflowLookupableResult extends BusinessObject {
    /**
     * Gets the returnUrl attribute.
     * 
     * @return Returns the returnUrl.
     */
    public String getReturnUrl();

    /**
     * Sets the returnUrl attribute.
     * 
     * @param returnUrl The returnUrl to set.
     */
    public void setReturnUrl(String returnUrl);

    /**
     * Gets the workflowLookupableResult attribute.
     * 
     * @return Returns the workflowLookupableResult
     */
    public WorkflowLookupableResult getWorkflowLookupableResult();

    /**
     * Sets the workflowLookupableResult attribute.
     * 
     * @param workflowLookupableResult The workflowLookupableResult to set.
     */
    public void setWorkflowLookupableResult(WorkflowLookupableResult workflowLookupableResult);
}
