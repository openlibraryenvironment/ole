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
package org.kuali.rice.krms.api.engine;

import java.util.Map;
import java.util.Set;

/**
 * <p>An {@link TermResolver} implementor is a utility class used for resolution (reification) of fact values for one or
 * more{@link Term}s.  There are a few key concepts to understand how {@link TermResolver}s function and work together.
 * </p>
 * <ul>
 * <li><b>they can require prerequisite {@link Term}s</b> (which must not have any parameters).  If they do, when the {@link TermResolutionEngine} calls
 * {@link #resolve(Map, Map)} it will pass the resolved prerequisites in the first argument.
 * <li><b>they can be chained.</b>  This means that if the {@link TermResolutionEngine} has {@link TermResolver}s (a &lt;- b) and 
 * (b &lt;- c), and you have the fact 'c', you can ask for term 'a' and the engine will chain the resolvers together
 * to resolve it correctly.</li>
 * <li><b>they can be parameterized.</b> If an TermResolver takes parameters, they must be declared via the
 * {@link #getParameterNames()} method.  All declared parameters are considered to be required.  Parameters can be set
 * on the {@link Term} (via the constructor) that you are asking to resolve. When the {@link TermResolutionEngine} calls 
 * {@link #resolve(Map, Map)}, the parameters will be passed in to the second argument.</li>
 * <li><b>Parameterized {@link TermResolver}s can not be intermediates in a resolution plan.</b>  Another way to say 
 * this is that they can only be the last resolver in a chain.  For example, say
 * the {@link TermResolutionEngine} has {@link TermResolver}s (a &lt;- b) which takes no parameters, 
 * (b &lt;- c) which takes a parameter "foo", and you have the fact 'c'.  If you ask for Term 'a', the engine will not be able to resolve
 * it because it would need to use a parameterized Term as an intermediate step in resolution, and that isn't allowed.</li>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 * @param <T> the class of the resolved object 
 */
public interface TermResolver <T> {
	
	/**
     * Gets the names of the terms that this resolver requires in order to be able to resolve its output,
     * or an empty set if it has no prereqs;
     *
	 * @return the prerequisite term names
	 */
	Set<String> getPrerequisites();
	
	/**
     * Gets the name of the term that this TermResolver resolves.
     *
	 * @return the name of the term this TermResolver resolves.
	 */
	String getOutput();
	
	/**
	 * Gets the names of any parameters that this {@link TermResolver} requires to resolve {@link Term}s.
     * This may be null if no parameters are required.  If this is non-null/non-empty, then this
     * resolver can not be used as an intermediate in a term resolution chain.
     *
     * @return the names of parameters this TermResolver requires for resolution.
	 */
	Set<String> getParameterNames();
	
	/**
	 * Gets an int representing the cost of resolving the term. 1 is cheap, Integer.MAX_INT is expensive.
     *
     * @return the cost.
	 */
	int getCost();
	
	/**
     * Resolves the output term's fact value given the resolved prerequisite terms and term parameters. The term
     * resolution engine will call this method providing the following parameters to the resolver:
     *
	 * @param resolvedPrereqs the resolved prereqs.  May be empty, but never null.
	 * @param parameters any parameters on the {@link Term} to be resolved (which must match those declared via
     * {@link #getParameterNames()}.  May be empty, but never null.
	 * @return the resolved fact value for the specified {@link Term}.  May be null.
	 * @throws {@link TermResolutionException} if something bad happens during the term resolution process.
	 */
	T resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) throws TermResolutionException;
}
