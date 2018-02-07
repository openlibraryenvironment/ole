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
package org.kuali.rice.krms.impl.repository;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.impl.authorization.AgendaAuthorizationService;
import org.kuali.rice.krms.impl.provider.repository.RepositoryToEngineTranslator;

import javax.xml.namespace.QName;

/**
 * This class keeps track of the KRMS Repository Services
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class KrmsRepositoryServiceLocator {

    private KrmsRepositoryServiceLocator() {
        // private constructor since this is class is all static utility methods
    }

	private static final Logger LOG = Logger.getLogger(KrmsRepositoryServiceLocator.class);

    public static final String KRMS_ATTRIBUTE_DEFINITION_SERVICE = "krmsAttributeDefinitionService";
    public static final String KRMS_TYPE_REPOSITORY_SERVICE = "krmsTypeRepositoryService";
    public static final String CRITERIA_LOOKUP_SERVICE = "criteriaLookupService";
    public static final String KRMS_CONTEXT_BO_SERVICE = "contextBoService";
    public static final String KRMS_AGENDA_BO_SERVICE = "agendaBoService";
    public static final String KRMS_FUNCTION_BO_SERVICE = "functionBoService";
    public static final String KRMS_TERM_BO_SERVICE = "termBoService";
    public static final String KRMS_RULE_BO_SERVICE = "ruleBoService";
    public static final String KRMS_AGENDA_AUTHORIZATION_SERVICE = "agendaAuthorizationService";
    public static final String KRMS_REPOSITORY_TO_ENGINE_TRANSLATOR = "repositoryToEngineTranslator";
    public static final String TYPE_TYPE_RELATION_BO_SERVICE = "typeTypeRelationBoService";

	private static KrmsAttributeDefinitionService krmsAttributeDefinitionService;
    private static ContextBoService contextBoService;
    private static TermBoService termBoService;
    private static AgendaBoService agendaBoService;
    private static FunctionBoService functionBoService;
    private static RuleBoService ruleBoService;
    private static AgendaAuthorizationService agendaAuthorizationService;
    private static KrmsTypeRepositoryService krmsTypeRepositoryService;
    private static RepositoryToEngineTranslator krmsRepositoryToEngineTranslator;
    private static TypeTypeRelationBoService typeTypeRelationBoService;

    public static <T extends Object> T getService(String serviceName) {
		return KrmsRepositoryServiceLocator.<T>getBean(serviceName);
	}

	public static <T extends Object> T getBean(String serviceName) {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug("Fetching service " + serviceName);
		}
		return GlobalResourceLoader.<T>getService(QName.valueOf(serviceName));
	}

    public static KrmsAttributeDefinitionService getKrmsAttributeDefinitionService() {
		if ( krmsAttributeDefinitionService == null ) {
			krmsAttributeDefinitionService = getService(KRMS_ATTRIBUTE_DEFINITION_SERVICE);
		}
		return krmsAttributeDefinitionService;
    }

    public static CriteriaLookupService getCriteriaLookupService() {
        return getService(CRITERIA_LOOKUP_SERVICE);
    }

	public static void setKrmsAttributeDefinitionService(final KrmsAttributeDefinitionService service) {
		krmsAttributeDefinitionService = service;
	}

    public static ContextBoService getContextBoService() {
        if (contextBoService == null) {
            contextBoService = getService(KRMS_CONTEXT_BO_SERVICE);
        }
        return contextBoService;
    }

    public static TermBoService getTermBoService() {
        if (termBoService == null) {
            termBoService = getService(KRMS_TERM_BO_SERVICE);
        }
        return termBoService;
    }

    public static AgendaBoService getAgendaBoService() {
        if (agendaBoService == null) {
            agendaBoService = getService(KRMS_AGENDA_BO_SERVICE);
        }
        return agendaBoService;
    }

    public static FunctionBoService getFunctionBoService() {
        if (functionBoService == null) {
            functionBoService = getService(KRMS_FUNCTION_BO_SERVICE);
        }
        return functionBoService;
    }

    public static RuleBoService getRuleBoService() {
        if (ruleBoService == null) {
            ruleBoService = getService(KRMS_RULE_BO_SERVICE);
        }
        return ruleBoService;
    }

    public static AgendaAuthorizationService getAgendaAuthorizationService() {
        if (agendaAuthorizationService == null) {
            agendaAuthorizationService = getService(KRMS_AGENDA_AUTHORIZATION_SERVICE);
        }
        return agendaAuthorizationService;
    }

    public static KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        if (krmsTypeRepositoryService == null) {
            krmsTypeRepositoryService = getService(KRMS_TYPE_REPOSITORY_SERVICE);
        }
        return krmsTypeRepositoryService;
    }

    public static RepositoryToEngineTranslator getKrmsRepositoryToEngineTranslator() {
        if (krmsRepositoryToEngineTranslator == null) {
            krmsRepositoryToEngineTranslator = getService(KRMS_REPOSITORY_TO_ENGINE_TRANSLATOR);
        }
        return krmsRepositoryToEngineTranslator;
    }

    public static TypeTypeRelationBoService getTypeTypeRelationBoService() {
        if (typeTypeRelationBoService == null) {
            typeTypeRelationBoService = getService(TYPE_TYPE_RELATION_BO_SERVICE);
        }
        return typeTypeRelationBoService;
    }
}
