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
package org.kuali.rice.location.framework.campus;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.location.api.campus.CampusContract;
import org.kuali.rice.location.api.campus.CampusTypeContract;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface CampusEbo extends CampusContract, ExternalizableBusinessObject, MutableInactivatable {

    /**
     * This is the name for the Campus.
     *
     * <p>
     * It is a name a campus.
     * </p>
     *
     * @return name for Campus.
     */
    String getName();

    /**
     * This is the short name for the Campus.
     *
     * <p>
     * It is a shorter name for a campus.
     * </p>
     *
     * @return short name for Campus.
     */
    String getShortName();

    /**
     * This is the campus type for the Campus.
     *
     * <p>
     * It is a object that defines the type of a campus.
     * </p>
     *
     * @return short name for Campus.
     */
    CampusTypeContract getCampusType();
    
    /**
     * The code value for this object.  In general a code value cannot be null or a blank string.
     *
     * @return the code value for this object.
     */
    String getCode();
    
    /**
     * Returns the version number for this object.  In general, this value should only
     * be null if the object has not yet been stored to a persistent data store.
     * This version number is generally used for the purposes of optimistic locking.
     * 
     * @return the version number, or null if one has not been assigned yet
     */
    Long getVersionNumber();
    
    /**
     * Return the globally unique object id of this object.  In general, this value should only
     * be null if the object has not yet been stored to a persistent data store.
     * 
     * @return the objectId of this object, or null if it has not been set yet
     */
    String getObjectId();
    
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
