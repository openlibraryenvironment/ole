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
package org.kuali.rice.krad.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.document.TransactionalDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.rules.TransactionalDocumentRuleBase;
import org.kuali.rice.krad.rules.rule.BusinessRule;
import org.kuali.rice.krad.rules.rule.event.AddAdHocRoutePersonEvent;
import org.kuali.rice.krad.rules.rule.event.AddAdHocRouteWorkgroupEvent;
import org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.MessageMap;

/**
 * Represents a rule evaluator for Kuali. This class is to be used for evaluating business rule checks. The class
 * defines one method right now - applyRules() which takes in a Document and a DocumentEvent and does the proper
 * business rule checks based on the context of the event and the document type
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KualiRuleServiceImpl implements KualiRuleService {
    private static final Logger LOG = Logger.getLogger(KualiRuleServiceImpl.class);

    private DocumentDictionaryService documentDictionaryService;
    private DataDictionaryService dataDictionaryService;

    /**
     * @see org.kuali.rice.krad.service.KualiRuleService#applyRules(org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent)
     */
    public boolean applyRules(KualiDocumentEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("invalid (null) event");
        }

        event.validate();
        if (LOG.isDebugEnabled()) {
            LOG.debug("calling applyRules for event " + event);
        }

        BusinessRule rule = getBusinessRulesInstance(event.getDocument(), event.getRuleInterfaceClass());

        boolean success = true;
        if (rule != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("processing " + event.getName() + " with rule " + rule.getClass().getName());
            }
            increaseErrorPath(event.getErrorPathPrefix());

            // get any child events and apply rules
            List<KualiDocumentEvent> events = event.generateEvents();
            for (KualiDocumentEvent generatedEvent : events) {
                success &= applyRules(generatedEvent);
            }

            // now call the event rule method
            success &= event.invokeRuleMethod(rule);

            decreaseErrorPath(event.getErrorPathPrefix());

            // report failures
            if (!success) {
                if (LOG.isDebugEnabled()) { // NO, this is not a type - only log if in debug mode - this is not an error in production
                    LOG.debug(event.getName() + " businessRule " + rule.getClass().getName() + " failed");
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("processed " + event.getName() + " for rule " + rule.getClass().getName());
                }
            }

        }
        return success;
    }

    /**
     * Builds a list containing AddAdHocRoutePersonEvents since the validation done for an AdHocRouteRecipient is the
     * same for all events
     *
     * @see org.kuali.rice.krad.service.KualiRuleService#generateAdHocRoutePersonEvents(org.kuali.rice.krad.document.Document)
     */
    public List<AddAdHocRoutePersonEvent> generateAdHocRoutePersonEvents(Document document) {
        List<AdHocRoutePerson> adHocRoutePersons = document.getAdHocRoutePersons();

        List<AddAdHocRoutePersonEvent> events = new ArrayList<AddAdHocRoutePersonEvent>();

        for (int i = 0; i < adHocRoutePersons.size(); i++) {
            events.add(new AddAdHocRoutePersonEvent(
                    KRADConstants.EXISTING_AD_HOC_ROUTE_PERSON_PROPERTY_NAME + "[" + i + "]", document,
                    adHocRoutePersons.get(i)));
        }

        return events;
    }

    /**
     * Builds a list containing AddAdHocRoutePersonEvents since the validation done for an AdHocRouteRecipient is the
     * same for all events
     *
     * @see org.kuali.rice.krad.service.KualiRuleService#generateAdHocRouteWorkgroupEvents(org.kuali.rice.krad.document.Document)
     */
    public List<AddAdHocRouteWorkgroupEvent> generateAdHocRouteWorkgroupEvents(Document document) {
        List<AdHocRouteWorkgroup> adHocRouteWorkgroups = document.getAdHocRouteWorkgroups();

        List<AddAdHocRouteWorkgroupEvent> events = new ArrayList<AddAdHocRouteWorkgroupEvent>();

        for (int i = 0; i < adHocRouteWorkgroups.size(); i++) {
            events.add(new AddAdHocRouteWorkgroupEvent(
                    KRADConstants.EXISTING_AD_HOC_ROUTE_WORKGROUP_PROPERTY_NAME + "[" + i + "]", document,
                    adHocRouteWorkgroups.get(i)));
        }

        return events;
    }

    /**
     * @param document
     * @param ruleInterface
     * @return instance of the businessRulesClass for the given document's type, if that businessRulesClass implements
     *         the given ruleInterface
     */
    public BusinessRule getBusinessRulesInstance(Document document, Class<? extends BusinessRule> ruleInterface) {
        // get the businessRulesClass
        Class<? extends BusinessRule> businessRulesClass = null;
        if (document instanceof TransactionalDocument) {
            TransactionalDocument transactionalDocument = (TransactionalDocument) document;

            businessRulesClass = getDocumentDictionaryService().getBusinessRulesClass(transactionalDocument);
            if (businessRulesClass == null) {
                return new TransactionalDocumentRuleBase(); // default to a generic rule that will enforce Required fields
            }
        } else if (document instanceof MaintenanceDocument) {
            MaintenanceDocument maintenanceDocument = (MaintenanceDocument) document;

            businessRulesClass = getDocumentDictionaryService().getBusinessRulesClass(maintenanceDocument);
            if (businessRulesClass == null) {
                return new MaintenanceDocumentRuleBase(); // default to a generic rule that will enforce Required fields
            }
        } else {
            LOG.error("unable to get businessRulesClass for unknown document type '"
                    + document.getClass().getName()
                    + "'");
        }

        // instantiate and return it if it implements the given ruleInterface
        BusinessRule rule = null;
        if (businessRulesClass != null) {
            try {
                if (ruleInterface.isAssignableFrom(businessRulesClass)) {
                    rule = businessRulesClass.newInstance();
                }
            } catch (IllegalAccessException e) {
                throw new RiceRuntimeException("error processing business rules", e);
            } catch (InstantiationException e) {
                throw new RiceRuntimeException("error processing business rules", e);
            }
        }

        return rule;
    }

    /**
     * Increases the registered error path, so that field highlighting can occur on the appropriate object
     * attribute
     *
     * @param errorPathPrefix
     */
    private void increaseErrorPath(String errorPathPrefix) {
        MessageMap errorMap = GlobalVariables.getMessageMap();

        if (!StringUtils.isBlank(errorPathPrefix)) {
            errorMap.addToErrorPath(errorPathPrefix);
        }
    }

    /**
     * Decreases the registered error path, so that field highlighting can occur on the appropriate object
     * attribute
     *
     * @param errorPathPrefix
     */
    private void decreaseErrorPath(String errorPathPrefix) {
        MessageMap errorMap = GlobalVariables.getMessageMap();

        if (!StringUtils.isBlank(errorPathPrefix)) {
            errorMap.removeFromErrorPath(errorPathPrefix);
        }
    }

    public DocumentDictionaryService getDocumentDictionaryService() {
        if (documentDictionaryService == null) {
            this.documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return documentDictionaryService;
    }

    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }

    public DataDictionaryService getDataDictionaryService() {
        return dataDictionaryService;
    }

    public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
        this.dataDictionaryService = dataDictionaryService;
    }
}
