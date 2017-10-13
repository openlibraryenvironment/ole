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
package org.kuali.rice.coreservice.framework.component;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.coreservice.api.component.ComponentContract;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;

/**
 * TODO: Likely should remove all methods from this interface after KULRICE-7170 is fixed
 */
public interface ComponentEbo extends ComponentContract, ExternalizableBusinessObject, MutableInactivatable  {

	/**
     * This is the name value for the component.  It cannot be null or a blank string.
     * @return name
     */
    String getName();

    /**
     * This is the namespace for the component.  It cannot be null or a blank string.
     * <p>
     * It is a way of assigning the component to a logical grouping within a rice application or rice ecosystem.
     * </p>
     *
     * @return namespace code
     */
    String getNamespaceCode();

    /**
     * Returns the id of the component set this component belongs to if this component was published as part of such
     * a component set.  Will return a null value if this component was not published as part of a component set.
     *
     * @return the id of the component set this component was published under, or null if this component is not part of
     * a published set
     */
    String getComponentSetId();
    
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
    
    /**
	 * The code value for this object.  In general a code value cannot be null or a blank string.
	 *
	 * @return the code value for this object.
	 */
	String getCode();
}
