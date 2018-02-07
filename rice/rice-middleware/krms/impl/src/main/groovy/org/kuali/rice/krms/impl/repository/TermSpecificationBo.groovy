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
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract
import javax.persistence.Transient

public class TermSpecificationBo extends PersistableBusinessObjectBase implements TermSpecificationDefinitionContract {
	
	private static final long serialVersionUID = 1L;

	def String id
	def String name
    def String namespace
	def String type
    def String description
    def boolean active = true

    def List<CategoryBo> categories = new ArrayList<CategoryBo>()
    def List<String> contextIds = new ArrayList<String>()

    /**
     * This field may require manual population based on the {@link #contextIds} list.
     */
    @Transient
    def transient List<ContextBo> contexts = new ArrayList<ContextBo>()

	/**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
   static TermSpecificationDefinition to(TermSpecificationBo bo) {
	   if (bo == null) { return null }
	   return org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition.Builder.create(bo).build()
   }

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
   static TermSpecificationBo from(TermSpecificationDefinition im) {
	   if (im == null) { return null }

	   TermSpecificationBo bo = new TermSpecificationBo()
	   bo.id = im.id
	   bo.namespace = im.namespace
	   bo.name = im.name
	   bo.type = im.type
       bo.description = im.description
       bo.categories = new ArrayList<CategoryBo>()
       for (category in im.categories) {
           bo.categories.add(CategoryBo.from(category))
       }

       bo.contextIds.addAll(im.contextIds);

       bo.active = im.active
	   bo.versionNumber = im.versionNumber
	   
	   return bo
   }
 
}