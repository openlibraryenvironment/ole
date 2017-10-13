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
package org.kuali.rice.kew.engine;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;


/**
 * Tests a new document being spawned from the post processing of an existing document
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PostProcessorSpawnDocument extends DefaultPostProcessor {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PostProcessorSpawnDocument.class);

	@Override
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
    	LOG.info("Moving document " + statusChangeEvent.getDocumentId() + " from status '" + statusChangeEvent.getOldRouteStatus() + "' to status '" + statusChangeEvent.getNewRouteStatus() + "'");
    	if (StringUtils.equals(KewApiConstants.ROUTE_HEADER_PROCESSED_CD, statusChangeEvent.getNewRouteStatus())) {
    		// spawn and route a new document
        	WorkflowDocument document = WorkflowDocumentFactory.createDocument(KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("ewestfal").getPrincipalId(), "SpawnedDocumentType");
        	document.route("");
    	}
        return new ProcessDocReport(true, "");
    }

}
