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
package org.kuali.rice.kim.api.identity.employment;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract;

/**
 * employment information for a KIM identity
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface EntityEmploymentContract extends Versioned, GloballyUnique, Inactivatable, Identifiable {


    /**
     * Gets this id of the parent identity object.
     * @return the identity id for this {@link org.kuali.rice.kim.api.identity.email.EntityEmailContract}
     */
    String getEntityId();

	/**
     * Gets this {@link EntityEmploymentContract}'s identity affiliation.
     * @return the identity affiliation for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	EntityAffiliationContract getEntityAffiliation();
	
	/**
     * Gets this {@link EntityEmploymentContract}'s employee status.
     * @return the employee status for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	CodedAttributeContract getEmployeeStatus();
	
	/**
     * Gets this {@link EntityEmploymentContract}'s employee type.
     * @return the employee type for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	CodedAttributeContract getEmployeeType();
	
	/**
     * Gets this {@link EntityEmploymentContract}'s primary department code.
     * @return the primary department code for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	String getPrimaryDepartmentCode();
	
	/**
     * Gets this {@link EntityEmploymentContract}'s employee id.
     * @return the employee id for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	String getEmployeeId();
	
	/**
     * Gets this {@link EntityEmploymentContract}'s employment record id.
     * @return the employment record id for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	String getEmploymentRecordId();
	
	/**
     * Gets this {@link EntityEmploymentContract}'s base salary amount.
     * @return the base salary amount for this {@link EntityEmploymentContract}, or null if none has been assigned.
     */
	KualiDecimal getBaseSalaryAmount();

    /**
	 * The primary value for this object.
	 *
	 * @return returns true if the record is the primary Employment record for the parent entity
	 */
	boolean isPrimary();

}
