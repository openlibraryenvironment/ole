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
import org.kuali.rice.ken.api.notification.NotificationListRecipient;
import org.kuali.rice.ken.api.notification.NotificationListRecipientContract;
import org.kuali.rice.ken.api.notification.NotificationProducer;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;

/**
 * This class represents the data structure that will house a default recipient list for a notification channel.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_RECIP_LIST_T")
public class NotificationRecipientListBo extends PersistableBusinessObjectBase implements NotificationListRecipientContract {
    @Id
    @GeneratedValue(generator="KREN_RECIP_LIST_S")
	@GenericGenerator(name="KREN_RECIP_LIST_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_RECIP_LIST_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RECIP_LIST_ID")
	private Long id;
    @Column(name="RECIP_TYP_CD", nullable=false)
	private String recipientType;
    @Column(name="RECIP_ID", nullable=false)
	private String recipientId;
    
    @ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name="CHNL_ID", insertable=false, updatable=false)
	private NotificationChannelBo channel;
    
    /**
     * Constructs a NotificationRecipientList.java instance.
     */
    public NotificationRecipientListBo() {
    }

    /**
     * Gets the channel attribute. 
     * @return Returns the channel.
     */
    public NotificationChannelBo getChannel() {
        return channel;
    }


    /**
     * Sets the channel attribute value.
     * @param channel The channel to set.
     */
    public void setChannel(NotificationChannelBo channel) {
        this.channel = channel;
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
     * Gets the recipientId attribute. 
     * @return Returns the recipientId.
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Sets the recipientId attribute value.
     * @param recipientId The recipientId to set.
     */
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * Gets the recipientType attribute. 
     * @return Returns the recipientType.
     */
    public String getRecipientType() {
        return recipientType;
    }

    /**
     * Sets the recipientType attribute value.
     * @param recipientType The recipientType to set.
     */
    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static NotificationListRecipient to(NotificationRecipientListBo bo) {
        if (bo == null) {
            return null;
        }

        return NotificationListRecipient.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationRecipientListBo from(NotificationListRecipient im) {
        if (im == null) {
            return null;
        }

        NotificationRecipientListBo bo = new NotificationRecipientListBo();
        bo.setId(im.getId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());

        bo.setRecipientType(im.getRecipientType());
        bo.setRecipientId(im.getRecipientId());

        bo.setChannel(im.getChannel() == null ? null : NotificationChannelBo.from(im.getChannel()));
        return bo;
    }
}

