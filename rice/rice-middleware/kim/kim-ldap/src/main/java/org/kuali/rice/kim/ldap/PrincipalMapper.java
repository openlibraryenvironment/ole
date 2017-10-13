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

import static java.util.Arrays.asList;
import static org.kuali.rice.core.util.BufferedLogger.debug;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 */
public class PrincipalMapper extends BaseMapper<Principal> {
    private ParameterService parameterService;
    
    @Override
    Principal mapDtoFromContext(DirContextOperations context) {
    	Principal.Builder builder = mapBuilderFromContext(context);
    	return builder != null ? builder.build() : null;
    }

    Principal.Builder mapBuilderFromContext(DirContextOperations context) {
        final String entityId      = context.getStringAttribute(getConstants().getKimLdapIdProperty());
        final String principalName = context.getStringAttribute(getConstants().getKimLdapNameProperty());
        final Principal.Builder person = Principal.Builder.create(principalName);
        
        if (entityId == null) {
            throw new InvalidLdapEntityException("LDAP Search Results yielded an invalid result with attributes " 
                                                 + context.getAttributes());
        }
        
        person.setPrincipalId(entityId);
        person.setEntityId(entityId);
        person.setActive(isPersonActive(context));

        return person;
    }
    
     /**
     * 
     * Checks the configured active principal affiliations, if one is found, returns true
     * @param context
     * @return true if a matching active affiliation is found
     */
    protected boolean isPersonActive(DirContextOperations context) {
        String[] affils = context.getStringAttributes(getConstants().getAffiliationLdapProperty());
        Object edsVal = getLdapValue("principals.active.Y");
        if (affils != null && affils.length > 0
                && edsVal != null) {
            if (edsVal instanceof List) {
                List<String> edsValLst = (List<String>)edsVal;
                for (String affil : affils) {
                    if (edsValLst.contains(affil)) {
                        return true;
                    }
                }
            } else {
                String edsValStr = (String)edsVal;
                for (String affil : affils) {
                    if (StringUtils.equals(affil, edsValStr)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected Object getLdapValue(String kimAttribute) {
        Matcher matcher = getKimAttributeMatcher(kimAttribute);
        debug("Does ", kimAttribute, " match? ", matcher.matches());
        if (!matcher.matches()) {
            return null;
        }
        String value = matcher.group(2);

        // If it's actually a list. It can only be a list if there are commas
        if (value.contains(",")) {
            return asList(value.split(","));
        }

        return value;
    }

    protected Matcher getKimAttributeMatcher(String kimAttribute) {
        String mappedParamValue = getParameterService().getParameterValueAsString(getConstants().getParameterNamespaceCode(),
                                                                        getConstants().getParameterDetailTypeCode(),
                                                                        getConstants().getMappedParameterName());

        String regexStr = String.format("(%s|.*;%s)=([^=;]*).*", kimAttribute, kimAttribute);
        debug("Matching KIM attribute with regex ", regexStr);
        Matcher retval = Pattern.compile(regexStr).matcher(mappedParamValue);
        
        if (!retval.matches()) {
            mappedParamValue = getParameterService().getParameterValueAsString(getConstants().getParameterNamespaceCode(),
                                                                  getConstants().getParameterDetailTypeCode(),
                                                                  getConstants().getMappedValuesName());
            retval = Pattern.compile(regexStr).matcher(mappedParamValue);
        }

        return retval;
    }


    public ParameterService getParameterService() {
        return this.parameterService;
    }

    public void setParameterService(ParameterService service) {
        this.parameterService = service;
    }
}