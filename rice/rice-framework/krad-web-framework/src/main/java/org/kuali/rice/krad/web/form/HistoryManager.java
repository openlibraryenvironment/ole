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
package org.kuali.rice.krad.web.form;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * HistoryManager stores the map of the most recentFlows and a map of flows stored by flowId concatenated with formId.
 * HistoryManager is used in session.
 */
public class HistoryManager implements Serializable {

    private static final long serialVersionUID = 7612500634309569727L;
    private Map<String, HistoryFlow> historyFlowMap = new HashMap<String, HistoryFlow>();
    private Map<String, HistoryFlow> recentFlows = new HashMap<String, HistoryFlow>();

    /**
     * Process/update the HistoryFlow stored in session for the flowKey, formKey, and currentUrl passed in.
     *
     * <p>If flowKey is blank or equal to "start", this will begin a new flow, otherwise the flow will continue by
     * picking
     * up the last flow that matches the flowKey, but if it also matches to a formKey that already is keyed to that
     * flowKey, it will "jump" back to that HistoryFlow instead.</p>
     *
     * @param flowKey the flow key
     * @param formKey the form key
     * @param currentUrl the currentUrl being process
     * @return the HistoryFlow which represents the current HistoryFlow based on the parameters passed in
     */
    public HistoryFlow process(String flowKey, String formKey, String currentUrl) {
        if (StringUtils.isBlank(flowKey) || flowKey.equalsIgnoreCase(UifConstants.HistoryFlow.START)) {
            flowKey = UUID.randomUUID().toString();
        }

        HistoryFlow newFlow = new HistoryFlow(flowKey);

        if (currentUrl.contains("?") && !currentUrl.contains(UifParameters.FORM_KEY) && StringUtils.isNotBlank(formKey)){
            currentUrl = currentUrl + "&" + UifParameters.FORM_KEY + "=" + formKey;
        }

        if (getMostRecentFlowByFormKey(flowKey, formKey) != null) {
            newFlow = getMostRecentFlowByFormKey(flowKey, formKey);
            newFlow.update(currentUrl);
        } else if (StringUtils.isNotBlank(flowKey)) {
            HistoryFlow recentFlow = recentFlows.get(flowKey);
            newFlow.continueFlow(recentFlow);
            newFlow.push(currentUrl);
        }

        recentFlows.put(flowKey, newFlow);
        historyFlowMap.put(flowKey + UifConstants.HistoryFlow.SEPARATOR + formKey, newFlow);

        return newFlow;
    }

    /**
     * Get the flow that matches the flow key.  This represents the most recent flow tied to this flow key.
     *
     * @param key the flow key
     * @return the HistoryFlow, null if not found
     */
    public HistoryFlow getMostRecentFlowByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }

        return recentFlows.get(key);
    }

    /**
     * Get the flow by flowKey and formKey.  This represents a flow specific to this combination.
     *
     * @param key the flow key
     * @param formKey the form key
     * @return the HistoryFlow if found, null otherwise
     */
    public HistoryFlow getMostRecentFlowByFormKey(String key, String formKey) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(formKey)) {
            return null;
        }

        return historyFlowMap.get(key + UifConstants.HistoryFlow.SEPARATOR + formKey);
    }
}
