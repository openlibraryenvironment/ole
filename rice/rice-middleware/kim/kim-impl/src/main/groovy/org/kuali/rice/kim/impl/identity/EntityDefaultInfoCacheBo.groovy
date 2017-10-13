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
package org.kuali.rice.kim.impl.identity

import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation
import org.kuali.rice.kim.api.identity.employment.EntityEmployment
import org.kuali.rice.kim.api.identity.entity.EntityDefault
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier
import org.kuali.rice.kim.api.identity.name.EntityName
import org.kuali.rice.kim.api.identity.principal.Principal
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

/**
 * Used to store a cache of person information to be used if the user's information disappears from KIM.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Entity
@Table(name="KRIM_ENTITY_CACHE_T")
class EntityDefaultInfoCacheBo extends PersistableBusinessObjectBase {

	private static final long serialVersionUID = 1L;
    private static final String UNAVAILABLE = "Unavailable";

    @Transient
	Long versionNumber; // prevent JPA from attempting to persist the version number attribute

	// principal data
	@Id
	@Column(name="PRNCPL_ID")
	String principalId;
	@Column(name="PRNCPL_NM")
	String principalName;
	@Column(name="ENTITY_ID")
	String entityId;
	@Column(name="ENTITY_TYP_CD")
	String entityTypeCode;

	// name data
	@Column(name="FIRST_NM")
	String firstName = "";
	@Column(name="MIDDLE_NM")
	String middleName = "";
	@Column(name="LAST_NM")
	String lastName = "";
	@Column(name="PRSN_NM")
	String name = "";

	@Column(name="CAMPUS_CD")
	String campusCode = "";

	// employment data
	@Column(name="PRMRY_DEPT_CD")
	String primaryDepartmentCode = "";
	@Column(name="EMP_ID")
	String employeeId = "";

	@Column(name="LAST_UPDT_TS")
	Timestamp lastUpdateTimestamp;

	EntityDefaultInfoCacheBo() {
	}

	EntityDefaultInfoCacheBo(EntityDefault entity) {
		if ( entity != null ) {
			entityId = entity.getEntityId();
			if ( entity.getPrincipals() != null && !entity.getPrincipals().isEmpty() ) {
				principalId = entity.getPrincipals().get(0).getPrincipalId();
                if(entity.getPrincipals().get(0).getPrincipalName() != null) {
                    principalName = entity.getPrincipals().get(0).getPrincipalName();
                } else {
                    principalName = UNAVAILABLE;
                }
			}
			if ( entity.getEntityTypeContactInfos() != null && !entity.getEntityTypeContactInfos().isEmpty() ) {
				entityTypeCode = entity.getEntityTypeContactInfos().get(0).getEntityTypeCode();
			}
			if ( entity.getName() != null ) {
				firstName = entity.getName().getFirstNameUnmasked();
				middleName = entity.getName().getMiddleNameUnmasked();
				lastName = entity.getName().getLastNameUnmasked();
				name = entity.getName().getCompositeNameUnmasked();
			}
			if ( entity.getDefaultAffiliation() != null ) {
				campusCode = entity.getDefaultAffiliation().getCampusCode();
			}
			if ( entity.getEmployment() != null ) {
				primaryDepartmentCode = entity.getEmployment().getPrimaryDepartmentCode();
				employeeId = entity.getEmployment().getEmployeeId();
			}
		}
	}

    @SuppressWarnings("unchecked")
	EntityDefault convertCacheToEntityDefaultInfo() {
		EntityDefault.Builder info = EntityDefault.Builder.create(this.entityId);

		// identity info
		info.setActive( this.isActive() );

		// principal info
        Principal.Builder principalInfo = null;
        if(this.getPrincipalName() != null) {
            principalInfo = Principal.Builder.create(this.getPrincipalName());
        } else {
            principalInfo = Principal.Builder.create(UNAVAILABLE);
        }
		principalInfo.setEntityId(this.getEntityId());
		principalInfo.setPrincipalId(this.getPrincipalId());
		principalInfo.setActive(this.isActive());
		info.setPrincipals(Collections.singletonList(principalInfo));

		// name info
		EntityName.Builder nameInfo = EntityName.Builder.create();
        nameInfo.setEntityId( this.getEntityId());
		nameInfo.setFirstName( this.getFirstName() );
		nameInfo.setLastName( this.getLastName() );
		nameInfo.setMiddleName( this.getMiddleName() );
		info.setName(nameInfo);

		// identity type information
		EntityTypeContactInfoDefault.Builder entityTypeInfo = EntityTypeContactInfoDefault.Builder.create();
        entityTypeInfo.setEntityTypeCode(this.getEntityTypeCode());
		info.setEntityTypeContactInfos(Collections.singletonList(entityTypeInfo));

		// affiliations
		EntityAffiliation.Builder aff = EntityAffiliation.Builder.create();
		aff.setCampusCode(this.getCampusCode());
		aff.setDefaultValue(true);
        aff.setEntityId(info.getEntityId());
		info.setDefaultAffiliation(aff);
		info.setAffiliations(Collections.singletonList(aff));

		// employment information
		EntityEmployment.Builder empInfo = EntityEmployment.Builder.create();
		empInfo.setEmployeeId( this.getEmployeeId() );
		empInfo.setPrimary(true);
		empInfo.setPrimaryDepartmentCode(this.getPrimaryDepartmentCode());
		info.setEmployment(empInfo);

		// external identifiers
		info.setExternalIdentifiers( Collections.singletonList(EntityExternalIdentifier.Builder.create()) );
		return info.build();

    }

	// handle automatic updating of the timestamp

	@Override
    protected void prePersist() {
    	super.prePersist();
        lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
    }

	@Override
    protected void preUpdate() {
    	super.preUpdate();
        lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
    }

    boolean isActive() {
		return false;
	}

}
