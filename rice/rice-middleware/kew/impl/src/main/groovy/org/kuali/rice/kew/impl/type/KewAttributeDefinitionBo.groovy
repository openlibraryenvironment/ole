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
package org.kuali.rice.kew.impl.type

import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinitionContract
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

public class KewAttributeDefinitionBo extends PersistableBusinessObjectBase implements KewAttributeDefinitionContract, MutableInactivatable{

	def String id
	def String name
	def String namespace
	def String label
    def String description
	def boolean active
	def String componentName   
    
    /**
    * Converts a mutable bo to it's immutable counterpart
    * @param bo the mutable business object
    * @return the immutable object
    */
   static KewAttributeDefinition to(KewAttributeDefinitionBo bo) {
       if (bo == null) { return null }
       return org.kuali.rice.kew.api.repository.type.KewAttributeDefinition.Builder.create(bo).build()
   }

   /**
    * Converts a immutable object to it's mutable bo counterpart
    * @param im immutable object
    * @return the mutable bo
    */
   static KewAttributeDefinitionBo from(KewAttributeDefinition im) {
       if (im == null) { return null }

       KewAttributeDefinitionBo bo = new KewAttributeDefinitionBo()
       bo.id = im.id
       bo.name = im.name
       bo.namespace = im.namespace
       bo.label = im.label
       bo.description = im.description
       bo.active = im.active
       bo.componentName = im.componentName
       bo.versionNumber = im.versionNumber
       return bo
   }
 
 
} 