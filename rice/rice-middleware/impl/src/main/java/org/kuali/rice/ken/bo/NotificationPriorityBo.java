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
import org.kuali.rice.ken.api.notification.NotificationPriority;
import org.kuali.rice.ken.api.notification.NotificationPriorityContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents a priority for a notification - i.e. "High", "Medium", "Low", "Emergency", etc.
 * In addition, it describes information about a priority such as its ranking order of priority.  Priority 
 * order within the system is assumed to be ascending.  This by no means impacts the order of delivery 
 * of a notification system message.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_PRIO_T")
public class NotificationPriorityBo extends PersistableBusinessObjectBase implements NotificationPriorityContract {
    @Id
    @GeneratedValue(generator="KREN_PRIO_S")
	@GenericGenerator(name="KREN_PRIO_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_PRIO_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="PRIO_ID")
	private Long id;
    @Column(name="NM", nullable=false)
	private String name;
    @Column(name="DESC_TXT", nullable=false)
	private String description;
    @Column(name="PRIO_ORD", nullable=false)
	private Integer order;
    
    /**
     * Constructs a NotificationPriority instance.
     */
    public NotificationPriorityBo() {
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
     * Gets the order attribute. 
     * @return Returns the order.
     */
    public Integer getOrder() {
	    return order;
    }

    /**
     * Sets the order attribute value.
     * @param order The order to set.
     */
    public void setOrder(Integer order) {
	    this.order = order;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static NotificationPriority to(NotificationPriorityBo bo) {
        if (bo == null) {
            return null;
        }

        return NotificationPriority.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationPriorityBo from(NotificationPriority im) {
        if (im == null) {
            return null;
        }

        NotificationPriorityBo bo = new NotificationPriorityBo();
        bo.setId(im.getId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());
        bo.setName(im.getName());
        bo.setDescription(im.getDescription());
        bo.setOrder(im.getOrder());

        return bo;
    }
}

