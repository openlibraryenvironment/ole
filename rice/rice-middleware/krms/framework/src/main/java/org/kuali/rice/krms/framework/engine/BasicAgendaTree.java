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

import java.util.Arrays;
import java.util.List;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;

/**
 * An implementation of {@link AgendaTree} that executes a {@link ExecutionEnvironment} over its list of {@link AgendaTreeEntry}s.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class BasicAgendaTree implements AgendaTree {
	
	private final List<AgendaTreeEntry> entries;

    /**
     * Create a BasicAgendaTree with the given {@link AgendaTreeEntry}s
     * @param entries - {@link AgendaTreeEntry}s to create a BasicAgendaTree with
     */
	public BasicAgendaTree(AgendaTreeEntry... entries) {
		this.entries = Arrays.asList(entries);
	}

    /**
     * Create a BasicAgendaTree with the given {@link AgendaTreeEntry}s
     * @param entries - {@link AgendaTreeEntry}s to create a BasicAgendaTree with
     * @throws IllegalArgumentException if the entries list is null
     */
	public BasicAgendaTree(List<AgendaTreeEntry> entries) {
		if (entries == null) {
			throw new IllegalArgumentException("entries list was null");
		}
		this.entries = entries;		
	}
	
    @Override
	public void execute(ExecutionEnvironment environment) {
		for (AgendaTreeEntry entry : entries) {
			entry.execute(environment);
		}
	}
	
}
