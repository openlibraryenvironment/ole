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

import java.util.List;
import java.util.Map;

import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface KrmsAttributeDefinitionService {

    /**
     * This will create a {@link KrmsAttributeDefinition} exactly like the parameter passed in.
     *
     * @param attributeDefinition - KrmsAttributeDefinition
     * @throws IllegalArgumentException if the attribute definition is null
     * @throws IllegalStateException if the attribute definition already exists in the system
     */
    @CacheEvict(value={KrmsAttributeDefinition.Cache.NAME}, allEntries = true)
	public KrmsAttributeDefinition createAttributeDefinition(KrmsAttributeDefinition attributeDefinition);

    /**
     * This will update a {@link KrmsAttributeDefinition}.
     *
     *
     * @param attributeDefinition - KrmsAttributeDefinition
     * @throws IllegalArgumentException if the attribute definition is null
     * @throws IllegalStateException if the attribute definition does not exist in the system
     */
    @CacheEvict(value={KrmsAttributeDefinition.Cache.NAME}, allEntries = true)
	public void updateAttributeDefinition(KrmsAttributeDefinition attributeDefinition);

    /**
     * Lookup a KrmsAttributeDefinition based on the given id.
     *
     * @param id the given KrmsAttributeDefinition id
     * @return a KrmsAttributeDefinition object with the given id.  A null reference is returned if an invalid or
     *         non-existant id is supplied.
     */
    @Cacheable(value= KrmsAttributeDefinition.Cache.NAME, key="'attributeDefinitionId=' + #p0")
	public KrmsAttributeDefinition getAttributeDefinitionById(String id);

    /**
     * Get a KrmsAttributeDefinition object based on name and namespace
     *
     * @param name the given name
     * @param namespace the given type namespace
     * @return A KrmsAttributeDefinition object with the given namespace and name if one with that name and namespace
     *         exists.  Otherwise, null is returned.
     * @throws IllegalStateException if multiple KrmsAttributeDefinitions exist with the same name and namespace
     */
    @Cacheable(value= KrmsAttributeDefinition.Cache.NAME, key="'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
	public KrmsAttributeDefinition getAttributeDefinitionByNameAndNamespace(String name, String namespace);

   /**
     * Returns all KrmsAttributeDefinition that for a given namespace.
     *
     * @return all KrmsAttributeDefinition for a namespace
     */
    @Cacheable(value= KrmsAttributeDefinition.Cache.NAME, key="'namespace=' + #p0")
	public List<KrmsAttributeDefinition> findAttributeDefinitionsByNamespace(String namespace);


    /**
      * Returns all KrmsAttributeDefinition that for a given type.
      *
      * @return all KrmsAttributeDefinition for a type.  May be empty, will not be null;
      */
    @Cacheable(value= KrmsAttributeDefinition.Cache.NAME, key="'typeId=' + #p0")
     public List<KrmsAttributeDefinition> findAttributeDefinitionsByType(String typeId);


    /**
     * Returns all KrmsAttributeDefinitions
     *
     * @return all KrmsAttributeDefinitions
     */
    @Cacheable(value= KrmsAttributeDefinition.Cache.NAME, key="'all'")
	public List<KrmsAttributeDefinition> findAllAttributeDefinitions();

	/**
	 * This method converts a collection of name/value attribute pairs to
	 * id/value attribute pairs.
	 * <p>
	 * At the api layer, attributes are represented as name/value pairs.
	 * However, in the database, the names of the attribute and the values are
	 * stored separately. The attribute definitions contain the attribute names.
	 * All defined attributes(for the various krms entity types) are stored 
	 * together in a single table. The attribute values themselves are stored 
	 * in separate tables for each entity type, and then reference the attribute
	 * definitions by the attribute definition id.
	 * <p>
	 * This method converts the name/value pairs to id/value pairs so they
	 * can be searched from a single table. This simplifies the queries for 
	 * attributes.
	 * <p>
	 * 
	 * @param attributesByName - a Map<String/String> containing the name/value
	 * 	pairs for the set of attributes.
	 * @param namespace - the namespace code of the set of attributes
	 * @return a Map<String,String> containing the id/value pairs for the set
	 * of attributes.
	 */
	public Map<String,String> convertAttributeKeys(Map<String,String> attributesByName, String namespace);

	/**
	 * This method gets the attribute definition ID for a given attribute
	 * 
	 * @param attributeName - the name of the attribute
	 * @param namespace - the namespace code of the attribute
	 * @return - the attribute definition id 
	 */
    @Cacheable(value= KrmsAttributeDefinition.Cache.NAME, key="'{ID}namespaceCode=' + #p0 + '|' + 'name=' + #p1")
	public String getKrmsAttributeId( String attributeName, String namespace);
	
	/**
	 * This method gets a KrmsAttributeDefinitionBo object for a given attribute.
	 * 
	 * @param attributeName - the name of the attribute
	 * @param namespace - the namespace code of the attribute
	 * @return - the attribute definition id 
	 */
	public KrmsAttributeDefinitionBo getKrmsAttributeBo( String attributeName, String namespace);

	
}
