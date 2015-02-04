/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.rice.krms.impl.rule;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.bo.GlobalBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.framework.type.ActionTypeService;
import org.kuali.rice.krms.framework.type.AgendaTypeService;
import org.kuali.rice.krms.impl.authorization.AgendaAuthorizationService;
import org.kuali.rice.krms.impl.repository.*;
import org.kuali.rice.krms.impl.ui.AgendaEditor;
import org.kuali.rice.krms.impl.util.KRMSPropertyConstants;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

/**
 * This class contains the rules for the AgendaEditor.
 */
public class AgendaEditorBusRule extends MaintenanceDocumentRuleBase {


    protected boolean primaryKeyCheck(MaintenanceDocument document) {
        // default to success if no failures
        boolean success = true;
        Class<?> dataObjectClass = document.getNewMaintainableObject().getDataObjectClass();

        // Since the dataObject is a wrapper class we need to return the agendaBo instead.
        Object oldBo = ((AgendaEditor) document.getOldMaintainableObject().getDataObject()).getAgenda();
        Object newDataObject = ((AgendaEditor) document.getNewMaintainableObject().getDataObject()).getAgenda();

        // We dont do primaryKeyChecks on Global Business Object maintenance documents. This is
        // because it doesnt really make any sense to do so, given the behavior of Globals. When a
        // Global Document completes, it will update or create a new record for each BO in the list.
        // As a result, there's no problem with having existing BO records in the system, they will
        // simply get updated.
        if (newDataObject instanceof GlobalBusinessObject) {
            return success;
        }

        // fail and complain if the person has changed the primary keys on
        // an EDIT maintenance document.
        if (document.isEdit()) {
            if (!getDataObjectMetaDataService().equalsByPrimaryKeys(oldBo, newDataObject)) {
                // add a complaint to the errors
                putDocumentError(KRADConstants.DOCUMENT_ERRORS,
                        RiceKeyConstants.ERROR_DOCUMENT_MAINTENANCE_PRIMARY_KEYS_CHANGED_ON_EDIT,
                        getHumanReadablePrimaryKeyFieldNames(dataObjectClass));
                success &= false;
            }
        }

        // fail and complain if the person has selected a new object with keys that already exist
        // in the DB.
        else if (document.isNew()) {

            // TODO: when/if we have standard support for DO retrieval, do this check for DO's
            if (newDataObject instanceof PersistableBusinessObject) {

                // get a map of the pk field names and values
                Map<String, ?> newPkFields = getDataObjectMetaDataService().getPrimaryKeyFieldValues(newDataObject);

                // TODO: Good suggestion from Aaron, dont bother checking the DB, if all of the
                // objects PK fields dont have values. If any are null or empty, then
                // we're done. The current way wont fail, but it will make a wasteful
                // DB call that may not be necessary, and we want to minimize these.

                // attempt to do a lookup, see if this object already exists by these Primary Keys
                PersistableBusinessObject testBo = getBoService()
                        .findByPrimaryKey(dataObjectClass.asSubclass(PersistableBusinessObject.class), newPkFields);

                // if the retrieve was successful, then this object already exists, and we need
                // to complain
                if (testBo != null) {
                    putDocumentError(KRADConstants.DOCUMENT_ERRORS,
                            RiceKeyConstants.ERROR_DOCUMENT_MAINTENANCE_KEYS_ALREADY_EXIST_ON_CREATE_NEW,
                            getHumanReadablePrimaryKeyFieldNames(dataObjectClass));
                    success &= false;
                }
            }
        }

        return success;
    }




    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;

        AgendaEditor agendaEditor = (AgendaEditor) document.getNewMaintainableObject().getDataObject();
        AgendaEditor oldAgendaEditor = (AgendaEditor) document.getOldMaintainableObject().getDataObject();
        isValid &= validContext(agendaEditor);
        isValid &= validAgendaName(agendaEditor);
        isValid &= validContextAgendaNamespace(agendaEditor);
        isValid &= validAgendaTypeAndAttributes(oldAgendaEditor, agendaEditor);

        return isValid;
    }

    /**
     * Check if the context exists and if user has authorization to edit agendas under this context.
     * @param agendaEditor
     * @return true if the context exist and has authorization, false otherwise
     */
    public boolean validContext(AgendaEditor agendaEditor) {
        boolean isValid = true;

        try {
            if (getContextBoService().getContextByContextId(agendaEditor.getAgenda().getContextId()) == null) {
                this.putFieldError(KRMSPropertyConstants.Agenda.CONTEXT, "error.agenda.invalidContext");
                isValid = false;
            } else {
                if (!getAgendaAuthorizationService().isAuthorized(KrmsConstants.MAINTAIN_KRMS_AGENDA,
                        agendaEditor.getAgenda().getContextId())) {
                    this.putFieldError(KRMSPropertyConstants.Agenda.CONTEXT, "error.agenda.unauthorizedContext");
                    isValid = false;
                }
            }
        }
        catch (IllegalArgumentException e) {
            this.putFieldError(KRMSPropertyConstants.Agenda.CONTEXT, "error.agenda.invalidContext");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Check if for namespace.
     * @param agendaEditor
     * @return
     */
    public boolean validContextAgendaNamespace(AgendaEditor agendaEditor) {
        // TODO validate through krms_cntxt_vld_agenda_t
        if (StringUtils.isBlank(agendaEditor.getNamespace())) {
            this.putFieldError(KRMSPropertyConstants.Context.NAMESPACE, "error.context.invalidNamespace");
            return false;
        }
        return true;
    }

    private boolean validAgendaTypeAndAttributes( AgendaEditor oldAgendaEditor, AgendaEditor newAgendaEditor) {
        if (validAgendaType(newAgendaEditor.getAgenda().getTypeId(), newAgendaEditor.getAgenda().getContextId())) {
            return validAgendaAttributes(oldAgendaEditor, newAgendaEditor);
        } else {
            return false;
        }
    }
    private boolean validAgendaType(String typeId, String contextId) {
        boolean isValid = true;

        if (!StringUtils.isBlank(typeId) && !StringUtils.isBlank(contextId)) {
            if (getKrmsTypeRepositoryService().getAgendaTypeByAgendaTypeIdAndContextId(typeId, contextId) != null) {
                return true;
            } else {
                this.putFieldError(KRMSPropertyConstants.Agenda.TYPE, "error.agenda.invalidType");
                return false;
            }
        }

        return isValid;
    }

    private boolean validAgendaAttributes(AgendaEditor oldAgendaEditor, AgendaEditor newAgendaEditor) {
        boolean isValid = true;

        String typeId = newAgendaEditor.getAgenda().getTypeId();

        if (!StringUtils.isEmpty(typeId)) {
            KrmsTypeDefinition typeDefinition = getKrmsTypeRepositoryService().getTypeById(typeId);

            if (typeDefinition == null) {
                throw new IllegalStateException("agenda typeId must match the id of a valid krms type");
            } else if (StringUtils.isBlank(typeDefinition.getServiceName())) {
                throw new IllegalStateException("agenda type definition must have a non-blank service name");
            } else {
                AgendaTypeService agendaTypeService =
                        (AgendaTypeService)KrmsRepositoryServiceLocator.getService(typeDefinition.getServiceName());

                if (agendaTypeService == null) {
                    throw new IllegalStateException("typeDefinition must have a valid serviceName");
                } else {

                    List<RemotableAttributeError> errors;
                    if (oldAgendaEditor == null) {
                        errors = agendaTypeService.validateAttributes(typeId, newAgendaEditor.getCustomAttributesMap());
                    } else {
                        errors = agendaTypeService.validateAttributesAgainstExisting(typeId, newAgendaEditor.getCustomAttributesMap(), oldAgendaEditor.getCustomAttributesMap());
                    }

                    if (!CollectionUtils.isEmpty(errors)) {
                        isValid = false;
                        for (RemotableAttributeError error : errors) {
                            for (String errorStr : error.getErrors()) {
                                this.putFieldError(
                                        KRMSPropertyConstants.AgendaEditor.CUSTOM_ATTRIBUTES_MAP +
                                                "['" + error.getAttributeName() + "']",
                                        errorStr
                                );
                            }
                        }
                    }
                }
            }
        }
        return isValid;
    }

    /**
     * Check if an agenda with that name exists already in the context.
     * @param agendaEditor
     * @return true if agenda name is unique, false otherwise
     */
    public boolean validAgendaName(AgendaEditor agendaEditor) {
        try {
            AgendaDefinition agendaFromDataBase = getAgendaBoService().getAgendaByNameAndContextId(
                    agendaEditor.getAgenda().getName(), agendaEditor.getAgenda().getContextId());
            if ((agendaFromDataBase != null) && !StringUtils.equals(agendaFromDataBase.getId(), agendaEditor.getAgenda().getId())) {
                this.putFieldError(KRMSPropertyConstants.Agenda.NAME, "error.agenda.duplicateName");
                return false;
            }
        }
        catch (IllegalArgumentException e) {
            this.putFieldError(KRMSPropertyConstants.Agenda.NAME, "error.agenda.invalidName");
            return false;
        }
        return true;
    }

    /**
     * Check if a agenda item is valid.
     *
     * @param document, the Agenda document of the added/edited agenda item
     * @return true if agenda item is valid, false otherwise
     */
    public boolean processAgendaItemBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;

        AgendaEditor newAgendaEditor = (AgendaEditor) document.getNewMaintainableObject().getDataObject();
        AgendaEditor oldAgendaEditor = (AgendaEditor) document.getOldMaintainableObject().getDataObject();
        RuleBo rule = newAgendaEditor.getAgendaItemLine().getRule();
        isValid &= validateRuleName(rule, newAgendaEditor.getAgenda());
        isValid &= validRuleType(rule.getTypeId(), newAgendaEditor.getAgenda().getContextId());
        isValid &= validateRuleAction(oldAgendaEditor, newAgendaEditor);

        return isValid;
    }

    /**
     * Check if a rule with that name exists already in the namespace.
     * @param rule
     * @parm agenda
     * @return true if rule name is unique, false otherwise
     */
    private boolean validateRuleName(RuleBo rule, AgendaBo agenda) {
        if (StringUtils.isBlank(rule.getName())) {
            this.putFieldError(KRMSPropertyConstants.Rule.NAME, "error.rule.invalidName");
            return false;
        }
        // check current bo for rules (including ones that aren't persisted to the database)
        for (AgendaItemBo agendaItem : agenda.getItems()) {
            if (!StringUtils.equals(agendaItem.getRule().getId(), rule.getId()) && StringUtils.equals(agendaItem.getRule().getName(), rule.getName())
                    && StringUtils.equals(agendaItem.getRule().getNamespace(), rule.getNamespace())) {
                this.putFieldError(KRMSPropertyConstants.Rule.NAME, "error.rule.duplicateName");
                return false;
            }
        }

        // check database for rules used with other agendas - the namespace might not yet be specified on new agendas.
        if (StringUtils.isNotBlank(rule.getNamespace())) {
            RuleDefinition ruleFromDatabase = getRuleBoService().getRuleByNameAndNamespace(rule.getName(), rule.getNamespace());
            try {
                if ((ruleFromDatabase != null) && !StringUtils.equals(ruleFromDatabase.getId(), rule.getId())) {
                    this.putFieldError(KRMSPropertyConstants.Rule.NAME, "error.rule.duplicateName");
                    return false;
                }
            }
            catch (IllegalArgumentException e) {
                this.putFieldError(KRMSPropertyConstants.Rule.NAME, "error.rule.invalidName");
                return false;
            }
        }
        return true;
    }

    /**
     * Check that the rule type is valid when specified.
     * @param ruleTypeId, the type id
     * @param contextId, the contextId the action needs to belong to.
     * @return true if valid, false otherwise.
     */
    private boolean validRuleType(String ruleTypeId, String contextId) {
        if (StringUtils.isBlank(ruleTypeId)) {
            return true;
        }

        if (getKrmsTypeRepositoryService().getRuleTypeByRuleTypeIdAndContextId(ruleTypeId, contextId) != null) {
            return true;
        } else {
            this.putFieldError(KRMSPropertyConstants.Rule.TYPE, "error.rule.invalidType");
            return false;
        }
    }

    private boolean validateRuleAction(AgendaEditor oldAgendaEditor, AgendaEditor newAgendaEditor) {
        boolean isValid = true;
        ActionBo newActionBo = newAgendaEditor.getAgendaItemLineRuleAction();

        isValid &= validRuleActionType(newActionBo.getTypeId(), newAgendaEditor.getAgenda().getContextId());
        if (isValid && StringUtils.isNotBlank(newActionBo.getTypeId())) {
            isValid &= validRuleActionName(newActionBo.getName());
            isValid &= validRuleActionAttributes(oldAgendaEditor, newAgendaEditor);
        }
        return isValid;
    }

    /**
     * Check that the rule action type is valid when specified.
     * @param typeId, the action type id
     * @parm contextId, the contextId the action needs to belong to.
     * @return true if valid, false otherwise.
     */
    private boolean validRuleActionType(String typeId, String contextId) {
        if (StringUtils.isBlank(typeId)) {
            return true;
        }

        if (getKrmsTypeRepositoryService().getActionTypeByActionTypeIdAndContextId(typeId, contextId) != null) {
            return true;
        } else {
            this.putFieldError(KRMSPropertyConstants.Action.TYPE, "error.action.invalidType");
            return false;
        }
    }

    /**
     * Check that a action name is specified.
     */
    private boolean validRuleActionName(String name) {
        if (StringUtils.isNotBlank(name)) {
            return true;
        } else {
            this.putFieldError(KRMSPropertyConstants.Action.NAME, "error.action.missingName");
            return false;
        }
    }

    private boolean validRuleActionAttributes(AgendaEditor oldAgendaEditor, AgendaEditor newAgendaEditor) {
        boolean isValid = true;

        String typeId = newAgendaEditor.getAgendaItemLineRuleAction().getTypeId();

        if (!StringUtils.isBlank(typeId)) {
            KrmsTypeDefinition typeDefinition = getKrmsTypeRepositoryService().getTypeById(typeId);

            if (typeDefinition == null) {
                throw new IllegalStateException("rule action typeId must match the id of a valid krms type");
            } else if (StringUtils.isBlank(typeDefinition.getServiceName())) {
                throw new IllegalStateException("rule action type definition must have a non-blank service name");
            } else {
                ActionTypeService actionTypeService = getActionTypeService(typeDefinition.getServiceName());

                if (actionTypeService == null) {
                    throw new IllegalStateException("typeDefinition must have a valid serviceName");
                } else {

                    List<RemotableAttributeError> errors;
                    if (oldAgendaEditor == null) {
                        errors = actionTypeService.validateAttributes(typeId,
                                newAgendaEditor.getCustomRuleActionAttributesMap());
                    } else {
                        errors = actionTypeService.validateAttributesAgainstExisting(typeId,
                                newAgendaEditor.getCustomRuleActionAttributesMap(), oldAgendaEditor.getCustomRuleActionAttributesMap());
                    }

                    if (!CollectionUtils.isEmpty(errors)) {
                        isValid = false;
                        for (RemotableAttributeError error : errors) {
                            for (String errorStr : error.getErrors()) {
                                this.putFieldError(
                                        KRMSPropertyConstants.AgendaEditor.CUSTOM_RULE_ACTION_ATTRIBUTES_MAP +
                                                "['" + error.getAttributeName() + "']",
                                        errorStr
                                );
                            }
                        }
                    }
                }
            }
        }
        return isValid;
    }

    public ContextBoService getContextBoService() {
        return KrmsRepositoryServiceLocator.getContextBoService();
    }

    public AgendaBoService getAgendaBoService() {
        return KrmsRepositoryServiceLocator.getAgendaBoService();
    }

    public RuleBoService getRuleBoService() {
        return KrmsRepositoryServiceLocator.getRuleBoService();
    }

    public KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
    }

    public ActionTypeService getActionTypeService(String serviceName) {
        return (ActionTypeService)GlobalResourceLoader.getService(QName.valueOf(serviceName));
    }
    public AgendaAuthorizationService getAgendaAuthorizationService() {
        return KrmsRepositoryServiceLocator.getAgendaAuthorizationService();
    }

}

