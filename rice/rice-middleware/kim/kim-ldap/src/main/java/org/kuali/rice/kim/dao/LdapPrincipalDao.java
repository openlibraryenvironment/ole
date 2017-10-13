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
package org.kuali.rice.kim.dao;

import java.util.List;
import java.util.Map;

import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.util.Constants;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.LdapTemplate;


/**
 * Interface class with public methods for accessing LDAP data store for entity information
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface LdapPrincipalDao {
    LdapTemplate getLdapTemplate();

    void setLdapTemplate(LdapTemplate template);

    /**
     * In EDS, the principalId, principalName, and entityId will all be the same.
     */
    Principal getPrincipal(String principalId);

    /**
     * In EDS, the principalId, principalName, and entityId will all be the same.
     */
    Principal getPrincipalByName(String principalName);

    <T> List<T> search(Class<T> type, Map<String, Object> criteria);

	/** Find entity objects based on the given criteria. */
	EntityDefault getEntityDefault(String entityId);

	Entity getEntity(String entityId);
	
	/**
     * Fetches full entity info, populated from EDS, based on the Entity's principal id
     * @param principalId the principal id to look the entity up for
     * @return the corresponding entity info
     */
    Entity getEntityByPrincipalId(String principalId);

	Entity getEntityByPrincipalName(String principalName);

    /**
     * entityid and principalId are treated as the same.
     * 
     * @see #getEntityDefault(String)
     */
	EntityDefault getEntityDefaultByPrincipalId(String principalId);

	EntityDefault getEntityDefaultByPrincipalName(String principalName);

	List<String> lookupEntityIds(Map<String,String> searchCriteria);

	EntityPrivacyPreferences getEntityPrivacyPreferences(String entityId);
	
    Map<String, EntityNamePrincipalName> getDefaultNamesForPrincipalIds(List<String> principalIds);

    Map<String, EntityNamePrincipalName> getDefaultNamesForEntityIds(List<String> entityIds);

    Map<String, ContextMapper> getContextMappers();
    
    Constants getKimConstants();
}
