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

import java.util.List;

import org.kuali.rice.krms.api.engine.Engine;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;

/**
 * Interface for logical propositions that may be executed in the {@link Engine}.
 * @see PropositionResult
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Proposition {

    /**
     * Evaluates this proposition -- and in the case of {@link Proposition}s containing children,
     * those children as well -- and returns the boolean result; 
     * 
     * @param environment the {@link ExecutionEnvironment} that this {@link Proposition} is running in
     * @return the boolean result of evaluation
     */
	public PropositionResult evaluate(ExecutionEnvironment environment);
	
	/**
	 * Returns the {@link List} of child {@link Proposition}s that belong to this object.
	 * If there are no children (e.g. for simple {@link Proposition} types), this must
	 * return an empty {@link List}.
	 * 
	 * @return a {@link List} containing any child {@link Proposition}s that belong to this object.  Must never return null.
	 */
	public List<Proposition> getChildren();
	
	/**
	 * Indicates whether this {@link Proposition} can have children.
	 * @return true if this {@link Proposition} can contain child {@link Proposition}s.
	 */
	public boolean isCompound();
		
}
