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
package org.kuali.rice.krad.document.authorization;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * This is a business object used to lock a document pessimistically.
 * Pessimistic locking is more strick than optimistic locking and assumes if a
 * lock exists that a user should only have read-only access to a document. For
 * more information see documentation pages.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@Entity
@Table(name="KRNS_PESSIMISTIC_LOCK_T")
public class PessimisticLock extends PersistableBusinessObjectBase {
    
    private static final long serialVersionUID = -5210762282545093555L;
    
    public static final String DEFAULT_LOCK_DESCRIPTOR = null;
    
    // id is sequence number and primary key
    @Id
    @GeneratedValue(generator="KRNS_LOCK_S")
	@GenericGenerator(name="KRNS_LOCK_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KRNS_LOCK_S"),
			@Parameter(name="value_column",value="id")
	})
    @Column(name="PESSIMISTIC_LOCK_ID")
    private Long id;
    
    @Column(name="PRNCPL_ID")
    private String ownedByPrincipalIdentifier;
    
    @Column(name="LOCK_DESC_TXT")
    private String lockDescriptor; // this will be defaulted to the value of DEFAULT_LOCK_DESCRIPTOR constant above
    
    @Column(name="GNRT_DT")
    private Timestamp generatedTimestamp;
    
    @Column(name="DOC_HDR_ID")
    private String documentNumber; // foreign key to document

    @Column(name="SESN_ID")
    private String sessionId;
    
    @Transient
    private Person ownedByUser;

    
    /**
     * This constructs an empty lock using the logged in user and default lock descriptor type
     * but will NOT assign a document number or session id.  Use another constructor.
     * @deprecated
     */
    @Deprecated
    public PessimisticLock() {}
    
    /**
     * This constructs a lock object using the logged in user and given lock type
     */
    public PessimisticLock(String documentNumber, String lockDescriptor, Person user, UserSession userSession) {
        this.documentNumber = documentNumber;
        this.ownedByPrincipalIdentifier = user.getPrincipalId();
        this.lockDescriptor = lockDescriptor;  
        this.generatedTimestamp = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
        this.sessionId = userSession.getKualiSessionId();
    }
    
    public boolean isOwnedByUser(Person user) {
        return user.getPrincipalId().equals(getOwnedByPrincipalIdentifier());
    }
    
    /**
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the ownedByPrincipalIdentifier
     */
    public String getOwnedByPrincipalIdentifier() {
        return this.ownedByPrincipalIdentifier;
    }

    /**
     * @param ownedByPrincipalIdentifier the ownedByPrincipalIdentifier to set
     */
    public void setOwnedByPrincipalIdentifier(String ownedByPrincipalIdentifier) {
        this.ownedByPrincipalIdentifier = ownedByPrincipalIdentifier;
    }

    /**
     * @return the lockDescriptor
     */
    public String getLockDescriptor() {
        return this.lockDescriptor;
    }

    /**
     * @param lockDescriptor the lockDescriptor to set
     */
    public void setLockDescriptor(String lockDescriptor) {
        this.lockDescriptor = lockDescriptor;
    }

    /**
     * @return the generatedTimestamp
     */
    public Timestamp getGeneratedTimestamp() {
        return this.generatedTimestamp;
    }

    /**
     * @param generatedTimestamp the generatedTimestamp to set
     */
    public void setGeneratedTimestamp(Timestamp generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

    /**
     * @return the documentNumber
     */
    public String getDocumentNumber() {
        return this.documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * @return the ownedByUser
     */
    public Person getOwnedByUser() {
        ownedByUser = KimApiServiceLocator.getPersonService().updatePersonIfNecessary(ownedByPrincipalIdentifier, ownedByUser);
        return ownedByUser;
    }

    /**
     * @param ownedByUser the ownedByUser to set
     */
    public void setOwnedByUser(Person ownedByUser) {
        this.ownedByUser = ownedByUser;
    }
}

