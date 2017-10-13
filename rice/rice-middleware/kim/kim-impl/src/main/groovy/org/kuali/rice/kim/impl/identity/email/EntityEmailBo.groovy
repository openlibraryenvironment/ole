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
package org.kuali.rice.kim.impl.identity.email

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.KimApiConstants
import org.kuali.rice.kim.api.identity.email.EntityEmail
import org.kuali.rice.kim.api.identity.email.EntityEmailContract
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name = "KRIM_ENTITY_EMAIL_T")
class EntityEmailBo extends PersistableBusinessObjectBase implements EntityEmailContract {
    private static final long serialVersionUID = 1L;

        @Id
        @Column(name = "ENTITY_EMAIL_ID")
        String id;

        @Column(name = "ENTITY_ID")
        String entityId;

        @Column(name = "ENT_TYP_CD")
        String entityTypeCode;

        @Column(name = "EMAIL_TYP_CD")
        String emailTypeCode;

        @Column(name = "EMAIL_ADDR")
	    String emailAddress;

        @ManyToOne(targetEntity=EntityEmailTypeBo.class, fetch = FetchType.EAGER, cascade = [])
        @JoinColumn(name = "PHONE_TYP_CD", insertable = false, updatable = false)
        EntityEmailTypeBo emailType;

        @Transient
        boolean suppressEmail;

        @Type(type="yes_no")
        @Column(name="ACTV_IND")
        boolean active;

        @Type(type="yes_no")
        @Column(name="DFLT_IND")
        boolean defaultValue;

     /*
       * Converts a mutable EntityEmailBo to an immutable EntityEmail representation.
       * @param bo
       * @return an immutable EntityEmail
       */
      static EntityEmail to(EntityEmailBo bo) {
        if (bo == null) { return null }
        return EntityEmail.Builder.create(bo).build()
      }

      /**
       * Creates a EntityEmailBo business object from an immutable representation of a EntityEmail.
       * @param an immutable EntityEmail
       * @return a EntityEmailBo
       */
      static EntityEmailBo from(EntityEmail immutable) {
        if (immutable == null) {return null}

        EntityEmailBo bo = new EntityEmailBo()
        bo.id = immutable.id
        bo.active = immutable.active

        bo.entityId = immutable.entityId
        bo.entityTypeCode = immutable.entityTypeCode
        if (immutable.emailType != null) {
            bo.emailTypeCode = immutable.emailType.code
        }
        bo.emailAddress = immutable.getEmailAddressUnmasked()
        bo.emailType = EntityEmailTypeBo.from(immutable.emailType)
        bo.defaultValue = immutable.defaultValue
        bo.versionNumber = immutable.versionNumber
        bo.objectId = immutable.objectId

        return bo;
      }

        @Override
        EntityEmailTypeBo getEmailType() {
            return this.emailType
        }

        public void setEmailType(EntityEmailTypeBo emailType) {
            this.emailType = emailType
        }

        @Override
        boolean isSuppressEmail() {
            if (this.suppressEmail == null) {
                EntityPrivacyPreferences privacy = KimApiServiceLocator.getIdentityService().getEntityPrivacyPreferences(getEntityId())
                if (privacy != null) {
                   this.suppressEmail = privacy.isSuppressEmail()
                } else {
                   this.suppressEmail = false
                }
            }

            return this.suppressEmail;
        }

        @Override
        String getEmailAddressUnmasked() {
            return this.emailAddress
        }

        @Override
        String getEmailAddress() {
            if (isSuppressEmail())  {
                return KimApiConstants.RestrictedMasks.RESTRICTED_DATA_MASK
            }
            return this.emailAddress
        }
}
