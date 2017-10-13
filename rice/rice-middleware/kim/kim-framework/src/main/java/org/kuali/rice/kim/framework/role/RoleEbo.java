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
package org.kuali.rice.kim.framework.role;

import org.kuali.rice.kim.api.role.RoleContract;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface RoleEbo extends RoleContract, ExternalizableBusinessObject {
    
    /**
     * A namespace for this role.  A namespace for a role identifies the system/module to which this role applies.
     *
     * @return Namespace for the role.
     */
    String getNamespaceCode();

    /**
     * The human readable name for this role.
     *
     * @return Human readable role name.
     */
    String getName();

    /**
     * A full textual description of this role.  This String should provide a verbose description of the role, what
     * it is meant to provide to principals assigned to it, and what permissions it implies.
     *
     * @return Description of the role.
     */
    String getDescription();

    /**
     * Provides the associated KimType identifier for this role.  This controls what additional attributes
     * are available.
     *
     * @return KimType Id
     */
    String getKimTypeId();
    
    /**
     * Returns the version number for this object.  In general, this value should only
     * be null if the object has not yet been stored to a persistent data store.
     * This version number is generally used for the purposes of optimistic locking.
     * 
     * @return the version number, or null if one has not been assigned yet
     */
    Long getVersionNumber();
    
    /**
     * The unique identifier for an object.  This can be null.
     *
     * @return the id
     */
    String getId();
    
    /**
     * The active indicator for an object.
     *
     * @return true if active false if not.
     */
    boolean isActive();
    
    /**
     * Return the globally unique object id of this object.  In general, this value should only
     * be null if the object has not yet been stored to a persistent data store.
     * 
     * @return the objectId of this object, or null if it has not been set yet
     */
    String getObjectId();
}
