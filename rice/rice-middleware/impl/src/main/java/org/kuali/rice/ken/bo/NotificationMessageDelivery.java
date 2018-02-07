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
package org.kuali.rice.ken.bo;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * This class represents and instance of a NotificationMessageDelivery.  A Notification gets delivered to 
 * recipients, possibly in various ways.  For each delivery type that a recipient gets sent to them, 
 * they have an instance of this entity.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_NTFCTN_MSG_DELIV_T")
public class NotificationMessageDelivery extends PersistableBusinessObjectBase implements Lockable {
    @Id
    @GeneratedValue(generator="KREN_NTFCTN_MSG_DELIV_S")
	@GenericGenerator(name="KREN_NTFCTN_MSG_DELIV_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_NTFCTN_MSG_DELIV_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="NTFCTN_MSG_DELIV_ID")
	private Long id;
    @Column(name="STAT_CD", nullable=false)
	private String messageDeliveryStatus;
    @Column(name="RECIP_ID", nullable=false)
	private String userRecipientId;
    @Column(name="SYS_ID", nullable=true)
	private String deliverySystemId;  // can hold an identifier from the endpoint delivery mechanism system (i.e. workflow id, SMS id, etc)
	@Column(name="LOCKD_DTTM", nullable=true)
	private Timestamp lockedDateValue;

    /**
     * Lock column for OJB optimistic locking
     */
//    @Version
//	@Column(name="VER_NBR")
//	private Integer lockVerNbr;
    
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="NTFCTN_ID")
	private NotificationBo notification;

    /**
     * Constructs a NotificationMessageDelivery instance.
     */
    public NotificationMessageDelivery() {
    }
    
    /**
     * Gets the id attribute. 
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id attribute value.
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Return value of lock column for OJB optimistic locking
     * @return value of lock column for OJB optimistic locking
     */
    //public Integer getLockVerNbr() {
    //    return lockVerNbr;
    	//return Integer.valueOf(super.getVersionNumber().intValue());
    //}

    /**
     * Set value of lock column for OJB optimistic locking
     * @param lockVerNbr value of lock column for OJB optimistic locking
     */
    //public void setLockVerNbr(Integer lockVerNbr) {
    //    this.lockVerNbr = lockVerNbr;
    //	//super.setVersionNumber(lockVerNbr.longValue());
    //}

    /**
     * Gets the messageDeliveryStatus attribute. 
     * @return Returns the messageDeliveryStatus.
     */
    public String getMessageDeliveryStatus() {
        return messageDeliveryStatus;
    }

    /**
     * Sets the messageDeliveryStatus attribute value.
     * @param deliveryStatus The messageDeliveryStatus to set.
     */
    public void setMessageDeliveryStatus(String deliveryStatus) {
        this.messageDeliveryStatus = deliveryStatus;
    }

    /**
     * Gets the userRecipientId attribute. 
     * @return Returns the userRecipientId.
     */
    public String getUserRecipientId() {
        return userRecipientId;
    }

    /**
     * Sets the userRecipientId attribute value.
     * @param userRecipientId The userRecipientId to set.
     */
    public void setUserRecipientId(String userRecipientId) {
        this.userRecipientId = userRecipientId;
    }

    /**
     * Gets the lockedDate attribute. 
     * @return Returns the lockedDate.
     */
    public Timestamp getLockedDateValue() {
        return this.lockedDateValue;
    }
    
    /**
     * Sets the lockedDate attribute value.
     * @param lockedDateValue The lockedDate to set.
     */
    public void setLockedDateValue(Timestamp lockedDateValue) {
        this.lockedDateValue = lockedDateValue;
    }

    /**
     * Gets the notification attribute. 
     * @return Returns the notification.
     */
    public NotificationBo getNotification() {
        return notification;
    }

    /**
     * Sets the notification attribute value.
     * @param notification The notification to set.
     */
    public void setNotification(NotificationBo notification) {
        this.notification = notification;
    }

    /**
     * Gets the deliverySystemId attribute. 
     * @return Returns the deliverySystemId.
     */
    public String getDeliverySystemId() {
        return deliverySystemId;
    }

    /**
     * Sets the deliverySystemId attribute value.
     * @param deliverySystemId The deliverySystemId to set.
     */
    public void setDeliverySystemId(String deliverySystemId) {
        this.deliverySystemId = deliverySystemId;
    }
}
