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
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ken.api.notification.NotificationProducer;
import org.kuali.rice.ken.api.notification.NotificationProducerContract;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an instance of who can actually submit notification messages to the system 
 * for processing.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_PRODCR_T")
public class NotificationProducerBo extends PersistableBusinessObjectBase implements NotificationProducerContract {
    @Id
    @GeneratedValue(generator="KREN_PRODCR_S")
	@GenericGenerator(name="KREN_PRODCR_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_PRODCR_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="PRODCR_ID")
	private Long id;
    @Column(name="NM", nullable=false)
	private String name;
    @Column(name="DESC_TXT", nullable=false)
	private String description;
    @Column(name="CNTCT_INFO", nullable=false)
	private String contactInfo;
    
    // List references
    @ManyToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})@JoinTable(name="KREN_CHNL_PRODCR_T", 
	           joinColumns=@JoinColumn(name="PRODCR_ID"), 
	           inverseJoinColumns=@JoinColumn(name="CHNL_ID"))
	@OrderBy("id ASC")
	private List<NotificationChannelBo> channels;
    
    /**
     * Constructs a NotificationProducer instance.
     */
    public NotificationProducerBo() {
	channels = new ArrayList<NotificationChannelBo>();
    }

    /**
     * Gets the contactInfo attribute. 
     * @return Returns the contactInfo.
     */
    public String getContactInfo() {
	return contactInfo;
    }

    @Override
    public List<Long> getChannelIds() {
        List<Long> ids = new ArrayList<Long>();
        for (NotificationChannelBo bo : this.getChannels()) {
            ids.add(bo.getId());
        }
        return ids;
    }

    /**
     * Sets the contactInfo attribute value.
     * @param contactInfo The contactInfo to set.
     */
    public void setContactInfo(String contactInfo) {
	this.contactInfo = contactInfo;
    }

    /**
     * Gets the description attribute. 
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the description attribute value.
     * @param description The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
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
     * Gets the name attribute. 
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name attribute value.
     * @param name The name to set.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the channels attribute. 
     * @return Returns the channels.
     */
    public List<NotificationChannelBo> getChannels() {
        return channels;
    }

    /**
     * Sets the channels attribute value.
     * @param channels The channels to set.
     */
    public void setChannels(List<NotificationChannelBo> channels) {
        this.channels = channels;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static NotificationProducer to(NotificationProducerBo bo) {
        if (bo == null) {
            return null;
        }

        return NotificationProducer.Builder.create(bo).build();
    }


    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationProducerBo from(NotificationProducer im) {
        if (im == null) {
            return null;
        }

        NotificationProducerBo bo = new NotificationProducerBo();
        bo.setId(im.getId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());

        bo.setName(im.getName());
        bo.setDescription(im.getDescription());
        bo.setContactInfo(im.getContactInfo());

        List<NotificationChannelBo> tempChannels = new ArrayList<NotificationChannelBo>();
        if (CollectionUtils.isNotEmpty(im.getChannelIds())) {
            NotificationChannelService ncs = GlobalResourceLoader.getService("notificationChannelService");
            for (Long channelId : im.getChannelIds()) {
                tempChannels.add(ncs.getNotificationChannel(channelId.toString()));
            }
            bo.setChannels(tempChannels);
        }
        return bo;
    }
}
