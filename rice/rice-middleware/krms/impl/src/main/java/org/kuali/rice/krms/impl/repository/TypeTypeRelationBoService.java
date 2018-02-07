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

import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import java.util.List;

/**
 * This is the interface for accessing repository {@link TypeTypeRelationBo} related business objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface TypeTypeRelationBoService {


    /**
     * This will create a {@link TypeTypeRelation} exactly like the parameter passed in.
     * 
     * @param typeTypeRelation  The TypeTypeRelation to create.
     * @throws IllegalArgumentException if the TypeTypeRelation is null.
     * @throws IllegalStateException if the TypeTypeRelation already exists in the system.
     * @return a {@link TypeTypeRelation} exactly like the parameter passed in.
     * 
     */
    public TypeTypeRelation createTypeTypeRelation(TypeTypeRelation typeTypeRelation);

    /**
     * Retrieves a TypeTypeRelation from the repository based on the given id.
     * 
     * @param typeTypeRelationId to retrieve.
     * @return a {@link TypeTypeRelation} identified by the given id.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     * 
     */
    public TypeTypeRelation getTypeTypeRelation(String typeTypeRelationId);

    /**
     * This will update an existing {@link TypeTypeRelation}.
     * 
     * @param typeTypeRelation  The TypeTypeRelation to update.
     * @throws IllegalArgumentException if the TypeTypeRelation is null.
     * @throws IllegalStateException if the TypeTypeRelation does not exists in the system.
     * 
     */
    public void updateTypeTypeRelation(TypeTypeRelation typeTypeRelation);

    /**
     * Delete the {@link TypeTypeRelation} with the given id.
     * 
     * @param typeTypeRelationId to delete.
     * @throws IllegalArgumentException if the TypeTypeRelation is null.
     * @throws IllegalStateException if the TypeTypeRelation does not exists in the system
     * 
     */
    public void deleteTypeTypeRelation(String typeTypeRelationId);

    public List<TypeTypeRelation> findTypeTypeRelationsByFromType(String fromTypeId);

    public List<TypeTypeRelation> findTypeTypeRelationsByToType(String toTypeId);

    public List<TypeTypeRelation> findTypeTypeRelationsByRelationshipType(RelationshipType relationshipType);

    public List<TypeTypeRelation> findTypeTypeRelationsBySequenceNumber(Integer sequenceNumber);

    /**
     * Converts a mutable {@link TypeTypeRelationBo} to its immutable counterpart, {@link TypeTypeRelation}.
     * @param typeTypeRelationBo the mutable business object.
     * @return a {@link TypeTypeRelation} the immutable object.
     * 
     */
    public TypeTypeRelation to(TypeTypeRelationBo typeTypeRelationBo);

    /**
     * Converts a immutable {@link TypeTypeRelation} to its mutable {@link TypeTypeRelationBo} counterpart.
     * @param typeTypeRelation the immutable object.
     * @return a {@link TypeTypeRelationBo} the mutable TypeTypeRelationBo.
     * 
     */
    public TypeTypeRelationBo from(TypeTypeRelation typeTypeRelation);

}
