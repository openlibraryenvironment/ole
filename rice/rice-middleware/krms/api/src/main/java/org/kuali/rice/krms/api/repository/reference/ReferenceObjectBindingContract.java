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
package org.kuali.rice.krms.api.repository.reference;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Defines the contract for a {@link ReferenceObjectBinding}
 *
 * @see ReferenceObjectBinding
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ReferenceObjectBindingContract extends Identifiable, Inactivatable, Versioned {
    /**
     * This is the CollectionName of the ReferenceObjectBinding
     * <p>
     * The CollectionName of the ReferenceObjectBinding
     * </p>
     * @return the CollectionName of the ReferenceObjectBinding
     */
    public String getCollectionName();

    /**
     * This is the KrmsDiscriminatorType of the ReferenceObjectBinding
     * <p>
     * The KrmsDiscriminatorType of the ReferenceObjectBinding
     * </p>
     * @return the KrmsDiscriminatorType of the ReferenceObjectBinding
     */
    public String getKrmsDiscriminatorType();

    /**
     * This is the KrmsObjectId of the ReferenceObjectBinding
     * <p>
     * The KrmsObjectId of the ReferenceObjectBinding
     * </p>
     * @return the KrmsObjectId of the ReferenceObjectBinding
     */
    public String getKrmsObjectId();

    /**
     * This is the namespace of the ReferenceObjectBinding
     * <p>
     * The namespace of the ReferenceObjectBinding
     * </p>
     * @return the namespace of the ReferenceObjectBinding
     */
    public String getNamespace();

    /**
     * This is the ReferenceDiscriminatorType of the ReferenceObjectBinding
     * <p>
     * The ReferenceDiscriminatorType of the ReferenceObjectBinding
     * </p>
     * @return the ReferenceDiscriminatorType of the ReferenceObjectBinding
     */
    public String getReferenceDiscriminatorType();

    /**
     * This is the ReferenceObjectId of the ReferenceObjectBinding
     * <p>
     * The ReferenceObjectId of the ReferenceObjectBinding
     * </p>
     * @return the ReferenceObjectId of the ReferenceObjectBinding
     */
    public String getReferenceObjectId();


}
