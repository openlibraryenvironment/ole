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
package org.kuali.rice.kim.impl.identity.external

import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Transient
import org.apache.commons.lang.StringUtils
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.kuali.rice.kim.api.KimConstants
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krad.service.KRADServiceLocator
import org.kuali.rice.core.api.CoreApiServiceLocator

class EntityExternalIdentifierBo extends PersistableBusinessObjectBase implements EntityExternalIdentifierContract {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EntityExternalIdentifierBo.class)
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="KRIM_ENTITY_EXT_ID_ID_S")
	@GenericGenerator(name="KRIM_ENTITY_EXT_ID_ID_S",strategy="org.kuali.rice.core.jpa.spring.RiceNumericStringSequenceStyleGenerator",parameters=[
			@Parameter(name="sequence_name",value="KRIM_ENTITY_EXT_ID_ID_S"),
			@Parameter(name="value_column",value="id")
		])
	@Column(name = "ENTITY_EXT_ID_ID")
	String id;

	@Column(name = "ENTITY_ID")
	String entityId;

	@Column(name = "EXT_ID_TYP_CD")
	String externalIdentifierTypeCode;

	@Column(name = "EXT_ID")
	String externalId;

	@ManyToOne(targetEntity=EntityExternalIdentifierTypeBo.class, fetch = FetchType.EAGER, cascade = [])
	@JoinColumn(name = "EXT_ID_TYP_CD", insertable = false, updatable = false)
	EntityExternalIdentifierTypeBo externalIdentifierType;

    @Transient
    private EntityExternalIdentifierTypeBo cachedExtIdType = null;
	@Transient
    private boolean encryptionRequired = false;
	@Transient
	private boolean decryptionNeeded = false;

      /*
       * Converts a mutable EntityExternalIdentifierBo to an immutable EntityExternalIdentifier representation.
       * @param bo
       * @return an immutable EntityExternalIdentifier
       */
      static EntityExternalIdentifier to(EntityExternalIdentifierBo bo) {
        if (bo == null) { return null }
        return EntityExternalIdentifier.Builder.create(bo).build()
      }

      /**
       * Creates a EntityExternalIdentifierBo business object from an immutable representation of a EntityExternalIdentifier.
       * @param an immutable EntityExternalIdentifier
       * @return a EntityExternalIdentifierBo
       */
      static EntityExternalIdentifierBo from(EntityExternalIdentifier immutable) {
        if (immutable == null) {return null}

        EntityExternalIdentifierBo bo = new EntityExternalIdentifierBo()
        bo.id = immutable.id
        bo.externalId = immutable.externalId
        bo.entityId = immutable.entityId
        bo.externalIdentifierTypeCode = immutable.externalIdentifierTypeCode
        bo.externalIdentifierType = EntityExternalIdentifierTypeBo.from(immutable.externalIdentifierType)
        bo.versionNumber = immutable.versionNumber
        bo.objectId = immutable.objectId

        return bo;
      }

    @Override
	protected void prePersist() {
		super.prePersist();
		encryptExternalId();
	}

	@Override
	protected void postLoad() {
        super.postLoad();
        decryptExternalId();
	}


	@Override
	protected void preUpdate() {
		super.preUpdate();
		if (!this.decryptionNeeded) {
			encryptExternalId();
		}
	}

    protected void encryptExternalId() {
		evaluateExternalIdentifierType();
		if ( encryptionRequired && StringUtils.isNotEmpty(this.externalId) ) {
			try {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
				    this.externalId = CoreApiServiceLocator.getEncryptionService().encrypt(this.externalId);
				    this.decryptionNeeded = true;
                }
			}
			catch ( Exception e ) {
				LOG.info("Unable to encrypt value : " + e.getMessage() + " or it is already encrypted");
			}
		}
	}

	protected void decryptExternalId() {
		evaluateExternalIdentifierType();
		if ( encryptionRequired && StringUtils.isNotEmpty(externalId) ) {
			try {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
				    this.externalId = CoreApiServiceLocator.getEncryptionService().decrypt(this.externalId);
                }
			}
			catch ( Exception e ) {
				LOG.info("Unable to decrypt value : " + e.getMessage() + " or it is already decrypted");
	        }
		}
    }

    protected void evaluateExternalIdentifierType() {
		if ( cachedExtIdType == null ) {
			Map<String, String> criteria = new HashMap<String, String>();
		    criteria.put(KimConstants.PrimaryKeyConstants.CODE, externalIdentifierTypeCode);
		    cachedExtIdType = (EntityExternalIdentifierTypeBo) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(EntityExternalIdentifierTypeBo.class, criteria);
		    encryptionRequired = cachedExtIdType!= null && cachedExtIdType.isEncryptionRequired();
		}
	}

    protected String decryptedExternalId() {
		evaluateExternalIdentifierType();
		if ( encryptionRequired && StringUtils.isNotEmpty(externalId) ) {
			try {
                if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
				    return CoreApiServiceLocator.getEncryptionService().decrypt(this.externalId);
                }
			}
			catch ( Exception e ) {
				LOG.info("Unable to decrypt value : " + e.getMessage() + " or it is already decrypted");
	        }
		}
		return "";
    }

    public void setExternalId(String externalId) {
		this.externalId = externalId
		this.decryptionNeeded = false
	}

	public void setExternalIdentifierTypeCode(String externalIdentifierTypeCode) {
		this.externalIdentifierTypeCode = externalIdentifierTypeCode
		cachedExtIdType = null
	}
    public void setExternalIdentifierType(EntityExternalIdentifierTypeBo externalIdentifierType) {
		this.externalIdentifierType = externalIdentifierType
		cachedExtIdType = null
	}


    @Override
    public EntityExternalIdentifierTypeBo getExternalIdentifierType() {
        return this.externalIdentifierType
    }

    @Override
    public String getExternalId() {
		if (this.decryptionNeeded) {
			return decryptedExternalId();
		}
		return externalId;
	}
}
