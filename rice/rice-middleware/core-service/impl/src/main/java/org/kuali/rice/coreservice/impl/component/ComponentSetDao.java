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
package org.kuali.rice.coreservice.impl.component;

/**
 * A Data Access Object which handles data operations related to the tracking of component sets
 * which have been published to the component system.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ComponentSetDao {

    ComponentSetBo getComponentSet(String componentSetId);

    /**
     * Saves the given ComponentSetBo, in the case that an optimistic locking exception occurs, it "eats" the exception
     * and returns "false".  Otherwise, if the save is successful it returns "true".
     */
    boolean saveIgnoreLockingFailure(ComponentSetBo componentSetBo);

}
