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

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.TermResolver;

/**
 * The context represents the area(s) of an organization's activity where a
 * rule applies and where the terms used to create the rule are defined and relevant.
 * An equivalent phrase often used is business domain. Rules only make sense in a
 * particular context and because they must be evaluated against the information in
 * that domain or context.
 * 
 * <p>For example, rules that are specifically authored and
 * that are meaningful in an application on a Research Proposal would be most
 * unlikely to make sense or be relevant in the context of a Student Record even
 * if the condition could be evaluated.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface Context {

    /**
     * Execute with the given {@link ExecutionEnvironment}
     * @param environment {@link ExecutionEnvironment} to execute with
     */
	void execute(ExecutionEnvironment environment);

    /**
     * Return a list of the {@link TermResolver}s
     * @return List<TermResolver<?>>
     */
	List<TermResolver<?>> getTermResolvers();
	
}
