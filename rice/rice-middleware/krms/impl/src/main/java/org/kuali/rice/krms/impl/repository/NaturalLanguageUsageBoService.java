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

import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import java.util.List;

/**
 * This is the interface for accessing repository {@link NaturalLanguageUsageBo} related business objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface NaturalLanguageUsageBoService {


    /**
     * This will create a {@link NaturalLanguageUsage} exactly like the parameter passed in.
     * 
     * @param naturalLanguageUsage  The NaturalLanguageUsage to create.
     * @throws IllegalArgumentException if the NaturalLanguageUsage is null.
     * @throws IllegalStateException if the NaturalLanguageUsage already exists in the system.
     * @return a {@link NaturalLanguageUsage} exactly like the parameter passed in.
     * 
     */
    public NaturalLanguageUsage createNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage);

    /**
     * Retrieves a NaturalLanguageUsage from the repository based on the given id.
     * 
     * @param naturalLanguageUsageId to retrieve.
     * @return a {@link NaturalLanguageUsage} identified by the given id.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     * 
     */
    public NaturalLanguageUsage getNaturalLanguageUsage(String naturalLanguageUsageId);

    /**
     * Retrieves a NaturalLanguageUsage from the repository based on the given namespace and name.
     *
     * @param namespace of the NaturalLanguageUsage to retrieve.
     * @param name of the NaturalLanguageUsage to retrieve.
     * @return a {@link NaturalLanguageUsage} identified by the given namespace and name.
     * A null reference is returned if the repository does not contain a NaturalLanguageUsage with the given namespace
     * and name.
     *
     */
    public NaturalLanguageUsage getNaturalLanguageUsageByName(String namespace, String name);

    /**
     * This will update an existing {@link NaturalLanguageUsage}.
     * 
     * @param naturalLanguageUsage  The NaturalLanguageUsage to update.
     * @throws IllegalArgumentException if the NaturalLanguageUsage is null.
     * @throws IllegalStateException if the NaturalLanguageUsage does not exists in the system.
     * 
     */
    public void updateNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage);

    /**
     * Delete the {@link NaturalLanguageUsage} with the given id.
     * 
     * @param naturalLanguageUsageId to delete.
     * @throws IllegalArgumentException if the NaturalLanguageUsage is null.
     * @throws IllegalStateException if the NaturalLanguageUsage does not exists in the system
     * 
     */
    public void deleteNaturalLanguageUsage(String naturalLanguageUsageId);

    public List<NaturalLanguageUsage> findNaturalLanguageUsagesByName(String name);

    public List<NaturalLanguageUsage> findNaturalLanguageUsagesByDescription(String description);

    public List<NaturalLanguageUsage> findNaturalLanguageUsagesByNamespace(String namespace);

    /**
     * Converts a mutable {@link NaturalLanguageUsageBo} to its immutable counterpart, {@link NaturalLanguageUsage}.
     * @param naturalLanguageUsageBo the mutable business object.
     * @return a {@link NaturalLanguageUsage} the immutable object.
     * 
     */
    public NaturalLanguageUsage to(NaturalLanguageUsageBo naturalLanguageUsageBo);

    /**
     * Converts a immutable {@link NaturalLanguageUsage} to its mutable {@link NaturalLanguageUsageBo} counterpart.
     * @param naturalLanguageUsage the immutable object.
     * @return a {@link NaturalLanguageUsageBo} the mutable NaturalLanguageUsageBo.
     * 
     */
    public NaturalLanguageUsageBo from(NaturalLanguageUsage naturalLanguageUsage);

}
