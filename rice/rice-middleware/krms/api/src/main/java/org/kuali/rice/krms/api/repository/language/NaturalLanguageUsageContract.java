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

/**
 * Defines the contract for a {@link NaturalLanguageUsage}
 *
 * @see NaturalLanguageUsage
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NaturalLanguageUsageContract extends Identifiable, Inactivatable, Versioned {

    /**
     * This is the Description of the NaturalLanguageUsage
     * <p>
     * The Description of the NaturalLanguageUsage
     * </p>
     * @return the Description of the NaturalLanguageUsage
     */
    public String getDescription();

    /**
     * This is the name of the NaturalLanguageUsage
     * <p>
     * The name of the NaturalLanguageUsage
     * </p>
     * @return the name of the NaturalLanguageUsage
     */
    public String getName();

    /**
     * This is the namespace of the NaturalLanguageUsage
     * <p>
     * The namespace of the NaturalLanguageUsage
     * </p>
     * @return the namespace of the NaturalLanguageUsage
     */
    public String getNamespace();
}
