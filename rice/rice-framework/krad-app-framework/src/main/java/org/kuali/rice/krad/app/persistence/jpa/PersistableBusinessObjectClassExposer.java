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
package org.kuali.rice.krad.app.persistence.jpa;

import java.util.Set;

/**
 * Contract for classes which plan to expose class names to the RicePersistenceUnitPostProcessor, for
 * dynamic loading 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface PersistableBusinessObjectClassExposer {
	/**
	 * Exposes a Set of class names for PersistableBusinessObjects, mapped as JPA entities, which
	 * should be managed by the JPA persistable unit
	 * @return a Set of class names to be managed by JPA
	 */
	public abstract Set<String> exposePersistableBusinessObjectClassNames();
}
