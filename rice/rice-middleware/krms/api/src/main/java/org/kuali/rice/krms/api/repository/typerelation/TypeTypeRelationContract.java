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
package org.kuali.rice.krms.api.repository.typerelation;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Defines the contract for a {@link TypeTypeRelation}
 *
 * @see TypeTypeRelation
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface TypeTypeRelationContract extends Identifiable, Inactivatable, Versioned {
    /**
     * This is the FromTypeId of the TypeTypeRelation
     * <p>
     * The FromTypeId of the TypeTypeRelation
     * </p>
     * @return the FromTypeId of the TypeTypeRelation
     */
    String getFromTypeId();
    /**
     * This is the ToTypeId of the TypeTypeRelation
     * <p>
     * The ToTypeId of the TypeTypeRelation
     * </p>
     * @return the ToTypeId of the TypeTypeRelation
     */
    String getToTypeId();
    /**
     * This is the RelationshipType of the TypeTypeRelation
     * <p>
     * The RelationshipType of the TypeTypeRelation
     * </p>
     * @return the RelationshipType of the TypeTypeRelation
     */
    RelationshipType getRelationshipType();

    Integer getSequenceNumber();
}
