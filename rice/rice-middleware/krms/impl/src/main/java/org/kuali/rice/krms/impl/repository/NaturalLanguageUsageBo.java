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
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsageContract;

/**
 * The mutable implementation of the @{link NaturalLanguageUsageContract} interface, the counterpart to the immutable implementation {@link NaturalLanguageUsage}.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class NaturalLanguageUsageBo
    extends PersistableBusinessObjectBase
    implements NaturalLanguageUsageContract
{

    private String name;
    private String description;
    private String namespace;
    private String id;
    private boolean active;
    private Long versionNumber;
    private SequenceAccessorService sequenceAccessorService;

    /**
     * Default Constructor
     * 
     */
    public NaturalLanguageUsageBo() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
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
     * Sets the value of name on this builder to the given value.
     * 
     * @param name the name value to set.
     * 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value of description on this builder to the given value.
     * 
     * @param description the description value to set.
     * 
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Converts a mutable {@link NaturalLanguageUsageBo} to its immutable counterpart, {@link NaturalLanguageUsage}.
     * @param naturalLanguageUsageBo the mutable business object.
     * @return a {@link NaturalLanguageUsage} the immutable object.
     * 
     */
    public static NaturalLanguageUsage to(NaturalLanguageUsageBo naturalLanguageUsageBo) {
        if (naturalLanguageUsageBo == null) { return null; }
        return NaturalLanguageUsage.Builder.create(naturalLanguageUsageBo).build();
    }

    /**
     * Converts a immutable {@link NaturalLanguageUsage} to its mutable {@link NaturalLanguageUsageBo} counterpart.
     * @param naturalLanguageUsage the immutable object.
     * @return a {@link NaturalLanguageUsageBo} the mutable NaturalLanguageUsageBo.
     * 
     */
    public static org.kuali.rice.krms.impl.repository.NaturalLanguageUsageBo from(NaturalLanguageUsage naturalLanguageUsage) {
        if (naturalLanguageUsage == null) return null;
        NaturalLanguageUsageBo naturalLanguageUsageBo = new NaturalLanguageUsageBo();
        naturalLanguageUsageBo.setName(naturalLanguageUsage.getName());
        naturalLanguageUsageBo.setDescription(naturalLanguageUsage.getDescription());
        naturalLanguageUsageBo.setNamespace(naturalLanguageUsage.getNamespace());
        naturalLanguageUsageBo.setId(naturalLanguageUsage.getId());
        naturalLanguageUsageBo.setActive(naturalLanguageUsage.isActive());
        naturalLanguageUsageBo.setVersionNumber(naturalLanguageUsage.getVersionNumber());
        // TODO collections, etc.
        return naturalLanguageUsageBo;
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
