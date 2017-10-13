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

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBindingQueryResults;
import java.util.List;

/**
 * This is the interface for accessing repository {@link ReferenceObjectBindingBo} related business objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface ReferenceObjectBindingBoService {


    /**
     * This will create a {@link ReferenceObjectBinding} exactly like the parameter passed in.
     * 
     * @param referenceObjectBinding  The ReferenceObjectBinding to create.
     * @throws IllegalArgumentException if the ReferenceObjectBinding is null.
     * @throws IllegalStateException if the ReferenceObjectBinding already exists in the system.
     * @return a {@link ReferenceObjectBinding} exactly like the parameter passed in.
     * 
     */
    public ReferenceObjectBinding createReferenceObjectBinding(ReferenceObjectBinding referenceObjectBinding);

    /**
     * Retrieves a ReferenceObjectBinding from the repository based on the given id.
     * 
     * @param referenceObjectBindingId to retrieve.
     * @return a {@link ReferenceObjectBinding} identified by the given id.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     * 
     */
    public ReferenceObjectBinding getReferenceObjectBinding(String referenceObjectBindingId);

    /**
     * This will update an existing {@link ReferenceObjectBinding}.
     * 
     * @param referenceObjectBinding  The ReferenceObjectBinding to update.
     * @throws IllegalArgumentException if the ReferenceObjectBinding is null.
     * @throws IllegalStateException if the ReferenceObjectBinding does not exists in the system.
     * 
     */
    public void updateReferenceObjectBinding(ReferenceObjectBinding referenceObjectBinding);

    /**
     * Delete the {@link ReferenceObjectBinding} with the given id.
     * 
     * @param referenceObjectBindingId to delete.
     * @throws IllegalArgumentException if the ReferenceObjectBinding is null.
     * @throws IllegalStateException if the ReferenceObjectBinding does not exists in the system
     * 
     */
    public void deleteReferenceObjectBinding(String referenceObjectBindingId);

    public List<ReferenceObjectBinding> findReferenceObjectBindingsByCollectionName(String collectionName);

    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsDiscriminatorType(String krmsDiscriminatorType);

    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsObject(String krmsObjectId);

    public List<ReferenceObjectBinding> findReferenceObjectBindingsByNamespace(String namespace);

    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceDiscriminatorType(String referenceDiscriminatorType);

    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceObject(String referenceObjectId);

    public List<String> findReferenceObjectBindingIds(final QueryByCriteria queryByCriteria);

    public ReferenceObjectBindingQueryResults findReferenceObjectBindings(final QueryByCriteria queryByCriteria);

    /**
     * Converts a mutable {@link ReferenceObjectBindingBo} to its immutable counterpart, {@link ReferenceObjectBinding}.
     * @param referenceObjectBindingBo the mutable business object.
     * @return a {@link ReferenceObjectBinding} the immutable object.
     * 
     */
    public ReferenceObjectBinding to(ReferenceObjectBindingBo referenceObjectBindingBo);

    /**
     * Converts a immutable {@link ReferenceObjectBinding} to its mutable {@link ReferenceObjectBindingBo} counterpart.
     * @param referenceObjectBinding the immutable object.
     * @return a {@link ReferenceObjectBindingBo} the mutable ReferenceObjectBindingBo.
     * 
     */
    public ReferenceObjectBindingBo from(ReferenceObjectBinding referenceObjectBinding);

}
