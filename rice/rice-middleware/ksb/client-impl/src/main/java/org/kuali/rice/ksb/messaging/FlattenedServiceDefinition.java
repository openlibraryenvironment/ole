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
package org.kuali.rice.ksb.messaging;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.ksb.service.KSBServiceLocator;

/**
 * A convenience class for encapsulating the serialized version of a ServiceDefinition object, allowing it to be lazy-loaded.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KRSB_FLT_SVC_DEF_T")
//@Sequence(name="KRSB_FLT_SVC_DEF_S", property="flattenedServiceDefinitionId")
public class FlattenedServiceDefinition implements Serializable {
	private static final long serialVersionUID = -4622179363250818637L;
	
	@Id
	@GeneratedValue(generator="KRSB_FLT_SVC_DEF_S")
	@GenericGenerator(name="KRSB_FLT_SVC_DEF_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRSB_FLT_SVC_DEF_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="FLT_SVC_DEF_ID")  
	private Long flattenedServiceDefinitionId;
	@Lob
	@Column(name="FLT_SVC_DEF", length=4000)
	private String flattenedServiceDefinitionData;
	
	//@PrePersist
    public void beforeInsert() {
        OrmUtils.populateAutoIncValue(this, KSBServiceLocator.getRegistryEntityManagerFactory().createEntityManager());
    }
	
	public FlattenedServiceDefinition() {
	}
	
	public FlattenedServiceDefinition(String flattenedServiceDefinitionData) {
		this.flattenedServiceDefinitionData = flattenedServiceDefinitionData;
	}
	
	public Long getFlattenedServiceDefinitionId() {
		return this.flattenedServiceDefinitionId;
	}
	public void setFlattenedServiceDefinitionId(Long flattenedServiceDefinitionId) {
		this.flattenedServiceDefinitionId = flattenedServiceDefinitionId;
	}
	
	public String getFlattenedServiceDefinitionData() {
		return this.flattenedServiceDefinitionData;
	}
	public void setFlattenedServiceDefinitionData(String flattenedServiceDefinitionData) {
		this.flattenedServiceDefinitionData = flattenedServiceDefinitionData;
	}
	
}
