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

import org.kuali.rice.kew.framework.postprocessor.DeleteEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.rule.service.RuleServiceInternal;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;


/**
 * The {@link org.kuali.rice.kew.framework.postprocessor.PostProcessor} implementation for rules.  Delegates to the
 * {@link org.kuali.rice.kew.rule.service.RuleServiceInternal#makeCurrent(Long)} method.
 *
 * @see org.kuali.rice.kew.rule.service.RuleServiceInternal#makeCurrent(Long)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RulePostProcessor extends DefaultPostProcessor {

    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
        DocumentRouteHeaderValue routeHeader = getRouteHeaderService().getRouteHeader(statusChangeEvent.getDocumentId());
        
        if (KewApiConstants.ROUTE_HEADER_PROCESSED_CD.equals(routeHeader.getDocRouteStatus())) {
            getRuleService().makeCurrent(routeHeader.getDocumentId());
        }
        return new ProcessDocReport(true, "");
    }    
    
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public String getVersion() throws Exception {
        return "";
    }

    private RouteHeaderService getRouteHeaderService() {
        return (RouteHeaderService) KEWServiceLocator.getService(KEWServiceLocator.DOC_ROUTE_HEADER_SRV);
    }

    private RuleServiceInternal getRuleService() {
        return (RuleServiceInternal) KEWServiceLocator.getService(KEWServiceLocator.RULE_SERVICE);
    }

}
