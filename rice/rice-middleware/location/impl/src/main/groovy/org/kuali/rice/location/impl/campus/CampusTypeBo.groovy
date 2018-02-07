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
package org.kuali.rice.location.impl.campus

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

import org.hibernate.annotations.Type
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.location.api.campus.CampusType
import org.kuali.rice.location.framework.campus.CampusTypeEbo

@Entity
@Table(name="KRLC_CMP_TYP_T")
public class CampusTypeBo extends PersistableBusinessObjectBase implements CampusTypeEbo {
	@Id
	@Column(name="CAMPUS_TYP_CD")
	def String code;
	
	@Column(name="CMP_TYP_NM")
	def String name;
	
	@Type(type="yes_no")
	@Column(name="ACTV_IND")
	def boolean active;
	
	/**
	* Converts a mutable CountryBo to an immutable Country representation.
	* @param bo
	* @return an immutable Country
	*/
   static CampusType to(CampusTypeBo bo) {
	 if (bo == null) { return null }
	 return CampusType.Builder.create(bo).build()
   }
 
   /**
	* Creates a CountryBo business object from an immutable representation of a Country.
	* @param an immutable Country
	* @return a CountryBo
	*/
   static CampusTypeBo from(CampusType im) {
	 if (im == null) {return null}
 
	 CampusTypeBo bo = new CampusTypeBo()
	 bo.code = im.code
	 bo.name = im.name
	 bo.active = im.active
     bo.versionNumber = im.versionNumber
	 bo.objectId = im.objectId
 
	 return bo;
   }
}