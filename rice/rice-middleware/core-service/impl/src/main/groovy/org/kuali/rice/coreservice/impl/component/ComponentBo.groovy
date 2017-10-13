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
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

import org.hibernate.annotations.Type
import org.kuali.rice.coreservice.api.component.Component
import org.kuali.rice.coreservice.framework.component.ComponentEbo
import org.kuali.rice.coreservice.impl.namespace.NamespaceBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.coreservice.api.namespace.NamespaceService
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator

@IdClass(ComponentId.class)
@Entity
@Table(name="KRCR_CMPNT_T")
public class ComponentBo extends PersistableBusinessObjectBase implements ComponentEbo {

    private static final long serialVersionUID = 1L;

    private static transient NamespaceService namespaceService

	@Id
	@Column(name="NMSPC_CD")
	String namespaceCode;

	@Id
	@Column(name="CMPNT_CD")
	String code;

	@Column(name="NM")
	String name;

	@Type(type="yes_no")
	@Column(name="ACTV_IND")
	boolean active = true;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="NMSPC_CD", insertable=false, updatable=false)
	NamespaceBo namespace;

    @Override
    String getComponentSetId() {
        return null;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static Component to(ComponentBo bo) {
        if (bo == null) {
            return null
        }

        return Component.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static ComponentBo from(Component im) {
        if (im == null) {
            return null
        }

        ComponentBo bo = new ComponentBo()
        bo.code = im.code
        bo.name = im.name
        bo.active = im.active
        bo.namespaceCode = im.namespaceCode
		bo.versionNumber = im.versionNumber
		bo.objectId = im.objectId

        bo.namespace = NamespaceBo.from(namespaceService.getNamespace(bo.namespaceCode))
        return bo;
    }

    public static void setNamespaceService(NamespaceService namespaceService) {
        this.namespaceService = namespaceService
    }
}

