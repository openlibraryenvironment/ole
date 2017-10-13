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
package org.kuali.rice.krms.impl.repository

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttributeContract
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable

public class KrmsTypeAttributeBo extends PersistableBusinessObjectBase implements MutableInactivatable, KrmsTypeAttributeContract {

	def String id
	def String typeId
	def String attributeDefinitionId
	def Integer sequenceNumber
	def boolean active = true;

	/**
	 * Converts a mutable bo to it's immutable counterpart
	 * @param bo the mutable business object
	 * @return the immutable object
	 */
	static KrmsTypeAttribute to(KrmsTypeAttributeBo bo) {
		if (bo == null) { return null }
		return org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute.Builder
			.create(bo).build()
	}

	/**
	 * Converts a immutable object to it's mutable bo counterpart
	 * @param im immutable object
	 * @return the mutable bo
	 */
	static KrmsTypeAttributeBo from(KrmsTypeAttribute im) {
		if (im == null) {
            return null
        }
		KrmsTypeAttributeBo bo = new KrmsTypeAttributeBo()
		bo.id = im.id
		bo.typeId = im.typeId
		bo.attributeDefinitionId = im.attributeDefinitionId
		bo.sequenceNumber = im.sequenceNumber
		bo.active = im.active
        bo.versionNumber = im.versionNumber
		return bo
	}
	
}