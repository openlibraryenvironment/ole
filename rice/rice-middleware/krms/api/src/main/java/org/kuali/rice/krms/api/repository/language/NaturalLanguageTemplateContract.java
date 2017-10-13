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
package org.kuali.rice.krms.api.repository.language;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

import java.util.Map;

/**
 * Defines the contract for a {@link NaturalLanguageTemplate}
 *
 * @see NaturalLanguageTemplate
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NaturalLanguageTemplateContract extends Identifiable, Inactivatable, Versioned {
    /**
     * This is the Language Code of the NaturalLanguageTemplate
     * <p>
     * The Language Code of the NaturalLanguageTemplate
     * </p>
     * @return the Language Code of the NaturalLanguageTemplate
     */
    String getLanguageCode();

    /**
     * This is the NaturalLanguageUsageId of the NaturalLanguageTemplate
     * <p>
     * The NaturalLanguageUsageId of the NaturalLanguageTemplate
     * </p>
     * @return the NaturalLanguageUsageId of the NaturalLanguageTemplate
     */
    String getNaturalLanguageUsageId();

    /**
     * This is the TypeId of the NaturalLanguageTemplate
     * <p>
     * The TypeId of the NaturalLanguageTemplate
     * </p>
     * @return the TypeId of the NaturalLanguageTemplate
     */
    String getTypeId();

    /**
     * This is the Template of the NaturalLanguageTemplate
     * <p>
     * The Template of the NaturalLanguageTemplate
     * </p>
     * @return the Template of the NaturalLanguageTemplate
     */
    String getTemplate();

    /**
     * This method returns a list of custom/remote attributes associated with the
     * agenda.
     * <p>
     * The attributes of the NaturalLanguageUsage
     * </p>
     * @return a list of custom/remote attribute of the agenda.
     */
    public Map<String, String> getAttributes();
}
