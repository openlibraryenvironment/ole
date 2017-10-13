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
 * This class represents a recipient preferences in the system.  This is a generic Key/Value structure
 * that is used by the system to store preferences that the user has set up. This will be 
 * used by the tickler plugins which will need a generic and dynamic structure for user specific settings.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_RECIP_PREFS_T")
public class RecipientPreference {

	/**
     * Field names for queries
     */
    public static final String RECIPIENT_FIELD = "recipientId";
    public static final String PROPERTY_FIELD = "property";
    
	@Id
	@Column(name="RECIP_PREFS_ID")
	private Long id;
	@Column(name="RECIP_ID", nullable=false)
	private String recipientId;
	@Column(name="PROP", nullable=false)
	private String property;
	@Column(name="VAL", nullable=true)
	private String value;

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
     * Gets the property attribute. 
     * @return Returns the property.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the property attribute value.
     * @param property The property to set.
     */
    public void setProperty(String property) {
        this.property = property;
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
     * Gets the value attribute. 
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value attribute value.
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
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
     * @see java.lang.Object#clone()
     */
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                       .append("id", id)
                       .append("recipientId", recipientId)
                       .append("property", property)
                       .append("value", value)
                       .append("lockVerNbr", lockVerNbr)
                       .toString();
    }
}
