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
package org.kuali.rice.kew.engine.node.service;

import java.util.List;

import org.kuali.rice.kew.engine.node.Branch;

/**
 * A service providing data access for {@link Branch} instances.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface BranchService {
    public void save(Branch branch);    
    public void deleteBranchStates(List statesToBeDeleted);
    /**
     * Responsible for inspecting the branch hierarchy/scope and returning a value
     * for the variable name if it exists somewere in scope. 
     * @param branch the lowermost scope to start resolution
     * @return a value for the key if it exists somewere in scope
     */
    public String getScopedVariableValue(Branch branch, String name);
    /**
     * Responsible for setting a value in the branch hierarchy/scope.  If the variable name
     * exists in a scope, it will be updated as opposed to created in a lower scope.  
     * @param branch the lowermost scope to start resolution
     * @param value the value to set for the variable
     * @return the replaced value of the variable, if the variable was already defined, or null otherwise
     */
    public String setScopedVariableValue(Branch branch, String name, String value);
}
