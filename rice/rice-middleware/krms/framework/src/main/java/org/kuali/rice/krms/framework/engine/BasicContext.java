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
package org.kuali.rice.krms.framework.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.TermResolver;

/**
 * An implementation of {@link Context}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class BasicContext implements Context {
	
	private final List<Agenda> agendas;
	private final List<TermResolver<?>> termResolvers;

    /**
     * Create a BasicContext with the given parameters
     * @param agendas List<{@link}Agenda}> to set the agendas to
     * @param termResolvers List<{@link TermResolver}<?>> to set the termResolvers to
     */
	public BasicContext(List<Agenda> agendas, List<TermResolver<?>> termResolvers) {
		this.agendas = agendas;
		this.termResolvers = termResolvers;
	}
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		if (termResolvers != null) for (TermResolver<?> termResolver : termResolvers) {
			environment.addTermResolver(termResolver);
		}
		List<Agenda> matchingAgendas = findMatchingAgendas(environment);
		for (Agenda matchingAgenda : matchingAgendas) {
			matchingAgenda.execute(environment);
		}
	}

    /**
     * Return {@link Agenda}s that appliesTo the given {@link ExecutionEnvironment}
     * @param environment {@link ExecutionEnvironment} to apply to
     * @return List<{@link Agenda}> that match
     */
	private List<Agenda> findMatchingAgendas(ExecutionEnvironment environment) {
		List<Agenda> matchingAgendas = new ArrayList<Agenda>();
		for (Agenda agenda : agendas) {
			if (agenda.appliesTo(environment)) {
				matchingAgendas.add(agenda);
			}
		}
		return matchingAgendas;
	}

    @Override
	public List<TermResolver<?>> getTermResolvers() {
		return Collections.unmodifiableList(termResolvers);
	}

}
