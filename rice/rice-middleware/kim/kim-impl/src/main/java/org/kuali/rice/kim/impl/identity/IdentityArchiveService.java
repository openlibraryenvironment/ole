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
package org.kuali.rice.kim.impl.identity;

import org.kuali.rice.kim.api.identity.entity.EntityDefault;

/**
 * 
 * This service archives EntityDefault.  It's purpose is to provide long term 
 * storage for basic identity data that may be removed from the IdentityService implementation's
 * backing store.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface IdentityArchiveService {

    /**
     * Gets a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} with an id from the archive.
     * {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} is a condensed version of {@link org.kuali.rice.kim.api.identity.entity.Entity} that contains
     * default values of its subclasses
     *
     * <p>
     *   This method will return null if the Entity does not exist.
     * </p>
     *
     * @param id the unique id to retrieve the entity by. cannot be null.
     * @return a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} or null
     * @throws IllegalArgumentException if the id is blank
     */
    EntityDefault getEntityDefaultFromArchive(String id ) throws IllegalArgumentException;

	/**
     * Gets a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} with an principalId from the archive.
     * {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} is a condensed version of {@link org.kuali.rice.kim.api.identity.entity.Entity} that contains
     * default values of its subclasses
     *
     * <p>
     *   This method will return null if the Entity does not exist.
     * </p>
     *
     * @param principalId the unique principalId to retrieve the entity by. cannot be null.
     * @return a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} or null
     * @throws IllegalArgumentException if the principalId is blank
     */
    EntityDefault getEntityDefaultFromArchiveByPrincipalId(String principalId) throws IllegalArgumentException;

	/**
     * Gets a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} with an principalName from the archive.
     * {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} is a condensed version of {@link org.kuali.rice.kim.api.identity.entity.Entity} that contains
     * default values of its subclasses
     *
     * <p>
     *   This method will return null if the Entity does not exist.
     * </p>
     *
     * @param principalName the unique principalName to retrieve the entity by. cannot be null.
     * @return a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} or null
     * @throws IllegalArgumentException if the principalName is blank
     */
	EntityDefault getEntityDefaultFromArchiveByPrincipalName(String principalName) throws IllegalArgumentException;
	
    /**
     * Gets a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} with an employeeId from the archive.
     * {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} is a condensed version of {@link org.kuali.rice.kim.api.identity.entity.Entity} that contains
     * default values of its subclasses
     *
     * <p>
     *   This method will return null if the Entity does not exist.
     * </p>
     *
     * @param employeeId the unique employeeId to retrieve the entity by. cannot be null.
     * @return a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} or null
     * @throws IllegalArgumentException if the employeeId is blank
     */
    EntityDefault getEntityDefaultFromArchiveByEmployeeId(String employeeId) throws IllegalArgumentException;
    
	/**
     * Saves a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} to the archive.
     * {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} is a condensed version of {@link org.kuali.rice.kim.api.identity.entity.Entity} that contains
     * default values of its subclasses
     *
     * <p>
     *   This method will return the saved EntityDefault object
     * </p>
     *
     * @param entityDefault the unique principalName to retrieve the entity by. cannot be null.
     * @return a {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} or null
     * @throws IllegalArgumentException if the entityDefault is null
     */
    //TODO: this should probably return some kind of Future<EntityDefault> if we can find a way to remote that
	void saveEntityDefaultToArchive(EntityDefault entityDefault) throws IllegalArgumentException;

    /**
     * Flushes {@link org.kuali.rice.kim.api.identity.entity.EntityDefault} to the archive.
     *
     * <p>
     *   This method flushes the "saved" entities to the database
     * </p>
     *
     * @return void
     */
    void flushToArchive() throws IllegalArgumentException;
	
}
