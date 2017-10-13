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
package org.kuali.rice.kew.api;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a constants file used to describe KEW properties
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KEWPropertyConstants {

    // Constants used by Document Search and the document search results
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_DOCUMENT_ID = "documentId";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_DOC_TYPE_LABEL = "docTypeLabel";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_DOCUMENT_TITLE = "documentTitle";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_ROUTE_STATUS_DESC = "docRouteStatusCodeDesc";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_INITIATOR = "initiator";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_DATE_CREATED = "dateCreated";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_ROUTE_LOG = "routeLog";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_DOC_STATUS = "applicationDocumentStatus";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_STATUS_CODE = "statusCode";
    public static final String DOC_SEARCH_RESULT_PROPERTY_NAME_INITIATOR_DISPLAY_NAME = "initiatorDisplayName";

    // Constants used by DocumentTypeLookupableHelperServiceImpl
    public static final String DOCUMENT_TYPE_ID = "documentTypeId";
    public static final String NAME = "name";
    public static final String DOC_TYP_LABEL = "label";
    public static final String DOC_TYPE_PARENT_ID = "documentTypeId";
    public static final String PARENT_DOC_TYPE_NAME = "parentDocType.name";
    public static final String ACTIVE = "active";
    public static final String BACK_LOCATION = "backLocation";
    public static final String APPLICATION_ID = "applicationId";
    public static final String DOC_FORM_KEY = "docFormKey";

    //Constants used by RouteNode
    public static final String ROUTE_NODE_ID = "routeNodeId";
    public static final String ROUTE_NODE_INSTANCE_ID = "routeNodeInstanceId";
    public static final String NODE_INSTANCE_ID = "nodeInstanceId";
    public static final String DOCUMENT_ID = "documentId";
    public static final String ROUTE_NODE_NAME = "routeNodeName";
    public static final String PROCESS_ID = "processId";
    public static final String COMPLETE = "complete";
    public static final String FINAL_APPROVAL = "finalApprovalInd";
    public static final String KEY = "key";
    public static final String ROUTE_NODE_STATE_ID = "nodeStateId";

    // people flow and type
    public static final String SEQUENCE_NUMBER = "sequenceNumber";
    
    public static final Set<String> DOC_SEARCH_RESULT_PROPERTY_NAME_SET = new HashSet<String>();
    static {
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_DOCUMENT_ID);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_DOC_TYPE_LABEL);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_DOCUMENT_TITLE);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_ROUTE_STATUS_DESC);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_DOC_STATUS);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_INITIATOR);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_DATE_CREATED);
        DOC_SEARCH_RESULT_PROPERTY_NAME_SET.add(DOC_SEARCH_RESULT_PROPERTY_NAME_ROUTE_LOG);
    }

	// Name of the section in the xml file.
	public static final String PERSON_RESP_SECTION = "personResponsibilities";
	// Name of the section in the xml file.
	public static final String GROUP_RESP_SECTION = "groupResponsibilities";
	// Name of the section in the xml file.
	public static final String ROLE_RESP_SECTION = "roleResponsibilities";
	
	public static final Set<String> RESP_SECTION_NAME_SET = new HashSet<String>();
	static {
		RESP_SECTION_NAME_SET.add(PERSON_RESP_SECTION);
		RESP_SECTION_NAME_SET.add(GROUP_RESP_SECTION);
		RESP_SECTION_NAME_SET.add(ROLE_RESP_SECTION);
	}
	
	private KEWPropertyConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
