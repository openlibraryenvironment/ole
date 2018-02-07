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
package org.kuali.rice.kim.api.identity.personal;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * ethnicity information for a KIM identity
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface EntityEthnicityContract extends Versioned, GloballyUnique, Identifiable {
	
	/**
     * Gets this {@link EntityEthnicityContract}'s identity id.
     * @return the identity id for this {@link EntityEthnicityContract}, or null if none has been assigned.
     */
	String getEntityId();
	
	/**
     * Gets this {@link EntityEthnicityContract}'s ethnicity code.
     * @return the ethnicity code for this {@link EntityEthnicityContract}, or null if none has been assigned.
     */
	String getEthnicityCode();
	
	/**
     * Gets this {@link EntityEthnicityContract}'s unmasked ethnicity code.
     * @return the unmasked ethnicity code for this {@link EntityEthnicityContract}, or null if none has been assigned.
     */
	String getEthnicityCodeUnmasked();
	
	/**
     * Gets this {@link EntityEthnicityContract}'s sub-ethnicity code.
     * @return the sub-ethnicity code for this {@link EntityEthnicityContract}, or null if none has been assigned.
     */
	String getSubEthnicityCode();
	
	/**
     * Gets this {@link EntityEthnicityContract}'s unmasked sub-ethnicity code.
     * @return the unmasked sub-ethnicity code for this {@link EntityEthnicityContract}, or null if none has been assigned.
     */
    String getSubEthnicityCodeUnmasked();

    /**
     * Returns a boolean value that determines if personal fields should be suppressed.
     * @return boolean value that determines if personal fields should be suppressed.
     */
	boolean isSuppressPersonal();
}
