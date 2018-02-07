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
package org.kuali.rice.kim.api.identity.type;


import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kim.api.identity.CodedAttributeContract;
import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;

import java.util.List;
/**
 * This is a contract for EntityTypeContactInfo.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public interface EntityTypeContactInfoContract extends Versioned, GloballyUnique, Inactivatable {
    /**
     * Gets the id of the parent identity object.
     * @return the identity id for this {@link EntityAddressContract}
     */
    String getEntityId();

    /**
     * Gets this entityTypeCode of the {@link EntityAddressContract}'s object.
     * @return the identity type code for this {@link EntityAddressContract}
     */
    String getEntityTypeCode();

    /**
     * Gets this identity Type of the {@link EntityTypeContactInfoContract}'s object.
     * @return the identity type for this {@link EntityTypeContactInfoContract}
     */
	CodedAttributeContract getEntityType();

	/**
     * Gets this {@link EntityTypeContactInfoContract}'s List of {@link org.kuali.rice.kim.api.identity.address.EntityAddress}S.
     * @return the List of {@link org.kuali.rice.kim.api.identity.address.EntityAddressContract}S for this {@link EntityTypeContactInfoContract}.
     * The returned List will never be null, an empty List will be assigned and returned if needed. 
     */
	List<? extends EntityAddressContract> getAddresses();

	/**
     * Gets this {@link EntityTypeContactInfoContract}'s List of {@link org.kuali.rice.kim.api.identity.email.EntityEmailContract}S.
     * @return the List of {@link org.kuali.rice.kim.api.identity.email.EntityEmailContract}S for this {@link EntityTypeContactInfoContract}.
     * The returned List will never be null, an empty List will be assigned and returned if needed. 
     */
	List<? extends EntityEmailContract> getEmailAddresses();
	
	/**
     * Gets this {@link EntityTypeContactInfoContract}'s List of {@link org.kuali.rice.kim.api.identity.phone.EntityPhone}S.
     * @return the List of {@link org.kuali.rice.kim.api.identity.phone.EntityPhoneContract}S for this {@link EntityTypeContactInfoContract}.
     * The returned List will never be null, an empty List will be assigned and returned if needed. 
     */
	List<? extends EntityPhoneContract> getPhoneNumbers();
	
	/** 
	 * Returns the default address record for the identity.  If no default is defined, then
	 * it returns the first one found.  If none are defined, it returns null.
	 */
	EntityAddressContract getDefaultAddress();

	/**
	 *  Returns the default email record for the identity.  If no default is defined, then
	 * it returns the first one found.  If none are defined, it returns null.
	 */
	EntityEmailContract getDefaultEmailAddress();

	/** 
	 * Returns the default phone record for the identity.  If no default is defined, then
	 * it returns the first one found.  If none are defined, it returns null.
	 */
	EntityPhoneContract getDefaultPhoneNumber();
}
