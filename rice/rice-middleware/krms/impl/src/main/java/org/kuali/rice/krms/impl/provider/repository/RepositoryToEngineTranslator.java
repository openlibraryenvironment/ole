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
package org.kuali.rice.krms.impl.provider.repository;

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.engine.Agenda;
import org.kuali.rice.krms.framework.engine.AgendaTree;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.engine.SubAgenda;

import java.util.List;

/**
 * Can perform translations from rules defined in a repository to an executable version consumable by the rules engine.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface RepositoryToEngineTranslator {

	public Context translateContextDefinition(ContextDefinition contextDefinition);
	
	public Agenda translateAgendaDefinition(AgendaDefinition agendaDefinition);
	
	public AgendaTree translateAgendaDefinitionToAgendaTree(AgendaDefinition agendaDefinition);
	
	public AgendaTree translateAgendaTreeDefinition(AgendaTreeDefinition agendaTreeDefinition);
		
	public Rule translateRuleDefinition(RuleDefinition ruleDefinition);
	
	public SubAgenda translateAgendaTreeDefinitionToSubAgenda(AgendaTreeDefinition subAgendaDefinition);
	
	public Proposition translatePropositionDefinition(PropositionDefinition propositionDefinition);
	
	public Action translateActionDefinition(ActionDefinition actionDefinition);

    public List<Action> translateActionDefinitions(List<ActionDefinition> actions);
}
