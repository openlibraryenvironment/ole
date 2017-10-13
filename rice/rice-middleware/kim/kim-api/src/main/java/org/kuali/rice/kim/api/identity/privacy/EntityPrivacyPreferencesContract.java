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
package org.kuali.rice.kim.api.identity.privacy;


import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
 /**
  * This is a contract for EntityPrivacyPreferences. Privacy preferences are used to
  * hide/suppress certain contact information.
  *
  * @author Kuali Rice Team (rice.collab@kuali.org)
  */
public interface EntityPrivacyPreferencesContract extends Versioned, GloballyUnique {

    /**
     * Gets this id of the parent identity object.
     * @return the identity id for this {@link EntityPrivacyPreferencesContract}
     */
    String getEntityId();

    /**
     * This is value designating if Entity Name should be suppressed.
     *
     * <p>
     * This is a boolean value that shows if identity names should be suppressed or not.
     * </p>
     *
     * @return suppressName
     */
    boolean isSuppressName();

    /**
     * This is value designating if Entity Address should be suppressed.
     *
     * <p>
     * This is a boolean value that shows if identity addresses should be suppressed or not.
     * </p>
     *
     * @return suppressAddress
     */
	boolean isSuppressAddress();

    /**
     * This is value designating if Entity Email should be suppressed.
     *
     * <p>
     * This is a boolean value that shows if identity emails should be suppressed or not.
     * </p>
     *
     * @return suppressEmail
     */
	boolean isSuppressEmail();

    /**
     * This is value designating if Entity Phone should be suppressed.
     *
     * <p>
     * This is a boolean value that shows if identity phones should be suppressed or not.
     * </p>
     *
     * @return suppressPhone
     */
	boolean isSuppressPhone();

    /**
     * This is value designating if Entity Personal information should be suppressed.
     *
     * <p>
     * This is a boolean value that shows if identity personal information should be suppressed or not.
     * </p>
     *
     * @return suppressPersonal
     */
	boolean isSuppressPersonal();
}
