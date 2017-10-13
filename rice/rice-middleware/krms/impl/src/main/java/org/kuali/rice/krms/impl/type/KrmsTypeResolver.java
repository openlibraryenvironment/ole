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

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.framework.engine.Agenda;
import org.kuali.rice.krms.framework.type.ActionTypeService;
import org.kuali.rice.krms.framework.type.AgendaTypeService;
import org.kuali.rice.krms.framework.type.FunctionTypeService;
import org.kuali.rice.krms.framework.type.PropositionTypeService;
import org.kuali.rice.krms.framework.type.RuleTypeService;
import org.kuali.rice.krms.framework.type.TermResolverTypeService;

/**
 * The KrmsTypeResolver knows how to resolve the "type services" for the various
 * components of KRMS.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface KrmsTypeResolver {

	PropositionTypeService getPropositionTypeService(PropositionDefinition propositionDefinition);
	
	ActionTypeService getActionTypeService(ActionDefinition actionDefinition);
	
    AgendaTypeService getAgendaTypeService(AgendaDefinition actionDefinition);

    RuleTypeService getRuleTypeService(RuleDefinition ruleDefinition);

	TermResolverTypeService getTermResolverTypeService(TermResolverDefinition termResolverDefintion);
	
	FunctionTypeService getFunctionTypeService(FunctionDefinition functionDefinition);
	
}
