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

import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * This is the interface for accessing KRMS repository Proposition related
 * business objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface PropositionBoService {

    /**
     * This will create a {@link PropositionDefinition} exactly like the parameter passed in.
     *
     * @param prop the proposition to create
     * @throws IllegalArgumentException if the proposition is null
     * @throws IllegalStateException if the proposition already exists in the system
     */
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    PropositionDefinition createProposition(PropositionDefinition prop);

    /**
     * This will update an existing {@link PropositionDefinition}.
     *
     * @param prop the proposition to update
     * @throws IllegalArgumentException if the proposition is null
     * @throws IllegalStateException if the proposition does not exist in the system
     */
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    void updateProposition(PropositionDefinition prop);

    /**
     * This will delete an existing {@link PropositionDefinition}.
     *
     * @param propId the proposition to delete
     * @throws IllegalArgumentException if the proposition is null
     * @throws IllegalStateException if the proposition does not exist in the system
     */
    void deleteProposition(String propId);

    /**
     * Lookup the proposition based on the given proposition id.
     *
     * @param propId the given proposition id
     * @return a proposition associated with the given proposition id.  A null reference is returned if an invalid or
     *         non-existent id is supplied.
     */
    @Cacheable(value= PropositionDefinition.Cache.NAME, key="'propId=' + #p0")
    PropositionDefinition getPropositionById(String propId);

    public Set<PropositionDefinition> getPropositionsByType(String typeId);

    public Set<PropositionDefinition> getPropositionsByRule(String ruleId);

    /**
     * This will create a {@link PropositionParameter} exactly like the parameter passed in.
     *
     * @param parameter the proposition parameter to create
     * @throws IllegalArgumentException if the proposition parameter is null
     * @throws IllegalStateException if the proposition parameter is already existing in the system
     */
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    void createParameter(PropositionParameter parameter);

    /**
     * This will update a {@link PropositionParameter}.
     *
     *
     * @param parameter the proposition parameter to update
     * @throws IllegalArgumentException if the proposition parameter is null
     * @throws IllegalStateException if the proposition parameter does not exist in the system
     */
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    void updateParameter(PropositionParameter parameter);


    /**
     * Lookup the proposition parameters based on the given proposition id.
     *
     * @param propId the given proposition id
     * @return a list of PropositionParameters associated with the given proposition id.  A null reference is returned if an invalid or
     *         non-existant id is supplied.
     */
    List<PropositionParameter> getParameters(String propId);

    /**
     * Lookup the proposition parameter based on the id.
     *
     * @param id the given proposition id
     * @return an immutable PropositionParameters associated with the given id.  A null reference is returned if an invalid or
     *         non-existant id is supplied.
     */
    PropositionParameter getParameterById(String id);

    /**
     * Lookup the proposition parameter based on the proposition id and sequence number.
     *
     * @param propId the given proposition id
     * @return an immutable PropositionParameters associated with the given proposition id and sequence number.  A null reference is returned if an invalid or
     *         non-existant.
     */
    PropositionParameter getParameterByPropIdAndSequenceNumber(String propId, Integer sequenceNumber);


}
