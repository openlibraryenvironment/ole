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
package org.kuali.rice.kew.impl.peopleflow

import org.kuali.rice.kew.impl.type.KewAttributeDefinitionBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition

class PeopleFlowAttributeBo extends PersistableBusinessObjectBase {

    String id
	String attributeDefinitionId
	String value
    String peopleFlowId

	def KewAttributeDefinitionBo attributeDefinition

    public void setAttributeDefinition(KewAttributeDefinitionBo attrDef) {
        if (attrDef != null) {
            this.attributeDefinitionId = attrDef.getId();
        } else {
            this.attributeDefinitionId = null;
        }
        this.attributeDefinition = attrDef;
    }

    public static PeopleFlowAttributeBo from(KewAttributeDefinition attributeDefinition, String id, String peopleFlowId, String value) {
        if (attributeDefinition == null) {
            return null;
        }
        PeopleFlowAttributeBo peopleFlowAttributeBo = new PeopleFlowAttributeBo();
        peopleFlowAttributeBo.setId(id);
        peopleFlowAttributeBo.setPeopleFlowId(peopleFlowId);
        peopleFlowAttributeBo.setValue(value);
        peopleFlowAttributeBo.setAttributeDefinition(KewAttributeDefinitionBo.from(attributeDefinition));
        peopleFlowAttributeBo.setAttributeDefinitionId(attributeDefinition.getId());
        return peopleFlowAttributeBo;
    }
    
}
