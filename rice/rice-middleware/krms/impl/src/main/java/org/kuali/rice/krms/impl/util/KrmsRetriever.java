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
package org.kuali.rice.krms.impl.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.framework.type.ActionTypeService;
import org.kuali.rice.krms.framework.type.AgendaTypeService;
import org.kuali.rice.krms.framework.type.RuleTypeService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.type.ActionTypeServiceBase;
import org.kuali.rice.krms.impl.type.AgendaTypeServiceBase;
import org.kuali.rice.krms.impl.type.RuleTypeServiceBase;
import org.kuali.rice.krms.impl.ui.AgendaEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * KRMS utilility class for retrieving KRMS objects for {@link org.kuali.rice.krms.impl.ui.AgendaEditorMaintainable} and
 * {@link org.kuali.rice.krms.impl.repository.AgendaInquiryHelperServiceImpl}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KrmsRetriever {

    private static final long serialVersionUID = 1L;

    /**
     * This only supports a single action within a rule.
     * @param agendaEditor
     * @return List<RemotableAttributeField>
     */
    public List<RemotableAttributeField> retrieveRuleActionCustomAttributes(AgendaEditor agendaEditor) {
        List<RemotableAttributeField> results = new ArrayList<RemotableAttributeField>();
        // if we have an rule action w/ a typeId set on it
        if (agendaEditor != null && agendaEditor.getAgendaItemLineRuleAction() != null
                &&!StringUtils.isBlank(agendaEditor.getAgendaItemLineRuleAction().getTypeId())) {
            ActionTypeService actionTypeService = getActionTypeService(agendaEditor.getAgendaItemLineRuleAction().getTypeId());
            results.addAll(actionTypeService.getAttributeFields(agendaEditor.getAgendaItemLineRuleAction().getTypeId()));
        }

        return results;
    }

    /**
     * Get the AgendaEditor out of the MaintenanceDocumentForm's newMaintainableObject
     * @param model the MaintenanceDocumentForm
     * @return the AgendaEditor
     */
    private AgendaEditor getAgendaEditor(Object model) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm)model;
        return (AgendaEditor)maintenanceForm.getDocument().getNewMaintainableObject().getDataObject();
    }

    private ActionTypeService getActionTypeService(String krmsTypeId) {
        //
        // Get the ActionTypeService by hook or by crook
        //

        KrmsTypeDefinition krmsType = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().getTypeById(krmsTypeId);

        ActionTypeService actionTypeService = null;

        if (!StringUtils.isBlank(krmsTypeId)) {
            String serviceName = krmsType.getServiceName();

            if (!StringUtils.isBlank(serviceName)) {
                actionTypeService = KrmsRepositoryServiceLocator.getService(serviceName);
            }
        }
        if (actionTypeService == null) {
            actionTypeService = ActionTypeServiceBase.defaultActionTypeService;
        }

        //        if (actionTypeService == null) { actionTypeService = AgendaTypeServiceBase.defaultAgendaTypeService; }

        return actionTypeService;
    }

    /**
     *
     * @param agendaEditor
     * @return List<RemotableAttributeField>
     */
    public List<RemotableAttributeField> retrieveAgendaCustomAttributes(AgendaEditor agendaEditor) {
        List<RemotableAttributeField> results = new ArrayList<RemotableAttributeField>();
        // if we have an agenda w/ a typeId set on it
        if (agendaEditor != null && agendaEditor.getAgenda() != null
                && !StringUtils.isBlank(agendaEditor.getAgenda().getTypeId())) {

            String krmsTypeId = agendaEditor.getAgenda().getTypeId();

            AgendaTypeService agendaTypeService = getAgendaTypeService(krmsTypeId);
            results.addAll(agendaTypeService.getAttributeFields(krmsTypeId));
        }

        return results;
    }

    private AgendaTypeService getAgendaTypeService(String krmsTypeId) {
        //
        // Get the AgendaTypeService by hook or by crook
        //

        KrmsTypeDefinition krmsType =
                KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().
                        getTypeById(krmsTypeId);

        AgendaTypeService agendaTypeService = null;

        if (!StringUtils.isBlank(krmsTypeId)) {
            String serviceName = krmsType.getServiceName();

            if (!StringUtils.isBlank(serviceName)) {
                agendaTypeService = KrmsRepositoryServiceLocator.getService(serviceName);
            }
        }

        if (agendaTypeService == null) { agendaTypeService = AgendaTypeServiceBase.defaultAgendaTypeService; }

        return agendaTypeService;
    }

    public List<RemotableAttributeField> retrieveRuleCustomAttributes(AgendaEditor agendaEditor) {
        List<RemotableAttributeField> results = new ArrayList<RemotableAttributeField>();
        // if we have an rule w/ a typeId set on it
        if (agendaEditor != null && agendaEditor.getAgendaItemLine() != null && agendaEditor.getAgendaItemLine().getRule() != null
                && !StringUtils.isBlank(agendaEditor.getAgendaItemLine().getRule().getTypeId())) {

            String krmsTypeId = agendaEditor.getAgendaItemLine().getRule().getTypeId();

            RuleTypeService ruleTypeService = getRuleTypeService(krmsTypeId);
            results.addAll(ruleTypeService.getAttributeFields(krmsTypeId));
        }

        return results;
    }


    private RuleTypeService getRuleTypeService(String krmsTypeId) {
        RuleTypeService ruleTypeService = null;
        String serviceName = getRuleTypeServiceName(krmsTypeId);

        if (!StringUtils.isBlank(serviceName)) {
            ruleTypeService = KrmsRepositoryServiceLocator.getService(serviceName);
        }
        if (ruleTypeService == null) {
            ruleTypeService = RuleTypeServiceBase.defaultRuleTypeService;
        }
        return ruleTypeService;
    }

    private String getRuleTypeServiceName(String krmsTypeId) {
        String serviceName = null;
        KrmsTypeDefinition krmsType =
                KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().
                        getTypeById(krmsTypeId);

        if (!StringUtils.isBlank(krmsTypeId)) {
            serviceName = krmsType.getServiceName();
        }
        return serviceName;
    }
}
