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
package org.kuali.rice.kew.plugin;

/**
 * Represents an entity which can be modified.  A Modifiable records it's state when
 * it's created.  As long as the Modifiable remains in this state then invocations of
 * isModified() should return false.  If this state changes in a way which makes this 
 * Modifiable "dirty" then subsequent calls to isModified() should return true.
 * 
 * <p>It is up to the implementor of the Modifiable to determine the contract between
 * the Modifiable and the entities interested in it's modified status.  This could
 * include interested entities taking action on the Modifiable once it has become
 * modified which result in it's modified status being reset.  Another possibility is
 * for a Modifiable to be discarded once it has been modified.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Modifiable {

    boolean isModified();
    
}
