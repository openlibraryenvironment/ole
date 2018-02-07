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
package org.kuali.rice.location.framework.country;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.location.api.country.CountryContract;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface CountryEbo extends CountryContract, ExternalizableBusinessObject, MutableInactivatable {
    
    /**
     * {@inheritDoc}
     *
     * An abbreviated String representing the unique identifying code for a given country.  This code correlates
     * directly to the alpha-2 country codes from the ISO-3166-1-alpha-2 standard.
     * <p>This property is required to exist.</p>
     */
    @Override
    String getCode();

    /**
     * An alternative country code to represent a country. This code correlats directly to the alpha-3 codes
     * from the ISO_3166-1-alpha-3 standard.
     * <p>This property is optional</p>
     *
     * @return The alternate country code if it exists.  null is returned if an alternate code does not exist.
     */
    String getAlternateCode();

    /**
     * A full, familiar, name of a country.
     * <p>This property is optional</p>
     *
     * @return The name of a country if it exists.  null is returned if a full name does not exist.
     */
    String getName();

    /**
     * Value representing whether a country is restricted.
     * <p>The meaning of restricted for a country varies depending upon the implementer - for instance if a country
     * may not be used in the address of a Vendor.</p>
     *
     * <p>The default value of this property is false.</p>
     *
     * @return if a country is restricted.
     */
    boolean isRestricted();

    /**
     * Returns the version number for this object.  In general, this value should only
     * be null if the object has not yet been stored to a persistent data store.
     * This version number is generally used for the purposes of optimistic locking.
     * 
     * @return the version number, or null if one has not been assigned yet
     */
    Long getVersionNumber();
    
    /**
     * The active indicator for an object.
     *
     * @return true if active false if not.
     */
    boolean isActive();
    
    /**
     * Sets the record to active or inactive.
     */
    void setActive(boolean active);
}
