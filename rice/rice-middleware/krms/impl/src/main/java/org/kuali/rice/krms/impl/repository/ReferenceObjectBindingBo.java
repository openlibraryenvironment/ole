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
package org.kuali.rice.krms.impl.repository;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBindingContract;

/**
 * The mutable implementation of the @{link ReferenceObjectBindingContract} interface, the counterpart to the immutable implementation {@link ReferenceObjectBinding}.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class ReferenceObjectBindingBo
    extends PersistableBusinessObjectBase
    implements ReferenceObjectBindingContract
{

    private String collectionName;
    private String krmsDiscriminatorType;
    private String krmsObjectId;
    private String namespace;
    private String referenceDiscriminatorType;
    private String referenceObjectId;
    private String id;
    private boolean active;
    private Long versionNumber;
    private SequenceAccessorService sequenceAccessorService;

    /**
     * Default Constructor
     * 
     */
    public ReferenceObjectBindingBo() {
    }

    @Override
    public String getCollectionName() {
        return this.collectionName;
    }

    @Override
    public String getKrmsDiscriminatorType() {
        return this.krmsDiscriminatorType;
    }

    @Override
    public String getKrmsObjectId() {
        return this.krmsObjectId;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getReferenceDiscriminatorType() {
        return this.referenceDiscriminatorType;
    }

    @Override
    public String getReferenceObjectId() {
        return this.referenceObjectId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * Sets the value of collectionName on this builder to the given value.
     * 
     * @param collectionName the collectionName value to set.
     * 
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * Sets the value of krmsDiscriminatorType on this builder to the given value.
     * 
     * @param krmsDiscriminatorType the krmsDiscriminatorType value to set.
     * 
     */
    public void setKrmsDiscriminatorType(String krmsDiscriminatorType) {
        this.krmsDiscriminatorType = krmsDiscriminatorType;
    }

    /**
     * Sets the value of krmsObjectId on this builder to the given value.
     * 
     * @param krmsObjectId the krmsObjectId value to set.
     * 
     */
    public void setKrmsObjectId(String krmsObjectId) {
        this.krmsObjectId = krmsObjectId;
    }

    /**
     * Sets the value of namespace on this builder to the given value.
     * 
     * @param namespace the namespace value to set.
     * 
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Sets the value of referenceDiscriminatorType on this builder to the given value.
     * 
     * @param referenceDiscriminatorType the referenceDiscriminatorType value to set.
     * 
     */
    public void setReferenceDiscriminatorType(String referenceDiscriminatorType) {
        this.referenceDiscriminatorType = referenceDiscriminatorType;
    }

    /**
     * Sets the value of referenceObjectId on this builder to the given value.
     * 
     * @param referenceObjectId the referenceObjectId value to set.
     * 
     */
    public void setReferenceObjectId(String referenceObjectId) {
        this.referenceObjectId = referenceObjectId;
    }

    /**
     * Sets the value of id on this builder to the given value.
     * 
     * @param id the id value to set.
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the value of active on this builder to the given value.
     * 
     * @param active the active value to set.
     * 
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the value of versionNumber on this builder to the given value.
     * 
     * @param versionNumber the versionNumber value to set.
     * 
     */
    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Converts a mutable {@link ReferenceObjectBindingBo} to its immutable counterpart, {@link ReferenceObjectBinding}.
     * @param referenceObjectBindingBo the mutable business object.
     * @return a {@link ReferenceObjectBinding} the immutable object.
     * 
     */
    public static ReferenceObjectBinding to(ReferenceObjectBindingBo referenceObjectBindingBo) {
        if (referenceObjectBindingBo == null) { return null; }
        return ReferenceObjectBinding.Builder.create(referenceObjectBindingBo).build();
    }

    /**
     * Converts a immutable {@link ReferenceObjectBinding} to its mutable {@link ReferenceObjectBindingBo} counterpart.
     * @param referenceObjectBinding the immutable object.
     * @return a {@link ReferenceObjectBindingBo} the mutable ReferenceObjectBindingBo.
     * 
     */
    public static org.kuali.rice.krms.impl.repository.ReferenceObjectBindingBo from(ReferenceObjectBinding referenceObjectBinding) {
        if (referenceObjectBinding == null) return null;
        ReferenceObjectBindingBo referenceObjectBindingBo = new ReferenceObjectBindingBo();
        referenceObjectBindingBo.setCollectionName(referenceObjectBinding.getCollectionName());
        referenceObjectBindingBo.setKrmsDiscriminatorType(referenceObjectBinding.getKrmsDiscriminatorType());
        referenceObjectBindingBo.setKrmsObjectId(referenceObjectBinding.getKrmsObjectId());
        referenceObjectBindingBo.setNamespace(referenceObjectBinding.getNamespace());
        referenceObjectBindingBo.setReferenceDiscriminatorType(referenceObjectBinding.getReferenceDiscriminatorType());
        referenceObjectBindingBo.setReferenceObjectId(referenceObjectBinding.getReferenceObjectId());
        referenceObjectBindingBo.setId(referenceObjectBinding.getId());
        referenceObjectBindingBo.setActive(referenceObjectBinding.isActive());
        referenceObjectBindingBo.setVersionNumber(referenceObjectBinding.getVersionNumber());
        // TODO collections, etc.
        return referenceObjectBindingBo;
    }

    /**
     * Returns the next available id for the given table and class.
     * @return String the next available id for the given table and class.
     * 
     */
    private String getNewId(String table, Class clazz) {
        if (sequenceAccessorService == null) {
            sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
        }
        Long id = sequenceAccessorService.getNextAvailableSequenceNumber(table, clazz);
        return id.toString();
    }

    /**
     * Set the SequenceAccessorService, useful for testing.
     * @param sas SequenceAccessorService to use for getNewId.
     * 
     */
    public void setSequenceAccessorService(SequenceAccessorService sas) {
        sequenceAccessorService = sas;
    }

    public SequenceAccessorService getSequenceAccessorService() {
        return sequenceAccessorService;
    }

}
