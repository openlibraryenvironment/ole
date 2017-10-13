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
package org.kuali.rice.kim.framework.identity.phone;

import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface EntityPhoneTypeEbo extends CodedAttributeContract, ExternalizableBusinessObject {

    /**
     * This the name for the AddressType.  This can be null or a blank string.
     *
     * @return the name of the AddressType
     */
    String getName();

    /**
     * This the sort code for the AddressType.  This can be null or a blank string.
     *
     * @return the sort code of the AddressType
     */
    String getSortCode();
    
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
     * The code value for this object.  In general a code value cannot be null or a blank string.
     *
     * @return the code value for this object.
     */
    String getCode();
}
