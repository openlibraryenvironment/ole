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

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.framework.engine.CompoundProposition;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.type.PropositionTypeService;

/**
 * An implementation of {@link PropositionTypeService} which loads a {@link CompoundProposition}
 * from the given {@link PropositionDefinition}.  A compound proposition contains one
 * or more propositions which are evaluated in conjunction with a logical operator
 * such as "AND" or "OR".
 * 
 * <p>The proposition given to the {@link #loadProposition(PropositionDefinition)}
 * method must be of type {@link PropositionType#COMPOUND}.
 * 
 * <p>The translation from a {@link PropositionDefinition} to a {@link Proposition}
 * is performed by the given {@link RepositoryToEngineTranslator}.  This must be
 * set on this class before it is used.
 * 
 * <p>This class is thread-safe and is designed to be wired as a singleton bean in
 * Spring.
 * 
 * @see CompoundProposition
 * @see RepositoryToEngineTranslator
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CompoundPropositionTypeService implements PropositionTypeService {

	private RepositoryToEngineTranslator translator;
	
	@Override
	public Proposition loadProposition(PropositionDefinition propositionDefinition) {
		if (translator == null) {
			throw new IllegalStateException("Service not configured properly, no translator available.");
		}
		if (propositionDefinition == null) {
			throw new IllegalArgumentException("propositionDefinition was null");
		}
		if (PropositionType.COMPOUND != PropositionType.fromCode(propositionDefinition.getPropositionTypeCode())) {
			throw new IllegalArgumentException("Given proposition definition was not compound, type code was: " + propositionDefinition.getPropositionTypeCode());
		}
		List<Proposition> propositions = new ArrayList<Proposition>();
		for (PropositionDefinition subProp : propositionDefinition.getCompoundComponents()) {
			propositions.add(translator.translatePropositionDefinition(subProp));
		}
		LogicalOperator operator = LogicalOperator.fromCode(propositionDefinition.getCompoundOpCode());
		return new CompoundProposition(operator, propositions);
	}
	
	/**
	 * Sets the translator on this service to the given translator.
	 * 
	 * @param translator the translator to set on this service
	 */
	public void setTranslator(RepositoryToEngineTranslator translator) {
		this.translator = translator;
	}

}
