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

import org.apache.commons.lang.StringUtils;


import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;


public class TermResolverParameterSpecificationBo extends PersistableBusinessObjectBase {

	def String id
	def String termResolverId
	def String name
		
	/**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
   static String to(TermResolverParameterSpecificationBo bo) {
	   if (bo == null) { return null }
	   return bo.name;
   }

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
   static TermResolverParameterSpecificationBo from(TermResolverDefinition resolver, String name) {
	   if (resolver == null) { return null }
	   if (StringUtils.isBlank(name)) { return null }

	   TermResolverParameterSpecificationBo bo = new TermResolverParameterSpecificationBo()
	   bo.termResolverId = resolver.id;
	   bo.name = name;

	   return bo
   }
 
} 