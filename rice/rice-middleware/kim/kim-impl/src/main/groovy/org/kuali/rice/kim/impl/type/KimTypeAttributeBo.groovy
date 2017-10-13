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

import org.kuali.rice.kim.api.type.KimTypeAttribute
import org.kuali.rice.kim.api.type.KimTypeAttributeContract
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

class KimTypeAttributeBo extends PersistableBusinessObjectBase implements KimTypeAttributeContract {
    String id
    String sortCode
    String kimAttributeId
    KimAttributeBo kimAttribute
    String kimTypeId
    boolean active

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static KimTypeAttribute to(KimTypeAttributeBo bo) {
        if (bo == null) {
            return null
        }

        return KimTypeAttribute.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static KimTypeAttributeBo from(KimTypeAttribute im) {
        if (im == null) {
            return null
        }

        KimTypeAttributeBo bo = new KimTypeAttributeBo()
        bo.id = im.id
        bo.sortCode = im.sortCode
        bo.kimAttributeId = im.kimAttribute?.id
        bo.kimAttribute = KimAttributeBo.from(im.kimAttribute)
        bo.kimTypeId = im.kimTypeId
        bo.active = im.active
        bo.versionNumber = im.versionNumber
        bo.objectId = im.objectId

        return bo
    }

    KimAttributeBo getKimAttribute() {
        return kimAttribute;
    }
}
