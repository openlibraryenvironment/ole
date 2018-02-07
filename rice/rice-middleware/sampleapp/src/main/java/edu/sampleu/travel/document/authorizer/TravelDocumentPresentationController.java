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
package edu.sampleu.travel.document.authorizer;

import java.util.HashSet;
import java.util.Set;

import org.kuali.rice.kns.authorization.AuthorizationConstants;
import org.kuali.rice.kns.document.authorization.TransactionalDocumentPresentationControllerBase;
import org.kuali.rice.krad.document.Document;

/**
 * Presentation controller for the travel document.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TravelDocumentPresentationController extends TransactionalDocumentPresentationControllerBase {

	public Set<String> getEditModes(Document document){
		// this logic isn't very good, but we need to get this working for now to facilitate testing
    	Set<String> editModes = new HashSet();
    	editModes.add(AuthorizationConstants.EditMode.FULL_ENTRY);
    	return editModes;
    }
	
}
