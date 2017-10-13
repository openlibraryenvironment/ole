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
package org.kuali.rice.coreservice.impl.component;


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table
import org.kuali.rice.coreservice.api.component.Component
import org.kuali.rice.coreservice.api.component.ComponentContract

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@IdClass(ComponentId.class)
@Entity
@Table(name="KRCR_DRVD_CMPNT_T")
public class DerivedComponentBo extends PersistableBusinessObjectBase implements ComponentContract {

	@Id
	@Column(name="NMSPC_CD")
	String namespaceCode

	@Id
	@Column(name="CMPNT_CD")
	String code

	@Column(name="NM")
	String name

    @Column(name="CMPNT_SET_ID")
    String componentSetId

    @Override
    String getObjectId() {
        return null
    }

    @Override
    boolean isActive() {
        return true
    }

    @Override
    Long getVersionNumber() {
        return null
    }
    
    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static Component to(DerivedComponentBo bo) {
        if (bo == null) {
            return null
        }
        return Component.Builder.create(bo).build()
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static DerivedComponentBo from(Component im) {
        if (im == null) {
            return null
        }

        DerivedComponentBo bo = new DerivedComponentBo()
        bo.code = im.code
        bo.name = im.name
        bo.namespaceCode = im.namespaceCode
		bo.componentSetId = im.componentSetId

        return bo
    }

    static ComponentBo toComponentBo(DerivedComponentBo derivedComponentBo) {
        if (derivedComponentBo == null) {
            return null
        }
        Component component = DerivedComponentBo.to(derivedComponentBo);
        return ComponentBo.from(component);
    }
}

