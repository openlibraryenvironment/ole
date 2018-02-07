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

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class represents an abstract message that has been sent to a single user
 * recipient and may result in several {@link MessageDelivery}s.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
// this class could possibly just extend the MessageDTO
@Entity
@Table(name="KREN_MSG_T")
public class Message {
    /**
     * Field names
     */
    public static final String ID_FIELD = "id";
    public static final String ORIGINID_FIELD = "originId";

    @Id
	@Column(name="MSG_ID")
	private Long id;
    /**
     * The origin id is an id provided by the originating system that creates the message
     */
    @Column(name="ORGN_ID", nullable=false)
	private String originId;
    @Column(name="DELIV_TYP", nullable=false)
	private String deliveryType;
    @Column(name="CHNL", nullable=false)
	private String channel;
    @Column(name="PRODCR", nullable=true)
	private String producer;
    @Column(name="CRTE_DTTM", nullable=false)
    private Timestamp creationDateTime = new Timestamp(System.currentTimeMillis());
    @Column(name="TTL", nullable=true)
	private String title;
    @Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="CNTNT", nullable=false)
	private String content;
    @Column(name="CNTNT_TYP", nullable=true)
	private String contentType;
    @Column(name="URL", nullable=true)
	private String url;
    @Column(name="RECIP_ID", nullable=false)
	private String recipient;

    /**
     * Lock column for OJB optimistic locking
     */
    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;

    /**
     * Normal no-arg constructor
     */
    public Message() {}

    /**
     * Shallow-copy constructor
     * @param m Message object to (shallow) copy
     */
    public Message(Message m) {
        this.id = m.id;
        this.channel = m.channel;
        this.content = m.content;
        this.contentType = m.contentType;
        this.creationDateTime = m.creationDateTime;
        this.deliveryType = m.deliveryType;
        this.lockVerNbr = m.lockVerNbr;
        this.producer = m.producer;
        this.recipient = m.recipient;
        this.title = m.title;
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
     * Gets the origin id
     * @return the origin id
     */
    public String getOriginId() {
        return this.originId;
    }

    /**
     * Sets the origin id
     * @param originId the origin id
     */
    public void setOriginId(String originId) {
        this.originId = originId;
    }

    /**
     * Returns when this Notification entry was created 
     * @return when this Notification entry was created
     */
    public Timestamp getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * Sets the creation date of this Notification entry
     * @param created the creation date of this Notification entry
     */
    public void setCreationDateTime(Timestamp created) {
        this.creationDateTime = created;
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
     * Gets the recipient attribute. 
     * @return Returns the recipient.
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Sets the recipient attribute value.
     * @param recipients The recipient to set.
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
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
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the contentType attribute value.
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
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
     * Gets the channel
     * @return the channel
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Sets the channel
     * @param channel the channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Gets the producer
     * @return the producer
     */
    public String getProducer() {
        return this.producer;
    }

    /**
     * Sets the producer
     * @param producer the producer
     */
    public void setProducer(String producer) {
        this.producer = producer;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                       .append("id", id)
                       .append("creationDateTime", creationDateTime)
                       .append("deliveryType", deliveryType)
                       .append("recipient", recipient)
                       .append("title", title)
                       .append("channel", channel)
                       .append("producer", producer)
                       .append("content", StringUtils.abbreviate(content, 100))
                       .append("contentType", contentType)
                       .append("lockVerNbr", lockVerNbr)
                       .toString();
    }

}
