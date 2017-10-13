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
package org.kuali.rice.krms.api.repository.agenda;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.krms.api.repository.rule.RuleDefinitionContract;

/**
 * Agenda Item Definition Contract
 *
 * @see AgendaItemDefinition
 */
public interface AgendaItemDefinitionContract extends Identifiable, Versioned {

	/**
	 * Returns the agenda id to which the agenda item belongs.
	 *
	 * @return id for the agenda associated with the agenda item
	 */
	public String getAgendaId();

    /**
     * Returns the rule id associated with the agenda item.
     *
     * @return ID of the Rule associated with the agenda item
     */
	public String getRuleId();

    /**
     * This is ID of the SubAgenda associated with this AgendaItemDefinition.
     *
     * Each AgendaItemDefinition has either a Rule or a SubAgenda associated with it, but not both.
     *
     * @return ID of the SubAgenda associated with the AgendaItemDefinition
     */
	public String getSubAgendaId();

    /**
     * This is ID of the next AgendaItemDefinition to be executed if the Rule associated
     * AgendaItemDefinition evaluates to true.
     *
     * @return ID of the next AgendaItemDefinition
     */	
	public String getWhenTrueId();
	
    /**
     * This is ID of the next AgendaItemDefinition to be executed if the Rule associated
     * AgendaItemDefinition evaluates to false.
     *
     * @return ID of the next AgendaItemDefinition
     */	
	public String getWhenFalseId();
	
    /**
     * This is ID of the next AgendaItemDefinition to be executed after following any
     * defined true or false actions.
     *
     * @return ID of the next AgendaItemDefinition
     */	
	public String getAlwaysId();

	/**
	 * This method returns the Rule associated with this AgendaItemDefinition.
	 * 
	 * @return an immutable representation of the Rule
	 */
	public RuleDefinitionContract getRule();

	/**
	 * 
	 * This method returns the SubAgenda associated with this AgendaItemDefinition.
	 * 
	 * @return an immutable representation of the SubAgenda
	 */
	public AgendaDefinitionContract getSubAgenda();
	
    /**
     * This method returns the next AgendaItemDefinition to be executed if the
     * Rule associated with this AgendaItemDefinition evaluates to true.
     *
     * @return an immutable representation of the next AgendaItemDefinition
     */	
	public AgendaItemDefinitionContract getWhenTrue();
	
    /**
     * This method returns the next AgendaItemDefinition to be executed if the
     * Rule associated with this AgendaItemDefinition evaluates to false.
     *
     * @return an immutable representation of the next AgendaItemDefinition
     */	
	public AgendaItemDefinitionContract getWhenFalse();

	/**
     * This is ID of the next AgendaItemDefinition to be executed after following any
     * defined true or false actions.
     *
     * @return an immutable representation of the next AgendaItemDefinition
     */	
	public AgendaItemDefinitionContract getAlways();

}
