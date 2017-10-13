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
package org.kuali.rice.kim.framework.group;

import java.util.Map;

import org.kuali.rice.kim.api.group.GroupContract;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface GroupEbo extends GroupContract, ExternalizableBusinessObject {
    
    /**
     * This is the namespace code for the Group.
     *
     * <p>
     * This is a namespace code assigned to a Group.  Together with name, it makes up another unique identifier for Group
     * </p>
     *
     * @return namespaceCode
     */
    String getNamespaceCode();

    /**
     * This is the name for the Group.
     *
     * <p>
     * This is a name assigned to a Group.  Together with NamespaceCode, it makes up another unique identifier for Group
     * </p>
     *
     * @return name
     */
    String getName();

    /**
     * This a description for the Group.
     *
     * <p>
     * This is a description assigned to a Group.
     * </p>
     *
     * @return description
     */
    String getDescription();

    /**
     * This a Kim Type Id for the Group.
     *
     * <p>
     * This links a Kim Type to the Group to allow custom types of Groups.
     * </p>
     *
     * @return description
     */
    String getKimTypeId();

    /**
     * This is a set of Attributes for a Group.
     *
     * <p>
     * This is a set of attributes which are key-label pairs that are defined by the Group's Kim Type.
     * </p>
     *
     * @return attributes
     */
    Map<String, String> getAttributes();
    
    /**
     * The unique identifier for an object.  This can be null.
     *
     * @return the id
     */
    String getId();
    
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
}
