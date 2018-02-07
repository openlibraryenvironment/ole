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
package org.kuali.rice.krad.util;


/**
 * Constants for the KNS module that describer object property fields
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KRADPropertyConstants {

	public static final String ACTIVE = "active";
	public static final String ACTIVE_FROM_DATE = "activeFromDate";
	public static final String ACTIVE_TO_DATE = "activeToDate";
	public static final String ACTIVE_AS_OF_DATE = "activeAsOfDate";
	public static final String ACTION_REQUESTED = "actionRequested";
	public static final String ACTIVE_INDICATOR = "activeIndicator";
	public static final String AD_HOC_ROUTE_PERSONS = "adHocRoutePersons";
	public static final String AD_HOC_ROUTE_WORKGROUPS = "adHocRouteWorkgroups";
	public static final String ATTACHMENT = "attachment";
	public static final String ATTRIBUTE_CONTROL_TYPE = "attributeControlType";
	public static final String ATTRIBUTE_DESCRIPTION = "attributeDescription";
	public static final String ATTRIBUTE_FORMATTER_CLASS_NAME = "attributeFormatterClassName";
	public static final String ATTRIBUTE_LABEL = "attributeLabel";
	public static final String ATTRIBUTE_MAX_LENGTH = "attributeMaxLength";
	public static final String ATTRIBUTE_NAME = "attributeName";
	public static final String ATTRIBUTE_SHORT_LABEL = "attributeShortLabel";
	public static final String ATTRIBUTE_SUMMARY = "attributeSummary";
	public static final String ATTRIBUTE_VALIDATING_EXPRESSION = "attributeValidatingExpression";
	public static final String CAMPUS = "campus";
	public static final String CAMPUS_CODE = "code";
	public static final String CAMPUS_NAME = "name";
	public static final String CAMPUS_SHORT_NAME = "shortName";
	public static final String CAMPUS_TYPE_CODE = "code";
	public static final String CREATE_DATE = "createDate";
	public static final String CODE = "code";
    public static final String COMPONENT_CODE = "componentCode";
	public static final String CURRENT = "current";
	public static final String DATA_OBJECT_MAINTENANCE_CODE_ACTIVE_INDICATOR = "dataObjectMaintenanceCodeActiveIndicator";
	public static final String DESCRIPTION = "description";
	public static final String DICTIONARY_BUSINESS_OBJECT_NAME = "dictionaryBusinessObjectName";
	public static final String DOCUMENT = "document";
	public static final String DOCUMENT_DESCRIPTION = "documentDescription";
	public static final String DOCUMENT_EXPLANATION = "documentExplanation";
	public static final String DOC_ID = "docId";
	public static final String DOCUMENT_HEADER = "documentHeader";
	public static final String DOCUMENT_NUMBER = "documentNumber";
	public static final String DOCUMENT_TEMPLATE_NUMBER = "documentTemplateNumber";
	public static final String DOCUMENT_TYPE_CODE = "documentTypeCode";
	public static final String EMAIL_ADDRESS = "emailAddress";
	public static final String EMPLOYEE_STATUS_CODE = "employeeStatusCode";
	public static final String EMPLOYEE_TYPE_CODE = "employeeTypeCode";
	public static final String EXPLANATION = "explanation";
	public static final String GENERIC_AMOUNT = "genericAmount";
	public static final String GENERIC_BIG_TEXT = "genericBigText";
	public static final String GENERIC_BOOLEAN = "genericBoolean";
	public static final String GENERIC_DATE = "genericDate";
	public static final String GENERIC_SYSTEM_ID = "genericSystemId";
	public static final String GENERIC_TIMESTAMP = "genericTimestamp";
	public static final String GROUPS = "groups";
	public static final String ID = "id";
	public static final String INITIATOR_NETWORK_ID = "initiatorNetworkId";
    public static final String KEY = "key";
	public static final String LOOKUP_DATE = "lookupDate";
    public static final String METHOD_TO_CALL = "methodToCall";
	public static final String NAME = "name";
    public static final String NAMESPACE_CODE = "namespaceCode";
	public static final String NEW_DOCUMENT_NOTE = "newDocumentNote";
	public static final String NEW_COLLECTION_RECORD = "newCollectionRecord";
	public static final String NEW_MAINTAINABLE_OBJECT = "newMaintainableObject";
	public static final String NOTE = "note";
	public static final String OBJECT_ID = "objectId";
	public static final String OLD_MAINTAINABLE_OBJECT = "oldMaintainableObject";
	public static final String ONE_DIGIT_TEXT_CODE = "oneDigitTextCode";
	public static final String OWNED_BY_PRINCIPAL_ID = "ownedByPrincipalIdentifier";
	public static final String OWNED_BY_USER = "ownedByUser";
	public static final String PERCENT = "percent";
    public static final String SESSION_ID = "sessionId";
	public static final String TWO_DIGIT_TEXT_CODE = "twoDigitTextCode";
	public static final String TYPE = "type";
	public static final String UNIVERSAL_USER = "person";
	public static final String UU_ID = "uuId";
	public static final String VALUE = "value";
	public static final String VERSION_NUMBER = "versionNumber";
	public static final String WORKFLOW_DOCUMENT = "workflowDocument";
	public static final String XML_DOCUMENT_CONTENTS = "xmlDocumentContents";
	public static final String LAST_UPDATED_DATE = "lastUpdatedDate";	
	public static final String POSTAL_COUNTRY_RESTRICTED_INDICATOR = "restricted";
	public static final String POSTAL_COUNTRY_CODE = "code";
    public static final String PROPERTY_NAME = "propertyName";
	public static final String ALTERNATE_POSTAL_COUNTRY_CODE = "alternateCode";
	public static final String STATE_CODE = "stateCode";
    public static final String COUNTY_CODE = "countyCode";
    public static final String POSTAL_ZIP_CODE = "postalZipCode";
    public static final String POSTAL_STATE_CODE = "postalStateCode";
    public static final String POSTAL_CODE = "postalCode";
    
    //  KULRICE-8091: Adhoc routing tab utilizing Groups on all documents missing asterisks 
    public static final String RECIPIENT_NAMESPACE_CODE = "recipientNamespaceCode";
    public static final String RECIPIENT_NAME="recipientName";
    
	private KRADPropertyConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
