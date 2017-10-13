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
package org.kuali.rice.kim.api.common.template;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
/**
 *
 * This is a contract for a Template. A template represents some course-grained information that is used to
 * create a permission or responsibility which then contains more specific
 * information in permission details or responsibility details.
 * Eg: of a template would be Use Screen,Maintain records etc.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public interface TemplateContract extends Versioned, GloballyUnique, Inactivatable, Identifiable {
    /**
     * The namespace code that this KIM Permission Template belongs too.
     *
     * @return namespaceCode
     */
    String getNamespaceCode();

    /**
     * The name of the KIM Permission Template.
     *
     * @return name
     */
    String getName();

    /**
     * The description of the KIM Permission Template.
     *
     * @return description
     */
	String getDescription();

    /**
     * The KIM Type ID referenced by the KIM Permission Template.
     *
     * @return typeId
     */
	String getKimTypeId();
}
