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
package org.kuali.rice.krms.framework.engine.expression;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolutionException;

/**
 * An implementation of {@link Expression} which resolves the given {@link Term}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class TermExpression implements Expression<Object> {

	private final Term term;

    /**
     * Create a TermExpression with the given value
     * @param term {@link Term} to invoke upon.
     */
	public TermExpression(Term term) {
		this.term = term;
	}

	/**
	 * @see org.kuali.rice.krms.framework.engine.expression.Expression#invoke(org.kuali.rice.krms.api.engine.ExecutionEnvironment)
	 * @throws TermResolutionException if there is a problem resolving the {@link Term}
	 */
	@Override
	public Object invoke(ExecutionEnvironment environment) {
	    return environment.resolveTerm(term, this);
	}

}
