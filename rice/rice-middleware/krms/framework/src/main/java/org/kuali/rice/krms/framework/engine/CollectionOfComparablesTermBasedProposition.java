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

import java.util.Collection;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.krms.framework.engine.result.BasicResult;

public class CollectionOfComparablesTermBasedProposition<T> extends ComparableTermBasedProposition<T> {
	private static final ResultLogger LOG = ResultLogger.getInstance();

	private CollectionOperator collectionOper;
	private Term term;

	public CollectionOfComparablesTermBasedProposition(CollectionOperator collectionOper, ComparisonOperator compareOper, Term term, T expectedValue) {
		super(compareOper, term, expectedValue);
		this.term = term;
		this.collectionOper = collectionOper;
	}

	/**
	 * @see org.kuali.rice.krms.framework.engine.ComparableTermBasedProposition#evaluate(org.kuali.rice.krms.api.engine.ExecutionEnvironment)
	 * @throws TermResolutionException if there is a problem resolving a {@link Term}
	 */
	@Override
	public PropositionResult evaluate(ExecutionEnvironment environment) {
		boolean collatedResult = collectionOper.getInitialCollatedResult();

		Collection<? extends Comparable<T>> termValue;

		termValue = environment.resolveTerm(term, this);

		if (termValue != null) {
			for (Comparable<T> item : termValue) {
				collatedResult = collectionOper.reduce(compare(item), collatedResult);
				if (collectionOper.shortCircuit(collatedResult)) break;
			}
		}

		// TODO: log this appropriately
		if (LOG.isEnabled(environment)) {
			LOG.logResult(new BasicResult(ResultEvent.PROPOSITION_EVALUATED, this, environment, collatedResult));
		}

		return new PropositionResult(collatedResult);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(collectionOper.toString());
		sb.append(" "+super.toString());
		return sb.toString();
	}
}
