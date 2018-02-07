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
package org.kuali.rice.kim.ldap;

import static org.apache.commons.lang.StringUtils.contains;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.kuali.rice.core.util.BufferedLogger.debug;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.springframework.ldap.core.DirContextOperations;

/**
 * Maps LDAP Information to KIM Entity Affiliation
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityAffiliationMapper extends BaseMapper<List<EntityAffiliation>> {

	@Override
    List<EntityAffiliation> mapDtoFromContext(DirContextOperations context) {
    	List<EntityAffiliation.Builder> builders = mapBuilderFromContext(context);
    	List<EntityAffiliation> affiliations = new ArrayList<EntityAffiliation>();
    	if (builders != null) {
	    	for (EntityAffiliation.Builder builder : builders) {
	    		affiliations.add(builder.build());
	    	}
    	}
    	return affiliations;
    }
    
    List<EntityAffiliation.Builder> mapBuilderFromContext(DirContextOperations context) {
        List<EntityAffiliation.Builder> retval = new ArrayList<EntityAffiliation.Builder>();
        final String primaryAffiliationProperty = getConstants().getPrimaryAffiliationLdapProperty();
        final String affiliationProperty = getConstants().getAffiliationLdapProperty();
        debug("Got affiliation ", context.getStringAttribute(primaryAffiliationProperty));
        debug("Got affiliation ", context.getStringAttribute(affiliationProperty));
        
        String primaryAffiliation = context.getStringAttribute(primaryAffiliationProperty);
        
        int affiliationId = 1;
        String affiliationCode = getAffiliationTypeCodeForName(primaryAffiliation);

        final EntityAffiliation.Builder aff1 = EntityAffiliation.Builder.create();
        aff1.setAffiliationType(EntityAffiliationType.Builder.create(affiliationCode == null ? "AFLT" : affiliationCode));
        aff1.setCampusCode(getConstants().getDefaultCampusCode());
        aff1.setId("" + affiliationId++);
        aff1.setDefaultValue(true);
        aff1.setActive(true);
        retval.add(aff1);
        
        String[] affiliations = context.getStringAttributes(affiliationProperty);
        // Create an empty array to prevent NPE
        if (affiliations == null) {
            affiliations = new String[] {};
        }

        for (String affiliation : affiliations) {
            if (!StringUtils.equals(affiliation, primaryAffiliation)) {
                affiliationCode = getAffiliationTypeCodeForName(affiliation);
                if (affiliationCode != null && !hasAffiliation(retval, affiliationCode)) {
                    final EntityAffiliation.Builder aff = EntityAffiliation.Builder.create();
                    aff.setAffiliationType(EntityAffiliationType.Builder.create(affiliationCode));
                    aff.setCampusCode(getConstants().getDefaultCampusCode());
                    aff.setId("" + affiliationId++);
                    aff.setDefaultValue(false);
                    aff.setActive(true);
                    retval.add(aff);
                }
            }
        }
        
        return retval;
    }
    
    /**
     *
     * Returns the affiliation type code for the given affiliation name. Returns null if the affiliation is not found
     * @param affiliationName
     * @return null if no matching affiliation is found
     */
    protected String getAffiliationTypeCodeForName(String affiliationName) {
        String[] mappings = getConstants().getAffiliationMappings().split(",");
        for (String affilMap : mappings) {
            if (contains(affilMap, affiliationName)) {
                return affilMap.split("=")[1];
            }
        }
        return null;
    }

    protected boolean hasAffiliation(List<EntityAffiliation.Builder> affiliations, String affiliationCode) {
        for (EntityAffiliation.Builder affiliation : affiliations) {
            if (equalsIgnoreCase(affiliation.getAffiliationType().getCode(), affiliationCode)) {
                return true;
            }
        }
        return false;
    }

}
