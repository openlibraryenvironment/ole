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
package org.kuali.rice.kim.impl.type

import org.kuali.rice.kim.api.type.KimType
import org.kuali.rice.kim.api.type.KimTypeContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

class KimTypeBo extends PersistableBusinessObjectBase implements KimTypeContract {
    String id
    String serviceName
    String namespaceCode
    String name
    List<KimTypeAttributeBo> attributeDefinitions
    boolean active

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static KimType to(KimTypeBo bo) {
        if (bo == null) {
            return null
        }

        return KimType.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static KimTypeBo from(KimType im) {
        if (im == null) {
            return null
        }

        KimTypeBo bo = new KimTypeBo()
        bo.id = im.id
        bo.serviceName = im.serviceName
        bo.namespaceCode = im.namespaceCode
        bo.name = im.name
        bo.attributeDefinitions = im.attributeDefinitions != null ? im.attributeDefinitions.collect { KimTypeAttributeBo.from(it) } : new ArrayList()
        bo.active = im.active
        bo.versionNumber = im.versionNumber
        bo.objectId = im.objectId

        return bo
    }
}
