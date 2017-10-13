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
package org.kuali.rice.kcb.bo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents an instance of a MessageDelivery.  A Message gets delivered to 
 * recipients, possibly in various ways.  For each delivery type that a recipient gets sent to them, 
 * they have an instance of this entity.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_MSG_DELIV_T")
public class MessageDelivery extends BaseLockable {
    private static final Integer ZERO = Integer.valueOf(0);

    /**
     * Field names
     */
    public static final String ID_FIELD = "id";
    public static final String SYSTEMID_FIELD = "delivererSystemId";
    public static final String MESSAGEID_FIELD = "message";
    public static final String DELIVERY_STATUS = "deliveryStatus";
    public static final String PROCESS_COUNT = "processCount";
    
    @Id
	@Column(name="MSG_DELIV_ID")
	private Long id;
    @Column(name="TYP_NM", nullable=false)
	private String delivererTypeName;
    @Column(name="SYS_ID", nullable=true)
	private String delivererSystemId;  // can hold an identifier from the endpoint deliverer mechanism system (i.e. workflow id, SMS id, etc)
    @Column(name="STAT_CD", nullable=true)
    private String deliveryStatus = MessageDeliveryStatus.UNDELIVERED.name();
    @Column(name="PROC_CNT", nullable=true)
    private Integer processCount = ZERO;

    /**
     * This delivery's message
     */
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinColumn(name="MSG_ID")
	private Message message;

    /**
     * Lock column for OJB optimistic locking
     */
    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;
    
    /**
     * Constructs a MessageDelivery instance.
     */
    public MessageDelivery() {
    }

    /**
     * Shallow-copy constructor
     * @param md MessageDelivery to (shallow) copy
     */
    public MessageDelivery(MessageDelivery md) {
        this.id = md.id;
        this.delivererTypeName = md.delivererTypeName;
        this.deliveryStatus = md.deliveryStatus;
        this.delivererSystemId = md.delivererSystemId;
        this.message = md.message;
        this.lockVerNbr = md.lockVerNbr;
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
    public Integer getLockVerNbr() {
        return lockVerNbr;
    }

    /**
     * Set value of lock column for OJB optimistic locking
     * @param lockVerNbr value of lock column for OJB optimistic locking
     */
    public void setLockVerNbr(Integer lockVerNbr) {
        this.lockVerNbr = lockVerNbr;
    }

    /**
     * Gets the deliveryStatus attribute. 
     * @return Returns the deliveryStatus.
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * Convenience method that sets the delivery status in a typesafe manner.
     * This method is preferred to {@link #setDeliveryStatus(String)}
     * @param deliveryStatus the MessageDeliveryStatus enum constant
     */
    public void setDeliveryStatus(MessageDeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus.name();
    }

    /**
     * Sets the deliveryStatus attribute value.
     * @param deliveryStatus The deliveryStatus to set.
     */
    public void setDeliveryStatus(String deliveryStatus) {
        // Enums will throw an IllegalArgumentException from valueOf if there
        // is no matching enum
        MessageDeliveryStatus.valueOf(deliveryStatus);
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * @return the number of times processing has been attempted for this message
     */
    public Integer getProcessCount() {
        return this.processCount;
    }

    /**
     * Sets the number of times processing has been attempted for this message
     * @param processCount the number of times processing has been attempted for this message
     */
    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }

    /**
     * Gets the delivererTypeName attribute. 
     * @return Returns the delivererTypeName.
     */
    public String getDelivererTypeName() {
        return delivererTypeName;
    }

    /**
     * Sets the delivererTypeName attribute value.
     * @param delivererTypeName The delivererTypeName to set.
     */
    public void setDelivererTypeName(String delivererTypeName) {
        this.delivererTypeName = delivererTypeName;
    }

    /**
     * Gets the delivererSystemId attribute. 
     * @return Returns the delivererSystemId.
     */
    public String getDelivererSystemId() {
        return delivererSystemId;
    }

    /**
     * Sets the delivererSystemId attribute value.
     * @param delivererSystemId The delivererSystemId to set.
     */
    public void setDelivererSystemId(String delivererSystemId) {
        this.delivererSystemId = delivererSystemId;
    }

    /**
     * @return this delivery's message
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * Sets this delivery's message
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                       .append("id", id)
                       .append("deliveryStatus", deliveryStatus)
                       .append("processCount", processCount)
                       .append("delivererTypename", delivererTypeName)
                       .append("delivererSystemId", delivererSystemId)
                       .append("message", message == null ? null : message.getId())
                       .toString();
    }
}
