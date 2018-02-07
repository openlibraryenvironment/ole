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
package org.kuali.rice.core.api.mo.common;

/**
 * This interface can be used to identify a model object which has a globally unique identifier.
 * This globally unique identifier is referred as the "objectId" of the object.  The means by
 * which it is generated or general format of this value is not specified, however it is
 * intended that some sort of GUID algorithm is used to generate this value, such as the one
 * provided by {@link java.util.UUID}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface GloballyUnique {

	/**
	 * Return the globally unique object id of this object.  In general, this value should only
	 * be null if the object has not yet been stored to a persistent data store.
	 * 
	 * @return the objectId of this object, or null if it has not been set yet
	 */
	String getObjectId();
	
}
