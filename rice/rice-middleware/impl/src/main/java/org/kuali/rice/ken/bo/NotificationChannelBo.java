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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.ken.api.notification.NotificationChannel;
import org.kuali.rice.ken.api.notification.NotificationChannelContract;
import org.kuali.rice.ken.api.notification.NotificationChannelReviewer;
import org.kuali.rice.ken.api.notification.NotificationChannelReviewerContract;
import org.kuali.rice.ken.api.notification.NotificationContentType;
import org.kuali.rice.ken.api.notification.NotificationListRecipient;
import org.kuali.rice.ken.api.notification.NotificationListRecipientContract;
import org.kuali.rice.ken.api.notification.NotificationProducer;
import org.kuali.rice.ken.api.notification.NotificationProducerContract;
import org.kuali.rice.ken.api.notification.UserChannelSubscription;
import org.kuali.rice.ken.api.notification.UserChannelSubscriptionContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents and instance of a Notification Channel. A Notification Channel is correlated to a specific class of
 * notification, or in other words a specific business purpose. For instance, all overdue books from a specific library could
 * be a channel or a channel for concerts coming to campus could be another channel.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name = "KREN_CHNL_T")
public class NotificationChannelBo extends PersistableBusinessObjectBase implements NotificationChannelContract {
	@Id
	@GeneratedValue(generator="KREN_CHNL_S")
	@GenericGenerator(name="KREN_CHNL_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_CHNL_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name = "CHNL_ID")
	private Long id;
	@Column(name = "NM", nullable = false)
	private String name;
	@Column(name = "DESC_TXT", nullable = false)
	private String description;
	@Column(name = "SUBSCRB_IND", nullable = false)
	private boolean subscribable;

	// List references
	@OneToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, 
			targetEntity=NotificationRecipientListBo.class, mappedBy="channel")
	@OrderBy ("id ASC")
	private List<NotificationRecipientListBo> recipientLists;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})@JoinTable(name="KREN_CHNL_PRODCR_T", 
			joinColumns=@JoinColumn(name="CHNL_ID"), 
			inverseJoinColumns=@JoinColumn(name="PRODCR_ID"))
	@OrderBy ("id ASC")
	private List<NotificationProducerBo> producers;
	
	@OneToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, 
			targetEntity=NotificationChannelReviewerBo.class, mappedBy="channel")
	@OrderBy ("id ASC")
	private List<NotificationChannelReviewerBo> reviewers = new ArrayList<NotificationChannelReviewerBo>();
	
	@OneToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, 
			targetEntity=UserChannelSubscriptionBo.class, mappedBy="channel")
	@OrderBy ("id ASC")
	private List<UserChannelSubscriptionBo> subscriptions = new ArrayList<UserChannelSubscriptionBo>();


	/**
	 * Constructs a NotificationChannel instance.
	 */
	public NotificationChannelBo() {
		super();
		recipientLists = new ArrayList<NotificationRecipientListBo>();
		producers = new ArrayList<NotificationProducerBo>();
	}

	/**
	 * Gets the recipientLists attribute.
	 * 
	 * @return Returns the recipientLists.
	 */
	public List<NotificationRecipientListBo> getRecipientLists() {
		return recipientLists;
	}

	/**
	 * Sets the recipientLists attribute value.
	 * 
	 * @param recipientLists
	 *            The recipientLists to set.
	 */
	public void setRecipientLists(List<NotificationRecipientListBo> recipientLists) {
		this.recipientLists = recipientLists;
	}

	/**
	 * This method adds a recipient list to the overall set of recipient lists that are associated with this channnel.
	 * 
	 * @param recipientList
	 */
	public void addRecipientList(NotificationRecipientListBo recipientList) {
		this.recipientLists.add(recipientList);
	}

	/**
	 * This method removes a recipient list object from the overall list.
	 * 
	 * @param recipientList
	 */
	public void removeRecipientList(NotificationRecipientListBo recipientList) {
		this.recipientLists.remove(recipientList);
	}

	/**
	 * Gets the description attribute.
	 * 
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description attribute value.
	 * 
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the id attribute.
	 * 
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id attribute value.
	 * 
	 * @param id
	 *            The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name attribute.
	 * 
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name attribute value.
	 * 
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the subscribable attribute.
	 * 
	 * @return Returns the subscribable.
	 */
	public boolean isSubscribable() {
		return subscribable;
	}

	/**
	 * Sets the subscribable attribute value.
	 * 
	 * @param subscribable
	 *            The subscribable to set.
	 */
	public void setSubscribable(boolean subscribable) {
		this.subscribable = subscribable;
	}

	/**
	 * Gets the producers attribute.
	 * 
	 * @return Returns the producers.
	 */
	public List<NotificationProducerBo> getProducers() {
		return producers;
	}

	/**
	 * Sets the producers attribute value.
	 * 
	 * @param producers
	 *            The producers to set.
	 */
	public void setProducers(List<NotificationProducerBo> producers) {
		this.producers = producers;
	}

	/**
	 * Gets the list of reviewers for notification publications to this channel
	 * 
	 * @return the list of reviewers for notification publications to this channel
	 */
	public List<NotificationChannelReviewerBo> getReviewers() {
		return reviewers;
	}

	/**
	 * Sets the list of reviewers for notification publications to this channel
	 * 
	 * @param reviewers
	 *            the list of reviewers for notification publications to this channel
	 */
	public void setReviewers(List<NotificationChannelReviewerBo> reviewers) {
		this.reviewers = reviewers;
	}

	/**
	 * Gets the list of subscriptions to this channel
	 * 
	 * @return the list of subscriptions to this channel
	 */
	public List<UserChannelSubscriptionBo> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * Sets the list of subscriptions to this channel
	 * 
	 * @param subscriptions
	 *            the list of subscriptions to this channel
	 */
	public void setSubscriptions(List<UserChannelSubscriptionBo> subscriptions) {
		this.subscriptions = subscriptions;
	}

	/**
	 * Compares the id values of each NotificationChannel object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		NotificationChannelBo channelToCompare = (NotificationChannelBo) obj;
		return this.getId().equals(channelToCompare.getId());
	}

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static NotificationChannel to(NotificationChannelBo bo) {
        if (bo == null) {
            return null;
        }

        return NotificationChannel.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationChannelBo from(NotificationChannel im) {
        if (im == null) {
            return null;
        }

        NotificationChannelBo bo = new NotificationChannelBo();
        bo.setId(im.getId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());
        bo.setName(im.getName());
        bo.setDescription(im.getDescription());

        bo.setSubscribable(im.isSubscribable());

        List<NotificationRecipientListBo> tempRecipientLists = new ArrayList<NotificationRecipientListBo>();
        if (CollectionUtils.isNotEmpty(im.getRecipientLists())) {
            for (NotificationListRecipient listRecipient : im.getRecipientLists()) {
                tempRecipientLists.add(NotificationRecipientListBo.from(listRecipient));
            }
            bo.setRecipientLists(tempRecipientLists);
        }

        List<NotificationProducerBo> tempProducers = new ArrayList<NotificationProducerBo>();
        if (CollectionUtils.isNotEmpty(im.getProducers())) {
            for (NotificationProducer producer : im.getProducers()) {
                tempProducers.add(NotificationProducerBo.from(producer));
            }
            bo.setProducers(tempProducers);
        }

        List<NotificationChannelReviewerBo> tempReviewers = new ArrayList<NotificationChannelReviewerBo>();
        if (CollectionUtils.isNotEmpty(im.getReviewers())) {
            for (NotificationChannelReviewer reviewer : im.getReviewers()) {
                tempReviewers.add(NotificationChannelReviewerBo.from(reviewer));
            }
            bo.setReviewers(tempReviewers);
        }

        List<UserChannelSubscriptionBo> tempSubscriptions = new ArrayList<UserChannelSubscriptionBo>();
        if (CollectionUtils.isNotEmpty(im.getSubscriptions())) {
            for (UserChannelSubscription subscription : im.getSubscriptions()) {
                tempSubscriptions.add(UserChannelSubscriptionBo.from(subscription));
            }
            bo.setSubscriptions(tempSubscriptions);
        }

        return bo;
    }
}
