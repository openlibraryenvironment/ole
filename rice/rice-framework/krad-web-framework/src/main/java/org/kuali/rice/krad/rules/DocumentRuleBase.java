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
package org.kuali.rice.krad.rules;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.coreservice.framework.parameter.ParameterConstants;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteRecipient;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.document.TransactionalDocument;
import org.kuali.rice.krad.rules.rule.AddAdHocRoutePersonRule;
import org.kuali.rice.krad.rules.rule.AddAdHocRouteWorkgroupRule;
import org.kuali.rice.krad.rules.rule.AddNoteRule;
import org.kuali.rice.krad.rules.rule.ApproveDocumentRule;
import org.kuali.rice.krad.rules.rule.CompleteDocumentRule;
import org.kuali.rice.krad.rules.rule.RouteDocumentRule;
import org.kuali.rice.krad.rules.rule.SaveDocumentRule;
import org.kuali.rice.krad.rules.rule.SendAdHocRequestsRule;
import org.kuali.rice.krad.rules.rule.event.ApproveDocumentEvent;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.DictionaryValidationService;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.MessageMap;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.RouteToCompletionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains all of the business rules that are common to all documents
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DocumentRuleBase implements SaveDocumentRule, RouteDocumentRule, ApproveDocumentRule, AddNoteRule,
        AddAdHocRoutePersonRule, AddAdHocRouteWorkgroupRule, SendAdHocRequestsRule, CompleteDocumentRule {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentRuleBase.class);

    private static PersonService personService;
    private static DictionaryValidationService dictionaryValidationService;
    private static DocumentDictionaryService documentDictionaryService;
    private static ConfigurationService kualiConfigurationService;
    private static GroupService groupService;
    private static PermissionService permissionService;
    private static DocumentTypeService documentTypeService;
    private static DataDictionaryService dataDictionaryService;

    // just some arbitrarily high max depth that's unlikely to occur in real life to prevent recursion problems
    private int maxDictionaryValidationDepth = 100;

    /**
     * Verifies that the document's overview fields are valid - it does required and format checks.
     *
     * @param document
     * @return boolean True if the document description is valid, false otherwise.
     */
    public boolean isDocumentOverviewValid(Document document) {
        // add in the documentHeader path
        GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);
        GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_HEADER_PROPERTY_NAME);

        // check the document header for fields like the description
        getDictionaryValidationService().validateBusinessObject(document.getDocumentHeader());
        validateSensitiveDataValue(KRADPropertyConstants.EXPLANATION, document.getDocumentHeader().getExplanation(),
                getDataDictionaryService().getAttributeLabel(DocumentHeader.class, KRADPropertyConstants.EXPLANATION));
        validateSensitiveDataValue(KRADPropertyConstants.DOCUMENT_DESCRIPTION,
                document.getDocumentHeader().getDocumentDescription(), getDataDictionaryService().getAttributeLabel(
                DocumentHeader.class, KRADPropertyConstants.DOCUMENT_DESCRIPTION));

        // drop the error path keys off now
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_HEADER_PROPERTY_NAME);
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        return GlobalVariables.getMessageMap().hasNoErrors();
    }

    /**
     * Validates the document attributes against the data dictionary.
     *
     * @param document
     * @param validateRequired if true, then an error will be retruned if a DD required field is empty. if false, no
     * required
     * checking is done
     * @return True if the document attributes are valid, false otherwise.
     */
    public boolean isDocumentAttributesValid(Document document, boolean validateRequired) {
        // start updating the error path name
        GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        // check the document for fields like explanation and org doc #
        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(document,
                getMaxDictionaryValidationDepth(), validateRequired);

        // drop the error path keys off now
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        return GlobalVariables.getMessageMap().hasNoErrors();
    }

    /**
     * Runs all business rules needed prior to saving. This includes both common rules for all documents, plus
     * class-specific
     * business rules. This method will only return false if it fails the isValidForSave() test. Otherwise, it will
     * always return
     * positive regardless of the outcome of the business rules. However, any error messages resulting from the business
     * rules will
     * still be populated, for display to the consumer of this service.
     *
     * @see org.kuali.rice.krad.rules.rule.SaveDocumentRule#processSaveDocument(org.kuali.rice.krad.document.Document)
     */
    public boolean processSaveDocument(Document document) {
        boolean isValid = true;

        isValid = isDocumentOverviewValid(document);

        GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        getDictionaryValidationService().validateDocumentAndUpdatableReferencesRecursively(document,
                getMaxDictionaryValidationDepth(), false);
        getDictionaryValidationService().validateDefaultExistenceChecksForTransDoc((TransactionalDocument) document);

        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.DOCUMENT_PROPERTY_NAME);

        isValid &= GlobalVariables.getMessageMap().hasNoErrors();
        isValid &= processCustomSaveDocumentBusinessRules(document);

        return isValid;
    }

    /**
     * This method should be overridden by children rule classes as a hook to implement document specific business rule
     * checks for
     * the "save document" event.
     *
     * @param document
     * @return boolean True if the rules checks passed, false otherwise.
     */
    protected boolean processCustomSaveDocumentBusinessRules(Document document) {
        return true;
    }

    /**
     * Runs all business rules needed prior to routing. This includes both common rules for all maintenance documents,
     * plus
     * class-specific business rules. This method will return false if any business rule fails, or if the document is in
     * an invalid
     * state, and not routable (see isDocumentValidForRouting()).
     *
     * @see org.kuali.rice.krad.rules.rule.RouteDocumentRule#processRouteDocument(org.kuali.rice.krad.document.Document)
     */
    public boolean processRouteDocument(Document document) {
        boolean isValid = true;

        isValid = isDocumentOverviewValid(document);

        boolean completeRequestPending = RouteToCompletionUtil.checkIfAtleastOneAdHocCompleteRequestExist(document);

        // Validate the document if the header is valid and no pending completion requests
        if (isValid && !completeRequestPending) {
            isValid &= isDocumentAttributesValid(document, true);
            isValid &= processCustomRouteDocumentBusinessRules(document);
        }

        return isValid;
    }

    /**
     * This method should be overridden by children rule classes as a hook to implement document specific business rule
     * checks for
     * the "route document" event.
     *
     * @param document
     * @return boolean True if the rules checks passed, false otherwise.
     */
    protected boolean processCustomRouteDocumentBusinessRules(Document document) {
        return true;
    }

    /**
     * Runs all business rules needed prior to approving. This includes both common rules for all documents, plus
     * class-specific
     * business rules. This method will return false if any business rule fails, or if the document is in an invalid
     * state, and not
     * approveble.
     *
     * @see org.kuali.rice.krad.rules.rule.ApproveDocumentRule#processApproveDocument(org.kuali.rice.krad.rules.rule.event.ApproveDocumentEvent)
     */
    public boolean processApproveDocument(ApproveDocumentEvent approveEvent) {
        boolean isValid = true;

        isValid = processCustomApproveDocumentBusinessRules(approveEvent);

        return isValid;
    }

    /**
     * This method should be overridden by children rule classes as a hook to implement document specific business rule
     * checks for
     * the "approve document" event.
     *
     * @param approveEvent
     * @return boolean True if the rules checks passed, false otherwise.
     */
    protected boolean processCustomApproveDocumentBusinessRules(ApproveDocumentEvent approveEvent) {
        return true;
    }

    /**
     * Runs all business rules needed prior to adding a document note. This method will return false if any business
     * rule fails
     */
    public boolean processAddNote(Document document, Note note) {
        boolean isValid = true;

        isValid &= isNoteValid(note);
        isValid &= processCustomAddNoteBusinessRules(document, note);

        return isValid;
    }

    /**
     * Verifies that the note's fields are valid - it does required and format checks.
     *
     * @param note
     * @return boolean True if the document description is valid, false otherwise.
     */
    public boolean isNoteValid(Note note) {
        // add the error path keys on the stack
        GlobalVariables.getMessageMap().addToErrorPath(UifPropertyPaths.NEW_COLLECTION_LINES
                + "['"
                + KRADConstants.DOCUMENT_PROPERTY_NAME
                + "."
                + KRADConstants.NOTES_PROPERTY_NAME
                + "']");

        // check the document header for fields like the description
        getDictionaryValidationService().validateBusinessObject(note);

        validateSensitiveDataValue(KRADConstants.NOTE_TEXT_PROPERTY_NAME, note.getNoteText(),
                getDataDictionaryService().getAttributeLabel(Note.class, KRADConstants.NOTE_TEXT_PROPERTY_NAME));

        // drop the error path keys off now
        GlobalVariables.getMessageMap().removeFromErrorPath(UifPropertyPaths.NEW_COLLECTION_LINES
                + "['"
                + KRADConstants.DOCUMENT_PROPERTY_NAME
                + "."
                + KRADConstants.NOTES_PROPERTY_NAME
                + "']");

        return GlobalVariables.getMessageMap().hasNoErrors();
    }

    /**
     * This method should be overridden by children rule classes as a hook to implement document specific business rule
     * checks for
     * the "add document note" event.
     *
     * @param document
     * @param note
     * @return boolean True if the rules checks passed, false otherwise.
     */
    protected boolean processCustomAddNoteBusinessRules(Document document, Note note) {
        return true;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.AddAdHocRoutePersonRule#processAddAdHocRoutePerson(org.kuali.rice.krad.document.Document,
     *      org.kuali.rice.krad.bo.AdHocRoutePerson)
     */
    public boolean processAddAdHocRoutePerson(Document document, AdHocRoutePerson adHocRoutePerson) {
        boolean isValid = true;

        isValid &= isAddHocRoutePersonValid(document, adHocRoutePerson);

        isValid &= processCustomAddAdHocRoutePersonBusinessRules(document, adHocRoutePerson);
        return isValid;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.SendAdHocRequestsRule#processSendAdHocRequests(org.kuali.rice.krad.document.Document)
     */
    public boolean processSendAdHocRequests(Document document) {
        boolean isValid = true;

        isValid &= isAdHocRouteRecipientsValid(document);
        isValid &= processCustomSendAdHocRequests(document);

        return isValid;
    }

    protected boolean processCustomSendAdHocRequests(Document document) {
        return true;
    }

    /**
     * Checks the adhoc route recipient list to ensure there are recipients or
     * else throws an error that at least one recipient is required.
     *
     * @param document
     * @return
     */
    protected boolean isAdHocRouteRecipientsValid(Document document) {
        boolean isValid = true;
        MessageMap errorMap = GlobalVariables.getMessageMap();

        if (errorMap.getErrorPath().size() == 0) {
            // add the error path keys on the stack
            errorMap.addToErrorPath(KRADConstants.NEW_AD_HOC_ROUTE_PERSON_PROPERTY_NAME);
        }

        if ((document.getAdHocRoutePersons() == null || document.getAdHocRoutePersons().isEmpty()) && (document
                .getAdHocRouteWorkgroups() == null || document.getAdHocRouteWorkgroups().isEmpty())) {

            GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID, "error.adhoc.missing.recipients");
            isValid = false;
        }

        // drop the error path keys off now
        errorMap.removeFromErrorPath(KRADConstants.NEW_AD_HOC_ROUTE_PERSON_PROPERTY_NAME);

        return isValid;
    }

    /**
     * Verifies that the adHocRoutePerson's fields are valid - it does required and format checks.
     *
     * @param person
     * @return boolean True if valid, false otherwise.
     */
    public boolean isAddHocRoutePersonValid(Document document, AdHocRoutePerson person) {
        MessageMap errorMap = GlobalVariables.getMessageMap();

        // new recipients are not embedded in the error path; existing lines should be
        if (errorMap.getErrorPath().size() == 0) {
            // add the error path keys on the stack
            errorMap.addToErrorPath(KRADConstants.NEW_AD_HOC_ROUTE_PERSON_PROPERTY_NAME);
        }

        String actionRequestedCode = person.getActionRequested();
        if (StringUtils.isNotBlank(person.getId())) {
            Person user = getPersonService().getPersonByPrincipalName(person.getId());

            if (user == null) {
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID,
                        RiceKeyConstants.ERROR_INVALID_ADHOC_PERSON_ID);
            } 
            else if (!getPermissionService().hasPermission(user.getPrincipalId(),
                    KimConstants.KIM_TYPE_DEFAULT_NAMESPACE, KimConstants.PermissionNames.LOG_IN)) {
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID,
                        RiceKeyConstants.ERROR_INACTIVE_ADHOC_PERSON_ID);
            }
            else if(this.isAdHocRouteCompletionToInitiator(document, user, actionRequestedCode)){
                // KULRICE-7419: Adhoc route completion validation rule (should not route to initiator for completion)
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID,
                        RiceKeyConstants.ERROR_ADHOC_COMPLETE_PERSON_IS_INITIATOR);
            } 
            else if(StringUtils.equals(actionRequestedCode, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ) && this.hasAdHocRouteCompletion(document, person)){
                // KULRICE-8760: Multiple complete adhoc requests should not be allowed on the same document
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID,
                        RiceKeyConstants.ERROR_ADHOC_COMPLETE_MORE_THAN_ONE);
            }
            else {
                Class docOrBoClass = null;
                if (document instanceof MaintenanceDocument) {
                    docOrBoClass = ((MaintenanceDocument) document).getNewMaintainableObject().getDataObjectClass();
                } else {
                    docOrBoClass = document.getClass();
                }

                if (!getDocumentDictionaryService().getDocumentAuthorizer(document).canReceiveAdHoc(document, user, actionRequestedCode)) {
                    GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID,
                            RiceKeyConstants.ERROR_UNAUTHORIZED_ADHOC_PERSON_ID);
                }
            }
        } else {
            GlobalVariables.getMessageMap().putError(KRADPropertyConstants.ID,
                    RiceKeyConstants.ERROR_MISSING_ADHOC_PERSON_ID);
        }

        // drop the error path keys off now
        errorMap.removeFromErrorPath(KRADConstants.NEW_AD_HOC_ROUTE_PERSON_PROPERTY_NAME);

        return GlobalVariables.getMessageMap().hasNoErrors();
    }
    
    /**
     * KULRICE-7419: Adhoc route completion validation rule (should not route to initiator for completion)
     * 
     * determine whether the document initiator is the same as the adhoc recipient for completion
     */
    protected boolean isAdHocRouteCompletionToInitiator(Document document, Person person, String actionRequestCode){
        if(!StringUtils.equals(actionRequestCode, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ)){
            return false;
        }
        
        String documentInitiator = document.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId();       
        String adhocRecipient = person.getPrincipalId();
        
        return StringUtils.equals(documentInitiator, adhocRecipient);
    }
    
    /**
     * KULRICE-8760: check whether there is any other complete adhoc request on the given document 
     */
    protected boolean hasAdHocRouteCompletion(Document document, AdHocRouteRecipient adHocRouteRecipient){         
        List<AdHocRoutePerson> adHocRoutePersons = document.getAdHocRoutePersons();
        if(ObjectUtils.isNotNull(adHocRoutePersons)){
            for(AdHocRoutePerson adhocRecipient : adHocRoutePersons){
                // the given adhoc route recipient doesn't count
                if(adHocRouteRecipient==adhocRecipient){
                    continue;
                }
                
                String actionRequestCode = adhocRecipient.getActionRequested();
                if(StringUtils.equals(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, actionRequestCode)){
                    return true;
                }
            }
        }
        
        List<AdHocRouteWorkgroup> adHocRouteWorkgroups = document.getAdHocRouteWorkgroups();
        if(ObjectUtils.isNotNull(adHocRouteWorkgroups)){
            for(AdHocRouteWorkgroup adhocRecipient : adHocRouteWorkgroups){
                // the given adhoc route recipient doesn't count
                if(adHocRouteRecipient==adhocRecipient){
                    continue;
                }
                
                String actionRequestCode = adhocRecipient.getActionRequested();
                if(StringUtils.equals(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, actionRequestCode)){
                    return true;
                }
            }
        }        
        
        return false;
    }    
    
    /**
     * This method should be overridden by children rule classes as a hook to implement document specific business rule
     * checks for
     * the "add ad hoc route person" event.
     *
     * @param document
     * @param person
     * @return boolean True if the rules checks passed, false otherwise.
     */
    protected boolean processCustomAddAdHocRoutePersonBusinessRules(Document document, AdHocRoutePerson person) {
        return true;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.AddAdHocRouteWorkgroupRule#processAddAdHocRouteWorkgroup(org.kuali.rice.krad.document.Document,
     *      org.kuali.rice.krad.bo.AdHocRouteWorkgroup)
     */
    public boolean processAddAdHocRouteWorkgroup(Document document, AdHocRouteWorkgroup adHocRouteWorkgroup) {
        boolean isValid = true;

        isValid &= isAddHocRouteWorkgroupValid(document, adHocRouteWorkgroup);

        isValid &= processCustomAddAdHocRouteWorkgroupBusinessRules(document, adHocRouteWorkgroup);
        return isValid;
    }

    /**
     * Verifies that the adHocRouteWorkgroup's fields are valid - it does required and format checks.
     *
     * @param workgroup
     * @return boolean True if valid, false otherwise.
     */
    public boolean isAddHocRouteWorkgroupValid(Document document, AdHocRouteWorkgroup workgroup) {
        MessageMap errorMap = GlobalVariables.getMessageMap();

        // new recipients are not embedded in the error path; existing lines should be
        if (errorMap.getErrorPath().size() == 0) {
            // add the error path keys on the stack
            GlobalVariables.getMessageMap().addToErrorPath(KRADConstants.NEW_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME);
        }

        if (workgroup.getRecipientName() != null && workgroup.getRecipientNamespaceCode() != null) {
            // validate that they are a workgroup from the workgroup service by looking them up
            try {
                Group group = getGroupService().getGroupByNamespaceCodeAndName(workgroup.getRecipientNamespaceCode(),
                        workgroup.getRecipientName());
                
                String actionRequestedCode = workgroup.getActionRequested();
                if (group == null || !group.isActive()) {
                    //  KULRICE-8091: Adhoc routing tab utilizing Groups on all documents missing asterisks 
                    GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAME,
                            RiceKeyConstants.ERROR_INVALID_ADHOC_WORKGROUP_ID);
                    GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAMESPACE_CODE, RiceKeyConstants.ERROR_ADHOC_INVALID_WORKGROUP_NAMESPACE);
                } 
                else if(StringUtils.equals(actionRequestedCode, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ) && this.hasAdHocRouteCompletion(document, workgroup)){
                    // KULRICE-8760: Multiple complete adhoc requests should not be allowed on the same document
                    GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAMESPACE_CODE,
                            RiceKeyConstants.ERROR_ADHOC_COMPLETE_MORE_THAN_ONE);
                }
                else {
                    org.kuali.rice.kew.api.document.WorkflowDocumentService
                            wds = KewApiServiceLocator.getWorkflowDocumentService();
                    DocumentType documentType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(
                            wds.getDocument(document.getDocumentNumber()).getDocumentTypeName());
                    Map<String, String> permissionDetails = buildDocumentTypeActionRequestPermissionDetails(
                            documentType, workgroup.getActionRequested());
                    if (useKimPermission(KewApiConstants.KEW_NAMESPACE, KewApiConstants.AD_HOC_REVIEW_PERMISSION, permissionDetails) ){
                        List<String> principalIds = getGroupService().getMemberPrincipalIds(group.getId());
                        // if any member of the group is not allowed to receive the request, then the group may not receive it
                        for (String principalId : principalIds) {
                            if (!getPermissionService().isAuthorizedByTemplate(principalId,
                                    KewApiConstants.KEW_NAMESPACE, KewApiConstants.AD_HOC_REVIEW_PERMISSION,
                                    permissionDetails, new HashMap<String, String>())) {
                                
                                //  KULRICE-8091: Adhoc routing tab utilizing Groups on all documents missing asterisks 
                                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAME,
                                        RiceKeyConstants.ERROR_UNAUTHORIZED_ADHOC_WORKGROUP_ID);
                                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAMESPACE_CODE, RiceKeyConstants.ERROR_ADHOC_INVALID_WORKGROUP_NAMESPACE);
                                
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("isAddHocRouteWorkgroupValid(AdHocRouteWorkgroup)", e);
                
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAME,
                        RiceKeyConstants.ERROR_INVALID_ADHOC_WORKGROUP_ID);
                
                //  KULRICE-8091: Adhoc routing tab utilizing Groups on all documents missing asterisks 
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAMESPACE_CODE, RiceKeyConstants.ERROR_ADHOC_INVALID_WORKGROUP_NAMESPACE);
            }
        } else {
            //  KULRICE-8091: Adhoc routing tab utilizing Groups on all documents missing asterisks 
            if(workgroup.getRecipientNamespaceCode()==null) {
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAMESPACE_CODE, RiceKeyConstants.ERROR_ADHOC_INVALID_WORKGROUP_NAMESPACE_MISSING);
            }
            
            if(workgroup.getRecipientName()==null) {
                GlobalVariables.getMessageMap().putError(KRADPropertyConstants.RECIPIENT_NAME,
                    RiceKeyConstants.ERROR_MISSING_ADHOC_WORKGROUP_ID);
            }
        }

        // drop the error path keys off now
        GlobalVariables.getMessageMap().removeFromErrorPath(KRADConstants.NEW_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME);

        return GlobalVariables.getMessageMap().hasNoErrors();
    }
    /**
     * This method should be overridden by children rule classes as a hook to implement document specific business rule
     * checks for
     * the "add ad hoc route workgroup" event.
     *
     * @param document
     * @param workgroup
     * @return boolean True if the rules checks passed, false otherwise.
     */
    protected boolean processCustomAddAdHocRouteWorkgroupBusinessRules(Document document,
            AdHocRouteWorkgroup workgroup) {
        return true;
    }

    /**
     * Gets the maximum number of levels the data-dictionary based validation will recurse for the document
     */
    public int getMaxDictionaryValidationDepth() {
        return this.maxDictionaryValidationDepth;
    }

    /**
     * Gets the maximum number of levels the data-dictionary based validation will recurse for the document
     */
    public void setMaxDictionaryValidationDepth(int maxDictionaryValidationDepth) {
        if (maxDictionaryValidationDepth < 0) {
            LOG.error("Dictionary validation depth should be greater than or equal to 0.  Value received was: "
                    + maxDictionaryValidationDepth);
            throw new RuntimeException(
                    "Dictionary validation depth should be greater than or equal to 0.  Value received was: "
                            + maxDictionaryValidationDepth);
        }
        this.maxDictionaryValidationDepth = maxDictionaryValidationDepth;
    }

    protected boolean validateSensitiveDataValue(String fieldName, String fieldValue, String fieldLabel) {
        boolean dataValid = true;

        if (fieldValue == null) {
            return dataValid;
        }

        boolean patternFound = KRADUtils.containsSensitiveDataPatternMatch(fieldValue);
        boolean warnForSensitiveData = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(
                KRADConstants.KNS_NAMESPACE, ParameterConstants.ALL_COMPONENT,
                KRADConstants.SystemGroupParameterNames.SENSITIVE_DATA_PATTERNS_WARNING_IND);
        if (patternFound && !warnForSensitiveData) {
            dataValid = false;
            GlobalVariables.getMessageMap().putError(fieldName,
                    RiceKeyConstants.ERROR_DOCUMENT_FIELD_CONTAINS_POSSIBLE_SENSITIVE_DATA, fieldLabel);
        }

        return dataValid;
    }

    /**
     * Business rules check will include all save action rules and any custom rules required by the document specific rule implementation
     *
     * @param document Document
     * @return true if all validations are passed
     */
    public boolean processCompleteDocument(Document document) {
        boolean isValid = true;
        isValid &= processSaveDocument(document);
        isValid &= processCustomCompleteDocumentBusinessRules(document);
        return isValid;
    }

    /**
     * Hook method for deriving business rule classes to provide custom validations required during completion action
     *
     * @param document
     * @return default is true
     */
    protected boolean processCustomCompleteDocumentBusinessRules(Document document) {
        return true;
    }

    protected boolean useKimPermission(String namespace, String permissionTemplateName, Map<String, String> permissionDetails) {
		Boolean b =  CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KewApiConstants.KIM_PRIORITY_ON_DOC_TYP_PERMS_IND);
		if (b == null || b) {
			return getPermissionService().isPermissionDefinedByTemplate(namespace, permissionTemplateName,
                    permissionDetails);
		}
		return false;
	}
    protected Map<String, String> buildDocumentTypeActionRequestPermissionDetails(DocumentType documentType, String actionRequestCode) {
		Map<String, String> details = buildDocumentTypePermissionDetails(documentType);
		if (!StringUtils.isBlank(actionRequestCode)) {
			details.put(KewApiConstants.ACTION_REQUEST_CD_DETAIL, actionRequestCode);
		}
		return details;
	}

    protected Map<String, String> buildDocumentTypePermissionDetails(DocumentType documentType) {
		Map<String, String> details = new HashMap<String, String>();
		details.put(KewApiConstants.DOCUMENT_TYPE_NAME_DETAIL, documentType.getName());
		return details;
	}

    protected DataDictionaryService getDataDictionaryService() {
        if (dataDictionaryService == null) {
            dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
        }
        return dataDictionaryService;
    }

    protected PersonService getPersonService() {
        if (personService == null) {
            personService = KimApiServiceLocator.getPersonService();
        }
        return personService;
    }

    public static GroupService getGroupService() {
        if (groupService == null) {
            groupService = KimApiServiceLocator.getGroupService();
        }
        return groupService;
    }

    public static PermissionService getPermissionService() {
        if (permissionService == null) {
            permissionService = KimApiServiceLocator.getPermissionService();
        }
        return permissionService;
    }

    protected DictionaryValidationService getDictionaryValidationService() {
        if (dictionaryValidationService == null) {
            dictionaryValidationService = KRADServiceLocatorWeb.getDictionaryValidationService();
        }
        return dictionaryValidationService;
    }

    protected ConfigurationService getKualiConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = CoreApiServiceLocator.getKualiConfigurationService();
        }
        return kualiConfigurationService;
    }

    protected static DocumentDictionaryService getDocumentDictionaryService() {
        if (documentDictionaryService == null) {
            documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return documentDictionaryService;
    }

    public static void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        DocumentRuleBase.documentDictionaryService = documentDictionaryService;
    }
}
