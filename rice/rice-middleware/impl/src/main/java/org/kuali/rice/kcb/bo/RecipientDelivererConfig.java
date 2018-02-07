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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents the enablement of a deliverer for a particular channel for a particular user.
 * Each RecipientDelivererConfig instance represents a user as having applied a deliverer type configuration
 * to a channel, such that any messages, targeted at the userId, will also be delivered to the correlating
 * deliverer type (delivererName) for that user.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_RECIP_DELIV_T")
public class RecipientDelivererConfig {
    /**
     * Field names for queries
     */
    public static final String RECIPIENT_ID = "recipientId";
    public static final String CHANNEL = "channel";
    
    @Id
	@Column(name="RECIP_DELIV_ID")
	private Long id;
    @Column(name="RECIP_ID", nullable=false)
	private String recipientId;
    @Column(name="NM", nullable=false)
	private String delivererName;
    @Column(name="CHNL", nullable=false)
	private String channel;
    /**
     * Lock column for OJB optimistic locking
     */
    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;

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
     * Gets the recipientId attribute. 
     * @return Returns the recipientId.
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Sets the recipientId attribute value.
     * @param recipientId The userId to set.
     */
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * Gets the delivererName attribute. 
     * @return Returns the name.
     */
    public String getDelivererName() {
        return delivererName;
    }

    /**
     * Sets the delivererName attribute value.
     * @param delivererName The delivererName to set.
     */
    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    /**
     * Gets the channels attribute. 
     * @return Returns the channel.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the channel attribute value.
     * @param channel The channel to set.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
       return new ToStringBuilder(this).append("id", id)
                                       .append("recipientId", recipientId)
                                       .append("delivererName", delivererName)
                                       .append("channel", channel)
                                       .toString();
    }
}
