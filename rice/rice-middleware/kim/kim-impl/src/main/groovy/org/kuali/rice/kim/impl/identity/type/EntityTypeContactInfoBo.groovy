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
package org.kuali.rice.kim.impl.identity.type

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

import org.apache.commons.collections.CollectionUtils
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.identity.EntityUtils
import org.kuali.rice.kim.api.identity.address.EntityAddress
import org.kuali.rice.kim.api.identity.email.EntityEmail
import org.kuali.rice.kim.api.identity.phone.EntityPhone
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoContract
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault
import org.kuali.rice.kim.impl.identity.EntityTypeBo
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@Entity
@IdClass(EntityTypeContactInfoId.class)
@Table(name = "KRIM_ENTITY_ENT_TYP_T")
public class EntityTypeContactInfoBo extends PersistableBusinessObjectBase implements EntityTypeContactInfoContract {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ENTITY_ID")
    String entityId;

    @Id
    @Column(name = "ENT_TYP_CD")
    String entityTypeCode;

    @ManyToOne(targetEntity = EntityTypeBo.class, fetch = FetchType.EAGER, cascade = [])
    @JoinColumn(name = "ENT_TYP_CD", insertable = false, updatable = false)
    EntityTypeBo entityType;

    @OneToMany(targetEntity = EntityTypeContactInfoBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
    @JoinColumns([
        @JoinColumn(name="ENTITY_ID", insertable = false, updatable = false),
        @JoinColumn(name="ENT_TYP_CD", insertable = false, updatable = false)
    ])
    List<EntityEmailBo> emailAddresses;

    @OneToMany(targetEntity = EntityPhoneBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
    @JoinColumns([
        @JoinColumn(name="ENTITY_ID", insertable = false, updatable = false),
        @JoinColumn(name="ENT_TYP_CD", insertable = false, updatable = false)
    ])
    List<EntityPhoneBo> phoneNumbers

    @OneToMany(targetEntity = EntityAddressBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
    @JoinColumns([
        @JoinColumn(name="ENTITY_ID", insertable = false, updatable = false),
        @JoinColumn(name="ENT_TYP_CD", insertable = false, updatable = false)
    ])
    List<EntityAddressBo> addresses;

    @Type(type="yes_no")
    @Column(name="ACTV_IND")
    boolean active;

         /*
       * Converts a mutable EntityTypeDataBo to an immutable EntityTypeData representation.
       * @param bo
       * @return an immutable EntityTypeData
       */
      static EntityTypeContactInfo to(EntityTypeContactInfoBo bo) {
        if (bo == null) { return null }
        return EntityTypeContactInfo.Builder.create(bo).build()
      }

      static EntityTypeContactInfoDefault toDefault(EntityTypeContactInfoBo bo) {
          if (bo == null) { return null }
          return new EntityTypeContactInfoDefault(bo.getEntityTypeCode(),
                                        EntityAddressBo.to(bo.getDefaultAddress()),
                                        EntityEmailBo.to(bo.getDefaultEmailAddress()),
                                        EntityPhoneBo.to(bo.getDefaultPhoneNumber()))
      }

      /**
       * Creates a EntityTypeDataBo business object from an immutable representation of a EntityTypeData.
       * @param an immutable EntityTypeData
       * @return a EntityTypeDataBo
       */
      static EntityTypeContactInfoBo from(EntityTypeContactInfo immutable) {
        if (immutable == null) {return null}

        EntityTypeContactInfoBo bo = new EntityTypeContactInfoBo()
        bo.active = immutable.active

        bo.entityId = immutable.entityId
        bo.entityTypeCode = immutable.entityTypeCode
        bo.addresses = new ArrayList<EntityAddressBo>()
        if (CollectionUtils.isNotEmpty(immutable.addresses)) {
            for (EntityAddress address : immutable.addresses) {
                bo.addresses.add(EntityAddressBo.from(address))
            }
        }
        bo.phoneNumbers = new ArrayList<EntityPhoneBo>()
        if (CollectionUtils.isNotEmpty(immutable.phoneNumbers)) {
            for (EntityPhone phone : immutable.phoneNumbers) {
                bo.phoneNumbers.add(EntityPhoneBo.from(phone))
            }
        }
        bo.emailAddresses = new ArrayList<EntityEmailBo>()
        if (CollectionUtils.isNotEmpty(immutable.emailAddresses)) {
            for (EntityEmail email : immutable.emailAddresses) {
                bo.emailAddresses.add(EntityEmailBo.from(email))
            }
        }
        bo.versionNumber = immutable.versionNumber
        bo.objectId = immutable.objectId

        return bo;
      }

    EntityAddressBo getDefaultAddress() {
        return EntityUtils.getDefaultItem(this.addresses);
    }
    EntityEmailBo getDefaultEmailAddress() {
        return EntityUtils.getDefaultItem(this.emailAddresses);
    }
    EntityPhoneBo getDefaultPhoneNumber() {
        return EntityUtils.getDefaultItem(this.phoneNumbers);
    }

    EntityTypeBo getEntityType() {
        return this.entityType
    }

}
