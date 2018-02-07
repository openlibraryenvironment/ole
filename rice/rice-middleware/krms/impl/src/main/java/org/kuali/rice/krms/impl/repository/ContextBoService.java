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


import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * This is the interface for accessing KRMS repository Context related bos 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ContextBoService {

    /**
     * This will create a {@link ContextDefinition} exactly like the parameter passed in.
     *
     * @param context  The Context to create
     * @throws IllegalArgumentException if the context is null
     * @throws IllegalStateException if the context already exists in the system
     */
    @CacheEvict(value={ContextDefinition.Cache.NAME}, allEntries = true)
	public ContextDefinition createContext(ContextDefinition context);

    /**
     * This will update an existing {@link ContextDefinition}.
     *
     * @param context  The Context to update
     * @throws IllegalArgumentException if the Context is null
     * @throws IllegalStateException if the Context does not exists in the system
     */
    @CacheEvict(value={ContextDefinition.Cache.NAME}, allEntries = true)
	public void updateContext(ContextDefinition context);
	
//	public void createContextAttribute(ContextAttribute contextAttribute);
//	public void updateContextAttribute(ContextAttribute contextAttribute);
	
    /**
     * Retrieves an Context from the repository based on the given context id.
     *
     * @param contextId the id of the Context to retrieve
     * @return an {@link ContextDefinition} identified by the given contextId.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     * @throws IllegalArgumentException if the contextId is null or blank.
     */
    @Cacheable(value= ContextDefinition.Cache.NAME, key="'actionId=' + #p0")
	public ContextDefinition getContextByContextId( String contextId );
	
    /**
     * Retrieves an Context from the repository based on the provided context name
     * and namespace.
     *
     * @param name the name of the Context to retrieve.
     * @param namespace the namespace that the context is under.
     * @return an {@link ContextDefinition} identified by the given name and namespace.  
     * A null reference is returned if an invalid or non-existent name and
     * namespace combination is supplied.
     * @throws IllegalArgumentException if the either the name or the namespace
     * is null or blank.
     */
    @Cacheable(value= ContextDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'namespace=' + #p1")
	public ContextDefinition getContextByNameAndNamespace( String name, String namespace );

	/**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
//	public ContextDefinition to( ContextBo bo);

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
//	public ContextBo from( ContextDefinition im );
}
