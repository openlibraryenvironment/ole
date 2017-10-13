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
package org.kuali.rice.krms.impl.type;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.EngineResourceUnavailableException;
import org.kuali.rice.krms.api.repository.RepositoryDataException;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.framework.type.ActionTypeService;
import org.kuali.rice.krms.framework.type.AgendaTypeService;
import org.kuali.rice.krms.framework.type.FunctionTypeService;
import org.kuali.rice.krms.framework.type.PropositionTypeService;
import org.kuali.rice.krms.framework.type.RuleTypeService;
import org.kuali.rice.krms.framework.type.TermResolverTypeService;

/**
 * An implementation of {@link KrmsTypeResolver} which knows how to load the
 * various type services in KRMS.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KrmsTypeResolverImpl implements KrmsTypeResolver {

	private KrmsTypeRepositoryService typeRepositoryService;
	private PropositionTypeService defaultCompoundPropositionTypeService;
	private PropositionTypeService defaultSimplePropositionTypeService;
	
	@Override
	public PropositionTypeService getPropositionTypeService(PropositionDefinition propositionDefinition) {
		if (propositionDefinition == null) {
			throw new IllegalArgumentException("propositionDefinition was null");
		}
		if (propositionDefinition.getTypeId() == null) {
			PropositionType propositionType = PropositionType.fromCode(propositionDefinition.getPropositionTypeCode());
			if (PropositionType.COMPOUND == propositionType) {
				return defaultCompoundPropositionTypeService;
			} else if (PropositionType.SIMPLE == propositionType) {
				return defaultSimplePropositionTypeService;
			}
			throw new RepositoryDataException("Proposition does not have a typeId defined and does not define a valid proposition type code.  Proposition id is: " + propositionDefinition.getId());
		}
		KrmsTypeDefinition typeDefinition = getTypeDefinition(propositionDefinition.getTypeId());
		return resolveTypeService(typeDefinition, PropositionTypeService.class);
	}

	@Override
	public ActionTypeService getActionTypeService(ActionDefinition actionDefinition) {
		if (actionDefinition == null) {
			throw new IllegalArgumentException("actionDefinition was null");
		}
		KrmsTypeDefinition typeDefinition = getTypeDefinition(actionDefinition.getTypeId());
		return resolveTypeService(typeDefinition, ActionTypeService.class);
	}

    @Override
    public AgendaTypeService getAgendaTypeService(AgendaDefinition agendaDefinition) {
        if (agendaDefinition == null) {
            throw new IllegalArgumentException("agendaDefinition was null");
        }
        KrmsTypeDefinition typeDefinition = getTypeDefinition(agendaDefinition.getTypeId());
        return resolveTypeService(typeDefinition, AgendaTypeService.class);
    }

    @Override
	public RuleTypeService getRuleTypeService(RuleDefinition ruleDefinition) {
        if (ruleDefinition == null) {
            throw new IllegalArgumentException("ruleDefinition was null");
        }
        KrmsTypeDefinition typeDefinition = getTypeDefinition(ruleDefinition.getTypeId());

        if (typeDefinition == null) { return RuleTypeServiceBase.defaultRuleTypeService; }

        return resolveTypeService(typeDefinition, RuleTypeService.class);
    }

	@Override
	public TermResolverTypeService getTermResolverTypeService(TermResolverDefinition termResolverDefinition) {
		if (termResolverDefinition == null) {
			throw new IllegalArgumentException("termResolverDefinition was null");
		}
		KrmsTypeDefinition typeDefinition = getTypeDefinition(termResolverDefinition.getTypeId());
		return resolveTypeService(typeDefinition, TermResolverTypeService.class);
	}
	
	@Override
	public FunctionTypeService getFunctionTypeService(FunctionDefinition functionDefinition) {
		if (functionDefinition == null) {
			throw new IllegalArgumentException("functionDefinition was null");
		}
		KrmsTypeDefinition typeDefinition = getTypeDefinition(functionDefinition.getTypeId());
		return resolveTypeService(typeDefinition, FunctionTypeService.class);
	}
	
	protected KrmsTypeDefinition getTypeDefinition(String typeId) {
		if (StringUtils.isBlank(typeId)) {
			return null;
		}
		KrmsTypeDefinition typeDefinition = typeRepositoryService.getTypeById(typeId);
		if (typeDefinition == null) {
			throw new RepositoryDataException("Failed to locate a type definition for typeId: " + typeId);
		}
		return typeDefinition;
	}
	
	protected <T> T resolveTypeService(KrmsTypeDefinition typeDefinition, Class<T> typeServiceClass) {
		QName serviceName = QName.valueOf(typeDefinition.getServiceName());
		Object service = GlobalResourceLoader.getService(serviceName);
		if (service == null) {
			throw new EngineResourceUnavailableException("Failed to locate the " + typeServiceClass.getSimpleName() + 
					" with name: " + serviceName);
		}
		if (!typeServiceClass.isAssignableFrom(service.getClass())) {
			throw new EngineResourceUnavailableException("The service with name '" + serviceName + "' defined on typeId '" + typeDefinition.getId() + 
					"' was not of type " + typeServiceClass.getSimpleName() + ": " + service);
		}
		return typeServiceClass.cast(service);
	}
	
	public void setTypeRepositoryService(KrmsTypeRepositoryService typeRepositoryService) {
		this.typeRepositoryService = typeRepositoryService;
	}

	public void setDefaultCompoundPropositionTypeService(PropositionTypeService defaultCompoundPropositionTypeService) {
		this.defaultCompoundPropositionTypeService = defaultCompoundPropositionTypeService;
	}

	public void setDefaultSimplePropositionTypeService(PropositionTypeService defaultSimplePropositionTypeService) {
		this.defaultSimplePropositionTypeService = defaultSimplePropositionTypeService;
	}
	
}
