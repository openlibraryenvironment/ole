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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.framework.engine.Agenda;

/**
 * {@link AgendaTypeService} provides access and validation for custom attributes on the agenda type.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface AgendaTypeService extends RemotableAttributeOwner {

    /**
     * Load an Agenda created from the given {@link AgendaDefinition}
     * @param agendaDefinition defines the {@link Agenda} to create
     * @return Agenda created from the given {@link AgendaDefinition}
     */
    public Agenda loadAgenda(AgendaDefinition agendaDefinition);

}
