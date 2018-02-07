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

import java.util.List;
import java.util.Set;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * This is the interface for accessing KRMS repository Agenda related
 * business objects. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface AgendaBoService {

    /**
     * This will create a {@link AgendaDefinition} exactly like the parameter passed in.
     *
     * @param agenda  The Agenda to create
     * @throws IllegalArgumentException if the Agenda is null
     * @throws IllegalStateException if the Agenda already exists in the system
     */
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
	public AgendaDefinition createAgenda(AgendaDefinition agenda);
	
    /**
     * This will update an existing {@link AgendaDefinition}.
     *
     * @param agenda  The Agenda to update
     * @throws IllegalArgumentException if the Agenda is null
     * @throws IllegalStateException if the Agenda does not exists in the system
     */
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
	public void updateAgenda(AgendaDefinition agenda);

    /**
     * Delete the {@link AgendaDefinition} with the given id.
     *
     * @param agendaId to delete.
     * @throws IllegalArgumentException if the Agenda is null.
     * @throws IllegalStateException if the Agenda does not exists in the system
     *
     */
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public void deleteAgenda(String agendaId);

    /**
     * Retrieves an Agenda from the repository based on the given agenda id.
     *
     * @param agendaId the id of the Agenda to retrieve
     * @return an {@link AgendaDefinition} identified by the given agendaId.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     */
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'agendaId=' + #p0")
	public AgendaDefinition getAgendaByAgendaId(String agendaId);
	
    /**
     * Retrieves an Agenda from the repository based on the provided agenda name
     * and context id.
     *
     * @param name the name of the Agenda to retrieve.
     * @param contextId the id of the context that the agenda belongs to.
     * @return an {@link AgendaDefinition} identified by the given name and namespace.  
     * A null reference is returned if an invalid or non-existent name and
     * namespace combination is supplied.
     */
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'contextId=' + #p1")
	public AgendaDefinition getAgendaByNameAndContextId(String name, String contextId);
	
    /**
     * Retrieves a set of Agendas associated with a context.
     *
     * @param contextId the id of the context
     * @return a set of {@link AgendaDefinition} associated with the given context.  
     * A null reference is returned if an invalid or contextId is supplied.
     */
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'contextId=' + #p0")
	public List<AgendaDefinition> getAgendasByContextId(String contextId);
	
    /**
     * This will create an {@link org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition} in the repository exactly like
     * the parameter passed in.
     *
     * @param agendaItem  The AgendaItemDefinition to create
     * @throws IllegalArgumentException if the AgendaItemDefinition is null
     * @throws IllegalStateException if the AgendaItemDefinition already exists in the system
     */
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
	public AgendaItemDefinition createAgendaItem(AgendaItemDefinition agendaItem);
	
    /**
     * This will update an existing {@link org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition}.
     *
     * @param agendaItem  The AgendaItemDefinition to update
     * @throws IllegalArgumentException if the AgendaItemDefinition is null
     * @throws IllegalStateException if the AgendaItemDefinition does not exists in the system
     */
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
	public void updateAgendaItem(AgendaItemDefinition agendaItem);
	
    /**
     * This will create an {@link org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition} in the repository exactly like
     * the parameter passed in.  The AgendaItemDefinition will be linked to an existing
     * AgendaItemDefinition in the relationship provided. Linking the AgendaItems effectively
     * builds a tree of AgendaItems that may be traversed by the engine.
     *
     * @param agendaItem  The AgendaItemDefinition to create
     * @param parentId  The id of the existing AgendaItemDefinition to be linked with the
     *  newly created AgendaItemDefinition
     * @param position A boolean used to specify the relationship between the
     *  linked AgendaItems.
     *  <p> If the position parameter is true, the new AgendaItemDefinition is linked as the next
     *  AgendaItemDefinition to be evaluated if the parent AgendaItemDefinition evaluates to TRUE.
     *  <p> If the position parameter is false, the new AgendaItemDefinition is linked as the next
     *  AgendaItemDefinition to be evaluated if the parent AgendaItemDefinition evaluates to FALSE.
     *  <p> If the position parameter is null,  the new AgendaItemDefinition is linked as the next
     *  AgendaItemDefinition to be evaluated after any true or false branches of the tree have
     *  been traversed.
     * @throws IllegalArgumentException if the AgendaItemDefinition is null
     * @throws IllegalStateException if the parent AgendaItemDefinition does not already exists in the system
     */
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
	public void addAgendaItem(AgendaItemDefinition agendaItem, String parentId, Boolean position);
	
    /**
     * Retrieves an AgendaItemDefinition from the repository based on the given agenda id.
     *
     * @param id the id of the AgendaItemDefinition to retrieve
     * @return an {@link org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition} identified by the given id.
     * A null reference is returned if an invalid or non-existent id is supplied.
     */
    @Cacheable(value= AgendaItemDefinition.Cache.NAME, key="'id=' + #p0")
	public AgendaItemDefinition getAgendaItemById(String id);

    // TODO: caching annotations
    public List<AgendaItemDefinition> getAgendaItemsByAgendaId(String id);

    // TODO: caching annotations
    public List<AgendaDefinition> getAgendasByType(String typeId) throws RiceIllegalArgumentException;

    // TODO: caching annotations
    public List<AgendaDefinition> getAgendasByTypeAndContext(String typeId, String contextId)
            throws RiceIllegalArgumentException;

    // TODO: caching annotations
    public List<AgendaItemDefinition> getAgendaItemsByType(String typeId) throws RiceIllegalArgumentException;

    // TODO: caching annotations
    public List<AgendaItemDefinition> getAgendaItemsByContext(String contextId) throws RiceIllegalArgumentException;

    // TODO: caching annotations
    public List<AgendaItemDefinition> getAgendaItemsByTypeAndContext(String typeId, String contextId)
            throws RiceIllegalArgumentException;

    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public void deleteAgendaItem(String id) throws RiceIllegalArgumentException;

	/**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
	public AgendaDefinition to(AgendaBo bo);

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
	public AgendaBo from(AgendaDefinition im);
}
