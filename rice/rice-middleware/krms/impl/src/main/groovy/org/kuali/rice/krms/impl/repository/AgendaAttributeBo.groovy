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
package org.kuali.rice.krms.impl.repository

/**
 * This class represents an AgendaAttribute business object.
 * Agenda attributes provide a way to attach custom data to an agenda based on the agenda's type.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AgendaAttributeBo extends BaseAttributeBo {

	// reference to the agenda associated with this attribute
	def String agendaId

} 