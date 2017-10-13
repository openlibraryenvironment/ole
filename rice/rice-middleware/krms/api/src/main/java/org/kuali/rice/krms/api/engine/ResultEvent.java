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

import org.joda.time.DateTime;

/**
 * Interface for defining ResultEvents
 * @see EngineResults
 * @see ResultListener
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ResultEvent {
    /**
     * RULE_EVALUATED
     */
    public static final String RULE_EVALUATED = "Rule Evaluated";

    /**
     * PROPOSITION_EVALUATED
     */
    public static final String PROPOSITION_EVALUATED = "Proposition Evaluated";

    /**
     * ACTION_EXECUTED
     */
    public static final String ACTION_EXECUTED = "Action Executed";

    /**
     * TIMING_EVENT
     */
    public static final String TIMING_EVENT = "Timing Event";

    /**
     * Returns the {@link ExecutionEnvironment} of the ResultEvent.
     * @return {@link ExecutionEnvironment} of the ResultEvent.
     */
	public ExecutionEnvironment getEnvironment();

    /**
     * Returns the type of the ResultEvent.
     * @return String of the type of ResultEvent.
     */
	public String getType();

    /**
     * Returns the source of the ResultEvent.
     * @return source as an Object of the ResultEvent.
     */
	public Object getSource();

    /**
     * Returns the org.joda.time.DateTime timestamp of the ResultEvent.
     * @return org.joda.time.DateTime timestamp of the ResultEvent.
     */
	public DateTime getTimestamp();

    /**
     * Returns the Boolean result of the ResultEvent.
     * @return Boolean result of the ResultEvent.
     */
	public Boolean getResult();

    /**
     * Returns the description of the ResultEvent as a String
     * @return description of the ResultEvent as a String
     */
	public String getDescription();

    /**
     * Returns the result details of the ResultEvent as a Map<?,?>
     * @return result details of the ResultEvent as a Map<?,?>
     */
	public Map<?,?> getResultDetails();
}
