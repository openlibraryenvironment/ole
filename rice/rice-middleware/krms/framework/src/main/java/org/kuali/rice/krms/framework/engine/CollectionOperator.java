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

/**
 * <p>Enumeration for simple collection operators used by {@link CollectionOfComparablesTermBasedProposition}.  The
 * operators encapsulate logic for how to collate results and when to short circuit as a collection is being
 * processed.  Correct usage is best summarized by this code block:</p>
 * <pre>
 * for (Comparable<T> item : comparableItems) {
 *     collatedResult = collectionOper.reduce(compare(item, compareValue), collatedResult);
 *     if (collectionOper.shortCircuit(collatedResult)) break;
 * }
 * </pre>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public enum CollectionOperator {

	ONE_OR_MORE(false) {
		@Override
		public boolean reduce(boolean elementResult, boolean collatedResult) {
			return elementResult || collatedResult;
		}
		
		@Override
		public boolean shortCircuit(boolean collatedResult) {
			return collatedResult;
		}
	},
	
	ALL(true) {
		@Override
		public boolean reduce(boolean elementResult, boolean collatedResult) {
			return elementResult && collatedResult;
		}

		@Override
		public boolean shortCircuit(boolean collatedResult) {
			return !collatedResult;
		}
	},
	
	NONE(true) {
		@Override
		public boolean reduce(boolean elementResult, boolean collatedResult) {
			return !elementResult && collatedResult;
		}

		@Override
		public boolean shortCircuit(boolean collatedResult) {
			return !collatedResult;
		}
	};
	
	private final boolean initialCollationResult;
	
	private CollectionOperator(boolean initialCollationResult) {
		this.initialCollationResult = initialCollationResult;
	}
	
	/**
	 * This method takes the collated result thus far and the result for the next element,
	 * and produces the next collated result.
	 * 
	 * @return the new collated result
	 */
	public abstract boolean reduce(boolean elementResult, boolean collatedResult);
	
	/**
	 * This method lets the engine know if it can short circuit its iteration through the list based on the 
	 * collated result.  The condition when short circuiting can be done varies with the operator.
	 * 
	 * @param collatedResult
	 * @return true if short circuiting can be done to optimize processing
	 */
	public abstract boolean shortCircuit(boolean collatedResult);
	
	/**
	 * when the result for the first item in the collection is calculated, there isn't yet a collated result 
	 * to use in the {@link #reduce(boolean, boolean)} method.  Different operators require different
	 * initial values to function correctly, so this property holds the correct initial collated value for the 
	 * given operator instance.
	 */
	public boolean getInitialCollatedResult() {
		return initialCollationResult;
	}
	
}
