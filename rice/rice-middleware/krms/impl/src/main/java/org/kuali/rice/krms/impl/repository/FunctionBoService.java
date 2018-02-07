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
package org.kuali.rice.krms.impl.repository;

import org.kuali.rice.krms.api.repository.function.FunctionDefinition;
import org.kuali.rice.krms.api.repository.function.FunctionRepositoryService;

/**
 * This is the interface for accessing KRMS repository Function related bos 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public interface FunctionBoService extends FunctionRepositoryService {

    /**
     * This will create a {@link FunctionDefinition} exactly like the function passed in.
     *
     * @param function  The Function to create
     * @throws IllegalArgumentException if the function is null
     * @throws IllegalStateException if the function already exists in the system
     */
	public FunctionDefinition createFunction(FunctionDefinition function);
	
    /**
     * This will update an existing {@link FunctionDefinition}.
     *
     * @param function  The Function to update
     * @throws IllegalArgumentException if the function is null
     * @throws IllegalStateException if the function does not exist in the system
     */	
	public void updateFunction(FunctionDefinition function);
	
    /**
     * Retrieves a Function from the repository based on the given function id.
     *
     * @param functionId the id of the Function to retrieve
     * @return a {@link FunctionDefinition} identified by the given functionId.  
     * A null reference is returned if an invalid or non-existent functionId is supplied.
     */
	public FunctionDefinition getFunctionById(String functionId);
	
    /**
     * Retrieves a Function from the repository based on the provided function name
     * and namespace.
     *
     * @param name the name of the Function to retrieve.
     * @param namespace the namespace that the Function is under.
     * @return a {@link FunctionDefinition} identified by the given name and namespace.  
     * A null reference is returned if an invalid or non-existent function name and
     * namespace combination is supplied.
     */
	public FunctionDefinition getFunctionByNameAndNamespace(String name, String namespace);
	
}
