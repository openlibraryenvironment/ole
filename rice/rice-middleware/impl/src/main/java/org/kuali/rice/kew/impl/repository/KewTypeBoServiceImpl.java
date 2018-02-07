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
package org.kuali.rice.kew.impl.repository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute;
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition;
import org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService;
import org.kuali.rice.kew.impl.type.KewTypeAttributeBo;
import org.kuali.rice.kew.impl.type.KewTypeBo;
import org.kuali.rice.krad.service.BusinessObjectService;

public final class KewTypeBoServiceImpl implements KewTypeRepositoryService {

    private BusinessObjectService businessObjectService;

	/**
	 * This overridden method creates a KewType if it does not already exist in the repository.
	 * 
	 * @see org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService#createKewType(org.kuali.rice.kew.api.repository.type.KewTypeDefinition)
	 */
	@Override
	public KewTypeDefinition createKewType(KewTypeDefinition kewType) {
		if (kewType == null){
	        throw new RiceIllegalArgumentException("kewType is null");
		}
		final String nameKey = kewType.getName();
		final String namespaceKey = kewType.getNamespace();
		final KewTypeDefinition existing = getTypeByNameAndNamespace(nameKey, namespaceKey);
		if (existing != null && existing.getName().equals(nameKey) && existing.getNamespace().equals(namespaceKey)){
            throw new RiceIllegalStateException("The KEW Type to create already exists: " + kewType);
		}
		
		KewTypeBo bo = (KewTypeBo)businessObjectService.save(KewTypeBo.from(kewType));
		
		return KewTypeBo.to(bo);
	}

	/**
	 * This overridden method updates an existing KewType
	 * 
	 * @see org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService#updateKewType(org.kuali.rice.kew.api.repository.type.KewTypeDefinition)
	 */
	@Override
	public void updateKewType(KewTypeDefinition kewType) {
        if (kewType == null) {
            throw new RiceIllegalArgumentException("kewType is null");
        }
		final String idKey = kewType.getId();
		final KewTypeBo existing = businessObjectService.findBySinglePrimaryKey(KewTypeBo.class, idKey);
        if (existing == null) {
            throw new RiceIllegalStateException("The KEW type does not exist: " + kewType);
        }
        final KewTypeDefinition toUpdate;
        if (!existing.getId().equals(kewType.getId())){
        	final KewTypeDefinition.Builder builder = KewTypeDefinition.Builder.create(kewType);
        	builder.setId(existing.getId());
        	toUpdate = builder.build();
        } else {
        	toUpdate = kewType;
        }
        
        businessObjectService.save(KewTypeBo.from(toUpdate));
	}

    @Override
    public KewTypeDefinition getTypeById(final String id) {
        if (StringUtils.isBlank(id)) {
            throw new RiceIllegalArgumentException("id is blank");
        }

        KewTypeBo kewTypeBo = businessObjectService.findBySinglePrimaryKey(KewTypeBo.class, id);

        return KewTypeBo.to(kewTypeBo);
    }

    @Override
    public KewTypeDefinition getTypeByNameAndNamespace(final String name, final String namespace) {
        if (StringUtils.isBlank(name)) {
            throw new RiceIllegalArgumentException("name is blank");
        }
        if (StringUtils.isBlank(namespace)) {
            throw new RiceIllegalArgumentException("namespace is blank");
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("namespace", namespace);

        KewTypeBo myType = businessObjectService.findByPrimaryKey(KewTypeBo.class, Collections.unmodifiableMap(map));
        return KewTypeBo.to(myType);
    }

    @Override
    public List<KewTypeDefinition> findAllTypesByNamespace(final String namespace) {
        if (StringUtils.isBlank(namespace)) {
            throw new RiceIllegalArgumentException("namespace is blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("namespace", namespace);
        map.put("active", Boolean.TRUE);

        Collection<KewTypeBo> kewTypeBos = businessObjectService.findMatching(KewTypeBo.class, Collections.unmodifiableMap(map));

        return convertListOfBosToImmutables(kewTypeBos);
    }

    @Override
    public List<KewTypeDefinition> findAllTypes() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active", Boolean.TRUE);

        Collection<KewTypeBo> kewTypeBos = businessObjectService.findMatching(KewTypeBo.class, Collections.unmodifiableMap(map));
        return convertListOfBosToImmutables(kewTypeBos);
    }

    /**
     * Sets the businessObjectService attribute value.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Converts a List<KewTypeBo> to an Unmodifiable List<KewType>
     *
     * @param kewTypeBos a mutable List<KewTypeBo> to made completely immutable.
     * @return An unmodifiable List<KewType>
     */
    List<KewTypeDefinition> convertListOfBosToImmutables(final Collection<KewTypeBo> kewTypeBos) {
        ArrayList<KewTypeDefinition> kewTypes = new ArrayList<KewTypeDefinition>();
        for (KewTypeBo bo : kewTypeBos) {
            KewTypeDefinition kewType = KewTypeBo.to(bo);
            kewTypes.add(kewType);
        }
        return Collections.unmodifiableList(kewTypes);
    }

	/**
     * This overridden method creates a KewTypeAttribute if it does not already exist in the repository.
	 * 
	 * @see org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService#createKewTypeAttribute(org.kuali.rice.kew.api.repository.type.KewTypeAttribute)
	 */
	@Override
	public void createKewTypeAttribute(KewTypeAttribute kewTypeAttribute) {
        if (kewTypeAttribute == null){
            throw new RiceIllegalArgumentException("kewTypeAttribute is null");
        }
        
        final String typeIdKey = kewTypeAttribute.getTypeId();
        final String attrDefIdKey = kewTypeAttribute.getAttributeDefinitionId();
        
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeIdKey);
        map.put("attributeDefinitionId", attrDefIdKey);

        KewTypeAttributeBo existing = businessObjectService.findByPrimaryKey(KewTypeAttributeBo.class, Collections.unmodifiableMap(map));

        if (existing != null && existing.getTypeId().equals(typeIdKey) && existing.getAttributeDefinitionId().equals(attrDefIdKey)){
            throw new RiceIllegalStateException("The KEW Type Attribute to create already exists: " + kewTypeAttribute);
        }
        
        KewTypeAttributeBo bo = (KewTypeAttributeBo)businessObjectService.save(KewTypeAttributeBo.from(kewTypeAttribute));
    }

    /**
     * This overridden method updates an existing KewTypeAttribute
     * 
     * @see org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService#updateKewTypeAttribute(org.kuali.rice.kew.api.repository.type.KewTypeAttribute)
     */
    @Override
    public void updateKewTypeAttribute(KewTypeAttribute kewTypeAttribute) {
        if (kewTypeAttribute == null) {
            throw new RiceIllegalArgumentException("kewTypeAttribute is null");
        }
        final String idKey = kewTypeAttribute.getId();
        final KewTypeAttributeBo existing = businessObjectService.findBySinglePrimaryKey(KewTypeAttributeBo.class, idKey);
        if (existing == null) {
            throw new RiceIllegalStateException("The KEW type Attribute does not exist: " + kewTypeAttribute);
        }
        final KewTypeAttribute toUpdate;
        if (!existing.getId().equals(kewTypeAttribute.getId())){
            final KewTypeAttribute.Builder builder = KewTypeAttribute.Builder.create(kewTypeAttribute);
            builder.setId(existing.getId());
            toUpdate = builder.build();
        } else {
            toUpdate = kewTypeAttribute;
        }
        
        businessObjectService.save(KewTypeAttributeBo.from(toUpdate));
    }
}
