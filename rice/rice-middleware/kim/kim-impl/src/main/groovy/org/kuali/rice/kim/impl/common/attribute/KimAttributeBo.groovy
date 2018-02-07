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
package org.kuali.rice.kim.impl.common.attribute

import org.kuali.rice.kim.api.common.attribute.KimAttribute
import org.kuali.rice.kim.api.common.attribute.KimAttributeContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

/**
 * The column names have been used in a native query in RoleDaoOjb and will need to be modified if any changes to the
 * column names are made here.
 */
class KimAttributeBo extends PersistableBusinessObjectBase implements KimAttributeContract {
    private static final long serialVersionUID = 1L;

    String id
    String componentName
    String attributeName
    String namespaceCode
    String attributeLabel
    boolean active

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static KimAttribute to(KimAttributeBo bo) {
        if (bo == null) {
            return null
        }

        return KimAttribute.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static KimAttributeBo from(KimAttribute im) {
        if (im == null) {
            return null
        }

        KimAttributeBo bo = new KimAttributeBo()
        bo.id = im.id
        bo.componentName = im.componentName
        bo.attributeName = im.attributeName
        bo.namespaceCode = im.namespaceCode
        bo.attributeLabel = im.attributeLabel
        bo.active = im.active
        bo.versionNumber = im.versionNumber
        bo.objectId = im.objectId

        return bo
    }
}
