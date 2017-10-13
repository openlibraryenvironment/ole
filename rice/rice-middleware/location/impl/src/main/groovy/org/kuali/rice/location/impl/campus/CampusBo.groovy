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
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

import org.hibernate.annotations.Type
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.location.framework.campus.CampusEbo

@Entity
@Table(name="KRLC_CMP_T")
public class CampusBo extends PersistableBusinessObjectBase implements CampusEbo {
	private static final long serialVersionUID = 787567094298971223L;
	@Id
	@Column(name="CAMPUS_CD")
	def String code;
	
	@Column(name="CAMPUS_NM")
	def String name;
	
	@Column(name="CAMPUS_SHRT_NM")
	def String shortName;
	
	@Column(name="CAMPUS_TYP_CD")
	def String campusTypeCode;
	
	@Type(type="yes_no")
	@Column(name="ACTV_IND")
	def boolean active; 

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="CAMPUS_TYP_CD", insertable=false, updatable=false)
	def CampusTypeBo campusType;
	
	/**
	* Converts a mutable bo to its immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
   static org.kuali.rice.location.api.campus.Campus to(CampusBo bo) {
	   if (bo == null) { return null }
	   return org.kuali.rice.location.api.campus.Campus.Builder.create(bo).build();
   }

   /**
	* Converts a immutable object to its mutable counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
   static CampusBo from(org.kuali.rice.location.api.campus.Campus im) {
	   if (im == null) { return null }

	   CampusBo bo = new CampusBo()
	   bo.code = im.code
	   bo.name = im.name
	   bo.shortName = im.shortName
	   bo.active = im.active
	   if (im.campusType != null) {
		   bo.campusTypeCode = im.campusType.code
	   }
	   bo.campusType = CampusTypeBo.from(im.campusType)
       bo.versionNumber = im.versionNumber
	   bo.objectId = im.objectId

	   return bo
   }
   
   @Override
   CampusTypeBo getCampusType() {
	   return campusType
   }
} 