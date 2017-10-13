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

import static org.kuali.rice.core.util.BufferedLogger.debug;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.util.Constants;
import org.springframework.ldap.core.DirContextOperations;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EntityTypeContactInfoMapper extends BaseMapper<EntityTypeContactInfo> {

    private EntityAddressMapper addressMapper;
    private EntityPhoneMapper phoneMapper;
    private EntityEmailMapper emailMapper;;

    @Override
    EntityTypeContactInfo mapDtoFromContext(DirContextOperations context) {
    	EntityTypeContactInfo.Builder builder = mapBuilderFromContext(context);
    	return builder != null ? builder.build() : null;
    }
    
    EntityTypeContactInfo.Builder mapBuilderFromContext(DirContextOperations context) {
        final String entityId       = (String) context.getStringAttribute(getConstants().getKimLdapIdProperty());
        final String entityTypeCode = (String ) getConstants().getPersonEntityTypeCode();

        final EntityTypeContactInfo.Builder builder = EntityTypeContactInfo.Builder.create(entityId, entityTypeCode); 
        final EntityAddress.Builder address = getAddressMapper().mapBuilderFromContext(context);
        final List<EntityAddress.Builder> addresses = new ArrayList<EntityAddress.Builder>();
        addresses.add(address);
        final List<EntityEmail.Builder> email = new ArrayList<EntityEmail.Builder>();
        email.add(getEmailMapper().mapBuilderFromContext(context));
        final List<EntityPhone.Builder> phone = new ArrayList<EntityPhone.Builder>();
        phone.add(getPhoneMapper().mapBuilderFromContext(context));
        builder.setAddresses(addresses);
        builder.setEmailAddresses(email);
        builder.setPhoneNumbers(phone);
        debug("Created Entity Type with code ", builder.getEntityTypeCode());                

        return builder;
    }

    /**
     * Gets the value of addressMapper
     *
     * @return the value of addressMapper
     */
    public final EntityAddressMapper getAddressMapper() {
        return this.addressMapper;
    }

    /**
     * Sets the value of addressMapper
     *
     * @param argAddressMapper Value to assign to this.addressMapper
     */
    public final void setAddressMapper(final EntityAddressMapper argAddressMapper) {
        this.addressMapper = argAddressMapper;
    }

    /**
     * Gets the value of phoneMapper
     *
     * @return the value of phoneMapper
     */
    public final EntityPhoneMapper getPhoneMapper() {
        return this.phoneMapper;
    }

    /**
     * Sets the value of phoneMapper
     *
     * @param argPhoneMapper Value to assign to this.phoneMapper
     */
    public final void setPhoneMapper(final EntityPhoneMapper argPhoneMapper) {
        this.phoneMapper = argPhoneMapper;
    }

    /**
     * Gets the value of emailMapper
     *
     * @return the value of emailMapper
     */
    public final EntityEmailMapper getEmailMapper() {
        return this.emailMapper;
    }

    /**
     * Sets the value of emailMapper
     *
     * @param argEmailMapper Value to assign to this.emailMapper
     */
    public final void setEmailMapper(final EntityEmailMapper argEmailMapper) {
        this.emailMapper = argEmailMapper;
    }
}