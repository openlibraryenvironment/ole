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
package org.kuali.rice.krad.maintenance;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.annotations.Sequence;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * List of business objects that this maintenance document is locking (prevents two documents from being routed trying to update the same object)
 * Most maintenance documents have only one lock, but globals have many
 */
@Entity
@Sequence(name="KRNS_MAINT_LOCK_S",property="lockId")
@Table(name="KRNS_MAINT_LOCK_T")
public class MaintenanceLock extends PersistableBusinessObjectBase {
    private static final long serialVersionUID = 7766326835852387301L;
	@Id
    @Column(name="MAINT_LOCK_ID")
    private String lockId;
	@Column(name="MAINT_LOCK_REP_TXT")
	private String lockingRepresentation;
    @Column(name="DOC_HDR_ID")
	private String documentNumber;

    public String getLockId() {
		return this.lockId;
	}

	public void setLockId(String lockId) {
		this.lockId = lockId;
	}

	public String getLockingRepresentation() {
        return lockingRepresentation;
    }

    public void setLockingRepresentation(String lockingRepresentation) {
        this.lockingRepresentation = lockingRepresentation;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

	/**
	 * Uses OrmUtils to set the sequence
     *
     * @see org.kuali.rice.krad.bo.PersistableBusinessObjectBase#prePersist()
     */
    @PrePersist
	protected void customPrePersist() {
		final EntityManagerFactory factory = KRADServiceLocator.getApplicationEntityManagerFactory();
		OrmUtils.populateAutoIncValue(this, factory.createEntityManager());

		super.prePersist();
	}

}

