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
package org.kuali.rice.kew.docsearch;

import org.joda.time.DateTime;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;


/**
 * Defines an externalizable business object interface for workflow documents.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */

// TODO map to DocSearchCriteriaBo
public interface DocumentSearchCriteriaEbo extends ExternalizableBusinessObject {

	public String getApplicationDocumentId();

	public DocumentStatus getStatus();

	public String getApplicationDocumentStatus();
	
	public String getTitle();

	public String getDocumentTypeName();

	public String getInitiatorPrincipalId();

	public String getDocumentId();

	public DateTime getDateCreated();

}
