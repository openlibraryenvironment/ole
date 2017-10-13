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
package org.kuali.rice.kns.service.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.authorization.BusinessObjectAuthorizer;
import org.kuali.rice.kns.bo.authorization.InquiryOrMaintenanceDocumentAuthorizer;
import org.kuali.rice.kns.bo.authorization.InquiryOrMaintenanceDocumentPresentationController;
import org.kuali.rice.kns.datadictionary.BusinessObjectEntry;
import org.kuali.rice.kns.datadictionary.FieldDefinition;
import org.kuali.rice.kns.datadictionary.InquiryCollectionDefinition;
import org.kuali.rice.kns.datadictionary.InquirySectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableCollectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableItemDefinition;
import org.kuali.rice.kns.datadictionary.MaintainableSectionDefinition;
import org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictionsBase;
import org.kuali.rice.kns.document.authorization.InquiryOrMaintenanceDocumentRestrictions;
import org.kuali.rice.kns.document.authorization.InquiryOrMaintenanceDocumentRestrictionsBase;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentAuthorizer;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentPresentationController;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentRestrictions;
import org.kuali.rice.kns.document.authorization.MaintenanceDocumentRestrictionsBase;
import org.kuali.rice.kns.inquiry.InquiryAuthorizer;
import org.kuali.rice.kns.inquiry.InquiryPresentationController;
import org.kuali.rice.kns.inquiry.InquiryRestrictions;
import org.kuali.rice.kns.service.BusinessObjectAuthorizationService;
import org.kuali.rice.kns.service.BusinessObjectDictionaryService;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.DataObjectEntry;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.impl.DataObjectAuthorizationServiceImpl;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Deprecated
public class BusinessObjectAuthorizationServiceImpl extends DataObjectAuthorizationServiceImpl implements BusinessObjectAuthorizationService {
	private DataDictionaryService dataDictionaryService;
	private PermissionService permissionService;
	private BusinessObjectDictionaryService businessObjectDictionaryService;
	private DocumentHelperService documentHelperService;
	private MaintenanceDocumentDictionaryService maintenanceDocumentDictionaryService;
	private ConfigurationService kualiConfigurationService;
	
	public BusinessObjectRestrictions getLookupResultRestrictions(
			Object dataObject, Person user) {
		BusinessObjectRestrictions businessObjectRestrictions = new BusinessObjectRestrictionsBase();
		considerBusinessObjectFieldUnmaskAuthorization(dataObject, user,
				businessObjectRestrictions, "", null);
		return businessObjectRestrictions;
	}

	public InquiryRestrictions getInquiryRestrictions(
			BusinessObject businessObject, Person user) {
		InquiryRestrictions inquiryRestrictions = new InquiryOrMaintenanceDocumentRestrictionsBase();
		BusinessObjectEntry businessObjectEntry = (BusinessObjectEntry) getDataDictionaryService()
				.getDataDictionary().getBusinessObjectEntry(
						businessObject.getClass().getName());
		InquiryPresentationController inquiryPresentationController = getBusinessObjectDictionaryService()
				.getInquiryPresentationController(businessObject.getClass());
		InquiryAuthorizer inquiryAuthorizer = getBusinessObjectDictionaryService()
				.getInquiryAuthorizer(businessObject.getClass());
		considerBusinessObjectFieldUnmaskAuthorization(businessObject, user,
				inquiryRestrictions, "", null);
		considerBusinessObjectFieldViewAuthorization(businessObjectEntry,
				businessObject, null, user, inquiryAuthorizer, inquiryRestrictions,
				"");
		considerInquiryOrMaintenanceDocumentPresentationController(
				inquiryPresentationController, businessObject,
				inquiryRestrictions);
		considerInquiryOrMaintenanceDocumentAuthorizer(inquiryAuthorizer,
				businessObject, user, inquiryRestrictions);
		for (InquirySectionDefinition inquirySectionDefinition : businessObjectEntry.getInquiryDefinition().getInquirySections()) {
			if (inquirySectionDefinition.getInquiryCollections() != null) {
				addInquirableItemRestrictions(inquirySectionDefinition.getInquiryCollections().values(), inquiryAuthorizer, 
						inquiryRestrictions, businessObject, businessObject, "", user);
			}
			// Collections may also be stored in the inquiry fields, so we need to parse through that
			List<FieldDefinition> inquiryFields = inquirySectionDefinition.getInquiryFields();
			if (inquiryFields != null) {
				for (FieldDefinition fieldDefinition : inquiryFields) {
					addInquirableItemRestrictions(inquiryFields, inquiryAuthorizer, 
							inquiryRestrictions, businessObject, businessObject, "", user);
				}
			}
		}
		
		return inquiryRestrictions;
	}

	public MaintenanceDocumentRestrictions getMaintenanceDocumentRestrictions(
			MaintenanceDocument maintenanceDocument, Person user) {

		MaintenanceDocumentRestrictions maintenanceDocumentRestrictions = new MaintenanceDocumentRestrictionsBase();
		DataObjectEntry dataObjectEntry = getDataDictionaryService()
				.getDataDictionary().getDataObjectEntry(
						maintenanceDocument.getNewMaintainableObject()
								.getDataObject().getClass().getName());
		MaintenanceDocumentPresentationController maintenanceDocumentPresentationController = (MaintenanceDocumentPresentationController) getDocumentHelperService()
				.getDocumentPresentationController(maintenanceDocument);
		MaintenanceDocumentAuthorizer maintenanceDocumentAuthorizer = (MaintenanceDocumentAuthorizer) getDocumentHelperService()
				.getDocumentAuthorizer(maintenanceDocument);
		considerBusinessObjectFieldUnmaskAuthorization(maintenanceDocument
				.getNewMaintainableObject().getDataObject(), user,
				maintenanceDocumentRestrictions, "", maintenanceDocument );
		considerBusinessObjectFieldViewAuthorization(dataObjectEntry,
				maintenanceDocument.getNewMaintainableObject().getDataObject(),
				null, user, maintenanceDocumentAuthorizer,
				maintenanceDocumentRestrictions, "");
		considerBusinessObjectFieldModifyAuthorization(dataObjectEntry,
				maintenanceDocument.getNewMaintainableObject().getDataObject(),
				null, user, maintenanceDocumentAuthorizer,
				maintenanceDocumentRestrictions, "");
		considerCustomButtonFieldAuthorization(dataObjectEntry,
				maintenanceDocument.getNewMaintainableObject().getDataObject(),
				null, user, maintenanceDocumentAuthorizer,
				maintenanceDocumentRestrictions, "");
		considerInquiryOrMaintenanceDocumentPresentationController(
				maintenanceDocumentPresentationController, maintenanceDocument,
				maintenanceDocumentRestrictions);
		considerInquiryOrMaintenanceDocumentAuthorizer(
				maintenanceDocumentAuthorizer, maintenanceDocument, user,
				maintenanceDocumentRestrictions);
		considerMaintenanceDocumentPresentationController(
				maintenanceDocumentPresentationController, maintenanceDocument,
				maintenanceDocumentRestrictions);
		considerMaintenanceDocumentAuthorizer(maintenanceDocumentAuthorizer,
				maintenanceDocument, user, maintenanceDocumentRestrictions);
		
		MaintenanceDocumentEntry maintenanceDocumentEntry = getMaintenanceDocumentDictionaryService().getMaintenanceDocumentEntry(maintenanceDocument
				.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
		for (MaintainableSectionDefinition maintainableSectionDefinition : maintenanceDocumentEntry.getMaintainableSections()) {
			addMaintainableItemRestrictions(maintainableSectionDefinition.getMaintainableItems(), maintenanceDocumentAuthorizer, maintenanceDocumentRestrictions,
					maintenanceDocument, maintenanceDocument.getNewMaintainableObject().getBusinessObject(), "", user);
		}
		return maintenanceDocumentRestrictions;
	}

	protected void considerBusinessObjectFieldUnmaskAuthorization(Object dataObject, Person user, BusinessObjectRestrictions businessObjectRestrictions, String propertyPrefix, Document document) {
		DataObjectEntry objectEntry = getDataDictionaryService().getDataDictionary().getDataObjectEntry(dataObject.getClass().getName());
		for (String attributeName : objectEntry.getAttributeNames()) {
			AttributeDefinition attributeDefinition = objectEntry.getAttributeDefinition(attributeName);
			if (attributeDefinition.getAttributeSecurity() != null) {
				if (attributeDefinition.getAttributeSecurity().isMask() && 
						!canFullyUnmaskField(user, dataObject.getClass(), attributeName, document)) {
					businessObjectRestrictions.addFullyMaskedField(propertyPrefix + attributeName, attributeDefinition.getAttributeSecurity().getMaskFormatter());
				}
				if (attributeDefinition.getAttributeSecurity().isPartialMask() && 
						!canPartiallyUnmaskField(user, dataObject.getClass(), attributeName, document)) {
					businessObjectRestrictions.addPartiallyMaskedField(propertyPrefix + attributeName, attributeDefinition.getAttributeSecurity().getPartialMaskFormatter());
				}
			}
		}
	}

	/**
	 * @param dataObjectEntry if collectionItemBusinessObject is not null, then it is the DD entry for collectionItemBusinessObject.
	 * Otherwise, it is the entry for primaryBusinessObject
	 * @param primaryDataObject the top-level BO that is being inquiried or maintained
	 * @param collectionItemBusinessObject an element of a collection under the primaryBusinessObject that we are evaluating view auths for
	 * @param user the logged in user
	 * @param businessObjectAuthorizer
	 * @param inquiryOrMaintenanceDocumentRestrictions
	 * @param propertyPrefix
	 */
	protected void considerBusinessObjectFieldViewAuthorization(
			DataObjectEntry dataObjectEntry,
			Object primaryDataObject,
			BusinessObject collectionItemBusinessObject,
			Person user,
			BusinessObjectAuthorizer businessObjectAuthorizer,
			InquiryOrMaintenanceDocumentRestrictions inquiryOrMaintenanceDocumentRestrictions,
			String propertyPrefix) {
		for (String attributeName : dataObjectEntry.getAttributeNames()) {
			AttributeDefinition attributeDefinition = dataObjectEntry
					.getAttributeDefinition(attributeName);
			if (attributeDefinition.getAttributeSecurity() != null) {
				if (attributeDefinition.getAttributeSecurity().isHide()) {
					Map<String, String> collectionItemPermissionDetails = new HashMap<String, String>();
					Map<String, String> collectionItemRoleQualifications = null;
					if (ObjectUtils.isNotNull(collectionItemBusinessObject)) {
						collectionItemPermissionDetails.putAll(getFieldPermissionDetails(collectionItemBusinessObject, attributeName));
						collectionItemPermissionDetails.putAll(businessObjectAuthorizer.
								getCollectionItemPermissionDetails(collectionItemBusinessObject));
						collectionItemRoleQualifications = new HashMap<String, String>(businessObjectAuthorizer.
								getCollectionItemRoleQualifications(collectionItemBusinessObject));
					}
					else {
						collectionItemPermissionDetails.putAll(getFieldPermissionDetails(primaryDataObject, attributeName));
					}
					if (!businessObjectAuthorizer
							.isAuthorizedByTemplate(
									primaryDataObject,
									KRADConstants.KNS_NAMESPACE,
									KimConstants.PermissionTemplateNames.VIEW_MAINTENANCE_INQUIRY_FIELD,
									user.getPrincipalId(),
									collectionItemPermissionDetails,
									collectionItemRoleQualifications)) {
						inquiryOrMaintenanceDocumentRestrictions
								.addHiddenField(propertyPrefix + attributeName);
					}
				}
			}
		}
	}

	/**
	 * @param dataObjectEntry if collectionItemBusinessObject is not null, then it is the DD entry for collectionItemBusinessObject.
	 * Otherwise, it is the entry for primaryBusinessObject
	 * @param primaryDataObject the top-level BO that is being inquiried or maintained
	 * @param collectionItemBusinessObject an element of a collection under the primaryBusinessObject that we are evaluating view auths for
	 * @param user the logged in user
	 * @param businessObjectAuthorizer
	 * @param inquiryOrMaintenanceDocumentRestrictions
	 * @param propertyPrefix
	 */
	protected void considerBusinessObjectFieldModifyAuthorization(
			DataObjectEntry dataObjectEntry,
			Object primaryDataObject,
			BusinessObject collectionItemBusinessObject, Person user,
			BusinessObjectAuthorizer businessObjectAuthorizer,
			MaintenanceDocumentRestrictions maintenanceDocumentRestrictions,
			String propertyPrefix) {
		for (String attributeName : dataObjectEntry.getAttributeNames()) {
			AttributeDefinition attributeDefinition = dataObjectEntry
					.getAttributeDefinition(attributeName);
			if (attributeDefinition.getAttributeSecurity() != null) {
				Map<String, String> collectionItemPermissionDetails = new HashMap<String, String>();
				Map<String, String> collectionItemRoleQualifications = null;
				if (ObjectUtils.isNotNull(collectionItemBusinessObject)) {
					collectionItemPermissionDetails.putAll(getFieldPermissionDetails(collectionItemBusinessObject, attributeName));
					collectionItemPermissionDetails.putAll(businessObjectAuthorizer.
							getCollectionItemPermissionDetails(collectionItemBusinessObject));
					collectionItemRoleQualifications = new HashMap<String, String>(businessObjectAuthorizer.
							getCollectionItemRoleQualifications(collectionItemBusinessObject));
				}
				else {
					collectionItemPermissionDetails.putAll(getFieldPermissionDetails(primaryDataObject, attributeName));
				}
				if (attributeDefinition.getAttributeSecurity().isReadOnly()) {
					if (!businessObjectAuthorizer
								.isAuthorizedByTemplate(
										primaryDataObject,
										KRADConstants.KNS_NAMESPACE,
										KimConstants.PermissionTemplateNames.MODIFY_FIELD,
										user.getPrincipalId(),
										collectionItemPermissionDetails,
										collectionItemRoleQualifications)) {
						maintenanceDocumentRestrictions
								.addReadOnlyField(propertyPrefix + attributeName);
					}
				}
			}
		}
	}
	
	/**
	 * @param dataObjectEntry if collectionItemBusinessObject is not null, then it is the DD entry for collectionItemBusinessObject.
	 * Otherwise, it is the entry for primaryBusinessObject
	 * @param primaryDataObject the top-level BO that is being inquiried or maintained
	 * @param collectionItemBusinessObject an element of a collection under the primaryBusinessObject that we are evaluating view auths for
	 * @param user the logged in user
	 * @param businessObjectAuthorizer
	 * @param inquiryOrMaintenanceDocumentRestrictions
	 * @param propertyPrefix
	 */
	protected void considerCustomButtonFieldAuthorization(
			DataObjectEntry dataObjectEntry,
			Object primaryDataObject,
			BusinessObject collectionItemBusinessObject,
			Person user,
			BusinessObjectAuthorizer businessObjectAuthorizer,
			MaintenanceDocumentRestrictions maintenanceDocumentRestrictions,
			String propertyPrefix) {
		for (String attributeName : dataObjectEntry.getAttributeNames()) {
			AttributeDefinition attributeDefinition = dataObjectEntry
					.getAttributeDefinition(attributeName);
			// TODO what is the equivalent of control.isButton in KRAD
			if (attributeDefinition.getControl() != null &&
			        attributeDefinition.getControl().isButton()) {
				Map<String, String> collectionItemPermissionDetails = new HashMap<String, String>();
				Map<String, String> collectionItemRoleQualifications = null;
				if (ObjectUtils.isNotNull(collectionItemBusinessObject)) {
					collectionItemPermissionDetails.putAll(getButtonFieldPermissionDetails(collectionItemBusinessObject, attributeName));
					collectionItemPermissionDetails.putAll(businessObjectAuthorizer.
							getCollectionItemPermissionDetails(collectionItemBusinessObject));
					collectionItemRoleQualifications = new HashMap<String, String>(businessObjectAuthorizer.
							getCollectionItemRoleQualifications(collectionItemBusinessObject));
				}
				else {
					getButtonFieldPermissionDetails(primaryDataObject, attributeName);
				}
				
				if (!businessObjectAuthorizer
						.isAuthorizedByTemplate(
								primaryDataObject,
								KRADConstants.KNS_NAMESPACE,
								KimConstants.PermissionTemplateNames.PERFORM_CUSTOM_MAINTENANCE_DOCUMENT_FUNCTION,
								user.getPrincipalId(),
								collectionItemPermissionDetails,
								collectionItemRoleQualifications)) {
					maintenanceDocumentRestrictions
							.addHiddenField(propertyPrefix + attributeName);
				}
			}
		}
	}

	protected void considerInquiryOrMaintenanceDocumentPresentationController(
			InquiryOrMaintenanceDocumentPresentationController businessObjectPresentationController,
			BusinessObject businessObject,
			InquiryOrMaintenanceDocumentRestrictions inquiryOrMaintenanceDocumentRestrictions) {
		for (String attributeName : businessObjectPresentationController
				.getConditionallyHiddenPropertyNames(businessObject)) {
			inquiryOrMaintenanceDocumentRestrictions
					.addHiddenField(attributeName);
		}
		for (String sectionId : businessObjectPresentationController
				.getConditionallyHiddenSectionIds(businessObject)) {
			inquiryOrMaintenanceDocumentRestrictions
					.addHiddenSectionId(sectionId);
		}
	}

	protected void considerInquiryOrMaintenanceDocumentAuthorizer(
			InquiryOrMaintenanceDocumentAuthorizer authorizer,
			BusinessObject businessObject, Person user,
			InquiryOrMaintenanceDocumentRestrictions restrictions) {
		for (String sectionId : authorizer
				.getSecurePotentiallyHiddenSectionIds()) {
			Map<String, String> additionalPermissionDetails = new HashMap<String, String>();
			additionalPermissionDetails
					.put(KimConstants.AttributeConstants.SECTION_ID, sectionId);
			if (!authorizer.isAuthorizedByTemplate(businessObject,
					KRADConstants.KNS_NAMESPACE,
					KimConstants.PermissionTemplateNames.VIEW_SECTION, user
							.getPrincipalId(), additionalPermissionDetails,
					null)) {
				restrictions.addHiddenSectionId(sectionId);
			}
		}
	}

	protected void considerMaintenanceDocumentPresentationController(
			MaintenanceDocumentPresentationController presentationController,
			MaintenanceDocument document,
			MaintenanceDocumentRestrictions restrictions) {
		for (String attributeName : presentationController
				.getConditionallyReadOnlyPropertyNames(document)) {
			restrictions.addReadOnlyField(attributeName);
		}
		for (String sectionId : presentationController
				.getConditionallyReadOnlySectionIds(document)) {
			restrictions.addReadOnlySectionId(sectionId);
		}
	}

	protected void considerMaintenanceDocumentAuthorizer(
			MaintenanceDocumentAuthorizer authorizer,
			MaintenanceDocument document, Person user,
			MaintenanceDocumentRestrictions restrictions) {
		for (String sectionId : authorizer
				.getSecurePotentiallyReadOnlySectionIds()) {
			Map<String, String> additionalPermissionDetails = new HashMap<String, String>();
			additionalPermissionDetails
					.put(KimConstants.AttributeConstants.SECTION_ID, sectionId);
			if (!authorizer.isAuthorizedByTemplate(document,
					KRADConstants.KNS_NAMESPACE,
					KimConstants.PermissionTemplateNames.MODIFY_SECTION, user
							.getPrincipalId(), additionalPermissionDetails,
					null)) {
				restrictions.addReadOnlySectionId(sectionId);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void addInquirableItemRestrictions(Collection sectionDefinitions,
			InquiryAuthorizer authorizer, InquiryRestrictions restrictions,
			BusinessObject primaryBusinessObject,
			BusinessObject businessObject, String propertyPrefix, Person user) {
		for (Object inquirableItemDefinition : sectionDefinitions) {
			if (inquirableItemDefinition instanceof InquiryCollectionDefinition) {
				InquiryCollectionDefinition inquiryCollectionDefinition = (InquiryCollectionDefinition) inquirableItemDefinition;
				BusinessObjectEntry collectionBusinessObjectEntry = (BusinessObjectEntry) getDataDictionaryService()
						.getDataDictionary().getBusinessObjectEntry(
								inquiryCollectionDefinition.getBusinessObjectClass().getName());

				try {
					Collection<BusinessObject> collection = (Collection<BusinessObject>) PropertyUtils
							.getProperty(businessObject,
									inquiryCollectionDefinition.getName());
					int i = 0;
					for (Iterator<BusinessObject> iterator = collection.iterator(); iterator
							.hasNext();) {
						String newPropertyPrefix = propertyPrefix + inquiryCollectionDefinition.getName() + "[" + i + "].";
						BusinessObject collectionItemBusinessObject = iterator.next();
						considerBusinessObjectFieldUnmaskAuthorization(
								collectionItemBusinessObject, user, restrictions,
								newPropertyPrefix, null);
						considerBusinessObjectFieldViewAuthorization(
								collectionBusinessObjectEntry, primaryBusinessObject, collectionItemBusinessObject,
								user, authorizer, restrictions, newPropertyPrefix);
						addInquirableItemRestrictions(
								inquiryCollectionDefinition
										.getInquiryCollections(),
								authorizer,
								restrictions,
								primaryBusinessObject,
								collectionItemBusinessObject,
								newPropertyPrefix,
								user);
						i++;
					}
				} catch (Exception e) {
					throw new RuntimeException(
							"Unable to resolve collection property: "
									+ businessObject.getClass() + ":"
									+ inquiryCollectionDefinition.getName(), e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void addMaintainableItemRestrictions(List<? extends MaintainableItemDefinition> itemDefinitions,
			MaintenanceDocumentAuthorizer authorizer,
			MaintenanceDocumentRestrictions restrictions,
			MaintenanceDocument maintenanceDocument,
			BusinessObject businessObject, String propertyPrefix, Person user) {
		for (MaintainableItemDefinition maintainableItemDefinition : itemDefinitions) {
			if (maintainableItemDefinition instanceof MaintainableCollectionDefinition) {
				try {
					MaintainableCollectionDefinition maintainableCollectionDefinition = (MaintainableCollectionDefinition) maintainableItemDefinition;
					
					Collection<BusinessObject> collection = (Collection<BusinessObject>) ObjectUtils
							.getNestedValue(businessObject,
									maintainableItemDefinition.getName());
					BusinessObjectEntry collectionBusinessObjectEntry = (BusinessObjectEntry) getDataDictionaryService()
							.getDataDictionary().getBusinessObjectEntry(
									maintainableCollectionDefinition.getBusinessObjectClass().getName());
					if (CollectionUtils.isNotEmpty(collection)) {
                    //if (collection != null && !collection.isEmpty()) {
				    	int i = 0;
			     		for (Iterator<BusinessObject> iterator = collection.iterator(); iterator
							.hasNext();) {
			     			String newPropertyPrefix = propertyPrefix + maintainableItemDefinition.getName() + "[" + i + "].";
					    	BusinessObject collectionBusinessObject = iterator.next();
					    	considerBusinessObjectFieldUnmaskAuthorization(
								collectionBusinessObject, user, restrictions,
								newPropertyPrefix, maintenanceDocument);
					    	considerBusinessObjectFieldViewAuthorization(
								collectionBusinessObjectEntry, maintenanceDocument, collectionBusinessObject, user,
								authorizer, restrictions, newPropertyPrefix);
					    	considerBusinessObjectFieldModifyAuthorization(
								collectionBusinessObjectEntry, maintenanceDocument, collectionBusinessObject, user,
								authorizer, restrictions, newPropertyPrefix);
					    	addMaintainableItemRestrictions(
								((MaintainableCollectionDefinition) maintainableItemDefinition)
										.getMaintainableCollections(),
								authorizer, restrictions, maintenanceDocument,
								collectionBusinessObject, newPropertyPrefix,
								user);
				     		addMaintainableItemRestrictions(
								((MaintainableCollectionDefinition) maintainableItemDefinition)
										.getMaintainableFields(), authorizer,
								restrictions, maintenanceDocument,
								collectionBusinessObject, newPropertyPrefix,
								user);
					    	i++;
				    	}
					}
				} catch (Exception e) {
					throw new RuntimeException(
							"Unable to resolve collection property: "
									+ businessObject.getClass() + ":"
									+ maintainableItemDefinition.getName(), e);
				}
			}
		}
	}
	
	public boolean canFullyUnmaskField(Person user,
			Class<?> dataObjectClass, String fieldName, Document document) {
		// KFSMI-5095
		if(isNonProductionEnvAndUnmaskingTurnedOff())
			return false;

		if(user==null || StringUtils.isEmpty(user.getPrincipalId())) 
			return false;
		Boolean result = null;
		if (document != null) { // if a document was passed, evaluate the permission in the context of a document
			try { // try/catch and fallthrough is a fix for KULRICE-3365
				result = getDocumentHelperService().getDocumentAuthorizer( document )
				.isAuthorizedByTemplate( document, 
						KRADConstants.KNS_NAMESPACE,
						KimConstants.PermissionTemplateNames.FULL_UNMASK_FIELD, 
						user.getPrincipalId(), getFieldPermissionDetails(dataObjectClass, fieldName), null  );
			} catch (IllegalArgumentException e) { 
				// document didn't have needed metadata
				// TODO: this requires intimate knowledge of DocumentHelperServiceImpl 
			} 
		}
		if (result == null) { 
			result = getPermissionService().isAuthorizedByTemplate(user.getPrincipalId(), KRADConstants.KNS_NAMESPACE,
                    KimConstants.PermissionTemplateNames.FULL_UNMASK_FIELD, new HashMap<String, String>(
                    getFieldPermissionDetails(dataObjectClass, fieldName)), Collections.<String, String>emptyMap());
		}
		return result; // should be safe to return Boolean here since the only circumstances that
		               // will leave it null will result in an exception being thrown above.
	}

	public boolean canPartiallyUnmaskField(
			Person user, Class<?> dataObjectClass, String fieldName, Document document) {
		// KFSMI-5095
		if(isNonProductionEnvAndUnmaskingTurnedOff())
			return false;
		
		if(user==null || StringUtils.isEmpty(user.getPrincipalId())) 
			return false;

		if ( document == null ) {
			return getPermissionService().isAuthorizedByTemplate(user.getPrincipalId(), KRADConstants.KNS_NAMESPACE,
                    KimConstants.PermissionTemplateNames.PARTIAL_UNMASK_FIELD, new HashMap<String, String>(
                    getFieldPermissionDetails(dataObjectClass, fieldName)), Collections.<String, String>emptyMap());
		} else { // if a document was passed, evaluate the permission in the context of a document
			return getDocumentHelperService().getDocumentAuthorizer( document )
					.isAuthorizedByTemplate( document, 
											 KRADConstants.KNS_NAMESPACE,
											 KimConstants.PermissionTemplateNames.PARTIAL_UNMASK_FIELD, 
											 user.getPrincipalId(), getFieldPermissionDetails(dataObjectClass, fieldName), Collections.<String, String>emptyMap()  );
		}
	}

	protected Map<String, String> getFieldPermissionDetails(
			Class<?> dataObjectClass, String attributeName) {
		try {
			return getFieldPermissionDetails(dataObjectClass.newInstance(),
					attributeName);
		} catch (Exception e) {
			throw new RuntimeException(
					"The getPermissionDetails method of BusinessObjectAuthorizationServiceImpl was unable to instantiate the dataObjectClass"
							+ dataObjectClass, e);
		}
	}

	protected Map<String, String> getFieldPermissionDetails(
			Object dataObject, String attributeName) {
		Map<String, String> permissionDetails = null;
		String namespaceCode = null;
		String componentName = null;
		String propertyName = null;
		// JHK: commenting out for KFSMI-2398 - permission checks need to be done at the level specified
		// that is, if the parent object specifies the security, that object should be used for the 
		// component
//		if (attributeName.contains(".")) {
//			try {
//				permissionDetails = KimCommonUtils
//						.getNamespaceAndComponentSimpleName(PropertyUtils
//								.getPropertyType(businessObject, attributeName
//										.substring(0, attributeName
//												.lastIndexOf("."))));
//			} catch (Exception e) {
//				throw new RuntimeException(
//						"Unable to discover nested business object class: "
//								+ businessObject.getClass() + " : "
//								+ attributeName, e);
//			}

//			permissionDetails.put(KimAttributes.PROPERTY_NAME, attributeName
//					.substring(attributeName.indexOf(".") + 1));
//		} else {
			permissionDetails = KRADUtils
					.getNamespaceAndComponentSimpleName(dataObject.getClass());
			permissionDetails.put(KimConstants.AttributeConstants.PROPERTY_NAME, attributeName);
//		}
		return permissionDetails;
	}
	
	protected Map<String, String> getButtonFieldPermissionDetails(
			Object businessObject, String attributeName) {
		Map<String, String> permissionDetails = new HashMap<String, String>();
		if (attributeName.contains(".")) {
			permissionDetails.put(KimConstants.AttributeConstants.BUTTON_NAME, attributeName);
		} else {
			permissionDetails.put(KimConstants.AttributeConstants.BUTTON_NAME, attributeName);
		}
		return permissionDetails;
	}

	private PermissionService getPermissionService() {
		if (permissionService == null) {
			permissionService = KimApiServiceLocator
					.getPermissionService();
		}
		return permissionService;
	}

	private BusinessObjectDictionaryService getBusinessObjectDictionaryService() {
		if (businessObjectDictionaryService == null) {
			businessObjectDictionaryService = KNSServiceLocator
					.getBusinessObjectDictionaryService();
		}
		return businessObjectDictionaryService;
	}

	private MaintenanceDocumentDictionaryService getMaintenanceDocumentDictionaryService() {
		if (maintenanceDocumentDictionaryService == null) {
			maintenanceDocumentDictionaryService = KNSServiceLocator
					.getMaintenanceDocumentDictionaryService();
		}
		return maintenanceDocumentDictionaryService;
	}

	private ConfigurationService getKualiConfigurationService() {
		if (kualiConfigurationService == null) {
			kualiConfigurationService = CoreApiServiceLocator.getKualiConfigurationService();
		}
		return kualiConfigurationService;
	}

	private boolean isNonProductionEnvAndUnmaskingTurnedOff(){
		return !getKualiConfigurationService().getPropertyValueAsString(KRADConstants.PROD_ENVIRONMENT_CODE_KEY)
                .equalsIgnoreCase(
                        getKualiConfigurationService().getPropertyValueAsString(KRADConstants.ENVIRONMENT_KEY)) &&
				!getKualiConfigurationService().getPropertyValueAsBoolean(KRADConstants.ENABLE_NONPRODUCTION_UNMASKING);
	}

    protected DocumentHelperService getDocumentHelperService() {
        return KNSServiceLocator.getDocumentHelperService();
    }

}
