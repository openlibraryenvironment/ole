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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * SelectionCritera are used to to select an {@link Agenda} to execute.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class SelectionCriteria {

	private final Long effectiveExecutionTime;
	private final Map<String, String> contextQualifiers;
	private final Map<String, String> agendaQualifiers;

    /**
     * Private constructor {@see SelectionCriteria createCriteria}
     * @param effectiveDate DateTime to use for constructing the SelectionCriteria
     */
	private SelectionCriteria(DateTime effectiveDate) {
		if (effectiveDate != null) {
			this.effectiveExecutionTime = effectiveDate.getMillis();
		} else {
			this.effectiveExecutionTime = null;
		}
		
		this.contextQualifiers = new HashMap<String, String>();
		this.agendaQualifiers = new HashMap<String, String>();
	}

	/**
	 * This static factory method creates a SelectionCriteria used to select an Agenda to execute.
	 * 
	 * @param effectiveExecutionTime the time that the rule is being executed at.  If null, the time of engine execution will be used.
	 * @param contextQualifiers qualifiers used to select the context
	 * @param agendaQualifiers qualifiers used to select the agenda from the context
	 * @return the {@link SelectionCriteria}
	 */
	public static SelectionCriteria createCriteria(DateTime effectiveExecutionTime, Map<String, String> contextQualifiers, Map<String, String> agendaQualifiers) {
		SelectionCriteria criteria = new SelectionCriteria(effectiveExecutionTime);
        if (contextQualifiers != null) {
		    criteria.contextQualifiers.putAll(contextQualifiers);
        }
        if (agendaQualifiers != null) {
		    criteria.agendaQualifiers.putAll(agendaQualifiers);
        }
		return criteria;
	}

	/**
	 * This method gets the effective date/time in epoch time, suitable for 
	 * converting to a {@link java.util.Date} via {@link java.util.Date#Date(long)}
	 * @return the epoch time for effective execution, or null 
	 * (which defers to the {@link Engine} but implies that the actual time when execution begins will be used).
	 */
	public Long getEffectiveExecutionTime() {
		return effectiveExecutionTime;
	}

    /**
     * @return the map of context qualifiers.  May be empty, will never be null.
     */
	public Map<String, String> getContextQualifiers() {
		return Collections.unmodifiableMap(contextQualifiers);
	}

    /**
     * @return the map of agenda qualifiers.  May be empty, will never be null.
     */
	public Map<String, String> getAgendaQualifiers() {
		return Collections.unmodifiableMap(agendaQualifiers);
	}

}
