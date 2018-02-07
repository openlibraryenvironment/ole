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
import org.hibernate.annotations.Type;
import org.kuali.rice.ken.api.notification.NotificationContentType;
import org.kuali.rice.ken.api.notification.NotificationContentTypeContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class represents the different types of Notification content that the system can handle.  
 * For example, and instance of content type could be "Alert" or "Event".
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREN_CNTNT_TYP_T")
public class NotificationContentTypeBo extends PersistableBusinessObjectBase implements NotificationContentTypeContract {
    @Id
    @GeneratedValue(generator="KREN_CNTNT_TYP_S")
	@GenericGenerator(name="KREN_CNTNT_TYP_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREN_CNTNT_TYP_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="CNTNT_TYP_ID")
	private Long id;
    @Column(name="NM", nullable=false)
	private String name;
    @Type(type="true_false")
    private boolean current = true;
    @Column(name="CNTNT_TYP_VER_NBR", nullable=false)
    private Integer version = Integer.valueOf(0);
    @Column(name="DESC_TXT", nullable=false)
	private String description;
    @Column(name="NMSPC_CD", nullable=false)
	private String namespace;
    @Column(name="XSD", nullable=false, length=4096)
	private String xsd;
    @Column(name="XSL", nullable=false, length=4096)
	private String xsl;

    /**
     * Constructs a NotificationContentType instance.
     */
    public NotificationContentTypeBo() {
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
     * @return whether this is the current version
     */
    public boolean isCurrent() {
        return this.current;
    }

    /**
     * @param current whether this is the current version
     */
    public void setCurrent(boolean current) {
        this.current = current;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Sets the name attribute value.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the namespace attribute. 
     * @return Returns the namespace.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace attribute value.
     * @param namespace The namespace to set.
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Gets the xsd attribute. The value of this field is used to validate a notification's content field dynamically.
     * @return Returns the xsd.
     */
    public String getXsd() {
        return xsd;
    }

    /**
     * Sets the xsd attribute value.  The value of this field is used to validate a notification's content field dynamically.
     * @param xsd The xsd to set.
     */
    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    /**
     * Gets the xsl attribute. The value of this field is used to render a notification's contents dynamically.
     * @return Returns the xsl.
     */
    public String getXsl() {
        return xsl;
    }

    /**
     * Sets the xsl attribute value.  The value of this field is used to render a notification's contents dynamically.
     * @param xsl The xsl to set.
     */
    public void setXsl(String xsl) {
        this.xsl = xsl;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static NotificationContentType to(NotificationContentTypeBo bo) {
        if (bo == null) {
            return null;
        }

        return NotificationContentType.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationContentTypeBo from(NotificationContentType im) {
        if (im == null) {
            return null;
        }

        NotificationContentTypeBo bo = new NotificationContentTypeBo();
        bo.setId(im.getId());
        bo.setVersionNumber(im.getVersionNumber());
        bo.setObjectId(im.getObjectId());
        bo.setName(im.getName());
        bo.setDescription(im.getDescription());

        bo.setCurrent(im.isCurrent());
        bo.setVersion(im.getVersion());
        bo.setNamespace(im.getNamespace());
        bo.setXsd(im.getXsd());
        bo.setXsl(im.getXsl());
        return bo;
    }
}
