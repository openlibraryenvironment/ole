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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.kuali.rice.ken.api.notification.Notification;
import org.kuali.rice.ken.api.notification.NotificationContract;
import org.kuali.rice.ken.api.notification.NotificationRecipient;
import org.kuali.rice.ken.api.notification.NotificationRecipientContract;
import org.kuali.rice.ken.api.notification.NotificationSender;
import org.kuali.rice.ken.api.notification.NotificationSenderContract;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an instace of a notification message that is received by the overall 
 * system.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_NTFCTN_T")
public class NotificationBo extends PersistableBusinessObjectBase implements NotificationContract, Lockable {
   
    @Id
    @GeneratedValue(generator="KREN_NTFCTN_S")
	@GenericGenerator(name="KREN_NTFCTN_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_NTFCTN_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="NTFCTN_ID")
	private Long id;
    @Column(name="DELIV_TYP", nullable=false)
	private String deliveryType;
	@Column(name="CRTE_DTTM", nullable=false)
	private Timestamp creationDateTimeValue;
	@Column(name="SND_DTTM", nullable=true)
	private Timestamp sendDateTimeValue;
	@Column(name="AUTO_RMV_DTTM", nullable=true)
	private Timestamp autoRemoveDateTimeValue;
    @Column(name="TTL", nullable=true)
	private String title;
    @Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="CNTNT", nullable=false)
	private String content;
    @Column(name="PROCESSING_FLAG", nullable=false)
	private String processingFlag;
	@Column(name="LOCKD_DTTM", nullable=true)
	private Timestamp lockedDateValue;
    @Column(name = "DOC_TYP_NM", nullable = true)
    private String docTypeName;
    /**
     * Lock column for OJB optimistic locking
     */
//    @Version
//	@Column(name="VER_NBR")
//	private Integer lockVerNbr;
    
    // object references
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="PRIO_ID")
	private NotificationPriorityBo priority;
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="CNTNT_TYP_ID")
	private NotificationContentTypeBo contentType;
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="CHNL_ID")
	private NotificationChannelBo channel;
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name="PRODCR_ID")
	private NotificationProducerBo producer;
    
    // lists
    @OneToMany(cascade={CascadeType.ALL},
           targetEntity=NotificationRecipientBo.class, mappedBy="notification")
    @OrderBy("id ASC")
	private List<NotificationRecipientBo> recipients;
    @OneToMany(cascade={CascadeType.ALL},
           targetEntity=NotificationSenderBo.class, mappedBy="notification")
	@OrderBy("id ASC")
    private List<NotificationSenderBo> senders;
    
    /**
     * Constructs a Notification instance.
     */
    public NotificationBo() {
        recipients = new ArrayList<NotificationRecipientBo>();
        senders = new ArrayList<NotificationSenderBo>();
        processingFlag = NotificationConstants.PROCESSING_FLAGS.UNRESOLVED;
    }

    /**
     * Returns when this Notification entry was created 
     * @return when this Notification entry was created
     */
    public Timestamp getCreationDateTimeValue() {
        return creationDateTimeValue;
    }

    @Override
    public DateTime getCreationDateTime() {
        return this.creationDateTimeValue == null ? null : new DateTime(this.creationDateTimeValue);
    }

    /**
     * Sets the creation date of this Notification entry
     * @param created the creation date of this Notification entry
     */
    public void setCreationDateTimeValue(Timestamp created) {
        this.creationDateTimeValue = created;
    }

    /**
     * Return value of lock column for OJB optimistic locking
     * @return value of lock column for OJB optimistic locking
     */
 // should discard this method and call super directly
    public Integer getLockVerNbr() {
    	return Integer.valueOf(super.getVersionNumber().intValue());
    }

    /**
     * Set value of lock column for OJB optimistic locking
     * @param lockVerNbr value of lock column for OJB optimistic locking
     */
 // should discard this method and call super directly
    public void setLockVerNbr(Integer lockVerNbr) {
    	super.setVersionNumber(lockVerNbr.longValue());
    }

    /**
     * Gets the recipients attribute. 
     * @return Returns the recipients.
     */
    public List<NotificationRecipientBo> getRecipients() {
        return recipients;
    }

    /**
     * Sets the recipients attribute value.
     * @param recipients The recipients to set.
     */
    public void setRecipients(List<NotificationRecipientBo> recipients) {
        this.recipients = recipients;
    }

    /**
     * Retrieves a recipient at the specified index
     * @param index the index in the recipients collection
     * @return the recipient if found or null
     */
    public NotificationRecipientBo getRecipient(int index) {
        return (NotificationRecipientBo) recipients.get(index);
    }
    
    /**
     * Adds a recipient
     * @param recipient The recipient to add
     */
    public void addRecipient(NotificationRecipientBo recipient) {
        recipients.add(recipient);
    }

    /**
     * Gets the senders attribute. 
     * @return Returns the senders.
     */
    public List<NotificationSenderBo> getSenders() {
        return senders;
    }

    /**
     * Sets the senders attribute value.
     * @param senders The senders to set.
     */
    public void setSenders(List<NotificationSenderBo> senders) {
        this.senders = senders;
    }

    /**
     * Retrieves a sender at the specified index
     * @param index the index in the senders collection
     * @return the sender if found or null
     */
    public NotificationSenderBo getSender(int index) {
        return (NotificationSenderBo) senders.get(index);
    }
    /**
     * Adds a sender
     * @param sender The sender to add
     */
    public void addSender(NotificationSenderBo sender) {
        senders.add(sender);
    }

    /**
     * Gets the autoRemoveDateTime attribute. 
     * @return Returns the autoRemoveDateTime.
     */
    public Timestamp getAutoRemoveDateTimeValue() {
	    return this.autoRemoveDateTimeValue;
    }

    @Override
    public DateTime getAutoRemoveDateTime() {
        return this.autoRemoveDateTimeValue == null ? null : new DateTime(this.autoRemoveDateTimeValue);
    }

    /**
     * Sets the autoRemoveDateTime attribute value.
     * @param autoRemoveDateTimeValue The autoRemoveDateTime to set.
     */
    public void setAutoRemoveDateTimeValue(Timestamp autoRemoveDateTimeValue) {
	    this.autoRemoveDateTimeValue = autoRemoveDateTimeValue;
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
     * Gets the content attribute. 
     * @return Returns the content.
     */
    public String getContent() {
	    return content;
    }

    /**
     * Sets the content attribute value.
     * @param content The content to set.
     */
    public void setContent(String content) {
	    this.content = content;
    }

    /**
     * Gets the contentType attribute. 
     * @return Returns the contentType.
     */
    public NotificationContentTypeBo getContentType() {
	    return contentType;
    }

    /**
     * Sets the contentType attribute value.
     * @param contentType The contentType to set.
     */
    public void setContentType(NotificationContentTypeBo contentType) {
	    this.contentType = contentType;
    }

    /**
     * Gets the deliveryType attribute. 
     * @return Returns the deliveryType.
     */
    public String getDeliveryType() {
	    return deliveryType;
    }

    /**
     * Sets the deliveryType attribute value.
     * @param deliveryType The deliveryType to set.
     */
    public void setDeliveryType(String deliveryType) {
	    this.deliveryType = deliveryType.toUpperCase();
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
     * Gets the priority attribute. 
     * @return Returns the priority.
     */
    public NotificationPriorityBo getPriority() {
	    return priority;
    }

    /**
     * Sets the priority attribute value.
     * @param priority The priority to set.
     */
    public void setPriority(NotificationPriorityBo priority) {
	    this.priority = priority;
    }

    /**
     * Gets the producer attribute. 
     * @return Returns the producer.
     */
    public NotificationProducerBo getProducer() {
	    return producer;
    }

    /**
     * Sets the producer attribute value.
     * @param producer The producer to set.
     */
    public void setProducer(NotificationProducerBo producer) {
	    this.producer = producer;
    }

    /**
     * Gets the sendDateTime attribute. 
     * @return Returns the sendDateTime.
     */
    public Timestamp getSendDateTimeValue() {
	    return this.sendDateTimeValue;
    }

    @Override
    public DateTime getSendDateTime() {
        return this.sendDateTimeValue == null ? null : new DateTime(this.sendDateTimeValue);
    }

    /**
     * Sets the sendDateTime attribute value.
     * @param sendDateTimeValue The sendDateTime to set.
     */
    public void setSendDateTimeValue(Timestamp sendDateTimeValue) {
	    this.sendDateTimeValue = sendDateTimeValue;
    }

    /**
     * Gets the processingFlag attribute. 
     * @return Returns the processingFlag.
     */
    public String getProcessingFlag() {
        return processingFlag;
    }

    /**
     * Sets the processingFlag attribute value.
     * @param processingFlag The processingFlag to set.
     */
    public void setProcessingFlag(String processingFlag) {
        this.processingFlag = processingFlag;
    }
    
    /**
     * Gets the lockedDate attribute. 
     * @return Returns the lockedDate.
     */
    public Timestamp getLockedDateValue() {
        return this.lockedDateValue;
    }

    @Override
    public DateTime getLockedDate() {
        return this.lockedDateValue == null ? null : new DateTime(this.lockedDateValue);
    }
    
    /**
     * Sets the lockedDate attribute value.
     * @param lockedDateValue The lockedDate to set.
     */
    public void setLockedDateValue(Timestamp lockedDateValue) {
        this.lockedDateValue = lockedDateValue;
    }

    /**
     * Gets the title
     * @return the title of this notification
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * @param title the title of this notification
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method just uses StringUtils to get at the content of the <message> tag 
     * that exists in the notification content.
     * @return String
     */
    public String getContentMessage() {
	    return StringUtils.substringBetween(content, NotificationConstants.XML_MESSAGE_CONSTANTS.MESSAGE_OPEN, NotificationConstants.XML_MESSAGE_CONSTANTS.MESSAGE_CLOSE);	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocTypeName() {
        return docTypeName;
    }

    /**
     * Sets the custom document type name.
     *
     * <p>
     * If null, the system will use the default {@code KualiNotification} document type when routing the notification.
     * If the document type does not match any document type name in the system, the system behavior is undefined.
     * </p>
     *
     * @param docTypeName document type name of this notification
     */
    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static Notification to(NotificationBo bo) {
        if (bo == null) {
            return null;
        }

        return Notification.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationBo from(Notification im) {
        if (im == null) {
            return null;
        }

        NotificationBo bo = new NotificationBo();
        bo.setId(im.getId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());
        bo.setDeliveryType(im.getDeliveryType());
        bo.setCreationDateTimeValue(im.getCreationDateTime() == null ? null : new Timestamp(im.getCreationDateTime().getMillis()));
        bo.setSendDateTimeValue(im.getSendDateTime() == null ? null : new Timestamp(im.getSendDateTime().getMillis()));
        bo.setAutoRemoveDateTimeValue(im.getAutoRemoveDateTime() == null ? null : new Timestamp(im.getAutoRemoveDateTime().getMillis()));
        bo.setTitle(im.getTitle());
        bo.setContent(im.getContent());
        bo.setLockedDateValue(im.getLockedDate() == null ? null : new Timestamp(im.getLockedDate().getMillis()));
        bo.setDocTypeName(im.getDocTypeName());

        // object references
        bo.setPriority(NotificationPriorityBo.from(im.getPriority()));
        bo.setContentType(NotificationContentTypeBo.from(im.getContentType()));
        bo.setChannel(NotificationChannelBo.from(im.getChannel()));
        bo.setProducer(NotificationProducerBo.from(im.getProducer()));

        // lists
        List<NotificationRecipientBo> tempRecipients = new ArrayList<NotificationRecipientBo>();
        if (CollectionUtils.isNotEmpty(im.getRecipients())) {
            for (NotificationRecipient recipient : im.getRecipients()) {
                tempRecipients.add(NotificationRecipientBo.from(recipient));
            }
            bo.setRecipients(tempRecipients);
        }
        List<NotificationSenderBo> tempSenders = new ArrayList<NotificationSenderBo>();
        if (CollectionUtils.isNotEmpty(im.getSenders())) {
            for (NotificationSender sender : im.getSenders()) {
                tempSenders.add(NotificationSenderBo.from(sender));
            }
            bo.setSenders(tempSenders);
        }

        return bo;
    }
}
