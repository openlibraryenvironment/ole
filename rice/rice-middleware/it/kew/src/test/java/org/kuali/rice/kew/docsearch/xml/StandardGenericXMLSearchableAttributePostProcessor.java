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
package org.kuali.rice.kew.docsearch.xml;


import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.postprocessor.DefaultPostProcessor;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;


public class StandardGenericXMLSearchableAttributePostProcessor extends DefaultPostProcessor {
    
    @Override
	public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
		WorkflowDocument doc = WorkflowDocumentFactory.loadDocument(KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName("rkirkend").getPrincipalId(), statusChangeEvent.getDocumentId());
		doc.setTitle("I'm a title - I should increment the lockVersion Number of this document");
		doc.saveDocumentData();
		return new ProcessDocReport(true);
	}



}
